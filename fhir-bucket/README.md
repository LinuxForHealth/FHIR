## Synthetic Data Loader
Scans cloud object storage buckets and uploads data using the FHIR REST API

### Background

Synthea is a project for generating "synthetic" patient/population data for healthcare applications.
It generates realistic data based on census statistics and a lot of configuration.
It supports generating data in FHIR R4 

This "fhir-bucket" project will help you upload Synthea-generated data to a FHIR R4 server.

To facilitate high-volume load scenarios, multiple instances of the application can be run and their work coordinated so that each file is loaded exactly once.

The loader records the identities of the created resources. These identities can be used in other test and load generator applications to access the data.

### Steps

1. Follow the steps at https://github.com/synthetichealth/synthea to clone and install Synthea
2. Configure Synthea to generate FHIR R4 resources
3. Generate a bunch of patients
4. Clone this repo and set it up with Maven/Eclipse
5. Configure the truststore with root certs required to trust connections to your FHIR server, cloud object store and tracking database
5. Tweak application.properties to point at your target server, cloud object storage bucket and tracking
6. Execute Main.class and pass it the path to the directory containing your FHIR R4 bundles   

