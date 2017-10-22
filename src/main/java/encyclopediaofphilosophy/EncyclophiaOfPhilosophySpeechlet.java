/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package encyclopediaofphilosophy;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;

import quote.GetQuote;
import iep.lucene.SearchFiles;
import iep.lucene.SearchResult;

import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class EncyclophiaOfPhilosophySpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(EncyclophiaOfPhilosophySpeechlet.class);
	
	/**
	 * URL prefix to download random quote from Encyclopedia of Philosophy.
	 */
    private static final String SLOT_SEARCH_PHRASE = "SearchPhrase";
    private SearchFiles searchFiles = new SearchFiles();
    private GetQuote getQuote = new GetQuote();

	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
	    return getWelcomeResponse();
	}

	@Override
	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = intent.getName();

		if ("GetQuote".equals(intentName)) {
			SearchResult searchResult;
			try {
				searchResult = getQuote.getRandomQuote();
			} catch (ParseException | IOException e) {
				throw new SpeechletException(e);
			}
			if ( searchResult.preamble.isEmpty()) {
				log.info("Intent = GetQuote: *** fails *** ");
				SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
				outputSpeech.setSsml("<speak>There is a problem connecting to the Encyclopedia of Philosophy at this time."
						+ " Please try again later.</speak>");
				return SpeechletResponse.newTellResponse(outputSpeech);
			}
			log.info("Intent = GetQuote: " + searchResult.subject);
			SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
			outputSpeech.setSsml("<speak>Random entry for "+searchResult.subject+"<break strength=\"x-strong\"/>" + searchResult.preamble + "</speak>");
			SimpleCard card = new SimpleCard();
			card.setTitle("Encyclopedia of Philosophy");
			card.setContent(searchResult.subject+"\n"+searchResult.url+"\n"+searchResult.preamble);
			SpeechletResponse speechletResponse = SpeechletResponse.newTellResponse(outputSpeech);
			speechletResponse.setCard(card);
			return speechletResponse;
		} else if ("SearchIntent".equals(intentName)) {
	        // add a player to the current game,
	        // terminate or continue the conversation based on whether the intent
	        // is from a one shot command or not.
	        String searchPhrase = intent.getSlot(SLOT_SEARCH_PHRASE).getValue();
	        if (searchPhrase == null || searchPhrase.isEmpty()) {
				SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
				outputSpeech.setSsml("<speak>Sorry, I didn't understand that word or phrase.</speak>");
				log.info("Didn't understand the search phrase: ");
				return SpeechletResponse.newTellResponse(outputSpeech);
	        }

			try {
				List<SearchResult> searchResults = searchFiles.query(searchPhrase);
				if ( searchResults.size() > 0 ) {
					// Create the plain text output
					SearchResult searchResult = searchResults.get(0);
					
					SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
					outputSpeech.setSsml("<speak>Results for \"" + searchPhrase +"\".<break strength=\"x-strong\"/>" + searchResults.get(0).preamble + "</speak>");
					log.info("Search found for " + searchPhrase + ". Result: " + searchResults.get(0).subject + " at " + searchResults.get(0).url);
					SimpleCard card = new SimpleCard();
					card.setTitle("Encyclopedia of Philosophy");
					card.setContent(searchResult.subject+searchResult.url+"\n"+searchResult.preamble);
					SpeechletResponse speechletResponse = SpeechletResponse.newTellResponse(outputSpeech);
					speechletResponse.setCard(card);
					return speechletResponse;
				} else {
					SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
					outputSpeech.setSsml("<speak>Sorry, nothing found for " + searchPhrase + ".</speak>");
					log.info("No results for " + searchPhrase);
					return SpeechletResponse.newTellResponse(outputSpeech);
				}
			} catch (ParseException | IOException e) {
				throw new SpeechletException(e);
			}
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			// Create the plain text output.
			String speechOutput = "With the Encyclopedia of Philosophy you can say get a quote or ask to search for a word or phrase.";
			String repromptText = "Would you like a quote?";

			return newAskResponse(speechOutput, false, repromptText, false);
		} else if ("AMAZON.StopIntent".equals(intentName)) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Goodbye");

			return SpeechletResponse.newTellResponse(outputSpeech);
		} else if ("AMAZON.CancelIntent".equals(intentName)) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Goodbye");

			return SpeechletResponse.newTellResponse(outputSpeech);
		} else {
			throw new SpeechletException("Invalid Intent");
		}
	}

	@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		// any session cleanup logic would go here
	}

	/**
	 * Function to handle the onLaunch skill behavior.
	 * 
	 * @return SpeechletResponse object with voice/card response to return to
	 *         the user
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechOutput = "With the Encyclopedia of Philosophy you can say get a quote or ask to search for a word or phrase.";
		String repromptText = "Would you like a quote?";

		return newAskResponse(speechOutput, false, repromptText, false);
	}

	/**
	 * Wrapper for creating the Ask response from the input strings.
	 * 
	 * @param stringOutput
	 *            the output to be spoken
	 * @param isOutputSsml
	 *            whether the output text is of type SSML
	 * @param repromptText
	 *            the reprompt for if the user doesn't reply or is
	 *            misunderstood.
	 * @param isRepromptSsml
	 *            whether the reprompt text is of type SSML
	 * @return SpeechletResponse the speechlet response
	 */
	private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml, String repromptText,
			boolean isRepromptSsml) {
		OutputSpeech outputSpeech, repromptOutputSpeech;
		if (isOutputSsml) {
			outputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
		} else {
			outputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
		}

		if (isRepromptSsml) {
			repromptOutputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
		} else {
			repromptOutputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
		}
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptOutputSpeech);
		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}



}
