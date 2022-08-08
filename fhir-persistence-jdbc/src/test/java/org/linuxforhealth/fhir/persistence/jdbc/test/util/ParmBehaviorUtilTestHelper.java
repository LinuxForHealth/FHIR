/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.database.utils.query.WhereFragment;
import org.linuxforhealth.fhir.database.utils.query.expression.StringExpNodeVisitor;
import org.linuxforhealth.fhir.database.utils.query.node.BindMarkerNode;

/**
 * Common helper methods for the [x]ParmBehaviorUtilTests
 */
public class ParmBehaviorUtilTestHelper {
    private static final Logger log = java.util.logging.Logger.getLogger(ParmBehaviorUtilTestHelper.class.getName());

    public static void assertExpectedSQL(WhereFragment actualWhereClauseSegment, String expectedSql, List<Object> expectedBindVariables) {
        assertExpectedSQL(actualWhereClauseSegment, expectedSql, expectedBindVariables, false);
    }

    public static void assertExpectedSQL(WhereFragment actualWhereClauseSegment, String expectedSql,
            List<Object> expectedBindVariables, boolean approx) {
        List<BindMarkerNode> collectBindMarkersInto = new ArrayList<>();
        StringExpNodeVisitor visitor = new StringExpNodeVisitor(null, collectBindMarkersInto, false);
        final String actualWhereClauseString = actualWhereClauseSegment.getExpression().visit(visitor);

        log.fine("whereClauseSegment -> " + actualWhereClauseString);
        log.fine("bind variables -> " + collectBindMarkersInto.stream()
                .map(b -> b.toValueString("~"))
                .collect(Collectors.toList()));

        assertEquals(actualWhereClauseString, expectedSql);
        assertEquals(collectBindMarkersInto.size(), expectedBindVariables.size());

        if(!approx) {
            for (int i=0; i<expectedBindVariables.size(); i++) {
                Object expectedValue = expectedBindVariables.get(i);
                BindMarkerNode bindMarker = collectBindMarkersInto.get(i);

                if (!bindMarker.checkTypeAndValue(expectedValue)) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("BIND[").append(i).append("] ")
                        .append("EXPECTED=").append(expectedValue)
                        .append("; ACTUAL=").append(bindMarker.toValueString("~"));
                    fail(msg.toString());
                }
            }
        }
    }
}
