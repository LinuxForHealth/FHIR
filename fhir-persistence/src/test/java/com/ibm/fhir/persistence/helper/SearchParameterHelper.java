/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.helper;

import static com.ibm.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.SearchParamType;

/**
 * Helper class to create {@link SearchParameter} model objects
 * 

 *
 */
public class SearchParameterHelper {

    public static final String dummyUri = "http://test.ibm.com/dummy";
    public static final String description = "test description";
    public static final String code = "test-code";

    private SearchParameterHelper() {
        // No Op
    }

    public static SearchParameter makeTestParameter(String name) {
        List<ResourceTypeCode> base = new ArrayList<>();
        base.add(ResourceTypeCode.PATIENT);

        return SearchParameter.builder()
                              .url(Uri.of(dummyUri)).name(string(name))
                              .status(PublicationStatus.ACTIVE)
                              .description(Markdown.of(description))
                              .code(Code.of(code))
                              .base(base)
                              .type(SearchParamType.STRING)
                              .build();

    }

    /**
     * Create a {@link SimpleQuantity} model object with the given values
     *
     * @param code
     * @param unit
     * @param system
     * @param value
     * @return
     */
    public static SimpleQuantity simpleQuantity(String code, String unit, String system, Number value) {
        return SimpleQuantity.builder().code(Code.of(code)).unit(com.ibm.fhir.model.type.String.of(unit)).system(Uri.of(system)).value(Decimal.of(value)).build();
    }

    /**
     * Create a range with a high limit but no low limit
     * 
     * @param code
     * @param unit
     * @param system
     * @param value
     * @return
     */
    public static Range range(String code, String unit, String system, Number value) {
        return Range.builder().high(simpleQuantity(code, unit, system, value)).build();
    }
}
