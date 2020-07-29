/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.util;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MediaType;

import org.testng.annotations.Test;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Medication;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIROperationContext.Type;

/**
 *
 */
public class BulkDataExportUtilTest {

    @Test
    public void testBatchJobIdEnDecryption() throws Exception {
        SecretKeySpec secretKey = BulkDataConfigUtil.getBatchJobIdEncryptionKey("test-key");
        assertNotNull(secretKey);

        // This results in at least one case where the naive base64 encoding of the encrypted jobId would
        // 1. have a leading '/' which is prohibited by the S3 client; and
        // 2. have consecutive '/' which can makes it harder to get
        for (int i = 0; i < 2000; i++) {
            String jobId = String.valueOf(i);

            String encryptedJobId = BulkDataExportUtil.encryptBatchJobId(jobId, secretKey);
            assertNotNull(encryptedJobId);
            assertFalse(encryptedJobId.equals(jobId));
            assertFalse(encryptedJobId.startsWith("/"));
            assertFalse(encryptedJobId.contains("//"));

            encryptedJobId = URLDecoder.decode(encryptedJobId, StandardCharsets.UTF_8.toString());
            assertNotNull(encryptedJobId);

            String decryptedJobId = BulkDataExportUtil.decryptBatchJobId(encryptedJobId, secretKey);
            assertNotNull(decryptedJobId);
            assertEquals(decryptedJobId, jobId);
        }
    }

    @Test
    public void testBatchJobIdEnDecryption_With_NullKey() throws Exception {
        String jobId = "100";
        SecretKeySpec secretKey = BulkDataConfigUtil.getBatchJobIdEncryptionKey(null);
        assertNull(secretKey);

        String encryptedJobId = BulkDataExportUtil.encryptBatchJobId(jobId, secretKey);
        assertNotNull(encryptedJobId);
        assertEquals(encryptedJobId, jobId);

        String decryptedJobId = BulkDataExportUtil.decryptBatchJobId(encryptedJobId, secretKey);
        assertNotNull(decryptedJobId);
        assertEquals(decryptedJobId, jobId);
    }

    @Test
    public void testCheckExportTypeInstance() {
        FHIROperationContext.Type type = Type.INSTANCE;

        Class<? extends Resource> resourceType = Patient.class;
        BulkDataConstants.ExportType exportType = BulkDataExportUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.INVALID);

        resourceType = Medication.class;
        exportType   = BulkDataExportUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.INVALID);

        resourceType = Group.class;
        exportType   = BulkDataExportUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.GROUP);
    }

    @Test
    public void testCheckExportTypeResourceType() {
        FHIROperationContext.Type type = Type.RESOURCE_TYPE;

        Class<? extends Resource> resourceType = Patient.class;
        BulkDataConstants.ExportType exportType = BulkDataExportUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.PATIENT);

        resourceType = Medication.class;
        exportType   = BulkDataExportUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.INVALID);

        resourceType = Group.class;
        exportType   = BulkDataExportUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.INVALID);
    }

    @Test
    public void testCheckExportTypeSystem() {
        FHIROperationContext.Type type = Type.SYSTEM;

        Class<? extends Resource> resourceType = Patient.class;
        BulkDataConstants.ExportType exportType = BulkDataExportUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.SYSTEM);

        resourceType = Medication.class;
        exportType   = BulkDataExportUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.SYSTEM);

        resourceType = Group.class;
        exportType   = BulkDataExportUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.SYSTEM);
    }

    @Test
    public void testCheckAndConvertToMediaType() throws FHIROperationException {
        // QueryParameters Map
        Map<String, List<String>> _mvm = new HashMap<String, List<String>>();
        _mvm.put("_outputFormat", Arrays.asList("application/fhir+ndjson"));

        // Default Format
        MediaType type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");

        // No Format
        _mvm.clear();
        type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");

        // Empty
        _mvm.clear();
        _mvm.put("_outputFormat", Collections.emptyList());
        type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");

        // Multiple values
        _mvm.clear();
        _mvm.put("_outputFormat", Arrays.asList("application/fhir+parquet", "ndjson"));
        type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+parquet");

        // Parquet Format
        _mvm.clear();
        _mvm.put("_outputFormat", Arrays.asList("application/fhir+parquet"));
        type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+parquet");

        // Not a valid format
        try {
            _mvm.clear();
            _mvm.put("_outputFormat", Arrays.asList("application/json"));
            type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
            fail();
        } catch (FHIROperationException e) {
            assertNotNull(e);
        }

        // Liberty Encoded + to ' ' and invalid
        try {
            _mvm.clear();
            _mvm.put("_outputFormat", Arrays.asList("application/nd fred"));
            type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
            fail();
        } catch (FHIROperationException e) {
            assertNotNull(e);
        }

        //  Liberty Encoded + to ' '
        _mvm.clear();
        _mvm.put("_outputFormat", Arrays.asList("application/fhir ndjson"));
        type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");

        //Test the format application/ndjson
        _mvm.put("_outputFormat", Arrays.asList("application/ndjson"));
        type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");

        //Test the format ndjson
        _mvm.put("_outputFormat", Arrays.asList("ndjson"));
        type = BulkDataExportUtil.checkAndConvertToMediaType(generateParametersFromMap(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");
    }

    @Test
    public void testCheckAndExtractSinceNull() {
        // No Parameters
        assertNull(BulkDataExportUtil.checkAndExtractSince(null));

        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_since")).value(null).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Instant inst = BulkDataExportUtil.checkAndExtractSince(ps);
        assertNull(inst);
    }

    @Test
    public void testCheckAndExtractSinceEmpty() {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Instant inst = BulkDataExportUtil.checkAndExtractSince(ps);
        assertNull(inst);
    }

    @Test
    public void testCheckAndExtractSinceWithString() {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_since")).value(string("2018-07-01T00:00:00Z")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Instant inst = BulkDataExportUtil.checkAndExtractSince(ps);
        assertNotNull(inst);
    }

    @Test
    public void testCheckAndExtractSinceWithInvalidString() {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("invalid")).value(string("2018-07-01T00:00:00Z")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Instant inst = BulkDataExportUtil.checkAndExtractSince(ps);
        assertNull(inst);
    }

    @Test
    public void testCheckAndExtractSinceWithInstant() {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_since")).value(Instant.of("2018-07-01T00:00:00Z")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Instant inst = BulkDataExportUtil.checkAndExtractSince(ps);
        assertNotNull(inst);
    }

    @Test
    public void testCheckAndExtractSinceWithInvalidType() {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_since")).value(PositiveInt.of(1)).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Instant inst = BulkDataExportUtil.checkAndExtractSince(ps);
        assertNull(inst);
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateTypesEmpty() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        // Value requires a string greater than 1
        parameters.add(Parameter.builder().name(string("_type")).value(string("1")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateTypes(ps);
        fail();
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateTypesNull() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_type")).value(null).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateTypes(ps);
        fail();
    }

    @Test
    public void testCheckAndValidateTypesPatientWithoutComma() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_type")).value(string("Patient")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        List<String> types = BulkDataExportUtil.checkAndValidateTypes(ps);
        assertNotNull(types);
    }

    @Test
    public void testCheckAndValidateTypesPatientWithComma() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_type")).value(string("Patient,")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        List<String> types = BulkDataExportUtil.checkAndValidateTypes(ps);
        assertNotNull(types);
    }

    @Test
    public void testCheckAndValidateTypesPatientMedicationWithComma() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_type")).value(string("Patient,Medication")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        List<String> types = BulkDataExportUtil.checkAndValidateTypes(ps);
        assertNotNull(types);
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateTypesPatientMedicationWithExtraComma() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_type")).value(string("Patient,,Medication")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateTypes(ps);
        fail();
    }

    @Test
    public void testCheckAndValidateTypesWithExtraComma() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_type")).value(string(",,")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        List<String> result = BulkDataExportUtil.checkAndValidateTypes(ps);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckAndValidateTypesNoParameters() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("french")).value(string("Patient,,Medication")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        List<String> result = BulkDataExportUtil.checkAndValidateTypes(ps);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckAndValidateTypesEmptyParameters() throws FHIROperationException {
        List<String> result = BulkDataExportUtil.checkAndValidateTypes(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckAndValidateTypeFilters() throws FHIROperationException {
        List<String> result = BulkDataExportUtil.checkAndValidateTypeFilters(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckAndValidateTypeFiltersNoParameters() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("french")).value(string("Patient,,Medication")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        List<String> result = BulkDataExportUtil.checkAndValidateTypeFilters(ps);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckAndValidateTypeFiltersParameters() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_typeFilter")).value(string("type1")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        List<String> result = BulkDataExportUtil.checkAndValidateTypeFilters(ps);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testCheckAndValidateTypeFiltersParametersTypes() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_typeFilter")).value(string("type1,type2")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        List<String> result = BulkDataExportUtil.checkAndValidateTypeFilters(ps);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateTypeFiltersParametersTypesComma() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_typeFilter")).value(string(",,2")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        List<String> result = BulkDataExportUtil.checkAndValidateTypeFilters(ps);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateTypeFiltersParametersTypesInvalidType() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_typeFilter")).value(PositiveInt.of(2)).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateTypeFilters(ps);
        fail();
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateTypeFiltersParametersTypesNull() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_typeFilter")).value(null).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateTypeFilters(ps);
        fail();
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateJobNull() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("job")).value(null).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateJob(ps);
        fail();
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateJobInvalidType() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("job")).value(PositiveInt.of(2)).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateJob(ps);
        fail();
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateJobNoJob() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("fred")).value(PositiveInt.of(2)).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateJob(ps);
        fail();
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateJobNullParameters() throws FHIROperationException {
        BulkDataExportUtil.checkAndValidateJob(null);
        fail();
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateJobInvalidQuestion() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("job")).value(string("1?")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateJob(ps);
        fail();
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateJobInvalidSlash() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("job")).value(string("1/")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        BulkDataExportUtil.checkAndValidateJob(ps);
        fail();
    }

    @Test
    public void testCheckAndValidateJobValid() throws FHIROperationException {
        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("job")).value(string("1234q346")).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        String result = BulkDataExportUtil.checkAndValidateJob(ps);
        assertNotNull(result);
        assertEquals(result, "1234q346");
    }

    @Test
    public void testGetOutputParametersWithJson() throws Exception {
        PollingLocationResponse pollingLocationResponse = new PollingLocationResponse();
        Parameters result = BulkDataExportUtil.getOutputParametersWithJson(pollingLocationResponse);
        assertNotNull(result);
        assertFalse(result.getParameter().isEmpty());
        assertFalse(
                result.getParameter().get(0).getValue().as(com.ibm.fhir.model.type.String.class).getValue().isEmpty());
    }

    private Parameters generateParametersFromMap(Map<String, List<String>> _mvm) {
        Parameters.Builder builder = Parameters.builder().id("BulkDataExportUtilTest");
        for (Map.Entry<String, List<String>> entry : _mvm.entrySet()) {
            for (String value : entry.getValue()) {
                builder.parameter(Parameter.builder()
                        .name(string(entry.getKey()))
                        .value(string(value))
                        .build());
            }
        }
        return builder.build();
    }
}