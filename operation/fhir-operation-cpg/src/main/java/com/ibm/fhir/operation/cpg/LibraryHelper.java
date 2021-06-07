package com.ibm.fhir.operation.cpg;

import static com.ibm.fhir.cql.engine.model.ModelUtil.fhircode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.opencds.cqf.cql.engine.execution.CqlLibraryReader;

import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.RelatedArtifactType;
import com.ibm.fhir.registry.FHIRRegistry;

public class LibraryHelper {

    public static List<org.cqframework.cql.elm.execution.Library> loadLibraries(Library fhirLibrary) {
        Set<String> visited = new HashSet<>();
        return loadLibraries(fhirLibrary, visited);
    }

    public static List<org.cqframework.cql.elm.execution.Library> loadLibraries(Library fhirLibrary, Set<String> visited) {
        List<org.cqframework.cql.elm.execution.Library> libraries = new ArrayList<>();
        libraries.add(loadLibrary(fhirLibrary));

        if (fhirLibrary.getRelatedArtifact() != null) {
            for (RelatedArtifact related : fhirLibrary.getRelatedArtifact()) {
                if (related.getType().equals(RelatedArtifactType.DEPENDS_ON)) {
                    Canonical canonical = related.getResource();
                    String canonicalURL = canonical.getValue();
                    if (!visited.contains(canonicalURL)) {
                        visited.add(canonicalURL);
                        if (isLibraryReference(canonicalURL)) {
                            Library dependency = FHIRRegistry.getInstance().getResource(canonical.getValue(), Library.class);
                            if (dependency != null) {
                                if (isLogicLibrary(dependency)) {
                                    libraries.addAll(loadLibraries(dependency, visited));
                                }
                            } else {
                                throw new IllegalArgumentException("Failed to load dependency " + canonicalURL);
                            }
                        }
                    }
                }
            }
        }

        return libraries;
    }

    public static org.cqframework.cql.elm.execution.Library loadLibrary(Library fhirLibrary) {
        org.cqframework.cql.elm.execution.Library result = null;

        if (fhirLibrary.getContent() != null) {
            Map<String, Attachment> mimeTypeIndex = new HashMap<>();
            for (Attachment attachment : fhirLibrary.getContent()) {
                if (attachment.getContentType() != null) {
                    mimeTypeIndex.put(attachment.getContentType().getValue(), attachment);
                } else {
                    throw new IllegalArgumentException(String.format("Library %s contains an attachment with no content type", fhirLibrary.getId()));
                }
            }

            Attachment elm = mimeTypeIndex.get(Constants.MIME_TYPE_APPLICATION_ELM_XML);
            if (elm != null) {
                try {
                    result = CqlLibraryReader.read(getAttachmentData(elm));
                } catch (Exception ex) {
                    throw new IllegalArgumentException(String.format("Library %s elm attachment failed to to deserialize", fhirLibrary.getId()), ex);
                }
            } else {
                Attachment cql = mimeTypeIndex.get(Constants.MIME_TYPE_TEXT_CQL);
                if (cql != null) {
                    // result = translationProvider.translate(getAttachmentData(cql));
                    throw new UnsupportedOperationException("CQL to ELM translation is not supported at this time");
                } else {
                    throw new IllegalArgumentException(String.format("Library %s must contain either an application/elm+xml or text/cql attachment", fhirLibrary.getId()));
                }
            }
        }

        return result;
    }

    public static boolean isLibraryReference(String resource) {
        return resource.startsWith("Library/") || !resource.contains("/") || resource.contains("/Library/");
    }

    public static boolean isLogicLibrary(Library fhirLibrary) {
        boolean result = false;

        if (fhirLibrary.getType() == null) {
            if (fhirLibrary.getContent() != null) {
                result = fhirLibrary.getContent().stream().filter(a -> a.getContentType() != null &&
                        (a.getContentType().equals(fhircode("application/elm+xml")) ||
                                a.getContentType().equals(fhircode("text/cql")))).collect(Collectors.counting()) > 0;
            }
        } else if (fhirLibrary.getType().getCoding() != null) {
            Coding needed = getLogicLibraryCoding();
            // can't use equals because we want to ignore display name for this purpose
            result = fhirLibrary.getType().getCoding().stream().filter(c -> c.getSystem().equals(needed.getSystem())
                    && c.getCode().equals(needed.getCode())).collect(Collectors.counting()) > 0;
        }
        return result;
    }

    public static Coding getLogicLibraryCoding() {
        Coding needed = Coding.builder().system(Uri.of("http://terminology.hl7.org/CodeSystem/library-type")).code(fhircode("logic-library")).build();
        return needed;
    }

    public static InputStream getAttachmentData(Attachment attachment) throws IOException {
        return new ByteArrayInputStream(attachment.getData().getValue());
    }
}
