/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.service.test;

import org.linuxforhealth.fhir.model.resource.ValueSet;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.term.util.ValueSetSupport;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ValueSetSupportTest {
    
    private static final String TEST_VALUE_SET_ID = "7.8.9";
    private static final String TEST_VALUE_SET_URL = "http://localhost/fhir/ValueSet/7.8.9";
    
    /**
     * Test ValueSetSupport.isExpanded with a valid expanded value set.
     */
    @Test
    public void testExpandedValueSet() {
        ValueSet valueSet = ValueSet.builder().id(TEST_VALUE_SET_ID).status(PublicationStatus.ACTIVE).url(Uri.of(TEST_VALUE_SET_URL)).expansion(ValueSet.Expansion.builder().timestamp(DateTime.now()).build()).build();
        boolean isExpanded = ValueSetSupport.isExpanded(valueSet);
        Assert.assertTrue(isExpanded, "ValueSet isExpanded validation failed.");
    }
    
    /**
     * Test ValueSetSupport.isExpanded with a partially expanded value set having ValueSet.expansion of .../valueset-toocostly.
     */
    @Test
    public void testPartiallyExpandedValueSet1() {
        Extension extension = Extension.builder()
                                .url("http://hl7.org/fhir/StructureDefinition/valueset-toocostly")
                                .value(org.linuxforhealth.fhir.model.type.Boolean.of("true"))
                                .build();
        ValueSet.Expansion.Builder expansionBuilder = ValueSet.Expansion.builder().timestamp(DateTime.now()).extension(extension);
        ValueSet valueSet = ValueSet.builder().id(TEST_VALUE_SET_ID).status(PublicationStatus.ACTIVE).url(Uri.of(TEST_VALUE_SET_URL)).expansion(expansionBuilder.build()).build();
        boolean isExpanded = ValueSetSupport.isExpanded(valueSet);
        Assert.assertFalse(isExpanded, "Partially expanded ValueSet isExpanded validation failed for value set having ValueSet.expansion of .../valueset-toocostly.");
    }
    
    /**
     * Test ValueSetSupport.isExpanded with a partially expanded value set having ValueSet.expansion of .../valueset-unclosed.
     */
    @Test
    public void testPartiallyExpandedValueSet2() {
        Extension extension = Extension.builder()
                                .url("http://hl7.org/fhir/StructureDefinition/valueset-unclosed")
                                .value(org.linuxforhealth.fhir.model.type.Boolean.of("true"))
                                .build();
        ValueSet.Expansion.Builder expansionBuilder = ValueSet.Expansion.builder().timestamp(DateTime.now()).extension(extension);
        ValueSet valueSet = ValueSet.builder().id(TEST_VALUE_SET_ID).status(PublicationStatus.ACTIVE).url(Uri.of(TEST_VALUE_SET_URL)).expansion(expansionBuilder.build()).build();
        boolean isExpanded = ValueSetSupport.isExpanded(valueSet);
        Assert.assertFalse(isExpanded, "Partially expanded ValueSet isExpanded validation failed for value set having ValueSet.expansion of .../valueset-unclosed.");
    }
    
    /**
     * Test ValueSetSupport.isExpanded with a partially expanded value set having ValueSet.expansion.parameter.name=limitedExpansion
     */
    @Test
    public void testPartiallyExpandedValueSet3() {
        
        ValueSet.Expansion.Parameter.Builder parameterBuilder = ValueSet.Expansion.Parameter.builder().name("limitedExpansion").value(org.linuxforhealth.fhir.model.type.Boolean.of("true"));
        ValueSet.Expansion.Builder expansionBuilder = ValueSet.Expansion.builder().timestamp(DateTime.now()).parameter(parameterBuilder.build());
        ValueSet valueSet = ValueSet.builder().id(TEST_VALUE_SET_ID).status(PublicationStatus.ACTIVE).url(Uri.of(TEST_VALUE_SET_URL)).expansion(expansionBuilder.build()).build();
        boolean isExpanded = ValueSetSupport.isExpanded(valueSet);
        Assert.assertFalse(isExpanded, "Partially expanded ValueSet isExpanded validation failed for value set having ValueSet.expansion.parameter.name=limitedExpansion.");
    }
    
    
}
