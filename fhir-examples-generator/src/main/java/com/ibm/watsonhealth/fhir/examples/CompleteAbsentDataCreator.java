/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.examples;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;

import com.ibm.watsonhealth.fhir.model.builder.Builder;
import com.ibm.watsonhealth.fhir.model.resource.List;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.examples.DataCreatorBase;

public class CompleteAbsentDataCreator extends DataCreatorBase {

    public CompleteAbsentDataCreator() throws IOException {
        super();
    }

    @Override
    protected Builder<?> addData(Builder<?> builder, int choiceIndicator) throws Exception {
        Method[] methods = builder.getClass().getDeclaredMethods();
        
        boolean empty = true;
        for (Method method : methods) {
            if (method.getName().equals("build") ||
                method.getName().equals("getClass") || 
                method.getName().equals("wait") ||
                method.getName().equals("toString") ||
                method.getName().equals("hashCode") ||
                method.getName().equals("notify") ||
                method.getName().equals("notifyAll") ||
                method.getName().equals("from") ||
                method.getName().equals("contained") ||
                method.getName().equals("extension")) {
                
                continue;
            }
            
            Class<?>[] parameterClasses = method.getParameterTypes();
            
            if (parameterClasses.length != 1) {
                throw new RuntimeException("Error adding data via builder " + builder.getClass() + "; expected 1 parameter, but found " + parameterClasses.length);
//                return (Builder) method.invoke(builder);
            }
            
            Class<?> parameterType = parameterClasses[0];
            // Special case to avoid infinite recursion
            if (builder instanceof Identifier.Builder && Reference.class.isAssignableFrom(parameterType)) {
                continue;
            }
            
            Object argument;
            if (Element.class.isAssignableFrom(parameterType)
                || Collection.class.isAssignableFrom(parameterType)) {
            
                // filter out inhereted methods like Code.Builder.extension and String.Builder.extension
                if (builder.getClass().equals(method.getReturnType())) {
                    argument = createArgument(builder.getClass().getEnclosingClass(), method, parameterType, 0, choiceIndicator);
                    if (argument != null && !(argument instanceof Collection && ((Collection) argument).isEmpty())) {
                        method.invoke(builder, argument);
                        empty = false;
                    }
                }
            }
        }
        
        if (empty) {
            if (builder instanceof Element.Builder){
                // We have a primitive type (i.e. an edge node)
                setDataAbsentReason((Element.Builder) builder);
            } else {
                throw new RuntimeException("Something went wrong; builder of type " + builder.getClass() + " doesn't have any valid entries.");
            }
        }
        return builder;
    }
}
