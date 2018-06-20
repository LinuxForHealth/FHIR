/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.replication.api.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.replication.api.Marshaller;
import com.ibm.watsonhealth.fhir.replication.api.model.ReplicationInfo;

public class ReplicationInfoMarshaller implements Marshaller<ReplicationInfo>{

	@Override
	public String marshall(ReplicationInfo obj) throws FHIROperationException {
		final Gson gson = new GsonBuilder()
				   .setDateFormat(ISO_8601_GMT_DATE_FORMAT).create();

		if(obj == null) {
			throw new FHIROperationException("Error while marshalling ReplicationInfo. Obj cannot be null.");
		}
			
		return gson.toJson(obj);
	}

}
