# Project: fhir-client-app

Project fhir-client-app is a stand-alone implementation wrapping the FHIRClient API. All the dependencies required to invoke FHIRClient operations are packaged into a shaded JAR file `fhir-client-app-{version}-cli.jar`.

This app is currently EXPERIMENTAL. It is used internally to assist development and debugging activities.

## Running fhir-client-app

The application is not built by default. To build:

```
WORKSPACE=/path/to/git/FHIR
cd ${WORKSPACE}
mvn clean install -f fhir-examples
mvn clean install -f fhir-parent
mvn clean install -f fhir-client-app
```

The FHIRClient uses several values typically stored in a properties file. To get started, use the `test.properties` file from the fhir-server-test project. Copy the following files to your working runtime directory:

```
cp ${WORKSPACE}/fhir-server-test/src/test/resources/test.properties .
cp ${WORKSPACE}/fhir-server-test/src/test/resources/fhirClientKeyStore.p12 .
cp ${WORKSPACE}/fhir-server-test/src/test/resources/fhirClientTrustStore.p12 .
```

The default configuration will allow you to connect to a local (development) instance of your FHIR server.

To fetch a known patient resource and pretty-print the JSON representation:

```
java -jar ${WORKSPACE}/fhir-client-app/target/fhir-client-app-*-cli.jar \
  --client-properties /path/to/clientapp.properties \
  --tenant-id your-tenant-name \
  --set-pretty true \
  --read Patient/AdaDaugherty \
  --print
```

## Usage

The CLI can perform multiple interactions, and the `print` operations apply to the results of the most recent interaction. For example:

```
java -jar ${WORKSPACE}/fhir-client-app/target/fhir-client-app-*-cli.jar \
  --client-properties /path/to/clientapp.properties \
  --tenant-id your-tenant-name \
  --set-pretty true \
  --vread Patient/AdaDaugherty/1 \
  --print
  --vread Patient/AdaDaugherty/2 \
  --print
```

The above command will VREAD version 1 of the named patient, pretty-print the returned resource as JSON, then VREAD version 2 and pretty-print the returned resource as JSON.

When performing a CREATE or UPDATE interaction, the value of the resource can be provided in one of three ways:

1. --resource '{ ... }' - the resource value is provided as a single argument
2. --resource-file {file-name} - the resource value is read from the named file
3. --resource-stdin - the resource value is read from standard input

## Options

| Option | Description |
| ------ | ----------- |
| --client-properties {property-file} | Reads the FHIRClient properties from the named file |
| --tenant-id {tenant-id} | Sets the tenant-id to use for each request. Not required if using the default tenant |
| --set-pretty {flag} | Sets JSON pretty-printing true/false |
| --property-value {property=value} | Override a FHIRClient property value |
| --read {resource-type}/{logical-id} | Performs a READ interaction for the given resourceType and logicalId |
| --vread {resource-type}/{logical-id}/{version} | Performs a VREAD interaction for the given resourceType, logicalId and version |
| --update {resource-type}/{logical-id} | Performs an UPDATE interaction for the given resource-type and logical-id |
| --create  | Performs a CREATE interaction for the resource value |
| --resource '{ ... }' | the resource value is provided as a single argument |
| --resource-file {file-name} | the resource value is read from the named file |
| --resource-stdin | the resource value is read from standard input |
| --print | Print out value of the resource that was most recently read |
| --print-location | Print the location value in the response header from a CREATE/UPDATE interaction |

Note, actions such as `--print-location` must be specified after the interaction for which you want to show. The order of arguments is important.
