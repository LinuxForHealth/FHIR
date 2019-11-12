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
import com.ibm.fhir.model.path.FHIRPathQuantityValue;
import com.ibm.fhir.model.path.FHIRPathResourceNode;
import com.ibm.fhir.model.path.FHIRPathStringValue;
import com.ibm.fhir.model.path.FHIRPathTimeValue;
import com.ibm.fhir.model.path.FHIRPathTypeInfoNode;

public interface FHIRPathNodeVisitor {
    void visit(FHIRPathBooleanValue value);
    void visit(FHIRPathDateValue value);
    void visit(FHIRPathDateTimeValue value);
    void visit(FHIRPathDecimalValue value);
    void visit(FHIRPathElementNode node);
    void visit(FHIRPathIntegerValue value);
    void visit(FHIRPathQuantityValue value);
    void visit(FHIRPathResourceNode node);
    void visit(FHIRPathStringValue value);
    void visit(FHIRPathTimeValue value);
    void visit(FHIRPathTypeInfoNode node);
}