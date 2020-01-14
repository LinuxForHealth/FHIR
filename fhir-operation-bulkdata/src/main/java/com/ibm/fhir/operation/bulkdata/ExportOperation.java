/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.AbstractOperation;
import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.util.BulkDataUtil;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.rest.FHIRResourceHelpers;

/**
 * Creates an System Export of FHIR Data to NDJSON format
 *
 * @link https://hl7.org/Fhir/uv/bulkdata/OperationDefinition-export.html At the time of building this...
 *       BulkDataAccess V1.0.0: STU1
 *
 * system export operation definition for <code>$export</code>:
 *
 */
public class ExportOperation extends AbstractOperation {

    private static final String FILE = "export.json";

    private static BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();

    public ExportOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        /**
         * Loads the operation definition file. In this case, there are three files, and only one is loaded.
         */
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE);) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext,
        Class<? extends Resource> resourceType, String logicalId, String versionId,
        Parameters parameters,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {

        FHIRRequestContext ctx = FHIRRequestContext.get();

        // Pick off parameters
        MediaType outputFormat = BulkDataUtil.checkAndConvertToMediaType(parameters, operationContext);
        Instant since = BulkDataUtil.checkAndExtractSince(parameters);
        List<String> types = BulkDataUtil.checkAndValidateTypes(parameters);
        List<String> typeFilters = BulkDataUtil.checkAndValidateTypeFilters(parameters);

        // If Patient - Export Patient Filter Resources
        Parameters response = null;
        if (BulkDataUtil.checkType(resourceType, "Patient")) {
            response =
                    BulkDataFactory.getExport(cache).exportPatient(logicalId, outputFormat, since, types, typeFilters, ctx, resourceHelper, operationContext, cache);
        } else if (BulkDataUtil.checkType(resourceType, "Group")) {
            // If Group, Export and Patient Members Filter Resources
            response =
                    BulkDataFactory.getExport(cache).exportGroup(logicalId, outputFormat, since, types, typeFilters, ctx, resourceHelper, operationContext, cache);
        } else if (resourceType == null) {
            // If Base, Export (Else Invalid)
            response =
                    BulkDataFactory.getExport(cache).exportBase(outputFormat, since, types, typeFilters, ctx, resourceHelper, operationContext, cache);
        } else {
            // Unsupported on Resource Type
            throw buildExceptionWithIssue("Invalid call $export operation call to '"
                    + resourceType.getSimpleName() + "'", IssueType.INVALID);
        }

        return response;
    }

}
