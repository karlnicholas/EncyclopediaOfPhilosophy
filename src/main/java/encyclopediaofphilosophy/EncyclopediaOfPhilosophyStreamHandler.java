/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package encyclopediaofphilosophy;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import encyclopediaofphilosophy.handlers.CancelandStopIntentHandler;
import encyclopediaofphilosophy.handlers.FallbackIntentHandler;
import encyclopediaofphilosophy.handlers.HelpIntentHandler;
import encyclopediaofphilosophy.handlers.LaunchRequestHandler;
import encyclopediaofphilosophy.handlers.EncyclopediaOfPhilosophySearchIntentHandler;
import encyclopediaofphilosophy.handlers.SessionEndedRequestHandler;
import encyclopediaofphilosophy.handlers.EncyclopediaOfPhilosophyQuoteIntentHandler;

public class EncyclopediaOfPhilosophyStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new EncyclopediaOfPhilosophyQuoteIntentHandler(),
                        new EncyclopediaOfPhilosophySearchIntentHandler(),
                        new LaunchRequestHandler(),
                        new CancelandStopIntentHandler(),
                        new SessionEndedRequestHandler(),
                        new HelpIntentHandler(),
                        new FallbackIntentHandler())
                // Add your skill id below
                .withSkillId("amzn1.ask.skill.b73dddb0-1092-421d-bfdd-04f9e61eff9a")
                .build();
    }

    public EncyclopediaOfPhilosophyStreamHandler() {
        super(getSkill());
    }

}
