/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a collection of search parameters extracted from a FHIR resource
 * held in a form that is easy to serialize/deserialize to a wire format
 * (e.g. JSON) for sending to a remote/async indexing service.
 * @implNote because we want to serialize/deserialize this object
 * as JSON, we need to keep it simple
 */
public class SearchParametersTransport {

    // The FHIR resource type name
    private String resourceType;

    // The logical id of the resource
    private String logicalId;

    // The database identifier assigned to this resource
    private long logicalResourceId;
    
    // The current version of the resource
    private int versionId;

    // The parameter hash computed for this set of parameters
    private String parameterHash;

    // The last_updated time
    private Instant lastUpdated;

    // The key value used for sharding the data when using a distributed database
    private String requestShard;

    // The lists of unique parameter values
    private List<StringParameter> stringValues;
    private List<NumberParameter> numberValues;
    private List<QuantityParameter> quantityValues;
    private List<TokenParameter> tokenValues;
    private List<DateParameter> dateValues;
    private List<LocationParameter> locationValues;
    private List<TagParameter> tagValues;
    private List<ProfileParameter> profileValues;
    private List<SecurityParameter> securityValues;
    private List<ReferenceParameter> refValues;

    /**
     * Factory method to create a {@link Builder} instance
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("resourceType[");
        result.append(resourceType);
        result.append("] ");
        result.append("logicalId[");
        result.append(logicalId);
        result.append("] ");
        result.append("versionId[");
        result.append(versionId);
        result.append("] ");
        result.append("parameterHash[");
        result.append(parameterHash);
        result.append("] ");
        result.append("lastUpdated[");
        result.append(lastUpdated);
        result.append("] ");
        return result.toString();
    }

    /**
     * A builder to make it easier to construct a {@link SearchParametersTransport}
     */
    public static class Builder {
        // values are collected into sets because we want to store only unique values
        private Set<StringParameter> stringValues = new HashSet<>();
        private Set<NumberParameter> numberValues = new HashSet<>();
        private Set<QuantityParameter> quantityValues = new HashSet<>();
        private Set<TokenParameter> tokenValues = new HashSet<>();
        private Set<DateParameter> dateValues = new HashSet<>();
        private Set<LocationParameter> locationValues = new HashSet<>();
        private Set<TagParameter> tagValues = new HashSet<>();
        private Set<ProfileParameter> profileValues = new HashSet<>();
        private Set<SecurityParameter> securityValues = new HashSet<>();
        private Set<ReferenceParameter> refValues = new HashSet<>();
    
        private String resourceType;
        private String logicalId;
        private long logicalResourceId = -1;
        private String requestShard;
        private int versionId;
        private String parameterHash;
        private Instant lastUpdated;

        /**
         * Set the resourceType
         * @param resourceType
         * @return
         */
        public Builder withResourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        /**
         * Set the parameterHash
         * @param hash
         * @return
         */
        public Builder withParameterHash(String hash) {
            this.parameterHash = hash;
            return this;
        }

        public Builder withLastUpdated(Instant lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        /**
         * Set the logicalId
         * @param logicalId
         * @return
         */
        public Builder withLogicalId(String logicalId) {
            this.logicalId = logicalId;
            return this;
        }

        /**
         * Set the versionId
         * @param versionId
         * @return
         */
        public Builder withVersionId(int versionId) {
            this.versionId = versionId;
            return this;
        }

        /**
         * Set the logicalResourceId
         * @param logicalResourceId
         * @return
         */
        public Builder withLogicalResourceId(long logicalResourceId) {
            this.logicalResourceId = logicalResourceId;
            return this;
        }

        /**
         * Set the shardKey
         * @param shardKey
         * @return
         */
        public Builder withRequestShard(String shardValue) {
            this.requestShard = shardValue;
            return this;
        }

        /**
         * Add a string parameter value
         * @param value
         * @return
         */
        public Builder addStringValue(StringParameter value) {
            stringValues.add(value);
            return this;
        }

        /**
         * Add a number parameter value
         * @param value
         * @return
         */
        public Builder addNumberValue(NumberParameter value) {
            numberValues.add(value);
            return this;
        }

        /**
         * Add a quantity parameter value
         * @param value
         * @return
         */
        public Builder addQuantityValue(QuantityParameter value) {
            quantityValues.add(value);
            return this;
        }

        /**
         * Add a token parameter value
         * @param value
         * @return
         */
        public Builder addTokenValue(TokenParameter value) {
            tokenValues.add(value);
            return this;
        }

        /**
         * Add a reference parameter value
         * @param value
         * @return
         */
        public Builder addReferenceValue(ReferenceParameter value) {
            refValues.add(value);
            return this;
        }

        /**
         * Add a tag parameter value
         * @param value
         * @return
         */
        public Builder addTagValue(TagParameter value) {
            tagValues.add(value);
            return this;
        }

        /**
         * Add a profile parameter value
         * @param value
         * @return
         */
        public Builder addProfileValue(ProfileParameter value) {
            profileValues.add(value);
            return this;
        }

        /**
         * Add a security parameter value
         * @param value
         * @return
         */
        public Builder addSecurityValue(SecurityParameter value) {
            securityValues.add(value);
            return this;
        }

        /**
         * Add a date parameter value
         * @param value
         * @return
         */
        public Builder addDateValue(DateParameter value) {
            dateValues.add(value);
            return this;
        }

        /**
         * Add a location parameter value
         * @param value
         * @return
         */
        public Builder addLocationValue(LocationParameter value) {
            locationValues.add(value);
            return this;
        }

        /**
         * Builder a new {@link SearchParametersTransport} instance based on the current state
         * of this {@link Builder}.
         * @return
         */
        public SearchParametersTransport build() {
            if (this.logicalResourceId < 0) {
                throw new IllegalStateException("Must set logicalResourceId");
            }
            if (this.resourceType == null) {
                throw new IllegalStateException("Must set resourceType");
            }
            if (this.logicalId == null) {
                throw new IllegalStateException("Must set logicalId");
            }

            SearchParametersTransport result = new SearchParametersTransport();
            result.resourceType = this.resourceType;
            result.logicalId = this.logicalId;
            result.logicalResourceId = this.logicalResourceId;
            result.setVersionId(this.versionId);
            result.setRequestShard(this.requestShard);
            result.setParameterHash(this.parameterHash);
            result.setLastUpdated(this.lastUpdated);

            if (this.stringValues.size() > 0) {
                result.stringValues = new ArrayList<>(this.stringValues);
            }
            if (this.numberValues.size() > 0) {
                result.numberValues = new ArrayList<>(this.numberValues);
            }
            if (this.quantityValues.size() > 0) {
                result.quantityValues = new ArrayList<>(this.quantityValues);
            }
            if (this.tokenValues.size() > 0) {
                result.tokenValues = new ArrayList<>(this.tokenValues);
            }
            if (this.dateValues.size() > 0) {
                result.dateValues = new ArrayList<>(this.dateValues);
            }
            if (this.locationValues.size() > 0) {
                result.locationValues = new ArrayList<>(this.locationValues);
            }
            if (this.tagValues.size() > 0) {
                result.setTagValues(new ArrayList<>(this.tagValues));
            }
            if (this.profileValues.size() > 0) {
                result.setProfileValues(new ArrayList<>(this.profileValues));
            }
            if (this.securityValues.size() > 0) {
                result.setSecurityValues(new ArrayList<>(this.securityValues));
            }
            if (this.refValues.size() > 0) {
                result.setRefValues(new ArrayList<>(this.refValues));
            }
            return result;
        }
    }
    
    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    
    /**
     * @param resourceType the resourceType to set
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    
    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }

    
    /**
     * @param logicalId the logicalId to set
     */
    public void setLogicalId(String logicalId) {
        this.logicalId = logicalId;
    }

    
    /**
     * @return the logicalResourceId
     */
    public long getLogicalResourceId() {
        return logicalResourceId;
    }

    
    /**
     * @param logicalResourceId the logicalResourceId to set
     */
    public void setLogicalResourceId(long logicalResourceId) {
        this.logicalResourceId = logicalResourceId;
    }

    
    /**
     * @return the stringValues
     */
    public List<StringParameter> getStringValues() {
        return stringValues;
    }

    
    /**
     * @param stringValues the stringValues to set
     */
    public void setStringValues(List<StringParameter> stringValues) {
        this.stringValues = stringValues;
    }

    
    /**
     * @return the numberValues
     */
    public List<NumberParameter> getNumberValues() {
        return numberValues;
    }

    
    /**
     * @param numberValues the numberValues to set
     */
    public void setNumberValues(List<NumberParameter> numberValues) {
        this.numberValues = numberValues;
    }

    
    /**
     * @return the quantityValues
     */
    public List<QuantityParameter> getQuantityValues() {
        return quantityValues;
    }

    
    /**
     * @param quantityValues the quantityValues to set
     */
    public void setQuantityValues(List<QuantityParameter> quantityValues) {
        this.quantityValues = quantityValues;
    }

    
    /**
     * @return the tokenValues
     */
    public List<TokenParameter> getTokenValues() {
        return tokenValues;
    }

    
    /**
     * @param tokenValues the tokenValues to set
     */
    public void setTokenValues(List<TokenParameter> tokenValues) {
        this.tokenValues = tokenValues;
    }

    
    /**
     * @return the dateValues
     */
    public List<DateParameter> getDateValues() {
        return dateValues;
    }

    
    /**
     * @param dateValues the dateValues to set
     */
    public void setDateValues(List<DateParameter> dateValues) {
        this.dateValues = dateValues;
    }

    
    /**
     * @return the locationValues
     */
    public List<LocationParameter> getLocationValues() {
        return locationValues;
    }

    
    /**
     * @param locationValues the locationValues to set
     */
    public void setLocationValues(List<LocationParameter> locationValues) {
        this.locationValues = locationValues;
    }


    /**
     * @return the requestShard
     */
    public String getRequestShard() {
        return requestShard;
    }


    /**
     * @param shardValue the request shard value to set
     */
    public void setRequestShard(String shardValue) {
        this.requestShard = shardValue;
    }


    /**
     * @return the tagValues
     */
    public List<TagParameter> getTagValues() {
        return tagValues;
    }


    /**
     * @param tagValues the tagValues to set
     */
    public void setTagValues(List<TagParameter> tagValues) {
        this.tagValues = tagValues;
    }


    /**
     * @return the profileValues
     */
    public List<ProfileParameter> getProfileValues() {
        return profileValues;
    }


    /**
     * @param profileValues the profileValues to set
     */
    public void setProfileValues(List<ProfileParameter> profileValues) {
        this.profileValues = profileValues;
    }

    /**
     * @return the securityValues
     */
    public List<SecurityParameter> getSecurityValues() {
        return securityValues;
    }

    /**
     * @param profileValues the profileValues to set
     */
    public void setSecurityValues(List<SecurityParameter> securityValues) {
        this.securityValues = securityValues;
    }


    /**
     * @return the versionId
     */
    public int getVersionId() {
        return versionId;
    }


    /**
     * @param versionId the versionId to set
     */
    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    /**
     * @return the parameterHash
     */
    public String getParameterHash() {
        return parameterHash;
    }

    /**
     * @param parameterHash the parameterHash to set
     */
    public void setParameterHash(String parameterHash) {
        this.parameterHash = parameterHash;
    }

    /**
     * @return the lastUpdated (UTC)
     */
    public Instant getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set.
     */
    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Convenience function to get the lastUpdated time as an Instant. All our times are
     * always UTC.
     * @return
     */
    public Instant getLastUpdatedInstant() {
        return Instant.from(lastUpdated.atOffset(ZoneOffset.UTC));
    }

    /**
     * @return the refValues
     */
    public List<ReferenceParameter> getRefValues() {
        return refValues;
    }

    /**
     * @param refValues the refValues to set
     */
    public void setRefValues(List<ReferenceParameter> refValues) {
        this.refValues = refValues;
    }
}
