---
layout: default
title: IBM FHIR Server - Search Configuration Overview
date:   2019-10-09 08:37:05 -0400
permalink: /fhirsearchconfigurationoverview/
markdown: kramdown
---
# IBM FHIR Server - Search Configuration Overview
The [FHIR Specification](https://www.hl7.org/fhir/r4/search.html) defines a set of searchable fields for each resource type, and corresponding RESTful API.  The IBM FHIR Server supports searching using the specification defined search parameters and using tenant-specific search parameters (extensions). 

Additionally, the IBM FHIR Server supports searching on additional fields, including:
* fields that are defined in the base specification, which are not configured for search;  
* extension elements that a tenant may add to a standard FHIR resource type; and  
* attributes that you define as part of a custom resource type  

The IBM FHIR Server allows deployers to define search parameters on a tenant-specific basis. This allows each tenant that shares an instance of the FHIR server while maintaining the ability to have their own set of search parameters.

A tenant must provide a [Bundle](https://www.hl7.org/fhir/r4/bundle.html) of [SearchParameter](https://www.hl7.org/fhir/r4/searchparameter.html) resources that define the additional search parameters which describe the searchable field and define the FHIRPath expression for extraction.  For example, a tenant that extends the `Patient` resource type with the `favorite-color` extension, enables search on `favorite-color` by defining a SearchParameter as part of this bundle.

Note, the [composite](https://www.hl7.org/fhir/r4/codesystem-search-param-type.html#search-param-type-composite) and [special](https://www.hl7.org/fhir/r4/codesystem-search-param-type.html#search-param-type-special) SearchParameter types are not supported.

## 1 Configuration
There are three layers of search parameter configuration.  
- specification defined 
- default 
- tenant-specific

The specification defined SearchParameters are embedded as JSON file in the `fhir-search` module.  The file is located at [search-parameters.json](https://github.com/IBM/FHIR/blob/master/fhir-search/src/main/resources/search-parameters.json).  The file has a twin file which maps each SearchParameter attribute to an expected target [Choice](https://www.hl7.org/fhir/formats.html#choice) data type using the [valuetypes-default.json](https://github.com/IBM/FHIR/blob/master/fhir-search/src/main/resources/valuetypes-default.json). 

The valuetypes json is a project defined specification which enables constraining of Choice data types when the extracted attributes are stored or searched.

The default and tenant level configurations are put in the `default` and tenant specific folder, such as `tenant1`. These folders are populated with `extension-search-parameters.json` and `extension-search-parameters-valuetypes.json`.  

The IBM FHIR Server configuration prefers the JSON formatted configuration documents, and implements a specific cache to store the [TenantSpecificSearchParameterCache.java](https://github.com/IBM/FHIR/blob/master/fhir-search/src/main/java/com/ibm/fhir/search/parameters/cache/TenantSpecificSearchParameterCache.java) and [TenantSpecificValueTypesCache.java](https://github.com/IBM/FHIR/blob/master/fhir-search/src/main/java/com/ibm/fhir/search/valuetypes/cache/TenantSpecificValueTypesCache.java). 

Note, the `fhir-search` also supports compartment searches, and stores the CompartmentDefinitions in a bundled resource in [compartments.json](https://github.com/IBM/FHIR/blob/master/fhir-search/src/main/resources/compartments.json).  This configuration changes with specification, and provides no default-level or tenant-level configuration. 

Note, the search-parameters.json and search-parameters.xml in the `fhir-search` module match the latest definition resource from the [FHIR download site](http://hl7.org/fhir/r4/downloads.html). 

### 1.1 Configuration per Tenant 
To configure tenant-specific search parameters, create a file called `extension-search-parameters.json` and `extension-search-parameters-valuetypes.json`, placing it in the `${server.config.dir}/config/<tenant-id>` directory. For example, the `${server.config.dir}/config/acme/extension-search-parameters.json` file would contain the search parameters for the `acme` tenant, while `${server.config.dir}/config/qpharma/extension-search-parameters.json` would contain search parameters used by the `qpharma` tenant.

When the FHIR server processes a request associated with the `acme` tenant, the server is only aware of the set of built-in search parameters (defined by the HL7 FHIR specification) plus the user-defined search parameters defined in the `acme` tenant's extension-search-parameters.json file. Likewise, when processing a request associated with the `qpharma` tenant, the server is only aware of the built-in search parameters plus the user-defined search parameters defined in the `qpharma` tenant's `extension-search-parameters.json` file.

If a tenant-specific extension-search-parameters.json does not exist, the server falls back to the global `extension-search-parameters.json` file found at `${server.config.dir}/config/default/extension-search-parameters.json`.

The FHIR server caches search parameters in memory (organized first by tenant id, then by resource type and search parameter name). Any updates to a tenant's `extension-search-parameters.json` file causes the FHIR server to re-load the tenant's search parameters and refresh the information stored in the cache, without requiring a server re-start. This allows the deployer to deploy a new tenant's `extension-search-parameters.json` or update an existing file without re-starting the FHIR server and any subsequent requests processed by the FHIR server after the updates have been made use the updated search parameters. However, it is important to note that this process **does not** re-index already-created resources that are stored on the FHIR Server. One technique for updating the indices for a given resource type is to `read` and `update` each resource instance with itself, triggering search parameter extraction (and creating a new version of each resource).

#### 1.1.1 Search Parameters Configuration: extension-search-parameters.json 
To configure the FHIR server with one or more custom search parameters, one must create a file called `extension-search-parameters.json` and populate the contents with a Bundle of `SearchParameter` resources.

The `fhir-search` module requires the [expression](https://www.hl7.org/fhir/r4/searchparameter-definitions.html#SearchParameter.expression),  [type](https://www.hl7.org/fhir/r4/searchparameter-definitions.html#SearchParameter.type) and [code](https://www.hl7.org/fhir/r4/searchparameter-definitions.html#SearchParameter.code) be set, as in the following example:
```
{
   "resourceType": "Bundle",
   "id": "searchParams",
   "meta": {
      "lastUpdated": "2019-07-12T22:37:54.724+11:00"
   },
   "type": "collection",
   "entry": [{
      "fullUrl": "http://ibm.com/fhir/SearchParameter/Patient-favorite-color",
      "resource": {
         "resourceType": "SearchParameter",
         "id": "Patient-favorite-color",
         "url": "http://ibm.com/fhir/SearchParameter/Patient-favorite-color",
         "version": "4.0.0",
         "name": "favorite-color",
         "status": "draft",
         "experimental": false,
         "date": "2018-12-27T22:37:54+11:00",
         "publisher": "IBM Server for HL7 FHIR Test",
         "contact": [{
            "telecom": [{
               "system": "url",
               "value": "http://ibm.com/fhir"
            }]
         },
         {
            "telecom": [{
               "system": "url",
               "value": "http://ibm.com/fhir"
            }]
         }],
         "description": "the patient's favorite color",
         "code": "favorite-color",
         "base": ["Patient"],
         "type": "string",
         "xpathUsage": "normal",
         "xpath": "f:Patient/f:extension[@url='http://ibm.com/fhir/extension/Patient/favorite-color']/f:valueString",
         "expression": "Patient.extension.where(url='http://ibm.com/fhir/extension/Patient/favorite-color').value",
         "multipleOr": true,
         "multipleAnd": true,
         "modifier": []
      }
   }
}
```

A few things to note are: 
- The Search Parameter specification includes an XPath entry.  The XPath entry is included in the example, however added for completeness during extraction. 
- The Search Parameter uses `value` which applies to Choice data types which are determined based on the Search Parameter type and the Resource's data type. 
- Each time a resource is created or updated, the FHIR server evaluates the FHIRPath expression applicable to the resource type and indexes the values of the matching elements, making these available via FHIR Search via a parameter code that matches the `code` element on the `SearchParameter` definition.

In the preceding example, extension elements (on a Patient resource) with a url of `http://ibm.com/fhir/extension/Patient/favorite-color` are indexed by the `favorite-color` search parameter. To search for Patients with a favorite color of "pink", users could send an HTTP GET request to a URL like `[base]/api/v1/Patient?favorite-color:exact=pink`.

For more information on search parameters, see the HL7 FHIR specification.

#### 1.1.2 Search Parameters Configuration: extension-search-parameters-valuetypes.json
The extension-search-parameters-valuetypes.json must be configured in concert with each search parameter to identify the expected choice element that are searched. 

For each Search Parameter, there needs to be an entry in the mapping. 
    - `resourceType` - the FHIR resource type. 
    - `name` - the Search Parameter code that matches to this mapping. 
    - `targetClasses` - maps to a specific choice element. 

```
{
    "value-types": "default-extensions",
    "mappings": [{
        "resourceType": "Patient",
        "name": "favorite-color",
        "targetClasses": ["String"]
    }]
}
```

#### 1.1.3 Test
When creating a SearchParameter extension, it is recommended that the FHIRPath expression is checked for validity. Many examples exist in the module `fhir-search`. 

### 1.2 Configuration: Filtering of search parameters
The FHIR server supports the filtering of built-in search parameters (that is, search parameters defined by the HL7 FHIR specification for each resource type). The default behavior of the FHIR server is to consider all built-in search parameters when storing resources or performing search results, but you can configure inclusion filters to restrict the FHIR server's view to specific search parameters on a resource type basis. This filtering feature does not apply to user-defined search parameters in the extension-search-parameters.json file. User-defined search parameters are always included in the FHIR server's view regardless of the configured inclusion filters.

Why would you want to filter built-in search parameters? The answer lies in how search parameters are used by the FHIR server. When the FHIR server processes a _create_ or _update_ operation, it stores the resource contents in the datastore, along with search index information that is used by the FHIR server when performing search operations. The search index information stored for a particular resource instance is driven by the search parameters defined for that resource type. Therefore if you are storing a resource whose type has a lot of built-in search parameters defined for it (e.g. `Patient`), then you could potentially be storing a lot of search index information for each resource.

For performance and scalability reasons, it might be desirable to limit the number of search parameters considered during a _create_ or _update_ operation for particular resource types, if those search parameters will never be used in search operations. After all, if there will be no need to use the search index information, there's no need to store it. For example, if you know that due to the way in which a particular tenant's applications use the FHIR REST API that those applications will never need to search Patients by birthdate, then there would be no need to store search index information for the `birthdate` attribute in `Patient` resources. And consequently, you could filter out the `birthdate` search parameter for the `Patient` resource type and not lose any needed functionality, but yet save a little bit of storage capacity in your datastore.

The search parameter filtering feature is supported through a set of inclusion rules specified via the `fhirServer/searchParameterFilter` configuration property. The search parameter inclusion rules allow you to define a set of search parameter names per resource type that should be included in the FHIR server's view of search parameters when storing resources and performing search operations. The rules also allow you to specify a wildcard for resource types and also for search parameter names. The following example shows the general form for specifying inclusion rules:

```
{
    "fhirServer": {
   "searchParameterFilter": {
       "<resource type1>": [ "sp-name1", "sp-name2", ..., "sp-namen"],
       "<resource type2>": [ "sp-name1", "sp-name2", ..., "sp-namen"],
       "<resource type3>": [ "*" ],
       "*": [ "*" ]
   }
    }
}
```

The `fhirServer/searchParameterFilter` property is a JSON map where the key represents the resource type, and the value is an array of strings representing search parameter names. The wildcard (`“*”`) can be used either as a resource type name or as a search parameter name.

The following sections presents several examples.

##### 1.2.1 Example 1
In the following example a single inclusion rule uses wildcards to instruct the FHIR server to include any search parameter for any resource type. This example also describes the default behavior of the FHIR server when the `fhirServer/searchParameterFilter` configuration property is not set:

```
{
    "fhirServer": {
   "_comment": "include all search parameters for all resource types (default)",
   "searchParameterFilter": {
       "*": ["*"]
   }
    }
}
```
##### 1.2.2 Example 2
In the following example inclusion rules are specified for a few specific resource types, and then wildcards are used to include search parameters for the remaining resource types:

```
{
    "__comment": "FHIR server configuration",
    "fhirServer": {
   "searchParameterFilter": {
       "Device": [ patient", "organization" ],
       "Observation": [ "code" ],
       "Patient": [ "active", "address", "birthdate", "name" ],
       "*": ["*"]
   }
    }
}
```

For Device resources, only the `patient` and `organization` search parameters are included, while for `Observation` resources, only the `code` search parameter is included. For `Patient` resources, you can see that the `active`, `address`, `birthdate` and `name` search parameters are included. And finally, for any other resource types, all of their built-in search parameters are included in the FHIR server's view when storing resources or performing search operations.

Note that if this example did not include the last inclusion rule, then no other resource type's built-in search parameters would be included.

##### 1.2.3 Summary of the inclusion rules and filtering algorithm

Here are some rules about the rules:

1.   Each rule specifies a resource type name and a JSON array of search parameter names.
2.   A resource type should appear in at most one rule. In other words, you cannot specify two or more inclusion rules with the same resource type name.
3.   At most one rule can specify the wildcard (`*`) for the resource type, and it should appear as the last rule in the map.

This is how the filtering algorithm works:
1.   When retrieving the built-in search parameters for a particular resource type, the FHIR server will retrieve the rule associated with that resource type, if one exists. If one doesn't exist, then the rule for the wildcard resource type (`“*”`) is retrieved if it exists.
2.   If no inclusion rule was found in Step 1 (that is, the resource type in question has no rule and there's no rule containing a wildcard for the resource type), then no built-in search parameters for this resource type will be included in the FHIR server's view of search parameters while storing a resource of performing a search operation.
3.   Using the search parameter names associated with the rule retrieved in Step 1, the FHIR server will apply the rule to each built-in search parameter defined for that resource type.If the search parameter's name is found within the inclusion rule's list of search parameter names or the inclusion rule's list of names includes the wildcard (`“*”`), then the search parameter will be included in the FHIR server's view of search parameters for that resource type.

#### 1.2.4 fhir-server-config.json properties
The following properties are available to configure searchParameterFilter: 

##### 1.2.4.1 Default property values
| Property Name                 | Default value   |
|-------------------------------| ----------------|
|`fhirServer/searchParameterFilter`|`"*": [*]`|

##### 1.2.4.2 Property attributes
| Property Name                 | Tenant-specific? | Dynamic? |
|-------------------------------|------------------|----------|
|`fhirServer/searchParameterFilter`|Y|Y|

In general, the configuration properties for a particular tenant are stored in the `<WLP_HOME>/wlp/usr/servers/fhir-server/config/<tenant-id>/fhir-server-config.json` file, where `<tenant-id>` refers to the tenant's “short name” or tenant id. The global configuration is considered to be associated with a tenant named `default`, so those properties are stored in the `<WLP_HOME>/wlp/usr/servers/fhir-server/config/default/fhir-server-config.json` file. Similarly, tenant-specific search parameters are found at `<WLP_HOME>/wlp/usr/servers/fhir-server/config/<tenant-id>/extension-search-parameters.json` whereas the global/default extension search parameters are at `<WLP_HOME>/wlp/usr/servers/fhir-server/config/default/extension-search-parameters.json`. Search parameters are handled like a single configuration properly; providing a tenant-specific file will override the global/default extension search parameters.

If you have any issues, open an issue with the IBM FHIR Server team. 

<hr>
FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
