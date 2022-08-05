/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.spec;

import java.util.HashMap;
import java.util.Map;

import org.linuxforhealth.fhir.config.FHIRConfigProvider;
import org.linuxforhealth.fhir.config.PropertyGroup;

/**
 * Allow unit-tests to provide configuration information to lower layers
 * without needed to have fhir-server-config.json files
 */
public class TestFHIRConfigProvider implements FHIRConfigProvider {
    // forward requests we don't care about to this delegate
    private final FHIRConfigProvider delegate;

    // Map of custom PropertyGroups addressed by their full path name
    private final Map<String,PropertyGroup> propertyGroupMap = new HashMap<>();

    /**
     * Public constructor
     * @param delegate
     */
    public TestFHIRConfigProvider(FHIRConfigProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public PropertyGroup getPropertyGroup(String pgName) {
        
        // Try the map first to see if a property group has been
        // overridden (the reason why we're here). If not, then
        // simply delegate
        PropertyGroup result = propertyGroupMap.get(pgName);
        if (result == null && delegate != null) {
            result = delegate.getPropertyGroup(pgName);
        }
        
        return result;
    }

    /**
     * Add a PropertyGroup using the given pgName as a key. This {@link PropertyGroup}
     * will be returned by {@link #getPropertyGroup(String)} based on a perfect
     * match of the pgName. Useful for simple injection of configuration. This does not
     * support any hierarchical capability of the actual FHIRConfiguration
     * implementation (by design).
     * @param pgName the full path key used to find this PropertyGroup
     * @param pg the PropertyGroup value
     */
    public void addPropertyGroup(String pgName, PropertyGroup pg) {
        this.propertyGroupMap.put(pgName, pg);
    }
}