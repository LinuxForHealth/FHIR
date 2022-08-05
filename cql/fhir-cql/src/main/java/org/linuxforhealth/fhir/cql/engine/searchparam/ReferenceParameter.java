/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.cql.engine.searchparam;

import org.linuxforhealth.fhir.core.ResourceType;
import org.linuxforhealth.fhir.search.SearchConstants.Modifier;

public class ReferenceParameter extends BaseQueryParameter<ReferenceParameter> {

    private ResourceType resourceTypeModifier;

    private String chainedProperty;
    private String value;

    public ReferenceParameter() {
        super();
    }

    public ReferenceParameter(String value) {
        this.value = value;
    }

    public ReferenceParameter(ResourceType resourceTypeModifier, String value) {
        setResourceTypeModifier(resourceTypeModifier);
        setValue(value);
    }

    public ReferenceParameter(ResourceType resourceTypeModifier, String chainedProperty, String value) {
        setResourceTypeModifier(resourceTypeModifier);
        setValue(value);
    }

    public ReferenceParameter(Modifier modifier, String value) {
        setModifier(modifier);
        setValue(value);
    }

    public ResourceType getResourceTypeModifier() {
        return resourceTypeModifier;
    }

    public ReferenceParameter setResourceTypeModifier(ResourceType resourceTypeModifier) {
        this.resourceTypeModifier = resourceTypeModifier;
        return this;
    }

    public String getChainedProperty() {
        return chainedProperty;
    }

    public ReferenceParameter setChainedProperty(String chainedProperty) {
        this.chainedProperty = chainedProperty;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ReferenceParameter setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String getParameterValue() {
        return getValue();
    }

    @Override
    public String toString() {
        String rtm = null;
        if (resourceTypeModifier != null) {
            rtm = resourceTypeModifier.value();
        }
        return "ReferenceParameter [resourceTypeModifier=" + rtm + ", chainedProperty=" + chainedProperty + ", value=" + value + "]";
    }
}
