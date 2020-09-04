/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.scout;

import static com.ibm.fhir.model.type.code.SearchParamType.DATE;
import static com.ibm.fhir.model.type.code.SearchParamType.NUMBER;
import static com.ibm.fhir.model.type.code.SearchParamType.QUANTITY;
import static com.ibm.fhir.model.type.code.SearchParamType.REFERENCE;
import static com.ibm.fhir.model.type.code.SearchParamType.STRING;
import static com.ibm.fhir.model.type.code.SearchParamType.TOKEN;
import static com.ibm.fhir.model.type.code.SearchParamType.URI;
import static com.ibm.fhir.search.date.DateTimeHandler.generateTimestamp;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
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
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.visitor.DefaultVisitor;
import com.ibm.fhir.model.visitor.Visitable;
import com.ibm.fhir.persistence.scout.SearchParameters.BigDec;
import com.ibm.fhir.persistence.scout.SearchParameters.BigInt;
import com.ibm.fhir.persistence.scout.SearchParameters.LatLngValue;
import com.ibm.fhir.search.date.DateTimeHandler;

/**
 * This class is the SCOUT persistence layer implementation for transforming
 * SearchParameters into Parameter Data Transfer Objects.
 * <p>
 * Call {@code Element.accept} with this visitor to add zero to many Parameters
 * to the result list and invoke {@code getResult}
 * to get the current list of extracted Parameter objects.
 * <p>
 * Note: this class DOES NOT set the resourceType on the underlying
 * Parameter objects it creates;
 * that is a responsibility of the caller.
 */
public class ParameterExtractionVisitor extends DefaultVisitor {
    private static final Logger log = Logger.getLogger(ParameterExtractionVisitor.class.getName());

    public static final String EXCEPTION_MSG = "Unexpected error while processing parameter [%s] with value [%s]";
    public static final String EXCEPTION_MSG_NAME_ONLY = "Unexpected error while processing parameter [%s]";

    // Datetime Limits from
    // DB2: https://www.ibm.com/support/knowledgecenter/en/SSEPGG_11.5.0/com.ibm.db2.luw.sql.ref.doc/doc/r0001029.html
    // Derby: https://db.apache.org/derby/docs/10.0/manuals/reference/sqlj271.html
    private static final Timestamp SMALLEST_TIMESTAMP = Timestamp.from(
            ZonedDateTime.parse("0001-01-01T00:00:00.000000Z").toInstant());
    // 23:59:59.999999 used instead of 24:00:00.000000 to ensure it could be represented in FHIR if needed
    private static final Timestamp LARGEST_TIMESTAMP = Timestamp.from(
            ZonedDateTime.parse("9999-12-31T23:59:59.999999Z").toInstant());

    // We only need the SearchParameter type and code, so just store those directly as members
    private final String searchParamCode;
    private final SearchParamType searchParamType;

    // The helper to support collection of all the parameters
    private final ParameterBlockBuilderHelper parameters;

    /**
     * Public constructor
     * @param parameters
     * @param searchParameter
     */
    public ParameterExtractionVisitor(ParameterBlockBuilderHelper parameters, SearchParameter searchParameter) {
        super(false);
        this.parameters = parameters;
        this.searchParamCode = searchParameter.getCode().getValue();
        this.searchParamType = searchParameter.getType();
    }


    @Override
    public boolean visit(String elementName, int elementIndex, Visitable visitable) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("The processing of %s is unsupported", visitable.getClass().getName()));
        }
        return false;
    }

    /*
     * ====================
     * Primitive Types *
     * ====================
     */

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Boolean _boolean) {
        if (_boolean.hasValue()) {
            if (!TOKEN.equals(searchParamType)) {
                throw invalidComboException(searchParamType, _boolean);
            }
            final String system = "http://terminology.hl7.org/CodeSystem/special-values";
            
            if (_boolean.getValue()) {
                parameters.addTokenParam(searchParamCode, "true", system);
            } else {
                parameters.addTokenParam(searchParamCode, "false", system);
            }
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Canonical canonical) {
        if (canonical.hasValue()) {
            if (!REFERENCE.equals(searchParamType) && !URI.equals(searchParamType)) {
                throw invalidComboException(searchParamType, canonical);
            }

            parameters.addStrParam(searchParamCode, canonical.getValue());
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Code code) {
        if (code.hasValue()) {
            if (!TOKEN.equals(searchParamType)) {
                throw invalidComboException(searchParamType, code);
            }
            parameters.addTokenParam(searchParamCode, code.getValue(), ModelSupport.getSystem(code));
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Date date) {
        if (date.hasValue()) {
            if (!DATE.equals(searchParamType)) {
                throw invalidComboException(searchParamType, date);
            }
            
            addDateParam(date);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.DateTime dateTime) {
        if (dateTime.hasValue()) {
            if (!DATE.equals(searchParamType)) {
                throw invalidComboException(searchParamType, dateTime);
            }
            
            addDateParam(dateTime);
        }
        return false;
    }
    
    /**
     * Convert the Java {@link BigDecimal} to the corresponding protobuf custom implementation
     * @param value
     * @return
     */
    private BigDec encode(BigDecimal value) {
        BigInteger biv = value.unscaledValue();
        BigInt.Builder intBuilder = BigInt.newBuilder();
        
        ByteString bs = ByteString.copyFrom(biv.toByteArray());
        intBuilder.setValue(bs);

        BigDec.Builder decBuilder = BigDec.newBuilder();
        decBuilder.setScale(value.scale());
        decBuilder.setValue(intBuilder.build());
        return decBuilder.build();
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Decimal decimal) {
        
        if (decimal.hasValue()) {
            if (!NUMBER.equals(searchParamType)) {
                throw invalidComboException(searchParamType, decimal);
            }
            
            parameters.addNumberParam(searchParamCode, decimal.getValue());
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Id id) {
        if (id.hasValue()) {
            if (!TOKEN.equals(searchParamType)) {
                throw invalidComboException(searchParamType, id);
            }
            
            parameters.addTokenParam(searchParamCode, id.getValue(), null);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Instant instant) {
        if (instant.hasValue()) {
            
            if (!DATE.equals(searchParamType)) {
                throw invalidComboException(searchParamType, instant);
            }
            
            // TODO decouple ourselves from java.sql
            Timestamp t = generateTimestamp(instant.getValue().toInstant());
            parameters.addDateParam(searchParamCode, t.getTime(), t.getTime());
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Integer integer) {
        if (integer.hasValue()) {
            if (!NUMBER.equals(searchParamType)) {
                throw invalidComboException(searchParamType, integer);
            }
            BigDecimal value = new BigDecimal(integer.getValue());
            parameters.addNumberParam(searchParamCode, value);
        }
        return false;
    }
    
    private void addStringParam(String value) {
        parameters.addStrParam(searchParamCode, value);

    }
    
    private void addTokenParam(String value) {
        parameters.addTokenParam(searchParamCode, value, null);
    }

    private void addTokenParam(String value, String codeSystem) {
        parameters.addTokenParam(searchParamCode, value, codeSystem);
    }

    @Override
    public boolean visit(String elementName, int elementIndex, com.ibm.fhir.model.type.String value) {
        if (value.hasValue()) {
            if (STRING.equals(searchParamType)) {
                addStringParam(value.getValue());
            } else if (TOKEN.equals(searchParamType)) {
                addTokenParam(value.getValue());
            } else {
                throw invalidComboException(searchParamType, value);
            }
        }
        return false;
    }

    @Override
    public boolean visit(String elementName, int elementIndex, Uri uri) {
        if (uri.hasValue()) {
            if (!URI.equals(searchParamType) && !REFERENCE.equals(searchParamType)) {
                throw invalidComboException(searchParamType, uri);
            }
            addStringParam(uri.getValue());
        }
        return false;
    }

    /*
     * ====================
     * Data Types *
     * ====================
     */

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Address address) {
        if (!STRING.equals(searchParamType)) {
            throw invalidComboException(searchParamType, address);
        }
        for (com.ibm.fhir.model.type.String aLine : address.getLine()) {
            addStringParam(aLine.getValue());
        }
        if (address.getCity() != null) {
            addStringParam(address.getCity().getValue());
        }
        if (address.getDistrict() != null) {
            addStringParam(address.getDistrict().getValue());
        }
        if (address.getState() != null) {
            addStringParam(address.getState().getValue());
        }
        if (address.getCountry() != null) {
            addStringParam(address.getCountry().getValue());
        }
        if (address.getPostalCode() != null) {
            addStringParam(address.getPostalCode().getValue());
        }
        if (address.getText() != null) {
            addStringParam(address.getText().getValue());
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
        if (coding.getCode() != null && coding.getCode().hasValue()) {
            if (!TOKEN.equals(searchParamType)) {
                throw invalidComboException(searchParamType, coding);
            }
            if (coding.getSystem() == null) {
                addTokenParam(coding.getCode().getValue(), coding.getSystem().getValue());
            } else {
                addTokenParam(coding.getCode().getValue());
            }
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ContactPoint contactPoint) {
        if (contactPoint.getValue() != null) {
            if (!TOKEN.equals(searchParamType)) {
                throw invalidComboException(searchParamType, contactPoint);
            }
            addTokenParam(contactPoint.getValue().getValue());
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HumanName humanName) {
        if (!STRING.equals(searchParamType)) {
            throw invalidComboException(searchParamType, humanName);
        }
        if (humanName.getFamily() != null) {
            // family is just a string in R4 (not a list)
            addStringParam(humanName.getFamily().getValue());
        }
        for (com.ibm.fhir.model.type.String given : humanName.getGiven()) {
            addStringParam(given.getValue());
        }
        for (com.ibm.fhir.model.type.String prefix : humanName.getPrefix()) {
            addStringParam(prefix.getValue());
        }
        for (com.ibm.fhir.model.type.String suffix : humanName.getSuffix()) {
            addStringParam(suffix.getValue());
        }
        if (humanName.getText() != null) {
            addStringParam(humanName.getText().getValue());
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Identifier identifier) {
        if (!TOKEN.equals(searchParamType)) {
            throw invalidComboException(searchParamType, identifier);
        }
        if (identifier != null && identifier.getValue() != null) {
            if (identifier.getSystem() == null) {
                addTokenParam(identifier.getValue().getValue());
            } else {
                addTokenParam(identifier.getValue().getValue(), identifier.getSystem().getValue());
            }
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Money money) {
        if (!QUANTITY.equals(searchParamType)) {
            throw invalidComboException(searchParamType, money);
        }
        if (money != null && money.getValue() != null && money.getValue().getValue() != null) {
            String code = null;
            if (money.getCurrency() != null) {
                code = money.getCurrency().getValue();
            }
            
            parameters.addQuantityParam(searchParamCode, money.getValue().getValue(), null, null, code, null);
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
        
        long start;
        if (period.getStart() == null || period.getStart().getValue() == null) {
            start = SMALLEST_TIMESTAMP.getTime();
        } else {
            java.time.Instant startInst = DateTimeHandler.generateValue(period.getStart().getValue());
            start = generateTimestamp(startInst).getTime();
        }
        
        long end;
        if (period.getEnd() == null || period.getEnd().getValue() == null) {
            end = LARGEST_TIMESTAMP.getTime();
        } else {
            java.time.Instant endInst = DateTimeHandler.generateValue(period.getEnd().getValue());
            end = generateTimestamp(endInst).getTime();
        }

        parameters.addDateParam(searchParamCode, start, end);
        
        return false;
    }

    // Also handles Quantity subtypes like Age and Duration
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Quantity quantity) {
        if (!QUANTITY.equals(searchParamType)) {
            throw invalidComboException(searchParamType, quantity);
        }
        
        if (quantity.getValue() != null && quantity.getValue().hasValue()) {
            BigDecimal value = quantity.getValue().getValue();
            BigDecimal valueLow = BigDecimalUtil.generateLowerBound(value);
            BigDecimal valueHigh = BigDecimalUtil.generateUpperBound(value);

            // see https://gforge.hl7.org/gf/project/fhir/tracker/?action=TrackerItemEdit&tracker_item_id=19597
            if (quantity.getCode() != null && quantity.getCode().hasValue()) {
                
                String system = null;
                if (quantity.getSystem() != null) {
                    system = quantity.getSystem().getValue();
                }

                parameters.addQuantityParam(searchParamCode, value, valueLow, valueHigh, quantity.getCode().getValue(), system);
            }
            
            if (quantity.getUnit() != null && quantity.getUnit().hasValue()) {
                String displayUnit = quantity.getUnit().getValue();
                // No need to save a second parameter value if the display unit matches the coded unit
                if (quantity.getCode() == null || !displayUnit.equals(quantity.getCode().getValue())) {
                    parameters.addQuantityParam(searchParamCode, value, valueLow, valueHigh, displayUnit, null);
                }
            }
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Range range) {
        if (!QUANTITY.equals(searchParamType)) {
            throw invalidComboException(searchParamType, range);
        }
        // The parameter isn't added unless either low or high holds a value
        String system = null;
        String code = null;
        BigDecimal low = null;
        BigDecimal high = null;
        if (range.getLow() != null && range.getLow().getValue() != null
                && range.getLow().getValue().getValue() != null) {
            
            if (range.getLow().getSystem() != null) {
                system = range.getLow().getSystem().getValue();
            }
            
            if (range.getLow().getCode() != null) {
                code = range.getLow().getCode().getValue();
            } else if (range.getLow().getUnit() != null) {
                code = range.getLow().getUnit().getValue();
            }
            
            low = range.getLow().getValue().getValue();

            // The unit and code/system elements of the low or high elements SHALL match
            if (range.getHigh() != null && range.getHigh().getValue() != null
                    && range.getHigh().getValue().getValue() != null) {
                high = range.getHigh().getValue().getValue();
            }
        } else if (range.getHigh() != null && range.getHigh().getValue() != null
                && range.getHigh().getValue().getValue() != null) {
            
            // Low is null, so just get the high value
            if (range.getHigh().getSystem() != null) {
                system = range.getHigh().getSystem().getValue();
            }
            
            if (range.getHigh().getCode() != null) {
                code = range.getHigh().getCode().getValue();
            } else if (range.getHigh().getUnit() != null) {
                code = range.getHigh().getUnit().getValue();
            }
            high = range.getHigh().getValue().getValue();
        }
        
        if (code != null) {
            // valid parameter
            parameters.addQuantityParam(searchParamCode, null, low, high, code, system);
        }
        
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Reference reference) {
        if (!REFERENCE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, reference);
        }
        
        if (reference.getReference() != null) {
            parameters.addStrParam(searchParamCode, reference.getReference().getValue());
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Timing timing) {
        if (!DATE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, timing);
        }
        /*
         * The specified scheduling details are ignored and only the outer limits
         * matter. For instance, a schedule that
         * specifies every second day between 31-Jan 2013 and 24-Mar 2013 includes 1-Feb
         * 2013, even though that is on an
         * odd day that is not specified by the period. This is to keep the server load
         * processing queries reasonable.
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

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Location.Position position) {
        LatLngValue.Builder p = LatLngValue.newBuilder();
        

        // The following code ensures that the lat/lon is only added when there is a pair.
        boolean add = false;
        if (position.getLatitude() != null && position.getLatitude().getValue() != null) {
            Double lat = position.getLatitude().getValue().doubleValue();

            if (position.getLongitude() != null && position.getLongitude().getValue() != null) {
                Double lng = position.getLongitude().getValue().doubleValue();
                parameters.addLatLngParam(searchParamCode, lat, lng);
                add = true;
            }
        }

        if (!add && log.isLoggable(Level.FINE)) {
            log.fine("The Location.Position parameter is invalid, and not added '" + searchParamCode + "'");
        }

        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Extension extension) {
        if (extension.getValue() != null) {
            extension.getValue().accept(this);
            return false;
        } else {
            return true;
        }
    }

    /*
     * ====================
     * Date/Time helpers *
     * ====================
     */

    /**
     * Configure the date values in the parameter based on the model {@link Date}
     * which again might be partial
     * (Year/YearMonth/LocalDate)
     *
     * @param p
     * @param date
     */
    private void addDateParam(Date date) {
        if (date.getValue() != null) {
            java.time.Instant start = DateTimeHandler.generateValue(date.getValue());
            java.time.Instant end = DateTimeHandler.generateUpperBound(date);
            
            parameters.addDateParam(searchParamCode, DateTimeHandler.generateTimestamp(start).getTime(), DateTimeHandler.generateTimestamp(end).getTime());
        }
    }

    /**
     * Configure the date values in the parameter based on the model
     * {@link DateTime} and the type of date it
     * represents.
     *
     * @param p
     * @param dateTime
     * @throws NullPointerException if p or dateTime are null
     */
    private void addDateParam(DateTime dateTime) {
        if (dateTime.getValue() != null) {
            java.time.Instant start = DateTimeHandler.generateValue(dateTime.getValue());
            java.time.Instant end = DateTimeHandler.generateUpperBound(dateTime);
            parameters.addDateParam(searchParamCode, DateTimeHandler.generateTimestamp(start).getTime(), DateTimeHandler.generateTimestamp(end).getTime());
        }
    }

    private IllegalArgumentException invalidComboException(SearchParamType paramType, Element value) {
        return new IllegalArgumentException("Data type '" + value.getClass().getSimpleName() + "' is not supported "
                + "for SearchParameter '" + searchParamCode + "' of type '" + paramType.getValue() + "'");
    }
}
