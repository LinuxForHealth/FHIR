/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.model.visitor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Visits a Resource or Element and collects all of its descendants of a given type into a collection
 * of those elements, indexed by their simple FHIRPath path.
 *
 * @param <T> The type of object to collect
 * @implNote The order of the list will be consistent with a depth-first traversal of the visited object
 */
public class PathAwareCollectingVisitor<T> extends PathAwareVisitor {
    protected final Map<String, T> result = new LinkedHashMap<>();
    protected final Class<T> type;

    public PathAwareCollectingVisitor(Class<T> type) {
        super();
        this.type = type;
    }

    protected void collect(Object object) {
        if (type.isInstance(object)) {
            result.put(getPath(), type.cast(object));
        }
    }

    public Map<String, T> getResult() {
        return Collections.unmodifiableMap(result);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Visitable visitable) {
        collect(visitable);
        return true;
    }

    @Override
    public void doVisit(java.lang.String elementName, byte[] value) {
        collect(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, BigDecimal value) {
        collect(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, java.lang.Boolean value) {
        collect(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, java.lang.Integer value) {
        collect(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, LocalDate value) {
        collect(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, LocalTime value) {
        collect(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, java.lang.String value) {
        collect(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, Year value) {
        collect(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, YearMonth value) {
        collect(value);
    }

    @Override
    public void doVisit(java.lang.String elementName, ZonedDateTime value) {
        collect(value);
    }
}
