/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class that generates the contents of the static initializer methods of the LanguageRegistry class
 * from the contents of the language-subtag-registry.txt file.
 * It does not generate the entire Java class, but rather just the sets/maps that need to be built by
 * the static initializer methods. The generated code can be copied/pasted into those methods of the
 * LanguageRegistry class.
 */
public class LanguageRegistryGenerator {

    private static final String FILE_DATE_ROW = "File-Date:";
    private static final String RECORD_DELIMITER = "%%";
    private static final String ROW_DELIMITER = ": ";
    private static final String TYPE = "Type";
    private static final String LANGUAGE = "language";
    private static final String EXTLANG = "extlang";
    private static final String SCRIPT = "script";
    private static final String REGION = "region";
    private static final String VARIANT = "variant";
    private static final String GRANDFATHERED = "grandfathered";
    private static final String DEPRECATED = "Deprecated";
    private static final String SUBTAG = "Subtag";
    private static final String PREFIX = "Prefix";
    private static final String TAG = "Tag";
    private static final String PRIVATE_USE_LANG_SUBTAG = "qaa..qtz";
    private static final String PRIVATE_USE_SCRIPT_SUBTAG = "Qaaa..Qabx";
    private static final String PRIVATE_USE_REGION_SUBTAG_1 = "QM..QZ";
    private static final String PRIVATE_USE_REGION_SUBTAG_2 = "XA..XZ";
    private final List<Map<String, String>> languageSubTagRegistry;

    /**
     * Instantiates a LanguageTagGenerator.
     * 
     * @param languageSubTagRegistry
     */
    public LanguageRegistryGenerator(List<Map<String, String>> languageSubTagRegistry) {
        this.languageSubTagRegistry = languageSubTagRegistry;
    }

    /**
     * Generates the Language Tag classes.
     */
    public void generate(String basePath) {
        String packageName = "com.ibm.fhir.model.lang.util";
        generateLanguages(basePath, packageName);
        generateExtLangs(basePath, packageName);
        generateScripts(basePath, packageName);
        generateRegions(basePath, packageName);
        generateVariants(basePath, packageName);
        generateGrandfathereds(basePath, packageName);
    }

    /**
     * Generate languages.
     */
    private void generateLanguages(String basePath, String packageName) {
        CodeBuilder cb = new CodeBuilder();

        List<String> lines = new ArrayList<>();
        for (Map<String, String> entryMap : languageSubTagRegistry) {
            if (LANGUAGE.equals(entryMap.get(TYPE))) {
                String subTag = entryMap.get(SUBTAG);
                String deprecated = entryMap.get(DEPRECATED);
                if (subTag != null && !PRIVATE_USE_LANG_SUBTAG.equalsIgnoreCase(subTag) && deprecated == null) {
                    lines.add("l.add(" + CodeBuilder.quote(subTag.toLowerCase()) + ");");
                }
            }
        }
        lines.add(0, "Set<String> l = new HashSet<>(" + lines.size() + ");");
        lines.add("return l;");
        cb.lines(lines);

        String className = "Languages";
        writeFile(cb, basePath, packageName, className);
    }

    /**
     * Generate language extensions.
     */
    private void generateExtLangs(String basePath, String packageName) {
        CodeBuilder cb = new CodeBuilder();

        List<String> lines = new ArrayList<>();
        for (Map<String, String> entryMap : languageSubTagRegistry) {
            if (EXTLANG.equals(entryMap.get(TYPE))) {
                String subTag = entryMap.get(SUBTAG);
                String prefix = entryMap.get(PREFIX);
                String deprecated = entryMap.get(DEPRECATED);
                if (subTag != null && prefix != null && deprecated == null) {
                    lines.add("e.put(" + CodeBuilder.quote(subTag.toLowerCase()) + ", " + CodeBuilder.quote(prefix.toLowerCase()) + ");");
                }
            }
        }
        lines.add(0, "Map<String, String> e = new HashMap<>(" + lines.size() + ");");
        lines.add("return e;");
        cb.lines(lines);

        String className = "ExtLangs";
        writeFile(cb, basePath, packageName, className);
    }

    /**
     * Generate scripts.
     */
    private void generateScripts(String basePath, String packageName) {
        CodeBuilder cb = new CodeBuilder();

        List<String> lines = new ArrayList<>();
        for (Map<String, String> entryMap : languageSubTagRegistry) {
            if (SCRIPT.equals(entryMap.get(TYPE))) {
                String subTag = entryMap.get(SUBTAG);
                String deprecated = entryMap.get(DEPRECATED);
                if (subTag != null && !PRIVATE_USE_SCRIPT_SUBTAG.equalsIgnoreCase(subTag) && deprecated == null) {
                    lines.add("s.add(" + CodeBuilder.quote(subTag.toLowerCase()) + ");");
                }
            }
        }
        lines.add(0, "Set<String> s = new HashSet<>(" + lines.size() + ");");
        lines.add("return s;");
        cb.lines(lines);

        String className = "Scripts";
        writeFile(cb, basePath, packageName, className);
    }

    /**
     * Generate regions.
     */
    private void generateRegions(String basePath, String packageName) {
        CodeBuilder cb = new CodeBuilder();

        List<String> lines = new ArrayList<>();
        for (Map<String, String> entryMap : languageSubTagRegistry) {
            if (REGION.equals(entryMap.get(TYPE))) {
                String subTag = entryMap.get(SUBTAG);
                String deprecated = entryMap.get(DEPRECATED);
                if (subTag != null && !PRIVATE_USE_REGION_SUBTAG_1.equalsIgnoreCase(subTag) && !PRIVATE_USE_REGION_SUBTAG_2.equalsIgnoreCase(subTag) && deprecated == null) {
                    lines.add("r.add(" + CodeBuilder.quote(subTag.toLowerCase()) + ");");
                }
            }
        }
        lines.add(0, "Set<String> r = new HashSet<>(" + lines.size() + ");");
        lines.add("return r;");
        cb.lines(lines);

        String className = "Regions";
        writeFile(cb, basePath, packageName, className);
    }

    /**
     * Generate variants.
     */
    private void generateVariants(String basePath, String packageName) {
        CodeBuilder cb = new CodeBuilder();

        List<String> lines = new ArrayList<>();
        for (Map<String, String> entryMap : languageSubTagRegistry) {
            if (VARIANT.equals(entryMap.get(TYPE))) {
                String subTag = entryMap.get(SUBTAG);
                String deprecated = entryMap.get(DEPRECATED);
                if (subTag != null && deprecated == null) {
                    String prefix = entryMap.get(PREFIX);
                    if (prefix != null) {
                        StringBuilder prefixesSb = new StringBuilder();
                        String[] prefixes = prefix.split(",");
                        for (String prefixPart : prefixes) {
                            prefixesSb.append(CodeBuilder.quote(prefixPart.trim().toLowerCase())).append(", ");
                        }
                        prefixesSb.setLength(prefixesSb.length() - 2);
                        lines.add("v.put(" + CodeBuilder.quote(subTag.toLowerCase()) + ", Arrays.asList(" + prefixesSb + "));");
                    } else {
                        lines.add("v.put(" + CodeBuilder.quote(subTag.toLowerCase()) + ", Collections.emptyList());");
                    }
                }
            }
        }
        lines.add(0, "Map<String, List<String>> v = new HashMap<>(" + lines.size() + ");");
        lines.add("return v;");
        cb.lines(lines);

        String className = "Variants";
        writeFile(cb, basePath, packageName, className);
    }

    /**
     * Generate granfathered.
     */
    private void generateGrandfathereds(String basePath, String packageName) {
        CodeBuilder cb = new CodeBuilder();

        List<String> lines = new LinkedList<>();
        for (Map<String, String> entryMap : languageSubTagRegistry) {
            if (GRANDFATHERED.equals(entryMap.get(TYPE))) {
                String tag = entryMap.get(TAG);
                String deprecated = entryMap.get(DEPRECATED);
                if (tag != null && deprecated == null) {
                    lines.add("g.add(" + CodeBuilder.quote(tag.toLowerCase()) + ");");
                }
            }
        }
        lines.add(0, "Set<String> g = new HashSet<>(" + lines.size() + ");");
        lines.add("return g;");
        cb.lines(lines);

        String className = "Grandfathereds";
        writeFile(cb, basePath, packageName, className);
    }

    /**
     * Write file.
     */
    private void writeFile(CodeBuilder cb, String basePath, String packageName, String className) {

        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/" + className + ".java");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                Files.createFile(file.toPath());
            }
        } catch (Exception e) {
            throw new Error(e);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cb.toString());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * Builds a list of language subtag registry entries.
     * 
     * @param path
     *            the file path to the language subtag registry file
     * @return the list of language subtag registry entries
     */
    public static List<Map<String, String>> buildLanguageSubTagRegistry(String path) {
        List<Map<String, String>> registry = new ArrayList<>();
        Map<String, String> curEntry = new HashMap<>();
        String curKey = null;
        String curValue = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
            for (int rowNum = 1;; rowNum++) {
                String fileLine = reader.readLine();

                // End of file
                if (fileLine == null) {
                    if (!curEntry.isEmpty()) {
                        registry.add(curEntry);
                    }
                    break;
                }

                // End of entry
                if (RECORD_DELIMITER.equals(fileLine)) {
                    if (!curEntry.isEmpty()) {
                        registry.add(curEntry);
                    }
                    curEntry = new HashMap<>();
                    curKey = null;
                    curValue = null;
                    continue;
                }

                // Skip file date row
                if (fileLine.startsWith(FILE_DATE_ROW)) {
                    continue;
                }

                // Parse the row
                String[] entryParts = fileLine.split(ROW_DELIMITER);
                if (entryParts.length == 2 && !entryParts[0].trim().isEmpty() && !entryParts[1].trim().isEmpty()) {
                    curKey = entryParts[0].trim();
                    String existingValue = curEntry.get(curKey);
                    curValue = (existingValue != null ? (existingValue + ", " + entryParts[1]) : entryParts[1]).trim();
                    curEntry.put(curKey, curValue);
                } else if (entryParts.length == 1 && !entryParts[0].trim().isEmpty() && curKey != null && curValue != null) {
                    // Append to value of previous row
                    curValue = (curValue + entryParts[0]).trim();
                } else {
                    throw new RuntimeException("Invalid rowNum: " + rowNum + ", fileLine: " + fileLine);
                }
            }
        } catch (Exception e) {
            throw new Error(e);
        }

        return registry;
    }

    /**
     * Generate the code snippets from the contents of the language-subtag-registry.txt file.
     * 
     * @param args
     *            the args
     * @throws Exception
     *             an exception
     */
    public static void main(String[] args) throws Exception {
        List<Map<String, String>> languageSubTagRegistry = buildLanguageSubTagRegistry("./definitions/language-subtag-registry.txt");
        LanguageRegistryGenerator generator = new LanguageRegistryGenerator(languageSubTagRegistry);
        generator.generate("./src/main/java");
    }
}
