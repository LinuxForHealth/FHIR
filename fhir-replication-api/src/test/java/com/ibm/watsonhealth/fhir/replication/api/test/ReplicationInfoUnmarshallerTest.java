/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.replication.api.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.replication.api.Unmarshaller;
import com.ibm.watsonhealth.fhir.replication.api.impl.ReplicationInfoUnmarshaller;
import com.ibm.watsonhealth.fhir.replication.api.model.ReplicationInfo;

public class ReplicationInfoUnmarshallerTest {
	private Unmarshaller<ReplicationInfo> unmarshaller = new ReplicationInfoUnmarshaller();
	
	@Test(expectedExceptions={ FHIRException.class })
	public void unmarshall_verifyNullReplicationInfo() throws FHIRException {
		unmarshaller.unmarshall("");
	}
	
	@Test
	public void unmarshall_verifyUnmarshalling() throws FHIRException, ParseException {
		final String replicationInfoJson = buidReplicationStr();
		
		final ReplicationInfo replicationInfo = unmarshaller.unmarshall(replicationInfoJson);
		
		assertNotNull(replicationInfo);
		assertEquals("theSourceKey", replicationInfo.getSourceKey());
		assertEquals("theServiceId", replicationInfo.getServiceId());
		assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse("2017-05-15T17:03:47-0300"), replicationInfo.getCreationTime());
		assertEquals(1, replicationInfo.getVersionId());
		assertEquals("theTxCorrelationId", replicationInfo.getTxCorrelationId());
		assertEquals("theChangedBy", replicationInfo.getChangedBy());
		assertEquals("theCorrelationToken", replicationInfo.getCorrelationToken());
		assertEquals("theTenantId", replicationInfo.getTenantId());
		assertEquals("theReason", replicationInfo.getReason());
		assertEquals("theEvent", replicationInfo.getEvent());
		assertEquals("theResourceType", replicationInfo.getResourceType());
		assertEquals("theSiteId", replicationInfo.getSiteId());
		assertEquals("theStudyId", replicationInfo.getStudyId());
		assertEquals("thePatientId", replicationInfo.getPatientId());
	}
	
	private String buidReplicationStr() {
		final StringBuilder buider = new StringBuilder();

		buider.append("{")
				.append("  \"sourceKey\": \"theSourceKey\",")
				.append("  \"serviceId\": \"theServiceId\",")
				.append("  \"creationTime\": \"2017-05-15T17:03:47-0300\",")
				.append("  \"versionId\": 1,")
				.append("  \"txCorrelationId\": \"theTxCorrelationId\",")
				.append("  \"changedBy\": \"theChangedBy\",")
				.append("  \"correlationToken\": \"theCorrelationToken\",")
				.append("  \"tenantId\": \"theTenantId\",")
				.append("  \"reason\": \"theReason\",")
				.append("  \"event\": \"theEvent\",")
				.append("  \"resourceType\": \"theResourceType\",")
				.append("  \"siteId\": \"theSiteId\",")
				.append("  \"studyId\": \"theStudyId\",")
				.append("  \"patientId\": \"thePatientId\"")
				.append("}");
		
		return buider.toString();
	}
}
