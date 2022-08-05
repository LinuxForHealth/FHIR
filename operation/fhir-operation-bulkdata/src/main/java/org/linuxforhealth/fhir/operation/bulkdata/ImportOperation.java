/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata;

import java.io.InputStream;
import java.util.List;

import org.linuxforhealth.fhir.core.FHIRVersionParam;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.operation.bulkdata.config.preflight.Preflight;
import org.linuxforhealth.fhir.operation.bulkdata.config.preflight.PreflightFactory;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.Input;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.StorageDetail;
import org.linuxforhealth.fhir.operation.bulkdata.processor.BulkDataFactory;
import org.linuxforhealth.fhir.operation.bulkdata.util.BulkDataImportUtil;
import org.linuxforhealth.fhir.operation.bulkdata.util.CommonUtil;
import org.linuxforhealth.fhir.operation.bulkdata.util.CommonUtil.Type;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.AbstractOperation;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * BulkData Specification Proposal:
 * <a href= "https://github.com/smart-on-fhir/bulk-import/blob/master/import.md">$import</a>
 */
public class ImportOperation extends AbstractOperation {
    private static final String FILE = "import.json";

    private static final CommonUtil COMMON = new CommonUtil(Type.IMPORT);

    public ImportOperation() {
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
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper)
            throws FHIROperationException {
        COMMON.checkEnabled();
        COMMON.checkAllowed(operationContext, true);

        // Checks the Import Type
        checkImportType(operationContext.getType());

        BulkDataImportUtil util = new BulkDataImportUtil(operationContext, parameters);

        // Parameter: inputFormat
        String inputFormat = util.retrieveInputFormat();

        // Parameter: inputSource
        String inputSource = util.retrieveInputSource();

        // Parameter: input
        FHIRVersionParam fhirVersion = (FHIRVersionParam) operationContext.getProperty(FHIROperationContext.PROPNAME_FHIR_VERSION);
        List<Input> inputs = util.retrieveInputs(fhirVersion);

        // Parameter: storageDetail
        StorageDetail storageDetail = util.retrieveStorageDetails();

        Preflight preflight =  PreflightFactory.getInstance(operationContext, inputs, null, inputFormat);
        preflight.checkStorageAllowed(storageDetail);
        preflight.preflight(true);
        return BulkDataFactory.getInstance(operationContext, true)
                .importBulkData(inputFormat, inputSource, inputs, storageDetail, operationContext);
    }

    private void checkImportType(FHIROperationContext.Type type) throws FHIROperationException {
        // Check Import Type is System.  We only support system right now.

        if (!FHIROperationContext.Type.SYSTEM.equals(type)) {
            throw buildExceptionWithIssue("Invalid call; $import can only be invoked at the system level",
                    IssueType.INVALID);
        }
    }
}