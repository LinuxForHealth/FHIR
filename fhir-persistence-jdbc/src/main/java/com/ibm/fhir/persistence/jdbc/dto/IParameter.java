/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This class defines the Data Transfer Object representing a row in the FHIR Parameter table.
 */
public interface IParameter {
    public void setId(long id);

    public long getId();

    public void setName(String name);

    public String getName();

    public String getResourceType();
    public void setResourceType(String resourceType);

    public long getResourceId();
    public void setResourceId(long resourceId);

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    public void accept(IParameterVisitor visitor) throws FHIRPersistenceException;

    /**
     * @return the base
     */
    public String getBase();

    /**
     * @param base the base to set
     */
    public void setBase(String base);
}
