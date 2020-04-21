package com.ibm.fhir.operation.bulkdata.util.group.examples;

import java.util.List;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;

/*
 * @see README.md for Group details.
 */
public class AgeRangeExample extends GroupExample {
    @Override
    public String typeName() {
        return "AgeRangeExample";
    }

    @Override
    public List<Group> groups() {
        // code: birthdate
        // range: >= 13 to < 56


        // 0 compliant and non-compliant

        // #1 Population 
        // Inclusion: 
        // - Gender: female
        // - Age: >= 13 to < 56
        // Exclusion: 
        // - Pregnancy in last 12 months

        // #2 Population 
        // Inclusion: 
        // - Gender: female
        // - Age: >= 13 to < 56
        // - Encounter: 
            // -ICD9    V72.31  Routine gynecological examination
            // in last 36 mo look back
        // Exclusion:
        // - Pregnancy in last 12 months
        
        // nested group

        return null;
    }

    @Override
    public Bundle sampleData() {
        // TODO Auto-generated method stub
        return null;
    }
}