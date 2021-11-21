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
     * Construct a ProfileBuilder with a fixed Extension.url and a type constraint on Extension.value[x].
     *
     * @param url
     * @param version
     * @param type
     */
    public ExtensionBuilder(String url, String version, String type) {
        super(Extension.class, url, version);
        fixed("Extension.url", string(url));
        type("Extension.value[x]", ProfileBuilder.type(type));
    }

    /**
     * Add context objects to the extension definition.
     *
     * @param context
     *     where the extension can be used in instances
     * @return
     *     this extension builder
     */
    public ExtensionBuilder context(Context... context) {
        for (Context c : context) {
            this.context.add(c);
        }
        return this;
    }
}
