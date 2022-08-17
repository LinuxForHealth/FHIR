/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.helper;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.List;

import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Decimal;
import org.linuxforhealth.fhir.model.type.Markdown;
import org.linuxforhealth.fhir.model.type.Range;
import org.linuxforhealth.fhir.model.type.SimpleQuantity;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.model.type.code.ResourceTypeCode;
import org.linuxforhealth.fhir.model.type.code.SearchParamType;

/**
 * Helper class to create {@link SearchParameter} model objects
 */
public class SearchParameterHelper {

    public static final String dummyUri = "http://example.com/dummy";
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
        return SimpleQuantity.builder().code(Code.of(code)).unit(org.linuxforhealth.fhir.model.type.String.of(unit)).system(Uri.of(system)).value(Decimal.of(value)).build();
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
