package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.engine.model.ModelUtil.fhirstring;
import static com.ibm.fhir.cql.engine.model.ModelUtil.fhiruri;
import static com.ibm.fhir.operation.cpg.TestHelper.concept;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.execution.Context;
import org.opencds.cqf.cql.engine.execution.InMemoryLibraryLoader;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;
import org.opencds.cqf.cql.engine.retrieve.RetrieveProvider;
import org.opencds.cqf.cql.engine.runtime.Date;
import org.opencds.cqf.cql.engine.runtime.Interval;

import com.ibm.fhir.cql.engine.model.FhirModelResolver;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.code.EncounterStatus;
import com.ibm.fhir.model.type.code.ProcedureStatus;
import com.ibm.fhir.model.type.code.RelatedArtifactType;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

public class AbstractCqlOperationTest {

    AbstractCqlOperation op;

    java.util.Date start;
    java.util.Date end;
    
    @Before
    public void setup() {
        op = new LibraryEvaluate();
        
        Calendar c = Calendar.getInstance();
        c.set(2000, 03, 04, 05, 06, 07);
        end = c.getTime();
        c.add(Calendar.YEAR, -1);
        start = c.getTime();
    }

    public Library useBundledLibraries(String bundleResource) throws Exception {
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(bundleResource)) {
            final Bundle bundle = (Bundle) FHIRParser.parser(Format.JSON).parse(is);

            final List<Library> fhirLibraries =
                    bundle.getEntry().stream().map(e -> e.getResource()).filter(r -> r instanceof Library).map(r -> (Library) r).collect(Collectors.toList());

            op = new LibraryEvaluate() {

                @Override
                protected LibraryLoader createLibraryLoader(Library primaryLibrary) {
                    List<org.cqframework.cql.elm.execution.Library> libraries =
                            fhirLibraries.stream().map(l -> LibraryHelper.loadLibrary(l)).filter(Objects::nonNull).collect(Collectors.toList());
                    return new InMemoryLibraryLoader(libraries);
                }
                
                @Override
                protected Map<String,Object> getCqlParameters(ParameterConverter converter, Map<String,Parameter> paramMap) {
                    Map<String,Object> parameters = new HashMap<>();
                    parameters.put("Measurement Period", new Interval(Date.fromJavaDate(start), true, Date.fromJavaDate(end), true));
                    return parameters;
                }
            };
            
            return fhirLibraries.get(0);
        }
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
    public void testDoEvaluation() throws Exception {
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
        when(resourceHelper.doRead(eq("Patient"), anyString(), anyBoolean(), anyBoolean(), any())).thenAnswer(x -> asResult(patient));

        try (MockedStatic<FHIRRegistry> staticRegistry = mockStatic(FHIRRegistry.class)) {
            FHIRRegistry mockRegistry = spy(FHIRRegistry.class);
            staticRegistry.when(FHIRRegistry::getInstance).thenReturn(mockRegistry);

            when(mockRegistry.getResource(fhirHelpersLibrary.getUrl().getValue(), Library.class)).thenReturn(fhirHelpersLibrary);

            op.doEvaluation(resourceHelper, paramMap, primaryLibrary);
        }
    }

    @Test
    public void testDoEvaluationEXM74() throws Exception {
        Patient patient = (Patient) TestHelper.getTestResource("Patient.json");

        String codesystem = "http://snomed.ct/info";
        String encounterCode = "office-visit";
        Coding reason = TestHelper.coding(codesystem, encounterCode); 
        Encounter encounter = Encounter.builder()
                .reasonCode(concept(reason))
                .status(EncounterStatus.FINISHED)
                .clazz(reason)
                .period(Period.builder().start(DateTime.now()).end(DateTime.now()).build())
                .build();
        
        String procedureCode = "fluoride-application";
        Coding type = TestHelper.coding(codesystem, procedureCode);
        Procedure procedure = Procedure.builder().subject(Reference.builder().reference(fhirstring("Patient/"
                + patient.getId())).build()).code(concept(type)).status(ProcedureStatus.COMPLETED).performed(DateTime.of("2019-03-14")).build();
        
        Library primaryLibrary = useBundledLibraries("EXM74-10.2.000-request.json");
        assertNotNull(primaryLibrary);

        Parameters.Parameter pSubject = Parameters.Parameter.builder().name(fhirstring("subject")).value(fhirstring("Patient/" + patient.getId())).build();
        Parameters.Parameter pLibrary =
                Parameters.Parameter.builder().name(fhirstring("library")).value(fhiruri("http://ibm.com/health/Library/EXM74")).build();
        Parameters parameters = Parameters.builder().parameter(pSubject, pLibrary).build();
        Map<String, Parameter> paramMap = op.indexParametersByName(parameters);

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        when(resourceHelper.doRead(eq("Patient"), anyString(), anyBoolean(), anyBoolean(), any())).thenAnswer(x -> asResult(patient));
      
        when(resourceHelper.doSearch(eq("Encounter"), anyString(), anyString(), any(), anyString(), any())).thenReturn( TestHelper.bundle(encounter) );
        when(resourceHelper.doSearch(eq("Procedure"), anyString(), anyString(), any(), anyString(), any())).thenReturn( TestHelper.bundle(procedure) );

        try (MockedStatic<FHIRRegistry> staticRegistry = mockStatic(FHIRRegistry.class)) {
            FHIRRegistry mockRegistry = spy(FHIRRegistry.class);
            staticRegistry.when(FHIRRegistry::getInstance).thenReturn(mockRegistry);

            
            when(mockRegistry.getResource("http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.101.12.1001", ValueSet.class)).thenReturn( TestHelper.valueset(codesystem, encounterCode) );
            when(mockRegistry.getResource("http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.464.1003.125.12.1002", ValueSet.class)).thenReturn( TestHelper.valueset(codesystem, procedureCode) );
            
            op.doEvaluation(resourceHelper, paramMap, primaryLibrary);
        }
    }

    @SuppressWarnings("unchecked")
    private SingleResourceResult<? extends Resource> asResult(Resource patient) {
        SingleResourceResult<Resource> result = (SingleResourceResult<Resource>) mock(SingleResourceResult.class);
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(patient);
        return result;
    }
}
