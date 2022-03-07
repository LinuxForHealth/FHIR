/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.location;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.bounding.BoundingRadius;
import com.ibm.fhir.search.location.bounding.BoundingType;
import com.ibm.fhir.search.test.BaseSearchTest;
import com.ibm.fhir.search.util.SearchHelper;

/**
 * Test the BoundingRadius with NearLocationHandler
 */
public class NearLocationHandlerBoundingRadiusTest extends BaseSearchTest {

    @Test
    public void testLocationBoundaryPositionsFromParameters() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("near", Collections.singletonList("-90.0|0.0|1.0|mi_us"));
        FHIRSearchContext ctx = searchHelper.parseQueryParameters(Location.class, queryParms, true, true);
        NearLocationHandler handler = new NearLocationHandler();
        handler.setBounding(true);
        List<Bounding> bounding = handler.generateLocationPositionsFromParameters(ctx.getSearchParameters());
        assertNotNull(bounding);

        BoundingRadius boundingRadius = (BoundingRadius) bounding.get(0);
        assertEquals(boundingRadius.getLatitude(), Double.valueOf("-90.0"));
        assertEquals(boundingRadius.getLongitude(), Double.valueOf("0.0"));
        assertEquals(boundingRadius.getRadius(), Double.valueOf("1.609344"));
        assertEquals(boundingRadius.getType(), BoundingType.RADIUS);
    }
}