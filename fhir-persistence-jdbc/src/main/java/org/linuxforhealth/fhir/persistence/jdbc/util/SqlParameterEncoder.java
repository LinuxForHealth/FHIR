/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides custom encoding and escaping functions for use by the JDBCQueryBuilder. Proper encoding/escaping reduces 
 * the likelihood of SQL penetration that may happen when called via the REST layer.
 */
public class SqlParameterEncoder {

    public static final String DEFAULT_ESCAPE_CHARACTER = "";

    private static final String BLACKLIST_CHARACTERS_REGEX = "['\"]";

    private final String escapeCharacter;
    private final String blackListCharactersRegex;
    private final Pattern escapeCharacterPattern;

    public SqlParameterEncoder() {
        this(DEFAULT_ESCAPE_CHARACTER, BLACKLIST_CHARACTERS_REGEX);
    }

    public SqlParameterEncoder(String escapeCharacter, String blackListCharactersRegex) {
        this.escapeCharacter = escapeCharacter;
        this.blackListCharactersRegex = blackListCharactersRegex;
        escapeCharacterPattern = Pattern.compile(this.blackListCharactersRegex);
    }

    public String encodeParameter(String parameter) {
        String safeParameter = Optional.ofNullable(parameter).orElse("");
        Matcher matcher = escapeCharacterPattern.matcher(safeParameter);
        safeParameter = matcher.replaceAll(getEscapeCharacter());
        
        return safeParameter;
    }
    
    public String getEscapeCharacter() {
      return this.escapeCharacter;
    }
    
    public static String encode(String parameter) {
        return encode(parameter, DEFAULT_ESCAPE_CHARACTER, BLACKLIST_CHARACTERS_REGEX);
    }

    public static String encode(String parameter, String defaultEscapeCharacter, String blackListCharactersRegex) {
        return new SqlParameterEncoder(defaultEscapeCharacter, blackListCharactersRegex).encodeParameter(parameter);
    }
}
