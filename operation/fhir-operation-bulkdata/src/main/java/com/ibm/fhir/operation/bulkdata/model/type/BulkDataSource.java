/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model.type;

/**
 * Used with $import operation to outline the type and url
 */
public class BulkDataSource {

    private String type;
    private String url;
    private String originalLocation = "";

    public BulkDataSource(String type, String url) {
        super();
        this.type = type;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(String originalLocation) {
        this.originalLocation = originalLocation;
    }

    @Override
    public String toString() {
        return "BulkDataSource [type=" + type + ", url=" + url + ", originalLocation=" + originalLocation + "]";
    }
}