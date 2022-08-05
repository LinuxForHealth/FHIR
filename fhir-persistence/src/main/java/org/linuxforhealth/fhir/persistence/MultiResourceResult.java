/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.ValidationSupport;

/**
 * A Result wrapper for FHIR interactions that can return multiple resources.
 * Instances are immutable and can be constructed via {@code new MultiResourceResult.Builder<T>()}.
 */
public class MultiResourceResult {
    @Required
    final boolean success;
    final List<ResourceResult<? extends Resource>> resourceResults;
    final OperationOutcome outcome;
    
    private MultiResourceResult(Builder builder) {
        success = ValidationSupport.requireNonNull(builder.success, "success");
        resourceResults = Collections.unmodifiableList(builder.resourceResults);
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
     * The resource results returned from the interaction
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ResourceResult<T>}
     */
    public List<ResourceResult<? extends Resource>> getResourceResults() {
        return this.resourceResults;
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
    
    public static Builder builder() {
        return new Builder();
    }
    
    // result builder
    public static class Builder {
        boolean success;
        final List<ResourceResult<? extends Resource>> resourceResults = new ArrayList<>();
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
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }
        

        /**
         * Add the resource results to the resourceResults list
         * @param resourceResultsParam
         * @return
         */
        @SafeVarargs
        public final Builder resourceResult(ResourceResult<? extends Resource>... resourceResultsParam) {
            for (ResourceResult<? extends Resource> value : resourceResultsParam) {
                this.resourceResults.add(value);
            }
            return this;
        }

        /**
         * Add the resource result list to resource list owned by this
         * @param resourceResultsParam
         * @return
         */
        public final Builder addResourceResults(List<ResourceResult<? extends Resource>> resourceResultsList) {
            if (resourceResultsList != null) {
                this.resourceResults.addAll(resourceResultsList);
            }
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
        public Builder outcome(OperationOutcome outcome) {
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
        public MultiResourceResult build() {
            return new MultiResourceResult(this);
        }
    }
}