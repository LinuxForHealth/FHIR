

## fhir-path-cli - command line execution of fhirpath over a FHIR Resource

The fhir-path-cli enables commandline processing of a FHIR Path over a FHIR resource and outputs the results. 

The options for the command line utility are:
- `--path 'fhir-path'`
- `--format ['json'|'xml']` Default is 'json'
- `--file path-to-file` The file that is accessible and read.
- `--resource 'resource-payload'` The FHIR resource as a well formed string.
- `--pretty` adds columns and start time and end time of the fhir path request
- `--throw-error` print and throw the stacktrace
- `--help`

For instance, to run the fhir-path-cli on Bundle-2000002.json and extract the ids from the Bundle's resources (as stored in the file).

``` shell
java -jar fhir-path-*-cli.jar --path entry.resource.id --file Bundle-2000002.json --pretty
```

``` text
FHIR Path Executed is: [entry.resource.id]
Start Time [Tue Mar 23 09:20:29 EDT 2021]
-------------------------------------------
Count: [2]
-------------------------------------------
- Result (#) - Value                      -
-------------------------------------------
[1] 1
[2] 1000001
-------------------------------------------
End Time [Tue Mar 23 09:20:29 EDT 2021]
```

Without the `--pretty` option, the results are: 

```
1
1000001
```

For instance, to run the fhir-path-cli using a string.

``` shell
java -jar fhir-path-*-cli.jar --path 'text.status.value' --resource '{
  "resourceType": "Patient",
  "id": "pat1",
  "text": {
    "status": "generated",
    "div": "<div xmlns=\"http://www.w3.org/1999/xhtml\">\n      \n      <p>Patient Donald DUCK @ Acme Healthcare, Inc. MR = 654321</p>\n    \n    </div>"
  },
  "identifier": [
    {
      "use": "usual",
      "type": {
        "coding": [
          {
            "system": "http://terminology.hl7.org/CodeSystem/v2-0203",
            "code": "MR"
          }
        ]
      },
      "system": "urn:oid:0.1.2.3.4.5.6.7",
      "value": "654321"
    }
  ],
  "active": true,
  "name": [
    {
      "use": "official",
      "family": "Donald",
      "given": [
        "Duck"
      ]
    }
  ],
  "gender": "male",
  "photo": [
    {
      "contentType": "image/gif",
      "data": "R0lGODlhEwARAPcAAAAAAAAA/+9aAO+1AP/WAP/eAP/eCP/eEP/eGP/nAP/nCP/nEP/nIf/nKf/nUv/nWv/vAP/vCP/vEP/vGP/vIf/vKf/vMf/vOf/vWv/vY//va//vjP/3c//3lP/3nP//tf//vf///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAAEALAAAAAATABEAAAi+AAMIDDCgYMGBCBMSvMCQ4QCFCQcwDBGCA4cLDyEGECDxAoAQHjxwyKhQAMeGIUOSJJjRpIAGDS5wCDly4AALFlYOgHlBwwOSNydM0AmzwYGjBi8IHWoTgQYORg8QIGDAwAKhESI8HIDgwQaRDI1WXXAhK9MBBzZ8/XDxQoUFZC9IiCBh6wEHGz6IbNuwQoSpWxEgyLCXL8O/gAnylNlW6AUEBRIL7Og3KwQIiCXb9HsZQoIEUzUjNEiaNMKAAAA7"
    }
  ],
  "contact": [
    {
      "relationship": [
        {
          "coding": [
            {
              "system": "http://terminology.hl7.org/CodeSystem/v2-0131",
              "code": "E"
            }
          ]
        }
      ],
      "organization": {
        "reference": "Organization/1",
        "display": "Walt Disney Corporation"
      }
    }
  ],
  "managingOrganization": {
    "reference": "Organization/1",
    "display": "ACME Healthcare, Inc"
  },
  "link": [
    {
      "other": {
        "reference": "Patient/pat2"
      },
      "type": "seealso"
    }
  ]
}'
```

Output is: 

``` shell
generated
```