---
title: "Security"
excerpt: ""
categories: security
slug: security
toc: true
---


* The IBM FHIR Server is a stateless offering. It is the responsibility of the user to ensure that the proper security measures are established when using the server. 

### Data in motion

* All transports used to interact with IBM FHIR Server must be encrypted. TLS 1.2 is recommended. 
* Users are expected to use TLS when configuring their IBM FHIR Server to connect with their database instance.

### Data at rest

* The prerequisite database must have data encryption enabled.  
* Each instance is responsible for Backup and Recovery of the Database and must backup solution specific configurations.