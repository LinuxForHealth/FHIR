/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.visitor;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathBooleanValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathDateTimeValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathDecimalValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathElementNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathIntegerValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathQuantityNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathResourceNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathStringValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTimeValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTypeInfoNode;

public abstract class FHIRPathAbstractNodeVisitor<T> implements FHIRPathNodeVisitor<T> {
    // called by template methods
    protected abstract void doVisit(T param, FHIRPathBooleanValue value);
    protected abstract void doVisit(T param, FHIRPathDateTimeValue value);
    protected abstract void doVisit(T param, FHIRPathDecimalValue value);
    protected abstract void doVisit(T param, FHIRPathElementNode node);
    protected abstract void doVisit(T param, FHIRPathIntegerValue value);
    protected abstract void doVisit(T param, FHIRPathQuantityNode node);
    protected abstract void doVisit(T param, FHIRPathResourceNode node);
    protected abstract void doVisit(T param, FHIRPathStringValue value);
    protected abstract void doVisit(T param, FHIRPathTimeValue value);
    protected abstract void doVisit(T param, FHIRPathTypeInfoNode node);
    
    @Override
    public final void visit(T param, FHIRPathBooleanValue value) {
        doVisit(param, value);
    }

    @Override
    public final void visit(T param, FHIRPathDateTimeValue value) {
        doVisit(param, value);
    }
    
    @Override
    public final void visit(T param, FHIRPathDecimalValue value) {
        doVisit(param, value);
    }

    @Override
    public final void visit(T param, FHIRPathElementNode node) {
        doVisit(param, node);
        visitChildren(param, node);        
    }

    @Override
    public final void visit(T param, FHIRPathIntegerValue value) {
        doVisit(param, value);
    }

    @Override
    public final void visit(T param, FHIRPathQuantityNode node) {
        doVisit(param, node);
        visitChildren(param, node);        
    }

    @Override
    public final void visit(T param, FHIRPathResourceNode node) {
        doVisit(param, node);
        visitChildren(param, node);        
    }

    @Override
    public final void visit(T param, FHIRPathStringValue value) {
        doVisit(param, value);
    }

    @Override
    public final void visit(T param, FHIRPathTimeValue value) {
        doVisit(param, value);
    }
    
    @Override
    public final void visit(T param, FHIRPathTypeInfoNode node) {
        doVisit(param, node);
    }

    private void visitChildren(T param, FHIRPathNode node) {
        for (FHIRPathNode child : node.children()) {
            child.accept(param, this);
        }
    }
}