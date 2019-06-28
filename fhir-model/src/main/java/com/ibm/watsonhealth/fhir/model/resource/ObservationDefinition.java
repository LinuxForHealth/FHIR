/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.AdministrativeGender;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.ObservationDataType;
import com.ibm.watsonhealth.fhir.model.type.ObservationRangeCategory;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable 
 * health care service.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ObservationDefinition extends DomainResource {
    private final List<CodeableConcept> category;
    private final CodeableConcept code;
    private final List<Identifier> identifier;
    private final List<ObservationDataType> permittedDataType;
    private final Boolean multipleResultsAllowed;
    private final CodeableConcept method;
    private final String preferredReportName;
    private final QuantitativeDetails quantitativeDetails;
    private final List<QualifiedInterval> qualifiedInterval;
    private final Reference validCodedValueSet;
    private final Reference normalCodedValueSet;
    private final Reference abnormalCodedValueSet;
    private final Reference criticalCodedValueSet;

    private ObservationDefinition(Builder builder) {
        super(builder);
        category = Collections.unmodifiableList(builder.category);
        code = ValidationSupport.requireNonNull(builder.code, "code");
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
     * <p>
     * A code that classifies the general type of observation.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * Describes what will be observed. Sometimes this is called the observation "name".
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * <p>
     * A unique identifier assigned to this ObservationDefinition artifact.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ObservationDataType}.
     */
    public List<ObservationDataType> getPermittedDataType() {
        return permittedDataType;
    }

    /**
     * <p>
     * Multiple results allowed for observations conforming to this ObservationDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getMultipleResultsAllowed() {
        return multipleResultsAllowed;
    }

    /**
     * <p>
     * The method or technique used to perform the observation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getMethod() {
        return method;
    }

    /**
     * <p>
     * The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPreferredReportName() {
        return preferredReportName;
    }

    /**
     * <p>
     * Characteristics for quantitative results of this observation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link QuantitativeDetails}.
     */
    public QuantitativeDetails getQuantitativeDetails() {
        return quantitativeDetails;
    }

    /**
     * <p>
     * Multiple ranges of results qualified by different contexts for ordinal or continuous observations conforming to this 
     * ObservationDefinition.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link QualifiedInterval}.
     */
    public List<QualifiedInterval> getQualifiedInterval() {
        return qualifiedInterval;
    }

    /**
     * <p>
     * The set of valid coded results for the observations conforming to this ObservationDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getValidCodedValueSet() {
        return validCodedValueSet;
    }

    /**
     * <p>
     * The set of normal coded results for the observations conforming to this ObservationDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getNormalCodedValueSet() {
        return normalCodedValueSet;
    }

    /**
     * <p>
     * The set of abnormal coded results for the observation conforming to this ObservationDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAbnormalCodedValueSet() {
        return abnormalCodedValueSet;
    }

    /**
     * <p>
     * The set of critical coded results for the observation conforming to this ObservationDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getCriticalCodedValueSet() {
        return criticalCodedValueSet;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(code);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.category.addAll(category);
        builder.identifier.addAll(identifier);
        builder.permittedDataType.addAll(permittedDataType);
        builder.multipleResultsAllowed = multipleResultsAllowed;
        builder.method = method;
        builder.preferredReportName = preferredReportName;
        builder.quantitativeDetails = quantitativeDetails;
        builder.qualifiedInterval.addAll(qualifiedInterval);
        builder.validCodedValueSet = validCodedValueSet;
        builder.normalCodedValueSet = normalCodedValueSet;
        builder.abnormalCodedValueSet = abnormalCodedValueSet;
        builder.criticalCodedValueSet = criticalCodedValueSet;
        return builder;
    }

    public static Builder builder(CodeableConcept code) {
        return new Builder(code);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final CodeableConcept code;

        // optional
        private List<CodeableConcept> category = new ArrayList<>();
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

        private Builder(CodeableConcept code) {
            super();
            this.code = code;
        }

        /**
         * <p>
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * </p>
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder id(Id id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * </p>
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * <p>
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * </p>
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * <p>
         * The base language in which the resource is written.
         * </p>
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * <p>
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * </p>
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * A code that classifies the general type of observation.
         * </p>
         * 
         * @param category
         *     Category of observation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A code that classifies the general type of observation.
         * </p>
         * 
         * @param category
         *     Category of observation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category.addAll(category);
            return this;
        }

        /**
         * <p>
         * A unique identifier assigned to this ObservationDefinition artifact.
         * </p>
         * 
         * @param identifier
         *     Business identifier for this ObservationDefinition instance
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A unique identifier assigned to this ObservationDefinition artifact.
         * </p>
         * 
         * @param identifier
         *     Business identifier for this ObservationDefinition instance
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
            return this;
        }

        /**
         * <p>
         * The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.
         * </p>
         * 
         * @param permittedDataType
         *     Quantity | CodeableConcept | string | boolean | integer | Range | Ratio | SampledData | time | dateTime | Period
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder permittedDataType(ObservationDataType... permittedDataType) {
            for (ObservationDataType value : permittedDataType) {
                this.permittedDataType.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.
         * </p>
         * 
         * @param permittedDataType
         *     Quantity | CodeableConcept | string | boolean | integer | Range | Ratio | SampledData | time | dateTime | Period
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder permittedDataType(Collection<ObservationDataType> permittedDataType) {
            this.permittedDataType.addAll(permittedDataType);
            return this;
        }

        /**
         * <p>
         * Multiple results allowed for observations conforming to this ObservationDefinition.
         * </p>
         * 
         * @param multipleResultsAllowed
         *     Multiple results allowed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder multipleResultsAllowed(Boolean multipleResultsAllowed) {
            this.multipleResultsAllowed = multipleResultsAllowed;
            return this;
        }

        /**
         * <p>
         * The method or technique used to perform the observation.
         * </p>
         * 
         * @param method
         *     Method used to produce the observation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder method(CodeableConcept method) {
            this.method = method;
            return this;
        }

        /**
         * <p>
         * The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.
         * </p>
         * 
         * @param preferredReportName
         *     Preferred report name
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder preferredReportName(String preferredReportName) {
            this.preferredReportName = preferredReportName;
            return this;
        }

        /**
         * <p>
         * Characteristics for quantitative results of this observation.
         * </p>
         * 
         * @param quantitativeDetails
         *     Characteristics of quantitative results
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder quantitativeDetails(QuantitativeDetails quantitativeDetails) {
            this.quantitativeDetails = quantitativeDetails;
            return this;
        }

        /**
         * <p>
         * Multiple ranges of results qualified by different contexts for ordinal or continuous observations conforming to this 
         * ObservationDefinition.
         * </p>
         * 
         * @param qualifiedInterval
         *     Qualified range for continuous and ordinal observation results
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder qualifiedInterval(QualifiedInterval... qualifiedInterval) {
            for (QualifiedInterval value : qualifiedInterval) {
                this.qualifiedInterval.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Multiple ranges of results qualified by different contexts for ordinal or continuous observations conforming to this 
         * ObservationDefinition.
         * </p>
         * 
         * @param qualifiedInterval
         *     Qualified range for continuous and ordinal observation results
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder qualifiedInterval(Collection<QualifiedInterval> qualifiedInterval) {
            this.qualifiedInterval.addAll(qualifiedInterval);
            return this;
        }

        /**
         * <p>
         * The set of valid coded results for the observations conforming to this ObservationDefinition.
         * </p>
         * 
         * @param validCodedValueSet
         *     Value set of valid coded values for the observations conforming to this ObservationDefinition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validCodedValueSet(Reference validCodedValueSet) {
            this.validCodedValueSet = validCodedValueSet;
            return this;
        }

        /**
         * <p>
         * The set of normal coded results for the observations conforming to this ObservationDefinition.
         * </p>
         * 
         * @param normalCodedValueSet
         *     Value set of normal coded values for the observations conforming to this ObservationDefinition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder normalCodedValueSet(Reference normalCodedValueSet) {
            this.normalCodedValueSet = normalCodedValueSet;
            return this;
        }

        /**
         * <p>
         * The set of abnormal coded results for the observation conforming to this ObservationDefinition.
         * </p>
         * 
         * @param abnormalCodedValueSet
         *     Value set of abnormal coded values for the observations conforming to this ObservationDefinition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder abnormalCodedValueSet(Reference abnormalCodedValueSet) {
            this.abnormalCodedValueSet = abnormalCodedValueSet;
            return this;
        }

        /**
         * <p>
         * The set of critical coded results for the observation conforming to this ObservationDefinition.
         * </p>
         * 
         * @param criticalCodedValueSet
         *     Value set of critical coded values for the observations conforming to this ObservationDefinition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder criticalCodedValueSet(Reference criticalCodedValueSet) {
            this.criticalCodedValueSet = criticalCodedValueSet;
            return this;
        }

        @Override
        public ObservationDefinition build() {
            return new ObservationDefinition(this);
        }
    }

    /**
     * <p>
     * Characteristics for quantitative results of this observation.
     * </p>
     */
    public static class QuantitativeDetails extends BackboneElement {
        private final CodeableConcept customaryUnit;
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
         * <p>
         * Customary unit used to report quantitative results of observations conforming to this ObservationDefinition.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCustomaryUnit() {
            return customaryUnit;
        }

        /**
         * <p>
         * SI unit used to report quantitative results of observations conforming to this ObservationDefinition.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getUnit() {
            return unit;
        }

        /**
         * <p>
         * Factor for converting value expressed with SI unit to value expressed with customary unit.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getConversionFactor() {
            return conversionFactor;
        }

        /**
         * <p>
         * Number of digits after decimal separator when the results of such observations are of type Quantity.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getDecimalPrecision() {
            return decimalPrecision;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(customaryUnit, "customaryUnit", visitor);
                    accept(unit, "unit", visitor);
                    accept(conversionFactor, "conversionFactor", visitor);
                    accept(decimalPrecision, "decimalPrecision", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private CodeableConcept customaryUnit;
            private CodeableConcept unit;
            private Decimal conversionFactor;
            private Integer decimalPrecision;

            private Builder() {
                super();
            }

            /**
             * <p>
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * </p>
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder extension(Extension... extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder extension(Collection<Extension> extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Customary unit used to report quantitative results of observations conforming to this ObservationDefinition.
             * </p>
             * 
             * @param customaryUnit
             *     Customary unit for quantitative results
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder customaryUnit(CodeableConcept customaryUnit) {
                this.customaryUnit = customaryUnit;
                return this;
            }

            /**
             * <p>
             * SI unit used to report quantitative results of observations conforming to this ObservationDefinition.
             * </p>
             * 
             * @param unit
             *     SI unit for quantitative results
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder unit(CodeableConcept unit) {
                this.unit = unit;
                return this;
            }

            /**
             * <p>
             * Factor for converting value expressed with SI unit to value expressed with customary unit.
             * </p>
             * 
             * @param conversionFactor
             *     SI to Customary unit conversion factor
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder conversionFactor(Decimal conversionFactor) {
                this.conversionFactor = conversionFactor;
                return this;
            }

            /**
             * <p>
             * Number of digits after decimal separator when the results of such observations are of type Quantity.
             * </p>
             * 
             * @param decimalPrecision
             *     Decimal precision of observation quantitative results
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder decimalPrecision(Integer decimalPrecision) {
                this.decimalPrecision = decimalPrecision;
                return this;
            }

            @Override
            public QuantitativeDetails build() {
                return new QuantitativeDetails(this);
            }

            private static Builder from(QuantitativeDetails quantitativeDetails) {
                Builder builder = new Builder();
                builder.customaryUnit = quantitativeDetails.customaryUnit;
                builder.unit = quantitativeDetails.unit;
                builder.conversionFactor = quantitativeDetails.conversionFactor;
                builder.decimalPrecision = quantitativeDetails.decimalPrecision;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Multiple ranges of results qualified by different contexts for ordinal or continuous observations conforming to this 
     * ObservationDefinition.
     * </p>
     */
    public static class QualifiedInterval extends BackboneElement {
        private final ObservationRangeCategory category;
        private final Range range;
        private final CodeableConcept context;
        private final List<CodeableConcept> appliesTo;
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
         * <p>
         * The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ObservationRangeCategory}.
         */
        public ObservationRangeCategory getCategory() {
            return category;
        }

        /**
         * <p>
         * The low and high values determining the interval. There may be only one of the two.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Range}.
         */
        public Range getRange() {
            return range;
        }

        /**
         * <p>
         * Codes to indicate the health context the range applies to. For example, the normal or therapeutic range.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getContext() {
            return context;
        }

        /**
         * <p>
         * Codes to indicate the target population this reference range applies to.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getAppliesTo() {
            return appliesTo;
        }

        /**
         * <p>
         * Sex of the population the range applies to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link AdministrativeGender}.
         */
        public AdministrativeGender getGender() {
            return gender;
        }

        /**
         * <p>
         * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the 
         * meaning says so.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Range}.
         */
        public Range getAge() {
            return age;
        }

        /**
         * <p>
         * The gestational age to which this reference range is applicable, in the context of pregnancy.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Range}.
         */
        public Range getGestationalAge() {
            return gestationalAge;
        }

        /**
         * <p>
         * Text based condition for which the reference range is valid.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getCondition() {
            return condition;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
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
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
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
             * <p>
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * </p>
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder extension(Extension... extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder extension(Collection<Extension> extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.
             * </p>
             * 
             * @param category
             *     reference | critical | absolute
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder category(ObservationRangeCategory category) {
                this.category = category;
                return this;
            }

            /**
             * <p>
             * The low and high values determining the interval. There may be only one of the two.
             * </p>
             * 
             * @param range
             *     The interval itself, for continuous or ordinal observations
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder range(Range range) {
                this.range = range;
                return this;
            }

            /**
             * <p>
             * Codes to indicate the health context the range applies to. For example, the normal or therapeutic range.
             * </p>
             * 
             * @param context
             *     Range context qualifier
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder context(CodeableConcept context) {
                this.context = context;
                return this;
            }

            /**
             * <p>
             * Codes to indicate the target population this reference range applies to.
             * </p>
             * 
             * @param appliesTo
             *     Targetted population of the range
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder appliesTo(CodeableConcept... appliesTo) {
                for (CodeableConcept value : appliesTo) {
                    this.appliesTo.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Codes to indicate the target population this reference range applies to.
             * </p>
             * 
             * @param appliesTo
             *     Targetted population of the range
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder appliesTo(Collection<CodeableConcept> appliesTo) {
                this.appliesTo.addAll(appliesTo);
                return this;
            }

            /**
             * <p>
             * Sex of the population the range applies to.
             * </p>
             * 
             * @param gender
             *     male | female | other | unknown
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder gender(AdministrativeGender gender) {
                this.gender = gender;
                return this;
            }

            /**
             * <p>
             * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the 
             * meaning says so.
             * </p>
             * 
             * @param age
             *     Applicable age range, if relevant
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder age(Range age) {
                this.age = age;
                return this;
            }

            /**
             * <p>
             * The gestational age to which this reference range is applicable, in the context of pregnancy.
             * </p>
             * 
             * @param gestationalAge
             *     Applicable gestational age range, if relevant
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder gestationalAge(Range gestationalAge) {
                this.gestationalAge = gestationalAge;
                return this;
            }

            /**
             * <p>
             * Text based condition for which the reference range is valid.
             * </p>
             * 
             * @param condition
             *     Condition associated with the reference range
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder condition(String condition) {
                this.condition = condition;
                return this;
            }

            @Override
            public QualifiedInterval build() {
                return new QualifiedInterval(this);
            }

            private static Builder from(QualifiedInterval qualifiedInterval) {
                Builder builder = new Builder();
                builder.category = qualifiedInterval.category;
                builder.range = qualifiedInterval.range;
                builder.context = qualifiedInterval.context;
                builder.appliesTo.addAll(qualifiedInterval.appliesTo);
                builder.gender = qualifiedInterval.gender;
                builder.age = qualifiedInterval.age;
                builder.gestationalAge = qualifiedInterval.gestationalAge;
                builder.condition = qualifiedInterval.condition;
                return builder;
            }
        }
    }
}
