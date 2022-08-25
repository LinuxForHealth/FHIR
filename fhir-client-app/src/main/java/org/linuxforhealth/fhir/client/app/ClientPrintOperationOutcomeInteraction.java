/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.client.app;

import org.linuxforhealth.fhir.client.FHIRClient;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;

/**
 * Prints the current context resource value to standard out
 */
public class ClientPrintOperationOutcomeInteraction extends ClientInteraction {

    /**
     * Public constructor
     */
    public ClientPrintOperationOutcomeInteraction() {
    }

    @Override
    public void invoke(IClientInteractionContext context, FHIRClient client) throws Exception {
        OperationOutcome opo = context.getOperationOutcome();
        if (opo != null) {
            FHIRGenerator.generator(Format.JSON, context.isPretty()).generate(opo, System.out);
        }
    }
}