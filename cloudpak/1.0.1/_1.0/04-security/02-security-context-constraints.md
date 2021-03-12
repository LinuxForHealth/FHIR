---
title: "SecurityContextConstraints Requirements"
excerpt: ""
categories: security
slug: security-context-constraints
toc: true
---


## SecurityContextConstraints Requirements

By default, the IBM FHIR Server Operator uses the `restricted` SecurityContextConstraints resource.

If desired, the following custom SecurityContextConstraints resource can be applied and used instead.

```yaml
apiVersion: security.openshift.io/v1
kind: SecurityContextConstraints
metadata:
  name: ibm-fhir-server-operator-scc
  annotations:
    kubernetes.io/description: ibm-fhir-server-operator-scc denies access to all
      host features and requires pods to be run with a UID, and SELinux context
      that are allocated to the namespace, enforces readOnlyRootFilesystem, and
      drops all capabilities.
allowHostDirVolumePlugin: false
allowHostIPC: false
allowHostNetwork: false
allowHostPID: false
allowHostPorts: false
allowPrivilegeEscalation: false
allowPrivilegedContainer: false
allowedCapabilities: []
defaultAddCapabilities: []
groups: []
fsGroup:
  type: MustRunAs
priority: null
readOnlyRootFilesystem: true
requiredDropCapabilities:
  - ALL
runAsUser:
  type: MustRunAsRange
seLinuxContext:
  type: MustRunAs
supplementalGroups:
  type: RunAsAny
users: []
volumes:
  - configMap
  - downwardAPI
  - emptyDir
  - persistentVolumeClaim
  - projected
  - secret
```

To cause the IBM FHIR Server Operator to use the custom SecurityContextConstraints resource.

1. Find the `ibm-fhir-server-operator-sa` ServiceAccount resource in the same namespace as the Operator.

2. Add the following to the rules in the ClusterRole resource that the ServiceAccount resource is bound to, and apply.

```yaml
- apiGroups:
    - security.openshift.io
  resourceNames:
    - ibm-fhir-server-operator-scc
  resources:
    - securitycontextconstraints
  verbs:
    - use
```

* The IBM FHIR Server Operator also creates custom ClusterRole, ClusterRoleBinding, Role, RoleBinding, SecurityContextConstraints, and ServiceAccount resources to ensure separation of duties.
