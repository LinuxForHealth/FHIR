/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import org.linuxforhealth.fhir.model.resource.Location;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.type.Address;
import org.linuxforhealth.fhir.model.type.Annotation;
import org.linuxforhealth.fhir.model.type.Attachment;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Base64Binary;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.ContactDetail;
import org.linuxforhealth.fhir.model.type.ContactPoint;
import org.linuxforhealth.fhir.model.type.Contributor;
import org.linuxforhealth.fhir.model.type.DataRequirement;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Decimal;
import org.linuxforhealth.fhir.model.type.Expression;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Markdown;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Money;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Oid;
import org.linuxforhealth.fhir.model.type.ParameterDefinition;
import org.linuxforhealth.fhir.model.type.Period;
import org.linuxforhealth.fhir.model.type.PositiveInt;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Range;
import org.linuxforhealth.fhir.model.type.Ratio;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.RelatedArtifact;
import org.linuxforhealth.fhir.model.type.SampledData;
import org.linuxforhealth.fhir.model.type.Signature;
import org.linuxforhealth.fhir.model.type.Time;
import org.linuxforhealth.fhir.model.type.Timing;
import org.linuxforhealth.fhir.model.type.TriggerDefinition;
import org.linuxforhealth.fhir.model.type.UnsignedInt;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.Url;
import org.linuxforhealth.fhir.model.type.UsageContext;
import org.linuxforhealth.fhir.model.type.Uuid;
import org.linuxforhealth.fhir.path.FHIRPathAbstractNode;
import org.linuxforhealth.fhir.path.FHIRPathBooleanValue;
import org.linuxforhealth.fhir.path.FHIRPathDateTimeValue;
import org.linuxforhealth.fhir.path.FHIRPathDecimalValue;
import org.linuxforhealth.fhir.path.FHIRPathElementNode;
import org.linuxforhealth.fhir.path.FHIRPathIntegerValue;
import org.linuxforhealth.fhir.path.FHIRPathQuantityValue;
import org.linuxforhealth.fhir.path.FHIRPathResourceNode;
import org.linuxforhealth.fhir.path.FHIRPathStringValue;
import org.linuxforhealth.fhir.path.FHIRPathTimeValue;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceProcessorException;

@Deprecated
public interface Processor<T> {
    T process(SearchParameter parameter, Object value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, java.lang.String value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, org.linuxforhealth.fhir.model.type.String value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Address value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Annotation value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Attachment value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, BackboneElement value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, Base64Binary value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, java.lang.Boolean value) throws FHIRPersistenceProcessorException;

    T process(SearchParameter parameter, org.linuxforhealth.fhir.model.type.Boolean value) throws FHIRPersistenceProcessorException;

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

    T process(SearchParameter parameter, org.linuxforhealth.fhir.model.type.Integer value) throws FHIRPersistenceProcessorException;

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
