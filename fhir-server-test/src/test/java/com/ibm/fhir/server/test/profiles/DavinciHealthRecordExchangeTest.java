/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.ig.davinci.hrex.test.tool.HREXExamplesUtil;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.Coverage;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests using Davinci Health Record Exchange (HREX) profile.
 */
public class DavinciHealthRecordExchangeTest extends ProfilesTestBaseV2 {

    public static final String EXPRESSION_OPERATION = "rest.resource.operation.name";

    private String coverageId = null;
    private String multipleMatchPatientId = null;
    private String singleMatchPatientId = null;
    private String patientId = null;

    @BeforeClass
    public void checkOperationExistsOnServer() throws Exception {
        CapabilityStatement conf = retrieveConformanceStatement();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(conf);
        // All the possible required operations
        Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, EXPRESSION_OPERATION);
        Collection<String> listOfOperations = tmpResults.stream().map(x -> x.getValue().asStringValue().string()).collect(Collectors.toList());

        if (!listOfOperations.contains("member-match")) {
            throw new SkipException("member match not found");
        }
    }

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-consent|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-organization|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-out|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-patient-demographics|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-patient-member|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-practitioner|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-practitionerrole|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-provenance|0.2.0", "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-task-data-request|0.2.0");
    }

    @Override
    public void loadResources() throws Exception {
        loadCoverage();
        loadMultiplePatients();
        loadSingleMatchPatientAndCoverage();
    }

    public void loadCoverage() throws Exception {
        String resource = "Coverage-full.json";
        Resource coverage = HREXExamplesUtil.readLocalJSONResource("020", resource);
        coverageId = createResourceAndReturnTheLogicalId("Coverage", coverage);
    }

    public void loadMultiplePatients() throws Exception {
        Parameters source = HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");
        Patient patient = source.getParameter().get(0).getResource().as(Patient.class);

        Identifier.Builder idBuilder = patient.getIdentifier().get(0).toBuilder();
        multipleMatchPatientId = UUID.randomUUID().toString();
        idBuilder.value(multipleMatchPatientId);

        patient = patient.toBuilder().identifier(Arrays.asList(idBuilder.build())).build();

        createResourceAndReturnTheLogicalId("Patient", patient);
        createResourceAndReturnTheLogicalId("Patient", patient);
    }

    public void loadSingleMatchPatientAndCoverage() throws Exception {
        singleMatchPatientId = UUID.randomUUID().toString();
        Parameters source = HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");
        Patient patient = source.getParameter().get(0).getResource().as(Patient.class);

        Identifier.Builder idBuilder = patient.getIdentifier().get(0).toBuilder();
        idBuilder.value(singleMatchPatientId);

        patient = patient.toBuilder().identifier(Arrays.asList(idBuilder.build())).build();

        patientId = createResourceAndReturnTheLogicalId("Patient", patient);

        createResourceAndReturnTheLogicalId("Coverage",
                source.getParameter().get(1).getResource().as(Coverage.class)
                    .toBuilder()
                    .beneficiary(Reference.builder()
                        .reference("Patient/" + patientId)
                        .build())
                    .build());
    }

    @Test
    public void testCoverageBySubscriberId() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("subscriber-id", "97531");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search("Coverage", parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, coverageId);
    }

    @Test
    public void testCoverageWithBadSubscriberId() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("subscriber-id", UUID.randomUUID() + "97531");
        FHIRResponse response = client.search("Coverage", parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 0);
    }

    @Test
    public void testMemberMatch_Unsupported_GET() throws Exception {
        runMemberMatchStrategy(new BadRequestMemberMatchTestInteraction() {

            @Override
            public MemberMatchTestInteractionType method() {
                return MemberMatchTestInteractionType.GET;
            }

            @Override
            public void verify(Response r) {
                OperationOutcome oo = r.readEntity(OperationOutcome.class);
                assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(), "HTTP method 'GET' is not supported for operation: 'member-match'");
            }
        });
    }

    @Test
    public void testMemberMatch_Unsupported_Path() throws Exception {
        runMemberMatchStrategy(new BadRequestMemberMatchTestInteraction() {

            @Override
            public String path() {
                return "Coverage/$member-match";
            }

            @Override
            public void verify(Response r) {
                OperationOutcome oo = r.readEntity(OperationOutcome.class);
                assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(), "Operation with code: 'member-match:Coverage' was not found");
            }
        });
    }

    @Test
    public void testMemberMatch_DisabledForTenant() throws Exception {
        runMemberMatchStrategy(new BadRequestMemberMatchTestInteraction() {

            @Override
            public String tenant() {
                return "tenant1";
            }

            @Override
            public Parameters getInputParameters() {
                try {
                    return HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("Unexpected");
                }
            }

            @Override
            public void verify(Response r) {
                OperationOutcome oo = r.readEntity(OperationOutcome.class);
                assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(), "$member-match is not supported");
            }
        });
    }

    @Test
    public void testMemberMatch_NoInput() throws Exception {
        runMemberMatchStrategy(new BadRequestMemberMatchTestInteraction() {

            @Override
            public Parameters getInputParameters() {
                try {
                    Parameters.Builder builder = Parameters.builder();
                    builder.setValidating(false);
                    return builder.build();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("Unexpected");
                }
            }

            @Override
            public void verify(Response r) {
                OperationOutcome oo = r.readEntity(OperationOutcome.class);
                assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(), "Missing required input parameter: 'MemberPatient'");
            }
        });
    }

    @Test
    public void testMemberMatch_WithBadProfile() throws Exception {
        runMemberMatchStrategy(new BadRequestMemberMatchTestInteraction() {

            @Override
            public Parameters getInputParameters() {
                try {
                    Parameters source = HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");

                    List<Parameters.Parameter> params = new ArrayList<>();
                    Patient patient = source.getParameter().get(0).getResource().as(Patient.class);
                    Parameters.Parameter.Builder patientParameterBuilder = Parameters.Parameter.builder();
                    patientParameterBuilder =
                            patientParameterBuilder.name("MemberPatient").resource(patient.toBuilder().identifier(Collections.emptyList()).build());
                    Parameters.Parameter patientParameter = patientParameterBuilder.build();
                    params.add(patientParameter);
                    params.add(source.getParameter().get(1));

                    Parameters.Builder builder = Parameters.builder().parameter(params);
                    return builder.build();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("Unexpected");
                }
            }

            @Override
            public void verify(Response r) {
                OperationOutcome oo = r.readEntity(OperationOutcome.class);
                assertTrue(oo.getIssue().get(0).getDetails().getText().getValue().startsWith("Failed to validate against the input"));
            }
        });
    }

    @Test
    public void testMemberMatch_WithoutCoverageToMatch() throws Exception {
        runMemberMatchStrategy(new BadRequestMemberMatchTestInteraction() {

            @Override
            public Parameters getInputParameters() {
                try {
                    Parameters source = HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");
                    Parameters.Builder builder = Parameters.builder().parameter(Arrays.asList(source.getParameter().get(0)));
                    return builder.build();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("Unexpected");
                }
            }

            @Override
            public void verify(Response r) {
                OperationOutcome oo = r.readEntity(OperationOutcome.class);
                assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(), "Missing required input parameter: 'CoverageToMatch'");
            }
        });
    }

    @Test
    public void testMemberMatch_WithBadProfileCoverage() throws Exception {
        runMemberMatchStrategy(new BadRequestMemberMatchTestInteraction() {

            @Override
            public Parameters getInputParameters() {
                try {
                    Parameters source = HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");

                    List<Parameters.Parameter> params = new ArrayList<>();
                    Coverage coverage = source.getParameter().get(1).getResource().as(Coverage.class);
                    Parameters.Parameter.Builder coverageParameterBuilder = Parameters.Parameter.builder();

                    Coverage.Class.Builder b = Coverage.Class.builder();
                    b.setValidating(false);
                    Coverage.Class clz = b.value("test").build();

                    coverageParameterBuilder = coverageParameterBuilder.name("CoverageToLink").resource(coverage.toBuilder().clazz(Arrays.asList(clz)).build());
                    Parameters.Parameter coverageParameter = coverageParameterBuilder.build();
                    params.add(source.getParameter().get(0));
                    params.add(coverageParameter);

                    Parameters.Builder builder = Parameters.builder().parameter(params);
                    return builder.build();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("Unexpected");
                }
            }

            @Override
            public void verify(Response r) {
                OperationOutcome oo = r.readEntity(OperationOutcome.class);
                assertTrue(oo.getIssue().get(0).getDetails().getText().getValue().startsWith("FHIRProvider: Missing required element"));
            }
        });
    }

    @Test
    public void testMemberMatch_WithNoMatch() throws Exception {
        runMemberMatchStrategy(new MatchProblemMemberMatchTestInteraction() {

            @Override
            public Parameters getInputParameters() {
                try {
                    Parameters source = HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");

                    List<Parameters.Parameter> params = new ArrayList<>();
                    Patient patient = source.getParameter().get(0).getResource().as(Patient.class);

                    Identifier.Builder idBuilder = patient.getIdentifier().get(0).toBuilder();
                    idBuilder.value(UUID.randomUUID().toString());

                    Parameters.Parameter.Builder patientParameterBuilder = Parameters.Parameter.builder();
                    patientParameterBuilder =
                            patientParameterBuilder.name("MemberPatient").resource(patient.toBuilder().identifier(Arrays.asList(idBuilder.build())).build());
                    Parameters.Parameter patientParameter = patientParameterBuilder.build();
                    params.add(patientParameter);
                    params.add(source.getParameter().get(1));

                    return Parameters.builder().parameter(params).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("Unexpected");
                }
            }

            @Override
            public void verify(Response r) {
                OperationOutcome oo = r.readEntity(OperationOutcome.class);
                assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(), "FHIROperationException: No match found for $member-match");
            }
        });
    }

    @Test
    public void testMemberMatch_WithMultipleMatch() throws Exception {
        runMemberMatchStrategy(new MatchProblemMemberMatchTestInteraction() {

            @Override
            public Parameters getInputParameters() {
                try {
                    Parameters source = HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");

                    List<Parameters.Parameter> params = new ArrayList<>();
                    Patient patient = source.getParameter().get(0).getResource().as(Patient.class);

                    Identifier.Builder idBuilder = patient.getIdentifier().get(0).toBuilder();
                    idBuilder.value(multipleMatchPatientId);

                    Parameters.Parameter.Builder patientParameterBuilder = Parameters.Parameter.builder();
                    patientParameterBuilder =
                            patientParameterBuilder.name("MemberPatient").resource(patient.toBuilder().identifier(Arrays.asList(idBuilder.build())).build());
                    Parameters.Parameter patientParameter = patientParameterBuilder.build();
                    params.add(patientParameter);
                    params.add(source.getParameter().get(1));

                    return Parameters.builder().parameter(params).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("Unexpected");
                }
            }

            @Override
            public void verify(Response r) {
                OperationOutcome oo = r.readEntity(OperationOutcome.class);
                assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(), "FHIROperationException: Multiple matches found for $member-match");
            }
        });
    }

    @Test
    public void testMemberMatch_WithSingleMatch() throws Exception {
        runMemberMatchStrategy(new BaseMemberMatchTestInteraction() {

            @Override
            public Parameters getInputParameters() {
                try {
                    Parameters source = HREXExamplesUtil.readLocalJSONResource("020", "Parameters-member-match-in.json");

                    List<Parameters.Parameter> params = new ArrayList<>();
                    Patient patient = source.getParameter().get(0).getResource().as(Patient.class);

                    Identifier.Builder idBuilder = patient.getIdentifier().get(0).toBuilder();
                    idBuilder = idBuilder.value(singleMatchPatientId);

                    Parameters.Parameter.Builder patientParameterBuilder = Parameters.Parameter.builder();
                    patientParameterBuilder =
                            patientParameterBuilder.name("MemberPatient").resource(patient.toBuilder().identifier(Arrays.asList(idBuilder.build())).build());
                    Parameters.Parameter patientParameter = patientParameterBuilder.build();
                    params.add(patientParameter);


                    Coverage coverage = source.getParameter().get(1).getResource().as(Coverage.class);

                    Parameters.Parameter.Builder coverageParameterBuilder = Parameters.Parameter.builder();
                    coverageParameterBuilder =
                            coverageParameterBuilder.name("CoverageToMatch")
                                .resource(coverage.toBuilder()
                                    .beneficiary(Reference.builder()
                                        .reference("Patient/" + patientId)
                                        .build())
                                    .build());
                    Parameters.Parameter coverageParameter = coverageParameterBuilder.build();
                    params.add(coverageParameter);

                    return Parameters.builder().parameter(params).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError("Unexpected");
                }
            }

            @Override
            public void verify(Response r) {
                Parameters output = r.readEntity(Parameters.class);
                assertEquals(output.getParameter().size(), 1);
                assertEquals(output.getParameter().get(0).getName().getValue(), "MemberIdentifier");
            }
        });
    }

    /*
     * Supporting Code follows:
     */
    public void runMemberMatchStrategy(MemberMatchTestInteraction interaction) {
        Entity<Parameters> entity = Entity.entity(interaction.getInputParameters(), FHIRMediaType.APPLICATION_FHIR_JSON);

        Builder builder =
                getWebTarget().path(interaction.path()).request(FHIRMediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", interaction.tenant()).header("X-FHIR-DSID", interaction.datastore());

        Response r;
        switch (interaction.method()) {
        case POST:
            r = builder.post(entity, Response.class);
            break;
        default: // GET
            r = builder.get(Response.class);
            break;
        }

        assertEquals(r.getStatus(), interaction.expectedStatusCode());
        interaction.verify(r);
    }

    /**
     * Defines if GET or POST
     */
    public static enum MemberMatchTestInteractionType {
        GET,
        POST
    }

    /**
     * Interaction details during the test, and the appropriate validation post test.
     */
    public static interface MemberMatchTestInteraction {

        default String path() {
            return "/Patient/$member-match";
        }

        default MemberMatchTestInteractionType method() {
            return MemberMatchTestInteractionType.POST;
        }

        default String tenant() {
            return "default";
        }

        default String datastore() {
            return "default";
        }

        default int expectedStatusCode() {
            return 200;
        }

        Parameters getInputParameters();

        void verify(Response r);
    }

    /**
     * Base Member Match Test
     */
    public static class BaseMemberMatchTestInteraction implements MemberMatchTestInteraction {

        @Override
        public Parameters getInputParameters() {
            Parameters.Builder builder = Parameters.builder();
            return builder.build();
        }

        @Override
        public void verify(Response r) {
            // NOP
        }
    }

    /**
     * Bad Request
     */
    public static class BadRequestMemberMatchTestInteraction implements MemberMatchTestInteraction {

        @Override
        public int expectedStatusCode() {
            return 400;
        }

        @Override
        public Parameters getInputParameters() {
            Parameters.Builder builder = Parameters.builder();
            return builder.build();
        }

        @Override
        public void verify(Response r) {
            // NOP
        }
    }

    /**
     * NO_MATCH and MANY_MATCH Error Case
     */
    public static class MatchProblemMemberMatchTestInteraction extends BadRequestMemberMatchTestInteraction {

        @Override
        public int expectedStatusCode() {
            return 422;
        }
    }
}