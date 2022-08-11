/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.search.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.test.TestUtil;

/**
 * <a href="https://hl7.org/fhir/r4/search.html#quantity">FHIR Specification:
 * Search - quantity</a>
 */
public abstract class AbstractSearchQuantityTest extends AbstractPLSearchTest {

    @Override
    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/basic/BasicQuantity.json");
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
        // Implicit range of indexed 'Quantity' value is 24.5 to 25.5
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
        // Implicit range of indexed 'Quantity' value is 24.5 to 25.5
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
        // Implicit range of indexed 'Quantity' value is 24.5 to 25.5
        assertSearchDoesntReturnSavedResource("Quantity", "lt24|http://unitsofmeasure.org|s");
        assertSearchDoesntReturnSavedResource("Quantity", "lt24.4999||s");
        assertSearchDoesntReturnSavedResource("Quantity", "lt24.5||s");
        assertSearchReturnsSavedResource("Quantity", "lt24.5001||s");
        assertSearchReturnsSavedResource("Quantity", "lt25||s");
        assertSearchReturnsSavedResource("Quantity", "lt25.4999||s");
        assertSearchReturnsSavedResource("Quantity", "lt25.5||s");
        assertSearchReturnsSavedResource("Quantity", "lt26|http://unitsofmeasure.org|s");

    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_GT() throws Exception {
        // Implicit range of indexed 'Quantity' value is 24.5 to 25.5
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
        // Implicit range of indexed 'Quantity' value is 24.5 to 25.5
        assertSearchDoesntReturnSavedResource("Quantity", "le24|http://unitsofmeasure.org|s");
        assertSearchDoesntReturnSavedResource("Quantity", "le24.4999||s");
        assertSearchDoesntReturnSavedResource("Quantity", "le24.5||s");
        assertSearchReturnsSavedResource("Quantity", "le24.5001||s");
        assertSearchReturnsSavedResource("Quantity", "le25||s");
        assertSearchReturnsSavedResource("Quantity", "le25.4999||s");
        assertSearchReturnsSavedResource("Quantity", "le25.5||s");
        assertSearchReturnsSavedResource("Quantity", "le26|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_GE() throws Exception {
        // Implicit range of indexed 'Quantity' value is 24.5 to 25.5
        assertSearchReturnsSavedResource("Quantity", "ge24|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "ge24.4999||s");
        assertSearchReturnsSavedResource("Quantity", "ge24.5||s");
        assertSearchReturnsSavedResource("Quantity", "ge25||s");
        assertSearchReturnsSavedResource("Quantity", "ge25.4999||s");
        assertSearchDoesntReturnSavedResource("Quantity", "ge25.5||s");
        assertSearchDoesntReturnSavedResource("Quantity", "ge25.5001||s");
        assertSearchDoesntReturnSavedResource("Quantity", "ge26|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_SA() throws Exception {
        // Implicit range of indexed 'Quantity' value is 24.5 to 25.5
        assertSearchDoesntReturnSavedResource("Quantity", "sa24|http://unitsofmeasure.org|s"); // 24.5 !> 24.5
        assertSearchReturnsSavedResource("Quantity", "sa24.4999||s"); // 24.5 > 24.49995
        assertSearchDoesntReturnSavedResource("Quantity", "sa24.5||s"); // 24.5 !> 24.55
        assertSearchDoesntReturnSavedResource("Quantity", "sa25||s"); // 24.5 !> 25.5
        assertSearchDoesntReturnSavedResource("Quantity", "sa25.4999||s"); // 24.5 !> 25.49995
        assertSearchDoesntReturnSavedResource("Quantity", "sa25.5||s"); // 24.5 !> 25.55
        assertSearchDoesntReturnSavedResource("Quantity", "sa26|http://unitsofmeasure.org|s"); // 24.5 !> 26.5
    }

    @Test
    public void testSearchQuantity_Quantity_withPrefix_EB() throws Exception {
        // Implicit range of indexed 'Quantity' value is 24.5 to 25.5
        assertSearchReturnsSavedResource("Quantity", "eb25.5001||s"); // 25.5 < 25.50005
        assertSearchDoesntReturnSavedResource("Quantity", "eb24|http://unitsofmeasure.org|s");
        assertSearchDoesntReturnSavedResource("Quantity", "eb24.4999||s"); // 25.5 !< 24.49985
        assertSearchDoesntReturnSavedResource("Quantity", "eb24.5||s"); // 25.5 !< 24.45
        assertSearchDoesntReturnSavedResource("Quantity", "eb25||s"); // 25.5 !< 24.5
        assertSearchDoesntReturnSavedResource("Quantity", "eb25.4999||s"); // 25.5 !< 25.49985
        assertSearchDoesntReturnSavedResource("Quantity", "eb25.5||s"); // 25.5 !< 25.45
        assertSearchDoesntReturnSavedResource("Quantity", "eb26|http://unitsofmeasure.org|s"); // 25.5 !< 25.5
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
        assertSearchReturnsSavedResource("Quantity-noCodeOrUnit", "1");
        assertSearchReturnsSavedResource("Quantity-noCodeOrUnit", "1||");
    }

    // Quantity search is of the form <prefix><number>|<unit_system>|<unit>.
    // We use custom units to mark the quantity comparators so we can scope our searches in the tests.

    @Test
    public void testSearchQuantity_Quantity_UnicodeLessThan() throws Exception {
        // Implicit range of indexed 'Quantity-unicodeLessThan' value is negative infinity to 2.999999999.

        // eq, ne
        // The target lower bound (negative infinity) can never be contained within the range of the search
        // value, so will never match an equals search, but will always match a not-equals search.
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "5||ult");
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "-1000000||ult");
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "ne5||ult");
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "ne-1000000||ult");

        // gt, ge
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "gt2||ult"); // < 3 may be > 2
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "gt4||ult"); // < 3 is not > 4
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "gt2.999999999||ult"); // < 3 is not > 2.999999999 (due to ranges)
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "gt2.999999998||ult"); // < 3 may be > 2.999999998
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "ge2||ult"); // < 3 may be >= 2
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "ge4||ult"); // < 3 is not >= 4
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "ge2.999999999||ult"); // < 3 is not >= 2.999999999 (due to ranges)

        // lt, le
        // The target lower bound (negative infinity) ensures that range of the target will always
        // intersect with the range below the search value, so a less-than or less-than-or-equals
        // search will always match.
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "lt4||ult");
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "lt-1000000||ult");
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "le1E-100||ult");
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "le1E+100||ult");

        // sa
        // The target lower bound (negative infinity) ensures that range of the target cannot be
        // contained by the range above the search value, so a starts-after search will never match.
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "sa4||ult");
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "sa-1000000||ult");

        // eb
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "eb2||ult"); // < 3 does not end before 2
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "eb4||ult"); // < 3 does end before 4
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "eb3||ult"); // < 3 does not end before 3 (due to ranges)

        // ap
        assertSearchDoesntReturnSavedResource("Quantity-unicodeLessThan", "ap4||ult"); // < 3 is not approximately 4
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "ap3.25||ult"); // < 3 is approximately 3.25
        assertSearchReturnsSavedResource("Quantity-unicodeLessThan", "ap-1000000||ult"); // < 3 is approximately anything under 3
    }

    @Test
    public void testSearchQuantity_Quantity_LessThan() throws Exception {
        // Implicit range of indexed 'Quantity-lessThan' value is negative infinity to 2.999999999.

        // eq, ne
        // The target lower bound (negative infinity) can never be contained within the range of the search
        // value, so will never match an equals search, but will always match a not-equals search.
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "5||lt");
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "-1000000||lt");
        assertSearchReturnsSavedResource("Quantity-lessThan", "ne5||lt");
        assertSearchReturnsSavedResource("Quantity-lessThan", "ne-1000000||lt");

        // gt, ge
        assertSearchReturnsSavedResource("Quantity-lessThan", "gt2||lt"); // < 3 may be > 2
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "gt4||lt"); // < 3 is not > 4
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "gt2.999999999||lt"); // < 3 is not > 2.999999999 (due to ranges)
        assertSearchReturnsSavedResource("Quantity-lessThan", "gt2.999999998||lt"); // < 3 may be > 2.999999998
        assertSearchReturnsSavedResource("Quantity-lessThan", "ge2||lt"); // < 3 may be >= 2
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "ge4||lt"); // < 3 is not >= 4
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "ge2.999999999||lt"); // < 3 is not >= 2.999999999 (due to ranges)

        // lt, le
        // The target lower bound (negative infinity) ensures that range of the target will always
        // intersect with the range below the search value, so a less-than or less-than-or-equals
        // search will always match.
        assertSearchReturnsSavedResource("Quantity-lessThan", "lt4||lt");
        assertSearchReturnsSavedResource("Quantity-lessThan", "lt-1000000||lt");
        assertSearchReturnsSavedResource("Quantity-lessThan", "le1E-100||lt");
        assertSearchReturnsSavedResource("Quantity-lessThan", "le1E+100||lt");

        // sa
        // The target lower bound (negative infinity) ensures that range of the target cannot be
        // contained by the range above the search value, so a starts-after search will never match.
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "sa4||lt");
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "sa-1000000||lt");

        // eb
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "eb2||lt"); // < 3 does not end before 2
        assertSearchReturnsSavedResource("Quantity-lessThan", "eb4||lt"); // < 3 does end before 4
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "eb3||lt"); // < 3 does not end before 3 (due to ranges)

        // ap
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "ap4||lt"); // < 3 is not approximately 4
        assertSearchReturnsSavedResource("Quantity-lessThan", "ap3.25||lt"); // < 3 is approximately 3.25
        assertSearchReturnsSavedResource("Quantity-lessThan", "ap-1000000||lt"); // < 3 is approximately anything under 3
    }

    @Test
    public void testSearchQuantity_Quantity_GreaterThan() throws Exception {
        // Implicit range of indexed 'Quantity-greaterThan' value is 3.000000001 to positive infinity.

        // eq, ne
        // The target upper bound (positive infinity) can never be contained within the range of the search
        // value, so will never match an equals search, but will always match a not-equals search.
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "2||gt");
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "1000000||gt");
        assertSearchReturnsSavedResource("Quantity-greaterThan", "ne2||gt");
        assertSearchReturnsSavedResource("Quantity-greaterThan", "ne1000000||gt");

        // gt, ge
        // The target upper bound (positive infinity) ensures that range of target will always intersect
        // with the range above the search value, so a greater-than or greater-than-or-equals
        // search will always match.
        assertSearchReturnsSavedResource("Quantity-greaterThan", "gt2||gt");
        assertSearchReturnsSavedResource("Quantity-greaterThan", "gt1000000||gt");
        assertSearchReturnsSavedResource("Quantity-greaterThan", "ge1E-100||gt");
        assertSearchReturnsSavedResource("Quantity-greaterThan", "ge1E100||gt");

        // lt, le
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "lt2||gt"); // > 3 is not < 2
        assertSearchReturnsSavedResource("Quantity-greaterThan", "lt4||gt"); // > 3 may be < 4
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "lt3.000000001||gt"); // < 3.000000001 is not > 3 (due to ranges)
        assertSearchReturnsSavedResource("Quantity-greaterThan", "lt3.000000002||gt"); // < 3.000000002 can be > 3
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "le2||gt"); // > 3 is not <= 2
        assertSearchReturnsSavedResource("Quantity-greaterThan", "le4||gt"); // > 3 may be <= 4
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "le3.000000001||gt"); // <= 3.000000001 is not > 3 (due to ranges)

        // sa
        assertSearchReturnsSavedResource("Quantity-greaterThan", "sa2||gt"); // > 3 starts after 2
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "sa4||gt"); // > 3 does not start after 4
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "sa3||gt"); // > 3 does not start after 3 (due to ranges)

        // eb
        // The target upper bound (positive infinity) ensures that range of target cannot be contained
        // by the range below the search value, so an ends-before search will never match.
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "eb2||gt");
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "eb1000000||gt");

        // ap
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "ap2||gt"); // > 3 is not approximately 2
        assertSearchReturnsSavedResource("Quantity-greaterThan", "ap2.75||gt"); // > 3 is approximately 2.75
        assertSearchReturnsSavedResource("Quantity-greaterThan", "ap1000000||gt"); // > 3 is approximately anything over 3
    }

    @Test
    public void testSearchQuantity_Quantity_LessThanOrEqual() throws Exception {
        // Implicit range of indexed 'Quantity-lessThanOrEqual' value is negative infinity to 3.

        // eq, ne
        // The target lower bound (negative infinity) can never be contained within the range of the search
        // value, so will never match an equals search, but will always match a not-equals search.
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "3||lte");
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "-1000000||lte");
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "ne3||lte");
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "ne-1000000||lte");

        // gt, ge
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "gt2||lte"); // <= 3 may be > 2
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "gt4||lte"); // <= 3 is not > 4
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "gt2.999999999||lte"); // <= 3 may be > 2.999999999
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "ge2||lte"); // <= 3 may be >= 2
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "ge4||lte"); // <= 3 is not >= 4
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "ge3||lte"); // <= 3 is not >= 3 (due to ranges)

        // lt, le
        // The target lower bound (negative infinity) ensures that range of target will always intersect
        // with the range below the search value, so a less-than or less-than-or-equals
        // search will always match.
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "lt4||lte");
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "lt-1000000||lte");
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "le1E-100||lte");
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "le1E+100||lte");

        // sa
        // The target lower bound (negative infinity) ensures that range of target cannot be contained
        // by the range above the search value, so a starts-after search will never match.
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "sa4||lte");
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "sa-1000000||lte");

        // eb
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "eb2||lte"); // <= 3 does not end before 2
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "eb4||lte"); // <= 3 does end before 4
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "eb3||lte"); // <= 3 does not end before 3

        // ap
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "ap4||lte"); // <= 3 is not approximately 4
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "ap3.25||lte"); // <= 3 is approximately 3.25
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "ap-1000000||lte"); // <= 3 is approximately anything under 3
    }

    @Test
    public void testSearchQuantity_Quantity_GreaterThanOrEqual() throws Exception {
        // Implicit range of indexed 'Quantity-greaterThanOrEqual' value is 3 to positive infinity.

        // eq, ne
        // The target upper bound (positive infinity) can never be contained within the range of the search
        // value, so will never match an equals search, but will always match a not-equals search.
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "2||gte");
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "1000000||gte");
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "ne2||gte");
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "ne1000000||gte");

        // gt, ge
        // The target upper bound (positive infinity) ensures that range of target will always intersect
        // with the range above the search value, so a greater-than or greater-than-or-equals
        // search will always match.
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "gt2||gte");
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "gt1000000||gte");
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "ge1E-100||gte");
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "ge1E100||gte");

        // lt, le
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "lt2||gte"); // >= 3 is not < 2
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "lt4||gte"); // >= 3 may be < 4
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "lt3.000000001||gte"); // < 3.000000001 may be >= 3
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "le2||gte"); // >= 3 is not <= 2
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "le4||gte"); // >= 3 may be <= 4
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "le3||gte"); // <= 3 is not >= 3 (due to ranges)

        // sa
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "sa2||gte"); // >= 3 starts after 2
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "sa4||gte"); // >= 3 does not start after 4
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "sa3||gte"); // >= 3 does not start after 3

        // eb
        // The target upper bound (positive infinity) ensures that range of target cannot be contained
        // by the range below the search value, so an ends-before search will never match.
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "eb2||gte");
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "eb1000000||gte");

        // ap
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "ap2||gte"); // >= 3 is not approximately 2
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "ap2.75||gte"); // >= 3 is approximately 2.75
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "ap1000000||gte"); // >= 3 is approximately anything over 3
    }

    @Test
    public void testSearchQuantity_Quantity_missing() throws Exception {
        assertSearchReturnsSavedResource("Quantity:missing", "false");
        assertSearchDoesntReturnSavedResource("Quantity:missing", "true");

        assertSearchReturnsSavedResource("missing-Quantity:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Quantity:missing", "false");
    }

    @Test
    public void testSearchQuantity_Quantity_missing_range() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("Quantity:missing", Collections.singletonList("false"));
        queryParms.put("Range", Collections.singletonList("ge4||s"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        queryParms.clear();
        queryParms.put("Quantity:missing", Collections.singletonList("true"));
        queryParms.put("Range", Collections.singletonList("ge4||s"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
    }

    @Test
    public void testSearchQuantity_Quantity_missing_range_missing() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("Quantity:missing", Collections.singletonList("false"));
        queryParms.put("Range:missing", Collections.singletonList("false"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        queryParms.clear();
        queryParms.put("Quantity:missing", Collections.singletonList("false"));
        queryParms.put("Range:missing", Collections.singletonList("true"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
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
        assertSearchDoesntReturnSavedResource("Range", "ap4||s");
        assertSearchReturnsSavedResource("Range", "ap5||s");
        assertSearchReturnsSavedResource("Range", "ap10||s");
        assertSearchReturnsSavedResource("Range", "ap11||s");
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
        assertSearchDoesntReturnSavedResource("Range", "ge10||s");
        assertSearchDoesntReturnSavedResource("Range", "ge11||s");
    }

    // Range is 5-10 seconds
    @Test
    public void testSearchQuantity_Range_LE() throws Exception {
        assertSearchDoesntReturnSavedResource("Range", "le4||s");
        assertSearchDoesntReturnSavedResource("Range", "le5||s");
        assertSearchReturnsSavedResource("Range", "le10||s");
        assertSearchReturnsSavedResource("Range", "le10.01||s");
        assertSearchReturnsSavedResource("Range", "le11||s");
    }

    // Range is 5-unknown seconds
    @Test
    public void testSearchQuantity_Range_NoHigh_EQ_Implied() throws Exception {
        // The range of the search value cannot fully contain the range
        // of the target value because the target has no high value,
        // so an equals search will never succeed.
        assertSearchDoesntReturnSavedResource("Range-noHigh", "1e1||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "4||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "5||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "10||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "11||s");
    }

    // Range is 5-unknown seconds
    @Test
    public void testSearchQuantity_Range_NoHigh_NE() throws Exception {
        // The range of the search value cannot fully contain the range
        // of the target value because the target has no high value,
        // so a not-equals search will always succeed.
        assertSearchReturnsSavedResource("Range-noHigh", "ne1e1||s");
        assertSearchReturnsSavedResource("Range-noHigh", "ne4||s");
        assertSearchReturnsSavedResource("Range-noHigh", "ne5||s");
        assertSearchReturnsSavedResource("Range-noHigh", "ne10||s");
        assertSearchReturnsSavedResource("Range-noHigh", "ne11||s");
    }

    // Range is 5-unknown seconds
    @Test
    public void testSearchQuantity_Range_NoHigh_AP() throws Exception {
        assertSearchReturnsSavedResource("Range-noHigh", "ap1e1||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "ap4||s");
        assertSearchReturnsSavedResource("Range-noHigh", "ap5||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "ap5.7||s");
        assertSearchReturnsSavedResource("Range-noHigh", "ap6||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "ap10||s");
    }

    // Range is 5-unknown seconds
    @Test
    public void testSearchQuantity_Range_NoHigh_LT() throws Exception {
        assertSearchDoesntReturnSavedResource("Range-noHigh", "lt4||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "lt5||s");
        assertSearchReturnsSavedResource("Range-noHigh", "lt5.000000001||s");
        assertSearchReturnsSavedResource("Range-noHigh", "lt10||s");
        assertSearchReturnsSavedResource("Range-noHigh", "lt11||s");
    }

    // Range is 5-unknown seconds
    @Test
    public void testSearchQuantity_Range_NoHigh_GT() throws Exception {
        assertSearchReturnsSavedResource("Range-noHigh", "gt4||s");
        assertSearchReturnsSavedResource("Range-noHigh", "gt4.99999999||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "gt5||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "gt10||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "gt11||s");
    }

    // Range is 5-unknown seconds
    @Test
    public void testSearchQuantity_Range_NoHigh_EB() throws Exception {
        // The range below the search value cannot fully contain the range
        // of the target value because the target has no high value,
        // so an ends-before search will never succeed.
        assertSearchDoesntReturnSavedResource("Range-noHigh", "eb4||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "eb5||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "eb10||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "eb1000||s");
    }

    // Range is 5-unknown seconds
    @Test
    public void testSearchQuantity_Range_NoHigh_SA() throws Exception {
        assertSearchReturnsSavedResource("Range-noHigh", "sa4||s");
        assertSearchReturnsSavedResource("Range-noHigh", "sa4.99999999||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "sa5||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "sa10||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "sa10.0||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "sa11||s");
    }

    // Range is 5-unknown seconds
    @Test
    public void testSearchQuantity_Range_NoHigh_GE() throws Exception {
        assertSearchReturnsSavedResource("Range-noHigh", "ge4||s");
        assertSearchReturnsSavedResource("Range-noHigh", "ge5||s");
        assertSearchReturnsSavedResource("Range-noHigh", "ge5.0||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "ge5.000000001||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "ge10||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "ge11||s");
    }

    // Range is 5-unknown seconds
    @Test
    public void testSearchQuantity_Range_NoHigh_LE() throws Exception {
        assertSearchDoesntReturnSavedResource("Range-noHigh", "le4||s");
        assertSearchDoesntReturnSavedResource("Range-noHigh", "le5||s");
        assertSearchReturnsSavedResource("Range-noHigh", "le5.000000001||s");
        assertSearchReturnsSavedResource("Range-noHigh", "le10||s");
        assertSearchReturnsSavedResource("Range-noHigh", "le11||s");
    }

    // Range is unknown-10 seconds
    @Test
    public void testSearchQuantity_Range_NoLow_EQ_Implied() throws Exception {
        // The range of the search value cannot fully contain the range
        // of the target value because the target has no low value,
        // so an equals search will never succeed.
        assertSearchDoesntReturnSavedResource("Range-noLow", "1e1||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "4||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "5||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "10||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "11||s");
    }

    // Range is unknown-10 seconds
    @Test
    public void testSearchQuantity_Range_NoLow_NE() throws Exception {
        // The range of the search value cannot fully contain the range
        // of the target value because the target has no low value,
        // so a not-equals search will always succeed.
        assertSearchReturnsSavedResource("Range-noLow", "ne1e1||s");
        assertSearchReturnsSavedResource("Range-noLow", "ne4||s");
        assertSearchReturnsSavedResource("Range-noLow", "ne5||s");
        assertSearchReturnsSavedResource("Range-noLow", "ne10||s");
        assertSearchReturnsSavedResource("Range-noLow", "ne11||s");
    }

    // Range is unknown-10 seconds
    @Test
    public void testSearchQuantity_Range_NoLow_AP() throws Exception {
        assertSearchReturnsSavedResource("Range-noLow", "ap1e1||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "ap4||s");
        assertSearchReturnsSavedResource("Range-noLow", "ap9||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "ap9.05||s");
        assertSearchReturnsSavedResource("Range-noLow", "ap10||s");
        assertSearchReturnsSavedResource("Range-noLow", "ap11||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "ap11.5||s");
    }

    // Range is unknown-10 seconds
    @Test
    public void testSearchQuantity_Range_NoLow_LT() throws Exception {
        assertSearchDoesntReturnSavedResource("Range-noLow", "lt4||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "lt5||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "lt10||s");
        assertSearchReturnsSavedResource("Range-noLow", "lt10.000000001||s");
        assertSearchReturnsSavedResource("Range-noLow", "lt11||s");
    }

    // Range is unknown-10 seconds
    @Test
    public void testSearchQuantity_Range_NoLow_GT() throws Exception {
        assertSearchReturnsSavedResource("Range-noLow", "gt4||s");
        assertSearchReturnsSavedResource("Range-noLow", "gt5||s");
        assertSearchReturnsSavedResource("Range-noLow", "gt9.99999999||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "gt10||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "gt11||s");
    }

    // Range is unknown-10 seconds
    @Test
    public void testSearchQuantity_Range_NoLow_EB() throws Exception {
        assertSearchDoesntReturnSavedResource("Range-noLow", "eb4||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "eb5||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "eb10||s");
        assertSearchReturnsSavedResource("Range-noLow", "eb10.000000001||s");
        assertSearchReturnsSavedResource("Range-noLow", "eb11||s");
    }

    // Range is unknown-10 seconds
    @Test
    public void testSearchQuantity_Range_NoLow_SA() throws Exception {
        // The range above the search value cannot fully contain the range
        // of the target value because the target has no low value,
        // so a starts-after search will never succeed.
        assertSearchDoesntReturnSavedResource("Range-noLow", "sa4||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "sa5||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "sa10||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "sa10.0||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "sa1000||s");
    }

    // Range is unknown-10 seconds
    @Test
    public void testSearchQuantity_Range_NoLow_GE() throws Exception {
        assertSearchReturnsSavedResource("Range-noLow", "ge4||s");
        assertSearchReturnsSavedResource("Range-noLow", "ge5||s");
        assertSearchReturnsSavedResource("Range-noLow", "ge9.99999999||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "ge10||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "ge11||s");
    }

    // Range is unknown-10 seconds
    @Test
    public void testSearchQuantity_Range_NoLow_LE() throws Exception {
        assertSearchDoesntReturnSavedResource("Range-noLow", "le4||s");
        assertSearchDoesntReturnSavedResource("Range-noLow", "le5||s");
        assertSearchReturnsSavedResource("Range-noLow", "le10||s");
        assertSearchReturnsSavedResource("Range-noLow", "le10.000000001||s");
        assertSearchReturnsSavedResource("Range-noLow", "le11||s");
    }

    @Test
    public void testSearchQuantity_Range_missing() throws Exception {
        assertSearchReturnsSavedResource("Range:missing", "false");
        assertSearchDoesntReturnSavedResource("Range:missing", "true");
        assertSearchReturnsSavedResource("missing-Range:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Range:missing", "false");
    }

    // Target value is 1.2E+2 (115-125)
    @Test
    public void testSearchQuantity_Exponent() throws Exception {
        // Value 1.2E+2 (115-125) should return the value
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "1.2E2");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "1.2E+2");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "1.2e2");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "1.2e+2");

        // here we use the non-range interpretation of `ap` and so 1.2e2 becomes [103-137]
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "ap1.2e2");

        // the prefixes give the search value absolute precision and so the search value is
        // the same as <prefix>120, despite the differing significant digits
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "lt1.2e2");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "gt1.2e2");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "le1.2e2");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "ge1.2e2");
        assertSearchDoesntReturnSavedResource("Quantity-withExponent", "sa1.2e2");
        assertSearchDoesntReturnSavedResource("Quantity-withExponent", "eb1.2e2");

        // Value 120 should not return the value since the range of 120
        // (119.5-120.5) is smaller than the range of the indexed value
        // 1.2E+2 (115-125) due to the difference in scale of those values.
        assertSearchDoesntReturnSavedResource("Quantity-withExponent", "120");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "1e2");

        // here we use the non-range interpretation of `ap` and so 114 becomes [102.1-125.9]
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "ap114");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "ap126");

        // the prefixes give the search value absolute precision and so the search value is
        // the same as <prefix>100, despite the differing significant digits
        assertSearchDoesntReturnSavedResource("Quantity-withExponent", "lt1e2");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "gt1e2");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "le1e2");
        assertSearchReturnsSavedResource(     "Quantity-withExponent", "ge1e2");
        assertSearchDoesntReturnSavedResource("Quantity-withExponent", "sa1e2");
        assertSearchDoesntReturnSavedResource("Quantity-withExponent", "eb1e2");
    }

    @Test
    public void testSearchQuantity_Quantity_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Quantity:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.Quantity:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-Quantity:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-Quantity:missing", "false");
    }
}
