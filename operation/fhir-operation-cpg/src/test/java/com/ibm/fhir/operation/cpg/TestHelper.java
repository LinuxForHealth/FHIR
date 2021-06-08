package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.engine.model.ModelUtil.fhircode;
import static com.ibm.fhir.cql.engine.model.ModelUtil.fhirstring;
import static com.ibm.fhir.cql.engine.model.ModelUtil.fhiruri;

import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Library.Builder;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.PublicationStatus;

public class TestHelper {

    public static Builder buildBasicLibrary(String id, String url, String name, String version) {
        Library.Builder builder =
                Library.builder().id(id).url(Uri.of(url)).name(fhirstring(name)).version(fhirstring(version)).status(PublicationStatus.ACTIVE);
        return builder;
    }

    public static Library getTestLibraryResource(String path) throws Exception {
        try (InputStream is = ClassLoader.getSystemResourceAsStream(path)) {
            return (Library) FHIRParser.parser(Format.JSON).parse(is);
        }
    }

    public static Resource getTestResource(String path) throws Exception {
        try (InputStream is = ClassLoader.getSystemResourceAsStream(path)) {
            return FHIRParser.parser(Format.JSON).parse(is);
        }
    }

    public static CodeableConcept getLogicLibraryConcept() {
        CodeableConcept valid =
                CodeableConcept.builder().coding(LibraryHelper.getLogicLibraryCoding().toBuilder().display(fhirstring("Logic Library")).build()).build();
        return valid;
    }

    public static Attachment attachment(String mimeType, String libraryResource) throws Exception {
        byte[] buffer = null;
        try (InputStream is = ClassLoader.getSystemResourceAsStream(libraryResource)) {
            buffer = IOUtils.toByteArray(is);
        }

        return extracted(mimeType, buffer);
    }

    public static Attachment extracted(String mimeType, byte[] buffer) {
        return Attachment.builder().contentType(fhircode(mimeType)).data(Base64Binary.of(buffer)).build();
    }
    
    public static ValueSet valueset(String codesystem, String code) {
        return ValueSet.builder().id(UUID.randomUUID().toString())
            .status(PublicationStatus.ACTIVE)
            .expansion(ValueSet.Expansion.builder()
                .timestamp(DateTime.now())
                .contains(ValueSet.Expansion.Contains.builder()
                    .system(fhiruri(codesystem))
                    .code(fhircode(code))
                    .build()
                    )
                .build()).build();
    }
    
    public static Bundle bundle(Resource... resources) {
        Bundle.Builder builder = Bundle.builder().type(BundleType.SEARCHSET);
        builder.total(UnsignedInt.of(resources.length));
        for( Resource resource : resources ) {
            builder.entry(Bundle.Entry.builder().resource(resource).build());
        }
        return builder.build();
    }
    
    public static Coding coding(String codesystem, String code) {
        return Coding.builder().system(fhiruri(codesystem)).code(fhircode(code)).build();
    }
    
    public static CodeableConcept concept(String codesystem, String code) {
        return concept( coding( codesystem, code ) );
    }
    
    public static CodeableConcept concept(Coding coding) {
        return CodeableConcept.builder().coding(coding).build();
    }
}
