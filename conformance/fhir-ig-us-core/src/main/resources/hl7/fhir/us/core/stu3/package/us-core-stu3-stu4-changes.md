# OperationDefinition
OperationDefinition-docref.json
{"op":"replace","path":"/parameter/2/type","value":"dateTime"}
{"op":"replace","path":"/parameter/1/type","value":"dateTime"}
it was originally "date"
We don't currently implement. No impact.

# Capability Statements
These changed. I don't think use/we care?

# SearchParameters
No Removals
No New Additionsn

1. SearchParameter-us-core-condition-onset-date.json

```
{"op":"replace","path":"/expression","value":"Condition.onset.as(dateTime)|Condition.onset.as(Period)"}
```

2. SearchParameter-us-core-location-name.json

```
{"op":"replace","path":"/expression","value":"Location.name|Location.alias"}
```

Location.alias is new.

3. SearchParameter-us-core-organization-name.json

```
{"op":"replace","path":"/expression","value":"Organization.name|Organization.alias"}
```

Organnization.alias is new.

Net: No breaking changes.

# ValueSet
Removed 
ValueSet-us-core-allergy-substance.json
ValueSet-us-core-careteam-provider-roles.json
ValueSet-us-core-medication-codes.json
ValueSet-us-core-ndc-vaccine-codes.json
ValueSet-us-core-observation-smokingstatus.json
ValueSet-us-core-procedure-icd10pcs.json
ValueSet-us-core-provider-specialty.json
ValueSet-us-core-vaccines-cvx.json

Any impact? I think these are just 'pointers'

Added new valueset alueSet-us-core-vital-signs.json 

# ConnceptMap
Removed ConceptMap-ndc-cvx.json

# Structure Definitions

Files in STU3 but not STU4


ig-r4.json

Files in STU4 but not STU3
StructureDefinition-us-core-blood-pressure.json
StructureDefinition-us-core-bmi.json
StructureDefinition-us-core-body-height.json
StructureDefinition-us-core-body-temperature.json
StructureDefinition-us-core-body-weight.json
StructureDefinition-us-core-head-circumference.json
StructureDefinition-us-core-heart-rate.json
StructureDefinition-us-core-respiratory-rate.json
StructureDefinition-us-core-vital-signs.json
