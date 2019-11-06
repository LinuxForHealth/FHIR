/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.model.type.code.SearchParamType.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.model.visitor.DefaultVisitor;
import com.ibm.fhir.model.visitor.Visitable;
import com.ibm.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.fhir.persistence.jdbc.dto.Parameter.TimeType;

/**
 * This class is the JDBC persistence layer implementation for transforming SearchParameters into Parameter Data Transfer Objects.
 *
 * <p>Call {@code Element.accept} with this visitor to add zero to many Parameters to the result list and invoke {@code getResult}
 * to get the current list of extracted Parameter objects.
 *
 * <p>Note: this class DOES NOT set the resourceType on the underlying JDBC Parameter objects it creates;
 * that is a responsibility of the caller.
 */
public class JDBCParameterBuildingVisitor extends DefaultVisitor {
    private static final Logger log = Logger.getLogger(JDBCParameterBuildingVisitor.class.getName());

    public static final String EXCEPTION_MSG = "Unexpected error while processing parameter [%s] with value [%s]";
    public static final String EXCEPTION_MSG_NAME_ONLY = "Unexpected error while processing parameter [%s]";

    // Datetime Limits from
    // DB2: https://www.ibm.com/support/knowledgecenter/en/SSEPGG_10.5.0/com.ibm.db2.luw.sql.ref.doc/doc/r0001029.html
    // Derby: https://db.apache.org/derby/docs/10.0/manuals/reference/sqlj271.html
    private static final Timestamp SMALLEST_TIMESTAMP = Timestamp.valueOf("0001-01-01 00:00:00.000000");
    private static final Timestamp LARGEST_TIMESTAMP = Timestamp.valueOf("9999-12-31 23:59:59.999999");

    // We only need the SearchParameter type and code, so just store those directly as members
    private final String searchParamCode;
    private final SearchParamType searchParamType;

    /**
     * The result of the visit(s)
     */
    private List<Parameter> result;

    public JDBCParameterBuildingVisitor(SearchParameter searchParameter) {
        super(false);
        this.searchParamCode = searchParameter.getCode().getValue();
        this.searchParamType = searchParameter.getType();

        result = new ArrayList<Parameter>();
    }

    /**
     * @return the Parameters extracted from the visited Elements
     */
    public List<Parameter> getResult() {
        return Collections.unmodifiableList(result);
    }

    @Override
    public boolean visit(String elementName, int elementIndex, Visitable visitable) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("The processing of %s is unsupported", visitable.getClass().getName()));
        }
        return false;
    }

    /*====================
     * Primitive Types   *
     ====================*/

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Boolean _boolean) {
        Parameter p = new Parameter();
        if (!TOKEN.equals(searchParamType)) {
            throw invalidComboException(searchParamType, _boolean);
        }
        p.setName(searchParamCode);
        p.setValueSystem("http://terminology.hl7.org/CodeSystem/special-values");
        if (_boolean.getValue()) {
            p.setValueCode("true");
        } else {
            p.setValueCode("false");
        }
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Canonical canonical) {
        Parameter p = new Parameter();
        if (!REFERENCE.equals(searchParamType) && !URI.equals(searchParamType)) {
            throw invalidComboException(searchParamType, canonical);
        }
        p.setName(searchParamCode);
        p.setValueString(canonical.getValue());
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Code code) {
        Parameter p = new Parameter();
        if (!TOKEN.equals(searchParamType)) {
            throw invalidComboException(searchParamType, code);
        }
        p.setName(searchParamCode);
        // TODO: get the implicit code system
        p.setValueCode(code.getValue());
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Date date) {
        Parameter p = new Parameter();
        if (!DATE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, date);
        }
        p.setName(searchParamCode);
        setDateValues(p, date);
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.DateTime dateTime) {
        Parameter p = new Parameter();
        if (!DATE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, dateTime);
        }
        p.setName(searchParamCode);
        setDateValues(p, dateTime);
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Decimal decimal) {
        Parameter p = new Parameter();
        if (!NUMBER.equals(searchParamType)) {
            throw invalidComboException(searchParamType, decimal);
        }
        p.setName(searchParamCode);
        p.setValueNumber(decimal.getValue());
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Id id) {
        Parameter p = new Parameter();
        if (!TOKEN.equals(searchParamType)) {
            throw invalidComboException(searchParamType, id);
        }
        p.setName(searchParamCode);
        p.setValueCode(id.getValue());
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Instant instant) {
        Parameter p = new Parameter();
        if (!DATE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, instant);
        }
        p.setName(searchParamCode);
        p.setValueDate(Timestamp.from(instant.getValue().toInstant()));
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Integer integer) {
        Parameter p = new Parameter();
        if (!NUMBER.equals(searchParamType)) {
            throw invalidComboException(searchParamType, integer);
        }
        p.setName(searchParamCode);
        // TODO: consider moving integer values to separate column so they can be searched different from decimals
        p.setValueNumber(new BigDecimal(integer.getValue()));
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(String elementName, int elementIndex, com.ibm.fhir.model.type.String value) {
        Parameter p = new Parameter();
        if (STRING.equals(searchParamType)) {
            p.setValueString(value.getValue());
        } else if (TOKEN.equals(searchParamType)) {
            p.setValueCode(value.getValue());
        } else {
            throw invalidComboException(searchParamType, value);
        }
        p.setName(searchParamCode);
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(String elementName, int elementIndex, Uri uri) {
        Parameter p = new Parameter();
        if (!URI.equals(searchParamType) && !REFERENCE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, uri);
        }
        p.setName(searchParamCode);
        p.setValueString(uri.getValue());
        result.add(p);
        return false;
    }

    /*====================
     * Data Types        *
     ====================*/

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Address address) {
        Parameter p;
        if (!STRING.equals(searchParamType)) {
            throw invalidComboException(searchParamType, address);
        }
        for (com.ibm.fhir.model.type.String aLine : address.getLine()) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(aLine.getValue());
            result.add(p);
        }
        if (address.getCity() != null) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(address.getCity().getValue());
            result.add(p);
        }
        if (address.getDistrict() != null) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(address.getDistrict().getValue());
            result.add(p);
        }
        if (address.getState() != null) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(address.getState().getValue());
            result.add(p);
        }
        if (address.getCountry() != null) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(address.getCountry().getValue());
            result.add(p);
        }
        if (address.getPostalCode() != null) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(address.getPostalCode().getValue());
            result.add(p);
        }
        if (address.getText() != null) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(address.getText().getValue());
            result.add(p);
        }
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeableConcept codeableConcept) {
        if (!TOKEN.equals(searchParamType)) {
            throw invalidComboException(searchParamType, codeableConcept);
        }
        for (Coding coding : codeableConcept.getCoding()) {
            visit(elementName, elementIndex, coding);
        }
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coding coding) {
        Parameter p = new Parameter();
        if (!TOKEN.equals(searchParamType)) {
            throw invalidComboException(searchParamType, coding);
        }
        p.setName(searchParamCode);
        if (coding.getSystem() != null) {
            p.setValueSystem(coding.getSystem().getValue());
        }
        if (coding.getCode() != null) {
            p.setValueCode(coding.getCode().getValue());
        }
        result.add(p);
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ContactPoint contactPoint) {
        if (!TOKEN.equals(searchParamType)) {
            throw invalidComboException(searchParamType, contactPoint);
        }
        if (contactPoint.getValue() != null) {
            Parameter telecom = new Parameter();
            telecom.setName(searchParamCode);
            telecom.setValueCode(contactPoint.getValue().getValue());
            result.add(telecom);
        }
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HumanName humanName) {
        Parameter p;
        if (!STRING.equals(searchParamType)) {
            throw invalidComboException(searchParamType, humanName);
        }
        if (humanName.getFamily() != null) {
            // family is just a string in R4 (not a list)
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(humanName.getFamily().getValue());
            result.add(p);
        }
        for (com.ibm.fhir.model.type.String given : humanName.getGiven()) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(given.getValue());
            result.add(p);
        }
        for (com.ibm.fhir.model.type.String prefix : humanName.getPrefix()) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(prefix.getValue());
            result.add(p);
        }
        for (com.ibm.fhir.model.type.String suffix : humanName.getSuffix()) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(suffix.getValue());
            result.add(p);
        }
        if (humanName.getText() != null) {
            p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(humanName.getText().getValue());
            result.add(p);
        }
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Identifier identifier) {
        if (!TOKEN.equals(searchParamType)) {
            throw invalidComboException(searchParamType, identifier);
        }
        if (identifier != null && identifier.getValue() != null) {
            Parameter p = new Parameter();
            p.setName(searchParamCode);
            if (identifier.getSystem() != null) {
                p.setValueSystem(identifier.getSystem().getValue());
            }
            p.setValueCode(identifier.getValue().getValue());
            result.add(p);
        }
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Money money) {
        if (!QUANTITY.equals(searchParamType)) {
            throw invalidComboException(searchParamType, money);
        }
        if (money != null && money.getValue() != null && money.getValue().getValue() != null) {
            Parameter p = new Parameter();
            p.setName(searchParamCode);
            p.setValueNumber(money.getValue().getValue());
            if (money.getCurrency() != null) {
                p.setValueCode(money.getCurrency().getValue());
            }
            result.add(p);
        }
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Period period) {
        if (!DATE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, period);
        }
        if (period.getStart() == null && period.getEnd() == null) {
            // early exit
            return false;
        }
        Parameter p = new Parameter();
        p.setName(searchParamCode);
        if (period.getStart() == null || period.getStart().getValue() == null) {
            p.setValueDateStart(SMALLEST_TIMESTAMP);
        } else {
            java.time.Instant startInst = QueryBuilderUtil.getInstant(period.getStart());
            p.setValueDateStart(Timestamp.from(startInst));
        }
        if (period.getEnd() == null || period.getEnd().getValue() == null) {
            p.setValueDateEnd(LARGEST_TIMESTAMP);
        } else {
            java.time.Instant endInst = QueryBuilderUtil.getInstant(period.getEnd());
            p.setValueDateEnd(Timestamp.from(endInst));
        }
        result.add(p);
        return false;
    }
    // Also handles Quantity subtypes like Age and Duration
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Quantity quantity) {
        if (!QUANTITY.equals(searchParamType)) {
            throw invalidComboException(searchParamType, quantity);
        }
        if (quantity != null && quantity.getValue() != null && quantity.getValue().getValue() != null &&
                (quantity.getCode() != null || quantity.getUnit() != null)) {
            Parameter p = new Parameter();
            p.setName(searchParamCode);
            p.setValueNumber(quantity.getValue().getValue());
            // see https://gforge.hl7.org/gf/project/fhir/tracker/?action=TrackerItemEdit&tracker_item_id=19597
            if (quantity.getCode() != null) {
                p.setValueCode(quantity.getCode().getValue());
                if (quantity.getSystem() != null) {
                    p.setValueSystem(quantity.getSystem().getValue());
                }
            } else if (quantity.getUnit() != null) {
                p.setValueCode(quantity.getUnit().getValue());
            }
            result.add(p);
        }
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Range range) {
        if (!QUANTITY.equals(searchParamType)) {
            throw invalidComboException(searchParamType, range);
        }
        // The parameter isn't added unless either low or high holds a value
        Parameter p = new Parameter();
        p.setName(searchParamCode);
        if (range.getLow() != null && range.getLow().getValue() != null && range.getLow().getValue().getValue() != null) {
            if (range.getLow().getSystem() != null) {
                p.setValueSystem(range.getLow().getSystem().getValue());
            }
            if (range.getLow().getCode() != null) {
                p.setValueCode(range.getLow().getCode().getValue());
            } else if (range.getLow().getUnit() != null) {
                p.setValueCode(range.getLow().getUnit().getValue());
            }
            p.setValueNumberLow(range.getLow().getValue().getValue());

            // The unit and code/system elements of the low or high elements SHALL match
            if (range.getHigh() != null && range.getHigh().getValue() != null && range.getHigh().getValue().getValue() != null) {
                p.setValueNumberHigh(range.getHigh().getValue().getValue());
            }

            result.add(p);
        } else if (range.getHigh() != null && range.getHigh().getValue() != null && range.getHigh().getValue().getValue() != null) {
            if (range.getHigh().getSystem() != null) {
                p.setValueSystem(range.getHigh().getSystem().getValue());
            }
            if (range.getHigh().getCode() != null) {
                p.setValueCode(range.getHigh().getCode().getValue());
            } else if (range.getHigh().getUnit() != null) {
                p.setValueCode(range.getHigh().getUnit().getValue());
            }
            p.setValueNumberHigh(range.getHigh().getValue().getValue());
            result.add(p);
        }
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Reference reference) {
        if (!REFERENCE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, reference);
        }
        if (reference.getReference() != null) {
            Parameter p = new Parameter();
            p.setName(searchParamCode);
            p.setValueString(reference.getReference().getValue());
            result.add(p);
        }
        return false;
    }
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Timing timing) {
        if (!DATE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, timing);
        }
        /*
         * The specified scheduling details are ignored and only the outer limits matter. For instance, a schedule that
         * specifies every second day between 31-Jan 2013 and 24-Mar 2013 includes 1-Feb 2013, even though that is on an
         * odd day that is not specified by the period. This is to keep the server load processing queries reasonable.
         */

        Timing.Repeat repeat = timing.getRepeat();
        if (repeat != null && repeat.getBounds() != null) {
            // bounds[x] is either Duration, Range, or Period; all 3 have their own visitor method
            // so use the visitor pattern (double-dispatch) back to the appropriate one
            repeat.getBounds().accept(this);
        } else {
            // TODO how to properly calculate the outer limits if there are no "bounds"?
        }
        return false;
    }

    /*====================
     * Date/Time helpers *
     ====================*/

    /**
     * Configure the date values in the parameter based on the model {@link Date} which again might be partial
     * (Year/YearMonth/LocalDate)
     *
     * @param p
     * @param date
     */
    private void setDateValues(Parameter p, Date date) {
        java.time.Instant start = QueryBuilderUtil.getStart(date);
        java.time.Instant end = QueryBuilderUtil.getEnd(date);
        setDateValues(p, start, end);
    }

    /**
     * Configure the date values in the parameter based on the model {@link DateTime} and the type of date it
     * represents.
     *
     * @param p
     * @param dateTime
     */
    private void setDateValues(Parameter p, DateTime dateTime) {

        if (!dateTime.isPartial()) {
            // fully specified time including zone, so we can interpret as an instant
            p.setValueDate(java.sql.Timestamp.from(java.time.Instant.from(dateTime.getValue())));
        } else {
            java.time.Instant start = QueryBuilderUtil.getStart(dateTime);
            java.time.Instant end = QueryBuilderUtil.getEnd(dateTime);
            setDateValues(p, start, end);
        }
    }

    /**
     * Set the date values on the {@link Parameter}, adjusting the end time slightly to make it exclusive (which is a TODO to fix).
     *
     * @param p
     * @param start
     * @param end
     */
    private void setDateValues(Parameter p, java.time.Instant start, java.time.Instant end) {
        Timestamp startTime = Timestamp.from(start);
        p.setValueDateStart(startTime);
        p.setValueDate(startTime);
        p.setTimeType(TimeType.UNKNOWN);

        Timestamp implicitEndExclusive = Timestamp.from(end);
        // TODO: Is it possible to avoid this by using <= or BETWEEN instead of < when constructing the query?
        Timestamp implicitEndInclusive = convertToInclusiveEnd(implicitEndExclusive);
        p.setValueDateEnd(implicitEndInclusive);
    }

    /**
     * Convert a period's end timestamp from an exclusive end timestamp to an inclusive one
     *
     * @param exlusiveEndTime
     * @return inclusiveEndTime
     */
    private Timestamp convertToInclusiveEnd(Timestamp exlusiveEndTime) {
        // Our current schema uses the db2/derby default of 6 decimal places (1000 nanoseconds) for fractional seconds.
        return Timestamp.from(exlusiveEndTime.toInstant().minusNanos(1000));
    }

    private IllegalArgumentException invalidComboException(SearchParamType paramType, Element value) {
        return new IllegalArgumentException("Data type '" + value.getClass().getSimpleName() + "' is not supported "
                + "for SearchParameter '" + searchParamCode + "' of type '" + paramType.getValue() + "'");
    }
}
