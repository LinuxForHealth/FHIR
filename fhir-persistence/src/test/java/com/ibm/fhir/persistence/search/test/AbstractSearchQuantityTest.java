/*
 * (C) Copyright IBM Corp. 2018, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.test.TestUtil;

/**
 * <a href="https://hl7.org/fhir/r4/search.html#quantity">FHIR Specification:
 * Search - quantity</a>
 */
public abstract class AbstractSearchQuantityTest extends AbstractPLSearchTest {

    @Override
    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicQuantity.json");
    }

    @Override
    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("quantity");
    }

    @Test
    public void testSearchQuantity_Quantity() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "25|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "25||s");
        assertSearchReturnsSavedResource("Quantity", "25");

        // https://jira.hl7.org/browse/FHIR-19597
        assertSearchReturnsSavedResource("Quantity", "25||sec");

        assertSearchDoesntReturnSavedResource("Quantity", "24.4999||s");
        assertSearchDoesntReturnSavedResource("Quantity", "24.5||s");
        assertSearchDoesntReturnSavedResource("Quantity", "25.019||s");
        assertSearchDoesntReturnSavedResource("Quantity", "25.5||s");
    }

    @Test
    public void testSearchToken_Quantity_or() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "10||a,25||s,30||z");
    }

    @Test
    public void testSearchToken_Quantity_escaped() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "25|http://unitsofmeasure.org|s");
        assertSearchDoesntReturnSavedResource("Quantity", "25|http://unitsofmeasure.org\\||s");
    }

    @Test
    public void testSearchQuantity_Quantity_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Quantity", "25|http://unitsofmeasure.org|s");
        assertSearchReturnsComposition("subject:Basic.Quantity", "25||s");
        assertSearchReturnsComposition("subject:Basic.Quantity", "25");
        assertSearchReturnsComposition("subject:Basic.Quantity", "25||sec");
    }

    @Test
    public void testSearchQuantity_Quantity_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("Quantity", Collections.singletonList("25|http://unitsofmeasure.org|s"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_NE() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "ne24|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "ne24.4999||s");
        assertSearchReturnsSavedResource("Quantity", "ne24.5||s");
        assertSearchDoesntReturnSavedResource("Quantity", "ne25||s");
        assertSearchReturnsSavedResource("Quantity", "ne25.4999||s");
        assertSearchReturnsSavedResource("Quantity", "ne25.5||s");
        assertSearchReturnsSavedResource("Quantity", "ne26|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_AP() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "ap24|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "ap24.4999||s");
        assertSearchReturnsSavedResource("Quantity", "ap24.5||s");
        assertSearchReturnsSavedResource("Quantity", "ap25||s");
        assertSearchReturnsSavedResource("Quantity", "ap25.4999||s");
        assertSearchReturnsSavedResource("Quantity", "ap25.5||s");
        assertSearchReturnsSavedResource("Quantity", "ap26|http://unitsofmeasure.org|s");
        assertSearchDoesntReturnSavedResource("Quantity", "ap30|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_LT() throws Exception {
        assertSearchDoesntReturnSavedResource("Quantity", "lt24|http://unitsofmeasure.org|s");
        assertSearchDoesntReturnSavedResource("Quantity", "lt24.4999||s");
        assertSearchDoesntReturnSavedResource("Quantity", "lt24.5||s");
        assertSearchReturnsSavedResource("Quantity", "lt25||s");
        assertSearchReturnsSavedResource("Quantity", "lt25.4999||s");
        assertSearchReturnsSavedResource("Quantity", "lt25.5||s");
        assertSearchReturnsSavedResource("Quantity", "lt26|http://unitsofmeasure.org|s");

    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_GT() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "gt24|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "gt24.4999||s");
        assertSearchReturnsSavedResource("Quantity", "gt24.5||s");
        assertSearchReturnsSavedResource("Quantity", "gt25||s");
        assertSearchReturnsSavedResource("Quantity", "gt25.4999||s");
        assertSearchDoesntReturnSavedResource("Quantity", "gt25.5||s");
        assertSearchDoesntReturnSavedResource("Quantity", "gt26|http://unitsofmeasure.org|s");

    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_LE() throws Exception {
        assertSearchDoesntReturnSavedResource("Quantity", "le24|http://unitsofmeasure.org|s");
        assertSearchDoesntReturnSavedResource("Quantity", "le24.4999||s");
        assertSearchReturnsSavedResource("Quantity", "le24.5||s");
        assertSearchReturnsSavedResource("Quantity", "le25||s");
        assertSearchReturnsSavedResource("Quantity", "le25.4999||s");
        assertSearchReturnsSavedResource("Quantity", "le25.5||s");
        assertSearchReturnsSavedResource("Quantity", "le26|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_GE() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "ge24|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "ge24.4999||s");
        assertSearchReturnsSavedResource("Quantity", "ge24.5||s");
        assertSearchReturnsSavedResource("Quantity", "ge25||s");
        assertSearchReturnsSavedResource("Quantity", "ge25.4999||s");
        // We need to track whether the high is inclusive or not to get this right
        // For decimals, the high is exclusive, but for Range the high is inclusive
//        assertSearchDoesntReturnSavedResource("Quantity", "ge25.5||s");
        assertSearchDoesntReturnSavedResource("Quantity", "ge25.5001||s");
        assertSearchDoesntReturnSavedResource("Quantity", "ge26|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_SA() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "sa24|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "sa24.4999||s");
        assertSearchDoesntReturnSavedResource("Quantity", "sa24.5||s");
        assertSearchDoesntReturnSavedResource("Quantity", "sa25||s");
        assertSearchDoesntReturnSavedResource("Quantity", "sa25.4999||s");
        assertSearchDoesntReturnSavedResource("Quantity", "sa25.5||s");
        assertSearchDoesntReturnSavedResource("Quantity", "sa26|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_EB() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "eb25.5001||s");
        assertSearchDoesntReturnSavedResource("Quantity", "eb24|http://unitsofmeasure.org|s");
        assertSearchDoesntReturnSavedResource("Quantity", "eb24.4999||s");
        assertSearchDoesntReturnSavedResource("Quantity", "eb24.5||s");
        assertSearchDoesntReturnSavedResource("Quantity", "eb25||s");
        assertSearchDoesntReturnSavedResource("Quantity", "eb25.4999||s");
        // We need to track whether the high is inclusive or not to get this right
        // For decimals, the high is exclusive, but for Range the high is inclusive
//        assertSearchReturnsSavedResource("Quantity", "eb25.5||s");
        assertSearchReturnsSavedResource("Quantity", "eb25.5001||s");
        assertSearchReturnsSavedResource("Quantity", "eb26|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefixes_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Quantity", "lt26|http://unitsofmeasure.org|s");
        assertSearchReturnsComposition("subject:Basic.Quantity", "gt24|http://unitsofmeasure.org|s");
        assertSearchReturnsComposition("subject:Basic.Quantity", "le26|http://unitsofmeasure.org|s");
        assertSearchReturnsComposition("subject:Basic.Quantity", "le25|http://unitsofmeasure.org|s");
        assertSearchReturnsComposition("subject:Basic.Quantity", "ge25|http://unitsofmeasure.org|s");
        assertSearchReturnsComposition("subject:Basic.Quantity", "ge24|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchQuantity_Quantity_NoDisplayUnit() throws Exception {
        assertSearchReturnsSavedResource("Quantity-noDisplayUnit", "1|http://snomed.info/sct|385049006");
        assertSearchReturnsSavedResource("Quantity-noDisplayUnit", "1||385049006");
    }

    @Test
    public void testSearchQuantity_Quantity_NoCode() throws Exception {
        assertSearchReturnsSavedResource("Quantity-noCode", "1||eq");
    }

    @Test
    public void testSearchQuantity_Quantity_NoCodeOrUnit() throws Exception {
        // spec isn't clear about whether quantities with no unit should be indexed
        // but since we require the unit while searching, it doesn't really matter
        assertSearchDoesntReturnSavedResource("Quantity-noCodeOrUnit", "1||eq");
    }

    /*
     * FHIR Server does not yet use quantity comparator to calculate search results.
     */
    // Quantity search is of the form <prefix><number>|<unit_system>|<unit>.
    // We use custom units to mark the quantity comparators so we can scope our searches in the tests.

    @Test
    public void testSearchQuantity_Quantity_LessThan() throws Exception {
        // For more details, please see Quantity search does not consider the quantity comparator field #493 https://github.com/IBM/FHIR/issues/493
        // assertSearchReturnsSavedResource("Quantity-lessThan", "2||lt");
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "4||lt");

        // For more details, please see Quantity search does not consider the quantity comparator field #493 https://github.com/IBM/FHIR/issues/493
        // assertSearchDoesntReturnSavedResource("Quantity-lessThan", "3||lt");
        // assertSearchReturnsSavedResource("Quantity-lessThan", "lt2||lt");      // < 3 may be < 2
        assertSearchReturnsSavedResource("Quantity-lessThan", "gt2||lt"); // < 3 may be > 2
        assertSearchReturnsSavedResource("Quantity-lessThan", "lt4||lt"); // < 3 may be < 4
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "gt4||lt"); // < 3 is not > 4
    }

    @Test
    public void testSearchQuantity_Quantity_GreaterThan() throws Exception {
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "2||gt");

        // For more details, please see Quantity search does not consider the quantity comparator field #493 https://github.com/IBM/FHIR/issues/493
        // In this test, the comparator should be from the DB
        // assertSearchReturnsSavedResource("Quantity-greaterThan", "4||gt");

        // In this test, the comparator should be from the DB
        // With implicit ranges, 3 (+/-0.5) actually might be > 3
        // For more details, please see Quantity search does not consider the quantity comparator field #493 https://github.com/IBM/FHIR/issues/493
        //assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "3||gt");
        assertSearchReturnsSavedResource("Quantity-greaterThan", "3||gt");

        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "lt2||gt"); // > 3 is not < 2
        assertSearchReturnsSavedResource("Quantity-greaterThan", "gt2||gt"); // > 3 may be > 2
        assertSearchReturnsSavedResource("Quantity-greaterThan", "lt4||gt"); // > 3 may be < 4

        // For more details, please see Quantity search does not consider the quantity comparator field #493 https://github.com/IBM/FHIR/issues/493
        // assertSearchReturnsSavedResource("Quantity-greaterThan", "gt4||gt");      // > 3 may be > 4
    }

    @Test
    public void testSearchQuantity_Quantity_LessThanOrEqual() throws Exception {
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "2||lte");
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "3||lte");
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "4||lte");

        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "lt2||lte"); // <= 3 may be < 2
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "gt2||lte"); // <= 3 may be > 2
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "lt4||lte"); // <= 3 may be < 4
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "gt4||lte"); // <= 3 is not > 4

        // <= 3 may be <= 3
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "le3||lte");
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "ge3||lte"); // <= 3 may be >= 3
    }

    @Test
    public void testSearchQuantity_Quantity_GreaterThanOrEqual() throws Exception {
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "2||gte");
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "3||gte");
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "4||gte");

        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "lt2||gte"); // >= 3 is not < 2
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "gt2||gte"); // >= 3 may be > 2
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "lt4||gte"); // >= 3 may be < 4
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "gt4||gte"); // >= 3 may be > 4

        // >= 3 may be <= 3
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "le3||gte");
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "ge3||gte"); // >= 3 is >= 3
    }

    @Test
    public void testSearchQuantity_Quantity_missing() throws Exception {
        assertSearchReturnsSavedResource("Quantity:missing", "false");
        assertSearchDoesntReturnSavedResource("Quantity:missing", "true");

        assertSearchReturnsSavedResource("missing-Quantity:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Quantity:missing", "false");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_EQ_Implied() throws Exception {
        // 1e1 has implicit range of 5 - 15
        assertSearchReturnsSavedResource("Range", "1e1||s");

        // the range of the search value doesn't fully contain the range of the target value
        assertSearchDoesntReturnSavedResource("Range", "4||s");
        assertSearchDoesntReturnSavedResource("Range", "5||s");
        assertSearchDoesntReturnSavedResource("Range", "10||s");
        assertSearchDoesntReturnSavedResource("Range", "11||s");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_NE() throws Exception {
        assertSearchReturnsSavedResource("Range", "ne4||s");
        assertSearchReturnsSavedResource("Range", "ne5||s");
        assertSearchReturnsSavedResource("Range", "ne10||s");
        assertSearchReturnsSavedResource("Range", "ne11||s");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_AP() throws Exception {
        assertSearchDoesntReturnSavedResource("Range", "ap11||s");
        assertSearchDoesntReturnSavedResource("Range", "ap4||s");
        assertSearchReturnsSavedResource("Range", "ap5||s");
        assertSearchReturnsSavedResource("Range", "ap10||s");
        assertSearchDoesntReturnSavedResource("Range", "ap11||s");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_LT() throws Exception {
        assertSearchDoesntReturnSavedResource("Range", "lt4||s");
        assertSearchDoesntReturnSavedResource("Range", "lt5||s");
        assertSearchReturnsSavedResource("Range", "lt10||s");
        assertSearchReturnsSavedResource("Range", "lt11||s");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_GT() throws Exception {
        assertSearchReturnsSavedResource("Range", "gt4||s");
        assertSearchReturnsSavedResource("Range", "gt5||s");
        assertSearchDoesntReturnSavedResource("Range", "gt10||s");
        assertSearchDoesntReturnSavedResource("Range", "gt11||s");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_EB() throws Exception {
        assertSearchDoesntReturnSavedResource("Range", "eb4||s");
        assertSearchDoesntReturnSavedResource("Range", "eb5||s");
        assertSearchDoesntReturnSavedResource("Range", "eb10||s");
        assertSearchReturnsSavedResource("Range", "eb11||s");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_SA() throws Exception {
        assertSearchReturnsSavedResource("Range", "sa4||s");
        assertSearchDoesntReturnSavedResource("Range", "sa5||s");
        assertSearchDoesntReturnSavedResource("Range", "sa10||s");
        assertSearchDoesntReturnSavedResource("Range", "sa10.0||s");
        assertSearchDoesntReturnSavedResource("Range", "sa11||s");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_GE() throws Exception {
        assertSearchReturnsSavedResource("Range", "ge4||s");
        assertSearchReturnsSavedResource("Range", "ge5||s");
        assertSearchReturnsSavedResource("Range", "ge10||s");
        assertSearchDoesntReturnSavedResource("Range", "ge11||s");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_LE() throws Exception {
        assertSearchDoesntReturnSavedResource("Range", "le4||s");
        assertSearchReturnsSavedResource("Range", "le5||s");
        assertSearchReturnsSavedResource("Range", "le10||s");
        assertSearchReturnsSavedResource("Range", "le10.01||s");
        assertSearchReturnsSavedResource("Range", "le11||s");
    }

    @Test
    public void testSearchQuantity_Range_missing() throws Exception {
        assertSearchReturnsSavedResource("Range:missing", "false");
        assertSearchDoesntReturnSavedResource("Range:missing", "true");
        assertSearchReturnsSavedResource("missing-Range:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Range:missing", "false");
    }

    @Test
    public void testSearchQuantity_Exponent() throws Exception {
        // Value 1.2E+2 should return the value
        // The value is extracted, and stored in the values tables.
        assertSearchReturnsSavedResource("Quantity-withExponent", "1.2E+2");
        assertSearchReturnsSavedResource("Quantity-withExponent", "120");
    }

    @Test
    public void testSearchQuantity_Quantity_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Quantity:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.Quantity:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-Quantity:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-Quantity:missing", "false");
    }
}
