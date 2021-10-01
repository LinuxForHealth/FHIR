01 DEC 2020 - Initial Load from http://build.fhir.org/ig/HL7/davinci-ehrx/
- Modified the expression for provenance-1 (https://jira.hl7.org/browse/FHIR-28451)
    - From `($this.agent.who.resolve().is Practitioner or Device) implies exists()`
    - To `(($this.agent.who.resolve() is Practitioner) or ($this.agent.who.resolve() is Device)) implies exists()`
- removed invalid ImplementationGuide parameters `copyrightyear`, `releaselabel`, and more

18 DEC 2020 - Removed invalid entry in the .index.json