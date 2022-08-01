Add your search parameters within the entry array.    


Here's an example:   

```
	{
            "fullUrl": "http://ibm.com/fhir/SearchParameter/Patient-favorite-color",
            "resource": {
                "resourceType": "SearchParameter",
                "id": "Patient-favorite-color",
                "url": "http://ibm.com/fhir/SearchParameter/Patient-favorite-color",
                "name": "favorite-color",
                "status": "active",
                "description": "test param",
                "code": "favorite-color",
                "base": [
                    "Patient"
                ],
                "type": "string",
                "expression": "Patient.extension.where(url='http://ibm.com/fhir/extension/Patient/favorite-color')",
                "xpath": "f:Patient/f:extension[@url='http://ibm.com/fhir/extension/Patient/favorite-color']",
                "xpathUsage": "normal"
            }
        }
```

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.