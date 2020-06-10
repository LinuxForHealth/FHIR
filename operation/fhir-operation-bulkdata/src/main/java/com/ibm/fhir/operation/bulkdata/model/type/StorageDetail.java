/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Import Storage Detail
 */
public class StorageDetail {
    private String type;
    private List<String> contentEncodings = new ArrayList<>();

    public StorageDetail(String type, String... contentEncodings) {
        super();
        this.type             = type;
        this.contentEncodings = new ArrayList<>();
        this.contentEncodings.addAll(Arrays.asList(contentEncodings));
    }

    public StorageDetail(String type, List<String> contentEncodings) {
        super();
        this.type             = type;
        this.contentEncodings = new ArrayList<>();
        this.contentEncodings.addAll(contentEncodings);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getContentEncodings() {
        return new ArrayList<>(contentEncodings);
    }

    public void addContentEncodings(String contentEncoding) {
        this.contentEncodings.add(contentEncoding);
    }
}