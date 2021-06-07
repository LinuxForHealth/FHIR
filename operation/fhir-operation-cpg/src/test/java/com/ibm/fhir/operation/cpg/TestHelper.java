package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.engine.model.ModelUtil.fhircode;
import static com.ibm.fhir.cql.engine.model.ModelUtil.fhirstring;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Library.Builder;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Uri;
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
}
