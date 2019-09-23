/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.model;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.ibm.fhir.audit.logging.beans.Context;

/**
 * Cadf to Json converter
 * 
 * @author Albert Wang
 */
public class CadfParser {

    // The configured mapper
    private final ObjectMapper mapper;

    /**
     * Public constructor
     */
    public CadfParser() {
        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        JacksonAnnotationIntrospector i = new JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(i);
    }

    /*
     * (non-Javadoc) convert CadfEvent object to Json
     */
    public String cadf2Json(CadfEvent event) throws JsonProcessingException {
        return mapper.writeValueAsString(event);
    }

    /*
     * (non-Javadoc) convert CadfEvent object to Json
     */
    public String fhirContext2Json(Context event) throws JsonProcessingException {
        return mapper.writeValueAsString(event);
    }

    /*
     * (non-Javadoc) convert json to CadfEvent object
     */
    public CadfEvent json2Cadf(String event) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(event, CadfEvent.class);
    }

}
