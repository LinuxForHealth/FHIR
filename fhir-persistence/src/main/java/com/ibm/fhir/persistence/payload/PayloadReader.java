/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.payload;

import java.io.InputStream;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Strategy for reading a resource from a stream
 */
public interface PayloadReader {

    /**
     * Read the resource of type T from the {@link InputStream}.
     * @param <T>
     * @param resourceType
     * @param is
     * @return
     * @throws FHIRPersistenceException
     */
    <T extends Resource> T read(Class<T> resourceType, InputStream is) throws FHIRPersistenceException;
}
