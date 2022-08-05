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
import org.linuxforhealth.fhir.path.FHIRPathQuantityNode;
import org.linuxforhealth.fhir.path.FHIRPathQuantityValue;
import org.linuxforhealth.fhir.path.FHIRPathResourceNode;
import org.linuxforhealth.fhir.path.FHIRPathStringValue;
import org.linuxforhealth.fhir.path.FHIRPathTimeValue;
import org.linuxforhealth.fhir.path.FHIRPathTypeInfoNode;

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