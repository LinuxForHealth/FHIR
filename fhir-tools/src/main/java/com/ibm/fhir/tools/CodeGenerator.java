/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.tools;

import static com.ibm.fhir.tools.CodeBuilder._new;
import static com.ibm.fhir.tools.CodeBuilder._this;
import static com.ibm.fhir.tools.CodeBuilder.args;
import static com.ibm.fhir.tools.CodeBuilder.implementsInterfaces;
import static com.ibm.fhir.tools.CodeBuilder.javadocLink;
import static com.ibm.fhir.tools.CodeBuilder.mods;
import static com.ibm.fhir.tools.CodeBuilder.param;
import static com.ibm.fhir.tools.CodeBuilder.params;
import static com.ibm.fhir.tools.CodeBuilder.quote;
import static com.ibm.fhir.tools.CodeBuilder.throwsExceptions;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.lang.model.SourceVersion;

public class CodeGenerator {
    private final Map<String, JsonObject> structureDefinitionMap;
    private final Map<String, JsonObject> codeSystemMap;
    private final Map<String, JsonObject> valueSetMap;
    private final List<String> generatedClassNames = new ArrayList<>();
    private final List<String> typeClassNames = new ArrayList<>();
    private final List<String> resourceClassNames = new ArrayList<>();
    private final List<String> primitiveTypeClassNames = new ArrayList<>();
    private final List<String> codeSubtypeClassNames = new ArrayList<>();
    private final List<String> quantitySubtypeClassNames = new ArrayList<>();
    private final List<String> stringSubtypeClassNames = new ArrayList<>();
    private final List<String> integerSubtypeClassNames = new ArrayList<>();
    private final List<String> uriSubtypeClassNames = new ArrayList<>();
    private final Map<String, String> superClassMap = new HashMap<>();
    private final Map<String, JsonObject> modifierExtensionDefinitionMap = new HashMap<>();
    private static final List<String> DATA_TYPE_NAMES = Arrays.asList(
        "Base64Binary",
        "Boolean",
        "Canonical",
        "Code",
        "Date",
        "DateTime",
        "Decimal",
        "Id",
        "Instant",
        "Integer",
        "Markdown",
        "Oid",
        "PositiveInt",
        "String",
        "Time",
        "UnsignedInt",
        "Uri",
        "Url",
        "Uuid",
        "Address",
        "Age",
        "Annotation",
        "Attachment",
        "CodeableConcept",
        "Coding",
        "ContactPoint",
        "Count",
        "Distance",
        "Duration",
        "HumanName",
        "Identifier",
        "Money",
        "MoneyQuantity", // profiled type
        "Period",
        "Quantity",
        "Range",
        "Ratio",
        "Reference",
        "SampledData",
        "SimpleQuantity", // profiled type
        "Signature",
        "Timing",
        "ContactDetail",
        "Contributor",
        "DataRequirement",
        "Expression",
        "ParameterDefinition",
        "RelatedArtifact",
        "TriggerDefinition",
        "UsageContext",
        "Dosage");
    private static final List<String> PROFILED_TYPES = Arrays.asList("SimpleQuantity", "MoneyQuantity");
    private static final List<String> MODEL_CHECKED_CONSTRAINTS = Arrays.asList("ele-1", "sqty-1");
    private static final List<String> HEADER = readHeader();

    private static final String URI_PATTERN = "\\S*";
    private static final String STRING_PATTERN = "[ \\r\\n\\t\\S]+";

    public CodeGenerator(Map<String, JsonObject> structureDefinitionMap, Map<String, JsonObject> codeSystemMap, Map<String, JsonObject> valueSetMap) {
        this.structureDefinitionMap = structureDefinitionMap;
        this.codeSystemMap = codeSystemMap;
        this.valueSetMap = valueSetMap;
    }

    private boolean isProfiledType(String className) {
        return PROFILED_TYPES.contains(className);
    }

    private static List<String> readHeader() {
        try {
            String baseDir = ".";
            if (System.getProperty("BaseDir") != null) {
                baseDir = System.getProperty("BaseDir");
            }
            return Files.readAllLines(new File(baseDir + "/codegen-support/header.txt").toPath());
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    private JsonObject buildModifierExtensionDefinition(String path) {
        return Json.createObjectBuilder()
                .add("id", path + ".modifierExtension")
                .add("path", path + ".modifierExtension")
                .add("short", "Extensions that cannot be ignored even if unrecognized")
                .add("definition", "May be used to represent additional information that is not part of the basic definition of the element and that modifies the understanding of the element in which it is contained and/or the understanding of the containing element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.\\n\\nModifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of modifierExtension itself).")
                .add("min", 0)
                .add("max", "*")
                .add("base", Json.createObjectBuilder()
                    .add("path", "BackboneElement.modifierExtension")
                    .add("min", 0)
                    .add("max", "*"))
                .add("type", Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                        .add("code", "Extension")))
                .build();
    }

    private String buildParseMethodInvocation(JsonObject elementDefinition, String elementName, String fieldType) {
        String elementIndex = "-1";
        if (isRepeating(elementDefinition)) {
            elementIndex = elementName + "ElementIndex++";
        }
        if (isPrimitiveType(fieldType) && !isPrimitiveSubtype(fieldType)) {
            return "parse" + fieldType + "(" + quote(elementName) + ", reader, " + elementIndex +")";
        } else if (isStringSubtype(fieldType) || isCodeSubtype(fieldType)) {
            return "(" + fieldType + ") " + "parseString(" + fieldType + ".builder(), "+ quote(elementName) + ", reader, " + elementIndex + ")";
        } else if (isUriSubtype(fieldType)) {
            return "(" + fieldType + ") " + "parseUri(" + fieldType + ".builder(), "+ quote(elementName) + ", reader, " + elementIndex + ")";
        } else if (isIntegerSubtype(fieldType)) {
            return "(" + fieldType + ") " + "parseInteger(" + fieldType + ".builder(), "+ quote(elementName) + ", reader, " + elementIndex + ")";
        } else if (isQuantitySubtype(fieldType)) {
            return "(" + fieldType + ") parseQuantity(" + fieldType + ".builder(), " + quote(elementName) + ", reader, " + elementIndex + ")";
        } else if (isJavaString(fieldType)) {
            return "parseJavaString(" + quote(elementName) + ", reader, " + elementIndex + ")";
        } else {
            return "parse" + fieldType.replace(".", "") + "(" + quote(elementName) + ", reader, " + elementIndex + ")";
        }
    }

    private String buildParseMethodInvocation(JsonObject elementDefinition, String elementName, String fieldType, boolean repeating) {
        if (repeating) {
            if (isPrimitiveType(fieldType) && !isPrimitiveSubtype(fieldType)) {
                return "parse" + fieldType + "(" + quote(elementName) + ", " + elementName + "Array.get(i), getJsonValue(_" + elementName + "Array, i), i)";
            } else if (isStringSubtype(fieldType) || isCodeSubtype(fieldType)) {
                return "(" + fieldType + ") parseString(" + fieldType + ".builder(), "+ quote(elementName) + ", " + elementName + "Array.get(i), getJsonValue(_" + elementName + "Array, i), i)";
            } else if (isUriSubtype(fieldType)) {
                return "(" + fieldType + ") parseUri(" + fieldType + ".builder(), "+ quote(elementName) + ", " + elementName + "Array.get(i), getJsonValue(_" + elementName + "Array, i), i)";
            } else if (isIntegerSubtype(fieldType)) {
                return "(" + fieldType + ") parseInteger(" + fieldType + ".builder(), "+ quote(elementName) + ", " + elementName + "Array.get(i), getJsonValue(_" + elementName + "Array, i), i)";
            } else if (isQuantitySubtype(fieldType)) {
                return "(" + fieldType + ") parseQuantity(" + fieldType + ".builder(), " + quote(elementName) + ", " + elementName + "Array.getJsonObject(i), i)";
            } else {
                return "parse" + fieldType.replace(".", "") + "(" + quote(elementName) + ", " + elementName + "Array.getJsonObject(i), i)";
            }
        } else {
            if (isPrimitiveType(fieldType) && !isPrimitiveSubtype(fieldType)) {
                String expectedType = getExpectedType(fieldType);
                return "parse" + fieldType + "(" + quote(elementName) + ", getJsonValue(jsonObject, " + quote(elementName) + ", " + expectedType + ".class), jsonObject.get(" + quote("_" + elementName) + "), -1)";
            } else if (isStringSubtype(fieldType) || isCodeSubtype(fieldType)) {
                String expectedType = getExpectedType(fieldType);
                return "(" + fieldType + ") " + "parseString(" + fieldType + ".builder(), "+ quote(elementName) + ", getJsonValue(jsonObject, " + quote(elementName) + ", " + expectedType + ".class), jsonObject.get(" + quote("_" + elementName) + "), -1)";
            } else if (isUriSubtype(fieldType)) {
                String expectedType = getExpectedType(fieldType);
                return "(" + fieldType + ") " + "parseUri(" + fieldType + ".builder(), "+ quote(elementName) + ", getJsonValue(jsonObject, " + quote(elementName) + ", " + expectedType + ".class), jsonObject.get(" + quote("_" + elementName) + "), -1)";
            } else if (isIntegerSubtype(fieldType)) {
                String expectedType = getExpectedType(fieldType);
                return "(" + fieldType + ") " + "parseInteger(" + fieldType + ".builder(), "+ quote(elementName) + ", getJsonValue(jsonObject, " + quote(elementName) + ", " + expectedType + ".class), jsonObject.get(" + quote("_" + elementName) + "), -1)";
            } else if (isQuantitySubtype(fieldType)) {
                return "(" + fieldType + ") parseQuantity(" + fieldType + ".builder(), " + quote(elementName) + ", getJsonValue(jsonObject, " + quote(elementName) + ", JsonObject.class), -1)";
            } else if (isChoiceElement(elementDefinition)) {
                String choiceTypeClasses = getChoiceTypeNames(elementDefinition).stream().map(s -> s + ".class").collect(Collectors.joining(", "));
                return "parseChoiceElement(" + quote(elementName) + ", jsonObject, " + choiceTypeClasses + ")";
            } else if (isJavaString(fieldType)) {
                return "parseJavaString(" + quote(elementName) + ", getJsonValue(jsonObject, " + quote(elementName) + ", JsonString.class), -1)";
            } else {
                return "parse" + fieldType.replace(".", "") + "(" + quote(elementName) + ", getJsonValue(jsonObject, " + quote(elementName) + ", JsonObject.class), -1)";
            }
        }
    }

    private List<String> getChoiceTypeNames(JsonObject elementDefinition) {
        List<String> choiceTypeNames = new ArrayList<>();
        List<JsonObject> types = getTypes(elementDefinition);
        for (JsonObject type : types) {
            String choiceTypeName = titleCase(type.getString("code"));
            if (type.containsKey("profile")) {
                String profile = type.getJsonArray("profile").getString(0);
                choiceTypeName = profile.substring(profile.lastIndexOf("/") + 1);
            }
            choiceTypeNames.add(choiceTypeName);
        }
        return choiceTypeNames;
    }

    private String camelCase(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private boolean containsBackboneElement(JsonObject structureDefinition, String backboneElement) {
        List<String> paths = getBackboneElementPaths(structureDefinition);
        for (String path : paths) {
            if (path.endsWith(backboneElement)) {
                return true;
            }
        }
        return false;
    }

    public void generate(String basePath) {
        for (String key : structureDefinitionMap.keySet()) {
            JsonObject structureDefinition = structureDefinitionMap.get(key);
            generateClass(structureDefinition, basePath);
            generateCodeSubtypeClasses(structureDefinition, basePath);
        }
        generateVisitorInterface(basePath);
        generateDefaultVisitorClass(basePath);
        generateJsonParser(basePath);
        generateXMLParser(basePath);
        generateModelClassesFile(basePath);
        generateCodeSubtypeClass("ConceptSubsumptionOutcome", "http://hl7.org/fhir/ValueSet/concept-subsumption-outcome", basePath);
    }

    private void generateModelClassesFile(String basePath) {
        // Work around the pseudo hardcoding
        String baseDir = ".";
        if (System.getProperty("TargetBaseDir") != null) {
            baseDir = System.getProperty("TargetBaseDir");
        }

        List<String> qualifiedClassNames = new ArrayList<>();
        for (String generatedClassName : generatedClassNames) {
            String packageName;
            if (generatedClassName.contains(".")) {
                // nested class
                String enclosingClassName = generatedClassName.substring(0, generatedClassName.indexOf("."));
                packageName = typeClassNames.contains(enclosingClassName) ?
                        "com.ibm.fhir.model.type" :
                        "com.ibm.fhir.model.resource";
            } else {
                packageName = typeClassNames.contains(generatedClassName) ?
                        "com.ibm.fhir.model.type" :
                        "com.ibm.fhir.model.resource";
            }
            String className = generatedClassName.replace(".", "$");
            String qualifiedClassName = packageName + "." + className;
            qualifiedClassNames.add(qualifiedClassName);
        }

        Collections.sort(qualifiedClassNames);

        try {
            File file = new File(baseDir + "/src/main/resources/modelClasses");
            Files.write(file.toPath(), qualifiedClassNames);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void generateDefaultVisitorClass(String basePath) {
        CodeBuilder cb = new CodeBuilder();

        String packageName = "com.ibm.fhir.model.visitor";
        cb.lines(HEADER).newLine();
        cb._package(packageName).newLine();

        cb._import("java.math.BigDecimal");
        cb._import("java.time.LocalDate");
        cb._import("java.time.LocalTime");
        cb._import("java.time.Year");
        cb._import("java.time.YearMonth");
        cb._import("java.time.ZonedDateTime");
        cb._import("javax.annotation.Generated");
        cb.newLine();

        cb._import("com.ibm.fhir.model.resource.*");
        cb._import("com.ibm.fhir.model.type.*");
        cb._import("com.ibm.fhir.model.type.Boolean");
        cb._import("com.ibm.fhir.model.type.Integer");
        cb._import("com.ibm.fhir.model.type.String");
        cb.newLine();

        cb.javadocStart();
        cb.javadoc("DefaultVisitor provides a default implementation of the Visitor interface which uses the");
        cb.javadoc("value of the passed {@code visitChildren} boolean to control whether or not to");
        cb.javadoc("visit the children of the Resource or Element being visited.");
        cb.javadoc("");
        cb.javadoc("Subclasses can override the default behavior in a number of places, including:");
        cb.javadoc("<ul>",false);
        cb.javadoc("<li>preVisit methods to control whether a given Resource or Element gets visited",false);
        cb.javadoc("<li>visitStart methods to provide setup behavior prior to the visit",false);
        cb.javadoc("<li>supertype visit methods to perform some common action on all visited Resources and Elements",false);
        cb.javadoc("<li>subtype visit methods to perform unique behavior that varies by the type being visited",false);
        cb.javadoc("<li>visitEnd methods to provide initial cleanup behavior after a Resource or Element has been visited",false);
        cb.javadoc("<li>postVisit methods to provide final cleanup behavior after a Resource or Element has been visited",false);
        cb.javadoc("</ul>",false);
        cb.javadocEnd();
        cb.annotation("Generated", quote("com.ibm.fhir.tools.CodeGenerator"));
        cb._class(mods("public"), "DefaultVisitor", null, implementsInterfaces("Visitor"));

        cb.decl(mods("protected"), "boolean", "visitChildren");
        cb.newLine();

        cb.javadocStart();
        cb.javadoc("Subclasses can override this method to provide a default action for all visit methods.");
        cb.javadocReturn("whether to visit the children of this resource; returns the value of the {@code visitChildren} boolean by default");
        cb.javadocEnd();
        cb.method(mods("public"), "boolean", "visit", params("java.lang.String elementName", "int elementIndex", "Visitable visitable"));
        cb._return("visitChildren");
        cb.end();
        cb.newLine();

        cb.javadocStart();
        cb.javadocParam("visitChildren", "Whether to visit children of a Resource or Element by default. " +
                        "Note that subclasses may override the visit methods and/or the defaultAction methods " +
                        "and decide whether to use the passed boolean or not.");
        cb.javadocEnd();
        cb.constructor(mods("public"), "DefaultVisitor", params("boolean visitChildren"));
        cb.assign(_this("visitChildren"), "visitChildren");
        cb.end().newLine();

        cb.override();
        cb.method(mods("public"), "boolean", "preVisit", params("Element element"))._return("true").end().newLine();

        cb.override();
        cb.method(mods("public"), "boolean", "preVisit", params("Resource resource"))._return("true").end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "postVisit", params("Element element")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "postVisit", params("Resource resource")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitStart", params("java.lang.String elementName", "int elementIndex", "Element element")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitStart", params("java.lang.String elementName", "int elementIndex", "Resource resource")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitStart", params("java.lang.String elementName", "java.util.List<? extends Visitable> visitables", "Class<?> type")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitEnd", params("java.lang.String elementName", "int elementIndex", "Element element")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitEnd", params("java.lang.String elementName", "int elementIndex", "Resource resource")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitEnd", params("java.lang.String elementName", "java.util.List<? extends Visitable> visitables", "Class<?> type")).end().newLine();

        Collections.sort(generatedClassNames);

        for (String className : generatedClassNames) {
            if (isNestedType(className)) {
                continue;
            }
            String paramName = camelCase(className).replace(".", "");
            if (SourceVersion.isKeyword(paramName)) {
                paramName = "_" + paramName;
            }

            String supertype = superClassMap.get(className);
            if (supertype == null) {
                supertype = "Visitable";
            }

            cb.javadocStart();
            cb.javadoc("Delegates to {@link #visit(elementName, elementIndex, " + supertype + ")}");
            cb.javadocReturn("{@inheritDoc}");
            cb.javadocEnd();
            cb.override();
            cb.method(mods("public"), "boolean", "visit", params("java.lang.String elementName", "int elementIndex", className + " " + paramName));
            cb._return("visit(elementName, elementIndex, (" + supertype + ") " + paramName + ")");
            cb.end().newLine();
        }

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "byte[] value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "BigDecimal value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "java.lang.Boolean value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "java.lang.Integer value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "LocalDate value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "LocalTime value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "java.lang.String value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "Year value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "YearMonth value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "ZonedDateTime value")).end().newLine();

        cb.override();
        cb.method(mods("public"), "boolean", "visit", params("java.lang.String elementName", "int elementIndex", "Location.Position position"));
        cb._return("visit(elementName, elementIndex, (BackboneElement) position)");
        cb.end();
        cb._end();

        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/DefaultVisitor.java");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cb.toString());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void generateAcceptMethod(JsonObject structureDefinition, String className, String path, CodeBuilder cb) {
        if (isAbstract(structureDefinition) || isCodeSubtype(className)) {
            return;
        }

        cb.override();
        cb.method(mods("public"), "void", "accept", params("java.lang.String elementName", "int elementIndex", "Visitor visitor"));

        cb._if("visitor.preVisit(this)");
        cb.invoke("visitor", "visitStart", args("elementName", "elementIndex", "this"));

        cb._if("visitor.visit(elementName, elementIndex, this)");
        cb.comment("visit children");

        List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path);

        for (JsonObject elementDefinition : elementDefinitions) {
            String elementName = getElementName(elementDefinition, path);
            String fieldName = getFieldName(elementName);

            List<String> args = new ArrayList<>();

            args.add(fieldName);
            args.add(quote(elementName));
            args.add("visitor");

            if (isRepeating(elementDefinition)) {
                String fieldType = getFieldType(structureDefinition, elementDefinition, false);
                args.add(fieldType + ".class");
            }

            cb.invoke("accept", args);
        }

        cb._end();

        cb.invoke("visitor", "visitEnd", args("elementName", "elementIndex", "this"));
        cb.invoke("visitor", "postVisit", args("this"));

        cb._end();

        cb.end().newLine();
    }

    private void generateBuilderClass(JsonObject structureDefinition, String className, String path, CodeBuilder cb, boolean nested) {
        String _super = null;
        if (nested) {
            _super = "BackboneElement.Builder";
        } else {
            JsonObject baseDefinition = getBaseDefinition(structureDefinition);
            if (baseDefinition != null) {
                _super = titleCase(baseDefinition.getString("name")) + ".Builder";
            } else {
                _super = "AbstractBuilder<" + structureDefinition.getString("name") + ">";
            }
        }

        if (isAbstract(structureDefinition)) {
            cb._class(mods("public", "static", "abstract"), "Builder", _super);
        } else {
            cb._class(mods("public", "static"), "Builder", _super);
        }

        List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path);

        String visibility = nested ? "private" : visibility(structureDefinition);

        int fieldCount = 0;
        List<JsonObject> declaredElementDefinitions = elementDefinitions.stream().filter(o -> o.getString("path").equals(o.getJsonObject("base").getString("path"))).collect(Collectors.toList());
        if (isProfiledType(className)) {
            declaredElementDefinitions = Collections.emptyList();
        }
        if (!declaredElementDefinitions.isEmpty()) {
            for (JsonObject elementDefinition : declaredElementDefinitions) {
                String fieldName = getFieldName(elementDefinition, path);
                String fieldType = getFieldType(structureDefinition, elementDefinition);
                String init = null;
                if (isRepeating(elementDefinition)) {
                    init = "new ArrayList<>()";
                }
                cb.field(mods(visibility), fieldType, fieldName, init);
                fieldCount++;
            }
        }

        if (fieldCount > 0) {
            cb.newLine();
        }

        cb.constructor(mods(visibility), "Builder")
            ._super()
        .end().newLine();

        List<String> requiredElementNames = new ArrayList<>();

        for (JsonObject elementDefinition : elementDefinitions) {
            boolean declaredBy = isDeclaredBy(className, elementDefinition);

            String fieldName = getFieldName(elementDefinition, path);
            String fieldType = getFieldType(structureDefinition, elementDefinition);

            if (isRequired(elementDefinition)) {
                requiredElementNames.add(getElementName(elementDefinition, path));
            }

            if (isRepeating(elementDefinition)) {
                generateBuilderMethodJavadoc(structureDefinition, elementDefinition, fieldName, "varargs", cb);
                if (!declaredBy) {
                    cb.override();
                }
                cb.method(mods("public"), "Builder", fieldName, params(param(fieldType.replace("java.util.", "").replace("List<", "").replace(">",  "..."), fieldName)));
                if (declaredBy) {
                    String varName = "value";
                    if ("value".equals(fieldName)) {
                        varName = "_value";
                    }
                    String varType = fieldType.replace("java.util.", "").replace("List<", "").replace(">", "");
                    cb._foreach(varType + " " + varName, fieldName)
                        .invoke(_this(fieldName), "add", args(varName))
                    ._end()
                    ._return("this");
                } else {
                    cb._return("(Builder) super." + fieldName + "(" + fieldName + ")");
                }

                cb.end();
                cb.newLine();

                String paramType = fieldType.replace("java.util.", "").replace("List<", "Collection<");

                if (containsBackboneElement(structureDefinition, "collection")) {
                    paramType = "java.util." + paramType;
                }

                generateBuilderMethodJavadoc(structureDefinition, elementDefinition, fieldName, "collection", cb);
                if (!declaredBy) {
                    cb.override();
                }
                cb.method(mods("public"), "Builder", fieldName, params(param(paramType, fieldName)));

                if (declaredBy) {
                    cb.assign(_this(fieldName), _new("ArrayList<>", args(fieldName)));
                    cb._return("this");
                } else {
                    cb._return("(Builder) super." + fieldName + "(" + fieldName + ")");
                }

                cb.end();
                // end if isRepeating
            } else {
                generateBuilderMethodJavadoc(structureDefinition, elementDefinition, fieldName, "single", cb);
                if (!declaredBy) {
                    cb.override();
                }
                cb.method(mods("public"), "Builder", fieldName, params(param(fieldType, fieldName)));
                if (declaredBy) {
                    cb.assign(_this(fieldName), fieldName);
                    cb._return("this");
                } else {
                    cb._return("(Builder) super." + fieldName + "(" + fieldName + ")");
                }
                cb.end();
            }

            cb.newLine();
        }

        if (isBase64Binary(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .invoke("Objects.requireNonNull", args("value"))
                .assign("java.lang.String valueNoWhitespace", "value.replaceAll(\"\\\\s\", \"\")")
                .invoke("ValidationSupport.validateBase64EncodedString", args("valueNoWhitespace"))
                .assign("this.value", "Base64.getDecoder().decode(valueNoWhitespace)")
                ._return("this")
            .end().newLine();
        }

        if (isDateTime(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "PARSER_FORMATTER.parseBest(value, ZonedDateTime::from, LocalDate::from, YearMonth::from, Year::from)")
                ._return("this")
            .end().newLine();
        }

        if (isDate(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "PARSER_FORMATTER.parseBest(value, LocalDate::from, YearMonth::from, Year::from)")
                ._return("this")
            .end().newLine();
        }

        if (isInstant(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "PARSER_FORMATTER.parse(value, ZonedDateTime::from)")
                ._return("this")
            .end().newLine();
        }

        if (isTime(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "PARSER_FORMATTER.parse(value, LocalTime::from)")
                ._return("this")
            .end().newLine();
        }

        if (isInteger(structureDefinition) || isIntegerSubtype(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"));
            if (isInteger(structureDefinition)) {
                cb.assign("this.value", "java.lang.Integer.parseInt(value)");
                cb._return("this");
            } else {
                cb._return("(Builder) super.value(value)");
            }
            cb.end().newLine();
        }

        if (isBoolean(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "java.lang.Boolean.parseBoolean(value)")
                ._return("this")
            .end().newLine();
        }

        if (isDecimal(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "new BigDecimal(value)")
                ._return("this")
            .end().newLine();
        }

        if (isAbstract(structureDefinition)) {
            cb.override();
            cb.abstractMethod(mods("public", "abstract"), className, "build");
        } else {
            cb.javadocStart();
            cb.javadoc("Build the {@link " + className + "}");
            cb.javadoc("");
            if (!requiredElementNames.isEmpty()) {
                cb.javadoc("<p>Required elements:", false);
                cb.javadoc("<ul>", false);
                for (String requiredElementName : requiredElementNames) {
                    cb.javadoc("<li>" + requiredElementName + "</li>", false);
                }
                cb.javadoc("</ul>", false);
                cb.javadoc("");
            }
            cb.javadocReturn("An immutable object of type {@link " + className + "}");
            cb.javadocThrows("IllegalStateException", "if the current state cannot be built into a valid "
                    + className + " per the base specification");
            cb.javadocEnd();
            cb.override();
            cb.method(mods("public"), className, "build")
                ._return(_new(className, args("this")))
            .end();
        }

        String paramName = camelCase(className);
        if (SourceVersion.isKeyword(paramName)) {
            paramName = "_" + paramName;
        }

        cb.newLine();
        cb.method(mods("protected"), "Builder", "from", params(param(className, paramName)));
        // Call super.from unless we are at the topMost resources in the model hierarchy
        if (!(isAbstract(structureDefinition) &&
                ("Resource".equals(className) || "Element".equals(className)))) {
            cb.invoke("super.from", args(paramName));
        }
        for (JsonObject elementDefinition : declaredElementDefinitions) {
            String fieldName = getFieldName(elementDefinition, path);
            String prefix = "";
            if (fieldName.equals(paramName)) {
                prefix = "this.";
            }
            if (isRepeating(elementDefinition)) {
                cb.invoke(prefix + fieldName, "addAll", args(paramName + "." + fieldName));
            } else {
                cb.assign(prefix + fieldName, paramName + "." + fieldName);
            }
        }
        cb._return("this");
        cb.end();

        cb._end();
    }

    /**
     * @param className The name of the class currently being generated
     * @param elementDefinition
     * @return true if the element specified by the ElementDefintion is declared by the class being generated
     */
    private boolean isDeclaredBy(String className, JsonObject elementDefinition) {
        String basePath = elementDefinition.getJsonObject("base").getString("path");
        boolean declaredBy = elementDefinition.getString("path").equals(basePath) && !isProfiledType(className);
        return declaredBy;
    }

    private void generateBuilderMethodJavadoc(JsonObject structureDefinition, JsonObject elementDefinition, String fieldName, String paramType, CodeBuilder cb) {
        String definition = elementDefinition.getString("definition");
        cb.javadocStart();
        cb.javadoc(Arrays.asList(definition.split(System.lineSeparator())), false, false, true);

        switch (paramType) {
        case "single":
            // do nothing
            break;
        case "varargs":
            cb.javadoc("");
            cb.javadoc("<p>Adds new element(s) to the existing list", false);
            break;
        case "collection":
            cb.javadoc("");
            cb.javadoc("<p>Replaces the existing list with a new one containing elements from the Collection", false);
            break;
        }
        cb.javadoc("");

        if (isRequired(elementDefinition)) {
            cb.javadoc("<p>This element is required.", false);
            cb.javadoc("");
        }
        if (isProhibited(elementDefinition)) {
            cb.javadoc("<p>This element is prohibited.", false);
            cb.javadoc("");
        }
        if (isChoiceElement(elementDefinition)) {
            cb.javadoc("<p>This is a choice element with the following allowed types:", false);
            cb.javadoc("<ul>", false);
            for (String choiceTypeName : getChoiceTypeNames(elementDefinition)) {
                cb.javadoc("<li>{@link " + choiceTypeName + "}</li>", false);
            }
            cb.javadoc("</ul>", false);
            cb.javadoc("");
        }
        if (isReferenceElement(structureDefinition, elementDefinition)) {
            List<String> referenceTypes = getReferenceTypes(elementDefinition);

            if (!referenceTypes.isEmpty()) {
                cb.javadoc("<p>Allowed resource types for this reference:", false);
                cb.javadoc("<ul>", false);
                for (String typeName : referenceTypes) {
                    cb.javadoc("<li>{@link " + typeName + "}</li>", false);
                }
                cb.javadoc("</ul>", false);
                cb.javadoc("");
            }
        }

        String _short = elementDefinition.getString("short");
        cb.javadocParam(fieldName, _short);

        cb.javadoc("");

        cb.javadocReturn("A reference to this Builder instance");
        cb.javadocEnd();
    }

    private void generateClass(JsonObject structureDefinition, String basePath) {
        CodeBuilder cb = new CodeBuilder();

        String className = titleCase(structureDefinition.getString("name"));

        String packageName = null;
        String kind = structureDefinition.getString("kind");
        if ("resource".equals(kind)) {
            packageName = "com.ibm.fhir.model.resource";
            resourceClassNames.add(className);
        } else {
            packageName = "com.ibm.fhir.model.type";
            typeClassNames.add(className);
        }
        cb.lines(HEADER).newLine();
        cb._package(packageName).newLine();

        generateImports(structureDefinition, className, cb);

        String path = getElementDefinitions(structureDefinition).get(0).getString("path");
        generateClass(structureDefinition, Collections.singletonList(path), cb, false);

        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/" + className + ".java");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                Files.createFile(file.toPath());
            }
        } catch (Exception e) {
            throw new Error(e);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cb.toString());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void generateClass(JsonObject structureDefinition, List<String> paths, CodeBuilder cb, boolean nested) {
        for (String path : paths) {
            List<String> mods = new ArrayList<>(mods("public"));

            if (isAbstract(structureDefinition)) {
                mods.add("abstract");
            }

            if (nested) {
                mods.add("static");
            }

            String _super = null;
            if (nested) {
                _super = "BackboneElement";
            } else {
                JsonObject baseDefinition = getBaseDefinition(structureDefinition);
                if (baseDefinition != null) {
                    _super = titleCase(baseDefinition.getString("name"));
                } else {
                    _super = "AbstractVisitable";
                }
            }

            cb.javadoc(Arrays.asList(getElementDefinition(structureDefinition, path).getString("definition").split(System.lineSeparator())));

            String className = nested ? titleCase(path.substring(path.lastIndexOf(".") + 1)) : titleCase(structureDefinition.getString("name"));
            if (nested) {
                String nestedClassName = Arrays.asList(path.split("\\.")).stream().map(s -> titleCase(s)).collect(Collectors.joining("."));
                generatedClassNames.add(nestedClassName);
                superClassMap.put(nestedClassName, "BackboneElement");
            } else {
                generatedClassNames.add(className);
                if (isPrimitiveType(structureDefinition)) {
                    primitiveTypeClassNames.add(className);
                }
                if (isQuantitySubtype(structureDefinition)) {
                    quantitySubtypeClassNames.add(className);
                }
                if (isIntegerSubtype(structureDefinition)) {
                    integerSubtypeClassNames.add(className);
                }
                if (isUriSubtype(structureDefinition)) {
                    uriSubtypeClassNames.add(className);
                }
                if (isStringSubtype(structureDefinition)) {
                    stringSubtypeClassNames.add(className);
                }
                if (!"AbstractVisitable".equals(_super)) {
                    superClassMap.put(className, _super);
                }
            }

            if (!nested) {
                generateConstraintAnnotations(structureDefinition, cb);
                generateBindingAnnotation(structureDefinition, cb, className, structureDefinition.getJsonObject("snapshot").getJsonArray("element").getJsonObject(0));
                cb.annotation("Generated", quote("com.ibm.fhir.tools.CodeGenerator"));
            }
            cb._class(mods, className, _super);

            if (isDateTime(structureDefinition)) {
                cb.field(mods("public", "static", "final"), "DateTimeFormatter", "PARSER_FORMATTER", "new DateTimeFormatterBuilder().appendPattern(\"yyyy\").optionalStart().appendPattern(\"-MM\").optionalStart().appendPattern(\"-dd\").optionalStart().appendPattern(\"'T'HH:mm:ss\").optionalStart().appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true).optionalEnd().appendPattern(\"XXX\").optionalEnd().optionalEnd().optionalEnd().toFormatter()").newLine();
            }

            if (isDate(structureDefinition)) {
                cb.field(mods("public", "static", "final"), "DateTimeFormatter", "PARSER_FORMATTER", "DateTimeFormatter.ofPattern(\"[yyyy[-MM[-dd]]]\")").newLine();
            }

            if (isInstant(structureDefinition)) {
                cb.field(mods("public", "static", "final"), "DateTimeFormatter", "PARSER_FORMATTER", "new DateTimeFormatterBuilder().appendPattern(\"yyyy-MM-dd'T'HH:mm:ss\").optionalStart().appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true).optionalEnd().appendPattern(\"XXX\").toFormatter()").newLine();
            }

            if (isTime(structureDefinition)) {
                cb.field(mods("public", "static", "final"), "DateTimeFormatter", "PARSER_FORMATTER", "new DateTimeFormatterBuilder().appendPattern(\"HH:mm:ss\").optionalStart().appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true).optionalEnd().toFormatter()").newLine();
            }

            if (isBoolean(structureDefinition)) {
                cb.field(mods("public", "static", "final"), "Boolean", "TRUE", "Boolean.of(true)");
                cb.field(mods("public", "static", "final"), "Boolean", "FALSE", "Boolean.of(false)").newLine();
            }

            if (isString(structureDefinition) || isUri(structureDefinition) || isStringSubtype(structureDefinition) || isUriSubtype(structureDefinition)) {
                String pattern = getPattern(structureDefinition);
                if (pattern != null) {
                    if (URI_PATTERN.equals(pattern) || STRING_PATTERN.equals(pattern) || isCode(structureDefinition) || isId(structureDefinition)) {
                        // String, Uri, Code, and Id validation is now handled directly in ValidationSupport
                        // and subtypes inheret these checks as well
                    } else {
                        cb.field(mods("private", "static", "final"), "Pattern", "PATTERN", "Pattern.compile(" + quote(pattern.replace("\\", "\\\\")) + ")").newLine();
                    }
                }
            }

            if (isPositiveInt(structureDefinition)) {
                cb.field(mods("private", "static", "final"), "int", "MIN_VALUE", "1").newLine();
            }

            if (isUnsignedInt(structureDefinition)) {
                cb.field(mods("private", "static", "final"), "int", "MIN_VALUE", "0").newLine();
            }

            List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path);
            if (isProfiledType(className)) {
                elementDefinitions = Collections.emptyList();
            }

            List<String> nestedPaths = new ArrayList<>();

            String visibility = nested ? "private" : visibility(structureDefinition);

            int fieldCount = 0;
            for (JsonObject elementDefinition : elementDefinitions) {
                String basePath = elementDefinition.getJsonObject("base").getString("path");
                if (elementDefinition.getString("path").equals(basePath)) {
                    String fieldName = getFieldName(elementDefinition, path);
                    String fieldType = getFieldType(structureDefinition, elementDefinition);
                    if (isSummary(elementDefinition)) {
                        cb.annotation("Summary");
                    }
                    if (isChoiceElement(elementDefinition)) {
                        String types = getChoiceTypeNames(elementDefinition).stream().map(s -> s + ".class").collect(Collectors.joining(", "));
                        cb.annotation("Choice", "{ " + types + " }");
                    }
                    if (isReferenceElement(structureDefinition, elementDefinition)) {
                        List<String> referenceTypes = getReferenceTypes(elementDefinition);
                        if (!referenceTypes.isEmpty()) {
                            String referenceTypesString = getReferenceTypes(elementDefinition).stream().collect(Collectors.joining("\", \""));
                            cb.annotation("ReferenceTarget", "{ \"" + referenceTypesString + "\" }");
                        }
                    }
                    generateBindingAnnotation(structureDefinition, cb, className, elementDefinition);
                    if (isRequired(elementDefinition)) {
                        cb.annotation("Required");
                    }
                    cb.field(mods(visibility, "final"), fieldType, fieldName);
                    if (isBackboneElement(elementDefinition)) {
                        nestedPaths.add(elementDefinition.getString("path"));
                    }
                    fieldCount++;
                }
            }

            if (fieldCount > 0) {
                cb.newLine();
            }

            if (!isAbstract(structureDefinition)) {
                cb.field(mods("private", "volatile"), "int", "hashCode").newLine();
            }

            cb.constructor(mods(visibility), className, args("Builder builder"));
            if ((!"Resource".equals(className) && !"Element".equals(className)) || nested) {
                cb._super(args("builder"));
            }

            for (JsonObject elementDefinition : elementDefinitions) {
                String basePath = elementDefinition.getJsonObject("base").getString("path");
                if (elementDefinition.getString("path").equals(basePath)) {
                    String elementName = getElementName(elementDefinition, path);
                    String fieldName = getFieldName(elementName);

                    if (isRequired(elementDefinition)) {
                        if (isRepeating(elementDefinition)) {
                            cb.assign(fieldName, "Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder." + fieldName + ", " + quote(elementName) + "))");
                        } else {
                            if (isChoiceElement(elementDefinition)) {
                                String types = getChoiceTypeNames(elementDefinition).stream().map(s -> s + ".class").collect(Collectors.joining(", "));
                                cb.assign(fieldName, "ValidationSupport.requireChoiceElement(builder." + fieldName + ", " + quote(elementName) + ", " + types + ")");
                            } else {
                                cb.assign(fieldName, "ValidationSupport.requireNonNull(builder." + fieldName + ", " + quote(elementName) + ")");
                            }
                        }
                    } else {
                        if (isRepeating(elementDefinition)) {
                            cb.assign(fieldName, "Collections.unmodifiableList(ValidationSupport.requireNonNull(builder." + fieldName + ", " + quote(elementName) + "))");
                        } else {
                            if (isChoiceElement(elementDefinition)) {
                                String types = getChoiceTypeNames(elementDefinition).stream().map(s -> s + ".class").collect(Collectors.joining(", "));
                                cb.assign(fieldName, "ValidationSupport.choiceElement(builder." + fieldName + ", " + quote(elementName) + ", " + types + ")");
                            } else {
                                // Instant and DateTime values require special handling
                                if ((isInstant(structureDefinition) || isDateTime(structureDefinition))
                                        && "value".equals(fieldName)) {
                                    cb.assign(fieldName, "ModelSupport.truncateTime(builder.value, ChronoUnit.MICROS)");
                                } else {
                                    cb.assign(fieldName, "builder." + fieldName);
                                }
                            }
                        }
                    }
                }
            }

            if (isUnsignedInt(structureDefinition) || isPositiveInt(structureDefinition)) {
                cb.invoke("ValidationSupport", "checkValue", args("value", "MIN_VALUE"));
            }

            if (isString(structureDefinition)) {
                cb.invoke("ValidationSupport", "checkString", args("value"));
            }
            if (isUri(structureDefinition)) {
                cb.invoke("ValidationSupport", "checkUri", args("value"));
            }

            if (isCode(structureDefinition)) {
                cb.invoke("ValidationSupport", "checkCode", args("value"));
            } else if (isId(structureDefinition)){
                cb.invoke("ValidationSupport", "checkId", args("value"));
            } else if (isStringSubtype(structureDefinition)) {
                if (!STRING_PATTERN.equals(getPattern(structureDefinition))) {
                    cb.invoke("ValidationSupport", "checkValue", args("value", "PATTERN"));
                }
            }

            if (isUriSubtype(structureDefinition)) {
                if (!URI_PATTERN.equals(getPattern(structureDefinition))) {
                    cb.invoke("ValidationSupport", "checkValue", args("value", "PATTERN"));
                }
            }

            if (isDate(structureDefinition)) {
                cb.invoke("ValidationSupport", "checkValueType", args("value", "LocalDate.class", "YearMonth.class", "Year.class"));
            }

            if (isDateTime(structureDefinition)) {
                cb.invoke("ValidationSupport", "checkValueType", args("value", "ZonedDateTime.class", "LocalDate.class", "YearMonth.class", "Year.class"));
            }

            for (JsonObject elementDefinition : getElementDefinitions(structureDefinition, path)) {
                if (isProhibited(elementDefinition)) {
                    String elementName = getElementName(elementDefinition, path);
                    String fieldName = getFieldName(elementName);
                    cb.invoke("ValidationSupport", "prohibited", args(fieldName, quote(elementName)));
                }
            }

            if ("Resource".equals(className) && !nested) {
                cb.invoke("ValidationSupport", "checkId", args("id"));
            }

            if ("Element".equals(className) && !nested) {
                cb.invoke("ValidationSupport", "checkString", args("id"));
            }

            if ("Extension".equals(className) && !nested) {
                cb.invoke("ValidationSupport", "checkUri", args("url"));
            }

            for (JsonObject elementDefinition : elementDefinitions) {
                String elementName = getElementName(elementDefinition, path);
                String fieldName = getFieldName(elementName);
                String fieldType = getFieldType(structureDefinition, elementDefinition);
                if ("CodeableConcept".equals(fieldType) && hasRequiredBinding(elementDefinition)) {
                    JsonObject binding = getBinding(elementDefinition);
                    String valueSet = binding.getString("valueSet");
                    String[] tokens = valueSet.split("\\|");
                    valueSet = tokens[0];
                    String system = getSystem(valueSet);
                    List<JsonObject> concepts = getConcepts(valueSet);
                    cb.invoke("ValidationSupport", "checkCodeableConcept", args(fieldName, quote(elementName), quote(valueSet), quote(system), concepts.stream().map(concept -> quote(concept.getString("code"))).collect(Collectors.joining(", "))));
                }
            }

            for (JsonObject elementDefinition : elementDefinitions) {
                String elementName = getElementName(elementDefinition, path);
                String fieldName = getFieldName(elementName);
                if (isReferenceElement(structureDefinition, elementDefinition)) {
                    List<String> referenceTypes = getReferenceTypes(elementDefinition);
                    if (!referenceTypes.isEmpty()) {
                        cb.invoke("ValidationSupport", "checkReferenceType", args(fieldName, quote(elementName), referenceTypes.stream().map(type -> quote(type)).collect(Collectors.joining(", "))));
                    }
                }
            }

            if ((!isResource(structureDefinition) &&
                    !isAbstract(structureDefinition) &&
                    !isStringSubtype(structureDefinition) &&
                    !isUriSubtype(structureDefinition) &&
                    !isQuantitySubtype(structureDefinition) &&
                    !isXhtml(structureDefinition)) ||
                    nested) {
                cb.invoke("ValidationSupport", "requireValueOrChildren", args("this"));
            }

            if (isResource(structureDefinition) && !isAbstract(structureDefinition) && !nested) {
                cb.invoke("ValidationSupport", "requireChildren", args("this"));
            }

            if (isXhtml(structureDefinition)) {
                cb.invoke("ValidationSupport", "checkXHTMLContent", args("value"));
            }

            cb.end().newLine();

            for (JsonObject elementDefinition : elementDefinitions) {
                String basePath = elementDefinition.getJsonObject("base").getString("path");
                if (elementDefinition.getString("path").equals(basePath)) {
                    String fieldName = getFieldName(elementDefinition, path);
                    String fieldType = getFieldType(structureDefinition, elementDefinition);
                    String methodName = "get" + titleCase(fieldName.replace("_", ""));
                    generateGetterMethodJavadoc(structureDefinition, elementDefinition, fieldType, cb);
                    cb.method(mods("public"), fieldType, methodName)._return(fieldName).end().newLine();
                }
            }

            generateIsPartialMethod(structureDefinition, cb);
            generateIsAsMethods(structureDefinition, cb);
            generateHasValueMethod(structureDefinition, cb);
            generateHasChildrenMethod(structureDefinition, path, cb, nested);
            generateFactoryMethods(structureDefinition, cb);
            generateAcceptMethod(structureDefinition, className, path, cb);
            generateEqualsHashCodeMethods(structureDefinition, className, path, cb);

            List<String> params = new ArrayList<>();
            List<String> args = new ArrayList<>();

            List<JsonObject> requiredElementDefinitions = elementDefinitions.stream().filter(o -> isRequired(o)).collect(Collectors.toList());

            if (isAbstract(structureDefinition)) {
                if (!"Resource".equals(className) && !"Element".equals(className)) {
                    cb.override();
                }
                cb.abstractMethod(mods("public", "abstract"), "Builder", "toBuilder").newLine();
            } else {
                for (JsonObject elementDefinition : requiredElementDefinitions) {
                    String fieldName = getFieldName(elementDefinition, path);
                    String fieldType = getFieldType(structureDefinition, elementDefinition);
                    String paramType = fieldType.replace("java.util.", "").replace("List<", "Collection<");
                    if (containsBackboneElement(structureDefinition, "collection")) {
                        paramType = "java.util." + paramType;
                    }
                    params.add(paramType + " " + fieldName);
                    args.add(fieldName);
                }

                cb.override();
                cb.method(mods("public"), "Builder", "toBuilder")
                    ._return(_new("Builder") + ".from(this)")
                .end().newLine();

                cb.method(mods("public", "static"), "Builder", "builder");
                    cb._return(_new("Builder"));
                cb.end().newLine();
            }

            generateBuilderClass(structureDefinition, className, path, cb, nested);

            if (!nestedPaths.isEmpty()) {
                cb.newLine();
                generateClass(structureDefinition, nestedPaths, cb, true);
            }

            cb._end();
            if (!isLast(paths, path)) {
                cb.newLine();
            }
        }
    }

    private List<String> getReferenceTypes(JsonObject elementDefinition) {
        List<String> referenceTypes = new ArrayList<>();
        for (JsonValue type : elementDefinition.getOrDefault("type", JsonArray.EMPTY_JSON_ARRAY).asJsonArray()) {
            for (JsonValue targetProfile : type.asJsonObject().getOrDefault("targetProfile", JsonArray.EMPTY_JSON_ARRAY).asJsonArray()) {
                String url = ((JsonString) targetProfile).getString();
                if (url.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
                    String referenceType = url.substring("http://hl7.org/fhir/StructureDefinition/".length());
                    if (!"Resource".equals(referenceType)) {
                        referenceTypes.add(referenceType);
                    }
                }
            }
        }
        return referenceTypes;
    }

    private void generateBindingAnnotation(JsonObject structureDefinition, CodeBuilder cb, String className, JsonObject elementDefinition) {
        JsonObject binding = getBinding(elementDefinition);
        if (binding != null) {
            String bindingName = getBindingName(binding);
            String strength = binding.getString("strength", null);
            String description = binding.getString("description", null);
            String valueSet = binding.getString("valueSet", null);
            String inheritedExtensibleValueSet = getInheritedExtensibleValueSet(binding);
            String minValueSet = getMinValueSet(binding);
            String maxValueSet = getMaxValueSet(binding);

            Map<String, String> valueMap = new LinkedHashMap<>();
            if (bindingName != null) {
                valueMap.put("bindingName", "\"" + bindingName + "\"");
            }
            if (strength != null) {
                valueMap.put("strength", "BindingStrength.ValueSet." + strength.toUpperCase());
            }
            if (description != null) {
                valueMap.put("description", "\"" + description.replace("\"", "\\\"") + "\"");
            }
            if (valueSet != null) {
                valueMap.put("valueSet", "\"" + valueSet + "\"");
            }
            if (inheritedExtensibleValueSet != null) {
                valueMap.put("inheritedExtensibleValueSet", "\"" + inheritedExtensibleValueSet + "\"");
            }
            if (minValueSet != null) {
                valueMap.put("minValueSet", "\"" + minValueSet + "\"");
            }
            if (maxValueSet != null) {
                valueMap.put("maxValueSet", "\"" + maxValueSet + "\"");
            }

            if (!valueMap.isEmpty()) {
                String name = structureDefinition.getString("name");

                String prefix = "";
                if ("ElementDefinition".equals(name) ||
                        ("OperationDefinition".equals(name) &&
                                ("Parameter".equals(className) || "Binding".equals(className)))) {
                    prefix = "com.ibm.fhir.model.annotation.";
                }

                cb.annotation(prefix + "Binding", valueMap);
            }
        }
    }

    @SuppressWarnings("unused")
    private void generateToStringMethod(JsonObject structureDefinition, CodeBuilder cb) {
        if (isDateTime(structureDefinition) || isInstant(structureDefinition) || isDate(structureDefinition) || isTime(structureDefinition)) {
            cb.override()
            .method(mods("public"), "java.lang.String", "toString")
                ._if("value != null")
                    ._return("PARSER_FORMATTER.format(value)")
                ._end()
                ._return("super.toString()")
            .end()
            .newLine();
        }
    }

    private void generateEqualsHashCodeMethods(JsonObject structureDefinition, String className, String path, CodeBuilder cb) {
        if (isAbstract(structureDefinition)) {
            return;
        }

        int level = path.split("\\.").length + 2;

        List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path);
        cb.override();
        cb.method(mods("public"), "boolean", "equals", params("Object obj"));
        cb._if("this == obj")
            ._return("true")
        ._end();
        cb._if("obj == null")
            ._return("false")
        ._end();
        cb._if("getClass() != obj.getClass()")
            ._return("false")
        ._end();
        cb.assign(className + " other", "(" + className + ") obj");
        StringJoiner joiner = new StringJoiner(" && " + System.lineSeparator() + indent(level));
        for (JsonObject elementDefinition : elementDefinitions) {
            String elementName = getElementName(elementDefinition, path);
            String fieldName = getFieldName(elementName);
            String fieldType = getFieldType(structureDefinition, elementDefinition);
            String prefix = "";
            if ("other".equals(fieldName)) {
                prefix = "this.";
            }
            if ("byte[]".equals(fieldType)) {
                joiner.add("Arrays.equals(" + prefix + fieldName + ", other." + fieldName +")");
            } else {
                joiner.add("Objects.equals(" + prefix + fieldName + ", other." + fieldName +")");
            }
        }
        cb._return(joiner.toString());
        cb.end().newLine();

        cb.override();
        cb.method(mods("public"), "int", "hashCode");
        cb.assign("int result", "hashCode");
        cb._if("result == 0");

        joiner = new StringJoiner(", " + System.lineSeparator() + indent(level + 1));
        for (JsonObject elementDefinition : elementDefinitions) {
            String elementName = getElementName(elementDefinition, path);
            String fieldName = getFieldName(elementName);
            joiner.add(fieldName);
        }

        cb.assign("result", "Objects.hash(" + joiner.toString() + ")");
        cb.assign("hashCode", "result");

        cb._end();

        cb._return("result");
        cb.end().newLine();
    }

    private String indent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("    ");
        }
        return sb.toString();
    }

    private void generateConstraintAnnotations(JsonObject structureDefinition, CodeBuilder cb) {
        String name = structureDefinition.getString("name");

        List<JsonObject> allConstraints = new ArrayList<>();

        Map<String, String> pathMap = new HashMap<>();

        for (JsonObject elementDefinition : getElementDefinitions(structureDefinition).stream().filter(e -> e.containsKey("constraint")).collect(Collectors.toList())) {
            String path = elementDefinition.getString("path");
            List<JsonObject> constraints = elementDefinition.getJsonArray("constraint").stream().map(o -> o.asJsonObject()).collect(Collectors.toList());
            constraints.forEach(c -> pathMap.put(c.getString("key"), path));
            allConstraints.addAll(constraints);
        }

        Collections.sort(allConstraints, new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject first, JsonObject second) {
                String firstKey = first.getString("key");
                firstKey = firstKey.substring(firstKey.indexOf("-") + 1);
                String firstSuffix = "";
                if (Character.isLetter(firstKey.charAt(firstKey.length() - 1))) {
                    firstSuffix = firstKey.substring(firstKey.length() - 1);
                    firstKey = firstKey.substring(0, firstKey.length() - 1);
                }

                String secondKey = second.getString("key");
                secondKey = secondKey.substring(secondKey.indexOf("-") + 1);
                String secondSuffix = "";
                if (Character.isLetter(secondKey.charAt(secondKey.length() - 1))) {
                    secondSuffix = secondKey.substring(secondKey.length() - 1);
                    secondKey = secondKey.substring(0, secondKey.length() - 1);
                }

                int firstValue = Integer.parseInt(firstKey);
                int secondValue = Integer.parseInt(secondKey);

                if (firstValue == secondValue) {
                    return firstSuffix.compareTo(secondSuffix);
                }

                return firstValue - secondValue;
            }
        });

        for (JsonObject constraint : allConstraints) {
            String key = constraint.getString("key");
            String path = pathMap.get(key);
            String severity = constraint.getString("severity");
            String human = constraint.getString("human");
            String expression = constraint.getString("expression");

            Map<String, String> valueMap = new LinkedHashMap<>();
            valueMap.put("id", quote(key));
            valueMap.put("level", "error".equals(severity) ? quote("Rule") : quote("Warning"));
            valueMap.put("location", path.equals(name) ? quote("(base)") : quote(path.replace(".div", ".`div`").replace("[x]", "")));
            valueMap.put("description", quote(human.replace("\"", "\\\"")));
            valueMap.put("expression", quote(expression.replace("\"", "\\\"")));
            if (MODEL_CHECKED_CONSTRAINTS.contains(key)) {
                valueMap.put("modelChecked", "true");
            }
            cb.annotation("Constraint", valueMap);
        }
    }

    private boolean hasConstraints(JsonObject structureDefinition) {
        for (JsonObject elementDefinition : getElementDefinitions(structureDefinition)) {
            if (elementDefinition.containsKey("constraint")) {
                return true;
            }
        }
        return false;
    }

    private void generateGetterMethodJavadoc(JsonObject structureDefinition, JsonObject elementDefinition, String fieldType, CodeBuilder cb) {
        String definition = elementDefinition.getString("definition");
        cb.javadocStart();
        cb.javadoc(Arrays.asList(definition.split(System.lineSeparator())), false, false, true);
        cb.javadoc("");
        if (isRepeating(elementDefinition)) {
            String reference = getFieldType(structureDefinition, elementDefinition, false);
            reference = reference.substring(reference.lastIndexOf(".") + 1);
            cb.javadocReturn("An unmodifiable list containing immutable objects of type " + javadocLink(reference) + ".");
        } else {
            // Processes the JavaDoc Link into an absolute path where available.
            String reference = getFieldTypeForJavaDocLink(structureDefinition, elementDefinition, fieldType);
            cb.javadocReturn("An immutable object of type " + javadocLink(reference) + ".");
        }
        cb.javadocEnd();
    }

    private void generateFactoryMethods(JsonObject structureDefinition, CodeBuilder cb) {
        String className = titleCase(structureDefinition.getString("name"));

        if (isDate(structureDefinition) || isDateTime(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("TemporalAccessor value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "of", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
        }

        if (isDateTime(structureDefinition)) {
            cb.method(mods("public", "static"), className, "now")
                ._return(className + ".builder().value(ZonedDateTime.now()).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "now", params("ZoneOffset offset"))
                ._return(className + ".builder().value(ZonedDateTime.now(offset)).build()")
            .end().newLine();
        }

        if (isInstant(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("ZonedDateTime value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "of", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "now")
                ._return(className + ".builder().value(ZonedDateTime.now()).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "now", params("ZoneOffset offset"))
                ._return(className + ".builder().value(ZonedDateTime.now(offset)).build()")
            .end().newLine();
        }

        if (isTime(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("LocalTime value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "of", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
        }

        if (isBoolean(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("java.lang.Boolean value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "of", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
        }

        if (isDecimal(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("BigDecimal value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "of", params("Number value"))
                ._return(className + ".builder().value(value.toString()).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "of", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
        }

        if (isInteger(structureDefinition) || isIntegerSubtype(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("java.lang.Integer value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "of", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
        }

        if (isString(structureDefinition) || isStringSubtype(structureDefinition) ||
                isUri(structureDefinition) || isUriSubtype(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
        }

        if (isCanonical(structureDefinition)) {
//            StringBuilder value = new StringBuilder(url);
//            if (version != null && !version.isEmpty()) {
//                value.append("|" + version);
//            }
//            return of(value.toString());
            cb.method(mods("public", "static"), className, "of", params("java.lang.String uri", "java.lang.String version"))
                .assign("StringBuilder value", "new StringBuilder(uri)")
                ._if("version != null && !version.isEmpty()")
                    .invoke("value", "append", args("'|'"))
                    .invoke("value", "append", args("version"))
                .end()
                ._return(className + ".builder().value(value.toString()).build()")
            .end().newLine();

            cb.method(mods("public", "static"), className, "of", params("java.lang.String uri", "java.lang.String version", "java.lang.String fragment"))
                .assign("StringBuilder value", "new StringBuilder(uri)")
                ._if("version != null && !version.isEmpty()")
                    .invoke("value", "append", args("'|'"))
                    .invoke("value", "append", args("version"))
                .end()
                ._if("fragment != null && !fragment.isEmpty()")
                    .invoke("value", "append", args("'#'"))
                    .invoke("value", "append", args("fragment"))
                .end()
            ._return(className + ".builder().value(value.toString()).build()")
        .end().newLine();
        }

        if (isString(structureDefinition) || isStringSubtype(structureDefinition)) {
            cb.method(mods("public", "static"), "String", "string", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
        }

        if (isUri(structureDefinition) || isUriSubtype(structureDefinition)) {
            cb.method(mods("public", "static"), "Uri", "uri", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
        }

        if (isInteger(structureDefinition) || isIntegerSubtype(structureDefinition)) {
            cb.method(mods("public", "static"), "Integer", "integer", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
        }

        if (isCode(structureDefinition)) {
            cb.method(mods("public", "static"), "Code", "code", params("java.lang.String value"))
                ._return("Code.builder().value(value).build()")
            .end().newLine();
        }

        if (isXhtml(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
            cb.method(mods("public", "static"), "Xhtml", "xhtml", params("java.lang.String value"))
                ._return("Xhtml.builder().value(value).build()")
            .end().newLine();
        }
    }

    private void generateImports(JsonObject structureDefinition, String className, CodeBuilder cb) {
        String name = structureDefinition.getString("name");

        Set<String> imports = new HashSet<>();

        boolean containsCollection = false;
        boolean repeating = false;

        if (!isAbstract(structureDefinition)) {
            imports.add("java.util.Objects");
        }

        if ("Resource".equals(name) || "Element".equals(name)) {
            imports.add("com.ibm.fhir.model.visitor.AbstractVisitable");
        }

        if (!isAbstract(structureDefinition) && !isCodeSubtype(className)) {
            imports.add("com.ibm.fhir.model.visitor.Visitor");
        }

        if (hasConstraints(structureDefinition)) {
            imports.add("com.ibm.fhir.model.annotation.Constraint");
        }

        imports.add("javax.annotation.Generated");

        for (JsonObject elementDefinition : getElementDefinitions(structureDefinition, true)) {
            String path = elementDefinition.getString("path");
            String basePath = elementDefinition.getJsonObject("base").getString("path");

            if (isBackboneElement(elementDefinition)) {
                imports.add("com.ibm.fhir.model.type.BackboneElement");
                imports.add("com.ibm.fhir.model.util.ValidationSupport");
            }

            if (path.endsWith("collection") && isBackboneElement(elementDefinition)) {
                containsCollection = true;
            }

            if (isRepeating(elementDefinition)) {
                if (basePath.startsWith(name) && !basePath.equals(name)) {
                    imports.add("com.ibm.fhir.model.util.ValidationSupport");
                    imports.add("java.util.ArrayList");
                    imports.add("java.util.Collections");
                    if (!"List".equals(name)) {
                        imports.add("java.util.List");
                    }
                }
                repeating = !basePath.equals(name);
            }

            if (isRequired(elementDefinition)) {
                imports.add("com.ibm.fhir.model.util.ValidationSupport");
                imports.add("com.ibm.fhir.model.annotation.Required");
            }

            if (isSummary(elementDefinition) && isDeclaredBy(className, elementDefinition)) {
                imports.add("com.ibm.fhir.model.annotation.Summary");
            }

            if (isChoiceElement(elementDefinition)) {
                imports.add("com.ibm.fhir.model.util.ValidationSupport");
                if (isResource(structureDefinition)) {
                    for (String dataTypeName : getChoiceTypeNames(elementDefinition)) {
                        imports.add("com.ibm.fhir.model.type." + dataTypeName);
                    }
                }
                imports.add("com.ibm.fhir.model.annotation.Choice");
            }

            if (isReferenceElement(structureDefinition, elementDefinition)) {
                if (!getReferenceTypes(elementDefinition).isEmpty()) {
                    imports.add("com.ibm.fhir.model.util.ValidationSupport");
                    imports.add("com.ibm.fhir.model.annotation.ReferenceTarget");
                }
            }

            JsonObject binding = getBinding(elementDefinition);
            if (binding != null &&
                    path.equals(basePath) &&
                    !"ElementDefinition".equals(name) &&
                    !isProfiledType(name)) {
                imports.add("com.ibm.fhir.model.type.code.BindingStrength");
                imports.add("com.ibm.fhir.model.annotation.Binding");
            }

            if (isBackboneElement(elementDefinition) || "Element.id".equals(basePath)) {
                continue;
            }

            String fieldType = getFieldType(structureDefinition, elementDefinition, false);
            JsonObject definition = structureDefinitionMap.get(fieldType);
            if (definition == null) {
                definition = structureDefinitionMap.get(camelCase(fieldType));
            }

            if (isResource(structureDefinition) && isDataType(definition)) {
                imports.add("com.ibm.fhir.model.type." + fieldType);
            } else if (hasRequiredBinding(elementDefinition)) {
                imports.add("com.ibm.fhir.model.type.code." + fieldType);
            }

            if (isProhibited(elementDefinition)) {
                imports.add("com.ibm.fhir.model.util.ValidationSupport");
            }
        }

        if (!containsCollection && repeating) {
            imports.add("java.util.Collection");
        }

        if ("Resource".equals(name)) {
            imports.add("com.ibm.fhir.model.util.ValidationSupport");
            imports.add("com.ibm.fhir.model.builder.AbstractBuilder");
        }
        if ("Element".equals(name)) {
            imports.add("com.ibm.fhir.model.builder.AbstractBuilder");
        }

        if (isBase64Binary(structureDefinition)) {
            imports.add("java.util.Base64");
            imports.add("java.util.Arrays");
        }

        if (isDateTime(structureDefinition) || isInstant(structureDefinition)) {
            imports.add("java.time.ZonedDateTime");
            imports.add("java.time.temporal.ChronoUnit");
            imports.add("com.ibm.fhir.model.util.ModelSupport");
        }

        if (isTime(structureDefinition)) {
            imports.add("java.time.LocalTime");
        }

        if (isDate(structureDefinition) || isDateTime(structureDefinition) || isInstant(structureDefinition) || isTime(structureDefinition)) {
            if (isDate(structureDefinition) || isDateTime(structureDefinition)) {
                imports.add("java.time.temporal.TemporalAccessor");
                imports.add("java.time.LocalDate");
                imports.add("java.time.Year");
                imports.add("java.time.YearMonth");
            }
            imports.add("java.time.format.DateTimeFormatter");
            if (isDateTime(structureDefinition) || isInstant(structureDefinition) || isTime(structureDefinition)) {
                imports.add("java.time.temporal.ChronoField");
                imports.add("java.time.format.DateTimeFormatterBuilder");
            }
        }

        if (isDateTime(structureDefinition) || isInstant(structureDefinition)) {
            imports.add("java.time.ZoneOffset");
        }

        if (isString(structureDefinition) ||
                isId(structureDefinition) ||
                isCode(structureDefinition) ||
                isUri(structureDefinition) ||
                isIntegerSubtype(structureDefinition) ||
                isDate(structureDefinition) ||
                isDateTime(structureDefinition)) {
            imports.add("com.ibm.fhir.model.util.ValidationSupport");
        } else if (isStringSubtype(structureDefinition)){
            // only add the Pattern import if the pattern differs from the base String pattern
            if (!STRING_PATTERN.equals(getPattern(structureDefinition))) {
                imports.add("com.ibm.fhir.model.util.ValidationSupport");
                imports.add("java.util.regex.Pattern");
            }
        } else if (isUriSubtype(structureDefinition)) {
            // only add the Pattern import if the pattern differs from the base Uri pattern
            if (!URI_PATTERN.equals(getPattern(structureDefinition))) {
                imports.add("com.ibm.fhir.model.util.ValidationSupport");
                imports.add("java.util.regex.Pattern");
            }
        }

        if ((!isResource(structureDefinition) &&
                !isAbstract(structureDefinition) &&
                !isStringSubtype(structureDefinition) &&
                !isUriSubtype(structureDefinition)) &&
                !isQuantitySubtype(structureDefinition)) {
            imports.add("com.ibm.fhir.model.util.ValidationSupport");
        }

        if (isDecimal(structureDefinition)) {
            imports.add("java.math.BigDecimal");
        }

        List<String> javaImports = new ArrayList<>();
        for (String _import : imports) {
            if (_import.startsWith("java.")) {
                javaImports.add(_import);
            }
        }

        if (!javaImports.isEmpty()) {
            Collections.sort(javaImports);
            for (String javaImport : javaImports) {
                cb._import(javaImport);
            }
            cb.newLine();
        }

        List<String> javaxImports = new ArrayList<>();
        for (String _import : imports) {
            if (_import.startsWith("javax")) {
                javaxImports.add(_import);
            }
        }

        if (!javaxImports.isEmpty()) {
            Collections.sort(javaxImports);
            for (String javaxImport : javaxImports) {
                cb._import(javaxImport);
            }
            cb.newLine();
        }

        List<String> otherImports = new ArrayList<>();
        for (String _import : imports) {
            if (_import.startsWith("com")) {
                otherImports.add(_import);
            }
        }

        if (!otherImports.isEmpty()) {
            Collections.sort(otherImports);
            for (String otherImport : otherImports) {
                cb._import(otherImport);
            }
            cb.newLine();
        }
    }

    private void generateIsAsMethods(JsonObject structureDefinition, CodeBuilder cb) {
        String name = structureDefinition.getString("name");
        if ("Resource".equals(name)) {
            cb.method(mods("public"), "<T extends Resource> boolean", "is", params("Class<T> resourceType"))
                ._return("resourceType.isInstance(this)")
            .end().newLine();

            cb.javadocStart()
                .javadocThrows("ClassCastException", "when this resources cannot be cast to the requested resourceType")
                .javadocEnd();
            cb.method(mods("public"), "<T extends Resource> T", "as", params("Class<T> resourceType"))
                ._return("resourceType.cast(this)")
            .end().newLine();
        }
        if ("Element".equals(name)) {
            cb.method(mods("public"), "<T extends Element> boolean", "is", params("Class<T> elementType"))
                ._return("elementType.isInstance(this)")
            .end().newLine();

            cb.javadocStart()
                .javadocThrows("ClassCastException", "when this element cannot be cast to the requested elementType")
                .javadocEnd();
            cb.method(mods("public"), "<T extends Element> T", "as", params("Class<T> elementType"))
                ._return("elementType.cast(this)")
            .end().newLine();
        }
    }

    private void generateHasValueMethod(JsonObject structureDefinition, CodeBuilder cb) {
        String name = structureDefinition.getString("name");

        if (!"Element".equals(name) && !isPrimitiveType(structureDefinition)) {
            return;
        }

        if ("Element".equals(name)) {
            cb.javadocStart()
                .javadocReturn("true if the element is a FHIR primitive type and has a primitive value "
                        + "(as opposed to not having a value and just having extensions), otherwise false")
                .javadocEnd();
        } else {
            cb.override();
        }

        cb.method(mods("public"), "boolean", "hasValue");

        if ("Element".equals(name)) {
            cb._return("false");
        } else {
            cb._return("(value != null)");
        }

        cb.end().newLine();
    }

    private void generateHasChildrenMethod(JsonObject structureDefinition, String path, CodeBuilder cb, boolean nested) {
        if (isStringSubtype(structureDefinition) ||
            isUriSubtype(structureDefinition) ||
            isQuantitySubtype(structureDefinition)) {
            return;
        }

        String name = structureDefinition.getString("name");

        if (!"Element".equals(name) && !"Resource".equals(name)) {
            cb.override();
        }

        cb.method(mods("public"), "boolean", "hasChildren");

        int level = path.split("\\.").length + 2;
        StringJoiner joiner = new StringJoiner(" || " + System.lineSeparator() + indent(level));

        if (!"Element".equals(name) && !"Resource".equals(name)) {
            joiner.add("super.hasChildren()");
        }

        List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path);
        for (JsonObject elementDefinition : elementDefinitions) {
            String basePath = elementDefinition.getJsonObject("base").getString("path");
            boolean declaredBy = elementDefinition.getString("path").equals(basePath);
            if (declaredBy) {
                String elementName = getElementName(elementDefinition, path);
                String fieldName = getFieldName(elementName);

                if ("Element".equals(name) && "id".equals(fieldName)) {
                    continue;
                }

                if (isPrimitiveType(structureDefinition) && "value".equals(fieldName)) {
                    continue;
                }

                if (isRepeating(elementDefinition)) {
                    joiner.add("!" + fieldName + ".isEmpty()");
                } else {
                    joiner.add("(" + fieldName + " != null)");
                }
            }
        }

        cb._return(joiner.toString());

        cb.end().newLine();
    }

    private void generateIsPartialMethod(JsonObject structureDefinition, CodeBuilder cb) {
        if (isDate(structureDefinition) || isDateTime(structureDefinition)) {
            cb.method(mods("public"), "boolean", "isPartial");
            if (isDate(structureDefinition)) {
                cb._return("!(value instanceof LocalDate)");
            } else {
                cb._return("!(value instanceof ZonedDateTime)");
            }
            cb.end().newLine();
        }
    }

    private void generateXMLParser(String basePath) {
        CodeBuilder cb = new CodeBuilder();

        String packageName = "com.ibm.fhir.model.parser";
        cb.lines(HEADER).newLine();
        cb._package(packageName).newLine();

        cb._importstatic("com.ibm.fhir.model.util.XMLSupport", "FHIR_NS_URI");
        cb._importstatic("com.ibm.fhir.model.util.XMLSupport", "XHTML_NS_URI");
        cb._importstatic("com.ibm.fhir.model.util.XMLSupport", "checkElementOrder");
        cb._importstatic("com.ibm.fhir.model.util.XMLSupport", "createStreamReaderDelegate");
        cb._importstatic("com.ibm.fhir.model.util.XMLSupport", "isResourceContainer");
        cb._importstatic("com.ibm.fhir.model.util.XMLSupport", "parseDiv");
        cb._importstatic("com.ibm.fhir.model.util.XMLSupport", "requireNamespace");
        cb.newLine();

        cb._import("java.io.InputStream");
        cb._import("java.io.Reader");
        cb._import("java.util.Stack");
        cb._import("java.util.StringJoiner");
        cb.newLine();

        cb._import("javax.annotation.Generated");
        cb._import("javax.xml.stream.XMLStreamException");
        cb._import("javax.xml.stream.XMLStreamReader");
        cb.newLine();

        cb._import("com.ibm.fhir.model.parser.FHIRAbstractParser");
        cb._import("com.ibm.fhir.model.parser.exception.FHIRParserException");
        cb._import("com.ibm.fhir.model.resource.*");
        cb._import("com.ibm.fhir.model.type.*");
        cb._import("com.ibm.fhir.model.type.code.*");
        cb._import("com.ibm.fhir.model.type.Boolean");
        cb._import("com.ibm.fhir.model.type.Integer");
        cb._import("com.ibm.fhir.model.type.String");
        cb._import("com.ibm.fhir.model.util.XMLSupport.StreamReaderDelegate");
        cb.newLine();

        cb._import("net.jcip.annotations.NotThreadSafe");
        cb.newLine();

        cb.annotation("NotThreadSafe");
        cb.annotation("Generated", quote("com.ibm.fhir.tools.CodeGenerator"));
        cb._class(mods("public"), "FHIRXMLParser", "FHIRAbstractParser");
        cb.field(mods("public", "static"), "boolean", "DEBUG", "false");
        cb.newLine();

        cb.field(mods("private", "final"), "Stack<java.lang.String>", "stack", _new("Stack<>"));
        cb.newLine();

        cb.constructor(mods(), "FHIRXMLParser");
        cb.comment("only visible to subclasses or classes/interfaces in the same package (e.g. FHIRParser)");
        cb.end();
        cb.newLine();

        // public <T extends Resource> T parse(InputStream in) throws FHIRParserException
        cb.annotation("SuppressWarnings", quote("unchecked"));
        cb.override();
        cb.method(mods("public"), "<T extends Resource> T", "parse", params("InputStream in"), throwsExceptions("FHIRParserException"))
            ._try("StreamReaderDelegate delegate = createStreamReaderDelegate(in)")
                .invoke("reset", args())
                ._while("delegate.hasNext()")
                    .assign("int eventType", "delegate.next()")
                    ._switch("eventType")
                    ._case("XMLStreamReader.START_ELEMENT")
                        .invoke("requireNamespace", args("delegate", "FHIR_NS_URI"))
                        ._return("(T) parseResource(getResourceType(delegate), delegate, -1)")
                    ._end()
                ._end()
                ._throw(_new("XMLStreamException", args(quote("Unexpected end of stream"))))
            ._catch("Exception e")
                ._throw(_new("FHIRParserException", args("e.getMessage()", "getPath()", "e")))
            ._end()
        .end();
        cb.newLine();

        // public <T extends Resource> T parse(Reader reader) throws FHIRParserException
        cb.annotation("SuppressWarnings", quote("unchecked"));
        cb.override();
        cb.method(mods("public"), "<T extends Resource> T", "parse", params("Reader reader"), throwsExceptions("FHIRParserException"))
            ._try("StreamReaderDelegate delegate = createStreamReaderDelegate(reader)")
                .invoke("reset", args())
                ._while("delegate.hasNext()")
                    .assign("int eventType", "delegate.next()")
                    ._switch("eventType")
                    ._case("XMLStreamReader.START_ELEMENT")
                        .invoke("requireNamespace", args("delegate", "FHIR_NS_URI"))
                        ._return("(T) parseResource(getResourceType(delegate), delegate, -1)")
                    ._end()
                ._end()
                ._throw(_new("XMLStreamException", args(quote("Unexpected end of stream"))))
            ._catch("Exception e")
                ._throw(_new("FHIRParserException", args("e.getMessage()", "getPath()", "e")))
            ._end()
        .end();
        cb.newLine();

        cb.method(mods("private"), "void", "reset")
            .invoke("stack", "clear", args())
        .end();
        cb.newLine();

        cb.method(mods("private"), "Resource", "parseResource", params("java.lang.String elementName", "XMLStreamReader reader", "int elementIndex"), throwsExceptions("XMLStreamException"));
        cb._if("isResourceContainer(elementName)")
            .invoke("reader", "nextTag", args())
        ._end();
        cb.assign("java.lang.String resourceType", "getResourceType(reader)");
        cb._switch("resourceType");
        for (String resourceClassName : resourceClassNames) {
            if ("Resource".equals(resourceClassName) || "DomainResource".equals(resourceClassName)) {
                continue;
            }
            cb._case(quote(resourceClassName));
            cb._return("parse" + resourceClassName + "(elementName, reader, elementIndex)");
        }
        cb._end();
        cb._return("null");
        cb.end();
        cb.newLine();

        Collections.sort(generatedClassNames);

        for (String generatedClassName : generatedClassNames) {
            JsonObject structureDefinition = getStructureDefinition(generatedClassName);
            generateXMLParseMethod(generatedClassName, structureDefinition, cb);
        }

        cb.method(mods("private"), "void", "stackPush", params("java.lang.String elementName", "int elementIndex"))
            ._if("elementIndex != -1")
                .invoke("stack", "push", args("elementName + \"[\" + elementIndex + \"]\""))
            ._else()
                .invoke("stack", "push", args("elementName"))
            ._end()
            ._if("DEBUG")
                .invoke("System.out", "println", args("getPath()"))
            ._end()
        .end().newLine();

        cb.method(mods("private"), "void", "stackPop")
            .invoke("stack", "pop", args())
        .end().newLine();

        cb.method(mods("private"), "java.lang.String", "getPath")
            .assign("StringJoiner joiner", "new StringJoiner(\".\")")
            ._foreach("java.lang.String s", "stack")
                .invoke("joiner", "add", args("s"))
            ._end()
            ._return("joiner.toString()")
        .end().newLine();

        cb.method(mods("private"), "java.lang.String", "parseJavaString", params("java.lang.String elementName", "XMLStreamReader reader", "int elementIndex"),
                throwsExceptions("XMLStreamException"))
            .invoke("stackPush", args("elementName", "elementIndex"))
            .assign("java.lang.String javaString", "reader.getAttributeValue(null, \"value\")")
            ._while("reader.hasNext()")
                .assign("int eventType", "reader.next()")
                ._switch("eventType")

                ._case("XMLStreamReader.START_ELEMENT")
                    .assign("java.lang.String localName", "reader.getLocalName()")
                    ._throw(_new("IllegalArgumentException", args("\"Unrecognized element: '\" + localName + \"'\"")))

                ._case("XMLStreamReader.END_ELEMENT")
                    ._if("reader.getLocalName().equals(elementName)")
                        .invoke("stackPop", args())
                        ._return("javaString")
                    ._end()
                    ._break()
                ._end()

            ._end()
            ._throw(_new("XMLStreamException", args(quote("Unexpected end of stream"))))
        .end()
        .newLine();

        cb.method(mods("private"), "java.lang.String", "getResourceType", params("XMLStreamReader reader"), throwsExceptions("XMLStreamException"))
            .assign("java.lang.String resourceType", "reader.getLocalName()")
            ._try()
                .invoke("ResourceType.ValueSet", "from", args("resourceType"))
            ._catch("IllegalArgumentException e")
                ._throw("new IllegalArgumentException(\"Invalid resource type: '\" + resourceType + \"'\")")
            ._end()
            ._return("resourceType")
        .end();

        cb._end();

        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/FHIRXMLParser.java");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                Files.createFile(file.toPath());
            }
        } catch (Exception e) {
            throw new Error(e);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cb.toString());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void generateXMLParseMethod(String generatedClassName, JsonObject structureDefinition, CodeBuilder cb) {
        if (isAbstract(structureDefinition)) {
            return;
        }

        if (isQuantitySubtype(structureDefinition)) {
            return;
        }

        if (isStringSubtype(generatedClassName) || isUriSubtype(generatedClassName) || isCodeSubtype(generatedClassName) || isIntegerSubtype(generatedClassName)) {
            return;
        }

        String path = titleCase(Arrays.asList(generatedClassName.split("\\.")).stream().map(s -> camelCase(s)).collect(Collectors.joining(".")));
        if (isPrimitiveType(structureDefinition)) {
            path = camelCase(path);
        }

        List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path);

        if ("Quantity".equals(generatedClassName)) {
            cb.method(mods("private"), "Quantity", "parseQuantity", params("Quantity.Builder builder", "java.lang.String elementName", "XMLStreamReader reader", "int elementIndex"), throwsExceptions("XMLStreamException"));
        } else if ("String".equals(generatedClassName) || "Uri".equals(generatedClassName) || "Integer".equals(generatedClassName)) {
            cb.method(mods("private"), generatedClassName, "parse" + generatedClassName, params(generatedClassName + ".Builder builder", "java.lang.String elementName", "XMLStreamReader reader", "int elementIndex"), throwsExceptions("XMLStreamException"));
        } else {
            cb.method(mods("private"), generatedClassName, "parse" + generatedClassName.replace(".", ""), params("java.lang.String elementName", "XMLStreamReader reader", "int elementIndex"), throwsExceptions("XMLStreamException"));
        }
        cb.invoke("stackPush", args("elementName", "elementIndex"));

        if (!"Quantity".equals(generatedClassName) && !"String".equals(generatedClassName) && !"Uri".equals(generatedClassName) && !"Integer".equals(generatedClassName)) {
            cb.assign(generatedClassName + ".Builder builder", generatedClassName + ".builder()");
        }

        if (typeClassNames.contains(generatedClassName) || generatedClassName.contains(".")) {
            cb.assign("java.lang.String id", "reader.getAttributeValue(null, \"id\")")
            ._if("id != null")
                .invoke("builder", "id", args("id"))
            ._end();
        }

        if ("Extension".equals(generatedClassName)) {
            cb.assign("java.lang.String url", "reader.getAttributeValue(null, \"url\")")
            ._if("url != null")
                .invoke("builder", "url", args("url"))
            ._end();
        }

        if (isPrimitiveType(structureDefinition)) {
            // primitive type
            if (isXhtml(structureDefinition)) {
                cb.invoke("builder", "value", args("parseDiv(reader)"));
                cb.invoke("stackPop", args());
                cb._return("builder.build()");
                cb.end().newLine();
                return;
            } else {
                cb.assign("java.lang.String value", "reader.getAttributeValue(null, \"value\")")
                ._if("value != null")
                    .invoke("builder", "value", args("value"))
                ._end();
            }
        }

        cb.assign("int position", "-1");
        generateElementIndexDeclarations(elementDefinitions, path, cb);

        cb._while("reader.hasNext()");

        cb.assign("int eventType", "reader.next()");
        cb._switch("eventType");

        cb._case("XMLStreamReader.START_ELEMENT");
        cb.assign("java.lang.String localName", "reader.getLocalName()");

        // check namespace
        if ("Narrative".equals(generatedClassName)) {
            cb._if("\"div\".equals(localName)")
                .invoke("requireNamespace", args("reader", "XHTML_NS_URI"))
            ._else()
                .invoke("requireNamespace", args("reader", "FHIR_NS_URI"))
            ._end();
        } else {
            cb.invoke("requireNamespace", args("reader", "FHIR_NS_URI"));
        }

        int orderIndex = 0;

        cb._switch("localName");
        for (JsonObject elementDefinition : elementDefinitions) {
            String elementName = getElementName(elementDefinition, path);

            if ((typeClassNames.contains(generatedClassName) || generatedClassName.contains(".")) && "id".equals(elementName)) {
                continue;
            }

            if ("Extension".equals(generatedClassName) && "url".equals(elementName)) {
                continue;
            }

            if (isPrimitiveType(structureDefinition) && "value".equals(elementName)) {
                continue;
            }

            String fieldName = getFieldName(elementName);
            String fieldType = getFieldType(structureDefinition, elementDefinition, false)
                    .replace("com.ibm.fhir.model.type.", "")
                    .replace("com.ibm.fhir.model.resource.", "");

            if (isBackboneElement(elementDefinition)) {
                fieldType = generatedClassName + "." + fieldType;
            }

            if (!isChoiceElement(elementDefinition)) {
                cb._case(quote(elementName));
                cb.assign("position", "checkElementOrder(" + quote(elementName) + ", " + orderIndex + ", position, " + isRepeating(elementDefinition) + ")");
                String parseMethodInvocation = buildParseMethodInvocation(elementDefinition, elementName, fieldType);
                cb.invoke("builder", fieldName, args(parseMethodInvocation));
                cb._break();
            } else {
                // generate choice element cases
                for (String choiceTypeName : getChoiceTypeNames(elementDefinition)) {
                    cb._case(quote(elementName + getConcreteTypeName(choiceTypeName)));
                    cb.assign("position", "checkElementOrder(" + quote(elementName + "[x]") + ", " + orderIndex + ", position, " + isRepeating(elementDefinition) + ")");
                    String parseMethodInvocation = buildParseMethodInvocation(elementDefinition, elementName + getConcreteTypeName(choiceTypeName), choiceTypeName);
                    cb.invoke("builder", fieldName, args(parseMethodInvocation));
                    cb._break();
                }
            }
            orderIndex++;
        }

        cb._default()
            ._throw(_new("IllegalArgumentException", args("\"Unrecognized element: '\" + localName + \"'\"")));

        cb._end();

        cb._break();

        cb._case("XMLStreamReader.END_ELEMENT")
            ._if("reader.getLocalName().equals(elementName)")
                .invoke("stackPop", args())
                ._return("builder.build()")
            ._end()
            ._break();

        cb._end();

        cb._end();

        cb._throw(_new("XMLStreamException", args(quote("Unexpected end of stream"))));

        cb.end();
        cb.newLine();

        if ("Quantity".equals(generatedClassName)) {
            cb.method(mods("private"), "Quantity", "parseQuantity", params("java.lang.String elementName", "XMLStreamReader reader", "int elementIndex"), throwsExceptions("XMLStreamException"));
            cb._return("parseQuantity(Quantity.builder(), elementName, reader, elementIndex)");
            cb.end().newLine();
        }

        if ("String".equals(generatedClassName) || "Uri".equals(generatedClassName) || "Integer".equals(generatedClassName)) {
            cb.method(mods("private"), generatedClassName, "parse" + generatedClassName, params("java.lang.String elementName", "XMLStreamReader reader", "int elementIndex"), throwsExceptions("XMLStreamException"))
                ._return("parse" + generatedClassName + "(" + generatedClassName + ".builder(), elementName, reader, elementIndex)")
            .end().newLine();
        }
    }

    private String getConcreteTypeName(String choiceTypeName) {
        if (isProfiledType(choiceTypeName)) {
            return "Quantity";
        }
        return choiceTypeName;
    }

    private void generateElementIndexDeclarations(List<JsonObject> elementDefinitions, String path, CodeBuilder cb) {
        StringJoiner joiner = new StringJoiner(" = 0, ");
        for (JsonObject elementDefinition : elementDefinitions) {
            if (isRepeating(elementDefinition)) {
                String elementName = getElementName(elementDefinition, path);
                joiner.add(elementName + "ElementIndex");
            }
        }
        if (!joiner.toString().isEmpty()) {
            cb.statement("int %s = 0", joiner.toString());
        }
    }

    private void generateJsonParser(String basePath) {
        CodeBuilder cb = new CodeBuilder();

        String packageName = "com.ibm.fhir.model.parser";
        cb.lines(HEADER).newLine();
        cb._package(packageName).newLine();

        cb._importstatic("com.ibm.fhir.model.util.JsonSupport", "checkForUnrecognizedElements");
        cb._importstatic("com.ibm.fhir.model.util.JsonSupport", "getJsonArray");
        cb._importstatic("com.ibm.fhir.model.util.JsonSupport", "getJsonValue");
        cb._importstatic("com.ibm.fhir.model.util.JsonSupport", "getResourceType");
        cb._importstatic("com.ibm.fhir.model.util.JsonSupport", "nonClosingInputStream");
        cb._importstatic("com.ibm.fhir.model.util.JsonSupport", "nonClosingReader");
        cb._importstatic("com.ibm.fhir.model.util.ModelSupport", "getChoiceElementName");
        cb.newLine();

        cb._import("java.io.InputStream");
        cb._import("java.io.Reader");
        cb._import("java.nio.charset.StandardCharsets");
        cb._import("java.util.Collection");
        cb._import("java.util.Stack");
        cb._import("java.util.StringJoiner");
        cb.newLine();

        cb._import("javax.annotation.Generated");
        cb._import("javax.json.Json");
        cb._import("javax.json.JsonArray");
        cb._import("javax.json.JsonNumber");
        cb._import("javax.json.JsonObject");
        cb._import("javax.json.JsonReader");
        cb._import("javax.json.JsonReaderFactory");
        cb._import("javax.json.JsonString");
        cb._import("javax.json.JsonValue");
        cb.newLine();

        cb._import("com.ibm.fhir.model.parser.FHIRParser");
        cb._import("com.ibm.fhir.model.parser.FHIRAbstractParser");
        cb._import("com.ibm.fhir.model.parser.exception.FHIRParserException");
        cb._import("com.ibm.fhir.model.resource.*");
        cb._import("com.ibm.fhir.model.type.*");
        cb._import("com.ibm.fhir.model.type.code.*");
        cb._import("com.ibm.fhir.model.type.Boolean");
        cb._import("com.ibm.fhir.model.type.Integer");
        cb._import("com.ibm.fhir.model.type.String");
        cb._import("com.ibm.fhir.model.util.ElementFilter");
        cb.newLine();

        cb._import("net.jcip.annotations.NotThreadSafe");
        cb.newLine();

        cb.annotation("NotThreadSafe");
        cb.annotation("Generated", quote("com.ibm.fhir.tools.CodeGenerator"));
        cb._class(mods("public"), "FHIRJsonParser", "FHIRAbstractParser");
        cb.field(mods("public", "static"), "boolean", "DEBUG", "false");
        cb.field(mods("private", "static", "final"), "JsonReaderFactory", "JSON_READER_FACTORY", "Json.createReaderFactory(null)");
        cb.newLine();

        cb.field(mods("private", "final"), "Stack<java.lang.String>", "stack", _new("Stack<>"));
        cb.newLine();

        cb.constructor(mods(), "FHIRJsonParser");
        cb.comment("only visible to subclasses or classes/interfaces in the same package (e.g. FHIRParser)");
        cb.end();
        cb.newLine();

        // public <T extends Resource> T parse(InputStream in) throws FHIRException
        cb.override();
        cb.method(mods("public"), "<T extends Resource> T", "parse", params("InputStream in"), throwsExceptions("FHIRParserException"))
            ._return("parseAndFilter(in, null)")
        .end();
        cb.newLine();

        // public <T extends Resource> T parseAndFilter(InputStream in, java.util.List<java.lang.String> elementsToInclude) throws FHIRException
        cb.method(mods("public"), "<T extends Resource> T", "parseAndFilter", params("InputStream in", "Collection<java.lang.String> elementsToInclude"), throwsExceptions("FHIRParserException"))
            ._try("JsonReader jsonReader = JSON_READER_FACTORY.createReader(nonClosingInputStream(in), StandardCharsets.UTF_8)")
                .assign("JsonObject jsonObject", "jsonReader.readObject()")
                ._return("parseAndFilter(jsonObject, elementsToInclude)")
            ._catch("FHIRParserException e")
                ._throw("e")
            ._catch("Exception e")
                ._throw("new FHIRParserException(e.getMessage(), getPath(), e)")
            ._end()
        .end();
        cb.newLine();

        // public <T extends Resource> T parse(Reader reader) throws FHIRException
        cb.override();
        cb.method(mods("public"), "<T extends Resource> T", "parse", params("Reader reader"), throwsExceptions("FHIRParserException"))
            ._return("parseAndFilter(reader, null)")
        .end();
        cb.newLine();

        // public <T extends Resource> T parseAndFilter(Reader reader, java.util.List<java.lang.String> elementsToInclude) throws FHIRException
        cb.method(mods("public"), "<T extends Resource> T", "parseAndFilter", params("Reader reader", "Collection<java.lang.String> elementsToInclude"), throwsExceptions("FHIRParserException"))
            ._try("JsonReader jsonReader = JSON_READER_FACTORY.createReader(nonClosingReader(reader))")
                .assign("JsonObject jsonObject", "jsonReader.readObject()")
                ._return("parseAndFilter(jsonObject, elementsToInclude)")
            ._catch("FHIRParserException e")
                ._throw("e")
            ._catch("Exception e")
                ._throw("new FHIRParserException(e.getMessage(), getPath(), e)")
            ._end()
        .end();
        cb.newLine();

        cb.method(mods("public"), "<T extends Resource> T", "parse", args("JsonObject jsonObject"), throwsExceptions("FHIRParserException"))
            ._return("parseAndFilter(jsonObject, null)")
        .end();
        cb.newLine();

        // public <T extends Resource> T parseAndFilter(JsonObject jsonObject, java.util.List<java.lang.String> elementsToInclude)
        cb.annotation("SuppressWarnings", quote("unchecked"));
        cb.method(mods("public"), "<T extends Resource> T", "parseAndFilter", params("JsonObject jsonObject", "Collection<java.lang.String> elementsToInclude"), throwsExceptions("FHIRParserException"))
            ._try()
                .invoke("reset", args())
                .assign("Class<?> resourceType", "getResourceType(jsonObject)")
                ._if("elementsToInclude != null")
                    .assign("ElementFilter elementFilter", "new ElementFilter(resourceType, elementsToInclude)")
                    .assign("jsonObject", "elementFilter.apply(jsonObject)")
                ._end()
                ._return("(T) parseResource(resourceType.getSimpleName(), jsonObject, -1)")
            ._catch("Exception e")
                ._throw("new FHIRParserException(e.getMessage(), getPath(), e)")
            ._end()
        .end();
        cb.newLine();

        cb.method(mods("private"), "void", "reset")
            .invoke("stack", "clear", args())
        .end();
        cb.newLine();

        cb.override();
        cb.method(mods("public"), "boolean", "isPropertySupported", params("java.lang.String name"))
            ._if("FHIRParser.PROPERTY_IGNORE_UNRECOGNIZED_ELEMENTS.equals(name)")
                ._return("true")
            ._end()
            ._return("false")
        .end();
        cb.newLine();

        cb.method(mods("private"), "Resource", "parseResource", params("java.lang.String elementName", "JsonObject jsonObject", "int elementIndex"));
        cb._if("jsonObject == null");
        cb._return("null");
        cb._end();
        cb.assign("Class<?> resourceType", "getResourceType(jsonObject)");
        cb._switch("resourceType.getSimpleName()");
        for (String resourceClassName : resourceClassNames) {
            if ("Resource".equals(resourceClassName) || "DomainResource".equals(resourceClassName)) {
                continue;
            }
            cb._case(quote(resourceClassName));
            cb._return("parse" + resourceClassName + "(elementName, jsonObject, elementIndex)");
        }
        cb._end();
        cb._return("null");
        cb.end();
        cb.newLine();

        Collections.sort(generatedClassNames);

        for (String generatedClassName : generatedClassNames) {
            JsonObject structureDefinition = getStructureDefinition(generatedClassName);
            if (isPrimitiveType(structureDefinition)) {
                generatePrimitiveTypeParseMethod(generatedClassName, structureDefinition, cb);
            } else {
                generateParseMethod(generatedClassName, structureDefinition, cb);
            }
        }

        cb.method(mods("private"), "void", "stackPush", params("java.lang.String elementName", "int elementIndex"))
            ._if("elementIndex != -1")
                .invoke("stack", "push", args("elementName + \"[\" + elementIndex + \"]\""))
            ._else()
                .invoke("stack", "push", args("elementName"))
            ._end()
            ._if("DEBUG")
                .invoke("System.out", "println", args("getPath()"))
            ._end()
        .end().newLine();

        cb.method(mods("private"), "void", "stackPop")
            .invoke("stack", "pop", args())
        .end().newLine();

        generateParseChoiceElementMethod(cb);

        cb.method(mods("private"), "java.lang.String", "getPath")
            .assign("StringJoiner joiner", "new StringJoiner(\".\")")
            ._foreach("java.lang.String s", "stack")
                .invoke("joiner", "add", args("s"))
            ._end()
            ._return("joiner.toString()")
        .end().newLine();

        cb.method(mods("private"), "java.lang.String", "parseJavaString", params("java.lang.String elementName", "JsonString jsonString", "int elementIndex"))
            ._if("jsonString == null")
                ._return("null")
            ._end()
            .invoke("stackPush", args("elementName", "elementIndex"))
            .assign("java.lang.String javaString", "jsonString.getString()")
            .invoke("stackPop", args())
            ._return("javaString")
        .end();

        cb._end();

        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/FHIRJsonParser.java");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                Files.createFile(file.toPath());
            }
        } catch (Exception e) {
            throw new Error(e);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cb.toString());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void generateParseChoiceElementMethod(CodeBuilder cb) {
        cb.method(mods("private"), "Element", "parseChoiceElement", params("java.lang.String name", "JsonObject jsonObject", "Class<?>... choiceTypes"));
        cb._if("jsonObject == null")
            ._return("null")
        ._end();

        cb.newLine();

        cb.assign("java.lang.String elementName", "null");
        cb.assign("java.lang.String _elementName", "null");
        cb.assign("Class<?> elementType", "null");

        cb.newLine();

        cb._foreach("Class<?> choiceType", "choiceTypes")
            .assign("java.lang.String key", "getChoiceElementName(name, choiceType)")
            ._if("jsonObject.containsKey(key)")
                ._if("elementName != null")
                    ._throw("new IllegalArgumentException()")
                ._end()
                .assign("elementName", "key")
                .assign("elementType", "choiceType")
            ._end()

            .newLine()

            .assign("java.lang.String _key", "\"_\" + key")
            ._if("jsonObject.containsKey(_key)")
                ._if("_elementName != null")
                    ._throw("new IllegalArgumentException()")
                ._end()
                .assign("_elementName", "_key")
                ._if("elementType == null")
                    .assign("elementType", "choiceType")
                ._end()
            ._end()
        ._end();

        cb.newLine();

        cb._if("elementName != null && _elementName != null && !_elementName.endsWith(elementName)")
            ._throw("new IllegalArgumentException()")
        ._end();

        cb.newLine();

        cb.assign("JsonValue jsonValue", "null");
        cb._if("elementName != null")
            .assign("jsonValue", "jsonObject.get(elementName)")
        ._end();

        cb.newLine();

        cb.assign("JsonValue _jsonValue", "null");
        cb._if("_elementName != null")
            .assign("_jsonValue", "jsonObject.get(_elementName)")
        ._end();

        cb.newLine();

        cb._if("elementType != null");
        cb._switch("elementType.getSimpleName()");
        for (String dataTypeName : DATA_TYPE_NAMES) {
            cb._case(quote(dataTypeName));
            if (isStringSubtype(dataTypeName)) {
                cb._return("parseString(" + dataTypeName + ".builder(), elementName, jsonValue, _jsonValue, -1)");
            } else if (isUriSubtype(dataTypeName)) {
                cb._return("parseUri(" + dataTypeName + ".builder(), elementName, jsonValue, _jsonValue, -1)");
            } else if (isIntegerSubtype(dataTypeName)) {
                cb._return("parseInteger(" + dataTypeName + ".builder(), elementName, jsonValue, _jsonValue, -1)");
            } else if (isQuantitySubtype(dataTypeName)) {
                cb._return("parseQuantity(" + dataTypeName + ".builder(), elementName, (JsonObject) jsonValue, -1)");
            } else if (isPrimitiveType(dataTypeName)) {
                cb._return("parse" + dataTypeName + "(elementName, jsonValue, _jsonValue, -1)");
            } else {
                cb._return("parse" + dataTypeName + "(elementName, (JsonObject) jsonValue, -1)");
            }
        }
        cb._end();
        cb._end();

        cb.newLine();

        cb._return("null");

        cb.end().newLine();
    }

    private void generateParseMethod(String generatedClassName, JsonObject structureDefinition, CodeBuilder cb) {
        if (isQuantitySubtype(structureDefinition)) {
            return;
        }

        String path = titleCase(Arrays.asList(generatedClassName.split("\\.")).stream().map(s -> camelCase(s)).collect(Collectors.joining(".")));

        List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path);

        if (isAbstract(structureDefinition)) {
            cb.method(mods("private"), "void", "parse" + generatedClassName.replace(".", ""), params(generatedClassName + ".Builder builder", "JsonObject jsonObject"));
        } else {
            if ("Quantity".equals(generatedClassName)) {
                cb.method(mods("private"), "Quantity", "parseQuantity", params("Quantity.Builder builder", "java.lang.String elementName", "JsonObject jsonObject", "int elementIndex"));
            } else {
                cb.method(mods("private"), generatedClassName, "parse" + generatedClassName.replace(".", ""), params("java.lang.String elementName", "JsonObject jsonObject", "int elementIndex"));
            }
            cb._if("jsonObject == null")
                ._return("null")
            ._end();
            cb.invoke("stackPush", args("elementName", "elementIndex"));
            cb._if("getPropertyOrDefault(FHIRParser.PROPERTY_IGNORE_UNRECOGNIZED_ELEMENTS, java.lang.Boolean.FALSE, java.lang.Boolean.class) == false")
                .invoke("checkForUnrecognizedElements", args(generatedClassName + ".class", "jsonObject"))
            ._end();
        }

        if (!isAbstract(structureDefinition) && !"Quantity".equals(generatedClassName)) {
            cb.assign(generatedClassName + ".Builder builder", generatedClassName + ".builder()");
        }

        String superClass = superClassMap.get(generatedClassName);
        if (superClass != null) {
            cb.invoke("parse" + superClass, args("builder", "jsonObject"));
        }

        for (JsonObject elementDefinition : elementDefinitions) {
            String basePath = elementDefinition.getJsonObject("base").getString("path");
            if (elementDefinition.getString("path").equals(basePath)) {
                String elementName = getElementName(elementDefinition, path);
                String fieldName = getFieldName(elementName);
                String fieldType = getFieldType(structureDefinition, elementDefinition, false)
                        .replace("com.ibm.fhir.model.type.", "")
                        .replace("com.ibm.fhir.model.resource.", "");

                if (isBackboneElement(elementDefinition)) {
                    fieldType = generatedClassName + "." + fieldType;
                }

                String parseMethodInvocation;
                if (isRepeating(elementDefinition)) {
                    if (isPrimitiveType(fieldType) || isCodeSubtype(fieldType)) {
                        cb.assign("JsonArray " + elementName + "Array", "getJsonArray(jsonObject, " + quote(elementName) + ", true)");
                    } else {
                        cb.assign("JsonArray " + elementName + "Array", "getJsonArray(jsonObject, " + quote(elementName) + ")");
                    }
                    cb._if(elementName + "Array != null");
                    if (isPrimitiveType(fieldType) || isCodeSubtype(fieldType)) {
                        cb.assign("JsonArray _" + elementName + "Array", "jsonObject.getJsonArray(" + quote("_" + elementName) + ")");
                    }
                    cb._for("int i = 0", "i < " + elementName + "Array.size()", "i++");
                    parseMethodInvocation = buildParseMethodInvocation(elementDefinition, elementName, fieldType, true);
                    cb.invoke("builder", fieldName, args(parseMethodInvocation));
                    cb._end();
                    cb._end();
                } else {
                    parseMethodInvocation = buildParseMethodInvocation(elementDefinition, elementName, fieldType, false);
                    cb.invoke("builder", fieldName, args(parseMethodInvocation));
                }
            }
        }

        if (!isAbstract(structureDefinition)) {
            cb.invoke("stackPop", args());
            cb._return("builder.build()");
        }

        cb.end().newLine();

        if ("Quantity".equals(generatedClassName)) {
            cb.method(mods("private"), "Quantity", "parseQuantity", params("java.lang.String elementName", "JsonObject jsonObject", "int elementIndex"));
            cb._return("parseQuantity(Quantity.builder(), elementName, jsonObject, elementIndex)");
            cb.end().newLine();
        }
    }

    private void generatePrimitiveTypeParseMethod(String generatedClassName, JsonObject structureDefinition, CodeBuilder cb) {
        if (isStringSubtype(generatedClassName) || isUriSubtype(generatedClassName) || isCodeSubtype(generatedClassName) || isIntegerSubtype(generatedClassName)) {
            return;
        }

        if ("String".equals(generatedClassName) || "Uri".equals(generatedClassName) || "Integer".equals(generatedClassName)) {
            cb.method(mods("private"), generatedClassName, "parse" + generatedClassName, params(generatedClassName + ".Builder builder", "java.lang.String elementName", "JsonValue jsonValue", "JsonValue _jsonValue", "int elementIndex"));
        } else {
            cb.method(mods("private"), generatedClassName, "parse" + generatedClassName, params("java.lang.String elementName", "JsonValue jsonValue", "JsonValue _jsonValue", "int elementIndex"));
        }

        cb._if("jsonValue == null && _jsonValue == null")
            ._return("null")
        ._end();

        cb.invoke("stackPush", args("elementName", "elementIndex"));

        if (!"String".equals(generatedClassName) && !"Uri".equals(generatedClassName) && !"Integer".equals(generatedClassName)) {
            cb.assign(generatedClassName + ".Builder builder", generatedClassName + ".builder()");
        }

        cb._if("_jsonValue != null && _jsonValue.getValueType() == JsonValue.ValueType.OBJECT")
            .assign("JsonObject jsonObject", "(JsonObject) _jsonValue")
            ._if("getPropertyOrDefault(FHIRParser.PROPERTY_IGNORE_UNRECOGNIZED_ELEMENTS, java.lang.Boolean.FALSE, java.lang.Boolean.class) == false")
                .invoke("checkForUnrecognizedElements", args("Element.class", "jsonObject"))
            ._end()
            .invoke("parseElement", args("builder", "jsonObject"))
        ._end();

        if ("Integer".equals(generatedClassName)) {
            cb._if("jsonValue != null && jsonValue.getValueType() == JsonValue.ValueType.NUMBER")
                .assign("JsonNumber jsonNumber", "(JsonNumber) jsonValue")
                .invoke("builder", "value", args("jsonNumber.intValueExact()"))
            ._end();
        }

        if ("String".equals(generatedClassName) ||
                "Uri".equals(generatedClassName) ||
                "Date".equals(generatedClassName) ||
                "DateTime".equals(generatedClassName) ||
                "Time".equals(generatedClassName) ||
                "Instant".equals(generatedClassName) ||
                "Base64Binary".equals(generatedClassName) ||
                "Xhtml".equals(generatedClassName)) {
            cb._if("jsonValue != null && jsonValue.getValueType() == JsonValue.ValueType.STRING")
                .assign("JsonString jsonString", "(JsonString) jsonValue")
                .invoke("builder", "value", args("jsonString.getString()"))
            ._end();
        }

        if ("Boolean".equals(generatedClassName)) {
            cb._if("JsonValue.TRUE.equals(jsonValue) || JsonValue.FALSE.equals(jsonValue)")
                .invoke("builder", "value", args("JsonValue.TRUE.equals(jsonValue) ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE"))
            ._end();
        }

        if ("Decimal".equals(generatedClassName)) {
            cb._if("jsonValue != null && jsonValue.getValueType() == JsonValue.ValueType.NUMBER")
                .assign("JsonNumber jsonNumber", "(JsonNumber) jsonValue")
                .invoke("builder", "value", args("jsonNumber.bigDecimalValue()"))
            ._end();
        }

        cb.invoke("stackPop", args());
        cb._return("builder.build()");
        cb.end().newLine();

        if ("String".equals(generatedClassName) || "Uri".equals(generatedClassName) || "Integer".equals(generatedClassName)) {
            cb.method(mods("private"), generatedClassName, "parse" + generatedClassName, params("java.lang.String elementName", "JsonValue jsonValue", "JsonValue _jsonValue", "int elementIndex"))
                ._return("parse" + generatedClassName + "(" + generatedClassName + ".builder(), elementName, jsonValue, _jsonValue, elementIndex)")
            .end().newLine();
        }
    }

    private void generateCodeSubtypeClasses(JsonObject structureDefinition, String basePath) {
        for (JsonObject elementDefinition : getElementDefinitions(structureDefinition, true)) {
            List<JsonObject> types = getTypes(elementDefinition);
            if (!types.isEmpty()) {
                String typeName = types.get(0).getString("code", null);
                if (!"code".equals(typeName)) {
                    continue;
                }
            }

            if (hasRequiredBinding(elementDefinition)) {
                JsonObject binding = getBinding(elementDefinition);
                String bindingName = getBindingName(binding);

                if ("messageheader-response-request".equals(bindingName)) {
                    bindingName = "MessageHeaderResponseRequest";
                } else if ("NutritiionOrderIntent".equals(bindingName)) {
                    bindingName = "NutritionOrderIntent";
                }

                bindingName = titleCase(bindingName);

                String valueSet = binding.getString("valueSet");
                String[] tokens = valueSet.split("\\|");
                valueSet = tokens[0];

                generateCodeSubtypeClass(bindingName, valueSet, basePath);
            }
        }
    }

    public void generateCodeSubtypeClass(String bindingName, String valueSet, String basePath) {
        CodeBuilder cb = new CodeBuilder();
        String packageName = "com.ibm.fhir.model.type.code";
        cb.lines(HEADER).newLine();
        cb._package(packageName).newLine();

        cb._import("com.ibm.fhir.model.annotation.System");
        cb._import("com.ibm.fhir.model.type.Code");
        cb._import("com.ibm.fhir.model.type.Extension");
        cb._import("com.ibm.fhir.model.type.String").newLine();

        cb._import("java.util.Collection");
        cb._import("java.util.Objects").newLine();

        cb._import("javax.annotation.Generated").newLine();

        String system = getSystem(valueSet);
        cb.annotation("System", quote(system));
        cb.annotation("Generated", quote("com.ibm.fhir.tools.CodeGenerator"));
        cb._class(mods("public"), bindingName, "Code");

        List<JsonObject> concepts = getConcepts(valueSet);
        for (JsonObject concept : concepts) {
            String value = concept.getString("code");
            String enumConstantName = getEnumConstantName(bindingName, value);
            generateConceptJavadoc(concept, cb);
            cb.field(mods("public", "static", "final"), bindingName, enumConstantName, bindingName + ".builder().value(ValueSet." + enumConstantName + ").build()")
                .newLine();
        }

        cb.field(mods("private", "volatile"), "int", "hashCode").newLine();

        cb.constructor(mods("private"), bindingName, params("Builder builder"))
            ._super(args("builder"))
        .end().newLine();

        cb.method(mods("public"), "ValueSet", "getValueAsEnumConstant")
            ._return("(value != null) ? ValueSet.from(value) : null")
        .end().newLine();

        cb.method(mods("public", "static"), bindingName, "of", args("ValueSet value"))
            ._switch("value");
            for (JsonObject concept : concepts) {
                String value = concept.getString("code");
                String enumConstantName = getEnumConstantName(bindingName, value);
                cb._case(enumConstantName)
                    ._return(enumConstantName);
            }
            cb._default()
                ._throw(_new("IllegalStateException", args("value.name()")));
            cb.end();
        cb.end().newLine();

        cb.method(mods("public", "static"), bindingName, "of", args("java.lang.String value"))
            ._return("of(ValueSet.from(value))")
        .end().newLine();

        cb.method(mods("public", "static"), "String", "string", args("java.lang.String value"))
            ._return("of(ValueSet.from(value))")
        .end().newLine();

        cb.method(mods("public", "static"), "Code", "code", args("java.lang.String value"))
            ._return("of(ValueSet.from(value))")
        .end().newLine();

        cb.override();
        cb.method(mods("public"), "boolean", "equals", params("Object obj"));
        cb._if("this == obj")
            ._return("true")
        ._end();
        cb._if("obj == null")
            ._return("false")
        ._end();
        cb._if("getClass() != obj.getClass()")
            ._return("false")
        ._end();
        cb.assign(bindingName + " other", "(" + bindingName + ") obj");
        cb._return("Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value)");
        cb.end().newLine();

        cb.override();
        cb.method(mods("public"), "int", "hashCode");
        cb.assign("int result", "hashCode");
        cb._if("result == 0");
        cb.assign("result", "Objects.hash(id, extension, value)");
        cb.assign("hashCode", "result");
        cb._end();
        cb._return("result");
        cb.end().newLine();

        cb.method(mods("public"), "Builder", "toBuilder")
            .assign("Builder builder", "new Builder()")
            .invoke("builder", "id", args("id"))
            .invoke("builder", "extension", args("extension"))
            .invoke("builder", "value", args("value"))
            ._return("builder")
        .end().newLine();

        cb.method(mods("public", "static"), "Builder", "builder")
            ._return(_new("Builder"))
        .end().newLine();

        cb._class(mods("public", "static"), "Builder", "Code.Builder");

        cb.constructor(mods("private"), "Builder")
            ._super()
        .end().newLine();

        cb.override();
        cb.method(mods("public"), "Builder", "id", args("java.lang.String id"))
            ._return("(Builder) super.id(id)")
        .end().newLine();

        cb.override();
        cb.method(mods("public"), "Builder", "extension", args("Extension... extension"))
            ._return("(Builder) super.extension(extension)")
        .end().newLine();

        cb.override();
        cb.method(mods("public"), "Builder", "extension", args("Collection<Extension> extension"))
            ._return("(Builder) super.extension(extension)")
        .end().newLine();

        cb.override();
        cb.method(mods("public"), "Builder", "value", args("java.lang.String value"))
            ._return("(value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this")
        .end().newLine();

        cb.method(mods("public"), "Builder", "value", args("ValueSet value"))
            ._return("(value != null) ? (Builder) super.value(value.value()) : this")
        .end().newLine();

        cb.override();
        cb.method(mods("public"), bindingName, "build")
            ._return(_new(bindingName, args("this")))
        .end();

        cb._end().newLine();

        String definition = getValueSetDefinition(valueSet);
        if (definition != null) {
            cb.javadoc(definition);
        }
        cb._enum(mods("public"), "ValueSet");


        for (JsonObject concept : concepts) {
            String value = concept.getString("code");
            String enumConstantName = getEnumConstantName(bindingName, value);
            generateConceptJavadoc(concept, cb);
            cb.enumConstant(enumConstantName, args(quote(value)), isLast(concepts, concept)).newLine();
        }

        cb.field(mods("private", "final"), "java.lang.String", "value").newLine();

        cb.constructor(mods(), "ValueSet", args("java.lang.String value"))
            .assign(_this("value"), "value")
        .end().newLine();

        cb.method(mods("public"), "java.lang.String", "value")
            ._return("value")
        .end().newLine();

        cb.method(mods("public", "static"), "ValueSet", "from", params("java.lang.String value"))
            ._foreach("ValueSet c", "ValueSet.values()")
                ._if("c.value.equals(value)")
                    ._return("c")
                ._end()
            ._end()
            ._throw(_new("IllegalArgumentException", args("value")))
        .end();

        cb._end();

        cb._end();


        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/" + bindingName + ".java");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                Files.createFile(file.toPath());
            }
        } catch (Exception e) {
            throw new Error(e);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cb.toString());
        } catch (Exception e) {
            throw new Error(e);
        }

        codeSubtypeClassNames.add(bindingName);
    }

    private void generateConceptJavadoc(JsonObject concept, CodeBuilder cb) {
        String display = getDisplay(concept);
        String definition = getDefinition(concept);

        StringBuilder sb = new StringBuilder();

        if (display != null) {
            sb.append(display);
        }

        if (definition != null) {
            if (display != null) {
                sb.append(System.lineSeparator()).append(System.lineSeparator());
            }
            sb.append(definition);
        }

        if (display != null || definition != null) {
            cb.javadoc(Arrays.asList(sb.toString().split(System.lineSeparator())), true, true, true);
        }
    }

    private void generateVisitorInterface(String basePath) {
        CodeBuilder cb = new CodeBuilder();

        String packageName = "com.ibm.fhir.model.visitor";
        cb.lines(HEADER).newLine();
        cb._package(packageName).newLine();

        cb._import("java.math.BigDecimal");
        cb._import("java.time.LocalDate");
        cb._import("java.time.LocalTime");
        cb._import("java.time.Year");
        cb._import("java.time.YearMonth");
        cb._import("java.time.ZonedDateTime");
        cb._import("javax.annotation.Generated");

        cb.newLine();

        cb._import("com.ibm.fhir.model.resource.*");
        cb._import("com.ibm.fhir.model.type.*");
        cb._import("com.ibm.fhir.model.type.Boolean");
        cb._import("com.ibm.fhir.model.type.Integer");
        cb._import("com.ibm.fhir.model.type.String");
        cb.newLine();

        cb.javadocStart();
        cb.javadoc("Visitor interface for visiting FHIR model objects that implement Visitable.");
        cb.javadoc("");
        cb.javadoc("Each model object can accept a visitor and contains logic for invoking the corresponding visit method for itself and all its members.");
        cb.javadoc("");
        cb.javadoc("At each level, the visitor can control traversal by returning true or false as indicated in the following snippet:");
        cb.javadoc("<pre>", false);
        cb.javadoc("if (visitor.preVisit(this)) {");
        cb.javadoc("    visitor.visitStart(elementName, elementIndex, this);");
        cb.javadoc("    if (visitor.visit(elementName, elementIndex, this)) {");
        cb.javadoc("        // visit children");
        cb.javadoc("    }");
        cb.javadoc("    visitor.visitEnd(elementName, elementIndex, this);");
        cb.javadoc("    visitor.postVisit(this);");
        cb.javadoc("}");
        cb.javadoc("</pre>", false);
        cb.javadocEnd();
        cb.annotation("Generated", quote("com.ibm.fhir.tools.CodeGenerator"));
        cb._interface(mods("public"), "Visitor");

        cb.javadocStart();
        cb.javadoc("@return true if this Element should be visited; otherwise false");
        cb.javadocEnd();
        cb.abstractMethod(mods(), "boolean", "preVisit", params("Element element")).newLine();
        cb.javadocStart();
        cb.javadoc("@return true if this Resource should be visited; otherwise false");
        cb.javadocEnd();
        cb.abstractMethod(mods(), "boolean", "preVisit", params("Resource resource")).newLine();

        cb.abstractMethod(mods(), "void", "postVisit", params("Element element"));
        cb.abstractMethod(mods(), "void", "postVisit", params("Resource resource"));
        cb.newLine();
        cb.abstractMethod(mods(), "void", "visitStart", params("java.lang.String elementName", "int elementIndex", "Element element"));
        cb.abstractMethod(mods(), "void", "visitStart", params("java.lang.String elementName", "int elementIndex", "Resource resource"));
        cb.abstractMethod(mods(), "void", "visitStart", params("java.lang.String elementName", "java.util.List<? extends Visitable> visitables", "Class<?> type"));
        cb.newLine();
        cb.abstractMethod(mods(), "void", "visitEnd", params("java.lang.String elementName", "int elementIndex", "Element element"));
        cb.abstractMethod(mods(), "void", "visitEnd", params("java.lang.String elementName", "int elementIndex", "Resource resource"));
        cb.abstractMethod(mods(), "void", "visitEnd", params("java.lang.String elementName", "java.util.List<? extends Visitable> visitables", "Class<?> type"));
        cb.newLine();

        cb.javadocStart();
        cb.javadocReturn("true if the children of this visitable should be visited; otherwise false");
        cb.javadocEnd();
        cb.abstractMethod(mods(), "boolean", "visit", params("java.lang.String elementName", "int elementIndex", "Visitable visitable"));

        Collections.sort(generatedClassNames);

        for (String className : generatedClassNames) {
            if (isNestedType(className)) {
                continue;
            }
            String paramName = camelCase(className).replace(".", "");
            if (SourceVersion.isKeyword(paramName)) {
                paramName = "_" + paramName;
            }
            cb.javadocStart();
            cb.javadocReturn("true if the children of this " + paramName + " should be visited; otherwise false");
            cb.javadocEnd();
            cb.abstractMethod(mods(), "boolean", "visit", params("java.lang.String elementName", "int elementIndex", className + " " + paramName));
            cb.newLine();
        }

        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "byte[] value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "BigDecimal value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "java.lang.Boolean value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "java.lang.Integer value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "LocalDate value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "LocalTime value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "java.lang.String value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "Year value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "YearMonth value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "ZonedDateTime value"));

        // Added to Process Location.Position.
        cb.javadocStart();
        cb.javadocReturn("true if the children of this Location.Position should be visited; otherwise false");
        cb.javadocEnd();
        cb.abstractMethod(mods(), "boolean", "visit", params("java.lang.String elementName", "int elementIndex", "Location.Position position"));

        cb._end();

        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/Visitor.java");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                Files.createFile(file.toPath());
            }
        } catch (Exception e) {
            throw new Error(e);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cb.toString());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private List<String> getBackboneElementPaths(JsonObject structureDefinition) {
        List<String> paths = new ArrayList<>();
        for (JsonObject elementDefinition : getElementDefinitions(structureDefinition)) {
            if (isBackboneElement(elementDefinition)) {
                paths.add(elementDefinition.getString("path"));
            }
        }
        return paths;
    }

    private JsonObject getBaseDefinition(JsonObject structureDefinition) {
        String baseDefinition = structureDefinition.getString("baseDefinition", null);
        if (baseDefinition != null) {
            return structureDefinitionMap.get(baseDefinition.substring(baseDefinition.lastIndexOf("/") + 1));
        }
        return null;
    }

    private JsonObject getBinding(JsonObject elementDefinition) {
        return elementDefinition.getJsonObject("binding");
    }

    private String getBindingName(JsonObject binding) {
        for (JsonValue extension : binding.getOrDefault("extension", JsonArray.EMPTY_JSON_ARRAY).asJsonArray()) {
            if (extension.asJsonObject().getString("url").endsWith("bindingName")) {
                return extension.asJsonObject().getString("valueString");
            }
        }
        return null;
    }

    private String getInheritedExtensibleValueSet(JsonObject binding) {
        for (JsonValue extension : binding.getOrDefault("extension", JsonArray.EMPTY_JSON_ARRAY).asJsonArray()) {
            if (extension.asJsonObject().getString("url").endsWith("inheritedExtensibleValueSet")) {
                String valueUri = extension.asJsonObject().getString("valueUri", null);
                String valueCanonical = extension.asJsonObject().getString("valueCanonical", null);
                if (valueUri != null) {
                    return valueUri;
                } else if (valueCanonical != null) {
                    return valueCanonical;
                }
            }
        }
        return null;
    }

    private String getMinValueSet(JsonObject binding) {
        for (JsonValue extension : binding.getOrDefault("extension", JsonArray.EMPTY_JSON_ARRAY).asJsonArray()) {
            if (extension.asJsonObject().getString("url").endsWith("minValueSet")) {
                String valueUri = extension.asJsonObject().getString("valueUri", null);
                String valueCanonical = extension.asJsonObject().getString("valueCanonical", null);
                if (valueUri != null) {
                    return valueUri;
                } else if (valueCanonical != null) {
                    return valueCanonical;
                }
            }
        }
        return null;
    }

    private String getMaxValueSet(JsonObject binding) {
        for (JsonValue extension : binding.getOrDefault("extension", JsonArray.EMPTY_JSON_ARRAY).asJsonArray()) {
            if (extension.asJsonObject().getString("url").endsWith("maxValueSet")) {
                String valueUri = extension.asJsonObject().getString("valueUri", null);
                String valueCanonical = extension.asJsonObject().getString("valueCanonical", null);
                if (valueUri != null) {
                    return valueUri;
                } else if (valueCanonical != null) {
                    return valueCanonical;
                }
            }
        }
        return null;
    }

    private List<JsonObject> getConcepts(String url) {
        List<JsonObject> concepts = new ArrayList<>();
        JsonObject valueSet = valueSetMap.get(url);
        if (valueSet != null) {
            JsonObject compose = valueSet.getJsonObject("compose");
            for (JsonValue include : compose.getJsonArray("include")) {
                if (include.asJsonObject().containsKey("concept")) {
                    concepts.addAll(getConcepts(include.asJsonObject().getJsonArray("concept")));
                } else {
                    String system = include.asJsonObject().getString("system");
                    JsonObject codeSystem = codeSystemMap.get(system);
                    if (codeSystem != null) {
                        concepts.addAll(getConcepts(codeSystem.getJsonArray(("concept"))));
                    }
                }
            }
        }
        return concepts;
    }

    private String getSystem(String url) {
        JsonObject valueSet = valueSetMap.get(url);
        if (valueSet != null) {
            JsonObject compose = valueSet.getJsonObject("compose");
            for (JsonValue include : compose.getJsonArray("include")) {
                return include.asJsonObject().getString("system");
            }
        }
        return null;
    }

    private List<JsonObject> getConcepts(JsonArray conceptArray) {
        List<JsonObject> concepts = new ArrayList<>();
        for (JsonValue jsonValue : conceptArray) {
            JsonObject concept = (JsonObject) jsonValue;
            concepts.add(concept);
            if (concept.containsKey("concept")) {
                concepts.addAll(getConcepts(concept.getJsonArray("concept")));
            }
        }
        return concepts;
    }

    private String getContentReference(JsonObject elementDefinition) {
        return elementDefinition.getString("contentReference", null);
    }

    private String getDisplay(JsonObject concept) {
        return concept.getString("display", null);
    }

    private String getDefinition(JsonObject concept) {
        return concept.getString("definition", null);
    }

    private JsonObject getElementDefinition(JsonObject structureDefinition, String path) {
        for (JsonObject elementDefinition : getElementDefinitions(structureDefinition)) {
            if (path.equals(elementDefinition.getString("path"))) {
                return elementDefinition;
            }
        }
        return null;
    }

    private List<JsonObject> getElementDefinitions(JsonObject structureDefinition) {
        return getElementDefinitions(structureDefinition, false);
    }

    private List<JsonObject> getElementDefinitions(JsonObject structureDefinition, boolean snapshot) {
        List<JsonObject> elementDefinitions = new ArrayList<>();
        JsonObject snapshotOrDifferential = structureDefinition.getJsonObject(snapshot ? "snapshot" : "differential");
        for (JsonValue element : snapshotOrDifferential.getJsonArray("element")) {
            elementDefinitions.add(element.asJsonObject());
            if (snapshot) {
                String path = element.asJsonObject().getString("path");
                if ("DataRequirement.codeFilter.extension".equals(path) ||
                        "DataRequirement.dateFilter.extension".equals(path) ||
                        "DataRequirement.sort.extension".equals(path) ||
                        "Dosage.doseAndRate.extension".equals(path) ||
                        "ElementDefinition.slicing.extension".equals(path) ||
                        "ElementDefinition.slicing.discriminator.extension".equals(path) ||
                        "ElementDefinition.base.extension".equals(path) ||
                        "ElementDefinition.type.extension".equals(path) ||
                        "ElementDefinition.example.extension".equals(path) ||
                        "ElementDefinition.constraint.extension".equals(path) ||
                        "ElementDefinition.binding.extension".equals(path) ||
                        "ElementDefinition.mapping.extension".equals(path) ||
                        "SubstanceAmount.referenceRange.extension".equals(path) ||
                        "Timing.repeat.extension".equals(path)) {
                    elementDefinitions.add(getModifierExtensionDefinition(path.replace(".extension", "")));
                }
            }
        }
        return elementDefinitions;
    }

    private List<JsonObject> getElementDefinitions(JsonObject structureDefinition, String path) {
        return getElementDefinitions(structureDefinition, true).stream().filter(o -> o.getString("path").startsWith(path + ".") && !o.getString("path").replaceFirst(path + ".",  "").contains(".") && !o.getString("path").equals(path)).collect(Collectors.toList());
    }

    private String getElementName(JsonObject elementDefinition, String path) {
        return elementDefinition.getString("path").replaceFirst(path + ".", "").replace("[x]", "");
    }

    private String getEnumConstantName(String name, String value) {
        StringBuilder sb = new StringBuilder();
        switch (name) {
        case "AuditEventAgentNetworkType":
            sb.append("TYPE_");
            break;
        case "AuditEventOutcome":
            sb.append("OUTCOME_");
            break;
        case "FHIRVersion":
            sb.append("VERSION_");
            break;
        case "SPDXLicense":
            sb.append("LICENSE_");
            break;
        }
        sb.append(String.join("_", value.split("(?<=[a-z])(?=[A-Z])"))
            .replace("<=", "less-or-equals")
            .replace(">=", "greater-or-equals")
            .replace("!=", "not-equals")
            .replace("=", "equals")
            .replace("<", "less-than")
            .replace(">", "greater-than")
            .replace("/", "_")
            .replace("-", "_")
            .replace(".", "_")
            .toUpperCase());
        return sb.toString();
    }

    private String getExpectedType(String className) {
        switch (className) {
        case "Boolean":
            return "JsonValue";
        case "Decimal":
        case "Integer":
        case "PositiveInt":
        case "UnsignedInt":
            return "JsonNumber";
        default:
            return "JsonString";
        }
    }

    private String getFieldName(JsonObject elementDefinition, String path) {
        return getFieldName(getElementName(elementDefinition, path));
    }

    private String getFieldName(String elementName) {
        if ("class".equals(elementName)) {
            return "clazz";
        }
        if (SourceVersion.isKeyword(elementName)) {
            return "_" + elementName;
        }
        return elementName;
    }

    private String getFieldType(JsonObject structureDefinition, JsonObject elementDefinition) {
        return getFieldType(structureDefinition, elementDefinition, isRepeating(elementDefinition));
    }

    /**
     * uses to rewrite the javadoc return link where the path is value.
     *
     * @param structureDefinition
     * @param elementDefinition
     * @param fieldType
     *
     * @return
     */
    public String getFieldTypeForJavaDocLink(JsonObject structureDefinition, JsonObject elementDefinition, String fieldType) {
        String path = elementDefinition.getString("path");

        if (isPrimitiveType(structureDefinition) && path.endsWith("value")) {
            String name = structureDefinition.getString("name");
            switch (name) {
            case "base64Binary":
                return "java.lang.byte[]";
            case "boolean":
                return "java.lang.Boolean";
            case "date":
            case "dateTime":
                return "java.time.TemporalAccessor";
            case "decimal":
                return "java.math.BigDecimal";
            case "instant":
                return "java.time.ZonedDateTime";
            case "integer":
            case "unsignedInt":
            case "positiveInt":
                return "java.lang.Integer";
            case "time":
                return "java.time.LocalTime";
            default:
                return "java.lang.String";
            }
        }

        // Not found, and drop through.
        return fieldType;
    }

    private String getFieldType(JsonObject structureDefinition, JsonObject elementDefinition, boolean repeating) {
        String path = elementDefinition.getString("path");
        String basePath = elementDefinition.getJsonObject("base").getString("path");

        if (isPrimitiveType(structureDefinition) && path.endsWith("value")) {
            String name = structureDefinition.getString("name");
            switch (name) {
            case "base64Binary":
                return "byte[]";
            case "boolean":
                return "java.lang.Boolean";
            case "date":
            case "dateTime":
                return "TemporalAccessor";
            case "decimal":
                return "BigDecimal";
            case "instant":
                return "ZonedDateTime";
            case "integer":
            case "unsignedInt":
            case "positiveInt":
                return "java.lang.Integer";
            case "time":
                return "LocalTime";
            default:
                return "java.lang.String";
            }
        }

        /*
        if ("Narrative.div".equals(path)) {
            return "java.lang.String";
        }
        */

        if (path.endsWith("[x]")) {
            return "Element";
        }

        if ("Resource.id".equals(basePath) || "Element.id".equals(basePath) || "Extension.url".equals(basePath)) {
            return "java.lang.String";
        }

        String fieldType = null;

        List<JsonObject> types = getTypes(elementDefinition);

        if (hasContentReference(elementDefinition)) {
            String contentReference = elementDefinition.getString("contentReference");
            contentReference = contentReference.substring(1);
            fieldType = Arrays.asList(contentReference.split("\\.")).stream().map(s -> titleCase(s)).collect(Collectors.joining("."));
        } else if (types.isEmpty() || isBackboneElement(elementDefinition)) {
            fieldType = titleCase(path.substring(path.lastIndexOf(".") + 1));
        } else {
            if (types.size() > 1) {
                throw new RuntimeException("Expected a single type but found " + types.size());
            }
            fieldType = titleCase(types.get(0).getString("code"));
            if (types.get(0).containsKey("profile")) {
                String profile = types.get(0).getJsonArray("profile").getString(0);
                fieldType = profile.substring(profile.lastIndexOf("/") + 1);
            }
            if ("Code".equals(fieldType) && hasRequiredBinding(elementDefinition)) {
                JsonObject binding = getBinding(elementDefinition);
                fieldType = getBindingName(binding);
                if ("messageheader-response-request".equals(fieldType)) {
                    fieldType = "MessageHeaderResponseRequest";
                } else if ("NutritiionOrderIntent".equals(fieldType)) {
                    fieldType = "NutritionOrderIntent";
                }
                fieldType = titleCase(fieldType);
            } else if ("Code".equals(fieldType) && containsBackboneElement(structureDefinition, "code")) {
                fieldType = "com.ibm.fhir.model.type.Code";
            } else if ("Dosage".equals(fieldType) && containsBackboneElement(structureDefinition, "dosage")) {
                fieldType = "com.ibm.fhir.model.type.Dosage";
            } else if ("Resource".equals(fieldType) && containsBackboneElement(structureDefinition, "resource")) {
                fieldType = "com.ibm.fhir.model.resource.Resource";
            }
        }

        if (repeating) {
            fieldType = String.format("List<%s>", fieldType);
            if ("List".equals(structureDefinition.getString("name"))) {
                fieldType = "java.util." + fieldType;
            }
        }

        return fieldType;
    }

    private String getMax(JsonObject elementDefinition) {
        return elementDefinition.getString("max");
    }

    private int getMin(JsonObject elementDefinition) {
        return elementDefinition.getInt("min", 0);
    }

    private JsonObject getModifierExtensionDefinition(String path) {
        return modifierExtensionDefinitionMap.computeIfAbsent(path, k -> buildModifierExtensionDefinition(k));
    }

    private String getPattern(JsonObject structureDefinition) {
        for (JsonObject elementDefinition : getElementDefinitions(structureDefinition)) {
            String path = elementDefinition.getString("path");
            if (path.endsWith("value")) {
                List<JsonObject> types = getTypes(elementDefinition);
                if (!types.isEmpty()) {
                    JsonObject type = types.get(0);
                    for (JsonValue extension : type.getOrDefault("extension", JsonArray.EMPTY_JSON_ARRAY).asJsonArray()) {
                        String url = extension.asJsonObject().getString("url", null);
                        if ("http://hl7.org/fhir/StructureDefinition/regex".equals(url)) {
                            return extension.asJsonObject().getString("valueString", null);
                        }
                    }
                }
            }
        }
        return null;
    }

    private JsonObject getStructureDefinition(String generatedClassName) {
        String[] tokens = generatedClassName.split("\\.");
        String key = tokens[0];
        JsonObject structureDefinition = structureDefinitionMap.get(key);
        if (structureDefinition == null) {
            structureDefinition = structureDefinitionMap.get(camelCase(key));
        }
        return structureDefinition;
    }

    private List<JsonObject> getTypes(JsonObject elementDefinition) {
        List<JsonObject> types = new ArrayList<>();
        for (JsonValue type : elementDefinition.getOrDefault("type", JsonArray.EMPTY_JSON_ARRAY).asJsonArray()) {
            types.add(type.asJsonObject());
        }
        return types;
    }

    private String getValueSetDefinition(String url) {
        JsonObject valueSet = valueSetMap.get(url);
        if (valueSet != null) {
            return valueSet.getString("definition", null);
        }
        return null;
    }

    private boolean hasConcepts(String valueSet) {
        return !getConcepts(valueSet).isEmpty();
    }

    private boolean hasContentReference(JsonObject elementDefinition) {
        return getContentReference(elementDefinition) != null;
    }

    private boolean hasRequiredBinding(JsonObject elementDefinition) {
        JsonObject binding = getBinding(elementDefinition);
        if (binding != null && binding.containsKey("valueSet") && binding.containsKey("strength")) {
            String valueSet = binding.getString("valueSet");
            String[] tokens = valueSet.split("\\|");
            valueSet = tokens[0];
            return "required".equals(binding.getString("strength")) && hasConcepts(valueSet);
        }
        return false;
    }

    private boolean hasSubtypes(String type) {
        return "Resource".equals(type) ||
                "DomainResource".equals(type) ||
                "Element".equals(type) ||
                "BackboneElement".equals(type) ||
                "Quantity".equals(type) ||
                "string".equals(type) ||
                "integer".equals(type) ||
                "code".equals(type) ||
                "uri".equals(type);
    }

    private boolean isAbstract(JsonObject structureDefinition) {
        return structureDefinition.getBoolean("abstract", false);
    }

    private boolean isBackboneElement(JsonObject elementDefinition) {
        String path = elementDefinition.getString("path");
        if ("DataRequirement.codeFilter".equals(path) ||
            "DataRequirement.dateFilter".equals(path) ||
            "DataRequirement.sort".equals(path) ||
            "Dosage.doseAndRate".equals(path) ||
            "ElementDefinition.slicing".equals(path) ||
            "ElementDefinition.slicing.discriminator".equals(path) ||
            "ElementDefinition.base".equals(path) ||
            "ElementDefinition.type".equals(path) ||
            "ElementDefinition.example".equals(path) ||
            "ElementDefinition.constraint".equals(path) ||
            "ElementDefinition.binding".equals(path) ||
            "ElementDefinition.mapping".equals(path) ||
            "SubstanceAmount.referenceRange".equals(path) ||
            "Timing.repeat".equals(path)) {
            return true;
        }
        List<JsonObject> types = getTypes(elementDefinition);
        return !types.isEmpty() && "BackboneElement".equals(types.get(0).getString("code", null));
    }

    /**
     * Whether the classNameWithoutPackage implies this is a nested type or not.
     * For example:
     * <ul>
     * <li>"Bundle" returns false
     * <li>"Bundle.Entry" returns true
     * </ul>
     */
    private boolean isNestedType(String classNameWithoutpackage) {
        return classNameWithoutpackage.contains(".");
    }

    private boolean isBase64Binary(JsonObject structureDefinition) {
        return "base64Binary".equals(structureDefinition.getString("name"));
    }

    private boolean isBoolean(JsonObject structureDefinition) {
        return "boolean".equals(structureDefinition.getString("name"));
    }

    private boolean isCanonical(JsonObject structureDefinition) {
        return "canonical".equals(structureDefinition.getString("name"));
    }

    private boolean isChoiceElement(JsonObject elementDefinition) {
        return elementDefinition.getString("path").endsWith("[x]");
    }

    private boolean isCode(JsonObject structureDefinition) {
        return "code".equals(structureDefinition.getString("name"));
    }

    private boolean isCodeSubtype(String fieldType) {
        String className = fieldType.replace("com.ibm.fhir.model.type.", "")
                .replace("com.ibm.fhir.model.resource.", "")
                .replace("java.util.", "")
                .replace("List<", "")
                .replace(">", "");
        return codeSubtypeClassNames.contains(className);
    }

    private boolean isReferenceElement(JsonObject structureDefinition, JsonObject elementDefinition) {
        return "Reference".equals(getFieldType(structureDefinition, elementDefinition));
    }

    private boolean isComplexType(JsonObject structureDefinition) {
        return "complex-type".equals(structureDefinition.getString("kind"));
    }

    private boolean isDataType(JsonObject structureDefinition) {
        if (structureDefinition == null) {
            return false;
        }
        return isComplexType(structureDefinition) || isPrimitiveType(structureDefinition);
    }

    private boolean isDate(JsonObject structureDefinition) {
        return "date".equals(structureDefinition.getString("name"));
    }

    private boolean isDateTime(JsonObject structureDefinition) {
        return "dateTime".equals(structureDefinition.getString("name"));
    }

    private boolean isDecimal(JsonObject structureDefinition) {
        return "decimal".equals(structureDefinition.getString("name"));
    }

    private boolean isId(JsonObject structureDefinition) {
        return "id".equals(structureDefinition.getString("name"));
    }

    private boolean isInstant(JsonObject structureDefinition) {
        return "instant".equals(structureDefinition.getString("name"));
    }

    private boolean isInteger(JsonObject structureDefinition) {
        return "integer".equals(structureDefinition.getString("name"));
    }

    private boolean isIntegerSubtype(JsonObject structureDefinition) {
        JsonObject baseDefinition = getBaseDefinition(structureDefinition);
        if (baseDefinition != null) {
            return "integer".equals(baseDefinition.getString("name"));
        }
        return false;
    }

    private boolean isIntegerSubtype(String className) {
        return integerSubtypeClassNames.contains(className);
    }

    private boolean isJavaString(String className) {
        return "java.lang.String".equals(className);
    }

    private boolean isLast(List<?> list, Object object) {
        return list.indexOf(object) == list.size() - 1;
    }

    private boolean isPositiveInt(JsonObject structureDefinition) {
        return "positiveInt".equals(structureDefinition.getString("name"));
    }

    private boolean isPrimitiveSubtype(String className) {
        return isStringSubtype(className) ||
                isCodeSubtype(className) ||
                isUriSubtype(className) ||
                isIntegerSubtype(className);
    }

    private boolean isPrimitiveType(JsonObject structureDefinition) {
        return "primitive-type".equals(structureDefinition.getString("kind"));
    }

    private boolean isPrimitiveType(String fieldType) {
        String className = fieldType.replace("com.ibm.fhir.model.type.", "")
                .replace("com.ibm.fhir.model.resource.", "")
                .replace("java.util.", "")
                .replace("List<", "")
                .replace(">", "");
        return primitiveTypeClassNames.contains(className);
    }

    private boolean isQuantitySubtype(JsonObject structureDefinition) {
        JsonObject baseDefinition = getBaseDefinition(structureDefinition);
        if (baseDefinition != null) {
            return "Quantity".equals(baseDefinition.getString("name"));
        }
        return false;
    }

    private boolean isQuantitySubtype(String className) {
        return quantitySubtypeClassNames.contains(className);
    }

    private boolean isRepeating(JsonObject elementDefinition) {
        return "*".equals(getMax(elementDefinition)) || (isProhibited(elementDefinition) && "*".equals(getBaseMax(elementDefinition)));
    }

    private Object getBaseMax(JsonObject elementDefinition) {
        return elementDefinition.getJsonObject("base").getString("max");
    }

    private boolean isProhibited(JsonObject elementDefinition) {
        return "0".equals(getMax(elementDefinition));
    }

    private boolean isRequired(JsonObject elementDefinition) {
        return getMin(elementDefinition) > 0;
    }

    private boolean isSummary(JsonObject elementDefinition) {
        return elementDefinition.getBoolean("isSummary", false);
    }

    private boolean isResource(JsonObject structureDefinition) {
        JsonObject baseDefinition = getBaseDefinition(structureDefinition);
        if (baseDefinition != null) {
            return "Resource".equals(baseDefinition.getString("name")) ||
                    "DomainResource".equals(baseDefinition.getString("name"));
        }
        return "Resource".equals(structureDefinition.getString("name"));
    }

    private boolean isString(JsonObject structureDefinition) {
        return "string".equals(structureDefinition.getString("name"));
    }

    private boolean isStringSubtype(JsonObject structureDefinition) {
        JsonObject baseDefinition = getBaseDefinition(structureDefinition);
        if (baseDefinition != null) {
            return "string".equals(baseDefinition.getString("name"));
        }
        return false;
    }

    private boolean isStringSubtype(String className) {
        return stringSubtypeClassNames.contains(className);
    }

    private boolean isTime(JsonObject structureDefinition) {
        return "time".equals(structureDefinition.getString("name"));
    }

    private boolean isUnsignedInt(JsonObject structureDefinition) {
        return "unsignedInt".equals(structureDefinition.getString("name"));
    }

    private boolean isUri(JsonObject structureDefinition) {
        return "uri".equals(structureDefinition.getString("name"));
    }

    private boolean isUriSubtype(JsonObject structureDefinition) {
        JsonObject baseDefinition = getBaseDefinition(structureDefinition);
        if (baseDefinition != null) {
            return "uri".equals(baseDefinition.getString("name"));
        }
        return false;
    }

    private boolean isUriSubtype(String className) {
        return uriSubtypeClassNames.contains(className);
    }

    private boolean isXhtml(JsonObject structureDefinition) {
        return "xhtml".equals(structureDefinition.getString("name"));
    }

    private String titleCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String visibility(JsonObject structureDefinition) {
        if (hasSubtypes(structureDefinition.getString("name"))) {
            return "protected";
        }
        return "private";
    }

    public static Map<String, JsonObject> buildResourceMap(String path, String resourceType) {
        try (JsonReader reader = Json.createReader(new FileReader(new File(path)))) {
            List<JsonObject> resources = new ArrayList<>();
            JsonObject bundle = reader.readObject();

            for (JsonValue entry : bundle.getJsonArray("entry")) {
                JsonObject resource = entry.asJsonObject().getJsonObject("resource");
                JsonString kind = resource.getJsonString("kind");
                if (kind != null && "logical".equals(kind.getString())) {
                    continue;
                }
                if (resourceType.equals(resource.getString("resourceType"))) {
                    resources.add(resource);
                }
            }

            Collections.sort(resources, new Comparator<JsonObject>() {
                @Override
                public int compare(JsonObject first, JsonObject second) {
                    return first.getString("name").compareTo(second.getString("name"));
                }
            });

            Map<String, JsonObject> resourceMap = new LinkedHashMap<>();
            for (JsonObject resource : resources) {
                if ("CodeSystem".equals(resourceType) || "ValueSet".equals(resourceType)) {
                    resourceMap.put(resource.getString("url"), resource);
                } else {
                    resourceMap.put(resource.getString("name"), resource);
                }
            }

            return resourceMap;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, JsonObject> structureDefinitionMap = buildResourceMap("./definitions/profiles-resources.json", "StructureDefinition");
        structureDefinitionMap.putAll(buildResourceMap("./definitions/profiles-types.json", "StructureDefinition"));

        Map<String, JsonObject> codeSystemMap = buildResourceMap("./definitions/valuesets.json", "CodeSystem");
        codeSystemMap.putAll(buildResourceMap("./definitions/v3-codesystems.json", "CodeSystem"));

        Map<String, JsonObject> valueSetMap = buildResourceMap("./definitions/valuesets.json", "ValueSet");
        valueSetMap.putAll(buildResourceMap("./definitions/v3-codesystems.json", "ValueSet"));

        CodeGenerator generator = new CodeGenerator(structureDefinitionMap, codeSystemMap, valueSetMap);
        generator.generate("./src/main/java");
    }
}
