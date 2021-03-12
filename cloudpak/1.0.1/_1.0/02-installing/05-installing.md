---
title: "Installing"
excerpt: "Installing IBM FHIR Server."
categories: installing
slug: installing
toc: true
---

The following sections provide instructions about installing IBM FHIR Server on the Red Hat OpenShift Container Platform. The instructions are based on using the OpenShift Container Platform web console and `oc` command line utility.

When deploying in an air-gapped environment, link to airgap instructions.


## Overview

 IBM FHIR Server is an [operator-based](https://kubernetes.io/docs/concepts/extend-kubernetes/operator/){:target="_blank"} release and uses custom resources to define your IBM FHIR Server configurations. The IBM FHIR Server operator uses the custom resources to deploy and manage the entire lifecycle of your IBM FHIR Server instances. Custom resources are presented as YAML configuration documents that define instances of the `IBMFHIRServer` custom resource type.

Installing IBM FHIR Server has two phases:

1. Install the IBM FHIR Server operator: this will deploy the operator that will install and manage your IBM FHIR Server instances.
2. Install one or more instances of IBM FHIR Server by using the operator.

## Before you begin

- Ensure you have set up your environment , including setting up your {{site.data.reuse.openshift_short}}.
- Obtain the connection details for your {{site.data.reuse.openshift_short}} cluster from your administrator.

## Create a project (namespace)

Create a namespace into which the IBM FHIR Server instance will be installed by creating a [project](https://docs.openshift.com/container-platform/4.6/applications/projects/working-with-projects.html){:target="_blank"}.
When you create a project, a namespace with the same name is also created.

Ensure you use a namespace that is dedicated to a single instance of IBM FHIR Server.

A single namespace per instance also allows for finer control of user accesses.

**Important:** Do not use any of the default or system namespaces to install an instance of IBM FHIR Server (some examples of these are: `default`, `kube-system`, `kube-public`, and `openshift-operators`).

## Add the IBM FHIR Server operator to the catalog

Before you can install the IBM FHIR Server operator and use it to create instances of IBM FHIR Server, you must have the IBM Operator Catalog available in your cluster.

If you have other IBM products installed in your cluster, then you already have the IBM Operator Catalog available, and you can continue to [installing](../installing) the IBM FHIR Server operator.

If you are installing IBM FHIR Server as the first IBM product in your cluster, complete the following steps.

To make the IBM FHIR Server operator available in the OpenShift OperatorHub catalog, create the following YAML files and apply them as  follows.

To add the IBM Operator Catalog:

1. Create a file for the IBM Operator Catalog source with the following content, and save as `IBMCatalogSource.yaml`:

   ```
   apiVersion: operators.coreos.com/v1alpha1
   kind: CatalogSource
   metadata:
      name: ibm-operator-catalog
      namespace: openshift-marketplace
   spec:
      displayName: "IBM Operator Catalog"
      publisher: IBM
      sourceType: grpc
      image: docker.io/ibmcom/ibm-operator-catalog
      updateStrategy:
        registryPoll:
          interval: 45m
   ```
2. {{site.data.reuse.openshift_cli_login}}
3. Apply the source by using the following command:

   `oc apply -f IBMCatalogSource.yaml`

The IBM Operator Catalog source is added to the OperatorHub catalog, making the IBM FHIR Server operator available to install.


