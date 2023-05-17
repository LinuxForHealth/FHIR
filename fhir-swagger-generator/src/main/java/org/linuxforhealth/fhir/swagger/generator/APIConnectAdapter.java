/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.swagger.generator;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObjectBuilder;

/**
 *
 */
public class APIConnectAdapter {

    /**
     * Set this to the hostname of your FHIR Server and the generated assembly will proxy this
     */
    private static final String HOST = "localhost";

    private static final JsonBuilderFactory factory = Json.createBuilderFactory(null);

    public static void addApiConnectStuff(JsonObjectBuilder swagger) {
        JsonObjectBuilder ibmConfig = factory.createObjectBuilder()
                                        .add("gateway", "datapower-api-gateway")
                                        .add("type", "rest")
                                        .add("phase", "realized")
                                        .add("enforced", true)
                                        .add("testable", true)
                                        .add("cors", factory.createObjectBuilder().add("enabled", true))
                                        .add("assembly", buildAssembly())
                                        .add("properties", buildProperties())
                                        .add("activity-log", factory.createObjectBuilder()
                                                               .add("enabled", true)
                                                               .add("error-content", "header")
                                                               .add("success-content", "activity"))
                                        .add("application-authentication", factory.createObjectBuilder()
                                                               .add("certificate", false))
                                        .add("catalogs", factory.createObjectBuilder());
        swagger.add("x-ibm-configuration", ibmConfig);
    }

    private static JsonObjectBuilder buildAssembly() {
        JsonObjectBuilder executeInvoke = factory.createObjectBuilder().add("invoke", buildExecuteInvoke());

        JsonArrayBuilder execute = factory.createArrayBuilder().add(executeInvoke);

        return factory.createObjectBuilder()
                .add("execute", execute)
                .add("catch", factory.createArrayBuilder());
    }

    private static JsonObjectBuilder buildExecuteInvoke() {
        JsonObjectBuilder headerControl = factory.createObjectBuilder()
                                            .add("type", "blacklist")
                                            .add("values", factory.createArrayBuilder());
        JsonObjectBuilder paramControl = factory.createObjectBuilder()
                                            .add("type", "whitelist")
                                            .add("values", factory.createArrayBuilder());
        return factory.createObjectBuilder()
                                    .add("version", "2.0.0")
                                    .add("title", "invoke")
                                    .add("header-control", headerControl)
                                    .add("parameter-control",paramControl)
                                    .add("timeout",  60)
                                    .add("verb", "keep")
                                    .add("cache-response", "protocol")
                                    .add("cache-ttl", 900)
                                    .add("stop-on-error", factory.createArrayBuilder())
                                    .add("target-url", "$(target-url)$(api.operation.path)$(request.search)");
    }

    private static JsonObjectBuilder buildProperties() {
        JsonObjectBuilder targetUrl = factory.createObjectBuilder()
                                            .add("value", "https://" + HOST + "/fhir-server/api/v4/")
                                            .add("description", "The URL of the target service")
                                            .add("encoded", false);

        return factory.createObjectBuilder().add("target-url", targetUrl);
    }
}
