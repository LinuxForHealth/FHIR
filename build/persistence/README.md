# End-to-End Automation Framework for Pesistence Layer Integration Tests

This document outlines the end-to-end persistence automation framework. 

The automation runs with these steps: 

- **Checkout source code** - Checks out the git code and populates the `github` environment variables.
- **Set up java** - Downloads and setup java based on the matrix values `matrix.java`.
- **Setup prerequisites** - This step builds the required artifacts necessary to test the build with the persistence layer. 
- **Server Integration Tests - Additional Persistence Layers** - The step executes the pre-integration-docker, then integration-test-docker and runs the post-integration-docker scripts.
- **Gather error logs** - This step only runs upon an error condition. 
- **Upload logs** - The step uploads the results of the integration tests and the error condition logs are posted to the job. 

The GitHub Action is parameterized with a matrix for each new persistence layer added. Each additional entry in the array ends up greating a multiple of the automation steps which must complete successfully for the workflow. For instance, postgres is parameterized and executes a job for each persistence layer value in the matrix (one java x one persistence = 1 workflow job). 

``` yaml
    strategy:
    matrix:
        java: [ 'openjdk11' ]
        persistence: [ 'postgres' ]
    fail-fast: false
```

Each persistence layer that is tested as part of the framework uses the default build files and the files that match the persistence name added to the `persistence.yml`.

|Filename|Purpose|
|----------|----------------|
|bin/gather-logs.sh|Gathers the logs from the build|
|bin/integration-test.sh|Run after the tests complete to release resources and package tests results|
|`<persistence>`/integration-test.sh|Overrides bin/integration-test.sh, replacing the prior test behavior.|
|bin/pre-integration-test.sh|Call the pre-integration-test step for `persistence`|
|`<persistence>`/pre-integration-test.sh|Run before integration-test.sh to startup image and services for the integration testing|
|bin/post-integration-test.sh|Call the post-integration-test step for `persistence`|
|`<persistence>`/post-integration-test.sh|Run after integration-test.sh to stop image and services from the integration testing|
|`<persistence>`/Dockerfile|The Docker file used in development and end-to-end tests|
|`<persistence>`/docker-compose.yml|The Docker Compose file used with the end-to-end tests|
|`<persistence>`/resources| Stores files used to support the end-to-end tests. |
|`<persistence>`/README.md|Describes the relevant details for this persistence layer.|
|`<persistence>`/.gitignore|Ignores files related to the persistence layer's tests|
|`README.md`|This file describing the framework|

Note, `<persistence>` is replaced with your persistence layer such as `postgres`. 

Consult the reference implementation (`postgres`) to start a new persistence layer. The minimum that must be implemented are the `pre-integration-test.sh`, `post-integration-test.sh`, the `Dockerfile`, `.gitignore` and the `README.md`.

Also, be sure to add an example `fhir-server-config-<persistence-name>.json` configuration file to the `fhir-server` project as well. 

## Test the Automation

To test the build, be sure to pre-set `WORKSPACE` with `export WORKSPACE=\`pwd\``/
You must also start Docker, so the image is built that supports The LinuxForHealth FHIR® Server.

If you have any questions, please reach out on Zulip.
