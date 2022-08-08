/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.smart.test;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

import org.linuxforhealth.fhir.model.resource.Encounter;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceTransaction;
import org.linuxforhealth.fhir.persistence.HistorySortOrder;
import org.linuxforhealth.fhir.persistence.InteractionStatus;
import org.linuxforhealth.fhir.persistence.MultiResourceResult;
import org.linuxforhealth.fhir.persistence.ResourceChangeLogRecord;
import org.linuxforhealth.fhir.persistence.ResourcePayload;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.payload.PayloadPersistenceResponse;

/**
 * Mock implementation of FHIRPersistence for use during testing.
 *
 */
public class MockPersistenceImpl implements FHIRPersistence {
    static final String ENCOUNTER_ID_GOOD = "good";
    static final String ENCOUNTER_ID_BAD = "bad";

    Patient patient = null;
    Observation observation = null;
    Encounter encounter_in_patient_compartment = null;
    Encounter encounter_not_in_patient_compartment = null;

    public MockPersistenceImpl(Patient patient, Observation observation) throws Exception {
        this.patient = patient;
        this.observation = observation;


        encounter_not_in_patient_compartment = TestUtil.getMinimalResource(Encounter.class);
        encounter_not_in_patient_compartment = encounter_not_in_patient_compartment.toBuilder()
                .meta(Meta.builder().lastUpdated(org.linuxforhealth.fhir.model.type.Instant.now()).versionId(Id.of("1")).build())
                .build();
        encounter_in_patient_compartment = encounter_not_in_patient_compartment.toBuilder()
                .id(ENCOUNTER_ID_GOOD)
                .subject(Reference.builder()
                    .reference(string("Patient/" + patient.getId()))
                    .build())
                .build();
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> read(FHIRPersistenceContext context, Class<T> resourceType, String logicalId)
            throws FHIRPersistenceException {

        if (resourceType == Patient.class) {
            return new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource((T)patient)
                    .interactionStatus(InteractionStatus.READ)
                    .build();
        }

        if (resourceType == Observation.class) {
            return new SingleResourceResult.Builder<T>()
                    .success(true)
                    .interactionStatus(InteractionStatus.READ)
                    .resource((T)observation)
                    .build();
        }

        if (resourceType == Encounter.class) {
            if (ENCOUNTER_ID_GOOD.equals(logicalId)) {
                return new SingleResourceResult.Builder<T>()
                        .success(true)
                        .interactionStatus(InteractionStatus.READ)
                        .resource((T)encounter_in_patient_compartment)
                        .build();
            } else if (ENCOUNTER_ID_BAD.equals(logicalId)){
                return new SingleResourceResult.Builder<T>()
                        .success(true)
                        .interactionStatus(InteractionStatus.READ)
                        .resource((T)encounter_not_in_patient_compartment)
                        .build();
            } else {
                return new SingleResourceResult.Builder<T>()
                        .success(false)
                        .interactionStatus(InteractionStatus.READ)
                        .outcome(FHIRUtil.buildOperationOutcome("Resource '" + resourceType + "/" + logicalId + "' not found.", null, null))
                        .build();
            }
        }

        return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> vread(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, String versionId)
            throws FHIRPersistenceException {

        if (resourceType == Patient.class) {
            return new SingleResourceResult.Builder<T>()
                    .success(true)
                    .interactionStatus(InteractionStatus.READ)
                    .resource((T)patient)
                    .build();
        }

        if (resourceType == Observation.class) {
            return new SingleResourceResult.Builder<T>()
                    .success(true)
                    .interactionStatus(InteractionStatus.READ)
                    .resource((T)observation)
                    .build();
        }

        return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public MultiResourceResult history(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public MultiResourceResult search(FHIRPersistenceContext context, Class<? extends Resource> resourceType) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public boolean isTransactional() {
        return false;
    }

    @Override
    public FHIRPersistenceTransaction getTransaction() {
        return null;
    }

    @Override
    public OperationOutcome getHealth() throws FHIRPersistenceException {
        return null;
    }

    @Override
    public int reindex(FHIRPersistenceContext context, OperationOutcome.Builder oob, Instant tstamp, List<Long> indexIds,
            String resourceLogicalId, boolean force) throws FHIRPersistenceException {
        return 0;
    }

    @Override
    public String generateResourceId() {
        return null;
    }

    @Override
    public ResourcePayload fetchResourcePayloads(Class<? extends Resource> resourceType, Instant fromLastModified, Instant toLastModified,
            Function<ResourcePayload, Boolean> process) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public List<ResourceChangeLogRecord> changes(FHIRPersistenceContext context, int resourceCount, Instant sinceLastModified, Instant beforeLastModified, Long afterResourceId, List<String> resourceTypeNames,
            boolean excludeTransactionTimeoutWindow, HistorySortOrder historySortOrder) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public List<Long> retrieveIndex(FHIRPersistenceContext context, int count, java.time.Instant notModifiedAfter, Long afterIndexId, String resourceTypeName) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public PayloadPersistenceResponse storePayload(Resource resource, String logicalId, int newVersionNumber, String resourcePayloadKey)
            throws FHIRPersistenceException {
        return null;
    }

    @Override
    public List<Resource> readResourcesForRecords(List<ResourceChangeLogRecord> records) throws FHIRPersistenceException {
        // NOP
        return null;
    }
}
