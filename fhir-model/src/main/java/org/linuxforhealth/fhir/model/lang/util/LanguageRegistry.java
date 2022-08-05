/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.lang.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that represents the BCP-47 language registry, with all subtags in lowercase.
 * Generated from "http://www.iana.org/assignments/language-subtag-registry/language-subtag-registry".
 * Private and deprecated tags/subtags are not included.
 */
class LanguageRegistry {

    private static final Set<String> languages = LanguageRegistryInitializer.buildLanguages();
    private static final Map<String, String> extlangs = LanguageRegistryInitializer.buildExtlangs();
    private static final Set<String> scripts = LanguageRegistryInitializer.buildScripts();
    private static final Set<String> regions = LanguageRegistryInitializer.buildRegions();
    private static final Map<String, List<String>> variants = LanguageRegistryInitializer.buildVariants();
    private static final Set<String> grandfathereds = LanguageRegistryInitializer.buildGrandfathereds();

    private LanguageRegistry() {
    }

    /**
     * Loads the class in the classloader in order to initialize static members.
     * Call this before using the class in order to avoid a slight performance hit on first use.
     */
    static void init() {
        // allows us to initialize this class during startup
    }

    /**
     * Determines if the language subtag exists.
     * 
     * @param subtag
     *            the language subtag (in lowercase)
     * @return true or false
     */
    static boolean isLanguageSubtag(String subtag) {
        return languages.contains(subtag);
    }

    /**
     * Determines if the extlang subtag exists.
     * 
     * @param subtag
     *            the extlang subtag (in lowercase)
     * @return true or false
     */
    static boolean isExtlangSubtag(String subtag) {
        return extlangs.containsKey(subtag);
    }

    /**
     * Gets the prefix tag (in lowercase) for the extlang.
     * 
     * @param subtag
     *            the extlang subtag (in lowercase)
     * @return the prefix tag (in lowercase)
     */
    static String getExtlangPrefix(String subtag) {
        return extlangs.get(subtag);
    }

    /**
     * Determines if the script subtag exists.
     * 
     * @param subtag
     *            the script subtag (in lowercase)
     * @return true or false
     */
    static boolean isScriptSubtag(String subtag) {
        return scripts.contains(subtag);
    }

    /**
     * Determines if the region subtag exists.
     * 
     * @param subtag
     *            the region subtag (in lowercase)
     * @return true or false
     */
    static boolean isRegionSubtag(String subtag) {
        return regions.contains(subtag);
    }

    /**
     * Determines if the variant subtag exists.
     * 
     * @param subtag
     *            the variant subtag (in lowercase)
     * @return true or false
     */
    static boolean isVariantSubtag(String subtag) {
        return variants.containsKey(subtag);
    }

    /**
     * Gets a list of prefix tags (in lowercase) for the variant.
     * 
     * @param subtag
     *            the variant subtag (in lowercase)
     * @return the list of prefix tags (in lowercase)
     */
    static List<String> getVariantPrefixes(String subtag) {
        return variants.get(subtag);
    }

    /**
     * Determines if the grandfathered tag exists.
     * 
     * @param tag
     *            the tag (in lowercase)
     * @return true or false
     */
    static boolean isGrandfatheredTag(String tag) {
        return grandfathereds.contains(tag);
    }

    /**
     * Private class that populates the lists/maps via parsing the language-subtag-registry.
     */
    private static class LanguageRegistryInitializer {

        private static final String REGISTRY_FILE = "language-subtag-registry";
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

        /**
         * Builds the set of language subtags.
         * 
         * @return the set
         */
        private static Set<String> buildLanguages() {
            Set<String> languages = new HashSet<>();

            for (Map<String, String> entryMap : getLanguageSubTagRegistryEntries(REGISTRY_FILE)) {
                if (LANGUAGE.equals(entryMap.get(TYPE))) {
                    String subTag = entryMap.get(SUBTAG);
                    String deprecated = entryMap.get(DEPRECATED);
                    if (subTag != null && !PRIVATE_USE_LANG_SUBTAG.equalsIgnoreCase(subTag) && deprecated == null) {
                        languages.add(subTag.toLowerCase());
                    }
                }
            }
            return languages;
        }

        /**
         * Builds the map of extlang subtags to prefix language subtag.
         * 
         * @return the map
         */
        private static Map<String, String> buildExtlangs() {
            Map<String, String> extLangs = new HashMap<>();

            for (Map<String, String> entryMap : getLanguageSubTagRegistryEntries(REGISTRY_FILE)) {
                if (EXTLANG.equals(entryMap.get(TYPE))) {
                    String subTag = entryMap.get(SUBTAG);
                    String prefix = entryMap.get(PREFIX);
                    String deprecated = entryMap.get(DEPRECATED);
                    if (subTag != null && prefix != null && deprecated == null) {
                        extLangs.put(subTag.toLowerCase(), prefix.toLowerCase());
                    }
                }
            }
            return extLangs;
        }

        /**
         * Builds the set of script subtags.
         * 
         * @return the set
         */
        private static Set<String> buildScripts() {
            Set<String> scripts = new HashSet<>();

            for (Map<String, String> entryMap : getLanguageSubTagRegistryEntries(REGISTRY_FILE)) {
                if (SCRIPT.equals(entryMap.get(TYPE))) {
                    String subTag = entryMap.get(SUBTAG);
                    String deprecated = entryMap.get(DEPRECATED);
                    if (subTag != null && !PRIVATE_USE_SCRIPT_SUBTAG.equalsIgnoreCase(subTag) && deprecated == null) {
                        scripts.add(subTag.toLowerCase());
                    }
                }
            }
            return scripts;
        }

        /**
         * Builds the set of region subtags.
         * 
         * @return the set
         */
        private static Set<String> buildRegions() {
            Set<String> regions = new HashSet<>();

            for (Map<String, String> entryMap : getLanguageSubTagRegistryEntries(REGISTRY_FILE)) {
                if (REGION.equals(entryMap.get(TYPE))) {
                    String subTag = entryMap.get(SUBTAG);
                    String deprecated = entryMap.get(DEPRECATED);
                    if (subTag != null && !PRIVATE_USE_REGION_SUBTAG_1.equalsIgnoreCase(subTag) && !PRIVATE_USE_REGION_SUBTAG_2.equalsIgnoreCase(subTag)
                            && deprecated == null) {
                        regions.add(subTag.toLowerCase());
                    }
                }
            }
            return regions;
        }

        /**
         * Builds the map of variant subtags to list of prefix tags (if any).
         * 
         * @return the map
         */
        private static Map<String, List<String>> buildVariants() {
            Map<String, List<String>> variants = new HashMap<>();

            for (Map<String, String> entryMap : getLanguageSubTagRegistryEntries(REGISTRY_FILE)) {
                if (VARIANT.equals(entryMap.get(TYPE))) {
                    String subTag = entryMap.get(SUBTAG);
                    String deprecated = entryMap.get(DEPRECATED);
                    if (subTag != null && deprecated == null) {
                        List<String> prefixList = new ArrayList<>();
                        String prefix = entryMap.get(PREFIX);
                        if (prefix != null) {
                            String[] prefixes = prefix.split(",");
                            for (String prefixPart : prefixes) {
                                prefixList.add(prefixPart.trim().toLowerCase());
                            }
                        }
                        variants.put(subTag.toLowerCase(), prefixList);
                    }
                }
            }
            return variants;
        }

        /**
         * Builds the set of grandfathered tags.
         * 
         * @return the set
         */
        private static Set<String> buildGrandfathereds() {
            Set<String> grandfathereds = new HashSet<>();

            for (Map<String, String> entryMap : getLanguageSubTagRegistryEntries(REGISTRY_FILE)) {
                if (GRANDFATHERED.equals(entryMap.get(TYPE))) {
                    String tag = entryMap.get(TAG);
                    String deprecated = entryMap.get(DEPRECATED);
                    if (tag != null && deprecated == null) {
                        grandfathereds.add(tag.toLowerCase());
                    }
                }
            }
            return grandfathereds;
        }

        /**
         * Gets a list of language subtag registry entries.
         * 
         * @param path
         *            the file path to the language subtag registry file
         * @return the list of language subtag registry entries
         */
        private static List<Map<String, String>> getLanguageSubTagRegistryEntries(String path) {
            List<Map<String, String>> registry = new ArrayList<>();
            Map<String, String> curEntry = new HashMap<>();
            String curKey = null;
            String curValue = null;

            try (BufferedReader reader = getBufferedReader(path)) {
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
                        throw new RuntimeException("Invalid language subtag registry file, rowNum: " + rowNum + ", fileLine: " + fileLine);
                    }
                }
            } catch (Exception e) {
                throw new Error(e);
            }

            return registry;
        }

        /**
         * Return a buffered reader for the specified file.
         * 
         * @param filePath
         *            the file path
         * @return buffered reader for reading the file line-by-line
         * @throws IOException
         *             an exception if file not found
         */
        private static BufferedReader getBufferedReader(String filePath) throws IOException {
            InputStream is;

            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            is = cl.getResourceAsStream(filePath);
            if (is == null) {
                // Try the class's classloader instead
                is = LanguageRegistryInitializer.class.getResourceAsStream(filePath);
            }

            if (is == null) {
                throw new FileNotFoundException("File not found: " + filePath);
            }

            return new BufferedReader(new InputStreamReader(is));
        }
    }
}
