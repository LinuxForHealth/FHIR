/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;

import com.ibm.watsonhealth.fhir.model.resource.*;
import com.ibm.watsonhealth.fhir.model.type.*;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.visitor.Visitable;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

public class CollectingVisitor<T> implements Visitor {
    protected final java.util.List<T> result = new ArrayList<>();
    protected final Class<T> type;

    public CollectingVisitor(Class<T> type) {
        this.type = type;
    }

    protected boolean collect(Object object) {
        if (type.isInstance(object)) {
            result.add(type.cast(object));
        }
        return true;
    }

    public java.util.List<T> getResult() {
        return Collections.unmodifiableList(result);
    }

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
        return collect(account);
    }

    @Override
    public boolean visit(java.lang.String elementName, Account.Coverage accountCoverage) {
        return collect(accountCoverage);
    }

    @Override
    public boolean visit(java.lang.String elementName, Account.Guarantor accountGuarantor) {
        return collect(accountGuarantor);
    }

    @Override
    public boolean visit(java.lang.String elementName, ActivityDefinition activityDefinition) {
        return collect(activityDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, ActivityDefinition.DynamicValue activityDefinitionDynamicValue) {
        return collect(activityDefinitionDynamicValue);
    }

    @Override
    public boolean visit(java.lang.String elementName, ActivityDefinition.Participant activityDefinitionParticipant) {
        return collect(activityDefinitionParticipant);
    }

    @Override
    public boolean visit(java.lang.String elementName, Address address) {
        return collect(address);
    }

    @Override
    public boolean visit(java.lang.String elementName, AdverseEvent adverseEvent) {
        return collect(adverseEvent);
    }

    @Override
    public boolean visit(java.lang.String elementName, AdverseEvent.SuspectEntity adverseEventSuspectEntity) {
        return collect(adverseEventSuspectEntity);
    }

    @Override
    public boolean visit(java.lang.String elementName, AdverseEvent.SuspectEntity.Causality adverseEventSuspectEntityCausality) {
        return collect(adverseEventSuspectEntityCausality);
    }

    @Override
    public boolean visit(java.lang.String elementName, Age age) {
        return collect(age);
    }

    @Override
    public boolean visit(java.lang.String elementName, AllergyIntolerance allergyIntolerance) {
        return collect(allergyIntolerance);
    }

    @Override
    public boolean visit(java.lang.String elementName, AllergyIntolerance.Reaction allergyIntoleranceReaction) {
        return collect(allergyIntoleranceReaction);
    }

    @Override
    public boolean visit(java.lang.String elementName, Annotation annotation) {
        return collect(annotation);
    }

    @Override
    public boolean visit(java.lang.String elementName, Appointment appointment) {
        return collect(appointment);
    }

    @Override
    public boolean visit(java.lang.String elementName, Appointment.Participant appointmentParticipant) {
        return collect(appointmentParticipant);
    }

    @Override
    public boolean visit(java.lang.String elementName, AppointmentResponse appointmentResponse) {
        return collect(appointmentResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, Attachment attachment) {
        return collect(attachment);
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent auditEvent) {
        return collect(auditEvent);
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Agent auditEventAgent) {
        return collect(auditEventAgent);
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Agent.Network auditEventAgentNetwork) {
        return collect(auditEventAgentNetwork);
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Entity auditEventEntity) {
        return collect(auditEventEntity);
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Entity.Detail auditEventEntityDetail) {
        return collect(auditEventEntityDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, AuditEvent.Source auditEventSource) {
        return collect(auditEventSource);
    }

    @Override
    public boolean visit(java.lang.String elementName, BackboneElement backboneElement) {
        return collect(backboneElement);
    }

    @Override
    public boolean visit(java.lang.String elementName, Base64Binary base64Binary) {
        return collect(base64Binary);
    }

    @Override
    public boolean visit(java.lang.String elementName, Basic basic) {
        return collect(basic);
    }

    @Override
    public boolean visit(java.lang.String elementName, Binary binary) {
        return collect(binary);
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct biologicallyDerivedProduct) {
        return collect(biologicallyDerivedProduct);
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct.Collection biologicallyDerivedProductCollection) {
        return collect(biologicallyDerivedProductCollection);
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct.Manipulation biologicallyDerivedProductManipulation) {
        return collect(biologicallyDerivedProductManipulation);
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct.Processing biologicallyDerivedProductProcessing) {
        return collect(biologicallyDerivedProductProcessing);
    }

    @Override
    public boolean visit(java.lang.String elementName, BiologicallyDerivedProduct.Storage biologicallyDerivedProductStorage) {
        return collect(biologicallyDerivedProductStorage);
    }

    @Override
    public boolean visit(java.lang.String elementName, BodyStructure bodyStructure) {
        return collect(bodyStructure);
    }

    @Override
    public boolean visit(java.lang.String elementName, Boolean _boolean) {
        return collect(_boolean);
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle bundle) {
        return collect(bundle);
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Entry bundleEntry) {
        return collect(bundleEntry);
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Entry.Request bundleEntryRequest) {
        return collect(bundleEntryRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Entry.Response bundleEntryResponse) {
        return collect(bundleEntryResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Entry.Search bundleEntrySearch) {
        return collect(bundleEntrySearch);
    }

    @Override
    public boolean visit(java.lang.String elementName, Bundle.Link bundleLink) {
        return collect(bundleLink);
    }

    @Override
    public boolean visit(java.lang.String elementName, Canonical canonical) {
        return collect(canonical);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement capabilityStatement) {
        return collect(capabilityStatement);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Document capabilityStatementDocument) {
        return collect(capabilityStatementDocument);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Implementation capabilityStatementImplementation) {
        return collect(capabilityStatementImplementation);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Messaging capabilityStatementMessaging) {
        return collect(capabilityStatementMessaging);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Messaging.Endpoint capabilityStatementMessagingEndpoint) {
        return collect(capabilityStatementMessagingEndpoint);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Messaging.SupportedMessage capabilityStatementMessagingSupportedMessage) {
        return collect(capabilityStatementMessagingSupportedMessage);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest capabilityStatementRest) {
        return collect(capabilityStatementRest);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Interaction capabilityStatementRestInteraction) {
        return collect(capabilityStatementRestInteraction);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Resource capabilityStatementRestResource) {
        return collect(capabilityStatementRestResource);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Resource.Interaction capabilityStatementRestResourceInteraction) {
        return collect(capabilityStatementRestResourceInteraction);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Resource.Operation capabilityStatementRestResourceOperation) {
        return collect(capabilityStatementRestResourceOperation);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Resource.SearchParam capabilityStatementRestResourceSearchParam) {
        return collect(capabilityStatementRestResourceSearchParam);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Rest.Security capabilityStatementRestSecurity) {
        return collect(capabilityStatementRestSecurity);
    }

    @Override
    public boolean visit(java.lang.String elementName, CapabilityStatement.Software capabilityStatementSoftware) {
        return collect(capabilityStatementSoftware);
    }

    @Override
    public boolean visit(java.lang.String elementName, CarePlan carePlan) {
        return collect(carePlan);
    }

    @Override
    public boolean visit(java.lang.String elementName, CarePlan.Activity carePlanActivity) {
        return collect(carePlanActivity);
    }

    @Override
    public boolean visit(java.lang.String elementName, CarePlan.Activity.Detail carePlanActivityDetail) {
        return collect(carePlanActivityDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, CareTeam careTeam) {
        return collect(careTeam);
    }

    @Override
    public boolean visit(java.lang.String elementName, CareTeam.Participant careTeamParticipant) {
        return collect(careTeamParticipant);
    }

    @Override
    public boolean visit(java.lang.String elementName, CatalogEntry catalogEntry) {
        return collect(catalogEntry);
    }

    @Override
    public boolean visit(java.lang.String elementName, CatalogEntry.RelatedEntry catalogEntryRelatedEntry) {
        return collect(catalogEntryRelatedEntry);
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItem chargeItem) {
        return collect(chargeItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItem.Performer chargeItemPerformer) {
        return collect(chargeItemPerformer);
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItemDefinition chargeItemDefinition) {
        return collect(chargeItemDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItemDefinition.Applicability chargeItemDefinitionApplicability) {
        return collect(chargeItemDefinitionApplicability);
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItemDefinition.PropertyGroup chargeItemDefinitionPropertyGroup) {
        return collect(chargeItemDefinitionPropertyGroup);
    }

    @Override
    public boolean visit(java.lang.String elementName, ChargeItemDefinition.PropertyGroup.PriceComponent chargeItemDefinitionPropertyGroupPriceComponent) {
        return collect(chargeItemDefinitionPropertyGroupPriceComponent);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim claim) {
        return collect(claim);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Accident claimAccident) {
        return collect(claimAccident);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.CareTeam claimCareTeam) {
        return collect(claimCareTeam);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Diagnosis claimDiagnosis) {
        return collect(claimDiagnosis);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Insurance claimInsurance) {
        return collect(claimInsurance);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Item claimItem) {
        return collect(claimItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Item.Detail claimItemDetail) {
        return collect(claimItemDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Item.Detail.SubDetail claimItemDetailSubDetail) {
        return collect(claimItemDetailSubDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Payee claimPayee) {
        return collect(claimPayee);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Procedure claimProcedure) {
        return collect(claimProcedure);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.Related claimRelated) {
        return collect(claimRelated);
    }

    @Override
    public boolean visit(java.lang.String elementName, Claim.SupportingInfo claimSupportingInfo) {
        return collect(claimSupportingInfo);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse claimResponse) {
        return collect(claimResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.AddItem claimResponseAddItem) {
        return collect(claimResponseAddItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.AddItem.Detail claimResponseAddItemDetail) {
        return collect(claimResponseAddItemDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.AddItem.Detail.SubDetail claimResponseAddItemDetailSubDetail) {
        return collect(claimResponseAddItemDetailSubDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Error claimResponseError) {
        return collect(claimResponseError);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Insurance claimResponseInsurance) {
        return collect(claimResponseInsurance);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Item claimResponseItem) {
        return collect(claimResponseItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Item.Adjudication claimResponseItemAdjudication) {
        return collect(claimResponseItemAdjudication);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Item.Detail claimResponseItemDetail) {
        return collect(claimResponseItemDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Item.Detail.SubDetail claimResponseItemDetailSubDetail) {
        return collect(claimResponseItemDetailSubDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Payment claimResponsePayment) {
        return collect(claimResponsePayment);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.ProcessNote claimResponseProcessNote) {
        return collect(claimResponseProcessNote);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClaimResponse.Total claimResponseTotal) {
        return collect(claimResponseTotal);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClinicalImpression clinicalImpression) {
        return collect(clinicalImpression);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClinicalImpression.Finding clinicalImpressionFinding) {
        return collect(clinicalImpressionFinding);
    }

    @Override
    public boolean visit(java.lang.String elementName, ClinicalImpression.Investigation clinicalImpressionInvestigation) {
        return collect(clinicalImpressionInvestigation);
    }

    @Override
    public boolean visit(java.lang.String elementName, Code code) {
        return collect(code);
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem codeSystem) {
        return collect(codeSystem);
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Concept codeSystemConcept) {
        return collect(codeSystemConcept);
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Concept.Designation codeSystemConceptDesignation) {
        return collect(codeSystemConceptDesignation);
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Concept.Property codeSystemConceptProperty) {
        return collect(codeSystemConceptProperty);
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Filter codeSystemFilter) {
        return collect(codeSystemFilter);
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeSystem.Property codeSystemProperty) {
        return collect(codeSystemProperty);
    }

    @Override
    public boolean visit(java.lang.String elementName, CodeableConcept codeableConcept) {
        return collect(codeableConcept);
    }

    @Override
    public boolean visit(java.lang.String elementName, Coding coding) {
        return collect(coding);
    }

    @Override
    public boolean visit(java.lang.String elementName, Communication communication) {
        return collect(communication);
    }

    @Override
    public boolean visit(java.lang.String elementName, Communication.Payload communicationPayload) {
        return collect(communicationPayload);
    }

    @Override
    public boolean visit(java.lang.String elementName, CommunicationRequest communicationRequest) {
        return collect(communicationRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, CommunicationRequest.Payload communicationRequestPayload) {
        return collect(communicationRequestPayload);
    }

    @Override
    public boolean visit(java.lang.String elementName, CompartmentDefinition compartmentDefinition) {
        return collect(compartmentDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, CompartmentDefinition.Resource compartmentDefinitionResource) {
        return collect(compartmentDefinitionResource);
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition composition) {
        return collect(composition);
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition.Attester compositionAttester) {
        return collect(compositionAttester);
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition.Event compositionEvent) {
        return collect(compositionEvent);
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition.RelatesTo compositionRelatesTo) {
        return collect(compositionRelatesTo);
    }

    @Override
    public boolean visit(java.lang.String elementName, Composition.Section compositionSection) {
        return collect(compositionSection);
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap conceptMap) {
        return collect(conceptMap);
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group conceptMapGroup) {
        return collect(conceptMapGroup);
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group.Element conceptMapGroupElement) {
        return collect(conceptMapGroupElement);
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group.Element.Target conceptMapGroupElementTarget) {
        return collect(conceptMapGroupElementTarget);
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group.Element.Target.DependsOn conceptMapGroupElementTargetDependsOn) {
        return collect(conceptMapGroupElementTargetDependsOn);
    }

    @Override
    public boolean visit(java.lang.String elementName, ConceptMap.Group.Unmapped conceptMapGroupUnmapped) {
        return collect(conceptMapGroupUnmapped);
    }

    @Override
    public boolean visit(java.lang.String elementName, Condition condition) {
        return collect(condition);
    }

    @Override
    public boolean visit(java.lang.String elementName, Condition.Evidence conditionEvidence) {
        return collect(conditionEvidence);
    }

    @Override
    public boolean visit(java.lang.String elementName, Condition.Stage conditionStage) {
        return collect(conditionStage);
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent consent) {
        return collect(consent);
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Policy consentPolicy) {
        return collect(consentPolicy);
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Provision consentProvision) {
        return collect(consentProvision);
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Provision.Actor consentProvisionActor) {
        return collect(consentProvisionActor);
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Provision.Data consentProvisionData) {
        return collect(consentProvisionData);
    }

    @Override
    public boolean visit(java.lang.String elementName, Consent.Verification consentVerification) {
        return collect(consentVerification);
    }

    @Override
    public boolean visit(java.lang.String elementName, ContactDetail contactDetail) {
        return collect(contactDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, ContactPoint contactPoint) {
        return collect(contactPoint);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract contract) {
        return collect(contract);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.ContentDefinition contractContentDefinition) {
        return collect(contractContentDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Friendly contractFriendly) {
        return collect(contractFriendly);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Legal contractLegal) {
        return collect(contractLegal);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Rule contractRule) {
        return collect(contractRule);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Signer contractSigner) {
        return collect(contractSigner);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term contractTerm) {
        return collect(contractTerm);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Action contractTermAction) {
        return collect(contractTermAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Action.Subject contractTermActionSubject) {
        return collect(contractTermActionSubject);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Asset contractTermAsset) {
        return collect(contractTermAsset);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Asset.Context contractTermAssetContext) {
        return collect(contractTermAssetContext);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Asset.ValuedItem contractTermAssetValuedItem) {
        return collect(contractTermAssetValuedItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Offer contractTermOffer) {
        return collect(contractTermOffer);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Offer.Answer contractTermOfferAnswer) {
        return collect(contractTermOfferAnswer);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.Offer.Party contractTermOfferParty) {
        return collect(contractTermOfferParty);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contract.Term.SecurityLabel contractTermSecurityLabel) {
        return collect(contractTermSecurityLabel);
    }

    @Override
    public boolean visit(java.lang.String elementName, Contributor contributor) {
        return collect(contributor);
    }

    @Override
    public boolean visit(java.lang.String elementName, Count count) {
        return collect(count);
    }

    @Override
    public boolean visit(java.lang.String elementName, Coverage coverage) {
        return collect(coverage);
    }

    @Override
    public boolean visit(java.lang.String elementName, Coverage.Class coverageClass) {
        return collect(coverageClass);
    }

    @Override
    public boolean visit(java.lang.String elementName, Coverage.CostToBeneficiary coverageCostToBeneficiary) {
        return collect(coverageCostToBeneficiary);
    }

    @Override
    public boolean visit(java.lang.String elementName, Coverage.CostToBeneficiary.Exception coverageCostToBeneficiaryException) {
        return collect(coverageCostToBeneficiaryException);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest coverageEligibilityRequest) {
        return collect(coverageEligibilityRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest.Insurance coverageEligibilityRequestInsurance) {
        return collect(coverageEligibilityRequestInsurance);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest.Item coverageEligibilityRequestItem) {
        return collect(coverageEligibilityRequestItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest.Item.Diagnosis coverageEligibilityRequestItemDiagnosis) {
        return collect(coverageEligibilityRequestItemDiagnosis);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityRequest.SupportingInfo coverageEligibilityRequestSupportingInfo) {
        return collect(coverageEligibilityRequestSupportingInfo);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse coverageEligibilityResponse) {
        return collect(coverageEligibilityResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse.Error coverageEligibilityResponseError) {
        return collect(coverageEligibilityResponseError);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse.Insurance coverageEligibilityResponseInsurance) {
        return collect(coverageEligibilityResponseInsurance);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse.Insurance.Item coverageEligibilityResponseInsuranceItem) {
        return collect(coverageEligibilityResponseInsuranceItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, CoverageEligibilityResponse.Insurance.Item.Benefit coverageEligibilityResponseInsuranceItemBenefit) {
        return collect(coverageEligibilityResponseInsuranceItemBenefit);
    }

    @Override
    public boolean visit(java.lang.String elementName, DataRequirement dataRequirement) {
        return collect(dataRequirement);
    }

    @Override
    public boolean visit(java.lang.String elementName, DataRequirement.CodeFilter dataRequirementCodeFilter) {
        return collect(dataRequirementCodeFilter);
    }

    @Override
    public boolean visit(java.lang.String elementName, DataRequirement.DateFilter dataRequirementDateFilter) {
        return collect(dataRequirementDateFilter);
    }

    @Override
    public boolean visit(java.lang.String elementName, DataRequirement.Sort dataRequirementSort) {
        return collect(dataRequirementSort);
    }

    @Override
    public boolean visit(java.lang.String elementName, Date date) {
        return collect(date);
    }

    @Override
    public boolean visit(java.lang.String elementName, DateTime dateTime) {
        return collect(dateTime);
    }

    @Override
    public boolean visit(java.lang.String elementName, Decimal decimal) {
        return collect(decimal);
    }

    @Override
    public boolean visit(java.lang.String elementName, DetectedIssue detectedIssue) {
        return collect(detectedIssue);
    }

    @Override
    public boolean visit(java.lang.String elementName, DetectedIssue.Evidence detectedIssueEvidence) {
        return collect(detectedIssueEvidence);
    }

    @Override
    public boolean visit(java.lang.String elementName, DetectedIssue.Mitigation detectedIssueMitigation) {
        return collect(detectedIssueMitigation);
    }

    @Override
    public boolean visit(java.lang.String elementName, Device device) {
        return collect(device);
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.DeviceName deviceDeviceName) {
        return collect(deviceDeviceName);
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.Property deviceProperty) {
        return collect(deviceProperty);
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.Specialization deviceSpecialization) {
        return collect(deviceSpecialization);
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.UdiCarrier deviceUdiCarrier) {
        return collect(deviceUdiCarrier);
    }

    @Override
    public boolean visit(java.lang.String elementName, Device.Version deviceVersion) {
        return collect(deviceVersion);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition deviceDefinition) {
        return collect(deviceDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.Capability deviceDefinitionCapability) {
        return collect(deviceDefinitionCapability);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.DeviceName deviceDefinitionDeviceName) {
        return collect(deviceDefinitionDeviceName);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.Material deviceDefinitionMaterial) {
        return collect(deviceDefinitionMaterial);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.Property deviceDefinitionProperty) {
        return collect(deviceDefinitionProperty);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.Specialization deviceDefinitionSpecialization) {
        return collect(deviceDefinitionSpecialization);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceDefinition.UdiDeviceIdentifier deviceDefinitionUdiDeviceIdentifier) {
        return collect(deviceDefinitionUdiDeviceIdentifier);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceMetric deviceMetric) {
        return collect(deviceMetric);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceMetric.Calibration deviceMetricCalibration) {
        return collect(deviceMetricCalibration);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceRequest deviceRequest) {
        return collect(deviceRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceRequest.Parameter deviceRequestParameter) {
        return collect(deviceRequestParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, DeviceUseStatement deviceUseStatement) {
        return collect(deviceUseStatement);
    }

    @Override
    public boolean visit(java.lang.String elementName, DiagnosticReport diagnosticReport) {
        return collect(diagnosticReport);
    }

    @Override
    public boolean visit(java.lang.String elementName, DiagnosticReport.Media diagnosticReportMedia) {
        return collect(diagnosticReportMedia);
    }

    @Override
    public boolean visit(java.lang.String elementName, Distance distance) {
        return collect(distance);
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentManifest documentManifest) {
        return collect(documentManifest);
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentManifest.Related documentManifestRelated) {
        return collect(documentManifestRelated);
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentReference documentReference) {
        return collect(documentReference);
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentReference.Content documentReferenceContent) {
        return collect(documentReferenceContent);
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentReference.Context documentReferenceContext) {
        return collect(documentReferenceContext);
    }

    @Override
    public boolean visit(java.lang.String elementName, DocumentReference.RelatesTo documentReferenceRelatesTo) {
        return collect(documentReferenceRelatesTo);
    }

    @Override
    public boolean visit(java.lang.String elementName, DomainResource domainResource) {
        return collect(domainResource);
    }

    @Override
    public boolean visit(java.lang.String elementName, Dosage dosage) {
        return collect(dosage);
    }

    @Override
    public boolean visit(java.lang.String elementName, Dosage.DoseAndRate dosageDoseAndRate) {
        return collect(dosageDoseAndRate);
    }

    @Override
    public boolean visit(java.lang.String elementName, Duration duration) {
        return collect(duration);
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis effectEvidenceSynthesis) {
        return collect(effectEvidenceSynthesis);
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.Certainty effectEvidenceSynthesisCertainty) {
        return collect(effectEvidenceSynthesisCertainty);
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.Certainty.CertaintySubcomponent effectEvidenceSynthesisCertaintyCertaintySubcomponent) {
        return collect(effectEvidenceSynthesisCertaintyCertaintySubcomponent);
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.EffectEstimate effectEvidenceSynthesisEffectEstimate) {
        return collect(effectEvidenceSynthesisEffectEstimate);
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.EffectEstimate.PrecisionEstimate effectEvidenceSynthesisEffectEstimatePrecisionEstimate) {
        return collect(effectEvidenceSynthesisEffectEstimatePrecisionEstimate);
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.ResultsByExposure effectEvidenceSynthesisResultsByExposure) {
        return collect(effectEvidenceSynthesisResultsByExposure);
    }

    @Override
    public boolean visit(java.lang.String elementName, EffectEvidenceSynthesis.SampleSize effectEvidenceSynthesisSampleSize) {
        return collect(effectEvidenceSynthesisSampleSize);
    }

    @Override
    public boolean visit(java.lang.String elementName, Element element) {
        return collect(element);
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition elementDefinition) {
        return collect(elementDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Base elementDefinitionBase) {
        return collect(elementDefinitionBase);
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Binding elementDefinitionBinding) {
        return collect(elementDefinitionBinding);
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Constraint elementDefinitionConstraint) {
        return collect(elementDefinitionConstraint);
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Example elementDefinitionExample) {
        return collect(elementDefinitionExample);
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Mapping elementDefinitionMapping) {
        return collect(elementDefinitionMapping);
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Slicing elementDefinitionSlicing) {
        return collect(elementDefinitionSlicing);
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Slicing.Discriminator elementDefinitionSlicingDiscriminator) {
        return collect(elementDefinitionSlicingDiscriminator);
    }

    @Override
    public boolean visit(java.lang.String elementName, ElementDefinition.Type elementDefinitionType) {
        return collect(elementDefinitionType);
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter encounter) {
        return collect(encounter);
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.ClassHistory encounterClassHistory) {
        return collect(encounterClassHistory);
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.Diagnosis encounterDiagnosis) {
        return collect(encounterDiagnosis);
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.Hospitalization encounterHospitalization) {
        return collect(encounterHospitalization);
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.Location encounterLocation) {
        return collect(encounterLocation);
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.Participant encounterParticipant) {
        return collect(encounterParticipant);
    }

    @Override
    public boolean visit(java.lang.String elementName, Encounter.StatusHistory encounterStatusHistory) {
        return collect(encounterStatusHistory);
    }

    @Override
    public boolean visit(java.lang.String elementName, Endpoint endpoint) {
        return collect(endpoint);
    }

    @Override
    public boolean visit(java.lang.String elementName, EnrollmentRequest enrollmentRequest) {
        return collect(enrollmentRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, EnrollmentResponse enrollmentResponse) {
        return collect(enrollmentResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, EpisodeOfCare episodeOfCare) {
        return collect(episodeOfCare);
    }

    @Override
    public boolean visit(java.lang.String elementName, EpisodeOfCare.Diagnosis episodeOfCareDiagnosis) {
        return collect(episodeOfCareDiagnosis);
    }

    @Override
    public boolean visit(java.lang.String elementName, EpisodeOfCare.StatusHistory episodeOfCareStatusHistory) {
        return collect(episodeOfCareStatusHistory);
    }

    @Override
    public boolean visit(java.lang.String elementName, EventDefinition eventDefinition) {
        return collect(eventDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, Evidence evidence) {
        return collect(evidence);
    }

    @Override
    public boolean visit(java.lang.String elementName, EvidenceVariable evidenceVariable) {
        return collect(evidenceVariable);
    }

    @Override
    public boolean visit(java.lang.String elementName, EvidenceVariable.Characteristic evidenceVariableCharacteristic) {
        return collect(evidenceVariableCharacteristic);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario exampleScenario) {
        return collect(exampleScenario);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Actor exampleScenarioActor) {
        return collect(exampleScenarioActor);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Instance exampleScenarioInstance) {
        return collect(exampleScenarioInstance);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Instance.ContainedInstance exampleScenarioInstanceContainedInstance) {
        return collect(exampleScenarioInstanceContainedInstance);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Instance.Version exampleScenarioInstanceVersion) {
        return collect(exampleScenarioInstanceVersion);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Process exampleScenarioProcess) {
        return collect(exampleScenarioProcess);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Process.Step exampleScenarioProcessStep) {
        return collect(exampleScenarioProcessStep);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Process.Step.Alternative exampleScenarioProcessStepAlternative) {
        return collect(exampleScenarioProcessStepAlternative);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExampleScenario.Process.Step.Operation exampleScenarioProcessStepOperation) {
        return collect(exampleScenarioProcessStepOperation);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit explanationOfBenefit) {
        return collect(explanationOfBenefit);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Accident explanationOfBenefitAccident) {
        return collect(explanationOfBenefitAccident);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.AddItem explanationOfBenefitAddItem) {
        return collect(explanationOfBenefitAddItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.AddItem.Detail explanationOfBenefitAddItemDetail) {
        return collect(explanationOfBenefitAddItemDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.AddItem.Detail.SubDetail explanationOfBenefitAddItemDetailSubDetail) {
        return collect(explanationOfBenefitAddItemDetailSubDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.BenefitBalance explanationOfBenefitBenefitBalance) {
        return collect(explanationOfBenefitBenefitBalance);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.BenefitBalance.Financial explanationOfBenefitBenefitBalanceFinancial) {
        return collect(explanationOfBenefitBenefitBalanceFinancial);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.CareTeam explanationOfBenefitCareTeam) {
        return collect(explanationOfBenefitCareTeam);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Diagnosis explanationOfBenefitDiagnosis) {
        return collect(explanationOfBenefitDiagnosis);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Insurance explanationOfBenefitInsurance) {
        return collect(explanationOfBenefitInsurance);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Item explanationOfBenefitItem) {
        return collect(explanationOfBenefitItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Item.Adjudication explanationOfBenefitItemAdjudication) {
        return collect(explanationOfBenefitItemAdjudication);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Item.Detail explanationOfBenefitItemDetail) {
        return collect(explanationOfBenefitItemDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Item.Detail.SubDetail explanationOfBenefitItemDetailSubDetail) {
        return collect(explanationOfBenefitItemDetailSubDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Payee explanationOfBenefitPayee) {
        return collect(explanationOfBenefitPayee);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Payment explanationOfBenefitPayment) {
        return collect(explanationOfBenefitPayment);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Procedure explanationOfBenefitProcedure) {
        return collect(explanationOfBenefitProcedure);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.ProcessNote explanationOfBenefitProcessNote) {
        return collect(explanationOfBenefitProcessNote);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Related explanationOfBenefitRelated) {
        return collect(explanationOfBenefitRelated);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.SupportingInfo explanationOfBenefitSupportingInfo) {
        return collect(explanationOfBenefitSupportingInfo);
    }

    @Override
    public boolean visit(java.lang.String elementName, ExplanationOfBenefit.Total explanationOfBenefitTotal) {
        return collect(explanationOfBenefitTotal);
    }

    @Override
    public boolean visit(java.lang.String elementName, Expression expression) {
        return collect(expression);
    }

    @Override
    public boolean visit(java.lang.String elementName, Extension extension) {
        return collect(extension);
    }

    @Override
    public boolean visit(java.lang.String elementName, FamilyMemberHistory familyMemberHistory) {
        return collect(familyMemberHistory);
    }

    @Override
    public boolean visit(java.lang.String elementName, FamilyMemberHistory.Condition familyMemberHistoryCondition) {
        return collect(familyMemberHistoryCondition);
    }

    @Override
    public boolean visit(java.lang.String elementName, Flag flag) {
        return collect(flag);
    }

    @Override
    public boolean visit(java.lang.String elementName, Goal goal) {
        return collect(goal);
    }

    @Override
    public boolean visit(java.lang.String elementName, Goal.Target goalTarget) {
        return collect(goalTarget);
    }

    @Override
    public boolean visit(java.lang.String elementName, GraphDefinition graphDefinition) {
        return collect(graphDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, GraphDefinition.Link graphDefinitionLink) {
        return collect(graphDefinitionLink);
    }

    @Override
    public boolean visit(java.lang.String elementName, GraphDefinition.Link.Target graphDefinitionLinkTarget) {
        return collect(graphDefinitionLinkTarget);
    }

    @Override
    public boolean visit(java.lang.String elementName, GraphDefinition.Link.Target.Compartment graphDefinitionLinkTargetCompartment) {
        return collect(graphDefinitionLinkTargetCompartment);
    }

    @Override
    public boolean visit(java.lang.String elementName, Group group) {
        return collect(group);
    }

    @Override
    public boolean visit(java.lang.String elementName, Group.Characteristic groupCharacteristic) {
        return collect(groupCharacteristic);
    }

    @Override
    public boolean visit(java.lang.String elementName, Group.Member groupMember) {
        return collect(groupMember);
    }

    @Override
    public boolean visit(java.lang.String elementName, GuidanceResponse guidanceResponse) {
        return collect(guidanceResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, HealthcareService healthcareService) {
        return collect(healthcareService);
    }

    @Override
    public boolean visit(java.lang.String elementName, HealthcareService.AvailableTime healthcareServiceAvailableTime) {
        return collect(healthcareServiceAvailableTime);
    }

    @Override
    public boolean visit(java.lang.String elementName, HealthcareService.Eligibility healthcareServiceEligibility) {
        return collect(healthcareServiceEligibility);
    }

    @Override
    public boolean visit(java.lang.String elementName, HealthcareService.NotAvailable healthcareServiceNotAvailable) {
        return collect(healthcareServiceNotAvailable);
    }

    @Override
    public boolean visit(java.lang.String elementName, HumanName humanName) {
        return collect(humanName);
    }

    @Override
    public boolean visit(java.lang.String elementName, Id id) {
        return collect(id);
    }

    @Override
    public boolean visit(java.lang.String elementName, Identifier identifier) {
        return collect(identifier);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImagingStudy imagingStudy) {
        return collect(imagingStudy);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImagingStudy.Series imagingStudySeries) {
        return collect(imagingStudySeries);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImagingStudy.Series.Instance imagingStudySeriesInstance) {
        return collect(imagingStudySeriesInstance);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImagingStudy.Series.Performer imagingStudySeriesPerformer) {
        return collect(imagingStudySeriesPerformer);
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization immunization) {
        return collect(immunization);
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization.Education immunizationEducation) {
        return collect(immunizationEducation);
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization.Performer immunizationPerformer) {
        return collect(immunizationPerformer);
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization.ProtocolApplied immunizationProtocolApplied) {
        return collect(immunizationProtocolApplied);
    }

    @Override
    public boolean visit(java.lang.String elementName, Immunization.Reaction immunizationReaction) {
        return collect(immunizationReaction);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImmunizationEvaluation immunizationEvaluation) {
        return collect(immunizationEvaluation);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImmunizationRecommendation immunizationRecommendation) {
        return collect(immunizationRecommendation);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImmunizationRecommendation.Recommendation immunizationRecommendationRecommendation) {
        return collect(immunizationRecommendationRecommendation);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImmunizationRecommendation.Recommendation.DateCriterion immunizationRecommendationRecommendationDateCriterion) {
        return collect(immunizationRecommendationRecommendationDateCriterion);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide implementationGuide) {
        return collect(implementationGuide);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition implementationGuideDefinition) {
        return collect(implementationGuideDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Grouping implementationGuideDefinitionGrouping) {
        return collect(implementationGuideDefinitionGrouping);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Page implementationGuideDefinitionPage) {
        return collect(implementationGuideDefinitionPage);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Parameter implementationGuideDefinitionParameter) {
        return collect(implementationGuideDefinitionParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Resource implementationGuideDefinitionResource) {
        return collect(implementationGuideDefinitionResource);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Definition.Template implementationGuideDefinitionTemplate) {
        return collect(implementationGuideDefinitionTemplate);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.DependsOn implementationGuideDependsOn) {
        return collect(implementationGuideDependsOn);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Global implementationGuideGlobal) {
        return collect(implementationGuideGlobal);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Manifest implementationGuideManifest) {
        return collect(implementationGuideManifest);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Manifest.Page implementationGuideManifestPage) {
        return collect(implementationGuideManifestPage);
    }

    @Override
    public boolean visit(java.lang.String elementName, ImplementationGuide.Manifest.Resource implementationGuideManifestResource) {
        return collect(implementationGuideManifestResource);
    }

    @Override
    public boolean visit(java.lang.String elementName, Instant instant) {
        return collect(instant);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan insurancePlan) {
        return collect(insurancePlan);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Contact insurancePlanContact) {
        return collect(insurancePlanContact);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Coverage insurancePlanCoverage) {
        return collect(insurancePlanCoverage);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Coverage.Benefit insurancePlanCoverageBenefit) {
        return collect(insurancePlanCoverageBenefit);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Coverage.Benefit.Limit insurancePlanCoverageBenefitLimit) {
        return collect(insurancePlanCoverageBenefitLimit);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan insurancePlanPlan) {
        return collect(insurancePlanPlan);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan.GeneralCost insurancePlanPlanGeneralCost) {
        return collect(insurancePlanPlanGeneralCost);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan.SpecificCost insurancePlanPlanSpecificCost) {
        return collect(insurancePlanPlanSpecificCost);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan.SpecificCost.Benefit insurancePlanPlanSpecificCostBenefit) {
        return collect(insurancePlanPlanSpecificCostBenefit);
    }

    @Override
    public boolean visit(java.lang.String elementName, InsurancePlan.Plan.SpecificCost.Benefit.Cost insurancePlanPlanSpecificCostBenefitCost) {
        return collect(insurancePlanPlanSpecificCostBenefitCost);
    }

    @Override
    public boolean visit(java.lang.String elementName, Integer integer) {
        return collect(integer);
    }

    @Override
    public boolean visit(java.lang.String elementName, Invoice invoice) {
        return collect(invoice);
    }

    @Override
    public boolean visit(java.lang.String elementName, Invoice.LineItem invoiceLineItem) {
        return collect(invoiceLineItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, Invoice.LineItem.PriceComponent invoiceLineItemPriceComponent) {
        return collect(invoiceLineItemPriceComponent);
    }

    @Override
    public boolean visit(java.lang.String elementName, Invoice.Participant invoiceParticipant) {
        return collect(invoiceParticipant);
    }

    @Override
    public boolean visit(java.lang.String elementName, Library library) {
        return collect(library);
    }

    @Override
    public boolean visit(java.lang.String elementName, Linkage linkage) {
        return collect(linkage);
    }

    @Override
    public boolean visit(java.lang.String elementName, Linkage.Item linkageItem) {
        return collect(linkageItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, List list) {
        return collect(list);
    }

    @Override
    public boolean visit(java.lang.String elementName, List.Entry listEntry) {
        return collect(listEntry);
    }

    @Override
    public boolean visit(java.lang.String elementName, Location location) {
        return collect(location);
    }

    @Override
    public boolean visit(java.lang.String elementName, Location.HoursOfOperation locationHoursOfOperation) {
        return collect(locationHoursOfOperation);
    }

    @Override
    public boolean visit(java.lang.String elementName, Location.Position locationPosition) {
        return collect(locationPosition);
    }

    @Override
    public boolean visit(java.lang.String elementName, Markdown markdown) {
        return collect(markdown);
    }

    @Override
    public boolean visit(java.lang.String elementName, MarketingStatus marketingStatus) {
        return collect(marketingStatus);
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure measure) {
        return collect(measure);
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.Group measureGroup) {
        return collect(measureGroup);
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.Group.Population measureGroupPopulation) {
        return collect(measureGroupPopulation);
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.Group.Stratifier measureGroupStratifier) {
        return collect(measureGroupStratifier);
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.Group.Stratifier.Component measureGroupStratifierComponent) {
        return collect(measureGroupStratifierComponent);
    }

    @Override
    public boolean visit(java.lang.String elementName, Measure.SupplementalData measureSupplementalData) {
        return collect(measureSupplementalData);
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport measureReport) {
        return collect(measureReport);
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group measureReportGroup) {
        return collect(measureReportGroup);
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Population measureReportGroupPopulation) {
        return collect(measureReportGroupPopulation);
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Stratifier measureReportGroupStratifier) {
        return collect(measureReportGroupStratifier);
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Stratifier.Stratum measureReportGroupStratifierStratum) {
        return collect(measureReportGroupStratifierStratum);
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Stratifier.Stratum.Component measureReportGroupStratifierStratumComponent) {
        return collect(measureReportGroupStratifierStratumComponent);
    }

    @Override
    public boolean visit(java.lang.String elementName, MeasureReport.Group.Stratifier.Stratum.Population measureReportGroupStratifierStratumPopulation) {
        return collect(measureReportGroupStratifierStratumPopulation);
    }

    @Override
    public boolean visit(java.lang.String elementName, Media media) {
        return collect(media);
    }

    @Override
    public boolean visit(java.lang.String elementName, Medication medication) {
        return collect(medication);
    }

    @Override
    public boolean visit(java.lang.String elementName, Medication.Batch medicationBatch) {
        return collect(medicationBatch);
    }

    @Override
    public boolean visit(java.lang.String elementName, Medication.Ingredient medicationIngredient) {
        return collect(medicationIngredient);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationAdministration medicationAdministration) {
        return collect(medicationAdministration);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationAdministration.Dosage medicationAdministrationDosage) {
        return collect(medicationAdministrationDosage);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationAdministration.Performer medicationAdministrationPerformer) {
        return collect(medicationAdministrationPerformer);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationDispense medicationDispense) {
        return collect(medicationDispense);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationDispense.Performer medicationDispensePerformer) {
        return collect(medicationDispensePerformer);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationDispense.Substitution medicationDispenseSubstitution) {
        return collect(medicationDispenseSubstitution);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge medicationKnowledge) {
        return collect(medicationKnowledge);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.AdministrationGuidelines medicationKnowledgeAdministrationGuidelines) {
        return collect(medicationKnowledgeAdministrationGuidelines);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.AdministrationGuidelines.Dosage medicationKnowledgeAdministrationGuidelinesDosage) {
        return collect(medicationKnowledgeAdministrationGuidelinesDosage);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.AdministrationGuidelines.PatientCharacteristics medicationKnowledgeAdministrationGuidelinesPatientCharacteristics) {
        return collect(medicationKnowledgeAdministrationGuidelinesPatientCharacteristics);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Cost medicationKnowledgeCost) {
        return collect(medicationKnowledgeCost);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.DrugCharacteristic medicationKnowledgeDrugCharacteristic) {
        return collect(medicationKnowledgeDrugCharacteristic);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Ingredient medicationKnowledgeIngredient) {
        return collect(medicationKnowledgeIngredient);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Kinetics medicationKnowledgeKinetics) {
        return collect(medicationKnowledgeKinetics);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.MedicineClassification medicationKnowledgeMedicineClassification) {
        return collect(medicationKnowledgeMedicineClassification);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.MonitoringProgram medicationKnowledgeMonitoringProgram) {
        return collect(medicationKnowledgeMonitoringProgram);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Monograph medicationKnowledgeMonograph) {
        return collect(medicationKnowledgeMonograph);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Packaging medicationKnowledgePackaging) {
        return collect(medicationKnowledgePackaging);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Regulatory medicationKnowledgeRegulatory) {
        return collect(medicationKnowledgeRegulatory);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Regulatory.MaxDispense medicationKnowledgeRegulatoryMaxDispense) {
        return collect(medicationKnowledgeRegulatoryMaxDispense);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Regulatory.Schedule medicationKnowledgeRegulatorySchedule) {
        return collect(medicationKnowledgeRegulatorySchedule);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.Regulatory.Substitution medicationKnowledgeRegulatorySubstitution) {
        return collect(medicationKnowledgeRegulatorySubstitution);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationKnowledge.RelatedMedicationKnowledge medicationKnowledgeRelatedMedicationKnowledge) {
        return collect(medicationKnowledgeRelatedMedicationKnowledge);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationRequest medicationRequest) {
        return collect(medicationRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationRequest.DispenseRequest medicationRequestDispenseRequest) {
        return collect(medicationRequestDispenseRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationRequest.DispenseRequest.InitialFill medicationRequestDispenseRequestInitialFill) {
        return collect(medicationRequestDispenseRequestInitialFill);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationRequest.Substitution medicationRequestSubstitution) {
        return collect(medicationRequestSubstitution);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicationStatement medicationStatement) {
        return collect(medicationStatement);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct medicinalProduct) {
        return collect(medicinalProduct);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.ManufacturingBusinessOperation medicinalProductManufacturingBusinessOperation) {
        return collect(medicinalProductManufacturingBusinessOperation);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.Name medicinalProductName) {
        return collect(medicinalProductName);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.Name.CountryLanguage medicinalProductNameCountryLanguage) {
        return collect(medicinalProductNameCountryLanguage);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.Name.NamePart medicinalProductNameNamePart) {
        return collect(medicinalProductNameNamePart);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProduct.SpecialDesignation medicinalProductSpecialDesignation) {
        return collect(medicinalProductSpecialDesignation);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductAuthorization medicinalProductAuthorization) {
        return collect(medicinalProductAuthorization);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductAuthorization.JurisdictionalAuthorization medicinalProductAuthorizationJurisdictionalAuthorization) {
        return collect(medicinalProductAuthorizationJurisdictionalAuthorization);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductAuthorization.Procedure medicinalProductAuthorizationProcedure) {
        return collect(medicinalProductAuthorizationProcedure);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductContraindication medicinalProductContraindication) {
        return collect(medicinalProductContraindication);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductContraindication.OtherTherapy medicinalProductContraindicationOtherTherapy) {
        return collect(medicinalProductContraindicationOtherTherapy);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIndication medicinalProductIndication) {
        return collect(medicinalProductIndication);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIndication.OtherTherapy medicinalProductIndicationOtherTherapy) {
        return collect(medicinalProductIndicationOtherTherapy);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient medicinalProductIngredient) {
        return collect(medicinalProductIngredient);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient.SpecifiedSubstance medicinalProductIngredientSpecifiedSubstance) {
        return collect(medicinalProductIngredientSpecifiedSubstance);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient.SpecifiedSubstance.Strength medicinalProductIngredientSpecifiedSubstanceStrength) {
        return collect(medicinalProductIngredientSpecifiedSubstanceStrength);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient.SpecifiedSubstance.Strength.ReferenceStrength medicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrength) {
        return collect(medicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrength);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductIngredient.Substance medicinalProductIngredientSubstance) {
        return collect(medicinalProductIngredientSubstance);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductInteraction medicinalProductInteraction) {
        return collect(medicinalProductInteraction);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductInteraction.Interactant medicinalProductInteractionInteractant) {
        return collect(medicinalProductInteractionInteractant);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductManufactured medicinalProductManufactured) {
        return collect(medicinalProductManufactured);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPackaged medicinalProductPackaged) {
        return collect(medicinalProductPackaged);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPackaged.BatchIdentifier medicinalProductPackagedBatchIdentifier) {
        return collect(medicinalProductPackagedBatchIdentifier);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPackaged.PackageItem medicinalProductPackagedPackageItem) {
        return collect(medicinalProductPackagedPackageItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical medicinalProductPharmaceutical) {
        return collect(medicinalProductPharmaceutical);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical.Characteristics medicinalProductPharmaceuticalCharacteristics) {
        return collect(medicinalProductPharmaceuticalCharacteristics);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical.RouteOfAdministration medicinalProductPharmaceuticalRouteOfAdministration) {
        return collect(medicinalProductPharmaceuticalRouteOfAdministration);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical.RouteOfAdministration.TargetSpecies medicinalProductPharmaceuticalRouteOfAdministrationTargetSpecies) {
        return collect(medicinalProductPharmaceuticalRouteOfAdministrationTargetSpecies);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductPharmaceutical.RouteOfAdministration.TargetSpecies.WithdrawalPeriod medicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriod) {
        return collect(medicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriod);
    }

    @Override
    public boolean visit(java.lang.String elementName, MedicinalProductUndesirableEffect medicinalProductUndesirableEffect) {
        return collect(medicinalProductUndesirableEffect);
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageDefinition messageDefinition) {
        return collect(messageDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageDefinition.AllowedResponse messageDefinitionAllowedResponse) {
        return collect(messageDefinitionAllowedResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageDefinition.Focus messageDefinitionFocus) {
        return collect(messageDefinitionFocus);
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageHeader messageHeader) {
        return collect(messageHeader);
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageHeader.Destination messageHeaderDestination) {
        return collect(messageHeaderDestination);
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageHeader.Response messageHeaderResponse) {
        return collect(messageHeaderResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, MessageHeader.Source messageHeaderSource) {
        return collect(messageHeaderSource);
    }

    @Override
    public boolean visit(java.lang.String elementName, Meta meta) {
        return collect(meta);
    }

    @Override
    public boolean visit(java.lang.String elementName, MetadataResource metadataResource) {
        return collect(metadataResource);
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence molecularSequence) {
        return collect(molecularSequence);
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.Quality molecularSequenceQuality) {
        return collect(molecularSequenceQuality);
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.Quality.Roc molecularSequenceQualityRoc) {
        return collect(molecularSequenceQualityRoc);
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.ReferenceSeq molecularSequenceReferenceSeq) {
        return collect(molecularSequenceReferenceSeq);
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.Repository molecularSequenceRepository) {
        return collect(molecularSequenceRepository);
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.StructureVariant molecularSequenceStructureVariant) {
        return collect(molecularSequenceStructureVariant);
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.StructureVariant.Inner molecularSequenceStructureVariantInner) {
        return collect(molecularSequenceStructureVariantInner);
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.StructureVariant.Outer molecularSequenceStructureVariantOuter) {
        return collect(molecularSequenceStructureVariantOuter);
    }

    @Override
    public boolean visit(java.lang.String elementName, MolecularSequence.Variant molecularSequenceVariant) {
        return collect(molecularSequenceVariant);
    }

    @Override
    public boolean visit(java.lang.String elementName, Money money) {
        return collect(money);
    }

    @Override
    public boolean visit(java.lang.String elementName, MoneyQuantity moneyQuantity) {
        return collect(moneyQuantity);
    }

    @Override
    public boolean visit(java.lang.String elementName, NamingSystem namingSystem) {
        return collect(namingSystem);
    }

    @Override
    public boolean visit(java.lang.String elementName, NamingSystem.UniqueId namingSystemUniqueId) {
        return collect(namingSystemUniqueId);
    }

    @Override
    public boolean visit(java.lang.String elementName, Narrative narrative) {
        return collect(narrative);
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder nutritionOrder) {
        return collect(nutritionOrder);
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.EnteralFormula nutritionOrderEnteralFormula) {
        return collect(nutritionOrderEnteralFormula);
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.EnteralFormula.Administration nutritionOrderEnteralFormulaAdministration) {
        return collect(nutritionOrderEnteralFormulaAdministration);
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.OralDiet nutritionOrderOralDiet) {
        return collect(nutritionOrderOralDiet);
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.OralDiet.Nutrient nutritionOrderOralDietNutrient) {
        return collect(nutritionOrderOralDietNutrient);
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.OralDiet.Texture nutritionOrderOralDietTexture) {
        return collect(nutritionOrderOralDietTexture);
    }

    @Override
    public boolean visit(java.lang.String elementName, NutritionOrder.Supplement nutritionOrderSupplement) {
        return collect(nutritionOrderSupplement);
    }

    @Override
    public boolean visit(java.lang.String elementName, Observation observation) {
        return collect(observation);
    }

    @Override
    public boolean visit(java.lang.String elementName, Observation.Component observationComponent) {
        return collect(observationComponent);
    }

    @Override
    public boolean visit(java.lang.String elementName, Observation.ReferenceRange observationReferenceRange) {
        return collect(observationReferenceRange);
    }

    @Override
    public boolean visit(java.lang.String elementName, ObservationDefinition observationDefinition) {
        return collect(observationDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, ObservationDefinition.QualifiedInterval observationDefinitionQualifiedInterval) {
        return collect(observationDefinitionQualifiedInterval);
    }

    @Override
    public boolean visit(java.lang.String elementName, ObservationDefinition.QuantitativeDetails observationDefinitionQuantitativeDetails) {
        return collect(observationDefinitionQuantitativeDetails);
    }

    @Override
    public boolean visit(java.lang.String elementName, Oid oid) {
        return collect(oid);
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition operationDefinition) {
        return collect(operationDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition.Overload operationDefinitionOverload) {
        return collect(operationDefinitionOverload);
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition.Parameter operationDefinitionParameter) {
        return collect(operationDefinitionParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition.Parameter.Binding operationDefinitionParameterBinding) {
        return collect(operationDefinitionParameterBinding);
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationDefinition.Parameter.ReferencedFrom operationDefinitionParameterReferencedFrom) {
        return collect(operationDefinitionParameterReferencedFrom);
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationOutcome operationOutcome) {
        return collect(operationOutcome);
    }

    @Override
    public boolean visit(java.lang.String elementName, OperationOutcome.Issue operationOutcomeIssue) {
        return collect(operationOutcomeIssue);
    }

    @Override
    public boolean visit(java.lang.String elementName, Organization organization) {
        return collect(organization);
    }

    @Override
    public boolean visit(java.lang.String elementName, Organization.Contact organizationContact) {
        return collect(organizationContact);
    }

    @Override
    public boolean visit(java.lang.String elementName, OrganizationAffiliation organizationAffiliation) {
        return collect(organizationAffiliation);
    }

    @Override
    public boolean visit(java.lang.String elementName, ParameterDefinition parameterDefinition) {
        return collect(parameterDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, Parameters parameters) {
        return collect(parameters);
    }

    @Override
    public boolean visit(java.lang.String elementName, Parameters.Parameter parametersParameter) {
        return collect(parametersParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, Patient patient) {
        return collect(patient);
    }

    @Override
    public boolean visit(java.lang.String elementName, Patient.Communication patientCommunication) {
        return collect(patientCommunication);
    }

    @Override
    public boolean visit(java.lang.String elementName, Patient.Contact patientContact) {
        return collect(patientContact);
    }

    @Override
    public boolean visit(java.lang.String elementName, Patient.Link patientLink) {
        return collect(patientLink);
    }

    @Override
    public boolean visit(java.lang.String elementName, PaymentNotice paymentNotice) {
        return collect(paymentNotice);
    }

    @Override
    public boolean visit(java.lang.String elementName, PaymentReconciliation paymentReconciliation) {
        return collect(paymentReconciliation);
    }

    @Override
    public boolean visit(java.lang.String elementName, PaymentReconciliation.Detail paymentReconciliationDetail) {
        return collect(paymentReconciliationDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, PaymentReconciliation.ProcessNote paymentReconciliationProcessNote) {
        return collect(paymentReconciliationProcessNote);
    }

    @Override
    public boolean visit(java.lang.String elementName, Period period) {
        return collect(period);
    }

    @Override
    public boolean visit(java.lang.String elementName, Person person) {
        return collect(person);
    }

    @Override
    public boolean visit(java.lang.String elementName, Person.Link personLink) {
        return collect(personLink);
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition planDefinition) {
        return collect(planDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action planDefinitionAction) {
        return collect(planDefinitionAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action.Condition planDefinitionActionCondition) {
        return collect(planDefinitionActionCondition);
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action.DynamicValue planDefinitionActionDynamicValue) {
        return collect(planDefinitionActionDynamicValue);
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action.Participant planDefinitionActionParticipant) {
        return collect(planDefinitionActionParticipant);
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Action.RelatedAction planDefinitionActionRelatedAction) {
        return collect(planDefinitionActionRelatedAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Goal planDefinitionGoal) {
        return collect(planDefinitionGoal);
    }

    @Override
    public boolean visit(java.lang.String elementName, PlanDefinition.Goal.Target planDefinitionGoalTarget) {
        return collect(planDefinitionGoalTarget);
    }

    @Override
    public boolean visit(java.lang.String elementName, Population population) {
        return collect(population);
    }

    @Override
    public boolean visit(java.lang.String elementName, PositiveInt positiveInt) {
        return collect(positiveInt);
    }

    @Override
    public boolean visit(java.lang.String elementName, Practitioner practitioner) {
        return collect(practitioner);
    }

    @Override
    public boolean visit(java.lang.String elementName, Practitioner.Qualification practitionerQualification) {
        return collect(practitionerQualification);
    }

    @Override
    public boolean visit(java.lang.String elementName, PractitionerRole practitionerRole) {
        return collect(practitionerRole);
    }

    @Override
    public boolean visit(java.lang.String elementName, PractitionerRole.AvailableTime practitionerRoleAvailableTime) {
        return collect(practitionerRoleAvailableTime);
    }

    @Override
    public boolean visit(java.lang.String elementName, PractitionerRole.NotAvailable practitionerRoleNotAvailable) {
        return collect(practitionerRoleNotAvailable);
    }

    @Override
    public boolean visit(java.lang.String elementName, Procedure procedure) {
        return collect(procedure);
    }

    @Override
    public boolean visit(java.lang.String elementName, Procedure.FocalDevice procedureFocalDevice) {
        return collect(procedureFocalDevice);
    }

    @Override
    public boolean visit(java.lang.String elementName, Procedure.Performer procedurePerformer) {
        return collect(procedurePerformer);
    }

    @Override
    public boolean visit(java.lang.String elementName, ProdCharacteristic prodCharacteristic) {
        return collect(prodCharacteristic);
    }

    @Override
    public boolean visit(java.lang.String elementName, ProductShelfLife productShelfLife) {
        return collect(productShelfLife);
    }

    @Override
    public boolean visit(java.lang.String elementName, Provenance provenance) {
        return collect(provenance);
    }

    @Override
    public boolean visit(java.lang.String elementName, Provenance.Agent provenanceAgent) {
        return collect(provenanceAgent);
    }

    @Override
    public boolean visit(java.lang.String elementName, Provenance.Entity provenanceEntity) {
        return collect(provenanceEntity);
    }

    @Override
    public boolean visit(java.lang.String elementName, Quantity quantity) {
        return collect(quantity);
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire questionnaire) {
        return collect(questionnaire);
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire.Item questionnaireItem) {
        return collect(questionnaireItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire.Item.AnswerOption questionnaireItemAnswerOption) {
        return collect(questionnaireItemAnswerOption);
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire.Item.EnableWhen questionnaireItemEnableWhen) {
        return collect(questionnaireItemEnableWhen);
    }

    @Override
    public boolean visit(java.lang.String elementName, Questionnaire.Item.Initial questionnaireItemInitial) {
        return collect(questionnaireItemInitial);
    }

    @Override
    public boolean visit(java.lang.String elementName, QuestionnaireResponse questionnaireResponse) {
        return collect(questionnaireResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, QuestionnaireResponse.Item questionnaireResponseItem) {
        return collect(questionnaireResponseItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, QuestionnaireResponse.Item.Answer questionnaireResponseItemAnswer) {
        return collect(questionnaireResponseItemAnswer);
    }

    @Override
    public boolean visit(java.lang.String elementName, Range range) {
        return collect(range);
    }

    @Override
    public boolean visit(java.lang.String elementName, Ratio ratio) {
        return collect(ratio);
    }

    @Override
    public boolean visit(java.lang.String elementName, Reference reference) {
        return collect(reference);
    }

    @Override
    public boolean visit(java.lang.String elementName, RelatedArtifact relatedArtifact) {
        return collect(relatedArtifact);
    }

    @Override
    public boolean visit(java.lang.String elementName, RelatedPerson relatedPerson) {
        return collect(relatedPerson);
    }

    @Override
    public boolean visit(java.lang.String elementName, RelatedPerson.Communication relatedPersonCommunication) {
        return collect(relatedPersonCommunication);
    }

    @Override
    public boolean visit(java.lang.String elementName, RequestGroup requestGroup) {
        return collect(requestGroup);
    }

    @Override
    public boolean visit(java.lang.String elementName, RequestGroup.Action requestGroupAction) {
        return collect(requestGroupAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, RequestGroup.Action.Condition requestGroupActionCondition) {
        return collect(requestGroupActionCondition);
    }

    @Override
    public boolean visit(java.lang.String elementName, RequestGroup.Action.RelatedAction requestGroupActionRelatedAction) {
        return collect(requestGroupActionRelatedAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchDefinition researchDefinition) {
        return collect(researchDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchElementDefinition researchElementDefinition) {
        return collect(researchElementDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchElementDefinition.Characteristic researchElementDefinitionCharacteristic) {
        return collect(researchElementDefinitionCharacteristic);
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchStudy researchStudy) {
        return collect(researchStudy);
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchStudy.Arm researchStudyArm) {
        return collect(researchStudyArm);
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchStudy.Objective researchStudyObjective) {
        return collect(researchStudyObjective);
    }

    @Override
    public boolean visit(java.lang.String elementName, ResearchSubject researchSubject) {
        return collect(researchSubject);
    }

    @Override
    public boolean visit(java.lang.String elementName, Resource resource) {
        return collect(resource);
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskAssessment riskAssessment) {
        return collect(riskAssessment);
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskAssessment.Prediction riskAssessmentPrediction) {
        return collect(riskAssessmentPrediction);
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis riskEvidenceSynthesis) {
        return collect(riskEvidenceSynthesis);
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.Certainty riskEvidenceSynthesisCertainty) {
        return collect(riskEvidenceSynthesisCertainty);
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.Certainty.CertaintySubcomponent riskEvidenceSynthesisCertaintyCertaintySubcomponent) {
        return collect(riskEvidenceSynthesisCertaintyCertaintySubcomponent);
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.RiskEstimate riskEvidenceSynthesisRiskEstimate) {
        return collect(riskEvidenceSynthesisRiskEstimate);
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.RiskEstimate.PrecisionEstimate riskEvidenceSynthesisRiskEstimatePrecisionEstimate) {
        return collect(riskEvidenceSynthesisRiskEstimatePrecisionEstimate);
    }

    @Override
    public boolean visit(java.lang.String elementName, RiskEvidenceSynthesis.SampleSize riskEvidenceSynthesisSampleSize) {
        return collect(riskEvidenceSynthesisSampleSize);
    }

    @Override
    public boolean visit(java.lang.String elementName, SampledData sampledData) {
        return collect(sampledData);
    }

    @Override
    public boolean visit(java.lang.String elementName, Schedule schedule) {
        return collect(schedule);
    }

    @Override
    public boolean visit(java.lang.String elementName, SearchParameter searchParameter) {
        return collect(searchParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, SearchParameter.Component searchParameterComponent) {
        return collect(searchParameterComponent);
    }

    @Override
    public boolean visit(java.lang.String elementName, ServiceRequest serviceRequest) {
        return collect(serviceRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, Signature signature) {
        return collect(signature);
    }

    @Override
    public boolean visit(java.lang.String elementName, SimpleQuantity simpleQuantity) {
        return collect(simpleQuantity);
    }

    @Override
    public boolean visit(java.lang.String elementName, Slot slot) {
        return collect(slot);
    }

    @Override
    public boolean visit(java.lang.String elementName, Specimen specimen) {
        return collect(specimen);
    }

    @Override
    public boolean visit(java.lang.String elementName, Specimen.Collection specimenCollection) {
        return collect(specimenCollection);
    }

    @Override
    public boolean visit(java.lang.String elementName, Specimen.Container specimenContainer) {
        return collect(specimenContainer);
    }

    @Override
    public boolean visit(java.lang.String elementName, Specimen.Processing specimenProcessing) {
        return collect(specimenProcessing);
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition specimenDefinition) {
        return collect(specimenDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition.TypeTested specimenDefinitionTypeTested) {
        return collect(specimenDefinitionTypeTested);
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition.TypeTested.Container specimenDefinitionTypeTestedContainer) {
        return collect(specimenDefinitionTypeTestedContainer);
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition.TypeTested.Container.Additive specimenDefinitionTypeTestedContainerAdditive) {
        return collect(specimenDefinitionTypeTestedContainerAdditive);
    }

    @Override
    public boolean visit(java.lang.String elementName, SpecimenDefinition.TypeTested.Handling specimenDefinitionTypeTestedHandling) {
        return collect(specimenDefinitionTypeTestedHandling);
    }

    @Override
    public boolean visit(java.lang.String elementName, String string) {
        return collect(string);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition structureDefinition) {
        return collect(structureDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition.Context structureDefinitionContext) {
        return collect(structureDefinitionContext);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition.Differential structureDefinitionDifferential) {
        return collect(structureDefinitionDifferential);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition.Mapping structureDefinitionMapping) {
        return collect(structureDefinitionMapping);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureDefinition.Snapshot structureDefinitionSnapshot) {
        return collect(structureDefinitionSnapshot);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap structureMap) {
        return collect(structureMap);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group structureMapGroup) {
        return collect(structureMapGroup);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Input structureMapGroupInput) {
        return collect(structureMapGroupInput);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule structureMapGroupRule) {
        return collect(structureMapGroupRule);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule.Dependent structureMapGroupRuleDependent) {
        return collect(structureMapGroupRuleDependent);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule.Source structureMapGroupRuleSource) {
        return collect(structureMapGroupRuleSource);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule.Target structureMapGroupRuleTarget) {
        return collect(structureMapGroupRuleTarget);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Group.Rule.Target.Parameter structureMapGroupRuleTargetParameter) {
        return collect(structureMapGroupRuleTargetParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, StructureMap.Structure structureMapStructure) {
        return collect(structureMapStructure);
    }

    @Override
    public boolean visit(java.lang.String elementName, Subscription subscription) {
        return collect(subscription);
    }

    @Override
    public boolean visit(java.lang.String elementName, Subscription.Channel subscriptionChannel) {
        return collect(subscriptionChannel);
    }

    @Override
    public boolean visit(java.lang.String elementName, Substance substance) {
        return collect(substance);
    }

    @Override
    public boolean visit(java.lang.String elementName, Substance.Ingredient substanceIngredient) {
        return collect(substanceIngredient);
    }

    @Override
    public boolean visit(java.lang.String elementName, Substance.Instance substanceInstance) {
        return collect(substanceInstance);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceAmount substanceAmount) {
        return collect(substanceAmount);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceAmount.ReferenceRange substanceAmountReferenceRange) {
        return collect(substanceAmountReferenceRange);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceNucleicAcid substanceNucleicAcid) {
        return collect(substanceNucleicAcid);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceNucleicAcid.Subunit substanceNucleicAcidSubunit) {
        return collect(substanceNucleicAcidSubunit);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceNucleicAcid.Subunit.Linkage substanceNucleicAcidSubunitLinkage) {
        return collect(substanceNucleicAcidSubunitLinkage);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceNucleicAcid.Subunit.Sugar substanceNucleicAcidSubunitSugar) {
        return collect(substanceNucleicAcidSubunitSugar);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer substancePolymer) {
        return collect(substancePolymer);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.MonomerSet substancePolymerMonomerSet) {
        return collect(substancePolymerMonomerSet);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.MonomerSet.StartingMaterial substancePolymerMonomerSetStartingMaterial) {
        return collect(substancePolymerMonomerSetStartingMaterial);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.Repeat substancePolymerRepeat) {
        return collect(substancePolymerRepeat);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.Repeat.RepeatUnit substancePolymerRepeatRepeatUnit) {
        return collect(substancePolymerRepeatRepeatUnit);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.Repeat.RepeatUnit.DegreeOfPolymerisation substancePolymerRepeatRepeatUnitDegreeOfPolymerisation) {
        return collect(substancePolymerRepeatRepeatUnitDegreeOfPolymerisation);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstancePolymer.Repeat.RepeatUnit.StructuralRepresentation substancePolymerRepeatRepeatUnitStructuralRepresentation) {
        return collect(substancePolymerRepeatRepeatUnitStructuralRepresentation);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceProtein substanceProtein) {
        return collect(substanceProtein);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceProtein.Subunit substanceProteinSubunit) {
        return collect(substanceProteinSubunit);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation substanceReferenceInformation) {
        return collect(substanceReferenceInformation);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation.Classification substanceReferenceInformationClassification) {
        return collect(substanceReferenceInformationClassification);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation.Gene substanceReferenceInformationGene) {
        return collect(substanceReferenceInformationGene);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation.GeneElement substanceReferenceInformationGeneElement) {
        return collect(substanceReferenceInformationGeneElement);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceReferenceInformation.Target substanceReferenceInformationTarget) {
        return collect(substanceReferenceInformationTarget);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial substanceSourceMaterial) {
        return collect(substanceSourceMaterial);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.FractionDescription substanceSourceMaterialFractionDescription) {
        return collect(substanceSourceMaterialFractionDescription);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.Organism substanceSourceMaterialOrganism) {
        return collect(substanceSourceMaterialOrganism);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.Organism.Author substanceSourceMaterialOrganismAuthor) {
        return collect(substanceSourceMaterialOrganismAuthor);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.Organism.Hybrid substanceSourceMaterialOrganismHybrid) {
        return collect(substanceSourceMaterialOrganismHybrid);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.Organism.OrganismGeneral substanceSourceMaterialOrganismOrganismGeneral) {
        return collect(substanceSourceMaterialOrganismOrganismGeneral);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSourceMaterial.PartDescription substanceSourceMaterialPartDescription) {
        return collect(substanceSourceMaterialPartDescription);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification substanceSpecification) {
        return collect(substanceSpecification);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Code substanceSpecificationCode) {
        return collect(substanceSpecificationCode);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Moiety substanceSpecificationMoiety) {
        return collect(substanceSpecificationMoiety);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Name substanceSpecificationName) {
        return collect(substanceSpecificationName);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Name.Official substanceSpecificationNameOfficial) {
        return collect(substanceSpecificationNameOfficial);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Property substanceSpecificationProperty) {
        return collect(substanceSpecificationProperty);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Relationship substanceSpecificationRelationship) {
        return collect(substanceSpecificationRelationship);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Structure substanceSpecificationStructure) {
        return collect(substanceSpecificationStructure);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Structure.Isotope substanceSpecificationStructureIsotope) {
        return collect(substanceSpecificationStructureIsotope);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Structure.Isotope.MolecularWeight substanceSpecificationStructureIsotopeMolecularWeight) {
        return collect(substanceSpecificationStructureIsotopeMolecularWeight);
    }

    @Override
    public boolean visit(java.lang.String elementName, SubstanceSpecification.Structure.Representation substanceSpecificationStructureRepresentation) {
        return collect(substanceSpecificationStructureRepresentation);
    }

    @Override
    public boolean visit(java.lang.String elementName, SupplyDelivery supplyDelivery) {
        return collect(supplyDelivery);
    }

    @Override
    public boolean visit(java.lang.String elementName, SupplyDelivery.SuppliedItem supplyDeliverySuppliedItem) {
        return collect(supplyDeliverySuppliedItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, SupplyRequest supplyRequest) {
        return collect(supplyRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, SupplyRequest.Parameter supplyRequestParameter) {
        return collect(supplyRequestParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, Task task) {
        return collect(task);
    }

    @Override
    public boolean visit(java.lang.String elementName, Task.Input taskInput) {
        return collect(taskInput);
    }

    @Override
    public boolean visit(java.lang.String elementName, Task.Output taskOutput) {
        return collect(taskOutput);
    }

    @Override
    public boolean visit(java.lang.String elementName, Task.Restriction taskRestriction) {
        return collect(taskRestriction);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities terminologyCapabilities) {
        return collect(terminologyCapabilities);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Closure terminologyCapabilitiesClosure) {
        return collect(terminologyCapabilitiesClosure);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.CodeSystem terminologyCapabilitiesCodeSystem) {
        return collect(terminologyCapabilitiesCodeSystem);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.CodeSystem.Version terminologyCapabilitiesCodeSystemVersion) {
        return collect(terminologyCapabilitiesCodeSystemVersion);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.CodeSystem.Version.Filter terminologyCapabilitiesCodeSystemVersionFilter) {
        return collect(terminologyCapabilitiesCodeSystemVersionFilter);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Expansion terminologyCapabilitiesExpansion) {
        return collect(terminologyCapabilitiesExpansion);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Expansion.Parameter terminologyCapabilitiesExpansionParameter) {
        return collect(terminologyCapabilitiesExpansionParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Implementation terminologyCapabilitiesImplementation) {
        return collect(terminologyCapabilitiesImplementation);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Software terminologyCapabilitiesSoftware) {
        return collect(terminologyCapabilitiesSoftware);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.Translation terminologyCapabilitiesTranslation) {
        return collect(terminologyCapabilitiesTranslation);
    }

    @Override
    public boolean visit(java.lang.String elementName, TerminologyCapabilities.ValidateCode terminologyCapabilitiesValidateCode) {
        return collect(terminologyCapabilitiesValidateCode);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport testReport) {
        return collect(testReport);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Participant testReportParticipant) {
        return collect(testReportParticipant);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Setup testReportSetup) {
        return collect(testReportSetup);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Setup.Action testReportSetupAction) {
        return collect(testReportSetupAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Setup.Action.Assert testReportSetupActionAssert) {
        return collect(testReportSetupActionAssert);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Setup.Action.Operation testReportSetupActionOperation) {
        return collect(testReportSetupActionOperation);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Teardown testReportTeardown) {
        return collect(testReportTeardown);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Teardown.Action testReportTeardownAction) {
        return collect(testReportTeardownAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Test testReportTest) {
        return collect(testReportTest);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestReport.Test.Action testReportTestAction) {
        return collect(testReportTestAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript testScript) {
        return collect(testScript);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Destination testScriptDestination) {
        return collect(testScriptDestination);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Fixture testScriptFixture) {
        return collect(testScriptFixture);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Metadata testScriptMetadata) {
        return collect(testScriptMetadata);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Metadata.Capability testScriptMetadataCapability) {
        return collect(testScriptMetadataCapability);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Metadata.Link testScriptMetadataLink) {
        return collect(testScriptMetadataLink);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Origin testScriptOrigin) {
        return collect(testScriptOrigin);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup testScriptSetup) {
        return collect(testScriptSetup);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup.Action testScriptSetupAction) {
        return collect(testScriptSetupAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup.Action.Assert testScriptSetupActionAssert) {
        return collect(testScriptSetupActionAssert);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup.Action.Operation testScriptSetupActionOperation) {
        return collect(testScriptSetupActionOperation);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Setup.Action.Operation.RequestHeader testScriptSetupActionOperationRequestHeader) {
        return collect(testScriptSetupActionOperationRequestHeader);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Teardown testScriptTeardown) {
        return collect(testScriptTeardown);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Teardown.Action testScriptTeardownAction) {
        return collect(testScriptTeardownAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Test testScriptTest) {
        return collect(testScriptTest);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Test.Action testScriptTestAction) {
        return collect(testScriptTestAction);
    }

    @Override
    public boolean visit(java.lang.String elementName, TestScript.Variable testScriptVariable) {
        return collect(testScriptVariable);
    }

    @Override
    public boolean visit(java.lang.String elementName, Time time) {
        return collect(time);
    }

    @Override
    public boolean visit(java.lang.String elementName, Timing timing) {
        return collect(timing);
    }

    @Override
    public boolean visit(java.lang.String elementName, Timing.Repeat timingRepeat) {
        return collect(timingRepeat);
    }

    @Override
    public boolean visit(java.lang.String elementName, TriggerDefinition triggerDefinition) {
        return collect(triggerDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, UnsignedInt unsignedInt) {
        return collect(unsignedInt);
    }

    @Override
    public boolean visit(java.lang.String elementName, Uri uri) {
        return collect(uri);
    }

    @Override
    public boolean visit(java.lang.String elementName, Url url) {
        return collect(url);
    }

    @Override
    public boolean visit(java.lang.String elementName, UsageContext usageContext) {
        return collect(usageContext);
    }

    @Override
    public boolean visit(java.lang.String elementName, Uuid uuid) {
        return collect(uuid);
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet valueSet) {
        return collect(valueSet);
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose valueSetCompose) {
        return collect(valueSetCompose);
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose.Include valueSetComposeInclude) {
        return collect(valueSetComposeInclude);
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose.Include.Concept valueSetComposeIncludeConcept) {
        return collect(valueSetComposeIncludeConcept);
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose.Include.Concept.Designation valueSetComposeIncludeConceptDesignation) {
        return collect(valueSetComposeIncludeConceptDesignation);
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Compose.Include.Filter valueSetComposeIncludeFilter) {
        return collect(valueSetComposeIncludeFilter);
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Expansion valueSetExpansion) {
        return collect(valueSetExpansion);
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Expansion.Contains valueSetExpansionContains) {
        return collect(valueSetExpansionContains);
    }

    @Override
    public boolean visit(java.lang.String elementName, ValueSet.Expansion.Parameter valueSetExpansionParameter) {
        return collect(valueSetExpansionParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, VerificationResult verificationResult) {
        return collect(verificationResult);
    }

    @Override
    public boolean visit(java.lang.String elementName, VerificationResult.Attestation verificationResultAttestation) {
        return collect(verificationResultAttestation);
    }

    @Override
    public boolean visit(java.lang.String elementName, VerificationResult.PrimarySource verificationResultPrimarySource) {
        return collect(verificationResultPrimarySource);
    }

    @Override
    public boolean visit(java.lang.String elementName, VerificationResult.Validator verificationResultValidator) {
        return collect(verificationResultValidator);
    }

    @Override
    public boolean visit(java.lang.String elementName, VisionPrescription visionPrescription) {
        return collect(visionPrescription);
    }

    @Override
    public boolean visit(java.lang.String elementName, VisionPrescription.LensSpecification visionPrescriptionLensSpecification) {
        return collect(visionPrescriptionLensSpecification);
    }

    @Override
    public boolean visit(java.lang.String elementName, VisionPrescription.LensSpecification.Prism visionPrescriptionLensSpecificationPrism) {
        return collect(visionPrescriptionLensSpecificationPrism);
    }

    @Override
    public void visit(java.lang.String elementName, byte[] value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, BigDecimal value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Boolean value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Integer value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, LocalDate value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, LocalTime value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.String value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, Year value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, YearMonth value) {
        collect(value);
    }

    @Override
    public void visit(java.lang.String elementName, ZonedDateTime value) {
        collect(value);
    }
}
