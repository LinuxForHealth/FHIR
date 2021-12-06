/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.cassandra.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.persistence.cassandra.payload.CqlPayloadStream;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.PayloadPersistenceHelper;
import com.ibm.fhir.persistence.util.InputOutputByteStream;

/**
 * Unit test for {@link CqlPayloadStream}
 */
public class PayloadStreamTest {

    @Test
    public void roundTrip() throws IOException, FHIRPersistenceException {
        Patient patient = Patient.builder()
                .id("a-unique-value")
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();
        
        InputOutputByteStream iobs = PayloadPersistenceHelper.render(patient, true);
        
        // Get the data as a ByteBuffer to pretend that it was stored in Cassandra and read back
        ByteBuffer bb = iobs.wrap();
        InputOutputByteStream readStream = new InputOutputByteStream(bb);
        try (InputStream in = new GZIPInputStream(readStream.inputStream())) {
            // See if we can read the resource from this stream
            Patient p = PayloadPersistenceHelper.parse(Patient.class, in, null);
            assertNotNull(p);
            assertEquals(p.getId(), patient.getId());
        }
    }
}