/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.changes;

import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.server.operation.spi.AbstractOperation;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIROperationUtil;

/**
 * Custom operation to invoke the persistence layer reindexing of resources
 */
public class ChangesOperation extends AbstractOperation {
    private static final Logger logger = Logger.getLogger(ChangesOperation.class.getName());

    private static final String PARAM_TSTAMP = "tstamp";
    private static final String PARAM_RESOURCE_COUNT = "resourceCount";
    private static final String PARAM_RESOURCE_ID = "resourceId";
    private static final String PARAM_RESOURCE_TYPE = "resourceType";

    // The max number of resources we allow to be processed by one request
    private static final int MAX_RESOURCE_COUNT = 1000;

    static final DateTimeFormatter DAY_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.of("UTC"));

    public ChangesOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("changes.json")) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
            throws FHIROperationException {

        // POST because we want to receive parameters
        String method = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_METHOD_TYPE);
        if (!"POST".equalsIgnoreCase(method)) {
            throw new FHIROperationException("HTTP method not supported: " + method);
        }

        try {
            Instant fromTstamp = null;
            int resourceCount = 10;
            Long afterResourceId = null;
            String resourceTypeName = null;

            if (parameters != null) {
                for (Parameters.Parameter parameter : parameters.getParameter()) {
                    if (parameter.getValue() != null && logger.isLoggable(Level.FINE)) {
                        logger.fine("changes param: " + parameter.getName().getValue() + " = " + parameter.getValue().toString());
                    }

                    if (PARAM_TSTAMP.equals(parameter.getName().getValue())
                            && parameter.getValue() != null
                            && parameter.getValue().is(com.ibm.fhir.model.type.String.class)) {
                        String val = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();

                        if (val.length() == 10) {
                            fromTstamp = DAY_FORMAT.parse(val, Instant::from);
                        } else {
                            // assume full ISO format
                            fromTstamp = Instant.parse(val);
                        }
                    } else if (PARAM_RESOURCE_COUNT.equals(parameter.getName().getValue())) {
                        Integer val = parameter.getValue().as(com.ibm.fhir.model.type.Integer.class).getValue();
                        if (val != null) {
                            if (val > MAX_RESOURCE_COUNT) {
                                logger.info("Clamping resourceCount " + val + " to max allowed: " + MAX_RESOURCE_COUNT);
                                val = MAX_RESOURCE_COUNT;
                            }
                            resourceCount = val;
                        }
                    } else if (PARAM_RESOURCE_ID.equals(parameter.getName().getValue())) {
                        // Starting value for resourceId if given. Provided as a String because FHIR model
                        // does not support Long.
                        String resourceIdStr = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                        if (resourceIdStr != null) {
                            afterResourceId = Long.parseLong(resourceIdStr);
                        }
                    } else if (PARAM_RESOURCE_TYPE.equals(parameter.getName().getValue())) {
                        // Starting value for resourceId if given. Provided as a String because FHIR model
                        // does not support Long.
                        resourceTypeName = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                    }
                }
            }

            // Delegate the heavy lifting to the helper
            FHIRRestOperationResponse response = resourceHelper.doChanges(operationContext, resourceCount, fromTstamp, afterResourceId, resourceTypeName);
            if (response.getOperationOutcome() != null) {
                checkOperationOutcome(response.getOperationOutcome());
                return FHIROperationUtil.getOutputParameters(response.getOperationOutcome());
            } else {
                return FHIROperationUtil.getOutputParameters(response.getResource());
            }

        } catch (FHIROperationException e) {
            throw e;
        } catch (Throwable t) {
            throw new FHIROperationException("Unexpected error occurred while processing request for operation '"
                    + getName() + "': " + getCausedByMessage(t), t);
        }
    }

    /**
     * Check the OperationOutcome for any errors
     * @param oo
     * @throws FHIROperationException
     */
    private void checkOperationOutcome(OperationOutcome oo) throws FHIROperationException {
        List<Issue> issues = oo.getIssue();
        for (Issue issue : issues) {
            IssueSeverity severity = issue.getSeverity();
            if (severity != null && (IssueSeverity.ERROR.getValue().equals(severity.getValue())
                    || IssueSeverity.FATAL.getValue().equals(severity.getValue()))) {
                throw new FHIROperationException("The persistence layer reported one or more issues").withIssue(issues);
            }
        }
    }

    private String getCausedByMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }
}
