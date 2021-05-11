/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.model.type.code.SearchParamType.DATE;
import static com.ibm.fhir.model.type.code.SearchParamType.NUMBER;
import static com.ibm.fhir.model.type.code.SearchParamType.QUANTITY;
import static com.ibm.fhir.model.type.code.SearchParamType.REFERENCE;
import static com.ibm.fhir.model.type.code.SearchParamType.STRING;
import static com.ibm.fhir.model.type.code.SearchParamType.TOKEN;
import static com.ibm.fhir.model.type.code.SearchParamType.URI;
import static com.ibm.fhir.search.date.DateTimeHandler.generateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.ibm.fhir.persistence.jdbc.dto.CompositeParmVal;
import com.ibm.fhir.persistence.jdbc.dto.DateParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.LocationParmVal;
import com.ibm.fhir.persistence.jdbc.dto.NumberParmVal;
import com.ibm.fhir.persistence.jdbc.dto.QuantityParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ReferenceParmVal;
import com.ibm.fhir.persistence.jdbc.dto.StringParmVal;
import com.ibm.fhir.persistence.jdbc.dto.TokenParmVal;
import com.ibm.fhir.persistence.jdbc.util.type.NumberParmBehaviorUtil;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.date.DateTimeHandler;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.util.ReferenceUtil;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.term.util.CodeSystemSupport;

/**
 * This class is the JDBC persistence layer implementation for transforming
 * SearchParameters into Parameter Data Transfer Objects.
 * <p>
 * Call {@code Element.accept} with this visitor to add zero to many Parameters
 * to the result list and invoke {@code getResult}
 * to get the current list of extracted Parameter objects.
 * <p>
 * Note: this class DOES NOT set the resourceType on the underlying JDBC
 * Parameter objects it creates;
 * that is a responsibility of the caller.
 */
public class JDBCParameterBuildingVisitor extends DefaultVisitor {
    private static final Logger log = Logger.getLogger(JDBCParameterBuildingVisitor.class.getName());

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

    private final String resourceType;
    // We only need the SearchParameter type and code, so just store those directly as members
    private final String searchParamCode;
    private final SearchParamType searchParamType;

    /**
     * The result of the visit(s)
     */
    private List<ExtractedParameterValue> result;

    public JDBCParameterBuildingVisitor(String resourceType, SearchParameter searchParameter) {
        super(false);
        this.resourceType = resourceType;
        this.searchParamCode = searchParameter.getCode().getValue();
        this.searchParamType = searchParameter.getType();

        result               = new ArrayList<>();
    }

    /**
     * @return the Parameters extracted from the visited Elements
     */
    public List<ExtractedParameterValue> getResult() {
        return Collections.unmodifiableList(result);
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
            TokenParmVal p = new TokenParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueSystem("http://terminology.hl7.org/CodeSystem/special-values");
            if (_boolean.getValue()) {
                p.setValueCode("true");
            } else {
                p.setValueCode("false");
            }
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Canonical canonical) {
        if (canonical.hasValue()) {
            if (!REFERENCE.equals(searchParamType) && !URI.equals(searchParamType)) {
                throw invalidComboException(searchParamType, canonical);
            }
            StringParmVal p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(canonical.getValue());
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Code code) {
        if (code.hasValue()) {
            if (!TOKEN.equals(searchParamType)) {
                throw invalidComboException(searchParamType, code);
            }
            TokenParmVal p = new TokenParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            String system = ModelSupport.getSystem(code);
            setTokenValues(p, system != null ? Uri.of(system) : null, code.getValue());
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Date date) {
        if (date.hasValue()) {
            if (!DATE.equals(searchParamType)) {
                throw invalidComboException(searchParamType, date);
            }
            DateParmVal p = new DateParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            setDateValues(p, date);
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.DateTime dateTime) {
        if (dateTime.hasValue()) {
            if (!DATE.equals(searchParamType)) {
                throw invalidComboException(searchParamType, dateTime);
            }
            DateParmVal p = new DateParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            setDateValues(p, dateTime);
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Decimal decimal) {
        if (decimal.hasValue()) {
            if (!NUMBER.equals(searchParamType)) {
                throw invalidComboException(searchParamType, decimal);
            }
            NumberParmVal p = new NumberParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            BigDecimal value = decimal.getValue();
            p.setValueNumber(value);
            p.setValueNumberLow(NumberParmBehaviorUtil.generateLowerBound(value));
            p.setValueNumberHigh(NumberParmBehaviorUtil.generateUpperBound(value));
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Id id) {
        if (id.hasValue()) {
            if (!TOKEN.equals(searchParamType)) {
                throw invalidComboException(searchParamType, id);
            }
            TokenParmVal p = new TokenParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueCode(id.getValue());
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Instant instant) {
        if (instant.hasValue()) {
            if (!DATE.equals(searchParamType)) {
                throw invalidComboException(searchParamType, instant);
            }
            DateParmVal p = new DateParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            Timestamp t = generateTimestamp(instant.getValue().toInstant());
            p.setValueDateStart(t);
            p.setValueDateEnd(t);
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, com.ibm.fhir.model.type.Integer integer) {
        if (integer.hasValue()) {
            if (!NUMBER.equals(searchParamType)) {
                throw invalidComboException(searchParamType, integer);
            }
            NumberParmVal p = new NumberParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            BigDecimal value = new BigDecimal(integer.getValue());
            p.setValueNumber(value);
            p.setValueNumberLow(value);
            p.setValueNumberHigh(value);
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(String elementName, int elementIndex, com.ibm.fhir.model.type.String value) {
        if (value.hasValue()) {
            if (STRING.equals(searchParamType)) {
                StringParmVal p = new StringParmVal();
                p.setResourceType(resourceType);
                p.setValueString(value.getValue());
                p.setName(searchParamCode);
                result.add(p);
            } else if (TOKEN.equals(searchParamType)) {
                TokenParmVal p = new TokenParmVal();
                p.setResourceType(resourceType);
                p.setValueCode(SearchUtil.normalizeForSearch(value.getValue()));
                p.setName(searchParamCode);
                result.add(p);
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

            // For REFERENCE search parameters, we need to treat Uris as tokens,
            // not strings.
            if (REFERENCE.equals(this.searchParamType)) {
                TokenParmVal p = new TokenParmVal();
                p.setResourceType(resourceType);
                p.setName(searchParamCode);
                p.setValueCode(uri.getValue());
                result.add(p);
            } else {
                StringParmVal p = new StringParmVal();
                p.setResourceType(resourceType);
                p.setName(searchParamCode);
                p.setValueString(uri.getValue());
                result.add(p);
            }
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
        StringParmVal p;
        if (!STRING.equals(searchParamType)) {
            throw invalidComboException(searchParamType, address);
        }
        for (com.ibm.fhir.model.type.String aLine : address.getLine()) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(aLine.getValue());
            result.add(p);
        }
        if (address.getCity() != null) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(address.getCity().getValue());
            result.add(p);
        }
        if (address.getDistrict() != null) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(address.getDistrict().getValue());
            result.add(p);
        }
        if (address.getState() != null) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(address.getState().getValue());
            result.add(p);
        }
        if (address.getCountry() != null) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(address.getCountry().getValue());
            result.add(p);
        }
        if (address.getPostalCode() != null) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(address.getPostalCode().getValue());
            result.add(p);
        }
        if (address.getText() != null) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
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
        if (codeableConcept.getText() != null && codeableConcept.getText().hasValue()) {
            // Extract as token as normalized string since :text modifier is simple string search
            TokenParmVal p = new TokenParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode + SearchConstants.TEXT_MODIFIER_SUFFIX);
            p.setValueCode(SearchUtil.normalizeForSearch(codeableConcept.getText().getValue()));
            result.add(p);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coding coding) {
        if (coding.getCode() != null && coding.getCode().hasValue()) {
            if (!TOKEN.equals(searchParamType)) {
                throw invalidComboException(searchParamType, coding);
            }
            TokenParmVal p = new TokenParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            setTokenValues(p, coding.getSystem(), coding.getCode().getValue());
            result.add(p);
            if (coding.getDisplay() != null && coding.getDisplay().hasValue()) {
                // Extract as token as normalized string since :text modifier is simple string search
                p = new TokenParmVal();
                p.setResourceType(resourceType);
                p.setName(searchParamCode + SearchConstants.TEXT_MODIFIER_SUFFIX);
                p.setValueCode(SearchUtil.normalizeForSearch(coding.getDisplay().getValue()));
                result.add(p);
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
            TokenParmVal telecom = new TokenParmVal();
            telecom.setResourceType(resourceType);
            telecom.setName(searchParamCode);
            telecom.setValueCode(contactPoint.getValue().getValue());
            result.add(telecom);
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HumanName humanName) {
        StringParmVal p;
        if (!STRING.equals(searchParamType)) {
            throw invalidComboException(searchParamType, humanName);
        }
        if (humanName.getFamily() != null) {
            // family is just a string in R4 (not a list)
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(humanName.getFamily().getValue());
            result.add(p);
        }
        for (com.ibm.fhir.model.type.String given : humanName.getGiven()) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(given.getValue());
            result.add(p);
        }
        for (com.ibm.fhir.model.type.String prefix : humanName.getPrefix()) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(prefix.getValue());
            result.add(p);
        }
        for (com.ibm.fhir.model.type.String suffix : humanName.getSuffix()) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(suffix.getValue());
            result.add(p);
        }
        if (humanName.getText() != null) {
            p = new StringParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            p.setValueString(humanName.getText().getValue());
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
            QuantityParmVal p = new QuantityParmVal();
            p.setResourceType(resourceType);
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
        DateParmVal p = new DateParmVal();
        p.setResourceType(resourceType);
        p.setName(searchParamCode);
        if (period.getStart() == null || period.getStart().getValue() == null) {
            p.setValueDateStart(SMALLEST_TIMESTAMP);
        } else {
            java.time.Instant startInst = DateTimeHandler.generateValue(period.getStart().getValue());
            p.setValueDateStart(generateTimestamp(startInst));
        }
        if (period.getEnd() == null || period.getEnd().getValue() == null) {
            p.setValueDateEnd(LARGEST_TIMESTAMP);

        } else {
            java.time.Instant endInst = DateTimeHandler.generateValue(period.getEnd().getValue());
            p.setValueDateEnd(generateTimestamp(endInst));
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
        if (quantity.getValue() != null && quantity.getValue().hasValue()) {
            BigDecimal value = quantity.getValue().getValue();
            BigDecimal valueLow = NumberParmBehaviorUtil.generateLowerBound(value);
            BigDecimal valueHigh = NumberParmBehaviorUtil.generateUpperBound(value);
            boolean addedCodeOrUnit = false;

            // see https://gforge.hl7.org/gf/project/fhir/tracker/?action=TrackerItemEdit&tracker_item_id=19597
            if (quantity.getCode() != null && quantity.getCode().hasValue()) {
                QuantityParmVal p = new QuantityParmVal();
                p.setResourceType(resourceType);
                p.setName(searchParamCode);
                p.setValueNumber(value);
                p.setValueNumberLow(valueLow);
                p.setValueNumberHigh(valueHigh);
                p.setValueCode(quantity.getCode().getValue());
                if (quantity.getSystem() != null) {
                    p.setValueSystem(quantity.getSystem().getValue());
                }
                result.add(p);
                addedCodeOrUnit = true;
            }
            if (quantity.getUnit() != null && quantity.getUnit().hasValue()) {
                String displayUnit = quantity.getUnit().getValue();
                // No need to save a second parameter value if the display unit matches the coded unit
                if (quantity.getCode() == null || !displayUnit.equals(quantity.getCode().getValue())) {
                    QuantityParmVal p = new QuantityParmVal();
                    p.setResourceType(resourceType);
                    p.setName(searchParamCode);
                    p.setValueNumber(value);
                    p.setValueNumberLow(valueLow);
                    p.setValueNumberHigh(valueHigh);
                    p.setValueCode(displayUnit);
                    result.add(p);
                    addedCodeOrUnit = true;
                }
            }
            // If neither code nor unit was specified, add a QuantityParmVal just with the value
            if (!addedCodeOrUnit) {
                QuantityParmVal p = new QuantityParmVal();
                p.setResourceType(resourceType);
                p.setName(searchParamCode);
                p.setValueNumber(value);
                p.setValueNumberLow(valueLow);
                p.setValueNumberHigh(valueHigh);
                result.add(p);
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
        QuantityParmVal p = new QuantityParmVal();
        p.setResourceType(resourceType);
        p.setName(searchParamCode);
        if (range.getLow() != null && range.getLow().getValue() != null
                && range.getLow().getValue().getValue() != null) {
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
            if (range.getHigh() != null && range.getHigh().getValue() != null
                    && range.getHigh().getValue().getValue() != null) {
                p.setValueNumberHigh(range.getHigh().getValue().getValue());
            }

            result.add(p);
        } else if (range.getHigh() != null && range.getHigh().getValue() != null
                && range.getHigh().getValue().getValue() != null) {
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
    public boolean visit(java.lang.String elementName, int elementIndex, Identifier identifier) {
        if (!TOKEN.equals(searchParamType)) {
            throw invalidComboException(searchParamType, identifier);
        }
        if (identifier != null && identifier.getValue() != null) {
            TokenParmVal p = new TokenParmVal();
            p.setResourceType(resourceType);
            p.setName(searchParamCode);
            setTokenValues(p, identifier.getSystem(), identifier.getValue().getValue());
            result.add(p);
            if (identifier.getType() != null) {
                for (Coding typeCoding : identifier.getType().getCoding()) {
                    if (typeCoding.getCode() != null && typeCoding.getCode().hasValue()) {
                        // Create as composite parm since type/value are correlated for the :of-type modifier
                        CompositeParmVal cp = new CompositeParmVal();
                        cp.setResourceType(resourceType);
                        cp.setName(searchParamCode + SearchConstants.OF_TYPE_MODIFIER_SUFFIX);

                        // type
                        p = new TokenParmVal();
                        p.setResourceType(cp.getResourceType());
                        p.setName(SearchUtil.makeCompositeSubCode(cp.getName(), SearchConstants.OF_TYPE_MODIFIER_COMPONENT_TYPE));
                        setTokenValues(p, typeCoding.getSystem(), typeCoding.getCode().getValue());
                        cp.addComponent(p);

                        // value
                        p = new TokenParmVal();
                        p.setResourceType(cp.getResourceType());
                        p.setName(SearchUtil.makeCompositeSubCode(cp.getName(), SearchConstants.OF_TYPE_MODIFIER_COMPONENT_VALUE));
                        p.setValueCode(identifier.getValue().getValue());
                        cp.addComponent(p);

                        result.add(cp);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Reference reference) {
        if (!REFERENCE.equals(searchParamType)) {
            throw invalidComboException(searchParamType, reference);
        }

        // TODO pass in the bundle if we want to support "a relative URL, which is relative to
        // the Service Base URL, or, if processing a resource from a bundle, which is relative
        // to the base URL implied by the Bundle.entry.fullUrl (see Resolving References in Bundles)"
        try {
            final String baseUrl = ReferenceUtil.getBaseUrl(null);
            ReferenceValue refValue = ReferenceUtil.createReferenceValueFrom(reference, baseUrl);
            if (refValue.getType() != ReferenceType.LOGICAL && refValue.getType() != ReferenceType.INVALID && refValue.getType() != ReferenceType.DISPLAY_ONLY) {
                ReferenceParmVal p = new ReferenceParmVal();
                p.setResourceType(resourceType);
                p.setRefValue(refValue);
                p.setName(searchParamCode);
                result.add(p);
            }
            Identifier identifier = reference.getIdentifier();
            if (identifier != null && identifier.getValue() != null) {
                TokenParmVal p = new TokenParmVal();
                p.setResourceType(resourceType);
                p.setName(searchParamCode + SearchConstants.IDENTIFIER_MODIFIER_SUFFIX);
                setTokenValues(p, identifier.getSystem(), identifier.getValue().getValue());
                result.add(p);
            }
        } catch (FHIRSearchException x) {
            // Log the error, but skip it because we're not supposed to throw exceptions here
            log.log(Level.WARNING, "Error processing reference", x);
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
        LocationParmVal p = new LocationParmVal();
        p.setResourceType(resourceType);
        p.setName(searchParamCode);

        // The following code ensures that the lat/lon is only added when there is a pair.
        boolean add = false;
        if (position.getLatitude() != null && position.getLatitude().getValue() != null) {
            Double lat = position.getLatitude().getValue().doubleValue();
            p.setValueLatitude(lat);

            if (position.getLongitude() != null && position.getLongitude().getValue() != null) {
                Double lon = position.getLongitude().getValue().doubleValue();
                p.setValueLongitude(lon);
                result.add(p);
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
    private void setDateValues(DateParmVal p, Date date) {
        if (date.getValue() != null) {
            java.time.Instant inst = DateTimeHandler.generateValue(date.getValue());
            p.setValueDateStart(DateTimeHandler.generateTimestamp(inst));
            inst = DateTimeHandler.generateUpperBound(date);
            p.setValueDateEnd(DateTimeHandler.generateTimestamp(inst));
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
    private void setDateValues(DateParmVal p, DateTime dateTime) {
        if (dateTime.getValue() != null) {
            java.time.Instant inst = DateTimeHandler.generateValue(dateTime.getValue());
            p.setValueDateStart(DateTimeHandler.generateTimestamp(inst));
            inst = DateTimeHandler.generateUpperBound(dateTime);
            p.setValueDateEnd(DateTimeHandler.generateTimestamp(inst));
        }
    }

    /*
     * ====================
     * System/Code helper *
     * ====================
     */

    /**
     * Configure the system and code values in the parameter. If the system
     * is non-null, determine if it's case-sensitive. If it's non-null and
     * not case-sensitive, normalize the value.
     *
     * @param p
     * @param system
     * @param code
     */
    private void setTokenValues(TokenParmVal p, Uri system, String code) {
        boolean caseSensitive = false;
        if (system != null && system.hasValue()) {
            caseSensitive = CodeSystemSupport.isCaseSensitive(system.getValue());
            p.setValueSystem(system.getValue());
        }
        p.setValueCode(caseSensitive ? code : SearchUtil.normalizeForSearch(code));
    }

    private IllegalArgumentException invalidComboException(SearchParamType paramType, Element value) {
        return new IllegalArgumentException("Data type '" + value.getClass().getSimpleName() + "' is not supported "
                + "for SearchParameter '" + searchParamCode + "' of type '" + paramType.getValue() + "'");
    }
}
