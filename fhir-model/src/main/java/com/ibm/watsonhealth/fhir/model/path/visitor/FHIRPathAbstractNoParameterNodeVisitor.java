/*
 * (C) Copyright IBM Corp. 2019
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watsonhealth.fhir.model.path.visitor;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathBooleanValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathDateTimeValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathDecimalValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathElementNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathIntegerValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathQuantityNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathResourceNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathStringValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTimeValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTypeInfoNode;

public abstract class FHIRPathAbstractNoParameterNodeVisitor extends FHIRPathAbstractNodeVisitor<Void> {
    // called by template methods
    protected abstract void doVisit(FHIRPathBooleanValue value);
    protected abstract void doVisit(FHIRPathDateTimeValue value);
    protected abstract void doVisit(FHIRPathDecimalValue value);
    protected abstract void doVisit(FHIRPathElementNode node);
    protected abstract void doVisit(FHIRPathIntegerValue value);
    protected abstract void doVisit(FHIRPathQuantityNode node);
    protected abstract void doVisit(FHIRPathResourceNode node);
    protected abstract void doVisit(FHIRPathStringValue value);
    protected abstract void doVisit(FHIRPathTimeValue value);
    protected abstract void doVisit(FHIRPathTypeInfoNode node);

    @Override
    protected final void doVisit(Void param, FHIRPathBooleanValue value) {
        doVisit(value);
    }

    @Override
    protected final void doVisit(Void param, FHIRPathDateTimeValue value) {
        doVisit(value);
    }

    @Override
    protected final void doVisit(Void param, FHIRPathDecimalValue value) {
        doVisit(value);
    }

    @Override
    protected final void doVisit(Void param, FHIRPathElementNode node) {
        doVisit(node);
    }

    @Override
    protected final void doVisit(Void param, FHIRPathIntegerValue value) {
        doVisit(value);
    }

    @Override
    protected final void doVisit(Void param, FHIRPathQuantityNode node) {
        doVisit(node);
    }

    @Override
    protected final void doVisit(Void param, FHIRPathResourceNode node) {
        doVisit(node);
    }

    @Override
    protected final void doVisit(Void param, FHIRPathStringValue value) {
        doVisit(value);
    }

    @Override
    protected final void doVisit(Void param, FHIRPathTimeValue value) {
        doVisit(value);
    }
    
    @Override
    protected final void doVisit(Void param, FHIRPathTypeInfoNode node) {
        doVisit(node);
    }
}