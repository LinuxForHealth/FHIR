/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.reindex;

import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * Custom operation to invoke the persistence layer reindexing of resources
 */
public class ReindexOperation extends AbstractOperation {
    private static final Logger logger = Logger.getLogger(ReindexOperation.class.getName());

    private static final String PARAM_TSTAMP = "tstamp";
    private static final String PARAM_INDEX_IDS = "indexIds";
    private static final String PARAM_RESOURCE_COUNT = "resourceCount";
    private static final String PARAM_RESOURCE_LOGICAL_ID = "resourceLogicalId";

    // The max number of resources we allow to be processed by one request
    private static final int MAX_RESOURCE_COUNT = 1000;

    static final DateTimeFormatter DAY_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.of("UTC"));

    public ReindexOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("reindex.json")) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected boolean isAbstractResourceTypesDisallowed() {
        return true;
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
            throws FHIROperationException {

        try {
            Instant tstamp = Instant.now();
            List<Long> indexIds = null;
            int resourceCount = 10;
            String resourceLogicalId = null;

            boolean hasSpecificResourceType = false;
            if (resourceType != null) {
                resourceLogicalId = resourceType.getSimpleName();
                if (logicalId != null) {
                    resourceLogicalId +=  "/" + logicalId;
                }
                hasSpecificResourceType = true;
            }

            if (parameters != null) {
                for (Parameters.Parameter parameter : parameters.getParameter()) {
                    if (parameter.getValue() != null && logger.isLoggable(Level.FINE)) {
                        logger.fine("reindex param: " + parameter.getName().getValue() + " = " + parameter.getValue().toString());
                    }

                    if (PARAM_TSTAMP.equals(parameter.getName().getValue())
                            && parameter.getValue() != null
                            && parameter.getValue().is(com.ibm.fhir.model.type.String.class)) {
                        String val = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();

                        if (val.length() == 10) {
                            tstamp = DAY_FORMAT.parse(val, Instant::from);
                        } else {
                            // assume full ISO format
                            tstamp = Instant.parse(val);
                        }
                    } else if (PARAM_INDEX_IDS.equals(parameter.getName().getValue())) {
                        // reindex a specific list of resources by index ID (comma-delimited), which is different than resource logical ID
                        String lrIdsString = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                        if (lrIdsString != null) {
                            Set<Long> lrIdSet = new LinkedHashSet<>();
                            String[] lrIdArray = lrIdsString.split("\\s*,\\s*");
                            if (lrIdArray.length == 0) {
                                lrIdSet.add(Long.valueOf(lrIdsString));
                            }
                            for (String lrIdString : lrIdArray) {
                                lrIdSet.add(Long.valueOf(lrIdString));
                            }
                            indexIds = new ArrayList<>(lrIdSet);
                            if (indexIds.size() > MAX_RESOURCE_COUNT) {
                                throw FHIROperationUtil.buildExceptionWithIssue("The specified number of index IDs exceeds the maximum allowed number of resources to reindex", IssueType.INVALID);
                            }
                        }
                    } else if (PARAM_RESOURCE_COUNT.equals(parameter.getName().getValue())) {
                        Integer val = parameter.getValue().as(com.ibm.fhir.model.type.Integer.class).getValue();
                        if (val != null) {
                            if (val > MAX_RESOURCE_COUNT) {
                                logger.info("Clamping resourceCount '" + val + "' to max allowed: " + MAX_RESOURCE_COUNT);
                                val = MAX_RESOURCE_COUNT;
                            }
                            resourceCount = val;
                        }
                    } else if (PARAM_RESOURCE_LOGICAL_ID.equals(parameter.getName().getValue())) {
                        if (hasSpecificResourceType) {
                            throw FHIROperationUtil.buildExceptionWithIssue("resourceLogicalId already specified using call to Operation on Type or Instance", IssueType.INVALID);
                        }
                        // reindex a specific resource or resourceType
                        resourceLogicalId = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                        String rt = resourceLogicalId;
                        if (resourceLogicalId.contains("/")) {
                            String[] parts = resourceLogicalId.split("/");
                            rt = parts[0];
                        }
                        // Check resource type
                        if (!ModelSupport.isConcreteResourceType(rt)) {
                            throw FHIROperationUtil.buildExceptionWithIssue("Resource type '" + rt + "' is not valid", IssueType.INVALID);
                        }
                    }
                }
            }

            // Delegate the heavy lifting to the helper
            OperationOutcome.Builder result = OperationOutcome.builder();
            int totalProcessed = 0;
            if (indexIds != null) {
                // All resources in one transaction
                totalProcessed = resourceHelper.doReindex(operationContext, result, tstamp, indexIds, null);
            } else {
                int processed = 1;
                // One resource per transaction
                for (int i=0; i<resourceCount && processed > 0; i++) {
                    processed = resourceHelper.doReindex(operationContext, result, tstamp, null, resourceLogicalId);
                    totalProcessed += processed;
                }
            }

            if (totalProcessed == 0) {
                // must have at least one issue for a valid OperationOutcome resource
                final String diag = "Reindex complete";
                result.issue(Issue.builder()
                    .code(IssueType.INFORMATIONAL)
                    .severity(IssueSeverity.INFORMATION)
                    .diagnostics(com.ibm.fhir.model.type.String.of(diag))
                    .build());
            }

            OperationOutcome operationOutcome = result.build();
            checkOperationOutcome(operationOutcome);
            return FHIROperationUtil.getOutputParameters(operationOutcome);
        } catch (java.time.format.DateTimeParseException dtpe) {
            throw FHIROperationUtil.buildExceptionWithIssue("Invalid format for 'tstamp' value, only 'yyyy-MM-dd' or ISO 8601 dateTime format is valid", IssueType.INVALID);
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
