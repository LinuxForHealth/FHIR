/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.visitor;

import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathDateTimeValue;
import com.ibm.fhir.path.FHIRPathDateValue;
import com.ibm.fhir.path.FHIRPathDecimalValue;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathIntegerValue;
import com.ibm.fhir.path.FHIRPathQuantityNode;
import com.ibm.fhir.path.FHIRPathQuantityValue;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.FHIRPathTimeValue;
import com.ibm.fhir.path.FHIRPathTypeInfoNode;

public interface FHIRPathNodeVisitor {
    void visit(FHIRPathBooleanValue value);
    void visit(FHIRPathDateValue value);
    void visit(FHIRPathDateTimeValue value);
    void visit(FHIRPathDecimalValue value);
    void visit(FHIRPathElementNode node);
    void visit(FHIRPathIntegerValue value);
    void visit(FHIRPathQuantityNode node);
    void visit(FHIRPathQuantityValue value);
    void visit(FHIRPathResourceNode node);
    void visit(FHIRPathStringValue value);
    void visit(FHIRPathTimeValue value);
    void visit(FHIRPathTypeInfoNode node);
}