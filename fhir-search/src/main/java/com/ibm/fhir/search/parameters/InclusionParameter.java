/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import com.ibm.fhir.search.SearchConstants.Modifier;

/**
 * Instances of this class encapsulate data elements related to the FHIR _include and _revinclude search result
 * parameters.
 */
public class InclusionParameter {

    private String joinResourceType;
    private String searchParameter;
    private String searchParameterTargetType;
    private Modifier modifier;
    private boolean userSpecifiedTargetType;

    public InclusionParameter(String joinRt, String searchParm, String searchParmTargetType, Modifier modifier,
        boolean userSpecifiedTargetType) {
        super();
        this.joinResourceType = joinRt;
        this.searchParameter = searchParm;
        this.searchParameterTargetType = searchParmTargetType;
        this.modifier = modifier;
        this.userSpecifiedTargetType = userSpecifiedTargetType;
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

    public Modifier getModifier() {
        return modifier;
    }

    public boolean isIterate() {
        return Modifier.ITERATE.equals(modifier);
    }

    
    public boolean isUserSpecifiedTargetType() {
        return userSpecifiedTargetType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((joinResourceType == null) ? 0 : joinResourceType.hashCode());
        result = prime * result + ((searchParameter == null) ? 0 : searchParameter.hashCode());
        result = prime * result + ((searchParameterTargetType == null) ? 0 : searchParameterTargetType.hashCode());
        result = prime * result + ((modifier == null) ? 0 : modifier.hashCode());
        result = prime * result + (userSpecifiedTargetType ? 1 : 0);
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
        if (modifier == null) {
            if (other.modifier != null) {
                return false;
            }
        } else if (!modifier.equals(other.modifier)) {
            return false;
        }
        if (userSpecifiedTargetType != other.userSpecifiedTargetType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InclusionParameter [joinResourceType=" + joinResourceType + ", searchParameter=" + searchParameter + ", searchParameterTargetType="
                + searchParameterTargetType + ", modifier=" + modifier.toString() + ", userSpecifiedTargetType=" + userSpecifiedTargetType + "]";
    }

}
