/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

/**
 * Instances of this class encapsulate data elements related to the FHIR _include and _revinclude search result
 * parameters.
 * 
 * @author markd
 * @author pbastide
 *
 */
public class InclusionParameter {

    private String joinResourceType;
    private String searchParameter;
    private String searchParameterTargetType;

    public InclusionParameter(String joinRt, String searchParm, String searchParmTargetType) {
        super();
        this.joinResourceType = joinRt;
        this.searchParameter = searchParm;
        this.searchParameterTargetType = searchParmTargetType;
    }

    public String getJoinResourceType() {
        return joinResourceType;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public String getSearchParameterTargetType() {
        return searchParameterTargetType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((joinResourceType == null) ? 0 : joinResourceType.hashCode());
        result = prime * result + ((searchParameter == null) ? 0 : searchParameter.hashCode());
        result = prime * result + ((searchParameterTargetType == null) ? 0 : searchParameterTargetType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InclusionParameter other = (InclusionParameter) obj;
        if (joinResourceType == null) {
            if (other.joinResourceType != null) {
                return false;
            }
        } else if (!joinResourceType.equals(other.joinResourceType)) {
            return false;
        }
        if (searchParameter == null) {
            if (other.searchParameter != null) {
                return false;
            }
        } else if (!searchParameter.equals(other.searchParameter)) {
            return false;
        }
        if (searchParameterTargetType == null) {
            if (other.searchParameterTargetType != null) {
                return false;
            }
        } else if (!searchParameterTargetType.equals(other.searchParameterTargetType)) {
            return false;
        }
        return true;
    }

    /*
     * issue 49: added toString
     */
    @Override
    public String toString() {
        return "InclusionParameter [joinResourceType=" + joinResourceType + ", searchParameter=" + searchParameter + ", searchParameterTargetType="
                + searchParameterTargetType + "]";
    }

}
