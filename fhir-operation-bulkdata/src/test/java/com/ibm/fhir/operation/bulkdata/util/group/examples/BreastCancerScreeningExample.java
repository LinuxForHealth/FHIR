package com.ibm.fhir.operation.bulkdata.util.group.examples;

import java.util.List;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;

/**
 * #4 Breast Cancer Screening - Adherence 
 */
public class BreastCancerScreeningExample extends GroupExample {
    @Override
    public String typeName() {
        return "BreastCancerScreening";
    }

    @Override
    public List<Group> groups() {
        // Population
        // - Gender: female
        // - Age: >= 40 and < 75
        // - Service: Mammogram in last 12 months
        //      ICD-9   V76.11  Screening mammogram for high-risk patient
        //      ICD-9   V76.12  Other screening mammogram  
        // Exclusion: 
        // - Evidence of Mastectomy (bilateral/unilateral) 
        // - Documentation of BreastCancer
        // 
        
        return null;
    }

    @Override
    public Bundle sampleData() {
        // TODO Auto-generated method stub
        return null;
    }
}