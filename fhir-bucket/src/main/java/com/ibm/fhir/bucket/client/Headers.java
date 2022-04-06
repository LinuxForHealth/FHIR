/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.client;

/**
 * Header constants used in iBM FHIR Server requests
 */
public class Headers {
    public static final String HOST_HEADER= "Host";
    public static final String AUTH_HEADER = "Authorization";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String ACCEPT_HEADER = "Accept";
    public static final String TENANT_HEADER = "X-FHIR-TENANT-ID";
    public static final String PREFER_HEADER = "Prefer";
}