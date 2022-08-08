/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.cql.helpers;

import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.fhircode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mockito.MockedStatic;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.type.Attachment;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.RelatedArtifact;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.RelatedArtifactType;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

public class LibraryHelperTest {
    
    private static Library TEMPLATE;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TEMPLATE = TestHelper.getTestLibraryResource("library-EXM104-8.2.000.json");
    }

    @Test
    public void testDeserializeElmAttachment() throws Exception {
        assertNotNull( LibraryHelper.loadLibrary(null, TEMPLATE) );
    }

    @Test
    public void testLoadLibrariesIgnoreNonLogic() throws Exception {
        try (MockedStatic<FHIRRegistry> staticRegistry = mockStatic(FHIRRegistry.class)) {
            FHIRRegistry mockRegistry = mock(FHIRRegistry.class);
            staticRegistry.when(FHIRRegistry::getInstance).thenReturn(mockRegistry);

            CodeableConcept valid = LibraryHelper.getLogicLibraryConcept();

            // valid type, valid content
            Library l1 =
                    TestHelper.buildBasicLibrary("1", "http://localhost/fhir/Library/first", "first", "1.0.0").type(valid).content(TEMPLATE.getContent()).build();
            when(mockRegistry.getResource(l1.getUrl().getValue(), Library.class)).thenReturn(l1);
            
            CodeableConcept irrelevant = getIrrelevantConcept();

            // irrelevant type, valid content
            Library l2 =
                    TestHelper.buildBasicLibrary("2", "http://localhost/fhir/Library/second", "second", "1.0.0").type(irrelevant).content(TEMPLATE.getContent()).build();
            when(mockRegistry.getResource(l2.getUrl().getValue(), Library.class)).thenReturn(l2);

            // all of the above plus a ValueSet
            Collection<RelatedArtifact> related =
                    Arrays.asList(l1.getUrl(), l2.getUrl(), Uri.of("http://localhost/fhir/ValueSet/1.2.3")).stream().map(url -> RelatedArtifact.builder().type(RelatedArtifactType.DEPENDS_ON).resource(Canonical.of(url.getValue())).build()).collect(Collectors.toList());

            // add in an irrelevant link type for completeness
            RelatedArtifact otherRelationType = RelatedArtifact.builder().type(RelatedArtifactType.CITATION).resource(Canonical.of("http://docs.org")).build();
            related.add(otherRelationType);

            Library parent =
                    TestHelper.buildBasicLibrary("parent", "http://localhost/fhir/Library/parent", "parent", "1.0.1").type(valid).relatedArtifact(related).content(TEMPLATE.getContent()).build();

            List<Library> actual = LibraryHelper.loadLibraries(parent);
            assertEquals(actual.size(), 2);
            
            CodeableConcept nullSystem =
                    CodeableConcept.builder().coding(Coding.builder().code(fhircode("ignore-me")).build()).build();
            Library l3 =
                    TestHelper.buildBasicLibrary("1", "http://localhost/fhir/Library/first", "first", "1.0.0").type(nullSystem).content(TEMPLATE.getContent()).build();
            when(mockRegistry.getResource(l1.getUrl().getValue(), Library.class)).thenReturn(l3);
        }
    }
    
    @Test
    public void testIsLogicLibraryIsTypeNullSafe() throws Exception {
        
        Library.Builder builder = TEMPLATE.toBuilder().type(null);
        builder.setValidating(false);
        assertTrue(LibraryHelper.isLogicLibrary(builder.build()));
    }
    
    @Test
    public void testIsLogicLibraryIsSystemNullSafe() throws Exception {        
        CodeableConcept nullSystem =
                CodeableConcept.builder().coding(Coding.builder().code(fhircode("ignore-me")).build()).build();
        
        Library actual = TEMPLATE.toBuilder().type(nullSystem).build();
        assertFalse(LibraryHelper.isLogicLibrary(actual));
    }
    
    @Test
    public void testIsLogicLibraryIsCodeNullSafe() throws Exception {        
        CodeableConcept nullCode =
                CodeableConcept.builder().coding(Coding.builder().system(Uri.of("http://nothing")).build()).build();
        
        Library actual = TEMPLATE.toBuilder().type(nullCode).build();
        assertFalse(LibraryHelper.isLogicLibrary(actual));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUnsupportedContent() {
        Library library =
                TestHelper.buildBasicLibrary("3", "http://localhost/fhir/Library/FHIR-Model", "FHIR-Model", "4.0.1")
                    .type(LibraryHelper.getLogicLibraryConcept())
                    .content(Attachment.builder().contentType(fhircode("text/xml")).build())
                    .build();

        LibraryHelper.loadLibrary(null, library);
    }

    private CodeableConcept getIrrelevantConcept() {
        CodeableConcept irrelevant =
                CodeableConcept.builder().coding(Coding.builder().system(Uri.of("http://nothing")).code(fhircode("ignore-me")).build()).build();
        return irrelevant;
    }
}
