/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.visitor;

import org.linuxforhealth.fhir.path.FHIRPathBooleanValue;
import org.linuxforhealth.fhir.path.FHIRPathDateTimeValue;
import org.linuxforhealth.fhir.path.FHIRPathDateValue;
import org.linuxforhealth.fhir.path.FHIRPathDecimalValue;
import org.linuxforhealth.fhir.path.FHIRPathElementNode;
import org.linuxforhealth.fhir.path.FHIRPathIntegerValue;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathQuantityNode;
import org.linuxforhealth.fhir.path.FHIRPathQuantityValue;
import org.linuxforhealth.fhir.path.FHIRPathResourceNode;
import org.linuxforhealth.fhir.path.FHIRPathStringValue;
import org.linuxforhealth.fhir.path.FHIRPathTimeValue;
import org.linuxforhealth.fhir.path.FHIRPathTypeInfoNode;

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
    public void visit(FHIRPathQuantityNode node) {
        visit((FHIRPathElementNode) node);
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