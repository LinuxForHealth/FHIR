/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test.tools;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 * Builds the output properties file for the ValueTypesUtil's cache.
 * 
 * @author pbastide
 *
 */
public class ValueTypesTool {

    static {
        // used in test to make the compiled code accessible.
        System.setProperty("javax.xml.accessExternalSchema", "file");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // In Two Packages
        // com.ibm.watsonhealth.fhir.model.resource
        // com.ibm.watsonhealth.fhir.model.type

        List<Class<?>> output = classNames();
        System.out.println("# Number of Resources : " + output.size());
        // outputClasses(output);
        for (Class<?> out : output) {
            generateFields(out, out, out.getSimpleName());
        }
    }

    public static void generateFields(Class<?> root, Class<?> c, String... paths) {

        for (Field f : c.getDeclaredFields()) {

            if (f.getName().compareTo("hashCode") != 0) {

                Class<?> fieldType = f.getType();
                java.lang.reflect.Type genericType = f.getGenericType();

                // Checks the java internal parameterized type.
                // and pulls out the Encapsulated FieldName
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    fieldType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                }
                
                

                System.out.println(generatePath(paths, f.getName()) + "=" + fieldType.getSimpleName());

                String[] arr = new String[paths.length + 1];
                for (int i = 0; i < paths.length; i++) {
                    arr[i] = paths[i];
                }
                arr[paths.length] = f.getName();

                if (!fieldType.getPackage().getName().contains("com.ibm.watsonhealth.fhir.model.type")) {
                    int mod = fieldType.getModifiers();
                    if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                    generateFields(root, fieldType, arr);
                    }
                }
            }
        }

        if (c.getSuperclass() != null) {
            generateFields(root, c.getSuperclass(), paths);
        }

    }

    /**
     * generates a path
     * 
     * Example: <code>Observation.x.y</code>
     * 
     * @param pastFields
     * @param currentField
     * @return
     */
    public static String generatePath(String[] pastFields, String currentField) {
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < pastFields.length; i++) {
            b.append(pastFields[i]);
            b.append(".");
        }
        b.append(currentField);
        return b.toString();
    }

    // Outputs the Canonical Name
    public static void outputClasses(List<Class<?>> output) {
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Classes to be analayzed and loaded");
        Comparator<String> byName = (String first, String second) -> first.compareTo(second);
        output.stream().map(e -> e.getCanonicalName()).collect(Collectors.toSet()).stream().sorted(byName).forEach(System.out::println);
        System.out.println("----------------------------------------------------------------------------------");

    }

    /**
     * The resource class lists.
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<Class<?>> classNames() {

        Set<Class<?>> classNamesWithPackages = new HashSet<>();

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Map<String, JsonObject> structureDefinitionMap = buildResourceMap("./src/test/resources/definitions/r4/profiles-resources.json", "StructureDefinition");
        structureDefinitionMap.putAll(buildResourceMap("./src/test/resources/definitions/r4/profiles-types.json", "StructureDefinition"));

        structureDefinitionMap.entrySet().stream().map(new Function<Map.Entry, Map.Entry>() {

            @Override
            public Entry apply(Entry e) {
                String x = (String) e.getKey();
                String key = Character.toUpperCase(x.charAt(0)) + x.substring(1, x.length());
                SimpleEntry<String, JsonObject> obj = new SimpleEntry<String, JsonObject>(key, (JsonObject) e.getValue());
                return obj;
            }
        }).map(e -> e.getKey()).forEach(new Consumer<Object>() {
            @Override
            public void accept(Object t) {
                // System.out.println(t);
                try {
                    Class<?> tx = Class.forName("com.ibm.watsonhealth.fhir.model.resource." + t, true, loader);
                    classNamesWithPackages.add(tx);
                } catch (ClassNotFoundException e) {
                    // Don't do anything

                    // try {
                    // Class<?> tx = Class.forName("com.ibm.watsonhealth.fhir.model.type." + t, true, loader);
                    // classNamesWithPackages.add(tx);
                    // } catch (ClassNotFoundException e1) {
                    // // Don't do anything
                    // }

                }

            }

        });

        // Returns sorted
        Comparator<Class<?>> byName = (Class<?> first, Class<?> second) -> first.getCanonicalName().compareTo(second.getCanonicalName());
        return classNamesWithPackages.stream().sorted(byName).collect(Collectors.toList());
    }

    /**
     * Builds the map of Resources
     * 
     * @param path
     * @param resourceType
     * @return
     */
    public static Map<String, JsonObject> buildResourceMap(String path, String resourceType) {

        try (JsonReader reader = Json.createReader(new FileReader(new File(path)))) {
            List<JsonObject> resources = new ArrayList<>();
            JsonObject bundle = reader.readObject();
            for (JsonValue entry : bundle.getJsonArray("entry")) {
                JsonObject resource = entry.asJsonObject().getJsonObject("resource");
                if (resourceType.equals(resource.getString("resourceType"))) {
                    resources.add(resource);
                }
            }
            Collections.sort(resources, new Comparator<JsonObject>() {
                @Override
                public int compare(JsonObject first, JsonObject second) {
                    return first.getString("name").compareTo(second.getString("name"));
                }
            });
            Map<String, JsonObject> resourceMap = new LinkedHashMap<>();
            for (JsonObject resource : resources) {
                if ("CodeSystem".equals(resourceType) || "ValueSet".equals(resourceType)) {
                    resourceMap.put(resource.getString("url"), resource);
                } else {
                    resourceMap.put(resource.getString("name"), resource);
                }
            }
            return resourceMap;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
