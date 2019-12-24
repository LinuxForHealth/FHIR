# FHIR Swagger Generator

This module provides classes for generating Swagger and/or OpenAPI definitions for the FHIR HTTP interface.

It uses a combination of the HL7-provided specification artifacts and the generated java classes from `fhir-model`.

## Usage

Both the FHIRSwaggerGenerator and the FHIROpenApiGenerator are designed to generate one interface definition (swagger 2.0 or openapi 3.0) per resource type.
The main driver for this approach is the assumption that most users of FHIR will be focused on a specific subset of the FHIR Resources. By default, the FHIRSwagger generates these files at `src/main/resources/swagger` whereas the OpenApiGenerator places them at `src/main/resources/openapi`. 

Additionally, the FHIROpenApiGenerator will generate an "all-in-one" definition called all-openapi.json. This is the version of the OpenAPI definition that we ship with our server via the `fhir-openapi` project (e.g. see `fhir-openapi/src/main/webapp/META-INF/openapi.json`).

To limit the number of resource types included in the output directories (and in the all-in-one mentioned above), you may pass a set of semicolon-delimited 
filter strings as program arguments.

For example, to generate definitions for `read, vread, and history` on the Patient API, `create, read, vread, history, and search` on the Contract API, and `read` on the RiskAssessment API (and no other output), you would invoke the generator with the following argument: 

```
Patient(read,vread,history);Contract(create,read,vread,history,search);RiskAssessment(read)
```

## Design decisions

The FHIR API is [notoriously](https://chat.fhir.org/#narrow/stream/179166-implementers/topic/OpenAPI.20Support) [difficult](https://chat.fhir.org/#narrow/stream/179166-implementers/topic/OpenAPI) to represent in Swagger/OpenAPI. In fact, the value of such an API definition is questionable when the API itself is defined in a balloted HL7 / ISO standard that has such a rich collection of open source clients and servers which provide native support.

Still, the broad adoption of Swagger/OpenAPI in the marketplace has driven a broad awareness and so many IT practitioners expect such an interface definition for all HTTP interfaces. We provide the generators in this module for these tools and practitioners and this has driven some of the key design decisions.

Importantly, the Swagger/OpenAPI definitions generated in this project are NOT a full representation of the full HL7 FHIR HTTP interface, nor of the subset of that API supported by the IBM FHIR Server. For example, here are some of the simplifications made to the schema in order to improve approachability from both a tooling and end user standpoint:
* FHIR supports extensions on primitive data types. However, the JSON serialization for that is a bit interesting, and these primitive extensions aren't used much in practice...so the generated swagger omits those.
* FHIR supports "contained" resources, and so each resource type could technically contain any other resource type within it. However, to properly represent that structure, it would require that every endpoint definition include every single resource and datatype definition in the specification. Instead, we include just the elements for the resource(s) being generated.
* FHIR choice elements with a type of `*` (like Extension.value[x]) can technically reference any FHIR data type, including rare/obscure ones like those defined at https://www.hl7.org/fhir/metadatatypes.html. For simplicity, we omit these from the swagger definition unless the Resource has an explicit field with this type.


## Alternatives

For those wishing to represent the full FHIR API in Swagger/OpenAPI (without the simplifications discussed above), we recommend using "external documentation" as described at https://swagger.io/specification/#externalDocumentationObject. Basically, instead of describing the FHIR schema in the subset of JsonSchema supported by OpenAPI, you just use OpenAPI to describe the interactions and reference the external specification as the documentation of the schema.


FHIR® is the registered trademark of HL7 and is used with the permission of HL7.