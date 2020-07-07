/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test.mains;

import java.util.List;
import java.util.Map;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.code.ContactPointSystem;
import com.ibm.fhir.model.type.code.ContactPointUse;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.search.test.BaseSearchTest;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * A driver for SearchUtil functions
 */
public class Main extends BaseSearchTest {

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        for (SearchParameter parameter : SearchUtil.getApplicableSearchParameters(Resource.class.getSimpleName())) {
            String code = parameter.getCode().getValue();
            String type = parameter.getType().getValue();
            String description = parameter.getDescription().getValue();
            String xpath = null;
            if (parameter.getXpath() != null) {
                xpath = parameter.getXpath().getValue();
            }
            System.out.println("name: " + code + ", type: " + type + ", description: " + description + ", xpath: " + xpath);
        }

        for (SearchParameter parameter : SearchUtil.getApplicableSearchParameters(Patient.class.getSimpleName())) {
            String code = parameter.getCode().getValue();
            String type = parameter.getType().getValue();
            String description = parameter.getDescription().getValue();
            String xpath = null;
            if (parameter.getXpath() != null) {
                xpath = parameter.getXpath().getValue();
            }
            System.out.println("name: " + code + ", type: " + type + ", description: " + description + ", xpath: " + xpath);
        }

        // Build Patient Resource
        HumanName.Builder humanNameBuilder = HumanName.builder();
        humanNameBuilder.given(com.ibm.fhir.model.type.String.of("John"));
        humanNameBuilder.family(com.ibm.fhir.model.type.String.of("Doe"));

        Date.Builder birthDateBuilder = Date.builder();
        birthDateBuilder.value("1950-08-15");

        ContactPointSystem.Builder contactPointSystemBuilder = ContactPointSystem.builder();
        contactPointSystemBuilder.value(ContactPointSystem.ValueSet.PHONE);

        ContactPointUse.Builder contactPointUseBuilder = ContactPointUse.builder();
        contactPointUseBuilder.value(ContactPointUse.ValueSet.HOME);

        ContactPoint.Builder telecomBuilder = ContactPoint.builder();
        telecomBuilder.system(contactPointSystemBuilder.build());
        telecomBuilder.value(com.ibm.fhir.model.type.String.of("555-1234"));
        telecomBuilder.use(contactPointUseBuilder.build());

        Patient.Builder builder = Patient.builder();
        builder.id("1234");
        builder.name(humanNameBuilder.build());
        builder.birthDate(birthDateBuilder.build());
        builder.telecom(telecomBuilder.build());

        Resource resource = builder.build();

        Map<SearchParameter, List<FHIRPathNode>> map = SearchUtil.extractParameterValues(resource);
        for (SearchParameter parameter : map.keySet()) {
            String code = parameter.getCode().getValue();
            String type = parameter.getType().getValue();
            String xpath = parameter.getXpath().getValue();
            System.out.println("code: " + code + ", type: " + type + ", xpath: " + xpath);
            List<FHIRPathNode> values = map.get(parameter);
            for (FHIRPathNode value : values) {
                System.out.println("    " + value.getClass().getName());
                String result = Processor.process(parameter, value);
                System.out.println("    result: " + result);
            }
        }
    }
}
