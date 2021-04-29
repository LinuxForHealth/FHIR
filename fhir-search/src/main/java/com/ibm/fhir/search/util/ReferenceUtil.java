/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.util;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;

/**
 * Encapsulates utility functions used when processing model Reference elements
 * from a FHIR resource
 */
public class ReferenceUtil {
    private static final Logger logger = Logger.getLogger(ReferenceUtil.class.getName());
    private static final String HISTORY = "_history";
    private static final String HTTP = "http:";
    private static final String HTTPS = "https:";
    private static final String URN = "urn:";

    // A set of resource type names to assist computing the base URL string
    private static final Set<String> resourceTypes = ModelSupport.getResourceTypes(false).stream()
            .map(Class::getSimpleName)
            .collect(Collectors.toSet());


    /**
     * Processes a Reference value from the FHIR model and interprets
     * it according to https://www.hl7.org/fhir/references.html#2.3.0
     *
     * <p>Absolute literal references will be converted to relative references if their base matches baseUrl.
     *
     * <p>The resulting ReferenceValue will contained an inferred ReferenceType
     * and the structure of the ReferenceValue.value will vary accordingly:
     * <ol>
     * <li>LITERAL_RELATIVE: the id of the referenced resource</li>
     * <li>LITERAL_ABSOLUTE: the full URI of the reference</li>
     * <li>LOGICAL: the Identifier.value (Identifier.system is not presently stored)</li>
     * <li>DISPLAY_ONLY: null</li>
     * <li>INVALID: null</li>
     * </ol>
     *
     * @param ref a non-null FHIR Reference object
     * @param baseUrl the base URL used to determine whether to convert absolute references to relative references
     * @return a structured representation of the reference value that varies by its inferred reference type
     */
    public static ReferenceValue createReferenceValueFrom(Reference ref, String baseUrl) {
        String value;
        String targetResourceType = null;
        ReferenceType referenceType =  ReferenceType.INVALID;
        Integer version = null;

        if (ref.getReference() != null && ref.getReference().getValue() != null) {
            return createReferenceValueFrom(ref.getReference().getValue(), ref.getType() != null ? ref.getType().getValue() : null, baseUrl);
        } else if (ref.getIdentifier() != null && ref.getIdentifier().getValue() != null && ref.getIdentifier().getValue().getValue() != null) {
            // LOGICAL REFERENCE
            value = ref.getIdentifier().getValue().getValue();
            referenceType = ReferenceType.LOGICAL;
            if (ref.getType() != null) {
                // We need
                targetResourceType = ref.getType().getValue();
            }
        } else if (ref.getDisplay() != null) {
            // According to the spec this is still a valid reference. But because it
            // doesn't refer to anything, we don't need to persist it
            referenceType = ReferenceType.DISPLAY_ONLY;
            value = null;
        } else {
            // model.Reference currently allows invalid references to be built, so for now we
            // need to expect and handle this.
            referenceType = ReferenceType.INVALID;
            value = null;
        }

        return new ReferenceValue(targetResourceType, value, referenceType, version);
    }

    /**
     * Processes the string value of a Reference object from the FHIR model and interprets
     * it according to https://www.hl7.org/fhir/references.html#2.3.0
     *
     * <p>Absolute literal references will be converted to relative references if their base matches baseUrl.
     *
     * <p>The resulting ReferenceValue will contain an inferred ReferenceType
     * and the structure of the ReferenceValue.value will vary accordingly:
     * <ol>
     * <li>LITERAL_RELATIVE: the id of the referenced resource</li>
     * <li>LITERAL_ABSOLUTE: the full URI of the reference</li>
     * <li>INVALID: null</li>
     * </ol>
     *
     * @param refValue a reference value string
     * @param refType a reference resource type (used for LITERAL_ABSOLUTE only)
     * @param baseUrl the base URL used to determine whether to convert absolute references to relative references
     * @return a structured representation of the reference value that varies by its inferred reference type
     */
    public static ReferenceValue createReferenceValueFrom(String refValue, String refType, String baseUrl) {
        String value = null;
        String targetResourceType = null;
        ReferenceType referenceType =  ReferenceType.INVALID;
        Integer version = null;

        if (refValue != null) {
            // LITERAL REFERENCE
            // * an absolute URL
            // * relative URL, which is relative to the Service Base URL, or, if processing a resource
            //   from a bundle, which is relative to the base URL implied by the Bundle.entry.fullUrl
            // Note that fragment (internal) references are not relevant here, because bundle
            // processing will already have resolved them, replacing them with the relative values
            value = refValue;

            if (baseUrl != null && value.startsWith(baseUrl)) {
                // - relative reference https://example.com/Patient/123
                // Because this reference is to a local FHIR resource (inside this server), we need to
                // use the correct resource type name (assigned as the code system)
                //  - https://localhost:9443/fhir-server/api/v4/Patient/1234
                //  - https://example.com/Patient/1234
                //  - https://example.com/Patient/1234/_history/2
                referenceType = ReferenceType.LITERAL_RELATIVE;
                value = value.substring(baseUrl.length());

                // Patient/1234
                // Patient/1234/_history/2
                String[] tokens = value.split("/");
                if (tokens.length > 1) {
                    targetResourceType = tokens[0];
                    value = tokens[1];
                    if (tokens.length == 4 && HISTORY.equals(tokens[2])) {
                        // versioned reference
                        version = Integer.parseInt(tokens[3]);
                    }
                }
            } else if (value != null && value.startsWith(HTTP) || value.startsWith(HTTPS) || value.startsWith(URN)) {
                // - Absolute reference. We only know the type if it is given by the type field
                referenceType = ReferenceType.LITERAL_ABSOLUTE;
                if (refType != null) {
                    targetResourceType = refType;
                }
            } else {
                //  - Relative ==> Patient/1234
                //  - Relative ==> Patient/1234/_history/2
                referenceType = ReferenceType.LITERAL_RELATIVE;
                String[] tokens = value.split("/");
                if (tokens.length > 1) {
                    targetResourceType = tokens[0];
                    value = tokens[1];
                    if (tokens.length == 4 && HISTORY.equals(tokens[2])) {
                        // versioned reference
                        version = Integer.parseInt(tokens[3]);
                    }
                }
            }
        }

        return new ReferenceValue(targetResourceType, value, referenceType, version);
    }

    /**
     * Extract the base URL from the bundle entry if one is given, otherwise
     * use the service base URL.
     * @param entry, can be null
     * @return the base URL for use in interpreting references
     * @throws FHIRSearchException
     */
    public static String getBaseUrl(Bundle.Entry entry) throws FHIRSearchException {
        if (entry != null) {
            return getBaseUrlFromBundle(entry);
        } else {
            return getServiceBaseUrl();
        }
    }

    /**
     * https://www.hl7.org/fhir/references.html#literal
     * See: a relative URL, which is relative to the Service Base URL, or, if processing a
     *      resource from a bundle, which is relative to the base URL implied by the
     *      Bundle.entry.fullUrl (see Resolving References in Bundles)
     *
     * If the fullUrl looks like this
     *   "fullUrl": "https://localhost:9443/fhir-server/api/v4/Observation/17546b5a5a9-872ecfe4-cb5e-4f8c-a381-5b13df536f87"
     * then the returned String will look like this:
     *   "https://localhost:9443/fhir-server/api/v4/"
     *
     * @param entry
     * @return
     */
    public static String getBaseUrlFromBundle(Bundle.Entry entry) throws FHIRSearchException {
        String value = entry.getFullUrl().getValue();
        try {
            return getServiceBaseUrl(value);
        } catch (FHIRSearchException x) {
            logger.warning("Bundle.entry.fullUrl is invalid: " + value);
            throw x;
        }
    }

    /**
     * Get the service base URL using the originalRequestUri currently set in the
     * FHIRRequestContext
     * @param bundle
     * @return the base url
     */
    public static String getServiceBaseUrl() throws FHIRSearchException {
        String uri = FHIRRequestContext.get().getOriginalRequestUri();
        return getServiceBaseUrl(uri);
    }

    /**
     * Get the service base URL from the given uri
     * @param uri
     * @return
     * @throws FHIRSearchException
     */
    public static String getServiceBaseUrl(String uri) throws FHIRSearchException {
        String result;

        // request URI is not set for all unit-tests, so we need to take that into account
        if (uri == null) {
            return null;
        }

        if (!uri.startsWith(HTTP) && !uri.startsWith(HTTPS)) {
            throw new FHIRSearchException("base URI expected to start with http(s):");
        }

        // Strip off any parameters
        int pmark = uri.indexOf('?');
        if (pmark >= 0) {
            uri = uri.substring(0, pmark);
        }

        // We keep parsing /field1/field2/... in the uri until we hit a valid resource type name
        int last = -1;
        int posn = -1;
        int start = -1;

        while (last < 0 && (posn = uri.indexOf('/', start+1)) >= 0) {
            String fragment = uri.substring(start+1, posn);
            if (resourceTypes.contains(fragment)) {
                // we've hit the resource type in the URL, so it's time to stop
                last = start+1; // so we include the last '/' in the
            } else {
                // try the next field
                start = posn;
            }
        }

        if (last < 0) {
            // Didn't see the resource-type anywhere in the URL, but need to
            // make sure we test the final field which may not be terminated
            // by a '/'
            posn = uri.length();
            String fragment = uri.substring(start+1, posn);
            if (resourceTypes.contains(fragment)) {
                last = start+1; // skip the final resource-type field
            } else {
                last = posn; // the whole string
            }
        }

        if (last >= 0) {
            String sb = uri.substring(0, last);
            if (!sb.endsWith("/")) {
                // make sure we always have a final "/" to make life easier downstream.
                sb = sb + "/";
            }
            result = sb;
        } else {
            // log locally, do not leak in exception...might contain server name/ip secrets
            logger.severe("FHIRRequestContext.originalRequestUri is invalid: " + uri);
            throw new FHIRSearchException("Invalid originalRequestUri in FHIRRequestContext. Details in log.");
        }

        return result;
    }
}