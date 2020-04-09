package com.ibm.fhir.operation.bulkdata.util.group.examples;

import java.util.List;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;

/**
 * #1 Wellness Annual - Adherence 
 */
public class AnnualWellnessExample extends GroupExample {

    @Override
    public String typeName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Group> groups() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bundle sampleData() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * #1 Wellness Annual - Adherence
     * Identify patients who have had a preventive visit according to the parameters specified for their gender and age
     * group.
     */
    public void generateAnnualPhysicalGroupAdherent() {
        // >= 18 Year
        // < 65 Year
        // Gender: Female
        // Had Preventitive Visit 

    }

    /*
     * #1 Wellness Annual - Adherence - Sample Data
     */
    public void generateAnnualPhysicalGroupAdherentSampleData() {
        // Patient
        // Age
        // Gender

        // Must have Match
        // Must not have Match
    }

    /*
     * #1 Wellness Annual - Non-Adherence
     * Identify patients who have NOT had a preventive visit according to the parameters specified for their gender and
     * age group.
     */
    public void generateAnnualPhysicalGroupNotAdherent() {
        // Patient
        // Age
        // Gender

        // Must have Match
        // Must not have Match

    }

    /*
     * #1 Wellness Annual - Non-Adherence - Sample Data
     */
    public void generateAnnualPhysicalGroupNotAdherentSampleData() {
        // >= 18 Year
        // < 40 Year
        // Gender: Male
        // Not Had Preventitive Visit

    }

}
