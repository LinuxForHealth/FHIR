# End-to-End Persistence Automation Framework 

This document outlines the end-to-end persistence automation framework. 

The automation runs with these steps: 
- **Setup Pre-Requisites** - This step builds the required artifacts necessary to test the build with the persistence layer. 
- **Run Server Integration Tests with Persistence Layer** - The step starts the pre-integration-docker script, executes the fhir-server-tests and runs the post-integration-docker script.
- **Gather error logs** - This step only runs upon an error condition. 
- **Upload logs** - The step uploads the results of the integration tests and the error condition logs are posted to the job. 

The GitHub Action is parameterized with a matrix for each new persistence layer added.  For instance, postgres is parameterized and executes a job for each persistence layer value in the matrix. 

``` yaml
    strategy:
    matrix:
        java: [ 'openjdk11' ]
        persistence: [ 'postgres' ]
    fail-fast: false
```

Each persistence layer that is tested as part of the framework is expected to implement the files in a folder that matches the persistence name added to the `persistence.yml`.

| Filename | Purpose |
|----------|----------------|
| pre-integration-test-docker.sh | Run before to startup image for testing |
| post-integration-test-docker.sh | Run after the tests complete to release resources and package tests results |
| Dockerfile-`persistence-name` | The Docker file used in development and end-to-end tests |
| docker-compose.yml | The Docker Compose file used with the end-to-end tests |
| resources | Stores files used to support the end-to-end tests |
| README.md | Describes the relevant details |

A reference implementation is `postgres` and `template` is used to start a new persistence layer. 

Be sure to add an example `fhir-server-config-<persistence-name>.json` configuration file to the fhir-server project as well. 

If you have any questions, please reach out on Zulip.