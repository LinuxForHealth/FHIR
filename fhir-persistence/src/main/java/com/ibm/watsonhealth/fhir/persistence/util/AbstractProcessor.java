/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.ibm.watsonhealth.fhir.model.Element;
import com.ibm.watsonhealth.fhir.model.Extension;
import com.ibm.watsonhealth.fhir.model.SearchParameter;

public abstract class AbstractProcessor<T> implements Processor<T> {
    @SuppressWarnings("unchecked")
    public T process(SearchParameter parameter, Object value) {
        try {
            Class<?> valueType = value.getClass();
            if (isEnumerationWrapper(valueType)) {
                value = getValue(value);
                valueType = value.getClass();
            } else if (valueType == Extension.class) {
                value = getValue((Extension) value);
                valueType = value.getClass();
            } else if (valueType.getDeclaredFields().length == 0) {
                valueType = valueType.getSuperclass();
            }
            Method processMethod = this.getClass().getMethod("process", SearchParameter.class, valueType);
            return (T) processMethod.invoke(this, parameter, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    protected boolean isEnumerationWrapper(Class<?> valueType) {
        try {
            Field valueField = valueType.getDeclaredField("value");
            if (valueField.getType().isEnum()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    protected String getValue(Object wrapper) {
        try {
            Object literal = wrapper.getClass().getMethod("getValue").invoke(wrapper);
            return (String) literal.getClass().getMethod("value").invoke(literal);
        } catch (Exception e) {
        }
        return null;
    }
    
    protected Element getValue(Extension extension) {
        try {
            for (Method method : Extension.class.getDeclaredMethods()) {
                String methodName = method.getName();
                if (methodName.startsWith("getValue")) {
                    Object value = method.invoke(extension);
                    if (value != null) {
                        return (Element) value;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
