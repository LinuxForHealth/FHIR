/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test.tools;

import static com.ibm.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.model.type.code.XPathUsageType;

/**
 * 
 * @author pbastide
 *
 */
public class ExtensionSearchTool {

    /*
     * used in test to make the compiled code accessible.
     */
    static {
        System.setProperty("javax.xml.accessExternalSchema", "file");
    }

    /**
     * @param args
     * @throws FHIRGeneratorException
     */
    public static void main(String[] args) throws FHIRGeneratorException {

        List<SearchParameter> params = new ArrayList<>();
        params.add(buildCommonBasic("boolean", "Boolean"));
        params.add(buildCommonBasic("integer", "Integer"));
        params.add(buildCommonBasic("string", "String"));
        params.add(buildCommonBasic("code", "Code"));
        params.add(buildCommonBasic("decimal", "Decimal"));
        params.add(buildCommonBasic("uri", "Uri"));
        params.add(buildCommonBasic("instant", "Instant"));
        params.add(buildCommonBasic("date", "Date"));
        params.add(buildCommonBasic("dateTime", "DateTime"));
        params.add(buildCommonBasic("Period", "Period"));
        params.add(buildCommonBasic("Timing", "Timing"));
        params.add(buildCommonBasic("Quantity", "Quantity"));
        params.add(buildCommonBasic("Reference", "Reference"));
        params.add(buildCommonBasic("Coding", "Coding"));
        params.add(buildCommonBasic("CodeableConcept", "CodeableConcept"));
        params.add(buildCommonBasic("Identifier", "Identifier"));
        params.add(buildCommonBasic("Period-noStart", "Period"));
        params.add(buildCommonBasic("Period-noEnd", "Period"));
        params.add(buildCommonBasic("Period-noStartOrEnd", "Period"));
        params.add(buildCommonBasic("Timing-eventsOnly", "Period"));
        params.add(buildCommonBasic("Timing-boundQuantity", "Timing"));
        params.add(buildCommonBasic("Basic-Timing-boundRange", "Timing"));
        params.add(buildCommonBasic("Basic-Timing-boundPeriod", "Timing"));
        params.add(buildCommonBasic("missing-instant", "Instant"));
        params.add(buildCommonBasic("missing-date", "Date"));
        params.add(buildCommonBasic("missing-dateTime", "DateTime"));
        params.add(buildCommonBasic("missing-Period", "Period"));

        // Output to Standard Out
        java.io.PrintStream out = System.out;
        output(params, out);
    }

    /*
     * used in fhir-persistence-jdbc
     */
    private static SearchParameter buildCommonBasic(String typeLowerCase, String typeCamelCase) {
        String id = "Basic-" + typeLowerCase;
        String url = "http://ibm.com/fhir/SearchParameter/Basic-" + typeLowerCase;
        String name = typeLowerCase;
        String code = typeLowerCase;
        SearchParamType type = SearchParamType.DATE;
        String desc = "test param";
        String expression = "Basic.extension.where(url='http://example.org/" + typeLowerCase + "').value" + typeCamelCase;
        String xpath = "f:Basic/f:extension[@url='http://example.org/" + typeLowerCase + "']/f:value" + typeCamelCase;
        return commonWithBase(id, url, name, code, type, desc, expression, xpath);
    }

    private static SearchParameter commonWithBase(String id, String uri, String name, String codeStr, SearchParamType type, String descriptionStr,
        String expression, String xpath) {
        List<ResourceTypeCode> baseResourceTypes = new ArrayList<>();
        baseResourceTypes.add(ResourceTypeCode.BASIC);
        return common(id, uri, name, codeStr, baseResourceTypes, type, descriptionStr, expression, xpath);
    }

    private static SearchParameter common(String id, String uri, String name, String codeStr, List<ResourceTypeCode> baseStrings, SearchParamType type,
        String descriptionStr, String expression, String xpath) {
        Uri url = Uri.of(uri);
        PublicationStatus status = PublicationStatus.ACTIVE;
        Markdown description = Markdown.of(descriptionStr);
        Code code = Code.of(codeStr);
        Collection<ResourceTypeCode> base = baseStrings;

        SearchParameter param = SearchParameter.builder()
                .url(url)
                .name(string(name))
                .status(status)
                .description(description)
                .code(code)
                .base(base)
                .type(type)
                .id(id)
                .expression(string(expression))
                .xpath(string(xpath))
                .xpathUsage(XPathUsageType.NORMAL)
                .build();
        return param;
    }

    /**
     * simple output method.
     * 
     * @param params
     * @param out
     * @throws FHIRGeneratorException
     */
    private static void output(List<SearchParameter> params, java.io.PrintStream out) throws FHIRGeneratorException {
        Bundle.Builder build = Bundle.builder().type(BundleType.COLLECTION);
        for (SearchParameter param : params) {
            build.entry(Bundle.Entry.builder().resource(param).fullUrl(param.getUrl()).build());

        }

        Bundle bundle = build.build();
        FHIRGenerator.generator(Format.JSON, true).generate(bundle, out);
        out.println(bundle.toString());
    }

}
