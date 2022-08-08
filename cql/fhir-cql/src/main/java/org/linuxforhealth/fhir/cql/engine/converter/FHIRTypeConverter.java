/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.cql.engine.converter;

import java.math.BigDecimal;

import org.opencds.cqf.cql.engine.runtime.BaseTemporal;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.runtime.Concept;
import org.opencds.cqf.cql.engine.runtime.Date;
import org.opencds.cqf.cql.engine.runtime.DateTime;
import org.opencds.cqf.cql.engine.runtime.Interval;
import org.opencds.cqf.cql.engine.runtime.Quantity;
import org.opencds.cqf.cql.engine.runtime.Ratio;
import org.opencds.cqf.cql.engine.runtime.Time;
import org.opencds.cqf.cql.engine.runtime.Tuple;

import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Instant;

/**
 * Define an interface for converting IBM FHIR Server types to CQL types and vice versa.
 */
public interface FHIRTypeConverter {

    // CQL-to-FHIR conversions

    /**
     * Tests if an Object is a FHIR structure
     * 
     * @param value
     *            the value to test
     * @return true if value is a FHIR structure, false otherwise
     * @throws NullPointerException
     *             if value is null
     */
    public Boolean isFhirType(Object value);

    /**
     * Converts an Object to a FHIR structure.
     * 
     * @param value
     *            the value to convert
     * @return a FHIR structure
     * @throws IllegalArgumentException
     *             is value is an Iterable
     */
    public Object toFhirType(Object value);

    /**
     * Converts an iterable of Objects to FHIR structures. Preserves ordering,
     * nulls, and sublist hierarchy
     * 
     * @param values
     *            an Iterable containing CQL structures, nulls, or sublists
     * @return an Iterable containing FHIR types, nulls, and sublists
     */
    public Iterable<Object> toFhirTypes(Iterable<?> values);

    /**
     * Converts a String to a FHIR Id
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Id
     */
    public Id toFhirId(String value);

    /**
     * Converts a Boolean to a FHIR Boolean
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Boolean
     */
    public org.linuxforhealth.fhir.model.type.Boolean toFhirBoolean(Boolean value);

    /**
     * Converts an Integer to a FHIR Integer
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Integer
     */
    public org.linuxforhealth.fhir.model.type.Integer toFhirInteger(Integer value);

    /**
     * Converts a BigDecimal to a FHIR Decimal
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Decimal
     */
    public org.linuxforhealth.fhir.model.type.Decimal toFhirDecimal(BigDecimal value);

    /**
     * Converts a CQL Date to a FHIR Date
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Date
     */
    public org.linuxforhealth.fhir.model.type.Date toFhirDate(Date value);

    /**
     * Converts a CQL DateTime to a FHIR DateTime
     * 
     * @param value
     *            the value to convert
     * @return a FHIR DateTime
     */
    public org.linuxforhealth.fhir.model.type.DateTime toFhirDateTime(DateTime value);

    /**
     * Converts a CQL Time to a FHIR Time
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Time
     */
    public org.linuxforhealth.fhir.model.type.Time toFhirTime(Time value);

    /**
     * Converts a String to a FHIR String
     * 
     * @param value
     *            the value to convert
     * @return a FHIR String
     */
    public org.linuxforhealth.fhir.model.type.String toFhirString(String value);

    /**
     * Converts a CQL Quantity to a FHIR Quantity
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Quantity
     */
    public org.linuxforhealth.fhir.model.type.Quantity toFhirQuantity(Quantity value);

    /**
     * Converts a CQL Ratio to a FHIR Ratio
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Ratio
     */
    public org.linuxforhealth.fhir.model.type.Ratio toFhirRatio(Ratio value);

    /**
     * Converts a CQL Any to a FHIR Any
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Any
     */
    // public org.linuxforhealth.fhir.model.type. toFhirAny(Object value);

    /**
     * Converts a CQL Code to a FHIR Coding
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Coding
     */
    public Coding toFhirCoding(Code value);

    /**
     * Converts a CQL Concept to a FHIR CodeableConcept
     * 
     * @param value
     *            the value to convert
     * @return a FHIR CodeableConcept
     */
    public org.linuxforhealth.fhir.model.type.CodeableConcept toFhirCodeableConcept(Concept value);

    /**
     * Converts a CQL Interval to a FHIR Period
     * 
     * @param value
     *            a Date or DateTime Interval
     * @return a FHIR Period
     */
    public org.linuxforhealth.fhir.model.type.Period toFhirPeriod(Interval value);

    /**
     * Converts a CQL Interval to a FHIR Range
     * 
     * @param value
     *            a Quantity Interval
     * @return a FHIR Range
     */
    public org.linuxforhealth.fhir.model.type.Range toFhirRange(Interval value);

    /**
     * Converts a CQL Interval to FHIR Range or Period
     * 
     * @param value
     *            a Quantity, Date, or DateTime interval
     * @return A FHIR Range or Period
     */
    public org.linuxforhealth.fhir.model.type.Element toFhirInterval(Interval value);

    /**
     * Converts a CQL Tuple to a FHIR Structure
     * 
     * @param value
     *            the value to convert
     * @return a FHIR Structure
     */
    public Object toFhirTuple(Tuple value);


    // FHIR-to-CQL conversions

    /**
     * Tests if an Object is a CQL type
     * 
     * @param value
     *            the value to convert
     * @return true if value is a CQL type, false otherwise
     * @throws NullPointerException
     *             if value is null
     */
    public Boolean isCqlType(Object value);

    /**
     * Converts an Object to a CQL type.
     * 
     * @param value
     *            the value to convert a FHIR structure
     * @return a CQL type
     * @throws IllegalArgumentException
     *             is value is an Iterable
     */
    public Object toCqlType(Object value);

    /**
     * Converts an iterable of Objects to CQL types. Preserves ordering, nulls, and
     * sublist hierarchy
     * 
     * @param values
     *            the values to convert an Iterable containing FHIR structures,
     *            nulls, or sublists
     * @return an Iterable containing CQL types, nulls, and sublists
     */
    public Iterable<Object> toCqlTypes(Iterable<?> values);

    /**
     * Converts a FHIR Id to a CQL String
     * 
     * @param value
     *            the value to convert
     * @return a String
     */
    public String toCqlId(Id value);

    /**
     * Converts a FHIR Boolean to a CQL Boolean
     * 
     * @param value
     *            the value to convert
     * @return a Boolean
     */
    public Boolean toCqlBoolean(org.linuxforhealth.fhir.model.type.Boolean value);

    /**
     * Converts a FHIR Integer to a CQL Integer
     * 
     * @param value
     *            the value to convert
     * @return an Integer
     */
    public Integer toCqlInteger(org.linuxforhealth.fhir.model.type.Integer value);

    /**
     * Converts a FHIR Decimal to a CQL Decimal
     * 
     * @param value
     *            the value to convert
     * @return a BigDecimal
     */
    public BigDecimal toCqlDecimal(org.linuxforhealth.fhir.model.type.Decimal value);

    /**
     * Converts a FHIR Date to a CQL Date
     * 
     * @param value
     *            the value to convert
     * @return a CQL Date
     * @throws IllegalArgumentException
     *             if value is not a Date
     */
    public Date toCqlDate(org.linuxforhealth.fhir.model.type.Date value);

    /**
     * Converts a FHIR DateTime to a CQL DateTime
     * 
     * @param value
     *            the value to convert
     * @return a CQL DateTime
     * @throws IllegalArgumentException
     *             if value is not a DateTime
     */
    public DateTime toCqlDateTime(org.linuxforhealth.fhir.model.type.DateTime value);

    /**
     * Converts a FHIR Instant to a CQL DateTime
     * 
     * @param value
     *            the value to convert
     * @return a CQL DateTime
     * @throws IllegalArgumentException
     *             if value is not a DateTime
     */
    public DateTime toCqlDateTime(Instant value);

    /**
     * Converts a FHIR DateTime, Date, or Instance to a CQL BaseTemporal
     * 
     * @param value
     *            the value to convert
     * @return a CQL BaseTemporal
     * @throws IllegalArgumentException
     *             if value is not a DateTime, Date, or Instant
     */
    public BaseTemporal toCqlTemporal(org.linuxforhealth.fhir.model.type.Element value);

    /**
     * Converts a FHIR Time to a CQL Time
     * 
     * @param value
     *            the value to convert
     * @return a CQL Time
     * @throws IllegalArgumentException
     *             if value is not a Time
     */
    public Time toCqlTime(org.linuxforhealth.fhir.model.type.Time value);

    /**
     * Converts a FHIR String to a CQL String
     * 
     * @param value
     *            the value to convert
     * @return a String
     */
    public String toCqlString(org.linuxforhealth.fhir.model.type.String value);

    /**
     * Converts a FHIR Quantity to a CQL Quantity
     * 
     * @param value
     *            the value to convert
     * @return a CQL Quantity
     * @throws IllegalArgumentException
     *             if value is not a Quantity
     */
    public Quantity toCqlQuantity(org.linuxforhealth.fhir.model.type.Quantity value);

    /**
     * Converts a FHIR Ratio to a CQL Ratio
     * 
     * @param value
     *            the value to convert
     * @return a CQL Ratio
     * @throws IllegalArgumentException
     *             if value is not a Ratio
     */
    public Ratio toCqlRatio(org.linuxforhealth.fhir.model.type.Ratio value);

    /**
     * Converts a FHIR Any to a CQL Any
     * 
     * @param value
     *            the value to convert
     * @return a CQL Any
     */
    // public Object toCqlAny(IBase value);

    /**
     * Converts a FHIR Coding to a CQL Code
     * 
     * @param value
     *            the value to convert
     * @return a CQL Code
     */
    public Code toCqlCode(org.linuxforhealth.fhir.model.type.Coding value);

    /**
     * Converts a FHIR CodeableConcept to a CQL Concept
     * 
     * @param value
     *            the value to convert
     * @return a CQL Concept
     * @throws IllegalArgumentException
     *             if value is not a CodeableConcept
     */
    public Concept toCqlConcept(org.linuxforhealth.fhir.model.type.CodeableConcept value);

    /**
     * Converts a FHIR Range or Period to a CQL Interval
     * 
     * @param value
     *            the value to convert
     * @return a CQL Interval
     * @throws IllegalArgumentException
     *             if value is not a Range or Period
     */
    public Interval toCqlInterval(org.linuxforhealth.fhir.model.type.Element value);


    /**
     * Converts a FHIR Structure to a CQL Tuple
@Override
    
     * 
     * @param value
     *            the value to convert
     * @return a CQL Tuple
     */
    // public Tuple toCqlTuple(IBase value);
}
