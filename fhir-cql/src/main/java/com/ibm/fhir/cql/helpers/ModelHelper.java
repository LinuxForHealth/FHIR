package com.ibm.fhir.cql.helpers;

import java.util.Optional;
import java.util.UUID;

import org.opencds.cqf.cql.engine.runtime.Code;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.RelatedArtifactType;

public class ModelHelper {

    public static com.ibm.fhir.model.type.Code fhircode(Code code) {
        return fhircode(code.getCode());
    }

    public static com.ibm.fhir.model.type.Code fhircode(String code) {
        com.ibm.fhir.model.type.Code fhirCode = com.ibm.fhir.model.type.Code.builder().value(code).build();
        return fhirCode;
    }

    public static com.ibm.fhir.model.type.String fhirstring(String str) {
        if (str != null) {
            return com.ibm.fhir.model.type.String.of(str);
        } else {
            return null;
        }
    }

    public static com.ibm.fhir.model.type.Uri fhiruri(String uri) {
        if (uri != null) {
            return com.ibm.fhir.model.type.Uri.of(uri);
        } else {
            return null;
        }
    }

    public static com.ibm.fhir.model.type.Boolean fhirboolean(boolean bool) {
        return com.ibm.fhir.model.type.Boolean.of(bool);
    }

    public static String javastring(com.ibm.fhir.model.type.String str) {
        if (str != null) {
            return str.getValue();
        } else {
            return null;
        }
    }

    public static String javastring(com.ibm.fhir.model.type.Uri uri) {
        if (uri != null) {
            return uri.getValue();
        } else {
            return null;
        }
    }

    public static Boolean javaboolean(com.ibm.fhir.model.type.Boolean bool) {
        if (bool != null) {
            return bool.getValue();
        } else {
            return null;
        }
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
    
    public static RelatedArtifact relatedArtifact(RelatedArtifactType type, com.ibm.fhir.model.type.Uri uri, com.ibm.fhir.model.type.String version) {
        return RelatedArtifact.builder().type(type).resource( canonical(uri, version) ).build();
    }

    public static Canonical canonical(com.ibm.fhir.model.type.Uri uri, com.ibm.fhir.model.type.String version) {
        StringBuilder url = new StringBuilder(uri.getValue());
        if( version != null ) {
            url.append("|");
            url.append(version.getValue());
        }
        return Canonical.of(url.toString());
    }
    
    public static Optional<Attachment> getAttachmentByType(Library library, String contentType) {
        Optional<Attachment> result = library.getContent().stream().filter(a -> a.getContentType().getValue().equals(contentType)).reduce((a, b) -> {
            throw new IllegalArgumentException(String.format("Found more than one attachment with the content type %s in library %s", contentType, library.getId()));
        });
        return result;
    }
    
    public static Bundle bundle(Resource... resources) {
        return bundle(BundleType.SEARCHSET, resources);
    }
    
    public static Bundle bundle(BundleType type, Resource... resources) {
        Bundle.Builder builder = Bundle.builder().type(type);
        builder.total(UnsignedInt.of(resources.length));
        for( Resource resource : resources ) {
            builder.entry(Bundle.Entry.builder().resource(resource).build());
        }
        return builder.build();
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
    
    public static Optional<String> getLinkByType(Bundle bundle, String type) {
        return bundle.getLink().stream().filter(l -> l.getRelation().getValue().equals(type)).map(l -> l.getUrl().getValue()).reduce((a, b) -> {
            throw new IllegalStateException(String.format("Multiple '%s' links found", type));
        });
    }
}
