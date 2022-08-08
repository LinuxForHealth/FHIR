/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.reindex;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.AbstractOperation;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * Custom operation to invoke the persistence layer to retrieve a list of index IDs.
 */
public class RetrieveIndexOperation extends AbstractOperation {
    private static final Logger logger = Logger.getLogger(RetrieveIndexOperation.class.getName());

    private static final String PARAM_COUNT = "_count";
    private static final String PARAM_AFTER_INDEX_ID = "afterIndexId";
    private static final String PARAM_NOT_MODIFIED_AFTER = "notModifiedAfter";
    private static final String PARAM_INDEX_IDS = "indexIds";

    // The max number of index IDs we allow to be retrieved by one request
    private static final int MAX_COUNT = 1000;

    static final DateTimeFormatter DAY_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.of("UTC"));

    public RetrieveIndexOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("retrieve-index.json")) {
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
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper)
            throws FHIROperationException {

        try {
            String indexIdsString = "";
            int count = MAX_COUNT;
            Long afterIndexId = null;
            Instant notModifiedAfter = Instant.now();
            String resourceTypeName = resourceType != null ? resourceType.getSimpleName() : null;

            if (parameters != null) {
                for (Parameters.Parameter parameter : parameters.getParameter()) {
                    if (parameter.getValue() != null && logger.isLoggable(Level.FINE)) {
                        logger.fine("retrieve-index param: " + parameter.getName().getValue() + " = " + parameter.getValue().toString());
                    }

                    if (PARAM_COUNT.equals(parameter.getName().getValue())) {
                        Integer val = parameter.getValue().as(org.linuxforhealth.fhir.model.type.Integer.class).getValue();
                        if (val != null) {
                            if (val > MAX_COUNT) {
                                logger.info("Clamping _count '" + val + "' to max allowed: " + MAX_COUNT);
                                val = MAX_COUNT;
                            }
                            count = val;
                        }
                    } else if (PARAM_NOT_MODIFIED_AFTER.equals(parameter.getName().getValue())) {
                        // Only retrieve index IDs for resources not last updated after the specified timestamp
                        String val = parameter.getValue().as(org.linuxforhealth.fhir.model.type.String.class).getValue();
                        if (val.length() == 10) {
                            notModifiedAfter = DAY_FORMAT.parse(val, Instant::from);
                        } else {
                            // assume full ISO format
                            notModifiedAfter = Instant.parse(val);
                        }
                    } else if (PARAM_AFTER_INDEX_ID.equals(parameter.getName().getValue())) {
                        // Start retrieving index IDs after this specified index ID
                        afterIndexId = Long.valueOf(parameter.getValue().as(org.linuxforhealth.fhir.model.type.String.class).getValue());
                    }
                }
            }

            // Get index IDs
            List<Long> indexIds = resourceHelper.doRetrieveIndex(operationContext, resourceTypeName, count, notModifiedAfter, afterIndexId);
            if (indexIds != null) {
                indexIdsString = indexIds.stream().map(l -> String.valueOf(l)).collect(Collectors.joining(","));
            }

            // Return output
            return FHIROperationUtil.getOutputParameters(PARAM_INDEX_IDS, !indexIdsString.isEmpty() ? string(indexIdsString) : null);

        } catch (FHIROperationException e) {
            throw e;
        } catch (Throwable t) {
            throw new FHIROperationException("Unexpected error occurred while processing request for operation '"
                    + getName() + "': " + getCausedByMessage(t), t);
        }
    }

    private String getCausedByMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }
}
