(window.webpackJsonp=window.webpackJsonp||[]).push([[33],{xV4d:function(e,t,r){"use strict";r.r(t),r.d(t,"_frontmatter",(function(){return c})),r.d(t,"default",(function(){return h}));r("tkto"),r("yXV3"),r("pNMO"),r("zKZe"),r("q1tI");var a=r("7ljp"),n=r("013z"),i=(r("qKvR"),["components"]);function o(){return(o=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var r=arguments[t];for(var a in r)Object.prototype.hasOwnProperty.call(r,a)&&(e[a]=r[a])}return e}).apply(this,arguments)}function s(e,t){if(null==e)return{};var r,a,n=function(e,t){if(null==e)return{};var r,a,n={},i=Object.keys(e);for(a=0;a<i.length;a++)r=i[a],t.indexOf(r)>=0||(n[r]=e[r]);return n}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)r=i[a],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(n[r]=e[r])}return n}var c={},l={_frontmatter:c},p=n.a;function h(e){var t=e.components,r=s(e,i);return Object(a.b)(p,o({},l,r,{components:t,mdxType:"MDXLayout"}),Object(a.b)("h1",null,"IBM FHIR Server - Search Configuration Overview"),Object(a.b)("p",null,"The ",Object(a.b)("a",{parentName:"p",href:"https://hl7.org/fhir/R4B/search.html"},"FHIR Specification")," defines a set of searchable fields for each resource type. The IBM FHIR Server supports searching via both specification-defined search parameters and tenant-specific search parameters."),Object(a.b)("p",null,"Specifically, the IBM FHIR Server supports searching on additional fields, including:"),Object(a.b)("ul",null,Object(a.b)("li",{parentName:"ul"},"fields that are defined in the base specification, which are not configured for search; and"),Object(a.b)("li",{parentName:"ul"},"extension elements that a tenant may add to a standard FHIR resource type")),Object(a.b)("p",null,"The IBM FHIR Server allows deployers to define search parameters on a tenant-specific basis. This allows each tenant to share an instance of the IBM FHIR Server while maintaining the ability to have their own set of search parameters. Additionally, specification-defined search parameters can be filtered out in order to avoid the cost of extracting and storing the corresponding indices."),Object(a.b)("p",null,"Tenant search parameters are defined via a ",Object(a.b)("a",{parentName:"p",href:"https://hl7.org/fhir/R4B/bundle.html"},"Bundle")," of ",Object(a.b)("a",{parentName:"p",href:"https://hl7.org/fhir/R4B/searchparameter.html"},"SearchParameter")," resources that define the additional search parameters which describe the searchable field and define the FHIRPath expression for extraction.  For example, a tenant that extends the ",Object(a.b)("inlineCode",{parentName:"p"},"Patient")," resource type with the ",Object(a.b)("inlineCode",{parentName:"p"},"favorite-color")," extension, enables search on ",Object(a.b)("inlineCode",{parentName:"p"},"favorite-color")," by defining a SearchParameter as part of this bundle."),Object(a.b)("h2",null,"1 Configuration"),Object(a.b)("p",null,"Since IBM FHIR Server 4.10.0, ",Object(a.b)("em",{parentName:"p"},"all")," SearchParameter definitions are loaded from the ",Object(a.b)("inlineCode",{parentName:"p"},"fhir-registry")," by the ",Object(a.b)("inlineCode",{parentName:"p"},"fhir-search")," module during server startup.\nThe definitions in the registry can come from the core specification, packaged implementation guides, or other registry resource providers via the FHIRRegistryResourceProvider SPI.\nAlthough FHIRRegistry is dynamic, changes to search parameters in the registry (e.g. addition, removal, or modification of SearchParameter resources) are ",Object(a.b)("em",{parentName:"p"},"NOT")," reflected in the running server. One must restart the server in order to apply the changes. Additionally, to apply the new search parameter configuration to previously-ingested resources, it is necessary to perform ",Object(a.b)("a",{parentName:"p",href:"#2-re-index"},"reindexing"),"."),Object(a.b)("p",null,"In previous versions, only the built-in parameters were loaded from the registry; tenant-specific parameters were loaded from ",Object(a.b)("inlineCode",{parentName:"p"},"extension-search-parameters.json")," files in the tenant config folders."),Object(a.b)("p",null,"For backwards compatibility, IBM FHIR Server 4.10.0 contains a new FHIRRegistryResourceProvider that reads these same ",Object(a.b)("inlineCode",{parentName:"p"},"extension-search-parameters.json")," files and contributes their contents to the registry, so that this same mechanism can still be used."),Object(a.b)("p",null,"The IBM FHIR Server supports compartment search based on CompartmentDefinition resources.\nSince IBM FHIR Server 4.8.1, the IBM FHIR Server will load compartment definitions from the registry.\nIn previous versions, the CompartmentDefinition resources came from a configuration file."),Object(a.b)("h3",null,"1.1 Tenant-specific parameters"),Object(a.b)("p",null,"To configure tenant-specific search parameters, create a file called ",Object(a.b)("inlineCode",{parentName:"p"},"extension-search-parameters.json"),", populate it with a Bundle of ",Object(a.b)("inlineCode",{parentName:"p"},"SearchParameter")," resources, and place it in the ",Object(a.b)("inlineCode",{parentName:"p"},"${server.config.dir}/config/<tenant-id>")," directory. For example, the ",Object(a.b)("inlineCode",{parentName:"p"},"${server.config.dir}/config/acme/extension-search-parameters.json")," file would contain the search parameters for the ",Object(a.b)("inlineCode",{parentName:"p"},"acme")," tenant, while ",Object(a.b)("inlineCode",{parentName:"p"},"${server.config.dir}/config/qpharma/extension-search-parameters.json")," would contain search parameters used by the ",Object(a.b)("inlineCode",{parentName:"p"},"qpharma")," tenant."),Object(a.b)("p",null,"If a tenant-specific extension-search-parameters.json file does not exist, the server falls back to the default ",Object(a.b)("inlineCode",{parentName:"p"},"extension-search-parameters.json")," file found at ",Object(a.b)("inlineCode",{parentName:"p"},"${server.config.dir}/config/default/extension-search-parameters.json"),". For performance reasons, we recommend having an ",Object(a.b)("inlineCode",{parentName:"p"},"extension-search-parameters.json")," for each tenant."),Object(a.b)("h4",null,"1.2 SearchParameter details"),Object(a.b)("p",null,"The ",Object(a.b)("inlineCode",{parentName:"p"},"fhir-search")," module requires that the ",Object(a.b)("a",{parentName:"p",href:"https://hl7.org/fhir/R4B/searchparameter-definitions.html#SearchParameter.expression"},"expression"),", ",Object(a.b)("a",{parentName:"p",href:"https://hl7.org/fhir/R4B/searchparameter-definitions.html#SearchParameter.type"},"type")," and ",Object(a.b)("a",{parentName:"p",href:"https://hl7.org/fhir/R4B/searchparameter-definitions.html#SearchParameter.code"},"code")," be set, as in the following example:"),Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-json"},'{\n  "fullUrl": "http://ibm.com/fhir/SearchParameter/Patient-favorite-color",\n  "resource": {\n    "resourceType": "SearchParameter",\n    "id": "Patient-favorite-color",\n    "url": "http://ibm.com/fhir/SearchParameter/Patient-favorite-color",\n    "version": "4.0.0",\n    "name": "favorite-color",\n    "status": "draft",\n    "experimental": false,\n    "date": "2018-12-27T22:37:54+11:00",\n    "publisher": "IBM FHIR Server Test",\n    "contact": [{\n      "telecom": [{\n        "system": "url",\n        "value": "http://ibm.com/fhir"\n      }]\n    },\n    {\n      "telecom": [{\n        "system": "url",\n        "value": "http://ibm.com/fhir"\n      }]\n    }],\n    "description": "the patient\'s favorite color",\n    "code": "favorite-color",\n    "base": ["Patient"],\n    "type": "string",\n    "xpathUsage": "normal",\n    "xpath": "f:Patient/f:extension[@url=\'http://ibm.com/fhir/extension/Patient/favorite-color\']/f:valueString",\n    "expression": "Patient.extension.where(url=\'http://ibm.com/fhir/extension/Patient/favorite-color\').value",\n    "multipleOr": true,\n    "multipleAnd": true,\n    "modifier": []\n  }\n}\n')),Object(a.b)("p",null,"A couple things to note:"),Object(a.b)("ul",null,Object(a.b)("li",{parentName:"ul"},"This SearchParameter includes an xpath element for completeness, but the IBM FHIR Server does not use the XPath during extraction; it only uses the expression (FHIRPath)."),Object(a.b)("li",{parentName:"ul"},"SearchParameter expressions that select choice type elements should follow FHIRPath rules and omit the specific type suffixed (e.g. use ",Object(a.b)("inlineCode",{parentName:"li"},"value")," and not ",Object(a.b)("inlineCode",{parentName:"li"},"valueString"),").")),Object(a.b)("p",null,"Each time a resource is created, updated or reindexed, the IBM FHIR Server evaluates the FHIRPath expression applicable to the resource type and indexes the values of the matching elements, making these available via a search where the query parameter name matches the ",Object(a.b)("inlineCode",{parentName:"p"},"code")," element on the ",Object(a.b)("inlineCode",{parentName:"p"},"SearchParameter")," definition."),Object(a.b)("p",null,"In the preceding example, extension elements (on a Patient resource) with a url of ",Object(a.b)("inlineCode",{parentName:"p"},"http://ibm.com/fhir/extension/Patient/favorite-color")," are indexed by the ",Object(a.b)("inlineCode",{parentName:"p"},"favorite-color")," search parameter. To search for Patients with a favorite color of “pink”, users could send an HTTP GET request to a URL like ",Object(a.b)("inlineCode",{parentName:"p"},"[base]/Patient?favorite-color:exact=pink"),"."),Object(a.b)("p",null,"When creating the SearchParameter FHIRPath expression, be sure to test both the FHIRPath expression and the search parameter."),Object(a.b)("p",null,"If a search parameter expression extracts an element with a data type that is incompatible with the declared search parameter type, the server skips the value and logs a message. For choice elements, like Extension.value, its recommended to restrict the expression to values of the desired type by using the ",Object(a.b)("inlineCode",{parentName:"p"},"as")," function. For example, to select only Decimal values from the ",Object(a.b)("a",{parentName:"p",href:"http://example.org/decimal"},"http://example.org/decimal")," extension, use an expressions like ",Object(a.b)("inlineCode",{parentName:"p"},"Basic.extension.where(url='http://example.org/decimal').value.as(decimal)"),"."),Object(a.b)("h4",null,"1.2.1 The implicit-system extension"),Object(a.b)("p",null,"The IBM FHIR Server team has introduced a custom SearchParameter extension that can be used to improve search performance for queries that are made against a token SearchParameter without passing a system. Specifically, for SearchParameter resources that index elements of type Code which have a required binding with a single system, adding the following extension to the SearchParameter definition allows the server to infer the system value without requiring end users to explicitly pass it in their queries:"),Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-json"},'{\n    "url": "http://ibm.com/fhir/extension/implicit-system",\n    "valueUri": "http://hl7.org/fhir/account-status"\n}\n')),Object(a.b)("p",null,"See the ",Object(a.b)("a",{parentName:"p",href:"FHIRPerformanceGuide#65-search-examples"},"FHIR Performance Guide")," for more information."),Object(a.b)("h3",null,"1.3 Filtering"),Object(a.b)("p",null,"The IBM FHIR Server supports the filtering of search parameters through ",Object(a.b)("inlineCode",{parentName:"p"},"fhir-server-config.json"),". The default behavior of the IBM FHIR Server is to consider all built-in and tenant-specific search parameters when storing resources or processing search requests, but you can configure inclusion filters to restrict the IBM FHIR Server’s view to specific search parameters on a given resource type."),Object(a.b)("p",null,"Why would you want to filter built-in search parameters? The answer lies in how search parameters are used by the IBM FHIR Server. When the FHIR server processes a ",Object(a.b)("em",{parentName:"p"},"create")," or ",Object(a.b)("em",{parentName:"p"},"update")," operation, it stores the resource contents in the datastore, along with search index information that is used by the IBM FHIR Server when performing search operations. The search index information stored for a particular resource instance is driven by the search parameters defined for that resource type. Therefore if you are storing a resource whose type has a lot of built-in search parameters defined for it (e.g. ",Object(a.b)("inlineCode",{parentName:"p"},"Patient"),"), then you could potentially be storing a lot of search index information for each resource."),Object(a.b)("p",null,"For performance and scalability reasons, it might be desirable to limit the number of search parameters considered during a ",Object(a.b)("em",{parentName:"p"},"create")," or ",Object(a.b)("em",{parentName:"p"},"update")," operation for particular resource types. If there will be no need to use the search index information, there’s no need to store it. For example, if you know that due to the way in which a particular tenant’s applications use the FHIR REST API that those applications will never need to search Patients by birthdate, then there would be no need to store search index information for the ",Object(a.b)("inlineCode",{parentName:"p"},"birthdate")," attribute in ",Object(a.b)("inlineCode",{parentName:"p"},"Patient")," resources. Consequently, you could filter out the ",Object(a.b)("inlineCode",{parentName:"p"},"birthdate")," search parameter for the ",Object(a.b)("inlineCode",{parentName:"p"},"Patient")," resource type and not lose any needed functionality, but yet save a little bit of storage capacity in your datastore."),Object(a.b)("p",null,"The search parameter filtering feature is supported through a set of inclusion rules specified via the ",Object(a.b)("inlineCode",{parentName:"p"},"fhirServer/resources")," property group in ",Object(a.b)("inlineCode",{parentName:"p"},"fhir-server-config.json"),". The search parameter inclusion rules allow you to define a set of search parameters per resource type that should be included in the IBM FHIR Server’s view of search parameters when storing resources and performing search operations. The following snippet shows the general form for specifying inclusion rules:"),Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre"},'"resources": {\n    "open": false,\n    "CarePlan": {\n        "searchParameters": {}\n    },\n    "ExplanationOfBenefit": {\n        "searchParameters": {\n            "_id": "http://hl7.org/fhir/SearchParameter/Resource-id",\n            "_lastUpdated": "http://hl7.org/fhir/SearchParameter/Resource-lastUpdated",\n            "patient": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-patient",\n            "type": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-type",\n            "identifier": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-identifier",\n            "service-date": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-service-date"\n        }\n    },\n    "Patient": {\n        "searchParameters": {}\n    },\n    "RelatedPerson": {\n        "searchParameters": {}\n    }\n}\n')),Object(a.b)("p",null,"The ",Object(a.b)("inlineCode",{parentName:"p"},"fhirServer/resources/<resourceType>/searchParameters")," property group is a JSON map where the key is the search parameter code that will be used to search on this parameter and the value is a canonical URL which resolves to a SearchParameter definition from the ",Object(a.b)("inlineCode",{parentName:"p"},"fhir-registry")," at run-time.\nOmitting this property is equivalent to supporting all search parameters in the server’s registry that apply to the given resource type.\nAn empty object, ",Object(a.b)("inlineCode",{parentName:"p"},"{}"),", can be used to indicate that no search parameters are supported."),Object(a.b)("p",null,"It may be desirable to re-define a single search parameter code. In this case, if you do not wish to filter any other parameters for this type, a value of ",Object(a.b)("inlineCode",{parentName:"p"},'"*": "*"')," can be used to prevent further filtering."),Object(a.b)("p",null,"Additionally, for SearchParameters defined across all resource types (i.e. SearchParameters with a base of type ",Object(a.b)("inlineCode",{parentName:"p"},"Resource"),"), the filter can be applied at this level as well:"),Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre"},'"resources": {\n    "open": false,\n    "CarePlan": {\n        "searchParameters": {}\n    },\n    "ExplanationOfBenefit": {\n        "searchParameters": {\n            "patient": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-patient",\n            "type": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-type",\n            "identifier": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-identifier",\n            "service-date": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-service-date"\n        }\n    },\n    "Patient": {\n        "searchParameters": {}\n    },\n    "RelatedPerson": {\n        "searchParameters": {}\n    },\n    "Resource": {\n        "searchParameters": {\n            "_id": "http://hl7.org/fhir/SearchParameter/Resource-id",\n            "_lastUpdated": "http://hl7.org/fhir/SearchParameter/Resource-lastUpdated"\n        }\n    }\n}\n')),Object(a.b)("p",null,"For reference, the following is the list of codes in this category:"),Object(a.b)("table",null,Object(a.b)("thead",{parentName:"table"},Object(a.b)("tr",{parentName:"thead"},Object(a.b)("th",{parentName:"tr",align:null},"code"))),Object(a.b)("tbody",{parentName:"table"},Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},"_id")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},"_lastUpdated")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},"_profile")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},"_security")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},"_source")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},"_tag")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},"_content")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},"_query")))),Object(a.b)("p",null,"Note: ",Object(a.b)("inlineCode",{parentName:"p"},"_content")," and ",Object(a.b)("inlineCode",{parentName:"p"},"_query")," are not yet supported by the IBM FHIR Server."),Object(a.b)("p",null,"The filter ",Object(a.b)("inlineCode",{parentName:"p"},'"*": "*"')," is not necessary to include these Resource-level parameters."),Object(a.b)("h4",null,"1.2.1 Handling unexpected search parameters"),Object(a.b)("p",null,"The IBM FHIR Server supports configurable handling of unknown or unsupported search parameters as defined at ",Object(a.b)("a",{parentName:"p",href:"https://ibm.github.io/FHIR/Conformance#http-headers"},"https://ibm.github.io/FHIR/Conformance#http-headers"),".\nFiltered search parameters are handled exactly the same as undefined search parameters, meaning that searches which include these parameters will fail in ",Object(a.b)("inlineCode",{parentName:"p"},"strict")," mode."),Object(a.b)("h4",null,"1.2.2 Compartment search considerations with filtering"),Object(a.b)("p",null,"For each compartment type, the rules for determining if a resource is a member of a compartment of that type, and thus if that resource should be returned on an associated compartment search, are based on inclusion criteria. Inclusion criteria is one or more search parameters whose value is a reference to the compartment resource type. These membership rules are defined by the FHIR specification for the following compartments:"),Object(a.b)("ul",null,Object(a.b)("li",{parentName:"ul"},Object(a.b)("a",{parentName:"li",href:"https://hl7.org/fhir/R4B/compartmentdefinition-device.html"},"Device")),Object(a.b)("li",{parentName:"ul"},Object(a.b)("a",{parentName:"li",href:"https://hl7.org/fhir/R4B/compartmentdefinition-encounter.html"},"Encounter")),Object(a.b)("li",{parentName:"ul"},Object(a.b)("a",{parentName:"li",href:"https://hl7.org/fhir/R4B/compartmentdefinition-patient.html"},"Patient")),Object(a.b)("li",{parentName:"ul"},Object(a.b)("a",{parentName:"li",href:"https://hl7.org/fhir/R4B/compartmentdefinition-practitioner.html"},"Practitioner")),Object(a.b)("li",{parentName:"ul"},Object(a.b)("a",{parentName:"li",href:"https://hl7.org/fhir/R4B/compartmentdefinition-relatedperson.html"},"RelatedPerson"))),Object(a.b)("p",null,"For example, for the ",Object(a.b)("inlineCode",{parentName:"p"},"Patient")," compartment, to determine if an ",Object(a.b)("inlineCode",{parentName:"p"},"Observation")," is a member, the inclusion criteria search parameters are ",Object(a.b)("inlineCode",{parentName:"p"},"subject")," and ",Object(a.b)("inlineCode",{parentName:"p"},"performer"),". If the ",Object(a.b)("inlineCode",{parentName:"p"},"Observation")," resource fields associated with these search parameters reference a ",Object(a.b)("inlineCode",{parentName:"p"},"Patient")," resource, the ",Object(a.b)("inlineCode",{parentName:"p"},"Observation")," resource is a member of that ",Object(a.b)("inlineCode",{parentName:"p"},"Patient")," compartment."),Object(a.b)("p",null,"As of IBM FHIR Server 4.11.0, compartment membership is always evaluated during ingestion, even if the search parameters that define compartment membership have been filtered out in fhir-server-config.json.\nHowever, in cases where the inclusion criteria parameters have been overridden, it is still possible for server config to affect compartment membership."),Object(a.b)("h2",null,"2 Re-index"),Object(a.b)("p",null,"Reindexing is implemented as a custom operation that tells the IBM FHIR Server to read a set of resources and replace the existing search parameters with those newly extracted from the resource body."),Object(a.b)("p",null,"The ",Object(a.b)("inlineCode",{parentName:"p"},"$reindex")," operation can be invoked via an HTTP(S) POST to ",Object(a.b)("inlineCode",{parentName:"p"},"[base]/$reindex"),", ",Object(a.b)("inlineCode",{parentName:"p"},"[base]/[type]/$reindex"),", or ",Object(a.b)("inlineCode",{parentName:"p"},"[base]/[type]/[instance]/$reindex"),". By default, the operation at the System-level or Type-level selects 10 resources and re-extracts their search parameters values based on the current configuration of the server."),Object(a.b)("p",null,"Reindexing is resource-intensive and can take several hours or even days to complete depending on the approach used, the number of resources currently in the system, and the capability of the hosting platform."),Object(a.b)("h3",null,"2.1 Server-side-driven approach"),Object(a.b)("p",null,"By default, the operation will select 10 resources and re-extract their search parameters values based on the current configuration of the server. The operation supports the following parameters to control the behavior:"),Object(a.b)("table",null,Object(a.b)("thead",{parentName:"table"},Object(a.b)("tr",{parentName:"thead"},Object(a.b)("th",{parentName:"tr",align:null},"name"),Object(a.b)("th",{parentName:"tr",align:null},"type"),Object(a.b)("th",{parentName:"tr",align:null},"description"))),Object(a.b)("tbody",{parentName:"table"},Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},Object(a.b)("inlineCode",{parentName:"td"},"tstamp")),Object(a.b)("td",{parentName:"tr",align:null},"string"),Object(a.b)("td",{parentName:"tr",align:null},"Reindex only resources not previously reindexed since this timestamp. Format as a date YYYY-MM-DD or time YYYY-MM-DDTHH:MM:SSZ.")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},Object(a.b)("inlineCode",{parentName:"td"},"resourceCount")),Object(a.b)("td",{parentName:"tr",align:null},"integer"),Object(a.b)("td",{parentName:"tr",align:null},"The maximum number of resources to reindex in this call. If this number is too large, the processing time might exceed the transaction timeout and fail.")))),Object(a.b)("p",null,"The IBM FHIR Server tracks when a resource was last reindexed and only resources with a reindex_tstamp value less than the given tstamp parameter will be processed. When a resource is reindexed, its reindex_tstamp is set to the given tstamp value. In most cases, using the current date (for example “2020-10-27”) is the best option for this value."),Object(a.b)("h3",null,"2.2 Client-side-driven approach"),Object(a.b)("p",null,"Another option is to have the client utilize the ",Object(a.b)("inlineCode",{parentName:"p"},"$retrieve-index")," and ",Object(a.b)("inlineCode",{parentName:"p"},"$reindex")," in parallel to drive the processing."),Object(a.b)("p",null,"The ",Object(a.b)("inlineCode",{parentName:"p"},"$retrieve-index")," operation is called to retrieve index IDs of resources available for reindexing. This can be done repeatedly by using the ",Object(a.b)("inlineCode",{parentName:"p"},"_count")," and ",Object(a.b)("inlineCode",{parentName:"p"},"afterIndexId")," parameters for pagination. The operation supports the following parameters to control the behavior:"),Object(a.b)("table",null,Object(a.b)("thead",{parentName:"table"},Object(a.b)("tr",{parentName:"thead"},Object(a.b)("th",{parentName:"tr",align:null},"name"),Object(a.b)("th",{parentName:"tr",align:null},"type"),Object(a.b)("th",{parentName:"tr",align:null},"description"))),Object(a.b)("tbody",{parentName:"table"},Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},Object(a.b)("inlineCode",{parentName:"td"},"_count")),Object(a.b)("td",{parentName:"tr",align:null},"string"),Object(a.b)("td",{parentName:"tr",align:null},"The maximum number of index IDs to retrieve. This may not exceed 1000. If not specified, the maximum number retrieved is 1000.")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},Object(a.b)("inlineCode",{parentName:"td"},"notModifiedAfter")),Object(a.b)("td",{parentName:"tr",align:null},"string"),Object(a.b)("td",{parentName:"tr",align:null},"Only retrieve index IDs for resources not last updated after this timestamp. Format as a date YYYY-MM-DD or time YYYY-MM-DDTHH:MM:SSZ.")),Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},Object(a.b)("inlineCode",{parentName:"td"},"afterIndexId")),Object(a.b)("td",{parentName:"tr",align:null},"string"),Object(a.b)("td",{parentName:"tr",align:null},"Retrieve index IDs starting with the first index ID after this index ID. If this parameter is not specified, the retrieved index IDs start with the first index ID.")))),Object(a.b)("p",null,"The ",Object(a.b)("inlineCode",{parentName:"p"},"$retrieve-index")," operation can be invoked via an HTTP(s) POST to ",Object(a.b)("inlineCode",{parentName:"p"},"[base]/$retrieve-index")," or ",Object(a.b)("inlineCode",{parentName:"p"},"[base]/[type]/$retrieve-index"),". Invoking this operation at the type-level only retrieves indexIDs for resources of that type."),Object(a.b)("p",null,"The ",Object(a.b)("inlineCode",{parentName:"p"},"$reindex")," operation is called to reindex the resources with index IDs in the specified list. The operation supports the following parameters to control the behavior:"),Object(a.b)("table",null,Object(a.b)("thead",{parentName:"table"},Object(a.b)("tr",{parentName:"thead"},Object(a.b)("th",{parentName:"tr",align:null},"name"),Object(a.b)("th",{parentName:"tr",align:null},"type"),Object(a.b)("th",{parentName:"tr",align:null},"description"))),Object(a.b)("tbody",{parentName:"table"},Object(a.b)("tr",{parentName:"tbody"},Object(a.b)("td",{parentName:"tr",align:null},Object(a.b)("inlineCode",{parentName:"td"},"indexIds")),Object(a.b)("td",{parentName:"tr",align:null},"string"),Object(a.b)("td",{parentName:"tr",align:null},"Reindex only resources with an index ID in the specified list, formatted as a comma-delimited list of strings. If number of index IDs in the list is too large, the processing time might exceed the transaction timeout and fail.")))),Object(a.b)("p",null,"By specifying the index IDs on the ",Object(a.b)("inlineCode",{parentName:"p"},"$reindex")," operation, the IBM FHIR Server avoids the database overhead of choosing the next resource to reindex and updating the reindex_tstamp. Though it requires the client side to track the reindex progress, it should allow for an overall faster reindex."),Object(a.b)("h3",null,"2.3 fhir-bucket"),Object(a.b)("p",null,"To aid in the re-indexing process, the IBM FHIR Server team has expanded the fhir-bucket resource-loading tool to support driving the reindex, with the option of using either the server-side-driven or client-side-driven approach. The fhir-bucket tool uses a thread-pool to make concurrent POST requests to the IBM FHIR Server ",Object(a.b)("inlineCode",{parentName:"p"},"$retrieve-index")," and ",Object(a.b)("inlineCode",{parentName:"p"},"$reindex")," custom operations."),Object(a.b)("p",null,"For more information on driving the reindex operation from fhir-bucket, see ",Object(a.b)("a",{parentName:"p",href:"https://github.com/IBM/FHIR/tree/main/fhir-bucket#driving-the-reindex-custom-operation"},"https://github.com/IBM/FHIR/tree/main/fhir-bucket#driving-the-reindex-custom-operation"),"."),Object(a.b)("hr",null),Object(a.b)("p",null,"FHIR® is the registered trademark of HL7 and is used with the permission of HL7."))}h.isMDXComponent=!0}}]);
//# sourceMappingURL=component---src-pages-guides-fhir-search-configuration-md-27740d07d6326eded2bf.js.map