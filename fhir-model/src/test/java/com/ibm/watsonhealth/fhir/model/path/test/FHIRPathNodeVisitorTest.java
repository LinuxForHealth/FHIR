/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.test;

import static com.ibm.watsonhealth.fhir.model.type.String.string;
import static com.ibm.watsonhealth.fhir.model.type.Xhtml.xhtml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathBooleanValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathDateTimeValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathDecimalValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathElementNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathIntegerValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNodeVisitor;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathQuantityNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathResourceNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathStringValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTimeValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.NarrativeStatus;

public class FHIRPathNodeVisitorTest {
    private static final List<String> EXPECTED = Arrays.asList(
        "FHIRPathResourceNode: Patient", 
        "FHIRPathElementNode: Id", 
        "FHIRPathStringValue: e8459165-5f3e-48e3-8f8a-cde399cd652b", 
        "FHIRPathElementNode: Meta", 
        "FHIRPathElementNode: Id", 
        "FHIRPathStringValue: 1", 
        "FHIRPathElementNode: Instant", 
        "FHIRPathBooleanValue: 2019-08-20T20:09:30.841Z", 
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
        "FHIRPathBooleanValue: 1980-01-01"
    );
    
    @BeforeClass
    public void setUp() {
    }
    
    @Test
    public void testFHIRPathNodeVisitor() {
        Patient patient = buildPatient();
        FHIRPathTree tree = FHIRPathTree.tree(patient);
        FHIRPathNode root = tree.getRoot();
        List<String> actual = new ArrayList<>();
        root.accept(actual, new PrintingVisitor());
        Assert.assertEquals(actual, EXPECTED);
    }
    
    public static class PrintingVisitor implements FHIRPathNodeVisitor<List<String>> {
        private void visitChildren(List<String> param, FHIRPathNode node) {
            for (FHIRPathNode child : node.children()) {
                child.accept(param, this);
            }
        }

        @Override
        public void visit(List<String> param, FHIRPathBooleanValue value) {
            param.add("FHIRPathBooleanValue: " + value._boolean());
            visitChildren(param, value);
        }

        @Override
        public void visit(List<String> param, FHIRPathDateTimeValue value) {
            param.add("FHIRPathBooleanValue: " + value.dateTime());
            visitChildren(param, value);
        }

        @Override
        public void visit(List<String> param, FHIRPathDecimalValue value) {
            param.add("FHIRPathDecimalValue: " + value.decimal());
            visitChildren(param, value);
        }

        @Override
        public void visit(List<String> param, FHIRPathElementNode node) {
            param.add("FHIRPathElementNode: " + node.element().getClass().getSimpleName());
            visitChildren(param, node);
        }

        @Override
        public void visit(List<String> param, FHIRPathIntegerValue value) {
            param.add("FHIRPathIntegerValue: " + value.integer());
            visitChildren(param, value);
        }

        @Override
        public void visit(List<String> param, FHIRPathQuantityNode node) {
            param.add("FHIRPathQuantityNode: " + node.quantity());
            visitChildren(param, node);
        }

        @Override
        public void visit(List<String> param, FHIRPathResourceNode node) {
            param.add("FHIRPathResourceNode: " + node.resource().getClass().getSimpleName());
            visitChildren(param, node);
        }

        @Override
        public void visit(List<String> param, FHIRPathStringValue value) {
            param.add("FHIRPathStringValue: " + value.string());
            visitChildren(param, value);
        }

        @Override
        public void visit(List<String> param, FHIRPathTimeValue value) {
            param.add("FHIRPathTimeValue: " + value.time());
            visitChildren(param, value);
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
