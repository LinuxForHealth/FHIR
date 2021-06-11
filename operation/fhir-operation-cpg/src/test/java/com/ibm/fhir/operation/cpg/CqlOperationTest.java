package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.helpers.ModelHelper.bundle;
import static com.ibm.fhir.cql.helpers.ModelHelper.coding;
import static com.ibm.fhir.cql.helpers.ModelHelper.concept;
import static com.ibm.fhir.cql.helpers.ModelHelper.fhirstring;
import static com.ibm.fhir.cql.helpers.ModelHelper.javastring;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.MockedStatic;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.code.EncounterStatus;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

public class CqlOperationTest extends BaseCqlOperationTest<CqlOperation> {
    
    @Override
    protected CqlOperation getOperation() {
        return new CqlOperation();
    }
    
    @Test
    public void testInlineExpression() throws Exception {
        Patient patient = (Patient) TestHelper.getTestResource("Patient.json");

        String codesystem = "http://snomed.ct/info";
        String encounterCode = "office-visit";
        Coding reason = coding(codesystem, encounterCode); 
        
        Encounter encounter = Encounter.builder()
                .reasonCode(concept(reason))
                .status(EncounterStatus.FINISHED)
                .clazz(reason)
                .period(Period.builder().start(DateTime.now()).end(DateTime.now()).build())
                .build();

        String expression = "[Encounter] e where e.status = 'finished'";
      
        Parameter pSubject = Parameter.builder().name(fhirstring("subject")).value(fhirstring("Patient/" + patient.getId())).build();
        Parameter pLibrary = Parameter.builder().name(fhirstring("expression")).value(fhirstring(expression)).build();
        Parameters parameters = Parameters.builder().parameter(pSubject, pLibrary).build();

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        when(resourceHelper.doRead(eq("Patient"), anyString(), anyBoolean(), anyBoolean(), any())).thenAnswer(x -> asResult(patient));
        when(resourceHelper.doSearch(eq("Encounter"), anyString(), anyString(), any(), anyString(), any())).thenReturn( bundle(encounter) );

        //Library fhirHelpers = TestHelper.getTestLibraryResource("FHIRHelpers-4.0.1.json");
        
        try (MockedStatic<FHIRRegistry> staticRegistry = mockStatic(FHIRRegistry.class)) {
            FHIRRegistry mockRegistry = spy(FHIRRegistry.class);
            staticRegistry.when(FHIRRegistry::getInstance).thenReturn(mockRegistry);
 
            //when(mockRegistry.getResource(javastring(fhirHelpers.getUrl()), Library.class)).thenReturn(fhirHelpers);
            
            FHIROperationContext ctx = FHIROperationContext.createSystemOperationContext();
            getOperation().doInvoke(ctx, null, null, null, parameters, resourceHelper);
        }
    }
    
    @Test
    public void testInlineExpressionWithIncludes() throws Exception {
        Patient patient = (Patient) TestHelper.getTestResource("Patient.json");

        String codesystem = "http://snomed.ct/info";
        String encounterCode = "office-visit";
        Coding reason = coding(codesystem, encounterCode); 
        
        Encounter encounter = Encounter.builder()
                .reasonCode(concept(reason))
                .status(EncounterStatus.FINISHED)
                .clazz(reason)
                .period(Period.builder().start(DateTime.now()).end(DateTime.now()).build())
                .build();

        Library fhirHelpers = TestHelper.getTestLibraryResource("FHIRHelpers-4.0.1.json");
        
        String expression = "[Encounter] e where e.status = 'finished'";
      
        Parameter pSubject = Parameter.builder().name(fhirstring("subject")).value(fhirstring("Patient/" + patient.getId())).build();
        Parameter pExpression = Parameter.builder().name(fhirstring("expression")).value(fhirstring(expression)).build();
        Parameter pIncludeFHIRHelpers = Parameter.builder().name(fhirstring("url")).value(Canonical.of(LibraryHelper.canonicalUrl(fhirHelpers))).build();
        Parameter pLibrary = Parameter.builder().name(fhirstring("library")).part(pIncludeFHIRHelpers).build();
        Parameters parameters = Parameters.builder().parameter(pSubject, pExpression, pLibrary).build();

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        when(resourceHelper.doRead(eq("Patient"), anyString(), anyBoolean(), anyBoolean(), any())).thenAnswer(x -> asResult(patient));
        when(resourceHelper.doSearch(eq("Encounter"), anyString(), anyString(), any(), anyString(), any())).thenReturn( bundle(encounter) );

        try (MockedStatic<FHIRRegistry> staticRegistry = mockStatic(FHIRRegistry.class)) {
            FHIRRegistry mockRegistry = spy(FHIRRegistry.class);
            staticRegistry.when(FHIRRegistry::getInstance).thenReturn(mockRegistry);
 
            when(mockRegistry.getResource(javastring(fhirHelpers.getUrl()), Library.class)).thenReturn(fhirHelpers);
            
            FHIROperationContext ctx = FHIROperationContext.createSystemOperationContext();
            getOperation().doInvoke(ctx, null, null, null, parameters, resourceHelper);
        }
    }
    
    @Test(expected=FHIROperationException.class)
    public void testInlineExpressionInvalid() throws Exception {
        Patient patient = (Patient) TestHelper.getTestResource("Patient.json");

        String expression = "[NotAResource]";
      
        Parameter pSubject = Parameter.builder().name(fhirstring("subject")).value(fhirstring("Patient/" + patient.getId())).build();
        Parameter pLibrary = Parameter.builder().name(fhirstring("expression")).value(fhirstring(expression)).build();
        Parameters parameters = Parameters.builder().parameter(pSubject, pLibrary).build();

        FHIRResourceHelpers resourceHelper = mock(FHIRResourceHelpers.class);
        when(resourceHelper.doRead(eq("Patient"), anyString(), anyBoolean(), anyBoolean(), any())).thenAnswer(x -> asResult(patient));
        
        try (MockedStatic<FHIRRegistry> staticRegistry = mockStatic(FHIRRegistry.class)) {
            FHIRRegistry mockRegistry = spy(FHIRRegistry.class);
            staticRegistry.when(FHIRRegistry::getInstance).thenReturn(mockRegistry);
             
            FHIROperationContext ctx = FHIROperationContext.createSystemOperationContext();
            getOperation().doInvoke(ctx, null, null, null, parameters, resourceHelper);
        }
    }
}
