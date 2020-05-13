# End-to-End Automation Framework for Pesistence Layer Integration Test for Postgres

This document outlines the end-to-end persistence automation framework for [Postgres](https://www.postgresql.org/)

The image is based on the [Postgres Alpine flavor](https://hub.docker.com/layers/postgres/library/postgres/12.2-alpine/images/sha256-8a3017b3d556d338a386b5c7762a5e7a6504665ad30d67907052af11bcffd476?context=explore)

The postgres image is created as a part of docker-compose: 

1. 


# Start it up
~/fhir/build/persistence/postgres$ docker-compose up -d
Creating network "postgres_backend" with driver "bridge"
Creating postgres_fhirdb_1 ... done

# Shut it down
``
~/fhir/build/persistence/postgres$ docker-compose down
Stopping postgres_fhirdb_1 ... done
Removing postgres_fhirdb_1 ... done
Removing network postgres_backend


docker-compose up -d
WARNING: Some networks were defined but are not used by any service: frontend
Starting postgres_fhirdb_1 ... done



## Key Commands
docker build . --squash

docker-compose build
docker-compose up
docker-compose down

docker run --name posttest -d -p 5432:5432 -e POSTGRES_PASSWORD=fred postgres:alpine

https://www.liquidweb.com/kb/listing-switching-databases-postgresql/
\c fhirdb
docker run --name postgres -d -p 5432:5432 -e POSTGRES_PASSWORD=postgres postgres:alpine

To upgrade to newer releases:

    Download the updated Docker image:

docker pull sameersbn/postgresql:10-2

    Stop the currently running image:

docker stop postgresql

    Remove the stopped container

docker rm -v postgresql

    Start the updated image

docker run --name postgresql -itd \
  [OPTIONS] \
  sameersbn/postgresql:10-2


  docker exec -it postgresql bash

PG_TRUST_LOCALNET=true

docker kill ----signal=SIGINT foo

https://hub.docker.com/layers/postgres/library/postgres/12.2-alpine/images/sha256-8a3017b3d556d338a386b5c7762a5e7a6504665ad30d67907052af11bcffd476?context=explore



**References**
- [Docker: Postgres](https://hub.docker.com/_/postgres?tab=description)