/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class supporting common functions for schema management
 */
public class SchemaGeneratorUtil {
    private SchemaGeneratorUtil() {
        // No Operation
    }

    /**
     * Read the procedure template as a resource and substitute the necessary tokens
     * 
     * @param adminSchemaName
     * @param schemaName
     * @param templateName
     * @param replacersArg
     * @return
     */
    public static String readTemplate(String adminSchemaName, String schemaName, String templateName,
            Collection<Replacer> replacersArg) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Be careful, this may return null if the resource doesn't exist.
        // Odd that it's not an IOException...because it makes this boilerplate
        // stuff really ugly
        try (InputStream is = classLoader.getResourceAsStream(templateName);) {
            if (is == null) {
                throw new IllegalArgumentException("template not found: " + templateName);
            }
            return readTemplate(adminSchemaName, schemaName, replacersArg, is);
        } catch (Exception e) {
            throw new IllegalArgumentException("Issue processing template", e);
        }
    }

    /**
     * Read the procedure template as a resource and substitute the necessary tokens
     * 
     * @param adminSchemaName
     * @param schemaName
     * @param replacersArg
     * @param is
     * @return
     */
    public static String readTemplate(String adminSchemaName, String schemaName, Collection<Replacer> replacersArg,
            InputStream is) {
        String result = "";

        List<Replacer> replacers = new ArrayList<>();
        if (replacersArg != null) {
            replacers.addAll(replacersArg);
        }
        replacers.add(new Replacer("SCHEMA_NAME", schemaName));
        replacers.add(new Replacer("ADMIN_SCHEMA_NAME", adminSchemaName));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {
            Stream<String> linesStream = reader.lines().map(x -> mapper(x, replacers));
            result = linesStream.collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception x) {
            throw new IllegalArgumentException("Unable to process the template", x);
        }
        return result;
    }

    public static String mapper(String original, List<Replacer> replacers) {
        String orig = original;
        for (Replacer replacer : replacers) {
            orig = replacer.process(orig);
        }
        return orig;
    }
}