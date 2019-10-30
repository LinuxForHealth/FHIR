/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test.mains;

import java.lang.reflect.Method;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.search.test.BaseSearchTest;

/**
 * from prior to R4
 * 
 * @author others
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

    public static String process(SearchParameter parameter, com.ibm.fhir.model.type.String string) {
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
