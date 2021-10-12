/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.client;


/**
 * Constants related to our COS connection
 */
public class COSConstants {
    // the IBM COS API key or S3 access key.
    public static final String COS_API_KEY = "apiKey";

    // the IBM COS service instance id or S3 secret key.
    public static final String COS_SRVINSTID = "srvinstid";

    // the IBM COS or S3 End point URL.
    public static final String COS_ENDPOINT_URL = "endpointUrl";

    // the IBM COS or S3 location.
    public static final String COS_LOCATION = "location";

    // the IBM COS or S3 bucket name to import from.
    public static final String COS_BUCKET_NAME = "bucketName";

    // if use IBM credential(Y/N), default(Y).
    public static final String COS_CREDENTIAL_IBM = "cosCredentialIbm";

    public static final String COS_REQUEST_TIMEOUT = "requestTimeout";
    
    public static final String COS_SOCKET_TIMEOUT = "socketTimeout";
    
    // The max keys to return per list objects request
    public static final String COS_MAX_KEYS = "maxKeys";

}
