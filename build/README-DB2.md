# Using the ibmcom/db2 docker image

Running fhir server integration tests using docker db2.


## Prerequisites

- [Docker](https://www.docker.com)
- `linuxforhealth/fhir-server:latest` (built from the fhir-install module)


## Run

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

## Details

The `pre-integration-test-docker.sh` script will
1. collect fhir-server-config-db2.json and the fhir-operation test jar; to be mounted from the fhir-server docker-compose service
2. build the db2 image, bring it up, create the db, deploy the schema, and allocate a tenant; this updates the fhir-server-config in the process
3. bring up fhir-server and exit when its ready

----

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
