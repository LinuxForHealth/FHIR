package com.ibm.fhir.operation.bulkdata.util.group.examples;

import java.util.List;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;

/**
 * #3 Well Child Exam - Adherence 
 */
public class WellChildExample extends GroupExample {
    @Override
    public String typeName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Group> groups() {
        // Population
        // - Age: >= 2 months and < 4 months old
        // - Follow-up Visit: Procedures, Services
        // - Window: Within 2 months
        // ICD9CM   V20.32  Health supervision for newborn 8 to 28 days old HEDIS 2016, Well-Care Value Set
        
        return null;
    }

    @Override
    public Bundle sampleData() {
        // TODO Auto-generated method stub
        return null;
    }
}