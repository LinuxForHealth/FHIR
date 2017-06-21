/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.replication.api;

import com.ibm.watsonhealth.fhir.exception.FHIRException;

/**
 * This interface defines a Marshaller for replication API.
 */
@FunctionalInterface
public interface Marshaller<T> {
	static final String ISO_8601_GMT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	/**
     * Returns string version of obj.
     * @throws FHIRPersistenceException
     */
	String marshall(T obj) throws FHIRException;;
}
