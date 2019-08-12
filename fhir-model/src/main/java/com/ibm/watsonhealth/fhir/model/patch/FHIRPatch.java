/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.patch;

import javax.json.JsonArray;
import javax.json.JsonPatch;

import com.ibm.watsonhealth.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.watsonhealth.fhir.model.resource.Resource;

public interface FHIRPatch {
    <T extends Resource> T apply(T resource) throws FHIRPatchException;
    
    default <T extends FHIRPatch> T as(Class<T> patchClass) {
        return patchClass.cast(this);
    }
    
    /**
     * Factory method that constructs a new FHIRPatch object 
     * from a JsonArray object
     * 
     * @param array the JsonArray object
     * @return
     *     the newly constructed FHIRPatch object
     */
    static FHIRPatch patch(JsonArray array) {
        return new FHIRJsonPatch(array);
    }
    
    /**
     * Factory method that constructs a new FHIRPatch object 
     * from a JsonPatch object
     * 
     * @param patch the JsonPatch object
     * @return
     *     the newly constructed FHIRPatch object
     */
    static FHIRPatch patch(JsonPatch patch) {
        return new FHIRJsonPatch(patch);
    }
}
