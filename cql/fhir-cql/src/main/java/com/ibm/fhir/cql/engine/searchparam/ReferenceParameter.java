/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.cql.engine.searchparam;

import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.search.SearchConstants.Modifier;

public class ReferenceParameter extends BaseQueryParameter<ReferenceParameter> {

    private ResourceTypeCode resourceTypeModifier;
    private String chainedProperty;
    private String value;

    public ReferenceParameter() {
        super();
    }

    public ReferenceParameter(String value) {
        this.value = value;
    }

    public ReferenceParameter(ResourceTypeCode resourceTypeModifier, String value) {
        setResourceTypeModifier(resourceTypeModifier);
        setValue(value);
    }

    public ReferenceParameter(ResourceTypeCode resourceTypeModifier, String chainedProperty, String value) {
        setResourceTypeModifier(resourceTypeModifier);
        setValue(value);
    }

    public ReferenceParameter(Modifier modifier, String value) {
        setModifier(modifier);
        setValue(value);
    }

    public ResourceTypeCode getResourceTypeModifier() {
        return resourceTypeModifier;
    }

    public ReferenceParameter setResourceTypeModifier(ResourceTypeCode resourceTypeModifier) {
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
}
