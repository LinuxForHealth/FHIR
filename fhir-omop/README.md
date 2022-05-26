# A prototype for mapping FHIR resource data into the OHDSI OMOP CDM

## Set up OHDSI OMOP CDM in PostgreSQL

These are the steps I took to set it up locally:

### Startup postgres
    ```
    docker run -p 5432:5432 --name omop -e POSTGRES_PASSWORD=mysecretpassword -d postgres
    ```

### Download and Install the OHDSI Common Data Model
1. Download the OHDSI CDM DDL from the latest 5.x release from https://github.com/OHDSI/CommonDataModel/releases

2. Unzip, navigate to the postgresql folder, and in each of the 4 files replace `@cdmDatabaseSchema` with your target schema name (or remove it if you're using the default schema which for me is "public")

3. If its 5.4.0 or earlier, read https://github.com/OHDSI/CommonDataModel/issues/452#issuecomment-930805244 and manually fix up the files as suggested

4. Run the DDL.  I used pgAdmin to do it.

5. Download vocabulary stuff from

### Download and load OHDSI vocabulary data
1. Register at https://athena.ohdsi.org/ (for me the confirmation email never came and then came straight away when I tried again the next day)

2. Log in, navigate to DOWNLOAD, select your terminologies, and choose DOWNLOAD VOCABULARIES

3. When its ready, go grab them

4. Disable the foreign key constraints. This is needed due to a circular dependency between DOMAIN.csv and CONCEPT.csv, so you'll need to disable foreign key constraints to load these tables via COPY.
    ```
    ALTER TABLE domain DISABLE TRIGGER ALL
    ```

5. Load the tables. The CONCEPT.csv and CONCEPT_SYNONYM.csv files have some terms that use `'` and some that use `"`, so to import these to our tables using PostgreSQL COPY, we need to set a special char for "QUOTE". I used the following from a psql prompt:

    ```
    \copy domain FROM '/Users/lmsurpre/Downloads/vocabulary_download_v5_{312db386-8083-405f-927b-3a6f6fc367fa}_1653574283988/DOMAIN.csv' WITH DELIMITER E'\t' CSV HEADER QUOTE E'\b' encoding 'UTF8'

    \copy relationship FROM '/Users/lmsurpre/Downloads/vocabulary_download_v5_{312db386-8083-405f-927b-3a6f6fc367fa}_1653574283988/RELATIONSHIP.csv' WITH DELIMITER E'\t' CSV HEADER QUOTE E'\b' encoding 'UTF8'

    \copy vocabulary FROM '/Users/lmsurpre/Downloads/vocabulary_download_v5_{312db386-8083-405f-927b-3a6f6fc367fa}_1653574283988/VOCABULARY.csv' WITH DELIMITER E'\t' CSV HEADER QUOTE E'\b' encoding 'UTF8'

    \copy concept_class FROM '/Users/lmsurpre/Downloads/vocabulary_download_v5_{312db386-8083-405f-927b-3a6f6fc367fa}_1653574283988/CONCEPT_CLASS.csv' WITH DELIMITER E'\t' CSV HEADER QUOTE E'\b' encoding 'UTF8'

    \copy concept FROM '/Users/lmsurpre/Downloads/vocabulary_download_v5_{312db386-8083-405f-927b-3a6f6fc367fa}_1653574283988/CONCEPT.csv' WITH DELIMITER E'\t' CSV HEADER QUOTE E'\b' encoding 'UTF8'

    \copy concept_synonym FROM '/Users/lmsurpre/Downloads/vocabulary_download_v5_{312db386-8083-405f-927b-3a6f6fc367fa}_1653574283988/CONCEPT_SYNONYM.csv' WITH DELIMITER E'\t' CSV HEADER QUOTE E'\b' encoding 'UTF8'

    \copy concept_relationship FROM '/Users/lmsurpre/Downloads/vocabulary_download_v5_{312db386-8083-405f-927b-3a6f6fc367fa}_1653574283988/CONCEPT_RELATIONSHIP.csv' WITH DELIMITER E'\t' CSV HEADER QUOTE E'\b' encoding 'UTF8'

    \copy concept_ancestor FROM '/Users/lmsurpre/Downloads/vocabulary_download_v5_{312db386-8083-405f-927b-3a6f6fc367fa}_1653574283988/CONCEPT_ANCESTOR.csv' WITH DELIMITER E'\t' CSV HEADER QUOTE E'\b' encoding 'UTF8'

    \copy drug_strength FROM '/Users/lmsurpre/Downloads/vocabulary_download_v5_{312db386-8083-405f-927b-3a6f6fc367fa}_1653574283988/DRUG_STRENGTH.csv' WITH DELIMITER E'\t' CSV HEADER QUOTE E'\b' encoding 'UTF8'
    ```

### Verify the results
From your IDE, set your db properties in fhir-omop/src/test/resources/omop.jdbc.propteries.
Then run `VocabServiceTest.java`.
This will print all of the concepts from the database to the console.

From my download:
> count: 1035027

## Run the tool to convert FHIR Resources to
Currently, the tool takes synthea patient bundles as input and outputs a set of CSVs that can be used to load an OMOP CDM.

Ensure your db properties are set in fhir-omop/src/test/resources/omop.jdbc.propteries and then run `GeneratorTest.java`.
This should produce a series of CSV files in the root of the fhir-omop projects:
* care_site.csv
* condition_occurrence.csv
* location.csv
* person.csv
* provider.csv
* visit_occurrence.csv

From my run:
> Processed reference count:   58698
> Unprocessed reference count: 0

## Load the data
