/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.ig.davinci.hrex.test.tool.HREXExamplesUtil;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Coverage;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Builder;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Patient.Communication;
import com.ibm.fhir.model.resource.Provenance.Agent;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AddressUse;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.ContactPointSystem;
import com.ibm.fhir.model.type.code.ContactPointUse;
import com.ibm.fhir.model.type.code.IdentifierUse;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.davinci.hrex.MemberMatchOperation;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationAdapter;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationFactory;
import com.ibm.fhir.operation.davinci.hrex.provider.MemberMatchFactory;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy.GetPatientIdentifier;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy.MemberMatchCovergeSearchCompiler;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy.MemberMatchPatientSearchCompiler;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult.ResponseType;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchStrategy;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.context.FHIRSystemHistoryContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResponse;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;
import com.ibm.fhir.server.spi.operation.FHIRRestOperationResponse;
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.exception.FHIRValidationException;

/**
 * Run the Unit Tests for MemberMatch
 */
public class MemberMatchTest {

    private static final Extension DATA_ABSENT =
            Extension
                .builder()
                .url("http://hl7.org/fhir/StructureDefinition/data-absent-reason")
                .value(Code.of("unknown"))
                .build();

    private static final com.ibm.fhir.model.type.String DATA_ABSENT_STRING =
            com.ibm.fhir.model.type.String
                .builder()
                .extension(DATA_ABSENT)
                .build();

    private static final Reference DATA_ABSENT_REFERENCE = Reference
        .builder()
        .extension(DATA_ABSENT)
        .build();

    private static final Uri DATA_ABSENT_URI = Uri.builder().extension(DATA_ABSENT).build();

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("src/test/resources");
    }

    public void createContext(String tenant) throws FHIRException {
        // Configure the request context for our search tests
        FHIRRequestContext.remove();
        FHIRRequestContext context = FHIRRequestContext.get();
        FHIRRequestContext.set(context);

        // Facilitate the switching of tenant configurations based on method name
        context.setTenantId(tenant);
    }

    @Test
    public void testOperationDefinition() {
        MemberMatchOperation operation = new MemberMatchOperation();
        OperationDefinition definition = operation.getDefinition();
        assertNotNull(definition);
    }

    @Test
    public void testConfiguration_EnabledByDefault() throws FHIRException {
        createContext("default");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertTrue(adapter.enabled());
        assertEquals(adapter.getStrategyKey(), "default");
        FHIRRequestContext.remove();
    }

    @Test(expectedExceptions = { NullPointerException.class })
    public void testConfiguration_NoExtendedProps() throws FHIRException {
        createContext("default");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        adapter.getExtendedStrategyPropertyGroup().getJsonObj();
    }

    @Test
    public void testConfiguration_EnabledOnTenant() throws FHIRException {
        createContext("defaulta");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertTrue(adapter.enabled());
        assertEquals(adapter.getStrategyKey(), "defaulta");
        FHIRRequestContext.remove();
    }

    @Test
    public void testConfiguration_Require() throws FHIRException {
        createContext("defaulta");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertTrue(adapter.enabled());
        FHIRRequestContext.remove();
    }

    @Test
    public void testConfiguration_Default() throws FHIRException {
        createContext("default");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertTrue(adapter.enabled());
        FHIRRequestContext.remove();
    }

    @Test
    public void testConfiguration_Disabled() throws FHIRException {
        createContext("disabled");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertFalse(adapter.enabled());
        FHIRRequestContext.remove();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMemberMatchResult_NoResponseTypeSpecified() {
        MemberMatchResult.builder().build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMemberMatchResult_SingleNoValueSpecified() {
        MemberMatchResult
            .builder()
            .responseType(ResponseType.SINGLE)
            .build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMemberMatchResult_SingleEmptyValueSpecified() {
        MemberMatchResult
            .builder()
            .responseType(ResponseType.SINGLE)
            .value("")
            .build();
    }

    @Test
    public void testMemberMatchFactory() throws FHIROperationException {
        MemberMatchFactory factory = MemberMatchFactory.factory();
        assertNotNull(factory);
        MemberMatchStrategy strategy = factory.getStrategy(new ConfigurationAdapter() {

            @Override
            public boolean enabled() {
                return false;
            }

            @Override
            public String getStrategyKey() {
                return "default";
            }

            @Override
            public PropertyGroup getExtendedStrategyPropertyGroup() {
                return null;
            }
        });
        assertNotNull(strategy);

    }

    @Test(expectedExceptions= {FHIROperationException.class})
    public void testMemberMatchFactory_BadKey() throws FHIROperationException {
        MemberMatchFactory factory = MemberMatchFactory.factory();
        assertNotNull(factory);
        factory.getStrategy(new ConfigurationAdapter() {

            @Override
            public boolean enabled() {
                return false;
            }

            @Override
            public String getStrategyKey() {
                return "defaultx";
            }

            @Override
            public PropertyGroup getExtendedStrategyPropertyGroup() {
                return null;
            }
        });
    }

    @Test
    public void testMemberMatchResult_All() {
        MemberMatchResult multiple = MemberMatchResult
            .builder()
            .responseType(ResponseType.MULTIPLE)
            .build();
        assertNotNull(multiple.getResponseType());
        assertEquals(multiple.getResponseType(), ResponseType.MULTIPLE);

        MemberMatchResult noMatch = MemberMatchResult
            .builder()
            .responseType(ResponseType.NO_MATCH)
            .build();
        assertNotNull(noMatch.getResponseType());
        assertEquals(noMatch.getResponseType(), ResponseType.NO_MATCH);

        MemberMatchResult single =
                MemberMatchResult
                    .builder()
                    .responseType(ResponseType.SINGLE)
                    .value("12345")
                    .type("system1", "code1")
                    .type("system2", "code2")
                    .system("my-system-identifier")
                    .build();
        assertNotNull(single.getResponseType());
        assertEquals(single.getResponseType(), ResponseType.SINGLE);
        assertNotNull(single.getSystem());
        assertEquals(single.getSystem(), "my-system-identifier");
        assertNotNull(single.getValue());
        assertEquals(single.getValue(), "12345");
        assertEquals(single.getTypes().size(), 3);
        assertTrue(single.getTypes().containsKey("system1"));
        assertTrue(single.getTypes().containsKey("system2"));
        assertTrue(single.getTypes().containsKey("http://terminology.hl7.org/CodeSystem/v2-0203"));
    }

    @Test
    public void testMemberMatchStrategy_OutputSingle() {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        MemberMatchResult single =
                MemberMatchResult
                    .builder()
                    .responseType(ResponseType.SINGLE)
                    .value("12345")
                    .type("system1", "code1")
                    .type("system2", "code2")
                    .system("my-system-identifier")
                    .build();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.setFHIROperationContext(ctx);
        Parameters output = strategy.output(single);
        assertNotNull(output);
        Parameters.Parameter param = output.getParameter().get(0);
        assertNotNull(param.getName());
        assertEquals(param.getName().getValue(), "MemberIdentifier");
    }

    @Test
    public void testMemberMatchStrategy_OutputSingle_NoSystem() {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        MemberMatchResult single =
                MemberMatchResult
                    .builder()
                    .responseType(ResponseType.SINGLE)
                    .value("12345")
                    .type("system1", "code1")
                    .type("system2", "code2")
                    .build();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.setFHIROperationContext(ctx);
        Parameters output = strategy.output(single);
        assertNotNull(output);
        Parameters.Parameter param = output.getParameter().get(0);
        assertNotNull(param.getName());
        assertEquals(param.getName().getValue(), "MemberIdentifier");
    }

    @Test
    public void testMemberMatchStrategy_Output_MultipleMatch() {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        MemberMatchResult multipleMatch =
                MemberMatchResult
                    .builder()
                    .responseType(ResponseType.MULTIPLE)
                    .build();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.setFHIROperationContext(ctx);
        Parameters output = strategy.output(multipleMatch);
        assertNotNull(output);
        assertNotNull(ctx.getProperty(FHIROperationContext.PROPNAME_RESPONSE));
        assertNotNull(ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE));
        Integer status = (Integer) ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE);
        assertEquals(status.intValue(), 422);

        Response response = (Response) ctx.getProperty(FHIROperationContext.PROPNAME_RESPONSE);
        assertNotNull(response);
    }

    @Test
    public void testMemberMatchStrategy_Output_NoMatch_MultipleMatch() {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        MemberMatchResult noMatch =
                MemberMatchResult
                    .builder()
                    .responseType(ResponseType.NO_MATCH)
                    .build();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.setFHIROperationContext(ctx);
        Parameters output = strategy.output(noMatch);
        assertNotNull(output);
        assertNotNull(ctx.getProperty(FHIROperationContext.PROPNAME_RESPONSE));
        assertNotNull(ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE));
        Integer status = (Integer) ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE);
        assertEquals(status.intValue(), 422);

        Response response = (Response) ctx.getProperty(FHIROperationContext.PROPNAME_RESPONSE);
        assertNotNull(response);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testMemberMatchStrategy_Execute_Null() throws FHIROperationException {
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.execute(null, null, new LocalResourceHelpers());
    }

    @Test
    public void testInputPatient() throws Exception {
        Parameters input = generateInput();
        Patient ptnt = input.getParameter().get(0).getResource().as(Patient.class);

        // Enrich the test data with an English Language as a preferred communication method.
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        ptnt = ptnt
            .toBuilder()
            .communication(Communication
                .builder()
                .language(CodeableConcept
                    .builder()
                    .coding(Coding
                        .builder()
                        .system(Uri.of("urn:ietf:bcp:47"))
                        .code(Code.of("en"))
                        .build())
                    .text("English")
                    .build())
                .preferred(Boolean.TRUE)
                .build())
            .telecom(ContactPoint
                .builder()
                .system(ContactPointSystem.PHONE)
                .use(ContactPointUse.HOME)
                .value("1-000-000-0000")
                .build(), ContactPoint
                    .builder()
                    .system(ContactPointSystem.PHONE)
                    .use(ContactPointUse.HOME)
                    .value("2-000-000-0000")
                    .build())
            .build();

        ptnt.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 6);
    }

    @Test
    public void testInputCoverage() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .beneficiary(Reference
                        .builder()
                        .reference("Patient/2-2-3-4")
                        .build())
                    .payor(Reference
                        .builder()
                        .reference("Organization/1-2-3-4")
                        .build())
                    .subscriber(Reference
                        .builder()
                        .reference("Patient/2-2-3-4")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 4);
    }

    @Test
    public void testInputCoverageIdentifiersUsual() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .identifier(Identifier
                        .builder()
                        .system(Uri.of("http://test.com/sys"))
                        .value("1-2-3-4")
                        .use(IdentifierUse.USUAL)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageIdentifiersUsualAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .identifier(Identifier
                        .builder()
                        .system(DATA_ABSENT_URI)
                        .value(DATA_ABSENT_STRING)
                        .use(IdentifierUse.USUAL)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageIdentifiersUsualAbsentValueOnly() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .identifier(Identifier
                        .builder()
                        .system(Uri.of("http://test.com/sys"))
                        .value(DATA_ABSENT_STRING)
                        .use(IdentifierUse.USUAL)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageIdentifiersNoValue() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        Coverage.Builder builder = coverage.toBuilder();
        builder.setValidating(false);
        builder = builder
            .identifier(Identifier
                .builder()
                .system(Uri.of("http://test.com/sys"))
                .use(IdentifierUse.USUAL)
                .build());
        coverage = builder.build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageIdentifiersUsualNoSys() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .identifier(Identifier
                        .builder()
                        .value("1-2-3-4")
                        .use(IdentifierUse.USUAL)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageIdentifiersTemp() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .identifier(Identifier
                        .builder()
                        .use(IdentifierUse.TEMP)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageEmpty() throws Exception {
        Coverage.Builder builder = Coverage.builder();
        builder.setValidating(false);
        Coverage coverage = builder.build();
        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 0);

        Identifier.Builder idBuilder = Identifier.builder();
        idBuilder.setValidating(false);
        assertFalse(compiler.visit("test", 1, idBuilder.build()));
    }

    @Test
    public void testInputCoverageDataAbsentString() throws Exception {
        Coverage.Builder builder = Coverage.builder();
        builder.setValidating(false);
        builder =
                builder
                    .beneficiary(DATA_ABSENT_REFERENCE)
                    .subscriber(DATA_ABSENT_REFERENCE)
                    .payor(DATA_ABSENT_REFERENCE)
                    .subscriberId(DATA_ABSENT_STRING);
        Coverage coverage = builder.build();
        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 1);
    }

    @Test
    public void testInputCoverageDataAbsentReference() throws Exception {
        Coverage.Builder builder = Coverage.builder();
        builder.setValidating(false);
        builder = builder
            .beneficiary(DATA_ABSENT_REFERENCE)
            .subscriber(DATA_ABSENT_REFERENCE)
            .payor(DATA_ABSENT_REFERENCE)
            .subscriberId(DATA_ABSENT_STRING);
        Coverage coverage = builder.build();
        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 1);
    }

    @Test
    public void testInputCoverageTypes() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .clazz(Coverage.Class
                        .builder()
                        .type(CodeableConcept
                            .builder()
                            .coding(Coding
                                .builder()
                                .system(Uri.of("urn:ietf:bcp:47"))
                                .code(Code.of("en"))
                                .build())
                            .text("English")
                            .build())
                        .value("Test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);

        Agent.Builder builder = Agent.builder();
        builder.setValidating(false);
        assertTrue(compiler.visit("test", 0, builder.build()));
        assertTrue(compiler.visit("class", 0, builder.build()));
    }

    @Test
    public void testInputCoverageTypesDataAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .clazz(Coverage.Class
                        .builder()
                        .type(CodeableConcept
                            .builder()
                            .coding(Coding
                                .builder()
                                .system(Uri.of("urn:ietf:bcp:47"))
                                .code(Code.of("en"))
                                .build())
                            .text("English")
                            .build())
                        .value(DATA_ABSENT_STRING)
                        .build())
                    .build();
        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageTypesDataAbsentCode() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .clazz(Coverage.Class
                        .builder()
                        .type(CodeableConcept
                            .builder()
                            .coding(Coding.builder().system(Uri.of("urn:ietf:bcp:47")).code(Code.builder().extension(DATA_ABSENT).build()).build())
                            .text("English")
                            .build())
                        .value("test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageTypesDataAbsentSystem() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .clazz(Coverage.Class
                        .builder()
                        .type(CodeableConcept
                            .builder()
                            .coding(Coding
                                .builder()
                                .system(Uri.builder().extension(DATA_ABSENT).build())
                                .code(Code.of("test"))
                                .build())
                            .text("English")
                            .build())
                        .value("test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageTypesCodeableConceptDataAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage
                    .toBuilder()
                    .clazz(Coverage.Class
                        .builder()
                        .type(CodeableConcept
                            .builder()
                            .extension(DATA_ABSENT)
                            .text("English")
                            .build())
                        .value(DATA_ABSENT_STRING)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageTypesCodeableConceptEmpty() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);
        Coverage.Class.Builder builder = Coverage.Class.builder();
        builder.setValidating(false);
        coverage =
                coverage.toBuilder().clazz(builder.value("test").build()).build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageTypesClassDataAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);
        Coverage.Class.Builder builder = Coverage.Class.builder();
        builder.setValidating(false);

        coverage =
                coverage.toBuilder().clazz(builder.extension(DATA_ABSENT).build()).build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageTypesCodeAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        Coding.Builder builder = Coding.builder();
        builder.setValidating(false);
        builder = builder.system(Uri.of("urn:ietf:bcp:47"));

        coverage =
                coverage
                    .toBuilder()
                    .clazz(Coverage.Class
                        .builder()
                        .type(CodeableConcept
                            .builder()
                            .coding(builder.build())
                            .text("English")
                            .build())
                        .value("Test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputCoverageTypesUriAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        Coding.Builder builder = Coding.builder();
        builder.setValidating(false);
        builder = builder.code(Code.of("test"));

        coverage =
                coverage
                    .toBuilder()
                    .clazz(Coverage.Class
                        .builder()
                        .type(CodeableConcept
                            .builder()
                            .coding(builder.build())
                            .text("English")
                            .build())
                        .value("Test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 2);
    }

    @Test
    public void testInputPatientEmpty() throws Exception {
        Patient.Builder builder = Patient.builder();
        builder.setValidating(false);
        Patient ptnt = builder.build();

        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        ptnt.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 0);
    }

    @Test
    public void testInputMemberMatchPatientSearchCompiler() throws Exception {
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();

        // no match on Backbone Element
        Agent.Builder builder = Agent.builder();
        builder.setValidating(false);
        assertFalse(compiler.visit("nomatch", 1, builder.build()));

        // unexpected visitor match
        assertFalse(compiler.visit("communication", 1, builder.build()));

        // No Language
        Patient.Communication.Builder commsBuilder = Patient.Communication.builder();
        commsBuilder.setValidating(false);
        assertFalse(compiler.visit("communication", 1, commsBuilder.build()));
    }

    @Test
    public void testInputPatientNoSystem() throws Exception {
        Parameters input = generateInput();
        Patient ptnt = input.getParameter().get(0).getResource().as(Patient.class);

        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();

        Coding.Builder builder = Coding.builder();
        builder.setValidating(false);

        builder = builder.code(Code.of("en"));
        Patient.Builder b = ptnt.toBuilder();
        b.setValidating(false);

        Communication.Builder c = Communication.builder();
        c.setValidating(false);
        ptnt = b
            .communication(c
                .language(CodeableConcept
                    .builder()
                    .coding(builder.build())
                    .text("English")
                    .build())
                .preferred(Boolean.TRUE)
                .build())
            .telecom(ContactPoint
                .builder()
                .system(ContactPointSystem.PHONE)
                .use(ContactPointUse.HOME)
                .value("1-000-000-0000")
                .build(), ContactPoint
                    .builder()
                    .system(ContactPointSystem.PHONE)
                    .use(ContactPointUse.HOME)
                    .value("2-000-000-0000")
                    .build())
            .build();

        ptnt.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 5);
    }

    @Test
    public void testInputPatientNoCode() throws Exception {
        Parameters input = generateInput();
        Patient ptnt = input.getParameter().get(0).getResource().as(Patient.class);

        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();

        Coding.Builder builder = Coding.builder();
        builder.setValidating(false);

        builder = builder.system(Uri.of("urn:ietf:bcp:47"));
        Patient.Builder b = ptnt.toBuilder();
        b.setValidating(false);

        Communication.Builder c = Communication.builder();
        c.setValidating(false);
        ptnt = b
            .communication(c.language(CodeableConcept.builder().coding(builder.build()).text("English").build()).preferred(Boolean.TRUE).build())
            .telecom(ContactPoint.builder().system(ContactPointSystem.PHONE).use(ContactPointUse.HOME).value("1-000-000-0000").build(), ContactPoint
                .builder()
                .system(ContactPointSystem.PHONE)
                .use(ContactPointUse.HOME)
                .value("2-000-000-0000")
                .build())
            .build();

        ptnt.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 5);
    }

    @Test
    public void testPatientGender() throws Exception {
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        Code code = Code.of("Test");
        compiler.visit("no-match", 1, code);
        assertEquals(compiler.getSearchParameters().size(), 0);

        Code codeGender = Code.builder().extension(DATA_ABSENT).build();
        compiler.visit("gender", 1, codeGender);
        assertEquals(compiler.getSearchParameters().size(), 0);

        Code gender = Code.of("female");
        compiler.visit("gender", 1, gender);
        assertEquals(compiler.getSearchParameters().size(), 1);
    }

    @Test
    public void testPatientDate() throws Exception {
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        Date date = Date.of("2017-10-01");
        compiler.visit("no-match", 1, date);
        assertEquals(compiler.getSearchParameters().size(), 0);

        Date dateAbsent = Date.builder().extension(DATA_ABSENT).build();
        compiler.visit("birthDate", 1, dateAbsent);
        assertEquals(compiler.getSearchParameters().size(), 0);

        compiler.visit("birthDate", 1, date);
        assertEquals(compiler.getSearchParameters().size(), 1);
    }

    @Test
    public void testPatientContactPoint() throws Exception {
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        ContactPoint.Builder builder = ContactPoint.builder();
        builder.setValidating(false);
        compiler.visit("no-match", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        ContactPoint contactPointAbsent = ContactPoint.builder().extension(DATA_ABSENT).build();
        compiler.visit("telecom", 1, contactPointAbsent);
        assertEquals(compiler.getSearchParameters().size(), 0);

        builder =
                ContactPoint
                    .builder()
                    .use(ContactPointUse.HOME)
                    .system(ContactPointSystem.PHONE)
                    .rank(PositiveInt.of(1))
                    .value(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build());
        compiler.visit("telecom", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        builder = ContactPoint.builder().use(ContactPointUse.HOME).system(ContactPointSystem.PHONE).rank(PositiveInt.of(1)).value("1-2-3-4-5-6");
        compiler.visit("telecom", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);
    }

    @Test
    public void testPatientHumanName() throws Exception {
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        HumanName.Builder builder = HumanName.builder();
        builder.setValidating(false);
        compiler.visit("no-match", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        HumanName humanNameAbsent = HumanName.builder().extension(DATA_ABSENT).build();
        compiler.visit("name", 1, humanNameAbsent);
        assertEquals(compiler.getSearchParameters().size(), 0);

        builder = HumanName.builder().family("Test").given(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build());
        compiler.visit("name", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);

        builder = HumanName.builder().family(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build()).given("Test");
        compiler.visit("name", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);

        builder = HumanName.builder().family("Test Family").given("Test");
        compiler.visit("name", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);
        assertEquals(compiler.getSearchParameters().get("name").size(), 2);
    }

    @Test
    public void testPatientIdentifier() {
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        Identifier.Builder builder = Identifier.builder();
        builder.setValidating(false);
        compiler.visit("no-match", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        Identifier absent = Identifier.builder().extension(DATA_ABSENT).build();
        compiler.visit("identifier", 1, absent);
        assertEquals(compiler.getSearchParameters().size(), 0);

        // Value Data Absent
        builder =
                Identifier
                    .builder()
                    .system(Uri.of("http://test.com/sys"))
                    .value(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build())
                    .use(IdentifierUse.USUAL);
        compiler.visit("identifier", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        // Value Missing
        builder = Identifier.builder().system(Uri.of("http://test.com/sys")).use(IdentifierUse.USUAL);
        compiler.visit("identifier", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        builder = Identifier.builder().system(com.ibm.fhir.model.type.Uri.builder().extension(DATA_ABSENT).build()).value("1-2-3-4").use(IdentifierUse.USUAL);
        compiler.visit("identifier", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        // Unsupported Identifier Type
        builder = Identifier.builder().system(Uri.of("http://test.com/sys")).value("1-2-3-4").use(IdentifierUse.OLD);
        compiler.visit("identifier", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        builder = Identifier.builder().system(Uri.of("http://test.com/sys")).value("1-2-3-4").use(IdentifierUse.USUAL);
        compiler.visit("identifier", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);
    }

    @Test
    public void testPatientAddress() {
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        Address.Builder builder = Address.builder();
        builder.setValidating(false);
        compiler.visit("no-match", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        // Top Level Data Absent
        Address absent = Address.builder().extension(DATA_ABSENT).build();
        compiler.visit("address", 1, absent);
        assertEquals(compiler.getSearchParameters().size(), 0);

        // Parts are Data Absent
        builder = Address.builder().line(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build());
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        builder = Address.builder().city(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build());
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        builder = Address.builder().country(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build());
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        builder = Address.builder().postalCode(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build());
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        builder = Address.builder().state(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build());
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        // Parts are Present in simple example
        compiler = new MemberMatchPatientSearchCompiler();
        builder = Address.builder().line("1234");
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);

        compiler = new MemberMatchPatientSearchCompiler();
        builder = Address.builder().city("city");
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);

        compiler = new MemberMatchPatientSearchCompiler();
        builder = Address.builder().country("1234");
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);

        compiler = new MemberMatchPatientSearchCompiler();
        builder = Address.builder().postalCode("1234");
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);

        compiler = new MemberMatchPatientSearchCompiler();
        builder = Address.builder().state("1234");
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);

        // Wrong Type of Use
        compiler = new MemberMatchPatientSearchCompiler();
        builder = Address.builder().state("1234").use(AddressUse.TEMP);
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 0);

        // Accepted Type of Use
        compiler = new MemberMatchPatientSearchCompiler();
        builder = Address.builder().state("1234").use(AddressUse.HOME);
        compiler.visit("address", 1, builder.build());
        assertEquals(compiler.getSearchParameters().size(), 1);
    }

    @Test
    public void testGetPatientIdentifier() throws Exception {
        GetPatientIdentifier compiler = new GetPatientIdentifier();
        Identifier.Builder builder = Identifier.builder();
        builder.setValidating(false);
        compiler.visit("no-match", 1, builder.build());
        assertNull(compiler.getSystem());
        assertNull(compiler.getValue());

        compiler = new GetPatientIdentifier();
        Identifier absent = Identifier.builder().extension(DATA_ABSENT).build();
        compiler.visit("identifier", 1, absent);
        assertNull(compiler.getSystem());
        assertNull(compiler.getValue());

        // Value Data Absent
        compiler = new GetPatientIdentifier();
        builder =
                Identifier
                    .builder()
                    .type(CodeableConcept
                            .builder()
                                .coding(
                                    Coding.builder()
                                        .system(Uri.of("http://terminology.hl7.org/CodeSystem/v2-0203"))
                                        .code(Code.of("MB"))
                                        .build())
                                .build())
                    .system(Uri.of("http://test.com/sys"))
                    .value(com.ibm.fhir.model.type.String.builder().extension(DATA_ABSENT).build())
                    .use(IdentifierUse.USUAL);
        compiler.visit("identifier", 1, builder.build());
        assertNotNull(compiler.getSystem());
        assertNull(compiler.getValue());

        // Value Missing
        compiler = new GetPatientIdentifier();
        builder = Identifier.builder().system(Uri.of("http://test.com/sys")).use(IdentifierUse.USUAL).type(CodeableConcept
            .builder()
            .coding(
                Coding.builder()
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v2-0203"))
                    .code(Code.of("MB"))
                    .build())
            .build());
        compiler.visit("identifier", 1, builder.build());
        assertNotNull(compiler.getSystem());
        assertNull(compiler.getValue());

        compiler = new GetPatientIdentifier();
        builder = Identifier.builder().system(com.ibm.fhir.model.type.Uri.builder().extension(DATA_ABSENT).build()).value("1-2-3-4").use(IdentifierUse.USUAL).type(CodeableConcept
            .builder()
            .coding(
                Coding.builder()
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v2-0203"))
                    .code(Code.of("MB"))
                    .build())
            .build());
        compiler.visit("identifier", 1, builder.build());
        assertNull(compiler.getSystem());
        assertNotNull(compiler.getValue());

        // Unsupported Identifier Type
        compiler = new GetPatientIdentifier();
        builder = Identifier.builder().system(Uri.of("http://test.com/sys")).value("1-2-3-4").use(IdentifierUse.OLD).type(CodeableConcept
            .builder()
            .coding(
                Coding.builder()
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v2-0203"))
                    .code(Code.of("MB"))
                    .build())
            .build());
        compiler.visit("identifier", 1, builder.build());
        assertNull(compiler.getSystem());
        assertNull(compiler.getValue());

        compiler = new GetPatientIdentifier();
        builder = Identifier.builder().system(Uri.of("http://test.com/sys")).value("1-2-3-4").use(IdentifierUse.USUAL).type(CodeableConcept
            .builder()
            .coding(
                Coding.builder()
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v2-0203"))
                    .code(Code.of("MB"))
                    .build())
            .build());
        compiler.visit("identifier", 1, builder.build());
        assertNotNull(compiler.getSystem());
        assertNotNull(compiler.getValue());

        // Check 2nd Identifier... which should be ignored
        builder = Identifier.builder().system(Uri.of("http://test.com/sys1")).value("5431").use(IdentifierUse.USUAL).type(CodeableConcept
            .builder()
            .coding(
                Coding.builder()
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v2-0203"))
                    .code(Code.of("MB"))
                    .build())
            .build());
        compiler.visit("identifier", 1, builder.build());
        assertNotNull(compiler.getSystem());
        assertNotNull(compiler.getValue());
        assertEquals(compiler.getSystem(), "http://test.com/sys");
        assertEquals(compiler.getValue(), "1-2-3-4");
    }

    @Test
    public void testInputStrategyValidateMissingAll() throws Exception {
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters.Builder builder = Parameters.builder();
        builder =
                builder
                    .meta(Meta
                        .builder()
                        .profile(Canonical.of("http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in"))
                        .build());
        builder.setValidating(false);
        try {
            strategy.validate(builder.build());
            fail("Unexpected");
        } catch (FHIROperationException e) {
            assertNotNull(e.getIssues());
            assertFalse(e.getIssues().isEmpty());

            OperationOutcome.Issue issue = e.getIssues().get(0);
            assertEquals(issue.getCode(), IssueType.INVALID);
        }
    }

    @Test
    public void testInputStrategyValidateMissingAllButMemberPatient() throws Exception {
        Parameters source = generateInput();

        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters.Builder builder = Parameters.builder();
        builder =
                builder
                    .meta(Meta
                        .builder()
                        .profile(Canonical.of("http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in"))
                        .build())
                    .parameter(source.getParameter().get(0));
        builder.setValidating(false);
        try {
            strategy.validate(builder.build());
            fail("Unexpected");
        } catch (FHIROperationException e) {
            assertNotNull(e.getIssues());
            assertFalse(e.getIssues().isEmpty());

            OperationOutcome.Issue issue = e.getIssues().get(0);
            assertEquals(issue.getCode(), IssueType.INVALID);
        }
    }

    @Test
    public void testInputStrategyValidateMissingCoverageToLinkButMemberPatientCoverageToMatch() throws Exception {
        Parameters source = generateInput();

        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters.Builder builder = Parameters.builder();
        builder =
                builder
                    .meta(Meta
                        .builder()
                        .profile(Canonical.of("http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in"))
                        .build())
                    .parameter(source.getParameter().get(0), source.getParameter().get(1));
        builder.setValidating(false);
        try {
            strategy.validate(builder.build());
            assertTrue(true);
        } catch (FHIROperationException e) {
            fail("Unexpected");
        }
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testInputStrategyValidateResourceNoResource() throws Exception {
        String profile = "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage";
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.validateResource(FHIRValidator.validator(), Patient.class, null, profile);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testInputStrategyValidateResource_NoMatchResource() throws Exception {
        String profile = "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage";
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters badType = generateInput();
        strategy.validateResource(FHIRValidator.validator(), Patient.class, badType, profile);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testInputStrategyValidateResource_ErrorOnResource() throws Exception {
        String profile = "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage";
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Patient.Builder builder = Patient.builder();
        builder.setValidating(false);
        strategy.validateResource(FHIRValidator.validator(), Patient.class, builder.build(), profile);
    }

    @Test(expectedExceptions = { FHIROperationException.class })
    public void testInputStrategyValidateResource_BadValidation() throws Exception {
        FHIRValidator validator = mock(FHIRValidator.class);
        String profile = "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage";
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Patient.Builder builder = Patient.builder();
        builder.setValidating(false);
        Patient p = builder.build();

        when(validator.validate(p, profile)).thenThrow(new FHIRValidationException("Test", new Exception("e")));

        strategy.validateResource(validator, Patient.class, builder.build(), profile);
    }

    @Test
    public void testInputStrategyValidateResource() throws Exception {
        Parameters source = generateInput();

        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters.Builder builder = Parameters.builder();
        builder =
                builder
                    .meta(Meta
                        .builder()
                        .profile(Canonical.of("http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in"))
                        .build())
                    .parameter(source.getParameter().get(0));
        builder.setValidating(false);
        try {
            strategy.validate(builder.build());
            fail("Unexpected");
        } catch (FHIROperationException e) {
            assertNotNull(e.getIssues());
            assertFalse(e.getIssues().isEmpty());

            OperationOutcome.Issue issue = e.getIssues().get(0);
            assertEquals(issue.getCode(), IssueType.INVALID);
        }
    }

    @Test
    public void testInputStrategyValidateResourceNotAParam() throws Exception {
        Parameters source = generateInput();

        Parameters.Parameter.Builder b = source.getParameter().get(1).toBuilder();
        b = b.name("NotIntheParametersMap");

        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters.Builder builder = Parameters.builder();
        builder =
                builder
                    .meta(Meta
                        .builder()
                        .profile(Canonical.of("http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in"))
                        .build())
                    .parameter(source.getParameter().get(0), b.build());
        builder.setValidating(false);
        try {
            strategy.validate(builder.build());
            fail("Unexpected");
        } catch (FHIROperationException e) {
            assertNotNull(e.getIssues());
            assertFalse(e.getIssues().isEmpty());

            OperationOutcome.Issue issue = e.getIssues().get(0);
            assertEquals(issue.getCode(), IssueType.INVALID);
        }
    }

    @Test
    public void testMemberMatchStrategy_Execute_NoResults() throws Exception {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters output = strategy.execute(ctx, generateInput(), new LocalResourceHelpers(0));
        assertNotNull(output);
        assertEquals(output.getParameter().size(), 1);
        assertEquals(output.getParameter().get(0).getName().getValue(), "return");
        assertNull(output.getParameter().get(0).getValue());

        assertEquals(ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE), Integer.valueOf(422));
    }

    @Test
    public void testMemberMatchStrategy_Execute_TooMany() throws Exception {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters output = strategy.execute(ctx, generateInput(), new LocalResourceHelpers(10));
        assertNotNull(output);
        assertEquals(output.getParameter().size(), 1);
        assertEquals(output.getParameter().get(0).getName().getValue(), "return");
        assertNull(output.getParameter().get(0).getValue());

        assertEquals(ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE), Integer.valueOf(422));
    }

    @Test
    public void testMemberMatchStrategy_Execute_Single_NoCoverage() throws Exception {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters output = strategy.execute(ctx, generateInput(), new LocalResourceHelpers(1, 0));
        assertNotNull(output);
        assertEquals(output.getParameter().size(), 1);
        assertEquals(output.getParameter().get(0).getName().getValue(), "return");
        assertNull(output.getParameter().get(0).getValue());
        assertEquals(ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE), Integer.valueOf(422));
    }

    @Test
    public void testMemberMatchStrategy_Execute_Single_WithCoverage() throws Exception {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters output = strategy.execute(ctx, generateInput(), new LocalResourceHelpers(1, 1));
        assertNotNull(output);
        assertEquals(output.getParameter().size(), 1);
        assertEquals(output.getParameter().get(0).getName().getValue(), "MemberIdentifier");
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testMemberMatchStrategy_Execute_Single_WithException() throws Exception {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.execute(ctx, generateInput(), new LocalResourceHelpers(true));
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testOperationInvokeDisabled() throws Exception {
        createContext("disabled");
        FHIROperationContext operationContext = FHIROperationContext.createResourceTypeOperationContext("member-match");
        operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "POST");
        Class<? extends Resource> resourceType = Patient.class;
        MemberMatchOperation operation = new MemberMatchOperation();
        operation.invoke(operationContext, resourceType, null, null, generateInput(), new LocalResourceHelpers(1,1));
    }

    @Test
    public void testOperationInvoke() throws Exception {
        FHIROperationContext operationContext = FHIROperationContext.createResourceTypeOperationContext("member-match");
        operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "POST");
        Class<? extends Resource> resourceType = Patient.class;
        MemberMatchOperation operation = new MemberMatchOperation();
        Parameters output = operation.invoke(operationContext, resourceType, null, null, generateInput(), new LocalResourceHelpers(1,1));
        assertNotNull(output);
        assertEquals(output.getParameter().size(), 1);
        assertEquals(output.getParameter().get(0).getName().getValue(), "MemberIdentifier");
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testMemberMatchStrategy_Execute_Single_WithoutIdentifiers() throws Exception {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("member-match");
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters input = generateInput();

        List<Parameters.Parameter> params = new ArrayList<>();

        Parameters.Parameter param = input.getParameter().get(0);
        Patient ptnt = param.getResource().as(Patient.class);
        ptnt = ptnt.toBuilder()
                .identifier(Collections.emptyList())
                .build();

        param = param.toBuilder().resource(ptnt).build();
        params.add(param);
        params.add(input.getParameter().get(1));

        input = input.toBuilder()
                .parameter(params)
                .build();

        strategy.execute(ctx, input, new LocalResourceHelpers(1, 1));
    }

    /**
     * generates the input
     *
     * @return
     * @throws Exception
     */
    private static Parameters generateInput() throws Exception {
        return HREXExamplesUtil.readLocalJSONResource("100", "Parameters-member-match-in.json");
    }

    private static class LocalResourceHelpers implements FHIRResourceHelpers {
        private int responseBundleSize = 0;
        private int coverageBundleSize = 0;
        private boolean throwException = false;
        private boolean firstCall = true;

        public LocalResourceHelpers() {
            // NOP
        }

        public LocalResourceHelpers(int responseBundleSize) {
            this.responseBundleSize = responseBundleSize;
        }

        public LocalResourceHelpers(boolean throwException) {
            this.throwException = throwException;
        }

        public LocalResourceHelpers(int responseBundleSize, int coverageBundleSize) {
            this.responseBundleSize = responseBundleSize;
            this.coverageBundleSize = coverageBundleSize;
        }

        @Override
        public Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters, String requestUri,
            Resource contextResource) throws Exception {
            // TODO Auto-generated method stub
            return doSearch(type, compartment, compartmentId, queryParameters, requestUri, contextResource, false, true);
        }

        @Override
        public Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters, String requestUri,
            Resource contextResource, boolean checkIfInteractionAllowed, boolean alwaysIncludeResource) throws Exception {
            if (throwException) {
                throw new Exception("Test");
            }
            if (firstCall) {
                firstCall = false;
                if (responseBundleSize == 0 || responseBundleSize > 1) {
                    List<Bundle.Entry> entries = new ArrayList<>();
                    for (int i = 0; i < responseBundleSize; i++) {
                        Bundle.Entry.Builder builder = Bundle.Entry.builder();
                        builder.setValidating(false);
                        entries.add(builder.build());
                    }
                    return Bundle.builder()
                            .type(BundleType.COLLECTION)
                            .total(UnsignedInt.of(responseBundleSize))
                            .entry(entries)
                            .build();
                } else { // Must Be 1
                    List<Bundle.Entry> entries = new ArrayList<>(1);
                    Bundle.Entry.Builder builder = Bundle.Entry.builder();
                    builder.setValidating(false);

                    Parameters input = generateInput();
                    Patient ptnt = input.getParameter().get(0).getResource().as(Patient.class);
                    ptnt = ptnt
                        .toBuilder()
                        .communication(Communication
                            .builder()
                            .language(CodeableConcept
                                .builder()
                                .coding(Coding
                                    .builder()
                                    .system(Uri.of("urn:ietf:bcp:47"))
                                    .code(Code.of("en"))
                                    .build())
                                .text("English")
                                .build())
                            .preferred(Boolean.TRUE)
                            .build())
                        .telecom(ContactPoint
                            .builder()
                            .system(ContactPointSystem.PHONE)
                            .use(ContactPointUse.HOME)
                            .value("1-000-000-0000")
                            .build(), ContactPoint
                                .builder()
                                .system(ContactPointSystem.PHONE)
                                .use(ContactPointUse.HOME)
                                .value("2-000-000-0000")
                                .build())
                        .build();
                    builder = builder.resource(ptnt);
                    entries.add(builder.build());


                    return Bundle.builder()
                            .type(BundleType.COLLECTION)
                            .total(UnsignedInt.of(1))
                            .entry(entries)
                            .build();
                }
            } else {
                if (coverageBundleSize == 0) {
                    List<Bundle.Entry> entries = new ArrayList<>();
                    for (int i = 0; i < coverageBundleSize; i++) {
                        Bundle.Entry.Builder builder = Bundle.Entry.builder();
                        builder.setValidating(false);
                        entries.add(builder.build());
                    }
                    return Bundle.builder()
                            .type(BundleType.COLLECTION)
                            .total(UnsignedInt.of(coverageBundleSize))
                            .entry(entries)
                            .build();
                } else { // Must Be 1 or more
                    List<Bundle.Entry> entries = new ArrayList<>(1);
                    Bundle.Entry.Builder builder = Bundle.Entry.builder();
                    builder.setValidating(false);
                    entries.add(builder.build());

                    return Bundle.builder()
                            .type(BundleType.COLLECTION)
                            .total(UnsignedInt.of(1))
                            .entry(entries)
                            .build();
                }
            }
        }

        // Unused parts of the interface
        @Override
        public FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist, boolean doValidation) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, boolean doValidation, Integer ifNoneMatch) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public FHIRRestOperationResponse doPatch(String type, String id, FHIRPatch patch, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public FHIRRestOperationResponse doDelete(String type, String id, String searchQueryString) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public SingleResourceResult<? extends Resource> doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted, Resource contextResource,
            MultivaluedMap<String, String> queryParameters) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public Resource doVRead(String type, String id, String versionId, MultivaluedMap<String, String> queryParameters) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public Bundle doHistory(MultivaluedMap<String, String> queryParameters, String requestUri, String resourceType) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public Bundle doBundle(Bundle bundle, boolean skippableUpdates) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public FHIRPersistenceTransaction getTransaction() throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public int doReindex(FHIROperationContext operationContext, Builder operationOutcomeResult, Instant tstamp, List<Long> indexIds,
            String resourceLogicalId) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public List<Long> doRetrieveIndex(FHIROperationContext operationContext, String resourceTypeName, int count, Instant notModifiedAfter,
            Long afterIndexId) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public void validateInteraction(Interaction interaction, String resourceType) throws FHIROperationException {
            throw new AssertionError("Unused");
        }

        @Override
        public FHIRRestOperationResponse doCreateMeta(FHIRPersistenceEvent event, List<Issue> warnings, String type, Resource resource, String ifNoneExist)
            throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public FHIRRestOperationResponse doCreatePersist(FHIRPersistenceEvent event, List<Issue> warnings, Resource resource, PayloadPersistenceResponse offloadResponse) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public FHIRRestOperationResponse doUpdateMeta(FHIRPersistenceEvent event, String type, String id, FHIRPatch patch, Resource newResource,
            String ifMatchValue, String searchQueryString, boolean skippableUpdate, boolean doValidation, List<Issue> warnings) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public FHIRRestOperationResponse doPatchOrUpdatePersist(FHIRPersistenceEvent event, String type, String id, boolean isPatch, Resource newResource,
            Resource prevResource, List<Issue> warnings, boolean isDeleted, Integer ifNoneMatch, PayloadPersistenceResponse offloadResponse) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public Map<String, Object> buildPersistenceEventProperties(String type, String id, String version, FHIRSearchContext searchContext, FHIRSystemHistoryContext systemHistoryContext)
            throws FHIRPersistenceException {
            throw new AssertionError("Unused");
        }

        @Override
        public String generateResourceId() {
            throw new AssertionError("Unused");
        }

        @Override
        public PayloadPersistenceResponse storePayload(Resource resource, String logicalId, int newVersionNumber, String resourcePayloadKey) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public Resource doInvoke(FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId, Resource resource,
            MultivaluedMap<String, String> queryParameters) throws Exception {
            throw new AssertionError("Unused");
        }

        @Override
        public List<Issue> validateResource(Resource resource) throws FHIROperationException {
            throw new AssertionError("Unused");
        }
    }
}