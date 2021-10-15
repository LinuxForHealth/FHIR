/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.client;


/**
 * Constants related to our COS connection for standalone applications 
 * configured from a foo.properties file.
 */
public class COSPropertyConstants {
    // the IBM COS API key or S3 access key.
    public static final String COS_API_KEY = "api.key";

    // the IBM COS service instance id or S3 secret key.
    public static final String COS_SRVINSTID = "srv.instid";

    // the IBM COS or S3 End point URL.
    public static final String COS_ENDPOINT_URL = "endpoint.url";

    // the IBM COS or S3 location.
    public static final String COS_LOCATION = "location";

    // the IBM COS or S3 bucket name to import from.
    public static final String COS_BUCKET_NAME = "bucket.name";

    // if use IBM credential(Y/N), default(Y).
    public static final String COS_CREDENTIAL_IBM = "cos.credential.ibm";

    public static final String COS_REQUEST_TIMEOUT = "request.timeout";
    
    public static final String COS_SOCKET_TIMEOUT = "socket.timeout";
    
    // The max keys to return per list objects request
    public static final String COS_MAX_KEYS = "max.keys";
}