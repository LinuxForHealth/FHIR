BulkData Operation is also the ImplementationGuide for STU2 (2.0.0) http://hl7.org/fhir/uv/bulkdata/STU2/.

The following is a brief template of the V2 Configuration. 

```
{
    ...
        "bulkdata": {
            "legacy": false,
            "enabled": true,
            "core": {
                "api": {
                    "url": "https://localhost:9443/ibm/api/batch",
                    "user": "fhiradmin",
                    "password": "change-password",
                    "truststore": "resources/security/fhirTrustStore.p12",
                    "truststore-password": "change-password", 
                    "trust-all": true
                },
                "cos" : { 
                    "max-resources": 200000,
                    "min-size": 10485760,
                    "max-size": 209715200,
                    "use-server-truststore": true,
                    "request-timeout": 120,
                    "socket-timeout": 20
                },
                "page-size": 100,
                "_comment": "max of 1000",
                "batch-id-encryption-key": "change-password",
                "max-partitions": 3,
                "iam-endpoint": "",
                "fast-tx-timeout": 90000, 
                "max-inputs": 5,
                "systemExportImpl": "none"
                "_systemExportImpl": "none|fast"
            },
            "storageProviders": {
                "default" : {
                    "type": "file",
                    "_typea": "ibm-cos",
                    "_type": "ibm-cos|aws-s3|file|https",
                    "valid-base-urls": [],
                    "file-base": "/Users/<path>/wffh/ol-fhir/wlp/usr/output",
                    "bucket-name": "fhir-bucketname",
                    "location": "us",
                    "endpoint-internal": "https://s3.us-east.cloud-object-storage.appdomain.cloud",
                    "endpoint-external": "https://s3.us-east.cloud-object-storage.appdomain.cloud",
                    "_iam_auth" : {
                        "_comment": "https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-curl",
                        "type": "iam",
                        "iam-api-key": "",
                        "iam-resource-instance-id": ""
                     },
                     "auth" : {
                        "_comment": "https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main",
                        "type": "hmac",
                        "access_key_id": "",
                        "secret_access_key": ""
                     },
                     "_basic_auth" : {
                        "type": "basic",
                        "username": "",
                        "password": ""
                     },
                    "enableParquet": false,
                    "disable-base-url-validation": true,
                    "export-public": true,
                    "disable-operation-outcomes": true,
                    "duplication-check": false, 
                    "validate-resources": false, 
                    "create": false,
                    "presigned": true
                }
            }
        }
    }
}
```


