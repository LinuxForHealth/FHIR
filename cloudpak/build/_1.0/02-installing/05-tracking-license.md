---
title: "Tracking License Usage"
excerpt: "Tracking license."
categories: installing
slug: tracking-license
toc: true
---


License Service is required for monitoring and measuring license usage of IBM FHIR Server in accord with the pricing rule for containerized environments. Manual license measurements are not allowed. Deploy License Service on all clusters where IBM FHIR Server is installed.

The IBM FHIR Server Operator contains an integrated service for measuring the license usage at the cluster level for license evidence purposes.

### Overview

The integrated licensing solution collects and stores the license usage information which can be used for audit purposes and for tracking license consumption in cloud environments. The solution works in the background and does not require any configuration. Only one instance of the License Service is deployed per cluster regardless of the number of Cloud Paks and containerized products that you have installed on the cluster.

### Deploying License Service

Deploy License Service on each cluster where IBM FHIR Server is installed. License Service can be deployed on any Kubernetes cluster. For more information about License Service, how to install and use it, see the [License Service](https://ibm.biz/license_service4containers) documentation.

### Validating if License Service is deployed on the cluster

To ensure license reporting continuity for license compliance purposes make sure that License Service is successfully deployed. It is recommended to periodically verify whether it is active.

To validate whether License Service is deployed and running on the cluster, you can, for example, log into the Red Hat OpenShift Container Platform cluster and run the following command:
```
$ oc get pods --all-namespaces | grep ibm-licensing | grep -v operator
```

The following response is a confirmation of successful deployment:
```
1/1     Running
```

### Archiving license usage data

Remember to archive the license usage evidence before you decommission the cluster where IBM FHIR Server was deployed. Retrieve the audit snapshot for the period when IBM FHIR Server was on the cluster and store it in case of audit.

For more information about the licensing solution, see [License Service documentation](https://www.ibm.com/support/knowledgecenter/SSHKN6/license-service/1.x.x/overview.html).
