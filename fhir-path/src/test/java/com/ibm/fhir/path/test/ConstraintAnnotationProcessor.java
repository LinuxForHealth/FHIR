/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.json.JsonObject;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.JsonSupport;

public class ConstraintAnnotationProcessor {
    public static void main(String[] args) throws Exception {
        Field field = JsonSupport.class.getDeclaredField("JSON_SUPPORT");
        field.setAccessible(true);
        
        JsonObject jsonSupport = (JsonObject) field.get(null);
        
        Set<String> expressionSet = new HashSet<>();
        
        int count = 0;
        
        int resourceConstraintCount = 0;
        int elementConstraintCount = 0;
        for (String name : jsonSupport.keySet()) {
            if (!name.contains(".")) {
                Class<?> modelClass = getModelClass(name);
                List<Constraint> constraints = Arrays.asList(modelClass.getDeclaredAnnotationsByType(Constraint.class));
                count += constraints.size();
                expressionSet.addAll(constraints.stream()
                        .map(c -> c.expression())
                        .collect(Collectors.toList()));
                if (Resource.class.isAssignableFrom(modelClass)) {
                    resourceConstraintCount += constraints.size();
                } else {
                    elementConstraintCount += constraints.size();
                }
            }
        }
        
        List<String> expressionList = new ArrayList<>(expressionSet);
        Collections.sort(expressionList);
        
        for (String expr : expressionList) {
            System.out.println(expr);
        }
        
        System.out.println("Resource types with constraints: " + resourceConstraintCount);
        System.out.println("Element types with constraints: " + elementConstraintCount);
        System.out.println("Total constraints: " + count);
        System.out.println("Total unique expressions: " + expressionList.size());
    }
    
    public static Class<?> getModelClass(String name) throws ClassNotFoundException {
        try {
            return Class.forName("com.ibm.fhir.model.resource." + name);
        } catch (ClassNotFoundException e) {
            return Class.forName("com.ibm.fhir.model.type." + name);
        }
    }
}
