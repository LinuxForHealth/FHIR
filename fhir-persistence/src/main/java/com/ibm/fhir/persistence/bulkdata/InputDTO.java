/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.bulkdata;

/**
 * Input DTO
 */
public class InputDTO {
    private String type;
    private String url;

    public InputDTO(String type, String url) {
        super();
        this.type = type;
        this.url  = url;
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
}