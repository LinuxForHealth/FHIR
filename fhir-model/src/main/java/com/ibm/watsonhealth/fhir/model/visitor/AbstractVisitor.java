/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.visitor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import com.ibm.watsonhealth.fhir.model.resource.*;
import com.ibm.watsonhealth.fhir.model.type.*;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.String;

public abstract class AbstractVisitor implements Visitor {
    @Override
    public boolean preVisit(Element element) {
        return true;
    }

    @Override
    public boolean preVisit(Resource resource) {
        return true;
    }

    @Override
    public void postVisit(Element element) {
    }

    @Override
    public void postVisit(Resource resource) {
    }

    @Override
    public void visitStart(java.lang.String elementName, Element element) {
    }

    @Override
    public void visitStart(java.lang.String elementName, Resource resource) {
    }

    @Override
    public void visitStart(java.lang.String elementName, java.util.List<? extends Visitable> visitables, Class<?> type) {
    }

    @Override
    public void visitEnd(java.lang.String elementName, Element element) {
    }

    @Override
    public void visitEnd(java.lang.String elementName, Resource resource) {
    }

    @Override
    public void visitEnd(java.lang.String elementName, java.util.List<? extends Visitable> visitables, Class<?> type) {
    }

    @Override
    public boolean visit(java.lang.String elementName, Account account) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Account.Coverage accountCoverage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Account.Guarantor accountGuarantor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ActivityDefinition activityDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ActivityDefinition.DynamicValue activityDefinitionDynamicValue) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ActivityDefinition.Participant activityDefinitionParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Address address) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AdverseEvent adverseEvent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AdverseEvent.SuspectEntity adverseEventSuspectEntity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AdverseEvent.SuspectEntity.Causality adverseEventSuspectEntityCausality) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Age age) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AllergyIntolerance allergyIntolerance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AllergyIntolerance.Reaction allergyIntoleranceReaction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Annotation annotation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Appointment appointment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Appointment.Participant appointmentParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AppointmentResponse appointmentResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Attachment attachment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent auditEvent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Agent auditEventAgent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Agent.Network auditEventAgentNetwork) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Entity auditEventEntity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Entity.Detail auditEventEntityDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Source auditEventSource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, BackboneElement backboneElement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Base64Binary base64Binary) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Basic basic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Binary binary) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct biologicallyDerivedProduct) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct.Collection biologicallyDerivedProductCollection) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct.Manipulation biologicallyDerivedProductManipulation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct.Processing biologicallyDerivedProductProcessing) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct.Storage biologicallyDerivedProductStorage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, BodyStructure bodyStructure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Boolean _boolean) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle bundle) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Entry bundleEntry) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Entry.Request bundleEntryRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Entry.Response bundleEntryResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Entry.Search bundleEntrySearch) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Link bundleLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Canonical canonical) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement capabilityStatement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Document capabilityStatementDocument) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Implementation capabilityStatementImplementation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Messaging capabilityStatementMessaging) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Messaging.Endpoint capabilityStatementMessagingEndpoint) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Messaging.SupportedMessage capabilityStatementMessagingSupportedMessage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest capabilityStatementRest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Interaction capabilityStatementRestInteraction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Resource capabilityStatementRestResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Resource.Interaction capabilityStatementRestResourceInteraction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Resource.Operation capabilityStatementRestResourceOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Resource.SearchParam capabilityStatementRestResourceSearchParam) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Security capabilityStatementRestSecurity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Software capabilityStatementSoftware) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CarePlan carePlan) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CarePlan.Activity carePlanActivity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CarePlan.Activity.Detail carePlanActivityDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CareTeam careTeam) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CareTeam.Participant careTeamParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CatalogEntry catalogEntry) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CatalogEntry.RelatedEntry catalogEntryRelatedEntry) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItem chargeItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItem.Performer chargeItemPerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItemDefinition chargeItemDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItemDefinition.Applicability chargeItemDefinitionApplicability) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItemDefinition.PropertyGroup chargeItemDefinitionPropertyGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItemDefinition.PropertyGroup.PriceComponent chargeItemDefinitionPropertyGroupPriceComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim claim) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Accident claimAccident) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.CareTeam claimCareTeam) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Diagnosis claimDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Insurance claimInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Item claimItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Item.Detail claimItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Item.Detail.SubDetail claimItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Payee claimPayee) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Procedure claimProcedure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Related claimRelated) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.SupportingInfo claimSupportingInfo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse claimResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.AddItem claimResponseAddItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.AddItem.Detail claimResponseAddItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.AddItem.Detail.SubDetail claimResponseAddItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Error claimResponseError) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Insurance claimResponseInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Item claimResponseItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Item.Adjudication claimResponseItemAdjudication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Item.Detail claimResponseItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Item.Detail.SubDetail claimResponseItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Payment claimResponsePayment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.ProcessNote claimResponseProcessNote) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Total claimResponseTotal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClinicalImpression clinicalImpression) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClinicalImpression.Finding clinicalImpressionFinding) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ClinicalImpression.Investigation clinicalImpressionInvestigation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Code code) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem codeSystem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Concept codeSystemConcept) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Concept.Designation codeSystemConceptDesignation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Concept.Property codeSystemConceptProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Filter codeSystemFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Property codeSystemProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeableConcept codeableConcept) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Coding coding) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Communication communication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Communication.Payload communicationPayload) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CommunicationRequest communicationRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CommunicationRequest.Payload communicationRequestPayload) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CompartmentDefinition compartmentDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CompartmentDefinition.Resource compartmentDefinitionResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition composition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition.Attester compositionAttester) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition.Event compositionEvent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition.RelatesTo compositionRelatesTo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition.Section compositionSection) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap conceptMap) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group conceptMapGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group.Element conceptMapGroupElement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group.Element.Target conceptMapGroupElementTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group.Element.Target.DependsOn conceptMapGroupElementTargetDependsOn) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group.Unmapped conceptMapGroupUnmapped) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Condition condition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Condition.Evidence conditionEvidence) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Condition.Stage conditionStage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent consent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Policy consentPolicy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Provision consentProvision) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Provision.Actor consentProvisionActor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Provision.Data consentProvisionData) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Verification consentVerification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ContactDetail contactDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ContactPoint contactPoint) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract contract) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.ContentDefinition contractContentDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Friendly contractFriendly) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Legal contractLegal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Rule contractRule) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Signer contractSigner) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term contractTerm) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Action contractTermAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Action.Subject contractTermActionSubject) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Asset contractTermAsset) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Asset.Context contractTermAssetContext) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Asset.ValuedItem contractTermAssetValuedItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Offer contractTermOffer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Offer.Answer contractTermOfferAnswer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Offer.Party contractTermOfferParty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.SecurityLabel contractTermSecurityLabel) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Contributor contributor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Count count) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Coverage coverage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Coverage.Class coverageClass) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Coverage.CostToBeneficiary coverageCostToBeneficiary) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Coverage.CostToBeneficiary.Exception coverageCostToBeneficiaryException) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest coverageEligibilityRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest.Insurance coverageEligibilityRequestInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest.Item coverageEligibilityRequestItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest.Item.Diagnosis coverageEligibilityRequestItemDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest.SupportingInfo coverageEligibilityRequestSupportingInfo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse coverageEligibilityResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse.Error coverageEligibilityResponseError) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse.Insurance coverageEligibilityResponseInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse.Insurance.Item coverageEligibilityResponseInsuranceItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse.Insurance.Item.Benefit coverageEligibilityResponseInsuranceItemBenefit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DataRequirement dataRequirement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DataRequirement.CodeFilter dataRequirementCodeFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DataRequirement.DateFilter dataRequirementDateFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DataRequirement.Sort dataRequirementSort) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Date date) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DateTime dateTime) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Decimal decimal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DetectedIssue detectedIssue) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DetectedIssue.Evidence detectedIssueEvidence) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DetectedIssue.Mitigation detectedIssueMitigation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Device device) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.DeviceName deviceDeviceName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.Property deviceProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.Specialization deviceSpecialization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.UdiCarrier deviceUdiCarrier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.Version deviceVersion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition deviceDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.Capability deviceDefinitionCapability) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.DeviceName deviceDefinitionDeviceName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.Material deviceDefinitionMaterial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.Property deviceDefinitionProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.Specialization deviceDefinitionSpecialization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.UdiDeviceIdentifier deviceDefinitionUdiDeviceIdentifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceMetric deviceMetric) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceMetric.Calibration deviceMetricCalibration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceRequest deviceRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceRequest.Parameter deviceRequestParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceUseStatement deviceUseStatement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DiagnosticReport diagnosticReport) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DiagnosticReport.Media diagnosticReportMedia) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Distance distance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentManifest documentManifest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentManifest.Related documentManifestRelated) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentReference documentReference) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentReference.Content documentReferenceContent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentReference.Context documentReferenceContext) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentReference.RelatesTo documentReferenceRelatesTo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, DomainResource domainResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Dosage dosage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Dosage.DoseAndRate dosageDoseAndRate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Duration duration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis effectEvidenceSynthesis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.Certainty effectEvidenceSynthesisCertainty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.Certainty.CertaintySubcomponent effectEvidenceSynthesisCertaintyCertaintySubcomponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.EffectEstimate effectEvidenceSynthesisEffectEstimate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.EffectEstimate.PrecisionEstimate effectEvidenceSynthesisEffectEstimatePrecisionEstimate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.ResultsByExposure effectEvidenceSynthesisResultsByExposure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.SampleSize effectEvidenceSynthesisSampleSize) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Element element) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition elementDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Base elementDefinitionBase) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Binding elementDefinitionBinding) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Constraint elementDefinitionConstraint) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Example elementDefinitionExample) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Mapping elementDefinitionMapping) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Slicing elementDefinitionSlicing) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Slicing.Discriminator elementDefinitionSlicingDiscriminator) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Type elementDefinitionType) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter encounter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.ClassHistory encounterClassHistory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.Diagnosis encounterDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.Hospitalization encounterHospitalization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.Location encounterLocation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.Participant encounterParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.StatusHistory encounterStatusHistory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Endpoint endpoint) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EnrollmentRequest enrollmentRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EnrollmentResponse enrollmentResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EpisodeOfCare episodeOfCare) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EpisodeOfCare.Diagnosis episodeOfCareDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EpisodeOfCare.StatusHistory episodeOfCareStatusHistory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EventDefinition eventDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Evidence evidence) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EvidenceVariable evidenceVariable) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, EvidenceVariable.Characteristic evidenceVariableCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario exampleScenario) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Actor exampleScenarioActor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Instance exampleScenarioInstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Instance.ContainedInstance exampleScenarioInstanceContainedInstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Instance.Version exampleScenarioInstanceVersion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Process exampleScenarioProcess) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Process.Step exampleScenarioProcessStep) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Process.Step.Alternative exampleScenarioProcessStepAlternative) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Process.Step.Operation exampleScenarioProcessStepOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit explanationOfBenefit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Accident explanationOfBenefitAccident) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.AddItem explanationOfBenefitAddItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.AddItem.Detail explanationOfBenefitAddItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.AddItem.Detail.SubDetail explanationOfBenefitAddItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.BenefitBalance explanationOfBenefitBenefitBalance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.BenefitBalance.Financial explanationOfBenefitBenefitBalanceFinancial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.CareTeam explanationOfBenefitCareTeam) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Diagnosis explanationOfBenefitDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Insurance explanationOfBenefitInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Item explanationOfBenefitItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Item.Adjudication explanationOfBenefitItemAdjudication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Item.Detail explanationOfBenefitItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Item.Detail.SubDetail explanationOfBenefitItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Payee explanationOfBenefitPayee) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Payment explanationOfBenefitPayment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Procedure explanationOfBenefitProcedure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.ProcessNote explanationOfBenefitProcessNote) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Related explanationOfBenefitRelated) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.SupportingInfo explanationOfBenefitSupportingInfo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Total explanationOfBenefitTotal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Expression expression) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Extension extension) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, FamilyMemberHistory familyMemberHistory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, FamilyMemberHistory.Condition familyMemberHistoryCondition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Flag flag) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Goal goal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Goal.Target goalTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, GraphDefinition graphDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, GraphDefinition.Link graphDefinitionLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, GraphDefinition.Link.Target graphDefinitionLinkTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, GraphDefinition.Link.Target.Compartment graphDefinitionLinkTargetCompartment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Group group) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Group.Characteristic groupCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Group.Member groupMember) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, GuidanceResponse guidanceResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, HealthcareService healthcareService) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, HealthcareService.AvailableTime healthcareServiceAvailableTime) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, HealthcareService.Eligibility healthcareServiceEligibility) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, HealthcareService.NotAvailable healthcareServiceNotAvailable) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, HumanName humanName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Id id) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Identifier identifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImagingStudy imagingStudy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImagingStudy.Series imagingStudySeries) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImagingStudy.Series.Instance imagingStudySeriesInstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImagingStudy.Series.Performer imagingStudySeriesPerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization immunization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization.Education immunizationEducation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization.Performer immunizationPerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization.ProtocolApplied immunizationProtocolApplied) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization.Reaction immunizationReaction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImmunizationEvaluation immunizationEvaluation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImmunizationRecommendation immunizationRecommendation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImmunizationRecommendation.Recommendation immunizationRecommendationRecommendation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImmunizationRecommendation.Recommendation.DateCriterion immunizationRecommendationRecommendationDateCriterion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide implementationGuide) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition implementationGuideDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Grouping implementationGuideDefinitionGrouping) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Page implementationGuideDefinitionPage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Parameter implementationGuideDefinitionParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Resource implementationGuideDefinitionResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Template implementationGuideDefinitionTemplate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.DependsOn implementationGuideDependsOn) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Global implementationGuideGlobal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Manifest implementationGuideManifest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Manifest.Page implementationGuideManifestPage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Manifest.Resource implementationGuideManifestResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Instant instant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan insurancePlan) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Contact insurancePlanContact) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Coverage insurancePlanCoverage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Coverage.Benefit insurancePlanCoverageBenefit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Coverage.Benefit.Limit insurancePlanCoverageBenefitLimit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan insurancePlanPlan) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan.GeneralCost insurancePlanPlanGeneralCost) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan.SpecificCost insurancePlanPlanSpecificCost) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan.SpecificCost.Benefit insurancePlanPlanSpecificCostBenefit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan.SpecificCost.Benefit.Cost insurancePlanPlanSpecificCostBenefitCost) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Integer integer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Invoice invoice) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Invoice.LineItem invoiceLineItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Invoice.LineItem.PriceComponent invoiceLineItemPriceComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Invoice.Participant invoiceParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Library library) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Linkage linkage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Linkage.Item linkageItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, List list) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, List.Entry listEntry) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Location location) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Location.HoursOfOperation locationHoursOfOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Location.Position locationPosition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Markdown markdown) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MarketingStatus marketingStatus) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure measure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.Group measureGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.Group.Population measureGroupPopulation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.Group.Stratifier measureGroupStratifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.Group.Stratifier.Component measureGroupStratifierComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.SupplementalData measureSupplementalData) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport measureReport) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group measureReportGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Population measureReportGroupPopulation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Stratifier measureReportGroupStratifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Stratifier.Stratum measureReportGroupStratifierStratum) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Stratifier.Stratum.Component measureReportGroupStratifierStratumComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Stratifier.Stratum.Population measureReportGroupStratifierStratumPopulation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Media media) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Medication medication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Medication.Batch medicationBatch) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Medication.Ingredient medicationIngredient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationAdministration medicationAdministration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationAdministration.Dosage medicationAdministrationDosage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationAdministration.Performer medicationAdministrationPerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationDispense medicationDispense) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationDispense.Performer medicationDispensePerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationDispense.Substitution medicationDispenseSubstitution) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge medicationKnowledge) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.AdministrationGuidelines medicationKnowledgeAdministrationGuidelines) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.AdministrationGuidelines.Dosage medicationKnowledgeAdministrationGuidelinesDosage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.AdministrationGuidelines.PatientCharacteristics medicationKnowledgeAdministrationGuidelinesPatientCharacteristics) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Cost medicationKnowledgeCost) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.DrugCharacteristic medicationKnowledgeDrugCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Ingredient medicationKnowledgeIngredient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Kinetics medicationKnowledgeKinetics) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.MedicineClassification medicationKnowledgeMedicineClassification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.MonitoringProgram medicationKnowledgeMonitoringProgram) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Monograph medicationKnowledgeMonograph) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Packaging medicationKnowledgePackaging) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Regulatory medicationKnowledgeRegulatory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Regulatory.MaxDispense medicationKnowledgeRegulatoryMaxDispense) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Regulatory.Schedule medicationKnowledgeRegulatorySchedule) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Regulatory.Substitution medicationKnowledgeRegulatorySubstitution) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.RelatedMedicationKnowledge medicationKnowledgeRelatedMedicationKnowledge) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationRequest medicationRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationRequest.DispenseRequest medicationRequestDispenseRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationRequest.DispenseRequest.InitialFill medicationRequestDispenseRequestInitialFill) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationRequest.Substitution medicationRequestSubstitution) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationStatement medicationStatement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct medicinalProduct) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.ManufacturingBusinessOperation medicinalProductManufacturingBusinessOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.Name medicinalProductName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.Name.CountryLanguage medicinalProductNameCountryLanguage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.Name.NamePart medicinalProductNameNamePart) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.SpecialDesignation medicinalProductSpecialDesignation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductAuthorization medicinalProductAuthorization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductAuthorization.JurisdictionalAuthorization medicinalProductAuthorizationJurisdictionalAuthorization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductAuthorization.Procedure medicinalProductAuthorizationProcedure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductContraindication medicinalProductContraindication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductContraindication.OtherTherapy medicinalProductContraindicationOtherTherapy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIndication medicinalProductIndication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIndication.OtherTherapy medicinalProductIndicationOtherTherapy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient medicinalProductIngredient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient.SpecifiedSubstance medicinalProductIngredientSpecifiedSubstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient.SpecifiedSubstance.Strength medicinalProductIngredientSpecifiedSubstanceStrength) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient.SpecifiedSubstance.Strength.ReferenceStrength medicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrength) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient.Substance medicinalProductIngredientSubstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductInteraction medicinalProductInteraction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductInteraction.Interactant medicinalProductInteractionInteractant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductManufactured medicinalProductManufactured) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPackaged medicinalProductPackaged) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPackaged.BatchIdentifier medicinalProductPackagedBatchIdentifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPackaged.PackageItem medicinalProductPackagedPackageItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical medicinalProductPharmaceutical) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical.Characteristics medicinalProductPharmaceuticalCharacteristics) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical.RouteOfAdministration medicinalProductPharmaceuticalRouteOfAdministration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical.RouteOfAdministration.TargetSpecies medicinalProductPharmaceuticalRouteOfAdministrationTargetSpecies) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical.RouteOfAdministration.TargetSpecies.WithdrawalPeriod medicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriod) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductUndesirableEffect medicinalProductUndesirableEffect) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageDefinition messageDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageDefinition.AllowedResponse messageDefinitionAllowedResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageDefinition.Focus messageDefinitionFocus) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageHeader messageHeader) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageHeader.Destination messageHeaderDestination) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageHeader.Response messageHeaderResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageHeader.Source messageHeaderSource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Meta meta) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MetadataResource metadataResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence molecularSequence) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.Quality molecularSequenceQuality) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.Quality.Roc molecularSequenceQualityRoc) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.ReferenceSeq molecularSequenceReferenceSeq) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.Repository molecularSequenceRepository) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.StructureVariant molecularSequenceStructureVariant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.StructureVariant.Inner molecularSequenceStructureVariantInner) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.StructureVariant.Outer molecularSequenceStructureVariantOuter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.Variant molecularSequenceVariant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Money money) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, MoneyQuantity moneyQuantity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, NamingSystem namingSystem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, NamingSystem.UniqueId namingSystemUniqueId) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Narrative narrative) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder nutritionOrder) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.EnteralFormula nutritionOrderEnteralFormula) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.EnteralFormula.Administration nutritionOrderEnteralFormulaAdministration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.OralDiet nutritionOrderOralDiet) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.OralDiet.Nutrient nutritionOrderOralDietNutrient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.OralDiet.Texture nutritionOrderOralDietTexture) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.Supplement nutritionOrderSupplement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Observation observation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Observation.Component observationComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Observation.ReferenceRange observationReferenceRange) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ObservationDefinition observationDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ObservationDefinition.QualifiedInterval observationDefinitionQualifiedInterval) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ObservationDefinition.QuantitativeDetails observationDefinitionQuantitativeDetails) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Oid oid) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition operationDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition.Overload operationDefinitionOverload) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition.Parameter operationDefinitionParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition.Parameter.Binding operationDefinitionParameterBinding) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition.Parameter.ReferencedFrom operationDefinitionParameterReferencedFrom) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationOutcome operationOutcome) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationOutcome.Issue operationOutcomeIssue) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Organization organization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Organization.Contact organizationContact) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, OrganizationAffiliation organizationAffiliation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ParameterDefinition parameterDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Parameters parameters) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Parameters.Parameter parametersParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Patient patient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Patient.Communication patientCommunication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Patient.Contact patientContact) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Patient.Link patientLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PaymentNotice paymentNotice) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PaymentReconciliation paymentReconciliation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PaymentReconciliation.Detail paymentReconciliationDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PaymentReconciliation.ProcessNote paymentReconciliationProcessNote) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Period period) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Person person) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Person.Link personLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition planDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action planDefinitionAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action.Condition planDefinitionActionCondition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action.DynamicValue planDefinitionActionDynamicValue) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action.Participant planDefinitionActionParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action.RelatedAction planDefinitionActionRelatedAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Goal planDefinitionGoal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Goal.Target planDefinitionGoalTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Population population) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PositiveInt positiveInt) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Practitioner practitioner) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Practitioner.Qualification practitionerQualification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PractitionerRole practitionerRole) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PractitionerRole.AvailableTime practitionerRoleAvailableTime) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, PractitionerRole.NotAvailable practitionerRoleNotAvailable) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Procedure procedure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Procedure.FocalDevice procedureFocalDevice) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Procedure.Performer procedurePerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ProdCharacteristic prodCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ProductShelfLife productShelfLife) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Provenance provenance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Provenance.Agent provenanceAgent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Provenance.Entity provenanceEntity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Quantity quantity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire questionnaire) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire.Item questionnaireItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire.Item.AnswerOption questionnaireItemAnswerOption) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire.Item.EnableWhen questionnaireItemEnableWhen) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire.Item.Initial questionnaireItemInitial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, QuestionnaireResponse questionnaireResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, QuestionnaireResponse.Item questionnaireResponseItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, QuestionnaireResponse.Item.Answer questionnaireResponseItemAnswer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Range range) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Ratio ratio) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Reference reference) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RelatedArtifact relatedArtifact) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RelatedPerson relatedPerson) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RelatedPerson.Communication relatedPersonCommunication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RequestGroup requestGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RequestGroup.Action requestGroupAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RequestGroup.Action.Condition requestGroupActionCondition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RequestGroup.Action.RelatedAction requestGroupActionRelatedAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchDefinition researchDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchElementDefinition researchElementDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchElementDefinition.Characteristic researchElementDefinitionCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchStudy researchStudy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchStudy.Arm researchStudyArm) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchStudy.Objective researchStudyObjective) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchSubject researchSubject) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Resource resource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskAssessment riskAssessment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskAssessment.Prediction riskAssessmentPrediction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis riskEvidenceSynthesis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.Certainty riskEvidenceSynthesisCertainty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.Certainty.CertaintySubcomponent riskEvidenceSynthesisCertaintyCertaintySubcomponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.RiskEstimate riskEvidenceSynthesisRiskEstimate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.RiskEstimate.PrecisionEstimate riskEvidenceSynthesisRiskEstimatePrecisionEstimate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.SampleSize riskEvidenceSynthesisSampleSize) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SampledData sampledData) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Schedule schedule) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SearchParameter searchParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SearchParameter.Component searchParameterComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ServiceRequest serviceRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Signature signature) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SimpleQuantity simpleQuantity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Slot slot) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Specimen specimen) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Specimen.Collection specimenCollection) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Specimen.Container specimenContainer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Specimen.Processing specimenProcessing) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition specimenDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition.TypeTested specimenDefinitionTypeTested) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition.TypeTested.Container specimenDefinitionTypeTestedContainer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition.TypeTested.Container.Additive specimenDefinitionTypeTestedContainerAdditive) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition.TypeTested.Handling specimenDefinitionTypeTestedHandling) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, String string) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition structureDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition.Context structureDefinitionContext) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition.Differential structureDefinitionDifferential) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition.Mapping structureDefinitionMapping) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition.Snapshot structureDefinitionSnapshot) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap structureMap) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group structureMapGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Input structureMapGroupInput) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule structureMapGroupRule) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule.Dependent structureMapGroupRuleDependent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule.Source structureMapGroupRuleSource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule.Target structureMapGroupRuleTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule.Target.Parameter structureMapGroupRuleTargetParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Structure structureMapStructure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Subscription subscription) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Subscription.Channel subscriptionChannel) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Substance substance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Substance.Ingredient substanceIngredient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Substance.Instance substanceInstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceAmount substanceAmount) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceAmount.ReferenceRange substanceAmountReferenceRange) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceNucleicAcid substanceNucleicAcid) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceNucleicAcid.Subunit substanceNucleicAcidSubunit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceNucleicAcid.Subunit.Linkage substanceNucleicAcidSubunitLinkage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceNucleicAcid.Subunit.Sugar substanceNucleicAcidSubunitSugar) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer substancePolymer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.MonomerSet substancePolymerMonomerSet) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.MonomerSet.StartingMaterial substancePolymerMonomerSetStartingMaterial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.Repeat substancePolymerRepeat) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.Repeat.RepeatUnit substancePolymerRepeatRepeatUnit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.Repeat.RepeatUnit.DegreeOfPolymerisation substancePolymerRepeatRepeatUnitDegreeOfPolymerisation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.Repeat.RepeatUnit.StructuralRepresentation substancePolymerRepeatRepeatUnitStructuralRepresentation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceProtein substanceProtein) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceProtein.Subunit substanceProteinSubunit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation substanceReferenceInformation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation.Classification substanceReferenceInformationClassification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation.Gene substanceReferenceInformationGene) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation.GeneElement substanceReferenceInformationGeneElement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation.Target substanceReferenceInformationTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial substanceSourceMaterial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.FractionDescription substanceSourceMaterialFractionDescription) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.Organism substanceSourceMaterialOrganism) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.Organism.Author substanceSourceMaterialOrganismAuthor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.Organism.Hybrid substanceSourceMaterialOrganismHybrid) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.Organism.OrganismGeneral substanceSourceMaterialOrganismOrganismGeneral) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.PartDescription substanceSourceMaterialPartDescription) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification substanceSpecification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Code substanceSpecificationCode) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Moiety substanceSpecificationMoiety) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Name substanceSpecificationName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Name.Official substanceSpecificationNameOfficial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Property substanceSpecificationProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Relationship substanceSpecificationRelationship) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Structure substanceSpecificationStructure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Structure.Isotope substanceSpecificationStructureIsotope) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Structure.Isotope.MolecularWeight substanceSpecificationStructureIsotopeMolecularWeight) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Structure.Representation substanceSpecificationStructureRepresentation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SupplyDelivery supplyDelivery) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SupplyDelivery.SuppliedItem supplyDeliverySuppliedItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SupplyRequest supplyRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, SupplyRequest.Parameter supplyRequestParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Task task) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Task.Input taskInput) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Task.Output taskOutput) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Task.Restriction taskRestriction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities terminologyCapabilities) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Closure terminologyCapabilitiesClosure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.CodeSystem terminologyCapabilitiesCodeSystem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.CodeSystem.Version terminologyCapabilitiesCodeSystemVersion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.CodeSystem.Version.Filter terminologyCapabilitiesCodeSystemVersionFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Expansion terminologyCapabilitiesExpansion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Expansion.Parameter terminologyCapabilitiesExpansionParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Implementation terminologyCapabilitiesImplementation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Software terminologyCapabilitiesSoftware) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Translation terminologyCapabilitiesTranslation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.ValidateCode terminologyCapabilitiesValidateCode) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport testReport) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Participant testReportParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Setup testReportSetup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Setup.Action testReportSetupAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Setup.Action.Assert testReportSetupActionAssert) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Setup.Action.Operation testReportSetupActionOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Teardown testReportTeardown) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Teardown.Action testReportTeardownAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Test testReportTest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Test.Action testReportTestAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript testScript) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Destination testScriptDestination) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Fixture testScriptFixture) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Metadata testScriptMetadata) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Metadata.Capability testScriptMetadataCapability) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Metadata.Link testScriptMetadataLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Origin testScriptOrigin) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup testScriptSetup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup.Action testScriptSetupAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup.Action.Assert testScriptSetupActionAssert) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup.Action.Operation testScriptSetupActionOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup.Action.Operation.RequestHeader testScriptSetupActionOperationRequestHeader) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Teardown testScriptTeardown) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Teardown.Action testScriptTeardownAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Test testScriptTest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Test.Action testScriptTestAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Variable testScriptVariable) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Time time) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Timing timing) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Timing.Repeat timingRepeat) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, TriggerDefinition triggerDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, UnsignedInt unsignedInt) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Uri uri) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Url url) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, UsageContext usageContext) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, Uuid uuid) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet valueSet) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose valueSetCompose) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose.Include valueSetComposeInclude) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose.Include.Concept valueSetComposeIncludeConcept) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose.Include.Concept.Designation valueSetComposeIncludeConceptDesignation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose.Include.Filter valueSetComposeIncludeFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Expansion valueSetExpansion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Expansion.Contains valueSetExpansionContains) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Expansion.Parameter valueSetExpansionParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, VerificationResult verificationResult) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, VerificationResult.Attestation verificationResultAttestation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, VerificationResult.PrimarySource verificationResultPrimarySource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, VerificationResult.Validator verificationResultValidator) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, VisionPrescription visionPrescription) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, VisionPrescription.LensSpecification visionPrescriptionLensSpecification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, VisionPrescription.LensSpecification.Prism visionPrescriptionLensSpecificationPrism) {
        return true;
    }

    @Override
    public void visit(java.lang.String elementName, byte[] value) {
    }

    @Override
    public void visit(java.lang.String elementName, BigDecimal value) {
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Boolean value) {
    }

    @Override
    public void visit(java.lang.String elementName, org.w3c.dom.Element value) {
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Integer value) {
    }

    @Override
    public void visit(java.lang.String elementName, LocalDate value) {
    }

    @Override
    public void visit(java.lang.String elementName, LocalTime value) {
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.String value) {
    }

    @Override
    public void visit(java.lang.String elementName, Year value) {
    }

    @Override
    public void visit(java.lang.String elementName, YearMonth value) {
    }

    @Override
    public void visit(java.lang.String elementName, ZonedDateTime value) {
    }
}
