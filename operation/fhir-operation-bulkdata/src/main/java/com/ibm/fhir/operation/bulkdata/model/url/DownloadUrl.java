/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model.url;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.HttpMethod;

import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.s3.S3HostStyle;

/**
 * Based on the IBM Cloud Documentation
 *
 * @see https://cloud.ibm.com/docs/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url#presign-url-put-python
 */
public class DownloadUrl {

    private static final Logger logger = Logger.getLogger(DownloadUrl.class.getName());

    private static final String HTTP_METHOD = HttpMethod.GET;

    private static final MessageDigest digest = createSigningDigest();

    private String server = null;
    private String region = null;
    private String bucketName = null;
    private String cosBucketPathPrefix = null;
    private String objectKey = null;
    private String accessKey = null;
    private String secretKey = null;
    private boolean presigned = false;
    private boolean path = true;
    private ZonedDateTime time = ZonedDateTime.now(ZoneOffset.UTC);

    public DownloadUrl(String server, String region, String bucketName, String cosBucketPathPrefix, String objectKey,
            String accessKey, String secretKey, boolean presigned, S3HostStyle hostStyle) {
        this.server = server;
        this.region = region;
        this.bucketName = bucketName;
        this.cosBucketPathPrefix = cosBucketPathPrefix;
        this.objectKey = objectKey;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.presigned = presigned;
        this.path = S3HostStyle.PATH.equals(hostStyle);
    }

    /*
     * gets the digest.
     * @return
     */
    private static MessageDigest createSigningDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            logger.warning("Message Digest is not found, and subsequent signing will fail, defaulting back to unsigned urls");
            throw new IllegalArgumentException(e);
        }
    }

    public String getUrl() {
        if (presigned) {
            try {
                return this.getSignedUrl();
            } catch (Exception e) {
                logger.warning("Unable to sign url, switching to unsigned url.");
                return this.getUnsignedUrl();
            }
        } else {
            return this.getUnsignedUrl();
        }
    }

    public String getUnsignedUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append(server);
        builder.append('/');
        builder.append(bucketName);
        if (path) {
            builder.append('/');
            builder.append(cosBucketPathPrefix);
        }
        builder.append('/');
        builder.append(objectKey);
        builder.append(".ndjson");
        return builder.toString();
    }

    public String getSignedUrl() throws Exception {
        objectKey += ".ndjson";
        // assemble the standardized request

        String datestamp = time.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timestamp = datestamp + "T" + time.format(DateTimeFormatter.ofPattern("HHmmss")) + "Z";

        String expirySeconds = String.valueOf(ConfigurationFactory.getInstance().getPresignedUrlExpiry());

        String standardizedQuerystring = "X-Amz-Algorithm=AWS4-HMAC-SHA256" +
                "&X-Amz-Credential=" + URLEncoder.encode(accessKey + "/" + datestamp + "/" + region + "/s3/aws4_request", StandardCharsets.UTF_8.toString()) +
                "&X-Amz-Date=" + timestamp +
                "&X-Amz-Expires=" + expirySeconds +
                "&X-Amz-SignedHeaders=host";

        // VirtualHost/Path - we inline the bucket if it's path based access.
        String bucketResource = "";
        if (path) {
            bucketResource = "/" + bucketName;
        }
        String standardizedResource = bucketResource + "/" + cosBucketPathPrefix + "/"+ objectKey;

        String payloadHash = "UNSIGNED-PAYLOAD";
        String standardizedHeaders = "host:" + server.replace("https://", "");
        String signedHeaders = "host";

        String standardizedRequest = HTTP_METHOD + "\n" +
                standardizedResource + "\n" +
                standardizedQuerystring + "\n" +
                standardizedHeaders + "\n" +
                "\n" +
                signedHeaders + "\n" +
                payloadHash;

        // assemble string-to-sign
        String hashingAlgorithm = "AWS4-HMAC-SHA256";
        String credentialScope = datestamp + "/" + region + "/" + "s3" + "/" + "aws4_request";
        String sts = hashingAlgorithm + "\n" +
                timestamp + "\n" +
                credentialScope + "\n" +
                hashHex(standardizedRequest);

        // generate the signature
        byte[] signatureKey = createSignatureKey(secretKey, datestamp, region, "s3");
        String signature = toHexString(hash(signatureKey, sts));

        // There are two styles of endpoint - virtual host and path.
        // we now selectively generate the externalURL for the virtualhost endpoint.
        String bucketSegment = "";
        if (path) {
            bucketSegment = bucketName + "/";
        }
        // create and send the request
        return server + "/" +
                bucketSegment +
                cosBucketPathPrefix + "/" +
                objectKey + "?" +
                standardizedQuerystring +
                "&X-Amz-Signature=" +
                signature;
    }

    public byte[] hash(byte[] key, String msg)  throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        return mac.doFinal(msg.getBytes("UTF8"));
    }

    private String toHexString(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

    public String hashHex(String msg) throws Exception {
        byte[] encodedhash = digest.digest(msg.getBytes(StandardCharsets.UTF_8));
        return toHexString(encodedhash);
    }

    // region is a wildcard value that takes the place of the AWS region value
    // as COS doesn"t use the same conventions for regions, this parameter can accept any string
    public byte[] createSignatureKey(String key, String datestamp, String region, String service) throws Exception {
        byte[] keyDate = hash(("AWS4" + key).getBytes("UTF8"), datestamp);
        byte[] keyString = hash(keyDate, region);
        byte[] keyService = hash(keyString, service);
        byte[] keySigning = hash(keyService, "aws4_request");
        return keySigning;
    }
}