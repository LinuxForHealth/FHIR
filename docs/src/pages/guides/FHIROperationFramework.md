---
layout: default
title: IBM FHIR Server - Extended Operations Framework
date:  2020-10-05
---

# IBM FHIR Server - Extended Operations Framework

The HL7 FHIR Specification describes a [framework](https://www.hl7.org/fhir/r4/operations.html) of extended operations for the REST API.

This framework defines REST endpoints at the Base Server (System), scoped to specific resources (Type) and scoped to specific instances. 
- **System** - https://my-server.com/fhir-server/api/v4/$convert - Convert from one format to another
- **Type** - https://my-server.com/fhir-server/api/v4/Patient/$export - Retrieve all the data from the Patient compartment
- **Instance** - https://my-server.com/fhir-server/api/v4/Patient/1-2-3-4/$export - Retrieve a specific Patient data from the Patient compartment data

A list of operations supported by the IBM FHIR Server is listed at https://ibm.github.io/FHIR/Conformance/#extended-operations

## Calling an Operation
There are two types of operation invocation requests: 
- Complex (via POST)
  - All operations can be invoked by passing a Parameters resource instance in the body of the request.
  - For operations with no input parameters, no body is required.
  - For operations with a single input parameter named “resource”, the Parameters wrapper can be omitted.
- Simple (via GET)
  - Only simple input parameters (i.e. no complex datatypes like ‘Identifier’ or ‘Reference’), operations can be invoked via HTTP GET by passing the parameters in the URL.

### Example: Complex

*Request*

``` sh
curl --location --request POST 'https://localhost:9443/fhir-server/api/v4/Patient/$validate' \
--header 'Authorization: Basic CHANGEME' \
--header 'Content-Type: application/json' \
--data-raw '{
 "resourceType": "Parameters",
 "parameter": [
  {
   "name": "resource",
   "resource": {
    "resourceType": "Patient",
    "id": "example",
    "name": [
     {
      "use": "official",
      "family": "Chalmers",
      "given": [
       "Peter",
       "James"
      ]
     }
    ]
   }
  }
 ],
 "meta": {
  "tag": [
   {
    "system": "http://terminology.hl7.org/CodeSystem/v3-ActReason",
    "code": "HTEST",
    "display": "test health data"
   }
  ]
 }
}'
```

*Response*
``` json
{
  "resourceType": "OperationOutcome",
  "id": "NoError",
  "text": {
    "status": "additional",
    "div": "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p>No error</p></div>"
  },
  "issue": [
    {
      "severity": "warning",
      "code": "invariant",
      "details": {
        "text": "dom-6: A resource should have narrative for robust management"
      },
      "expression": [
        "Patient"
      ]
    }
  ]
}
```

### Example: Simple

*Request*
``` sh 
curl --location --request GET 'https://localhost:9443/fhir-server/api/v4/$healthcheck' \
--header 'Authorization: Basic CHANGEME' \
--header 'Content-Type: application/json'
```

*Response* 
``` json 
{
  "resourceType": "OperationOutcome",
  "issue": [
    {
      "severity": "information",
      "code": "informational",
      "details": {
        "text": "All OK"
      }
    }
  ]
}
```

## Inspecting a FHIR Server’s Capability Statement (Operations)
Each FHIR server is expected to have a Capability Statement outlining the System, Type and Instance operations, and advertising them via the metadata endpoint. 

To check the Type and Instance operations, you can check using this curl/jq command. 

`curl -k --location --request GET 'https://localhost:9443/fhir-server/api/v4/metadata' | jq -r '.rest[].resource[].operation[].name' | sort -u`

```
apply
closure
convert
document
expand
export
lookup
subsumes
translate
validate
validate-code
```

To check the System Level operations, you can check using this curl/jq command. 

`curl -k --location --request GET 'https://localhost:9443/fhir-server/api/v4/metadata' | jq -r '.rest[].operation[].name'`

``` 
bulkdata-status
closure
convert
export
healthcheck
hello
import
```

# The Specification in More Detail 

Each Operation per [the specification](https://www.hl7.org/fhir/r4/operations.html#defining) is an [OperationDefinition](https://www.hl7.org/fhir/r4/operationdefinition.html) Resource.

The elements needed are: 
- Unique Name of the Operation - `OperationDefinition.name`
  - For instance, `"name": "import"`
- Code used to activate the Operation - `OperationDefinition.code`
  - For instance, `"code": "import"`
  - The *code* is used in the CapabilityStatement.
- The Context - System, Type, Instance - `OperationDefinition.system`, `Operation.type`, `Operation.instance`
  - For instance, `"system": true`
- Kind - always an `operation` - `OperationDefinition.kind`
- Status - `OperationDefinition.status`, for implementers that are not intending to manage the lifecycle, best to put `draft`
- Parameter - The list of parameters for the Operation - `OperationDefinition.parameter` - [Link](https://www.hl7.org/fhir/r4/operationdefinition-definitions.html#OperationDefinition.parameter)
  - Specificy your Query Parameter
  - Be sure to have a return parameter
  - Typically this is a FHIR Resource

An example operation is [$validate](https://www.hl7.org/fhir/r4/resource-operation-validate.html). The OperationDefinition is at [validation.json](https://github.com/IBM/FHIR/blob/main/operation/fhir-operation-validate/src/main/resources/validate.json).

# IBM FHIR Server Extended Operations Framework

The IBM FHIR Server has integrated the Extended Operations Framework into the code base. 

![Framework](https://raw.githubusercontent.com/wiki/IBM/FHIR/operation/operation-framework.png)

Upon startup of the IBM FHIR Server webapplication, the FHIR Operation Registry uses the Java ServiceLoader framework. Each Operation is registered with the FHIROperationRegistry using `META-INF/services/com.ibm.fhir.server.operation.spi.FHIROperation` which lists each of the classes that extends the `AbstractOperation`.

Upon receiving a request, the REST Layer checks to see if the JAX-RS path parameter `${operationName}` from a System, Type or Instance call is a registered Operation. If the operationName exists and is valid, the framework invokes the specific Operation for that specific Operation type.

The Operation's custom logic is executed using the specific Operation logic, and the Persistence Helper is used to access the helper methods to start and end the transaction. 

The Operation's custom logic builds a valid Parameters resource as a response to the client, and the framework serializes back to the client.

Note, once the Operation is loaded it is available until the server is restarted and redoes the ServiceLoader discovery.

## Code Examples
There are a number of examples in the [IBM/FHIR repo](https://github.com/IBM/FHIR/tree/main/operation).

## Installation of an Operation
The Operations are installed into the `userlib` folder of the IBM FHIR Server. Some solutions are installing the operation as a layer in a docker image, or mount it as a volume.

![install location](https://raw.githubusercontent.com/wiki/IBM/FHIR/operation/installation-location.png)

## Building a FHIR Operation 
To build a FHIR Operation, a great starting point is the GitHub repo.

1. Create a Maven Java project. Prefix the artifactid with `fhir-operation-` and give it a useful name, such as fhir-operation-myop.
2. Add the relevant dependencies, such as `fhir-server`.
The pom.xml should look like: 

``` xml 
<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <artifactId>fhir-operation-myop</artifactId>

    <parent>
        <groupId>com.ibm.fhir</groupId>
        <artifactId>fhir-parent</artifactId>
        <version>4.4.1-SNAPSHOT</version>
        <relativePath>../../fhir-parent</relativePath>
    </parent>

    <dependencies>
        <dependency>
  <groupId>${project.groupId}</groupId>
  <artifactId>fhir-server</artifactId>
  <version>${project.version}</version>
        </dependency>
    </dependencies>

</project>
```

Note, if you are using the persistence layer, you naturally get the fhir-persistence libraries as a dependency. 
Note, if you are including some custom libraries or other libraries, you should shade the dependencies into a single jar or deliverable. 

3. Add your OperationDefinition to the `src/main/resources`, such as healthcare.json. 
4. Extend the AbstractOperation in your package, such as, `com.ibm.fhir.demo.MyOperation`
5. Create the Services Loader file `com.ibm.fhir.server.operation.spi.FHIROperation` in `src/main/resources/META-INF/services/`
6. Put a single line in the package for each of the Operations that are going to be hosted in the package.
  ```
  com.ibm.fhir.demo.MyOperation
  com.ibm.fhir.demo.DemoOperation
  ```
7. Add the constructor, such as: 
``` java
    public HealthcheckOperation() {
        super();
    }
```

8. In the class extending the AbstractOperation, override the `buildOperationDefinition()` to load the OperationDefinition (note you could use the OperationDefinition.Builder to generate the configuration).

``` java
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("healthcheck.json")) {
  return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
  throw new Error(e);
        }
    }
```

9. Override the `doInvoke`. 

``` java
@Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        // Do Business Logic
    }
```

10. Implement your business logic using the invoke Parameters. Every request is secured by a user-group and has an associated tenant/user.

| Method Parameter | How to use it |
|------------|------------|
| `FHIROperationContext operationContext` | If you need access to the URI, HTTP Headers, Request Properties, GET or POST (and subsequent) setting of a response status or body, these are used for decision making|
| `Class<? extends Resource> resourceType` | If an instance or type call, this is non-null, and used to help the business logic make switches between different code paths |
| `String logicalId` | If an instance call, this is non-null, and used to help the business logic make switches between different code paths |
| `String versionId` | If an instance call, this is non-null, and used to help the business logic make switches between different code paths |
| `Parameters parameters` | The input parameters used to trigger specific elements of the code path and used to help select outcomes |
| `FHIRResourceHelpers resourceHelper` | Helper method to call Search using the REST interface (which is the easiest) |

11. Return your response

```
return FHIROperationUtil.getOutputParameters(operationOutcome);
```

If you return a single Resource in the Parameters object, the result is the single resource, such as a single OperationOutcome returned without the response Parameters.

# Tips and Tricks
The following are some tips and tricks:

## Length of Interaction
The framework is best suited for non-long poll requests such that an operation.

If you need to dispatch an operation, use two operations - one request operation, and one status/response operation. 

## Transactions
If you need to access the underlying persistence layer, you should request a transaction, and rollback or end it using on the FHIRPersistenceTransaction. 

``` java
  FHIRPersistence pl =
(FHIRPersistence) operationContext.getProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL);

  FHIRPersistenceTransaction tx = resourceHelper.getTransaction();
  tx.begin();
  
  try {
      // Do Operation
      return FHIROperationUtil.getOutputParameters(operationOutcome);
  } catch (Throwable t) {
      tx.setRollbackOnly();
      throw t;
  } finally {
      tx.end();
  }
```

Naturally you'll have access to any persistence operation which includes the RESTful actions - create, read, _vread, search, et cetra.

## Multitenancy
By default, all tenants have access to an operation. Each Operation, if it has a multi-tenant requirement, must uses the FHIRRequestContext to get access to the Tenant Id and make a decision based on the id.

```
  FHIRRequestContext ctx = FHIRRequestContext.get();
  String tenantId = ctx.getTenantId();
```

## Mutating the Response Code and Non-FHIR Resource Type responses

To break out of the default response, use the `FHIROperationContext` which is ThreadSafe to mutate the Response. 

*Mutated Status*

`operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);`

*Mutated Response* - Non-FHIR

``` java
Response response = Response.status(Status.OK).entity(PollingLocationResponse.Writer.generate(pollingResponse)).type(MediaType.APPLICATION_JSON).build();
operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
```

You can return null as a Parameters object - `return FHIROperationUtil.getOutputParameters(null);`

# Questions and Support
If you need further support, you can reach out to the development team at: 

- [Zulip: ibm](https://chat.fhir.org/#narrow/stream/212434-ibm)
- [GitHub: issues](https://github.com/IBM/FHIR/issues)
