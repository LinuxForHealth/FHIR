/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility class supporting common functions for schema management
 */
public class SchemaGeneratorUtil {

    /**
     * Read the procedure template as a resource and substitute the necessary tokens
     * @param templateName
     * @return
     */
    public static String readTemplate(String adminSchemaName, String schemaName, String templateName, Collection<Replacer> replacersArg) {
        StringBuilder result = new StringBuilder();

        List<Replacer> replacers = new ArrayList<>();
        if (replacersArg != null) {
            replacers.addAll(replacersArg);
        }
        replacers.add(new Replacer("SCHEMA_NAME", schemaName));
        replacers.add(new Replacer("ADMIN_SCHEMA_NAME", adminSchemaName));
        replacers.add(new Replacer("DATE", Instant.now().toString()));

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Be careful, this may return null if the resource doesn't exist.
        // Odd that it's not an IOException...because it makes this boilerplate
        // stuff really ugly
        InputStream is = classLoader.getResourceAsStream(templateName);
        if (is == null) {
            throw new IllegalArgumentException("template not found: " + templateName);
        }

        try {
            // InputStream is closed later, so we don't need to worry here
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                // Substitute any tokens on this line
                for (Replacer r : replacers) {
                    line = r.process(line);
                }

                result.append(line);
                result.append(System.lineSeparator());
            }
        }
        catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
        finally {
            try {
                is.close();
            } catch (IOException x) {
                throw new IllegalStateException(x);
            }
        }

        return result.toString();

    }

}
