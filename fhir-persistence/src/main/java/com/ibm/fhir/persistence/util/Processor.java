/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Contributor;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.ParameterDefinition;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.SampledData;
import com.ibm.fhir.model.type.Signature;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.TriggerDefinition;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.path.FHIRPathAbstractNode;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathDateTimeValue;
import com.ibm.fhir.path.FHIRPathDecimalValue;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathIntegerValue;
import com.ibm.fhir.path.FHIRPathQuantityValue;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.FHIRPathTimeValue;
import com.ibm.fhir.persistence.exception.FHIRPersistenceProcessorException;

@Deprecated
public interface Processor<T> {
    T process(SearchParameter parameter, Object value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, java.lang.String value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, com.ibm.fhir.model.type.String value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Address value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Annotation value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Attachment value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, BackboneElement value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Base64Binary value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, java.lang.Boolean value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, com.ibm.fhir.model.type.Boolean value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Canonical value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Code value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, CodeableConcept value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Coding value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, ContactDetail value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, ContactPoint value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Contributor value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Date value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, DateTime value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, DataRequirement value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Decimal value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Expression value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, HumanName value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Id value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Identifier value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Instant value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, java.lang.Integer value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, com.ibm.fhir.model.type.Integer value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Markdown value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Meta value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Money value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Narrative value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Oid value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, ParameterDefinition value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Period value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, PositiveInt value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Quantity value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Range value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Ratio value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Reference value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, RelatedArtifact value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, SampledData value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Signature value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Time value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Timing value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, TriggerDefinition value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, UnsignedInt value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Uri value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Url value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, UsageContext value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Uuid value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Location.Position value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathAbstractNode value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathElementNode value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathDateTimeValue value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathStringValue value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathTimeValue value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathResourceNode value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathIntegerValue value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathDecimalValue value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathBooleanValue value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, FHIRPathQuantityValue value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, ZonedDateTime value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, LocalDate value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, YearMonth value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Year value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, BigDecimal value) throws FHIRPersistenceProcessorException;
}
