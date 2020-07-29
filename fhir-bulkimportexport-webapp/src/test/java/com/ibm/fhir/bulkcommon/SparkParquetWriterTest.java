/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkcommon;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.ibm.fhir.jbatch.bulkdata.export.system.SparkParquetWriter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.NarrativeStatus;

/**
 * Only the file-system based path is tested by default;
 * manually set the constants and enable testWriteCOSviaHMAC to test the write to Cloug Object Storage
 */
public class SparkParquetWriterTest {
    /**
     * The S3 endpoint for regional buckets in us-south
     */
    private static final String ENDPOINT = "https://s3.us-south.cloud-object-storage.appdomain.cloud";

    /**
     * The name of the bucket to use for the test
     */
    private static final String BUCKET_NAME = "fhir-bulkimexport-connectathon";

    /**
     * Access Key for HMAC Authentication
     */
    private static final String ACCESS_KEY = "REPLACEME";
    /**
     * Secret Key for HMAC Authentication
     */
    private static final String SECRET_KEY = "REPLACEME";

    /**
     * IBM Cloud Object Storage Service Id for IAM-based authentication
     */
    private static final String SERVICE_ID = "REPLACEME";
    /**
     * IAM API Key
     */
    private static final String API_KEY = "REPLACEME";

    @Test
    public void testWriteFile() throws Exception {
        try (SparkParquetWriter sparkParquetWriter = new SparkParquetWriter()) {
            Patient patient = buildPatient();

            Path tmpDir = Files.createTempDirectory("SparkParquetWriterTest");
            sparkParquetWriter.writeParquet(Collections.singletonList(patient), tmpDir.toString());
            assertTrue(Files.exists(tmpDir));
            FileUtils.deleteDirectory(tmpDir.toFile());
        }
    }

    /**
     * Enter valid values for ENDPOINT, BUCKET_NAME, ACCESS_KEY, and SECRET_KEY and manually enable this test
     * to perform a write to IBM Cloud Object Storage
     */
    @Test(enabled = false)
    public void testWriteCOSviaHMAC() throws Exception {
        try (SparkParquetWriter sparkParquetWriter = new SparkParquetWriter(false, ENDPOINT, ACCESS_KEY, SECRET_KEY)) {
            Patient patient = buildPatient();

            String itemName = "cos://" + BUCKET_NAME + ".fhir/Patient_2.parquet";
            sparkParquetWriter.writeParquet(Collections.singletonList(patient), itemName);
        }
    }

    /**
     * Disabled until we get a version of Stocator that will work with IAM creds
     */
    @Test(enabled = false)
    public void testWriteCOSviaIAM() throws Exception {
        try (SparkParquetWriter sparkParquetWriter = new SparkParquetWriter(true, ENDPOINT, API_KEY, SERVICE_ID)) {
            Patient patient = buildPatient();

            String itemName = "cos://" + BUCKET_NAME + ".fhir/Patient_1.parquet";
            sparkParquetWriter.writeParquet(Collections.singletonList(patient), itemName);
        }
    }

    private static Patient buildPatient() {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";

        java.lang.String id = "9aac1d9c-ea5f-4513-af9c-897ab21dd11d";

        Meta meta = Meta.builder().versionId(Id.of("1"))
                .lastUpdated(Instant.of("2019-01-01T12:00:00Z"))
                .build();

        HumanName name = HumanName.builder()
                .id("someId")
                .given(string("John"))
                .given(string("Jingle"))
                .given(string("value no extension"))
                .family(string("Doe"))
                .build();

        Narrative text = Narrative.builder()
                .status(NarrativeStatus.GENERATED)
                .div(Xhtml.xhtml(div))
                .build();

        Patient patient = Patient.builder()
                .id(id)
                .text(text)
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .multipleBirth(com.ibm.fhir.model.type.Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of("1970-01-01"))
                .build();

        return patient;
    }
}
