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
    public void visitStart(java.lang.String elementName, int elementIndex, Element element) {
    }

    @Override
    public void visitStart(java.lang.String elementName, int elementIndex, Resource resource) {
    }

    @Override
    public void visitStart(java.lang.String elementName, java.util.List<? extends Visitable> visitables, Class<?> type) {
    }

    @Override
    public void visitEnd(java.lang.String elementName, int elementIndex, Element element) {
    }

    @Override
    public void visitEnd(java.lang.String elementName, int elementIndex, Resource resource) {
    }

    @Override
    public void visitEnd(java.lang.String elementName, java.util.List<? extends Visitable> visitables, Class<?> type) {
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Account account) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Account.Coverage accountCoverage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Account.Guarantor accountGuarantor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ActivityDefinition activityDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ActivityDefinition.DynamicValue activityDefinitionDynamicValue) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ActivityDefinition.Participant activityDefinitionParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Address address) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AdverseEvent adverseEvent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AdverseEvent.SuspectEntity adverseEventSuspectEntity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AdverseEvent.SuspectEntity.Causality adverseEventSuspectEntityCausality) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Age age) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AllergyIntolerance allergyIntolerance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AllergyIntolerance.Reaction allergyIntoleranceReaction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Annotation annotation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Appointment appointment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Appointment.Participant appointmentParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AppointmentResponse appointmentResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Attachment attachment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AuditEvent auditEvent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AuditEvent.Agent auditEventAgent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AuditEvent.Agent.Network auditEventAgentNetwork) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AuditEvent.Entity auditEventEntity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AuditEvent.Entity.Detail auditEventEntityDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AuditEvent.Source auditEventSource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BackboneElement backboneElement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Base64Binary base64Binary) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Basic basic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Binary binary) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BiologicallyDerivedProduct biologicallyDerivedProduct) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BiologicallyDerivedProduct.Collection biologicallyDerivedProductCollection) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BiologicallyDerivedProduct.Manipulation biologicallyDerivedProductManipulation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BiologicallyDerivedProduct.Processing biologicallyDerivedProductProcessing) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BiologicallyDerivedProduct.Storage biologicallyDerivedProductStorage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BodyStructure bodyStructure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Boolean _boolean) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Bundle bundle) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Bundle.Entry bundleEntry) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Bundle.Entry.Request bundleEntryRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Bundle.Entry.Response bundleEntryResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Bundle.Entry.Search bundleEntrySearch) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Bundle.Link bundleLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement capabilityStatement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Document capabilityStatementDocument) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Implementation capabilityStatementImplementation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Messaging capabilityStatementMessaging) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Messaging.Endpoint capabilityStatementMessagingEndpoint) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Messaging.SupportedMessage capabilityStatementMessagingSupportedMessage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Rest capabilityStatementRest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Rest.Interaction capabilityStatementRestInteraction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Rest.Resource capabilityStatementRestResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Rest.Resource.Interaction capabilityStatementRestResourceInteraction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Rest.Resource.Operation capabilityStatementRestResourceOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Rest.Resource.SearchParam capabilityStatementRestResourceSearchParam) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Rest.Security capabilityStatementRestSecurity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement.Software capabilityStatementSoftware) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CarePlan carePlan) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CarePlan.Activity carePlanActivity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CarePlan.Activity.Detail carePlanActivityDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CareTeam careTeam) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CareTeam.Participant careTeamParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CatalogEntry catalogEntry) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CatalogEntry.RelatedEntry catalogEntryRelatedEntry) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItem chargeItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItem.Performer chargeItemPerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItemDefinition chargeItemDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItemDefinition.Applicability chargeItemDefinitionApplicability) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItemDefinition.PropertyGroup chargeItemDefinitionPropertyGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItemDefinition.PropertyGroup.PriceComponent chargeItemDefinitionPropertyGroupPriceComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim claim) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.Accident claimAccident) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.CareTeam claimCareTeam) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.Diagnosis claimDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.Insurance claimInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.Item claimItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.Item.Detail claimItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.Item.Detail.SubDetail claimItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.Payee claimPayee) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.Procedure claimProcedure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.Related claimRelated) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim.SupportingInfo claimSupportingInfo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse claimResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.AddItem claimResponseAddItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.AddItem.Detail claimResponseAddItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.AddItem.Detail.SubDetail claimResponseAddItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.Error claimResponseError) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.Insurance claimResponseInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.Item claimResponseItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.Item.Adjudication claimResponseItemAdjudication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.Item.Detail claimResponseItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.Item.Detail.SubDetail claimResponseItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.Payment claimResponsePayment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.ProcessNote claimResponseProcessNote) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse.Total claimResponseTotal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClinicalImpression clinicalImpression) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClinicalImpression.Finding clinicalImpressionFinding) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClinicalImpression.Investigation clinicalImpressionInvestigation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeSystem codeSystem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeSystem.Concept codeSystemConcept) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeSystem.Concept.Designation codeSystemConceptDesignation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeSystem.Concept.Property codeSystemConceptProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeSystem.Filter codeSystemFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeSystem.Property codeSystemProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeableConcept codeableConcept) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coding coding) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Communication communication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Communication.Payload communicationPayload) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CommunicationRequest communicationRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CommunicationRequest.Payload communicationRequestPayload) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CompartmentDefinition compartmentDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CompartmentDefinition.Resource compartmentDefinitionResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Composition composition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Composition.Attester compositionAttester) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Composition.Event compositionEvent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Composition.RelatesTo compositionRelatesTo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Composition.Section compositionSection) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ConceptMap conceptMap) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ConceptMap.Group conceptMapGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ConceptMap.Group.Element conceptMapGroupElement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ConceptMap.Group.Element.Target conceptMapGroupElementTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ConceptMap.Group.Element.Target.DependsOn conceptMapGroupElementTargetDependsOn) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ConceptMap.Group.Unmapped conceptMapGroupUnmapped) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Condition condition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Condition.Evidence conditionEvidence) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Condition.Stage conditionStage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Consent consent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Consent.Policy consentPolicy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Consent.Provision consentProvision) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Consent.Provision.Actor consentProvisionActor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Consent.Provision.Data consentProvisionData) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Consent.Verification consentVerification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ContactDetail contactDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ContactPoint contactPoint) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract contract) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.ContentDefinition contractContentDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Friendly contractFriendly) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Legal contractLegal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Rule contractRule) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Signer contractSigner) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term contractTerm) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term.Action contractTermAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term.Action.Subject contractTermActionSubject) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term.Asset contractTermAsset) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term.Asset.Context contractTermAssetContext) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term.Asset.ValuedItem contractTermAssetValuedItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term.Offer contractTermOffer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term.Offer.Answer contractTermOfferAnswer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term.Offer.Party contractTermOfferParty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract.Term.SecurityLabel contractTermSecurityLabel) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contributor contributor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Count count) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coverage coverage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coverage.Class coverageClass) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coverage.CostToBeneficiary coverageCostToBeneficiary) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coverage.CostToBeneficiary.Exception coverageCostToBeneficiaryException) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityRequest coverageEligibilityRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityRequest.Insurance coverageEligibilityRequestInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityRequest.Item coverageEligibilityRequestItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityRequest.Item.Diagnosis coverageEligibilityRequestItemDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityRequest.SupportingInfo coverageEligibilityRequestSupportingInfo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityResponse coverageEligibilityResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityResponse.Error coverageEligibilityResponseError) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityResponse.Insurance coverageEligibilityResponseInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityResponse.Insurance.Item coverageEligibilityResponseInsuranceItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityResponse.Insurance.Item.Benefit coverageEligibilityResponseInsuranceItemBenefit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DataRequirement dataRequirement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DataRequirement.CodeFilter dataRequirementCodeFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DataRequirement.DateFilter dataRequirementDateFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DataRequirement.Sort dataRequirementSort) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Date date) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DateTime dateTime) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Decimal decimal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DetectedIssue detectedIssue) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DetectedIssue.Evidence detectedIssueEvidence) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DetectedIssue.Mitigation detectedIssueMitigation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Device device) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Device.DeviceName deviceDeviceName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Device.Property deviceProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Device.Specialization deviceSpecialization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Device.UdiCarrier deviceUdiCarrier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Device.Version deviceVersion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition deviceDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition.Capability deviceDefinitionCapability) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition.DeviceName deviceDefinitionDeviceName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition.Material deviceDefinitionMaterial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition.Property deviceDefinitionProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition.Specialization deviceDefinitionSpecialization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition.UdiDeviceIdentifier deviceDefinitionUdiDeviceIdentifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceMetric deviceMetric) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceMetric.Calibration deviceMetricCalibration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceRequest deviceRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceRequest.Parameter deviceRequestParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceUseStatement deviceUseStatement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DiagnosticReport diagnosticReport) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DiagnosticReport.Media diagnosticReportMedia) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Distance distance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentManifest documentManifest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentManifest.Related documentManifestRelated) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentReference documentReference) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentReference.Content documentReferenceContent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentReference.Context documentReferenceContext) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentReference.RelatesTo documentReferenceRelatesTo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DomainResource domainResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Dosage dosage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Dosage.DoseAndRate dosageDoseAndRate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Duration duration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EffectEvidenceSynthesis effectEvidenceSynthesis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EffectEvidenceSynthesis.Certainty effectEvidenceSynthesisCertainty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EffectEvidenceSynthesis.Certainty.CertaintySubcomponent effectEvidenceSynthesisCertaintyCertaintySubcomponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EffectEvidenceSynthesis.EffectEstimate effectEvidenceSynthesisEffectEstimate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EffectEvidenceSynthesis.EffectEstimate.PrecisionEstimate effectEvidenceSynthesisEffectEstimatePrecisionEstimate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EffectEvidenceSynthesis.ResultsByExposure effectEvidenceSynthesisResultsByExposure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EffectEvidenceSynthesis.SampleSize effectEvidenceSynthesisSampleSize) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Element element) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition elementDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition.Base elementDefinitionBase) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition.Binding elementDefinitionBinding) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition.Constraint elementDefinitionConstraint) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition.Example elementDefinitionExample) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition.Mapping elementDefinitionMapping) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition.Slicing elementDefinitionSlicing) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition.Slicing.Discriminator elementDefinitionSlicingDiscriminator) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition.Type elementDefinitionType) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Encounter encounter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Encounter.ClassHistory encounterClassHistory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Encounter.Diagnosis encounterDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Encounter.Hospitalization encounterHospitalization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Encounter.Location encounterLocation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Encounter.Participant encounterParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Encounter.StatusHistory encounterStatusHistory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Endpoint endpoint) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EnrollmentRequest enrollmentRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EnrollmentResponse enrollmentResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EpisodeOfCare episodeOfCare) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EpisodeOfCare.Diagnosis episodeOfCareDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EpisodeOfCare.StatusHistory episodeOfCareStatusHistory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EventDefinition eventDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Evidence evidence) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EvidenceVariable evidenceVariable) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EvidenceVariable.Characteristic evidenceVariableCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario exampleScenario) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario.Actor exampleScenarioActor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario.Instance exampleScenarioInstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario.Instance.ContainedInstance exampleScenarioInstanceContainedInstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario.Instance.Version exampleScenarioInstanceVersion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario.Process exampleScenarioProcess) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario.Process.Step exampleScenarioProcessStep) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario.Process.Step.Alternative exampleScenarioProcessStepAlternative) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario.Process.Step.Operation exampleScenarioProcessStepOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit explanationOfBenefit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Accident explanationOfBenefitAccident) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.AddItem explanationOfBenefitAddItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.AddItem.Detail explanationOfBenefitAddItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.AddItem.Detail.SubDetail explanationOfBenefitAddItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.BenefitBalance explanationOfBenefitBenefitBalance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.BenefitBalance.Financial explanationOfBenefitBenefitBalanceFinancial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.CareTeam explanationOfBenefitCareTeam) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Diagnosis explanationOfBenefitDiagnosis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Insurance explanationOfBenefitInsurance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Item explanationOfBenefitItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Item.Adjudication explanationOfBenefitItemAdjudication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Item.Detail explanationOfBenefitItemDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Item.Detail.SubDetail explanationOfBenefitItemDetailSubDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Payee explanationOfBenefitPayee) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Payment explanationOfBenefitPayment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Procedure explanationOfBenefitProcedure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.ProcessNote explanationOfBenefitProcessNote) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Related explanationOfBenefitRelated) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.SupportingInfo explanationOfBenefitSupportingInfo) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit.Total explanationOfBenefitTotal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Expression expression) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Extension extension) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, FamilyMemberHistory familyMemberHistory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, FamilyMemberHistory.Condition familyMemberHistoryCondition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Flag flag) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Goal goal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Goal.Target goalTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, GraphDefinition graphDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, GraphDefinition.Link graphDefinitionLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, GraphDefinition.Link.Target graphDefinitionLinkTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, GraphDefinition.Link.Target.Compartment graphDefinitionLinkTargetCompartment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Group group) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Group.Characteristic groupCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Group.Member groupMember) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, GuidanceResponse guidanceResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HealthcareService healthcareService) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HealthcareService.AvailableTime healthcareServiceAvailableTime) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HealthcareService.Eligibility healthcareServiceEligibility) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HealthcareService.NotAvailable healthcareServiceNotAvailable) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HumanName humanName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Identifier identifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImagingStudy imagingStudy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImagingStudy.Series imagingStudySeries) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImagingStudy.Series.Instance imagingStudySeriesInstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImagingStudy.Series.Performer imagingStudySeriesPerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Immunization immunization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Immunization.Education immunizationEducation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Immunization.Performer immunizationPerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Immunization.ProtocolApplied immunizationProtocolApplied) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Immunization.Reaction immunizationReaction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImmunizationEvaluation immunizationEvaluation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImmunizationRecommendation immunizationRecommendation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImmunizationRecommendation.Recommendation immunizationRecommendationRecommendation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImmunizationRecommendation.Recommendation.DateCriterion immunizationRecommendationRecommendationDateCriterion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide implementationGuide) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Definition implementationGuideDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Definition.Grouping implementationGuideDefinitionGrouping) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Definition.Page implementationGuideDefinitionPage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Definition.Parameter implementationGuideDefinitionParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Definition.Resource implementationGuideDefinitionResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Definition.Template implementationGuideDefinitionTemplate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.DependsOn implementationGuideDependsOn) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Global implementationGuideGlobal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Manifest implementationGuideManifest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Manifest.Page implementationGuideManifestPage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide.Manifest.Resource implementationGuideManifestResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Instant instant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan insurancePlan) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan.Contact insurancePlanContact) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan.Coverage insurancePlanCoverage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan.Coverage.Benefit insurancePlanCoverageBenefit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan.Coverage.Benefit.Limit insurancePlanCoverageBenefitLimit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan.Plan insurancePlanPlan) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan.Plan.GeneralCost insurancePlanPlanGeneralCost) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan.Plan.SpecificCost insurancePlanPlanSpecificCost) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan.Plan.SpecificCost.Benefit insurancePlanPlanSpecificCostBenefit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan.Plan.SpecificCost.Benefit.Cost insurancePlanPlanSpecificCostBenefitCost) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Integer integer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Invoice invoice) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Invoice.LineItem invoiceLineItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Invoice.LineItem.PriceComponent invoiceLineItemPriceComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Invoice.Participant invoiceParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Library library) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Linkage linkage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Linkage.Item linkageItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, List list) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, List.Entry listEntry) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Location location) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Location.HoursOfOperation locationHoursOfOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Location.Position locationPosition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MarketingStatus marketingStatus) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Measure measure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Measure.Group measureGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Measure.Group.Population measureGroupPopulation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Measure.Group.Stratifier measureGroupStratifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Measure.Group.Stratifier.Component measureGroupStratifierComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Measure.SupplementalData measureSupplementalData) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MeasureReport measureReport) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MeasureReport.Group measureReportGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MeasureReport.Group.Population measureReportGroupPopulation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MeasureReport.Group.Stratifier measureReportGroupStratifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MeasureReport.Group.Stratifier.Stratum measureReportGroupStratifierStratum) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MeasureReport.Group.Stratifier.Stratum.Component measureReportGroupStratifierStratumComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MeasureReport.Group.Stratifier.Stratum.Population measureReportGroupStratifierStratumPopulation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Media media) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Medication medication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Medication.Batch medicationBatch) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Medication.Ingredient medicationIngredient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationAdministration medicationAdministration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationAdministration.Dosage medicationAdministrationDosage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationAdministration.Performer medicationAdministrationPerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationDispense medicationDispense) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationDispense.Performer medicationDispensePerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationDispense.Substitution medicationDispenseSubstitution) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge medicationKnowledge) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.AdministrationGuidelines medicationKnowledgeAdministrationGuidelines) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.AdministrationGuidelines.Dosage medicationKnowledgeAdministrationGuidelinesDosage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.AdministrationGuidelines.PatientCharacteristics medicationKnowledgeAdministrationGuidelinesPatientCharacteristics) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.Cost medicationKnowledgeCost) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.DrugCharacteristic medicationKnowledgeDrugCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.Ingredient medicationKnowledgeIngredient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.Kinetics medicationKnowledgeKinetics) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.MedicineClassification medicationKnowledgeMedicineClassification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.MonitoringProgram medicationKnowledgeMonitoringProgram) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.Monograph medicationKnowledgeMonograph) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.Packaging medicationKnowledgePackaging) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.Regulatory medicationKnowledgeRegulatory) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.Regulatory.MaxDispense medicationKnowledgeRegulatoryMaxDispense) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.Regulatory.Schedule medicationKnowledgeRegulatorySchedule) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.Regulatory.Substitution medicationKnowledgeRegulatorySubstitution) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge.RelatedMedicationKnowledge medicationKnowledgeRelatedMedicationKnowledge) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationRequest medicationRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationRequest.DispenseRequest medicationRequestDispenseRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationRequest.DispenseRequest.InitialFill medicationRequestDispenseRequestInitialFill) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationRequest.Substitution medicationRequestSubstitution) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationStatement medicationStatement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProduct medicinalProduct) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProduct.ManufacturingBusinessOperation medicinalProductManufacturingBusinessOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProduct.Name medicinalProductName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProduct.Name.CountryLanguage medicinalProductNameCountryLanguage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProduct.Name.NamePart medicinalProductNameNamePart) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProduct.SpecialDesignation medicinalProductSpecialDesignation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductAuthorization medicinalProductAuthorization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductAuthorization.JurisdictionalAuthorization medicinalProductAuthorizationJurisdictionalAuthorization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductAuthorization.Procedure medicinalProductAuthorizationProcedure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductContraindication medicinalProductContraindication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductContraindication.OtherTherapy medicinalProductContraindicationOtherTherapy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductIndication medicinalProductIndication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductIndication.OtherTherapy medicinalProductIndicationOtherTherapy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductIngredient medicinalProductIngredient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductIngredient.SpecifiedSubstance medicinalProductIngredientSpecifiedSubstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductIngredient.SpecifiedSubstance.Strength medicinalProductIngredientSpecifiedSubstanceStrength) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductIngredient.SpecifiedSubstance.Strength.ReferenceStrength medicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrength) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductIngredient.Substance medicinalProductIngredientSubstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductInteraction medicinalProductInteraction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductInteraction.Interactant medicinalProductInteractionInteractant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductManufactured medicinalProductManufactured) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPackaged medicinalProductPackaged) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPackaged.BatchIdentifier medicinalProductPackagedBatchIdentifier) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPackaged.PackageItem medicinalProductPackagedPackageItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPharmaceutical medicinalProductPharmaceutical) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPharmaceutical.Characteristics medicinalProductPharmaceuticalCharacteristics) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPharmaceutical.RouteOfAdministration medicinalProductPharmaceuticalRouteOfAdministration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPharmaceutical.RouteOfAdministration.TargetSpecies medicinalProductPharmaceuticalRouteOfAdministrationTargetSpecies) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPharmaceutical.RouteOfAdministration.TargetSpecies.WithdrawalPeriod medicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriod) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductUndesirableEffect medicinalProductUndesirableEffect) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageDefinition messageDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageDefinition.AllowedResponse messageDefinitionAllowedResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageDefinition.Focus messageDefinitionFocus) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageHeader messageHeader) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageHeader.Destination messageHeaderDestination) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageHeader.Response messageHeaderResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageHeader.Source messageHeaderSource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Meta meta) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MetadataResource metadataResource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence molecularSequence) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence.Quality molecularSequenceQuality) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence.Quality.Roc molecularSequenceQualityRoc) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence.ReferenceSeq molecularSequenceReferenceSeq) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence.Repository molecularSequenceRepository) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence.StructureVariant molecularSequenceStructureVariant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence.StructureVariant.Inner molecularSequenceStructureVariantInner) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence.StructureVariant.Outer molecularSequenceStructureVariantOuter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence.Variant molecularSequenceVariant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Money money) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MoneyQuantity moneyQuantity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NamingSystem namingSystem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NamingSystem.UniqueId namingSystemUniqueId) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Narrative narrative) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder nutritionOrder) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder.EnteralFormula nutritionOrderEnteralFormula) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder.EnteralFormula.Administration nutritionOrderEnteralFormulaAdministration) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder.OralDiet nutritionOrderOralDiet) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder.OralDiet.Nutrient nutritionOrderOralDietNutrient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder.OralDiet.Texture nutritionOrderOralDietTexture) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder.Supplement nutritionOrderSupplement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Observation observation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Observation.Component observationComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Observation.ReferenceRange observationReferenceRange) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ObservationDefinition observationDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ObservationDefinition.QualifiedInterval observationDefinitionQualifiedInterval) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ObservationDefinition.QuantitativeDetails observationDefinitionQuantitativeDetails) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationDefinition operationDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationDefinition.Overload operationDefinitionOverload) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationDefinition.Parameter operationDefinitionParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationDefinition.Parameter.Binding operationDefinitionParameterBinding) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationDefinition.Parameter.ReferencedFrom operationDefinitionParameterReferencedFrom) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationOutcome operationOutcome) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationOutcome.Issue operationOutcomeIssue) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Organization organization) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Organization.Contact organizationContact) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OrganizationAffiliation organizationAffiliation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ParameterDefinition parameterDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Parameters parameters) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Parameters.Parameter parametersParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Patient patient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Patient.Communication patientCommunication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Patient.Contact patientContact) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Patient.Link patientLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PaymentNotice paymentNotice) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PaymentReconciliation paymentReconciliation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PaymentReconciliation.Detail paymentReconciliationDetail) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PaymentReconciliation.ProcessNote paymentReconciliationProcessNote) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Period period) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Person person) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Person.Link personLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition planDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition.Action planDefinitionAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition.Action.Condition planDefinitionActionCondition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition.Action.DynamicValue planDefinitionActionDynamicValue) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition.Action.Participant planDefinitionActionParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition.Action.RelatedAction planDefinitionActionRelatedAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition.Goal planDefinitionGoal) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition.Goal.Target planDefinitionGoalTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Population population) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Practitioner practitioner) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Practitioner.Qualification practitionerQualification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PractitionerRole practitionerRole) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PractitionerRole.AvailableTime practitionerRoleAvailableTime) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PractitionerRole.NotAvailable practitionerRoleNotAvailable) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Procedure procedure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Procedure.FocalDevice procedureFocalDevice) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Procedure.Performer procedurePerformer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ProdCharacteristic prodCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ProductShelfLife productShelfLife) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Provenance provenance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Provenance.Agent provenanceAgent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Provenance.Entity provenanceEntity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Quantity quantity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Questionnaire questionnaire) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Questionnaire.Item questionnaireItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Questionnaire.Item.AnswerOption questionnaireItemAnswerOption) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Questionnaire.Item.EnableWhen questionnaireItemEnableWhen) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Questionnaire.Item.Initial questionnaireItemInitial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, QuestionnaireResponse questionnaireResponse) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, QuestionnaireResponse.Item questionnaireResponseItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, QuestionnaireResponse.Item.Answer questionnaireResponseItemAnswer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Range range) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Ratio ratio) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Reference reference) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RelatedArtifact relatedArtifact) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RelatedPerson relatedPerson) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RelatedPerson.Communication relatedPersonCommunication) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RequestGroup requestGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RequestGroup.Action requestGroupAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RequestGroup.Action.Condition requestGroupActionCondition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RequestGroup.Action.RelatedAction requestGroupActionRelatedAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchDefinition researchDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchElementDefinition researchElementDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchElementDefinition.Characteristic researchElementDefinitionCharacteristic) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchStudy researchStudy) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchStudy.Arm researchStudyArm) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchStudy.Objective researchStudyObjective) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchSubject researchSubject) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Resource resource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskAssessment riskAssessment) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskAssessment.Prediction riskAssessmentPrediction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskEvidenceSynthesis riskEvidenceSynthesis) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskEvidenceSynthesis.Certainty riskEvidenceSynthesisCertainty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskEvidenceSynthesis.Certainty.CertaintySubcomponent riskEvidenceSynthesisCertaintyCertaintySubcomponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskEvidenceSynthesis.RiskEstimate riskEvidenceSynthesisRiskEstimate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskEvidenceSynthesis.RiskEstimate.PrecisionEstimate riskEvidenceSynthesisRiskEstimatePrecisionEstimate) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskEvidenceSynthesis.SampleSize riskEvidenceSynthesisSampleSize) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SampledData sampledData) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Schedule schedule) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SearchParameter searchParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SearchParameter.Component searchParameterComponent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ServiceRequest serviceRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Signature signature) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SimpleQuantity simpleQuantity) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Slot slot) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Specimen specimen) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Specimen.Collection specimenCollection) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Specimen.Container specimenContainer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Specimen.Processing specimenProcessing) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SpecimenDefinition specimenDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SpecimenDefinition.TypeTested specimenDefinitionTypeTested) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SpecimenDefinition.TypeTested.Container specimenDefinitionTypeTestedContainer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SpecimenDefinition.TypeTested.Container.Additive specimenDefinitionTypeTestedContainerAdditive) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SpecimenDefinition.TypeTested.Handling specimenDefinitionTypeTestedHandling) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, String string) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureDefinition structureDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureDefinition.Context structureDefinitionContext) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureDefinition.Differential structureDefinitionDifferential) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureDefinition.Mapping structureDefinitionMapping) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureDefinition.Snapshot structureDefinitionSnapshot) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap structureMap) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap.Group structureMapGroup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap.Group.Input structureMapGroupInput) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap.Group.Rule structureMapGroupRule) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap.Group.Rule.Dependent structureMapGroupRuleDependent) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap.Group.Rule.Source structureMapGroupRuleSource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap.Group.Rule.Target structureMapGroupRuleTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap.Group.Rule.Target.Parameter structureMapGroupRuleTargetParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap.Structure structureMapStructure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Subscription subscription) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Subscription.Channel subscriptionChannel) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Substance substance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Substance.Ingredient substanceIngredient) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Substance.Instance substanceInstance) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceAmount substanceAmount) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceAmount.ReferenceRange substanceAmountReferenceRange) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceNucleicAcid substanceNucleicAcid) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceNucleicAcid.Subunit substanceNucleicAcidSubunit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceNucleicAcid.Subunit.Linkage substanceNucleicAcidSubunitLinkage) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceNucleicAcid.Subunit.Sugar substanceNucleicAcidSubunitSugar) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstancePolymer substancePolymer) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstancePolymer.MonomerSet substancePolymerMonomerSet) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstancePolymer.MonomerSet.StartingMaterial substancePolymerMonomerSetStartingMaterial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstancePolymer.Repeat substancePolymerRepeat) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstancePolymer.Repeat.RepeatUnit substancePolymerRepeatRepeatUnit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstancePolymer.Repeat.RepeatUnit.DegreeOfPolymerisation substancePolymerRepeatRepeatUnitDegreeOfPolymerisation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstancePolymer.Repeat.RepeatUnit.StructuralRepresentation substancePolymerRepeatRepeatUnitStructuralRepresentation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceProtein substanceProtein) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceProtein.Subunit substanceProteinSubunit) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceReferenceInformation substanceReferenceInformation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceReferenceInformation.Classification substanceReferenceInformationClassification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceReferenceInformation.Gene substanceReferenceInformationGene) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceReferenceInformation.GeneElement substanceReferenceInformationGeneElement) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceReferenceInformation.Target substanceReferenceInformationTarget) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSourceMaterial substanceSourceMaterial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSourceMaterial.FractionDescription substanceSourceMaterialFractionDescription) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSourceMaterial.Organism substanceSourceMaterialOrganism) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSourceMaterial.Organism.Author substanceSourceMaterialOrganismAuthor) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSourceMaterial.Organism.Hybrid substanceSourceMaterialOrganismHybrid) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSourceMaterial.Organism.OrganismGeneral substanceSourceMaterialOrganismOrganismGeneral) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSourceMaterial.PartDescription substanceSourceMaterialPartDescription) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification substanceSpecification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Code substanceSpecificationCode) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Moiety substanceSpecificationMoiety) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Name substanceSpecificationName) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Name.Official substanceSpecificationNameOfficial) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Property substanceSpecificationProperty) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Relationship substanceSpecificationRelationship) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Structure substanceSpecificationStructure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Structure.Isotope substanceSpecificationStructureIsotope) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Structure.Isotope.MolecularWeight substanceSpecificationStructureIsotopeMolecularWeight) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification.Structure.Representation substanceSpecificationStructureRepresentation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SupplyDelivery supplyDelivery) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SupplyDelivery.SuppliedItem supplyDeliverySuppliedItem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SupplyRequest supplyRequest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SupplyRequest.Parameter supplyRequestParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Task task) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Task.Input taskInput) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Task.Output taskOutput) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Task.Restriction taskRestriction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities terminologyCapabilities) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.Closure terminologyCapabilitiesClosure) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.CodeSystem terminologyCapabilitiesCodeSystem) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.CodeSystem.Version terminologyCapabilitiesCodeSystemVersion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.CodeSystem.Version.Filter terminologyCapabilitiesCodeSystemVersionFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.Expansion terminologyCapabilitiesExpansion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.Expansion.Parameter terminologyCapabilitiesExpansionParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.Implementation terminologyCapabilitiesImplementation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.Software terminologyCapabilitiesSoftware) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.Translation terminologyCapabilitiesTranslation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities.ValidateCode terminologyCapabilitiesValidateCode) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport testReport) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport.Participant testReportParticipant) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport.Setup testReportSetup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport.Setup.Action testReportSetupAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport.Setup.Action.Assert testReportSetupActionAssert) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport.Setup.Action.Operation testReportSetupActionOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport.Teardown testReportTeardown) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport.Teardown.Action testReportTeardownAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport.Test testReportTest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport.Test.Action testReportTestAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript testScript) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Destination testScriptDestination) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Fixture testScriptFixture) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Metadata testScriptMetadata) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Metadata.Capability testScriptMetadataCapability) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Metadata.Link testScriptMetadataLink) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Origin testScriptOrigin) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Setup testScriptSetup) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Setup.Action testScriptSetupAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Setup.Action.Assert testScriptSetupActionAssert) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Setup.Action.Operation testScriptSetupActionOperation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Setup.Action.Operation.RequestHeader testScriptSetupActionOperationRequestHeader) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Teardown testScriptTeardown) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Teardown.Action testScriptTeardownAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Test testScriptTest) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Test.Action testScriptTestAction) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript.Variable testScriptVariable) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Time time) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Timing timing) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Timing.Repeat timingRepeat) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TriggerDefinition triggerDefinition) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Uri uri) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, UsageContext usageContext) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet valueSet) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet.Compose valueSetCompose) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet.Compose.Include valueSetComposeInclude) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet.Compose.Include.Concept valueSetComposeIncludeConcept) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet.Compose.Include.Concept.Designation valueSetComposeIncludeConceptDesignation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet.Compose.Include.Filter valueSetComposeIncludeFilter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet.Expansion valueSetExpansion) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet.Expansion.Contains valueSetExpansionContains) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet.Expansion.Parameter valueSetExpansionParameter) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VerificationResult verificationResult) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VerificationResult.Attestation verificationResultAttestation) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VerificationResult.PrimarySource verificationResultPrimarySource) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VerificationResult.Validator verificationResultValidator) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VisionPrescription visionPrescription) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VisionPrescription.LensSpecification visionPrescriptionLensSpecification) {
        return true;
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VisionPrescription.LensSpecification.Prism visionPrescriptionLensSpecificationPrism) {
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
