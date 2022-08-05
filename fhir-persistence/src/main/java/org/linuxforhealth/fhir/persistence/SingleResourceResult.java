/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence;

import java.time.Instant;

import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.ValidationSupport;

/**
 * A Result wrapper for FHIR interactions that return a single resource.
 * Instances are immutable and can be constructed via {@code new SingleResourceResult.Builder<T>()}.
 */
public class SingleResourceResult<T extends Resource> {
    // The resourceResult will only be set if success == true
    // Note: if you add/modify any members here, be sure to also update the #replace(T newResource) method
    private final ResourceResult<T> resourceResult;
    private final boolean success;
    private final OperationOutcome outcome;
    private final InteractionStatus interactionStatus;

    // The current version of the resource returned by the database if we hit IfNoneMatch
    final Integer ifNoneMatchVersion;

    private SingleResourceResult(Builder<T> builder) {
        success = ValidationSupport.requireNonNull(builder.success, "success");

        if (success) {
            resourceResult = builder.resourceResultBuilder.build();
        } else {
            resourceResult = null;
        }
        outcome = builder.outcome;
        interactionStatus = builder.interactionStatus;
        ifNoneMatchVersion = builder.ifNoneMatchVersion;

        if (!success && (outcome == null || outcome.getIssue().isEmpty())) {
            throw new IllegalStateException("Failed interaction results must include an OperationOutcome with one or more issue.");
        }

        if (interactionStatus == null) {
            throw new IllegalStateException("All interaction results must include a valid InteractionStatus");
        }

        if (interactionStatus == InteractionStatus.IF_NONE_MATCH_EXISTED && ifNoneMatchVersion == null) {
            throw new IllegalStateException("Must specify ifNoneMatchVersion when IF_NON_MATCH_EXISTED is true");
        }
    }

    /**
     * Whether or not the interaction was successful
     *
     * @return
     *     whether the interaction was successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Whether or not the resource is deleted
     * @return whether the resource is deleted
     */
    public boolean isDeleted() {
        // we can only say if the resource is deleted if we were able to read the record
        // (in which case resourceResult will be non-null)
        return resourceResult != null && resourceResult.isDeleted();
    }

    /**
     * Get the lastUpdated value for the resource we just read
     * @return
     */
    public Instant getLastUpdated() {
        if (resourceResult != null) {
            return resourceResult.getLastUpdated();
        } else {
            return null;
        }
    }

    /**
     * The resource resulting from the interaction
     *
     * @return
     *     An immutable object of type {@link Resource}.
     */
    public T getResource() {
        if (this.success) {
            return resourceResult.getResource();
        } else {
            return null;
        }
    }

    /**
     * Getter for the interaction status
     * @return
     */
    public InteractionStatus getStatus() {
        return this.interactionStatus;
    }

    /**
     * Getter for the ifNoneMatchVersion value
     * @return
     */
    public Integer getIfNoneMatchVersion() {
        return this.ifNoneMatchVersion;
    }

    /**
     * An OperationOutcome that represents the outcome of the interaction
     *
     * @return
     *     An immutable object of type {@link OperationOutcome}
     */
    public OperationOutcome getOutcome() {
        return outcome;
    }

    /**
     * @return the type name of the resource
     */
    public String getResourceTypeName() {
        if (!success) {
            throw new IllegalStateException("getResourceTypeName called for a resource which doesn't exist");
        }
        return resourceResult.getResourceTypeName();
    }

    /**
     * @return the logicalId of the resource
     */
    public String getLogicalId() {
        if (!success) {
            throw new IllegalStateException("getLogicalId called for a resource which doesn't exist");
        }
        return resourceResult.getLogicalId();
    }

    /**
     * @return the version of the resource, or 0 if resourceResult is null
     */
    public int getVersion() {
        return resourceResult != null ? resourceResult.getVersion() : 0;
    }

    /**
     * Obtain a copy of this {@link SingleResourceResult} with the resource value replaced
     * with the given newResource parameter. As an optimization, if the newResource equals
     * the current value, this object is returned.
     * @implNote this function needs its own generic so that it can be called with
     * a plain Resource value. Unfortunately this means we need to force a type cast
     * in order to return 'this' as an optimization for when the resource doesn't
     * need to be changed (which is the most common case).
     * @param newResource
     * @return
     */
    @SuppressWarnings("unchecked")
    public <X extends Resource> SingleResourceResult<X> replace(X newResource) {
        // we can't allow an interceptor to change the success status
        if (newResource == null || newResource == getResource() || !isSuccess()) {
            // no need to change the resource, so we can simply return this
            return (SingleResourceResult<X>)this;
        }

        // Build a new instance copying all the values from this but replacing the resource
        return new Builder<X>()
                .success(isSuccess()) // will be true
                .interactionStatus(this.interactionStatus)
                .ifNoneMatchVersion(getIfNoneMatchVersion())
                .resource(newResource) // the updated resource value
                .outcome(getOutcome())
                .deleted(isDeleted())
                .version(getVersion())
                .logicalId(getLogicalId())
                .resourceTypeName(getResourceTypeName())
                .build()
                ;
    }

    // result builder
    public static class Builder<T extends Resource> {
        private boolean success;
        private OperationOutcome outcome;
        private InteractionStatus interactionStatus;
        private Integer ifNoneMatchVersion;
        private final ResourceResult.Builder<T> resourceResultBuilder = new ResourceResult.Builder<>();

        /**
         * Whether or not the interaction was successful
         *
         * <p>This field is required.
         *
         * @param success
         *     whether the interaction was successful
         *
         * @return
         *     A reference to this Builder instance
         */
        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        /**
         * Sets the interaction status
         *
         * @param status
         * @return a reference to this Builder instance
         */
        public Builder<T> interactionStatus(InteractionStatus interactionStatus) {
            this.interactionStatus = interactionStatus;
            return this;
        }

        /**
         * Sets the version we found hitting IfNoneMatch. Null in other cases.
         *
         * @param versionId
         * @return
         */
        public Builder<T> ifNoneMatchVersion(Integer versionId) {
            this.ifNoneMatchVersion = versionId;
            return this;
        }

        /**
         * Whether or not the resource is deleted
         *
         * @param flag
         * @return A reference to this Builder instance
         */
        public Builder<T> deleted(boolean flag) {
            resourceResultBuilder.deleted(flag);
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
            resourceResultBuilder.resource(resource);
            return this;
        }

        /**
         * An OperationOutcome that represents the outcome of the interaction
         *
         * <p>This field is required when the interaction is not successful
         *
         * @param outcome
         *     the outcome of the interaction
         *
         * @return
         *     A reference to this Builder instance
         */
        public Builder<T> outcome(OperationOutcome outcome) {
            this.outcome = outcome;
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
            resourceResultBuilder.resourceTypeName(resourceTypeName);
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
            resourceResultBuilder.logicalId(logicalId);
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
            resourceResultBuilder.version(version);
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
        public SingleResourceResult<T> build() {
            return new SingleResourceResult<T>(this);
        }
    }
}