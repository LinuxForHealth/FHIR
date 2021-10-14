/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.time.Instant;
import java.util.List;

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
import com.ibm.fhir.model.resource.OperationOutcome.Builder;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Patient.Communication;
import com.ibm.fhir.model.resource.Provenance.Agent;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ContactPointSystem;
import com.ibm.fhir.model.type.code.ContactPointUse;
import com.ibm.fhir.model.type.code.IdentifierUse;
import com.ibm.fhir.operation.davinci.hrex.MemberMatchOperation;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationAdapter;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationFactory;
import com.ibm.fhir.operation.davinci.hrex.provider.MemberMatchFactory;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy.MemberMatchCovergeSearchCompiler;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy.MemberMatchPatientSearchCompiler;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult.ResponseType;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchStrategy;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

/**
 * Run the Unit Tests for MemberMatch
 */
public class MemberMatchTest {

    private static final Extension DATA_ABSENT =
            Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build();

    private static final com.ibm.fhir.model.type.String DATA_ABSENT_STRING = com.ibm.fhir.model.type.String
                .builder()
                .extension(DATA_ABSENT).build();

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("src/test/resources");
    }

    public void createContext(String tenant) throws FHIRException {
        // Configure the request context for our search tests
        FHIRRequestContext.remove();
        FHIRRequestContext context = FHIRRequestContext.get();
        if (context == null) {
            context = new FHIRRequestContext();
        }
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
    public void testOperationInvoke() throws Exception {
        FHIROperationContext operationContext = FHIROperationContext.createResourceTypeOperationContext();
        operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "POST");
        Class<? extends Resource> resourceType = Patient.class;
        MemberMatchOperation operation = new MemberMatchOperation();
        Parameters output = operation.invoke(operationContext, resourceType, null, null, generateInput(), new LocalResourceHelpers());
        assertNotNull(output);
    }

    @Test
    public void testConfiguration_DisabledByDefault() throws FHIRException {
        createContext("default");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertFalse(adapter.enabled());
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
    public void testConfiguration_EnabledByDefault() throws FHIRException {
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
        assertFalse(adapter.enabled());
        FHIRRequestContext.remove();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMemberMatchResult_NoResponseTypeSpecified() {
        MemberMatchResult.builder().build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMemberMatchResult_SingleNoValueSpecified() {
        MemberMatchResult.builder().responseType(ResponseType.SINGLE).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMemberMatchResult_SingleEmptyValueSpecified() {
        MemberMatchResult.builder().responseType(ResponseType.SINGLE).value("").build();
    }

    @Test
    public void testMemberMatchFactory() {
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

    @Test
    public void testMemberMatchFactory_BadKey() {
        MemberMatchFactory factory = MemberMatchFactory.factory();
        assertNotNull(factory);
        MemberMatchStrategy strategy = factory.getStrategy(new ConfigurationAdapter() {

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
        assertNotNull(strategy);
        assertEquals("default", strategy.getMemberMatchIdentifier());
    }

    @Test
    public void testMemberMatchResult_All() {
        MemberMatchResult multiple = MemberMatchResult.builder().responseType(ResponseType.MULTIPLE).build();
        assertNotNull(multiple.getResponseType());
        assertEquals(multiple.getResponseType(), ResponseType.MULTIPLE);

        MemberMatchResult noMatch = MemberMatchResult.builder().responseType(ResponseType.NO_MATCH).build();
        assertNotNull(noMatch.getResponseType());
        assertEquals(noMatch.getResponseType(), ResponseType.NO_MATCH);

        MemberMatchResult single =
                MemberMatchResult.builder().responseType(ResponseType.SINGLE).value("12345").type("system1", "code1").type("system2", "code2").system("my-system-identifier").build();
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
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        MemberMatchResult single =
                MemberMatchResult.builder().responseType(ResponseType.SINGLE).value("12345").type("system1", "code1").type("system2", "code2").system("my-system-identifier").build();
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
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        MemberMatchResult single =
                MemberMatchResult.builder().responseType(ResponseType.SINGLE).value("12345").type("system1", "code1").type("system2", "code2").build();
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
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        MemberMatchResult multipleMatch =
                MemberMatchResult.builder().responseType(ResponseType.MULTIPLE).build();
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
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        MemberMatchResult noMatch =
                MemberMatchResult.builder().responseType(ResponseType.NO_MATCH).build();
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
    public void testMemberMatchStrategy_Execute() throws Exception {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters output = strategy.execute(ctx, generateInput(), new LocalResourceHelpers());

        assertNotNull(output);
        assertEquals(output.getParameter().get(0).getValue().as(com.ibm.fhir.model.type.Identifier.class).getValue().getValue(), "55678");
    }

    @Test
    public void testInputPatient() throws Exception {
        Parameters input = generateInput();
        Patient ptnt = input.getParameter().get(0).getResource().as(Patient.class);

        /*
         * Enrich the test data with an English Language as a preferred communication method.
         */
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        ptnt = ptnt.toBuilder()
                .communication(
                    Communication.builder()
                        .language(
                            CodeableConcept.builder()
                                .coding(
                                    Coding.builder()
                                        .system(Uri.of("urn:ietf:bcp:47"))
                                        .code(Code.of("en")).build())
                                .text("English")
                                .build())
                        .preferred(Boolean.TRUE)
                        .build())
                .telecom(ContactPoint.builder().system(ContactPointSystem.PHONE).use(ContactPointUse.HOME).value("1-000-000-0000").build(), ContactPoint.builder().system(ContactPointSystem.PHONE).use(ContactPointUse.HOME).value("2-000-000-0000").build()).build();

        ptnt.accept(compiler);
        System.out.println(compiler.getSearchParameters());
    }

    @Test
    public void testInputCoverage() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .beneficiary(Reference.builder().reference("Patient/2-2-3-4").build())
                    .payor(Reference.builder().reference("Organization/1-2-3-4").build())
                    .subscriber(Reference.builder().reference("Patient/2-2-3-4").build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        System.out.println(compiler.getSearchParameters());
    }

    @Test
    public void testInputCoverageIdentifiersUsual() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .identifier(Identifier.builder()
                        .system(Uri.of("http://test.com/sys"))
                        .value("1-2-3-4")
                        .use(IdentifierUse.USUAL)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        System.out.println(compiler.getSearchParameters());
    }

    @Test
    public void testInputCoverageIdentifiersUsualAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .identifier(Identifier.builder()
                        .system(Uri.builder()
                            .extension(DATA_ABSENT)
                            .build())
                        .value(com.ibm.fhir.model.type.String.builder()
                                .extension(DATA_ABSENT)
                            .build())
                        .use(IdentifierUse.USUAL)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        System.out.println(compiler.getSearchParameters());
    }

    @Test
    public void testInputCoverageIdentifiersUsualAbsentValueOnly() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .identifier(Identifier.builder()
                        .system(Uri.of("http://test.com/sys"))
                        .value(com.ibm.fhir.model.type.String.builder()
                                .extension(DATA_ABSENT)
                            .build())
                        .use(IdentifierUse.USUAL)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        System.out.println(compiler.getSearchParameters());
    }

    @Test
    public void testInputCoverageIdentifiersNoValue() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        Coverage.Builder builder = coverage.toBuilder();
        builder.setValidating(false);
        builder = builder.identifier(Identifier.builder()
                        .system(Uri.of("http://test.com/sys"))
                        .use(IdentifierUse.USUAL)
                        .build());
        coverage = builder.build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 3);
    }

    @Test
    public void testInputCoverageIdentifiersUsualNoSys() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .identifier(Identifier.builder()
                        .value("1-2-3-4")
                        .use(IdentifierUse.USUAL)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        System.out.println(compiler.getSearchParameters());
    }

    @Test
    public void testInputCoverageIdentifiersTemp() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .identifier(Identifier.builder()
                        .use(IdentifierUse.TEMP)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        System.out.println(compiler.getSearchParameters());
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
        builder = builder
                    .beneficiary(Reference.builder().reference(DATA_ABSENT_STRING).build())
                    .subscriber(Reference.builder().reference(DATA_ABSENT_STRING).build())
                    .payor(Reference.builder().reference(DATA_ABSENT_STRING).build())
                    .subscriberId(DATA_ABSENT_STRING);
        Coverage coverage = builder.build();
        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 1);
    }

    @Test
    public void testInputCoverageDataAbsentReference() throws Exception {
        Reference reference = Reference.builder().extension(DATA_ABSENT).build();

        Coverage.Builder builder = Coverage.builder();
        builder.setValidating(false);
        builder = builder
                    .beneficiary(reference)
                    .subscriber(reference)
                    .payor(reference)
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
                coverage.toBuilder()
                    .clazz(Coverage.Class.builder()
                        .type(CodeableConcept.builder()
                                .coding(
                                    Coding.builder()
                                        .system(Uri.of("urn:ietf:bcp:47"))
                                        .code(Code.of("en")).build())
                                .text("English")
                                .build())
                        .value("Test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        System.out.println(compiler.getSearchParameters());

        Agent.Builder builder = Agent.builder();
        builder.setValidating(false);
        assertFalse(compiler.visit("test", 0, builder.build()));
        assertFalse(compiler.visit("class", 0, builder.build()));
    }

    @Test
    public void testInputCoverageTypesDataAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .clazz(Coverage.Class.builder()
                        .type(CodeableConcept.builder()
                                .coding(
                                    Coding.builder()
                                        .system(Uri.of("urn:ietf:bcp:47"))
                                        .code(Code.of("en")).build())
                                .text("English")
                                .build())
                        .value(DATA_ABSENT_STRING)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 3);
    }

    @Test
    public void testInputCoverageTypesDataAbsentCode() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .clazz(Coverage.Class.builder()
                        .type(CodeableConcept.builder()
                                .coding(
                                    Coding.builder()
                                        .system(Uri.of("urn:ietf:bcp:47"))
                                        .code(Code.builder().extension(DATA_ABSENT).build()).build())
                                .text("English")
                                .build())
                        .value("test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 3);
    }

    @Test
    public void testInputCoverageTypesDataAbsentSystem() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .clazz(Coverage.Class.builder()
                        .type(CodeableConcept.builder()
                                .coding(
                                    Coding.builder()
                                        .system(Uri.builder().extension(DATA_ABSENT).build())
                                        .code(Code.of("test")).build())
                                .text("English")
                                .build())
                        .value("test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 3);
    }

    @Test
    public void testInputCoverageTypesCodeableConceptDataAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        coverage =
                coverage.toBuilder()
                    .clazz(Coverage.Class.builder()
                        .type(CodeableConcept.builder()
                                .extension(DATA_ABSENT)
                                .text("English")
                                .build())
                        .value(DATA_ABSENT_STRING)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 3);
    }

    @Test
    public void testInputCoverageTypesCodeableConceptEmpty() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);
        Coverage.Class.Builder builder = Coverage.Class.builder();
        builder.setValidating(false);
        coverage =
                coverage.toBuilder()
                    .clazz(builder
                        .value("test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 3);
    }

    @Test
    public void testInputCoverageTypesClassDataAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);
        Coverage.Class.Builder builder = Coverage.Class.builder();
        builder.setValidating(false);

        coverage =
                coverage.toBuilder()
                    .clazz( builder.extension(DATA_ABSENT)
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");
        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 3);
    }

    @Test
    public void testInputCoverageTypesCodeAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        Coding.Builder builder = Coding.builder();
        builder.setValidating(false);
        builder = builder.system(Uri.of("urn:ietf:bcp:47"));

        coverage =
                coverage.toBuilder()
                    .clazz(Coverage.Class.builder()
                        .type(CodeableConcept.builder()
                                .coding(builder.build())
                                .text("English")
                                .build())
                        .value("Test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 3);
    }


    @Test
    public void testInputCoverageTypesUriAbsent() throws Exception {
        Parameters input = generateInput();
        Coverage coverage = input.getParameter().get(1).getResource().as(Coverage.class);

        Coding.Builder builder = Coding.builder();
        builder.setValidating(false);
        builder = builder.code(Code.of("test"));

        coverage =
                coverage.toBuilder()
                    .clazz(Coverage.Class.builder()
                        .type(CodeableConcept.builder()
                                .coding(builder.build())
                                .text("English")
                                .build())
                        .value("Test")
                        .build())
                    .build();

        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("1-2-3-4");

        coverage.accept(compiler);
        assertEquals(compiler.getSearchParameters().size(), 3);
    }

    @Test
    public void testInputPatientEmpty() throws Exception {
        Patient.Builder builder = Patient.builder();
        builder.setValidating(false);
        Patient ptnt = builder.build();

        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        ptnt.accept(compiler);
        System.out.println(compiler.getSearchParameters());
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
        ptnt = b.communication(
                    c.language(
                            CodeableConcept.builder()
                                .coding(builder.build())
                                .text("English")
                                .build())
                        .preferred(Boolean.TRUE)
                        .build())
                .telecom(ContactPoint.builder().system(ContactPointSystem.PHONE).use(ContactPointUse.HOME).value("1-000-000-0000").build(), ContactPoint.builder().system(ContactPointSystem.PHONE).use(ContactPointUse.HOME).value("2-000-000-0000").build()).build();

        ptnt.accept(compiler);
        System.out.println(compiler.getSearchParameters());
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
        ptnt = b.communication(
                    c.language(
                            CodeableConcept.builder()
                                .coding(builder.build())
                                .text("English")
                                .build())
                        .preferred(Boolean.TRUE)
                        .build())
                .telecom(ContactPoint.builder().system(ContactPointSystem.PHONE).use(ContactPointUse.HOME).value("1-000-000-0000").build(), ContactPoint.builder().system(ContactPointSystem.PHONE).use(ContactPointUse.HOME).value("2-000-000-0000").build()).build();

        ptnt.accept(compiler);
        System.out.println(compiler.getSearchParameters());
    }

    /**
     * generates the input
     *
     * @return
     * @throws Exception
     */
    private Parameters generateInput() throws Exception {
        return HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");
    }

    private static class LocalResourceHelpers implements FHIRResourceHelpers {

        @Override
        public FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist, boolean doValidation) throws Exception {

            return null;
        }

        @Override
        public FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, boolean doValidation) throws Exception {

            return null;
        }

        @Override
        public FHIRRestOperationResponse doPatch(String type, String id, FHIRPatch patch, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate) throws Exception {

            return null;
        }

        @Override
        public FHIRRestOperationResponse doDelete(String type, String id, String searchQueryString) throws Exception {

            return null;
        }

        @Override
        public SingleResourceResult<? extends Resource> doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted, Resource contextResource,
            MultivaluedMap<String, String> queryParameters) throws Exception {

            return null;
        }

        @Override
        public Resource doVRead(String type, String id, String versionId, MultivaluedMap<String, String> queryParameters) throws Exception {

            return null;
        }

        @Override
        public Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception {

            return null;
        }

        @Override
        public Bundle doHistory(MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception {

            return null;
        }

        @Override
        public Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters, String requestUri,
            Resource contextResource) throws Exception {

            return null;
        }

        @Override
        public Resource doInvoke(FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId, String operationName,
            Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {

            return null;
        }

        @Override
        public Bundle doBundle(Bundle bundle, boolean skippableUpdates) throws Exception {

            return null;
        }

        @Override
        public FHIRPersistenceTransaction getTransaction() throws Exception {

            return null;
        }

        @Override
        public int doReindex(FHIROperationContext operationContext, Builder operationOutcomeResult, Instant tstamp, List<Long> indexIds,
            String resourceLogicalId) throws Exception {

            return 0;
        }

        @Override
        public List<Long> doRetrieveIndex(FHIROperationContext operationContext, String resourceTypeName, int count, Instant notModifiedAfter,
            Long afterIndexId) throws Exception {

            return null;
        }

    }
}
