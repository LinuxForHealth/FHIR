/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test.mains;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.id;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.string;

import java.util.List;
import java.util.Map;

import com.ibm.watsonhealth.fhir.model.ContactPointSystemList;
import com.ibm.watsonhealth.fhir.model.ContactPointUseList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

public class Main {
	public static void main(String[] args) throws Exception {
		for (SearchParameter parameter : SearchUtil.getSearchParameters(Resource.class)) {
			String name = parameter.getName().getValue();
			String type = parameter.getType().getValue();
			String description = parameter.getDescription().getValue();
			String xpath = null;
			if (parameter.getXpath() != null) {
				xpath = parameter.getXpath().getValue();
			}
			System.out.println("name: " + name + ", type: " + type + ", description: " + description + ", xpath: " + xpath);
		}
		
		for (SearchParameter parameter : SearchUtil.getSearchParameters(Patient.class)) {
			String name = parameter.getName().getValue();
			String type = parameter.getType().getValue();
			String description = parameter.getDescription().getValue();
			String xpath = null;
			if (parameter.getXpath() != null) {
				xpath = parameter.getXpath().getValue();
			}
			System.out.println("name: " + name + ", type: " + type + ", description: " + description + ", xpath: " + xpath);
		}		
		
		// build patient object using fluent API
		ObjectFactory f = new ObjectFactory();
		Patient patient = f.createPatient()
			.withId(id("1234"))
			.withName(
				f.createHumanName()
					.withGiven(string("John"))
					.withFamily(string("Doe"))
			)
			.withBirthDate(f.createDate().withValue("1950-08-15"))
			.withTelecom(
				f.createContactPoint()
					.withUse(f.createContactPointUse().withValue(ContactPointUseList.HOME))
					.withSystem(f.createContactPointSystem().withValue(ContactPointSystemList.PHONE))
					.withValue(string("555-1234"))
			);
		
		Resource resource = patient;
		
		Map<SearchParameter, List<Object>> map = SearchUtil.extractParameterValues(resource);
		for (SearchParameter parameter : map.keySet()) {
			String name = parameter.getName().getValue();
			String type = parameter.getType().getValue();
			String xpath = parameter.getXpath().getValue();
			System.out.println("name: " + name + ", type: " + type + ", xpath: " + xpath);
			List<Object> values = map.get(parameter);
			for (Object value : values) {
				System.out.println("    " + value.getClass().getName());
				String result = Processor.process(parameter, value);
				System.out.println("    result: " + result);
			}
		}
	}
}
