/*
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test;

import static org.testng.AssertJUnit.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import org.testng.annotations.Test;

import com.ibm.fhir.model.path.FHIRPathAbstractNode;
import com.ibm.fhir.model.path.FHIRPathBooleanValue;
import com.ibm.fhir.model.path.FHIRPathDateTimeValue;
import com.ibm.fhir.model.path.FHIRPathDecimalValue;
import com.ibm.fhir.model.path.FHIRPathElementNode;
import com.ibm.fhir.model.path.FHIRPathIntegerValue;
import com.ibm.fhir.model.path.FHIRPathQuantityNode;
import com.ibm.fhir.model.path.FHIRPathResourceNode;
import com.ibm.fhir.model.path.FHIRPathStringValue;
import com.ibm.fhir.model.path.FHIRPathTimeValue;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
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
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.type.code.AccountStatus;
import com.ibm.fhir.persistence.exception.FHIRPersistenceProcessorException;
import com.ibm.fhir.persistence.util.AbstractProcessor;
import com.ibm.fhir.persistence.util.Processor;

/**
 * TODO update tests
 * TODO add processor support for R4 types
 * Rewritten for R4 to drive tests by introspecting model
 * @author rarnold
 *
 */
public class ProcessorTest {
    private Processor<String> processor = new MockProcessor();

    /**
     * Old (DSTU2) implementation example
     * @throws Exception
     */
    @Test
    public void testProcessAccountStatus()  throws Exception {

    	AccountStatus value = AccountStatus.builder()
    			.value(AccountStatus.ValueSet.ACTIVE)
    			.build();
        
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Code)", result);
    }


    public static class MockProcessor extends AbstractProcessor<String> {
    	
    	@Override
        public String process(SearchParameter parameter, Address value) {
            return "process(SearchParameter, Address)";
        }

    	@Override
        public String process(SearchParameter parameter, Annotation value) {
            return "process(SearchParameter, Annotation)";
        }

    	@Override
        public String process(SearchParameter parameter, Attachment value) {
            return "process(SearchParameter, Attachment)";
        }

    	@Override
        public String process(SearchParameter parameter, Base64Binary value) {
            return "process(SearchParameter, Base64Binary)";
        }

    	@Override
        public String process(SearchParameter parameter, Boolean value) {
            return "process(SearchParameter, Boolean)";
        }

    	@Override
        public String process(SearchParameter parameter, Code value) {
            return "process(SearchParameter, Code)";
        }

    	@Override
        public String process(SearchParameter parameter, CodeableConcept value) {
            return "process(SearchParameter, CodeableConcept)";
        }

    	@Override
        public String process(SearchParameter parameter, Coding value) {
            return "process(SearchParameter, Coding)";
        }

    	@Override
        public String process(SearchParameter parameter, ContactPoint value)  {
            return "process(SearchParameter, ContactPoint)";
        }

    	@Override
        public String process(SearchParameter parameter, Date value)  {
            return "process(SearchParameter, Date)";
        }

    	@Override
        public String process(SearchParameter parameter, DateTime value)  {
            return "process(SearchParameter, DateTime)";
        }

    	@Override
        public String process(SearchParameter parameter, Decimal value)  {
            return "process(SearchParameter, Decimal)";
        }

    	@Override
        public String process(SearchParameter parameter, HumanName value) {
            return "process(SearchParameter, HumanName)";
        }

    	@Override
        public String process(SearchParameter parameter, Id value) {
            return "process(SearchParameter, Id)";
        }

    	@Override
        public String process(SearchParameter parameter, Identifier value) {
            return "process(SearchParameter, Identifier)";
        }

    	@Override
        public String process(SearchParameter parameter, Instant value) {
            return "process(SearchParameter, Instant)";
        }

    	@Override
        public String process(SearchParameter parameter, java.lang.Integer value) {
            return "process(SearchParameter, Integer)";
        }

    	@Override
        public String process(SearchParameter parameter, Location.Position value) {
            return "process(SearchParameter, Location.Position)";
        }

    	@Override
        public String process(SearchParameter parameter, Markdown value) {
            return "process(SearchParameter, Markdown)";
        }

    	@Override
        public String process(SearchParameter parameter, Meta value) {
            return "process(SearchParameter, Meta)";
        }

    	@Override
        public String process(SearchParameter parameter, Money value) {
            return "process(SearchParameter, Money)";
        }

    	@Override
        public String process(SearchParameter parameter, Oid value) {
            return "process(SearchParameter, Oid)";
        }

    	@Override
        public String process(SearchParameter parameter, Period value) {
            return "process(SearchParameter, Period)";
        }

    	@Override
        public String process(SearchParameter parameter, PositiveInt value) {
            return "process(SearchParameter, PositiveInt)";
        }

    	@Override
        public String process(SearchParameter parameter, Quantity value) {
            return "process(SearchParameter, Quantity)";
        }

    	@Override
        public String process(SearchParameter parameter, Range value) {
            return "process(SearchParameter, Range)";
        }

    	@Override
        public String process(SearchParameter parameter, Ratio value) {
            return "process(SearchParameter, Ratio)";
        }

    	@Override
        public String process(SearchParameter parameter, Reference value) {
            return "process(SearchParameter, Reference)";
        }

    	@Override
        public String process(SearchParameter parameter, SampledData value) {
            return "process(SearchParameter, SampledData)";
        }

    	@Override
        public String process(SearchParameter parameter, Signature value) {
            return "process(SearchParameter, Signature)";
        }

    	@Override
        public String process(SearchParameter parameter, com.ibm.fhir.model.type.String value) {
            return "process(SearchParameter, String)";
        }

    	@Override
        public String process(SearchParameter parameter, Time value) {
            return "process(SearchParameter, Time)";
        }

    	@Override
        public String process(SearchParameter parameter, Timing value) {
            return "process(SearchParameter, Timing)";
        }

    	@Override
        public String process(SearchParameter parameter, UnsignedInt value) {
            return "process(SearchParameter, UnsignedInt)";
        }

    	@Override
        public String process(SearchParameter parameter, Uri value) {
            return "process(SearchParameter, Uri)";
        }

    	@Override
        public String process(SearchParameter parameter, Uuid value) {
            return "process(SearchParameter, Uuid)";
        }

    	@Override
        public String process(SearchParameter parameter, String value) {
            return "process(SearchParameter, String)";
        }

		@Override
		public String process(SearchParameter parameter, BackboneElement value)
				throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, BackboneElement)";
		}

		@Override
		public String process(SearchParameter parameter, ContactDetail value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, ContactDetail)";
		}

		@Override
		public String process(SearchParameter parameter, Contributor value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, Constributor)";
		}

		@Override
		public String process(SearchParameter parameter, DataRequirement value)
				throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, DataRequirement)";
		}

		@Override
		public String process(SearchParameter parameter, Expression value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, Expression)";
		}

		@Override
		public String process(SearchParameter parameter, Narrative value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, Narrative)";
		}

		@Override
		public String process(SearchParameter parameter, ParameterDefinition value)
				throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, ParameterDefinition)";
		}

		@Override
		public String process(SearchParameter parameter, RelatedArtifact value)
				throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, RelatedArtiface)";
		}

		@Override
		public String process(SearchParameter parameter, TriggerDefinition value)
				throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, TriggerDefinition)";
		}

		@Override
		public String process(SearchParameter parameter, UsageContext value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, UsageContext)";
		}

        @Override
        public String process(SearchParameter parameter, FHIRPathAbstractNode value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathAbstractNode)";
        }

        @Override
        public String process(SearchParameter parameter, FHIRPathElementNode value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathElementNode)";
        }

        @Override
        public String process(SearchParameter parameter, FHIRPathDateTimeValue value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathDateTimeValue)";
        }
        
        @Override
        public String process(SearchParameter parameter, FHIRPathStringValue value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathStringValue)";
        }

        @Override
        public String process(SearchParameter parameter, ZonedDateTime value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, ZonedDateTime)";
        }

        @Override
        public String process(SearchParameter parameter, FHIRPathTimeValue value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathTimeValue)";
        }

        @Override
        public String process(SearchParameter parameter, FHIRPathResourceNode value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathResourceNode)";
        }

        @Override
        public String process(SearchParameter parameter, FHIRPathIntegerValue value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathIntegerValue)";
        }

        @Override
        public String process(SearchParameter parameter, FHIRPathDecimalValue value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathDecimalValue)";
        }

        @Override
        public String process(SearchParameter parameter, FHIRPathBooleanValue value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathBooleanValue)";
        }

        @Override
        public String process(SearchParameter parameter, LocalDate value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, LocalDate)";
        }

        @Override
        public String process(SearchParameter parameter, YearMonth value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, YearMonth)";
        }
        
        @Override
        public String process(SearchParameter parameter, FHIRPathQuantityNode value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, FHIRPathQuantityNode)";
        }

        @Override
        public String process(SearchParameter parameter, Year value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, Year)";
        }

        @Override
        public String process(SearchParameter parameter, BigDecimal value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, BigDecimal)";
        }

        @Override
        public String process(SearchParameter parameter, com.ibm.fhir.model.type.Boolean value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, com.ibm.fhir.model.type.Boolean)";
        }

        @Override
        public String process(SearchParameter parameter, com.ibm.fhir.model.type.Integer value) throws FHIRPersistenceProcessorException {
            return "process(SearchParameter, com.ibm.fhir.model.type.Integer)";
        }
    }
}
