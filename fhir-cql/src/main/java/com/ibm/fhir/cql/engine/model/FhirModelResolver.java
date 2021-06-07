package com.ibm.fhir.cql.engine.model;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.opencds.cqf.cql.engine.exception.InvalidCast;
import org.opencds.cqf.cql.engine.model.ModelResolver;

import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Count;
import com.ibm.fhir.model.type.Distance;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.MoneyQuantity;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.Uuid;

public class FhirModelResolver implements ModelResolver {

    private static final Logger log = Logger.getLogger(FhirModelResolver.class.getName());

    public static final String BASE_PACKAGE_NAME = "com.ibm.fhir.model";

    public static final String[] ALL_PACKAGES = new String[] {
            BASE_PACKAGE_NAME + ".resource",
            BASE_PACKAGE_NAME + ".type",
            BASE_PACKAGE_NAME + ".type.code" };

    private String packageName = BASE_PACKAGE_NAME;

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Object resolvePath(Object target, String path) {
        // TODO - use FHIRPath to resolve 
        Object result = null;

        try {
            if (target != null) {
                result = PropertyUtils.getProperty(target, path);
            }
        } catch (Exception ex) {
            // do nothing
        }

        return result;
    }

    public Object getContextPath(String contextType, String targetType) {
        Object result = null;
        if (targetType != null && contextType != null) {

            if (contextType != null && !(contextType.equals("Unspecified") || contextType.equals("Population"))) {
                if (targetType != null && contextType.equals(targetType)) {
                    result = "id";
                } else {
                    Class<?> clazz = resolveType(targetType);

                    if (clazz != null) {
                        Set<String> possibleAnswers = new HashSet<String>();
                        getReferenceTargets(clazz, contextType, "", possibleAnswers);

                        if (possibleAnswers.isEmpty()) {
                            throw new IllegalArgumentException(String.format("Unable to determine context path value for context '%s' in type '%s'", contextType, targetType));
                        } else if (possibleAnswers.size() == 1) {
                            result = possibleAnswers.iterator().next();
                        } else {
                            if (contextType.equals("Patient")) {
                                String[] bestGuesses = new String[] { "subject", "patient", "beneficiary" };
                                for (String guess : bestGuesses) {
                                    if (possibleAnswers.contains(guess)) {
                                        result = guess;
                                        break;
                                    }
                                }
                            }
                            if (result == null) {
                                throw new IllegalArgumentException(String.format("ContextPath '%s' is ambiguous for type '%s'. Possible answers are '%s'.", contextType, targetType, possibleAnswers.toString()));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private void getReferenceTargets(Class<?> clazz, String contextType, String prefix, Set<String> possibleAnswers) {
        while (clazz != null && clazz.getPackage().getName().startsWith(this.packageName)) {

            getReferenceTargetFromFields(clazz, contextType, prefix, possibleAnswers);

            for (Class<?> subclass : clazz.getDeclaredClasses()) {
                if (!subclass.getName().endsWith("$Builder")) {
                    String newPrefix = getPrefixed(prefix, StringUtils.uncapitalize(subclass.getSimpleName()));
                    getReferenceTargets(subclass, contextType, newPrefix, possibleAnswers);
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    private void getReferenceTargetFromFields(Class<?> clazz, String contextType, String prefix, Set<String> possibleAnswers) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(Reference.class)) {

                ReferenceTarget refTarget = field.getAnnotation(ReferenceTarget.class);
                if (refTarget != null) {
                    for (String target : refTarget.value()) {
                        if (target.equals(contextType)) {
                            possibleAnswers.add(getPrefixed(prefix, field.getName()));
                            break;
                        }
                    }
                } else {
                    // This handles the "Any" reference type
                    possibleAnswers.add(getPrefixed(prefix, field.getName()));
                }
            }
        }
    }

    private String getPrefixed(String prefix, String suffix) {
        if (prefix != null && prefix.length() > 0) {
            return prefix + "." + suffix;
        } else {
            return suffix;
        }
    }

    public Class<?> resolveType(String typeName) {

        if (typeName.contains(".")) {
            typeName = typeName.replaceAll("\\.", "\\$");
        }

        switch (typeName) {
        // typo in the 4.0.1 modelinfo
        case "NutritiionOrderIntent":
            typeName = "NutritionOrderIntent";
            break;

        // custom stuff carried over from OSS and mapped to IBM FHIR
        case "ConfidentialityClassification":
            typeName = "DocumentConfidentiality";
            break;
        case "ContractResourceStatusCodes":
            typeName = "ContractStatus";
            break;
        case "EventStatus":
            typeName = "ProcedureStatus";
            break;
        case "FinancialResourceStatusCodes":
            typeName = "ClaimResponseStatus";
            break;
        case "SampledDataDataType":
            typeName = "String";
            break;
        case "ClaimProcessingCodes":
            typeName = "RemittanceOutcome";
            break;
        case "vConfidentialityClassification":
            typeName = "DocumentConfidentiality";
            break;
        case "ContractResourcePublicationStatusCodes":
            typeName = "ContractPublicationStatus";
            break;

        // stuff for 4.0.0 modelinfo
        case "MedicationStatusCodes":
            typeName = "MedicationStatus";
            break;
        case "ImmunizationEvaluationStatusCodes":
            typeName = "ImmunizationEvaluationStatus";
            break;
        case "ImmunizationStatusCodes":
            typeName = "ImmunizationStatus";
            break;
        case "ExpressionLanguage":
            typeName = "Expression";
            break;
        case "RequestResourceType":
            typeName = "Code";
            break;

        // These were reported as bugs in HAPI 4.2 the OSS impl
        case "CurrencyCode":
            typeName = "Code";
            break;
        case "MimeType":
            typeName = "Code";
            break;
        case "Messageheader_Response_Request":
        case "messageheaderResponseRequest":
            typeName = "Code";
            break;
        }

        Class<?> result = null;

        // TODO - These are types. Are they the only ones that will be in the .type package?
        // We could optimize the search path if we know what to expect.
        if (Character.isLowerCase(typeName.charAt(0))) {
            typeName = StringUtils.capitalize(typeName);
        }

        for (String prefix : ALL_PACKAGES) {
            try {
                result = Class.forName(prefix + "." + typeName);
                break;
            } catch (ClassNotFoundException cnfe) {
                // do nothing
            }
        }

        if (result == null) {
            if (log.isLoggable(Level.WARNING)) {
                log.warning("Failed to resolve type " + typeName);
            }
        } /*
           * else if( Element.class.isAssignableFrom(result) ) {
           * Method m;
           * try {
           * m = result.getMethod("getValue");
           * result = m.getReturnType();
           * } catch (Exception e) {
           * //throw new RuntimeException(String.
           * format("While processing %s, failed to retrieve getValue method from primitive FHIR type %s", typeName,
           * result.getName()));
           * // Some things, such as Address, are "FHIR primitives" but don't have a getValue method
           * }
           * }
           */

        return result;
    }

    public Class<?> resolveType(Object value) {
        Class<?> result = null;

        if (value != null) {
            result = resolveType(value.getClass().getSimpleName());
        } else {
            result = Object.class;
        }

        return result;
    }

    public Boolean is(Object value, Class<?> type) {
        Boolean result = null;
        if (value != null) {
            result = type.isAssignableFrom(value.getClass());
        }

        return result;
    }

    public Object as(Object value, Class<?> type, boolean isStrict) {
        Object result = null;
        if (value != null) {
            if (type.isAssignableFrom(value.getClass())) {
                result = value;
            } else {
                if (value instanceof Uri) {
                    Uri uri = (Uri) value;
                    switch (type.getSimpleName()) {
                    case "Url":
                        result = Url.of(uri.getValue());
                        break;
                    case "Canonical":
                        result = Canonical.of(uri.getValue());
                        break;
                    case "Uuid":
                        result = Uuid.of(uri.getValue());
                        break;
                    case "Oid":
                        result = Oid.of(uri.getValue());
                    }
                } else if (value instanceof com.ibm.fhir.model.type.Integer) {
                    com.ibm.fhir.model.type.Integer integer = (com.ibm.fhir.model.type.Integer) value;
                    switch (type.getSimpleName()) {
                    case "PositiveInt":
                        result = PositiveInt.of(integer.getValue());
                        break;
                    case "UnsignedInt":
                        result = UnsignedInt.of(integer.getValue());
                        break;
                    }
                } else if (value instanceof com.ibm.fhir.model.type.String) {
                    com.ibm.fhir.model.type.String string = (com.ibm.fhir.model.type.String) value;
                    switch (type.getSimpleName()) {
                    case "Code":
                        result = Code.of(string.getValue());
                        break;
                    case "Markdown":
                        result = Markdown.of(string.getValue());
                        break;
                    case "Id":
                        result = Id.of(string.getValue());
                        break;
                    }
                } else if (value instanceof Quantity) {
                    Quantity quantity = (Quantity) value;
                    Quantity.Builder builder = null;
                    switch (type.getSimpleName()) {
                    case "Age":
                        builder = Age.builder();
                        break;
                    case "Distance":
                        builder = Distance.builder();
                        break;
                    case "Duration":
                        builder = Duration.builder();
                        break;
                    case "Count":
                        builder = Count.builder();
                        break;
                    case "SimpleQuantity":
                        builder = SimpleQuantity.builder();
                        break;
                    case "MoneyQuantity":
                        builder = MoneyQuantity.builder();
                        break;
                    }
                    if (builder != null) {
                        // TODO - what about units? Should we also set those. The original code doesn't
                        result = builder.code(quantity.getCode()).value(quantity.getValue()).build();
                    }
                }
            }
        }

        if (result == null && isStrict) {
            throw new InvalidCast(String.format("Cannot cast a value of type %s as %s.", value.getClass().getName(), type.getName()));
        }

        return result;
    }

    public Object createInstance(String typeName) {
        throw new UnsupportedOperationException("IBM FHIR model types must be constructed with a value or children");
        // Object result = null;
        //
        // Class<?> clazz = resolveType(typeName);
        // if (clazz != null) {
        // // IBM FHIR model objects are constructed using nested builder classes
        // // They require a value and it isn't clear how we would provide that given
        // // the inputs provided.
        // try {
        // Method builderMethod = clazz.getMethod("builder");
        // if( builderMethod != null ) {
        // Builder<?> builder = (Builder<?>) builderMethod.invoke(null);
        // builder.id( UUID.randomUUID().toString() );
        // result = builder.build();
        // }
        // } catch (Exception iaex) {
        // throw new RuntimeException(iaex);
        // }
        // }
        //
        // return result;
    }

    public void setValue(Object target, String path, Object value) {
        // TODO - consider implementing a pattern where new objects are constructed
        // each time this method is called. The models have the ability
        // to convert themselves to a builder using a toBuilder() function
        // which can them be appended to as needed.
        throw new UnsupportedOperationException("IBM FHIR model types are immutable");
    }

    public Boolean objectEqual(Object left, Object right) {
        Boolean result = null;
        if (left != null && right != null) {
            result = left.equals(right);
        }
        return result;
    }

    public Boolean objectEquivalent(Object left, Object right) {
        Boolean result = null;

        if (left == null && right == null) {
            result = true;
        } else if (left == null) {
            result = false;
        } else {
            result = objectEqual(left, right);
        }

        return result;
    }

}
