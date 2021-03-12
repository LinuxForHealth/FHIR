---
title: "Installing IBM FHIR Server"
excerpt: "Try out installing IBM FHIR Server."
categories: installing
slug: trying-out
toc: true
---

* The IBM FHIR Server operator can be installed in an on-line cluster through the OpenShift CLI. 
* Multiple instances of the IBM FHIR Server operator may be deployed into different namespaces, one per namespace.


## Limitations

* The Operator may be deployed into different namespaces, one per namespace.
* The Operator has limited support for IBM FHIR Server configuration.


*Schema upgrades require downtime:* The IBM FHIR Server requires downtime to complete upgrades of the IBM FHIR Server's relational data. During the upgrade Values tables are refreshed, updated and optimized for the workloads that the FHIR specification supports.