/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cpg;

import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.fhircode;
import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.fhirstring;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.resource.Library.Builder;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Attachment;
import org.linuxforhealth.fhir.model.type.Base64Binary;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.AdministrativeGender;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;

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
    
    public static <T> List<T> getBundleResources(String bundlePath, Class<T> clazz) throws Exception {
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(bundlePath)) {
            final Bundle bundle = (Bundle) FHIRParser.parser(Format.JSON).parse(is);

            return getBundleResources( bundle, clazz );
        }
    }

    public static <T> List<T> getBundleResources(Bundle bundle, Class<T> clazz) {
        return bundle.getEntry().stream().map(e -> e.getResource()).filter(r -> clazz.isInstance(r)).map(r -> clazz.cast(r) ).collect(Collectors.toList());
    }    

    public static Attachment attachment(String mimeType, String resource) throws Exception {
        byte[] buffer = null;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            buffer = IOUtils.toByteArray(is);
        }

        return attachment(mimeType, buffer);
    }

    public static Attachment attachment(String mimeType, byte[] buffer) {
        return Attachment.builder().contentType(fhircode(mimeType)).data(Base64Binary.of(buffer)).build();
    }
    
    public static Patient.Builder john_doe() {
        return Patient.builder().id("123")
                .gender(AdministrativeGender.MALE)
                .name(HumanName.builder().id("human-name").text(fhirstring("John Doe")).build())
                .birthDate(Date.of("1969-02-15"));
    }
}
