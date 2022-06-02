/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.visitor;

import static com.ibm.fhir.model.util.ModelSupport.delimit;
import static com.ibm.fhir.model.util.ModelSupport.isKeyword;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;

import net.jcip.annotations.NotThreadSafe;

/**
 * PathAwareVisitor extends the DefaultVisitor with a {@link #getPath()} method that can be used to get the FHIRPath
 * path of a Resource or Element during a visit.
 */
@NotThreadSafe
public class PathAwareVisitor extends DefaultVisitor {
    private static final Logger log = Logger.getLogger(PathAwareVisitor.class.getName());

    private final Stack<String> pathStack = new Stack<>();

    /**
     * Get the FHIRPath path of the Resource or Element currently being visited.
     *
     * This method is primarily for subclasses but can also be used externally to retrieve a path to the Resource
     * or Element that was being visited when an Exception occurs.
     *
     * @return The path of the Resource or Element currently being visited, the path that was being visited when an
     *         exception was thrown, or null if there is no Resource or Element being visited.
     * @implSpec Path segments are appended in the visitStart methods and removed in the visitEnd methods.
     */
    public final String getPath() {
        if (!pathStack.isEmpty()) {
            return pathStack.stream().collect(Collectors.joining("."));
        }
        return null;
    }

    /**
     * Reset the state of the PathAwareVisitor.
     *
     * Invoke this method when visiting has failed and you want to clear the path in order to re-use the visitor.
     */
    public final void reset() {
        if (!pathStack.isEmpty()) {
            pathStack.clear();
        }
    }

    /**
     * @implSpec {@link #visitStart(String, int, Element)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visitStart behavior for Elements.
     */
    protected void doVisitStart(String elementName, int elementIndex, Element element) { }

    /**
     * @implSpec {@link #visitStart(String, int, Resource)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visitStart behavior for Resources.
     */
    protected void doVisitStart(String elementName, int elementIndex, Resource resource) { }

    /**
     * @implSpec {@link #visitEnd(String, int, Element)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visitEnd behavior for Elements.
     */
    protected void doVisitEnd(String elementName, int elementIndex, Element element) { }

    /**
     * @implSpec {@link #visitEnd(String, int, Resource)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visitEnd behavior for Resources.
     */
    protected void doVisitEnd(String elementName, int elementIndex, Resource resource) { }

    private void pathStackPop() {
        pathStack.pop();
    }

    private void pathStackPush(String elementName, int index) {
        if (isKeyword(elementName)) {
            elementName = delimit(elementName);
        }
        if (index != -1) {
            pathStack.push(elementName + "[" + index + "]");
        } else {
            pathStack.push(elementName);
        }
        if (log.isLoggable(Level.FINEST)) {
            log.finest(getPath());
        }
    }

    public PathAwareVisitor() {
        super(true);
    }

    @Override
    public final void visitEnd(java.lang.String elementName, int elementIndex, Element element) {
        doVisitEnd(elementName, elementIndex, element);
        pathStackPop();
    }

    @Override
    public final void visitEnd(java.lang.String elementName, int elementIndex, Resource resource) {
        doVisitEnd(elementName, elementIndex, resource);
        pathStackPop();
    }

    @Override
    public final void visitStart(java.lang.String elementName, int elementIndex, Element element) {
        pathStackPush(elementName, elementIndex);
        doVisitStart(elementName, elementIndex, element);
    }

    @Override
    public final void visitStart(java.lang.String elementName, int elementIndex, Resource resource) {
        pathStackPush(elementName, elementIndex);
        doVisitStart(elementName, elementIndex, resource);
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values of type {@code byte[]}.
     *           Subclasses can override {@link #doVisit(String, byte[])} to provide specific visit behavior.
     * @implNote Needed for FHIR.base64binary element values.
     */
    @Override
    public final void visit(java.lang.String elementName, byte[] value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values of type {@code BigDecimal}.
     *           Subclasses can override {@link #doVisit(String, BigDecimal)} to provide specific visit behavior.
     * @implNote Needed for FHIR.decimal element values.
     */
    @Override
    public final void visit(java.lang.String elementName, BigDecimal value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values of type {@code Boolean}.
     *           Subclasses can override {@link #doVisit(String, Boolean)} to provide specific visit behavior.
     * @implNote Needed for FHIR.decimal element values.
     */
    @Override
    public final void visit(java.lang.String elementName, java.lang.Boolean value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values of type {@code Integer}.
     *           Subclasses can override {@link #doVisit(String, Integer)} to provide specific visit behavior.
     * @implNote Needed for FHIR.integer, FHIR.unsignedInt, and FHIR.positiveInt element values.
     */
    @Override
    public final void visit(java.lang.String elementName, java.lang.Integer value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values of type {@code LocalDate}.
     *           Subclasses can override {@link #doVisit(String, LocalDate)} to provide specific visit behavior.
     * @implNote Needed for FHIR.date element values.
     */
    @Override
    public final void visit(java.lang.String elementName, LocalDate value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values of type {@code LocalTime}.
     *           Subclasses can override {@link #doVisit(String, LocalTime)} to provide specific visit behavior.
     * @implNote Needed for FHIR.time element values.
     */
    @Override
    public final void visit(java.lang.String elementName, LocalTime value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values and non-visitable elements of type {@code String}.
     *           Subclasses can override {@link #doVisit(String, String)} to provide specific visit behavior.
     * @implNote Needed for both
     *           1. FHIR elements like Resource.id, Element.id, and Extension.url which are system strings; and
     *           2. FHIR.string, FHIR.uri, FHIR.code, FHIR.oid, FHIR.id, FHIR.uuid, FHIR.sid, and FHIR.markdown element values.
     */
    @Override
    public final void visit(java.lang.String elementName, java.lang.String value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values of type {@code Year}.
     *           Subclasses can override {@link #doVisit(String, Year)} to provide specific visit behavior.
     * @implNote Needed for FHIR.date and FHIR.dateTime element values with resolution to the year.
     */
    @Override
    public final void visit(java.lang.String elementName, Year value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values of type {@code YearMonth}.
     *           Subclasses can override {@link #doVisit(String, YearMonth)} to provide specific visit behavior.
     * @implNote Needed for FHIR.date and FHIR.dateTime element values with resolution to the month.
     */
    @Override
    public final void visit(java.lang.String elementName, YearMonth value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec PathAwareVisitor makes this method final to ensure that we always set the path for primitive
     *           values of type {@code ZonedDateTime}.
     *           Subclasses can override {@link #doVisit(String, ZonedDateTime)} to provide specific visit behavior.
     * @implNote Needed for FHIR.dateTime and FHIR.instant element values.
     */
    @Override
    public final void visit(java.lang.String elementName, ZonedDateTime value) {
        pathStackPush(elementName, -1);
        doVisit(elementName, value);
        pathStackPop();
    }

    /**
     * @implSpec {@link #visit(String, byte[])} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for byte[] values.
     */
    protected void doVisit(java.lang.String elementName, byte[] value) { }

    /**
     * @implSpec {@link #visit(String, BigDecimal)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for BigDecimal values.
     */
    protected void doVisit(java.lang.String elementName, BigDecimal value) { }

    /**
     * @implSpec {@link #visit(String, Boolean)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for java.lang.Boolean values.
     */
    protected void doVisit(java.lang.String elementName, Boolean value) { }

    /**
     * @implSpec {@link #visit(String, Integer)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for java.lang.Integer values.
     */
    protected void doVisit(java.lang.String elementName, Integer value) { }

    /**
     * @implSpec {@link #visit(String, LocalDate)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for LocalDate values.
     */
    protected void doVisit(java.lang.String elementName, LocalDate value) { }

    /**
     * @implSpec {@link #visit(String, LocalTime)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for LocalTime values.
     */
    protected void doVisit(java.lang.String elementName, LocalTime value) { }

    /**
     * @implSpec {@link #visit(String, String)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for java.lang.String values.
     */
    protected void doVisit(java.lang.String elementName, String value) { }

    /**
     * @implSpec {@link #visit(String, Year)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for Year values.
     */
    protected void doVisit(java.lang.String elementName, Year value) { }

    /**
     * @implSpec {@link #visit(String, YearMonth)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for YearMonth values.
     */
    protected void doVisit(java.lang.String elementName, YearMonth value) { }

    /**
     * @implSpec {@link #visit(String, ZonedDateTime)} was made final (to avoid potential issues with the pathStack)
     * and so this method was introduced to allow subclasses to implement visit behavior for ZonedDateTime values.
     */
    protected void doVisit(java.lang.String elementName, ZonedDateTime value) { }
}
