# FHIR SPL Mapping FHIR Implementation Guide - 0.1.0 - Build CI

Per the Implementation Guide, 

```
A set of FHIR profiles to enable the sending of FDA SPL submissions as well as a 
mapping to and from the existing FDA SPL V3 specification and documentation of an 
initial FHIR-based architecture for handling existing FDA SPL use cases in parallel with the current SPL-based architecture.
```

The source is from [http://build.fhir.org/ig/HL7/fhir-spl/branches/main/toc.html](http://build.fhir.org/ig/HL7/fhir-spl/branches/main/toc.html).

- Commit [https://github.com/HL7/fhir-spl/commit/6b7a76262424d8a3e34ac8d17ca2669604f8541f](https://github.com/HL7/fhir-spl/commit/6b7a76262424d8a3e34ac8d17ca2669604f8541f)
- Date 2021-APR-26

1 - Downloaded the package.tgz

``` sh
curl -o package.tgz http://build.fhir.org/ig/HL7/fhir-spl/branches/main/package.tgz -L
```

2 - Extract the package to `src/main/resources`

This packaging is experimental.