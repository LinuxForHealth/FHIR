/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.search.test;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Basic;

/**
 * @author lmsurpre
 * @see https://hl7.org/fhir/dstu2/search.html#quantity
 */
public abstract class AbstractSearchQuantityTest extends AbstractPLSearchTest {

    @Test
    public void testCreateBasicResource() throws Exception {
        Basic resource = readResource(Basic.class, "BasicQuantity.json");
        saveBasicResource(resource);
    }

    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "25|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "25||s");
        
        // DSTU2 does not say if this is allowed or not, but we do not support it.
        // In more recent versions, they clarified that it should work:  https://build.fhir.org/search.html#quantity
//        assertSearchReturnsSavedResource("Quantity", "25");
        
        // I think this should return the resource but it currently doesn't.
        // https://gforge.hl7.org/gf/project/fhir/tracker/?action=TrackerItemEdit&tracker_item_id=19597
//        assertSearchReturnsSavedResource("Quantity", "25||sec");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_withModifiers() throws Exception {
        assertSearchReturnsSavedResource("Quantity", "lt26|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "gt24|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "le26|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "le25|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "ge25|http://unitsofmeasure.org|s");
        assertSearchReturnsSavedResource("Quantity", "ge24|http://unitsofmeasure.org|s");
    }

    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_NoDisplayUnit() throws Exception {
        assertSearchReturnsSavedResource("Quantity-noDisplayUnit", "1|http://snomed.info/sct|385049006");
        assertSearchReturnsSavedResource("Quantity-noDisplayUnit", "1||385049006");
    }

    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_NoCode() throws Exception {
        assertSearchReturnsSavedResource("Quantity-noCode", "1||eq");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_NoCodeOrUnit() throws Exception {
        // spec isn't clear about whether quantities with no unit should be indexed
        // but since we require the unit while searching, it doesn't really matter
        assertSearchDoesntReturnSavedResource("Quantity-noCodeOrUnit", "1||eq");
    }
    
    /*********************************************************************************
     * FHIR Server does not yet use quantity comparator to calculate search results. *
     *********************************************************************************/
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_LessThan() throws Exception {
        // Later versions of the spec indicate that there is an implicit precision 
        // of .5 of the next least significant digit.  We don't support that now, but 
        // lets use numbers far enough away that it won't matter.
//        assertSearchReturnsSavedResource("Quantity-lessThan", "2||lt");
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "4||lt");
        
        // With implicit ranges, 3 (+/-0.5) actually might be < 3
//      assertSearchDoesntReturnSavedResource("Quantity-lessThan", "3||lt");
        
//        assertSearchReturnsSavedResource("Quantity-lessThan", "lt2||lt");      // < 3 may be < 2
        assertSearchReturnsSavedResource("Quantity-lessThan", "gt2||lt");      // < 3 may be > 2
        assertSearchReturnsSavedResource("Quantity-lessThan", "lt4||lt");      // < 3 may be < 4 
        assertSearchDoesntReturnSavedResource("Quantity-lessThan", "gt4||lt"); // < 3 is not > 4
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_GreaterThan() throws Exception {
        // Later versions of the spec indicate that there is an implicit precision 
        // of .5 of the next least significant digit.  We don't support that now, but 
        // lets use numbers far enough away that it won't matter.
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "2||gt");
//        assertSearchReturnsSavedResource("Quantity-greaterThan", "4||gt");
        
        // With implicit ranges, 3 (+/-0.5) actually might be > 3
//      assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "3||gt");
        
        assertSearchDoesntReturnSavedResource("Quantity-greaterThan", "lt2||gt"); // > 3 is not < 2
        assertSearchReturnsSavedResource("Quantity-greaterThan", "gt2||gt");      // > 3 may be > 2
        assertSearchReturnsSavedResource("Quantity-greaterThan", "lt4||gt");      // > 3 may be < 4 
//        assertSearchReturnsSavedResource("Quantity-greaterThan", "gt4||gt");      // > 3 may be > 4
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_LessThanOrEqual() throws Exception {
//        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "2||lte");
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "3||lte");
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "4||lte");
        
//        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "lt2||lte");      // <= 3 may be < 2
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "gt2||lte");      // <= 3 may be > 2
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "lt4||lte");      // <= 3 may be < 4 
        assertSearchDoesntReturnSavedResource("Quantity-lessThanOrEqual", "gt4||lte"); // <= 3 is not > 4
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "le3||lte");      // <= 3 is <= 3
        assertSearchReturnsSavedResource("Quantity-lessThanOrEqual", "ge3||lte");      // <= 3 may be >= 3
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_GreaterThanOrEqual() throws Exception {
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "2||gte");
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "3||gte");
//        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "4||gte");
        
        assertSearchDoesntReturnSavedResource("Quantity-greaterThanOrEqual", "lt2||gte"); // >= 3 is not < 2
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "gt2||gte");      // >= 3 may be > 2
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "lt4||gte");      // >= 3 may be < 4 
//        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "gt4||gte");      // >= 3 may be > 4
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "le3||gte");         // >= 3 may be <= 3
        assertSearchReturnsSavedResource("Quantity-greaterThanOrEqual", "ge3||gte");         // >= 3 is >= 3
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchQuantity_Quantity_missing() throws Exception {
        assertSearchReturnsSavedResource("Quantity:missing", "false");
        assertSearchDoesntReturnSavedResource("Quantity:missing", "true");
        
        assertSearchReturnsSavedResource("missing-Quantity:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Quantity:missing", "false");
    }
}
