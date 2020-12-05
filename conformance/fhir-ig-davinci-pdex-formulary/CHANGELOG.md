30 NOV 2020 - Initial Load
 - remove invalid <br/> tags from between <li> tags in CapabilityStatement-usdf-server.json (see ResourceProcessor.java)
 - removed invalid ImplementationGuide parameters `copyrightyear`, `releaselabel`, and more
 - added missing / to the CapabilityStatement.url (https://jira.hl7.org/browse/FHIR-29936)
 - update the searchParam.definition for List-identifier to properly reference a core spec parameter (https://jira.hl7.org/browse/FHIR-29937)