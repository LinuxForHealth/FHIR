/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.examples;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRClient;
import org.linuxforhealth.fhir.examples.Index;
import org.linuxforhealth.fhir.model.spec.test.DriverMetrics;
import org.linuxforhealth.fhir.model.spec.test.R4ExamplesDriver;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;
import org.linuxforhealth.fhir.validation.test.ValidationProcessor;

/**
 * Basic sniff test of the FHIR Server.
 */
public class R4ExampleServerTest extends FHIRServerTestBase {

    // the tenant id to use for the FHIR server requests
    private String tenantId;

    /**
     * Process all the examples in the fhir-r4-spec example library
     */
    @Test(groups = { "server-examples" })
    public void processExamples() throws Exception {
        // Process each of the examples using the provided ExampleRequestProcessor. We want to
        // validate first before we try and send to FHIR
        final R4ExamplesDriver driver = new R4ExamplesDriver();

        // Setup a Pool
        ExecutorService es = Executors.newFixedThreadPool(5);
        driver.setPool(es, 5);

        DriverMetrics dm = new DriverMetrics();
        driver.setMetrics(dm);
        driver.setValidator(new ValidationProcessor());
        driver.setProcessor(new ExampleRequestProcessor(this, tenantId, dm, 1));

        String index = System.getProperty(this.getClass().getName()
            + ".index", Index.MINIMAL_JSON.name());
        driver.processIndex(Index.valueOf(index));
    }

    @Override
    public void setUp(Properties properties) throws Exception {
        super.setUp(properties);
        this.tenantId = properties.getProperty(FHIRClient.PROPNAME_TENANT_ID, "default");
    }
}
