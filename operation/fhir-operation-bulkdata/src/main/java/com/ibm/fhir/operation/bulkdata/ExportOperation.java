/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
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
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * <a href="https://hl7.org/Fhir/uv/bulkdata/OperationDefinition-export.html">BulkDataAccess V1.0.0: STU1 -
 * ExportOperation</a> Creates an System Export of FHIR Data to NDJSON format system export operation definition for
 * <code>$export</code>
 */
public class ExportOperation extends AbstractOperation {
    private static final String FILE = "export.json";

    private static final CommonUtil common = new CommonUtil();
    private static final BulkDataExportUtil export = new BulkDataExportUtil();

    public ExportOperation() {
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
        common.checkEnabled();
        common.checkAllowed(operationContext, false);

        // Pick off parameters
        MediaType outputFormat = export.checkAndConvertToMediaType(parameters);

        Instant since = export.checkAndExtractSince(parameters);
        List<String> types = export.checkAndValidateTypes(parameters);
        List<String> typeFilters = export.checkAndValidateTypeFilters(parameters);

        // If Patient - Export Patient Filter Resources
        Parameters response = null;
        OperationConstants.ExportType exportType = export.checkExportType(operationContext.getType(), resourceType);

        if (!ExportType.INVALID.equals(exportType)) {
            if (ExportType.PATIENT.equals(exportType) || ExportType.GROUP.equals(exportType)) {
                if (types != null && !types.isEmpty()) {
                    export.checkExportPatientResourceTypes(types);
                } else {
                    types = export.addDefaultsForPatientCompartment();
                }
            } else if ((ExportType.SYSTEM.equals(exportType) )
                            && (types == null || types.isEmpty())) {
                types = export.defaultResourceTypes();
            }

            // Early detection of potential issues.
            Preflight preflight =  PreflightFactory.getInstance(operationContext, null, exportType, outputFormat.toString());
            preflight.preflight();

            // Checks if parquet is enabled for the storage provider
            if (FHIRMediaType.SUBTYPE_FHIR_PARQUET.equals(outputFormat.getSubtype()) && !preflight.checkParquet()) {
                throw buildExceptionWithIssue(
                        "Export to parquet is not enabled; try 'application/fhir+ndjson' or contact the system administrator",
                         IssueType.INVALID);
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