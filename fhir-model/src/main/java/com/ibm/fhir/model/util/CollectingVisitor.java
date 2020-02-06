/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibm.fhir.model.visitor.DefaultVisitor;
import com.ibm.fhir.model.visitor.Visitable;

/**
 * Visits a Resource or Element and collects all of its descendants of a given type into a single list
 * 
 * @param <T> The type of object to collect
 * @implNote The order of the list will be consistent with a depth-first traversal of the visited object
 */
public class CollectingVisitor<T> extends DefaultVisitor {
    protected final List<T> result = new ArrayList<>();
    protected final Class<T> type;

    public CollectingVisitor(Class<T> type) {
        super(true);
        this.type = type;
    }

    protected void collect(Object object) {
        if (type.isInstance(object)) {
            result.add(type.cast(object));
        }
    }

    public List<T> getResult() {
        return Collections.unmodifiableList(result);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Visitable visitable) {
        collect(visitable);
        return true;
    }

    @Override
    public void visit(java.lang.String elementName, byte[] value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, BigDecimal value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Boolean value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Integer value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, LocalDate value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, LocalTime value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.String value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, Year value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, YearMonth value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, ZonedDateTime value) {
        collect(value);
    }
}
