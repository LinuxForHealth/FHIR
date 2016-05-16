/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test;

import static org.testng.AssertJUnit.assertEquals;

import com.ibm.watsonhealth.fhir.model.AccountStatus;
import com.ibm.watsonhealth.fhir.model.ActionList;
import com.ibm.watsonhealth.fhir.model.Address;
import com.ibm.watsonhealth.fhir.model.AddressUse;
import com.ibm.watsonhealth.fhir.model.AllergyIntoleranceCategory;
import com.ibm.watsonhealth.fhir.model.AllergyIntoleranceCriticality;
import com.ibm.watsonhealth.fhir.model.AllergyIntoleranceSeverity;
import com.ibm.watsonhealth.fhir.model.AllergyIntoleranceStatus;
import com.ibm.watsonhealth.fhir.model.AllergyIntoleranceType;
import com.ibm.watsonhealth.fhir.model.Annotation;
import com.ibm.watsonhealth.fhir.model.AppointmentStatus;
import com.ibm.watsonhealth.fhir.model.Attachment;
import com.ibm.watsonhealth.fhir.model.AuditEventAction;
import com.ibm.watsonhealth.fhir.model.Base64Binary;
import com.ibm.watsonhealth.fhir.model.CarePlanRelationship;
import com.ibm.watsonhealth.fhir.model.ClinicalImpressionStatus;
import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.Coding;
import com.ibm.watsonhealth.fhir.model.CommunicationRequestStatus;
import com.ibm.watsonhealth.fhir.model.CommunicationStatus;
import com.ibm.watsonhealth.fhir.model.CompositionStatus;
import com.ibm.watsonhealth.fhir.model.ContactPoint;
import com.ibm.watsonhealth.fhir.model.DataElementStringency;
import com.ibm.watsonhealth.fhir.model.Date;
import com.ibm.watsonhealth.fhir.model.DateTime;
import com.ibm.watsonhealth.fhir.model.Decimal;
import com.ibm.watsonhealth.fhir.model.DeviceMetricCategory;
import com.ibm.watsonhealth.fhir.model.DiagnosticOrderStatus;
import com.ibm.watsonhealth.fhir.model.DiagnosticReportStatus;
import com.ibm.watsonhealth.fhir.model.DigitalMediaType;
import com.ibm.watsonhealth.fhir.model.DocumentRelationshipType;
import com.ibm.watsonhealth.fhir.model.EncounterState;
import com.ibm.watsonhealth.fhir.model.EpisodeOfCareStatus;
import com.ibm.watsonhealth.fhir.model.Extension;
import com.ibm.watsonhealth.fhir.model.ExtensionContext;
import com.ibm.watsonhealth.fhir.model.GoalStatus;
import com.ibm.watsonhealth.fhir.model.GroupType;
import com.ibm.watsonhealth.fhir.model.HumanName;
import com.ibm.watsonhealth.fhir.model.Id;
import com.ibm.watsonhealth.fhir.model.Identifier;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.model.ListStatus;
import com.ibm.watsonhealth.fhir.model.LocationPosition;
import com.ibm.watsonhealth.fhir.model.LocationStatus;
import com.ibm.watsonhealth.fhir.model.Markdown;
import com.ibm.watsonhealth.fhir.model.MedicationAdministrationStatus;
import com.ibm.watsonhealth.fhir.model.MedicationDispenseStatus;
import com.ibm.watsonhealth.fhir.model.MedicationOrderStatus;
import com.ibm.watsonhealth.fhir.model.MedicationStatementStatus;
import com.ibm.watsonhealth.fhir.model.Meta;
import com.ibm.watsonhealth.fhir.model.NamingSystemIdentifierType;
import com.ibm.watsonhealth.fhir.model.NamingSystemType;
import com.ibm.watsonhealth.fhir.model.NutritionOrderStatus;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.ObservationRelationshipType;
import com.ibm.watsonhealth.fhir.model.ObservationStatus;
import com.ibm.watsonhealth.fhir.model.Oid;
import com.ibm.watsonhealth.fhir.model.OperationKind;
import com.ibm.watsonhealth.fhir.model.OrderStatus;
import com.ibm.watsonhealth.fhir.model.ParticipantStatus;
import com.ibm.watsonhealth.fhir.model.ParticipationStatus;
import com.ibm.watsonhealth.fhir.model.Period;
import com.ibm.watsonhealth.fhir.model.PositiveInt;
import com.ibm.watsonhealth.fhir.model.Quantity;
import com.ibm.watsonhealth.fhir.model.QuestionnaireResponseStatus;
import com.ibm.watsonhealth.fhir.model.QuestionnaireStatus;
import com.ibm.watsonhealth.fhir.model.Range;
import com.ibm.watsonhealth.fhir.model.Ratio;
import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.ReferralStatus;
import com.ibm.watsonhealth.fhir.model.ResponseType;
import com.ibm.watsonhealth.fhir.model.RestfulConformanceMode;
import com.ibm.watsonhealth.fhir.model.SampledData;
import com.ibm.watsonhealth.fhir.model.SearchParameter;
import com.ibm.watsonhealth.fhir.model.Signature;
import com.ibm.watsonhealth.fhir.model.SlotStatus;
import com.ibm.watsonhealth.fhir.model.StructureDefinitionKind;
import com.ibm.watsonhealth.fhir.model.SubscriptionChannelType;
import com.ibm.watsonhealth.fhir.model.SubscriptionStatus;
import com.ibm.watsonhealth.fhir.model.SupplyDeliveryStatus;
import com.ibm.watsonhealth.fhir.model.SupplyRequestStatus;
import com.ibm.watsonhealth.fhir.model.Time;
import com.ibm.watsonhealth.fhir.model.Timing;
import com.ibm.watsonhealth.fhir.model.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.Uri;
import com.ibm.watsonhealth.fhir.model.Use;
import com.ibm.watsonhealth.fhir.model.Uuid;
import com.ibm.watsonhealth.fhir.persistence.util.AbstractProcessor;
import com.ibm.watsonhealth.fhir.persistence.util.Processor;

import org.testng.annotations.Test;

public class ProcessorTest {
    private ObjectFactory factory = new ObjectFactory();
    private Processor<String> processor = new MockProcessor();

    @Test
    public void testProcessAccountStatus() {
        AccountStatus value = factory.createAccountStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.AccountStatusList.ACTIVE);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessActionList() {
        ActionList value = factory.createActionList();
        value.setValue(com.ibm.watsonhealth.fhir.model.ActionListList.CANCEL);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessAddress() {
        Object value = factory.createAddress();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Address)", result);
    }

    @Test
    public void testProcessAddressUse() {
        AddressUse value = factory.createAddressUse();
        value.setValue(com.ibm.watsonhealth.fhir.model.AddressUseList.HOME);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessAllergyIntoleranceCategory() {
        AllergyIntoleranceCategory value = factory.createAllergyIntoleranceCategory();
        value.setValue(com.ibm.watsonhealth.fhir.model.AllergyIntoleranceCategoryList.FOOD);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessAllergyIntoleranceCriticality() {
        AllergyIntoleranceCriticality value = factory.createAllergyIntoleranceCriticality();
        value.setValue(com.ibm.watsonhealth.fhir.model.AllergyIntoleranceCriticalityList.CRITL);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessAllergyIntoleranceSeverity() {
        AllergyIntoleranceSeverity value = factory.createAllergyIntoleranceSeverity();
        value.setValue(com.ibm.watsonhealth.fhir.model.AllergyIntoleranceSeverityList.MILD);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessAllergyIntoleranceStatus() {
        AllergyIntoleranceStatus value = factory.createAllergyIntoleranceStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.AllergyIntoleranceStatusList.ACTIVE);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessAllergyIntoleranceType() {
        AllergyIntoleranceType value = factory.createAllergyIntoleranceType();
        value.setValue(com.ibm.watsonhealth.fhir.model.AllergyIntoleranceTypeList.ALLERGY);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessAnnotation() {
        Object value = factory.createAnnotation();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Annotation)", result);
    }

    @Test
    public void testProcessAppointmentStatus() {
        AppointmentStatus value = factory.createAppointmentStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.AppointmentStatusList.PROPOSED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessAttachment() {
        Object value = factory.createAttachment();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Attachment)", result);
    }

    @Test
    public void testProcessAuditEventAction() {
        AuditEventAction value = factory.createAuditEventAction();
        value.setValue(com.ibm.watsonhealth.fhir.model.AuditEventActionList.C);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessBase64Binary() {
        Object value = factory.createBase64Binary();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Base64Binary)", result);
    }

    @Test
    public void testProcessBoolean() {
        Object value = factory.createBoolean();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Boolean)", result);
    }

    @Test
    public void testProcessCarePlanRelationship() {
        CarePlanRelationship value = factory.createCarePlanRelationship();
        value.setValue(com.ibm.watsonhealth.fhir.model.CarePlanRelationshipList.INCLUDES);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessClinicalImpressionStatus() {
        ClinicalImpressionStatus value = factory.createClinicalImpressionStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.ClinicalImpressionStatusList.IN_PROGRESS);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessCode() {
        Object value = factory.createCode();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Code)", result);
    }

    @Test
    public void testProcessCodeableConcept() {
        Object value = factory.createCodeableConcept();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, CodeableConcept)", result);
    }

    @Test
    public void testProcessCoding() {
        Object value = factory.createCoding();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Coding)", result);
    }

    @Test
    public void testProcessCommunicationRequestStatus() {
        CommunicationRequestStatus value = factory.createCommunicationRequestStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.CommunicationRequestStatusList.PROPOSED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessCommunicationStatus() {
        CommunicationStatus value = factory.createCommunicationStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.CommunicationStatusList.IN_PROGRESS);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessCompositionStatus() {
        CompositionStatus value = factory.createCompositionStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.CompositionStatusList.PRELIMINARY);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessContactPoint() {
        Object value = factory.createContactPoint();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, ContactPoint)", result);
    }

    @Test
    public void testProcessDataElementStringency() {
        DataElementStringency value = factory.createDataElementStringency();
        value.setValue(com.ibm.watsonhealth.fhir.model.DataElementStringencyList.COMPARABLE);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessDate() {
        Object value = factory.createDate();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Date)", result);
    }

    @Test
    public void testProcessDateTime() {
        Object value = factory.createDateTime();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, DateTime)", result);
    }

    @Test
    public void testProcessDecimal() {
        Object value = factory.createDecimal();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Decimal)", result);
    }

    @Test
    public void testProcessDeviceMetricCategory() {
        DeviceMetricCategory value = factory.createDeviceMetricCategory();
        value.setValue(com.ibm.watsonhealth.fhir.model.DeviceMetricCategoryList.MEASUREMENT);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessDiagnosticOrderStatus() {
        DiagnosticOrderStatus value = factory.createDiagnosticOrderStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.DiagnosticOrderStatusList.PROPOSED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessDiagnosticReportStatus() {
        DiagnosticReportStatus value = factory.createDiagnosticReportStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.DiagnosticReportStatusList.REGISTERED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessDigitalMediaType() {
        DigitalMediaType value = factory.createDigitalMediaType();
        value.setValue(com.ibm.watsonhealth.fhir.model.DigitalMediaTypeList.PHOTO);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessDocumentRelationshipType() {
        DocumentRelationshipType value = factory.createDocumentRelationshipType();
        value.setValue(com.ibm.watsonhealth.fhir.model.DocumentRelationshipTypeList.REPLACES);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessDuration() {
        Object value = factory.createDuration();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Quantity)", result);
    }

    @Test
    public void testProcessEncounterState() {
        EncounterState value = factory.createEncounterState();
        value.setValue(com.ibm.watsonhealth.fhir.model.EncounterStateList.PLANNED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessEpisodeOfCareStatus() {
        EpisodeOfCareStatus value = factory.createEpisodeOfCareStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.EpisodeOfCareStatusList.PLANNED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessExtension() {
        Extension extension = factory.createExtension();
        String result = null;

        com.ibm.watsonhealth.fhir.model.Boolean _boolean = factory.createBoolean();
        extension.setValueBoolean(_boolean);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Boolean)", result);
        extension.setValueBoolean(null);

        com.ibm.watsonhealth.fhir.model.Integer integer = factory.createInteger();
        extension.setValueInteger(integer);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Integer)", result);
        extension.setValueInteger(null);

        Decimal decimal = factory.createDecimal();
        extension.setValueDecimal(decimal);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Decimal)", result);
        extension.setValueDecimal(null);

        Base64Binary base64Binary = factory.createBase64Binary();
        extension.setValueBase64Binary(base64Binary);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Base64Binary)", result);
        extension.setValueBase64Binary(null);

        Instant instant = factory.createInstant();
        extension.setValueInstant(instant);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Instant)", result);
        extension.setValueInstant(null);

        com.ibm.watsonhealth.fhir.model.String string = factory.createString();
        extension.setValueString(string);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, String)", result);
        extension.setValueString(null);

        Uri uri = factory.createUri();
        extension.setValueUri(uri);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Uri)", result);
        extension.setValueUri(null);

        Date date = factory.createDate();
        extension.setValueDate(date);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Date)", result);
        extension.setValueDate(null);

        DateTime dateTime = factory.createDateTime();
        extension.setValueDateTime(dateTime);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, DateTime)", result);
        extension.setValueDateTime(null);

        Time time = factory.createTime();
        extension.setValueTime(time);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Time)", result);
        extension.setValueTime(null);

        Code code = factory.createCode();
        extension.setValueCode(code);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Code)", result);
        extension.setValueCode(null);

        Oid oid = factory.createOid();
        extension.setValueOid(oid);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Oid)", result);
        extension.setValueOid(null);

        Uuid uuid = factory.createUuid();
        extension.setValueUuid(uuid);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Uuid)", result);
        extension.setValueUuid(null);

        Id id = factory.createId();
        extension.setValueId(id);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Id)", result);
        extension.setValueId(null);

        UnsignedInt unsignedInt = factory.createUnsignedInt();
        extension.setValueUnsignedInt(unsignedInt);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, UnsignedInt)", result);
        extension.setValueUnsignedInt(null);

        PositiveInt positiveInt = factory.createPositiveInt();
        extension.setValuePositiveInt(positiveInt);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, PositiveInt)", result);
        extension.setValuePositiveInt(null);

        Markdown markdown = factory.createMarkdown();
        extension.setValueMarkdown(markdown);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Markdown)", result);
        extension.setValueMarkdown(null);

        Annotation annotation = factory.createAnnotation();
        extension.setValueAnnotation(annotation);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Annotation)", result);
        extension.setValueAnnotation(null);

        Attachment attachment = factory.createAttachment();
        extension.setValueAttachment(attachment);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Attachment)", result);
        extension.setValueAttachment(null);

        Identifier identifier = factory.createIdentifier();
        extension.setValueIdentifier(identifier);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Identifier)", result);
        extension.setValueIdentifier(null);

        CodeableConcept codeableConcept = factory.createCodeableConcept();
        extension.setValueCodeableConcept(codeableConcept);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, CodeableConcept)", result);
        extension.setValueCodeableConcept(null);

        Coding coding = factory.createCoding();
        extension.setValueCoding(coding);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Coding)", result);
        extension.setValueCoding(null);

        Quantity quantity = factory.createQuantity();
        extension.setValueQuantity(quantity);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Quantity)", result);
        extension.setValueQuantity(null);

        Range range = factory.createRange();
        extension.setValueRange(range);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Range)", result);
        extension.setValueRange(null);

        Period period = factory.createPeriod();
        extension.setValuePeriod(period);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Period)", result);
        extension.setValuePeriod(null);

        Ratio ratio = factory.createRatio();
        extension.setValueRatio(ratio);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Ratio)", result);
        extension.setValueRatio(null);

        Reference reference = factory.createReference();
        extension.setValueReference(reference);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Reference)", result);
        extension.setValueReference(null);

        SampledData sampledData = factory.createSampledData();
        extension.setValueSampledData(sampledData);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, SampledData)", result);
        extension.setValueSampledData(null);

        Signature signature = factory.createSignature();
        extension.setValueSignature(signature);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Signature)", result);
        extension.setValueSignature(null);

        HumanName humanName = factory.createHumanName();
        extension.setValueHumanName(humanName);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, HumanName)", result);
        extension.setValueHumanName(null);

        Address address = factory.createAddress();
        extension.setValueAddress(address);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Address)", result);
        extension.setValueAddress(null);

        ContactPoint contactPoint = factory.createContactPoint();
        extension.setValueContactPoint(contactPoint);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, ContactPoint)", result);
        extension.setValueContactPoint(null);

        Timing timing = factory.createTiming();
        extension.setValueTiming(timing);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Timing)", result);
        extension.setValueTiming(null);

        Meta meta = factory.createMeta();
        extension.setValueMeta(meta);
        result = processor.process(null, extension);
        assertEquals("process(SearchParameter, Meta)", result);
        extension.setValueMeta(null);

    }

    @Test
    public void testProcessExtensionContext() {
        ExtensionContext value = factory.createExtensionContext();
        value.setValue(com.ibm.watsonhealth.fhir.model.ExtensionContextList.RESOURCE);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessGoalStatus() {
        GoalStatus value = factory.createGoalStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.GoalStatusList.PROPOSED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessGroupType() {
        GroupType value = factory.createGroupType();
        value.setValue(com.ibm.watsonhealth.fhir.model.GroupTypeList.PERSON);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessHumanName() {
        Object value = factory.createHumanName();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, HumanName)", result);
    }

    @Test
    public void testProcessId() {
        Object value = factory.createId();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Id)", result);
    }

    @Test
    public void testProcessIdentifier() {
        Object value = factory.createIdentifier();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Identifier)", result);
    }

    @Test
    public void testProcessInstant() {
        Object value = factory.createInstant();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Instant)", result);
    }

    @Test
    public void testProcessInteger() {
        Object value = factory.createInteger();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Integer)", result);
    }

    @Test
    public void testProcessListStatus() {
        ListStatus value = factory.createListStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.ListStatusList.CURRENT);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessLocationPosition() {
        Object value = factory.createLocationPosition();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, LocationPosition)", result);
    }

    @Test
    public void testProcessLocationStatus() {
        LocationStatus value = factory.createLocationStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.LocationStatusList.ACTIVE);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessMarkdown() {
        Object value = factory.createMarkdown();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Markdown)", result);
    }

    @Test
    public void testProcessMedicationAdministrationStatus() {
        MedicationAdministrationStatus value = factory.createMedicationAdministrationStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.MedicationAdministrationStatusList.IN_PROGRESS);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessMedicationDispenseStatus() {
        MedicationDispenseStatus value = factory.createMedicationDispenseStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.MedicationDispenseStatusList.IN_PROGRESS);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessMedicationOrderStatus() {
        MedicationOrderStatus value = factory.createMedicationOrderStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.MedicationOrderStatusList.ACTIVE);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessMedicationStatementStatus() {
        MedicationStatementStatus value = factory.createMedicationStatementStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.MedicationStatementStatusList.ACTIVE);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessMeta() {
        Object value = factory.createMeta();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Meta)", result);
    }

    @Test
    public void testProcessMoney() {
        Object value = factory.createMoney();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Quantity)", result);
    }

    @Test
    public void testProcessNamingSystemIdentifierType() {
        NamingSystemIdentifierType value = factory.createNamingSystemIdentifierType();
        value.setValue(com.ibm.watsonhealth.fhir.model.NamingSystemIdentifierTypeList.OID);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessNamingSystemType() {
        NamingSystemType value = factory.createNamingSystemType();
        value.setValue(com.ibm.watsonhealth.fhir.model.NamingSystemTypeList.CODESYSTEM);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessNutritionOrderStatus() {
        NutritionOrderStatus value = factory.createNutritionOrderStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.NutritionOrderStatusList.PROPOSED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessObservationRelationshipType() {
        ObservationRelationshipType value = factory.createObservationRelationshipType();
        value.setValue(com.ibm.watsonhealth.fhir.model.ObservationRelationshipTypeList.HAS_MEMBER);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessObservationStatus() {
        ObservationStatus value = factory.createObservationStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.ObservationStatusList.REGISTERED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessOid() {
        Object value = factory.createOid();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Oid)", result);
    }

    @Test
    public void testProcessOperationKind() {
        OperationKind value = factory.createOperationKind();
        value.setValue(com.ibm.watsonhealth.fhir.model.OperationKindList.OPERATION);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessOrderStatus() {
        OrderStatus value = factory.createOrderStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.OrderStatusList.PENDING);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessParticipantStatus() {
        ParticipantStatus value = factory.createParticipantStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.ParticipantStatusList.ACCEPTED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessParticipationStatus() {
        ParticipationStatus value = factory.createParticipationStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.ParticipationStatusList.ACCEPTED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessPeriod() {
        Object value = factory.createPeriod();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Period)", result);
    }

    @Test
    public void testProcessPositiveInt() {
        Object value = factory.createPositiveInt();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, PositiveInt)", result);
    }

    @Test
    public void testProcessQuantity() {
        Object value = factory.createQuantity();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Quantity)", result);
    }

    @Test
    public void testProcessQuestionnaireResponseStatus() {
        QuestionnaireResponseStatus value = factory.createQuestionnaireResponseStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.QuestionnaireResponseStatusList.IN_PROGRESS);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessQuestionnaireStatus() {
        QuestionnaireStatus value = factory.createQuestionnaireStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.QuestionnaireStatusList.DRAFT);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessRange() {
        Object value = factory.createRange();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Range)", result);
    }

    @Test
    public void testProcessRatio() {
        Object value = factory.createRatio();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Ratio)", result);
    }

    @Test
    public void testProcessReference() {
        Object value = factory.createReference();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Reference)", result);
    }

    @Test
    public void testProcessReferralStatus() {
        ReferralStatus value = factory.createReferralStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.ReferralStatusList.DRAFT);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessResponseType() {
        ResponseType value = factory.createResponseType();
        value.setValue(com.ibm.watsonhealth.fhir.model.ResponseTypeList.OK);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessRestfulConformanceMode() {
        RestfulConformanceMode value = factory.createRestfulConformanceMode();
        value.setValue(com.ibm.watsonhealth.fhir.model.RestfulConformanceModeList.CLIENT);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessSampledData() {
        Object value = factory.createSampledData();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, SampledData)", result);
    }

    @Test
    public void testProcessSignature() {
        Object value = factory.createSignature();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Signature)", result);
    }

    @Test
    public void testProcessSimpleQuantity() {
        Object value = factory.createSimpleQuantity();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Quantity)", result);
    }

    @Test
    public void testProcessSlotStatus() {
        SlotStatus value = factory.createSlotStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.SlotStatusList.BUSY);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessString() {
        Object value = factory.createString();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessStructureDefinitionKind() {
        StructureDefinitionKind value = factory.createStructureDefinitionKind();
        value.setValue(com.ibm.watsonhealth.fhir.model.StructureDefinitionKindList.DATATYPE);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessSubscriptionChannelType() {
        SubscriptionChannelType value = factory.createSubscriptionChannelType();
        value.setValue(com.ibm.watsonhealth.fhir.model.SubscriptionChannelTypeList.REST_HOOK);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessSubscriptionStatus() {
        SubscriptionStatus value = factory.createSubscriptionStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.SubscriptionStatusList.REQUESTED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessSupplyDeliveryStatus() {
        SupplyDeliveryStatus value = factory.createSupplyDeliveryStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.SupplyDeliveryStatusList.IN_PROGRESS);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessSupplyRequestStatus() {
        SupplyRequestStatus value = factory.createSupplyRequestStatus();
        value.setValue(com.ibm.watsonhealth.fhir.model.SupplyRequestStatusList.REQUESTED);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessTime() {
        Object value = factory.createTime();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Time)", result);
    }

    @Test
    public void testProcessTiming() {
        Object value = factory.createTiming();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Timing)", result);
    }

    @Test
    public void testProcessUnsignedInt() {
        Object value = factory.createUnsignedInt();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, UnsignedInt)", result);
    }

    @Test
    public void testProcessUri() {
        Object value = factory.createUri();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Uri)", result);
    }

    @Test
    public void testProcessUse() {
        Use value = factory.createUse();
        value.setValue(com.ibm.watsonhealth.fhir.model.UseList.COMPLETE);
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, String)", result);
    }

    @Test
    public void testProcessUuid() {
        Object value = factory.createUuid();
        String result = processor.process(null, value);
        assertEquals("process(SearchParameter, Uuid)", result);
    }

    public static class MockProcessor extends AbstractProcessor<String> {
        public String process(SearchParameter parameter, Address value) {
            return "process(SearchParameter, Address)";
        }

        public String process(SearchParameter parameter, Annotation value) {
            return "process(SearchParameter, Annotation)";
        }

        public String process(SearchParameter parameter, Attachment value) {
            return "process(SearchParameter, Attachment)";
        }

        public String process(SearchParameter parameter, Base64Binary value) {
            return "process(SearchParameter, Base64Binary)";
        }

        public String process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.Boolean value) {
            return "process(SearchParameter, Boolean)";
        }

        public String process(SearchParameter parameter, Code value) {
            return "process(SearchParameter, Code)";
        }

        public String process(SearchParameter parameter, CodeableConcept value) {
            return "process(SearchParameter, CodeableConcept)";
        }

        public String process(SearchParameter parameter, Coding value) {
            return "process(SearchParameter, Coding)";
        }

        public String process(SearchParameter parameter, ContactPoint value) {
            return "process(SearchParameter, ContactPoint)";
        }

        public String process(SearchParameter parameter, Date value) {
            return "process(SearchParameter, Date)";
        }

        public String process(SearchParameter parameter, DateTime value) {
            return "process(SearchParameter, DateTime)";
        }

        public String process(SearchParameter parameter, Decimal value) {
            return "process(SearchParameter, Decimal)";
        }

        public String process(SearchParameter parameter, HumanName value) {
            return "process(SearchParameter, HumanName)";
        }

        public String process(SearchParameter parameter, Id value) {
            return "process(SearchParameter, Id)";
        }

        public String process(SearchParameter parameter, Identifier value) {
            return "process(SearchParameter, Identifier)";
        }

        public String process(SearchParameter parameter, Instant value) {
            return "process(SearchParameter, Instant)";
        }

        public String process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.Integer value) {
            return "process(SearchParameter, Integer)";
        }

        public String process(SearchParameter parameter, LocationPosition value) {
            return "process(SearchParameter, LocationPosition)";
        }

        public String process(SearchParameter parameter, Markdown value) {
            return "process(SearchParameter, Markdown)";
        }

        public String process(SearchParameter parameter, Meta value) {
            return "process(SearchParameter, Meta)";
        }

        public String process(SearchParameter parameter, Oid value) {
            return "process(SearchParameter, Oid)";
        }

        public String process(SearchParameter parameter, Period value) {
            return "process(SearchParameter, Period)";
        }

        public String process(SearchParameter parameter, PositiveInt value) {
            return "process(SearchParameter, PositiveInt)";
        }

        public String process(SearchParameter parameter, Quantity value) {
            return "process(SearchParameter, Quantity)";
        }

        public String process(SearchParameter parameter, Range value) {
            return "process(SearchParameter, Range)";
        }

        public String process(SearchParameter parameter, Ratio value) {
            return "process(SearchParameter, Ratio)";
        }

        public String process(SearchParameter parameter, Reference value) {
            return "process(SearchParameter, Reference)";
        }

        public String process(SearchParameter parameter, SampledData value) {
            return "process(SearchParameter, SampledData)";
        }

        public String process(SearchParameter parameter, Signature value) {
            return "process(SearchParameter, Signature)";
        }

        public String process(SearchParameter parameter, com.ibm.watsonhealth.fhir.model.String value) {
            return "process(SearchParameter, String)";
        }

        public String process(SearchParameter parameter, Time value) {
            return "process(SearchParameter, Time)";
        }

        public String process(SearchParameter parameter, Timing value) {
            return "process(SearchParameter, Timing)";
        }

        public String process(SearchParameter parameter, UnsignedInt value) {
            return "process(SearchParameter, UnsignedInt)";
        }

        public String process(SearchParameter parameter, Uri value) {
            return "process(SearchParameter, Uri)";
        }

        public String process(SearchParameter parameter, Uuid value) {
            return "process(SearchParameter, Uuid)";
        }

        public String process(SearchParameter parameter, String value) {
            return "process(SearchParameter, String)";
        }
    }
}
