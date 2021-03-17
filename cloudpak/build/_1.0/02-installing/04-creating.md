---
title: "Creating an IBM FHIR Server"
excerpt: "Configure your IBM FHIR Server installation."
categories: installing
slug: creating
toc: true
---

## Prerequisites

Installing an instance of IBM FHIR Server requires an installed IBM FHIR Server Operator.
See [Installing](../installing) for instructions to install the IBM FHIR Server Operator.

Installing an instance of IBM FHIR Server requires at least namespace administration privileges. 

If you intend to use CLI commands, ensure you have the following installed:

  - [IBM Cloud Pak CLI (cloudctl)](https://github.com/IBM/cloud-pak-cli)
  - [Red Hat OpenShift Container Platform CLI (oc)](https://docs.openshift.com/container-platform/4.4/cli_reference/openshift_cli/getting-started-cli.html)

## Creating an instance

Complete the following steps to create an instance of IBM FHIR Server.

### 1. Define IBM FHIR Server configuration

Create Secret resource containing IBM FHIR Server configuration.

1. Define values for the following secret keys:

  - IBM\_FHIR\_SCHEMA\_TOOL\_INPUT

      The value must be set to the contents of the configuration file for IBM FHIR Server Schema Tool.

      See [IBM FHIR Server Schema Tool](https://hub.docker.com/r/ibmcom/ibm-fhir-schematool) for how to create a configuration file.
      
      Put the contents of the configuration file in a file named `persistence.json`.

  - IBM\_FHIR\_SERVER\_CONFIG

      The value must be set to the contents of the fhir-server-config.json configuration file for IBM FHIR Server.

      See [IBM FHIR Server](https://hub.docker.com/r/ibmcom/ibm-fhir-server) for how to create a fhir-server-config.json configuration file.

      Put the contents of the configuration file in a file named `fhir-server-config.json`.

  - IBM\_FHIR\_SERVER\_CERT

      If using Postgres as the database, this value must be set to the public key of the intermediate CA certificate.

      Put the public key of the intermediate CA certificate in a file named `db.cert`.

      If using Db2 as the database, leave the file named `db.cert` empty.

  - IBM\_FHIR\_SERVER\_ADMIN\_PASSWORD

      The value must be set to the admin password to use for the IBM FHIR Server.

      Put the admin password in a file named `admin.txt`.

  - IBM\_FHIR\_SERVER\_USER\_PASSWORD

      The value must be set to the user password to use for the IBM FHIR Server.

      Put the user password in a file named `user.txt`.

  - IBM\_FHIR\_SERVER\_HOST

      The value must be set to the contents of a configuration file for defining the host.
      
      See [Defining the host](#defining-the-host) for how to define the host.
      
      Put the contents of the configuration file in a file named `host.xml`.

      If you do not want to define the host, leave the file named `host.xml` empty.

2. Create the secret from the files.

  ```
  $ oc create secret generic <secret-name> \
      --from-file=IBM_FHIR_SCHEMA_TOOL_INPUT=./persistence.json \
      --from-file=IBM_FHIR_SERVER_CONFIG=./fhir-server-config.json \
      --from-file=IBM_FHIR_SERVER_CERT=./db.cert \
      --from-file=IBM_FHIR_SERVER_ADMIN_PASSWORD=./admin.txt \
      --from-file=IBM_FHIR_SERVER_USER_PASSWORD=./user.txt \
      --from-file=IBM_FHIR_SERVER_HOSTNAME=./host.xml \
      --namespace=<target-namespace>
  ```
  - `<secret-name>` is the name of the secret to contain the IBM FHIR Server configuration.
  - `<target-namespace>` is the target namespace.

### 2. Create IBM FHIR Server instance

Create an instance of IBM FHIR Server of the following methods:

#### Red Hat OpenShift Container Platform web console
 
  1. Log into the OpenShift Container Platform web console using your login credentials.

  2. Navigate to Installed Operators and click on IBM FHIR Server.

  3. Click `Create Instance` on the `IBMFHIRServer` tile.

  4. Enter a name for the instance, and enter the name of the Secret resource containing the IBM FHIR Server configuration.

  5. Click `Create`.

#### Red Hat OpenShift Container Platform CLI

  1. Create an IBMFHIRServer resource in the target namespace by editing the namespace and secret name in the sample file, `files/fhirserver_v1beta1_ibmfhirserver.yaml`, and then apply the file.

  ```
  $ oc apply -f files/fhirserver_v1beta1_ibmfhirserver.yaml
  ```

#### IBM Cloud Pak CLI

  1. Run the apply-custom-resources action.

  ```
  $ cloudctl case launch \
      --case case/ibm-fhir-server-case \
      --namespace <target-namespace> \
      --inventory ibmFhirServerOperator \
      --action apply-custom-resources \
      --args "--secretName <secret-name>"
  ```
  - `<target-namespace>` is the target namespace.
  - `<secret-name>` is the name of the Secret resource containing the IBM FHIR Server configuration.

### 3. Accessing IBM FHIR Server instance

  1. Verify the IBM FHIR Server instance is functional.

  ```
  $ oc get ibmfhirservers -n <target-namespace>
  ```
  - `<target-namespace>` is the target namespace.

    The READY value of "True" indicates the IBM FHIR Server instance is functional.

  2. Connect to the IBM FHIR Server instance.

  ```
  $ oc get services -n <target-namespace>
  ```
  - `<target-namespace>` is the target namespace.

    To make external connections to the IBM FHIR Server instance, either port-forward or create a route to the service.

    See the [IBM FHIR Server User's Guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide) for how to verify that it’s running properly by invoking the `$healthcheck` endpoint.

## Scaling

By default, the deployment will contain 2 replicas.

Use the `oc scale` command to manually scale an IBM FHIR Server deployment.

## Defining the host

It is recommended, for security purposes, to explicitly define the hosts for which the IBM FHIR Server will handle requests.

For any Route resources created for the IBM FHIR Server, ensure those hosts (e.g. test-fhir-server) are represented in the configuration file.

```
<server description="fhir-server">
    <httpEndpoint host="*" httpPort="-1" httpsPort="9443" id="defaultHttpEndpoint" onError="FAIL" />
    <virtualHost id="default_host" allowedFromEndpointRef="defaultHttpEndpoint">
      <hostAlias>*:9443</hostAlias>
      <hostAlias>test-fhir-server:443</hostAlias>
    </virtualHost>
</server>
```

## Deleting an instance

Delete an instance of IBM FHIR Server using the Red Hat OpenShift Container Platform web console.
 
  1. Log into the OpenShift Container Platform web console using your login credentials.

  2. Navigate to `Installed Operators` and click on `IBM FHIR Server`.

  3. Navigate to the `IBMFHIRServer` tab and click on the instance you want to delete.

  4. Select `Delete IBMFHIRServer` from the `Actions` menu.