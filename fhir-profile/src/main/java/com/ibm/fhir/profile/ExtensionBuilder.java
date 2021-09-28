/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.profile;

import static com.ibm.fhir.model.type.String.string;

import com.ibm.fhir.model.resource.StructureDefinition.Context;
import com.ibm.fhir.model.type.Extension;

/**
 * Specializes ProfileBuilder for making it more convenient to build extension definitions
 */
public class ExtensionBuilder extends ProfileBuilder {

    /**
     * Construct a ProfileBuilder with a fixed Extension.url and a type constraint on Extension.value[x]
     *
     * @param url
     * @param version
     * @param context
     * @param type
     */
    public ExtensionBuilder(String url, String version, Context context, String type) {
        super(Extension.class, url, version, context);
        fixed("Extension.url", string("http://ibm.com/fhir/StructureDefinition/favorite-team"));
        type("Extension.value[x]", ProfileBuilder.type(type));
    }

}
