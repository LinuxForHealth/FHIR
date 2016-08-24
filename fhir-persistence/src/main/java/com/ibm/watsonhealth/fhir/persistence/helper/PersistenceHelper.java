/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.helper;

import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

public interface PersistenceHelper {

    /**
     * Returns an appropriate FHIRPersistance implementation according to the current configuration.
     * @throws FHIRPersistenceException 
     */
    FHIRPersistence getFHIRPersistenceImplementation() throws FHIRPersistenceException;

    /**
     * Returns an appropriate FHIRPersistance implementation according to the current configuration.
     * @throws FHIRPersistenceException 
     */
    FHIRPersistence getFHIRPersistenceImplementation(String factoryPropertyName) throws FHIRPersistenceException;

}
