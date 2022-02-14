/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.resource.Resource;

/**
 * The base result wrapper used to represent a resource being returned from a
 * persistence interaction.
 * Instances are immutable and can be constructed via {@code new ResourceResult.Builder<T>()}.
 */
public class ResourceResult<T extends Resource> {
    @Required
    private final T resource;
    private final boolean deleted;
    private final String resourceTypeName;
    private final String logicalId;
    private final int version;
    private final Instant lastUpdated;

    /**
     * Private constructor used by the Builder to create a new ResourceResult instance
     * @param builder
     */
    private ResourceResult(Builder<T> builder) {
        if (builder.resource == null && builder.version < 1) {
            // This is an extra check to make sure that the caller has set the version
            // correctly when no version is available.
            throw new IllegalArgumentException("version must be set if resource is null");
        }

        resource = builder.resource;
        deleted = builder.deleted;
        resourceTypeName = builder.resourceTypeName;
        logicalId = builder.logicalId;
        lastUpdated = builder.lastUpdated;

        // as a convenience (primarily for unit test mocks) set the version from
        // the resource if we have one
        if (this.resource != null && builder.version < 1) {
            this.version = Integer.parseInt(resource.getMeta().getVersionId().getValue());
        } else {
            this.version = builder.version;
        }

        if (this.version < 1) {
            throw new IllegalStateException("version not set");
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(resourceTypeName);
        result.append("/");
        result.append(logicalId);
        result.append("/_history/");
        result.append(version);
        
        if (deleted) {
            result.append(" [deleted]");
        }
        return result.toString();
    }

    /**
     * Create a Builder for building instances of this class
     * @return
     */
    public static <T extends Resource> ResourceResult.Builder<T> builder() {
        return new ResourceResult.Builder<>();
    }

    /**
     * Convenience method to convert a Resource value to a ResourceResult,
     * which is useful for unit-tests
     *
     * @param <T>
     * @param resource with a valid meta field
     * @return
     */
    public static ResourceResult<? extends Resource> from(Resource resource) {
        return ResourceResult.builder()
                .resource(resource)
                .lastUpdated(resource.getMeta().getLastUpdated().getValue().toInstant())
                .version(Integer.parseInt(resource.getMeta().getVersionId().getValue()))
                .logicalId(resource.getId())
                .resourceTypeName(resource.getClass().getSimpleName())
                .build();
    }

    /**
     * Convenience function to convert a list of ResourceResults to a list of Resources
     * which are not null
     * @param resourceResults
     * @return
     */
    public static List<? extends Resource> toResourceList(List<ResourceResult<? extends Resource>> resourceResults) {
        return resourceResults.stream().filter(rr -> rr.getResource() != null).map(rr -> rr.getResource()).collect(Collectors.toList());
    }

    /**
     * Whether or not the resource is deleted
     * @return whether the resource is deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * The resource resulting from the interaction
     *
     * @return
     *     An immutable object of type {@link Resource}.
     */
    public T getResource() {
        return resource;
    }

    /**
     * @return the resourceTypeName
     */
    public String getResourceTypeName() {
        return resourceTypeName;
    }

    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @return the lastUpdated
     */
    public Instant getLastUpdated() {
        return lastUpdated;
    }

    // result builder
    public static class Builder<T extends Resource> {
        private T resource;
        private boolean deleted;
        private String resourceTypeName;
        private String logicalId;
        private int version;
        private Instant lastUpdated;

        /**
         * Whether or not the resource is deleted
         * 
         * @param flag
         * @return A reference to this Builder instance
         */
        public Builder<T> deleted(boolean flag) {
            this.deleted = flag;
            return this;
        }

        /**
         * The resulting resource from the interaction
         *
         * @param resource
         *     the resulting resource from the interaction; this may be null if the interaction was not successful
         *
         * @return
         *     A reference to this Builder instance
         */
        public Builder<T> resource(T resource) {
            this.resource = resource;
            return this;
        }

        /**
         * The lastUpdated time of the resource
         *
         * @param lastUpdated
         *     the lastUpdated timestamp representing the UTC modification time of the resource
         *
         * @return
         *     A reference to this Builder instance
         */
        public Builder<T> lastUpdated(Instant lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        /**
         * The type name of the resource which should be set when the resource
         * value itself is null
         * 
         * @param resourceTypeName
         *     The type name of the resource this result represents
         * @return
         *     A reference to this Builder instance
         */
        public Builder<T> resourceTypeName(String resourceTypeName) {
            this.resourceTypeName = resourceTypeName;
            return this;
        }

        /**
         * Sets the logicalId of the resource which should be set when the resource
         * value itself is null
         * 
         * @param logicalId
         *     The logicalId of this resource
         * @return
         *     A reference to this Builder instance
         */
        public Builder<T> logicalId(String logicalId) {
            this.logicalId = logicalId;
            return this;
        }

        /**
         * Sets the version of the resource which should be set when the resource
         * value itself is null
         * 
         * @param version
         *     The version of this resource
         * @return
         *     A reference to this Builder instance
         */
        public Builder<T> version(int version) {
            this.version = version;
            return this;
        }

        /**
         * Build the {@link SingleResourceResult}
         *
         * <p>Required fields:
         * <ul>
         * <li>success</li>
         * </ul>
         *
         * @return
         *     An immutable object of type {@link SingleResourceResult}
         */
        public ResourceResult<T> build() {
            return new ResourceResult<T>(this);
        }
    }
}