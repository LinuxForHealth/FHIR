/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.parameters;

import static com.ibm.watsonhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.type.SearchParamType;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.search.test.BaseSearchTest;

/**
 * 
 * Tests ParametersUtil
 * 
 * @author pbastide
 *
 */
public class ParametersUtilTest extends BaseSearchTest {

    @Test
    public void testGetBuildInSearchParameterMap() throws IOException {
        // Tests JSON
        Map<String, Map<String, SearchParameter>> params = ParametersUtil.getBuiltInSearchParameterMap();
        assertNotNull(params);
        // Intentionally the data is caputred in the bytearray output stream.
        try (ByteArrayOutputStream outBA = new ByteArrayOutputStream(); PrintStream out = new PrintStream(outBA, true);) {
            ParametersUtil.print(out);
            Assert.assertNotNull(outBA);
        }
        // 134 + DomainResource
        assertEquals(params.size(), 135);
    }

    @Test()
    public void testPopulateSearchParameterMapFromStreamXML() throws IOException {
        // Tests XML (once we support reading XML)
        try (InputStream stream = ParametersUtil.class.getClassLoader().getResourceAsStream("search-parameters.xml")) {
            Map<String, Map<String, SearchParameter>> params = ParametersUtil.populateSearchParameterMapFromStreamXML(stream);
            assertNotNull(params);
            if (DEBUG) {
                ParametersUtil.print(System.out);
            }
            // 134 + DomainResource
            assertEquals(params.size(), 135);
        }

    }

    @Test(expectedExceptions = {})
    public void testPopulateSearchParameterMapFromFile() throws IOException {
        File customSearchParams = new File("src/test/resources/config/tenant1/extension-search-parameters.json");
        if (DEBUG) {
            System.out.println(customSearchParams.getAbsolutePath());
        }
        Map<String, Map<String, SearchParameter>> params = ParametersUtil.populateSearchParameterMapFromFile(customSearchParams);
        if (DEBUG) {
            System.out.println(params.keySet());
        }

        // validates checks
        assertNotNull(params);
        assertFalse(params.isEmpty());
        assertEquals(params.size(), 2);

    }

    @Test
    public void testPrint() {
        // Test the output, OK, if it gets through.
        try (ByteArrayOutputStream outBA = new ByteArrayOutputStream(); PrintStream out = new PrintStream(outBA, true);) {
            ParametersUtil.print(out);
            assertNotNull(outBA);
            assertNotNull(outBA.toByteArray());
        } catch (Exception e) {
            fail();
        }

    }

    @Test(expectedExceptions = {})
    public void testGetBuiltInSearchParameterMapByResourceTypeInvalid() {
        // getBuiltInSearchParameterMapByResourceType
        Map<String, SearchParameter> result = ParametersUtil.getBuiltInSearchParameterMapByResourceType("Junk");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(expectedExceptions = {})
    public void testGetBuiltInSearchParameterMapByResourceTypeValid() {
        // getBuiltInSearchParameterMapByResourceType
        Map<String, SearchParameter> result = ParametersUtil.getBuiltInSearchParameterMapByResourceType("Observation");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test(expectedExceptions = { FileNotFoundException.class })
    public void testPopulateSearchParameterMapFromResourceNull() throws IOException {
        String invalidResourceName = "INVALID_RESOURCE";
        ParametersUtil.populateSearchParameterMapFromResource(invalidResourceName);
    }

    @Test
    public void testResourceDefaults() throws IOException {
        Map<String, SearchParameter> params1 = ParametersUtil.getBuiltInSearchParameterMapByResourceType("Observation");
        Map<String, SearchParameter> params2 = ParametersUtil.getBuiltInSearchParameterMapByResourceType("Resource");

        // Check that each returned "Resource" is included in the first set returned.
        assertNotNull(params1);
        assertNotNull(params2);
        assertEquals(params2.size(), 7);
        params2.keySet().stream().forEach(new Consumer<String>() {

            @Override
            public void accept(String resourceParam) {
                if (DEBUG) {
                    System.out.println("Checking Resource Param -> " + resourceParam);
                }
                assertTrue(params1.containsKey(resourceParam));
            }

        });
    }

    /*
     * Generate Search parameter
     */
    private SearchParameter generateSearchParameter(String expressions) {
        SearchParameter.Builder builder = SearchParameter.builder();
        builder.url(com.ibm.watsonhealth.fhir.model.type.Uri.uri("test"));
        builder.name(string("test"));
        builder.status(PublicationStatus.ACTIVE);
        builder.description(Markdown.builder().id("test").value("test").build());
        builder.code(Code.builder().id("test").value("test").build());
        builder.base(ResourceType.ACCOUNT);
        builder.type(SearchParamType.COMPOSITE);
        builder.expression(string(expressions));
        return builder.build();
    }

    @Test
    public void testRemoveUnsupportedExpressionsParameterResolves() {

        String expressions = "resolve() ";
        assertNotNull(ParametersUtil.removeUnsupportedExpressions(generateSearchParameter(expressions)));

        expressions = "resolve() | resolve() | resolve()";
        assertNotNull(ParametersUtil.removeUnsupportedExpressions(generateSearchParameter(expressions)));

        expressions = "resolve() | resolve() | resolve ()";
        assertNotNull(ParametersUtil.removeUnsupportedExpressions(generateSearchParameter(expressions)));

        // issue 206: Resolve is no longer removed
        // The checks are flipped and left for posterity as they should be true.
        expressions = ".where(resolve())";
        assertNotNull(ParametersUtil.removeUnsupportedExpressions(generateSearchParameter(expressions)));

        expressions = "CarePlan.subject.where(resolve() is Patient) ";
        assertEquals(ParametersUtil.processResolve(expressions), expressions);

        expressions =
                "AllergyIntolerance.patient | CarePlan.subject.where(resolve() is Patient) | CareTeam.subject.where(resolve() is Patient) | ClinicalImpression.subject.where(resolve() is Patient) | Composition.subject.where(resolve() is Patient) | Condition.subject.where(resolve() is Patient) | Consent.patient | DetectedIssue.patient | DeviceRequest.subject.where(resolve() is Patient) | DeviceUseStatement.subject | DiagnosticReport.subject.where(resolve() is Patient) | DocumentManifest.subject.where(resolve() is Patient) | DocumentReference.subject.where(resolve() is Patient) | Encounter.subject.where(resolve() is Patient) | EpisodeOfCare.patient | FamilyMemberHistory.patient | Flag.subject.where(resolve() is Patient) | Goal.subject.where(resolve() is Patient) | ImagingStudy.subject.where(resolve() is Patient) | Immunization.patient | List.subject.where(resolve() is Patient) | MedicationAdministration.subject.where(resolve() is Patient) | MedicationDispense.subject.where(resolve() is Patient) | MedicationRequest.subject.where(resolve() is Patient) | MedicationStatement.subject.where(resolve() is Patient) | NutritionOrder.patient | Observation.subject.where(resolve() is Patient) | Procedure.subject.where(resolve() is Patient) | RiskAssessment.subject.where(resolve() is Patient) | ServiceRequest.subject.where(resolve() is Patient) | SupplyDelivery.patient | VisionPrescription.patient";
        assertEquals(ParametersUtil.processResolve(expressions), expressions);

        expressions =
                "AllergyIntolerance.patient | CarePlan.subject.where(resolve() is Patient) | CareTeam.subject.where(resolve() is Patient) | ClinicalImpression.subject.where(resolve() is Patient) | Composition.subject.where(resolve() is Patient) | Condition.subject.where(resolve() is Patient) | Consent.patient | DetectedIssue.patient | DeviceRequest.subject.where(resolve() is Patient) | DeviceUseStatement.subject | DiagnosticReport.subject.where(resolve() is Patient) | DocumentManifest.subject.where(resolve() is Patient) | DocumentReference.subject.where(resolve() is Patient) | Encounter.subject.where(resolve() is Patient) | EpisodeOfCare.patient | FamilyMemberHistory.patient | Flag.subject.where(resolve() is Patient) | Goal.subject.where(resolve() is Patient) | ImagingStudy.subject.where(resolve() is Patient) | Immunization.patient | List.subject.where(resolve() is Patient) | MedicationAdministration.subject.where(resolve() is Patient) | MedicationDispense.subject.where(resolve() is Patient) | MedicationRequest.subject.where(resolve() is Patient) | MedicationStatement.subject.where(resolve() is Patient) | NutritionOrder.patient | Observation.subject.where(resolve() is Patient) | Procedure.subject.where(resolve() is Patient) | RiskAssessment.subject.where(resolve() is Patient) | ServiceRequest.subject.where(resolve() is Patient) | SupplyDelivery.patient | VisionPrescription.patient";
        String output = ParametersUtil.removeUnsupportedExpressions(generateSearchParameter(expressions)).getExpression().getValue();
        assertNotNull(output);
        if (DEBUG) {
            System.out.println(output);
        }
        assertTrue(output.contains("resolve()"));

    }

    @Test
    public void testProcessResolve() {
        // issue 206: Resolve is no longer removed
        String expressions = "Procedure.subject.where(resolve() is Patient)";
        assertEquals(ParametersUtil.processResolve(expressions), expressions);

    }

    @Test
    public void testPrintSearchParameter() throws IOException {
        // Intentionally the data is caputred in the bytearray output stream.
        try (ByteArrayOutputStream outBA = new ByteArrayOutputStream(); PrintStream out = new PrintStream(outBA, true);) {

            SearchParameter.Builder builder =
                    SearchParameter.builder().url(Uri.of("test")).name(string("test")).status(PublicationStatus.DRAFT).description(Markdown.of("test")).code(Code.of("test")).base(Arrays.asList(ResourceType.ACCOUNT)).type(SearchParamType.NUMBER);
            builder.expression(string("test"));
            ParametersUtil.printSearchParameter(builder.build(), out);
            assertNotNull(outBA.toByteArray());
            if (DEBUG) {
                System.out.println(outBA.toString("UTF-8"));
            }
        }

    }

    @Test
    public void testInit() {
        ParametersUtil.init();
        assertTrue(true);
    }

    @Test
    public void testCheckAndWarnForIssueWithCodeAndName() {
        // Issue 202 : added warning and corresponding test.
        ParametersUtil.checkAndWarnForIssueWithCodeAndName(null, null);
        ParametersUtil.checkAndWarnForIssueWithCodeAndName(null, "");
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("", null);
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("", "");
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("_code", "_code");

        // Run as individual unit test, you should see only one warning:
        // WARNING: The code and name of the search parameter does not match [_code] [_notcode]
        ParametersUtil.checkAndWarnForIssueWithCodeAndName("_code", "_notcode");

        assertTrue(true);
    }
    
    @Test
    public void testProcessContains() {
        String expression = "ValueSet.expansion.contains.code";
        String result = ParametersUtil.processContains(expression);
        assertEquals(result, "ValueSet.expansion.`contains`.code");
        
        expression = "ValueSet.expansion.contains";
        result = ParametersUtil.processContains(expression);
        assertEquals(result, "ValueSet.expansion.`contains`");
        
        expression = "ValueSet.expansion.contains()";
        result = ParametersUtil.processContains(expression);
        assertEquals(result, "ValueSet.expansion.contains()");
        
        expression = "ValueSet.expansion.contains().code";
        result = ParametersUtil.processContains(expression);
        assertEquals(result, "ValueSet.expansion.contains().code");
    }

}
