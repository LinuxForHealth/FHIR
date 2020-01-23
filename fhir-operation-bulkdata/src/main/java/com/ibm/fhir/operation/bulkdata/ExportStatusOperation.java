/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.AbstractOperation;
import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;
import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.util.BulkDataUtil;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.rest.FHIRResourceHelpers;

/**
 * <a href="https://build.fhir.org/ig/HL7/bulk-data/index.html">BulkDataAccess IG: STU1 - Polling Response</a><br>
 * There are two specific operations
 * <li>status of a bulkdata export/import job</li>
 * <li>delete a bulkdata export/import job</li>
 */
public class ExportStatusOperation extends AbstractOperation {
    private static final String FILE = "export-status.json";

    public ExportStatusOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE);) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
            throws FHIROperationException {
        if (logicalId == null && versionId == null && resourceType == null) {
            BulkDataTenantSpecificCache cache = BulkDataConfigUtil.getInstance();
            String method = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_METHOD_TYPE);
            if ("DELETE".equalsIgnoreCase(method)) {
                // Assume GET or POST
                String job = BulkDataUtil.checkAndValidateJob(parameters);
                // For now, we're going to execute the status update, and check. 
                // If Base, Export Status (Else Invalid)
                return BulkDataFactory.getTenantInstance(cache).delete(job, operationContext);
            } else {
                // Assume GET or POST
                String job = BulkDataUtil.checkAndValidateJob(parameters);
                // For now, we're going to execute the status update, and check. 
                // If Base, Export Status (Else Invalid)
                return BulkDataFactory.getTenantInstance(cache).status(job, operationContext);
            }
        } else {
            // Unsupported on Resource Type
            // Root operation is only supported, and we signal it back here. 
            // Don't get fancy, just send it back. 
            throw buildExceptionWithIssue("Invalid call $export-status operation call", IssueType.INVALID);
        }
    }
}