/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.registry;

import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;

public class ValueSetRegistryResource extends FHIRRegistryResource {
    private static final Logger log = Logger.getLogger(ValueSetRegistryResource.class.getName());

    protected final ValueSet valueSet;

    private ValueSetRegistryResource(String id, String url, Version version, ValueSet valueSet) {
        super(ValueSet.class, id, url, version, null, null);
        this.valueSet = valueSet;
    }

    @Override
    public Resource getResource() {
        return valueSet;
    }

    public static ValueSetRegistryResource from(ValueSet valueSet) {
        String id = valueSet.getId();
        String url = (valueSet.getUrl() != null) ? valueSet.getUrl().getValue() : null;
        String version = (valueSet.getVersion() != null) ? valueSet.getVersion().getValue() : null;
        if (url == null) {
            log.warning(String.format("Could not create ValueSetRegistryResource from ValueSet with: id: %s, url: %s, and version: %s", id, url, version));
            return null;
        }
        return new ValueSetRegistryResource(id, url, (version != null) ? Version.from(version) : FHIRRegistryResource.NO_VERSION, valueSet);
    }
}
