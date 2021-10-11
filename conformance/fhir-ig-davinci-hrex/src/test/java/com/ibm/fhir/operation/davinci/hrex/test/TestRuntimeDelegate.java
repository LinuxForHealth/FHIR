/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

/**
 * Test Delegate to avoid stacktrace when running Unit Tests
 */
public class TestRuntimeDelegate extends RuntimeDelegate {

    @Override
    public ResponseBuilder createResponseBuilder() {

        return null;
    }

    @Override
    public UriBuilder createUriBuilder() {

        return null;
    }

    @Override
    public VariantListBuilder createVariantListBuilder() {

        return null;
    }

    @Override
    public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException, UnsupportedOperationException {

        return null;
    }

    @Override
    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) throws IllegalArgumentException {

        return null;
    }

    @Override
    public Builder createLinkBuilder() {

        return null;
    }

}
