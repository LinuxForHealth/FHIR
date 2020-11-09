/*
 * (C) Copyright IBM Corp. 2019,2020
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
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Test the BoundingRadius with NearLocationHandler
 */
public class NearLocationHandlerBoundingRadiusTest {

    @Test
    public void testLocationBoundaryPositionsFromParameters() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("near", Collections.singletonList("-90.0|0.0|1.0|mi_us"));
        FHIRSearchContext ctx = SearchUtil.parseQueryParameters(Location.class, queryParms, true);
        NearLocationHandler handler = new NearLocationHandler();
        handler.setBounding(true);
        List<Bounding> bounding = handler.generateLocationPositionsFromParameters(ctx.getSearchParameters());
        assertNotNull(bounding);

        BoundingRadius boundingBox = (BoundingRadius) bounding.get(0);
        assertEquals(boundingBox.getLatitude(), Double.valueOf("-90.0"));
        assertEquals(boundingBox.getLongitude(), Double.valueOf("0.0"));
        assertEquals(boundingBox.getRadius(), Double.valueOf("1.609344"));
        assertEquals(boundingBox.getType(), BoundingType.RADIUS);
    }
}