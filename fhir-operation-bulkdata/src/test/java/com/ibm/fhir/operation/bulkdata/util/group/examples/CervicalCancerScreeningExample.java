package com.ibm.fhir.operation.bulkdata.util.group.examples;

import java.util.List;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;

/**
 * #5 Cervical Cancer Screening - Adherence
 */
public class CervicalCancerScreeningExample extends GroupExample {
    @Override
    public String typeName() {
        return "CervicalCancerScreening";
    }

    @Override
    public List<Group> groups() {
        // Population #1
        // - Gender: female
        // - Age: >= 21 and < 65
        // - Service: 
        // Pap Screening in last 3 years
        // Exclusion: 
        // - evidence of hysterectomy

        // Population #2
        // - Gender: female
        // - Age: >= 30 and < 65
        // - Service: 
        //      Patient  ≥30 to <65 yrs of age who had cervical cytology (Pap smear) 
        //      AND human papillomavirus (HPV) test performed with service dates 4 or less days apart during the last 5 years
        // Exclusion: 
        // - evidence of hysterectomy

        return null;
    }

    @Override
    public Bundle sampleData() {
        // TODO Auto-generated method stub
        return null;
    }
}