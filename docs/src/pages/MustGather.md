---
layout: post
title: Must Gather for the IBM FHIR Server
description: Must Gather for the IBM FHIR Server
date:   2020-12-04
permalink: /MustGather/
---

The IBM FHIR Server - Must-Gather Information

1. Which environment did the problem occur in?
    a. Kubernetes Version (IKS, ROKS)
    b. If not one of the standard environments,
        Please indicate the Operating System and java version in use
        OS:  Windows/Linux/Mac
        Java: (output of "java -fullversion")
    c. The IBM FHIR Version
    d. Docker Version

2. Describe the problem as clearly as you can. Be sure to include:
    a. Steps to reproduce the problem
    b. The actual REST API URL strings (rather than placeholders copied from a test plan, etc.)
        e.g. https://hostname/fhir-server/api/v4
        Only provide as appropriate for sharing publically.

    c. Full stacktraces (include all "caused by" clauses)
    d. The error messages from liberty's messages.log file that are related to the problem
    e. Contents of any applicable FHIR resources (avoid PHI)

    Note: As part of an error response, the IBM FHIR Server typically returns
    an OperationOutcome in the HTTP response, and the error message contains
    a "probeId=xxx" value.   You can search for this probeId value within the messages.log
    and/or trace.log to find the matching server-side messages related to the problem.

3. If possible, please include a trace.log (or snippet) which shows the server activity when the problem occurred.
    The liberty "traceSpecification" string should include `com.ibm.fhir.*=fine`

# Gathering logs on Red Hat OpenShift

To help IBM Support troubleshoot any issues with your *IBM FHIR Server instance* on OpenShift, use the `oc adm must-gather` command to capture the must gather logs. The logs are stored in a folder in the current working directory.

To gather diagnostic logs, run the following commands as Cluster Administrator and replace `YOUR_NAMESPACE` with your namespace that the IBM FHIR Server and the IBM FHIR Server Schematool is running.

1. Log in to your Red Hat OpenShift Container Platform as a cluster administrator by using the oc CLI.

1. Provide the output of the OpenShift version.

```
oc version > version.txt
```

1. Provide the output of the Kubectl version.

```
kubectl version >> version.txt
```

1. Collection information about the nodes.

```
oc get nodes -o wide -n YOUR_NAMESPACE > nodes.txt
```

1. Collect information about the pod statuses

```
oc get pods -n YOUR_NAMESPACE > pods.txt
```

1. Collect information about the pod containers

```
oc get pods -o jsonpath="{..image}"  -n YOUR_NAMESPACE > containerInfo.txt
```

1. Collect the defined secrets

```
oc get secrets  -n YOUR_NAMESPACE > secrets.txt
```

1. Collect the defined persistent volume claims

```
oc get pvc -n YOUR_NAMESPACE > pvcs.txt
```

1. Collect the description and log of any pod you are having issues with:

```
oc describe pod YOUR_POD_NAME -n YOUR_NAMESPACE > describe-YOUR_POD_NAME.txt
oc logs YOUR_POD_NAME -n YOUR_NAMESPACE > log-YOUR_POD_NAME.log
```

1. The oc adm must-gather CLI command collects the information from your cluster that is most likely needed for debugging issues, such as:

```
oc adm must-gather
```

1. To obtain your cluster ID using the OpenShift CLI (oc), run the following command:

```
oc get clusterversion -o jsonpath='{.items[].spec.clusterID}{"\n"}'
```

1. Provide the Custom Resource(CR) .yaml file used by the operator to configure the environment

```
kubectl get YOUR_CONFIG -o yaml > config.yaml
```

1. Obtain detailed description of a pod

```
oc describe pod -n YOUR_NAMESPACE POD_NAME &> POD_NAME_describe.log
```

1. Run the oc adm inspect

```
oc adm inspect
```

Executing commands/bash shell inside a pod:

```
oc exec [flags] -n YOUR_NAMESPACE POD [-c CONTAINER] -- COMMAND [args...]
```

Example: to run a bash shell within the pod and tail the liberty messages.log:

```
     oc exec -ti  ibmfhirserver-as6jql -- /bin/bash 
     bash-4.2$ tail -f /logs/messages.log 
```

Provide the data to your support team. Note, please review and redact any sensistive data before posting in any issue.