# End-to-End Automation Framework for Pesistence Layer Integration Test for Postgres

This document outlines the end-to-end persistence automation framework for [Postgres](https://www.postgresql.org/)

The image is based on the [Postgres Alpine flavor](https://hub.docker.com/layers/postgres/library/postgres/12.2-alpine/images/sha256-8a3017b3d556d338a386b5c7762a5e7a6504665ad30d67907052af11bcffd476?context=explore)

The postgres image is created as a part of docker-compose: 

# Start it up

```
~/fhir/build/persistence/postgres$ docker-compose up -d
Creating network "postgres_backend" with driver "bridge"
Creating postgres_fhirdb_1 ... done
```

# Shut it down

```
~/fhir/build/persistence/postgres$ docker-compose down
Stopping postgres_fhirdb_1 ... done
Removing postgres_fhirdb_1 ... done
Removing network postgres_backend
```

```
docker-compose up -d
WARNING: Some networks were defined but are not used by any service: frontend
Starting postgres_fhirdb_1 ... done
```

## Key Commands

Build
```
docker build . --squash
```

Turn on
```
docker-compose up
```

Turn off
```
docker-compose down
```

Running Locally 
```
docker run --name postgres -d -p 5432:5432 -e POSTGRES_PASSWORD=fred postgres:alpine
```

Connect to the db from commandline
```
\c fhirdb
```

**References**
- [Docker: Postgres](https://hub.docker.com/_/postgres?tab=description)