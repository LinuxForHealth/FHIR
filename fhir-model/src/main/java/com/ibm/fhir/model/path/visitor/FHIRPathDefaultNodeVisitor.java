/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path.visitor;

import com.ibm.fhir.model.path.FHIRPathBooleanValue;
import com.ibm.fhir.model.path.FHIRPathDateTimeValue;
import com.ibm.fhir.model.path.FHIRPathDateValue;
import com.ibm.fhir.model.path.FHIRPathDecimalValue;
import com.ibm.fhir.model.path.FHIRPathElementNode;
import com.ibm.fhir.model.path.FHIRPathIntegerValue;
import com.ibm.fhir.model.path.FHIRPathNode;
import com.ibm.fhir.model.path.FHIRPathQuantityValue;
import com.ibm.fhir.model.path.FHIRPathResourceNode;
import com.ibm.fhir.model.path.FHIRPathStringValue;
import com.ibm.fhir.model.path.FHIRPathTimeValue;
import com.ibm.fhir.model.path.FHIRPathTypeInfoNode;

public class FHIRPathDefaultNodeVisitor implements FHIRPathNodeVisitor {
    protected void doVisit(FHIRPathResourceNode node) {
        // do nothing
    }
    
    protected void doVisit(FHIRPathElementNode node) {
        // do nothing
    }
    
    protected void visitChildren(FHIRPathNode node) {
        for (FHIRPathNode child : node.children()) {
            child.accept(this);
        }
    }
    
    @Override
    public void visit(FHIRPathBooleanValue value) {
        // do nothing
    }

    @Override
    public void visit(FHIRPathDateValue value) {
        // do nothing
    }

    @Override
    public void visit(FHIRPathDateTimeValue value) {
        // do nothing
    }

    @Override
    public void visit(FHIRPathDecimalValue value) {
        // do nothing
    }

    @Override
    public final void visit(FHIRPathElementNode node) {
        doVisit(node);
        visitChildren(node);
    }

    @Override
    public void visit(FHIRPathIntegerValue value) {
        // do nothing
    }

    @Override
    public void visit(FHIRPathQuantityValue value) {
        // do nothing
    }

    @Override
    public final void visit(FHIRPathResourceNode node) {
        doVisit(node);
        visitChildren(node);
    }

    @Override
    public void visit(FHIRPathStringValue value) {
        // do nothing
    }

    @Override
    public void visit(FHIRPathTimeValue value) {
        // do nothing
    }

    @Override
    public void visit(FHIRPathTypeInfoNode node) {
        // do nothing
    }
}