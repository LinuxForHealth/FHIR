---
layout: post
title:  Bulk export - Converting NDJSON to Parquet
description: Bulk export - Converting NDJSON to Parquet
date:   2022-05-09
---

By Lee Surprenant    |    Published May 10, 2022

# Background
In IBM FHIR Server 4.4.0, we introduced experimental support for ["export to parquet"](https://github.com/IBM/FHIR/issues/1340). The feature was implemented by embedding a single-node Apache Spark cluster and using it to:
1. infer a schema from a collection of JSON resources;
2. write Parquet to Amazon S3 / IBM Cloud Object Storage.

I planned to either split this into a separate component or use an external Spark service for this feature (or both!), but the demand for the feature has not warranted the investment that would require.
Thus, beginning with IBM FHIR Server 4.11.0, the "export to parquet" feature has been removed.

But fear not, the IBM FHIR Server still supports exporting to newline-delimited JSON (NDJSON) on Amazon S3 / IBM Cloud Object Storage and users with access to the bucket can use these same Spark features to convert from NDJSON to Parquet.

# Bulk Export
Bulk export can be performed via HTTP GET or POST and the IBM FHIR Server supports three flavors:
* System export:  `[base]/$export`
* Patient export:  `[base]/Patient/$export`
* Group export:  `[base]/Group/[id]/$export`

The export operations are defined at https://hl7.org/fhir/uv/bulkdata/export.html and usage information can be found in the IBM FHIR Server [Bulk Data Guide](https://ibm.github.io/FHIR/guides/FHIRBulkOperations#export-operation-dollarexport).

For example, to export all Patient and Condition resources from an IBM FHIR Server at example.com:
```
curl --request POST \
  --url 'https://example.com/fhir-server/api/v4/$export' \
  --header 'Authorization: *****' \
  --header 'Content-Type: application/json' \
  --data '{
   "resourceType": "Parameters",
   "parameter": [
       {
           "name": "_type",
           "valueString": "Patient,Condition"
       }
   ]
}'
```

By default, the IBM FHIR Server uses a psuedo-folder structure for the output files of each job. In the example above, it might produce output files like the following in the configured bucket:
* long-job-id/Condition_1.ndjson
* long-job-id/Condition_2.ndjson
* long-job-id/Condition_3.ndjson
* long-job-id/Patient_1.ndjson

Normally, a client would retrieve the exported NDJSON data from the download urls obtained from the `$bulkdata-status` URL in the Location header of the $export response. Users could then copy those files to their own S3 / Cloud Object Storage bucket (or any other Hadoop-compatible storage) for analysis.
Alternatively, privileged users with access to the export bucket can operate directly over the exported files.

# Convert from NDJSON to Parquet via Apache Spark
Given a properly configured Spark environment, converting the exported NDJSON files to Parquet can be done in just a few lines of code.

For example, using pyspark to operate over data in IBM Cloud Object storage in "us-east":
```
import ibmos2spark
from pyspark.sql.functions import *
from pyspark.sql.types import *

credentials = {
    'service_id': cos_api_key['iam_serviceid_crn'],
    'api_key': cos_api_key['apikey'],
    'endpoint': 'https://s3.private.us-east.cloud-object-storage.appdomain.cloud',
    'iam_service_endpoint': 'https://iam.ng.bluemix.net/oidc/token'
}
configuration_name = 'your_config_name'  # Must be unique for each bucket / configuration!
spark_cos = ibmos2spark.CloudObjectStorage(sc, credentials, configuration_name, 'bluemix_cos')

bucket = 'unique-export-bucket'  # The globally-unique bucket with the exported NDJSON
in_files = 'long-job-id/Condition_*.ndjson'  # Note the wildcard pattern!
condition = spark.read.format('json').load(spark_cos.url(in_files, bucket))

out_file = 'condition.parquet'
condition.write.parquet(spark_cos.url(out_file, bucket))
```

The initial read may take some time as Spark must infer the schema from the data.
However, that schema will be saved to the parquet output and, from there, the data can be loaded very quickly.

Spark automatically splits the data into a number of reasonably-sized parquet files (called "bucketing"), but it also provides configuration options so that you can optimize the parquet storage for your particular use.

# Working with FHIR data from Apache Spark
Now that you have the data in a format that works well with Spark, you can use Spark to shape / transform the data into whatever format is most useful for your project. For an example, check out the recording from our FHIR DevDays presentation [FHIR from Jupyter](https://youtu.be/CZe48jUzNY0?t=1149) or jump straight to [the notebooks](https://github.com/Alvearie/FHIR-from-Jupyter).
