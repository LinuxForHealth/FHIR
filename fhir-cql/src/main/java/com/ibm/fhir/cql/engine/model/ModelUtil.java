package com.ibm.fhir.cql.engine.model;

import org.opencds.cqf.cql.engine.runtime.Code;

public class ModelUtil {

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
}
