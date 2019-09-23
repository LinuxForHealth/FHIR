/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.model;

/**
 * Representation of the CADF Endpoint object. The Endpoint type is used to
 * provide information about a resource's location on a network
 */
public final class CadfEndpoint {
    private final String url;
    private final String name;
    private final String port;

    /**
     * Construct a CADF Endpoint object
     * 
     * @param url  -- String. Required. The network address of the endpoint; for
     *             IP-based addresses. Note: The IP address value may include the
     *             port number as part of the syntax as an alternative to separating
     *             it out into the optional port attribute
     * @param name - String. Optional. Provides a logical name for the object.
     * @param port - String. Optional. An optional property to provide the port
     *             value separate from the address property, intended to facilitate
     *             a consistent means to query resource information on a specific
     *             port.
     */
    public CadfEndpoint(String url, String name, String port) {
        this.url = url;
        this.name = name;
        this.port = port;
    }

    public String getUrl() {
        return this.url;
    }

    public String getName() {
        return this.name;
    }

    public String getPort() {
        return this.port;
    }
}
