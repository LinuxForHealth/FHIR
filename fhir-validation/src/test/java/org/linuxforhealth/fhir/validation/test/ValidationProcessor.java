/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.spec.test.IExampleProcessor;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.validation.FHIRValidator;

/**
 * Strategy to process resources using the {@link FHIRValidator}
 */
public class ValidationProcessor implements IExampleProcessor {
    private static final Logger logger = Logger.getLogger(ValidationProcessor.class.getName());

    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        List<OperationOutcome.Issue> issues = FHIRValidator.validator().validate(resource);
        if (!issues.isEmpty()) {
            List<String> issueStrings = new ArrayList<String>();
            for (Issue issue : issues) {
                if (FHIRUtil.isFailure(issue.getSeverity())) {
                    String details = "<missing details>";
                    if (issue.getDetails() != null && issue.getDetails().getText() != null) {
                        details = issue.getDetails().getText().getValue();
                    }
                    String locations = issue.getExpression().stream()
                        .flatMap(loc -> Stream.of(loc.getValue()))
                        .collect(Collectors.joining(","));
                    issueStrings.add(details + " (" + locations + ")");
                }
            }

            // Only errors or worse should result in a failure.
            boolean includesFailure = false;
            for (OperationOutcome.Issue issue: issues) {
                if (FHIRUtil.isFailure(issue.getSeverity())) {
                    logger.fine(issue.toString());
                    includesFailure = true;
                } else {
                    logger.finest(issue.toString());
                }
            }

            if (includesFailure) {
                // we want the test to fail
                throw new Exception("Input resource failed validation: \n\t" + String.join("\n\t", issueStrings));
            }
            else {
                logger.fine("Validation issues on '" + jsonFile + "' [INFO]: \n\t" + String.join("\n\t", issueStrings));
            }
        }
    }

}
