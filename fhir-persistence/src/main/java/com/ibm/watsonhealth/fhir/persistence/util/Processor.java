/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import com.ibm.watsonhealth.fhir.model.Address;
import com.ibm.watsonhealth.fhir.model.Annotation;
import com.ibm.watsonhealth.fhir.model.Attachment;
import com.ibm.watsonhealth.fhir.model.Base64Binary;
import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.Coding;
import com.ibm.watsonhealth.fhir.model.ContactPoint;
import com.ibm.watsonhealth.fhir.model.Date;
import com.ibm.watsonhealth.fhir.model.DateTime;
import com.ibm.watsonhealth.fhir.model.Decimal;
import com.ibm.watsonhealth.fhir.model.HumanName;
import com.ibm.watsonhealth.fhir.model.Id;
import com.ibm.watsonhealth.fhir.model.Identifier;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.model.LocationPosition;
import com.ibm.watsonhealth.fhir.model.Markdown;
import com.ibm.watsonhealth.fhir.model.Meta;
import com.ibm.watsonhealth.fhir.model.Oid;
import com.ibm.watsonhealth.fhir.model.Period;
import com.ibm.watsonhealth.fhir.model.PositiveInt;
import com.ibm.watsonhealth.fhir.model.Quantity;
import com.ibm.watsonhealth.fhir.model.Range;
import com.ibm.watsonhealth.fhir.model.Ratio;
import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.SampledData;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.model.Signature;
import com.ibm.watsonhealth.fhir.model.Time;
import com.ibm.watsonhealth.fhir.model.Timing;
import com.ibm.watsonhealth.fhir.model.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.Uri;
import com.ibm.watsonhealth.fhir.model.Uuid;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceProcessorException;

public interface Processor<T> {
    T process(SearchParameter parameter, Object value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, String value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Address value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Annotation value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Attachment value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Base64Binary value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.Boolean value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Code value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, CodeableConcept value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Coding value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, ContactPoint value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Date value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, DateTime value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Decimal value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, HumanName value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Id value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Identifier value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Instant value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.Integer value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, LocationPosition value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Markdown value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Meta value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Oid value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Period value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, PositiveInt value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Quantity value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Range value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Ratio value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Reference value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, SampledData value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Signature value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.String value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Time value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Timing value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, UnsignedInt value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Uri value) throws FHIRPersistenceProcessorException;
    T process(SearchParameter parameter, Uuid value) throws FHIRPersistenceProcessorException;
}
