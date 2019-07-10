/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.tools;

import static com.ibm.watsonhealth.fhir.tools.CodeBuilder._new;
import static com.ibm.watsonhealth.fhir.tools.CodeBuilder._this;
import static com.ibm.watsonhealth.fhir.tools.CodeBuilder.args;
import static com.ibm.watsonhealth.fhir.tools.CodeBuilder.implementsInterfaces;
import static com.ibm.watsonhealth.fhir.tools.CodeBuilder.javadocLink;
import static com.ibm.watsonhealth.fhir.tools.CodeBuilder.mods;
import static com.ibm.watsonhealth.fhir.tools.CodeBuilder.param;
import static com.ibm.watsonhealth.fhir.tools.CodeBuilder.params;
import static com.ibm.watsonhealth.fhir.tools.CodeBuilder.quote;
import static com.ibm.watsonhealth.fhir.tools.CodeBuilder.throwsExceptions;

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
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
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
        "Period", 
        "Quantity", 
        "Range", 
        "Ratio", 
        "Reference", 
        "SampledData", 
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
    private static final List<String> HEADER = readHeader();
    private static final List<String> FUNCTIONS = readFunctions();

    public CodeGenerator(Map<String, JsonObject> structureDefinitionMap, Map<String, JsonObject> codeSystemMap, Map<String, JsonObject> valueSetMap) {
        this.structureDefinitionMap = structureDefinitionMap;
        this.codeSystemMap = codeSystemMap;
        this.valueSetMap = valueSetMap;
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
    
    private static List<String> readFunctions() {
        try {
            String baseDir = ".";
            if (System.getProperty("BaseDir") != null) {
                baseDir = System.getProperty("BaseDir");
            }
            return Files.readAllLines(new File(baseDir + "/codegen-support/functions.txt").toPath());
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
    
    private String buildParseMethodInvocation(JsonObject elementDefinition, String elementName, String fieldType, boolean repeating) {
        if (repeating) {
            if (isPrimitiveType(fieldType) && !isPrimitiveSubtype(fieldType)) {
                return "parse" + fieldType + "(" + quote(elementName) + ", jsonValue, JsonSupport.getJsonValue(_" + elementName + "Array, index), index)";
            } else if (isStringSubtype(fieldType) || isCodeSubtype(fieldType)) {
                return "(" + fieldType + ") parseString(" + fieldType + ".builder(), "+ quote(elementName) + ", jsonValue, JsonSupport.getJsonValue(_" + elementName + "Array, index), index)";
            } else if (isUriSubtype(fieldType)) {
                return "(" + fieldType + ") parseUri(" + fieldType + ".builder(), "+ quote(elementName) + ", jsonValue, JsonSupport.getJsonValue(_" + elementName + "Array, index), index)";
            } else if (isIntegerSubtype(fieldType)) {
                return "(" + fieldType + ") parseInteger(" + fieldType + ".builder(), "+ quote(elementName) + ", jsonValue, JsonSupport.getJsonValue(_" + elementName + "Array, index), index)";
            } else if (isQuantitySubtype(fieldType)) {
                return "(" + fieldType + ") parseQuantity(" + fieldType + ".builder(), " + quote(elementName) + ", (JsonObject) jsonValue, index)";
            } else {
                return "parse" + fieldType.replace(".", "") + "(" + quote(elementName) + ", (JsonObject) jsonValue, index)";
            }
        } else {
            if (isPrimitiveType(fieldType) && !isPrimitiveSubtype(fieldType)) {
                String expectedType = getExpectedType(fieldType);
                return "parse" + fieldType + "(" + quote(elementName) + ", JsonSupport.getJsonValue(jsonObject, " + quote(elementName) + ", " + expectedType + ".class), jsonObject.get(" + quote("_" + elementName) + "), -1)";
            } else if (isStringSubtype(fieldType) || isCodeSubtype(fieldType)) {
                String expectedType = getExpectedType(fieldType);
                return "(" + fieldType + ") " + "parseString(" + fieldType + ".builder(), "+ quote(elementName) + ", JsonSupport.getJsonValue(jsonObject, " + quote(elementName) + ", " + expectedType + ".class), jsonObject.get(" + quote("_" + elementName) + "), -1)";
            } else if (isUriSubtype(fieldType)) {
                String expectedType = getExpectedType(fieldType);
                return "(" + fieldType + ") " + "parseUri(" + fieldType + ".builder(), "+ quote(elementName) + ", JsonSupport.getJsonValue(jsonObject, " + quote(elementName) + ", " + expectedType + ".class), jsonObject.get(" + quote("_" + elementName) + "), -1)";
            } else if (isIntegerSubtype(fieldType)) {
                String expectedType = getExpectedType(fieldType);
                return "(" + fieldType + ") " + "parseInteger(" + fieldType + ".builder(), "+ quote(elementName) + ", JsonSupport.getJsonValue(jsonObject, " + quote(elementName) + ", " + expectedType + ".class), jsonObject.get(" + quote("_" + elementName) + "), -1)";
            } else if (isQuantitySubtype(fieldType)) {
                return "(" + fieldType + ") parseQuantity(" + fieldType + ".builder(), " + quote(elementName) + ", JsonSupport.getJsonValue(jsonObject, " + quote(elementName) + ", JsonObject.class), -1)";
            } else if (isChoiceElement(elementDefinition)) {
                List<JsonObject> types = getTypes(elementDefinition);
                String choiceTypeNames = types.stream().map(o -> quote(titleCase(o.getString("code")))).collect(Collectors.joining(", "));
                return "parseChoiceElement(" + quote(elementName) + ", jsonObject, " + choiceTypeNames + ")";
            } else if (isJavaString(fieldType)) {
                return "parseJavaString(" + quote(elementName) + ", JsonSupport.getJsonValue(jsonObject, " + quote(elementName) + ", JsonString.class), -1)";
            } else {
                return "parse" + fieldType.replace(".", "") + "(" + quote(elementName) + ", JsonSupport.getJsonValue(jsonObject, " + quote(elementName) + ", JsonObject.class), -1)";
            }
        }
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
        generateAbstractVisitorClass(basePath);
        generateJsonParser(basePath);
        generateFunctions(basePath);
    }

    private void generateFunctions(String basePath) {
        List<String> functionClassNames = new ArrayList<>();
        
        for (String line : FUNCTIONS) {
            if (line.contains("expression")) {
                continue;
            }
            
            CodeBuilder cb = new CodeBuilder();
            
            String packageName = "com.ibm.watsonhealth.fhir.model.path.function";
            cb.javadoc(HEADER, true, true, false).newLine();
            cb._package(packageName).newLine();
            
            cb._import("java.util.Collection");
            cb._import("java.util.List");
            cb.newLine();
            cb._import("com.ibm.watsonhealth.fhir.model.path.FHIRPathNode");
            cb.newLine();
            
            String functionName = line.substring(0, line.indexOf("("));
            String className = titleCase(functionName) + "Function";
            functionClassNames.add(className);
            
            long maxArity;
            if (line.contains("()")) {
                maxArity = 0;
            } else {
                maxArity = 1;
                if (line.contains(",")) {
                    maxArity = line.chars().filter(ch -> ch == ',').count() + 1;
                }
            }
            long minArity = maxArity - line.chars().filter(ch -> ch == '[').count();
            
            cb._class(mods("public"), className, null, implementsInterfaces("FHIRPathFunction"));
            
            cb.override();
            cb.method(mods("public"), "String", "getName");
            cb._return(quote(functionName));
            cb.end();
            
            cb.newLine();
            
            cb.override();
            cb.method(mods("public"), "int", "getMinArity");
            cb._return(String.valueOf(minArity));
            cb.end();
            
            cb.newLine();
            
            cb.override();
            cb.method(mods("public"), "int", "getMaxArity");
            cb._return(String.valueOf(maxArity));
            cb.end();
            
            cb.newLine();
            
            cb.method(mods("public"), "Collection<FHIRPathNode>", "apply", params("Collection<FHIRPathNode> context", "List<Collection<FHIRPathNode>> arguments"));
            cb._throw(_new("UnsupportedOperationException", args("\"Function: '\" + getName() + \"' is not supported\"")));
            cb.end();
            
            cb._end();
            
            File file = new File(basePath + "/" + packageName.replace(".", "/") + "/" + className + ".java");
            
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(cb.toString());
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        
        Collections.sort(functionClassNames);
        
        // Work around the pseudo hardcoding
        String baseDir = ".";
        if (System.getProperty("BaseDir") != null) {
            baseDir = System.getProperty("BaseDir");
        }
        
        File file = new File(baseDir + "/src/main/resources/META-INF/services/com.ibm.watsonhealth.fhir.model.path.function.FHIRPathFunction");

        try (FileWriter writer = new FileWriter(file)) {
            for (String functionClassName : functionClassNames) {
                writer.write("com.ibm.watsonhealth.fhir.model.path.function." + functionClassName);
                if (!isLast(functionClassNames, functionClassName)) {
                    writer.write(System.lineSeparator());
                }
            }
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private void generateAbstractVisitorClass(String basePath) {
        CodeBuilder cb = new CodeBuilder();
        
        String packageName = "com.ibm.watsonhealth.fhir.model.visitor";
        cb.javadoc(HEADER, true, true, false).newLine();
        cb._package(packageName).newLine();
        
        cb._import("java.math.BigDecimal");
        cb._import("java.time.LocalDate");
        cb._import("java.time.LocalTime");
        cb._import("java.time.Year");
        cb._import("java.time.YearMonth");
        cb._import("java.time.ZonedDateTime");
        
        cb.newLine();
        
        cb._import("com.ibm.watsonhealth.fhir.model.resource.*");
        /*
        Collections.sort(typeClassNames);
        
        for (String typeClassName : typeClassNames) {
            cb._import("com.ibm.watsonhealth.fhir.model.type." + typeClassName);
        }
        */
        cb._import("com.ibm.watsonhealth.fhir.model.type.*");
        cb._import("com.ibm.watsonhealth.fhir.model.type.Boolean");
        cb._import("com.ibm.watsonhealth.fhir.model.type.Integer");
        cb._import("com.ibm.watsonhealth.fhir.model.type.String");
        cb.newLine();
        
        cb._class(mods("public", "abstract"), "AbstractVisitor", null, implementsInterfaces("Visitor"));
        
        cb.override();
        cb.method(mods("public"), "boolean", "preVisit", params("Element element"))._return("true").end().newLine();

        cb.override();
        cb.method(mods("public"), "boolean", "preVisit", params("Resource resource"))._return("true").end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "postVisit", params("Element element")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "postVisit", params("Resource resource")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitStart", params("java.lang.String elementName", "Element element")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitStart", params("java.lang.String elementName", "Resource resource")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitStart", params("java.lang.String elementName", "java.util.List<? extends Visitable> visitables", "Class<?> type")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitEnd", params("java.lang.String elementName", "Element element")).end().newLine();

        cb.override();
        cb.method(mods("public"), "void", "visitEnd", params("java.lang.String elementName", "Resource resource")).end().newLine();
        
        cb.override();
        cb.method(mods("public"), "void", "visitEnd", params("java.lang.String elementName", "java.util.List<? extends Visitable> visitables", "Class<?> type")).end().newLine();
        
        Collections.sort(generatedClassNames);
        
        for (String className : generatedClassNames) {
            if (isPrimitiveSubtype(className)) {
                continue;
            }
            String paramName = camelCase(className).replace(".", "");
            if (SourceVersion.isKeyword(paramName)) {
                paramName = "_" + paramName;
            }
            cb.override();
            cb.method(mods("public"), "boolean", "visit", params("java.lang.String elementName", className + " " + paramName))._return("true").end().newLine();
        }

        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "byte[] value")).end().newLine();
        
        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "BigDecimal value")).end().newLine();
        
        cb.override();
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "java.lang.Boolean value")).end().newLine();

//      cb.override();
//      cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "org.w3c.dom.Element value")).end().newLine();

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
        cb.method(mods("public"), "void", "visit", params("java.lang.String elementName", "ZonedDateTime value")).end();

        cb._end();
        
        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/AbstractVisitor.java");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cb.toString());
        } catch (Exception e) {
            throw new Error(e);
        }        
    }

    private void generateAcceptMethod(JsonObject structureDefinition, String className, String path, CodeBuilder cb, boolean nested) {
        if (isAbstract(structureDefinition) || isPrimitiveSubtype(structureDefinition)) {
            return;
        }
        
        cb.override();
        cb.method(mods("public"), "void", "accept", params("java.lang.String elementName", "Visitor visitor"));
        
        cb._if("visitor.preVisit(this)");
        cb.invoke("visitor", "visitStart", args("elementName", "this"));
        
        cb._if("visitor.visit(elementName, this)");
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
        
        cb.invoke("visitor", "visitEnd", args("elementName", "this"));
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
        List<JsonObject> requiredElementDefinitions = elementDefinitions.stream().filter(o -> isRequired(o) && o.getString("path").equals(o.getJsonObject("base").getString("path"))).collect(Collectors.toList());
        if (!requiredElementDefinitions.isEmpty()) {
            cb.comment("required");
            for (JsonObject elementDefinition : requiredElementDefinitions) {
                String fieldName = getFieldName(elementDefinition, path);
                String fieldType = getFieldType(structureDefinition, elementDefinition);
                cb.field(mods(visibility, "final"), fieldType, fieldName);
                fieldCount++;
            }
        }
        
        List<JsonObject> optionalElementDefinitions = elementDefinitions.stream().filter(o -> !isRequired(o) && o.getString("path").equals(o.getJsonObject("base").getString("path"))).collect(Collectors.toList());
        if (!optionalElementDefinitions.isEmpty()) {
            if (fieldCount > 0) {
                cb.newLine();
            }
            cb.comment("optional");
            for (JsonObject elementDefinition : optionalElementDefinitions) {
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
        
        List<String> params = new ArrayList<>();
        List<String> args = new ArrayList<>();
        
        requiredElementDefinitions = elementDefinitions.stream().filter(o -> isRequired(o)).collect(Collectors.toList());
        for (JsonObject elementDefinition : requiredElementDefinitions) {
            String fieldName = getFieldName(elementDefinition, path);
            String fieldType = getFieldType(structureDefinition, elementDefinition);
            params.add(fieldType + " " + fieldName);
            args.add(fieldName);
        }
                
        cb.constructor(mods(visibility), "Builder", params)._super();
        for (JsonObject elementDefinition : requiredElementDefinitions) {
            String fieldName = getFieldName(elementDefinition, path);
            cb.assign(_this(fieldName), fieldName);
        }
        cb.end().newLine();
        
        optionalElementDefinitions = elementDefinitions.stream().filter(o -> !isRequired(o)).collect(Collectors.toList());
        for (JsonObject elementDefinition : optionalElementDefinitions) {
            String basePath = elementDefinition.getJsonObject("base").getString("path");
            boolean declaredBy = elementDefinition.getString("path").equals(basePath);
            
            String fieldName = getFieldName(elementDefinition, path);
            String fieldType = getFieldType(structureDefinition, elementDefinition);
            
            if (isRepeating(elementDefinition)) {
                generateBuilderMethodJavadoc(structureDefinition, elementDefinition, fieldName, cb);
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
                
                generateBuilderMethodJavadoc(structureDefinition, elementDefinition, fieldName, cb);
                if (!declaredBy) {
                    cb.override();
                }
                cb.method(mods("public"), "Builder", fieldName, params(param(paramType, fieldName)));
                
                if (declaredBy) {
                    cb.invoke(_this(fieldName), "addAll", args(fieldName));
                    cb._return("this");
                } else {
                    cb._return("(Builder) super." + fieldName + "(" + fieldName + ")");
                }
                
                cb.end();
            } else {
                generateBuilderMethodJavadoc(structureDefinition, elementDefinition, fieldName, cb);
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
                .assign("this.value", "Base64.getDecoder().decode(value)")
                ._return("this")
            .end().newLine();
        }
        
        if (isDateTime(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "PARSER.parseBest(value, ZonedDateTime::from, LocalDate::from, YearMonth::from, Year::from)")
                ._return("this")
            .end().newLine();
        }
        
        if (isDate(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "PARSER.parseBest(value, LocalDate::from, YearMonth::from, Year::from)")
                ._return("this")
            .end().newLine();
        }
        
        if (isInstant(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "PARSER.parse(value, ZonedDateTime::from)")
                ._return("this")
            .end().newLine();
        }
        
        if (isTime(structureDefinition)) {
            cb.method(mods("public"), "Builder", "value", params("java.lang.String value"))
                .assign("this.value", "LocalTime.parse(value)")
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
        
        cb.override();
        if (isAbstract(structureDefinition)) {
            cb.abstractMethod(mods("public", "abstract"), className, "build");
        } else {
            cb.method(mods("public"), className, "build")
                ._return(_new(className, args("this")))
            .end();
        }
        
        String paramName = camelCase(className);
        if (SourceVersion.isKeyword(paramName)) {
            paramName = "_" + paramName;
        }
        
        if (!isAbstract(structureDefinition)) {
            cb.newLine();
            cb.method(mods("private"), "Builder", "from", params(param(className, paramName)));
            for (JsonObject elementDefinition : optionalElementDefinitions) {                
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
        }
        
        cb._end();
    }

    private void generateBuilderMethodJavadoc(JsonObject structureDefinition, JsonObject elementDefinition, String fieldName, CodeBuilder cb) {
        String definition = elementDefinition.getString("definition");
        cb.javadocStart();
        cb.javadoc(Arrays.asList(definition.split(System.lineSeparator())), false, false, true);
        cb.javadoc("");
        
        String _short = elementDefinition.getString("short");
        cb.javadocParam(fieldName, _short);
        
        cb.javadoc("");
        
        String link = getFieldType(structureDefinition, elementDefinition, false);
        link = link.substring(link.lastIndexOf(".") + 1);
        cb.javadocReturn("A reference to this Builder instance.");
        cb.javadocEnd();
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
                cb.annotation("Generated", quote("com.ibm.watsonhealth.fhir.tools.CodeGenerator"));
            }
            cb._class(mods, className, _super);
                        
            if (isDateTime(structureDefinition)) {
                cb.field(mods("private", "static", "final"), "DateTimeFormatter", "PARSER", "new DateTimeFormatterBuilder().appendPattern(\"yyyy\").optionalStart().appendPattern(\"-MM\").optionalStart().appendPattern(\"-dd\").optionalStart().appendPattern(\"'T'HH:mm:ss\").optionalStart().appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true).optionalEnd().appendPattern(\"XXX\").optionalEnd().optionalEnd().optionalEnd().toFormatter()").newLine();
            }
            
            if (isDate(structureDefinition)) {
                cb.field(mods("private", "static", "final"), "DateTimeFormatter", "PARSER", "DateTimeFormatter.ofPattern(\"[yyyy[-MM[-dd]]]\")").newLine();
            }
            
            if (isInstant(structureDefinition)) {
                cb.field(mods("private", "static", "final"), "DateTimeFormatter", "PARSER", "new DateTimeFormatterBuilder().appendPattern(\"yyyy-MM-dd'T'HH:mm:ss\").optionalStart().appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true).optionalEnd().appendPattern(\"XXX\").toFormatter()").newLine();
            }
            
            if (isBoolean(structureDefinition)) {
                cb.field(mods("public", "static", "final"), "Boolean", "TRUE", "Boolean.of(true)");
                cb.field(mods("public", "static", "final"), "Boolean", "FALSE", "Boolean.of(false)").newLine();
            }
            
            if (isString(structureDefinition) || isUri(structureDefinition) || isStringSubtype(structureDefinition) || isUriSubtype(structureDefinition)) {
                String pattern = getPattern(structureDefinition);
                if (pattern != null) {
                    cb.field(mods("private", "static", "final"), "Pattern", "PATTERN", "Pattern.compile(" + quote(pattern.replace("\\", "\\\\")) + ")").newLine();
                }
            }
            
            if (isPositiveInt(structureDefinition)) {
                cb.field(mods("private", "static", "final"), "int", "MIN_VALUE", "1").newLine();
            }
            
            if (isUnsignedInt(structureDefinition)) {
                cb.field(mods("private", "static", "final"), "int", "MIN_VALUE", "0").newLine();
            }

            List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path);
                        
            List<String> nestedPaths = new ArrayList<>();  
            
            String visibility = nested ? "private" : visibility(structureDefinition);
            
            int fieldCount = 0;
            for (JsonObject elementDefinition : elementDefinitions) {
                String basePath = elementDefinition.getJsonObject("base").getString("path");
                if (elementDefinition.getString("path").equals(basePath)) {
                    String fieldName = getFieldName(elementDefinition, path);
                    String fieldType = getFieldType(structureDefinition, elementDefinition);
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
                                String types = getTypes(elementDefinition).stream().map(o -> titleCase(o.getString("code")) + ".class").collect(Collectors.joining(", "));
                                cb.assign(fieldName, "ValidationSupport.requireChoiceElement(builder." + fieldName + ", " + quote(elementName) + ", " + types + ")");
                            } else {
                                cb.assign(fieldName, "ValidationSupport.requireNonNull(builder." + fieldName + ", " + quote(elementName) + ")");
                            }
                        }
                    } else {
                        if (isRepeating(elementDefinition)) {
                            cb.assign(fieldName, "Collections.unmodifiableList(builder." + fieldName + ")");
                        } else {
                            if (isChoiceElement(elementDefinition)) {
                                String types = getTypes(elementDefinition).stream().map(o -> titleCase(o.getString("code")) + ".class").collect(Collectors.joining(", "));
                                cb.assign(fieldName, "ValidationSupport.choiceElement(builder." + fieldName + ", " + quote(elementName) + ", " + types + ")");
                            } else {
                                cb.assign(fieldName, "builder." + fieldName);
                            }
                        }
                    }
                }
            }
            
            if (isString(structureDefinition) || 
                    isUri(structureDefinition) || 
                    isStringSubtype(structureDefinition) || 
                    isUriSubtype(structureDefinition) || 
                    isIntegerSubtype(structureDefinition)) {
                if (isString(structureDefinition) || isUri(structureDefinition)) {
                    cb.invoke("ValidationSupport", "checkMaxLength", args("value"));
                }
                if (isString(structureDefinition) || isUriSubtype(structureDefinition)) {
                    cb.invoke("ValidationSupport", "checkMinLength", args("value"));
                }
                if (isUnsignedInt(structureDefinition)) {
                    cb.invoke("ValidationSupport", "checkValue", args("value", "MIN_VALUE"));
                }
                if (isPositiveInt(structureDefinition)) {
                    cb.invoke("ValidationSupport", "checkValue", args("value", "MIN_VALUE"));
                }
                if (isString(structureDefinition) || isUri(structureDefinition) || isStringSubtype(structureDefinition) || isUriSubtype(structureDefinition)) {
                    cb.invoke("ValidationSupport", "checkValue", args("value", "PATTERN"));
                }
            }
            
            if ("Narrative".equals(structureDefinition.getString("name"))) {
                cb.invoke("ValidationSupport", "checkXHTMLContent", args("div"));
            }
            
            if (isDate(structureDefinition)) {
                cb.invoke("ValidationSupport", "checkValueType", args("value", "LocalDate.class", "YearMonth.class", "Year.class"));
            }
            
            if (isDateTime(structureDefinition)) {
                cb.invoke("ValidationSupport", "checkValueType", args("value", "ZonedDateTime.class", "LocalDate.class", "YearMonth.class", "Year.class"));
            }

            cb.end().newLine();
            
            for (JsonObject elementDefinition : elementDefinitions) {
                String basePath = elementDefinition.getJsonObject("base").getString("path");
                if (elementDefinition.getString("path").equals(basePath)) {
                    String fieldName = getFieldName(elementDefinition, path);
                    String fieldType = getFieldType(structureDefinition, elementDefinition);
                    String methodName = "get" + titleCase(fieldName).replace("_", "");
                    generateGetterMethodJavadoc(structureDefinition, elementDefinition, fieldType, cb);
//                  cb.javadoc(Arrays.asList(elementDefinition.getString("definition").split(System.lineSeparator())));
                    cb.method(mods("public"), fieldType, methodName)._return(fieldName).end().newLine();
                }
            }
            
            generateIsPartialMethod(structureDefinition, cb);
            generateIsAsMethods(structureDefinition, cb);
            generateFactoryMethods(structureDefinition, cb);
            generateAcceptMethod(structureDefinition, className, path, cb, nested);
            generateEqualsHashCodeMethods(structureDefinition, className, path, cb);
                        
            List<String> params = new ArrayList<>();
            List<String> args = new ArrayList<>();

            List<JsonObject> requiredElementDefinitions = elementDefinitions.stream().filter(o -> isRequired(o)).collect(Collectors.toList());

            for (JsonObject elementDefinition : requiredElementDefinitions) {
                String fieldName = getFieldName(elementDefinition, path);
                String fieldType = getFieldType(structureDefinition, elementDefinition);
                params.add(fieldType + " " + fieldName);
                args.add(fieldName);
            }
            
            if (isAbstract(structureDefinition)) {
                if (!"Resource".equals(className) && !"Element".equals(className)) {
                    cb.override();
                }
                cb.abstractMethod(mods("public", "abstract"), "Builder", "toBuilder").newLine();
            } else {
                cb.override();
                cb.method(mods("public"), "Builder", "toBuilder")
                    ._return(_new("Builder", args) + ".from(this)")
                .end().newLine();
                
                if (!params.isEmpty()) {
                    cb.method(mods("public"), "Builder", "toBuilder", params)
                        ._return(_new("Builder", args) + ".from(this)")
                    .end().newLine();
                }
                
                cb.method(mods("public", "static"), "Builder", "builder", params)
                    ._return(_new("Builder", args))
                .end().newLine();
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
            String prefix = "";
            if ("other".equals(fieldName)) {
                prefix = "this.";
            }
            joiner.add("Objects.equals(" + prefix + fieldName + ", other." + fieldName +")");
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
            valueMap.put("id", key);
            valueMap.put("level", "error".equals(severity) ? "Rule" : "Warning");
            valueMap.put("location", path.equals(name) ? "(base)" : path);
            valueMap.put("description", human.replace("\"", "\\\""));
            valueMap.put("expression", expression.replace("\"", "\\\"").replace("div", "`div`"));
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
            cb.javadocReturn("A list containing immutable objects of type " + javadocLink(reference) + ".");
        } else {
            String reference = fieldType;
            if (!"java.lang.String".equals(fieldType)) {
                reference = fieldType.substring(fieldType.lastIndexOf(".") + 1);
            }
            fieldType.substring(fieldType.lastIndexOf(".") + 1);
            cb.javadocReturn("An immutable object of type " + javadocLink(reference) + ".");
        }
        cb.javadocEnd();
    }

    private void generateClass(JsonObject structureDefinition, String basePath) {
        String name = structureDefinition.getString("name");
        
        if ("xhtml".equals(name)) {
            return;
        }
        
        CodeBuilder cb = new CodeBuilder();
        
        String className = titleCase(structureDefinition.getString("name"));
        
        String packageName = null;
        String kind = structureDefinition.getString("kind");
        if ("resource".equals(kind) || "logical".equals(kind)) {
            packageName = "com.ibm.watsonhealth.fhir.model.resource";
            resourceClassNames.add(className);
        } else {
            packageName = "com.ibm.watsonhealth.fhir.model.type";
            typeClassNames.add(className);
        }
        cb.javadoc(HEADER, true, true, false).newLine();
        cb._package(packageName).newLine();
        
        generateImports(structureDefinition, cb);

        String path = getElementDefinitions(structureDefinition).get(0).getString("path");
        generateClass(structureDefinition, Collections.singletonList(path), cb, false);
        
        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/" + className + ".java");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(cb.toString());
        } catch (Exception e) {
            throw new Error(e);
        }
        
//      System.out.println(cb.toString());
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
        
        if (isInstant(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("ZonedDateTime value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
            
            cb.method(mods("public", "static"), className, "of", params("java.lang.String value"))
                ._return(className + ".builder().value(value).build()")
            .end().newLine();
            
            cb.method(mods("public", "static"), className, "now", params("boolean normalize"))
                .assign("ZonedDateTime now", "ZonedDateTime.now()")
                ._if("normalize")
                    .comment("normalize to UTC")
                    .assign("now", "now.withZoneSameInstant(ZoneOffset.UTC)")
                ._end()
                ._return(className + ".builder().value(now).build()")
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
        
        if (isTime(structureDefinition)) {
            cb.method(mods("public", "static"), className, "of", params("LocalTime value"))
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
    }
    
    private void generateImports(JsonObject structureDefinition, CodeBuilder cb) {
        String name = structureDefinition.getString("name");
                
        Set<String> imports = new HashSet<>();
        
        boolean containsCollection = false;
        boolean repeating = false;
        
        if (!isAbstract(structureDefinition)) {
            imports.add("java.util.Objects");
        }
        
        if ("Resource".equals(name) || "Element".equals(name)) {
            imports.add("com.ibm.watsonhealth.fhir.model.visitor.AbstractVisitable");
        }
        if (!isAbstract(structureDefinition) && !isPrimitiveSubtype(structureDefinition)) {
            imports.add("com.ibm.watsonhealth.fhir.model.visitor.Visitor");
        }
        
        if (hasConstraints(structureDefinition)) {
            imports.add("com.ibm.watsonhealth.fhir.model.annotation.Constraint");
        }
        
        imports.add("javax.annotation.Generated");
        
        for (JsonObject elementDefinition : getElementDefinitions(structureDefinition, true)) {
            String path = elementDefinition.getString("path");
            String basePath = elementDefinition.getJsonObject("base").getString("path");
            
            if (isBackboneElement(elementDefinition)) {
                imports.add("com.ibm.watsonhealth.fhir.model.type.BackboneElement");
            }
            
            if (path.endsWith("collection") && isBackboneElement(elementDefinition)) {
                containsCollection = true;
            }
            
            if (isRepeating(elementDefinition)) {
                if (basePath.startsWith(name) && !basePath.equals(name)) {
                    if (!isRequired(elementDefinition)) {
                        imports.add("java.util.ArrayList");
                    }
                    imports.add("java.util.Collections");
                    if (!"List".equals(name)) {
                        imports.add("java.util.List");
                    }
                }
                repeating = !basePath.equals(name);
            }
            
            if (isRequired(elementDefinition)) {
                imports.add("com.ibm.watsonhealth.fhir.model.util.ValidationSupport");
            }
            
            if (isChoiceElement(elementDefinition)) {
                imports.add("com.ibm.watsonhealth.fhir.model.util.ValidationSupport");
                if (isResource(structureDefinition)) {
                    for (String dataTypeName : getTypes(elementDefinition).stream().map(o -> titleCase(o.getString("code"))).collect(Collectors.toList())) {
                        imports.add("com.ibm.watsonhealth.fhir.model.type." + dataTypeName);
                    }
                }
            }
            
            if (isBackboneElement(elementDefinition) || "Element.id".equals(basePath)) {
                continue;
            }
            
            String fieldType = getFieldType(structureDefinition, elementDefinition, false);
            JsonObject definition = structureDefinitionMap.get(fieldType);
            if (definition == null) {
                definition = structureDefinitionMap.get(camelCase(fieldType));
            }
            
            if (isResource(structureDefinition) && (isDataType(definition)) || hasRequiredBinding(elementDefinition)) {
                imports.add("com.ibm.watsonhealth.fhir.model.type." + fieldType);
            }
        }
        
        if (!containsCollection && repeating) {
            imports.add("java.util.Collection");
        }
        
        if ("Resource".equals(name) || "Element".equals(name)) {
            imports.add("com.ibm.watsonhealth.fhir.model.builder.AbstractBuilder");
        }
        
        if (isBase64Binary(structureDefinition)) {
            imports.add("java.util.Base64");
        }
        
        if (isDateTime(structureDefinition) || isInstant(structureDefinition)) {
            imports.add("java.time.ZonedDateTime");
        }
        
        if (isTime(structureDefinition)) {
            imports.add("java.time.LocalTime");
        }
        
        if (isDate(structureDefinition) || isDateTime(structureDefinition) || isInstant(structureDefinition)) {
            if (isDate(structureDefinition) || isDateTime(structureDefinition)) {
                imports.add("java.time.temporal.TemporalAccessor");
                imports.add("java.time.LocalDate");
                imports.add("java.time.Year");
                imports.add("java.time.YearMonth");
            }
            imports.add("java.time.format.DateTimeFormatter");
            if (isDateTime(structureDefinition) || isInstant(structureDefinition)) {
                imports.add("java.time.temporal.ChronoField");
                imports.add("java.time.format.DateTimeFormatterBuilder");
            }
        }
        
        if (isInstant(structureDefinition)) {
            imports.add("java.time.ZoneOffset");
        }
        
        if (isString(structureDefinition) || 
                isUri(structureDefinition) || 
                isStringSubtype(structureDefinition) || 
                isUriSubtype(structureDefinition) || 
                isIntegerSubtype(structureDefinition) || 
                isDate(structureDefinition) || 
                isDateTime(structureDefinition)) {
            imports.add("com.ibm.watsonhealth.fhir.model.util.ValidationSupport");
            if (isString(structureDefinition) || isUri(structureDefinition) || isStringSubtype(structureDefinition) || isUriSubtype(structureDefinition)) {
                imports.add("java.util.regex.Pattern");
            }
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
            
            cb.method(mods("public"), "<T extends Resource> T", "as", params("Class<T> resourceType"))
                ._return("resourceType.cast(this)")
            .end().newLine();
        }
        if ("Element".equals(name)) {
            cb.method(mods("public"), "<T extends Element> boolean", "is", params("Class<T> elementType"))
                ._return("elementType.isInstance(this)")
            .end().newLine();
            
            cb.method(mods("public"), "<T extends Element> T", "as", params("Class<T> elementType"))
                ._return("elementType.cast(this)")
            .end().newLine();
        }
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

    private void generateJsonParser(String basePath) {
        CodeBuilder cb = new CodeBuilder();
        
        String packageName = "com.ibm.watsonhealth.fhir.model.parser";
        cb.javadoc(HEADER, true, true, false).newLine();
        cb._package(packageName).newLine();
        /*
        cb._import("java.math.BigDecimal");
        cb._import("java.time.LocalDate");
        cb._import("java.time.LocalTime");
        cb._import("java.time.Year");
        cb._import("java.time.YearMonth");
        cb._import("java.time.ZonedDateTime");
        */
        cb._import("java.io.InputStream");
        cb._import("java.io.Reader");
        cb._import("java.util.ArrayList");
        cb._import("java.util.Collection");
        cb._import("java.util.Set");
        cb._import("java.util.Stack");
        cb._import("java.util.StringJoiner");
        
        cb.newLine();
        
        cb._import("javax.json.Json");
        cb._import("javax.json.JsonArray");
        cb._import("javax.json.JsonNumber");
        cb._import("javax.json.JsonObject");
        cb._import("javax.json.JsonReader");
        cb._import("javax.json.JsonReaderFactory");
        cb._import("javax.json.JsonString");
        cb._import("javax.json.JsonValue");
        
        cb.newLine();
        
        cb._import("com.ibm.watsonhealth.fhir.model.parser.FHIRParser");
        cb._import("com.ibm.watsonhealth.fhir.model.parser.exception.FHIRParserException");
        cb._import("com.ibm.watsonhealth.fhir.model.resource.*");
        cb._import("com.ibm.watsonhealth.fhir.model.type.*");
        cb._import("com.ibm.watsonhealth.fhir.model.type.Boolean");
        cb._import("com.ibm.watsonhealth.fhir.model.type.Integer");
        cb._import("com.ibm.watsonhealth.fhir.model.type.String");
        cb._import("com.ibm.watsonhealth.fhir.model.util.ElementFilter");
        cb._import("com.ibm.watsonhealth.fhir.model.util.JsonSupport");
        
        /*
        Collections.sort(typeClassNames);
        
        for (String typeClassName : typeClassNames) {
            cb._import("com.ibm.watsonhealth.fhir.model.type." + typeClassName);
        }
        */
        
        cb.newLine();
        
        cb._class(mods("public"), "FHIRJsonParser", null, implementsInterfaces("FHIRParser"));
        cb.field(mods("public", "static"), "boolean", "DEBUG", "false");
        cb.field(mods("protected", "static", "final"), "JsonReaderFactory", "JSON_READER_FACTORY", "Json.createReaderFactory(null)");
        
        cb.newLine();
        
        cb.field(mods("protected", "final"), "Stack<java.lang.String>", "stack", _new("Stack<>"));
        
        cb.newLine();
        
        cb.constructor(mods("protected"), "FHIRJsonParser");
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
            ._try("JsonReader jsonReader = JSON_READER_FACTORY.createReader(in)")
                .assign("JsonObject jsonObject", "jsonReader.readObject()")
                ._return("parseAndFilter(jsonObject, elementsToInclude)")
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
            ._try("JsonReader jsonReader = JSON_READER_FACTORY.createReader(reader)")
                .assign("JsonObject jsonObject", "jsonReader.readObject()")
                ._return("parseAndFilter(jsonObject, elementsToInclude)")
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
                .assign("java.lang.String resourceType", "getResourceType(jsonObject)")
                ._if("elementsToInclude != null")
                    .assign("ElementFilter elementFilter", "new ElementFilter(resourceType, elementsToInclude)")
                    .assign("jsonObject", "elementFilter.apply(jsonObject)")
                ._end()
                ._return("(T) parseResource(resourceType, jsonObject, -1)")
            ._catch("Exception e")
                ._throw("new FHIRParserException(e.getMessage(), getPath(), e)")
            ._end()
        .end();
        
        cb.newLine();
        
        cb.override();
        cb.method(mods("public"), "void", "reset")
            .invoke("stack", "clear", args())
        .end();
        
        cb.newLine();
        
        cb.method(mods("protected"), "Resource", "parseResource", params("java.lang.String elementName", "JsonObject jsonObject", "int elementIndex"));
        cb._if("jsonObject == null");
        cb._return("null");
        cb._end();
        cb.assign("java.lang.String resourceType", "getResourceType(jsonObject)");
        cb._switch("resourceType");
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
        
        cb.method(mods("protected"), "void", "stackPush", params("java.lang.String elementName", "int elementIndex"))
            ._if("elementIndex != -1")
                .invoke("stack", "push", args("elementName + \"[\" + elementIndex + \"]\""))
            ._else()
                .invoke("stack", "push", args("elementName"))
            ._end()
            ._if("DEBUG")
                .invoke("System.out", "println", args("getPath()"))
            ._end()
        .end().newLine();
        
        cb.method(mods("protected"), "void", "stackPop")
            .invoke("stack", "pop", args())
        .end().newLine();
        
        generateParseChoiceElementMethod(cb);
        
        cb.method(mods("protected"), "java.lang.String", "getPath")
            .assign("StringJoiner joiner", "new StringJoiner(\".\")")
            ._foreach("java.lang.String s", "stack")
                .invoke("joiner", "add", args("s"))
            ._end()
            ._return("joiner.toString()")
        .end().newLine();
        
        cb.method(mods("protected"), "java.lang.String", "parseJavaString", params("java.lang.String elementName", "JsonString jsonString", "int elementIndex"))
            ._if("jsonString == null")
                ._return("null")
            ._end()
            .invoke("stackPush", args("elementName", "elementIndex"))
            .assign("java.lang.String javaString", "jsonString.getString()")
            .invoke("stackPop", args())
            ._return("javaString")
        .end().newLine();
        
        cb.method(mods("protected"), "void", "checkForUnrecognizedElements", args("java.lang.String typeName", "JsonObject jsonObject"))
            .assign("Set<java.lang.String> elementNames", "JsonSupport.getElementNames(typeName)")
            ._foreach("java.lang.String key", "jsonObject.keySet()")
                ._if("!elementNames.contains(key) && !\"resourceType\".equals(key) && !\"fhir_comments\".equals(key)")
                    .statement("throw new IllegalArgumentException(\"Unrecognized element: '\" + key + \"'\")")
                ._end()
            ._end()
        .end().newLine();
        
        cb.method(mods("protected"), "java.lang.String", "getResourceType", params("JsonObject jsonObject"))
            .assign("JsonString resourceTypeString", "jsonObject.getJsonString(\"resourceType\")")
            ._if("resourceTypeString == null")
                ._throw("new IllegalArgumentException(\"Missing required element: 'resourceType'\")")
            ._end()
            .assign("java.lang.String resourceType", "resourceTypeString.getString()")
            ._try()
                .invoke("ResourceType.ValueSet", "from", args("resourceType"))
            ._catch("IllegalArgumentException e")
                ._throw("new IllegalArgumentException(\"Invalid resource type: '\" + resourceType + \"'\")")
            ._end()
            ._return("resourceTypeString.getString()")
        .end();
        
        JsonObject jsonSupport = buildJsonSupportObject();

        cb._end();
        
        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/FHIRJsonParser.java");

        Map<String, Object> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(config);
        
        // Work around the pseudo hardcoding
        String baseDir = ".";
        if (System.getProperty("BaseDir") != null) {
            baseDir = System.getProperty("BaseDir");
        }
        
        try (FileWriter writer = new FileWriter(file); JsonWriter jsonWriter = jsonWriterFactory.createWriter(new FileWriter(new File(baseDir + "/src/main/resources/json-support.json")))) {
            writer.write(cb.toString());
            jsonWriter.write(jsonSupport);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    
    private void generateParseChoiceElementMethod(CodeBuilder cb) {
        cb.method(mods("protected"), "Element", "parseChoiceElement", params("java.lang.String name", "JsonObject jsonObject", "java.lang.String... choiceTypeNames"));
        cb._if("jsonObject == null")
            ._return("null")
        ._end();
        
        cb.newLine();
        
        cb.assign("java.lang.String elementName", "null");
        cb.assign("java.lang.String _elementName", "null");
        cb.assign("java.lang.String elementType", "null");
        
        cb.newLine();
        
        cb._foreach("java.lang.String choiceTypeName", "choiceTypeNames")
            .assign("java.lang.String key", "name + choiceTypeName")
            ._if("jsonObject.containsKey(key)")
                ._if("elementName != null")
                    ._throw("new IllegalArgumentException()")
                ._end()
                .assign("elementName", "key")
                .assign("elementType", "choiceTypeName")
            ._end()
            
            .newLine()
            
            .assign("java.lang.String _key", "\"_\" + key")
            ._if("jsonObject.containsKey(_key)")
                ._if("_elementName != null")
                    ._throw("new IllegalArgumentException()")
                ._end()
                .assign("_elementName", "_key")
                ._if("elementType == null")
                    .assign("elementType", "choiceTypeName")
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
        cb._switch("elementType");
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
            cb.method(mods("protected"), "void", "parse" + generatedClassName.replace(".", ""), params(generatedClassName + ".Builder builder", "JsonObject jsonObject")); 
        } else {
            if ("Quantity".equals(generatedClassName)) {
                cb.method(mods("protected"), "Quantity", "parseQuantity", params("Quantity.Builder builder", "java.lang.String elementName", "JsonObject jsonObject", "int elementIndex"));
            } else {
                cb.method(mods("protected"), generatedClassName, "parse" + generatedClassName.replace(".", ""), params("java.lang.String elementName", "JsonObject jsonObject", "int elementIndex"));
            }
            cb._if("jsonObject == null")
                ._return("null")
            ._end();
            cb.invoke("stackPush", args("elementName", "elementIndex"));
            cb.invoke("checkForUnrecognizedElements", args(quote(generatedClassName), "jsonObject"));
        }
        
        List<String> args = new ArrayList<>();
        List<JsonObject> requiredElementDefinitions = elementDefinitions.stream().filter(o -> isRequired(o)).collect(Collectors.toList());
        for (JsonObject elementDefinition : requiredElementDefinitions) {
            String elementName = getElementName(elementDefinition, path);
            String fieldName = getFieldName(elementName);
            args.add(fieldName);
            
            String fieldType = getFieldType(structureDefinition, elementDefinition, false)
                    .replace("com.ibm.watsonhealth.fhir.model.type.", "")
                    .replace("com.ibm.watsonhealth.fhir.model.resource.", "");
                        
            if (isBackboneElement(elementDefinition)) {
                fieldType = generatedClassName + "." + fieldType;
            }
            
            String parseMethodInvocation;
            if (isRepeating(elementDefinition)) {
                cb.assign("java.util.List<" + fieldType + "> " + fieldName, "new ArrayList<>()");
                cb.assign("JsonArray " + elementName + "Array", "JsonSupport.getJsonArray(jsonObject, " + quote(elementName) + ")");
                cb._if(elementName + "Array != null");
                cb.assign("int index", "0");
                if (isPrimitiveType(fieldType) || isCodeSubtype(fieldType)) {
                    cb.assign("JsonArray _" + elementName + "Array", "jsonObject.getJsonArray(" + quote("_" + elementName) + ")");
                }
                cb._foreach("JsonValue jsonValue", elementName + "Array");
                parseMethodInvocation = buildParseMethodInvocation(elementDefinition, elementName, fieldType, true);
                cb.invoke(fieldName, "add", args(parseMethodInvocation));
                cb.statement("index++");
                cb._end();
                cb._end();
            } else {
                parseMethodInvocation = buildParseMethodInvocation(elementDefinition, elementName, fieldType, false);
                cb.assign(fieldType + " " + fieldName, parseMethodInvocation);
            }
        }
        
        if (!isAbstract(structureDefinition) && !"Quantity".equals(generatedClassName)) {
            cb.assign(generatedClassName + ".Builder builder", generatedClassName + ".builder(" + String.join(", ", args) + ")");
        }
        
        String superClass = superClassMap.get(generatedClassName);
        if (superClass != null) {
            cb.invoke("parse" + superClass, args("builder", "jsonObject"));
        }
        
        List<JsonObject> optionalElementDefinitions = elementDefinitions.stream().filter(o -> !isRequired(o)).collect(Collectors.toList());
        for (JsonObject elementDefinition : optionalElementDefinitions) {
            String basePath = elementDefinition.getJsonObject("base").getString("path");
            if (elementDefinition.getString("path").equals(basePath)) {
                String elementName = getElementName(elementDefinition, path);
                String fieldName = getFieldName(elementName);            
                String fieldType = getFieldType(structureDefinition, elementDefinition, false)
                        .replace("com.ibm.watsonhealth.fhir.model.type.", "")
                        .replace("com.ibm.watsonhealth.fhir.model.resource.", "");
                
                if (isBackboneElement(elementDefinition)) {
                    fieldType = generatedClassName + "." + fieldType;
                }
                
                String parseMethodInvocation;            
                if (isRepeating(elementDefinition)) {
                    cb.assign("JsonArray " + elementName + "Array", "JsonSupport.getJsonArray(jsonObject, " + quote(elementName) + ")");
                    cb._if(elementName + "Array != null");
                    cb.assign("int index", "0");
                    if (isPrimitiveType(fieldType) || isCodeSubtype(fieldType)) {
                        cb.assign("JsonArray _" + elementName + "Array", "jsonObject.getJsonArray(" + quote("_" + elementName) + ")");
                    }
                    cb._foreach("JsonValue jsonValue", elementName + "Array");
                    parseMethodInvocation = buildParseMethodInvocation(elementDefinition, elementName, fieldType, true);
                    cb.invoke("builder", fieldName, args(parseMethodInvocation));
                    cb.statement("index++");
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
            cb.method(mods("protected"), "Quantity", "parseQuantity", params("java.lang.String elementName", "JsonObject jsonObject", "int elementIndex"));
            cb._return("parseQuantity(Quantity.builder(), elementName, jsonObject, elementIndex)");
            cb.end().newLine();
        }
    }

    private void generatePrimitiveTypeParseMethod(String generatedClassName, JsonObject structureDefinition, CodeBuilder cb) {
        if (isStringSubtype(generatedClassName) || isUriSubtype(generatedClassName) || isCodeSubtype(generatedClassName) || isIntegerSubtype(generatedClassName)) {
            return;
        }
        
        if ("String".equals(generatedClassName) || "Uri".equals(generatedClassName) || "Integer".equals(generatedClassName)) {
            cb.method(mods("protected"), generatedClassName, "parse" + generatedClassName, params(generatedClassName + ".Builder builder", "java.lang.String elementName", "JsonValue jsonValue", "JsonValue _jsonValue", "int elementIndex"));
        } else {
            cb.method(mods("protected"), generatedClassName, "parse" + generatedClassName, params("java.lang.String elementName", "JsonValue jsonValue", "JsonValue _jsonValue", "int elementIndex"));
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
            .invoke("checkForUnrecognizedElements", args(quote("Element"), "jsonObject"))
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
                "Base64Binary".equals(generatedClassName)) {
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
            cb.method(mods("protected"), generatedClassName, "parse" + generatedClassName, params("java.lang.String elementName", "JsonValue jsonValue", "JsonValue _jsonValue", "int elementIndex"))
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
                
                CodeBuilder cb = new CodeBuilder();
                String packageName = "com.ibm.watsonhealth.fhir.model.type";
                cb.javadoc(HEADER, true, true, false).newLine();
                cb._package(packageName).newLine();
                
                cb._import("java.util.Collection");
                cb._import("java.util.Objects").newLine();
                
                cb._class(mods("public"), bindingName, "Code");
                
                List<JsonObject> concepts = getConcepts(valueSet);
                for (JsonObject concept : concepts) {
                    String value = concept.getString("code");
                    String enumConstantName = getEnumConstantName(bindingName, value);
                    String display = getDisplay(concept);
                    if (display != null) {
                        cb.javadocStart().javadoc(display).javadocEnd();
                    }
                    cb.field(mods("public", "static", "final"), bindingName, enumConstantName, bindingName + ".of(ValueSet." + enumConstantName + ")").newLine();
                }
                
                cb.field(mods("private", "volatile"), "int", "hashCode").newLine();
                
                cb.constructor(mods("private"), bindingName, params("Builder builder"))
                    ._super(args("builder"))
                .end().newLine();
                
                cb.method(mods("public", "static"), bindingName, "of", args("java.lang.String value"))
                    ._return(bindingName + ".builder().value(value).build()")
                .end().newLine();
                
                cb.method(mods("public", "static"), bindingName, "of", args("ValueSet value"))
                    ._return(bindingName + ".builder().value(value).build()")
                .end().newLine();
                
                cb.method(mods("public", "static"), "String", "string", args("java.lang.String value"))
                    ._return(bindingName + ".builder().value(value).build()")
                .end().newLine();
                
                cb.method(mods("public", "static"), "Code", "code", args("java.lang.String value"))
                    ._return(bindingName + ".builder().value(value).build()")
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
                cb.end();
                
                cb.method(mods("public"), "Builder", "toBuilder")
                    .assign("Builder builder", "new Builder()")
                    .assign("builder.id", "id")
                    .invoke("builder.extension", "addAll", args("extension"))
                    .assign("builder.value", "value")
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
                    ._return("(Builder) super.value(ValueSet.from(value).value())")
                .end().newLine();
                
                cb.method(mods("public"), "Builder", "value", args("ValueSet value"))
                    ._return("(Builder) super.value(value.value())")
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
                    String display = getDisplay(concept);
                    if (display != null) {
                        cb.javadocStart().javadoc(display).javadocEnd();
                    }
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

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(cb.toString());
                } catch (Exception e) {
                    throw new Error(e);
                }
                
//              System.out.println(cb.toString());
                codeSubtypeClassNames.add(bindingName);
            }
        }
    }

    private void generateVisitorInterface(String basePath) {
        CodeBuilder cb = new CodeBuilder();
        
        String packageName = "com.ibm.watsonhealth.fhir.model.visitor";
        cb.javadoc(HEADER, true, true, false).newLine();
        cb._package(packageName).newLine();
        
        cb._import("java.math.BigDecimal");
        cb._import("java.time.LocalDate");
        cb._import("java.time.LocalTime");
        cb._import("java.time.Year");
        cb._import("java.time.YearMonth");
        cb._import("java.time.ZonedDateTime");
        
        cb.newLine();
        
        cb._import("com.ibm.watsonhealth.fhir.model.resource.*");
        /*
        Collections.sort(typeClassNames);
        
        for (String typeClassName : typeClassNames) {
            cb._import("com.ibm.watsonhealth.fhir.model.type." + typeClassName);
        }
        */
        cb._import("com.ibm.watsonhealth.fhir.model.type.*");
        cb._import("com.ibm.watsonhealth.fhir.model.type.Boolean");
        cb._import("com.ibm.watsonhealth.fhir.model.type.Integer");
        cb._import("com.ibm.watsonhealth.fhir.model.type.String");
        cb.newLine();
        
        cb._interface(mods("public"), "Visitor");
        
        cb.abstractMethod(mods(), "boolean", "preVisit", params("Element element"));
        cb.abstractMethod(mods(), "boolean", "preVisit", params("Resource resource"));
        cb.abstractMethod(mods(), "void", "postVisit", params("Element element"));
        cb.abstractMethod(mods(), "void", "postVisit", params("Resource resource"));
        cb.abstractMethod(mods(), "void", "visitStart", params("java.lang.String elementName", "Element element"));
        cb.abstractMethod(mods(), "void", "visitStart", params("java.lang.String elementName", "Resource resource"));
        cb.abstractMethod(mods(), "void", "visitStart", params("java.lang.String elementName", "java.util.List<? extends Visitable> visitables", "Class<?> type"));
        cb.abstractMethod(mods(), "void", "visitEnd", params("java.lang.String elementName", "Element element"));
        cb.abstractMethod(mods(), "void", "visitEnd", params("java.lang.String elementName", "Resource resource"));
        cb.abstractMethod(mods(), "void", "visitEnd", params("java.lang.String elementName", "java.util.List<? extends Visitable> visitables", "Class<?> type"));
        
        Collections.sort(generatedClassNames);
        
        for (String className : generatedClassNames) {
            if (isPrimitiveSubtype(className)) {
                continue;
            }
            String paramName = camelCase(className).replace(".", "");
            if (SourceVersion.isKeyword(paramName)) {
                paramName = "_" + paramName;
            }
            cb.abstractMethod(mods(), "boolean", "visit", params("java.lang.String elementName", className + " " + paramName));
        }

        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "byte[] value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "BigDecimal value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "java.lang.Boolean value"));
//      cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "org.w3c.dom.Element value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "java.lang.Integer value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "LocalDate value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "LocalTime value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "java.lang.String value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "Year value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "YearMonth value"));
        cb.abstractMethod(mods(), "void", "visit", params("java.lang.String elementName", "ZonedDateTime value"));

        cb._end();
        
        File file = new File(basePath + "/" + packageName.replace(".", "/") + "/Visitor.java");

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

    private List<JsonObject> getConcepts(String url) {
        List<JsonObject> concepts = new ArrayList<>();
        JsonObject valueSet = valueSetMap.get(url);
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
        return concepts;
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
    
    private JsonObject buildJsonSupportObject() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        for (String generatedClassName : generatedClassNames) {
            if (isPrimitiveType(generatedClassName)) {
                continue;
            }
            
            JsonArrayBuilder elementNameArrayBuilder = Json.createArrayBuilder();
            for (String elementName : getElementNames(generatedClassName)) {
                elementNameArrayBuilder.add(elementName);
            }
            
            JsonArrayBuilder requiredElementNameArrayBuilder = Json.createArrayBuilder();
            for (String elementName : getRequiredElementNames(generatedClassName)) {
                requiredElementNameArrayBuilder.add(elementName);
            }
            
            JsonArrayBuilder choiceElementNameArrayBuilder = Json.createArrayBuilder();
            for (String elementName : getChoiceElementNames(generatedClassName)) {
                choiceElementNameArrayBuilder.add(elementName);
            }
            
            builder.add(generatedClassName, Json.createObjectBuilder()
                .add("elementNames", elementNameArrayBuilder)
                .add("requiredElementNames", requiredElementNameArrayBuilder)
                .add("choiceElementNames", choiceElementNameArrayBuilder));            
        }
        
        return builder.build();
    }
    
    private List<String> getChoiceElementNames(String generatedClassName) {
        List<String> choiceElementNames = new ArrayList<>();
        JsonObject structureDefinition = getStructureDefinition(generatedClassName);
        String path = getPath(generatedClassName);
        List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path).stream()
                .filter(e -> isChoiceElement(e))
                .collect(Collectors.toList());
        for (JsonObject elementDefinition : elementDefinitions) {
            String elementName = getElementName(elementDefinition, path);
            choiceElementNames.add(elementName);
        }
        return choiceElementNames;
    }

    private List<String> getElementNames(String generatedClassName) {
        return getElementNames(generatedClassName, false);
    }
    
    private List<String> getRequiredElementNames(String generatedClassName) {
        return getElementNames(generatedClassName, true);
    }
    
    private List<String> getElementNames(String generatedClassName, boolean required) {
        List<String> elementNames = new ArrayList<>();
        
        JsonObject structureDefinition = getStructureDefinition(generatedClassName);
        
        String path = getPath(generatedClassName);
        List<JsonObject> elementDefinitions = getElementDefinitions(structureDefinition, path);
        
        for (JsonObject elementDefinition : elementDefinitions) {
            if (!isRequired(elementDefinition) && required) {
                continue;
            }
            
            String elementName = getElementName(elementDefinition, path);           
            if (isChoiceElement(elementDefinition)) {
                for (String type : getTypes(elementDefinition).stream().map(o -> titleCase(o.getString("code"))).collect(Collectors.toList())) {
                    String choiceElementName = elementName + type;
                    elementNames.add(choiceElementName);
                    if (isPrimitiveType(type)) {
                        elementNames.add("_" + choiceElementName);
                    }
                }
            } else {
                elementNames.add(elementName);
                List<JsonObject> types = getTypes(elementDefinition);
                if (!types.isEmpty()) {
                    String type = types.get(0).getString("code", null);
                    if (type != null && isPrimitiveType(titleCase(type))) {
                        elementNames.add("_" + elementName);
                    }
                }
            }
        }
        
        return elementNames;
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

        if ("Narrative.div".equals(path)) {
//          return "org.w3c.dom.Element";
            return "java.lang.String";
        }
        
        if (path.endsWith("[x]")) {
            return "Element";
        }
        
        if ("Element.id".equals(basePath) || "Extension.url".equals(basePath)) {
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
            fieldType = titleCase(types.get(0).getString("code"));
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
                fieldType = "com.ibm.watsonhealth.fhir.model.type.Code";
            } else if ("Dosage".equals(fieldType) && containsBackboneElement(structureDefinition, "dosage")) {
                fieldType = "com.ibm.watsonhealth.fhir.model.type.Dosage";
            } else if ("Resource".equals(fieldType) && containsBackboneElement(structureDefinition, "resource")) {
                fieldType = "com.ibm.watsonhealth.fhir.model.resource.Resource";
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
    
    private String getPath(String generatedClassName) {
        if ("SimpleQuantity".equals(generatedClassName)) {
            return "Quantity";
        }
        
        JsonObject structureDefinition = getStructureDefinition(generatedClassName);
                
        String path = titleCase(Arrays.asList(generatedClassName.split("\\.")).stream().map(s -> camelCase(s)).collect(Collectors.joining(".")));
        if (isPrimitiveType(structureDefinition)) {
            path = camelCase(path);
        }
        
        return path;
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
        if (binding != null) {
            String valueSet = binding.getString("valueSet");
            String [] tokens = valueSet.split("\\|");
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

    private boolean isBase64Binary(JsonObject structureDefinition) {
        return "base64Binary".equals(structureDefinition.getString("name"));
    }
    
    private boolean isBoolean(JsonObject structureDefinition) {
        return "boolean".equals(structureDefinition.getString("name"));
    }
    
    private boolean isChoiceElement(JsonObject elementDefinition) {
        return elementDefinition.getString("path").endsWith("[x]");
    }
        
    private boolean isCode(JsonObject structureDefinition) {
        return "code".equals(structureDefinition.getString("name"));
    }
    
    private boolean isCodeSubtype(String fieldType) {
        String className = fieldType.replace("com.ibm.watsonhealth.fhir.model.type.", "")
                .replace("com.ibm.watsonhealth.fhir.model.resource.", "")
                .replace("java.util.", "")
                .replace("List<", "")
                .replace(">", "");
        return codeSubtypeClassNames.contains(className);
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
    
    private boolean isPrimitiveSubtype(JsonObject structureDefinition) {
        return isStringSubtype(structureDefinition) || isUriSubtype(structureDefinition) || isIntegerSubtype(structureDefinition);
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
        String className = fieldType.replace("com.ibm.watsonhealth.fhir.model.type.", "")
                .replace("com.ibm.watsonhealth.fhir.model.resource.", "")
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
        return "*".equals(getMax(elementDefinition));
    }
    
    private boolean isRequired(JsonObject elementDefinition) {
        return getMin(elementDefinition) > 0;
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
