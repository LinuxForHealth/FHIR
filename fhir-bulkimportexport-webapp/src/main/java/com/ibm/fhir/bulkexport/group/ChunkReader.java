/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.group;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.bulkexport.common.CheckPointUserData;
import com.ibm.fhir.bulkexport.common.TransientUserData;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Group.Member;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Bulk patient group export Chunk implementation - the Reader.
 *
 */
public class ChunkReader extends AbstractItemReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());
    int indexOfCurrentResourceType = 0;
    // Control the number of records to read in each page.
    int pageSize = Constants.DEFAULT_SEARCH_PAGE_SIZE;

    private FHIRPersistence fhirPersistence;
    private List<String> resourceTypes;

    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = "fhir.tenant")
    String fhirTenant;

    /**
     * Fhir data store id.
     */
    @Inject
    @BatchProperty(name = "fhir.datastoreid")
    String fhirDatastoreId;

    /**
     * Fhir ResourceType.
     */
    @Inject
    @BatchProperty(name = "fhir.resourcetype")
    String fhirResourceType;

    /**
     * Fhir Search from date.
     */
    @Inject
    @BatchProperty(name = "fhir.search.fromdate")
    String fhirSearchFromDate;

    /**
     * Fhir search to date.
     */
    @Inject
    @BatchProperty(name = "fhir.search.todate")
    String fhirSearchToDate;

    /**
     * Fhir search patient group id.
     */
    @Inject
    @BatchProperty(name = "fhir.search.patientgroupid")
    String fhirSearchPatientGroupId;

    /**
     * Fhir search page size.
     */
    @Inject
    @BatchProperty(name = "fhir.search.pagesize")
    String fhirSearchPageSize;

    @Inject
    JobContext jobContext;

    public ChunkReader() {
        super();
    }

    private void fillChunkDataBuffer(List<Member> patientRefs) throws Exception {
        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();
        int compartmentPageNum = 1;
        int resSubTotal = 0;
        FHIRSearchContext searchContext;
        Class<? extends Resource> resourceType = ModelSupport
                .getResourceType(resourceTypes.get(indexOfCurrentResourceType));
        if (chunkData != null) {
            for (Member patientRef : patientRefs) {
                if (patientRef == null) {
                    continue;
                }

                String patientId =  patientRef.getEntity().getReference().getValue().substring(8);
                Map<String, List<String>> queryParameters = new HashMap<>();

                List<String> searchCreterial = new ArrayList<String>();

                if (fhirSearchFromDate != null) {
                    searchCreterial.add("ge" + fhirSearchFromDate);
                }
                if (fhirSearchToDate != null) {
                    searchCreterial.add("lt" + fhirSearchToDate);
                }
                if (!searchCreterial.isEmpty()) {
                    queryParameters.put(Constants.FHIR_SEARCH_LASTUPDATED, searchCreterial);
                }

                queryParameters.put("_sort", Arrays.asList(new String[] { Constants.FHIR_SEARCH_LASTUPDATED }));
                searchContext = SearchUtil.parseQueryParameters("Patient", patientId,
                        ModelSupport.getResourceType(resourceTypes.get(indexOfCurrentResourceType)), queryParameters, null, true);
                do {
                    searchContext.setPageSize(pageSize);
                    searchContext.setPageNumber(compartmentPageNum);
                    FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
                    txn.begin();
                    FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
                    List<Resource> resources2 = fhirPersistence.search(persistenceContext, resourceType).getResource();
                    txn.commit();
                    compartmentPageNum++;

                    for (Resource res2 : resources2) {
                        if (res2 == null) {
                            continue;
                        }
                        try {
                            FHIRGenerator.generator(Format.JSON).generate(res2, chunkData.getBufferStream());
                            chunkData.getBufferStream().write(Constants.NDJSON_LINESEPERATOR.getBytes());
                            resSubTotal++;
                        } catch (FHIRGeneratorException e) {
                            logger.log(Level.WARNING, "fillChunkDataBuffer: Error while writing resources with id '"
                                        + patientId + "'", e);
                        } catch (IOException e) {
                            logger.warning("fillChunkDataBuffer: chunkDataBuffer written error!");
                            throw e;
                        }
                    }

                } while (searchContext.getLastPageNumber() >= compartmentPageNum);
            }
            chunkData.setCurrentPartResourceNum(chunkData.getCurrentPartResourceNum() + resSubTotal);
            logger.fine("fillChunkDataBuffer: Processed resources - " + resSubTotal + "; Bufferred data size - "
                    + chunkData.getBufferStream().size());
        } else {
            logger.warning("fillChunkDataBuffer: chunkData is null, this should never happen!");
            throw new Exception("fillChunkDataBuffer: chunkData is null, this should never happen!");
        }
    }

    private void expandGroup2Patients(String fhirTenant, String fhirDatastoreId, Group group, List<Member> patients, HashSet<String> groupsInPath)
            throws Exception{
        if (group == null) {
            return;
        }
        groupsInPath.add(group.getId());
        for (Member member : group.getMember()) {
            String refValue = member.getEntity().getReference().getValue();
            if (refValue.startsWith("Patient")) {
                patients.add(member);
            } else if (refValue.startsWith("Group")) {
                Group group2 = findGroupByID(fhirTenant, fhirDatastoreId, refValue.substring(6));
                if (!groupsInPath.contains(group.getId())) {
                    expandGroup2Patients(fhirTenant, fhirDatastoreId, group2, patients, groupsInPath);
                }
            }
        }
    }

    private Group findGroupByID(String fhirTenant, String fhirDatastoreId, String groupId) throws Exception{
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));
        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();

        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_id", Arrays.asList(new String[] { groupId }));
        searchContext = SearchUtil.parseQueryParameters(Group.class, queryParameters);
        List<Resource> resources = null;
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
        resources = fhirPersistence.search(persistenceContext, Group.class).getResource();
        txn.commit();

        if (resources != null) {
            return (Group) resources.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Object readItem() throws Exception {
        List <String> allCompartmentResourceTypes = CompartmentUtil.getCompartmentResourceTypes("Patient");
        if (fhirResourceType == null ) {
            resourceTypes = allCompartmentResourceTypes;
        } else {
            List<String> tmpResourceTypes = Arrays.asList(fhirResourceType.split("\\s*,\\s*"));
            resourceTypes = tmpResourceTypes.stream().filter(item-> allCompartmentResourceTypes.contains(item)).collect(Collectors.toList());
            if (resourceTypes == null || resourceTypes.isEmpty()) {
                throw new Exception("readItem: None of the input resource types is valid!");
            }
        }

        if (fhirSearchPatientGroupId == null) {
            throw new Exception("readItem: missing group id for this group export job!");
        }

        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();
        if (chunkData != null) {
            if (resourceTypes.size() == indexOfCurrentResourceType + 1) {
                // No more resource type and page to read, so return null to end the reading.
                return null;
            } else {
                // More resource types to read, so reset partNum and move resource type index to the next.
                chunkData.setPartNum(1);
                indexOfCurrentResourceType++;
            }
        }
        if (fhirTenant == null) {
            fhirTenant = Constants.DEFAULT_FHIR_TENANT;
            logger.info("readItem: Set tenant to default!");
        }
        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            logger.info("readItem: Set DatastoreId to default!");
        }
        if (fhirSearchPageSize != null) {
            try {
                pageSize = Integer.parseInt(fhirSearchPageSize);
                logger.fine("readItem: Set page size to " + pageSize + ".");
            } catch (Exception e) {
                logger.warning("readItem: Set page size to default(" + Constants.DEFAULT_SEARCH_PAGE_SIZE + ").");
            }
        }

        Group group = findGroupByID(fhirTenant, fhirDatastoreId, fhirSearchPatientGroupId);
        // List for the patients
        List<Member> patientMembers = new ArrayList<>();
        // List for the group and sub groups in the expansion paths, this is used to avoid dead loop caused by circle reference of the groups.
        HashSet<String> groupsInPath = new HashSet<>();
        expandGroup2Patients(fhirTenant, fhirDatastoreId, group, patientMembers, groupsInPath);

        if (chunkData == null) {
            chunkData = new TransientUserData(0, null, new ArrayList<PartETag>(), 1);
            chunkData.setIndexOfCurrentResourceType(0);
            jobContext.setTransientUserData(chunkData);
        } else {
            chunkData.setIndexOfCurrentResourceType(indexOfCurrentResourceType);
        }
        // The fhir resources of one resource type for all the patients will be exported into one COS object.
        // Here we simply set the lastPageNum to be smaller than the next PageNum to ask the common ChunkWriter
        // to close the writing for current resource type.
        chunkData.setPageNum(2);
        chunkData.setLastPageNum(1);

        if (!patientMembers.isEmpty()) {
            logger.fine("readItem: loaded patients number - " + patientMembers.size());
            fillChunkDataBuffer(patientMembers);
        } else {
            logger.fine("readItem: End of reading!");
        }

        return patientMembers;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint != null) {
            CheckPointUserData checkPointData = (CheckPointUserData) checkpoint;
            indexOfCurrentResourceType = checkPointData.getIndexOfCurrentResourceType();
            jobContext.setTransientUserData(TransientUserData.fromCheckPointUserData(checkPointData));
        }
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return CheckPointUserData.fromTransientUserData((TransientUserData) jobContext.getTransientUserData());
    }

}
