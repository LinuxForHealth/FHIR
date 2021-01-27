/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;
import com.ibm.fhir.server.operation.spi.AbstractOperation;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

/**
 * <a href="https://hl7.org/Fhir/uv/bulkdata/OperationDefinition-export.html">BulkDataAccess V1.0.0: STU1 -
 * ExportOperation</a> Creates an System Export of FHIR Data to NDJSON format system export operation definition for
 * <code>$export</code>
 */
public class ExportOperation extends AbstractOperation {
    private static final String FILE = "export.json";

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
        // Pick off parameters
        MediaType outputFormat = BulkDataExportUtil.checkAndConvertToMediaType(parameters);
        if (FHIRMediaType.SUBTYPE_FHIR_PARQUET.equals(outputFormat.getSubtype())) {
            Boolean enableParquet = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_ENABLEPARQUET, false);
            if (!enableParquet) {
                throw buildExceptionWithIssue(
                        "Export to parquet is not enabled; try 'application/fhir+ndjson' or contact the system administrator",
                        IssueType.INVALID);
            }
        }

        Instant since = BulkDataExportUtil.checkAndExtractSince(parameters);
        List<String> types = BulkDataExportUtil.checkAndValidateTypes(parameters);
        List<String> typeFilters = BulkDataExportUtil.checkAndValidateTypeFilters(parameters);

        // If Patient - Export Patient Filter Resources
        Parameters response = null;
        BulkDataConstants.ExportType exportType = BulkDataExportUtil.checkExportType(operationContext.getType(), resourceType);

        // Allow the configuration to force use of the "legacy" system export implementation
        // (useful for A/B comparison)
        String systemExportImpl = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_SYSTEMEXPIMPL, "fast");

        if (!ExportType.INVALID.equals(exportType)) {
            // For System $export, resource type(s) is required.
            if (ExportType.SYSTEM.equals(exportType) && types == null) {
                throw BulkDataExportUtil.buildOperationException("Missing resource type(s)!", IssueType.INVALID);
            }

            response = BulkDataFactory.getTenantInstance().export(logicalId, exportType, outputFormat, since, types,
                    typeFilters, operationContext, resourceHelper, systemExportImpl);
        } else {
            // Unsupported on instance, specific types other than group/patient/system
            throw buildExceptionWithIssue(
                    "Invalid call $export operation call to '" + resourceType.getSimpleName() + "'", IssueType.INVALID);
        }
        return response;
    }
}