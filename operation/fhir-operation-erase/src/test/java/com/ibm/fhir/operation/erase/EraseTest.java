/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.parser.FHIRJsonParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.operation.erase.adapter.ResourceEraseRecordAdapter;
import com.ibm.fhir.operation.erase.audit.EraseOperationAuditLogger;
import com.ibm.fhir.operation.erase.impl.EraseRest;
import com.ibm.fhir.operation.erase.impl.EraseRestFactory;
import com.ibm.fhir.operation.erase.impl.EraseRestImpl;
import com.ibm.fhir.operation.erase.mock.MockAuditLogService;
import com.ibm.fhir.operation.erase.mock.MockFHIRResourceHelpers;
import com.ibm.fhir.operation.erase.mock.MockHttpServletRequest;
import com.ibm.fhir.operation.erase.mock.MockSecurityContext;
import com.ibm.fhir.operation.erase.util.EraseParametersBuilder;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.ResourceEraseRecord.Status;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.search.compartment.CompartmentHelper;
import com.ibm.fhir.search.util.SearchHelper;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;

/**
 * Tests the Java code for the EraseOperation
 */
public class EraseTest {
    // Flip to TRUE to see which tenant is running during a test (or peek at the end of the test that failed).
    // if not specified, then it's the default.
    private static final Boolean DEBUG = Boolean.FALSE;

    private CompartmentHelper compartmentHelper;
    private SearchHelper searchHelper;

    private static final String LONG_STRING =
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
        compartmentHelper = new CompartmentHelper();
        searchHelper = new SearchHelper();
    }

    @BeforeMethod
    public void startMethod(Method method) throws FHIRException {
        // Configure the request context for our search tests
        FHIRRequestContext context = FHIRRequestContext.get();

        //Facilitate the switching of tenant configurations based on method name
        String tenant = "default";
        String methodName = method.getName();
        if (methodName.contains("_tenant_")) {
            int idx = methodName.indexOf("_tenant_") + "_tenant_".length();
            tenant = methodName.substring(idx);
            if (DEBUG) {
                System.out.println("Testing with Tenant: " + tenant);
            }
        }
        context.setTenantId(tenant);

        context.setOriginalRequestUri("https://localhost:9443/fhir-server/api/v4");
    }

    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }

    /**
     * verifies the dto
     * @param dto note these are optional
     * @param resourceType note these are optional
     * @param logicalId note these are optional
     * @param patient note these are optional
     * @param reason note these are optional
     * @param version note these are optional
     */
    public void verifyDto(EraseDTO dto, Class<? extends Resource> resourceType, String logicalId, Optional<String> patient, Optional<String> reason, Optional<Integer> version) {
        // These are always not null
        assertNotNull(dto.getResourceType());
        assertNotNull(dto.getLogicalId());

        assertEquals(dto.getResourceType(), resourceType.getSimpleName());
        assertEquals(dto.getLogicalId(), logicalId);

        // 0..1 optional
        if (patient.isPresent()) {
            assertNotNull(dto.getPatient());
            assertEquals(dto.getPatient(), patient.get());
        } else {
            assertNull(dto.getPatient());
        }

        if (reason.isPresent()) {
            assertNotNull(dto.getReason());
            assertEquals(dto.getReason(), reason.get());
        } else {
            assertNull(dto.getReason());
        }

        if (version.isPresent()) {
            assertNotNull(dto.getVersion());
            assertEquals(dto.getVersion(), version.get());
        } else {
            assertNull(dto.getVersion());
        }
    }

    /**
     * generates the context based on the role and method.
     * @param method
     * @param checkRole
     * @return
     */
    private FHIROperationContext generateContext(String method, String checkRole) {
        FHIROperationContext ctx = FHIROperationContext.createInstanceOperationContext("erase");
        ctx.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, method);
        ctx.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, new MockSecurityContext(checkRole));
        ctx.setProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST, new MockHttpServletRequest());
        return ctx;
    }

    @Test
    public void testEraseParametersBuilderNotEmpty() {
        Parameters parameters = EraseParametersBuilder.builder()
            .build(false);
        assertNotNull(parameters);
    }

    @Test
    public void testEraseParametersBuilderEmpty() {
        Parameters parameters = EraseParametersBuilder.builder()
            .build(true);
        assertNotNull(parameters);
    }

    @Test
    public void testEraseParametersBuilder() {
        Parameters parameters = EraseParametersBuilder.builder()
                .badPatient()
                .badReason()
                .nullPatient()
                .nullReason()
                .patient("Patient/1")
                .reason("The Real Reason")
                .id("1-2-3-4")
                .version(1)
            .build(true);
        assertNotNull(parameters);
        assertEquals(parameters.getParameter().size(), 8);
    }

    @Test
    public void testEraseParametersBuilderIncludeEmpty() {
        Parameters parameters = EraseParametersBuilder.builder()
                .badPatient()
                .badReason()
                .nullPatient()
                .nullReason()
                .nullId()
                .nullVersion()
                .patient("Patient/1")
                .reason("The Real Reason")
                .badId()
                .badVersion()
            .build(false);
        assertNotNull(parameters);
        assertEquals(parameters.getParameter().size(), 10);
    }

    @Test
    public void testEraseFactory() {
        Parameters parameters = EraseParametersBuilder.builder()
            .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("GET", "SAMPLE_ROLE");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
    }

    @Test
    public void testEraseOperationEnable_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("GET", "SAMPLE_ROLE");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.enabled();
        assertTrue(true);
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationEnable_tenant_disabled() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("GET", "SAMPLE_ROLE");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.enabled();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationEnable_tenant_noconfig() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("GET", "SAMPLE_ROLE");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.enabled();
        fail();
    }

    @Test
    public void testEraseOperationAuthorize_tenant_noconfig() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("GET", "SAMPLE_ROLE");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        assertTrue(true);
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorize_tenant_emptyroles() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("GET", "SAMPLE_ROLE");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeInvalid_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("GET", "SAMPLE_ROLE");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        fail();
    }

    @Test
    public void testEraseOperationAuthorizeValid_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("GET", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        assertTrue(true);
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeVerifyInvalidGet_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("GET", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeVerifyBadReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test
    public void testEraseOperationAuthorizeVerifyValidReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("My own reason")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        EraseDTO dto = erase.verify();
        assertNotNull(dto);
        verifyDto(dto, resourceType, logicalId, Optional.empty(), Optional.of("My own reason"), Optional.empty());
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationVerifyBadTypeWithLogicalId_tenant_defaulta() throws FHIROperationException {
        String logicalId = UUID.randomUUID().toString();
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("My own reason")
                .id(logicalId)
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;

        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationTooManyIds_tenant_defaulta() throws FHIROperationException {
        String logicalId1 = UUID.randomUUID().toString();
        String logicalId2 = UUID.randomUUID().toString();
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("My own reason")
                .id(logicalId1)
                .id(logicalId2)
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;

        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, null);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationBadId_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("My own reason")
                .badId()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;

        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, null);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAbsentId_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("My own reason")
                .absentId()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;

        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, null);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test
    public void testEraseOperationAuthorizeVerifyValidReasonAtTypeLevel_tenant_defaulta() throws FHIROperationException {
        String logicalId = UUID.randomUUID().toString();
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("My own reason")
                .id(logicalId)
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;

        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, null);
        assertNotNull(erase);
        erase.authorize();
        EraseDTO dto = erase.verify();
        assertNotNull(dto);
        verifyDto(dto, resourceType, logicalId, Optional.empty(), Optional.of("My own reason"), Optional.empty());
    }


    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeVerifyTwoReasons_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("1")
                .reason("2")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeVerifyBadReasonType_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .badReason()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeVerifyBadLongReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason(LONG_STRING)
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeVerifyBadEmptyReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .nullReason()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeAbsentVerifyBadEmptyReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .absentReason()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }


    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizePatientVerifyBadReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test
    public void testEraseOperationAuthorizePatientVerifyValidReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("My own reason")
                .patient("Patient/1")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        EraseDTO dto = erase.verify();
        assertNotNull(dto);
        verifyDto(dto, resourceType, logicalId, Optional.of("Patient/1"), Optional.of("My own reason"), Optional.empty());
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizePatientVerifyTwoReasons_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("1")
                .reason("2")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizePatientVerifyBadReasonType_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .badReason()
                .badPatient()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizePatientVerifyBadLongReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason(LONG_STRING)
                .patient("Patient/1")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizePatientVerifyBadEmptyReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .nullReason()
                .nullPatient()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizePatientsVerifyBadLongReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason(LONG_STRING)
                .patient("Patient/1")
                .patient("Patient/2")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeLongPatientVerifyBadLongReason_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason(LONG_STRING)
                .patient(LONG_STRING)
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test
    public void testEraseOperationAuthorizePatientVerifyWithReasonInCompartment_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("123-Reason")
                .patient("123-ptnt")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        EraseDTO dto = erase.verify();
        assertNotNull(dto);
        verifyDto(dto, resourceType, logicalId, Optional.of("123-ptnt"), Optional.of("123-Reason"), Optional.empty());
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAuthorizeNoPatientVerifyWithReasonInCompartment_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("123-Reason")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAbsentPatient_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("1")
                .absentPatient()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationAbsentReasonPatient_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .absentReason()
                .absentPatient()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test
    public void testLogException() {
        Parameters parameters = EraseParametersBuilder.builder()
                .absentReason()
                .absentPatient()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();

        EraseRestImpl erase = new EraseRestImpl("POST", new MockSecurityContext("FHIROpsAdmin"), parameters, resourceType, logicalId, compartmentHelper);
        java.util.List<Issue> issues = new ArrayList<>();
        erase.logException(issues, new Exception("Test"));
        assertEquals(issues.size(), 1);
    }

    @Test
    public void testMockProcessVersionFhirPathException() throws FHIRPathException {
        Parameters parameters = EraseParametersBuilder.builder()
                .absentReason()
                .absentPatient()
                .absentVersion()
                .absentId()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();

        EraseRestImpl erase = new EraseRestImpl("POST", new MockSecurityContext("FHIROpsAdmin"), parameters, resourceType, logicalId, compartmentHelper);

        // Return a bean
        EraseDTO bean = new EraseDTO();

        FHIRPathEvaluator evaluator = mock(FHIRPathEvaluator.class);
        EvaluationContext evaluationContext = new EvaluationContext(parameters);
        when(evaluator.evaluate(evaluationContext, "parameter.where(name = 'version').value"))
            .thenThrow(new FHIRPathException("Test"));

        java.util.List<Issue> issues = new ArrayList<>();
        erase.processVersion(bean, evaluator, evaluationContext, issues);
        assertEquals(issues.size(), 1);
    }

    @Test
    public void testMockProcessPatientFhirPathException() throws FHIRPathException {
        Parameters parameters = EraseParametersBuilder.builder()
                .absentReason()
                .absentPatient()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();

        EraseRestImpl erase = new EraseRestImpl("POST", new MockSecurityContext("FHIROpsAdmin"), parameters, resourceType, logicalId, compartmentHelper);

        // Return a bean
        EraseDTO bean = new EraseDTO();

        FHIRPathEvaluator evaluator = mock(FHIRPathEvaluator.class);
        EvaluationContext evaluationContext = new EvaluationContext(parameters);
        when(evaluator.evaluate(evaluationContext, "parameter.where(name = 'patient').value"))
            .thenThrow(new FHIRPathException("Test"));


        java.util.List<Issue> issues = new ArrayList<>();
        erase.processPatient(bean, evaluator, evaluationContext, issues);
        assertEquals(issues.size(), 1);
    }

    @Test
    public void testMockProcessLogicalIdFhirPathException() throws FHIRPathException {
        Parameters parameters = EraseParametersBuilder.builder()
                .absentReason()
                .absentPatient()
                .absentId()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;

        EraseRestImpl erase = new EraseRestImpl("POST", new MockSecurityContext("FHIROpsAdmin"), parameters, resourceType, null, compartmentHelper);

        // Return a bean
        EraseDTO bean = new EraseDTO();

        FHIRPathEvaluator evaluator = mock(FHIRPathEvaluator.class);
        EvaluationContext evaluationContext = new EvaluationContext(parameters);
        when(evaluator.evaluate(evaluationContext, "parameter.where(name = 'id').value"))
            .thenThrow(new FHIRPathException("Test"));

        java.util.List<Issue> issues = new ArrayList<>();
        erase.processLogicalId(bean, evaluator, evaluationContext, issues, null);
        assertEquals(issues.size(), 1);
    }

    @Test
    public void testMockProcessReasonFhirPathException() throws FHIRPathException {
        Parameters parameters = EraseParametersBuilder.builder()
                .absentReason()
                .absentPatient()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();

        EraseRestImpl erase = new EraseRestImpl("POST", new MockSecurityContext("FHIROpsAdmin"), parameters, resourceType, logicalId, compartmentHelper);

        // Return a bean
        EraseDTO bean = new EraseDTO();

        FHIRPathEvaluator evaluator = mock(FHIRPathEvaluator.class);
        EvaluationContext evaluationContext = new EvaluationContext(parameters);
        when(evaluator.evaluate(evaluationContext, "parameter.where(name = 'reason').value"))
            .thenThrow(new FHIRPathException("Test"));

        java.util.List<Issue> issues = new ArrayList<>();
        erase.processReason(bean, evaluator, evaluationContext, issues);
        assertEquals(issues.size(), 2);
    }

    @Test(expectedExceptions = {java.lang.Error.class})
    public void testOperationDefFail() throws FHIRParserException {
        FHIRJsonParser parser = mock(FHIRJsonParser.class, new Answer<Resource>() {
            @Override
            public Resource answer(InvocationOnMock invocation) throws Throwable {
                throw new Exception("E");
            }
        });

        EraseOperation operation = new EraseOperation();
        operation.generateOperationDefinition(parser);
    }

    @Test
    public void testOperation() {
        EraseOperation operation = new EraseOperation();
        OperationDefinition def = operation.buildOperationDefinition();
        assertNotNull(def);
    }

    @Test(expectedExceptions = {})
    public void testEraseOperation_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseOperation operation = new EraseOperation();
        Parameters parmetersReturn = operation.doInvoke(operationContext, resourceType, logicalId, "1", parameters,
                new MockFHIRResourceHelpers(false, false), searchHelper);
        assertNotNull(parmetersReturn);
    }

    @Test(expectedExceptions = {})
    public void testEraseOperationPartial_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseOperation operation = new EraseOperation();
        Parameters parmetersReturn = operation.doInvoke(operationContext, resourceType, logicalId, "1", parameters,
                new MockFHIRResourceHelpers(false, true), searchHelper);
        assertNotNull(parmetersReturn);
    }

    @Test(expectedExceptions = {})
    public void testEraseOperationPartialWithPatient_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .patient("Patient/1")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseOperation operation = new EraseOperation();
        Parameters parmetersReturn = operation.doInvoke(operationContext, resourceType, logicalId, "1", parameters,
                new MockFHIRResourceHelpers(false, true), searchHelper);
        assertNotNull(parmetersReturn);
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationFail_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = StructureDefinition.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseOperation operation = new EraseOperation();
        operation.doInvoke(operationContext, resourceType, logicalId, "1", parameters,
                new MockFHIRResourceHelpers(true, true), searchHelper);
        fail();
    }

    @Test
    public void testEraseOperationPartialWithPatient_tenant_mockaudit() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .patient("Patient/1")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseOperation operation = new EraseOperation();
        Parameters parmetersReturn = operation.doInvoke(operationContext, resourceType, logicalId, "1", parameters,
                new MockFHIRResourceHelpers(false, true), searchHelper);
        assertNotNull(parmetersReturn);
    }

    @Test
    public void testEraseOperationEraseOperationAuditLogger_tenant_mockaudit() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .patient("Patient/1")
                .build(false);
        assertNotNull(parameters);
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");

        EraseOperationAuditLogger auditLogger = new EraseOperationAuditLogger(operationContext);
        EraseDTO eraseDto = new EraseDTO();
        auditLogger.audit(parameters, eraseDto);
        assertTrue(true);
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationEraseOperationAuditLoggerLogErase_tenant_mockaudit() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .patient("Patient/1")
                .build(false);
        assertNotNull(parameters);
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");

        EraseOperationAuditLogger auditLogger = new EraseOperationAuditLogger(operationContext);
        auditLogger.logEraseOperation(new MockAuditLogService(true, true), parameters, Response.Status.OK, "reason", "patient");
        assertTrue(true);
    }

    @Test
    public void testEraseOperationEraseOperationAuditLoggerLogDisabled_tenant_mockaudit() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .patient("Patient/1")
                .build(false);
        assertNotNull(parameters);
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");

        EraseOperationAuditLogger auditLogger = new EraseOperationAuditLogger(operationContext);
        auditLogger.logEraseOperation(new MockAuditLogService(false, true), parameters, Response.Status.OK, "reason", "patient");
        assertTrue(true);
    }

    @Test(expectedExceptions = {com.ibm.fhir.exception.FHIROperationException.class})
    public void testEraseOperationResourceNotFound_tenant_mockaudit() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .patient("Patient/1")
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseOperation operation = new EraseOperation();
        operation.doInvoke(operationContext, resourceType, logicalId, "1", parameters,
                new MockFHIRResourceHelpers(false, false, true), searchHelper);
    }

    @Test
    public void testAdapter_tenant_mockaudit() throws FHIROperationException {
        ResourceEraseRecordAdapter adapter = new ResourceEraseRecordAdapter();
        EraseDTO eraseDto = new EraseDTO();
        eraseDto.setReason("dummy");
        Parameters parameter = adapter.adapt(new ResourceEraseRecord(), eraseDto);
        assertNotNull(parameter);
    }

    @Test
    public void testAdapterDone_tenant_mockaudit() throws FHIROperationException {
        ResourceEraseRecordAdapter adapter = new ResourceEraseRecordAdapter();
        EraseDTO eraseDto = new EraseDTO();
        eraseDto.setReason("dummy");
        ResourceEraseRecord eraseRecord = new ResourceEraseRecord();
        eraseRecord.setStatus(Status.DONE);
        Parameters parameter = adapter.adapt(eraseRecord, eraseDto);
        assertNotNull(parameter);
    }

    @Test
    public void testEraseOperationVersion_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("123-Reason")
                .patient("Patient/1")
                .version(1)
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();

        EraseDTO dto = erase.verify();
        assertNotNull(dto);
        verifyDto(dto, resourceType, logicalId, Optional.of("Patient/1"), Optional.of("123-Reason"), Optional.of(1));
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationVersionBad_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("123-Reason")
                .patient("Patient/1")
                .badVersion()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test
    public void testEraseOperationVersionNull_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("123-Reason")
                .patient("Patient/1")
                .nullVersion()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();

        EraseDTO dto = erase.verify();
        assertNotNull(dto);
        verifyDto(dto, resourceType, logicalId, Optional.of("Patient/1"), Optional.of("123-Reason"), Optional.empty());
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationVersionMany_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("123-Reason")
                .patient("Patient/1")
                .version(1)
                .version(2)
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testEraseOperationVersionAsbsent_tenant_defaulta() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("123-Reason")
                .patient("Patient/1")
                .absentVersion()
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseRest erase = EraseRestFactory.getInstance(operationContext, parameters, resourceType, logicalId);
        assertNotNull(erase);
        erase.authorize();
        erase.verify();
        fail();
    }

    @Test(expectedExceptions = {com.ibm.fhir.exception.FHIROperationException.class})
    public void testEraseOperationResourceNotSupportedGreater_tenant_mockaudit() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .patient("Patient/1")
                .version(100)
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseOperation operation = new EraseOperation();
        operation.doInvoke(operationContext, resourceType, logicalId, "1", parameters,
                new MockFHIRResourceHelpers(false, false, false, true, false), searchHelper);
    }

    @Test(expectedExceptions = {com.ibm.fhir.exception.FHIROperationException.class})
    public void testEraseOperationResourceNotLatest_tenant_mockaudit() throws FHIROperationException {
        Parameters parameters = EraseParametersBuilder.builder()
                .reason("reason")
                .patient("Patient/1")
                .version(1)
                .build(false);
        assertNotNull(parameters);
        Class<? extends Resource> resourceType = Patient.class;
        String logicalId = UUID.randomUUID().toString();
        FHIROperationContext operationContext = generateContext("POST", "FHIROpsAdmin");
        EraseOperation operation = new EraseOperation();
        operation.doInvoke(operationContext, resourceType, logicalId, "1", parameters,
                new MockFHIRResourceHelpers(false, false, false, false, true), searchHelper);
    }
}