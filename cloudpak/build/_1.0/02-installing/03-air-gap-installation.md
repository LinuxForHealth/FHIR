---
title: "Air Gap Installation"
excerpt: "."
categories: installing
slug: air-gap-installation
toc: true
---

Since air gap environments do not have access to the public internet, and therefore no access to DockerHub, the following preparation steps are necessary to make the required images accessable to the Red Hat OpenShift Container Platform cluster.

If the Red Hat OpenShift Container Platform cluster has a Bastion host, ensure that the Bastion host can access:

- The public internet to download the CASE and images.
- The target (air gap) image registry where all the images will be mirrored to.
- The Red Hat OpenShift Container Platform cluster to install the Operator on.

In the absence of a Bastion host, a portable host with access to the public internet may used. By downloading the CASE and images onto the portable host, and then transporting the portable host into the air gap environment, the images can then be mirrored to the target (air gap) image registry.

If using a Bastion host, refer to [Using a Bastion host](#using-a-bastion-host).
If using a portable host, refer to [Using a portable host](#using-a-portable-host).

### Using a Bastion host

#### 1. Prepare the Bastion host

Ensure you have the following installed on the Bastion host:

  - [Docker CLI (docker)](https://docker.com)
  - [IBM Cloud Pak CLI (cloudctl)](https://github.com/IBM/cloud-pak-cli)
  - [Red Hat OpenShift Container Platform CLI (oc)](https://docs.openshift.com/container-platform/4.4/cli_reference/openshift_cli/getting-started-cli.html)
  - [Skopeo (skopeo)](https://github.com/containers/skopeo)

#### 2. Download the CASE

1. Create a local directory in which to save the CASE.

```
$ mkdir -p $HOME/offline
```

2. Save the CASE.

```
$ cloudctl case save --case <case-path> --outputdir $HOME/offline
```
  - `<case-path>` is the path or URL to the CASE to save.

The following output is displayed:
```
Downloading and extracting the CASE ...
- Success
Retrieving CASE version ...
- Success
Validating the CASE ...
- Success
Creating inventory ...
- Success
Finding inventory items
- Success
Resolving inventory items ...
Parsing inventory items
- Success
```

3. Verify the CASE (.tgz) file and images (.csv) file have been downloaded.

```
$ ls $HOME/offline
charts
ibm-fhir-server-case-<version>-charts.csv
ibm-fhir-server-case-<version>-images.csv
ibm-fhir-server-case-<version>.tgz
```
  - `<version>` is the CASE version.

#### 3. Log into cluster

Log into the Red Hat OpenShift Container Platform cluster as a cluster administrator using the `oc login` command.

#### 4. Configure target registry authentication secret

For IBM FHIR Server, all images are available publicly in DockerHub, so no authentication secret for the source (public) registry is needed.

1. Create the authentication secret for the target (air gap) registry.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --inventory ibmFhirServerOperatorSetup \
    --action configure-creds-airgap \
    --args "--registry <target-registry> --user <registry-user> --pass <registry-password>"
```
  - `<case-file>` is the CASE file.
  - `<target-registry>` is the target registry.
  - `<registry-user>` is the username for the target registry.
  - `<registry-password>` is the password for the target registry.

The credentials are saved to `$HOME/.airgap/secrets/<target-registry>.json`

#### 5. Mirror images to target registry

1. Copy the images in the CASE from the source (public) registry to the target (air gap) registry.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --inventory ibmFhirServerOperatorSetup \
    --action mirror-images \
    --args "--registry <target-registry> --inputDir $HOME/offline"
```
  - `<case-file>` is the CASE file.
  - `<target-registry>` is the target registry.

#### 6. Configure cluster to access target registry

1. Configure a global image pull secret and ImageContentSourcePolicy resource.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --namespace openshift-marketplace \
    --inventory ibmFhirServerOperatorSetup \
    --action configure-cluster-airgap \
    --args "--registry <target-registry> --inputDir $HOME/offline"
```
  - `<case-file>` is the CASE file.
  - `<target-registry>` is the target registry.

*WARNING:* This step may restart all cluster nodes. The cluster resources might be unavailable until the time the new pull secret is applied.

2. Optional: If you are using an insecure target registry, you must add the target registry to the cluster insecureRegistries list.

```
$ oc patch image.config.openshift.io/cluster --type=merge \
    -p '{"spec":{"registrySources":{"insecureRegistries":["'<target-registry>'"]}}}'
```
  - `<target-registry>` is the target registry.

#### 7. Proceed with installation

Now that the air gap installation preparation steps are complete, you may continue with the [IBM FHIR Server Operator installation](../installing).

### Using a portable host

#### 1. Prepare the portable host

Ensure you have the following installed on the portable host:

  - [Docker CLI (docker)](https://docker.com)
  - [IBM Cloud Pak CLI (cloudctl)](https://github.com/IBM/cloud-pak-cli)
  - [Red Hat OpenShift Container Platform CLI (oc)](https://docs.openshift.com/container-platform/4.4/cli_reference/openshift_cli/getting-started-cli.html)
  - [Skopeo (skopeo)](https://github.com/containers/skopeo)

#### 2. Download the CASE

1. Create a local directory in which to save the CASE.

```
$ mkdir -p $HOME/offline
```

2. Save the CASE.

```
$ cloudctl case save --case <case-path> --outputdir $HOME/offline
```
  - `<case-path>` is the path or URL to the CASE to save.

The following output is displayed:
```
Downloading and extracting the CASE ...
- Success
Retrieving CASE version ...
- Success
Validating the CASE ...
- Success
Creating inventory ...
- Success
Finding inventory items
- Success
Resolving inventory items ...
Parsing inventory items
- Success
```

#### 3. Configure portable registry and authentication secret

For IBM FHIR Server, all images are available publicly in DockerHub, so no authentication secret for the source (public) registry is needed.

1. Initialize the portable registry.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --inventory ibmFhirServerOperatorSetup \
    --action init-registry \
    --args "--registry localhost --user <registry-user> --pass <registry-password> \
        --dir $HOME/offline/imageregistry"
```
  - `<case-file>` is the CASE file.
  - `<registry-user>` is the username for the registry, which is initialized to this value.
  - `<registry-password>` is the password for the registry, which is initialized to this value.

2. Start the portable registry.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --inventory ibmFhirServerOperatorSetup \
    --action start-registry \
    --args "--registry localhost --port 443 --user <registry-user> --pass <registry-password> \
        --dir $HOME/offline/imageregistry"
```
  - `<case-file>` is the CASE file.
  - `<registry-user>` is the username for the registry.
  - `<registry-password>` is the password for the registry.

3. Create the authentication secret for the portable registry.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --inventory ibmFhirServerOperatorSetup \
    --action configure-creds-airgap \
    --args "--registry localhost:443 --user <registry-user> --pass <registry-password>"
```
  - `<case-file>` is the CASE file.
  - `<registry-user>` is the username for the registry.
  - `<registry-password>` is the password for the registry.

The credentials are saved to `$HOME/.airgap/secrets/localhost:443.json`

#### 4. Mirror images to portable registry

1. The following step copies the images in the CASE from the source (public) registry to the portable registry.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --inventory ibmFhirServerOperatorSetup \
    --action mirror-images \
    --args "--registry localhost:443 --inputDir $HOME/offline"
```
  - `<case-file>` is the CASE file.

#### 5. Transport portable device

Now that the images are in the portable registry, transport the portable host into the air gap environment.

#### 6. Log into the cluster

Log into the Red Hat OpenShift Container Platform cluster as a cluster administrator using the `oc login` command.

#### 7. Configure target registry authentication secret

1. Create the authentication secret for the target (air gap) registry.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --inventory ibmFhirServerOperatorSetup \
    --action configure-creds-airgap \
    --args "--registry <target-registry> --user <registry-user> --pass <registry-password>"
```
  - `<case-file>` is the CASE file.
  - `<target-registry>` is the target registry.
  - `<registry-user>` is the username for the target registry.
  - `<registry-password>` is the password for the target registry.

The credentials are saved to `$HOME/.airgap/secrets/$TARGET_REGISTRY.json`

#### 8. Mirror images to target registry

1. The following step copies the images in the CASE from the portable registry to the target (air gap) registry.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --inventory ibmFhirServerOperatorSetup \
    --action mirror-images \
    --args "--fromRegistry localhost:443 --registry <target-registry> --inputDir $HOME/offline"
```
  - `<case-file>` is the CASE file.
  - `<target-registry>` is the target registry.

#### 9. Configure cluster to access target registry

1. Configure a global image pull secret and ImageContentSourcePolicy resource.

```
$ cloudctl case launch \
    --case $HOME/offline/<case-file> \
    --namespace openshift-marketplace \
    --inventory ibmFhirServerOperatorSetup \
    --action configure-cluster-airgap \
    --args "--registry <target-registry> --inputDir $HOME/offline"
```
  - `<case-file>` is the CASE file.
  - `<target-registry>` is the target registry.

*WARNING:* This step may restart all cluster nodes. The cluster resources might be unavailable until the time the new pull secret is applied.

2. Optional: If you are using an insecure target registry, you must add the target registry to the cluster insecureRegistries list.

```
$ oc patch image.config.openshift.io/cluster --type=merge \
    -p '{"spec":{"registrySources":{"insecureRegistries":["'<target-registry>'"]}}}'
```
  - `<target-registry>` is the target registry.

#### 10. Proceed with installation

Now that the air gap installation preparation steps are complete, you may continue with the installation [IBM FHIR Server Operator installation](../installing).