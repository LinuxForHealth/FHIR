/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit.cadf.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.audit.cadf.CadfAttachment;
import org.linuxforhealth.fhir.audit.cadf.CadfCredential;
import org.linuxforhealth.fhir.audit.cadf.CadfEndpoint;
import org.linuxforhealth.fhir.audit.cadf.CadfEvent;
import org.linuxforhealth.fhir.audit.cadf.CadfGeolocation;
import org.linuxforhealth.fhir.audit.cadf.CadfMapItem;
import org.linuxforhealth.fhir.audit.cadf.CadfMeasurement;
import org.linuxforhealth.fhir.audit.cadf.CadfMetric;
import org.linuxforhealth.fhir.audit.cadf.CadfReason;
import org.linuxforhealth.fhir.audit.cadf.CadfReporterStep;
import org.linuxforhealth.fhir.audit.cadf.CadfResource;
import org.linuxforhealth.fhir.audit.cadf.enums.Action;
import org.linuxforhealth.fhir.audit.cadf.enums.EventType;
import org.linuxforhealth.fhir.audit.cadf.enums.Outcome;
import org.linuxforhealth.fhir.audit.cadf.enums.ReporterRole;
import org.linuxforhealth.fhir.audit.cadf.enums.ResourceType;
import org.linuxforhealth.fhir.exception.FHIRException;

public class CadfModelTest {

    @Test
    public void testCadfMapItem() throws IOException, FHIRException {
        CadfMapItem item = CadfMapItem.builder().build();
        assertNotNull(item);

        CadfMapItem.Builder builder = CadfMapItem.builder();
        builder.value("value");
        builder.key("key");
        item = builder.build();

        String jsonString = CadfMapItem.Writer.generate(item);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        item = CadfMapItem.Parser.parse(bais);
        assertNotNull(item);
        assertEquals(item.getKey(), "key");
        assertEquals(item.getValue(), "value");

    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfMapItemForcedException() throws FHIRException {
        CadfMapItem.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfMapItemWrongContentException() throws FHIRException {
        String jsonString =
                "{\n" +
                        "    \"key\": \"key\",\n" +
                        "    \"value\": \"rO0_SO_VERY_WRONG_ABXQABXZhbHVl\"\n" +
                        "}";
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        CadfMapItem.Parser.parse(bais);
    }

    @Test
    public void testCadfEndpoint() throws IOException, FHIRException {
        CadfEndpoint item = CadfEndpoint.builder().build();
        assertNotNull(item);

        CadfEndpoint.Builder builder = CadfEndpoint.builder();
        builder.name("name").port("80").url("url");
        item = builder.build();

        String jsonString = CadfEndpoint.Writer.generate(item);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        item = CadfEndpoint.Parser.parse(bais);
        assertNotNull(item);
        assertEquals(item.getName(), "name");
        assertEquals(item.getPort(), "80");
        assertEquals(item.getUrl(), "url");
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfEndpointForcedException() throws FHIRException {
        CadfEndpoint.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test
    public void testCadfAttachment() throws FHIRException, IOException {
        CadfAttachment item = CadfAttachment.builder().build();
        assertNotNull(item);

        CadfAttachment.Builder builder = CadfAttachment.builder();
        builder.content("value");
        builder.contentType("contentType");
        builder.name("test");
        item = builder.build();

        String jsonString = CadfAttachment.Writer.generate(item);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        item = CadfAttachment.Parser.parse(bais);
        assertNotNull(item);
        assertEquals(item.getContentType(), "contentType");
        assertEquals(item.getName(), "test");
        assertEquals(item.getContent(), "value");

    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfAttachmentForcedException() throws FHIRException {
        CadfAttachment.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfAttachmentWrongContentException() throws FHIRException {
        String jsonString =
                "{\n" +
                        "    \"name\": \"key\",\n" +
                        "    \"content\": \"rO0_SO_VERY_WRONG_ABXQABXZhbHVl\"\n" +
                        "}";
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        CadfAttachment.Parser.parse(bais);
    }

    @Test
    public void testCadfMetric() throws IOException, FHIRException {

        CadfMetric item = CadfMetric.builder().build();
        assertNotNull(item);

        CadfMetric.Builder builder = CadfMetric.builder();
        CadfMapItem mapItem = CadfMapItem.builder().value("value").key("key").build();
        builder.annotation(mapItem);
        builder.metricId("metricId");
        builder.name("name");
        builder.unit("unit");
        item = builder.build();

        String jsonString = CadfMetric.Writer.generate(item);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        item = CadfMetric.Parser.parse(bais);
        assertNotNull(item);
        assertEquals(item.getMetricId(), "metricId");
        assertEquals(item.getName(), "name");
        assertEquals(item.getUnit(), "unit");

        boolean processed = false;
        for (CadfMapItem i : item.getAnnotations()) {
            assertNotNull(i);
            assertEquals(i.getKey(), "key");
            assertEquals(i.getValue(), "value");
            processed = true;
        }
        assertTrue(processed);
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfMetricForcedException() throws FHIRException {
        CadfMetric.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfCredentialNoToken() throws FHIRException, IOException {
        @SuppressWarnings("unused")
        CadfCredential item = CadfCredential.builder().build();
    }

    @Test
    public void testCadfCredential() throws FHIRException, IOException {
        CadfCredential item = CadfCredential.builder().token("token").build();
        assertNotNull(item);

        CadfCredential.Builder builder = CadfCredential.builder();
        CadfMapItem mapItem = CadfMapItem.builder().value("value").key("key").build();
        builder.assertion(mapItem);
        builder.authority("authority");
        builder.token("token");
        builder.type("type");

        item = builder.build();

        String jsonString = CadfCredential.Writer.generate(item);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        item = CadfCredential.Parser.parse(bais);
        assertNotNull(item);
        assertEquals(item.getAuthority(), "authority");
        assertEquals(item.getToken(), "token");
        assertEquals(item.getType(), "type");

        boolean processed = false;
        for (CadfMapItem i : item.getAssertions()) {
            assertNotNull(i);
            assertEquals(i.getKey(), "key");
            assertEquals(i.getValue(), "value");
            processed = true;
        }
        assertTrue(processed);

        CadfCredential.Builder builder2 = CadfCredential.builder();
        CadfMapItem mapItem2 = CadfMapItem.builder().value("value2").key("key2").build();
        builder2.assertions(new ArrayList<>(Arrays.asList(mapItem2)));
        builder2.authority("authority");
        builder2.token("token");
        builder2.type("type");

        CadfCredential item2 = builder2.build();

        assertNotNull(item2);
        assertEquals(item2.getAuthority(), "authority");
        assertEquals(item2.getToken(), "token");
        assertEquals(item2.getType(), "type");

        processed = false;
        for (CadfMapItem i : item2.getAssertions()) {
            assertNotNull(i);
            assertEquals(i.getKey(), "key2");
            assertEquals(i.getValue(), "value2");
            processed = true;
        }
        assertTrue(processed);

        CadfCredential.Builder builder3 = CadfCredential.builder();
        CadfMapItem mapItem3 = CadfMapItem.builder().value("value3").key("key3").build();
        CadfMapItem[] arr = new CadfMapItem[] { mapItem3 };
        builder3.assertions(arr);
        builder3.authority("authority");
        builder3.token("token");
        builder3.type("type");

        CadfCredential item3 = builder3.build();

        assertNotNull(item3);
        assertEquals(item3.getAuthority(), "authority");
        assertEquals(item3.getToken(), "token");
        assertEquals(item3.getType(), "type");

        processed = false;
        for (CadfMapItem i : item3.getAssertions()) {
            assertNotNull(i);
            assertEquals(i.getKey(), "key3");
            assertEquals(i.getValue(), "value3");
            processed = true;
        }
        assertTrue(processed);

    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfCredentialForcedException() throws FHIRException {
        CadfCredential.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonInvalidAll() {
        CadfReason item = CadfReason.builder().build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonInvalidWithPolicyId() {
        CadfReason item = CadfReason.builder().policyId("policyId").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonInvalidWithReasonCode() {
        CadfReason item = CadfReason.builder().reasonCode("reasonCode").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonInvalidWithPolicyIdEmptyString() {
        CadfReason item = CadfReason.builder().policyId("").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonInvalidWithPolicyIdAndPolicyType() {
        CadfReason item = CadfReason.builder().policyId("policyId").policyType("").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonInvalidWithPolicyIdAndPolicyTypeAndReasonCodeEmpty() {
        CadfReason item = CadfReason.builder().policyId("policyId").policyType("").reasonCode("").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonInvalidWithPolicyIdAndPolicyTypeAndReasonCode() {
        CadfReason item = CadfReason.builder().policyId("policyId").policyType("").reasonCode("fudge").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonInvalidWithPolicyIdAndPolicyTypeEmptyString() {
        CadfReason item = CadfReason.builder().policyId("policyId").policyType("").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonInvalidWithPolicyIdAndPolicyTypeReasonCode() {
        CadfReason item =
                CadfReason.builder().policyId("policyId").policyType("policyType").reasonCode("reasonCode").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonValidEmptyReasonType() {
        CadfReason item =
                CadfReason.builder().policyId("policyId").policyType("policyType").reasonCode("reasonCode")
                        .reasonType("").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonValidNullReasonType() {
        CadfReason item =
                CadfReason.builder().policyId("policyId").policyType("policyType").reasonCode("reasonCode")
                        .build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonWithPolicyTypeNull() {
        CadfReason item =
                CadfReason.builder().policyId("policyId").policyType(null).reasonCode("reasonCode")
                        .reasonType("reasonType").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReasonWithPolicyTypeEmpty() {
        CadfReason item =
                CadfReason.builder().policyId("policyId").policyType("").reasonCode("reasonCode")
                        .reasonType("reasonType").build();
        assertNotNull(item);
        item.validate();
    }

    @Test(expectedExceptions = {})
    public void testCadfReasonValidNonEmpty() {
        CadfReason item =
                CadfReason.builder().policyId("policyId").policyType("policyType").reasonCode("reasonCode")
                        .reasonType("reasonType").build();
        assertNotNull(item);
        item.validate();
        assertTrue(true);
    }

    @Test
    public void testCadfReasonFull() throws IOException, FHIRException {
        CadfReason item =
                CadfReason.builder().policyId("policyId").policyType("policyType").reasonCode("reasonCode")
                        .reasonType("reasonType").build();

        String jsonString = CadfReason.Writer.generate(item);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        item = CadfReason.Parser.parse(bais);
        assertNotNull(item);
        assertEquals(item.getPolicyId(), "policyId");
        assertEquals(item.getPolicyType(), "policyType");
        assertEquals(item.getReasonCode(), "reasonCode");
        assertEquals(item.getReasonType(), "reasonType");
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfReasonForcedException() throws FHIRException {
        CadfReason.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { java.lang.IllegalStateException.class })
    public void testCadfReporterStepInvalidRole() throws FHIRException, IOException {
        CadfReporterStep step = CadfReporterStep.builder().build();
        assertNotNull(step);
    }

    @Test
    public void testCadfReporterStep() throws FHIRException, IOException {
        CadfReporterStep.Builder builder =
                CadfReporterStep.builder().reporterId("reporterId").reporterTime(Instant.now().atZone(ZoneId.of("UTC")))
                        .role(ReporterRole.modifier);

        CadfAttachment.Builder attachmentBuilder = CadfAttachment.builder();
        attachmentBuilder.content("value");
        attachmentBuilder.contentType("contentType");
        attachmentBuilder.name("test");
        builder.attachment(attachmentBuilder.build());

        CadfGeolocation geo = CadfGeolocation.builder().city("city").region("regionIcann").build();

        builder.reporter(CadfResource.builder().typeURI(ResourceType.compute_cpu).id("id").geolocation(geo).build());

        CadfReporterStep item = builder.build();

        String jsonString = CadfReporterStep.Writer.generate(item);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        item = CadfReporterStep.Parser.parse(bais);
        assertNotNull(item);
        assertEquals(item.getReporterId(), "reporterId");
        assertNotNull(item.getReporterTime());
        assertEquals(item.getRole().toString(), ReporterRole.modifier.name());

        boolean processed = false;
        for (CadfAttachment i : item.getAttachments()) {
            assertNotNull(i);
            assertEquals(i.getContentType(), "contentType");
            assertEquals(i.getContent(), "value");
            assertEquals(i.getName(), "test");
            processed = true;
        }
        assertTrue(processed);

        CadfReporterStep.Builder b =
                new CadfReporterStep.Builder(ReporterRole.modifier,
                        CadfResource.builder().typeURI(ResourceType.compute_cpu).id("id").geolocation(geo).build(),
                        "repId", ZonedDateTime.now());
        b.attachments(new CadfAttachment[] {});
        b.attachments(new ArrayList<>(Arrays.asList()));
        assertNotNull(b.build());

        CadfReporterStep.Builder b2 =
                new CadfReporterStep.Builder(ReporterRole.modifier,
                        CadfResource.builder().typeURI(ResourceType.compute_cpu).id("id").geolocation(geo).build(),
                        "repId", null);
        assertNotNull(b2.build());
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReporterStepValidateExceptionBadRole() throws FHIRException {
        CadfGeolocation geo = CadfGeolocation.builder().city("city").region("regionIcann").build();
        CadfReporterStep.Builder b2 =
                new CadfReporterStep.Builder(null,
                        CadfResource.builder().typeURI(ResourceType.compute_cpu).id("id").geolocation(geo).build(),
                        "repId", null);
        assertNotNull(b2.build());
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReporterStepValidateExceptionNullReporter() throws FHIRException {
        CadfReporterStep.builder().role(ReporterRole.modifier).build();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfReporterStepValidateExceptionEmpty() throws FHIRException {
        CadfReporterStep.builder().role(ReporterRole.modifier).reporterId("").build();
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfReporterStepForcedException() throws FHIRException {
        CadfReporterStep.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfMeasurementForcedException() throws FHIRException {
        CadfMeasurement.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test
    public void testCadfGeolocation() throws FHIRException, IOException {
        CadfGeolocation.Builder builder = CadfGeolocation.builder();
        CadfGeolocation geo =
                builder.city("city").state("state")
                        .region("regionIcann").id("id")
                        .elevation(10.0)
                        .accuracy(10.0)
                        .build();

        String jsonString = CadfGeolocation.Writer.generate(geo);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        geo = CadfGeolocation.Parser.parse(bais);
        assertNotNull(geo);
    }

    @SuppressWarnings("unused")
    @Test(expectedExceptions = { java.lang.IllegalStateException.class })
    public void testCadfGeolocationInvalidWithCity() {
        CadfGeolocation.Builder builder = CadfGeolocation.builder();
        CadfGeolocation geo = builder.city("city").build();
        geo = null;
    }

    @SuppressWarnings("unused")
    @Test(expectedExceptions = { java.lang.IllegalStateException.class })
    public void testCadfGeolocationInvalidWithLong() {
        CadfGeolocation.Builder builder = CadfGeolocation.builder();
        CadfGeolocation geo = builder.longitude("longitude").build();
        geo = null;
    }

    @SuppressWarnings("unused")
    @Test(expectedExceptions = { java.lang.IllegalStateException.class })
    public void testCadfGeolocationInvalidWithLat() {
        CadfGeolocation.Builder builder = CadfGeolocation.builder();
        CadfGeolocation geo = builder.latitude("longitude").build();
        geo = null;
    }

    @SuppressWarnings("unused")
    @Test(expectedExceptions = { java.lang.IllegalStateException.class })
    public void testCadfGeolocationInvalidWithRegion() {
        CadfGeolocation.Builder builder = CadfGeolocation.builder();
        CadfGeolocation geo = builder.region("regionIcann").build();
        geo = null;
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfGeolocationForcedException() throws FHIRException {
        CadfGeolocation.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test
    public void testCadfGeolocationWithLatLong() throws FHIRException, IOException {
        CadfGeolocation.Builder builder = CadfGeolocation.builder();
        CadfMapItem mapItem = CadfMapItem.builder().value("value").key("key").build();
        CadfGeolocation geo =
                builder.city("city").state("state")
                        .region("regionIcann").id("id")
                        .latitude("10.0")
                        .longitude("10.0")
                        .addAnnotation(mapItem)
                        .build();

        String jsonString = CadfGeolocation.Writer.generate(geo);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        geo = CadfGeolocation.Parser.parse(bais);
        assertNotNull(geo);
    }

    @Test
    public void testCadfGeolocationWithArray() throws FHIRException, IOException {
        CadfGeolocation.Builder builder = CadfGeolocation.builder();
        CadfMapItem mapItem = CadfMapItem.builder().value("value").key("key").build();
        ArrayList<CadfMapItem> list = new ArrayList<>(Arrays.asList(mapItem));
        CadfGeolocation geo =
                builder.city("city").state("state")
                        .region("regionIcann").id("id")
                        .latitude("10.0")
                        .longitude("10.0")
                        .annotations(list)
                        .annotations(new CadfMapItem[] { mapItem })
                        .build();

        String jsonString = CadfGeolocation.Writer.generate(geo);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        geo = CadfGeolocation.Parser.parse(bais);
        assertNotNull(geo);
    }

    @Test
    public void testCadfGeolocationWithConstructor() throws FHIRException, IOException {
        CadfGeolocation.Builder builder = new CadfGeolocation.Builder("1.0", "2.0", 10.0, 10.0);
        CadfGeolocation geo =
                builder.city("city").state("state")
                        .build();

        String jsonString = CadfGeolocation.Writer.generate(geo);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        geo = CadfGeolocation.Parser.parse(bais);
        assertNotNull(geo);
    }

    @Test
    public void testCadfMeasurement() throws FHIRException, IOException {
        CadfMeasurement.Builder builder = CadfMeasurement.builder();
        builder.calculatedById("calculatedById");
        builder.metricId("metricId");
        builder.result("result");

        CadfGeolocation.Builder builder3 = new CadfGeolocation.Builder("1.0", "2.0", 10.0, 10.0);
        CadfGeolocation geo =
                builder3.city("city").state("state")
                        .build();

        builder.calculatedBy(
                CadfResource.builder().typeURI(ResourceType.compute_cpu).id("id").geolocation(geo).build());

        CadfMetric.Builder builder2 = CadfMetric.builder();
        CadfMapItem mapItem = CadfMapItem.builder().value("value").key("key").build();
        builder2.annotation(mapItem);
        builder2.metricId("metricId");
        builder2.name("name");
        builder2.unit("unit");
        builder.metric(builder2.build());
        CadfMeasurement measurement = builder.build();

        String jsonString = CadfMeasurement.Writer.generate(measurement);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        measurement = CadfMeasurement.Parser.parse(bais);
        assertNotNull(measurement);
    }

    @SuppressWarnings("unused")
    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfMeasurementBadSerializationResult() throws Exception {
        String jsonString =
                "{\n" +
                        "    \"result\": \"badbadbad==\",\n" +
                        "    \"metricId\": \"metricId\",\n" +
                        "    \"metric\": {\n" +
                        "        \"name\": \"name\",\n" +
                        "        \"unit\": \"unit\",\n" +
                        "        \"metricId\": \"metricId\",\n" +
                        "        \"annotations\": [\n" +
                        "            {\n" +
                        "                \"key\": \"key\",\n" +
                        "                \"value\": \"rO0ABXQABXZhbHVl\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"calculatedBy\": {\n" +
                        "        \"id\": \"id\",\n" +
                        "        \"typeURI\": \"compute/cpu\",\n" +
                        "        \"geolocation\": {\n" +
                        "            \"accuracy\": 10.0,\n" +
                        "            \"elevation\": 10.0,\n" +
                        "            \"city\": \"city\",\n" +
                        "            \"state\": \"state\",\n" +
                        "            \"latitude\": \"1.0\",\n" +
                        "            \"longitude\": \"1.0\",\n" +
                        "            \"annotations\": [\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"calculatedById\": \"calculatedById\"\n" +
                        "}";

        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        CadfMeasurement measurement = CadfMeasurement.Parser.parse(bais);
    }

    @SuppressWarnings("unused")
    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfMeasurementBadSerializationAnnotationValue() throws Exception {
        String jsonString =
                "{\n" +
                        "    \"result\": \"rO0ABXQABnJlc3VsdA==\",\n" +
                        "    \"metricId\": \"metricId\",\n" +
                        "    \"metric\": {\n" +
                        "        \"name\": \"name\",\n" +
                        "        \"unit\": \"unit\",\n" +
                        "        \"metricId\": \"metricId\",\n" +
                        "        \"annotations\": [\n" +
                        "            {\n" +
                        "                \"key\": \"key\",\n" +
                        "                \"value\": \"BADBADBAD BAD\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"calculatedBy\": {\n" +
                        "        \"id\": \"id\",\n" +
                        "        \"typeURI\": \"compute/cpu\",\n" +
                        "        \"geolocation\": {\n" +
                        "            \"accuracy\": 10.0,\n" +
                        "            \"elevation\": 10.0,\n" +
                        "            \"city\": \"city\",\n" +
                        "            \"state\": \"state\",\n" +
                        "            \"latitude\": \"1.0\",\n" +
                        "            \"longitude\": \"1.0\",\n" +
                        "            \"annotations\": [\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"calculatedById\": \"calculatedById\"\n" +
                        "}";

        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        CadfMeasurement measurement = CadfMeasurement.Parser.parse(bais);
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfResource() throws FHIRException {
        CadfResource.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { FHIRException.class })
    public void testCadfEvent() throws FHIRException {
        CadfEvent.Parser.parse(AuditTestUtil.generateExceptionStream());
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfResourceBlankBuild() throws FHIRException {
        CadfResource.builder().build();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfResourceWithUri() throws FHIRException {
        CadfResource.builder().typeURI(ResourceType.compute_cpu).build();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfResourceWithUriId() throws FHIRException {
        CadfResource.builder().typeURI(ResourceType.compute_cpu)
                .id("id").build();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfResourceWithUriEmptyId() throws FHIRException {
        CadfResource.builder().typeURI(ResourceType.compute_cpu)
                .id("").build();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfResourceWithUriIdGeo() throws FHIRException {
        CadfResource.builder().typeURI(ResourceType.compute_cpu)
                .id("id").geolocation(null).geolocationId(null).build();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testCadfResourceWithUriIdGeoIdEmpty() throws FHIRException {
        CadfResource.builder().typeURI(ResourceType.compute_cpu)
                .id("id").geolocation(null).geolocationId("").build();
    }

    @Test
    public void testCadfResourceValid() throws FHIRException, IOException {
        CadfGeolocation.Builder builder3 = new CadfGeolocation.Builder("1.0", "2.0", 10.0, 10.0);
        CadfGeolocation geo =
                builder3.city("city").state("state")
                        .build();

        CadfResource.Builder builder = new CadfResource.Builder("id", ResourceType.compute_cpu);
        builder =
                CadfResource.builder().typeURI(ResourceType.compute_cpu)
                        .id("id").geolocation(geo).geolocationId("geoId");
        builder.name("name");
        builder.host("host");
        builder.domain("domain");

        CadfCredential.Builder ccBuilder = CadfCredential.builder();
        CadfMapItem mapItem = CadfMapItem.builder().value("value").key("key").build();
        ccBuilder.assertion(mapItem);
        ccBuilder.authority("authority");
        ccBuilder.token("token");
        ccBuilder.type("type");
        builder.credential(ccBuilder.build());

        CadfAttachment.Builder aBuilder = CadfAttachment.builder();
        aBuilder.content("value");
        aBuilder.contentType("contentType");
        aBuilder.name("test");
        CadfAttachment attachment = aBuilder.build();

        builder.attachment(attachment);
        builder.attachments(new ArrayList<>(Arrays.asList(attachment)));
        builder.attachments(new CadfAttachment[] { attachment });

        CadfEndpoint.Builder ceBuilder = CadfEndpoint.builder();
        ceBuilder.name("name").port("80").url("url");
        CadfEndpoint endpoint = ceBuilder.build();
        builder.address(endpoint);
        builder.addresses(new ArrayList<>(Arrays.asList(endpoint)));
        builder.addresses(new CadfEndpoint[] { endpoint });

        CadfResource resource = builder.build();

        String jsonString = CadfResource.Writer.generate(resource);
        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        resource = CadfResource.Parser.parse(bais);
        assertNotNull(resource);
    }

    @Test
    public void testCadfEventValid() throws FHIRException, IOException {
        CadfGeolocation.Builder builder3 = new CadfGeolocation.Builder("1.0", "2.0", 10.0, 10.0);
        CadfGeolocation geo =
                builder3.city("city").state("state")
                        .build();

        CadfResource.Builder builder =
                CadfResource.builder().typeURI(ResourceType.compute_cpu)
                        .id("id").geolocation(geo).geolocationId("geoId");
        builder.name("name");
        builder.host("host");
        builder.domain("domain");

        CadfCredential.Builder ccBuilder = CadfCredential.builder();
        CadfMapItem mapItem = CadfMapItem.builder().value("value").key("key").build();
        ccBuilder.assertion(mapItem);
        ccBuilder.authority("authority");
        ccBuilder.token("token");
        ccBuilder.type("type");
        builder.credential(ccBuilder.build());

        CadfAttachment.Builder aBuilder = CadfAttachment.builder();
        aBuilder.content("value");
        aBuilder.contentType("contentType");
        aBuilder.name("test");
        CadfAttachment attachment = aBuilder.build();

        builder.attachment(attachment);
        builder.attachments(new ArrayList<>(Arrays.asList(attachment)));
        builder.attachments(new CadfAttachment[] { attachment });

        CadfEndpoint.Builder ceBuilder = CadfEndpoint.builder();
        ceBuilder.name("name").port("80").url("url");
        CadfEndpoint endpoint = ceBuilder.build();
        builder.address(endpoint);
        builder.addresses(new ArrayList<>(Arrays.asList(endpoint)));
        builder.addresses(new CadfEndpoint[] { endpoint });

        CadfResource resource = builder.build();
        assertNotNull(resource);

        CadfEvent.Builder eventBuilder = CadfEvent.builder();
        eventBuilder = new CadfEvent.Builder(null, EventType.activity, null, Action.allow, Outcome.failure);
        eventBuilder = new CadfEvent.Builder("id", EventType.activity, "eventTime", Action.allow, Outcome.failure);
        eventBuilder.id("id");
        eventBuilder.eventType(EventType.control);
        eventBuilder.eventTime("eventTime");
        eventBuilder.action(Action.allow);
        eventBuilder.outcome(Outcome.success);
        eventBuilder.typeUri("typeUri");
        eventBuilder.reason("reasonType", "reasonCode", "policyType", "policyId");

        CadfReason item =
                CadfReason.builder().policyId("policyId").policyType("policyType").reasonCode("reasonCode")
                        .reasonType("reasonType").build();
        eventBuilder.reason(item);
        eventBuilder.name("name");
        eventBuilder.severity("SEVERE");
        eventBuilder.duration("duration");
        eventBuilder.initiatorId("initiatorId");

        eventBuilder.tags(new String[] { "a", "b" });
        eventBuilder.tag("c");
        eventBuilder.targetId("targetId");
        eventBuilder.observerId("observerId");

        aBuilder = CadfAttachment.builder();
        aBuilder.content("valueeb");
        aBuilder.contentType("contentTypeeb");
        aBuilder.name("testeb");
        CadfAttachment attachment2 = aBuilder.build();

        eventBuilder.attachment(attachment2);
        eventBuilder.attachments(new CadfAttachment[] { attachment2 });

        CadfMeasurement.Builder builderCM = CadfMeasurement.builder();
        builderCM.calculatedById("calculatedById");
        builderCM.metricId("metricId");
        builderCM.result("result");

        CadfGeolocation.Builder builder3a = new CadfGeolocation.Builder("1.0", "2.0", 10.0, 10.0);
        CadfGeolocation geo3a =
                builder3a.city("city").state("state")
                        .build();

        builderCM.calculatedBy(
                CadfResource.builder().typeURI(ResourceType.compute_cpu).id("id").geolocation(geo3a).build());

        CadfMetric.Builder builder2 = CadfMetric.builder();
        CadfMapItem mapItem2 = CadfMapItem.builder().value("value").key("key").build();
        builder2.annotation(mapItem2);
        builder2.metricId("metricId");
        builder2.name("name");
        builder2.unit("unit");
        builderCM.metric(builder2.build());
        CadfMeasurement measurement = builderCM.build();

        eventBuilder.measurement(measurement);
        eventBuilder.measurements(new CadfMeasurement[] { measurement });

        CadfReporterStep.Builder builderCrs =
                CadfReporterStep.builder().reporterId("reporterId").reporterTime(Instant.now().atZone(ZoneId.of("UTC")))
                        .role(ReporterRole.modifier);

        CadfAttachment.Builder attachmentBuilder = CadfAttachment.builder();
        attachmentBuilder.content("value");
        attachmentBuilder.contentType("contentType");
        attachmentBuilder.name("test");
        builder.attachment(attachmentBuilder.build());

        CadfGeolocation geo9 = CadfGeolocation.builder().city("city").region("regionIcann").build();

        builderCrs
                .reporter(CadfResource.builder().typeURI(ResourceType.compute_cpu).id("id").geolocation(geo9).build());

        CadfReporterStep reporterStep = builderCrs.build();
        eventBuilder.reporterChain(new CadfReporterStep[] { reporterStep });

        CadfEvent event = eventBuilder.build();

        String jsonString = CadfEvent.Writer.generate(event);

        ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
        event = CadfEvent.Parser.parse(bais);
        assertNotNull(event);
        assertEquals(event.getAction().toString(), "allow");
        assertEquals(event.getDuration(), "duration");
        assertEquals(event.getEventTime(), "eventTime");
        assertEquals(event.getEventType().name(), "control");
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testResourceType() {
        ResourceType.of("not-valid");
    }

    @Test
    public void testCadfResourceWithAddress() throws IOException, org.json.JSONException {
        CadfGeolocation geo = CadfGeolocation.builder().city("city").region("regionIcann").build();
        CadfEndpoint endpoint = CadfEndpoint.builder().name("test").port("80").url("Here").build();
        CadfResource resource = CadfResource.builder().typeURI(ResourceType.compute_cpu).id("id").geolocation(geo).address(endpoint).build();
        String output = CadfResource.Writer.generate(resource);
        JSONAssert.assertEquals("{\n" +
                "    \"id\": \"id\",\n" +
                "    \"typeURI\": \"compute/cpu\",\n" +
                "    \"addresses\": [\n" +
                "        {\n" +
                "            \"url\": \"Here\",\n" +
                "            \"name\": \"test\",\n" +
                "            \"port\": \"80\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"geolocation\": {\n" +
                "        \"city\": \"city\",\n" +
                "        \"region\": \"regionIcann\",\n" +
                "        \"annotations\": [\n" +
                "        ]\n" +
                "    }\n" +
                "}", output, false);
    }
}