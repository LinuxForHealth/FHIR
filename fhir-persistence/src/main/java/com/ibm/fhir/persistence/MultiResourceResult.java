/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ValidationSupport;

/**
 * A Result wrapper for FHIR interactions that can return multiple resources.
 * Instances are immutable and can be constructed via {@code new MultiResourceResult.Builder<T>()}.
 */
public class MultiResourceResult<T extends Resource> {
    @Required
    final boolean success;
    final List<T> resource;
    final OperationOutcome outcome;
    
    private MultiResourceResult(Builder<T> builder) {
        success = ValidationSupport.requireNonNull(builder.success, "success");
        resource = Collections.unmodifiableList(builder.resource);
        outcome = builder.outcome;
        if (!success && (outcome == null || outcome.getIssue().isEmpty())) {
            throw new IllegalStateException("Failed interaction results must include an OperationOutcome with one or more issue.");
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
     * The resources returned from the interaction
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Resource}.
     */
    public List<T> getResource() {
        return resource;
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
    
    public static <T extends Resource> Builder<T> builder(Class<T> clazz) {
        return new Builder<T>();
    }
    
    // result builder
    public static class Builder<T extends Resource> {
        boolean success;
        List<T> resource = new ArrayList<>();
        OperationOutcome outcome;
        
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
         * The return resources from the interaction
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param resource
         *     the resources to return from the interaction; this may be empty if there are no results
         * 
         * @return
         *     A reference to this Builder instance
         */
        @SafeVarargs
        public final Builder<T> resource(T... resource) {
            for (T value : resource) {
                this.resource.add(value);
            }
            return this;
        }
        
        /**
         * The return resources from the interaction
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param resource
         *     the resources to return from the interaction; this may be empty if there are no results
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder<T> resource(Collection<T> resource) {
            this.resource = new ArrayList<>(resource);
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
         * Build the {@link MultiResourceResult}
         * 
         * <p>Required fields:
         * <ul>
         * <li>success</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MultiResourceResult}
         */
        public MultiResourceResult<T> build() {
            return new MultiResourceResult<T>(this);
        }
    }
}