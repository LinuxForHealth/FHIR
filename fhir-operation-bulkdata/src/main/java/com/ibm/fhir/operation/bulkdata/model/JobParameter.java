/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model;

public class JobParameter {


    String fhirResourceType;
    String fhirSearchFromDate;
    String fhirTenant;
    String fhirDataStoreId;

    String cosBucketName;
    String cosLocation;
    String cosEndpointUrl;
    String cosCredentialIbm;
    String cosApiKey;
    String cosSrvInstId;
    String cosBucketPathPrefix;

    public String getCosBucketPathPrefix() {
        return cosBucketPathPrefix;
    }

    public void setCosBucketPathPrefix(String cosBucketPathPrefix) {
        this.cosBucketPathPrefix = cosBucketPathPrefix;
    }

    public String getFhirResourceType() {
        return fhirResourceType;
    }

    public void setFhirResourceType(String fhirResourceType) {
        this.fhirResourceType = fhirResourceType;
    }

    public String getFhirSearchFromDate() {
        return fhirSearchFromDate;
    }

    public void setFhirSearchFromDate(String fhirSearchFromDate) {
        this.fhirSearchFromDate = fhirSearchFromDate;
    }

    public String getCosBucketName() {
        return cosBucketName;
    }

    public void setCosBucketName(String cosBucketName) {
        this.cosBucketName = cosBucketName;
    }

    public String getCosLocation() {
        return cosLocation;
    }

    public void setCosLocation(String cosLocation) {
        this.cosLocation = cosLocation;
    }

    public String getCosEndpointUrl() {
        return cosEndpointUrl;
    }

    public void setCosEndpointUrl(String cosEndpointUrl) {
        this.cosEndpointUrl = cosEndpointUrl;
    }

    public String getCosCredentialIbm() {
        return cosCredentialIbm;
    }

    public void setCosCredentialIbm(String cosCredentialIbm) {
        this.cosCredentialIbm = cosCredentialIbm;
    }

    public String getCosApiKey() {
        return cosApiKey;
    }

    public void setCosApiKey(String cosApiKey) {
        this.cosApiKey = cosApiKey;
    }

    public String getCosSrvInstId() {
        return cosSrvInstId;
    }

    public void setCosSrvInstId(String cosSrvInstId) {
        this.cosSrvInstId = cosSrvInstId;
    }

    public String getFhirTenant() {
        return fhirTenant;
    }

    public void setFhirTenant(String fhirTenant) {
        this.fhirTenant = fhirTenant;
    }

    public String getFhirDataStoreId() {
        return fhirDataStoreId;
    }

    public void setFhirDataStoreId(String fhirDataStoreId) {
        this.fhirDataStoreId = fhirDataStoreId;
    }


}
