/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase;

import java.io.InputStream;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.erase.adapter.ResourceEraseRecordAdapter;
import com.ibm.fhir.operation.erase.audit.EraseOperationAuditLogger;
import com.ibm.fhir.operation.erase.impl.EraseRest;
import com.ibm.fhir.operation.erase.impl.EraseRestFactory;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * Custom Operation to Erase a specific instance or instance-version of the FHIR
 * Resource and it's history and values table supported by the IBM FHIR Server.
 */
public class EraseOperation extends AbstractOperation {
    private ResourceEraseRecordAdapter adapter = new ResourceEraseRecordAdapter();

    public EraseOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        FHIRParser parser = FHIRParser.parser(Format.JSON);
        return generateOperationDefinition(parser);
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper, SearchUtil searchHelper) throws FHIROperationException {
        // Create the audit, so we get the start time.
        EraseOperationAuditLogger audit = new EraseOperationAuditLogger(operationContext);

        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);

        // Authorize should be executed first so it is the first error before enabled check.
        erase.authorize();
        erase.enabled();
        EraseDTO eraseDto = erase.verify();

        try {
            ResourceEraseRecord eraseRecord = resourceHelper.doErase(operationContext, eraseDto);
            // Checks to see if it's not found
            if (ResourceEraseRecord.Status.NOT_FOUND == eraseRecord.getStatus()) {
                FHIROperationException notFoundEx = FHIROperationUtil.buildExceptionWithIssue("Resource not found", IssueType.NOT_FOUND);
                audit.error(parameters, notFoundEx, eraseDto);
                throw notFoundEx;
            } else if (ResourceEraseRecord.Status.NOT_SUPPORTED_GREATER == eraseRecord.getStatus()) {
                FHIROperationException badVersion = FHIROperationUtil.buildExceptionWithIssue("Resource Version specified is greater than the version found", IssueType.INVALID);
                audit.error(parameters, badVersion, eraseDto);
                throw badVersion;
            } else if (ResourceEraseRecord.Status.NOT_SUPPORTED_LATEST == eraseRecord.getStatus()) {
                FHIROperationException badVersion = FHIROperationUtil.buildExceptionWithIssue("Resource Version specified is the latest version found", IssueType.INVALID);
                audit.error(parameters, badVersion, eraseDto);
                throw badVersion;
            }

            // Adapt to the output type and log it.
            Parameters response = adapter.adapt(eraseRecord, eraseDto);
            audit.audit(response, eraseDto);

            return response;
        } catch (FHIROperationException e) {
            audit.error(parameters, e, eraseDto);
            throw e;
        }
    }

    /**
     * Parser generates the OperationDefinition
     * @implNote this is broken down into a separate method to facilitate testing.
     * @param parser
     * @return
     */
    public OperationDefinition generateOperationDefinition(FHIRParser parser) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("erase.json")) {
            return parser.parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}