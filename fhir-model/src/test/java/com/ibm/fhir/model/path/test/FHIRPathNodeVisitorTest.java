/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Xhtml.xhtml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.path.FHIRPathBooleanValue;
import com.ibm.fhir.model.path.FHIRPathDateTimeValue;
import com.ibm.fhir.model.path.FHIRPathDecimalValue;
import com.ibm.fhir.model.path.FHIRPathElementNode;
import com.ibm.fhir.model.path.FHIRPathIntegerValue;
import com.ibm.fhir.model.path.FHIRPathQuantityValue;
import com.ibm.fhir.model.path.FHIRPathResourceNode;
import com.ibm.fhir.model.path.FHIRPathStringValue;
import com.ibm.fhir.model.path.FHIRPathTimeValue;
import com.ibm.fhir.model.path.FHIRPathTree;
import com.ibm.fhir.model.path.FHIRPathTypeInfoNode;
import com.ibm.fhir.model.path.visitor.FHIRPathDefaultNodeVisitor;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.code.NarrativeStatus;

public class FHIRPathNodeVisitorTest {
    private static final List<String> EXPECTED = Arrays.asList(
        "FHIRPathResourceNode: Patient", 
        "FHIRPathElementNode: Id", 
        "FHIRPathStringValue: e8459165-5f3e-48e3-8f8a-cde399cd652b", 
        "FHIRPathElementNode: Meta", 
        "FHIRPathElementNode: Id", 
        "FHIRPathStringValue: 1", 
        "FHIRPathElementNode: Instant", 
        "FHIRPathDateTimeValue: 2019-08-20T20:09:30.841Z", 
        "FHIRPathElementNode: Narrative", 
        "FHIRPathElementNode: NarrativeStatus", 
        "FHIRPathStringValue: generated", 
        "FHIRPathElementNode: Xhtml", 
        "FHIRPathStringValue: <div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>", 
        "FHIRPathElementNode: Boolean", 
        "FHIRPathBooleanValue: true", 
        "FHIRPathElementNode: HumanName", 
        "FHIRPathElementNode: String", 
        "FHIRPathStringValue: Doe", 
        "FHIRPathElementNode: String", 
        "FHIRPathStringValue: John", 
        "FHIRPathElementNode: Date", 
        "FHIRPathDateTimeValue: 1980-01-01"
    );
    
    @BeforeClass
    public void setUp() {
    }
    
    @Test
    public void testFHIRPathNodeVisitor() {
        Patient patient = buildPatient();
        FHIRPathTree tree = FHIRPathTree.tree(patient);        
        ListBuildingVisitor visitor = new ListBuildingVisitor();
        tree.getRoot().accept(visitor);
        Assert.assertEquals(visitor.getResult(), EXPECTED);
    }
    
    public static class ListBuildingVisitor extends FHIRPathDefaultNodeVisitor {
        private List<String> result = new ArrayList<>();
        
        public List<String> getResult() {
            return result;
        }
        
        @Override
        public void visit(FHIRPathBooleanValue value) {
            result.add("FHIRPathBooleanValue: " + value._boolean());
        }

        @Override
        public void visit(FHIRPathDateTimeValue value) {
            result.add("FHIRPathDateTimeValue: " + value.dateTime());
        }

        @Override
        public void visit(FHIRPathDecimalValue value) {
            result.add("FHIRPathDecimalValue: " + value.decimal());
        }

        @Override
        public void doVisit(FHIRPathElementNode node) {
            result.add("FHIRPathElementNode: " + node.element().getClass().getSimpleName());
        }

        @Override
        public void visit(FHIRPathIntegerValue value) {
            result.add("FHIRPathIntegerValue: " + value.integer());
        }

        @Override
        public void visit(FHIRPathQuantityValue value) {
            result.add("FHIRPathQuantityValue: " + value.toString());
        }

        @Override
        public void doVisit(FHIRPathResourceNode node) {
            result.add("FHIRPathResourceNode: " + node.resource().getClass().getSimpleName());
        }

        @Override
        public void visit(FHIRPathStringValue value) {
            result.add("FHIRPathStringValue: " + value.string());
        }

        @Override
        public void visit(FHIRPathTimeValue value) {
            result.add("FHIRPathTimeValue: " + value.time());
        }

        @Override
        public void visit(FHIRPathTypeInfoNode node) {
            result.add("FHIRPathTypeInfoNode: " + node.typeInfo());
        }
    }
    
    private Patient buildPatient() {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";
        
        Id id = Id.builder()
                .value("e8459165-5f3e-48e3-8f8a-cde399cd652b")
                .build();
        
        Meta meta = Meta.builder()
                .versionId(Id.of("1"))
                .lastUpdated(Instant.of("2019-08-20T20:09:30.841Z"))
                .build();
        
        HumanName name = HumanName.builder()
                .given(string("John"))
                .family(string("Doe"))
                .build();
        
        Narrative text = Narrative.builder()
                .status(NarrativeStatus.GENERATED)
                .div(xhtml(div))
                .build();
        
        return Patient.builder()
                .id(id)
                .meta(meta)
                .text(text)
                .active(Boolean.TRUE)
                .name(name)
                .birthDate(Date.of("1980-01-01"))
                .build();
    }
}