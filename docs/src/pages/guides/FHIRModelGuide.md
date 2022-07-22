---
slug:  "/FHIR/guides/FHIRModelGuide"
title: "FHIR Model Guide"
date:  "2019-10-25 12:00:00 -0400"
---

## Overview

The FHIR model component provides Java APIs for parsing, building, generating and validating FHIR resources. Java model classes that represent FHIR resources and data types are generated directly from the structure definitions distributed with the spec. All model objects are thread-safe and immutable. Each model class implements the Java builder pattern (Effective Java, Joshua Bloch) and the visitor pattern (GoF). The classes also implement Java equals, hashCode and toString methods. All date/time processing is done using the Java time library.

Many of the data type classes include additional factory methods to facilitate object construction for common use cases. The model includes generated Javadoc comments complete with excerpts taken directly from the specification. Model classes also include Java annotations for constraints (`@Constraint`), required elements (`@Required`), choice element types (`@Choice`) and value set bindings (`@Binding`). Value set bindings are implemented using Code subclasses with constant fields and nested enumerations. Backbone elements are implemented as Java nested classes to keep them organized.

All schema-level (structure, cardinality, value domain) and global (empty resource, empty element) constraint validation is happens during object construction. This means that it is virtually impossible to build a schema invalid FHIR resource using the APIs. Additional constraint validation (invariants, profile, terminology) is performed using the FHIRValidator class. FHIRParser and FHIRGenerator classes are used to parse and generate both JSON and XML formats. FHIRPathEvaluator is a FHIRPath evaluation engine built on an ANTLR4 generated parser. It implements are large portion of the FHIRPath specification and is used for validation and search parameter value extraction.

## Building a Resource using the FHIR Model API

The FHIR model API implements the Builder pattern for constructing Resource instances.

```
Observation bodyWeight = Observation.builder()
    .meta(Meta.builder()
        .profile(Canonical.of("http://hl7.org/fhir/StructureDefinition/bodyweight"))
        .build())
    .status(ObservationStatus.FINAL)
    .effective(DateTime.builder()
        .value("2019-01-01")
        .build())
    .category(CodeableConcept.builder()
        .coding(Coding.builder()
            .system(Uri.of("http://terminology.hl7.org/CodeSystem/observation-category"))
            .code(Code.of("vital-signs"))
            .build())
        .build())
    .code(CodeableConcept.builder()
        .coding(Coding.builder()
            .system(Uri.of("http://loinc.org"))
            .code(Code.of("29463-7"))
            .build())
        .build())
    .value(Quantity.builder()
        .value(Decimal.of(200))
        .system(Uri.of("http://unitsofmeasure.org"))
        .code(Code.of("[lb_av]"))
        .unit("lbs")
        .build())
    .build();
```

In the example above, a number of different builder classes are used:

- `Observation.Builder`
- `DateTime.Builder`
- `CodeableConcept.Builder`
- `Quantity.Builder`

Every type in the model that represents a FHIR resource or element has a corresponding nested, static Builder class used for constructing thread-safe, immutable instances.

Several static factory / utility methods are also used:

- `Canonical.of(...)`
- `Uri.of(...)`
- `Code.of(...)`

Many of the primitive data types contain this type of "helper" method.

## Modifying a model object

Although model objects are immutable, the `toBuilder()` method can be used to construct a builder with the same values.
This builder can then be modified and built into a new model object.
```
Observation modifiedBodyWeight = bodyWeight.toBuilder()
    .value(bodyWeight.getValue().as(Quantity.class).toBuilder()
        .value(Decimal.of(210))
        .build())
    .build();
```

Alternatively, if the fhir-path module is included, objects can be modified via FHIRPathUtil or FHIRPathPatch.
```
// Using FHIRPathUtil
Observation patchedBodyWeight1 = FHIRPathUtil.replace(bodyWeight, "Observation.value.value", Decimal.of(210));

// Using FHIRPathPatch (useful if applying a series of patches to many resource instances)
FHIRPathPatch patch = FHIRPathPatch.builder()
    .replace("Observation.value.value", Decimal.of(210))
    .build();
Observation patchedBodyWeight2 = patch.apply(bodyWeight);
```

## Parsing a Resource from an InputStream or Reader

```
// Parse JSON from InputStream
InputStream in = getInputStream("JSON/bodyweight.json");
Observation observation = FHIRParser.parser(Format.JSON).parse(in);

// Parse JSON from Reader
Reader reader = getReader("JSON/bodyweight.json");
Observation observation = FHIRParser.parser(Format.JSON).parse(reader);
```

The model also supports parsing XML.
If you already have JSON or XML in a java.lang.String, you can wrap that in a StringReader.

## Generating JSON and XML formats from a Resource instance

```
// Generate JSON format
FHIRGenerator.generator(Format.JSON).generate(bodyWeight, System.out);

// Generate XML format
FHIRGenerator.generator(Format.XML).generate(bodyWeight, System.out);
```

The `FHIRGenerator` interface has a separate factory method that takes `boolean prettyPrinting` as a parameter:

```
// Generate JSON format (with pretty printing)
FHIRGenerator.generator(Format.JSON, true).generate(bodyWeight, System.out);
```

To generate either format to a java.lang.String, you can pass the generate method a StringWriter.

## Evaluating FHIRPath expressions on a Resource instance

The fhir-path module implements HL7 FHIRPath 2.0 for evaluating FHIRPath expressions against the model objects.

For example:
```
EvaluationContext evaluationContext = new EvaluationContext(bodyWeight);
Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext, "Observation.value.as(Quantity).value >= 200");
assert(FHIRPathUtil.isTrue(result));
```

The `EvaluationContext` class builds a `FHIRPathTree` from a FHIR resource or element. A `FHIRPathTree` is a tree of labeled nodes that wrap FHIR elements and are used by the FHIRPath evaluation engine (`FHIRPathEvaluator`).

## Validating a Resource instance

Schema-level validation occurs during object construction. This includes validation of cardinality constraints and value domains. Additional validation of constraints specified in the model is performed using the `FHIRValidator` class from the fhir-validation module.

```
Observation observation = getObservation();

List<Issue> issues = FHIRValidator.validator().validate(observation);

for (Issue issue : issues) {
    if (IssueSeverity.ERROR.equals(issue.getSeverity())) {
        // handle error
    }
}
```

See the [Validation Guide](FHIRValidationGuide) for more information.

## Using the FHIR model with JAX-RS

Java and XML - Restful Services (JAX-RS) is an API specification for building and consuming HTTP-based interfaces in Java.
The LinuxForHealth fhir-core module defines FHIR media types (`application/fhir+xml` and `application/json+xml`) and the fhir-provider module implements JAX-RS providers that use the fhir-model Parsers and Generators to read and write these media types.

To integrate the LinuxForHealth JAX-RS providers with your own JAX-RS implementation, you must register a provider. For example, when building a JAX-RS client:
```
ClientBuilder cb = ClientBuilder.newBuilder()
        .register(new FHIRProvider(RuntimeType.CLIENT));
```

There are other providers for working with FHIR data through the jakarta.json API (`FHIRJsonProvider`) and JSONPatch (`FHIRJsonPatchProvider`).