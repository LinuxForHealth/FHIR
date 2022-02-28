/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.OperationConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.config.preflight.Preflight;
import com.ibm.fhir.operation.bulkdata.config.preflight.PreflightFactory;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;
import com.ibm.fhir.operation.bulkdata.util.CommonUtil;
import com.ibm.fhir.operation.bulkdata.util.CommonUtil.Type;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * Creates a System Export of FHIR Data to NDJSON format
 * @see https://hl7.org/Fhir/uv/bulkdata/OperationDefinition-export.html
 */
public class ExportOperation extends AbstractOperation {

    private static final CommonUtil COMMON = new CommonUtil(Type.EXPORT);
    private static final BulkDataExportUtil export = new BulkDataExportUtil();

    public ExportOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/uv/bulkdata/OperationDefinition/export", OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
            throws FHIROperationException {
        COMMON.checkEnabled();
        COMMON.checkAllowed(operationContext, false);

        // Pick off parameters
        javax.ws.rs.core.UriInfo uriInfo = (javax.ws.rs.core.UriInfo) operationContext.getProperty(FHIROperationContext.PROPNAME_URI_INFO);
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();

        MediaType outputFormat = export.checkAndConvertToMediaType(parameters);

        Instant since = export.checkAndExtractSince(parameters);

        List<String> typeFilters = export.checkAndValidateTypeFilters(parameters);

        // If Patient - Export Patient Filter Resources
        Parameters response = null;
        OperationConstants.ExportType exportType = export.checkExportType(operationContext.getType(), resourceType);

        List<String> types = export.checkAndValidateTypes(exportType, parameters, queryParameters);

        if (!ExportType.INVALID.equals(exportType)) {

            // Early detection of potential issues.
            Preflight preflight =  PreflightFactory.getInstance(operationContext, null, exportType, outputFormat.toString());
            preflight.preflight();

            // Warning that Parquet is deprecated.
            if (FHIRMediaType.SUBTYPE_FHIR_PARQUET.equals(outputFormat.getSubtype())) {
                throw buildExceptionWithIssue("Export to parquet is no longer supported; try 'application/fhir+ndjson'", IssueType.INVALID);
            }

            response = BulkDataFactory.getInstance(operationContext)
                        .export(logicalId, exportType, outputFormat, since, types, typeFilters, operationContext);
        } else {
            // Unsupported on instance, specific types other than group/patient/system
            throw buildExceptionWithIssue(
                    "Invalid call $export operation call to '" + resourceType.getSimpleName() + "'", IssueType.INVALID);
        }
        return response;
    }
}