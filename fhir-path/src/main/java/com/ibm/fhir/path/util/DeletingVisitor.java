/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

import com.ibm.fhir.model.builder.Builder;
import com.ibm.fhir.model.visitor.CopyingVisitor;
import com.ibm.fhir.model.visitor.Visitable;

class DeletingVisitor<T extends Visitable> extends CopyingVisitor<T> {
    private String pathToDelete;
    private static final String VALUE = "value";
    private static final Set<String> ALLOWED_STRING_ID = Set.of("id", "url", "value");

    public DeletingVisitor(String pathToDelete) {
        this.pathToDelete = Objects.requireNonNull(pathToDelete, "pathToDelete");
    }

    @Override
    public boolean visit(String elementName, int index, Visitable value) {
        if (pathToDelete.equals(getPath())) {
            delete();
            markDirty();
            return false;
        }
        return true;
    }

    @Override
    public void visit(java.lang.String elementName, byte[] value) {
        removeValueIfTargeted(elementName, byte[].class);
    }

    @Override
    public void visit(java.lang.String elementName, BigDecimal value) {
        removeValueIfTargeted(elementName, BigDecimal.class);
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Boolean value) {
        removeValueIfTargeted(elementName, Boolean.class);
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Integer value) {
        removeValueIfTargeted(elementName, Integer.class);
    }

    @Override
    public void visit(java.lang.String elementName, LocalDate value) {
        removeValueIfTargeted(elementName, LocalDate.class);
    }

    @Override
    public void visit(java.lang.String elementName, LocalTime value) {
        removeValueIfTargeted(elementName, LocalTime.class);
    }

    /**
     * @implNote String has special logic because Resource.id, Element.id, and Extension.url are of type System.String
     */
    @Override
    public void visit(String elementName, String value) {
        if (isTargeted(elementName)) {
            if (!ALLOWED_STRING_ID.contains(elementName)) {
                throw new IllegalArgumentException("Primitive value with name '" + elementName + "' cannot be modified");
            }

            removeValue(elementName, String.class);
        }
    }

    @Override
    public void visit(java.lang.String elementName, Year value) {
        removeValueIfTargeted(elementName, Year.class);
    }

    @Override
    public void visit(java.lang.String elementName, YearMonth value) {
        removeValueIfTargeted(elementName, YearMonth.class);
    }

    @Override
    public void visit(java.lang.String elementName, ZonedDateTime value) {
        removeValueIfTargeted(elementName, ZonedDateTime.class);
    }

    private void removeValueIfTargeted(String elementName, Class<?> valueType) {
        if (isTargeted(elementName)) {
            validateElementNameIsValue(elementName);
            removeValue(elementName, valueType);
        }
    }

    private boolean isTargeted(String elementName) {
        String pathToElement = getPath() + ".";
        return pathToDelete.startsWith(pathToElement) && pathToDelete.substring(pathToElement.length()).equals(elementName);
    }

    private void validateElementNameIsValue(java.lang.String elementName) {
        if (!VALUE.equals(elementName)) {
            throw new IllegalArgumentException("Primitive node with name '" + elementName + "' cannot be modified");
        }
    }

    private void removeValue(String elementName, Class<?> valueType) {
        Builder<?> builder = getBuilder();
        try {
            // reflection can be slow
            Method method = builder.getClass().getDeclaredMethod(elementName, valueType);
            method.invoke(builder, (Object)null);
        } catch (Exception e) {
            throw new RuntimeException("Error while setting '" + elementName + "' on builder of type " + builder.getClass().getCanonicalName(), e);
        }
        markDirty();
    }
}