# Build and Test the LinuxForHealth FHIR Server

This `build` directory contains scripts for executing LinuxForHealth FHIR Continous Integration (CI) tests.

Alternatively, LinuxForHealth FHIR can be built manually from the root of the project using Maven:
```sh
mvn clean install -f fhir-examples

mvn clean install -f fhir-parent
```

The latter command will execute the unit tests for each module.
If you want to skip the tests, add `-DskipTests` to the end of the command.

Once the project is built, the LinuxForHealth FHIR Server container image can be built from the Dockerfile under fhir-install:
```sh
docker build fhir-install -t linuxforhealth/fhir-server
```

## Running the integration tests locally

These commands are expected to work on MacOS and/or Linux. For Windows, use the PowerShell (.ps1) variants instead.

### Prerequisites

- Build the project manually using the Maven commands listed above. This will produce the fhir-server-distribution.zip used
to install the server.

### Run

From the root of the project (aka the "WORKSPACE"):
1. Set up and start the server 
    ```sh
    build/pre-integration-test.sh
    ```

2. Run the tests. 
    ```sh
    mvn test -DskipTests=false -f fhir-server-test
    ```

3. Collect logs and clean up
    ```sh
    build/post-integration-test.sh
    ```

## Running the integraiton tests using docker compose

### Prerequisites

- [Docker](https://www.docker.com)
- `linuxforhealth/fhir-server:latest` (built from the fhir-install module as described above)

### Run

From the root of the project (aka the "WORKSPACE"):
1. Set up and start the containers 
    ```sh
    build/pre-integration-test-docker.sh
    ```
    
   Note: If you are testing NATS notifications, invoke the NATS subscriber via `node fhir-server-test/src/test/nodejs/nats-subscriber`.  If this is your first time, install the dependencies first by installing [Node.js](https://nodejs.org/en/download) (if not already installed) and running `(cd fhir-server-test/src/test/nodejs && npm install)`.  
    
2. Run the tests. 
    ```sh
    mvn test -DskipTests=false -f fhir-server-test
    ```

3. Collect logs and tear down the containers
    ```sh
    build/post-integration-test-docker.sh
    ```

### Details

The `pre-integration-test-docker.sh` script will:
1. Configure volume mounts under build/docker/fhir-server and add the data and configuration needed for the tests.
2. Bring up the containers and configure the database.
3. Wait for the fhir-server healthcheck to pass and then exit.

----

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
