# FHIR Examples

This module collects a large number of FHIR examples into a single spot. The examples are organized into directories for convenience and generally follow the pattern `format/source[/category]/filename`

The resources under `json/spec` and `xml/spec` were downloaded from FHIR version 4.0.1 on 2019-12-05. The JSON spec examples appear as-is, but the XML example filenames were modified (remove parenthetical content) for congruity with the previous (FHIR 4.0.0) XML examples.

Additionally, the fhir-examples module includes Java helpers for working with these examples:
* `com.ibm.fhir.examples.Index` is enum which intends to mirror the ".txt" files found at `src/main/resources`
* `com.ibm.fhir.examples.ExamplesUtil` has static helpers for obtaining a FileReader for both the index file and the example resources themselves

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.