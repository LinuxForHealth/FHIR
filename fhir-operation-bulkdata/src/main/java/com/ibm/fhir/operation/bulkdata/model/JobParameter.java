/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model;

import java.io.IOException;

import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

import com.ibm.fhir.exception.FHIROperationException;

/**
 * Common Configuration Parameters for the Job Request/Response.
 */
public class JobParameter {
    private String fhirResourceType;
    private String fhirSearchFromDate;
    private String fhirTenant;
    private String fhirDataStoreId;
    private String fhirPatientGroupId;
    private String fhirTypeFilters;

    private String cosBucketName;
    private String cosLocation;
    private String cosEndpointUrl;
    private String cosCredentialIbm;
    private String cosApiKey;
    private String cosSrvInstId;
    private String cosBucketPathPrefix;

    public String getFhirPatientGroupId() {
        return fhirPatientGroupId;
    }

    public void setFhirPatientGroupId(String fhirPatientGroupId) {
        this.fhirPatientGroupId = fhirPatientGroupId;
    }

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

    public String getFhirTypeFilters() {
        return fhirTypeFilters;
    }

    public void setFhirTypeFilters(String fhirTypeFilters) {
        this.fhirTypeFilters = fhirTypeFilters;
    }

    /**
     * Generates JSON from this object.
     */
    public static class Writer {
        private Writer() {
            // No Operation
        }

        /**
         * @param generator
         * @param parameter     job parameter details.
         * @param withSensitive indicates sensitive information should or should not be included.
         * @throws IOException
         */
        public static void generate(JsonGenerator generator, JobParameter parameter, boolean withSensitive)
                throws IOException {
            if (withSensitive) {
                if (parameter.getCosApiKey() != null) {
                    generator.write("cos.api.key", parameter.getCosApiKey());
                }
            }

            if (withSensitive) {
                if (parameter.getCosBucketName() != null) {
                    generator.write("cos.bucket.name", parameter.getCosBucketName());
                }
            }

            if (parameter.getCosCredentialIbm() != null) {
                generator.write("cos.credential.ibm", parameter.getCosCredentialIbm());
            }

            if (withSensitive) {
                if (parameter.getCosEndpointUrl() != null) {
                    generator.write("cos.endpointurl", parameter.getCosEndpointUrl());
                }
            }
            if (parameter.getCosLocation() != null) {
                generator.write("cos.location", parameter.getCosLocation());
            }

            if (withSensitive) {
                if (parameter.getCosSrvInstId() != null) {
                    generator.write("cos.srvinst.id", parameter.getCosSrvInstId());
                }
            }

            if (parameter.getFhirResourceType() != null) {
                generator.write("fhir.resourcetype", parameter.getFhirResourceType());
            }

            if (parameter.getFhirSearchFromDate() != null) {
                generator.write("fhir.search.fromdate", parameter.getFhirSearchFromDate());
            }

            if (parameter.getFhirTenant() != null) {
                generator.write("fhir.tenant", parameter.getFhirTenant());
            }

            if (parameter.getFhirPatientGroupId() != null) {
                generator.write("fhir.search.patientgroupid", parameter.getFhirPatientGroupId());
            }

            if (withSensitive) {
                if (parameter.getCosBucketPathPrefix() != null) {
                    generator.write("cos.bucket.pathprefix", parameter.getCosBucketPathPrefix());
                }
            }

            if (parameter.getFhirDataStoreId() != null) {
                generator.write("fhir.datastoreid", parameter.getFhirDataStoreId());
            }

            if (parameter.getFhirTypeFilters() != null) {
                generator.write("fhir.typeFilters", parameter.getFhirTypeFilters());
            }
            generator.writeEnd();
        }
    }

    /**
     * common build parameters for JobParameters
     */
    public interface Builder {
        public Builder fhirResourceType(String fhirResourceType);

        public Builder fhirSearchFromDate(String fhirSearchFromDate);

        public Builder cosBucketName(String cosBucketName);

        public Builder cosLocation(String cosLocation);

        public Builder cosEndpointUrl(String cosEndpointUrl);

        public Builder cosCredentialIbm(String cosCredentialIbm);

        public Builder cosApiKey(String cosApiKey);

        public Builder cosSrvInstId(String cosSrvInstId);

        public Builder fhirTenant(String fhirTenant);

        public Builder fhirDataStoreId(String fhirDataStoreId);

        public Builder fhirPatientGroupId(String fhirPatientGroupId);

        public Builder cosBucketPathPrefix(String cosBucketPathPrefix);

        public Builder fhirTypeFilters(String fhirTypeFilters);
    }

    public static class Parser {
        private Parser() {
            // No Op
        }

        public static void parse(Builder builder, JsonObject obj) throws FHIROperationException {
            if (obj.containsKey("fhir.resourcetype")) {
                String fhirResourceType = obj.getString("fhir.resourcetype");
                builder.fhirResourceType(fhirResourceType);
            }

            if (obj.containsKey("fhir.search.fromdate")) {
                String fhirSearchFromdate = obj.getString("fhir.search.fromdate");
                builder.fhirSearchFromDate(fhirSearchFromdate);
            }

            if (obj.containsKey("cos.bucket.name")) {
                String cosBucketName = obj.getString("cos.bucket.name");
                builder.cosBucketName(cosBucketName);
            }

            if (obj.containsKey("cos.location")) {
                String cosLocation = obj.getString("cos.location");
                builder.cosLocation(cosLocation);
            }

            if (obj.containsKey("cos.endpointurl")) {
                String cosEndpointUrl = obj.getString("cos.endpointurl");
                builder.cosEndpointUrl(cosEndpointUrl);
            }

            if (obj.containsKey("cos.credential.ibm")) {
                String cosCredentialIbm = obj.getString("cos.credential.ibm");
                builder.cosCredentialIbm(cosCredentialIbm);
            }

            if (obj.containsKey("cos.api.key")) {
                String cosApiKey = obj.getString("cos.api.key");
                builder.cosApiKey(cosApiKey);
            }

            if (obj.containsKey("cos.srvinst.id")) {
                String cosSrvinstId = obj.getString("cos.srvinst.id");
                builder.cosSrvInstId(cosSrvinstId);
            }

            if (obj.containsKey("fhir.tenant")) {
                String fhirTenant = obj.getString("fhir.tenant");
                builder.fhirTenant(fhirTenant);
            }

            if (obj.containsKey("fhir.datastoreid")) {
                String fhirDataStoreId = obj.getString("fhir.datastoreid");
                builder.fhirDataStoreId(fhirDataStoreId);
            }

            if (obj.containsKey("cos.bucket.pathprefix")) {
                String cosBucketPathPrefix = obj.getString("cos.bucket.pathprefix");
                builder.cosBucketPathPrefix(cosBucketPathPrefix);
            }

            if (obj.containsKey("fhir.typeFilters")) {
                String fhirTypeFilters = obj.getString("fhir.typeFilters");
                builder.fhirTypeFilters(fhirTypeFilters);
            }

            if (obj.containsKey("fhir.search.patientgroupid")) {
                String fhirPatientGroupId = obj.getString("fhir.search.patientgroupid");
                builder.fhirPatientGroupId(fhirPatientGroupId);
            }
        }
    }

    @Override
    public String toString() {
        return "JobParameter [fhirResourceType=" + fhirResourceType + ", fhirSearchFromDate=" + fhirSearchFromDate
                + ", fhirTenant=" + fhirTenant + ", fhirDataStoreId=" + fhirDataStoreId + ", fhirPatientGroupId="
                + fhirPatientGroupId + ", fhirTypeFilters=" + fhirTypeFilters + ", cosBucketName=" + cosBucketName
                + ", cosLocation=" + cosLocation + ", cosEndpointUrl=" + cosEndpointUrl + ", cosCredentialIbm="
                + cosCredentialIbm + ", cosApiKey=" + cosApiKey + ", cosSrvInstId=" + cosSrvInstId
                + ", cosBucketPathPrefix=" + cosBucketPathPrefix + "]";
    }
}