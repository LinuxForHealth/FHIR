/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc;

/**
 * Enumerates the types of schemas supported in the FHIR JDBC persistence layer.
 * @author markd
 *
 */
public enum SchemaType {
	BASIC("basic"), 
	NORMALIZED("normalized"); 
			
	private String value = null;
	
	SchemaType(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
	
	public static SchemaType fromValue(String value) {
		for (SchemaType type : SchemaType.values()) {
			if (type.value.equalsIgnoreCase(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException("No constant with value " + value + " found.");
	}
}
