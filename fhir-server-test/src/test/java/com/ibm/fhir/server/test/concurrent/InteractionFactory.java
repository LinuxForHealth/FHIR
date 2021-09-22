/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.concurrent;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.concurrent.Callable;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import com.ibm.fhir.client.FHIRClient;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.test.operation.EraseOperationTest;

/**
 * There is a lot packed into this one class for testing purposes only.
 */
public final class InteractionFactory {

    /**
     * The type of concurrent interaction
     */
    public static enum InteractionType {
        CREATE,
        READ,
        UPDATE,
        DELETE,
        ERASE
    }

    private InteractionFactory() {
        // No Operation
    }

    public static InteractionStrategy getInteraction(InteractionType type) {
        InteractionStrategy strategy;
        switch (type) {
        case ERASE:
            strategy = new EraseInteraction();
            break;
        case CREATE:
            strategy = new CreateInteraction();
        case UPDATE:
            strategy = new UpdateInteraction();
            break;
        case READ:
            strategy = new ReadInteraction();
            break;
        case DELETE:
            strategy = new DeleteInteraction();
            break;
        default:
            throw new AssertionError("There are no other interaction types");
        }
        return strategy;
    }

    public static interface InteractionStrategy {

        /**
         * executes the Interaction Strategy and Returns the Resource
         *
         * @param client
         * @param resource
         * @param resourceType
         * @param logicalId
         * @param maxExecutions
         * @return
         */
        public Resource execute(FHIRClient client, Resource resource, String resourceType, String logicalId, int maxExecutions);
    }

    /**
     * EraseInteraction encapsulates the Interaction with the REST API.
     */
    public static class EraseInteraction implements InteractionStrategy {

        @Override
        public Resource execute(FHIRClient client, Resource resource, String resourceType, String logicalId, int maxExecutions) {
            Entity<Parameters> entity = Entity.entity(EraseOperationTest.generateParameters(true, true, "Test", null), FHIRMediaType.APPLICATION_FHIR_JSON);

            for (int i = 0; i < maxExecutions; i++) {
                try {
                    Response r = client.getWebTarget().path("/" + resourceType + "/" + logicalId
                            + "/$erase").request(FHIRMediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "default").header("X-FHIR-DSID", "default").post(entity, Response.class);
                    assertTrue(r.getStatus() == Response.Status.OK.getStatusCode() || r.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
                    Thread.sleep(1000);
                } catch (Exception e) {
                    fail("Unexpected", e);
                }
            }
            return null;
        }
    }

    /**
     * Update Interaction
     */
    public static class UpdateInteraction implements InteractionStrategy {

        @Override
        public Resource execute(FHIRClient client, Resource resource, String resourceType, String logicalId, int maxExecutions) {
            int status = 500;
            do {
                FHIRResponse response;
                try {
                    response = client.update(resource);
                    status = response.getStatus();
                } catch (Exception e) {
                    status = 500;
                    fail("Unexpected", e);
                }
            } while (status == Response.Status.CONFLICT.getStatusCode());

            assertTrue(status == Response.Status.OK.getStatusCode() || status == Response.Status.CREATED.getStatusCode(), "Status=" + status);
            return null;
        }
    }

    /**
     * Create Interaction
     */
    public static class CreateInteraction implements InteractionStrategy {

        @Override
        public Resource execute(FHIRClient client, Resource resource, String resourceType, String logicalId, int maxExecutions) {
            int status = 500;
            do {
                FHIRResponse response;
                try {
                    response = client.create(resource);
                    status = response.getStatus();
                } catch (Exception e) {
                    status = 500;
                    fail("Unexpected", e);
                }
            } while (status == Response.Status.CONFLICT.getStatusCode());

            assertTrue(status == Response.Status.OK.getStatusCode() || status == Response.Status.CREATED.getStatusCode(), "Status=" + status);
            return null;
        }
    }

    /**
     * Read Interaction
     */
    public static class ReadInteraction implements InteractionStrategy {

        @Override
        public Resource execute(FHIRClient client, Resource resource, String resourceType, String logicalId, int maxExecutions) {
            int status = 500;
            do {
                FHIRResponse response;
                try {
                    response = client.read(resourceType, logicalId);
                    status = response.getStatus();
                } catch (Exception e) {
                    status = 500;
                    fail("Unexpected", e);
                }
            } while (status == Response.Status.CONFLICT.getStatusCode());

            assertTrue(status == Response.Status.OK.getStatusCode(), "Status=" + status);
            return null;
        }
    }

    /**
     * Delete Interaction
     */
    public static class DeleteInteraction implements InteractionStrategy {
        @Override
        public Resource execute(FHIRClient client, Resource resource, String resourceType, String logicalId, int maxExecutions) {
            int status = 500;
            do {
                FHIRResponse response;
                try {
                    response = client.delete(resourceType, logicalId);
                    status = response.getStatus();
                } catch (Exception e) {
                    status = 500;
                    fail("Unexpected", e);
                }
            } while (status == Response.Status.CONFLICT.getStatusCode());

            assertTrue(status == Response.Status.OK.getStatusCode() || status == Response.Status.NOT_FOUND.getStatusCode(), "Status=" + status);
            return null;
        }
    }

    /**
     * This inner class invokes the InteractionType FHIRClient API for the resource instance it encapsulates,
     * retrying in a tight loop in the case of a 409 Conflict.
     */
    public static class InteractionCallable implements Callable<Resource> {
        private InteractionStrategy strategy = null;
        private FHIRClient client = null;
        private Resource resource = null;
        private String resourceType = null;
        private String logicalId = null;
        private int maxExecutions = -1;

        public InteractionCallable(InteractionType type, FHIRClient client, Resource resource, String resourceType, String logicalId, int maxExecutions) {
            this.strategy = getInteraction(type);
            this.client = client;
            this.resource = resource;
            this.resourceType = resourceType;
            this.logicalId = logicalId;
            this.maxExecutions = maxExecutions;
        }

        @Override
        public Resource call() throws Exception {
            return strategy.execute(client, resource, resourceType, logicalId, maxExecutions);
        }
    }
}