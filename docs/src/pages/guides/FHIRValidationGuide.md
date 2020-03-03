---
layout: post
title: FHIR Validation Guide
description: FHIR Validation Guide
date:   2020-01-18 09:25:00 -0400
permalink: /FHIRValidationGuide/
---

## Overview

The IBM FHIR Server Validation component ([fhir-validation](https://github.com/IBM/FHIR/tree/master/fhir-validation)) provides Java APIs for validating FHIR resources using constraints specified in their corresponding structure definitions. For example, in the Patient resource, we have the following constraint:

```
@Constraint(
    id = "pat-1",
    level = "Rule",
    location = "Patient.contact",
    description = "SHALL at least contain a contact's details or a reference to an organization",
    expression = "name.exists() or telecom.exists() or address.exists() or organization.exists()"
)
```

The validation component picks up the Java annotation, pulls out the FHIRPath expression and passes it on to the IBM FHIR Server FHIRPath component ([fhir-path](https://github.com/IBM/FHIR/tree/master/fhir-path)) for evaluation. If the invariant evaluates to `false` then the FHIR validator will generate an OperationOutcome.Issue with the severity set relative to the "level" of the constraint (i.e. "Rule" or "Warning");

![https://ibm.github.io/FHIR/images/fhir-dependency-graph.png](https://ibm.github.io/FHIR/images/fhir-dependency-graph.png)

## Profile Support

The validation component will also validate a resource against profiles that it asserts conformance to in the `Resource.meta.profile` element assuming those profiles are available to the IBM FHIR Server via the FHIR registry component ([fhir-registry](https://github.com/IBM/FHIR/tree/master/fhir-registry)) at runtime.

Given a FHIR profile (structure definition) as input, the IBM FHIR Server Profile component ([fhir-profile](https://github.com/IBM/FHIR/tree/master/fhir-profile)) generates FHIRPath expressions for a number of different types of constraints. The current scope of constraint generation is:

- Cardinality constraints (required and prohibited elements)
- Fixed value constraints (Code and Uri data types)
- Pattern value constraints (CodeableConcept daa type)
- Reference type constraints (FHIRPath resolve/is/conformsTo functions)
- Extension constraints (FHIRPath `conformsTo` function)
- Vocabulary constraints (FHIRPath `memberOf` function)
- Choice type constraints (FHIRPath `as` function)

NOTE: there is currently no support for `closed` or `ordered` slices.

For example, the HL7 bodyweight profile has the following cardinality and fixed value constraints (some details removed for brevity):

```json
{
    "id": "Observation.code.coding:BodyWeightCode.system",
    "path": "Observation.code.coding.system",
    "min": 1,
    "max": "1",
    "type": [{
        "code": "uri"
    }],
    "fixedUri": "http://loinc.org"
}
```
```json
{
    "id": "Observation.code.coding:BodyWeightCode.code",
    "path": "Observation.code.coding.code",
    "min": 1,
    "max": "1",
    "type": [{
        "code": "code"
    }],
    "fixedCode": "29463-7",
}
```
which are used by the ConstraintGenerator to generate the following FHIRPath expression:

```
code.where(coding.where(system = 'http://loinc.org' and code = '29463-7').exists()).exists()
```

The HL7 bodyweight profile has the following reference type constraint (some details removed for brevity):

```json
{
    "id": "Observation.subject",
    "path": "Observation.subject",
    "type": [{
        "code": "Reference",
        "targetProfile": [
            "http://hl7.org/fhir/StructureDefinition/Patient"
        ]
    }]      
}
```
which is used by the ConstraintGenerator to generate the following FHIRPath expression:

```
subject.resolve().is(Patient)
```

Complex reference types are also supported, for example:

```
derivedFrom.exists() implies (derivedFrom.all(resolve().is(DocumentReference) or resolve().is(ImagingStudy) or resolve().is(Media) or resolve().is(QuestionnaireResponse) or resolve().is(MolecularSequence) or resolve().conformsTo('http://hl7.org/fhir/StructureDefinition/vitalsigns')))
```
is generated from the element definition `Observation.derivedFrom` in the same bodyweight profile.

FHIRPath based constraints specified in `StructureDefinition.snapshot.element.constraint` elements, will also be evaluated during profile validation. All of the constraints generated for a given profile are cached in memory so that they can be reused to validate multiple resources that are asserting conformance to the same profile.

### Making profiles available to the FHIR registry component (FHIRRegistry)

The FHIR registry component keeps track of definitional resource types (e.g. StructureDefinition, ValueSet, CodeSystem, etc.). It uses the Java ServiceLoader to look for implementations of the FHIRRegistryResourceProvider interface:

```
public interface FHIRRegistryResourceProvider {
    Collection<FHIRRegistryResource> getResources();
}
```

The FHIRRegistry loads FHIRRegistryResource instances into a map on startup. The FHIRRegistryResource class lazily loads the underlying resource into memory when it is accessed. Multiple versions of the same resource can be registered. FHIR registry providers can be bundled into a jar file and deployed with the IBM FHIR server in the user lib directory.

## The IBM FHIR Server $validate operation

The IBM FHIR Server provides a basic implementation of the $validate operation that invokes the FHIRValidator via a REST API. The $validate operation will validate against the base specification and any profiles asserted in `Resource.meta.profile`. The optional `profile` parameter is not currently supported.

## ValueSet membership checking (FHIRPath `memberOf` function)

Coded elements (code, Coding, CodeableConcept data types), maybe have a binding element that specifies a ValueSet that that element is bound to. This means that the coded element must have a value that comes from that value set. The FHIR profile component will expand value sets according to the ValueSet [expansion algorithm](http://hl7.org/fhir/valueset.html#expansion) for ValueSets that include CodeSystem resources availble via the FHIR registry component.
