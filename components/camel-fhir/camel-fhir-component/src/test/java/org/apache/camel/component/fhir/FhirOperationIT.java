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
package org.apache.camel.component.fhir;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.fhir.internal.FhirApiCollection;
import org.apache.camel.component.fhir.internal.FhirOperationApiMethod;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for {@link org.apache.camel.component.fhir.api.FhirOperation} APIs.
 */
@DisabledIfSystemProperty(named = "ci.env.name", matches = "apache.org",
                          disabledReason = "Apache CI nodes are too resource constrained for this test - see CAMEL-19659")
public class FhirOperationIT extends AbstractFhirTestSupport {

    private static final Logger LOG = LoggerFactory.getLogger(FhirOperationIT.class);
    private static final String PATH_PREFIX
            = FhirApiCollection.getCollection().getApiName(FhirOperationApiMethod.class).getName();

    @Test
    public void testOnInstance() {
        final Map<String, Object> headers = new HashMap<>();
        // parameter type is org.hl7.fhir.instance.model.api.IIdType
        headers.put("CamelFhir.id", this.patient.getIdElement());
        // parameter type is String
        headers.put("CamelFhir.name", "everything");
        // parameter type is org.hl7.fhir.instance.model.api.IBaseParameters
        headers.put("CamelFhir.parameters", null);
        // parameter type is Class
        headers.put("CamelFhir.outputParameterType", Parameters.class);
        headers.put("CamelFhir.useHttpGet", Boolean.FALSE);
        // parameter type is Class
        headers.put("CamelFhir.returnType", null);
        // parameter type is java.util.Map
        headers.put("CamelFhir.extraParameters", null);

        final Parameters result = requestBodyAndHeaders("direct://ON_INSTANCE", null, headers);

        LOG.debug("onInstance: " + result);
        assertNotNull(result, "onInstance result");
        Bundle bundle = (Bundle) result.getParameter().get(0).getResource();
        assertNotNull(bundle, "onInstance result");
        IdType id = bundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless();
        assertEquals(patient.getIdElement().toUnqualifiedVersionless(), id);
    }

    @Test
    public void testOnInstanceVersion() {
        final Map<String, Object> headers = new HashMap<>();
        // parameter type is org.hl7.fhir.instance.model.api.IIdType
        headers.put("CamelFhir.id", this.patient.getIdElement());
        // parameter type is String
        headers.put("CamelFhir.name", "everything");
        // parameter type is org.hl7.fhir.instance.model.api.IBaseParameters
        headers.put("CamelFhir.parameters", null);
        // parameter type is Class
        headers.put("CamelFhir.outputParameterType", Parameters.class);
        headers.put("CamelFhir.useHttpGet", Boolean.FALSE);
        // parameter type is Class
        headers.put("CamelFhir.returnType", null);
        // parameter type is java.util.Map
        headers.put("CamelFhir.extraParameters", null);

        final Parameters result = requestBodyAndHeaders("direct://ON_INSTANCE_VERSION", null, headers);

        LOG.debug("onInstance: " + result);
        assertNotNull(result, "onInstance result");
        Bundle bundle = (Bundle) result.getParameter().get(0).getResource();
        assertNotNull(bundle, "onInstance result");
        IdType id = bundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless();
        assertEquals(patient.getIdElement().toUnqualifiedVersionless(), id);
    }

    @Test
    public void testOnServer() {
        final Map<String, Object> headers = new HashMap<>();
        // parameter type is String
        headers.put("CamelFhir.name", "$get-resource-counts");
        // parameter type is org.hl7.fhir.instance.model.api.IBaseParameters
        headers.put("CamelFhir.parameters", null);
        // parameter type is Class
        headers.put("CamelFhir.outputParameterType", Parameters.class);
        headers.put("CamelFhir.useHttpGet", Boolean.TRUE);
        // parameter type is Class
        headers.put("CamelFhir.returnType", null);
        // parameter type is java.util.Map
        headers.put("CamelFhir.extraParameters", null);

        final Parameters result = requestBodyAndHeaders("direct://ON_SERVER", null, headers);
        assertNotNull(result, "onServer result");
    }

    @Test
    public void testOnType() {
        final Map<String, Object> headers = new HashMap<>();
        // parameter type is Class
        headers.put("CamelFhir.resourceType", Patient.class);
        // parameter type is String
        headers.put("CamelFhir.name", "everything");
        // parameter type is org.hl7.fhir.instance.model.api.IBaseParameters
        headers.put("CamelFhir.parameters", null);
        // parameter type is Class
        headers.put("CamelFhir.outputParameterType", Parameters.class);
        headers.put("CamelFhir.useHttpGet", Boolean.FALSE);
        // parameter type is Class
        headers.put("CamelFhir.returnType", null);
        // parameter type is java.util.Map
        headers.put("CamelFhir.extraParameters", null);

        final org.hl7.fhir.instance.model.api.IBaseResource result = requestBodyAndHeaders("direct://ON_TYPE", null, headers);

        assertNotNull(result, "onType result");
        LOG.debug("onType: " + result);
    }

    @Disabled("Not implemented yet in HAPI FHIR server side, see"
              + " https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-jpaserver-base/src/main/java/ca/uhn/fhir/jpa/dao/dstu3/FhirResourceDaoMessageHeaderDstu3.java#L33")
    @Test
    public void testProcessMessage() {
        final Map<String, Object> headers = new HashMap<>();
        // parameter type is String
        headers.put("CamelFhir.respondToUri", null);
        // parameter type is org.hl7.fhir.instance.model.api.IBaseBundle
        headers.put("CamelFhir.msgBundle", null);
        headers.put("CamelFhir.asynchronous", Boolean.FALSE);
        // parameter type is Class
        headers.put("CamelFhir.responseClass", null);
        // parameter type is java.util.Map
        headers.put("CamelFhir.extraParameters", null);

        final org.hl7.fhir.instance.model.api.IBaseBundle result
                = requestBodyAndHeaders("direct://PROCESS_MESSAGE", null, headers);

        assertNotNull(result, "processMessage result");
        LOG.debug("processMessage: " + result);
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                // test route for onInstance
                from("direct://ON_INSTANCE")
                        .to("fhir://" + PATH_PREFIX + "/onInstance");

                // test route for onInstanceVersion
                from("direct://ON_INSTANCE_VERSION")
                        .to("fhir://" + PATH_PREFIX + "/onInstanceVersion");

                // test route for onServer
                from("direct://ON_SERVER")
                        .to("fhir://" + PATH_PREFIX + "/onServer");

                // test route for onType
                from("direct://ON_TYPE")
                        .to("fhir://" + PATH_PREFIX + "/onType");

                // test route for processMessage
                from("direct://PROCESS_MESSAGE")
                        .to("fhir://" + PATH_PREFIX + "/processMessage");

            }
        };
    }
}
