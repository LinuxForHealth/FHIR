/*
 * (C) Copyright IBM Corp. 2019
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.model.path.visitor;

import com.ibm.fhir.model.path.FHIRPathBooleanValue;
import com.ibm.fhir.model.path.FHIRPathDateTimeValue;
import com.ibm.fhir.model.path.FHIRPathDecimalValue;
import com.ibm.fhir.model.path.FHIRPathElementNode;
import com.ibm.fhir.model.path.FHIRPathIntegerValue;
import com.ibm.fhir.model.path.FHIRPathQuantityNode;
import com.ibm.fhir.model.path.FHIRPathResourceNode;
import com.ibm.fhir.model.path.FHIRPathStringValue;
import com.ibm.fhir.model.path.FHIRPathTimeValue;
import com.ibm.fhir.model.path.FHIRPathTypeInfoNode;

public class FHIRPathVoidParameterNodeVisitorAdapter extends FHIRPathVoidParameterAbstractNodeVisitor {
    @Override
    protected void doVisit(FHIRPathBooleanValue value) {
        // do nothing
    }

    @Override
    protected void doVisit(FHIRPathDateTimeValue value) {
        // do nothing
    }

    @Override
    protected void doVisit(FHIRPathDecimalValue value) {
        // do nothing
    }

    @Override
    protected void doVisit(FHIRPathElementNode node) {
        // do nothing
    }

    @Override
    protected void doVisit(FHIRPathIntegerValue value) {
        // do nothing
    }

    @Override
    protected void doVisit(FHIRPathQuantityNode node) {
        // do nothing
    }

    @Override
    protected void doVisit(FHIRPathResourceNode node) {
        // do nothing
    }

    @Override
    protected void doVisit(FHIRPathStringValue value) {
        // do nothing
    }

    @Override
    protected void doVisit(FHIRPathTimeValue value) {
        // do nothing
    }

    @Override
    protected void doVisit(FHIRPathTypeInfoNode node) {
        // do nothing
    }
}