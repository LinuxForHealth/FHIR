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

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;

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
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.operation.context.FHIROperationContext.Type;

/**
 *
 */
public class BulkDataUtilTest {

    @Test
    public void testBatchJobIdEnDecryption() throws Exception {
        String jobId = "100";
        SecretKeySpec secretKey = BulkDataConfigUtil.getBatchJobIdEncryptionKey("test-key");
        assertNotNull(secretKey);

        String encryptedJobId = BulkDataUtil.encryptBatchJobId(jobId, secretKey);
        assertNotNull(encryptedJobId);
        assertFalse(encryptedJobId.equals(jobId));

        encryptedJobId = URLDecoder.decode(encryptedJobId, StandardCharsets.UTF_8.toString());
        assertNotNull(encryptedJobId);

        String decryptedJobId = BulkDataUtil.decryptBatchJobId(encryptedJobId, secretKey);
        assertNotNull(decryptedJobId);
        assertEquals(decryptedJobId, jobId);
    }

    @Test
    public void testBatchJobIdEnDecryption_With_NullKey() throws Exception {
        String jobId = "100";
        SecretKeySpec secretKey = BulkDataConfigUtil.getBatchJobIdEncryptionKey(null);
        assertNull(secretKey);

        String encryptedJobId = BulkDataUtil.encryptBatchJobId(jobId, secretKey);
        assertNotNull(encryptedJobId);
        assertEquals(encryptedJobId, jobId);

        String decryptedJobId = BulkDataUtil.decryptBatchJobId(encryptedJobId, secretKey);
        assertNotNull(decryptedJobId);
        assertEquals(decryptedJobId, jobId);
    }

    @Test
    public void testCheckExportTypeInstance() {
        FHIROperationContext.Type type = Type.INSTANCE;

        Class<? extends Resource> resourceType = Patient.class;
        BulkDataConstants.ExportType exportType = BulkDataUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.INVALID);

        resourceType = Medication.class;
        exportType   = BulkDataUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.INVALID);

        resourceType = Group.class;
        exportType   = BulkDataUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.GROUP);
    }

    @Test
    public void testCheckExportTypeResourceType() {
        FHIROperationContext.Type type = Type.RESOURCE_TYPE;

        Class<? extends Resource> resourceType = Patient.class;
        BulkDataConstants.ExportType exportType = BulkDataUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.PATIENT);

        resourceType = Medication.class;
        exportType   = BulkDataUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.INVALID);

        resourceType = Group.class;
        exportType   = BulkDataUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.INVALID);
    }

    @Test
    public void testCheckExportTypeSystem() {
        FHIROperationContext.Type type = Type.SYSTEM;

        Class<? extends Resource> resourceType = Patient.class;
        BulkDataConstants.ExportType exportType = BulkDataUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.SYSTEM);

        resourceType = Medication.class;
        exportType   = BulkDataUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.SYSTEM);

        resourceType = Group.class;
        exportType   = BulkDataUtil.checkExportType(type, resourceType);
        assertEquals(exportType, ExportType.SYSTEM);
    }

    @Test
    public void testGetRandomKey() {
        String output = BulkDataUtil.getRandomKey("AES");
        assertFalse(output.isEmpty());
        output = BulkDataUtil.getRandomKey("FANCY_KEYS");
        assertFalse(output.isEmpty());
    }

    @Test
    public void testCheckAndConvertToMediaType() throws FHIROperationException {
        // QueryParameters Map
        Map<String, List<String>> _mvm = new HashMap<String, List<String>>();
        _mvm.put("_outputFormat", Arrays.asList("application/fhir+ndjson"));

        // Default Format
        MediaType type = BulkDataUtil.checkAndConvertToMediaType(generateFHIROperationContextWithUriInfo(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");

        // No Format
        _mvm.clear();
        type = BulkDataUtil.checkAndConvertToMediaType(generateFHIROperationContextWithUriInfo(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");

        // Empty
        try {
            _mvm.clear();
            _mvm.put("_outputFormat", Collections.emptyList());
            type = BulkDataUtil.checkAndConvertToMediaType(generateFHIROperationContextWithUriInfo(_mvm));
            fail();
        } catch (FHIROperationException e) {
            assertNotNull(e);
        }

        // multiple values
        try {
            _mvm.clear();
            _mvm.put("_outputFormat", Arrays.asList("application/ndjson", "ndjson"));
            type = BulkDataUtil.checkAndConvertToMediaType(generateFHIROperationContextWithUriInfo(_mvm));
            fail();
        } catch (FHIROperationException e) {
            assertNotNull(e);
        }

        // Not a valid format
        try {
            _mvm.clear();
            _mvm.put("_outputFormat", Arrays.asList("application/json"));
            type = BulkDataUtil.checkAndConvertToMediaType(generateFHIROperationContextWithUriInfo(_mvm));
            fail();
        } catch (FHIROperationException e) {
            assertNotNull(e);
        }

        // Liberty Encoded + to ' ' and invalid
        try {
            _mvm.clear();
            _mvm.put("_outputFormat", Arrays.asList("application/nd fred"));
            type = BulkDataUtil.checkAndConvertToMediaType(generateFHIROperationContextWithUriInfo(_mvm));
            fail();
        } catch (FHIROperationException e) {
            assertNotNull(e);
        }

        //  Liberty Encoded + to ' '
        _mvm.clear();
        _mvm.put("_outputFormat", Arrays.asList("application/fhir ndjson"));
        type = BulkDataUtil.checkAndConvertToMediaType(generateFHIROperationContextWithUriInfo(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");

        //Test the format application/ndjson
        _mvm.put("_outputFormat", Arrays.asList("application/ndjson"));
        type = BulkDataUtil.checkAndConvertToMediaType(generateFHIROperationContextWithUriInfo(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");

        //Test the format ndjson
        _mvm.put("_outputFormat", Arrays.asList("ndjson"));
        type = BulkDataUtil.checkAndConvertToMediaType(generateFHIROperationContextWithUriInfo(_mvm));
        assertNotNull(type);
        assertEquals(type.getType(), "application");
        assertEquals(type.getSubtype(), "fhir+ndjson");
    }

    @Test
    public void testCheckAndExtractSinceNull() {
        // No Parameters
        assertNull(BulkDataUtil.checkAndExtractSince(null));

        // parameters
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.builder().name(string("_since")).value(null).build());
        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        Instant inst = BulkDataUtil.checkAndExtractSince(ps);
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

        Instant inst = BulkDataUtil.checkAndExtractSince(ps);
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

        Instant inst = BulkDataUtil.checkAndExtractSince(ps);
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

        Instant inst = BulkDataUtil.checkAndExtractSince(ps);
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

        Instant inst = BulkDataUtil.checkAndExtractSince(ps);
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

        Instant inst = BulkDataUtil.checkAndExtractSince(ps);
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

        BulkDataUtil.checkAndValidateTypes(ps);
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

        BulkDataUtil.checkAndValidateTypes(ps);
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

        List<String> types = BulkDataUtil.checkAndValidateTypes(ps);
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

        List<String> types = BulkDataUtil.checkAndValidateTypes(ps);
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

        List<String> types = BulkDataUtil.checkAndValidateTypes(ps);
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

        BulkDataUtil.checkAndValidateTypes(ps);
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

        List<String> result = BulkDataUtil.checkAndValidateTypes(ps);
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

        List<String> result = BulkDataUtil.checkAndValidateTypes(ps);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckAndValidateTypesEmptyParameters() throws FHIROperationException {
        List<String> result = BulkDataUtil.checkAndValidateTypes(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckAndValidateTypeFilters() throws FHIROperationException {
        List<String> result = BulkDataUtil.checkAndValidateTypeFilters(null);
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

        List<String> result = BulkDataUtil.checkAndValidateTypeFilters(ps);
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

        List<String> result = BulkDataUtil.checkAndValidateTypeFilters(ps);
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

        List<String> result = BulkDataUtil.checkAndValidateTypeFilters(ps);
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

        List<String> result = BulkDataUtil.checkAndValidateTypeFilters(ps);
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

        BulkDataUtil.checkAndValidateTypeFilters(ps);
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

        BulkDataUtil.checkAndValidateTypeFilters(ps);
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

        BulkDataUtil.checkAndValidateJob(ps);
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

        BulkDataUtil.checkAndValidateJob(ps);
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

        BulkDataUtil.checkAndValidateJob(ps);
        fail();
    }

    @Test(expectedExceptions = { com.ibm.fhir.exception.FHIROperationException.class })
    public void testCheckAndValidateJobNullParameters() throws FHIROperationException {
        BulkDataUtil.checkAndValidateJob(null);
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

        BulkDataUtil.checkAndValidateJob(ps);
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

        BulkDataUtil.checkAndValidateJob(ps);
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

        String result = BulkDataUtil.checkAndValidateJob(ps);
        assertNotNull(result);
        assertEquals(result, "1234q346");
    }

    @Test
    public void testGetOutputParametersWithJson() throws Exception {
        PollingLocationResponse pollingLocationResponse = new PollingLocationResponse();
        Parameters result = BulkDataUtil.getOutputParametersWithJson(pollingLocationResponse);
        assertNotNull(result);
        assertFalse(result.getParameter().isEmpty());
        assertFalse(
                result.getParameter().get(0).getValue().as(com.ibm.fhir.model.type.String.class).getValue().isEmpty());
    }

    private FHIROperationContext generateFHIROperationContextWithUriInfo(Map<String, List<String>> _mvm) {
        FHIROperationContext ctx = FHIROperationContext.createSystemOperationContext();
        javax.ws.rs.core.UriInfo uriInfo = new javax.ws.rs.core.UriInfo() {
            @Override
            public MultivaluedMap<String, String> getQueryParameters() {
                MultivaluedMap<String, String> mvm = new MultivaluedMap<String, String>() {
                    @Override
                    public List<String> get(Object key) {
                        return _mvm.get(key);
                    }

                    @Override
                    public List<String> put(String key, List<String> value) {
                        return _mvm.put(key, value);
                    }

                    @Override
                    public void add(String key, String value) {
                        if (_mvm.containsKey(key)) {
                            _mvm.get(key).add(value);
                        } else {
                            _mvm.put(key, Arrays.asList(value));
                        }
                    }

                    @Override
                    public int size() {
                        return _mvm.size();
                    }

                    @Override
                    public boolean isEmpty() {
                        return _mvm.isEmpty();
                    }

                    @Override
                    public void clear() {
                        _mvm.clear();
                    }

                    @Override
                    public boolean containsKey(Object key) {
                        return false;
                    }

                    @Override
                    public boolean containsValue(Object value) {
                        return false;
                    }

                    @Override
                    public List<String> remove(Object key) {
                        return null;
                    }

                    @Override
                    public void putAll(Map<? extends String, ? extends List<String>> m) {
                    }

                    @Override
                    public Set<String> keySet() {
                        return null;
                    }

                    @Override
                    public Collection<List<String>> values() {
                        return null;
                    }

                    @Override
                    public Set<Entry<String, List<String>>> entrySet() {
                        return null;
                    }

                    @Override
                    public void putSingle(String key, String value) {
                    }

                    @Override
                    public String getFirst(String key) {
                        return null;
                    }

                    @Override
                    public void addAll(String key, String... newValues) {
                    }

                    @Override
                    public void addAll(String key, List<String> valueList) {
                        _mvm.put(key, valueList);
                    }

                    @Override
                    public void addFirst(String key, String value) {
                    }

                    @Override
                    public boolean equalsIgnoreValueOrder(MultivaluedMap<String, String> otherMap) {
                        return false;
                    }

                };
                return mvm;
            }

            @Override
            public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
                MultivaluedMap<String, String> mvm = getQueryParameters();
                List<String> mvms =
                        mvm.get("_outputFormat").stream().map(s -> s.replace(" ", "+")).collect(Collectors.toList());
                mvm.clear();
                mvm.addAll("_outputFormat", mvms);
                return mvm;
            }

            @Override
            public String getPath() {
                return null;
            }

            @Override
            public String getPath(boolean decode) {
                return null;
            }

            @Override
            public List<PathSegment> getPathSegments() {
                return null;
            }

            @Override
            public List<PathSegment> getPathSegments(boolean decode) {
                return null;
            }

            @Override
            public URI getRequestUri() {
                return null;
            }

            @Override
            public UriBuilder getRequestUriBuilder() {
                return null;
            }

            @Override
            public URI getAbsolutePath() {
                return null;
            }

            @Override
            public UriBuilder getAbsolutePathBuilder() {
                return null;
            }

            @Override
            public URI getBaseUri() {
                return null;
            }

            @Override
            public UriBuilder getBaseUriBuilder() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getPathParameters() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getPathParameters(boolean decode) {
                return null;
            }

            @Override
            public List<String> getMatchedURIs() {
                return null;
            }

            @Override
            public List<String> getMatchedURIs(boolean decode) {
                return null;
            }

            @Override
            public List<Object> getMatchedResources() {
                return null;
            }

            @Override
            public URI resolve(URI uri) {
                return null;
            }

            @Override
            public URI relativize(URI uri) {
                return null;
            }
        };

        ctx.setProperty(FHIROperationContext.PROPNAME_URI_INFO, uriInfo);
        return ctx;
    }
}