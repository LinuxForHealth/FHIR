# FHIR Swagger Generator

This module provides classes for generating Swagger and OpenAPI definitions for the FHIR HTTP interface.
It uses a combination of the HL7-provided specification artifacts and the generated java classes from `fhir-model`.

This module is built into two different jar files. The executable command line interface (cli) version of this module is can also be downloaded directly from [Maven Central](https://repo1.maven.org/maven2/com/ibm/fhir/fhir-swagger-generator) or is available from the Releases tab.  Alternatively, this jar can be built locally and found in the folder - `fhir-swagger-generator/target`.

## Execute the fhir-swagger-generator command line interface (CLI)

The fhir-swagger-generator project builds into an executable jar file that includes all of its dependencies.
By default, the jar executes the FHIROpenApiGenerator main method, which generates OpenAPI 3.0 definitions for each FHIR resource type.
For example, for the 4.6.1 release, execute:

```
java -jar fhir-swagger-generator-4.6.1-cli.jar
```

To limit the number of resource types included in the outputs, pass a list of semicolon-delimited filter strings as program arguments.

For example, to generate definitions for `read, vread, and history` on the Patient API, `create, read, vread, history, and search` on the Contract API, and `read` on the RiskAssessment API, invoke the generator with the following argument: 

```
java -jar fhir-swagger-generator-4.6.1-cli.jar "Patient(read,vread,history);Contract(create,read,vread,history,search);RiskAssessment(read)"
```

To generate Swagger 2.0 definitions instead, execute the org.linuxforhealth.fhir.swagger.generator.FHIRSwaggerGenerator class:

```
java -cp fhir-swagger-generator-4.6.1-cli.jar org.linuxforhealth.fhir.swagger.generator.FHIRSwaggerGenerator [OPTIONAL FILTER]
```

## Output

Both the FHIRSwaggerGenerator and the FHIROpenApiGenerator are designed to generate one interface definition per resource type.
This allows users to mix and match the APIs which they want to expose from API management tools like IBM API Connect.
The FHIRSwagger generates these files at `./swagger/` whereas the OpenApiGenerator places them at `./openapi/`. 

Additionally, the FHIROpenApiGenerator will generate an "all-in-one" definition called all-openapi.json. This is the version of the OpenAPI definition that we ship with our server via the `fhir-openapi` project (e.g. see `fhir-openapi/src/main/webapp/META-INF/openapi.json`).

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