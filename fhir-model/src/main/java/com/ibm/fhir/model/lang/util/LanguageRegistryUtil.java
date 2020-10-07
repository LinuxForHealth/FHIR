/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.lang.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for language registry.
 */
public class LanguageRegistryUtil {

    private LanguageRegistryUtil() {
    }

    /**
     * Loads the class in the classloader in order to initialize static members in LanguageRegistry.
     * Call this before using the class in order to avoid a slight performance hit on first use.
     */
    public static void init() {
        LanguageRegistry.init();
    }

    /**
     * Determines if the tag is a valid language tag registered with BCP-47.
     * If the tag contains any extensions or private-use subtags, it is not considered valid.
     * 
     * @param tag
     *            the tag
     * @return true or false
     */
    public static boolean isValidLanguageTag(String tag) {

        // Check that only valid characters are used
        if (tag == null || tag.isEmpty() || tag.indexOf("--") > -1 || !tag.matches("^[a-zA-Z0-9-]+$")) {
            return false;
        }

        // Convert to lowercase since language tags are case insensitive and LanguageRegistry contains them in lower case
        tag = tag.toLowerCase();
        
        // Check if the tag is a grandfathered tag
        if (LanguageRegistry.isGrandfatheredTag(tag)) {
            return true;
        }

        // Check if the tag contains an extension or private use subtag
        if (containsExtensionOrPrivateUseSubtags(tag)) {
            return false;
        }

        String[] subtags = tag.split("-");
        if (subtags.length < 1) {
            return false;
        }

        // Check the language subtag
        String language = subtags[0];
        if (!LanguageRegistry.isLanguageSubtag(language)) {
            return false;
        }

        // Check the other subtags (which are optional)
        if (subtags.length > 1) {
            String extlangFound = null;
            String scriptFound = null;
            String regionFound = null;
            List<String> variantWithPrefixesFound = new ArrayList<>();
            List<String> variantsWithoutPrefixesFound = new ArrayList<>();
            for (int i = 1; i < subtags.length; i++) {
                String subtag = subtags[i];

                if (LanguageRegistry.isExtlangSubtag(subtag)) {
                    // Multiple extlang not allowed, and must be before script, region, and variant subtags
                    if (extlangFound != null || scriptFound != null || regionFound != null || !variantWithPrefixesFound.isEmpty()
                            || !variantWithPrefixesFound.isEmpty()) {
                        return false;
                    }
                    extlangFound = subtag;
                } else if (LanguageRegistry.isScriptSubtag(subtag)) {
                    // Multiple scripts not allowed, and must be before region and variant subtags
                    if (scriptFound != null || regionFound != null || !variantWithPrefixesFound.isEmpty() || !variantWithPrefixesFound.isEmpty()) {
                        return false;
                    }
                    scriptFound = subtag;
                } else if (LanguageRegistry.isRegionSubtag(subtag)) {
                    // Multiple regions not allowed, and must be before variant subtags
                    if (regionFound != null || !variantWithPrefixesFound.isEmpty() || !variantWithPrefixesFound.isEmpty()) {
                        return false;
                    }
                    regionFound = subtag;
                } else if (LanguageRegistry.isVariantSubtag(subtag)) {
                    // Duplicate variants not allowed
                    if (variantWithPrefixesFound.contains(subtag) || variantsWithoutPrefixesFound.contains(subtag)) {
                        return false;
                    }
                    // Variants with prefixes defined must come before variants without prefixes defined
                    List<String> variantPrefixes = LanguageRegistry.getVariantPrefixes(subtag);
                    if (!variantPrefixes.isEmpty()) {
                        if (!variantsWithoutPrefixesFound.isEmpty()) {
                            return false;
                        }
                        // Check prefixes against tag so far (with and without region subtag)
                        StringBuilder prefixToCheck1Sb = new StringBuilder(language);
                        StringBuilder prefixToCheck2Sb = new StringBuilder(language);
                        if (extlangFound != null) {
                            prefixToCheck1Sb.append("-").append(extlangFound);
                            prefixToCheck2Sb.append("-").append(extlangFound);
                        }
                        if (scriptFound != null) {
                            prefixToCheck1Sb.append("-").append(scriptFound);
                            prefixToCheck2Sb.append("-").append(scriptFound);
                        }
                        if (regionFound != null) {
                            prefixToCheck1Sb.append("-").append(regionFound);
                        }
                        for (String variantWithPrefixFound : variantWithPrefixesFound) {
                            prefixToCheck1Sb.append("-").append(variantWithPrefixFound);
                            prefixToCheck2Sb.append("-").append(variantWithPrefixFound);
                        }
                        if (!variantPrefixes.contains(prefixToCheck1Sb.toString()) && !variantPrefixes.contains(prefixToCheck2Sb.toString())) {
                            return false;
                        }
                        variantWithPrefixesFound.add(subtag);
                    } else {
                        variantsWithoutPrefixesFound.add(subtag);
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Determines if the tag contains an extension or private use subtag.
     * 
     * @param tag
     *            the tag
     * @return true or false
     */
    private static boolean containsExtensionOrPrivateUseSubtags(String tag) {
        return tag.matches("^.-") || tag.matches("-.-");
    }
}