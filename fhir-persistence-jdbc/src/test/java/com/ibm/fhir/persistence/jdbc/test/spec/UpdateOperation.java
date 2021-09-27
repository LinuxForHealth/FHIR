/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.time.ZoneOffset;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Resource.Builder;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

public class UpdateOperation extends BaseOperation {

    private static final Logger logger = Logger.getLogger(UpdateOperation.class.getName());

	@Override
	public void process(TestContext tc) throws FHIRPersistenceException {
	    
        Resource resource = tc.getResource();
        final FHIRPersistenceContext context = tc.createPersistenceContext();

        final String logicalId = resource.getId();
        logger.fine("Updating: " + logicalId);
        
        // For 1869, we need to update the resource meta before we call the persistence layer
        Instant lastUpdated = Instant.now(ZoneOffset.UTC);
        final int newVersionNumber = Integer.parseInt(resource.getMeta().getVersionId().getValue()) + 1;
        resource = copyAndSetResourceMetaFields(resource, resource.getId(), newVersionNumber, lastUpdated);

        Resource newResource = tc.getPersistence().update(context, logicalId, newVersionNumber, resource).getResource();
        check(tc, tc.getResource(), newResource, this.getClass().getSimpleName());
        
        // Update the context with the modified resource
        tc.setResource(newResource);
	}

    /**
     * Creates and returns a copy of the passed resource with the {@code Resource.id}
     * {@code Resource.meta.versionId}, and {@code Resource.meta.lastUpdated} elements replaced.
     *
     * @param resource
     * @param logicalId
     * @param newVersionNumber
     * @param lastUpdated
     * @return the updated resource
     */
    @SuppressWarnings("unchecked")
    private <T extends Resource> T copyAndSetResourceMetaFields(T resource, String logicalId, int newVersionNumber, com.ibm.fhir.model.type.Instant lastUpdated) {
        Meta meta = resource.getMeta();
        Meta.Builder metaBuilder = meta == null ? Meta.builder() : meta.toBuilder();
        metaBuilder.versionId(Id.of(Integer.toString(newVersionNumber)));
        metaBuilder.lastUpdated(lastUpdated);

        Builder resourceBuilder = resource.toBuilder();
        resourceBuilder.setValidating(false);
        return (T) resourceBuilder
                .id(logicalId)
                .meta(metaBuilder.build())
                .build();
    }
}
