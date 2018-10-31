/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package encyclopediaofphilosophy.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import iep.lucene.SearchResult;
import quote.GetQuote;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;

import static com.amazon.ask.request.Predicates.intentName;

public class EncyclopediaOfPhilosophyQuoteIntentHandler implements RequestHandler {
    private static final Logger logger = LogManager.getLogger(EncyclopediaOfPhilosophyQuoteIntentHandler.class);
    private GetQuote getQuote = new GetQuote();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("GetQuote"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
    	String speechText;
		SearchResult searchResult = null ;
		try {
			searchResult = getQuote.getRandomQuote();
		} catch (IOException | ParseException e) {
			logger.error(e);
		}
		if ( searchResult == null || searchResult.preamble.isEmpty()) {
			speechText = "There is a problem connecting to the Encyclopedia of Philosophy at this time."
					+ "Please try again later.";
			logger.error("There is a problem connecting to the Encyclopedia of Philosophy at this time.");
	        return input.getResponseBuilder()
	                .withSpeech(speechText)
	                .build();
		} else {
			speechText = "Random entry for "+searchResult.subject+". " + searchResult.preamble;
			logger.info("Random entry for "+searchResult.subject + "=" + searchResult.url);
		}

        return input.getResponseBuilder()
                .withSpeech(speechText + "<p>You can ask for another quote or do a search.</p>")
                .withSimpleCard("Entry for " + searchResult.subject,  "https://www.iep.utm.edu/" + searchResult.url + "\n" + speechText)
                .withReprompt("You can search for an entry, ask for a quote, or stop.")
                .withShouldEndSession(false)
                .build();
    }
}
