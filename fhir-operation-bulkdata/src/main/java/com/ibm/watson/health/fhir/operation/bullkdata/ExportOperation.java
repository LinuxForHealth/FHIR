/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.operation.bullkdata;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.exception.FHIROperationException;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.resource.OperationDefinition;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.model.type.IssueType;
import com.ibm.watson.health.fhir.operation.AbstractOperation;
import com.ibm.watson.health.fhir.operation.bullkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.watson.health.fhir.operation.bullkdata.processor.BulkDataFactory;
import com.ibm.watson.health.fhir.operation.bullkdata.util.BulkDataUtil;
import com.ibm.watson.health.fhir.operation.context.FHIROperationContext;
import com.ibm.watson.health.fhir.rest.FHIRResourceHelpers;

/**
 * Creates an Export of FHIR Data to NDJSON format
 * 
 * @link https://build.fhir.org/ig/HL7/bulk-data/OperationDefinition-export.html At the time of building this...
 *       BulkDataAccess IG: STU1
 * 
 * These three operation definitions are for <code>$export</code>:
 * @link export https://build.fhir.org/ig/HL7/bulk-data/OperationDefinition-export.json.html
 * @link patient-export https://build.fhir.org/ig/HL7/bulk-data/OperationDefinition-patient-export.json.html
 * @link group-export https://build.fhir.org/ig/HL7/bulk-data/OperationDefinition-group-export.json.html
 * 
 * @author pbastide
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
                    BulkDataFactory.getExport(cache).exportPatient(logicalId, outputFormat, since, types, typeFilters, ctx, resourceHelper);
        } else if (BulkDataUtil.checkType(resourceType, "Group")) {
            // If Group, Export and Patient Members Filter Resources
            response =
                    BulkDataFactory.getExport(cache).exportGroup(logicalId, outputFormat, since, types, typeFilters, ctx, resourceHelper);
        } else if (resourceType == null) {
            // If Base, Export (Else Invalid)
            response =
                    BulkDataFactory.getExport(cache).exportBase(outputFormat, since, types, typeFilters, ctx, resourceHelper, operationContext, cache);
        } else {
            // Unsupported on Resource Type
            throw buildExceptionWithIssue("Invalid call $export operation call to '"
                    + resourceType.getSimpleName() + "'", IssueType.ValueSet.INVALID);
        }

        return response;
    }
    
}
