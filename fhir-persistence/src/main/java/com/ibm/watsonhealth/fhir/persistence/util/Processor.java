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

public interface Processor<T> {
    T process(SearchParameter parameter, Object value);
    T process(SearchParameter parameter, String value);
    T process(SearchParameter parameter, Address value);
    T process(SearchParameter parameter, Annotation value);
    T process(SearchParameter parameter, Attachment value);
    T process(SearchParameter parameter, Base64Binary value);
    T process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.Boolean value);
    T process(SearchParameter parameter, Code value);
    T process(SearchParameter parameter, CodeableConcept value);
    T process(SearchParameter parameter, Coding value);
    T process(SearchParameter parameter, ContactPoint value);
    T process(SearchParameter parameter, Date value);
    T process(SearchParameter parameter, DateTime value);
    T process(SearchParameter parameter, Decimal value);
    T process(SearchParameter parameter, HumanName value);
    T process(SearchParameter parameter, Id value);
    T process(SearchParameter parameter, Identifier value);
    T process(SearchParameter parameter, Instant value);
    T process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.Integer value);
    T process(SearchParameter parameter, LocationPosition value);
    T process(SearchParameter parameter, Markdown value);
    T process(SearchParameter parameter, Meta value);
    T process(SearchParameter parameter, Oid value);
    T process(SearchParameter parameter, Period value);
    T process(SearchParameter parameter, PositiveInt value);
    T process(SearchParameter parameter, Quantity value);
    T process(SearchParameter parameter, Range value);
    T process(SearchParameter parameter, Ratio value);
    T process(SearchParameter parameter, Reference value);
    T process(SearchParameter parameter, SampledData value);
    T process(SearchParameter parameter, Signature value);
    T process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.String value);
    T process(SearchParameter parameter, Time value);
    T process(SearchParameter parameter, Timing value);
    T process(SearchParameter parameter, UnsignedInt value);
    T process(SearchParameter parameter, Uri value);
    T process(SearchParameter parameter, Uuid value);
}
