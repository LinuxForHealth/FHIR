<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:rule context="//f:Patient[./f:meta/f:profile/@value='http://ibm.com/fhir/profile/acme-sample']">
      <sch:assert test="exists(f:extension[@url='http://ibm.com/fhir/extension/acme-sample/study_ID'])">acme-sample-1: Patient must have a study ID specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
