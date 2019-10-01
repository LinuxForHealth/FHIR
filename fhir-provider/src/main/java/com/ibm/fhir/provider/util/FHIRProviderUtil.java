/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.provider.util;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.OperationOutcome;

public final class FHIRProviderUtil {
    private FHIRProviderUtil() { }
    
    public static MediaType getMediaType(String acceptHeader) {
        MediaType mediaType = null;
        try {
            mediaType = FHIRMediaType.valueOf(acceptHeader);
        } catch (IllegalArgumentException e) {
            // ignore
        }
        if (mediaType != null) {
            MediaType outMediaType = null;
            if (mediaType.isCompatible(FHIRMediaType.APPLICATION_FHIR_JSON_TYPE)) {               
                outMediaType = FHIRMediaType.APPLICATION_FHIR_JSON_TYPE;
            } else if (mediaType.isCompatible(FHIRMediaType.APPLICATION_JSON_TYPE)) {
                outMediaType = MediaType.APPLICATION_JSON_TYPE;
            } else if (mediaType.isCompatible(FHIRMediaType.APPLICATION_FHIR_XML_TYPE)) {
                outMediaType = FHIRMediaType.APPLICATION_FHIR_XML_TYPE;
            } else if (mediaType.isCompatible(FHIRMediaType.APPLICATION_XML_TYPE)) {
                outMediaType = MediaType.APPLICATION_XML_TYPE;
            } else {
                outMediaType = FHIRMediaType.APPLICATION_FHIR_JSON_TYPE;
            }
            // Need to get the charset setting from the acceptHeader if there
            if (mediaType.getParameters() != null
                    && mediaType.getParameters().get("charset") != null) {
                outMediaType = outMediaType.withCharset(mediaType.getParameters().get("charset"));
            }
            return outMediaType;
        }
        // default
        return FHIRMediaType.APPLICATION_FHIR_JSON_TYPE;
    }
    
    public static Response buildResponse(OperationOutcome operationOutcome, MediaType mediaType) {
        Response response = Response.status(Response.Status.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, mediaType)
                .entity(operationOutcome)
                .build();
        return response;
    }
    
    public static void dumpHeaders(MultivaluedMap<String, ?> httpHeaders) {
        for (String key : httpHeaders.keySet()) {
            List<?> values = httpHeaders.get(key);
            System.out.println("key: " + key + ", values: " + values);
        }
    }
}
