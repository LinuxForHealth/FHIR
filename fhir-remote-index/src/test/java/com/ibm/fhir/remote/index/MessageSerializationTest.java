/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.time.Instant;

import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.ibm.fhir.persistence.index.RemoteIndexConstants;
import com.ibm.fhir.persistence.index.RemoteIndexMessage;
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
        SearchParametersTransportAdapter adapter = new SearchParametersTransportAdapter(resourceType, logicalId, logicalResourceId, 
            versionId, lastUpdated, requestShard, parameterHash);
        sent.setData(adapter.build());
        final String payload = marshallToString(sent);
        System.out.println("payload: " + payload);
        // Now unmarshall the payload and check everything matches
        RemoteIndexMessage rcvd = unmarshallPayload(payload);
        assertNotNull(rcvd);
        assertNotNull(rcvd.getData());
        assertEquals(rcvd.getMessageVersion(), RemoteIndexConstants.MESSAGE_VERSION);
        assertEquals(rcvd.getData().getParameterHash(), parameterHash);
        assertEquals(rcvd.getData().getLastUpdated(), lastUpdated.toString());
    }

    /**
     * Marshall the message to a string
     * @param message
     * @return
     */
    private String marshallToString(RemoteIndexMessage message) {
        final Gson gson = new Gson();
        return gson.toJson(message);
    }
    private RemoteIndexMessage unmarshallPayload(String jsonPayload) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(jsonPayload, RemoteIndexMessage.class);
    }
}
