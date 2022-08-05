/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

import org.linuxforhealth.fhir.server.util.FHIRUrlParser;

/**
 * Executes a VREAD (version read) operation on the visitor
 */
public class FHIRRestInteractionVRead extends FHIRRestInteractionBase {

    private final String type;
    private final String id;
    private final String versionId;
    private final MultivaluedMap<String, String> queryParameters;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param type
     * @param id
     * @param versionId
     * @param queryParameters
     */
    public FHIRRestInteractionVRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            String type, String id, String versionId, MultivaluedMap<String, String> queryParameters) {
        super(entryIndex, requestDescription, requestURL);
        this.type = type;
        this.id = id;
        this.versionId = versionId;
        this.queryParameters = queryParameters;
    }

    @Override
    public void process(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.doVRead(getEntryIndex(), getRequestDescription(), getRequestURL(), getAccumulatedTime(), type, id,
                versionId, queryParameters);
    }
}