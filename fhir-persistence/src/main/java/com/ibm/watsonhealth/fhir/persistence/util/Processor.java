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
import com.ibm.watsonhealth.fhir.persistence.exception.SearchParmException;

public interface Processor<T> {
    T process(SearchParameter parameter, Object value) throws SearchParmException;
    T process(SearchParameter parameter, String value) throws SearchParmException;
    T process(SearchParameter parameter, Address value) throws SearchParmException;
    T process(SearchParameter parameter, Annotation value) throws SearchParmException;
    T process(SearchParameter parameter, Attachment value) throws SearchParmException;
    T process(SearchParameter parameter, Base64Binary value) throws SearchParmException;
    T process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.Boolean value) throws SearchParmException;
    T process(SearchParameter parameter, Code value) throws SearchParmException;
    T process(SearchParameter parameter, CodeableConcept value) throws SearchParmException;
    T process(SearchParameter parameter, Coding value) throws SearchParmException;
    T process(SearchParameter parameter, ContactPoint value) throws SearchParmException;
    T process(SearchParameter parameter, Date value) throws SearchParmException;
    T process(SearchParameter parameter, DateTime value) throws SearchParmException;
    T process(SearchParameter parameter, Decimal value) throws SearchParmException;
    T process(SearchParameter parameter, HumanName value) throws SearchParmException;
    T process(SearchParameter parameter, Id value) throws SearchParmException;
    T process(SearchParameter parameter, Identifier value) throws SearchParmException;
    T process(SearchParameter parameter, Instant value) throws SearchParmException;
    T process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.Integer value) throws SearchParmException;
    T process(SearchParameter parameter, LocationPosition value) throws SearchParmException;
    T process(SearchParameter parameter, Markdown value) throws SearchParmException;
    T process(SearchParameter parameter, Meta value) throws SearchParmException;
    T process(SearchParameter parameter, Oid value) throws SearchParmException;
    T process(SearchParameter parameter, Period value) throws SearchParmException;
    T process(SearchParameter parameter, PositiveInt value) throws SearchParmException;
    T process(SearchParameter parameter, Quantity value) throws SearchParmException;
    T process(SearchParameter parameter, Range value) throws SearchParmException;
    T process(SearchParameter parameter, Ratio value) throws SearchParmException;
    T process(SearchParameter parameter, Reference value) throws SearchParmException;
    T process(SearchParameter parameter, SampledData value) throws SearchParmException;
    T process(SearchParameter parameter, Signature value) throws SearchParmException;
    T process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.String value) throws SearchParmException;
    T process(SearchParameter parameter, Time value) throws SearchParmException;
    T process(SearchParameter parameter, Timing value) throws SearchParmException;
    T process(SearchParameter parameter, UnsignedInt value) throws SearchParmException;
    T process(SearchParameter parameter, Uri value) throws SearchParmException;
    T process(SearchParameter parameter, Uuid value) throws SearchParmException;
}
