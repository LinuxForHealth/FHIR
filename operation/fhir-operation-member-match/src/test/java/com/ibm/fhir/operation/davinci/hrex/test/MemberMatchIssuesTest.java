/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.model.resource.Coverage;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy.MemberMatchCovergeSearchCompiler;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy.MemberMatchPatientSearchCompiler;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * A set of tests used in debugging issues
 */
public class MemberMatchIssuesTest {
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    /*
     * Addresses issues: MemberMatch improperly generates date formats which are spec invalid #3252
     */
    @Test
    public void testCompilerForInteroperabilityUseCase() throws Exception {
        Parameters parameters = TestUtil.readLocalResource("JSON/member-match-in.json");
        Patient patient = parameters.getParameter().get(0).getResource().as(Patient.class);
        MemberMatchPatientSearchCompiler compiler = new MemberMatchPatientSearchCompiler();
        patient.accept(compiler);
        assertTrue(compiler.getSearchParameters().get("birthdate").contains("eq1970-02-02"));
    }

    @Test
    public void testCompilerCoverageForContainedResources() throws Exception {
        Coverage coverage = TestUtil.readLocalResource("JSON/coverage.json");
        MemberMatchCovergeSearchCompiler compiler = new MemberMatchCovergeSearchCompiler("test");
        coverage.accept(compiler);
        System.out.println(compiler.getSearchParameters());
        assertFalse(compiler.getSearchParameters().get("identifier").contains("http://hl7.org/fhir/sid/us-npi|12345BRANDNEW3"));
    }
}
