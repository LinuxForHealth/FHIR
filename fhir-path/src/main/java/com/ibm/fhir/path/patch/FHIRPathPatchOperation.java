/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.patch;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Parameters.Parameter;

abstract class FHIRPathPatchOperation implements FHIRPatch {
    public static final String OPERATION = "operation";

    public static final String TYPE = "type";
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String INDEX = "index";
    public static final String SOURCE = "source";
    public static final String DESTINATION = "destination";

    /**
     * Convert the FHIRPathPatchOperation to a Parameters.Parameter element
     */
    public abstract Parameter toParameter();
}