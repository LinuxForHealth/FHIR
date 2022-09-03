# End-to-End Automation Framework for FHIR Audit Integration Tests

This document outlines the end-to-end audit automation framework. 

The automation runs with these steps: 

- **Checkout source code** - Checks out the git code and populates the `github` environment variables.
- **Set up java** - Downloads and setup java based on the matrix values `matrix.java`.
- **Setup prerequisites** - This step builds the required artifacts necessary to test the build with the audit. 
- **Server Integration Tests - Additional audit Layers** - The step executes the pre-integration-docker, then integration-test-docker and runs the post-integration-docker scripts.
- **Gather error logs** - This step only runs upon an error condition. 
- **Upload logs** - The step uploads the results of the integration tests and the error condition logs are posted to the job. 

The GitHub Action is parameterized with a matrix for each new audits. Each additional entry in the array ends up creating multiple automation steps which must complete successfully for the workflow. For instance, kafka is parameterized and executes a job for each audit layer value in the matrix (one java x one audit = 1 workflow job). 

``` yaml
    strategy:
    matrix:
        java: [ 'openjdk11' ]
        audit: [ 'kafka' ]
    fail-fast: false
```

Each audit layer that is tested as part of the framework uses the default build files and the files that match the audit name added to the `audit.yml`.

|Filename|Purpose|
|----------|----------------|
|bin/gather-logs.sh|Gathers the logs from the build|
|bin/integration-test.sh|Run after the tests complete to release resources and package tests results|
|`<audit>`/integration-test.sh|Overrides bin/integration-test.sh, replacing the prior test behavior.|
|bin/pre-integration-test.sh|Call the pre-integration-test step for `audit`|
|`<audit>`/pre-integration-test.sh|Run before integration-test.sh to startup image and services for the integration testing|
|bin/post-integration-test.sh|Call the post-integration-test step for `audit`|
|`<audit>`/post-integration-test.sh|Run after integration-test.sh to stop image and services from the integration testing|
|`<audit>`/Dockerfile|The Docker file used in development and end-to-end tests|
|`<audit>`/docker-compose.yml|The Docker Compose file used with the end-to-end tests|
|`<audit>`/resources| Stores files used to support the end-to-end tests. |
|`<audit>`/README.md|Describes the relevant details for this audit layer.|
|`<audit>`/.gitignore|Ignores files related to the audit layer's tests|
|`README.md`|This file describing the framework|

Note, `<audit>` is replaced with your audit layer such as `kafka`. 

Consult the reference implementation (`kafka`) to start a new audit tests. The minimum that must be implemented are the `pre-integration-test.sh`, `post-integration-test.sh`, the `Dockerfile`, `.gitignore` and the `README.md`.

Also, be sure to add an example `fhir-server-config-<audit-name>.json` configuration file to the `fhir-server` project as well. 

## Test the Automation

To test the build, be sure to pre-set `WORKSPACE` with `export WORKSPACE=$(pwd)`.
You must also start Docker, so the image is built that supports the LinuxForHealth FHIR Server.

If you have any questions, please reach out on Zulip.
