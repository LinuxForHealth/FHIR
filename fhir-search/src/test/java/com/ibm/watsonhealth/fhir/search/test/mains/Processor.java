/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test.mains;

import java.lang.reflect.Method;

import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.search.test.BaseSearchTest;

/**
 * from prior to R4
 * 
 * @author paulbastide
 *
 */
public class Processor extends BaseSearchTest {
    public static String process(SearchParameter parameter, Object value) {
        try {
            Method method = Processor.class.getMethod("process", SearchParameter.class, value.getClass());
            return (String) method.invoke(null, parameter, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String process(SearchParameter parameter, Date date) {
        return "processed " + date.getClass().getName();
    }
    
    public static String process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.type.String string) {
        return "processed " + string.getClass().getName();
    }
    
    public static String process(SearchParameter parameter, HumanName name) {
        return "processed " + name.getClass().getName();
    }
    
    public static String process(SearchParameter parameter, ContactPoint contactPoint) {
        return "processed " + contactPoint.getClass().getName();
    }
    
    public static String process(SearchParameter parameter, Id id) {
        return "processed " + id.getClass().getName();
    }
}
