/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ObservationDataType;
import com.ibm.fhir.model.type.code.ObservationRangeCategory;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable 
 * health care service.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "observationDefinition-0",
    level = "Warning",
    location = "quantitativeDetails.customaryUnit",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/ucum-units",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/ucum-units', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/ObservationDefinition",
    generated = true
)
@Constraint(
    id = "observationDefinition-1",
    level = "Warning",
    location = "quantitativeDetails.unit",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/ucum-units",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/ucum-units', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/ObservationDefinition",
    generated = true
)
@Constraint(
    id = "observationDefinition-2",
    level = "Warning",
    location = "qualifiedInterval.context",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/referencerange-meaning",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/referencerange-meaning', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/ObservationDefinition",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ObservationDefinition extends DomainResource {
    @Summary
    @Binding(
        bindingName = "ObservationCategory",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for high level observation categories.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-category"
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "ObservationCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes identifying names of simple observations.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-codes"
    )
    @Required
    private final CodeableConcept code;
    @Summary
    private final List<Identifier> identifier;
    @Binding(
        bindingName = "ObservationDataType",
        strength = BindingStrength.Value.REQUIRED,
        description = "Permitted data type for observation value.",
        valueSet = "http://hl7.org/fhir/ValueSet/permitted-data-type|4.3.0"
    )
    private final List<ObservationDataType> permittedDataType;
    private final Boolean multipleResultsAllowed;
    @Binding(
        bindingName = "ObservationMethod",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Methods for simple observations.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-methods"
    )
    private final CodeableConcept method;
    private final String preferredReportName;
    private final QuantitativeDetails quantitativeDetails;
    private final List<QualifiedInterval> qualifiedInterval;
    @ReferenceTarget({ "ValueSet" })
    private final Reference validCodedValueSet;
    @ReferenceTarget({ "ValueSet" })
    private final Reference normalCodedValueSet;
    @ReferenceTarget({ "ValueSet" })
    private final Reference abnormalCodedValueSet;
    @ReferenceTarget({ "ValueSet" })
    private final Reference criticalCodedValueSet;

    private ObservationDefinition(Builder builder) {
        super(builder);
        category = Collections.unmodifiableList(builder.category);
        code = builder.code;
        identifier = Collections.unmodifiableList(builder.identifier);
        permittedDataType = Collections.unmodifiableList(builder.permittedDataType);
        multipleResultsAllowed = builder.multipleResultsAllowed;
        method = builder.method;
        preferredReportName = builder.preferredReportName;
        quantitativeDetails = builder.quantitativeDetails;
        qualifiedInterval = Collections.unmodifiableList(builder.qualifiedInterval);
        validCodedValueSet = builder.validCodedValueSet;
        normalCodedValueSet = builder.normalCodedValueSet;
        abnormalCodedValueSet = builder.abnormalCodedValueSet;
        criticalCodedValueSet = builder.criticalCodedValueSet;
    }

    /**
     * A code that classifies the general type of observation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * Describes what will be observed. Sometimes this is called the observation "name".
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * A unique identifier assigned to this ObservationDefinition artifact.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ObservationDataType} that may be empty.
     */
    public List<ObservationDataType> getPermittedDataType() {
        return permittedDataType;
    }

    /**
     * Multiple results allowed for observations conforming to this ObservationDefinition.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getMultipleResultsAllowed() {
        return multipleResultsAllowed;
    }

    /**
     * The method or technique used to perform the observation.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getMethod() {
        return method;
    }

    /**
     * The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPreferredReportName() {
        return preferredReportName;
    }

    /**
     * Characteristics for quantitative results of this observation.
     * 
     * @return
     *     An immutable object of type {@link QuantitativeDetails} that may be null.
     */
    public QuantitativeDetails getQuantitativeDetails() {
        return quantitativeDetails;
    }

    /**
     * Multiple ranges of results qualified by different contexts for ordinal or continuous observations conforming to this 
     * ObservationDefinition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link QualifiedInterval} that may be empty.
     */
    public List<QualifiedInterval> getQualifiedInterval() {
        return qualifiedInterval;
    }

    /**
     * The set of valid coded results for the observations conforming to this ObservationDefinition.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getValidCodedValueSet() {
        return validCodedValueSet;
    }

    /**
     * The set of normal coded results for the observations conforming to this ObservationDefinition.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getNormalCodedValueSet() {
        return normalCodedValueSet;
    }

    /**
     * The set of abnormal coded results for the observation conforming to this ObservationDefinition.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAbnormalCodedValueSet() {
        return abnormalCodedValueSet;
    }

    /**
     * The set of critical coded results for the observation conforming to this ObservationDefinition.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getCriticalCodedValueSet() {
        return criticalCodedValueSet;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !category.isEmpty() || 
            (code != null) || 
            !identifier.isEmpty() || 
            !permittedDataType.isEmpty() || 
            (multipleResultsAllowed != null) || 
            (method != null) || 
            (preferredReportName != null) || 
            (quantitativeDetails != null) || 
            !qualifiedInterval.isEmpty() || 
            (validCodedValueSet != null) || 
            (normalCodedValueSet != null) || 
            (abnormalCodedValueSet != null) || 
            (criticalCodedValueSet != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(meta, "meta", visitor);
                accept(implicitRules, "implicitRules", visitor);
                accept(language, "language", visitor);
                accept(text, "text", visitor);
                accept(contained, "contained", visitor, Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(code, "code", visitor);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(permittedDataType, "permittedDataType", visitor, ObservationDataType.class);
                accept(multipleResultsAllowed, "multipleResultsAllowed", visitor);
                accept(method, "method", visitor);
                accept(preferredReportName, "preferredReportName", visitor);
                accept(quantitativeDetails, "quantitativeDetails", visitor);
                accept(qualifiedInterval, "qualifiedInterval", visitor, QualifiedInterval.class);
                accept(validCodedValueSet, "validCodedValueSet", visitor);
                accept(normalCodedValueSet, "normalCodedValueSet", visitor);
                accept(abnormalCodedValueSet, "abnormalCodedValueSet", visitor);
                accept(criticalCodedValueSet, "criticalCodedValueSet", visitor);
            }
            visitor.visitEnd(elementName, elementIndex, this);
            visitor.postVisit(this);
        }
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
        ObservationDefinition other = (ObservationDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(category, other.category) && 
            Objects.equals(code, other.code) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(permittedDataType, other.permittedDataType) && 
            Objects.equals(multipleResultsAllowed, other.multipleResultsAllowed) && 
            Objects.equals(method, other.method) && 
            Objects.equals(preferredReportName, other.preferredReportName) && 
            Objects.equals(quantitativeDetails, other.quantitativeDetails) && 
            Objects.equals(qualifiedInterval, other.qualifiedInterval) && 
            Objects.equals(validCodedValueSet, other.validCodedValueSet) && 
            Objects.equals(normalCodedValueSet, other.normalCodedValueSet) && 
            Objects.equals(abnormalCodedValueSet, other.abnormalCodedValueSet) && 
            Objects.equals(criticalCodedValueSet, other.criticalCodedValueSet);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                text, 
                contained, 
                extension, 
                modifierExtension, 
                category, 
                code, 
                identifier, 
                permittedDataType, 
                multipleResultsAllowed, 
                method, 
                preferredReportName, 
                quantitativeDetails, 
                qualifiedInterval, 
                validCodedValueSet, 
                normalCodedValueSet, 
                abnormalCodedValueSet, 
                criticalCodedValueSet);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private List<CodeableConcept> category = new ArrayList<>();
        private CodeableConcept code;
        private List<Identifier> identifier = new ArrayList<>();
        private List<ObservationDataType> permittedDataType = new ArrayList<>();
        private Boolean multipleResultsAllowed;
        private CodeableConcept method;
        private String preferredReportName;
        private QuantitativeDetails quantitativeDetails;
        private List<QualifiedInterval> qualifiedInterval = new ArrayList<>();
        private Reference validCodedValueSet;
        private Reference normalCodedValueSet;
        private Reference abnormalCodedValueSet;
        private Reference criticalCodedValueSet;

        private Builder() {
            super();
        }

        /**
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * The base language in which the resource is written.
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * A code that classifies the general type of observation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Category of observation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * A code that classifies the general type of observation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Category of observation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * Describes what will be observed. Sometimes this is called the observation "name".
         * 
         * <p>This element is required.
         * 
         * @param code
         *     Type of observation (code / type)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * A unique identifier assigned to this ObservationDefinition artifact.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for this ObservationDefinition instance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * A unique identifier assigned to this ObservationDefinition artifact.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier for this ObservationDefinition instance
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param permittedDataType
         *     Quantity | CodeableConcept | string | boolean | integer | Range | Ratio | SampledData | time | dateTime | Period
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder permittedDataType(ObservationDataType... permittedDataType) {
            for (ObservationDataType value : permittedDataType) {
                this.permittedDataType.add(value);
            }
            return this;
        }

        /**
         * The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param permittedDataType
         *     Quantity | CodeableConcept | string | boolean | integer | Range | Ratio | SampledData | time | dateTime | Period
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder permittedDataType(Collection<ObservationDataType> permittedDataType) {
            this.permittedDataType = new ArrayList<>(permittedDataType);
            return this;
        }

        /**
         * Convenience method for setting {@code multipleResultsAllowed}.
         * 
         * @param multipleResultsAllowed
         *     Multiple results allowed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #multipleResultsAllowed(com.ibm.fhir.model.type.Boolean)
         */
        public Builder multipleResultsAllowed(java.lang.Boolean multipleResultsAllowed) {
            this.multipleResultsAllowed = (multipleResultsAllowed == null) ? null : Boolean.of(multipleResultsAllowed);
            return this;
        }

        /**
         * Multiple results allowed for observations conforming to this ObservationDefinition.
         * 
         * @param multipleResultsAllowed
         *     Multiple results allowed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder multipleResultsAllowed(Boolean multipleResultsAllowed) {
            this.multipleResultsAllowed = multipleResultsAllowed;
            return this;
        }

        /**
         * The method or technique used to perform the observation.
         * 
         * @param method
         *     Method used to produce the observation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder method(CodeableConcept method) {
            this.method = method;
            return this;
        }

        /**
         * Convenience method for setting {@code preferredReportName}.
         * 
         * @param preferredReportName
         *     Preferred report name
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #preferredReportName(com.ibm.fhir.model.type.String)
         */
        public Builder preferredReportName(java.lang.String preferredReportName) {
            this.preferredReportName = (preferredReportName == null) ? null : String.of(preferredReportName);
            return this;
        }

        /**
         * The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.
         * 
         * @param preferredReportName
         *     Preferred report name
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder preferredReportName(String preferredReportName) {
            this.preferredReportName = preferredReportName;
            return this;
        }

        /**
         * Characteristics for quantitative results of this observation.
         * 
         * @param quantitativeDetails
         *     Characteristics of quantitative results
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quantitativeDetails(QuantitativeDetails quantitativeDetails) {
            this.quantitativeDetails = quantitativeDetails;
            return this;
        }

        /**
         * Multiple ranges of results qualified by different contexts for ordinal or continuous observations conforming to this 
         * ObservationDefinition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param qualifiedInterval
         *     Qualified range for continuous and ordinal observation results
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder qualifiedInterval(QualifiedInterval... qualifiedInterval) {
            for (QualifiedInterval value : qualifiedInterval) {
                this.qualifiedInterval.add(value);
            }
            return this;
        }

        /**
         * Multiple ranges of results qualified by different contexts for ordinal or continuous observations conforming to this 
         * ObservationDefinition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param qualifiedInterval
         *     Qualified range for continuous and ordinal observation results
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder qualifiedInterval(Collection<QualifiedInterval> qualifiedInterval) {
            this.qualifiedInterval = new ArrayList<>(qualifiedInterval);
            return this;
        }

        /**
         * The set of valid coded results for the observations conforming to this ObservationDefinition.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link ValueSet}</li>
         * </ul>
         * 
         * @param validCodedValueSet
         *     Value set of valid coded values for the observations conforming to this ObservationDefinition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder validCodedValueSet(Reference validCodedValueSet) {
            this.validCodedValueSet = validCodedValueSet;
            return this;
        }

        /**
         * The set of normal coded results for the observations conforming to this ObservationDefinition.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link ValueSet}</li>
         * </ul>
         * 
         * @param normalCodedValueSet
         *     Value set of normal coded values for the observations conforming to this ObservationDefinition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder normalCodedValueSet(Reference normalCodedValueSet) {
            this.normalCodedValueSet = normalCodedValueSet;
            return this;
        }

        /**
         * The set of abnormal coded results for the observation conforming to this ObservationDefinition.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link ValueSet}</li>
         * </ul>
         * 
         * @param abnormalCodedValueSet
         *     Value set of abnormal coded values for the observations conforming to this ObservationDefinition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder abnormalCodedValueSet(Reference abnormalCodedValueSet) {
            this.abnormalCodedValueSet = abnormalCodedValueSet;
            return this;
        }

        /**
         * The set of critical coded results for the observation conforming to this ObservationDefinition.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link ValueSet}</li>
         * </ul>
         * 
         * @param criticalCodedValueSet
         *     Value set of critical coded values for the observations conforming to this ObservationDefinition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder criticalCodedValueSet(Reference criticalCodedValueSet) {
            this.criticalCodedValueSet = criticalCodedValueSet;
            return this;
        }

        /**
         * Build the {@link ObservationDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>code</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ObservationDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ObservationDefinition per the base specification
         */
        @Override
        public ObservationDefinition build() {
            ObservationDefinition observationDefinition = new ObservationDefinition(this);
            if (validating) {
                validate(observationDefinition);
            }
            return observationDefinition;
        }

        protected void validate(ObservationDefinition observationDefinition) {
            super.validate(observationDefinition);
            ValidationSupport.checkList(observationDefinition.category, "category", CodeableConcept.class);
            ValidationSupport.requireNonNull(observationDefinition.code, "code");
            ValidationSupport.checkList(observationDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(observationDefinition.permittedDataType, "permittedDataType", ObservationDataType.class);
            ValidationSupport.checkList(observationDefinition.qualifiedInterval, "qualifiedInterval", QualifiedInterval.class);
            ValidationSupport.checkReferenceType(observationDefinition.validCodedValueSet, "validCodedValueSet", "ValueSet");
            ValidationSupport.checkReferenceType(observationDefinition.normalCodedValueSet, "normalCodedValueSet", "ValueSet");
            ValidationSupport.checkReferenceType(observationDefinition.abnormalCodedValueSet, "abnormalCodedValueSet", "ValueSet");
            ValidationSupport.checkReferenceType(observationDefinition.criticalCodedValueSet, "criticalCodedValueSet", "ValueSet");
        }

        protected Builder from(ObservationDefinition observationDefinition) {
            super.from(observationDefinition);
            category.addAll(observationDefinition.category);
            code = observationDefinition.code;
            identifier.addAll(observationDefinition.identifier);
            permittedDataType.addAll(observationDefinition.permittedDataType);
            multipleResultsAllowed = observationDefinition.multipleResultsAllowed;
            method = observationDefinition.method;
            preferredReportName = observationDefinition.preferredReportName;
            quantitativeDetails = observationDefinition.quantitativeDetails;
            qualifiedInterval.addAll(observationDefinition.qualifiedInterval);
            validCodedValueSet = observationDefinition.validCodedValueSet;
            normalCodedValueSet = observationDefinition.normalCodedValueSet;
            abnormalCodedValueSet = observationDefinition.abnormalCodedValueSet;
            criticalCodedValueSet = observationDefinition.criticalCodedValueSet;
            return this;
        }
    }

    /**
     * Characteristics for quantitative results of this observation.
     */
    public static class QuantitativeDetails extends BackboneElement {
        @Binding(
            bindingName = "ObservationUnit",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Codes identifying units of measure.",
            valueSet = "http://hl7.org/fhir/ValueSet/ucum-units"
        )
        private final CodeableConcept customaryUnit;
        @Binding(
            bindingName = "ObservationUnit",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Codes identifying units of measure.",
            valueSet = "http://hl7.org/fhir/ValueSet/ucum-units"
        )
        private final CodeableConcept unit;
        private final Decimal conversionFactor;
        private final Integer decimalPrecision;

        private QuantitativeDetails(Builder builder) {
            super(builder);
            customaryUnit = builder.customaryUnit;
            unit = builder.unit;
            conversionFactor = builder.conversionFactor;
            decimalPrecision = builder.decimalPrecision;
        }

        /**
         * Customary unit used to report quantitative results of observations conforming to this ObservationDefinition.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCustomaryUnit() {
            return customaryUnit;
        }

        /**
         * SI unit used to report quantitative results of observations conforming to this ObservationDefinition.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getUnit() {
            return unit;
        }

        /**
         * Factor for converting value expressed with SI unit to value expressed with customary unit.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getConversionFactor() {
            return conversionFactor;
        }

        /**
         * Number of digits after decimal separator when the results of such observations are of type Quantity.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getDecimalPrecision() {
            return decimalPrecision;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (customaryUnit != null) || 
                (unit != null) || 
                (conversionFactor != null) || 
                (decimalPrecision != null);
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(customaryUnit, "customaryUnit", visitor);
                    accept(unit, "unit", visitor);
                    accept(conversionFactor, "conversionFactor", visitor);
                    accept(decimalPrecision, "decimalPrecision", visitor);
                }
                visitor.visitEnd(elementName, elementIndex, this);
                visitor.postVisit(this);
            }
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
            QuantitativeDetails other = (QuantitativeDetails) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(customaryUnit, other.customaryUnit) && 
                Objects.equals(unit, other.unit) && 
                Objects.equals(conversionFactor, other.conversionFactor) && 
                Objects.equals(decimalPrecision, other.decimalPrecision);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    customaryUnit, 
                    unit, 
                    conversionFactor, 
                    decimalPrecision);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept customaryUnit;
            private CodeableConcept unit;
            private Decimal conversionFactor;
            private Integer decimalPrecision;

            private Builder() {
                super();
            }

            /**
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder extension(Extension... extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder extension(Collection<Extension> extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Customary unit used to report quantitative results of observations conforming to this ObservationDefinition.
             * 
             * @param customaryUnit
             *     Customary unit for quantitative results
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder customaryUnit(CodeableConcept customaryUnit) {
                this.customaryUnit = customaryUnit;
                return this;
            }

            /**
             * SI unit used to report quantitative results of observations conforming to this ObservationDefinition.
             * 
             * @param unit
             *     SI unit for quantitative results
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder unit(CodeableConcept unit) {
                this.unit = unit;
                return this;
            }

            /**
             * Factor for converting value expressed with SI unit to value expressed with customary unit.
             * 
             * @param conversionFactor
             *     SI to Customary unit conversion factor
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder conversionFactor(Decimal conversionFactor) {
                this.conversionFactor = conversionFactor;
                return this;
            }

            /**
             * Convenience method for setting {@code decimalPrecision}.
             * 
             * @param decimalPrecision
             *     Decimal precision of observation quantitative results
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #decimalPrecision(com.ibm.fhir.model.type.Integer)
             */
            public Builder decimalPrecision(java.lang.Integer decimalPrecision) {
                this.decimalPrecision = (decimalPrecision == null) ? null : Integer.of(decimalPrecision);
                return this;
            }

            /**
             * Number of digits after decimal separator when the results of such observations are of type Quantity.
             * 
             * @param decimalPrecision
             *     Decimal precision of observation quantitative results
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder decimalPrecision(Integer decimalPrecision) {
                this.decimalPrecision = decimalPrecision;
                return this;
            }

            /**
             * Build the {@link QuantitativeDetails}
             * 
             * @return
             *     An immutable object of type {@link QuantitativeDetails}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid QuantitativeDetails per the base specification
             */
            @Override
            public QuantitativeDetails build() {
                QuantitativeDetails quantitativeDetails = new QuantitativeDetails(this);
                if (validating) {
                    validate(quantitativeDetails);
                }
                return quantitativeDetails;
            }

            protected void validate(QuantitativeDetails quantitativeDetails) {
                super.validate(quantitativeDetails);
                ValidationSupport.requireValueOrChildren(quantitativeDetails);
            }

            protected Builder from(QuantitativeDetails quantitativeDetails) {
                super.from(quantitativeDetails);
                customaryUnit = quantitativeDetails.customaryUnit;
                unit = quantitativeDetails.unit;
                conversionFactor = quantitativeDetails.conversionFactor;
                decimalPrecision = quantitativeDetails.decimalPrecision;
                return this;
            }
        }
    }

    /**
     * Multiple ranges of results qualified by different contexts for ordinal or continuous observations conforming to this 
     * ObservationDefinition.
     */
    public static class QualifiedInterval extends BackboneElement {
        @Binding(
            bindingName = "ObservationRangeCategory",
            strength = BindingStrength.Value.REQUIRED,
            description = "Codes identifying the category of observation range.",
            valueSet = "http://hl7.org/fhir/ValueSet/observation-range-category|4.3.0"
        )
        private final ObservationRangeCategory category;
        private final Range range;
        @Binding(
            bindingName = "ObservationRangeMeaning",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Code identifying the health context of a range.",
            valueSet = "http://hl7.org/fhir/ValueSet/referencerange-meaning"
        )
        private final CodeableConcept context;
        @Binding(
            bindingName = "ObservationRangeAppliesTo",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes identifying the population the reference range applies to.",
            valueSet = "http://hl7.org/fhir/ValueSet/referencerange-appliesto"
        )
        private final List<CodeableConcept> appliesTo;
        @Binding(
            bindingName = "AdministrativeGender",
            strength = BindingStrength.Value.REQUIRED,
            description = "The gender of a person used for administrative purposes.",
            valueSet = "http://hl7.org/fhir/ValueSet/administrative-gender|4.3.0"
        )
        private final AdministrativeGender gender;
        private final Range age;
        private final Range gestationalAge;
        private final String condition;

        private QualifiedInterval(Builder builder) {
            super(builder);
            category = builder.category;
            range = builder.range;
            context = builder.context;
            appliesTo = Collections.unmodifiableList(builder.appliesTo);
            gender = builder.gender;
            age = builder.age;
            gestationalAge = builder.gestationalAge;
            condition = builder.condition;
        }

        /**
         * The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.
         * 
         * @return
         *     An immutable object of type {@link ObservationRangeCategory} that may be null.
         */
        public ObservationRangeCategory getCategory() {
            return category;
        }

        /**
         * The low and high values determining the interval. There may be only one of the two.
         * 
         * @return
         *     An immutable object of type {@link Range} that may be null.
         */
        public Range getRange() {
            return range;
        }

        /**
         * Codes to indicate the health context the range applies to. For example, the normal or therapeutic range.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getContext() {
            return context;
        }

        /**
         * Codes to indicate the target population this reference range applies to.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getAppliesTo() {
            return appliesTo;
        }

        /**
         * Sex of the population the range applies to.
         * 
         * @return
         *     An immutable object of type {@link AdministrativeGender} that may be null.
         */
        public AdministrativeGender getGender() {
            return gender;
        }

        /**
         * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the 
         * meaning says so.
         * 
         * @return
         *     An immutable object of type {@link Range} that may be null.
         */
        public Range getAge() {
            return age;
        }

        /**
         * The gestational age to which this reference range is applicable, in the context of pregnancy.
         * 
         * @return
         *     An immutable object of type {@link Range} that may be null.
         */
        public Range getGestationalAge() {
            return gestationalAge;
        }

        /**
         * Text based condition for which the reference range is valid.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getCondition() {
            return condition;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (category != null) || 
                (range != null) || 
                (context != null) || 
                !appliesTo.isEmpty() || 
                (gender != null) || 
                (age != null) || 
                (gestationalAge != null) || 
                (condition != null);
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(category, "category", visitor);
                    accept(range, "range", visitor);
                    accept(context, "context", visitor);
                    accept(appliesTo, "appliesTo", visitor, CodeableConcept.class);
                    accept(gender, "gender", visitor);
                    accept(age, "age", visitor);
                    accept(gestationalAge, "gestationalAge", visitor);
                    accept(condition, "condition", visitor);
                }
                visitor.visitEnd(elementName, elementIndex, this);
                visitor.postVisit(this);
            }
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
            QualifiedInterval other = (QualifiedInterval) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(category, other.category) && 
                Objects.equals(range, other.range) && 
                Objects.equals(context, other.context) && 
                Objects.equals(appliesTo, other.appliesTo) && 
                Objects.equals(gender, other.gender) && 
                Objects.equals(age, other.age) && 
                Objects.equals(gestationalAge, other.gestationalAge) && 
                Objects.equals(condition, other.condition);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    category, 
                    range, 
                    context, 
                    appliesTo, 
                    gender, 
                    age, 
                    gestationalAge, 
                    condition);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private ObservationRangeCategory category;
            private Range range;
            private CodeableConcept context;
            private List<CodeableConcept> appliesTo = new ArrayList<>();
            private AdministrativeGender gender;
            private Range age;
            private Range gestationalAge;
            private String condition;

            private Builder() {
                super();
            }

            /**
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder extension(Extension... extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder extension(Collection<Extension> extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.
             * 
             * @param category
             *     reference | critical | absolute
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder category(ObservationRangeCategory category) {
                this.category = category;
                return this;
            }

            /**
             * The low and high values determining the interval. There may be only one of the two.
             * 
             * @param range
             *     The interval itself, for continuous or ordinal observations
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder range(Range range) {
                this.range = range;
                return this;
            }

            /**
             * Codes to indicate the health context the range applies to. For example, the normal or therapeutic range.
             * 
             * @param context
             *     Range context qualifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder context(CodeableConcept context) {
                this.context = context;
                return this;
            }

            /**
             * Codes to indicate the target population this reference range applies to.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param appliesTo
             *     Targetted population of the range
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder appliesTo(CodeableConcept... appliesTo) {
                for (CodeableConcept value : appliesTo) {
                    this.appliesTo.add(value);
                }
                return this;
            }

            /**
             * Codes to indicate the target population this reference range applies to.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param appliesTo
             *     Targetted population of the range
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder appliesTo(Collection<CodeableConcept> appliesTo) {
                this.appliesTo = new ArrayList<>(appliesTo);
                return this;
            }

            /**
             * Sex of the population the range applies to.
             * 
             * @param gender
             *     male | female | other | unknown
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder gender(AdministrativeGender gender) {
                this.gender = gender;
                return this;
            }

            /**
             * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the 
             * meaning says so.
             * 
             * @param age
             *     Applicable age range, if relevant
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder age(Range age) {
                this.age = age;
                return this;
            }

            /**
             * The gestational age to which this reference range is applicable, in the context of pregnancy.
             * 
             * @param gestationalAge
             *     Applicable gestational age range, if relevant
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder gestationalAge(Range gestationalAge) {
                this.gestationalAge = gestationalAge;
                return this;
            }

            /**
             * Convenience method for setting {@code condition}.
             * 
             * @param condition
             *     Condition associated with the reference range
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #condition(com.ibm.fhir.model.type.String)
             */
            public Builder condition(java.lang.String condition) {
                this.condition = (condition == null) ? null : String.of(condition);
                return this;
            }

            /**
             * Text based condition for which the reference range is valid.
             * 
             * @param condition
             *     Condition associated with the reference range
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder condition(String condition) {
                this.condition = condition;
                return this;
            }

            /**
             * Build the {@link QualifiedInterval}
             * 
             * @return
             *     An immutable object of type {@link QualifiedInterval}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid QualifiedInterval per the base specification
             */
            @Override
            public QualifiedInterval build() {
                QualifiedInterval qualifiedInterval = new QualifiedInterval(this);
                if (validating) {
                    validate(qualifiedInterval);
                }
                return qualifiedInterval;
            }

            protected void validate(QualifiedInterval qualifiedInterval) {
                super.validate(qualifiedInterval);
                ValidationSupport.checkList(qualifiedInterval.appliesTo, "appliesTo", CodeableConcept.class);
                ValidationSupport.requireValueOrChildren(qualifiedInterval);
            }

            protected Builder from(QualifiedInterval qualifiedInterval) {
                super.from(qualifiedInterval);
                category = qualifiedInterval.category;
                range = qualifiedInterval.range;
                context = qualifiedInterval.context;
                appliesTo.addAll(qualifiedInterval.appliesTo);
                gender = qualifiedInterval.gender;
                age = qualifiedInterval.age;
                gestationalAge = qualifiedInterval.gestationalAge;
                condition = qualifiedInterval.condition;
                return this;
            }
        }
    }
}
