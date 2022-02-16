# $versions operation

The [$versions](https://hl7.org/fhir/capabilitystatement-operation-versions.html) operation allows a client to find out what versions a server supports.

A client invokes the operation with no parameters and the server returns the list of supported versions, along with the default version it will use if no [fhirVersion parameter](https://hl7.org/fhir/http.html#version-parameter) is present. The response format is requested via `Accept` HTTP header, which may be set to `application/fhir+json`, `application/fhir+xml`, `application/json`, `application/xml`. If the `Accept` HTTP header is not set, the default response format is `application/fhir+json`.

## Examples

### JSON (FHIR)

Request:

```
GET [base]/$versions
Accept: application/fhir+json
[other headers]
```

Response:

```
HTTP/1.1 200 OK
[other headers]
```
```json
{
    "resourceType": "Parameters",
    "parameter": [
        {
            "name": "version",
            "valueString": "4.0"
        },
        {
            "name": "version",
            "valueString": "4.3"
        },
        {
            "name": "default",
            "valueString": "4.0"
        }
    ]
}
```

### XML (FHIR)

Request:

```
GET [base]/$versions
Accept: application/fhir+xml
[other headers]
```

Response:

```
HTTP/1.1 200 OK
[other headers]
```
```xml
<Parameters xmlns="http://hl7.org/fhir">
    <parameter>
        <name value="version"/>
        <valueString value="4.0"/>
    </parameter>
    <parameter>
        <name value="version"/>
        <valueString value="4.3"/>
    </parameter>
    <parameter>
        <name value="default"/>
        <valueString value="4.0"/>
    </parameter>
</Parameters>
```

### JSON

For the convenience of non-FHIR clients, responses may also be returned in plain JSON:

Request:

```
GET [base]/$versions
Accept: application/json
[other headers]
```

Response:

```
HTTP/1.1 200 OK
[other headers]
```
```json
{
    "versions": [
        "4.0",
        "4.3"
    ],
    "default": "4.0"
}
```

### XML

For the convenience of non-FHIR clients, responses may also be returned in plain XML:

Request:

```
GET [base]/$versions
Accept: application/xml
[other headers]
```

Response:

```
HTTP/1.1 200 OK
[other headers]
```
```xml
<versions>
    <version>4.0</version>
    <version>4.3</version>
    <default>4.0</default>
</versions>
```
