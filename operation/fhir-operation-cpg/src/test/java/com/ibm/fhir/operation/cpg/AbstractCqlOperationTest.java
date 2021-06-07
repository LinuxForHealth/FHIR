package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.engine.model.ModelUtil.fhirstring;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
//import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.retrieve.RetrieveProvider;

import com.ibm.fhir.cql.engine.model.FhirModelResolver;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.code.RelatedArtifactType;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

public class AbstractCqlOperationTest {

    AbstractCqlOperation op;

    @Before
    public void setup() {
        op = new LibraryEvaluate();
    }

    @Test
    public void testGetCqlContext() {
        Parameters.Parameter p = Parameters.Parameter.builder().name(fhirstring("subject")).value(fhirstring("Patient/123")).build();
        Parameters parameters = Parameters.builder().parameter(p).build();

        Pair<String, Object> context = op.getCqlContext(op.indexParametersByName(parameters));
        assertEquals("Patient", context.getKey());
        assertEquals("123", context.getValue());
    }

    @Test
    public void testDataProviderResolveResourcePackage() {
        RetrieveProvider retrieveProvider = mock(RetrieveProvider.class);
        Map<String, DataProvider> dataProviders = op.createDataProviders(retrieveProvider);
        assertTrue(dataProviders.size() > 0);

        org.cqframework.cql.elm.execution.Library library = mock(org.cqframework.cql.elm.execution.Library.class);
        when(library.getIdentifier()).thenReturn(new VersionedIdentifier().withId("test"));

        Context context = new Context(library);
        for (Map.Entry<String, DataProvider> entry : dataProviders.entrySet()) {
            context.registerDataProvider(entry.getKey(), entry.getValue());
        }

        DataProvider resolved = context.resolveDataProvider(FhirModelResolver.BASE_PACKAGE_NAME + ".resource");
        assertNotNull(resolved);
    }

    @Test
    // @Ignore
    public void testDoEvaluation() throws Exception {

        /*
         * LocalDate birthDate = LocalDate.of(2001, Month.OCTOBER, 12);
         * Patient patient = Patient.builder()
         * .id("123")
         * .gender(AdministrativeGender.MALE)
         * .birthDate(Date.of(birthDate))
         * .extension(
         * Extension.builder().url("http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName").value(
         * fhirstring("Adah626 Cummerata161")).build() )
         * .build();
         */
        Patient patient = (Patient) TestHelper.getTestResource("Patient.json");

        Library fhirHelpersLibrary =
                TestHelper.buildBasicLibrary("FHIRHelpers", "http://ibm.com/fhir/Library/FHIRHelpers", "FHIRHelpers", "4.0.1").type(TestHelper.getLogicLibraryConcept()).content(TestHelper.attachment(Constants.MIME_TYPE_APPLICATION_ELM_XML, "FHIRHelpers-4.0.1.xml")).build();

        Library primaryLibrary =
                TestHelper.buildBasicLibrary("SupplementalDataElements", "http://ibm.com/fhir/Library/SupplementalDataElements-2.0.0", "SupplementalDataElements", "2.0.0").type(TestHelper.getLogicLibraryConcept()).relatedArtifact(RelatedArtifact.builder().type(RelatedArtifactType.DEPENDS_ON).resource(Canonical.of(fhirHelpersLibrary.getUrl().getValue())).build()).content(TestHelper.attachment(Constants.MIME_TYPE_APPLICATION_ELM_XML, "SupplementalDataElements-2.0.0.xml")).build();

        Parameters.Parameter pSubject = Parameters.Parameter.builder().name(fhirstring("subject")).value(fhirstring("Patient/" + patient.getId())).build();
        Parameters.Parameter pLibrary = Parameters.Parameter.builder().name(fhirstring("library")).value(primaryLibrary.getUrl()).build();
        Parameters parameters = Parameters.builder().parameter(pSubject, pLibrary).build();
        Map<String, Parameter> paramMap = op.indexParametersByName(parameters);

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        when(resourceHelper.doRead(eq("Patient"), anyString(), any(Boolean.class), any(Boolean.class), any())).thenAnswer(x -> asResult(patient));

        try (MockedStatic<FHIRRegistry> staticRegistry = mockStatic(FHIRRegistry.class)) {
            FHIRRegistry mockRegistry = spy(FHIRRegistry.class);
            staticRegistry.when(FHIRRegistry::getInstance).thenReturn(mockRegistry);

            when(mockRegistry.getResource(fhirHelpersLibrary.getUrl().getValue(), Library.class)).thenReturn(fhirHelpersLibrary);

            op.doEvaluation(resourceHelper, paramMap, primaryLibrary);
        }
    }

    @SuppressWarnings("unchecked")
    private SingleResourceResult<? extends Resource> asResult(Resource patient) {
        SingleResourceResult<Resource> result = (SingleResourceResult<Resource>) mock(SingleResourceResult.class);
        when(result.getResource()).thenReturn(patient);
        return result;
    }
}
