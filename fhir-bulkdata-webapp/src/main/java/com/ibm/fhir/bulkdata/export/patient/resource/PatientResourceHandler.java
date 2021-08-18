/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.export.patient.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.ibm.fhir.bulkdata.audit.BulkAuditLogger;
import com.ibm.fhir.bulkdata.export.system.resource.SystemExportResourceHandler;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * The PatientResourceHandler controls the population of the Patient Resources (Or Group member Resources into the TransientData object)
 */
public class PatientResourceHandler extends SystemExportResourceHandler {

    private static final Logger logger = Logger.getLogger(PatientResourceHandler.class.getName());

    private BulkAuditLogger auditLogger = new BulkAuditLogger();

    // Used to prevent the same resource from being exported multiple times when multiple _typeFilter for the same
    // resource type are used, which leads to multiple search requests which can have overlaps of resources,
    // loadedResourceIds and isDoDuplicationCheck are always reset when moving to the next resource type.
    Set<String> loadedResourceIds = new HashSet<>();

    private ExportTransientUserData chunkData;
    private BulkDataContext ctx;
    private FHIRPersistence fhirPersistence;
    private int pageSize = 1;
    private Class<? extends Resource> resourceType;
    private Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes;
    private String provider = null;

    public PatientResourceHandler() {
        // No Operation
    }

    public void register(ExportTransientUserData chunkData, BulkDataContext ctx, FHIRPersistence fhirPersistence,
            int pageSize, Class<? extends Resource> resourceType, Map<Class<? extends Resource>,
            List<Map<String, List<String>>>> searchParametersForResoureTypes, String provider) {
        this.chunkData = chunkData;
        this.ctx = ctx;
        this.fhirPersistence = fhirPersistence;
        this.pageSize = pageSize;
        this.resourceType = resourceType;
        this.searchParametersForResoureTypes = searchParametersForResoureTypes;
        this.provider = provider;
    }

    /**
     * @param patientIds the patient ids to use to scope the search
     * @throws Exception
     */
    public List<Resource> executeSearch(Set<String> patientIds) throws Exception {
        List<Map<String, List<String>>> typeFilters = searchParametersForResoureTypes.get(resourceType);
        boolean isDoDuplicationCheck = typeFilters != null && typeFilters.size() > 1 ?
                true : adapter.shouldStorageProviderCheckDuplicate(ctx.getSource());
        int indexOfCurrentTypeFilter = 0;
        int compartmentPageNum = 1;
        FHIRSearchContext searchContext;

        List<Resource> results = new ArrayList<>();

        if (chunkData == null) {
            String msg = "fillChunkDataBuffer: chunkData is null, this should never happen!";
            logger.warning(msg);
            throw new Exception(msg);
        }

        do {
            Map<String, List<String>> queryParameters = new HashMap<>();
            // Add the search parameters from the current typeFilter for current resource type.
            if (searchParametersForResoureTypes.get(resourceType) != null) {
                queryParameters.putAll(searchParametersForResoureTypes.get(resourceType).get(indexOfCurrentTypeFilter));
            }
            List<String> searchCriteria = new ArrayList<>();
            if (ctx.getFhirSearchFromDate() != null) {
                // https://www.hl7.org/fhir/r4/search.html#prefix
                searchCriteria.add(Prefix.GE.value() + ctx.getFhirSearchFromDate());
            }
            if (ctx.getFhirSearchToDate() != null) {
                searchCriteria.add(Prefix.LT.value() + ctx.getFhirSearchToDate());
            }

            if (!searchCriteria.isEmpty()) {
                queryParameters.put(SearchConstants.LAST_UPDATED, searchCriteria);
            }
            queryParameters.put(SearchConstants.SORT, Arrays.asList(SearchConstants.LAST_UPDATED));

            if (Patient.class.isAssignableFrom(resourceType)) {
                String patientIdQueryParameterValue = patientIds.stream().collect(Collectors.joining(","));
                queryParameters.put(SearchConstants.ID, Arrays.asList(patientIdQueryParameterValue));
                searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
            } else {
                searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
                QueryParameter inclusionCriteria = SearchUtil.buildInclusionCriteria("Patient", patientIds, resourceType.getSimpleName());
                searchContext.getSearchParameters().add(0, inclusionCriteria);
            }
            searchContext.setPageSize(pageSize);

            do {
                searchContext.setPageNumber(compartmentPageNum);
                FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);

                Date startTime = new Date(System.currentTimeMillis());
                List<Resource> resources = fhirPersistence.search(persistenceContext, resourceType).getResource();

                if (isDoDuplicationCheck) {
                    resources = resources.stream()
                            // the add returns false if the id already exists, which filters it out of the collection
                            .filter(r -> loadedResourceIds.add(r.getId()))
                            .collect(Collectors.toList());
                }
                results.addAll(resources);

                if (auditLogger.shouldLog() && resources != null) {
                    Date endTime = new Date(System.currentTimeMillis());
                    auditLogger.logSearchOnExport(ctx.getPartitionResourceType(), queryParameters, resources.size(), startTime, endTime,
                            Response.Status.OK, "StorageProvider@" + provider, "BulkDataOperator");
                }
                compartmentPageNum++;
            } while (compartmentPageNum <= searchContext.getLastPageNumber());
            compartmentPageNum = 1;

            indexOfCurrentTypeFilter++;
        } while (searchParametersForResoureTypes.get(resourceType) != null
                && indexOfCurrentTypeFilter < searchParametersForResoureTypes.get(resourceType).size());

        return results;
    }
}
