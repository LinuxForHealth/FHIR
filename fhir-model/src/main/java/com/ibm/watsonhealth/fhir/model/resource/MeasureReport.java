/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.MeasureReportStatus;
import com.ibm.watsonhealth.fhir.model.type.MeasureReportType;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the 
 * resources involved in that calculation.
 * </p>
 */
@Constraint(
    id = "mrp-1",
    level = "Rule",
    location = "(base)",
    description = "Measure Reports used for data collection SHALL NOT communicate group and score information",
    expression = "(type != 'data-collection') or group.exists().not()"
)
@Constraint(
    id = "mrp-2",
    level = "Rule",
    location = "(base)",
    description = "Stratifiers SHALL be either a single criteria or a set of criteria components",
    expression = "group.stratifier.stratum.all(value.exists() xor component.exists())"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MeasureReport extends DomainResource {
    private final List<Identifier> identifier;
    private final MeasureReportStatus status;
    private final MeasureReportType type;
    private final Canonical measure;
    private final Reference subject;
    private final DateTime date;
    private final Reference reporter;
    private final Period period;
    private final CodeableConcept improvementNotation;
    private final List<Group> group;
    private final List<Reference> evaluatedResource;

    private MeasureReport(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.type = ValidationSupport.requireNonNull(builder.type, "type");
        this.measure = ValidationSupport.requireNonNull(builder.measure, "measure");
        this.subject = builder.subject;
        this.date = builder.date;
        this.reporter = builder.reporter;
        this.period = ValidationSupport.requireNonNull(builder.period, "period");
        this.improvementNotation = builder.improvementNotation;
        this.group = builder.group;
        this.evaluatedResource = builder.evaluatedResource;
    }

    /**
     * <p>
     * A formal identifier that is used to identify this MeasureReport when it is represented in other formats or referenced 
     * in a specification, model, design or an instance.
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
     * The MeasureReport status. No data will be available until the MeasureReport status is complete.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MeasureReportStatus}.
     */
    public MeasureReportStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The type of measure report. This may be an individual report, which provides the score for the measure for an 
     * individual member of the population; a subject-listing, which returns the list of members that meet the various 
     * criteria in the measure; a summary report, which returns a population count for each of the criteria in the measure; 
     * or a data-collection, which enables the MeasureReport to be used to exchange the data-of-interest for a quality 
     * measure.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MeasureReportType}.
     */
    public MeasureReportType getType() {
        return type;
    }

    /**
     * <p>
     * A reference to the Measure that was calculated to produce this report.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getMeasure() {
        return measure;
    }

    /**
     * <p>
     * Optional subject identifying the individual or individuals the report is for.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * <p>
     * The date this measure report was generated.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * <p>
     * The individual, location, or organization that is reporting the data.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getReporter() {
        return reporter;
    }

    /**
     * <p>
     * The reporting period for which the report was calculated.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * <p>
     * Whether improvement in the measure is noted by an increase or decrease in the measure score.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getImprovementNotation() {
        return improvementNotation;
    }

    /**
     * <p>
     * The results of the calculation, one for each population group in the measure.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Group}.
     */
    public List<Group> getGroup() {
        return group;
    }

    /**
     * <p>
     * A reference to a Bundle containing the Resources that were used in the calculation of this measure.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getEvaluatedResource() {
        return evaluatedResource;
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
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(status, "status", visitor);
                accept(type, "type", visitor);
                accept(measure, "measure", visitor);
                accept(subject, "subject", visitor);
                accept(date, "date", visitor);
                accept(reporter, "reporter", visitor);
                accept(period, "period", visitor);
                accept(improvementNotation, "improvementNotation", visitor);
                accept(group, "group", visitor, Group.class);
                accept(evaluatedResource, "evaluatedResource", visitor, Reference.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, type, measure, period);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.subject = subject;
        builder.date = date;
        builder.reporter = reporter;
        builder.improvementNotation = improvementNotation;
        builder.group.addAll(group);
        builder.evaluatedResource.addAll(evaluatedResource);
        return builder;
    }

    public static Builder builder(MeasureReportStatus status, MeasureReportType type, Canonical measure, Period period) {
        return new Builder(status, type, measure, period);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final MeasureReportStatus status;
        private final MeasureReportType type;
        private final Canonical measure;
        private final Period period;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Reference subject;
        private DateTime date;
        private Reference reporter;
        private CodeableConcept improvementNotation;
        private List<Group> group = new ArrayList<>();
        private List<Reference> evaluatedResource = new ArrayList<>();

        private Builder(MeasureReportStatus status, MeasureReportType type, Canonical measure, Period period) {
            super();
            this.status = status;
            this.type = type;
            this.measure = measure;
            this.period = period;
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
         * A formal identifier that is used to identify this MeasureReport when it is represented in other formats or referenced 
         * in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the MeasureReport
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
         * A formal identifier that is used to identify this MeasureReport when it is represented in other formats or referenced 
         * in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the MeasureReport
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
         * Optional subject identifying the individual or individuals the report is for.
         * </p>
         * 
         * @param subject
         *     What individual(s) the report is for
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * The date this measure report was generated.
         * </p>
         * 
         * @param date
         *     When the report was generated
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * <p>
         * The individual, location, or organization that is reporting the data.
         * </p>
         * 
         * @param reporter
         *     Who is reporting the data
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reporter(Reference reporter) {
            this.reporter = reporter;
            return this;
        }

        /**
         * <p>
         * Whether improvement in the measure is noted by an increase or decrease in the measure score.
         * </p>
         * 
         * @param improvementNotation
         *     increase | decrease
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder improvementNotation(CodeableConcept improvementNotation) {
            this.improvementNotation = improvementNotation;
            return this;
        }

        /**
         * <p>
         * The results of the calculation, one for each population group in the measure.
         * </p>
         * 
         * @param group
         *     Measure results for each group
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder group(Group... group) {
            for (Group value : group) {
                this.group.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The results of the calculation, one for each population group in the measure.
         * </p>
         * 
         * @param group
         *     Measure results for each group
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder group(Collection<Group> group) {
            this.group.addAll(group);
            return this;
        }

        /**
         * <p>
         * A reference to a Bundle containing the Resources that were used in the calculation of this measure.
         * </p>
         * 
         * @param evaluatedResource
         *     What data was used to calculate the measure score
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder evaluatedResource(Reference... evaluatedResource) {
            for (Reference value : evaluatedResource) {
                this.evaluatedResource.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A reference to a Bundle containing the Resources that were used in the calculation of this measure.
         * </p>
         * 
         * @param evaluatedResource
         *     What data was used to calculate the measure score
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder evaluatedResource(Collection<Reference> evaluatedResource) {
            this.evaluatedResource.addAll(evaluatedResource);
            return this;
        }

        @Override
        public MeasureReport build() {
            return new MeasureReport(this);
        }
    }

    /**
     * <p>
     * The results of the calculation, one for each population group in the measure.
     * </p>
     */
    public static class Group extends BackboneElement {
        private final CodeableConcept code;
        private final List<Population> population;
        private final Quantity measureScore;
        private final List<Stratifier> stratifier;

        private Group(Builder builder) {
            super(builder);
            this.code = builder.code;
            this.population = builder.population;
            this.measureScore = builder.measureScore;
            this.stratifier = builder.stratifier;
        }

        /**
         * <p>
         * The meaning of the population group as defined in the measure definition.
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
         * The populations that make up the population group, one for each type of population appropriate for the measure.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Population}.
         */
        public List<Population> getPopulation() {
            return population;
        }

        /**
         * <p>
         * The measure score for this population group, calculated as appropriate for the measure type and scoring method, and 
         * based on the contents of the populations defined in the group.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getMeasureScore() {
            return measureScore;
        }

        /**
         * <p>
         * When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the 
         * measure.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Stratifier}.
         */
        public List<Stratifier> getStratifier() {
            return stratifier;
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
                    accept(code, "code", visitor);
                    accept(population, "population", visitor, Population.class);
                    accept(measureScore, "measureScore", visitor);
                    accept(stratifier, "stratifier", visitor, Stratifier.class);
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
            private CodeableConcept code;
            private List<Population> population = new ArrayList<>();
            private Quantity measureScore;
            private List<Stratifier> stratifier = new ArrayList<>();

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
             * The meaning of the population group as defined in the measure definition.
             * </p>
             * 
             * @param code
             *     Meaning of the group
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * <p>
             * The populations that make up the population group, one for each type of population appropriate for the measure.
             * </p>
             * 
             * @param population
             *     The populations in the group
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder population(Population... population) {
                for (Population value : population) {
                    this.population.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The populations that make up the population group, one for each type of population appropriate for the measure.
             * </p>
             * 
             * @param population
             *     The populations in the group
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder population(Collection<Population> population) {
                this.population.addAll(population);
                return this;
            }

            /**
             * <p>
             * The measure score for this population group, calculated as appropriate for the measure type and scoring method, and 
             * based on the contents of the populations defined in the group.
             * </p>
             * 
             * @param measureScore
             *     What score this group achieved
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder measureScore(Quantity measureScore) {
                this.measureScore = measureScore;
                return this;
            }

            /**
             * <p>
             * When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the 
             * measure.
             * </p>
             * 
             * @param stratifier
             *     Stratification results
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder stratifier(Stratifier... stratifier) {
                for (Stratifier value : stratifier) {
                    this.stratifier.add(value);
                }
                return this;
            }

            /**
             * <p>
             * When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the 
             * measure.
             * </p>
             * 
             * @param stratifier
             *     Stratification results
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder stratifier(Collection<Stratifier> stratifier) {
                this.stratifier.addAll(stratifier);
                return this;
            }

            @Override
            public Group build() {
                return new Group(this);
            }

            private static Builder from(Group group) {
                Builder builder = new Builder();
                builder.id = group.id;
                builder.extension.addAll(group.extension);
                builder.modifierExtension.addAll(group.modifierExtension);
                builder.code = group.code;
                builder.population.addAll(group.population);
                builder.measureScore = group.measureScore;
                builder.stratifier.addAll(group.stratifier);
                return builder;
            }
        }

        /**
         * <p>
         * The populations that make up the population group, one for each type of population appropriate for the measure.
         * </p>
         */
        public static class Population extends BackboneElement {
            private final CodeableConcept code;
            private final Integer count;
            private final Reference subjectResults;

            private Population(Builder builder) {
                super(builder);
                this.code = builder.code;
                this.count = builder.count;
                this.subjectResults = builder.subjectResults;
            }

            /**
             * <p>
             * The type of the population.
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
             * The number of members of the population.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Integer}.
             */
            public Integer getCount() {
                return count;
            }

            /**
             * <p>
             * This element refers to a List of subject level MeasureReport resources, one for each subject in this population.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getSubjectResults() {
                return subjectResults;
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
                        accept(code, "code", visitor);
                        accept(count, "count", visitor);
                        accept(subjectResults, "subjectResults", visitor);
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
                private CodeableConcept code;
                private Integer count;
                private Reference subjectResults;

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
                 * The type of the population.
                 * </p>
                 * 
                 * @param code
                 *     initial-population | numerator | numerator-exclusion | denominator | denominator-exclusion | denominator-exception | 
                 *     measure-population | measure-population-exclusion | measure-observation
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * <p>
                 * The number of members of the population.
                 * </p>
                 * 
                 * @param count
                 *     Size of the population
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder count(Integer count) {
                    this.count = count;
                    return this;
                }

                /**
                 * <p>
                 * This element refers to a List of subject level MeasureReport resources, one for each subject in this population.
                 * </p>
                 * 
                 * @param subjectResults
                 *     For subject-list reports, the subject results in this population
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder subjectResults(Reference subjectResults) {
                    this.subjectResults = subjectResults;
                    return this;
                }

                @Override
                public Population build() {
                    return new Population(this);
                }

                private static Builder from(Population population) {
                    Builder builder = new Builder();
                    builder.id = population.id;
                    builder.extension.addAll(population.extension);
                    builder.modifierExtension.addAll(population.modifierExtension);
                    builder.code = population.code;
                    builder.count = population.count;
                    builder.subjectResults = population.subjectResults;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * When a measure includes multiple stratifiers, there will be a stratifier group for each stratifier defined by the 
         * measure.
         * </p>
         */
        public static class Stratifier extends BackboneElement {
            private final List<CodeableConcept> code;
            private final List<Stratum> stratum;

            private Stratifier(Builder builder) {
                super(builder);
                this.code = builder.code;
                this.stratum = builder.stratum;
            }

            /**
             * <p>
             * The meaning of this stratifier, as defined in the measure definition.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getCode() {
                return code;
            }

            /**
             * <p>
             * This element contains the results for a single stratum within the stratifier. For example, when stratifying on 
             * administrative gender, there will be four strata, one for each possible gender value.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Stratum}.
             */
            public List<Stratum> getStratum() {
                return stratum;
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
                        accept(code, "code", visitor, CodeableConcept.class);
                        accept(stratum, "stratum", visitor, Stratum.class);
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
                private List<CodeableConcept> code = new ArrayList<>();
                private List<Stratum> stratum = new ArrayList<>();

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
                 * The meaning of this stratifier, as defined in the measure definition.
                 * </p>
                 * 
                 * @param code
                 *     What stratifier of the group
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder code(CodeableConcept... code) {
                    for (CodeableConcept value : code) {
                        this.code.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The meaning of this stratifier, as defined in the measure definition.
                 * </p>
                 * 
                 * @param code
                 *     What stratifier of the group
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder code(Collection<CodeableConcept> code) {
                    this.code.addAll(code);
                    return this;
                }

                /**
                 * <p>
                 * This element contains the results for a single stratum within the stratifier. For example, when stratifying on 
                 * administrative gender, there will be four strata, one for each possible gender value.
                 * </p>
                 * 
                 * @param stratum
                 *     Stratum results, one for each unique value, or set of values, in the stratifier, or stratifier components
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder stratum(Stratum... stratum) {
                    for (Stratum value : stratum) {
                        this.stratum.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * This element contains the results for a single stratum within the stratifier. For example, when stratifying on 
                 * administrative gender, there will be four strata, one for each possible gender value.
                 * </p>
                 * 
                 * @param stratum
                 *     Stratum results, one for each unique value, or set of values, in the stratifier, or stratifier components
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder stratum(Collection<Stratum> stratum) {
                    this.stratum.addAll(stratum);
                    return this;
                }

                @Override
                public Stratifier build() {
                    return new Stratifier(this);
                }

                private static Builder from(Stratifier stratifier) {
                    Builder builder = new Builder();
                    builder.id = stratifier.id;
                    builder.extension.addAll(stratifier.extension);
                    builder.modifierExtension.addAll(stratifier.modifierExtension);
                    builder.code.addAll(stratifier.code);
                    builder.stratum.addAll(stratifier.stratum);
                    return builder;
                }
            }

            /**
             * <p>
             * This element contains the results for a single stratum within the stratifier. For example, when stratifying on 
             * administrative gender, there will be four strata, one for each possible gender value.
             * </p>
             */
            public static class Stratum extends BackboneElement {
                private final CodeableConcept value;
                private final List<Component> component;
                private final List<Population> population;
                private final Quantity measureScore;

                private Stratum(Builder builder) {
                    super(builder);
                    this.value = builder.value;
                    this.component = builder.component;
                    this.population = builder.population;
                    this.measureScore = builder.measureScore;
                }

                /**
                 * <p>
                 * The value for this stratum, expressed as a CodeableConcept. When defining stratifiers on complex values, the value 
                 * must be rendered such that the value for each stratum within the stratifier is unique.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept}.
                 */
                public CodeableConcept getValue() {
                    return value;
                }

                /**
                 * <p>
                 * A stratifier component value.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link Component}.
                 */
                public List<Component> getComponent() {
                    return component;
                }

                /**
                 * <p>
                 * The populations that make up the stratum, one for each type of population appropriate to the measure.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link Population}.
                 */
                public List<Population> getPopulation() {
                    return population;
                }

                /**
                 * <p>
                 * The measure score for this stratum, calculated as appropriate for the measure type and scoring method, and based on 
                 * only the members of this stratum.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Quantity}.
                 */
                public Quantity getMeasureScore() {
                    return measureScore;
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
                            accept(value, "value", visitor);
                            accept(component, "component", visitor, Component.class);
                            accept(population, "population", visitor, Population.class);
                            accept(measureScore, "measureScore", visitor);
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
                    private CodeableConcept value;
                    private List<Component> component = new ArrayList<>();
                    private List<Population> population = new ArrayList<>();
                    private Quantity measureScore;

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
                     * The value for this stratum, expressed as a CodeableConcept. When defining stratifiers on complex values, the value 
                     * must be rendered such that the value for each stratum within the stratifier is unique.
                     * </p>
                     * 
                     * @param value
                     *     The stratum value, e.g. male
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder value(CodeableConcept value) {
                        this.value = value;
                        return this;
                    }

                    /**
                     * <p>
                     * A stratifier component value.
                     * </p>
                     * 
                     * @param component
                     *     Stratifier component values
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder component(Component... component) {
                        for (Component value : component) {
                            this.component.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * A stratifier component value.
                     * </p>
                     * 
                     * @param component
                     *     Stratifier component values
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder component(Collection<Component> component) {
                        this.component.addAll(component);
                        return this;
                    }

                    /**
                     * <p>
                     * The populations that make up the stratum, one for each type of population appropriate to the measure.
                     * </p>
                     * 
                     * @param population
                     *     Population results in this stratum
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder population(Population... population) {
                        for (Population value : population) {
                            this.population.add(value);
                        }
                        return this;
                    }

                    /**
                     * <p>
                     * The populations that make up the stratum, one for each type of population appropriate to the measure.
                     * </p>
                     * 
                     * @param population
                     *     Population results in this stratum
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder population(Collection<Population> population) {
                        this.population.addAll(population);
                        return this;
                    }

                    /**
                     * <p>
                     * The measure score for this stratum, calculated as appropriate for the measure type and scoring method, and based on 
                     * only the members of this stratum.
                     * </p>
                     * 
                     * @param measureScore
                     *     What score this stratum achieved
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder measureScore(Quantity measureScore) {
                        this.measureScore = measureScore;
                        return this;
                    }

                    @Override
                    public Stratum build() {
                        return new Stratum(this);
                    }

                    private static Builder from(Stratum stratum) {
                        Builder builder = new Builder();
                        builder.id = stratum.id;
                        builder.extension.addAll(stratum.extension);
                        builder.modifierExtension.addAll(stratum.modifierExtension);
                        builder.value = stratum.value;
                        builder.component.addAll(stratum.component);
                        builder.population.addAll(stratum.population);
                        builder.measureScore = stratum.measureScore;
                        return builder;
                    }
                }

                /**
                 * <p>
                 * A stratifier component value.
                 * </p>
                 */
                public static class Component extends BackboneElement {
                    private final CodeableConcept code;
                    private final CodeableConcept value;

                    private Component(Builder builder) {
                        super(builder);
                        this.code = ValidationSupport.requireNonNull(builder.code, "code");
                        this.value = ValidationSupport.requireNonNull(builder.value, "value");
                    }

                    /**
                     * <p>
                     * The code for the stratum component value.
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
                     * The stratum component value.
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link CodeableConcept}.
                     */
                    public CodeableConcept getValue() {
                        return value;
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
                                accept(code, "code", visitor);
                                accept(value, "value", visitor);
                            }
                            visitor.visitEnd(elementName, this);
                            visitor.postVisit(this);
                        }
                    }

                    @Override
                    public Builder toBuilder() {
                        return Builder.from(this);
                    }

                    public static Builder builder(CodeableConcept code, CodeableConcept value) {
                        return new Builder(code, value);
                    }

                    public static class Builder extends BackboneElement.Builder {
                        // required
                        private final CodeableConcept code;
                        private final CodeableConcept value;

                        private Builder(CodeableConcept code, CodeableConcept value) {
                            super();
                            this.code = code;
                            this.value = value;
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

                        @Override
                        public Component build() {
                            return new Component(this);
                        }

                        private static Builder from(Component component) {
                            Builder builder = new Builder(component.code, component.value);
                            builder.id = component.id;
                            builder.extension.addAll(component.extension);
                            builder.modifierExtension.addAll(component.modifierExtension);
                            return builder;
                        }
                    }
                }

                /**
                 * <p>
                 * The populations that make up the stratum, one for each type of population appropriate to the measure.
                 * </p>
                 */
                public static class Population extends BackboneElement {
                    private final CodeableConcept code;
                    private final Integer count;
                    private final Reference subjectResults;

                    private Population(Builder builder) {
                        super(builder);
                        this.code = builder.code;
                        this.count = builder.count;
                        this.subjectResults = builder.subjectResults;
                    }

                    /**
                     * <p>
                     * The type of the population.
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
                     * The number of members of the population in this stratum.
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link Integer}.
                     */
                    public Integer getCount() {
                        return count;
                    }

                    /**
                     * <p>
                     * This element refers to a List of subject level MeasureReport resources, one for each subject in this population in 
                     * this stratum.
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link Reference}.
                     */
                    public Reference getSubjectResults() {
                        return subjectResults;
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
                                accept(code, "code", visitor);
                                accept(count, "count", visitor);
                                accept(subjectResults, "subjectResults", visitor);
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
                        private CodeableConcept code;
                        private Integer count;
                        private Reference subjectResults;

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
                         * The type of the population.
                         * </p>
                         * 
                         * @param code
                         *     initial-population | numerator | numerator-exclusion | denominator | denominator-exclusion | denominator-exception | 
                         *     measure-population | measure-population-exclusion | measure-observation
                         * 
                         * @return
                         *     A reference to this Builder instance.
                         */
                        public Builder code(CodeableConcept code) {
                            this.code = code;
                            return this;
                        }

                        /**
                         * <p>
                         * The number of members of the population in this stratum.
                         * </p>
                         * 
                         * @param count
                         *     Size of the population
                         * 
                         * @return
                         *     A reference to this Builder instance.
                         */
                        public Builder count(Integer count) {
                            this.count = count;
                            return this;
                        }

                        /**
                         * <p>
                         * This element refers to a List of subject level MeasureReport resources, one for each subject in this population in 
                         * this stratum.
                         * </p>
                         * 
                         * @param subjectResults
                         *     For subject-list reports, the subject results in this population
                         * 
                         * @return
                         *     A reference to this Builder instance.
                         */
                        public Builder subjectResults(Reference subjectResults) {
                            this.subjectResults = subjectResults;
                            return this;
                        }

                        @Override
                        public Population build() {
                            return new Population(this);
                        }

                        private static Builder from(Population population) {
                            Builder builder = new Builder();
                            builder.id = population.id;
                            builder.extension.addAll(population.extension);
                            builder.modifierExtension.addAll(population.modifierExtension);
                            builder.code = population.code;
                            builder.count = population.count;
                            builder.subjectResults = population.subjectResults;
                            return builder;
                        }
                    }
                }
            }
        }
    }
}
