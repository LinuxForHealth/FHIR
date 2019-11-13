package com.ibm.fhir.benchmark;

import static com.ibm.fhir.model.type.String.string;

import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.PublicationStatus;

/**
 * Execute with -javaagent:target/fhir-benchmarch-4.0.0-SNAPSHOT.jar
 */
public class PrintObjectSize {
    public static void main(String[] args) throws Exception {
        final ValueSet.Builder vsBuilder = ValueSet.builder().status(PublicationStatus.DRAFT);
        final ValueSet.Expansion.Builder expansionBuilder = ValueSet.Expansion.builder().timestamp(DateTime.now());
        final ValueSet.Expansion.Contains.Builder template = ValueSet.Expansion.Contains.builder()
                .system(Uri.of("http://snomed.info/sct"))
                .version(string("http://snomed.info/sct/[sctid]/version/[YYYYMMDD]"));

        // Build your valueset
        
        ValueSet vs = vsBuilder.expansion(expansionBuilder.build()).build();
        System.out.println("The object is " + ObjectSizeEstimator.getObjectSize(vs) + " bytes.");
    }
}
