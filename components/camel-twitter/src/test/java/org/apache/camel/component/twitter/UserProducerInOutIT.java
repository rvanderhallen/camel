/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.twitter;

import java.util.Date;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.v1.Status;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests posting a twitter update and getting the status update id from the Twitter API response
 */
@EnabledIfSystemProperty(named = "enable.twitter.itests", matches = "true",
                         disabledReason = "Likely has API limits, so it's better to keep it off by default")
public class UserProducerInOutIT extends CamelTwitterITSupport {

    private static final Logger LOG = LoggerFactory.getLogger(UserProducerInOutIT.class);

    @EndpointInject("mock:result")
    protected MockEndpoint resultEndpoint;

    @Test
    public void testPostStatusUpdateRequestResponse() throws Exception {
        Date now = new Date();
        String tweet = "UserProducerInOutTest: This is a tweet posted on " + now.toString();
        LOG.info("Tweet: " + tweet);
        ProducerTemplate producerTemplate = context.createProducerTemplate();
        // send tweet to the twitter endpoint
        producerTemplate.sendBodyAndHeader("direct:tweets", tweet, "customHeader", 12312);

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodyReceived().body(Status.class);
        // Message headers should be preserved
        resultEndpoint.expectedHeaderReceived("customHeader", 12312);
        resultEndpoint.assertIsSatisfied();

        List<Exchange> tweets = resultEndpoint.getExchanges();
        assertNotNull(tweets);
        assertThat(tweets.size(), is(1));
        Status receivedTweet = tweets.get(0).getIn().getBody(Status.class);
        assertNotNull(receivedTweet);
        // The identifier for the published tweet should be there
        assertNotEquals(0, receivedTweet.getId());
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:tweets")
                        //.to("log:org.apache.camel.component.twitter?level=INFO&showAll=true&multiline=true")
                        .to(ExchangePattern.InOut, "twitter-timeline://user?" + getUriTokens())
                        //.to("log:org.apache.camel.component.twitter?level=INFO&showAll=true&multiline=true")
                        //.transform().simple("The tweet '${body.text}' was published with the id '${body.id}'")
                        //.to("log:org.apache.camel.component.twitter?level=INFO&showAll=true&multiline=true")
                        .to("mock:result");
            }
        };
    }
}
