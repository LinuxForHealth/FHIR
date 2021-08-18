/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.common.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;

public class BulkDataUtilsTest {
    @Test
    public void testGetSearchParametersFromTypeFilters() throws Exception {
        Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes
            = BulkDataUtils.getSearchParametersFromTypeFilters("MedicationRequest%3Fstatus%3Dactive,MedicationRequest%3Fstatus%3Dcompleted%26date%3Dgt2018-07-01T00%3A00%3A00Z%26date%3Dlt2019-07-01T00%3A00%3A00Z");
        assertNotNull(searchParametersForResoureTypes);

        List<Map<String, List<String>>> searchParametersForMedicationRequest = searchParametersForResoureTypes.get(ModelSupport.getResourceType("MedicationRequest"));
        assertNotNull(searchParametersForMedicationRequest);
        assertEquals(2, searchParametersForMedicationRequest.size());
        assertEquals("active" ,searchParametersForMedicationRequest.get(0).get("status").get(0));
        assertEquals("completed" ,searchParametersForMedicationRequest.get(1).get("status").get(0));
        assertEquals("gt2018-07-01T00:00:00Z" ,searchParametersForMedicationRequest.get(1).get("date").get(0));
        assertEquals("lt2019-07-01T00:00:00Z" ,searchParametersForMedicationRequest.get(1).get("date").get(1));
    }

    @Test
    public void testGetSearchParametersFromTypeFiltersWithSystems() throws Exception {
        Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes
            = BulkDataUtils.getSearchParametersFromTypeFilters("Patient%3F_has%3ACondition%3Apatient%3Acode%3Dhttp%3A%2F%2Fsnomed.info%2Fsct%7C44054006");
        assertNotNull(searchParametersForResoureTypes);

        List<Map<String, List<String>>> searchParametersForMedicationRequest = searchParametersForResoureTypes.get(ModelSupport.getResourceType("Patient"));
        assertNotNull(searchParametersForMedicationRequest);
        assertEquals(1, searchParametersForMedicationRequest.size());
    }
}
