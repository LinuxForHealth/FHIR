# $member-match operation
The contents in this module are from HL7 FHIR® Da Vinci Health Record Exchange (HRex) 0.2.0 - STU R1 - 2nd ballot.

The [$member-match](http://build.fhir.org/ig/HL7/davinci-ehrx/OperationDefinition-member-match.html) operation allows one health plan to retrieve a unique identifier for a member from another health plan using a member's demographic and coverage information. This identifier can then be used to perform subsequent queries and operations. Members implementing a deterministic match will require a match on member id or subscriber id at a minimum.

The `$member-match` operation is a POST operation that is executed on the `Patient` Resource.

To use the operation, the `fhir-ig-us-core` US Core 3.1.1 profile and the `fhir-ig-davinci-hrex` HREX 0.2.0 profile must be installed on the system in the server's userlib along with this module `fhir-operation-member-match`.

```
userlib\
    fhir-ig-us-core-4.10.0.jar
    fhir-ig-davinci-hrex-4.10.0.jar
    fhir-operation-member-match-4.10.0.jar
```

The `$member-match` operation is configured for each tenant using the respective fhir-server-config.json. The configuration is rooted under the path `fhirServer/operations/membermatch`.

| Name          | Default | Description                      |
|---------------|---------|----------------------------------|
| enabled       | true    | Enables or Disable the MemberMatch operation for the tenant |
| strategy      | default | The key used to identify the `MemberMatchStrategy` that is loaded using the Java Service Loader|
| extendedProps | true    | Used by custom MemberMatchStrategy implementations |

```
{
    "__comment": "",
    "fhirServer": {
        "operations": {
            "membermatch": {
                "enabled": true,
                "strategy": "default",
                "extendedProps": {
                    "a": "b"
                }
            }
        }
    }
}
```

The default `$member-match` strategy executes a series of Searches on the local FHIR Server to find a Patient on the system with a Patient and Coverage (to-match).

The strategy may be extended using a JAR that implements the service loader. To register a JAR, `META-INF/services/com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchStrategy` the file must point to the package and class that implements `MemberMatchStrategy`.

For implementers, there is an existing `AbstractMemberMatch` which provides a template and series of hooks to extend: 

```
    /**
     *
     * @throws FHIROperationException
     *             indicating a MemberMatch execution Error
     */
    abstract MemberMatchResult executeMemberMatch() throws FHIROperationException;

    /**
     * validates the contents and type of the member match
     *
     * @param input to the MemberMatchOperation
     * @throws FHIROperationException
     */
    abstract void validate(Parameters input) throws FHIROperationException;
```

More advanced processing of the input and validation is shown in `DefaultMemberMatchStrategy` which processes the input resources to generate SearchParameter values to query the local IBM FHIR Server.

It's highly recommended to extend the `default` implementation and override the identifier for the strategy: 

```
    /**
     * used to uniquely identify the strategy.
     * @implNote "default" is reserved.
     * @return the member match strategy identifier
     */
    String getMemberMatchIdentifier();
```