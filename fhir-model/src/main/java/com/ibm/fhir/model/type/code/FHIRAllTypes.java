/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/data-types")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class FHIRAllTypes extends Code {
    /**
     * Address
     * 
     * <p>An address expressed using postal conventions (as opposed to GPS or other location definition formats). This data 
     * type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be 
     * valid for mail delivery. There are a variety of postal address formats defined around the world.
     */
    public static final FHIRAllTypes ADDRESS = FHIRAllTypes.builder().value(Value.ADDRESS).build();

    /**
     * Age
     * 
     * <p>A duration of time during which an organism (or a process) has existed.
     */
    public static final FHIRAllTypes AGE = FHIRAllTypes.builder().value(Value.AGE).build();

    /**
     * Annotation
     * 
     * <p>A text note which also contains information about who made the statement and when.
     */
    public static final FHIRAllTypes ANNOTATION = FHIRAllTypes.builder().value(Value.ANNOTATION).build();

    /**
     * Attachment
     * 
     * <p>For referring to data content defined in other formats.
     */
    public static final FHIRAllTypes ATTACHMENT = FHIRAllTypes.builder().value(Value.ATTACHMENT).build();

    /**
     * BackboneElement
     * 
     * <p>Base definition for all elements that are defined inside a resource - but not those in a data type.
     */
    public static final FHIRAllTypes BACKBONE_ELEMENT = FHIRAllTypes.builder().value(Value.BACKBONE_ELEMENT).build();

    /**
     * CodeableConcept
     * 
     * <p>A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
     */
    public static final FHIRAllTypes CODEABLE_CONCEPT = FHIRAllTypes.builder().value(Value.CODEABLE_CONCEPT).build();

    /**
     * CodeableReference
     * 
     * <p>A reference to a resource (by instance), or instead, a reference to a cencept defined in a terminology or ontology 
     * (by class).
     */
    public static final FHIRAllTypes CODEABLE_REFERENCE = FHIRAllTypes.builder().value(Value.CODEABLE_REFERENCE).build();

    /**
     * Coding
     * 
     * <p>A reference to a code defined by a terminology system.
     */
    public static final FHIRAllTypes CODING = FHIRAllTypes.builder().value(Value.CODING).build();

    /**
     * ContactDetail
     * 
     * <p>Specifies contact information for a person or organization.
     */
    public static final FHIRAllTypes CONTACT_DETAIL = FHIRAllTypes.builder().value(Value.CONTACT_DETAIL).build();

    /**
     * ContactPoint
     * 
     * <p>Details for all kinds of technology mediated contact points for a person or organization, including telephone, 
     * email, etc.
     */
    public static final FHIRAllTypes CONTACT_POINT = FHIRAllTypes.builder().value(Value.CONTACT_POINT).build();

    /**
     * Contributor
     * 
     * <p>A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.
     */
    public static final FHIRAllTypes CONTRIBUTOR = FHIRAllTypes.builder().value(Value.CONTRIBUTOR).build();

    /**
     * Count
     * 
     * <p>A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that 
     * are not precisely quantified, including amounts involving arbitrary units and floating currencies.
     */
    public static final FHIRAllTypes COUNT = FHIRAllTypes.builder().value(Value.COUNT).build();

    /**
     * DataRequirement
     * 
     * <p>Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters 
     * of the data.
     */
    public static final FHIRAllTypes DATA_REQUIREMENT = FHIRAllTypes.builder().value(Value.DATA_REQUIREMENT).build();

    /**
     * DataType
     * 
     * <p>The base class for all re-useable types defined as part of the FHIR Specification.
     */
    public static final FHIRAllTypes DATA_TYPE = FHIRAllTypes.builder().value(Value.DATA_TYPE).build();

    /**
     * Distance
     * 
     * <p>A length - a value with a unit that is a physical distance.
     */
    public static final FHIRAllTypes DISTANCE = FHIRAllTypes.builder().value(Value.DISTANCE).build();

    /**
     * Dosage
     * 
     * <p>Indicates how the medication is/was taken or should be taken by the patient.
     */
    public static final FHIRAllTypes DOSAGE = FHIRAllTypes.builder().value(Value.DOSAGE).build();

    /**
     * Duration
     * 
     * <p>A length of time.
     */
    public static final FHIRAllTypes DURATION = FHIRAllTypes.builder().value(Value.DURATION).build();

    /**
     * Element
     * 
     * <p>Base definition for all elements in a resource.
     */
    public static final FHIRAllTypes ELEMENT = FHIRAllTypes.builder().value(Value.ELEMENT).build();

    /**
     * ElementDefinition
     * 
     * <p>Captures constraints on each element within the resource, profile, or extension.
     */
    public static final FHIRAllTypes ELEMENT_DEFINITION = FHIRAllTypes.builder().value(Value.ELEMENT_DEFINITION).build();

    /**
     * Expression
     * 
     * <p>A expression that is evaluated in a specified context and returns a value. The context of use of the expression 
     * must specify the context in which the expression is evaluated, and how the result of the expression is used.
     */
    public static final FHIRAllTypes EXPRESSION = FHIRAllTypes.builder().value(Value.EXPRESSION).build();

    /**
     * Extension
     * 
     * <p>Optional Extension Element - found in all resources.
     */
    public static final FHIRAllTypes EXTENSION = FHIRAllTypes.builder().value(Value.EXTENSION).build();

    /**
     * HumanName
     * 
     * <p>A human's name with the ability to identify parts and usage.
     */
    public static final FHIRAllTypes HUMAN_NAME = FHIRAllTypes.builder().value(Value.HUMAN_NAME).build();

    /**
     * Identifier
     * 
     * <p>An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.
     */
    public static final FHIRAllTypes IDENTIFIER = FHIRAllTypes.builder().value(Value.IDENTIFIER).build();

    /**
     * MarketingStatus
     * 
     * <p>The marketing status describes the date when a medicinal product is actually put on the market or the date as of 
     * which it is no longer available.
     */
    public static final FHIRAllTypes MARKETING_STATUS = FHIRAllTypes.builder().value(Value.MARKETING_STATUS).build();

    /**
     * Meta
     * 
     * <p>The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to 
     * the content might not always be associated with version changes to the resource.
     */
    public static final FHIRAllTypes META = FHIRAllTypes.builder().value(Value.META).build();

    /**
     * Money
     * 
     * <p>An amount of economic utility in some recognized currency.
     */
    public static final FHIRAllTypes MONEY = FHIRAllTypes.builder().value(Value.MONEY).build();

    /**
     * MoneyQuantity
     */
    public static final FHIRAllTypes MONEY_QUANTITY = FHIRAllTypes.builder().value(Value.MONEY_QUANTITY).build();

    /**
     * Narrative
     * 
     * <p>A human-readable summary of the resource conveying the essential clinical and business information for the resource.
     */
    public static final FHIRAllTypes NARRATIVE = FHIRAllTypes.builder().value(Value.NARRATIVE).build();

    /**
     * ParameterDefinition
     * 
     * <p>The parameters to the module. This collection specifies both the input and output parameters. Input parameters are 
     * provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
     */
    public static final FHIRAllTypes PARAMETER_DEFINITION = FHIRAllTypes.builder().value(Value.PARAMETER_DEFINITION).build();

    /**
     * Period
     * 
     * <p>A time period defined by a start and end date and optionally time.
     */
    public static final FHIRAllTypes PERIOD = FHIRAllTypes.builder().value(Value.PERIOD).build();

    /**
     * Population
     * 
     * <p>A populatioof people with some set of grouping criteria.
     */
    public static final FHIRAllTypes POPULATION = FHIRAllTypes.builder().value(Value.POPULATION).build();

    /**
     * ProdCharacteristic
     * 
     * <p>The marketing status describes the date when a medicinal product is actually put on the market or the date as of 
     * which it is no longer available.
     */
    public static final FHIRAllTypes PROD_CHARACTERISTIC = FHIRAllTypes.builder().value(Value.PROD_CHARACTERISTIC).build();

    /**
     * ProductShelfLife
     * 
     * <p>The shelf-life and storage information for a medicinal product item or container can be described using this class.
     */
    public static final FHIRAllTypes PRODUCT_SHELF_LIFE = FHIRAllTypes.builder().value(Value.PRODUCT_SHELF_LIFE).build();

    /**
     * Quantity
     * 
     * <p>A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that 
     * are not precisely quantified, including amounts involving arbitrary units and floating currencies.
     */
    public static final FHIRAllTypes QUANTITY = FHIRAllTypes.builder().value(Value.QUANTITY).build();

    /**
     * Range
     * 
     * <p>A set of ordered Quantities defined by a low and high limit.
     */
    public static final FHIRAllTypes RANGE = FHIRAllTypes.builder().value(Value.RANGE).build();

    /**
     * Ratio
     * 
     * <p>A relationship of two Quantity values - expressed as a numerator and a denominator.
     */
    public static final FHIRAllTypes RATIO = FHIRAllTypes.builder().value(Value.RATIO).build();

    /**
     * RatioRange
     * 
     * <p>A range of ratios expressed as a low and high numerator and a denominator.
     */
    public static final FHIRAllTypes RATIO_RANGE = FHIRAllTypes.builder().value(Value.RATIO_RANGE).build();

    /**
     * Reference
     * 
     * <p>A reference from one resource to another.
     */
    public static final FHIRAllTypes REFERENCE = FHIRAllTypes.builder().value(Value.REFERENCE).build();

    /**
     * RelatedArtifact
     * 
     * <p>Related artifacts such as additional documentation, justification, or bibliographic references.
     */
    public static final FHIRAllTypes RELATED_ARTIFACT = FHIRAllTypes.builder().value(Value.RELATED_ARTIFACT).build();

    /**
     * SampledData
     * 
     * <p>A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in 
     * the data.
     */
    public static final FHIRAllTypes SAMPLED_DATA = FHIRAllTypes.builder().value(Value.SAMPLED_DATA).build();

    /**
     * Signature
     * 
     * <p>A signature along with supporting context. The signature may be a digital signature that is cryptographic in 
     * nature, or some other signature acceptable to the domain. This other signature may be as simple as a graphical image 
     * representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.
     */
    public static final FHIRAllTypes SIGNATURE = FHIRAllTypes.builder().value(Value.SIGNATURE).build();

    /**
     * SimpleQuantity
     */
    public static final FHIRAllTypes SIMPLE_QUANTITY = FHIRAllTypes.builder().value(Value.SIMPLE_QUANTITY).build();

    /**
     * Timing
     * 
     * <p>Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, 
     * expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used 
     * when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were 
     * carried out.
     */
    public static final FHIRAllTypes TIMING = FHIRAllTypes.builder().value(Value.TIMING).build();

    /**
     * TriggerDefinition
     * 
     * <p>A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined 
     * by the type element.
     */
    public static final FHIRAllTypes TRIGGER_DEFINITION = FHIRAllTypes.builder().value(Value.TRIGGER_DEFINITION).build();

    /**
     * UsageContext
     * 
     * <p>Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This 
     * metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care 
     * (e.g., venue, care setting, provider of care).
     */
    public static final FHIRAllTypes USAGE_CONTEXT = FHIRAllTypes.builder().value(Value.USAGE_CONTEXT).build();

    /**
     * base64Binary
     * 
     * <p>A stream of bytes
     */
    public static final FHIRAllTypes BASE64BINARY = FHIRAllTypes.builder().value(Value.BASE64BINARY).build();

    /**
     * boolean
     * 
     * <p>Value of "true" or "false"
     */
    public static final FHIRAllTypes BOOLEAN = FHIRAllTypes.builder().value(Value.BOOLEAN).build();

    /**
     * canonical
     * 
     * <p>A URI that is a reference to a canonical URL on a FHIR resource
     */
    public static final FHIRAllTypes CANONICAL = FHIRAllTypes.builder().value(Value.CANONICAL).build();

    /**
     * code
     * 
     * <p>A string which has at least one character and no leading or trailing whitespace and where there is no whitespace 
     * other than single spaces in the contents
     */
    public static final FHIRAllTypes CODE = FHIRAllTypes.builder().value(Value.CODE).build();

    /**
     * date
     * 
     * <p>A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema 
     * types gYear, gYearMonth and date. Dates SHALL be valid dates.
     */
    public static final FHIRAllTypes DATE = FHIRAllTypes.builder().value(Value.DATE).build();

    /**
     * dateTime
     * 
     * <p>A date, date-time or partial date (e.g. just year or year + month). If hours and minutes are specified, a time zone 
     * SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be 
     * provided due to schema type constraints but may be zero-filled and may be ignored. Dates SHALL be valid dates.
     */
    public static final FHIRAllTypes DATE_TIME = FHIRAllTypes.builder().value(Value.DATE_TIME).build();

    /**
     * decimal
     * 
     * <p>A rational number with implicit precision
     */
    public static final FHIRAllTypes DECIMAL = FHIRAllTypes.builder().value(Value.DECIMAL).build();

    /**
     * id
     * 
     * <p>Any combination of letters, numerals, "-" and ".", with a length limit of 64 characters. (This might be an integer, 
     * an unprefixed OID, UUID or any other identifier pattern that meets these constraints.) Ids are case-insensitive.
     */
    public static final FHIRAllTypes ID = FHIRAllTypes.builder().value(Value.ID).build();

    /**
     * instant
     * 
     * <p>An instant in time - known at least to the second
     */
    public static final FHIRAllTypes INSTANT = FHIRAllTypes.builder().value(Value.INSTANT).build();

    /**
     * integer
     * 
     * <p>A whole number
     */
    public static final FHIRAllTypes INTEGER = FHIRAllTypes.builder().value(Value.INTEGER).build();

    /**
     * markdown
     * 
     * <p>A string that may contain Github Flavored Markdown syntax for optional processing by a mark down presentation engine
     */
    public static final FHIRAllTypes MARKDOWN = FHIRAllTypes.builder().value(Value.MARKDOWN).build();

    /**
     * oid
     * 
     * <p>An OID represented as a URI
     */
    public static final FHIRAllTypes OID = FHIRAllTypes.builder().value(Value.OID).build();

    /**
     * positiveInt
     * 
     * <p>An integer with a value that is positive (e.g. &gt;0)
     */
    public static final FHIRAllTypes POSITIVE_INT = FHIRAllTypes.builder().value(Value.POSITIVE_INT).build();

    /**
     * string
     * 
     * <p>A sequence of Unicode characters
     */
    public static final FHIRAllTypes STRING = FHIRAllTypes.builder().value(Value.STRING).build();

    /**
     * time
     * 
     * <p>A time during the day, with no date specified
     */
    public static final FHIRAllTypes TIME = FHIRAllTypes.builder().value(Value.TIME).build();

    /**
     * unsignedInt
     * 
     * <p>An integer with a value that is not negative (e.g. &gt;= 0)
     */
    public static final FHIRAllTypes UNSIGNED_INT = FHIRAllTypes.builder().value(Value.UNSIGNED_INT).build();

    /**
     * uri
     * 
     * <p>String of characters used to identify a name or a resource
     */
    public static final FHIRAllTypes URI = FHIRAllTypes.builder().value(Value.URI).build();

    /**
     * url
     * 
     * <p>A URI that is a literal reference
     */
    public static final FHIRAllTypes URL = FHIRAllTypes.builder().value(Value.URL).build();

    /**
     * uuid
     * 
     * <p>A UUID, represented as a URI
     */
    public static final FHIRAllTypes UUID = FHIRAllTypes.builder().value(Value.UUID).build();

    /**
     * XHTML
     * 
     * <p>XHTML format, as defined by W3C, but restricted usage (mainly, no active content)
     */
    public static final FHIRAllTypes XHTML = FHIRAllTypes.builder().value(Value.XHTML).build();

    /**
     * Account
     * 
     * <p>A financial tool for tracking value accrued for a particular purpose. In the healthcare field, used to track 
     * charges for a patient, cost centers, etc.
     */
    public static final FHIRAllTypes ACCOUNT = FHIRAllTypes.builder().value(Value.ACCOUNT).build();

    /**
     * ActivityDefinition
     * 
     * <p>This resource allows for the definition of some activity to be performed, independent of a particular patient, 
     * practitioner, or other performance context.
     */
    public static final FHIRAllTypes ACTIVITY_DEFINITION = FHIRAllTypes.builder().value(Value.ACTIVITY_DEFINITION).build();

    /**
     * AdministrableProductDefinition
     * 
     * <p>A medicinal product in the final form which is suitable for administering to a patient (after any mixing of 
     * multiple components, dissolution etc. has been performed).
     */
    public static final FHIRAllTypes ADMINISTRABLE_PRODUCT_DEFINITION = FHIRAllTypes.builder().value(Value.ADMINISTRABLE_PRODUCT_DEFINITION).build();

    /**
     * AdverseEvent
     * 
     * <p>Actual or potential/avoided event causing unintended physical injury resulting from or contributed to by medical 
     * care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or 
     * hospitalization, or that results in death.
     */
    public static final FHIRAllTypes ADVERSE_EVENT = FHIRAllTypes.builder().value(Value.ADVERSE_EVENT).build();

    /**
     * AllergyIntolerance
     * 
     * <p>Risk of harmful or undesirable, physiological response which is unique to an individual and associated with 
     * exposure to a substance.
     */
    public static final FHIRAllTypes ALLERGY_INTOLERANCE = FHIRAllTypes.builder().value(Value.ALLERGY_INTOLERANCE).build();

    /**
     * Appointment
     * 
     * <p>A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a 
     * specific date/time. This may result in one or more Encounter(s).
     */
    public static final FHIRAllTypes APPOINTMENT = FHIRAllTypes.builder().value(Value.APPOINTMENT).build();

    /**
     * AppointmentResponse
     * 
     * <p>A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
     */
    public static final FHIRAllTypes APPOINTMENT_RESPONSE = FHIRAllTypes.builder().value(Value.APPOINTMENT_RESPONSE).build();

    /**
     * AuditEvent
     * 
     * <p>A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion 
     * attempts and monitoring for inappropriate usage.
     */
    public static final FHIRAllTypes AUDIT_EVENT = FHIRAllTypes.builder().value(Value.AUDIT_EVENT).build();

    /**
     * Basic
     * 
     * <p>Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing 
     * resource, and custom resources not appropriate for inclusion in the FHIR specification.
     */
    public static final FHIRAllTypes BASIC = FHIRAllTypes.builder().value(Value.BASIC).build();

    /**
     * Binary
     * 
     * <p>A resource that represents the data of a single raw artifact as digital content accessible in its native format. A 
     * Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
     */
    public static final FHIRAllTypes BINARY = FHIRAllTypes.builder().value(Value.BINARY).build();

    /**
     * BiologicallyDerivedProduct
     * 
     * <p>A material substance originating from a biological entity intended to be transplanted or infused
     * <p>into another (possibly the same) biological entity.
     */
    public static final FHIRAllTypes BIOLOGICALLY_DERIVED_PRODUCT = FHIRAllTypes.builder().value(Value.BIOLOGICALLY_DERIVED_PRODUCT).build();

    /**
     * BodyStructure
     * 
     * <p>Record details about an anatomical structure. This resource may be used when a coded concept does not provide the 
     * necessary detail needed for the use case.
     */
    public static final FHIRAllTypes BODY_STRUCTURE = FHIRAllTypes.builder().value(Value.BODY_STRUCTURE).build();

    /**
     * Bundle
     * 
     * <p>A container for a collection of resources.
     */
    public static final FHIRAllTypes BUNDLE = FHIRAllTypes.builder().value(Value.BUNDLE).build();

    /**
     * CapabilityStatement
     * 
     * <p>A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server for a particular version of 
     * FHIR that may be used as a statement of actual server functionality or a statement of required or desired server 
     * implementation.
     */
    public static final FHIRAllTypes CAPABILITY_STATEMENT = FHIRAllTypes.builder().value(Value.CAPABILITY_STATEMENT).build();

    /**
     * CarePlan
     * 
     * <p>Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or 
     * community for a period of time, possibly limited to care for a specific condition or set of conditions.
     */
    public static final FHIRAllTypes CARE_PLAN = FHIRAllTypes.builder().value(Value.CARE_PLAN).build();

    /**
     * CareTeam
     * 
     * <p>The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of 
     * care for a patient.
     */
    public static final FHIRAllTypes CARE_TEAM = FHIRAllTypes.builder().value(Value.CARE_TEAM).build();

    /**
     * CatalogEntry
     * 
     * <p>Catalog entries are wrappers that contextualize items included in a catalog.
     */
    public static final FHIRAllTypes CATALOG_ENTRY = FHIRAllTypes.builder().value(Value.CATALOG_ENTRY).build();

    /**
     * ChargeItem
     * 
     * <p>The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore 
     * referring not only to the product, but containing in addition details of the provision, like date, time, amounts and 
     * participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal 
     * cost allocation.
     */
    public static final FHIRAllTypes CHARGE_ITEM = FHIRAllTypes.builder().value(Value.CHARGE_ITEM).build();

    /**
     * ChargeItemDefinition
     * 
     * <p>The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate 
     * costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a 
     * rough structure and requires profiling for each type of billing code system.
     */
    public static final FHIRAllTypes CHARGE_ITEM_DEFINITION = FHIRAllTypes.builder().value(Value.CHARGE_ITEM_DEFINITION).build();

    /**
     * Citation
     * 
     * <p>The Citation Resource enables reference to any knowledge artifact for purposes of identification and attribution. 
     * The Citation Resource supports existing reference structures and developing publication practices such as versioning, 
     * expressing complex contributorship roles, and referencing computable resources.
     */
    public static final FHIRAllTypes CITATION = FHIRAllTypes.builder().value(Value.CITATION).build();

    /**
     * Claim
     * 
     * <p>A provider issued list of professional services and products which have been provided, or are to be provided, to a 
     * patient which is sent to an insurer for reimbursement.
     */
    public static final FHIRAllTypes CLAIM = FHIRAllTypes.builder().value(Value.CLAIM).build();

    /**
     * ClaimResponse
     * 
     * <p>This resource provides the adjudication details from the processing of a Claim resource.
     */
    public static final FHIRAllTypes CLAIM_RESPONSE = FHIRAllTypes.builder().value(Value.CLAIM_RESPONSE).build();

    /**
     * ClinicalImpression
     * 
     * <p>A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning 
     * the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with 
     * a clinical consultation / encounter, but this varies greatly depending on the clinical workflow. This resource is 
     * called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools 
     * such as Apgar score.
     */
    public static final FHIRAllTypes CLINICAL_IMPRESSION = FHIRAllTypes.builder().value(Value.CLINICAL_IMPRESSION).build();

    /**
     * ClinicalUseDefinition
     * 
     * <p>A single issue - either an indication, contraindication, interaction or an undesirable effect for a medicinal 
     * product, medication, device or procedure.
     */
    public static final FHIRAllTypes CLINICAL_USE_DEFINITION = FHIRAllTypes.builder().value(Value.CLINICAL_USE_DEFINITION).build();

    /**
     * CodeSystem
     * 
     * <p>The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement 
     * and its key properties, and optionally define a part or all of its content.
     */
    public static final FHIRAllTypes CODE_SYSTEM = FHIRAllTypes.builder().value(Value.CODE_SYSTEM).build();

    /**
     * Communication
     * 
     * <p>An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public 
     * health agency that was notified about a reportable condition.
     */
    public static final FHIRAllTypes COMMUNICATION = FHIRAllTypes.builder().value(Value.COMMUNICATION).build();

    /**
     * CommunicationRequest
     * 
     * <p>A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the 
     * CDS system proposes that the public health agency be notified about a reportable condition.
     */
    public static final FHIRAllTypes COMMUNICATION_REQUEST = FHIRAllTypes.builder().value(Value.COMMUNICATION_REQUEST).build();

    /**
     * CompartmentDefinition
     * 
     * <p>A compartment definition that defines how resources are accessed on a server.
     */
    public static final FHIRAllTypes COMPARTMENT_DEFINITION = FHIRAllTypes.builder().value(Value.COMPARTMENT_DEFINITION).build();

    /**
     * Composition
     * 
     * <p>A set of healthcare-related information that is assembled together into a single logical package that provides a 
     * single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who 
     * is making the statement. A Composition defines the structure and narrative content necessary for a document. However, 
     * a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where 
     * Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in 
     * the Bundle (for example Patient, Practitioner, Encounter, etc.).
     */
    public static final FHIRAllTypes COMPOSITION = FHIRAllTypes.builder().value(Value.COMPOSITION).build();

    /**
     * ConceptMap
     * 
     * <p>A statement of relationships from one set of concepts to one or more other concepts - either concepts in code 
     * systems, or data element/data element concepts, or classes in class models.
     */
    public static final FHIRAllTypes CONCEPT_MAP = FHIRAllTypes.builder().value(Value.CONCEPT_MAP).build();

    /**
     * Condition
     * 
     * <p>A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a 
     * level of concern.
     */
    public static final FHIRAllTypes CONDITION = FHIRAllTypes.builder().value(Value.CONDITION).build();

    /**
     * Consent
     * 
     * <p>A record of a healthcare consumer’s choices, which permits or denies identified recipient(s) or recipient role(s) 
     * to perform one or more actions within a given policy context, for specific purposes and periods of time.
     */
    public static final FHIRAllTypes CONSENT = FHIRAllTypes.builder().value(Value.CONSENT).build();

    /**
     * Contract
     * 
     * <p>Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
     */
    public static final FHIRAllTypes CONTRACT = FHIRAllTypes.builder().value(Value.CONTRACT).build();

    /**
     * Coverage
     * 
     * <p>Financial instrument which may be used to reimburse or pay for health care products and services. Includes both 
     * insurance and self-payment.
     */
    public static final FHIRAllTypes COVERAGE = FHIRAllTypes.builder().value(Value.COVERAGE).build();

    /**
     * CoverageEligibilityRequest
     * 
     * <p>The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to 
     * respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is 
     * valid and in-force and optionally to provide the insurance details of the policy.
     */
    public static final FHIRAllTypes COVERAGE_ELIGIBILITY_REQUEST = FHIRAllTypes.builder().value(Value.COVERAGE_ELIGIBILITY_REQUEST).build();

    /**
     * CoverageEligibilityResponse
     * 
     * <p>This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.
     */
    public static final FHIRAllTypes COVERAGE_ELIGIBILITY_RESPONSE = FHIRAllTypes.builder().value(Value.COVERAGE_ELIGIBILITY_RESPONSE).build();

    /**
     * DetectedIssue
     * 
     * <p>Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for 
     * a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
     */
    public static final FHIRAllTypes DETECTED_ISSUE = FHIRAllTypes.builder().value(Value.DETECTED_ISSUE).build();

    /**
     * Device
     * 
     * <p>A type of a manufactured item that is used in the provision of healthcare without being substantially changed 
     * through that activity. The device may be a medical or non-medical device.
     */
    public static final FHIRAllTypes DEVICE = FHIRAllTypes.builder().value(Value.DEVICE).build();

    /**
     * DeviceDefinition
     * 
     * <p>The characteristics, operational status and capabilities of a medical-related component of a medical device.
     */
    public static final FHIRAllTypes DEVICE_DEFINITION = FHIRAllTypes.builder().value(Value.DEVICE_DEFINITION).build();

    /**
     * DeviceMetric
     * 
     * <p>Describes a measurement, calculation or setting capability of a medical device.
     */
    public static final FHIRAllTypes DEVICE_METRIC = FHIRAllTypes.builder().value(Value.DEVICE_METRIC).build();

    /**
     * DeviceRequest
     * 
     * <p>Represents a request for a patient to employ a medical device. The device may be an implantable device, or an 
     * external assistive device, such as a walker.
     */
    public static final FHIRAllTypes DEVICE_REQUEST = FHIRAllTypes.builder().value(Value.DEVICE_REQUEST).build();

    /**
     * DeviceUseStatement
     * 
     * <p>A record of a device being used by a patient where the record is the result of a report from the patient or another 
     * clinician.
     */
    public static final FHIRAllTypes DEVICE_USE_STATEMENT = FHIRAllTypes.builder().value(Value.DEVICE_USE_STATEMENT).build();

    /**
     * DiagnosticReport
     * 
     * <p>The findings and interpretation of diagnostic tests performed on patients, groups of patients, devices, and 
     * locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider 
     * information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation 
     * of diagnostic reports.
     */
    public static final FHIRAllTypes DIAGNOSTIC_REPORT = FHIRAllTypes.builder().value(Value.DIAGNOSTIC_REPORT).build();

    /**
     * DocumentManifest
     * 
     * <p>A collection of documents compiled for a purpose together with metadata that applies to the collection.
     */
    public static final FHIRAllTypes DOCUMENT_MANIFEST = FHIRAllTypes.builder().value(Value.DOCUMENT_MANIFEST).build();

    /**
     * DocumentReference
     * 
     * <p>A reference to a document of any kind for any purpose. Provides metadata about the document so that the document 
     * can be discovered and managed. The scope of a document is any seralized object with a mime-type, so includes formal 
     * patient centric documents (CDA), cliical notes, scanned paper, and non-patient specific documents like policy text.
     */
    public static final FHIRAllTypes DOCUMENT_REFERENCE = FHIRAllTypes.builder().value(Value.DOCUMENT_REFERENCE).build();

    /**
     * DomainResource
     * 
     * <p>A resource that includes narrative, extensions, and contained resources.
     */
    public static final FHIRAllTypes DOMAIN_RESOURCE = FHIRAllTypes.builder().value(Value.DOMAIN_RESOURCE).build();

    /**
     * Encounter
     * 
     * <p>An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or 
     * assessing the health status of a patient.
     */
    public static final FHIRAllTypes ENCOUNTER = FHIRAllTypes.builder().value(Value.ENCOUNTER).build();

    /**
     * Endpoint
     * 
     * <p>The technical details of an endpoint that can be used for electronic services, such as for web services providing 
     * XDS.b or a REST endpoint for another FHIR server. This may include any security context information.
     */
    public static final FHIRAllTypes ENDPOINT = FHIRAllTypes.builder().value(Value.ENDPOINT).build();

    /**
     * EnrollmentRequest
     * 
     * <p>This resource provides the insurance enrollment details to the insurer regarding a specified coverage.
     */
    public static final FHIRAllTypes ENROLLMENT_REQUEST = FHIRAllTypes.builder().value(Value.ENROLLMENT_REQUEST).build();

    /**
     * EnrollmentResponse
     * 
     * <p>This resource provides enrollment and plan details from the processing of an EnrollmentRequest resource.
     */
    public static final FHIRAllTypes ENROLLMENT_RESPONSE = FHIRAllTypes.builder().value(Value.ENROLLMENT_RESPONSE).build();

    /**
     * EpisodeOfCare
     * 
     * <p>An association between a patient and an organization / healthcare provider(s) during which time encounters may 
     * occur. The managing organization assumes a level of responsibility for the patient during this time.
     */
    public static final FHIRAllTypes EPISODE_OF_CARE = FHIRAllTypes.builder().value(Value.EPISODE_OF_CARE).build();

    /**
     * EventDefinition
     * 
     * <p>The EventDefinition resource provides a reusable description of when a particular event can occur.
     */
    public static final FHIRAllTypes EVENT_DEFINITION = FHIRAllTypes.builder().value(Value.EVENT_DEFINITION).build();

    /**
     * Evidence
     * 
     * <p>The Evidence Resource provides a machine-interpretable expression of an evidence concept including the evidence 
     * variables (eg population, exposures/interventions, comparators, outcomes, measured variables, confounding variables), 
     * the statistics, and the certainty of this evidence.
     */
    public static final FHIRAllTypes EVIDENCE = FHIRAllTypes.builder().value(Value.EVIDENCE).build();

    /**
     * EvidenceReport
     * 
     * <p>The EvidenceReport Resource is a specialized container for a collection of resources and codable concepts, adapted 
     * to support compositions of Evidence, EvidenceVariable, and Citation resources and related concepts.
     */
    public static final FHIRAllTypes EVIDENCE_REPORT = FHIRAllTypes.builder().value(Value.EVIDENCE_REPORT).build();

    /**
     * EvidenceVariable
     * 
     * <p>The EvidenceVariable resource describes an element that knowledge (Evidence) is about.
     */
    public static final FHIRAllTypes EVIDENCE_VARIABLE = FHIRAllTypes.builder().value(Value.EVIDENCE_VARIABLE).build();

    /**
     * ExampleScenario
     * 
     * <p>Example of workflow instance.
     */
    public static final FHIRAllTypes EXAMPLE_SCENARIO = FHIRAllTypes.builder().value(Value.EXAMPLE_SCENARIO).build();

    /**
     * ExplanationOfBenefit
     * 
     * <p>This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally 
     * account balance information, for informing the subscriber of the benefits provided.
     */
    public static final FHIRAllTypes EXPLANATION_OF_BENEFIT = FHIRAllTypes.builder().value(Value.EXPLANATION_OF_BENEFIT).build();

    /**
     * FamilyMemberHistory
     * 
     * <p>Significant health conditions for a person related to the patient relevant in the context of care for the patient.
     */
    public static final FHIRAllTypes FAMILY_MEMBER_HISTORY = FHIRAllTypes.builder().value(Value.FAMILY_MEMBER_HISTORY).build();

    /**
     * Flag
     * 
     * <p>Prospective warnings of potential issues when providing care to the patient.
     */
    public static final FHIRAllTypes FLAG = FHIRAllTypes.builder().value(Value.FLAG).build();

    /**
     * Goal
     * 
     * <p>Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring 
     * an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
     */
    public static final FHIRAllTypes GOAL = FHIRAllTypes.builder().value(Value.GOAL).build();

    /**
     * GraphDefinition
     * 
     * <p>A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by 
     * following references. The Graph Definition resource defines a set and makes rules about the set.
     */
    public static final FHIRAllTypes GRAPH_DEFINITION = FHIRAllTypes.builder().value(Value.GRAPH_DEFINITION).build();

    /**
     * Group
     * 
     * <p>Represents a defined collection of entities that may be discussed or acted upon collectively but which are not 
     * expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an 
     * Organization.
     */
    public static final FHIRAllTypes GROUP = FHIRAllTypes.builder().value(Value.GROUP).build();

    /**
     * GuidanceResponse
     * 
     * <p>A guidance response is the formal response to a guidance request, including any output parameters returned by the 
     * evaluation, as well as the description of any proposed actions to be taken.
     */
    public static final FHIRAllTypes GUIDANCE_RESPONSE = FHIRAllTypes.builder().value(Value.GUIDANCE_RESPONSE).build();

    /**
     * HealthcareService
     * 
     * <p>The details of a healthcare service available at a location.
     */
    public static final FHIRAllTypes HEALTHCARE_SERVICE = FHIRAllTypes.builder().value(Value.HEALTHCARE_SERVICE).build();

    /**
     * ImagingStudy
     * 
     * <p>Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which 
     * includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a 
     * common context. A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple 
     * series of different modalities.
     */
    public static final FHIRAllTypes IMAGING_STUDY = FHIRAllTypes.builder().value(Value.IMAGING_STUDY).build();

    /**
     * Immunization
     * 
     * <p>Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a 
     * patient, a clinician or another party.
     */
    public static final FHIRAllTypes IMMUNIZATION = FHIRAllTypes.builder().value(Value.IMMUNIZATION).build();

    /**
     * ImmunizationEvaluation
     * 
     * <p>Describes a comparison of an immunization event against published recommendations to determine if the 
     * administration is "valid" in relation to those recommendations.
     */
    public static final FHIRAllTypes IMMUNIZATION_EVALUATION = FHIRAllTypes.builder().value(Value.IMMUNIZATION_EVALUATION).build();

    /**
     * ImmunizationRecommendation
     * 
     * <p>A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional 
     * supporting justification.
     */
    public static final FHIRAllTypes IMMUNIZATION_RECOMMENDATION = FHIRAllTypes.builder().value(Value.IMMUNIZATION_RECOMMENDATION).build();

    /**
     * ImplementationGuide
     * 
     * <p>A set of rules of how a particular interoperability or standards problem is solved - typically through the use of 
     * FHIR resources. This resource is used to gather all the parts of an implementation guide into a logical whole and to 
     * publish a computable definition of all the parts.
     */
    public static final FHIRAllTypes IMPLEMENTATION_GUIDE = FHIRAllTypes.builder().value(Value.IMPLEMENTATION_GUIDE).build();

    /**
     * Ingredient
     * 
     * <p>An ingredient of a manufactured item or pharmaceutical product.
     */
    public static final FHIRAllTypes INGREDIENT = FHIRAllTypes.builder().value(Value.INGREDIENT).build();

    /**
     * InsurancePlan
     * 
     * <p>Details of a Health Insurance product/plan provided by an organization.
     */
    public static final FHIRAllTypes INSURANCE_PLAN = FHIRAllTypes.builder().value(Value.INSURANCE_PLAN).build();

    /**
     * Invoice
     * 
     * <p>Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing 
     * purpose.
     */
    public static final FHIRAllTypes INVOICE = FHIRAllTypes.builder().value(Value.INVOICE).build();

    /**
     * Library
     * 
     * <p>The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and 
     * expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a 
     * collection of knowledge assets.
     */
    public static final FHIRAllTypes LIBRARY = FHIRAllTypes.builder().value(Value.LIBRARY).build();

    /**
     * Linkage
     * 
     * <p>Identifies two or more records (resource instances) that refer to the same real-world "occurrence".
     */
    public static final FHIRAllTypes LINKAGE = FHIRAllTypes.builder().value(Value.LINKAGE).build();

    /**
     * List
     * 
     * <p>A list is a curated collection of resources.
     */
    public static final FHIRAllTypes LIST = FHIRAllTypes.builder().value(Value.LIST).build();

    /**
     * Location
     * 
     * <p>Details and position information for a physical place where services are provided and resources and participants 
     * may be stored, found, contained, or accommodated.
     */
    public static final FHIRAllTypes LOCATION = FHIRAllTypes.builder().value(Value.LOCATION).build();

    /**
     * ManufacturedItemDefinition
     * 
     * <p>The definition and characteristics of a medicinal manufactured item, such as a tablet or capsule, as contained in a 
     * packaged medicinal product.
     */
    public static final FHIRAllTypes MANUFACTURED_ITEM_DEFINITION = FHIRAllTypes.builder().value(Value.MANUFACTURED_ITEM_DEFINITION).build();

    /**
     * Measure
     * 
     * <p>The Measure resource provides the definition of a quality measure.
     */
    public static final FHIRAllTypes MEASURE = FHIRAllTypes.builder().value(Value.MEASURE).build();

    /**
     * MeasureReport
     * 
     * <p>The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the 
     * resources involved in that calculation.
     */
    public static final FHIRAllTypes MEASURE_REPORT = FHIRAllTypes.builder().value(Value.MEASURE_REPORT).build();

    /**
     * Media
     * 
     * <p>A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by 
     * direct reference.
     */
    public static final FHIRAllTypes MEDIA = FHIRAllTypes.builder().value(Value.MEDIA).build();

    /**
     * Medication
     * 
     * <p>This resource is primarily used for the identification and definition of a medication for the purposes of 
     * prescribing, dispensing, and administering a medication as well as for making statements about medication use.
     */
    public static final FHIRAllTypes MEDICATION = FHIRAllTypes.builder().value(Value.MEDICATION).build();

    /**
     * MedicationAdministration
     * 
     * <p>Describes the event of a patient consuming or otherwise being administered a medication. This may be as simple as 
     * swallowing a tablet or it may be a long running infusion. Related resources tie this event to the authorizing 
     * prescription, and the specific encounter between patient and health care practitioner.
     */
    public static final FHIRAllTypes MEDICATION_ADMINISTRATION = FHIRAllTypes.builder().value(Value.MEDICATION_ADMINISTRATION).build();

    /**
     * MedicationDispense
     * 
     * <p>Indicates that a medication product is to be or has been dispensed for a named person/patient. This includes a 
     * description of the medication product (supply) provided and the instructions for administering the medication. The 
     * medication dispense is the result of a pharmacy system responding to a medication order.
     */
    public static final FHIRAllTypes MEDICATION_DISPENSE = FHIRAllTypes.builder().value(Value.MEDICATION_DISPENSE).build();

    /**
     * MedicationKnowledge
     * 
     * <p>Information about a medication that is used to support knowledge.
     */
    public static final FHIRAllTypes MEDICATION_KNOWLEDGE = FHIRAllTypes.builder().value(Value.MEDICATION_KNOWLEDGE).build();

    /**
     * MedicationRequest
     * 
     * <p>An order or request for both supply of the medication and the instructions for administration of the medication to 
     * a patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to 
     * generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with 
     * workflow patterns.
     */
    public static final FHIRAllTypes MEDICATION_REQUEST = FHIRAllTypes.builder().value(Value.MEDICATION_REQUEST).build();

    /**
     * MedicationStatement
     * 
     * <p>A record of a medication that is being consumed by a patient. A MedicationStatement may indicate that the patient 
     * may be taking the medication now or has taken the medication in the past or will be taking the medication in the 
     * future. The source of this information can be the patient, significant other (such as a family member or spouse), or a 
     * clinician. A common scenario where this information is captured is during the history taking process during a patient 
     * visit or stay. The medication information may come from sources such as the patient's memory, from a prescription 
     * bottle, or from a list of medications the patient, clinician or other party maintains. 
     * 
     * <p>The primary difference between a medication statement and a medication administration is that the medication 
     * administration has complete administration information and is based on actual administration information from the 
     * person who administered the medication. A medication statement is often, if not always, less specific. There is no 
     * required date/time when the medication was administered, in fact we only know that a source has reported the patient 
     * is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete 
     * or missing or less precise. As stated earlier, the medication statement information may come from the patient's 
     * memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains. 
     * Medication administration is more formal and is not missing detailed information.
     */
    public static final FHIRAllTypes MEDICATION_STATEMENT = FHIRAllTypes.builder().value(Value.MEDICATION_STATEMENT).build();

    /**
     * MedicinalProductDefinition
     * 
     * <p>Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use, 
     * drug catalogs).
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_DEFINITION = FHIRAllTypes.builder().value(Value.MEDICINAL_PRODUCT_DEFINITION).build();

    /**
     * MessageDefinition
     * 
     * <p>Defines the characteristics of a message that can be shared between systems, including the type of event that 
     * initiates the message, the content to be transmitted and what response(s), if any, are permitted.
     */
    public static final FHIRAllTypes MESSAGE_DEFINITION = FHIRAllTypes.builder().value(Value.MESSAGE_DEFINITION).build();

    /**
     * MessageHeader
     * 
     * <p>The header for a message exchange that is either requesting or responding to an action. The reference(s) that are 
     * the subject of the action as well as other information related to the action are typically transmitted in a bundle in 
     * which the MessageHeader resource instance is the first resource in the bundle.
     */
    public static final FHIRAllTypes MESSAGE_HEADER = FHIRAllTypes.builder().value(Value.MESSAGE_HEADER).build();

    /**
     * MolecularSequence
     * 
     * <p>Raw data describing a biological sequence.
     */
    public static final FHIRAllTypes MOLECULAR_SEQUENCE = FHIRAllTypes.builder().value(Value.MOLECULAR_SEQUENCE).build();

    /**
     * NamingSystem
     * 
     * <p>A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, 
     * devices, etc. Represents a "System" used within the Identifier and Coding data types.
     */
    public static final FHIRAllTypes NAMING_SYSTEM = FHIRAllTypes.builder().value(Value.NAMING_SYSTEM).build();

    /**
     * NutritionOrder
     * 
     * <p>A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
     */
    public static final FHIRAllTypes NUTRITION_ORDER = FHIRAllTypes.builder().value(Value.NUTRITION_ORDER).build();

    /**
     * NutritionProduct
     * 
     * <p>A food or fluid product that is consumed by patients.
     */
    public static final FHIRAllTypes NUTRITION_PRODUCT = FHIRAllTypes.builder().value(Value.NUTRITION_PRODUCT).build();

    /**
     * Observation
     * 
     * <p>Measurements and simple assertions made about a patient, device or other subject.
     */
    public static final FHIRAllTypes OBSERVATION = FHIRAllTypes.builder().value(Value.OBSERVATION).build();

    /**
     * ObservationDefinition
     * 
     * <p>Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable 
     * health care service.
     */
    public static final FHIRAllTypes OBSERVATION_DEFINITION = FHIRAllTypes.builder().value(Value.OBSERVATION_DEFINITION).build();

    /**
     * OperationDefinition
     * 
     * <p>A formal computable definition of an operation (on the RESTful interface) or a named query (using the search 
     * interaction).
     */
    public static final FHIRAllTypes OPERATION_DEFINITION = FHIRAllTypes.builder().value(Value.OPERATION_DEFINITION).build();

    /**
     * OperationOutcome
     * 
     * <p>A collection of error, warning, or information messages that result from a system action.
     */
    public static final FHIRAllTypes OPERATION_OUTCOME = FHIRAllTypes.builder().value(Value.OPERATION_OUTCOME).build();

    /**
     * Organization
     * 
     * <p>A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some 
     * form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare 
     * practice groups, payer/insurer, etc.
     */
    public static final FHIRAllTypes ORGANIZATION = FHIRAllTypes.builder().value(Value.ORGANIZATION).build();

    /**
     * OrganizationAffiliation
     * 
     * <p>Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of 
     * relationship/sub-division relationship.
     */
    public static final FHIRAllTypes ORGANIZATION_AFFILIATION = FHIRAllTypes.builder().value(Value.ORGANIZATION_AFFILIATION).build();

    /**
     * PackagedProductDefinition
     * 
     * <p>A medically related item or items, in a container or package.
     */
    public static final FHIRAllTypes PACKAGED_PRODUCT_DEFINITION = FHIRAllTypes.builder().value(Value.PACKAGED_PRODUCT_DEFINITION).build();

    /**
     * Parameters
     * 
     * <p>This resource is a non-persisted resource used to pass information into and back from an [operation](operations.
     * html). It has no other use, and there is no RESTful endpoint associated with it.
     */
    public static final FHIRAllTypes PARAMETERS = FHIRAllTypes.builder().value(Value.PARAMETERS).build();

    /**
     * Patient
     * 
     * <p>Demographics and other administrative information about an individual or animal receiving care or other health-
     * related services.
     */
    public static final FHIRAllTypes PATIENT = FHIRAllTypes.builder().value(Value.PATIENT).build();

    /**
     * PaymentNotice
     * 
     * <p>This resource provides the status of the payment for goods and services rendered, and the request and response 
     * resource references.
     */
    public static final FHIRAllTypes PAYMENT_NOTICE = FHIRAllTypes.builder().value(Value.PAYMENT_NOTICE).build();

    /**
     * PaymentReconciliation
     * 
     * <p>This resource provides the details including amount of a payment and allocates the payment items being paid.
     */
    public static final FHIRAllTypes PAYMENT_RECONCILIATION = FHIRAllTypes.builder().value(Value.PAYMENT_RECONCILIATION).build();

    /**
     * Person
     * 
     * <p>Demographics and administrative information about a person independent of a specific health-related context.
     */
    public static final FHIRAllTypes PERSON = FHIRAllTypes.builder().value(Value.PERSON).build();

    /**
     * PlanDefinition
     * 
     * <p>This resource allows for the definition of various types of plans as a sharable, consumable, and executable 
     * artifact. The resource is general enough to support the description of a broad range of clinical and non-clinical 
     * artifacts such as clinical decision support rules, order sets, protocols, and drug quality specifications.
     */
    public static final FHIRAllTypes PLAN_DEFINITION = FHIRAllTypes.builder().value(Value.PLAN_DEFINITION).build();

    /**
     * Practitioner
     * 
     * <p>A person who is directly or indirectly involved in the provisioning of healthcare.
     */
    public static final FHIRAllTypes PRACTITIONER = FHIRAllTypes.builder().value(Value.PRACTITIONER).build();

    /**
     * PractitionerRole
     * 
     * <p>A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a 
     * period of time.
     */
    public static final FHIRAllTypes PRACTITIONER_ROLE = FHIRAllTypes.builder().value(Value.PRACTITIONER_ROLE).build();

    /**
     * Procedure
     * 
     * <p>An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or 
     * less invasive like long term services, counseling, or hypnotherapy.
     */
    public static final FHIRAllTypes PROCEDURE = FHIRAllTypes.builder().value(Value.PROCEDURE).build();

    /**
     * Provenance
     * 
     * <p>Provenance of a resource is a record that describes entities and processes involved in producing and delivering or 
     * otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling 
     * trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become 
     * important records with their own provenance. Provenance statement indicates clinical significance in terms of 
     * confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document 
     * Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust 
     * policies.
     */
    public static final FHIRAllTypes PROVENANCE = FHIRAllTypes.builder().value(Value.PROVENANCE).build();

    /**
     * Questionnaire
     * 
     * <p>A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide 
     * detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
     */
    public static final FHIRAllTypes QUESTIONNAIRE = FHIRAllTypes.builder().value(Value.QUESTIONNAIRE).build();

    /**
     * QuestionnaireResponse
     * 
     * <p>A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, 
     * corresponding to the structure of the grouping of the questionnaire being responded to.
     */
    public static final FHIRAllTypes QUESTIONNAIRE_RESPONSE = FHIRAllTypes.builder().value(Value.QUESTIONNAIRE_RESPONSE).build();

    /**
     * RegulatedAuthorization
     * 
     * <p>Regulatory approval, clearance or licencing related to a regulated product, treatment, facility or activity that is 
     * cited in a guidance, regulation, rule or legislative act. An example is Market Authorization relating to a Medicinal 
     * Product.
     */
    public static final FHIRAllTypes REGULATED_AUTHORIZATION = FHIRAllTypes.builder().value(Value.REGULATED_AUTHORIZATION).build();

    /**
     * RelatedPerson
     * 
     * <p>Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor 
     * has a formal responsibility in the care process.
     */
    public static final FHIRAllTypes RELATED_PERSON = FHIRAllTypes.builder().value(Value.RELATED_PERSON).build();

    /**
     * RequestGroup
     * 
     * <p>A group of related requests that can be used to capture intended activities that have inter-dependencies such as 
     * "give this medication after that one".
     */
    public static final FHIRAllTypes REQUEST_GROUP = FHIRAllTypes.builder().value(Value.REQUEST_GROUP).build();

    /**
     * ResearchDefinition
     * 
     * <p>The ResearchDefinition resource describes the conditional state (population and any exposures being compared within 
     * the population) and outcome (if specified) that the knowledge (evidence, assertion, recommendation) is about.
     */
    public static final FHIRAllTypes RESEARCH_DEFINITION = FHIRAllTypes.builder().value(Value.RESEARCH_DEFINITION).build();

    /**
     * ResearchElementDefinition
     * 
     * <p>The ResearchElementDefinition resource describes a "PICO" element that knowledge (evidence, assertion, 
     * recommendation) is about.
     */
    public static final FHIRAllTypes RESEARCH_ELEMENT_DEFINITION = FHIRAllTypes.builder().value(Value.RESEARCH_ELEMENT_DEFINITION).build();

    /**
     * ResearchStudy
     * 
     * <p>A process where a researcher or organization plans and then executes a series of steps intended to increase the 
     * field of healthcare-related knowledge. This includes studies of safety, efficacy, comparative effectiveness and other 
     * information about medications, devices, therapies and other interventional and investigative techniques. A 
     * ResearchStudy involves the gathering of information about human or animal subjects.
     */
    public static final FHIRAllTypes RESEARCH_STUDY = FHIRAllTypes.builder().value(Value.RESEARCH_STUDY).build();

    /**
     * ResearchSubject
     * 
     * <p>A physical entity which is the primary unit of operational and/or administrative interest in a study.
     */
    public static final FHIRAllTypes RESEARCH_SUBJECT = FHIRAllTypes.builder().value(Value.RESEARCH_SUBJECT).build();

    /**
     * Resource
     * 
     * <p>This is the base resource type for everything.
     */
    public static final FHIRAllTypes RESOURCE = FHIRAllTypes.builder().value(Value.RESOURCE).build();

    /**
     * RiskAssessment
     * 
     * <p>An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
     */
    public static final FHIRAllTypes RISK_ASSESSMENT = FHIRAllTypes.builder().value(Value.RISK_ASSESSMENT).build();

    /**
     * Schedule
     * 
     * <p>A container for slots of time that may be available for booking appointments.
     */
    public static final FHIRAllTypes SCHEDULE = FHIRAllTypes.builder().value(Value.SCHEDULE).build();

    /**
     * SearchParameter
     * 
     * <p>A search parameter that defines a named search item that can be used to search/filter on a resource.
     */
    public static final FHIRAllTypes SEARCH_PARAMETER = FHIRAllTypes.builder().value(Value.SEARCH_PARAMETER).build();

    /**
     * ServiceRequest
     * 
     * <p>A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
     */
    public static final FHIRAllTypes SERVICE_REQUEST = FHIRAllTypes.builder().value(Value.SERVICE_REQUEST).build();

    /**
     * Slot
     * 
     * <p>A slot of time on a schedule that may be available for booking appointments.
     */
    public static final FHIRAllTypes SLOT = FHIRAllTypes.builder().value(Value.SLOT).build();

    /**
     * Specimen
     * 
     * <p>A sample to be used for analysis.
     */
    public static final FHIRAllTypes SPECIMEN = FHIRAllTypes.builder().value(Value.SPECIMEN).build();

    /**
     * SpecimenDefinition
     * 
     * <p>A kind of specimen with associated set of requirements.
     */
    public static final FHIRAllTypes SPECIMEN_DEFINITION = FHIRAllTypes.builder().value(Value.SPECIMEN_DEFINITION).build();

    /**
     * StructureDefinition
     * 
     * <p>A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in 
     * FHIR, and also for describing extensions and constraints on resources and data types.
     */
    public static final FHIRAllTypes STRUCTURE_DEFINITION = FHIRAllTypes.builder().value(Value.STRUCTURE_DEFINITION).build();

    /**
     * StructureMap
     * 
     * <p>A Map of relationships between 2 structures that can be used to transform data.
     */
    public static final FHIRAllTypes STRUCTURE_MAP = FHIRAllTypes.builder().value(Value.STRUCTURE_MAP).build();

    /**
     * Subscription
     * 
     * <p>The subscription resource is used to define a push-based subscription from a server to another system. Once a 
     * subscription is registered with the server, the server checks every resource that is created or updated, and if the 
     * resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an 
     * appropriate action.
     */
    public static final FHIRAllTypes SUBSCRIPTION = FHIRAllTypes.builder().value(Value.SUBSCRIPTION).build();

    /**
     * SubscriptionStatus
     * 
     * <p>The SubscriptionStatus resource describes the state of a Subscription during notifications.
     */
    public static final FHIRAllTypes SUBSCRIPTION_STATUS = FHIRAllTypes.builder().value(Value.SUBSCRIPTION_STATUS).build();

    /**
     * SubscriptionTopic
     * 
     * <p>Describes a stream of resource state changes or events and annotated with labels useful to filter projections from 
     * this topic.
     */
    public static final FHIRAllTypes SUBSCRIPTION_TOPIC = FHIRAllTypes.builder().value(Value.SUBSCRIPTION_TOPIC).build();

    /**
     * Substance
     * 
     * <p>A homogeneous material with a definite composition.
     */
    public static final FHIRAllTypes SUBSTANCE = FHIRAllTypes.builder().value(Value.SUBSTANCE).build();

    /**
     * SubstanceDefinition
     * 
     * <p>The detailed description of a substance, typically at a level beyond what is used for prescribing.
     */
    public static final FHIRAllTypes SUBSTANCE_DEFINITION = FHIRAllTypes.builder().value(Value.SUBSTANCE_DEFINITION).build();

    /**
     * SupplyDelivery
     * 
     * <p>Record of delivery of what is supplied.
     */
    public static final FHIRAllTypes SUPPLY_DELIVERY = FHIRAllTypes.builder().value(Value.SUPPLY_DELIVERY).build();

    /**
     * SupplyRequest
     * 
     * <p>A record of a request for a medication, substance or device used in the healthcare setting.
     */
    public static final FHIRAllTypes SUPPLY_REQUEST = FHIRAllTypes.builder().value(Value.SUPPLY_REQUEST).build();

    /**
     * Task
     * 
     * <p>A task to be performed.
     */
    public static final FHIRAllTypes TASK = FHIRAllTypes.builder().value(Value.TASK).build();

    /**
     * TerminologyCapabilities
     * 
     * <p>A TerminologyCapabilities resource documents a set of capabilities (behaviors) of a FHIR Terminology Server that 
     * may be used as a statement of actual server functionality or a statement of required or desired server implementation.
     */
    public static final FHIRAllTypes TERMINOLOGY_CAPABILITIES = FHIRAllTypes.builder().value(Value.TERMINOLOGY_CAPABILITIES).build();

    /**
     * TestReport
     * 
     * <p>A summary of information based on the results of executing a TestScript.
     */
    public static final FHIRAllTypes TEST_REPORT = FHIRAllTypes.builder().value(Value.TEST_REPORT).build();

    /**
     * TestScript
     * 
     * <p>A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR 
     * specification.
     */
    public static final FHIRAllTypes TEST_SCRIPT = FHIRAllTypes.builder().value(Value.TEST_SCRIPT).build();

    /**
     * ValueSet
     * 
     * <p>A ValueSet resource instance specifies a set of codes drawn from one or more code systems, intended for use in a 
     * particular context. Value sets link between [CodeSystem](codesystem.html) definitions and their use in [coded elements]
     * (terminologies.html).
     */
    public static final FHIRAllTypes VALUE_SET = FHIRAllTypes.builder().value(Value.VALUE_SET).build();

    /**
     * VerificationResult
     * 
     * <p>Describes validation requirements, source(s), status and dates for one or more elements.
     */
    public static final FHIRAllTypes VERIFICATION_RESULT = FHIRAllTypes.builder().value(Value.VERIFICATION_RESULT).build();

    /**
     * VisionPrescription
     * 
     * <p>An authorization for the provision of glasses and/or contact lenses to a patient.
     */
    public static final FHIRAllTypes VISION_PRESCRIPTION = FHIRAllTypes.builder().value(Value.VISION_PRESCRIPTION).build();

    /**
     * Type
     * 
     * <p>A place holder that means any kind of data type
     */
    public static final FHIRAllTypes TYPE = FHIRAllTypes.builder().value(Value.TYPE).build();

    /**
     * Any
     * 
     * <p>A place holder that means any kind of resource
     */
    public static final FHIRAllTypes ANY = FHIRAllTypes.builder().value(Value.ANY).build();

    private volatile int hashCode;

    private FHIRAllTypes(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this FHIRAllTypes as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating FHIRAllTypes objects from a passed enum value.
     */
    public static FHIRAllTypes of(Value value) {
        switch (value) {
        case ADDRESS:
            return ADDRESS;
        case AGE:
            return AGE;
        case ANNOTATION:
            return ANNOTATION;
        case ATTACHMENT:
            return ATTACHMENT;
        case BACKBONE_ELEMENT:
            return BACKBONE_ELEMENT;
        case CODEABLE_CONCEPT:
            return CODEABLE_CONCEPT;
        case CODEABLE_REFERENCE:
            return CODEABLE_REFERENCE;
        case CODING:
            return CODING;
        case CONTACT_DETAIL:
            return CONTACT_DETAIL;
        case CONTACT_POINT:
            return CONTACT_POINT;
        case CONTRIBUTOR:
            return CONTRIBUTOR;
        case COUNT:
            return COUNT;
        case DATA_REQUIREMENT:
            return DATA_REQUIREMENT;
        case DATA_TYPE:
            return DATA_TYPE;
        case DISTANCE:
            return DISTANCE;
        case DOSAGE:
            return DOSAGE;
        case DURATION:
            return DURATION;
        case ELEMENT:
            return ELEMENT;
        case ELEMENT_DEFINITION:
            return ELEMENT_DEFINITION;
        case EXPRESSION:
            return EXPRESSION;
        case EXTENSION:
            return EXTENSION;
        case HUMAN_NAME:
            return HUMAN_NAME;
        case IDENTIFIER:
            return IDENTIFIER;
        case MARKETING_STATUS:
            return MARKETING_STATUS;
        case META:
            return META;
        case MONEY:
            return MONEY;
        case MONEY_QUANTITY:
            return MONEY_QUANTITY;
        case NARRATIVE:
            return NARRATIVE;
        case PARAMETER_DEFINITION:
            return PARAMETER_DEFINITION;
        case PERIOD:
            return PERIOD;
        case POPULATION:
            return POPULATION;
        case PROD_CHARACTERISTIC:
            return PROD_CHARACTERISTIC;
        case PRODUCT_SHELF_LIFE:
            return PRODUCT_SHELF_LIFE;
        case QUANTITY:
            return QUANTITY;
        case RANGE:
            return RANGE;
        case RATIO:
            return RATIO;
        case RATIO_RANGE:
            return RATIO_RANGE;
        case REFERENCE:
            return REFERENCE;
        case RELATED_ARTIFACT:
            return RELATED_ARTIFACT;
        case SAMPLED_DATA:
            return SAMPLED_DATA;
        case SIGNATURE:
            return SIGNATURE;
        case SIMPLE_QUANTITY:
            return SIMPLE_QUANTITY;
        case TIMING:
            return TIMING;
        case TRIGGER_DEFINITION:
            return TRIGGER_DEFINITION;
        case USAGE_CONTEXT:
            return USAGE_CONTEXT;
        case BASE64BINARY:
            return BASE64BINARY;
        case BOOLEAN:
            return BOOLEAN;
        case CANONICAL:
            return CANONICAL;
        case CODE:
            return CODE;
        case DATE:
            return DATE;
        case DATE_TIME:
            return DATE_TIME;
        case DECIMAL:
            return DECIMAL;
        case ID:
            return ID;
        case INSTANT:
            return INSTANT;
        case INTEGER:
            return INTEGER;
        case MARKDOWN:
            return MARKDOWN;
        case OID:
            return OID;
        case POSITIVE_INT:
            return POSITIVE_INT;
        case STRING:
            return STRING;
        case TIME:
            return TIME;
        case UNSIGNED_INT:
            return UNSIGNED_INT;
        case URI:
            return URI;
        case URL:
            return URL;
        case UUID:
            return UUID;
        case XHTML:
            return XHTML;
        case ACCOUNT:
            return ACCOUNT;
        case ACTIVITY_DEFINITION:
            return ACTIVITY_DEFINITION;
        case ADMINISTRABLE_PRODUCT_DEFINITION:
            return ADMINISTRABLE_PRODUCT_DEFINITION;
        case ADVERSE_EVENT:
            return ADVERSE_EVENT;
        case ALLERGY_INTOLERANCE:
            return ALLERGY_INTOLERANCE;
        case APPOINTMENT:
            return APPOINTMENT;
        case APPOINTMENT_RESPONSE:
            return APPOINTMENT_RESPONSE;
        case AUDIT_EVENT:
            return AUDIT_EVENT;
        case BASIC:
            return BASIC;
        case BINARY:
            return BINARY;
        case BIOLOGICALLY_DERIVED_PRODUCT:
            return BIOLOGICALLY_DERIVED_PRODUCT;
        case BODY_STRUCTURE:
            return BODY_STRUCTURE;
        case BUNDLE:
            return BUNDLE;
        case CAPABILITY_STATEMENT:
            return CAPABILITY_STATEMENT;
        case CARE_PLAN:
            return CARE_PLAN;
        case CARE_TEAM:
            return CARE_TEAM;
        case CATALOG_ENTRY:
            return CATALOG_ENTRY;
        case CHARGE_ITEM:
            return CHARGE_ITEM;
        case CHARGE_ITEM_DEFINITION:
            return CHARGE_ITEM_DEFINITION;
        case CITATION:
            return CITATION;
        case CLAIM:
            return CLAIM;
        case CLAIM_RESPONSE:
            return CLAIM_RESPONSE;
        case CLINICAL_IMPRESSION:
            return CLINICAL_IMPRESSION;
        case CLINICAL_USE_DEFINITION:
            return CLINICAL_USE_DEFINITION;
        case CODE_SYSTEM:
            return CODE_SYSTEM;
        case COMMUNICATION:
            return COMMUNICATION;
        case COMMUNICATION_REQUEST:
            return COMMUNICATION_REQUEST;
        case COMPARTMENT_DEFINITION:
            return COMPARTMENT_DEFINITION;
        case COMPOSITION:
            return COMPOSITION;
        case CONCEPT_MAP:
            return CONCEPT_MAP;
        case CONDITION:
            return CONDITION;
        case CONSENT:
            return CONSENT;
        case CONTRACT:
            return CONTRACT;
        case COVERAGE:
            return COVERAGE;
        case COVERAGE_ELIGIBILITY_REQUEST:
            return COVERAGE_ELIGIBILITY_REQUEST;
        case COVERAGE_ELIGIBILITY_RESPONSE:
            return COVERAGE_ELIGIBILITY_RESPONSE;
        case DETECTED_ISSUE:
            return DETECTED_ISSUE;
        case DEVICE:
            return DEVICE;
        case DEVICE_DEFINITION:
            return DEVICE_DEFINITION;
        case DEVICE_METRIC:
            return DEVICE_METRIC;
        case DEVICE_REQUEST:
            return DEVICE_REQUEST;
        case DEVICE_USE_STATEMENT:
            return DEVICE_USE_STATEMENT;
        case DIAGNOSTIC_REPORT:
            return DIAGNOSTIC_REPORT;
        case DOCUMENT_MANIFEST:
            return DOCUMENT_MANIFEST;
        case DOCUMENT_REFERENCE:
            return DOCUMENT_REFERENCE;
        case DOMAIN_RESOURCE:
            return DOMAIN_RESOURCE;
        case ENCOUNTER:
            return ENCOUNTER;
        case ENDPOINT:
            return ENDPOINT;
        case ENROLLMENT_REQUEST:
            return ENROLLMENT_REQUEST;
        case ENROLLMENT_RESPONSE:
            return ENROLLMENT_RESPONSE;
        case EPISODE_OF_CARE:
            return EPISODE_OF_CARE;
        case EVENT_DEFINITION:
            return EVENT_DEFINITION;
        case EVIDENCE:
            return EVIDENCE;
        case EVIDENCE_REPORT:
            return EVIDENCE_REPORT;
        case EVIDENCE_VARIABLE:
            return EVIDENCE_VARIABLE;
        case EXAMPLE_SCENARIO:
            return EXAMPLE_SCENARIO;
        case EXPLANATION_OF_BENEFIT:
            return EXPLANATION_OF_BENEFIT;
        case FAMILY_MEMBER_HISTORY:
            return FAMILY_MEMBER_HISTORY;
        case FLAG:
            return FLAG;
        case GOAL:
            return GOAL;
        case GRAPH_DEFINITION:
            return GRAPH_DEFINITION;
        case GROUP:
            return GROUP;
        case GUIDANCE_RESPONSE:
            return GUIDANCE_RESPONSE;
        case HEALTHCARE_SERVICE:
            return HEALTHCARE_SERVICE;
        case IMAGING_STUDY:
            return IMAGING_STUDY;
        case IMMUNIZATION:
            return IMMUNIZATION;
        case IMMUNIZATION_EVALUATION:
            return IMMUNIZATION_EVALUATION;
        case IMMUNIZATION_RECOMMENDATION:
            return IMMUNIZATION_RECOMMENDATION;
        case IMPLEMENTATION_GUIDE:
            return IMPLEMENTATION_GUIDE;
        case INGREDIENT:
            return INGREDIENT;
        case INSURANCE_PLAN:
            return INSURANCE_PLAN;
        case INVOICE:
            return INVOICE;
        case LIBRARY:
            return LIBRARY;
        case LINKAGE:
            return LINKAGE;
        case LIST:
            return LIST;
        case LOCATION:
            return LOCATION;
        case MANUFACTURED_ITEM_DEFINITION:
            return MANUFACTURED_ITEM_DEFINITION;
        case MEASURE:
            return MEASURE;
        case MEASURE_REPORT:
            return MEASURE_REPORT;
        case MEDIA:
            return MEDIA;
        case MEDICATION:
            return MEDICATION;
        case MEDICATION_ADMINISTRATION:
            return MEDICATION_ADMINISTRATION;
        case MEDICATION_DISPENSE:
            return MEDICATION_DISPENSE;
        case MEDICATION_KNOWLEDGE:
            return MEDICATION_KNOWLEDGE;
        case MEDICATION_REQUEST:
            return MEDICATION_REQUEST;
        case MEDICATION_STATEMENT:
            return MEDICATION_STATEMENT;
        case MEDICINAL_PRODUCT_DEFINITION:
            return MEDICINAL_PRODUCT_DEFINITION;
        case MESSAGE_DEFINITION:
            return MESSAGE_DEFINITION;
        case MESSAGE_HEADER:
            return MESSAGE_HEADER;
        case MOLECULAR_SEQUENCE:
            return MOLECULAR_SEQUENCE;
        case NAMING_SYSTEM:
            return NAMING_SYSTEM;
        case NUTRITION_ORDER:
            return NUTRITION_ORDER;
        case NUTRITION_PRODUCT:
            return NUTRITION_PRODUCT;
        case OBSERVATION:
            return OBSERVATION;
        case OBSERVATION_DEFINITION:
            return OBSERVATION_DEFINITION;
        case OPERATION_DEFINITION:
            return OPERATION_DEFINITION;
        case OPERATION_OUTCOME:
            return OPERATION_OUTCOME;
        case ORGANIZATION:
            return ORGANIZATION;
        case ORGANIZATION_AFFILIATION:
            return ORGANIZATION_AFFILIATION;
        case PACKAGED_PRODUCT_DEFINITION:
            return PACKAGED_PRODUCT_DEFINITION;
        case PARAMETERS:
            return PARAMETERS;
        case PATIENT:
            return PATIENT;
        case PAYMENT_NOTICE:
            return PAYMENT_NOTICE;
        case PAYMENT_RECONCILIATION:
            return PAYMENT_RECONCILIATION;
        case PERSON:
            return PERSON;
        case PLAN_DEFINITION:
            return PLAN_DEFINITION;
        case PRACTITIONER:
            return PRACTITIONER;
        case PRACTITIONER_ROLE:
            return PRACTITIONER_ROLE;
        case PROCEDURE:
            return PROCEDURE;
        case PROVENANCE:
            return PROVENANCE;
        case QUESTIONNAIRE:
            return QUESTIONNAIRE;
        case QUESTIONNAIRE_RESPONSE:
            return QUESTIONNAIRE_RESPONSE;
        case REGULATED_AUTHORIZATION:
            return REGULATED_AUTHORIZATION;
        case RELATED_PERSON:
            return RELATED_PERSON;
        case REQUEST_GROUP:
            return REQUEST_GROUP;
        case RESEARCH_DEFINITION:
            return RESEARCH_DEFINITION;
        case RESEARCH_ELEMENT_DEFINITION:
            return RESEARCH_ELEMENT_DEFINITION;
        case RESEARCH_STUDY:
            return RESEARCH_STUDY;
        case RESEARCH_SUBJECT:
            return RESEARCH_SUBJECT;
        case RESOURCE:
            return RESOURCE;
        case RISK_ASSESSMENT:
            return RISK_ASSESSMENT;
        case SCHEDULE:
            return SCHEDULE;
        case SEARCH_PARAMETER:
            return SEARCH_PARAMETER;
        case SERVICE_REQUEST:
            return SERVICE_REQUEST;
        case SLOT:
            return SLOT;
        case SPECIMEN:
            return SPECIMEN;
        case SPECIMEN_DEFINITION:
            return SPECIMEN_DEFINITION;
        case STRUCTURE_DEFINITION:
            return STRUCTURE_DEFINITION;
        case STRUCTURE_MAP:
            return STRUCTURE_MAP;
        case SUBSCRIPTION:
            return SUBSCRIPTION;
        case SUBSCRIPTION_STATUS:
            return SUBSCRIPTION_STATUS;
        case SUBSCRIPTION_TOPIC:
            return SUBSCRIPTION_TOPIC;
        case SUBSTANCE:
            return SUBSTANCE;
        case SUBSTANCE_DEFINITION:
            return SUBSTANCE_DEFINITION;
        case SUPPLY_DELIVERY:
            return SUPPLY_DELIVERY;
        case SUPPLY_REQUEST:
            return SUPPLY_REQUEST;
        case TASK:
            return TASK;
        case TERMINOLOGY_CAPABILITIES:
            return TERMINOLOGY_CAPABILITIES;
        case TEST_REPORT:
            return TEST_REPORT;
        case TEST_SCRIPT:
            return TEST_SCRIPT;
        case VALUE_SET:
            return VALUE_SET;
        case VERIFICATION_RESULT:
            return VERIFICATION_RESULT;
        case VISION_PRESCRIPTION:
            return VISION_PRESCRIPTION;
        case TYPE:
            return TYPE;
        case ANY:
            return ANY;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating FHIRAllTypes objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static FHIRAllTypes of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating FHIRAllTypes objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating FHIRAllTypes objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(Value.from(value));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FHIRAllTypes other = (FHIRAllTypes) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for FHIRAllTypes
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public FHIRAllTypes build() {
            FHIRAllTypes fHIRAllTypes = new FHIRAllTypes(this);
            if (validating) {
                validate(fHIRAllTypes);
            }
            return fHIRAllTypes;
        }

        protected void validate(FHIRAllTypes fHIRAllTypes) {
            super.validate(fHIRAllTypes);
        }

        protected Builder from(FHIRAllTypes fHIRAllTypes) {
            super.from(fHIRAllTypes);
            return this;
        }
    }

    public enum Value {
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        EFFECT_EVIDENCE_SYNTHESIS("EffectEvidenceSynthesis"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT("MedicinalProduct"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT_AUTHORIZATION("MedicinalProductAuthorization"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT_CONTRAINDICATION("MedicinalProductContraindication"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT_INDICATION("MedicinalProductIndication"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT_INGREDIENT("MedicinalProductIngredient"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT_INTERACTION("MedicinalProductInteraction"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT_MANUFACTURED("MedicinalProductManufactured"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT_PACKAGED("MedicinalProductPackaged"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT_PHARMACEUTICAL("MedicinalProductPharmaceutical"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT("MedicinalProductUndesirableEffect"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        RISK_EVIDENCE_SYNTHESIS("RiskEvidenceSynthesis"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        SUBSTANCE_NUCLEIC_ACID("SubstanceNucleicAcid"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        SUBSTANCE_POLYMER("SubstancePolymer"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        SUBSTANCE_PROTEIN("SubstanceProtein"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        SUBSTANCE_REFERENCE_INFORMATION("SubstanceReferenceInformation"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        SUBSTANCE_SOURCE_MATERIAL("SubstanceSourceMaterial"),
        /**
         * This resource type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        SUBSTANCE_SPECIFICATION("SubstanceSpecification"),

        /**
         * This data type is retired and should never be used.
         * It is retained here only for backwards compatibility.
         */
        @Deprecated
        SUBSTANCE_AMOUNT("SubstanceAmount"),

        /**
         * Address
         * 
         * <p>An address expressed using postal conventions (as opposed to GPS or other location definition formats). This data 
         * type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be 
         * valid for mail delivery. There are a variety of postal address formats defined around the world.
         */
        ADDRESS("Address"),

        /**
         * Age
         * 
         * <p>A duration of time during which an organism (or a process) has existed.
         */
        AGE("Age"),

        /**
         * Annotation
         * 
         * <p>A text note which also contains information about who made the statement and when.
         */
        ANNOTATION("Annotation"),

        /**
         * Attachment
         * 
         * <p>For referring to data content defined in other formats.
         */
        ATTACHMENT("Attachment"),

        /**
         * BackboneElement
         * 
         * <p>Base definition for all elements that are defined inside a resource - but not those in a data type.
         */
        BACKBONE_ELEMENT("BackboneElement"),

        /**
         * CodeableConcept
         * 
         * <p>A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
         */
        CODEABLE_CONCEPT("CodeableConcept"),

        /**
         * CodeableReference
         * 
         * <p>A reference to a resource (by instance), or instead, a reference to a cencept defined in a terminology or ontology 
         * (by class).
         */
        CODEABLE_REFERENCE("CodeableReference"),

        /**
         * Coding
         * 
         * <p>A reference to a code defined by a terminology system.
         */
        CODING("Coding"),

        /**
         * ContactDetail
         * 
         * <p>Specifies contact information for a person or organization.
         */
        CONTACT_DETAIL("ContactDetail"),

        /**
         * ContactPoint
         * 
         * <p>Details for all kinds of technology mediated contact points for a person or organization, including telephone, 
         * email, etc.
         */
        CONTACT_POINT("ContactPoint"),

        /**
         * Contributor
         * 
         * <p>A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.
         */
        CONTRIBUTOR("Contributor"),

        /**
         * Count
         * 
         * <p>A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that 
         * are not precisely quantified, including amounts involving arbitrary units and floating currencies.
         */
        COUNT("Count"),

        /**
         * DataRequirement
         * 
         * <p>Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters 
         * of the data.
         */
        DATA_REQUIREMENT("DataRequirement"),

        /**
         * DataType
         * 
         * <p>The base class for all re-useable types defined as part of the FHIR Specification.
         */
        DATA_TYPE("DataType"),

        /**
         * Distance
         * 
         * <p>A length - a value with a unit that is a physical distance.
         */
        DISTANCE("Distance"),

        /**
         * Dosage
         * 
         * <p>Indicates how the medication is/was taken or should be taken by the patient.
         */
        DOSAGE("Dosage"),

        /**
         * Duration
         * 
         * <p>A length of time.
         */
        DURATION("Duration"),

        /**
         * Element
         * 
         * <p>Base definition for all elements in a resource.
         */
        ELEMENT("Element"),

        /**
         * ElementDefinition
         * 
         * <p>Captures constraints on each element within the resource, profile, or extension.
         */
        ELEMENT_DEFINITION("ElementDefinition"),

        /**
         * Expression
         * 
         * <p>A expression that is evaluated in a specified context and returns a value. The context of use of the expression 
         * must specify the context in which the expression is evaluated, and how the result of the expression is used.
         */
        EXPRESSION("Expression"),

        /**
         * Extension
         * 
         * <p>Optional Extension Element - found in all resources.
         */
        EXTENSION("Extension"),

        /**
         * HumanName
         * 
         * <p>A human's name with the ability to identify parts and usage.
         */
        HUMAN_NAME("HumanName"),

        /**
         * Identifier
         * 
         * <p>An identifier - identifies some entity uniquely and unambiguously. Typically this is used for business identifiers.
         */
        IDENTIFIER("Identifier"),

        /**
         * MarketingStatus
         * 
         * <p>The marketing status describes the date when a medicinal product is actually put on the market or the date as of 
         * which it is no longer available.
         */
        MARKETING_STATUS("MarketingStatus"),

        /**
         * Meta
         * 
         * <p>The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to 
         * the content might not always be associated with version changes to the resource.
         */
        META("Meta"),

        /**
         * Money
         * 
         * <p>An amount of economic utility in some recognized currency.
         */
        MONEY("Money"),

        /**
         * MoneyQuantity
         */
        MONEY_QUANTITY("MoneyQuantity"),

        /**
         * Narrative
         * 
         * <p>A human-readable summary of the resource conveying the essential clinical and business information for the resource.
         */
        NARRATIVE("Narrative"),

        /**
         * ParameterDefinition
         * 
         * <p>The parameters to the module. This collection specifies both the input and output parameters. Input parameters are 
         * provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
         */
        PARAMETER_DEFINITION("ParameterDefinition"),

        /**
         * Period
         * 
         * <p>A time period defined by a start and end date and optionally time.
         */
        PERIOD("Period"),

        /**
         * Population
         * 
         * <p>A populatioof people with some set of grouping criteria.
         */
        POPULATION("Population"),

        /**
         * ProdCharacteristic
         * 
         * <p>The marketing status describes the date when a medicinal product is actually put on the market or the date as of 
         * which it is no longer available.
         */
        PROD_CHARACTERISTIC("ProdCharacteristic"),

        /**
         * ProductShelfLife
         * 
         * <p>The shelf-life and storage information for a medicinal product item or container can be described using this class.
         */
        PRODUCT_SHELF_LIFE("ProductShelfLife"),

        /**
         * Quantity
         * 
         * <p>A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that 
         * are not precisely quantified, including amounts involving arbitrary units and floating currencies.
         */
        QUANTITY("Quantity"),

        /**
         * Range
         * 
         * <p>A set of ordered Quantities defined by a low and high limit.
         */
        RANGE("Range"),

        /**
         * Ratio
         * 
         * <p>A relationship of two Quantity values - expressed as a numerator and a denominator.
         */
        RATIO("Ratio"),

        /**
         * RatioRange
         * 
         * <p>A range of ratios expressed as a low and high numerator and a denominator.
         */
        RATIO_RANGE("RatioRange"),

        /**
         * Reference
         * 
         * <p>A reference from one resource to another.
         */
        REFERENCE("Reference"),

        /**
         * RelatedArtifact
         * 
         * <p>Related artifacts such as additional documentation, justification, or bibliographic references.
         */
        RELATED_ARTIFACT("RelatedArtifact"),

        /**
         * SampledData
         * 
         * <p>A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in 
         * the data.
         */
        SAMPLED_DATA("SampledData"),

        /**
         * Signature
         * 
         * <p>A signature along with supporting context. The signature may be a digital signature that is cryptographic in 
         * nature, or some other signature acceptable to the domain. This other signature may be as simple as a graphical image 
         * representing a hand-written signature, or a signature ceremony Different signature approaches have different utilities.
         */
        SIGNATURE("Signature"),

        /**
         * SimpleQuantity
         */
        SIMPLE_QUANTITY("SimpleQuantity"),

        /**
         * Timing
         * 
         * <p>Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, 
         * expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used 
         * when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were 
         * carried out.
         */
        TIMING("Timing"),

        /**
         * TriggerDefinition
         * 
         * <p>A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined 
         * by the type element.
         */
        TRIGGER_DEFINITION("TriggerDefinition"),

        /**
         * UsageContext
         * 
         * <p>Specifies clinical/business/etc. metadata that can be used to retrieve, index and/or categorize an artifact. This 
         * metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care 
         * (e.g., venue, care setting, provider of care).
         */
        USAGE_CONTEXT("UsageContext"),

        /**
         * base64Binary
         * 
         * <p>A stream of bytes
         */
        BASE64BINARY("base64Binary"),

        /**
         * boolean
         * 
         * <p>Value of "true" or "false"
         */
        BOOLEAN("boolean"),

        /**
         * canonical
         * 
         * <p>A URI that is a reference to a canonical URL on a FHIR resource
         */
        CANONICAL("canonical"),

        /**
         * code
         * 
         * <p>A string which has at least one character and no leading or trailing whitespace and where there is no whitespace 
         * other than single spaces in the contents
         */
        CODE("code"),

        /**
         * date
         * 
         * <p>A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema 
         * types gYear, gYearMonth and date. Dates SHALL be valid dates.
         */
        DATE("date"),

        /**
         * dateTime
         * 
         * <p>A date, date-time or partial date (e.g. just year or year + month). If hours and minutes are specified, a time zone 
         * SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be 
         * provided due to schema type constraints but may be zero-filled and may be ignored. Dates SHALL be valid dates.
         */
        DATE_TIME("dateTime"),

        /**
         * decimal
         * 
         * <p>A rational number with implicit precision
         */
        DECIMAL("decimal"),

        /**
         * id
         * 
         * <p>Any combination of letters, numerals, "-" and ".", with a length limit of 64 characters. (This might be an integer, 
         * an unprefixed OID, UUID or any other identifier pattern that meets these constraints.) Ids are case-insensitive.
         */
        ID("id"),

        /**
         * instant
         * 
         * <p>An instant in time - known at least to the second
         */
        INSTANT("instant"),

        /**
         * integer
         * 
         * <p>A whole number
         */
        INTEGER("integer"),

        /**
         * markdown
         * 
         * <p>A string that may contain Github Flavored Markdown syntax for optional processing by a mark down presentation engine
         */
        MARKDOWN("markdown"),

        /**
         * oid
         * 
         * <p>An OID represented as a URI
         */
        OID("oid"),

        /**
         * positiveInt
         * 
         * <p>An integer with a value that is positive (e.g. &gt;0)
         */
        POSITIVE_INT("positiveInt"),

        /**
         * string
         * 
         * <p>A sequence of Unicode characters
         */
        STRING("string"),

        /**
         * time
         * 
         * <p>A time during the day, with no date specified
         */
        TIME("time"),

        /**
         * unsignedInt
         * 
         * <p>An integer with a value that is not negative (e.g. &gt;= 0)
         */
        UNSIGNED_INT("unsignedInt"),

        /**
         * uri
         * 
         * <p>String of characters used to identify a name or a resource
         */
        URI("uri"),

        /**
         * url
         * 
         * <p>A URI that is a literal reference
         */
        URL("url"),

        /**
         * uuid
         * 
         * <p>A UUID, represented as a URI
         */
        UUID("uuid"),

        /**
         * XHTML
         * 
         * <p>XHTML format, as defined by W3C, but restricted usage (mainly, no active content)
         */
        XHTML("xhtml"),

        /**
         * Account
         * 
         * <p>A financial tool for tracking value accrued for a particular purpose. In the healthcare field, used to track 
         * charges for a patient, cost centers, etc.
         */
        ACCOUNT("Account"),

        /**
         * ActivityDefinition
         * 
         * <p>This resource allows for the definition of some activity to be performed, independent of a particular patient, 
         * practitioner, or other performance context.
         */
        ACTIVITY_DEFINITION("ActivityDefinition"),

        /**
         * AdministrableProductDefinition
         * 
         * <p>A medicinal product in the final form which is suitable for administering to a patient (after any mixing of 
         * multiple components, dissolution etc. has been performed).
         */
        ADMINISTRABLE_PRODUCT_DEFINITION("AdministrableProductDefinition"),

        /**
         * AdverseEvent
         * 
         * <p>Actual or potential/avoided event causing unintended physical injury resulting from or contributed to by medical 
         * care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or 
         * hospitalization, or that results in death.
         */
        ADVERSE_EVENT("AdverseEvent"),

        /**
         * AllergyIntolerance
         * 
         * <p>Risk of harmful or undesirable, physiological response which is unique to an individual and associated with 
         * exposure to a substance.
         */
        ALLERGY_INTOLERANCE("AllergyIntolerance"),

        /**
         * Appointment
         * 
         * <p>A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a 
         * specific date/time. This may result in one or more Encounter(s).
         */
        APPOINTMENT("Appointment"),

        /**
         * AppointmentResponse
         * 
         * <p>A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
         */
        APPOINTMENT_RESPONSE("AppointmentResponse"),

        /**
         * AuditEvent
         * 
         * <p>A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion 
         * attempts and monitoring for inappropriate usage.
         */
        AUDIT_EVENT("AuditEvent"),

        /**
         * Basic
         * 
         * <p>Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing 
         * resource, and custom resources not appropriate for inclusion in the FHIR specification.
         */
        BASIC("Basic"),

        /**
         * Binary
         * 
         * <p>A resource that represents the data of a single raw artifact as digital content accessible in its native format. A 
         * Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
         */
        BINARY("Binary"),

        /**
         * BiologicallyDerivedProduct
         * 
         * <p>A material substance originating from a biological entity intended to be transplanted or infused
         * <p>into another (possibly the same) biological entity.
         */
        BIOLOGICALLY_DERIVED_PRODUCT("BiologicallyDerivedProduct"),

        /**
         * BodyStructure
         * 
         * <p>Record details about an anatomical structure. This resource may be used when a coded concept does not provide the 
         * necessary detail needed for the use case.
         */
        BODY_STRUCTURE("BodyStructure"),

        /**
         * Bundle
         * 
         * <p>A container for a collection of resources.
         */
        BUNDLE("Bundle"),

        /**
         * CapabilityStatement
         * 
         * <p>A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server for a particular version of 
         * FHIR that may be used as a statement of actual server functionality or a statement of required or desired server 
         * implementation.
         */
        CAPABILITY_STATEMENT("CapabilityStatement"),

        /**
         * CarePlan
         * 
         * <p>Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or 
         * community for a period of time, possibly limited to care for a specific condition or set of conditions.
         */
        CARE_PLAN("CarePlan"),

        /**
         * CareTeam
         * 
         * <p>The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of 
         * care for a patient.
         */
        CARE_TEAM("CareTeam"),

        /**
         * CatalogEntry
         * 
         * <p>Catalog entries are wrappers that contextualize items included in a catalog.
         */
        CATALOG_ENTRY("CatalogEntry"),

        /**
         * ChargeItem
         * 
         * <p>The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore 
         * referring not only to the product, but containing in addition details of the provision, like date, time, amounts and 
         * participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal 
         * cost allocation.
         */
        CHARGE_ITEM("ChargeItem"),

        /**
         * ChargeItemDefinition
         * 
         * <p>The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate 
         * costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a 
         * rough structure and requires profiling for each type of billing code system.
         */
        CHARGE_ITEM_DEFINITION("ChargeItemDefinition"),

        /**
         * Citation
         * 
         * <p>The Citation Resource enables reference to any knowledge artifact for purposes of identification and attribution. 
         * The Citation Resource supports existing reference structures and developing publication practices such as versioning, 
         * expressing complex contributorship roles, and referencing computable resources.
         */
        CITATION("Citation"),

        /**
         * Claim
         * 
         * <p>A provider issued list of professional services and products which have been provided, or are to be provided, to a 
         * patient which is sent to an insurer for reimbursement.
         */
        CLAIM("Claim"),

        /**
         * ClaimResponse
         * 
         * <p>This resource provides the adjudication details from the processing of a Claim resource.
         */
        CLAIM_RESPONSE("ClaimResponse"),

        /**
         * ClinicalImpression
         * 
         * <p>A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning 
         * the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with 
         * a clinical consultation / encounter, but this varies greatly depending on the clinical workflow. This resource is 
         * called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools 
         * such as Apgar score.
         */
        CLINICAL_IMPRESSION("ClinicalImpression"),

        /**
         * ClinicalUseDefinition
         * 
         * <p>A single issue - either an indication, contraindication, interaction or an undesirable effect for a medicinal 
         * product, medication, device or procedure.
         */
        CLINICAL_USE_DEFINITION("ClinicalUseDefinition"),

        /**
         * CodeSystem
         * 
         * <p>The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement 
         * and its key properties, and optionally define a part or all of its content.
         */
        CODE_SYSTEM("CodeSystem"),

        /**
         * Communication
         * 
         * <p>An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public 
         * health agency that was notified about a reportable condition.
         */
        COMMUNICATION("Communication"),

        /**
         * CommunicationRequest
         * 
         * <p>A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the 
         * CDS system proposes that the public health agency be notified about a reportable condition.
         */
        COMMUNICATION_REQUEST("CommunicationRequest"),

        /**
         * CompartmentDefinition
         * 
         * <p>A compartment definition that defines how resources are accessed on a server.
         */
        COMPARTMENT_DEFINITION("CompartmentDefinition"),

        /**
         * Composition
         * 
         * <p>A set of healthcare-related information that is assembled together into a single logical package that provides a 
         * single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who 
         * is making the statement. A Composition defines the structure and narrative content necessary for a document. However, 
         * a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where 
         * Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in 
         * the Bundle (for example Patient, Practitioner, Encounter, etc.).
         */
        COMPOSITION("Composition"),

        /**
         * ConceptMap
         * 
         * <p>A statement of relationships from one set of concepts to one or more other concepts - either concepts in code 
         * systems, or data element/data element concepts, or classes in class models.
         */
        CONCEPT_MAP("ConceptMap"),

        /**
         * Condition
         * 
         * <p>A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a 
         * level of concern.
         */
        CONDITION("Condition"),

        /**
         * Consent
         * 
         * <p>A record of a healthcare consumer’s choices, which permits or denies identified recipient(s) or recipient role(s) 
         * to perform one or more actions within a given policy context, for specific purposes and periods of time.
         */
        CONSENT("Consent"),

        /**
         * Contract
         * 
         * <p>Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
         */
        CONTRACT("Contract"),

        /**
         * Coverage
         * 
         * <p>Financial instrument which may be used to reimburse or pay for health care products and services. Includes both 
         * insurance and self-payment.
         */
        COVERAGE("Coverage"),

        /**
         * CoverageEligibilityRequest
         * 
         * <p>The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to 
         * respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is 
         * valid and in-force and optionally to provide the insurance details of the policy.
         */
        COVERAGE_ELIGIBILITY_REQUEST("CoverageEligibilityRequest"),

        /**
         * CoverageEligibilityResponse
         * 
         * <p>This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.
         */
        COVERAGE_ELIGIBILITY_RESPONSE("CoverageEligibilityResponse"),

        /**
         * DetectedIssue
         * 
         * <p>Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for 
         * a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
         */
        DETECTED_ISSUE("DetectedIssue"),

        /**
         * Device
         * 
         * <p>A type of a manufactured item that is used in the provision of healthcare without being substantially changed 
         * through that activity. The device may be a medical or non-medical device.
         */
        DEVICE("Device"),

        /**
         * DeviceDefinition
         * 
         * <p>The characteristics, operational status and capabilities of a medical-related component of a medical device.
         */
        DEVICE_DEFINITION("DeviceDefinition"),

        /**
         * DeviceMetric
         * 
         * <p>Describes a measurement, calculation or setting capability of a medical device.
         */
        DEVICE_METRIC("DeviceMetric"),

        /**
         * DeviceRequest
         * 
         * <p>Represents a request for a patient to employ a medical device. The device may be an implantable device, or an 
         * external assistive device, such as a walker.
         */
        DEVICE_REQUEST("DeviceRequest"),

        /**
         * DeviceUseStatement
         * 
         * <p>A record of a device being used by a patient where the record is the result of a report from the patient or another 
         * clinician.
         */
        DEVICE_USE_STATEMENT("DeviceUseStatement"),

        /**
         * DiagnosticReport
         * 
         * <p>The findings and interpretation of diagnostic tests performed on patients, groups of patients, devices, and 
         * locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider 
         * information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation 
         * of diagnostic reports.
         */
        DIAGNOSTIC_REPORT("DiagnosticReport"),

        /**
         * DocumentManifest
         * 
         * <p>A collection of documents compiled for a purpose together with metadata that applies to the collection.
         */
        DOCUMENT_MANIFEST("DocumentManifest"),

        /**
         * DocumentReference
         * 
         * <p>A reference to a document of any kind for any purpose. Provides metadata about the document so that the document 
         * can be discovered and managed. The scope of a document is any seralized object with a mime-type, so includes formal 
         * patient centric documents (CDA), cliical notes, scanned paper, and non-patient specific documents like policy text.
         */
        DOCUMENT_REFERENCE("DocumentReference"),

        /**
         * DomainResource
         * 
         * <p>A resource that includes narrative, extensions, and contained resources.
         */
        DOMAIN_RESOURCE("DomainResource"),

        /**
         * Encounter
         * 
         * <p>An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or 
         * assessing the health status of a patient.
         */
        ENCOUNTER("Encounter"),

        /**
         * Endpoint
         * 
         * <p>The technical details of an endpoint that can be used for electronic services, such as for web services providing 
         * XDS.b or a REST endpoint for another FHIR server. This may include any security context information.
         */
        ENDPOINT("Endpoint"),

        /**
         * EnrollmentRequest
         * 
         * <p>This resource provides the insurance enrollment details to the insurer regarding a specified coverage.
         */
        ENROLLMENT_REQUEST("EnrollmentRequest"),

        /**
         * EnrollmentResponse
         * 
         * <p>This resource provides enrollment and plan details from the processing of an EnrollmentRequest resource.
         */
        ENROLLMENT_RESPONSE("EnrollmentResponse"),

        /**
         * EpisodeOfCare
         * 
         * <p>An association between a patient and an organization / healthcare provider(s) during which time encounters may 
         * occur. The managing organization assumes a level of responsibility for the patient during this time.
         */
        EPISODE_OF_CARE("EpisodeOfCare"),

        /**
         * EventDefinition
         * 
         * <p>The EventDefinition resource provides a reusable description of when a particular event can occur.
         */
        EVENT_DEFINITION("EventDefinition"),

        /**
         * Evidence
         * 
         * <p>The Evidence Resource provides a machine-interpretable expression of an evidence concept including the evidence 
         * variables (eg population, exposures/interventions, comparators, outcomes, measured variables, confounding variables), 
         * the statistics, and the certainty of this evidence.
         */
        EVIDENCE("Evidence"),

        /**
         * EvidenceReport
         * 
         * <p>The EvidenceReport Resource is a specialized container for a collection of resources and codable concepts, adapted 
         * to support compositions of Evidence, EvidenceVariable, and Citation resources and related concepts.
         */
        EVIDENCE_REPORT("EvidenceReport"),

        /**
         * EvidenceVariable
         * 
         * <p>The EvidenceVariable resource describes an element that knowledge (Evidence) is about.
         */
        EVIDENCE_VARIABLE("EvidenceVariable"),

        /**
         * ExampleScenario
         * 
         * <p>Example of workflow instance.
         */
        EXAMPLE_SCENARIO("ExampleScenario"),

        /**
         * ExplanationOfBenefit
         * 
         * <p>This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally 
         * account balance information, for informing the subscriber of the benefits provided.
         */
        EXPLANATION_OF_BENEFIT("ExplanationOfBenefit"),

        /**
         * FamilyMemberHistory
         * 
         * <p>Significant health conditions for a person related to the patient relevant in the context of care for the patient.
         */
        FAMILY_MEMBER_HISTORY("FamilyMemberHistory"),

        /**
         * Flag
         * 
         * <p>Prospective warnings of potential issues when providing care to the patient.
         */
        FLAG("Flag"),

        /**
         * Goal
         * 
         * <p>Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring 
         * an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
         */
        GOAL("Goal"),

        /**
         * GraphDefinition
         * 
         * <p>A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by 
         * following references. The Graph Definition resource defines a set and makes rules about the set.
         */
        GRAPH_DEFINITION("GraphDefinition"),

        /**
         * Group
         * 
         * <p>Represents a defined collection of entities that may be discussed or acted upon collectively but which are not 
         * expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an 
         * Organization.
         */
        GROUP("Group"),

        /**
         * GuidanceResponse
         * 
         * <p>A guidance response is the formal response to a guidance request, including any output parameters returned by the 
         * evaluation, as well as the description of any proposed actions to be taken.
         */
        GUIDANCE_RESPONSE("GuidanceResponse"),

        /**
         * HealthcareService
         * 
         * <p>The details of a healthcare service available at a location.
         */
        HEALTHCARE_SERVICE("HealthcareService"),

        /**
         * ImagingStudy
         * 
         * <p>Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which 
         * includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a 
         * common context. A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple 
         * series of different modalities.
         */
        IMAGING_STUDY("ImagingStudy"),

        /**
         * Immunization
         * 
         * <p>Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a 
         * patient, a clinician or another party.
         */
        IMMUNIZATION("Immunization"),

        /**
         * ImmunizationEvaluation
         * 
         * <p>Describes a comparison of an immunization event against published recommendations to determine if the 
         * administration is "valid" in relation to those recommendations.
         */
        IMMUNIZATION_EVALUATION("ImmunizationEvaluation"),

        /**
         * ImmunizationRecommendation
         * 
         * <p>A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional 
         * supporting justification.
         */
        IMMUNIZATION_RECOMMENDATION("ImmunizationRecommendation"),

        /**
         * ImplementationGuide
         * 
         * <p>A set of rules of how a particular interoperability or standards problem is solved - typically through the use of 
         * FHIR resources. This resource is used to gather all the parts of an implementation guide into a logical whole and to 
         * publish a computable definition of all the parts.
         */
        IMPLEMENTATION_GUIDE("ImplementationGuide"),

        /**
         * Ingredient
         * 
         * <p>An ingredient of a manufactured item or pharmaceutical product.
         */
        INGREDIENT("Ingredient"),

        /**
         * InsurancePlan
         * 
         * <p>Details of a Health Insurance product/plan provided by an organization.
         */
        INSURANCE_PLAN("InsurancePlan"),

        /**
         * Invoice
         * 
         * <p>Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing 
         * purpose.
         */
        INVOICE("Invoice"),

        /**
         * Library
         * 
         * <p>The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and 
         * expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a 
         * collection of knowledge assets.
         */
        LIBRARY("Library"),

        /**
         * Linkage
         * 
         * <p>Identifies two or more records (resource instances) that refer to the same real-world "occurrence".
         */
        LINKAGE("Linkage"),

        /**
         * List
         * 
         * <p>A list is a curated collection of resources.
         */
        LIST("List"),

        /**
         * Location
         * 
         * <p>Details and position information for a physical place where services are provided and resources and participants 
         * may be stored, found, contained, or accommodated.
         */
        LOCATION("Location"),

        /**
         * ManufacturedItemDefinition
         * 
         * <p>The definition and characteristics of a medicinal manufactured item, such as a tablet or capsule, as contained in a 
         * packaged medicinal product.
         */
        MANUFACTURED_ITEM_DEFINITION("ManufacturedItemDefinition"),

        /**
         * Measure
         * 
         * <p>The Measure resource provides the definition of a quality measure.
         */
        MEASURE("Measure"),

        /**
         * MeasureReport
         * 
         * <p>The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the 
         * resources involved in that calculation.
         */
        MEASURE_REPORT("MeasureReport"),

        /**
         * Media
         * 
         * <p>A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by 
         * direct reference.
         */
        MEDIA("Media"),

        /**
         * Medication
         * 
         * <p>This resource is primarily used for the identification and definition of a medication for the purposes of 
         * prescribing, dispensing, and administering a medication as well as for making statements about medication use.
         */
        MEDICATION("Medication"),

        /**
         * MedicationAdministration
         * 
         * <p>Describes the event of a patient consuming or otherwise being administered a medication. This may be as simple as 
         * swallowing a tablet or it may be a long running infusion. Related resources tie this event to the authorizing 
         * prescription, and the specific encounter between patient and health care practitioner.
         */
        MEDICATION_ADMINISTRATION("MedicationAdministration"),

        /**
         * MedicationDispense
         * 
         * <p>Indicates that a medication product is to be or has been dispensed for a named person/patient. This includes a 
         * description of the medication product (supply) provided and the instructions for administering the medication. The 
         * medication dispense is the result of a pharmacy system responding to a medication order.
         */
        MEDICATION_DISPENSE("MedicationDispense"),

        /**
         * MedicationKnowledge
         * 
         * <p>Information about a medication that is used to support knowledge.
         */
        MEDICATION_KNOWLEDGE("MedicationKnowledge"),

        /**
         * MedicationRequest
         * 
         * <p>An order or request for both supply of the medication and the instructions for administration of the medication to 
         * a patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to 
         * generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with 
         * workflow patterns.
         */
        MEDICATION_REQUEST("MedicationRequest"),

        /**
         * MedicationStatement
         * 
         * <p>A record of a medication that is being consumed by a patient. A MedicationStatement may indicate that the patient 
         * may be taking the medication now or has taken the medication in the past or will be taking the medication in the 
         * future. The source of this information can be the patient, significant other (such as a family member or spouse), or a 
         * clinician. A common scenario where this information is captured is during the history taking process during a patient 
         * visit or stay. The medication information may come from sources such as the patient's memory, from a prescription 
         * bottle, or from a list of medications the patient, clinician or other party maintains. 
         * 
         * <p>The primary difference between a medication statement and a medication administration is that the medication 
         * administration has complete administration information and is based on actual administration information from the 
         * person who administered the medication. A medication statement is often, if not always, less specific. There is no 
         * required date/time when the medication was administered, in fact we only know that a source has reported the patient 
         * is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete 
         * or missing or less precise. As stated earlier, the medication statement information may come from the patient's 
         * memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains. 
         * Medication administration is more formal and is not missing detailed information.
         */
        MEDICATION_STATEMENT("MedicationStatement"),

        /**
         * MedicinalProductDefinition
         * 
         * <p>Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use, 
         * drug catalogs).
         */
        MEDICINAL_PRODUCT_DEFINITION("MedicinalProductDefinition"),

        /**
         * MessageDefinition
         * 
         * <p>Defines the characteristics of a message that can be shared between systems, including the type of event that 
         * initiates the message, the content to be transmitted and what response(s), if any, are permitted.
         */
        MESSAGE_DEFINITION("MessageDefinition"),

        /**
         * MessageHeader
         * 
         * <p>The header for a message exchange that is either requesting or responding to an action. The reference(s) that are 
         * the subject of the action as well as other information related to the action are typically transmitted in a bundle in 
         * which the MessageHeader resource instance is the first resource in the bundle.
         */
        MESSAGE_HEADER("MessageHeader"),

        /**
         * MolecularSequence
         * 
         * <p>Raw data describing a biological sequence.
         */
        MOLECULAR_SEQUENCE("MolecularSequence"),

        /**
         * NamingSystem
         * 
         * <p>A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, 
         * devices, etc. Represents a "System" used within the Identifier and Coding data types.
         */
        NAMING_SYSTEM("NamingSystem"),

        /**
         * NutritionOrder
         * 
         * <p>A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
         */
        NUTRITION_ORDER("NutritionOrder"),

        /**
         * NutritionProduct
         * 
         * <p>A food or fluid product that is consumed by patients.
         */
        NUTRITION_PRODUCT("NutritionProduct"),

        /**
         * Observation
         * 
         * <p>Measurements and simple assertions made about a patient, device or other subject.
         */
        OBSERVATION("Observation"),

        /**
         * ObservationDefinition
         * 
         * <p>Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable 
         * health care service.
         */
        OBSERVATION_DEFINITION("ObservationDefinition"),

        /**
         * OperationDefinition
         * 
         * <p>A formal computable definition of an operation (on the RESTful interface) or a named query (using the search 
         * interaction).
         */
        OPERATION_DEFINITION("OperationDefinition"),

        /**
         * OperationOutcome
         * 
         * <p>A collection of error, warning, or information messages that result from a system action.
         */
        OPERATION_OUTCOME("OperationOutcome"),

        /**
         * Organization
         * 
         * <p>A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some 
         * form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare 
         * practice groups, payer/insurer, etc.
         */
        ORGANIZATION("Organization"),

        /**
         * OrganizationAffiliation
         * 
         * <p>Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of 
         * relationship/sub-division relationship.
         */
        ORGANIZATION_AFFILIATION("OrganizationAffiliation"),

        /**
         * PackagedProductDefinition
         * 
         * <p>A medically related item or items, in a container or package.
         */
        PACKAGED_PRODUCT_DEFINITION("PackagedProductDefinition"),

        /**
         * Parameters
         * 
         * <p>This resource is a non-persisted resource used to pass information into and back from an [operation](operations.
         * html). It has no other use, and there is no RESTful endpoint associated with it.
         */
        PARAMETERS("Parameters"),

        /**
         * Patient
         * 
         * <p>Demographics and other administrative information about an individual or animal receiving care or other health-
         * related services.
         */
        PATIENT("Patient"),

        /**
         * PaymentNotice
         * 
         * <p>This resource provides the status of the payment for goods and services rendered, and the request and response 
         * resource references.
         */
        PAYMENT_NOTICE("PaymentNotice"),

        /**
         * PaymentReconciliation
         * 
         * <p>This resource provides the details including amount of a payment and allocates the payment items being paid.
         */
        PAYMENT_RECONCILIATION("PaymentReconciliation"),

        /**
         * Person
         * 
         * <p>Demographics and administrative information about a person independent of a specific health-related context.
         */
        PERSON("Person"),

        /**
         * PlanDefinition
         * 
         * <p>This resource allows for the definition of various types of plans as a sharable, consumable, and executable 
         * artifact. The resource is general enough to support the description of a broad range of clinical and non-clinical 
         * artifacts such as clinical decision support rules, order sets, protocols, and drug quality specifications.
         */
        PLAN_DEFINITION("PlanDefinition"),

        /**
         * Practitioner
         * 
         * <p>A person who is directly or indirectly involved in the provisioning of healthcare.
         */
        PRACTITIONER("Practitioner"),

        /**
         * PractitionerRole
         * 
         * <p>A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a 
         * period of time.
         */
        PRACTITIONER_ROLE("PractitionerRole"),

        /**
         * Procedure
         * 
         * <p>An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or 
         * less invasive like long term services, counseling, or hypnotherapy.
         */
        PROCEDURE("Procedure"),

        /**
         * Provenance
         * 
         * <p>Provenance of a resource is a record that describes entities and processes involved in producing and delivering or 
         * otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling 
         * trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become 
         * important records with their own provenance. Provenance statement indicates clinical significance in terms of 
         * confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document 
         * Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust 
         * policies.
         */
        PROVENANCE("Provenance"),

        /**
         * Questionnaire
         * 
         * <p>A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide 
         * detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
         */
        QUESTIONNAIRE("Questionnaire"),

        /**
         * QuestionnaireResponse
         * 
         * <p>A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, 
         * corresponding to the structure of the grouping of the questionnaire being responded to.
         */
        QUESTIONNAIRE_RESPONSE("QuestionnaireResponse"),

        /**
         * RegulatedAuthorization
         * 
         * <p>Regulatory approval, clearance or licencing related to a regulated product, treatment, facility or activity that is 
         * cited in a guidance, regulation, rule or legislative act. An example is Market Authorization relating to a Medicinal 
         * Product.
         */
        REGULATED_AUTHORIZATION("RegulatedAuthorization"),

        /**
         * RelatedPerson
         * 
         * <p>Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor 
         * has a formal responsibility in the care process.
         */
        RELATED_PERSON("RelatedPerson"),

        /**
         * RequestGroup
         * 
         * <p>A group of related requests that can be used to capture intended activities that have inter-dependencies such as 
         * "give this medication after that one".
         */
        REQUEST_GROUP("RequestGroup"),

        /**
         * ResearchDefinition
         * 
         * <p>The ResearchDefinition resource describes the conditional state (population and any exposures being compared within 
         * the population) and outcome (if specified) that the knowledge (evidence, assertion, recommendation) is about.
         */
        RESEARCH_DEFINITION("ResearchDefinition"),

        /**
         * ResearchElementDefinition
         * 
         * <p>The ResearchElementDefinition resource describes a "PICO" element that knowledge (evidence, assertion, 
         * recommendation) is about.
         */
        RESEARCH_ELEMENT_DEFINITION("ResearchElementDefinition"),

        /**
         * ResearchStudy
         * 
         * <p>A process where a researcher or organization plans and then executes a series of steps intended to increase the 
         * field of healthcare-related knowledge. This includes studies of safety, efficacy, comparative effectiveness and other 
         * information about medications, devices, therapies and other interventional and investigative techniques. A 
         * ResearchStudy involves the gathering of information about human or animal subjects.
         */
        RESEARCH_STUDY("ResearchStudy"),

        /**
         * ResearchSubject
         * 
         * <p>A physical entity which is the primary unit of operational and/or administrative interest in a study.
         */
        RESEARCH_SUBJECT("ResearchSubject"),

        /**
         * Resource
         * 
         * <p>This is the base resource type for everything.
         */
        RESOURCE("Resource"),

        /**
         * RiskAssessment
         * 
         * <p>An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
         */
        RISK_ASSESSMENT("RiskAssessment"),

        /**
         * Schedule
         * 
         * <p>A container for slots of time that may be available for booking appointments.
         */
        SCHEDULE("Schedule"),

        /**
         * SearchParameter
         * 
         * <p>A search parameter that defines a named search item that can be used to search/filter on a resource.
         */
        SEARCH_PARAMETER("SearchParameter"),

        /**
         * ServiceRequest
         * 
         * <p>A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
         */
        SERVICE_REQUEST("ServiceRequest"),

        /**
         * Slot
         * 
         * <p>A slot of time on a schedule that may be available for booking appointments.
         */
        SLOT("Slot"),

        /**
         * Specimen
         * 
         * <p>A sample to be used for analysis.
         */
        SPECIMEN("Specimen"),

        /**
         * SpecimenDefinition
         * 
         * <p>A kind of specimen with associated set of requirements.
         */
        SPECIMEN_DEFINITION("SpecimenDefinition"),

        /**
         * StructureDefinition
         * 
         * <p>A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in 
         * FHIR, and also for describing extensions and constraints on resources and data types.
         */
        STRUCTURE_DEFINITION("StructureDefinition"),

        /**
         * StructureMap
         * 
         * <p>A Map of relationships between 2 structures that can be used to transform data.
         */
        STRUCTURE_MAP("StructureMap"),

        /**
         * Subscription
         * 
         * <p>The subscription resource is used to define a push-based subscription from a server to another system. Once a 
         * subscription is registered with the server, the server checks every resource that is created or updated, and if the 
         * resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an 
         * appropriate action.
         */
        SUBSCRIPTION("Subscription"),

        /**
         * SubscriptionStatus
         * 
         * <p>The SubscriptionStatus resource describes the state of a Subscription during notifications.
         */
        SUBSCRIPTION_STATUS("SubscriptionStatus"),

        /**
         * SubscriptionTopic
         * 
         * <p>Describes a stream of resource state changes or events and annotated with labels useful to filter projections from 
         * this topic.
         */
        SUBSCRIPTION_TOPIC("SubscriptionTopic"),

        /**
         * Substance
         * 
         * <p>A homogeneous material with a definite composition.
         */
        SUBSTANCE("Substance"),

        /**
         * SubstanceDefinition
         * 
         * <p>The detailed description of a substance, typically at a level beyond what is used for prescribing.
         */
        SUBSTANCE_DEFINITION("SubstanceDefinition"),

        /**
         * SupplyDelivery
         * 
         * <p>Record of delivery of what is supplied.
         */
        SUPPLY_DELIVERY("SupplyDelivery"),

        /**
         * SupplyRequest
         * 
         * <p>A record of a request for a medication, substance or device used in the healthcare setting.
         */
        SUPPLY_REQUEST("SupplyRequest"),

        /**
         * Task
         * 
         * <p>A task to be performed.
         */
        TASK("Task"),

        /**
         * TerminologyCapabilities
         * 
         * <p>A TerminologyCapabilities resource documents a set of capabilities (behaviors) of a FHIR Terminology Server that 
         * may be used as a statement of actual server functionality or a statement of required or desired server implementation.
         */
        TERMINOLOGY_CAPABILITIES("TerminologyCapabilities"),

        /**
         * TestReport
         * 
         * <p>A summary of information based on the results of executing a TestScript.
         */
        TEST_REPORT("TestReport"),

        /**
         * TestScript
         * 
         * <p>A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR 
         * specification.
         */
        TEST_SCRIPT("TestScript"),

        /**
         * ValueSet
         * 
         * <p>A ValueSet resource instance specifies a set of codes drawn from one or more code systems, intended for use in a 
         * particular context. Value sets link between [CodeSystem](codesystem.html) definitions and their use in [coded elements]
         * (terminologies.html).
         */
        VALUE_SET("ValueSet"),

        /**
         * VerificationResult
         * 
         * <p>Describes validation requirements, source(s), status and dates for one or more elements.
         */
        VERIFICATION_RESULT("VerificationResult"),

        /**
         * VisionPrescription
         * 
         * <p>An authorization for the provision of glasses and/or contact lenses to a patient.
         */
        VISION_PRESCRIPTION("VisionPrescription"),

        /**
         * Type
         * 
         * <p>A place holder that means any kind of data type
         */
        TYPE("Type"),

        /**
         * Any
         * 
         * <p>A place holder that means any kind of resource
         */
        ANY("Any");

        private final java.lang.String value;

        Value(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating FHIRAllTypes.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding FHIRAllTypes.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "EffectEvidenceSynthesis":
                return EFFECT_EVIDENCE_SYNTHESIS;
            case "MedicinalProduct":
                return MEDICINAL_PRODUCT;
            case "MedicinalProductAuthorization":
                return MEDICINAL_PRODUCT_AUTHORIZATION;
            case "MedicinalProductContraindication":
                return MEDICINAL_PRODUCT_CONTRAINDICATION;
            case "MedicinalProductIndication":
                return MEDICINAL_PRODUCT_INDICATION;
            case "MedicinalProductIngredient":
                return MEDICINAL_PRODUCT_INGREDIENT;
            case "MedicinalProductInteraction":
                return MEDICINAL_PRODUCT_INTERACTION;
            case "MedicinalProductManufactured":
                return MEDICINAL_PRODUCT_MANUFACTURED;
            case "MedicinalProductPackaged":
                return MEDICINAL_PRODUCT_PACKAGED;
            case "MedicinalProductPharmaceutical":
                return MEDICINAL_PRODUCT_PHARMACEUTICAL;
            case "MedicinalProductUndesirableEffect":
                return MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT;
            case "RiskEvidenceSynthesis":
                return RISK_EVIDENCE_SYNTHESIS;
            case "SubstanceNucleicAcid":
                return SUBSTANCE_NUCLEIC_ACID;
            case "SubstancePolymer":
                return SUBSTANCE_POLYMER;
            case "SubstanceProtein":
                return SUBSTANCE_PROTEIN;
            case "SubstanceReferenceInformation":
                return SUBSTANCE_REFERENCE_INFORMATION;
            case "SubstanceSourceMaterial":
                return SUBSTANCE_SOURCE_MATERIAL;
            case "SubstanceSpecification":
                return SUBSTANCE_SPECIFICATION;
            case "SubstanceAmount":
                return SUBSTANCE_AMOUNT;
            case "Address":
                return ADDRESS;
            case "Age":
                return AGE;
            case "Annotation":
                return ANNOTATION;
            case "Attachment":
                return ATTACHMENT;
            case "BackboneElement":
                return BACKBONE_ELEMENT;
            case "CodeableConcept":
                return CODEABLE_CONCEPT;
            case "CodeableReference":
                return CODEABLE_REFERENCE;
            case "Coding":
                return CODING;
            case "ContactDetail":
                return CONTACT_DETAIL;
            case "ContactPoint":
                return CONTACT_POINT;
            case "Contributor":
                return CONTRIBUTOR;
            case "Count":
                return COUNT;
            case "DataRequirement":
                return DATA_REQUIREMENT;
            case "DataType":
                return DATA_TYPE;
            case "Distance":
                return DISTANCE;
            case "Dosage":
                return DOSAGE;
            case "Duration":
                return DURATION;
            case "Element":
                return ELEMENT;
            case "ElementDefinition":
                return ELEMENT_DEFINITION;
            case "Expression":
                return EXPRESSION;
            case "Extension":
                return EXTENSION;
            case "HumanName":
                return HUMAN_NAME;
            case "Identifier":
                return IDENTIFIER;
            case "MarketingStatus":
                return MARKETING_STATUS;
            case "Meta":
                return META;
            case "Money":
                return MONEY;
            case "MoneyQuantity":
                return MONEY_QUANTITY;
            case "Narrative":
                return NARRATIVE;
            case "ParameterDefinition":
                return PARAMETER_DEFINITION;
            case "Period":
                return PERIOD;
            case "Population":
                return POPULATION;
            case "ProdCharacteristic":
                return PROD_CHARACTERISTIC;
            case "ProductShelfLife":
                return PRODUCT_SHELF_LIFE;
            case "Quantity":
                return QUANTITY;
            case "Range":
                return RANGE;
            case "Ratio":
                return RATIO;
            case "RatioRange":
                return RATIO_RANGE;
            case "Reference":
                return REFERENCE;
            case "RelatedArtifact":
                return RELATED_ARTIFACT;
            case "SampledData":
                return SAMPLED_DATA;
            case "Signature":
                return SIGNATURE;
            case "SimpleQuantity":
                return SIMPLE_QUANTITY;
            case "Timing":
                return TIMING;
            case "TriggerDefinition":
                return TRIGGER_DEFINITION;
            case "UsageContext":
                return USAGE_CONTEXT;
            case "base64Binary":
                return BASE64BINARY;
            case "boolean":
                return BOOLEAN;
            case "canonical":
                return CANONICAL;
            case "code":
                return CODE;
            case "date":
                return DATE;
            case "dateTime":
                return DATE_TIME;
            case "decimal":
                return DECIMAL;
            case "id":
                return ID;
            case "instant":
                return INSTANT;
            case "integer":
                return INTEGER;
            case "markdown":
                return MARKDOWN;
            case "oid":
                return OID;
            case "positiveInt":
                return POSITIVE_INT;
            case "string":
                return STRING;
            case "time":
                return TIME;
            case "unsignedInt":
                return UNSIGNED_INT;
            case "uri":
                return URI;
            case "url":
                return URL;
            case "uuid":
                return UUID;
            case "xhtml":
                return XHTML;
            case "Account":
                return ACCOUNT;
            case "ActivityDefinition":
                return ACTIVITY_DEFINITION;
            case "AdministrableProductDefinition":
                return ADMINISTRABLE_PRODUCT_DEFINITION;
            case "AdverseEvent":
                return ADVERSE_EVENT;
            case "AllergyIntolerance":
                return ALLERGY_INTOLERANCE;
            case "Appointment":
                return APPOINTMENT;
            case "AppointmentResponse":
                return APPOINTMENT_RESPONSE;
            case "AuditEvent":
                return AUDIT_EVENT;
            case "Basic":
                return BASIC;
            case "Binary":
                return BINARY;
            case "BiologicallyDerivedProduct":
                return BIOLOGICALLY_DERIVED_PRODUCT;
            case "BodyStructure":
                return BODY_STRUCTURE;
            case "Bundle":
                return BUNDLE;
            case "CapabilityStatement":
                return CAPABILITY_STATEMENT;
            case "CarePlan":
                return CARE_PLAN;
            case "CareTeam":
                return CARE_TEAM;
            case "CatalogEntry":
                return CATALOG_ENTRY;
            case "ChargeItem":
                return CHARGE_ITEM;
            case "ChargeItemDefinition":
                return CHARGE_ITEM_DEFINITION;
            case "Citation":
                return CITATION;
            case "Claim":
                return CLAIM;
            case "ClaimResponse":
                return CLAIM_RESPONSE;
            case "ClinicalImpression":
                return CLINICAL_IMPRESSION;
            case "ClinicalUseDefinition":
                return CLINICAL_USE_DEFINITION;
            case "CodeSystem":
                return CODE_SYSTEM;
            case "Communication":
                return COMMUNICATION;
            case "CommunicationRequest":
                return COMMUNICATION_REQUEST;
            case "CompartmentDefinition":
                return COMPARTMENT_DEFINITION;
            case "Composition":
                return COMPOSITION;
            case "ConceptMap":
                return CONCEPT_MAP;
            case "Condition":
                return CONDITION;
            case "Consent":
                return CONSENT;
            case "Contract":
                return CONTRACT;
            case "Coverage":
                return COVERAGE;
            case "CoverageEligibilityRequest":
                return COVERAGE_ELIGIBILITY_REQUEST;
            case "CoverageEligibilityResponse":
                return COVERAGE_ELIGIBILITY_RESPONSE;
            case "DetectedIssue":
                return DETECTED_ISSUE;
            case "Device":
                return DEVICE;
            case "DeviceDefinition":
                return DEVICE_DEFINITION;
            case "DeviceMetric":
                return DEVICE_METRIC;
            case "DeviceRequest":
                return DEVICE_REQUEST;
            case "DeviceUseStatement":
                return DEVICE_USE_STATEMENT;
            case "DiagnosticReport":
                return DIAGNOSTIC_REPORT;
            case "DocumentManifest":
                return DOCUMENT_MANIFEST;
            case "DocumentReference":
                return DOCUMENT_REFERENCE;
            case "DomainResource":
                return DOMAIN_RESOURCE;
            case "Encounter":
                return ENCOUNTER;
            case "Endpoint":
                return ENDPOINT;
            case "EnrollmentRequest":
                return ENROLLMENT_REQUEST;
            case "EnrollmentResponse":
                return ENROLLMENT_RESPONSE;
            case "EpisodeOfCare":
                return EPISODE_OF_CARE;
            case "EventDefinition":
                return EVENT_DEFINITION;
            case "Evidence":
                return EVIDENCE;
            case "EvidenceReport":
                return EVIDENCE_REPORT;
            case "EvidenceVariable":
                return EVIDENCE_VARIABLE;
            case "ExampleScenario":
                return EXAMPLE_SCENARIO;
            case "ExplanationOfBenefit":
                return EXPLANATION_OF_BENEFIT;
            case "FamilyMemberHistory":
                return FAMILY_MEMBER_HISTORY;
            case "Flag":
                return FLAG;
            case "Goal":
                return GOAL;
            case "GraphDefinition":
                return GRAPH_DEFINITION;
            case "Group":
                return GROUP;
            case "GuidanceResponse":
                return GUIDANCE_RESPONSE;
            case "HealthcareService":
                return HEALTHCARE_SERVICE;
            case "ImagingStudy":
                return IMAGING_STUDY;
            case "Immunization":
                return IMMUNIZATION;
            case "ImmunizationEvaluation":
                return IMMUNIZATION_EVALUATION;
            case "ImmunizationRecommendation":
                return IMMUNIZATION_RECOMMENDATION;
            case "ImplementationGuide":
                return IMPLEMENTATION_GUIDE;
            case "Ingredient":
                return INGREDIENT;
            case "InsurancePlan":
                return INSURANCE_PLAN;
            case "Invoice":
                return INVOICE;
            case "Library":
                return LIBRARY;
            case "Linkage":
                return LINKAGE;
            case "List":
                return LIST;
            case "Location":
                return LOCATION;
            case "ManufacturedItemDefinition":
                return MANUFACTURED_ITEM_DEFINITION;
            case "Measure":
                return MEASURE;
            case "MeasureReport":
                return MEASURE_REPORT;
            case "Media":
                return MEDIA;
            case "Medication":
                return MEDICATION;
            case "MedicationAdministration":
                return MEDICATION_ADMINISTRATION;
            case "MedicationDispense":
                return MEDICATION_DISPENSE;
            case "MedicationKnowledge":
                return MEDICATION_KNOWLEDGE;
            case "MedicationRequest":
                return MEDICATION_REQUEST;
            case "MedicationStatement":
                return MEDICATION_STATEMENT;
            case "MedicinalProductDefinition":
                return MEDICINAL_PRODUCT_DEFINITION;
            case "MessageDefinition":
                return MESSAGE_DEFINITION;
            case "MessageHeader":
                return MESSAGE_HEADER;
            case "MolecularSequence":
                return MOLECULAR_SEQUENCE;
            case "NamingSystem":
                return NAMING_SYSTEM;
            case "NutritionOrder":
                return NUTRITION_ORDER;
            case "NutritionProduct":
                return NUTRITION_PRODUCT;
            case "Observation":
                return OBSERVATION;
            case "ObservationDefinition":
                return OBSERVATION_DEFINITION;
            case "OperationDefinition":
                return OPERATION_DEFINITION;
            case "OperationOutcome":
                return OPERATION_OUTCOME;
            case "Organization":
                return ORGANIZATION;
            case "OrganizationAffiliation":
                return ORGANIZATION_AFFILIATION;
            case "PackagedProductDefinition":
                return PACKAGED_PRODUCT_DEFINITION;
            case "Parameters":
                return PARAMETERS;
            case "Patient":
                return PATIENT;
            case "PaymentNotice":
                return PAYMENT_NOTICE;
            case "PaymentReconciliation":
                return PAYMENT_RECONCILIATION;
            case "Person":
                return PERSON;
            case "PlanDefinition":
                return PLAN_DEFINITION;
            case "Practitioner":
                return PRACTITIONER;
            case "PractitionerRole":
                return PRACTITIONER_ROLE;
            case "Procedure":
                return PROCEDURE;
            case "Provenance":
                return PROVENANCE;
            case "Questionnaire":
                return QUESTIONNAIRE;
            case "QuestionnaireResponse":
                return QUESTIONNAIRE_RESPONSE;
            case "RegulatedAuthorization":
                return REGULATED_AUTHORIZATION;
            case "RelatedPerson":
                return RELATED_PERSON;
            case "RequestGroup":
                return REQUEST_GROUP;
            case "ResearchDefinition":
                return RESEARCH_DEFINITION;
            case "ResearchElementDefinition":
                return RESEARCH_ELEMENT_DEFINITION;
            case "ResearchStudy":
                return RESEARCH_STUDY;
            case "ResearchSubject":
                return RESEARCH_SUBJECT;
            case "Resource":
                return RESOURCE;
            case "RiskAssessment":
                return RISK_ASSESSMENT;
            case "Schedule":
                return SCHEDULE;
            case "SearchParameter":
                return SEARCH_PARAMETER;
            case "ServiceRequest":
                return SERVICE_REQUEST;
            case "Slot":
                return SLOT;
            case "Specimen":
                return SPECIMEN;
            case "SpecimenDefinition":
                return SPECIMEN_DEFINITION;
            case "StructureDefinition":
                return STRUCTURE_DEFINITION;
            case "StructureMap":
                return STRUCTURE_MAP;
            case "Subscription":
                return SUBSCRIPTION;
            case "SubscriptionStatus":
                return SUBSCRIPTION_STATUS;
            case "SubscriptionTopic":
                return SUBSCRIPTION_TOPIC;
            case "Substance":
                return SUBSTANCE;
            case "SubstanceDefinition":
                return SUBSTANCE_DEFINITION;
            case "SupplyDelivery":
                return SUPPLY_DELIVERY;
            case "SupplyRequest":
                return SUPPLY_REQUEST;
            case "Task":
                return TASK;
            case "TerminologyCapabilities":
                return TERMINOLOGY_CAPABILITIES;
            case "TestReport":
                return TEST_REPORT;
            case "TestScript":
                return TEST_SCRIPT;
            case "ValueSet":
                return VALUE_SET;
            case "VerificationResult":
                return VERIFICATION_RESULT;
            case "VisionPrescription":
                return VISION_PRESCRIPTION;
            case "Type":
                return TYPE;
            case "Any":
                return ANY;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
