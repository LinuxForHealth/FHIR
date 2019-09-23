/*
 * (C) Copyright IBM Corp. 2019
 * 
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

public interface FHIRPathNodeVisitor<T> {
    void visit(T param, FHIRPathBooleanValue value);
    void visit(T param, FHIRPathDateTimeValue value);
    void visit(T param, FHIRPathDecimalValue value);
    void visit(T param, FHIRPathElementNode node);
    void visit(T param, FHIRPathIntegerValue value);
    void visit(T param, FHIRPathQuantityNode node);
    void visit(T param, FHIRPathResourceNode node);
    void visit(T param, FHIRPathStringValue value);
    void visit(T param, FHIRPathTimeValue value);
    void visit(T param, FHIRPathTypeInfoNode node);
}