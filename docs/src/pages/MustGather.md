---
layout: post
title: Must Gather for the IBM FHIR Server
description: Must Gather for the IBM FHIR Server
date:   2020-11-17
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

