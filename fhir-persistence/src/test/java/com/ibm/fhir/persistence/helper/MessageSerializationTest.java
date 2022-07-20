/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.helper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.Instant;

import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.ibm.fhir.persistence.index.RemoteIndexConstants;
import com.ibm.fhir.persistence.index.RemoteIndexMessage;
import com.ibm.fhir.persistence.index.SearchParametersTransport;
import com.ibm.fhir.persistence.index.SearchParametersTransportAdapter;

/**
 * Unit test for message serialization (for the payload sent over Kafka as a string)
 */
public class MessageSerializationTest {

    @Test
    public void testRoundtrip() throws Exception {
        RemoteIndexMessage sent = new RemoteIndexMessage();
        sent.setMessageVersion(RemoteIndexConstants.MESSAGE_VERSION);

        final String resourceType = "Observation";
        final String logicalId = "patientOne";
        final long logicalResourceId = 1;
        final int versionId = 1;
        final Instant lastUpdated = Instant.now();
        final String requestShard = null;
        final String parameterHash = "1Z+NWYZb739Ava9Pd/d7wt2xecKmC2FkfLlCCml0I5M=";
        final Instant ts1 = lastUpdated.plusMillis(1000);
        final Instant ts2 = lastUpdated.plusMillis(2000);
        final BigDecimal valueNumber = BigDecimal.valueOf(1.0);
        final BigDecimal valueNumberLow = BigDecimal.valueOf(0.5);
        final BigDecimal valueNumberHigh = BigDecimal.valueOf(1.5);
        final String valueSystem = "system1";
        final String valueCode = "code1";
        final String refResourceType = "Patient";
        final String refLogicalId = "pat1";
        final Integer refVersion = 2;
        final boolean wholeSystem = false;
        final Integer compositeId = null;
        final String valueString = "str1";
        final String url = "http://some.profile/location";
        final String profileVersion = "1.0";
        
        SearchParametersTransportAdapter adapter = new SearchParametersTransportAdapter(resourceType, logicalId, logicalResourceId, 
            versionId, lastUpdated, requestShard, parameterHash);
        adapter.dateValue("date-param", ts1, ts2, null, true);
        adapter.locationValue("location-param", 0.1, 0.2, null);
        adapter.numberValue("number-param", valueNumber, valueNumberLow, valueNumberHigh, null);
        adapter.profileValue("profile-param", url, profileVersion, null, true);
        adapter.quantityValue("quantity-param", valueSystem, valueCode, valueNumber, valueNumberLow, valueNumberHigh, compositeId);
        adapter.referenceValue("reference-param", refResourceType, refLogicalId, refVersion, compositeId);
        adapter.securityValue("security-param", valueSystem, valueCode, wholeSystem);
        adapter.stringValue("string-param", valueString, compositeId, wholeSystem);
        adapter.tagValue("tag-param", valueSystem, valueCode, wholeSystem);
        adapter.tokenValue("token-param", valueSystem, valueCode, compositeId, false);

        sent.setData(adapter.build());
        final String payload = RemoteIndexSupport.marshallToString(sent);
        // Now unmarshall the payload and check everything matches
        RemoteIndexMessage rcvd = RemoteIndexSupport.unmarshall(payload);
        assertNotNull(rcvd);
        assertEquals(rcvd.getMessageVersion(), RemoteIndexConstants.MESSAGE_VERSION);

        SearchParametersTransport data = rcvd.getData();
        assertNotNull(data);
        assertEquals(data.getParameterHash(), parameterHash);
        assertEquals(data.getLastUpdatedInstant(), lastUpdated);
        assertEquals(data.getLogicalResourceId(), logicalResourceId);
        assertEquals(data.getResourceType(), resourceType);
        assertEquals(data.getLogicalId(), logicalId);

        assertEquals(data.getDateValues().size(), 1);
        assertEquals(data.getDateValues().get(0).getName(), "date-param");
        assertEquals(data.getDateValues().get(0).getValueDateStart(), ts1);
        assertEquals(data.getDateValues().get(0).getValueDateEnd(), ts2);
        assertEquals(data.getLocationValues().size(), 1);
        assertEquals(data.getLocationValues().get(0).getValueLatitude(), 0.1);
        assertEquals(data.getLocationValues().get(0).getValueLongitude(), 0.2);
        assertEquals(data.getNumberValues().size(), 1);
        assertEquals(data.getNumberValues().get(0).getValue(), valueNumber);
        assertEquals(data.getNumberValues().get(0).getLowValue(), valueNumberLow);
        assertEquals(data.getNumberValues().get(0).getHighValue(), valueNumberHigh);
        assertEquals(data.getProfileValues().size(), 1);
        assertEquals(data.getProfileValues().get(0).getUrl(), url);
        assertEquals(data.getProfileValues().get(0).getVersion(), profileVersion);
        assertEquals(data.getQuantityValues().size(), 1);
        assertEquals(data.getQuantityValues().get(0).getValueNumber(), valueNumber);
        assertEquals(data.getQuantityValues().get(0).getValueNumberLow(), valueNumberLow);
        assertEquals(data.getQuantityValues().get(0).getValueNumberHigh(), valueNumberHigh);
        assertEquals(data.getRefValues().size(), 1);
        assertEquals(data.getRefValues().get(0).getResourceType(), refResourceType);
        assertEquals(data.getRefValues().get(0).getLogicalId(), refLogicalId);
        assertEquals(data.getSecurityValues().size(), 1);
        assertEquals(data.getSecurityValues().get(0).getValueSystem(), valueSystem);
        assertEquals(data.getSecurityValues().get(0).getValueCode(), valueCode);
        assertEquals(data.getStringValues().size(), 1);
        assertEquals(data.getStringValues().get(0).getValue(), valueString);
        assertEquals(data.getTagValues().size(), 1);
        assertEquals(data.getTagValues().get(0).getValueSystem(), valueSystem);
        assertEquals(data.getTagValues().get(0).getValueCode(), valueCode);
        assertEquals(data.getTokenValues().size(), 1);
        assertEquals(data.getTokenValues().get(0).getValueSystem(), valueSystem);
        assertEquals(data.getTokenValues().get(0).getValueCode(), valueCode);
    }

    @Test
    public void testInstant() {
        Gson gson = RemoteIndexSupport.getGson();
        Instant x = Instant.now();
        String value = gson.toJson(x);

        // now try and convert the other way
        Instant x2 = gson.fromJson(value, Instant.class);
        assertEquals(x, x2);
    }
}
