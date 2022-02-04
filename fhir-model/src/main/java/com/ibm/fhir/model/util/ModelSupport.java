/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.constraint.spi.ConstraintProvider;
import com.ibm.fhir.model.constraint.spi.ModelConstraintProvider;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.CodeableReference;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Contributor;
import com.ibm.fhir.model.type.Count;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Distance;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.MarketingStatus;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.MoneyQuantity;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.ParameterDefinition;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Population;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.ProdCharacteristic;
import com.ibm.fhir.model.type.ProductShelfLife;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.RatioRange;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.SampledData;
import com.ibm.fhir.model.type.Signature;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.TriggerDefinition;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.type.Xhtml;

public final class ModelSupport {
    private static final Logger log = Logger.getLogger(ModelSupport.class.getName());

    public static final Class<com.ibm.fhir.model.type.Boolean> FHIR_BOOLEAN = com.ibm.fhir.model.type.Boolean.class;
    public static final Class<com.ibm.fhir.model.type.Integer> FHIR_INTEGER = com.ibm.fhir.model.type.Integer.class;
    public static final Class<com.ibm.fhir.model.type.String> FHIR_STRING = com.ibm.fhir.model.type.String.class;
    public static final Class<com.ibm.fhir.model.type.Date> FHIR_DATE = com.ibm.fhir.model.type.Date.class;
    public static final Class<com.ibm.fhir.model.type.Instant> FHIR_INSTANT = com.ibm.fhir.model.type.Instant.class;

    private static final Map<Class<?>, Class<?>> CONCRETE_TYPE_MAP = buildConcreteTypeMap();
    private static final List<Class<?>> MODEL_CLASSES = Arrays.asList(
        com.ibm.fhir.model.resource.Account.class,
        com.ibm.fhir.model.resource.Account.Coverage.class,
        com.ibm.fhir.model.resource.Account.Guarantor.class,
        com.ibm.fhir.model.resource.ActivityDefinition.class,
        com.ibm.fhir.model.resource.ActivityDefinition.DynamicValue.class,
        com.ibm.fhir.model.resource.ActivityDefinition.Participant.class,
        com.ibm.fhir.model.resource.AdministrableProductDefinition.class,
        com.ibm.fhir.model.resource.AdministrableProductDefinition.Property.class,
        com.ibm.fhir.model.resource.AdministrableProductDefinition.RouteOfAdministration.class,
        com.ibm.fhir.model.resource.AdministrableProductDefinition.RouteOfAdministration.TargetSpecies.class,
        com.ibm.fhir.model.resource.AdministrableProductDefinition.RouteOfAdministration.TargetSpecies.WithdrawalPeriod.class,
        com.ibm.fhir.model.resource.AdverseEvent.class,
        com.ibm.fhir.model.resource.AdverseEvent.SuspectEntity.class,
        com.ibm.fhir.model.resource.AdverseEvent.SuspectEntity.Causality.class,
        com.ibm.fhir.model.resource.AllergyIntolerance.class,
        com.ibm.fhir.model.resource.AllergyIntolerance.Reaction.class,
        com.ibm.fhir.model.resource.Appointment.class,
        com.ibm.fhir.model.resource.Appointment.Participant.class,
        com.ibm.fhir.model.resource.AppointmentResponse.class,
        com.ibm.fhir.model.resource.AuditEvent.class,
        com.ibm.fhir.model.resource.AuditEvent.Agent.class,
        com.ibm.fhir.model.resource.AuditEvent.Agent.Network.class,
        com.ibm.fhir.model.resource.AuditEvent.Entity.class,
        com.ibm.fhir.model.resource.AuditEvent.Entity.Detail.class,
        com.ibm.fhir.model.resource.AuditEvent.Source.class,
        com.ibm.fhir.model.resource.Basic.class,
        com.ibm.fhir.model.resource.Binary.class,
        com.ibm.fhir.model.resource.BiologicallyDerivedProduct.class,
        com.ibm.fhir.model.resource.BiologicallyDerivedProduct.Collection.class,
        com.ibm.fhir.model.resource.BiologicallyDerivedProduct.Manipulation.class,
        com.ibm.fhir.model.resource.BiologicallyDerivedProduct.Processing.class,
        com.ibm.fhir.model.resource.BiologicallyDerivedProduct.Storage.class,
        com.ibm.fhir.model.resource.BodyStructure.class,
        com.ibm.fhir.model.resource.Bundle.class,
        com.ibm.fhir.model.resource.Bundle.Entry.class,
        com.ibm.fhir.model.resource.Bundle.Entry.Request.class,
        com.ibm.fhir.model.resource.Bundle.Entry.Response.class,
        com.ibm.fhir.model.resource.Bundle.Entry.Search.class,
        com.ibm.fhir.model.resource.Bundle.Link.class,
        com.ibm.fhir.model.resource.CapabilityStatement.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Document.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Implementation.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Messaging.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Messaging.Endpoint.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Messaging.SupportedMessage.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Rest.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Rest.Interaction.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource.Interaction.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource.Operation.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource.SearchParam.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Rest.Security.class,
        com.ibm.fhir.model.resource.CapabilityStatement.Software.class,
        com.ibm.fhir.model.resource.CarePlan.class,
        com.ibm.fhir.model.resource.CarePlan.Activity.class,
        com.ibm.fhir.model.resource.CarePlan.Activity.Detail.class,
        com.ibm.fhir.model.resource.CareTeam.class,
        com.ibm.fhir.model.resource.CareTeam.Participant.class,
        com.ibm.fhir.model.resource.CatalogEntry.class,
        com.ibm.fhir.model.resource.CatalogEntry.RelatedEntry.class,
        com.ibm.fhir.model.resource.ChargeItem.class,
        com.ibm.fhir.model.resource.ChargeItem.Performer.class,
        com.ibm.fhir.model.resource.ChargeItemDefinition.class,
        com.ibm.fhir.model.resource.ChargeItemDefinition.Applicability.class,
        com.ibm.fhir.model.resource.ChargeItemDefinition.PropertyGroup.class,
        com.ibm.fhir.model.resource.ChargeItemDefinition.PropertyGroup.PriceComponent.class,
        com.ibm.fhir.model.resource.Citation.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Abstract.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Classification.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Classification.WhoClassified.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Contributorship.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Contributorship.Entry.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Contributorship.Entry.AffiliationInfo.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Contributorship.Entry.ContributionInstance.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Contributorship.Summary.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Part.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.PublicationForm.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.PublicationForm.PeriodicRelease.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.PublicationForm.PeriodicRelease.DateOfPublication.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.PublicationForm.PublishedIn.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.RelatesTo.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.StatusDate.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Title.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.Version.class,
        com.ibm.fhir.model.resource.Citation.CitedArtifact.WebLocation.class,
        com.ibm.fhir.model.resource.Citation.Classification.class,
        com.ibm.fhir.model.resource.Citation.RelatesTo.class,
        com.ibm.fhir.model.resource.Citation.StatusDate.class,
        com.ibm.fhir.model.resource.Claim.class,
        com.ibm.fhir.model.resource.Claim.Accident.class,
        com.ibm.fhir.model.resource.Claim.CareTeam.class,
        com.ibm.fhir.model.resource.Claim.Diagnosis.class,
        com.ibm.fhir.model.resource.Claim.Insurance.class,
        com.ibm.fhir.model.resource.Claim.Item.class,
        com.ibm.fhir.model.resource.Claim.Item.Detail.class,
        com.ibm.fhir.model.resource.Claim.Item.Detail.SubDetail.class,
        com.ibm.fhir.model.resource.Claim.Payee.class,
        com.ibm.fhir.model.resource.Claim.Procedure.class,
        com.ibm.fhir.model.resource.Claim.Related.class,
        com.ibm.fhir.model.resource.Claim.SupportingInfo.class,
        com.ibm.fhir.model.resource.ClaimResponse.class,
        com.ibm.fhir.model.resource.ClaimResponse.AddItem.class,
        com.ibm.fhir.model.resource.ClaimResponse.AddItem.Detail.class,
        com.ibm.fhir.model.resource.ClaimResponse.AddItem.Detail.SubDetail.class,
        com.ibm.fhir.model.resource.ClaimResponse.Error.class,
        com.ibm.fhir.model.resource.ClaimResponse.Insurance.class,
        com.ibm.fhir.model.resource.ClaimResponse.Item.class,
        com.ibm.fhir.model.resource.ClaimResponse.Item.Adjudication.class,
        com.ibm.fhir.model.resource.ClaimResponse.Item.Detail.class,
        com.ibm.fhir.model.resource.ClaimResponse.Item.Detail.SubDetail.class,
        com.ibm.fhir.model.resource.ClaimResponse.Payment.class,
        com.ibm.fhir.model.resource.ClaimResponse.ProcessNote.class,
        com.ibm.fhir.model.resource.ClaimResponse.Total.class,
        com.ibm.fhir.model.resource.ClinicalImpression.class,
        com.ibm.fhir.model.resource.ClinicalImpression.Finding.class,
        com.ibm.fhir.model.resource.ClinicalImpression.Investigation.class,
        com.ibm.fhir.model.resource.ClinicalUseDefinition.class,
        com.ibm.fhir.model.resource.ClinicalUseDefinition.Contraindication.class,
        com.ibm.fhir.model.resource.ClinicalUseDefinition.Contraindication.OtherTherapy.class,
        com.ibm.fhir.model.resource.ClinicalUseDefinition.Indication.class,
        com.ibm.fhir.model.resource.ClinicalUseDefinition.Interaction.class,
        com.ibm.fhir.model.resource.ClinicalUseDefinition.Interaction.Interactant.class,
        com.ibm.fhir.model.resource.ClinicalUseDefinition.UndesirableEffect.class,
        com.ibm.fhir.model.resource.ClinicalUseDefinition.Warning.class,
        com.ibm.fhir.model.resource.CodeSystem.class,
        com.ibm.fhir.model.resource.CodeSystem.Concept.class,
        com.ibm.fhir.model.resource.CodeSystem.Concept.Designation.class,
        com.ibm.fhir.model.resource.CodeSystem.Concept.Property.class,
        com.ibm.fhir.model.resource.CodeSystem.Filter.class,
        com.ibm.fhir.model.resource.CodeSystem.Property.class,
        com.ibm.fhir.model.resource.Communication.class,
        com.ibm.fhir.model.resource.Communication.Payload.class,
        com.ibm.fhir.model.resource.CommunicationRequest.class,
        com.ibm.fhir.model.resource.CommunicationRequest.Payload.class,
        com.ibm.fhir.model.resource.CompartmentDefinition.class,
        com.ibm.fhir.model.resource.CompartmentDefinition.Resource.class,
        com.ibm.fhir.model.resource.Composition.class,
        com.ibm.fhir.model.resource.Composition.Attester.class,
        com.ibm.fhir.model.resource.Composition.Event.class,
        com.ibm.fhir.model.resource.Composition.RelatesTo.class,
        com.ibm.fhir.model.resource.Composition.Section.class,
        com.ibm.fhir.model.resource.ConceptMap.class,
        com.ibm.fhir.model.resource.ConceptMap.Group.class,
        com.ibm.fhir.model.resource.ConceptMap.Group.Element.class,
        com.ibm.fhir.model.resource.ConceptMap.Group.Element.Target.class,
        com.ibm.fhir.model.resource.ConceptMap.Group.Element.Target.DependsOn.class,
        com.ibm.fhir.model.resource.ConceptMap.Group.Unmapped.class,
        com.ibm.fhir.model.resource.Condition.class,
        com.ibm.fhir.model.resource.Condition.Evidence.class,
        com.ibm.fhir.model.resource.Condition.Stage.class,
        com.ibm.fhir.model.resource.Consent.class,
        com.ibm.fhir.model.resource.Consent.Policy.class,
        com.ibm.fhir.model.resource.Consent.Provision.class,
        com.ibm.fhir.model.resource.Consent.Provision.Actor.class,
        com.ibm.fhir.model.resource.Consent.Provision.Data.class,
        com.ibm.fhir.model.resource.Consent.Verification.class,
        com.ibm.fhir.model.resource.Contract.class,
        com.ibm.fhir.model.resource.Contract.ContentDefinition.class,
        com.ibm.fhir.model.resource.Contract.Friendly.class,
        com.ibm.fhir.model.resource.Contract.Legal.class,
        com.ibm.fhir.model.resource.Contract.Rule.class,
        com.ibm.fhir.model.resource.Contract.Signer.class,
        com.ibm.fhir.model.resource.Contract.Term.class,
        com.ibm.fhir.model.resource.Contract.Term.Action.class,
        com.ibm.fhir.model.resource.Contract.Term.Action.Subject.class,
        com.ibm.fhir.model.resource.Contract.Term.Asset.class,
        com.ibm.fhir.model.resource.Contract.Term.Asset.Context.class,
        com.ibm.fhir.model.resource.Contract.Term.Asset.ValuedItem.class,
        com.ibm.fhir.model.resource.Contract.Term.Offer.class,
        com.ibm.fhir.model.resource.Contract.Term.Offer.Answer.class,
        com.ibm.fhir.model.resource.Contract.Term.Offer.Party.class,
        com.ibm.fhir.model.resource.Contract.Term.SecurityLabel.class,
        com.ibm.fhir.model.resource.Coverage.class,
        com.ibm.fhir.model.resource.Coverage.Class.class,
        com.ibm.fhir.model.resource.Coverage.CostToBeneficiary.class,
        com.ibm.fhir.model.resource.Coverage.CostToBeneficiary.Exception.class,
        com.ibm.fhir.model.resource.CoverageEligibilityRequest.class,
        com.ibm.fhir.model.resource.CoverageEligibilityRequest.Insurance.class,
        com.ibm.fhir.model.resource.CoverageEligibilityRequest.Item.class,
        com.ibm.fhir.model.resource.CoverageEligibilityRequest.Item.Diagnosis.class,
        com.ibm.fhir.model.resource.CoverageEligibilityRequest.SupportingInfo.class,
        com.ibm.fhir.model.resource.CoverageEligibilityResponse.class,
        com.ibm.fhir.model.resource.CoverageEligibilityResponse.Error.class,
        com.ibm.fhir.model.resource.CoverageEligibilityResponse.Insurance.class,
        com.ibm.fhir.model.resource.CoverageEligibilityResponse.Insurance.Item.class,
        com.ibm.fhir.model.resource.CoverageEligibilityResponse.Insurance.Item.Benefit.class,
        com.ibm.fhir.model.resource.DetectedIssue.class,
        com.ibm.fhir.model.resource.DetectedIssue.Evidence.class,
        com.ibm.fhir.model.resource.DetectedIssue.Mitigation.class,
        com.ibm.fhir.model.resource.Device.class,
        com.ibm.fhir.model.resource.Device.DeviceName.class,
        com.ibm.fhir.model.resource.Device.Property.class,
        com.ibm.fhir.model.resource.Device.Specialization.class,
        com.ibm.fhir.model.resource.Device.UdiCarrier.class,
        com.ibm.fhir.model.resource.Device.Version.class,
        com.ibm.fhir.model.resource.DeviceDefinition.class,
        com.ibm.fhir.model.resource.DeviceDefinition.Capability.class,
        com.ibm.fhir.model.resource.DeviceDefinition.DeviceName.class,
        com.ibm.fhir.model.resource.DeviceDefinition.Material.class,
        com.ibm.fhir.model.resource.DeviceDefinition.Property.class,
        com.ibm.fhir.model.resource.DeviceDefinition.Specialization.class,
        com.ibm.fhir.model.resource.DeviceDefinition.UdiDeviceIdentifier.class,
        com.ibm.fhir.model.resource.DeviceMetric.class,
        com.ibm.fhir.model.resource.DeviceMetric.Calibration.class,
        com.ibm.fhir.model.resource.DeviceRequest.class,
        com.ibm.fhir.model.resource.DeviceRequest.Parameter.class,
        com.ibm.fhir.model.resource.DeviceUseStatement.class,
        com.ibm.fhir.model.resource.DiagnosticReport.class,
        com.ibm.fhir.model.resource.DiagnosticReport.Media.class,
        com.ibm.fhir.model.resource.DocumentManifest.class,
        com.ibm.fhir.model.resource.DocumentManifest.Related.class,
        com.ibm.fhir.model.resource.DocumentReference.class,
        com.ibm.fhir.model.resource.DocumentReference.Content.class,
        com.ibm.fhir.model.resource.DocumentReference.Context.class,
        com.ibm.fhir.model.resource.DocumentReference.RelatesTo.class,
        com.ibm.fhir.model.resource.DomainResource.class,
        com.ibm.fhir.model.resource.Encounter.class,
        com.ibm.fhir.model.resource.Encounter.ClassHistory.class,
        com.ibm.fhir.model.resource.Encounter.Diagnosis.class,
        com.ibm.fhir.model.resource.Encounter.Hospitalization.class,
        com.ibm.fhir.model.resource.Encounter.Location.class,
        com.ibm.fhir.model.resource.Encounter.Participant.class,
        com.ibm.fhir.model.resource.Encounter.StatusHistory.class,
        com.ibm.fhir.model.resource.Endpoint.class,
        com.ibm.fhir.model.resource.EnrollmentRequest.class,
        com.ibm.fhir.model.resource.EnrollmentResponse.class,
        com.ibm.fhir.model.resource.EpisodeOfCare.class,
        com.ibm.fhir.model.resource.EpisodeOfCare.Diagnosis.class,
        com.ibm.fhir.model.resource.EpisodeOfCare.StatusHistory.class,
        com.ibm.fhir.model.resource.EventDefinition.class,
        com.ibm.fhir.model.resource.Evidence.class,
        com.ibm.fhir.model.resource.Evidence.Certainty.class,
        com.ibm.fhir.model.resource.Evidence.Statistic.class,
        com.ibm.fhir.model.resource.Evidence.Statistic.AttributeEstimate.class,
        com.ibm.fhir.model.resource.Evidence.Statistic.ModelCharacteristic.class,
        com.ibm.fhir.model.resource.Evidence.Statistic.ModelCharacteristic.Variable.class,
        com.ibm.fhir.model.resource.Evidence.Statistic.SampleSize.class,
        com.ibm.fhir.model.resource.Evidence.VariableDefinition.class,
        com.ibm.fhir.model.resource.EvidenceReport.class,
        com.ibm.fhir.model.resource.EvidenceReport.RelatesTo.class,
        com.ibm.fhir.model.resource.EvidenceReport.Section.class,
        com.ibm.fhir.model.resource.EvidenceReport.Subject.class,
        com.ibm.fhir.model.resource.EvidenceReport.Subject.Characteristic.class,
        com.ibm.fhir.model.resource.EvidenceVariable.class,
        com.ibm.fhir.model.resource.EvidenceVariable.Category.class,
        com.ibm.fhir.model.resource.EvidenceVariable.Characteristic.class,
        com.ibm.fhir.model.resource.EvidenceVariable.Characteristic.TimeFromStart.class,
        com.ibm.fhir.model.resource.ExampleScenario.class,
        com.ibm.fhir.model.resource.ExampleScenario.Actor.class,
        com.ibm.fhir.model.resource.ExampleScenario.Instance.class,
        com.ibm.fhir.model.resource.ExampleScenario.Instance.ContainedInstance.class,
        com.ibm.fhir.model.resource.ExampleScenario.Instance.Version.class,
        com.ibm.fhir.model.resource.ExampleScenario.Process.class,
        com.ibm.fhir.model.resource.ExampleScenario.Process.Step.class,
        com.ibm.fhir.model.resource.ExampleScenario.Process.Step.Alternative.class,
        com.ibm.fhir.model.resource.ExampleScenario.Process.Step.Operation.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Accident.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.AddItem.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.AddItem.Detail.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.AddItem.Detail.SubDetail.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.BenefitBalance.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.BenefitBalance.Financial.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.CareTeam.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Diagnosis.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Insurance.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Item.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Item.Adjudication.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Item.Detail.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Item.Detail.SubDetail.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Payee.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Payment.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Procedure.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.ProcessNote.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Related.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.SupportingInfo.class,
        com.ibm.fhir.model.resource.ExplanationOfBenefit.Total.class,
        com.ibm.fhir.model.resource.FamilyMemberHistory.class,
        com.ibm.fhir.model.resource.FamilyMemberHistory.Condition.class,
        com.ibm.fhir.model.resource.Flag.class,
        com.ibm.fhir.model.resource.Goal.class,
        com.ibm.fhir.model.resource.Goal.Target.class,
        com.ibm.fhir.model.resource.GraphDefinition.class,
        com.ibm.fhir.model.resource.GraphDefinition.Link.class,
        com.ibm.fhir.model.resource.GraphDefinition.Link.Target.class,
        com.ibm.fhir.model.resource.GraphDefinition.Link.Target.Compartment.class,
        com.ibm.fhir.model.resource.Group.class,
        com.ibm.fhir.model.resource.Group.Characteristic.class,
        com.ibm.fhir.model.resource.Group.Member.class,
        com.ibm.fhir.model.resource.GuidanceResponse.class,
        com.ibm.fhir.model.resource.HealthcareService.class,
        com.ibm.fhir.model.resource.HealthcareService.AvailableTime.class,
        com.ibm.fhir.model.resource.HealthcareService.Eligibility.class,
        com.ibm.fhir.model.resource.HealthcareService.NotAvailable.class,
        com.ibm.fhir.model.resource.ImagingStudy.class,
        com.ibm.fhir.model.resource.ImagingStudy.Series.class,
        com.ibm.fhir.model.resource.ImagingStudy.Series.Instance.class,
        com.ibm.fhir.model.resource.ImagingStudy.Series.Performer.class,
        com.ibm.fhir.model.resource.Immunization.class,
        com.ibm.fhir.model.resource.Immunization.Education.class,
        com.ibm.fhir.model.resource.Immunization.Performer.class,
        com.ibm.fhir.model.resource.Immunization.ProtocolApplied.class,
        com.ibm.fhir.model.resource.Immunization.Reaction.class,
        com.ibm.fhir.model.resource.ImmunizationEvaluation.class,
        com.ibm.fhir.model.resource.ImmunizationRecommendation.class,
        com.ibm.fhir.model.resource.ImmunizationRecommendation.Recommendation.class,
        com.ibm.fhir.model.resource.ImmunizationRecommendation.Recommendation.DateCriterion.class,
        com.ibm.fhir.model.resource.ImplementationGuide.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Definition.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Definition.Grouping.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Definition.Page.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Definition.Parameter.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Definition.Resource.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Definition.Template.class,
        com.ibm.fhir.model.resource.ImplementationGuide.DependsOn.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Global.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Manifest.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Manifest.Page.class,
        com.ibm.fhir.model.resource.ImplementationGuide.Manifest.Resource.class,
        com.ibm.fhir.model.resource.Ingredient.class,
        com.ibm.fhir.model.resource.Ingredient.Manufacturer.class,
        com.ibm.fhir.model.resource.Ingredient.Substance.class,
        com.ibm.fhir.model.resource.Ingredient.Substance.Strength.class,
        com.ibm.fhir.model.resource.Ingredient.Substance.Strength.ReferenceStrength.class,
        com.ibm.fhir.model.resource.InsurancePlan.class,
        com.ibm.fhir.model.resource.InsurancePlan.Contact.class,
        com.ibm.fhir.model.resource.InsurancePlan.Coverage.class,
        com.ibm.fhir.model.resource.InsurancePlan.Coverage.Benefit.class,
        com.ibm.fhir.model.resource.InsurancePlan.Coverage.Benefit.Limit.class,
        com.ibm.fhir.model.resource.InsurancePlan.Plan.class,
        com.ibm.fhir.model.resource.InsurancePlan.Plan.GeneralCost.class,
        com.ibm.fhir.model.resource.InsurancePlan.Plan.SpecificCost.class,
        com.ibm.fhir.model.resource.InsurancePlan.Plan.SpecificCost.Benefit.class,
        com.ibm.fhir.model.resource.InsurancePlan.Plan.SpecificCost.Benefit.Cost.class,
        com.ibm.fhir.model.resource.Invoice.class,
        com.ibm.fhir.model.resource.Invoice.LineItem.class,
        com.ibm.fhir.model.resource.Invoice.LineItem.PriceComponent.class,
        com.ibm.fhir.model.resource.Invoice.Participant.class,
        com.ibm.fhir.model.resource.Library.class,
        com.ibm.fhir.model.resource.Linkage.class,
        com.ibm.fhir.model.resource.Linkage.Item.class,
        com.ibm.fhir.model.resource.List.class,
        com.ibm.fhir.model.resource.List.Entry.class,
        com.ibm.fhir.model.resource.Location.class,
        com.ibm.fhir.model.resource.Location.HoursOfOperation.class,
        com.ibm.fhir.model.resource.Location.Position.class,
        com.ibm.fhir.model.resource.ManufacturedItemDefinition.class,
        com.ibm.fhir.model.resource.ManufacturedItemDefinition.Property.class,
        com.ibm.fhir.model.resource.Measure.class,
        com.ibm.fhir.model.resource.Measure.Group.class,
        com.ibm.fhir.model.resource.Measure.Group.Population.class,
        com.ibm.fhir.model.resource.Measure.Group.Stratifier.class,
        com.ibm.fhir.model.resource.Measure.Group.Stratifier.Component.class,
        com.ibm.fhir.model.resource.Measure.SupplementalData.class,
        com.ibm.fhir.model.resource.MeasureReport.class,
        com.ibm.fhir.model.resource.MeasureReport.Group.class,
        com.ibm.fhir.model.resource.MeasureReport.Group.Population.class,
        com.ibm.fhir.model.resource.MeasureReport.Group.Stratifier.class,
        com.ibm.fhir.model.resource.MeasureReport.Group.Stratifier.Stratum.class,
        com.ibm.fhir.model.resource.MeasureReport.Group.Stratifier.Stratum.Component.class,
        com.ibm.fhir.model.resource.MeasureReport.Group.Stratifier.Stratum.Population.class,
        com.ibm.fhir.model.resource.Media.class,
        com.ibm.fhir.model.resource.Medication.class,
        com.ibm.fhir.model.resource.Medication.Batch.class,
        com.ibm.fhir.model.resource.Medication.Ingredient.class,
        com.ibm.fhir.model.resource.MedicationAdministration.class,
        com.ibm.fhir.model.resource.MedicationAdministration.Dosage.class,
        com.ibm.fhir.model.resource.MedicationAdministration.Performer.class,
        com.ibm.fhir.model.resource.MedicationDispense.class,
        com.ibm.fhir.model.resource.MedicationDispense.Performer.class,
        com.ibm.fhir.model.resource.MedicationDispense.Substitution.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.AdministrationGuidelines.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.AdministrationGuidelines.Dosage.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.AdministrationGuidelines.PatientCharacteristics.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.Cost.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.DrugCharacteristic.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.Ingredient.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.Kinetics.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.MedicineClassification.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.MonitoringProgram.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.Monograph.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.Packaging.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.Regulatory.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.Regulatory.MaxDispense.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.Regulatory.Schedule.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.Regulatory.Substitution.class,
        com.ibm.fhir.model.resource.MedicationKnowledge.RelatedMedicationKnowledge.class,
        com.ibm.fhir.model.resource.MedicationRequest.class,
        com.ibm.fhir.model.resource.MedicationRequest.DispenseRequest.class,
        com.ibm.fhir.model.resource.MedicationRequest.DispenseRequest.InitialFill.class,
        com.ibm.fhir.model.resource.MedicationRequest.Substitution.class,
        com.ibm.fhir.model.resource.MedicationStatement.class,
        com.ibm.fhir.model.resource.MedicinalProductDefinition.class,
        com.ibm.fhir.model.resource.MedicinalProductDefinition.Characteristic.class,
        com.ibm.fhir.model.resource.MedicinalProductDefinition.Contact.class,
        com.ibm.fhir.model.resource.MedicinalProductDefinition.CrossReference.class,
        com.ibm.fhir.model.resource.MedicinalProductDefinition.Name.class,
        com.ibm.fhir.model.resource.MedicinalProductDefinition.Name.CountryLanguage.class,
        com.ibm.fhir.model.resource.MedicinalProductDefinition.Name.NamePart.class,
        com.ibm.fhir.model.resource.MedicinalProductDefinition.Operation.class,
        com.ibm.fhir.model.resource.MessageDefinition.class,
        com.ibm.fhir.model.resource.MessageDefinition.AllowedResponse.class,
        com.ibm.fhir.model.resource.MessageDefinition.Focus.class,
        com.ibm.fhir.model.resource.MessageHeader.class,
        com.ibm.fhir.model.resource.MessageHeader.Destination.class,
        com.ibm.fhir.model.resource.MessageHeader.Response.class,
        com.ibm.fhir.model.resource.MessageHeader.Source.class,
        com.ibm.fhir.model.resource.MolecularSequence.class,
        com.ibm.fhir.model.resource.MolecularSequence.Quality.class,
        com.ibm.fhir.model.resource.MolecularSequence.Quality.Roc.class,
        com.ibm.fhir.model.resource.MolecularSequence.ReferenceSeq.class,
        com.ibm.fhir.model.resource.MolecularSequence.Repository.class,
        com.ibm.fhir.model.resource.MolecularSequence.StructureVariant.class,
        com.ibm.fhir.model.resource.MolecularSequence.StructureVariant.Inner.class,
        com.ibm.fhir.model.resource.MolecularSequence.StructureVariant.Outer.class,
        com.ibm.fhir.model.resource.MolecularSequence.Variant.class,
        com.ibm.fhir.model.resource.NamingSystem.class,
        com.ibm.fhir.model.resource.NamingSystem.UniqueId.class,
        com.ibm.fhir.model.resource.NutritionOrder.class,
        com.ibm.fhir.model.resource.NutritionOrder.EnteralFormula.class,
        com.ibm.fhir.model.resource.NutritionOrder.EnteralFormula.Administration.class,
        com.ibm.fhir.model.resource.NutritionOrder.OralDiet.class,
        com.ibm.fhir.model.resource.NutritionOrder.OralDiet.Nutrient.class,
        com.ibm.fhir.model.resource.NutritionOrder.OralDiet.Texture.class,
        com.ibm.fhir.model.resource.NutritionOrder.Supplement.class,
        com.ibm.fhir.model.resource.NutritionProduct.class,
        com.ibm.fhir.model.resource.NutritionProduct.Ingredient.class,
        com.ibm.fhir.model.resource.NutritionProduct.Instance.class,
        com.ibm.fhir.model.resource.NutritionProduct.Nutrient.class,
        com.ibm.fhir.model.resource.NutritionProduct.ProductCharacteristic.class,
        com.ibm.fhir.model.resource.Observation.class,
        com.ibm.fhir.model.resource.Observation.Component.class,
        com.ibm.fhir.model.resource.Observation.ReferenceRange.class,
        com.ibm.fhir.model.resource.ObservationDefinition.class,
        com.ibm.fhir.model.resource.ObservationDefinition.QualifiedInterval.class,
        com.ibm.fhir.model.resource.ObservationDefinition.QuantitativeDetails.class,
        com.ibm.fhir.model.resource.OperationDefinition.class,
        com.ibm.fhir.model.resource.OperationDefinition.Overload.class,
        com.ibm.fhir.model.resource.OperationDefinition.Parameter.class,
        com.ibm.fhir.model.resource.OperationDefinition.Parameter.Binding.class,
        com.ibm.fhir.model.resource.OperationDefinition.Parameter.ReferencedFrom.class,
        com.ibm.fhir.model.resource.OperationOutcome.class,
        com.ibm.fhir.model.resource.OperationOutcome.Issue.class,
        com.ibm.fhir.model.resource.Organization.class,
        com.ibm.fhir.model.resource.Organization.Contact.class,
        com.ibm.fhir.model.resource.OrganizationAffiliation.class,
        com.ibm.fhir.model.resource.PackagedProductDefinition.class,
        com.ibm.fhir.model.resource.PackagedProductDefinition.LegalStatusOfSupply.class,
        com.ibm.fhir.model.resource.PackagedProductDefinition.Package.class,
        com.ibm.fhir.model.resource.PackagedProductDefinition.Package.ContainedItem.class,
        com.ibm.fhir.model.resource.PackagedProductDefinition.Package.Property.class,
        com.ibm.fhir.model.resource.Parameters.class,
        com.ibm.fhir.model.resource.Parameters.Parameter.class,
        com.ibm.fhir.model.resource.Patient.class,
        com.ibm.fhir.model.resource.Patient.Communication.class,
        com.ibm.fhir.model.resource.Patient.Contact.class,
        com.ibm.fhir.model.resource.Patient.Link.class,
        com.ibm.fhir.model.resource.PaymentNotice.class,
        com.ibm.fhir.model.resource.PaymentReconciliation.class,
        com.ibm.fhir.model.resource.PaymentReconciliation.Detail.class,
        com.ibm.fhir.model.resource.PaymentReconciliation.ProcessNote.class,
        com.ibm.fhir.model.resource.Person.class,
        com.ibm.fhir.model.resource.Person.Link.class,
        com.ibm.fhir.model.resource.PlanDefinition.class,
        com.ibm.fhir.model.resource.PlanDefinition.Action.class,
        com.ibm.fhir.model.resource.PlanDefinition.Action.Condition.class,
        com.ibm.fhir.model.resource.PlanDefinition.Action.DynamicValue.class,
        com.ibm.fhir.model.resource.PlanDefinition.Action.Participant.class,
        com.ibm.fhir.model.resource.PlanDefinition.Action.RelatedAction.class,
        com.ibm.fhir.model.resource.PlanDefinition.Goal.class,
        com.ibm.fhir.model.resource.PlanDefinition.Goal.Target.class,
        com.ibm.fhir.model.resource.Practitioner.class,
        com.ibm.fhir.model.resource.Practitioner.Qualification.class,
        com.ibm.fhir.model.resource.PractitionerRole.class,
        com.ibm.fhir.model.resource.PractitionerRole.AvailableTime.class,
        com.ibm.fhir.model.resource.PractitionerRole.NotAvailable.class,
        com.ibm.fhir.model.resource.Procedure.class,
        com.ibm.fhir.model.resource.Procedure.FocalDevice.class,
        com.ibm.fhir.model.resource.Procedure.Performer.class,
        com.ibm.fhir.model.resource.Provenance.class,
        com.ibm.fhir.model.resource.Provenance.Agent.class,
        com.ibm.fhir.model.resource.Provenance.Entity.class,
        com.ibm.fhir.model.resource.Questionnaire.class,
        com.ibm.fhir.model.resource.Questionnaire.Item.class,
        com.ibm.fhir.model.resource.Questionnaire.Item.AnswerOption.class,
        com.ibm.fhir.model.resource.Questionnaire.Item.EnableWhen.class,
        com.ibm.fhir.model.resource.Questionnaire.Item.Initial.class,
        com.ibm.fhir.model.resource.QuestionnaireResponse.class,
        com.ibm.fhir.model.resource.QuestionnaireResponse.Item.class,
        com.ibm.fhir.model.resource.QuestionnaireResponse.Item.Answer.class,
        com.ibm.fhir.model.resource.RegulatedAuthorization.class,
        com.ibm.fhir.model.resource.RegulatedAuthorization.Case.class,
        com.ibm.fhir.model.resource.RelatedPerson.class,
        com.ibm.fhir.model.resource.RelatedPerson.Communication.class,
        com.ibm.fhir.model.resource.RequestGroup.class,
        com.ibm.fhir.model.resource.RequestGroup.Action.class,
        com.ibm.fhir.model.resource.RequestGroup.Action.Condition.class,
        com.ibm.fhir.model.resource.RequestGroup.Action.RelatedAction.class,
        com.ibm.fhir.model.resource.ResearchDefinition.class,
        com.ibm.fhir.model.resource.ResearchElementDefinition.class,
        com.ibm.fhir.model.resource.ResearchElementDefinition.Characteristic.class,
        com.ibm.fhir.model.resource.ResearchStudy.class,
        com.ibm.fhir.model.resource.ResearchStudy.Arm.class,
        com.ibm.fhir.model.resource.ResearchStudy.Objective.class,
        com.ibm.fhir.model.resource.ResearchSubject.class,
        com.ibm.fhir.model.resource.Resource.class,
        com.ibm.fhir.model.resource.RiskAssessment.class,
        com.ibm.fhir.model.resource.RiskAssessment.Prediction.class,
        com.ibm.fhir.model.resource.Schedule.class,
        com.ibm.fhir.model.resource.SearchParameter.class,
        com.ibm.fhir.model.resource.SearchParameter.Component.class,
        com.ibm.fhir.model.resource.ServiceRequest.class,
        com.ibm.fhir.model.resource.Slot.class,
        com.ibm.fhir.model.resource.Specimen.class,
        com.ibm.fhir.model.resource.Specimen.Collection.class,
        com.ibm.fhir.model.resource.Specimen.Container.class,
        com.ibm.fhir.model.resource.Specimen.Processing.class,
        com.ibm.fhir.model.resource.SpecimenDefinition.class,
        com.ibm.fhir.model.resource.SpecimenDefinition.TypeTested.class,
        com.ibm.fhir.model.resource.SpecimenDefinition.TypeTested.Container.class,
        com.ibm.fhir.model.resource.SpecimenDefinition.TypeTested.Container.Additive.class,
        com.ibm.fhir.model.resource.SpecimenDefinition.TypeTested.Handling.class,
        com.ibm.fhir.model.resource.StructureDefinition.class,
        com.ibm.fhir.model.resource.StructureDefinition.Context.class,
        com.ibm.fhir.model.resource.StructureDefinition.Differential.class,
        com.ibm.fhir.model.resource.StructureDefinition.Mapping.class,
        com.ibm.fhir.model.resource.StructureDefinition.Snapshot.class,
        com.ibm.fhir.model.resource.StructureMap.class,
        com.ibm.fhir.model.resource.StructureMap.Group.class,
        com.ibm.fhir.model.resource.StructureMap.Group.Input.class,
        com.ibm.fhir.model.resource.StructureMap.Group.Rule.class,
        com.ibm.fhir.model.resource.StructureMap.Group.Rule.Dependent.class,
        com.ibm.fhir.model.resource.StructureMap.Group.Rule.Source.class,
        com.ibm.fhir.model.resource.StructureMap.Group.Rule.Target.class,
        com.ibm.fhir.model.resource.StructureMap.Group.Rule.Target.Parameter.class,
        com.ibm.fhir.model.resource.StructureMap.Structure.class,
        com.ibm.fhir.model.resource.Subscription.class,
        com.ibm.fhir.model.resource.Subscription.Channel.class,
        com.ibm.fhir.model.resource.SubscriptionStatus.class,
        com.ibm.fhir.model.resource.SubscriptionStatus.NotificationEvent.class,
        com.ibm.fhir.model.resource.SubscriptionTopic.class,
        com.ibm.fhir.model.resource.SubscriptionTopic.CanFilterBy.class,
        com.ibm.fhir.model.resource.SubscriptionTopic.EventTrigger.class,
        com.ibm.fhir.model.resource.SubscriptionTopic.NotificationShape.class,
        com.ibm.fhir.model.resource.SubscriptionTopic.ResourceTrigger.class,
        com.ibm.fhir.model.resource.SubscriptionTopic.ResourceTrigger.QueryCriteria.class,
        com.ibm.fhir.model.resource.Substance.class,
        com.ibm.fhir.model.resource.Substance.Ingredient.class,
        com.ibm.fhir.model.resource.Substance.Instance.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.Code.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.Moiety.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.MolecularWeight.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.Name.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.Name.Official.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.Property.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.Relationship.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.SourceMaterial.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.Structure.class,
        com.ibm.fhir.model.resource.SubstanceDefinition.Structure.Representation.class,
        com.ibm.fhir.model.resource.SupplyDelivery.class,
        com.ibm.fhir.model.resource.SupplyDelivery.SuppliedItem.class,
        com.ibm.fhir.model.resource.SupplyRequest.class,
        com.ibm.fhir.model.resource.SupplyRequest.Parameter.class,
        com.ibm.fhir.model.resource.Task.class,
        com.ibm.fhir.model.resource.Task.Input.class,
        com.ibm.fhir.model.resource.Task.Output.class,
        com.ibm.fhir.model.resource.Task.Restriction.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.Closure.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.CodeSystem.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.CodeSystem.Version.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.CodeSystem.Version.Filter.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.Expansion.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.Expansion.Parameter.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.Implementation.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.Software.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.Translation.class,
        com.ibm.fhir.model.resource.TerminologyCapabilities.ValidateCode.class,
        com.ibm.fhir.model.resource.TestReport.class,
        com.ibm.fhir.model.resource.TestReport.Participant.class,
        com.ibm.fhir.model.resource.TestReport.Setup.class,
        com.ibm.fhir.model.resource.TestReport.Setup.Action.class,
        com.ibm.fhir.model.resource.TestReport.Setup.Action.Assert.class,
        com.ibm.fhir.model.resource.TestReport.Setup.Action.Operation.class,
        com.ibm.fhir.model.resource.TestReport.Teardown.class,
        com.ibm.fhir.model.resource.TestReport.Teardown.Action.class,
        com.ibm.fhir.model.resource.TestReport.Test.class,
        com.ibm.fhir.model.resource.TestReport.Test.Action.class,
        com.ibm.fhir.model.resource.TestScript.class,
        com.ibm.fhir.model.resource.TestScript.Destination.class,
        com.ibm.fhir.model.resource.TestScript.Fixture.class,
        com.ibm.fhir.model.resource.TestScript.Metadata.class,
        com.ibm.fhir.model.resource.TestScript.Metadata.Capability.class,
        com.ibm.fhir.model.resource.TestScript.Metadata.Link.class,
        com.ibm.fhir.model.resource.TestScript.Origin.class,
        com.ibm.fhir.model.resource.TestScript.Setup.class,
        com.ibm.fhir.model.resource.TestScript.Setup.Action.class,
        com.ibm.fhir.model.resource.TestScript.Setup.Action.Assert.class,
        com.ibm.fhir.model.resource.TestScript.Setup.Action.Operation.class,
        com.ibm.fhir.model.resource.TestScript.Setup.Action.Operation.RequestHeader.class,
        com.ibm.fhir.model.resource.TestScript.Teardown.class,
        com.ibm.fhir.model.resource.TestScript.Teardown.Action.class,
        com.ibm.fhir.model.resource.TestScript.Test.class,
        com.ibm.fhir.model.resource.TestScript.Test.Action.class,
        com.ibm.fhir.model.resource.TestScript.Variable.class,
        com.ibm.fhir.model.resource.ValueSet.class,
        com.ibm.fhir.model.resource.ValueSet.Compose.class,
        com.ibm.fhir.model.resource.ValueSet.Compose.Include.class,
        com.ibm.fhir.model.resource.ValueSet.Compose.Include.Concept.class,
        com.ibm.fhir.model.resource.ValueSet.Compose.Include.Concept.Designation.class,
        com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter.class,
        com.ibm.fhir.model.resource.ValueSet.Expansion.class,
        com.ibm.fhir.model.resource.ValueSet.Expansion.Contains.class,
        com.ibm.fhir.model.resource.ValueSet.Expansion.Parameter.class,
        com.ibm.fhir.model.resource.VerificationResult.class,
        com.ibm.fhir.model.resource.VerificationResult.Attestation.class,
        com.ibm.fhir.model.resource.VerificationResult.PrimarySource.class,
        com.ibm.fhir.model.resource.VerificationResult.Validator.class,
        com.ibm.fhir.model.resource.VisionPrescription.class,
        com.ibm.fhir.model.resource.VisionPrescription.LensSpecification.class,
        com.ibm.fhir.model.resource.VisionPrescription.LensSpecification.Prism.class,
        com.ibm.fhir.model.type.Address.class,
        com.ibm.fhir.model.type.Age.class,
        com.ibm.fhir.model.type.Annotation.class,
        com.ibm.fhir.model.type.Attachment.class,
        com.ibm.fhir.model.type.BackboneElement.class,
        com.ibm.fhir.model.type.Base64Binary.class,
        com.ibm.fhir.model.type.Boolean.class,
        com.ibm.fhir.model.type.Canonical.class,
        com.ibm.fhir.model.type.Code.class,
        com.ibm.fhir.model.type.CodeableConcept.class,
        com.ibm.fhir.model.type.CodeableReference.class,
        com.ibm.fhir.model.type.Coding.class,
        com.ibm.fhir.model.type.ContactDetail.class,
        com.ibm.fhir.model.type.ContactPoint.class,
        com.ibm.fhir.model.type.Contributor.class,
        com.ibm.fhir.model.type.Count.class,
        com.ibm.fhir.model.type.DataRequirement.class,
        com.ibm.fhir.model.type.DataRequirement.CodeFilter.class,
        com.ibm.fhir.model.type.DataRequirement.DateFilter.class,
        com.ibm.fhir.model.type.DataRequirement.Sort.class,
        com.ibm.fhir.model.type.DataType.class,
        com.ibm.fhir.model.type.Date.class,
        com.ibm.fhir.model.type.DateTime.class,
        com.ibm.fhir.model.type.Decimal.class,
        com.ibm.fhir.model.type.Distance.class,
        com.ibm.fhir.model.type.Dosage.class,
        com.ibm.fhir.model.type.Dosage.DoseAndRate.class,
        com.ibm.fhir.model.type.Duration.class,
        com.ibm.fhir.model.type.Element.class,
        com.ibm.fhir.model.type.ElementDefinition.class,
        com.ibm.fhir.model.type.ElementDefinition.Base.class,
        com.ibm.fhir.model.type.ElementDefinition.Binding.class,
        com.ibm.fhir.model.type.ElementDefinition.Constraint.class,
        com.ibm.fhir.model.type.ElementDefinition.Example.class,
        com.ibm.fhir.model.type.ElementDefinition.Mapping.class,
        com.ibm.fhir.model.type.ElementDefinition.Slicing.class,
        com.ibm.fhir.model.type.ElementDefinition.Slicing.Discriminator.class,
        com.ibm.fhir.model.type.ElementDefinition.Type.class,
        com.ibm.fhir.model.type.Expression.class,
        com.ibm.fhir.model.type.Extension.class,
        com.ibm.fhir.model.type.HumanName.class,
        com.ibm.fhir.model.type.Id.class,
        com.ibm.fhir.model.type.Identifier.class,
        com.ibm.fhir.model.type.Instant.class,
        com.ibm.fhir.model.type.Integer.class,
        com.ibm.fhir.model.type.Markdown.class,
        com.ibm.fhir.model.type.MarketingStatus.class,
        com.ibm.fhir.model.type.Meta.class,
        com.ibm.fhir.model.type.Money.class,
        com.ibm.fhir.model.type.MoneyQuantity.class,
        com.ibm.fhir.model.type.Narrative.class,
        com.ibm.fhir.model.type.Oid.class,
        com.ibm.fhir.model.type.ParameterDefinition.class,
        com.ibm.fhir.model.type.Period.class,
        com.ibm.fhir.model.type.Population.class,
        com.ibm.fhir.model.type.PositiveInt.class,
        com.ibm.fhir.model.type.ProdCharacteristic.class,
        com.ibm.fhir.model.type.ProductShelfLife.class,
        com.ibm.fhir.model.type.Quantity.class,
        com.ibm.fhir.model.type.Range.class,
        com.ibm.fhir.model.type.Ratio.class,
        com.ibm.fhir.model.type.RatioRange.class,
        com.ibm.fhir.model.type.Reference.class,
        com.ibm.fhir.model.type.RelatedArtifact.class,
        com.ibm.fhir.model.type.SampledData.class,
        com.ibm.fhir.model.type.Signature.class,
        com.ibm.fhir.model.type.SimpleQuantity.class,
        com.ibm.fhir.model.type.String.class,
        com.ibm.fhir.model.type.Time.class,
        com.ibm.fhir.model.type.Timing.class,
        com.ibm.fhir.model.type.Timing.Repeat.class,
        com.ibm.fhir.model.type.TriggerDefinition.class,
        com.ibm.fhir.model.type.UnsignedInt.class,
        com.ibm.fhir.model.type.Uri.class,
        com.ibm.fhir.model.type.Url.class,
        com.ibm.fhir.model.type.UsageContext.class,
        com.ibm.fhir.model.type.Uuid.class,
        com.ibm.fhir.model.type.Xhtml.class
            );
    private static final Map<Class<?>, Map<String, ElementInfo>> MODEL_CLASS_ELEMENT_INFO_MAP = buildModelClassElementInfoMap();
    private static final Map<String, Class<? extends Resource>> RESOURCE_TYPE_MAP = buildResourceTypeMap();
    private static final Set<Class<? extends Resource>> CONCRETE_RESOURCE_TYPES = getResourceTypes().stream()
            .filter(rt -> !isAbstract(rt))
            .collect(Collectors.toSet());
    private static final Map<Class<?>, List<Constraint>> MODEL_CLASS_CONSTRAINT_MAP = buildModelClassConstraintMap();
    // LinkedHashSet is used just to preserve the order, for convenience only
    private static final Set<Class<? extends Element>> CHOICE_ELEMENT_TYPES = new LinkedHashSet<>(Arrays.asList(
            Base64Binary.class,
            com.ibm.fhir.model.type.Boolean.class,
            Canonical.class,
            Code.class,
            Date.class,
            DateTime.class,
            Decimal.class,
            Id.class,
            Instant.class,
            com.ibm.fhir.model.type.Integer.class,
            Markdown.class,
            Oid.class,
            PositiveInt.class,
            com.ibm.fhir.model.type.String.class,
            Time.class,
            UnsignedInt.class,
            Uri.class,
            Url.class,
            Uuid.class,
            Address.class,
            Age.class,
            Annotation.class,
            Attachment.class,
            CodeableConcept.class,
            CodeableReference.class,
            Coding.class,
            ContactPoint.class,
            Count.class,
            Distance.class,
            Duration.class,
            HumanName.class,
            Identifier.class,
            Money.class,
            MoneyQuantity.class, // profiled type
            Period.class,
            Quantity.class,
            Range.class,
            Ratio.class,
            RatioRange.class,
            Reference.class,
            SampledData.class,
            SimpleQuantity.class, // profiled type
            Signature.class,
            Timing.class,
            ContactDetail.class,
            Contributor.class,
            DataRequirement.class,
            Expression.class,
            ParameterDefinition.class,
            RelatedArtifact.class,
            TriggerDefinition.class,
            UsageContext.class,
            Dosage.class,
            Meta.class));
    private static final Set<Class<? extends Element>> DATA_TYPES;
    static {
        // LinkedHashSet is used just to preserve the order, for convenience only
        Set<Class<? extends Element>> dataTypes = new LinkedHashSet<>(CHOICE_ELEMENT_TYPES);
        dataTypes.add(Xhtml.class);
        dataTypes.add(Narrative.class);
        dataTypes.add(Extension.class);
        dataTypes.add(ElementDefinition.class);
        dataTypes.add(MarketingStatus.class);
        dataTypes.add(Population.class);
        dataTypes.add(ProductShelfLife.class);
        dataTypes.add(ProdCharacteristic.class);
        DATA_TYPES = Collections.unmodifiableSet(dataTypes);
    }
    private static final Map<String, Class<?>> DATA_TYPE_MAP = buildDataTypeMap();
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "$index",
        "$this",
        "$total",
        "and",
        "as",
        "contains",
        "day",
        "days",
        "div",
        "false",
        "hour",
        "hours",
        "implies",
        "in",
        "is",
        "millisecond",
        "milliseconds",
        "minute",
        "minutes",
        "mod",
        "month",
        "months",
        "or",
        "seconds",
        "true",
        "week",
        "weeks",
        "xor",
        "year",
        "years",
        "second"
    ));
    private static final Map<String, Class<?>> CODE_SUBTYPE_MAP = buildCodeSubtypeMap();

    private ModelSupport() { }

    /**
     * Calling this method allows us to load/initialize this class during startup.
     */
    public static void init() { }

    public static final class ElementInfo {
        private final String name;
        private final Class<?> type;
        private final Class<?> declaringType;
        private final boolean required;
        private final boolean repeating;
        private final boolean choice;
        private final Set<Class<?>> choiceTypes;
        private final boolean reference;
        private final Set<String> referenceTypes;
        private final Binding binding;
        private final boolean summary;

        private final Set<String> choiceElementNames;

        ElementInfo(String name,
                Class<?> type,
                Class<?> declaringType,
                boolean required,
                boolean repeating,
                boolean choice,
                Set<Class<?>> choiceTypes,
                boolean reference,
                Set<String> referenceTypes,
                Binding binding,
                boolean isSummary) {
            this.name = name;
            this.declaringType = declaringType;
            this.type = type;
            this.required = required;
            this.repeating = repeating;
            this.choice = choice;
            this.choiceTypes = choiceTypes;
            this.reference = reference;
            this.referenceTypes = referenceTypes;
            this.binding = binding;
            this.summary = isSummary;
            Set<String> choiceElementNames = new LinkedHashSet<>();
            if (this.choice) {
                for (Class<?> choiceType : this.choiceTypes) {
                    choiceElementNames.add(getChoiceElementName(this.name, choiceType));
                }
            }
            this.choiceElementNames = Collections.unmodifiableSet(choiceElementNames);
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        public Class<?> getDeclaringType() {
            return declaringType;
        }

        public boolean isDeclaredBy(Class<?> type) {
            return declaringType.equals(type);
        }

        public boolean isRequired() {
            return required;
        }

        public boolean isSummary() {
            return summary;
        }

        public boolean isRepeating() {
            return repeating;
        }

        public boolean isChoice() {
            return choice;
        }

        public Set<Class<?>> getChoiceTypes() {
            return choiceTypes;
        }

        public boolean isReference() {
            return reference;
        }

        public Set<String> getReferenceTypes() {
            return referenceTypes;
        }

        public Binding getBinding() {
            return binding;
        }

        public boolean hasBinding() {
            return (binding != null);
        }

        public Set<String> getChoiceElementNames() {
            return choiceElementNames;
        }
    }

    private static Map<String, Class<?>> buildCodeSubtypeMap() {
        Map<String, Class<?>> codeSubtypeMap = new LinkedHashMap<>();
        codeSubtypeMap.put("AccountStatus", com.ibm.fhir.model.type.code.AccountStatus.class);
        codeSubtypeMap.put("ActionCardinalityBehavior", com.ibm.fhir.model.type.code.ActionCardinalityBehavior.class);
        codeSubtypeMap.put("ActionConditionKind", com.ibm.fhir.model.type.code.ActionConditionKind.class);
        codeSubtypeMap.put("ActionGroupingBehavior", com.ibm.fhir.model.type.code.ActionGroupingBehavior.class);
        codeSubtypeMap.put("ActionParticipantType", com.ibm.fhir.model.type.code.ActionParticipantType.class);
        codeSubtypeMap.put("ActionPrecheckBehavior", com.ibm.fhir.model.type.code.ActionPrecheckBehavior.class);
        codeSubtypeMap.put("ActionRelationshipType", com.ibm.fhir.model.type.code.ActionRelationshipType.class);
        codeSubtypeMap.put("ActionRequiredBehavior", com.ibm.fhir.model.type.code.ActionRequiredBehavior.class);
        codeSubtypeMap.put("ActionSelectionBehavior", com.ibm.fhir.model.type.code.ActionSelectionBehavior.class);
        codeSubtypeMap.put("ActivityDefinitionKind", com.ibm.fhir.model.type.code.ActivityDefinitionKind.class);
        codeSubtypeMap.put("ActivityParticipantType", com.ibm.fhir.model.type.code.ActivityParticipantType.class);
        codeSubtypeMap.put("AddressType", com.ibm.fhir.model.type.code.AddressType.class);
        codeSubtypeMap.put("AddressUse", com.ibm.fhir.model.type.code.AddressUse.class);
        codeSubtypeMap.put("AdministrativeGender", com.ibm.fhir.model.type.code.AdministrativeGender.class);
        codeSubtypeMap.put("AdverseEventActuality", com.ibm.fhir.model.type.code.AdverseEventActuality.class);
        codeSubtypeMap.put("AggregationMode", com.ibm.fhir.model.type.code.AggregationMode.class);
        codeSubtypeMap.put("AllergyIntoleranceCategory", com.ibm.fhir.model.type.code.AllergyIntoleranceCategory.class);
        codeSubtypeMap.put("AllergyIntoleranceCriticality", com.ibm.fhir.model.type.code.AllergyIntoleranceCriticality.class);
        codeSubtypeMap.put("AllergyIntoleranceSeverity", com.ibm.fhir.model.type.code.AllergyIntoleranceSeverity.class);
        codeSubtypeMap.put("AllergyIntoleranceType", com.ibm.fhir.model.type.code.AllergyIntoleranceType.class);
        codeSubtypeMap.put("AppointmentStatus", com.ibm.fhir.model.type.code.AppointmentStatus.class);
        codeSubtypeMap.put("AssertionDirectionType", com.ibm.fhir.model.type.code.AssertionDirectionType.class);
        codeSubtypeMap.put("AssertionOperatorType", com.ibm.fhir.model.type.code.AssertionOperatorType.class);
        codeSubtypeMap.put("AssertionResponseTypes", com.ibm.fhir.model.type.code.AssertionResponseTypes.class);
        codeSubtypeMap.put("AuditEventAction", com.ibm.fhir.model.type.code.AuditEventAction.class);
        codeSubtypeMap.put("AuditEventAgentNetworkType", com.ibm.fhir.model.type.code.AuditEventAgentNetworkType.class);
        codeSubtypeMap.put("AuditEventOutcome", com.ibm.fhir.model.type.code.AuditEventOutcome.class);
        codeSubtypeMap.put("BindingStrength", com.ibm.fhir.model.type.code.BindingStrength.class);
        codeSubtypeMap.put("BiologicallyDerivedProductCategory", com.ibm.fhir.model.type.code.BiologicallyDerivedProductCategory.class);
        codeSubtypeMap.put("BiologicallyDerivedProductStatus", com.ibm.fhir.model.type.code.BiologicallyDerivedProductStatus.class);
        codeSubtypeMap.put("BiologicallyDerivedProductStorageScale", com.ibm.fhir.model.type.code.BiologicallyDerivedProductStorageScale.class);
        codeSubtypeMap.put("BundleType", com.ibm.fhir.model.type.code.BundleType.class);
        codeSubtypeMap.put("CapabilityStatementKind", com.ibm.fhir.model.type.code.CapabilityStatementKind.class);
        codeSubtypeMap.put("CarePlanActivityKind", com.ibm.fhir.model.type.code.CarePlanActivityKind.class);
        codeSubtypeMap.put("CarePlanActivityStatus", com.ibm.fhir.model.type.code.CarePlanActivityStatus.class);
        codeSubtypeMap.put("CarePlanIntent", com.ibm.fhir.model.type.code.CarePlanIntent.class);
        codeSubtypeMap.put("CarePlanStatus", com.ibm.fhir.model.type.code.CarePlanStatus.class);
        codeSubtypeMap.put("CareTeamStatus", com.ibm.fhir.model.type.code.CareTeamStatus.class);
        codeSubtypeMap.put("CatalogEntryRelationType", com.ibm.fhir.model.type.code.CatalogEntryRelationType.class);
        codeSubtypeMap.put("CharacteristicCombination", com.ibm.fhir.model.type.code.CharacteristicCombination.class);
        codeSubtypeMap.put("ChargeItemDefinitionPriceComponentType", com.ibm.fhir.model.type.code.ChargeItemDefinitionPriceComponentType.class);
        codeSubtypeMap.put("ChargeItemStatus", com.ibm.fhir.model.type.code.ChargeItemStatus.class);
        codeSubtypeMap.put("ClaimResponseStatus", com.ibm.fhir.model.type.code.ClaimResponseStatus.class);
        codeSubtypeMap.put("ClaimStatus", com.ibm.fhir.model.type.code.ClaimStatus.class);
        codeSubtypeMap.put("ClinicalImpressionStatus", com.ibm.fhir.model.type.code.ClinicalImpressionStatus.class);
        codeSubtypeMap.put("ClinicalUseDefinitionType", com.ibm.fhir.model.type.code.ClinicalUseDefinitionType.class);
        codeSubtypeMap.put("CodeSearchSupport", com.ibm.fhir.model.type.code.CodeSearchSupport.class);
        codeSubtypeMap.put("CodeSystemContentMode", com.ibm.fhir.model.type.code.CodeSystemContentMode.class);
        codeSubtypeMap.put("CodeSystemHierarchyMeaning", com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning.class);
        codeSubtypeMap.put("CommunicationPriority", com.ibm.fhir.model.type.code.CommunicationPriority.class);
        codeSubtypeMap.put("CommunicationRequestStatus", com.ibm.fhir.model.type.code.CommunicationRequestStatus.class);
        codeSubtypeMap.put("CommunicationStatus", com.ibm.fhir.model.type.code.CommunicationStatus.class);
        codeSubtypeMap.put("CompartmentCode", com.ibm.fhir.model.type.code.CompartmentCode.class);
        codeSubtypeMap.put("CompartmentType", com.ibm.fhir.model.type.code.CompartmentType.class);
        codeSubtypeMap.put("CompositionAttestationMode", com.ibm.fhir.model.type.code.CompositionAttestationMode.class);
        codeSubtypeMap.put("CompositionStatus", com.ibm.fhir.model.type.code.CompositionStatus.class);
        codeSubtypeMap.put("ConceptMapEquivalence", com.ibm.fhir.model.type.code.ConceptMapEquivalence.class);
        codeSubtypeMap.put("ConceptMapGroupUnmappedMode", com.ibm.fhir.model.type.code.ConceptMapGroupUnmappedMode.class);
        codeSubtypeMap.put("ConceptSubsumptionOutcome", com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome.class);
        codeSubtypeMap.put("ConditionalDeleteStatus", com.ibm.fhir.model.type.code.ConditionalDeleteStatus.class);
        codeSubtypeMap.put("ConditionalReadStatus", com.ibm.fhir.model.type.code.ConditionalReadStatus.class);
        codeSubtypeMap.put("ConsentDataMeaning", com.ibm.fhir.model.type.code.ConsentDataMeaning.class);
        codeSubtypeMap.put("ConsentProvisionType", com.ibm.fhir.model.type.code.ConsentProvisionType.class);
        codeSubtypeMap.put("ConsentState", com.ibm.fhir.model.type.code.ConsentState.class);
        codeSubtypeMap.put("ConstraintSeverity", com.ibm.fhir.model.type.code.ConstraintSeverity.class);
        codeSubtypeMap.put("ContactPointSystem", com.ibm.fhir.model.type.code.ContactPointSystem.class);
        codeSubtypeMap.put("ContactPointUse", com.ibm.fhir.model.type.code.ContactPointUse.class);
        codeSubtypeMap.put("ContractPublicationStatus", com.ibm.fhir.model.type.code.ContractPublicationStatus.class);
        codeSubtypeMap.put("ContractStatus", com.ibm.fhir.model.type.code.ContractStatus.class);
        codeSubtypeMap.put("ContributorType", com.ibm.fhir.model.type.code.ContributorType.class);
        codeSubtypeMap.put("CoverageStatus", com.ibm.fhir.model.type.code.CoverageStatus.class);
        codeSubtypeMap.put("CriteriaNotExistsBehavior", com.ibm.fhir.model.type.code.CriteriaNotExistsBehavior.class);
        codeSubtypeMap.put("DataAbsentReason", com.ibm.fhir.model.type.code.DataAbsentReason.class);
        codeSubtypeMap.put("DayOfWeek", com.ibm.fhir.model.type.code.DayOfWeek.class);
        codeSubtypeMap.put("DaysOfWeek", com.ibm.fhir.model.type.code.DaysOfWeek.class);
        codeSubtypeMap.put("DetectedIssueSeverity", com.ibm.fhir.model.type.code.DetectedIssueSeverity.class);
        codeSubtypeMap.put("DetectedIssueStatus", com.ibm.fhir.model.type.code.DetectedIssueStatus.class);
        codeSubtypeMap.put("DeviceMetricCalibrationState", com.ibm.fhir.model.type.code.DeviceMetricCalibrationState.class);
        codeSubtypeMap.put("DeviceMetricCalibrationType", com.ibm.fhir.model.type.code.DeviceMetricCalibrationType.class);
        codeSubtypeMap.put("DeviceMetricCategory", com.ibm.fhir.model.type.code.DeviceMetricCategory.class);
        codeSubtypeMap.put("DeviceMetricColor", com.ibm.fhir.model.type.code.DeviceMetricColor.class);
        codeSubtypeMap.put("DeviceMetricOperationalStatus", com.ibm.fhir.model.type.code.DeviceMetricOperationalStatus.class);
        codeSubtypeMap.put("DeviceNameType", com.ibm.fhir.model.type.code.DeviceNameType.class);
        codeSubtypeMap.put("DeviceRequestStatus", com.ibm.fhir.model.type.code.DeviceRequestStatus.class);
        codeSubtypeMap.put("DeviceUseStatementStatus", com.ibm.fhir.model.type.code.DeviceUseStatementStatus.class);
        codeSubtypeMap.put("DiagnosticReportStatus", com.ibm.fhir.model.type.code.DiagnosticReportStatus.class);
        codeSubtypeMap.put("DiscriminatorType", com.ibm.fhir.model.type.code.DiscriminatorType.class);
        codeSubtypeMap.put("DocumentConfidentiality", com.ibm.fhir.model.type.code.DocumentConfidentiality.class);
        codeSubtypeMap.put("DocumentMode", com.ibm.fhir.model.type.code.DocumentMode.class);
        codeSubtypeMap.put("DocumentReferenceStatus", com.ibm.fhir.model.type.code.DocumentReferenceStatus.class);
        codeSubtypeMap.put("DocumentRelationshipType", com.ibm.fhir.model.type.code.DocumentRelationshipType.class);
        codeSubtypeMap.put("EligibilityRequestPurpose", com.ibm.fhir.model.type.code.EligibilityRequestPurpose.class);
        codeSubtypeMap.put("EligibilityRequestStatus", com.ibm.fhir.model.type.code.EligibilityRequestStatus.class);
        codeSubtypeMap.put("EligibilityResponsePurpose", com.ibm.fhir.model.type.code.EligibilityResponsePurpose.class);
        codeSubtypeMap.put("EligibilityResponseStatus", com.ibm.fhir.model.type.code.EligibilityResponseStatus.class);
        codeSubtypeMap.put("EnableWhenBehavior", com.ibm.fhir.model.type.code.EnableWhenBehavior.class);
        codeSubtypeMap.put("EncounterLocationStatus", com.ibm.fhir.model.type.code.EncounterLocationStatus.class);
        codeSubtypeMap.put("EncounterStatus", com.ibm.fhir.model.type.code.EncounterStatus.class);
        codeSubtypeMap.put("EndpointStatus", com.ibm.fhir.model.type.code.EndpointStatus.class);
        codeSubtypeMap.put("EnrollmentRequestStatus", com.ibm.fhir.model.type.code.EnrollmentRequestStatus.class);
        codeSubtypeMap.put("EnrollmentResponseStatus", com.ibm.fhir.model.type.code.EnrollmentResponseStatus.class);
        codeSubtypeMap.put("EpisodeOfCareStatus", com.ibm.fhir.model.type.code.EpisodeOfCareStatus.class);
        codeSubtypeMap.put("EventCapabilityMode", com.ibm.fhir.model.type.code.EventCapabilityMode.class);
        codeSubtypeMap.put("EventTiming", com.ibm.fhir.model.type.code.EventTiming.class);
        codeSubtypeMap.put("EvidenceVariableHandling", com.ibm.fhir.model.type.code.EvidenceVariableHandling.class);
        codeSubtypeMap.put("ExampleScenarioActorType", com.ibm.fhir.model.type.code.ExampleScenarioActorType.class);
        codeSubtypeMap.put("ExplanationOfBenefitStatus", com.ibm.fhir.model.type.code.ExplanationOfBenefitStatus.class);
        codeSubtypeMap.put("ExtensionContextType", com.ibm.fhir.model.type.code.ExtensionContextType.class);
        codeSubtypeMap.put("FamilyHistoryStatus", com.ibm.fhir.model.type.code.FamilyHistoryStatus.class);
        codeSubtypeMap.put("FHIRAllTypes", com.ibm.fhir.model.type.code.FHIRAllTypes.class);
        codeSubtypeMap.put("FHIRDefinedType", com.ibm.fhir.model.type.code.FHIRDefinedType.class);
        codeSubtypeMap.put("FHIRDeviceStatus", com.ibm.fhir.model.type.code.FHIRDeviceStatus.class);
        codeSubtypeMap.put("FHIRSubstanceStatus", com.ibm.fhir.model.type.code.FHIRSubstanceStatus.class);
        codeSubtypeMap.put("FHIRVersion", com.ibm.fhir.model.type.code.FHIRVersion.class);
        codeSubtypeMap.put("FilterOperator", com.ibm.fhir.model.type.code.FilterOperator.class);
        codeSubtypeMap.put("FlagStatus", com.ibm.fhir.model.type.code.FlagStatus.class);
        codeSubtypeMap.put("GoalLifecycleStatus", com.ibm.fhir.model.type.code.GoalLifecycleStatus.class);
        codeSubtypeMap.put("GraphCompartmentRule", com.ibm.fhir.model.type.code.GraphCompartmentRule.class);
        codeSubtypeMap.put("GraphCompartmentUse", com.ibm.fhir.model.type.code.GraphCompartmentUse.class);
        codeSubtypeMap.put("GroupMeasure", com.ibm.fhir.model.type.code.GroupMeasure.class);
        codeSubtypeMap.put("GroupType", com.ibm.fhir.model.type.code.GroupType.class);
        codeSubtypeMap.put("GuidanceResponseStatus", com.ibm.fhir.model.type.code.GuidanceResponseStatus.class);
        codeSubtypeMap.put("GuidePageGeneration", com.ibm.fhir.model.type.code.GuidePageGeneration.class);
        codeSubtypeMap.put("GuideParameterCode", com.ibm.fhir.model.type.code.GuideParameterCode.class);
        codeSubtypeMap.put("HTTPVerb", com.ibm.fhir.model.type.code.HTTPVerb.class);
        codeSubtypeMap.put("IdentifierUse", com.ibm.fhir.model.type.code.IdentifierUse.class);
        codeSubtypeMap.put("IdentityAssuranceLevel", com.ibm.fhir.model.type.code.IdentityAssuranceLevel.class);
        codeSubtypeMap.put("ImagingStudyStatus", com.ibm.fhir.model.type.code.ImagingStudyStatus.class);
        codeSubtypeMap.put("ImmunizationEvaluationStatus", com.ibm.fhir.model.type.code.ImmunizationEvaluationStatus.class);
        codeSubtypeMap.put("ImmunizationStatus", com.ibm.fhir.model.type.code.ImmunizationStatus.class);
        codeSubtypeMap.put("InvoicePriceComponentType", com.ibm.fhir.model.type.code.InvoicePriceComponentType.class);
        codeSubtypeMap.put("InvoiceStatus", com.ibm.fhir.model.type.code.InvoiceStatus.class);
        codeSubtypeMap.put("IssueSeverity", com.ibm.fhir.model.type.code.IssueSeverity.class);
        codeSubtypeMap.put("IssueType", com.ibm.fhir.model.type.code.IssueType.class);
        codeSubtypeMap.put("LinkageType", com.ibm.fhir.model.type.code.LinkageType.class);
        codeSubtypeMap.put("LinkType", com.ibm.fhir.model.type.code.LinkType.class);
        codeSubtypeMap.put("ListMode", com.ibm.fhir.model.type.code.ListMode.class);
        codeSubtypeMap.put("ListStatus", com.ibm.fhir.model.type.code.ListStatus.class);
        codeSubtypeMap.put("LocationMode", com.ibm.fhir.model.type.code.LocationMode.class);
        codeSubtypeMap.put("LocationStatus", com.ibm.fhir.model.type.code.LocationStatus.class);
        codeSubtypeMap.put("MeasureReportStatus", com.ibm.fhir.model.type.code.MeasureReportStatus.class);
        codeSubtypeMap.put("MeasureReportType", com.ibm.fhir.model.type.code.MeasureReportType.class);
        codeSubtypeMap.put("MediaStatus", com.ibm.fhir.model.type.code.MediaStatus.class);
        codeSubtypeMap.put("MedicationAdministrationStatus", com.ibm.fhir.model.type.code.MedicationAdministrationStatus.class);
        codeSubtypeMap.put("MedicationDispenseStatus", com.ibm.fhir.model.type.code.MedicationDispenseStatus.class);
        codeSubtypeMap.put("MedicationKnowledgeStatus", com.ibm.fhir.model.type.code.MedicationKnowledgeStatus.class);
        codeSubtypeMap.put("MedicationRequestIntent", com.ibm.fhir.model.type.code.MedicationRequestIntent.class);
        codeSubtypeMap.put("MedicationRequestPriority", com.ibm.fhir.model.type.code.MedicationRequestPriority.class);
        codeSubtypeMap.put("MedicationRequestStatus", com.ibm.fhir.model.type.code.MedicationRequestStatus.class);
        codeSubtypeMap.put("MedicationStatementStatus", com.ibm.fhir.model.type.code.MedicationStatementStatus.class);
        codeSubtypeMap.put("MedicationStatus", com.ibm.fhir.model.type.code.MedicationStatus.class);
        codeSubtypeMap.put("MessageHeaderResponseRequest", com.ibm.fhir.model.type.code.MessageHeaderResponseRequest.class);
        codeSubtypeMap.put("MessageSignificanceCategory", com.ibm.fhir.model.type.code.MessageSignificanceCategory.class);
        codeSubtypeMap.put("MethodCode", com.ibm.fhir.model.type.code.MethodCode.class);
        codeSubtypeMap.put("NameUse", com.ibm.fhir.model.type.code.NameUse.class);
        codeSubtypeMap.put("NamingSystemIdentifierType", com.ibm.fhir.model.type.code.NamingSystemIdentifierType.class);
        codeSubtypeMap.put("NamingSystemType", com.ibm.fhir.model.type.code.NamingSystemType.class);
        codeSubtypeMap.put("NarrativeStatus", com.ibm.fhir.model.type.code.NarrativeStatus.class);
        codeSubtypeMap.put("NoteType", com.ibm.fhir.model.type.code.NoteType.class);
        codeSubtypeMap.put("NutritionOrderIntent", com.ibm.fhir.model.type.code.NutritionOrderIntent.class);
        codeSubtypeMap.put("NutritionOrderStatus", com.ibm.fhir.model.type.code.NutritionOrderStatus.class);
        codeSubtypeMap.put("NutritionProductStatus", com.ibm.fhir.model.type.code.NutritionProductStatus.class);
        codeSubtypeMap.put("ObservationDataType", com.ibm.fhir.model.type.code.ObservationDataType.class);
        codeSubtypeMap.put("ObservationRangeCategory", com.ibm.fhir.model.type.code.ObservationRangeCategory.class);
        codeSubtypeMap.put("ObservationStatus", com.ibm.fhir.model.type.code.ObservationStatus.class);
        codeSubtypeMap.put("OperationKind", com.ibm.fhir.model.type.code.OperationKind.class);
        codeSubtypeMap.put("OperationParameterUse", com.ibm.fhir.model.type.code.OperationParameterUse.class);
        codeSubtypeMap.put("OrientationType", com.ibm.fhir.model.type.code.OrientationType.class);
        codeSubtypeMap.put("ParameterUse", com.ibm.fhir.model.type.code.ParameterUse.class);
        codeSubtypeMap.put("ParticipantRequired", com.ibm.fhir.model.type.code.ParticipantRequired.class);
        codeSubtypeMap.put("ParticipantStatus", com.ibm.fhir.model.type.code.ParticipantStatus.class);
        codeSubtypeMap.put("ParticipationStatus", com.ibm.fhir.model.type.code.ParticipationStatus.class);
        codeSubtypeMap.put("PaymentNoticeStatus", com.ibm.fhir.model.type.code.PaymentNoticeStatus.class);
        codeSubtypeMap.put("PaymentReconciliationStatus", com.ibm.fhir.model.type.code.PaymentReconciliationStatus.class);
        codeSubtypeMap.put("ProcedureStatus", com.ibm.fhir.model.type.code.ProcedureStatus.class);
        codeSubtypeMap.put("PropertyRepresentation", com.ibm.fhir.model.type.code.PropertyRepresentation.class);
        codeSubtypeMap.put("PropertyType", com.ibm.fhir.model.type.code.PropertyType.class);
        codeSubtypeMap.put("ProvenanceEntityRole", com.ibm.fhir.model.type.code.ProvenanceEntityRole.class);
        codeSubtypeMap.put("PublicationStatus", com.ibm.fhir.model.type.code.PublicationStatus.class);
        codeSubtypeMap.put("QualityType", com.ibm.fhir.model.type.code.QualityType.class);
        codeSubtypeMap.put("QuantityComparator", com.ibm.fhir.model.type.code.QuantityComparator.class);
        codeSubtypeMap.put("QuestionnaireItemOperator", com.ibm.fhir.model.type.code.QuestionnaireItemOperator.class);
        codeSubtypeMap.put("QuestionnaireItemType", com.ibm.fhir.model.type.code.QuestionnaireItemType.class);
        codeSubtypeMap.put("QuestionnaireResponseStatus", com.ibm.fhir.model.type.code.QuestionnaireResponseStatus.class);
        codeSubtypeMap.put("ReferenceHandlingPolicy", com.ibm.fhir.model.type.code.ReferenceHandlingPolicy.class);
        codeSubtypeMap.put("ReferenceVersionRules", com.ibm.fhir.model.type.code.ReferenceVersionRules.class);
        codeSubtypeMap.put("ReferredDocumentStatus", com.ibm.fhir.model.type.code.ReferredDocumentStatus.class);
        codeSubtypeMap.put("RelatedArtifactType", com.ibm.fhir.model.type.code.RelatedArtifactType.class);
        codeSubtypeMap.put("RemittanceOutcome", com.ibm.fhir.model.type.code.RemittanceOutcome.class);
        codeSubtypeMap.put("ReportRelationshipType", com.ibm.fhir.model.type.code.ReportRelationshipType.class);
        codeSubtypeMap.put("RepositoryType", com.ibm.fhir.model.type.code.RepositoryType.class);
        codeSubtypeMap.put("RequestIntent", com.ibm.fhir.model.type.code.RequestIntent.class);
        codeSubtypeMap.put("RequestPriority", com.ibm.fhir.model.type.code.RequestPriority.class);
        codeSubtypeMap.put("RequestStatus", com.ibm.fhir.model.type.code.RequestStatus.class);
        codeSubtypeMap.put("ResearchElementType", com.ibm.fhir.model.type.code.ResearchElementType.class);
        codeSubtypeMap.put("ResearchStudyStatus", com.ibm.fhir.model.type.code.ResearchStudyStatus.class);
        codeSubtypeMap.put("ResearchSubjectStatus", com.ibm.fhir.model.type.code.ResearchSubjectStatus.class);
        codeSubtypeMap.put("ResourceTypeCode", com.ibm.fhir.model.type.code.ResourceTypeCode.class);
        codeSubtypeMap.put("ResourceVersionPolicy", com.ibm.fhir.model.type.code.ResourceVersionPolicy.class);
        codeSubtypeMap.put("ResponseType", com.ibm.fhir.model.type.code.ResponseType.class);
        codeSubtypeMap.put("RestfulCapabilityMode", com.ibm.fhir.model.type.code.RestfulCapabilityMode.class);
        codeSubtypeMap.put("RiskAssessmentStatus", com.ibm.fhir.model.type.code.RiskAssessmentStatus.class);
        codeSubtypeMap.put("SearchComparator", com.ibm.fhir.model.type.code.SearchComparator.class);
        codeSubtypeMap.put("SearchEntryMode", com.ibm.fhir.model.type.code.SearchEntryMode.class);
        codeSubtypeMap.put("SearchModifierCode", com.ibm.fhir.model.type.code.SearchModifierCode.class);
        codeSubtypeMap.put("SearchParamType", com.ibm.fhir.model.type.code.SearchParamType.class);
        codeSubtypeMap.put("SectionMode", com.ibm.fhir.model.type.code.SectionMode.class);
        codeSubtypeMap.put("SequenceType", com.ibm.fhir.model.type.code.SequenceType.class);
        codeSubtypeMap.put("ServiceRequestIntent", com.ibm.fhir.model.type.code.ServiceRequestIntent.class);
        codeSubtypeMap.put("ServiceRequestPriority", com.ibm.fhir.model.type.code.ServiceRequestPriority.class);
        codeSubtypeMap.put("ServiceRequestStatus", com.ibm.fhir.model.type.code.ServiceRequestStatus.class);
        codeSubtypeMap.put("SlicingRules", com.ibm.fhir.model.type.code.SlicingRules.class);
        codeSubtypeMap.put("SlotStatus", com.ibm.fhir.model.type.code.SlotStatus.class);
        codeSubtypeMap.put("SortDirection", com.ibm.fhir.model.type.code.SortDirection.class);
        codeSubtypeMap.put("SPDXLicense", com.ibm.fhir.model.type.code.SPDXLicense.class);
        codeSubtypeMap.put("SpecimenContainedPreference", com.ibm.fhir.model.type.code.SpecimenContainedPreference.class);
        codeSubtypeMap.put("SpecimenStatus", com.ibm.fhir.model.type.code.SpecimenStatus.class);
        codeSubtypeMap.put("StandardsStatus", com.ibm.fhir.model.type.code.StandardsStatus.class);
        codeSubtypeMap.put("Status", com.ibm.fhir.model.type.code.Status.class);
        codeSubtypeMap.put("StrandType", com.ibm.fhir.model.type.code.StrandType.class);
        codeSubtypeMap.put("StructureDefinitionKind", com.ibm.fhir.model.type.code.StructureDefinitionKind.class);
        codeSubtypeMap.put("StructureMapContextType", com.ibm.fhir.model.type.code.StructureMapContextType.class);
        codeSubtypeMap.put("StructureMapGroupTypeMode", com.ibm.fhir.model.type.code.StructureMapGroupTypeMode.class);
        codeSubtypeMap.put("StructureMapInputMode", com.ibm.fhir.model.type.code.StructureMapInputMode.class);
        codeSubtypeMap.put("StructureMapModelMode", com.ibm.fhir.model.type.code.StructureMapModelMode.class);
        codeSubtypeMap.put("StructureMapSourceListMode", com.ibm.fhir.model.type.code.StructureMapSourceListMode.class);
        codeSubtypeMap.put("StructureMapTargetListMode", com.ibm.fhir.model.type.code.StructureMapTargetListMode.class);
        codeSubtypeMap.put("StructureMapTransform", com.ibm.fhir.model.type.code.StructureMapTransform.class);
        codeSubtypeMap.put("SubscriptionChannelType", com.ibm.fhir.model.type.code.SubscriptionChannelType.class);
        codeSubtypeMap.put("SubscriptionNotificationType", com.ibm.fhir.model.type.code.SubscriptionNotificationType.class);
        codeSubtypeMap.put("SubscriptionStatusCode", com.ibm.fhir.model.type.code.SubscriptionStatusCode.class);
        codeSubtypeMap.put("SubscriptionTopicFilterBySearchModifier", com.ibm.fhir.model.type.code.SubscriptionTopicFilterBySearchModifier.class);
        codeSubtypeMap.put("SupplyDeliveryStatus", com.ibm.fhir.model.type.code.SupplyDeliveryStatus.class);
        codeSubtypeMap.put("SupplyRequestStatus", com.ibm.fhir.model.type.code.SupplyRequestStatus.class);
        codeSubtypeMap.put("SystemRestfulInteraction", com.ibm.fhir.model.type.code.SystemRestfulInteraction.class);
        codeSubtypeMap.put("TaskIntent", com.ibm.fhir.model.type.code.TaskIntent.class);
        codeSubtypeMap.put("TaskPriority", com.ibm.fhir.model.type.code.TaskPriority.class);
        codeSubtypeMap.put("TaskStatus", com.ibm.fhir.model.type.code.TaskStatus.class);
        codeSubtypeMap.put("TestReportActionResult", com.ibm.fhir.model.type.code.TestReportActionResult.class);
        codeSubtypeMap.put("TestReportParticipantType", com.ibm.fhir.model.type.code.TestReportParticipantType.class);
        codeSubtypeMap.put("TestReportResult", com.ibm.fhir.model.type.code.TestReportResult.class);
        codeSubtypeMap.put("TestReportStatus", com.ibm.fhir.model.type.code.TestReportStatus.class);
        codeSubtypeMap.put("TestScriptRequestMethodCode", com.ibm.fhir.model.type.code.TestScriptRequestMethodCode.class);
        codeSubtypeMap.put("TriggerType", com.ibm.fhir.model.type.code.TriggerType.class);
        codeSubtypeMap.put("TypeDerivationRule", com.ibm.fhir.model.type.code.TypeDerivationRule.class);
        codeSubtypeMap.put("TypeRestfulInteraction", com.ibm.fhir.model.type.code.TypeRestfulInteraction.class);
        codeSubtypeMap.put("UDIEntryType", com.ibm.fhir.model.type.code.UDIEntryType.class);
        codeSubtypeMap.put("UnitsOfTime", com.ibm.fhir.model.type.code.UnitsOfTime.class);
        codeSubtypeMap.put("Use", com.ibm.fhir.model.type.code.Use.class);
        codeSubtypeMap.put("VariableType", com.ibm.fhir.model.type.code.VariableType.class);
        codeSubtypeMap.put("VisionBase", com.ibm.fhir.model.type.code.VisionBase.class);
        codeSubtypeMap.put("VisionEyes", com.ibm.fhir.model.type.code.VisionEyes.class);
        codeSubtypeMap.put("VisionStatus", com.ibm.fhir.model.type.code.VisionStatus.class);
        codeSubtypeMap.put("XPathUsageType", com.ibm.fhir.model.type.code.XPathUsageType.class);
        return Collections.unmodifiableMap(codeSubtypeMap);
    }

    private static Map<String, Class<?>> buildDataTypeMap() {
        Map<String, Class<?>> dataTypeMap = new LinkedHashMap<>();
        for (Class<?> dataType : DATA_TYPES) {
            dataTypeMap.put(getTypeName(dataType), dataType);
        }
        return Collections.unmodifiableMap(dataTypeMap);
    }

    private static Map<Class<?>, Class<?>> buildConcreteTypeMap() {
        Map<Class<?>, Class<?>> concreteTypeMap = new LinkedHashMap<>();
        concreteTypeMap.put(SimpleQuantity.class, Quantity.class);
        concreteTypeMap.put(MoneyQuantity.class, Quantity.class);
        return Collections.unmodifiableMap(concreteTypeMap);
    }

    private static Map<Class<?>, List<Constraint>> buildModelClassConstraintMap() {
        Map<Class<?>, List<Constraint>> modelClassConstraintMap = new LinkedHashMap<>(1024);
        List<ModelConstraintProvider> providers = ConstraintProvider.providers(ModelConstraintProvider.class);
        for (Class<?> modelClass : getModelClasses()) {
            List<Constraint> constraints = new ArrayList<>();
            for (Class<?> clazz : getClosure(modelClass)) {
                for (Constraint constraint : clazz.getDeclaredAnnotationsByType(Constraint.class)) {
                    constraints.add(Constraint.Factory.createConstraint(
                        constraint.id(),
                        constraint.level(),
                        constraint.location(),
                        constraint.description(),
                        constraint.expression(),
                        constraint.source(),
                        constraint.modelChecked(),
                        constraint.generated()));
                }
            }
            for (ModelConstraintProvider provider : providers) {
                if (provider.appliesTo(modelClass)) {
                    for (Predicate<Constraint> removalPredicate : provider.getRemovalPredicates()) {
                        constraints.removeIf(removalPredicate);
                    }
                    constraints.addAll(provider.getConstraints());
                }
            }
            modelClassConstraintMap.put(modelClass, Collections.unmodifiableList(constraints));
        }
        return Collections.unmodifiableMap(modelClassConstraintMap);
    }

    private static Map<Class<?>, Map<String, ElementInfo>> buildModelClassElementInfoMap() {
        Map<Class<?>, Map<String, ElementInfo>> modelClassElementInfoMap = new LinkedHashMap<>(1024);
        for(Class<?> modelClass : MODEL_CLASSES) {
            Map<String, ElementInfo> elementInfoMap = getElementInfoMap(modelClass, modelClassElementInfoMap);
            modelClassElementInfoMap.put(modelClass, Collections.unmodifiableMap(elementInfoMap));
        }
        return Collections.unmodifiableMap(modelClassElementInfoMap);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Class<? extends Resource>> buildResourceTypeMap() {
        Map<String, Class<? extends Resource>> resourceTypeMap = new LinkedHashMap<>(256);
        for (Class<?> modelClass : getModelClasses()) {
            if (isResourceType(modelClass)) {
                resourceTypeMap.put(modelClass.getSimpleName(), (Class<? extends Resource>) modelClass);
            }
        }
        return Collections.unmodifiableMap(resourceTypeMap);
    }

    private static Map<String, ElementInfo> getElementInfoMap(Class<?> modelClass,
            Map<Class<?>, Map<String,ElementInfo>> elementInfoMapCache) {
        Map<String, ElementInfo> elementInfoMap = new LinkedHashMap<>();

        // Loop through this class and its supertypes to collect ElementInfo for all the fields
        for (Class<?> clazz : getClosure(modelClass)) {
            // If we've already created ElementInfo for this class, then use that
            if (elementInfoMapCache.containsKey(clazz)) {
                elementInfoMap.putAll(elementInfoMapCache.get(clazz));
                continue;
            }

            // Else use reflection and model annotations to construct ElementInfo for all fields in this class
            for (Field field : clazz.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isVolatile(modifiers)) {
                    continue;
                }

                String elementName = getElementName(field);
                Class<?> type = getFieldType(field);
                Class<?> declaringType = field.getDeclaringClass();
                boolean required = isRequired(field);
                boolean summary = isSummary(field);
                boolean repeating = isRepeating(field);
                boolean choice = isChoice(field);
                boolean reference = isReference(field);
                Binding binding = field.getAnnotation(Binding.class);
                Set<Class<?>> choiceTypes = choice ? Collections.unmodifiableSet(getChoiceTypes(field)) : Collections.emptySet();
                Set<String> referenceTypes = reference ? Collections.unmodifiableSet(getReferenceTypes(field)) : Collections.emptySet();
                elementInfoMap.put(elementName, new ElementInfo(
                        elementName,
                        type,
                        declaringType,
                        required,
                        repeating,
                        choice,
                        choiceTypes,
                        reference,
                        referenceTypes,
                        binding,
                        summary
                    )
                );
            }
        }
        return elementInfoMap;
    }

    /**
     * @param name
     *            the name of the choice element without any type suffix
     * @param type
     *            the model class which represents the choice value for the choice element
     * @return the serialized name of the choice element {@code name} with choice type {@code type}
     */
    public static String getChoiceElementName(String name, Class<?> type) {
        return name + getConcreteType(type).getSimpleName();
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @param elementName
     *            the name of the choice element without any type suffix
     * @return the set of model classes for the allowed types of the specified choice element
     */
    public static Set<Class<?>> getChoiceElementTypes(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.getChoiceTypes();
        }
        return Collections.emptySet();
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @param elementName
     *            the name of the reference element
     * @return a set of Strings which represent the the allowed target types for the reference
     */
    public static Set<String> getReferenceTargetTypes(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.getReferenceTypes();
        }
        return Collections.emptySet();
    }

    private static Set<Class<?>> getChoiceTypes(Field field) {
        return new LinkedHashSet<>(Arrays.asList(field.getAnnotation(Choice.class).value()));
    }

    private static Set<String> getReferenceTypes(Field field) {
        return new LinkedHashSet<>(Arrays.asList(field.getAnnotation(ReferenceTarget.class).value()));
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @return A list of superclasses ordered from parent to child, including the modelClass itself
     */
    public static List<Class<?>> getClosure(Class<?> modelClass) {
        List<Class<?>> closure = new ArrayList<>();
        while (!Object.class.equals(modelClass)) {
            closure.add(modelClass);
            modelClass = modelClass.getSuperclass();
        }
        Collections.reverse(closure);
        return closure;
    }

    /**
     * @param type
     * @return the class for the concrete type of the passed type if it is a profiled type; otherwise the passed type
     *         itself
     */
    public static Class<?> getConcreteType(Class<?> type) {
        if (isProfiledType(type)) {
            return CONCRETE_TYPE_MAP.get(type);
        }
        return type;
    }

    /**
     * @return the list of constraints for the modelClass or empty if there are none
     */
    public static List<Constraint> getConstraints(Class<?> modelClass) {
        return MODEL_CLASS_CONSTRAINT_MAP.getOrDefault(modelClass, Collections.emptyList());
    }

    /**
     * @return ElementInfo for the element with the passed name on the passed modelClass or null if the modelClass does
     *         not contain an element with this name
     */
    public static ElementInfo getElementInfo(Class<?> modelClass, String elementName) {
        return MODEL_CLASS_ELEMENT_INFO_MAP.getOrDefault(modelClass, Collections.emptyMap()).get(elementName);
    }

    /**
     * @return a collection of ElementInfo for all elements of the passed modelClass or empty if the class is not a FHIR
     *         model class
     */
    public static Collection<ElementInfo> getElementInfo(Class<?> modelClass) {
        return MODEL_CLASS_ELEMENT_INFO_MAP.getOrDefault(modelClass, Collections.emptyMap()).values();
    }

    /**
     * @return ElementInfo for the choice element with the passed typeSpecificElementName of the passed modelClass or
     *         null if the modelClass does not contain a choice element that can have this typeSpecificElementName
     */
    public static ElementInfo getChoiceElementInfo(Class<?> modelClass, String typeSpecificElementName) {
        for (ElementInfo elementInfo : getElementInfo(modelClass)) {
            if (elementInfo.isChoice() && elementInfo.getChoiceElementNames().contains(typeSpecificElementName)) {
                return elementInfo;
            }
        }
        return null;
    }

    /**
     * Get the actual element name from a Java field.
     */
    public static String getElementName(Field field) {
        return getElementName(field.getName());
    }

    /**
     * Get the actual element name from a Java field name.
     * This method reverses any encoding that was required to represent the FHIR element name in Java,
     * such as converting class to clazz.
     */
    public static String getElementName(String fieldName) {
        if ("clazz".equals(fieldName)) {
            return "class";
        }
        if (fieldName.startsWith("_")) {
            return fieldName.substring(1);
        }
        return fieldName;
    }

    /**
     * @return the set of element names for the passed modelClass or empty if it is not a FHIR model class
     * @implSpec choice type element names are returned without a type suffix; see {@link #getChoiceElementName(String,
     *           Class<?>)} for building the serialized name
     */
    public static Set<String> getElementNames(Class<?> modelClass) {
        return MODEL_CLASS_ELEMENT_INFO_MAP.getOrDefault(modelClass, Collections.emptyMap()).keySet();
    }

    /**
     * @return the model class for the element with name elementName on the passed modelClass or
     *         null if the passed modelClass does not have an element {@code elementName}
     */
    public static Class<?> getElementType(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.getType();
        }
        return null;
    }

    /**
     * Get the model class which declares the elementName found on the passed modelClass.
     *
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @param elementName
     *            the name of the element; choice element names do not include a type suffix
     * @return modelClass or a superclass of modelClass, or null if the element is not found on the passed modelClass
     */
    public static Class<?> getElementDeclaringType(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.getDeclaringType();
        }
        return null;
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @param elementName
     *            the name of the element; choice element names do not include a type suffix
     * @param type
     *            the model class to check
     * @return true if the passed modelClass contains an element with name elementName and the passed type is the one
     *         that declares it; otherwise false
     */
    public static boolean isElementDeclaredBy(Class<?> modelClass, String elementName, Class<?> type) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isDeclaredBy(type);
        }
        return false;
    }

    private static Class<?> getFieldType(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        return field.getType();
    }

    /**
     * @return all model classes, including both resources and elements, concrete and abstract
     */
    public static Set<Class<?>> getModelClasses() {
        return MODEL_CLASS_ELEMENT_INFO_MAP.keySet();
    }

    /**
     * @param name
     *            the resource type name in titlecase to match the corresponding model class name
     * @return the model class that corresponds to the passed resource type name
     */
    public static Class<? extends Resource> getResourceType(String name) {
        return RESOURCE_TYPE_MAP.get(name);
    }

    /**
     * @return a collection of FHIR resource type model classes, including abstract supertypes
     */
    public static Collection<Class<? extends Resource>> getResourceTypes() {
        return RESOURCE_TYPE_MAP.values();
    }

    /**
     * @return a collection of FHIR resource type model classes
     */
    public static Collection<Class<? extends Resource>> getResourceTypes(boolean includeAbstractTypes) {
        if (includeAbstractTypes) {
            return RESOURCE_TYPE_MAP.values();
        } else {
            return CONCRETE_RESOURCE_TYPES;
        }
    }

    /**
     * @return the set of classes for the FHIR elements
     */
    public static Set<Class<? extends Element>> getDataTypes() {
        return DATA_TYPES;
    }

    /**
     * @return the name of the FHIR data type which corresponds to the passed type
     * @implNote primitive types will start with a lowercase letter,
     *           complex types and resources with an uppercaseletter
     */
    public static String getTypeName(Class<?> type) {
        String typeName = type.getSimpleName();
        if (Code.class.isAssignableFrom(type)) {
            typeName = "code";
        } else if (isPrimitiveType(type)) {
            typeName = typeName.substring(0, 1).toLowerCase() + typeName.substring(1);
        }
        return typeName;
    }

    /**
     * @return the set of FHIR data type names for the passed modelClass and its supertypes
     * @implNote primitive types will start with a lowercase letter,
     *           complex types and resources with an uppercaseletter
     */
    public static Set<String> getTypeNames(Class<?> modelClass) {
        Set<String> typeNames = new HashSet<>();
        while (!Object.class.equals(modelClass)) {
            typeNames.add(getTypeName(modelClass));
            modelClass = modelClass.getSuperclass();
        }
        return typeNames;
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @return true if and only if {@code modelClass} is a BackboneElement
     */
    public static boolean isBackboneElementType(Class<?> modelClass) {
        return BackboneElement.class.isAssignableFrom(modelClass);
    }

    private static boolean isChoice(Field field) {
        return field.isAnnotationPresent(Choice.class);
    }

    private static boolean isReference(Field field) {
        return field.isAnnotationPresent(com.ibm.fhir.model.annotation.ReferenceTarget.class);
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @param elementName
     *            the name of the element; choice element names do not include a type suffix
     * @return true if {@code modelClass} contains a choice element with name @{code elementName}; otherwise false
     */
    public static boolean isChoiceElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isChoice();
        }
        return false;
    }

    /**
     * @param type
     *            a model class which represents a FHIR element
     * @return true if {@code type} is an allowed choice element type; otherwise false
     */
    public static boolean isChoiceElementType(Class<?> type) {
        return CHOICE_ELEMENT_TYPES.contains(type);
    }

    /**
     * @param type
     *            a model class which represents a FHIR element
     * @return true if {@code type} is subclass of com.ibm.fhir.model.type.Code; otherwise false
     */
    public static boolean isCodeSubtype(Class<?> type) {
        return Code.class.isAssignableFrom(type) && !Code.class.equals(type);
    }

    /**
     * @param modelObject
     *            a model object which represents a FHIR resource or element
     * @return true if {@code modelObject} is an element; otherwise false
     */
    public static boolean isElement(Object modelObject) {
        return (modelObject instanceof Element);
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @return true if {@code modelClass} is an element; otherwise false
     */
    public static boolean isElementType(Class<?> modelClass) {
        return Element.class.isAssignableFrom(modelClass);
    }

    /**
     * @param type
     *            a model class which represents a FHIR element
     * @return true if {@code type} is a metadata type; otherwise false
     * @see <a href="https://www.hl7.org/fhir/R4/metadatatypes.html">https://www.hl7.org/fhir/R4/metadatatypes.html</a>
     */
    public static boolean isMetadataType(Class<?> type) {
        return ContactDetail.class.equals(type) ||
                Contributor.class.equals(type) ||
                DataRequirement.class.isAssignableFrom(type) ||
                RelatedArtifact.class.isAssignableFrom(type) ||
                UsageContext.class.equals(type) ||
                ParameterDefinition.class.equals(type) ||
                Expression.class.equals(type) ||
                TriggerDefinition.class.equals(type);
    }

    /**
     * @return true if {@code type} is a model class that represents a FHIR resource or element; otherwise false
     */
    public static boolean isModelClass(Class<?> type) {
        return isResourceType(type) || isElementType(type);
    }

    /**
     * @param type
     *            a model class which represents a FHIR element
     * @return true if {@code type} is a model class that represents a FHIR primitive type; otherwise false
     * @implNote xhtml is considered a primitive type
     * @see <a href="https://www.hl7.org/fhir/R4/datatypes.html#primitive">https://www.hl7.org/fhir/R4/datatypes.html#primitive</a>
     */
    public static boolean isPrimitiveType(Class<?> type) {
        return Base64Binary.class.equals(type) ||
            com.ibm.fhir.model.type.Boolean.class.equals(type) ||
            com.ibm.fhir.model.type.String.class.isAssignableFrom(type) ||
            Uri.class.isAssignableFrom(type) ||
            DateTime.class.equals(type) ||
            Date.class.equals(type) ||
            Time.class.equals(type) ||
            Instant.class.equals(type) ||
            com.ibm.fhir.model.type.Integer.class.isAssignableFrom(type) ||
            Decimal.class.equals(type) ||
            Xhtml.class.equals(type);
    }

    /**
     * @param type
     *            a model class which represents a FHIR element
     * @return true if {@code type} is a profiled data type; otherwise false
     */
    public static boolean isProfiledType(Class<?> type) {
        return CONCRETE_TYPE_MAP.containsKey(type);
    }

    private static boolean isRepeating(Field field) {
        return List.class.equals(field.getType());
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @param elementName
     *            the name of the element; choice element names do not include a type suffix
     * @return true if {@code modelClass} has an element {@code elementName} and it has max cardinality > 1;
     *         otherwise false
     */
    public static boolean isRepeatingElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isRepeating();
        }
        return false;
    }

    private static boolean isRequired(Field field) {
        return field.isAnnotationPresent(Required.class);
    }

    private static boolean isSummary(Field field) {
        return field.isAnnotationPresent(Summary.class);
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @param elementName
     *            the name of the element; choice element names do not include a type suffix
     * @return true if {@code modelClass} has an element {@code elementName} and it has min cardinality > 0;
     *         otherwise false
     */
    public static boolean isRequiredElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isRequired();
        }
        return false;
    }

    /**
     * @param modelObject
     *            a model object which represents a FHIR resource or element
     * @return true if {@code modelObject} represents a FHIR resource; otherwise false
     */
    public static boolean isResource(Object modelObject) {
        return (modelObject instanceof Resource);
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @return true if {@code modelClass} represents a FHIR resource; otherwise false
     */
    public static boolean isResourceType(Class<?> modelClass) {
        return Resource.class.isAssignableFrom(modelClass);
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @return true if {@code modelClass} is an abstract FHIR model class; otherwise false
     */
    public static boolean isAbstract(Class<?> modelClass) {
        return Modifier.isAbstract(modelClass.getModifiers());
    }

    /**
     * @param name
     *            the resource type name in titlecase to match the corresponding model class name
     * @return true if {@code name} is a valid FHIR resource name; otherwise false
     * @implSpec this method returns true for abstract types like {@code Resource} and {@code DomainResource}
     */
    public static boolean isResourceType(String name) {
        return RESOURCE_TYPE_MAP.containsKey(name);
    }

    /**
     * @param name
     *            the resource type name in titlecase to match the corresponding model class name
     * @return true if {@code name} is a valid FHIR resource name; otherwise false
     * @implSpec this method returns false for abstract types like {@code Resource} and {@code DomainResource}
     */
    public static boolean isConcreteResourceType(String name) {
        Class<?> modelClass = RESOURCE_TYPE_MAP.get(name);
        return modelClass != null && !isAbstract(modelClass);
    }

    /**
     * @param modelClass
     *            a model class which represents a FHIR resource or element
     * @param elementName
     *            the name of the element; choice element names do not include a type suffix
     * @return true if {@code modelClass} has an element {@code elementName} and its marked as a summary element;
     *         otherwise false
     */
    public static boolean isSummaryElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isSummary();
        }
        return false;
    }

    /**
     * @return a copy of the passed ZonedDateTime with the time truncated to {@code unit}
     */
    public static ZonedDateTime truncateTime(ZonedDateTime dateTime, ChronoUnit unit) {
        return dateTime == null ? null : dateTime.truncatedTo(unit);
    }

    /**
     * @return a copy of the passed LocalTime with the time truncated to {@code unit}
     */
    public static LocalTime truncateTime(LocalTime time, ChronoUnit unit) {
        return time == null ? null : time.truncatedTo(unit);
    }

    /**
     * @return a copy of the passed TemporalAccessor with the time truncated to {@code unit}
     */
    public static TemporalAccessor truncateTime(TemporalAccessor ta, ChronoUnit unit) {
        if (ta instanceof java.time.Instant) {
            ta = ((java.time.Instant) ta).truncatedTo(unit);
        } else if (ta instanceof ZonedDateTime) {
            ta = ((ZonedDateTime) ta).truncatedTo(unit);
        } else if (ta instanceof LocalDateTime) {
            ta = ((LocalDateTime) ta).truncatedTo(unit);
        } else if (ta instanceof LocalTime) {
            ta = ((LocalTime) ta).truncatedTo(unit);
        } else if (ta instanceof OffsetTime) {
            ta = ((OffsetTime) ta).truncatedTo(unit);
        } else if (ta instanceof OffsetDateTime) {
            ta = ((OffsetDateTime) ta).truncatedTo(unit);
        }

        return ta;
    }

    /**
     * @return true if {@code identifier} is a reserved keyword in FHIRPath version N1
     * @see <a href="http://hl7.org/fhirpath/2018Sep/index.html#keywords">http://hl7.org/fhirpath/2018Sep/index.html#keywords</a>
     */
    public static boolean isKeyword(String identifier) {
        return KEYWORDS.contains(identifier);
    }

    /**
     * Wraps the passed string identifier for use in FHIRPath
     * @see <a href="http://hl7.org/fhirpath/2018Sep/index.html#keywords">http://hl7.org/fhirpath/2018Sep/index.html#keywords</a>
     */
    public static String delimit(String identifier) {
        return String.format("`%s`", identifier);
    }

    /**
     * @return the implicit system for {@code code} if present, otherwise null
     */
    public static String getSystem(Code code) {
        if (code != null && code.getClass().isAnnotationPresent(com.ibm.fhir.model.annotation.System.class)) {
            return code.getClass().getAnnotation(com.ibm.fhir.model.annotation.System.class).value();
        }
        return null;
    }

    /**
     * @return the data type class associated with {@code typeName} parameter if exists, otherwise null
     */
    public static Class<?> getDataType(String typeName) {
        return DATA_TYPE_MAP.get(typeName);
    }

    public static boolean isCodeSubtype(String name) {
        return CODE_SUBTYPE_MAP.containsKey(name);
    }

    public static Collection<Class<?>> getCodeSubtypes() {
        return CODE_SUBTYPE_MAP.values();
    }
}
