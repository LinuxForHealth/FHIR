/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.code.AggregationMode;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ConstraintSeverity;
import com.ibm.fhir.model.type.code.DiscriminatorType;
import com.ibm.fhir.model.type.code.PropertyRepresentation;
import com.ibm.fhir.model.type.code.ReferenceVersionRules;
import com.ibm.fhir.model.type.code.SlicingRules;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Captures constraints on each element within the resource, profile, or extension.
 */
@Constraint(
    id = "eld-1",
    level = "Rule",
    location = "ElementDefinition.slicing",
    description = "If there are no discriminators, there must be a definition",
    expression = "discriminator.exists() or description.exists()"
)
@Constraint(
    id = "eld-2",
    level = "Rule",
    location = "(base)",
    description = "Min <= Max",
    expression = "min.empty() or max.empty() or (max = '*') or iif(max != '*', min <= max.toInteger())"
)
@Constraint(
    id = "eld-3",
    level = "Rule",
    location = "ElementDefinition.max",
    description = "Max SHALL be a number or \"*\"",
    expression = "empty() or ($this = '*') or (toInteger() >= 0)"
)
@Constraint(
    id = "eld-4",
    level = "Rule",
    location = "ElementDefinition.type",
    description = "Aggregation may only be specified if one of the allowed types for the element is a reference",
    expression = "aggregation.empty() or (code = 'Reference') or (code = 'canonical')"
)
@Constraint(
    id = "eld-5",
    level = "Rule",
    location = "(base)",
    description = "if the element definition has a contentReference, it cannot have type, defaultValue, fixed, pattern, example, minValue, maxValue, maxLength, or binding",
    expression = "contentReference.empty() or (type.empty() and defaultValue.empty() and fixed.empty() and pattern.empty() and example.empty() and minValue.empty() and maxValue.empty() and maxLength.empty() and binding.empty())"
)
@Constraint(
    id = "eld-6",
    level = "Rule",
    location = "(base)",
    description = "Fixed value may only be specified if there is one type",
    expression = "fixed.empty() or (type.count()  <= 1)"
)
@Constraint(
    id = "eld-7",
    level = "Rule",
    location = "(base)",
    description = "Pattern may only be specified if there is one type",
    expression = "pattern.empty() or (type.count() <= 1)"
)
@Constraint(
    id = "eld-8",
    level = "Rule",
    location = "(base)",
    description = "Pattern and fixed are mutually exclusive",
    expression = "pattern.empty() or fixed.empty()"
)
@Constraint(
    id = "eld-11",
    level = "Rule",
    location = "(base)",
    description = "Binding can only be present for coded elements, string, and uri",
    expression = "binding.empty() or type.code.empty() or type.select((code = 'code') or (code = 'Coding') or (code='CodeableConcept') or (code = 'Quantity') or (code = 'string') or (code = 'uri')).exists()"
)
@Constraint(
    id = "eld-12",
    level = "Rule",
    location = "ElementDefinition.binding",
    description = "ValueSet SHALL start with http:// or https:// or urn:",
    expression = "valueSet.exists() implies (valueSet.startsWith('http:') or valueSet.startsWith('https') or valueSet.startsWith('urn:'))"
)
@Constraint(
    id = "eld-13",
    level = "Rule",
    location = "(base)",
    description = "Types must be unique by code",
    expression = "type.select(code).isDistinct()"
)
@Constraint(
    id = "eld-14",
    level = "Rule",
    location = "(base)",
    description = "Constraints must be unique by key",
    expression = "constraint.select(key).isDistinct()"
)
@Constraint(
    id = "eld-15",
    level = "Rule",
    location = "(base)",
    description = "default value and meaningWhenMissing are mutually exclusive",
    expression = "defaultValue.empty() or meaningWhenMissing.empty()"
)
@Constraint(
    id = "eld-16",
    level = "Rule",
    location = "(base)",
    description = "sliceName must be composed of proper tokens separated by \"/\"",
    expression = "sliceName.empty() or sliceName.matches('^[a-zA-Z0-9\\/\\-_\\[\\]\\@]+$')"
)
@Constraint(
    id = "eld-17",
    level = "Rule",
    location = "ElementDefinition.type",
    description = "targetProfile is only allowed if the type is Reference or canonical",
    expression = "(code='Reference' or code = 'canonical') or targetProfile.empty()"
)
@Constraint(
    id = "eld-18",
    level = "Rule",
    location = "(base)",
    description = "Must have a modifier reason if isModifier = true",
    expression = "(isModifier.exists() and isModifier) implies isModifierReason.exists()"
)
@Constraint(
    id = "eld-19",
    level = "Rule",
    location = "(base)",
    description = "Element names cannot include some special characters",
    expression = "path.matches('[^\\s\\.,:;\\\'\"\\/|?!@#$%&*()\\[\\]{}]{1,64}(\\.[^\\s\\.,:;\\\'\"\\/|?!@#$%&*()\\[\\]{}]{1,64}(\\[x\\])?(\\:[^\\s\\.]+)?)*')"
)
@Constraint(
    id = "eld-20",
    level = "Warning",
    location = "(base)",
    description = "Element names should be simple alphanumerics with a max of 64 characters, or code generation tools may be broken",
    expression = "path.matches('[A-Za-z][A-Za-z0-9]*(\\.[a-z][A-Za-z0-9]*(\\[x])?)*')"
)
@Constraint(
    id = "eld-21",
    level = "Warning",
    location = "ElementDefinition.constraint",
    description = "Constraints should have an expression or else validators will not be able to enforce them",
    expression = "expression.exists()"
)
@Constraint(
    id = "eld-22",
    level = "Rule",
    location = "(base)",
    description = "sliceIsConstraining can only appear if slicename is present",
    expression = "sliceIsConstraining.exists() implies sliceName.exists()"
)
@Constraint(
    id = "elementDefinition-23",
    level = "Warning",
    location = "type.code",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/defined-types",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/defined-types', 'extensible')",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ElementDefinition extends BackboneElement {
    @Summary
    @Required
    private final String path;
    @Summary
    @com.ibm.fhir.model.annotation.Binding(
        bindingName = "PropertyRepresentation",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "How a property is represented when serialized.",
        valueSet = "http://hl7.org/fhir/ValueSet/property-representation|4.0.1"
    )
    private final List<PropertyRepresentation> representation;
    @Summary
    private final String sliceName;
    @Summary
    private final Boolean sliceIsConstraining;
    @Summary
    private final String label;
    @Summary
    @com.ibm.fhir.model.annotation.Binding(
        bindingName = "ElementDefinitionCode",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "Codes that indicate the meaning of a data element.",
        valueSet = "http://hl7.org/fhir/ValueSet/observation-codes"
    )
    private final List<Coding> code;
    @Summary
    private final Slicing slicing;
    @Summary
    private final String _short;
    @Summary
    private final Markdown definition;
    @Summary
    private final Markdown comment;
    @Summary
    private final Markdown requirements;
    @Summary
    private final List<String> alias;
    @Summary
    private final UnsignedInt min;
    @Summary
    private final String max;
    @Summary
    private final Base base;
    @Summary
    private final Uri contentReference;
    @Summary
    private final List<Type> type;
    @Summary
    @Choice({ Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class })
    private final Element defaultValue;
    @Summary
    private final Markdown meaningWhenMissing;
    @Summary
    private final String orderMeaning;
    @Summary
    @Choice({ Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class })
    private final Element fixed;
    @Summary
    @Choice({ Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class })
    private final Element pattern;
    @Summary
    private final List<Example> example;
    @Summary
    @Choice({ Date.class, DateTime.class, Instant.class, Time.class, Decimal.class, Integer.class, PositiveInt.class, UnsignedInt.class, Quantity.class })
    private final Element minValue;
    @Summary
    @Choice({ Date.class, DateTime.class, Instant.class, Time.class, Decimal.class, Integer.class, PositiveInt.class, UnsignedInt.class, Quantity.class })
    private final Element maxValue;
    @Summary
    private final Integer maxLength;
    @Summary
    private final List<Id> condition;
    @Summary
    private final List<Constraint> constraint;
    @Summary
    private final Boolean mustSupport;
    @Summary
    private final Boolean isModifier;
    @Summary
    private final String isModifierReason;
    @Summary
    private final Boolean isSummary;
    @Summary
    private final Binding binding;
    @Summary
    private final List<Mapping> mapping;

    private volatile int hashCode;

    private ElementDefinition(Builder builder) {
        super(builder);
        path = ValidationSupport.requireNonNull(builder.path, "path");
        representation = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.representation, "representation"));
        sliceName = builder.sliceName;
        sliceIsConstraining = builder.sliceIsConstraining;
        label = builder.label;
        code = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.code, "code"));
        slicing = builder.slicing;
        _short = builder._short;
        definition = builder.definition;
        comment = builder.comment;
        requirements = builder.requirements;
        alias = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.alias, "alias"));
        min = builder.min;
        max = builder.max;
        base = builder.base;
        contentReference = builder.contentReference;
        type = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.type, "type"));
        defaultValue = ValidationSupport.choiceElement(builder.defaultValue, "defaultValue", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class);
        meaningWhenMissing = builder.meaningWhenMissing;
        orderMeaning = builder.orderMeaning;
        fixed = ValidationSupport.choiceElement(builder.fixed, "fixed", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class);
        pattern = ValidationSupport.choiceElement(builder.pattern, "pattern", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class);
        example = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.example, "example"));
        minValue = ValidationSupport.choiceElement(builder.minValue, "minValue", Date.class, DateTime.class, Instant.class, Time.class, Decimal.class, Integer.class, PositiveInt.class, UnsignedInt.class, Quantity.class);
        maxValue = ValidationSupport.choiceElement(builder.maxValue, "maxValue", Date.class, DateTime.class, Instant.class, Time.class, Decimal.class, Integer.class, PositiveInt.class, UnsignedInt.class, Quantity.class);
        maxLength = builder.maxLength;
        condition = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.condition, "condition"));
        constraint = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.constraint, "constraint"));
        mustSupport = builder.mustSupport;
        isModifier = builder.isModifier;
        isModifierReason = builder.isModifierReason;
        isSummary = builder.isSummary;
        binding = builder.binding;
        mapping = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.mapping, "mapping"));
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name 
     * of the resource or extension.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getPath() {
        return path;
    }

    /**
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link PropertyRepresentation} that may be empty.
     */
    public List<PropertyRepresentation> getRepresentation() {
        return representation;
    }

    /**
     * The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. 
     * This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to 
     * different slices of the same element.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getSliceName() {
        return sliceName;
    }

    /**
     * If true, indicates that this slice definition is constraining a slice definition with the same name in an inherited 
     * profile. If false, the slice is not overriding any slice in an inherited profile. If missing, the slice might or might 
     * not be overriding a slice in an inherited profile, depending on the sliceName.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getSliceIsConstraining() {
        return sliceIsConstraining;
    }

    /**
     * A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt 
     * for the element in a user display or form.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getLabel() {
        return label;
    }

    /**
     * A code that has the same meaning as the element in a particular terminology.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
     */
    public List<Coding> getCode() {
        return code;
    }

    /**
     * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are 
     * multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has 
     * cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that 
     * come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path 
     * terminates the set).
     * 
     * @return
     *     An immutable object of type {@link Slicing} that may be null.
     */
    public Slicing getSlicing() {
        return slicing;
    }

    /**
     * A concise description of what this element means (e.g. for use in autogenerated summaries).
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getShort() {
        return _short;
    }

    /**
     * Provides a complete explanation of the meaning of the data element for human readability. For the case of elements 
     * derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but 
     * convey the meaning of the element in the particular context of use of the resource. (Note: The text you are reading is 
     * specified in ElementDefinition.definition).
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDefinition() {
        return definition;
    }

    /**
     * Explanatory notes and implementation guidance about the data element, including notes about how to use the data 
     * properly, exceptions to proper use, etc. (Note: The text you are reading is specified in ElementDefinition.comment).
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getComment() {
        return comment;
    }

    /**
     * This element is for traceability of why the element was created and why the constraints exist as they do. This may be 
     * used to point to source materials or specifications that drove the structure of this element.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getRequirements() {
        return requirements;
    }

    /**
     * Identifies additional names by which this element might also be known.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getAlias() {
        return alias;
    }

    /**
     * The minimum number of times this element SHALL appear in the instance.
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt} that may be null.
     */
    public UnsignedInt getMin() {
        return min;
    }

    /**
     * The maximum number of times this element is permitted to appear in the instance.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getMax() {
        return max;
    }

    /**
     * Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation 
     * of the element through the derived and related profiles. When the element definition is not the original definition of 
     * an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot - then the 
     * information in provided in the element definition may be different to the base definition. On the original definition 
     * of the element, it will be same.
     * 
     * @return
     *     An immutable object of type {@link Base} that may be null.
     */
    public Base getBase() {
        return base;
    }

    /**
     * Identifies an element defined elsewhere in the definition whose content rules should be applied to the current 
     * element. ContentReferences bring across all the rules that are in the ElementDefinition for the element, including 
     * definitions, cardinality constraints, bindings, invariants etc.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getContentReference() {
        return contentReference;
    }

    /**
     * The data type or resource that the value of this element is permitted to be.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Type} that may be empty.
     */
    public List<Type> getType() {
        return type;
    }

    /**
     * The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the 
     * abstract is false').
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getDefaultValue() {
        return defaultValue;
    }

    /**
     * The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the 
     * period is ongoing').
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getMeaningWhenMissing() {
        return meaningWhenMissing;
    }

    /**
     * If present, indicates that the order of the repeating element has meaning and describes what that meaning is. If 
     * absent, it means that the order of the element has no meaning.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getOrderMeaning() {
        return orderMeaning;
    }

    /**
     * Specifies a value that SHALL be exactly the value for this element in the instance. For purposes of comparison, non-
     * significant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing 
     * elements/attributes must also be missing.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getFixed() {
        return fixed;
    }

    /**
     * Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the 
     * instance. Other additional values may be found too. This is effectively constraint by example. 
     * 
     * <p>When pattern[x] is used to constrain a primitive, it means that the value provided in the pattern[x] must match the 
     * instance value exactly.
     * 
     * <p>When pattern[x] is used to constrain an array, it means that each element provided in the pattern[x] array must 
     * (recursively) match at least one element from the instance array.
     * 
     * <p>When pattern[x] is used to constrain a complex object, it means that each property in the pattern must be present 
     * in the complex object, and its value must recursively match -- i.e.,
     * 
     * <p>1. If primitive: it must match exactly the pattern value
     * <p>2. If a complex object: it must match (recursively) the pattern value
     * <p>3. If an array: it must match (recursively) the pattern value.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getPattern() {
        return pattern;
    }

    /**
     * A sample value for this element demonstrating the type of information that would typically be found in the element.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Example} that may be empty.
     */
    public List<Example> getExample() {
        return example;
    }

    /**
     * The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, 
     * instant, time, decimal, integer, and Quantity.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getMinValue() {
        return minValue;
    }

    /**
     * The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, 
     * instant, time, decimal, integer, and Quantity.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getMaxValue() {
        return maxValue;
    }

    /**
     * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is 
     * expected to be supported by conformant consumers that support the element.
     * 
     * @return
     *     An immutable object of type {@link Integer} that may be null.
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Id} that may be empty.
     */
    public List<Id> getCondition() {
        return condition;
    }

    /**
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the 
     * context of the instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Constraint} that may be empty.
     */
    public List<Constraint> getConstraint() {
        return constraint;
    }

    /**
     * If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful 
     * way. If false, the element may be ignored and not supported. If false, whether to populate or use the data element in 
     * any way is at the discretion of the implementation.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getMustSupport() {
        return mustSupport;
    }

    /**
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the 
     * value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The 
     * effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process 
     * it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getIsModifier() {
        return isModifier;
    }

    /**
     * Explains how that element affects the interpretation of the resource or element that contains it.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getIsModifierReason() {
        return isModifierReason;
    }

    /**
     * Whether the element should be included if a client requests a search with the parameter _summary=true.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getIsSummary() {
        return isSummary;
    }

    /**
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, 
     * uri).
     * 
     * @return
     *     An immutable object of type {@link Binding} that may be null.
     */
    public Binding getBinding() {
        return binding;
    }

    /**
     * Identifies a concept from an external specification that roughly corresponds to this element.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Mapping} that may be empty.
     */
    public List<Mapping> getMapping() {
        return mapping;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (path != null) || 
            !representation.isEmpty() || 
            (sliceName != null) || 
            (sliceIsConstraining != null) || 
            (label != null) || 
            !code.isEmpty() || 
            (slicing != null) || 
            (_short != null) || 
            (definition != null) || 
            (comment != null) || 
            (requirements != null) || 
            !alias.isEmpty() || 
            (min != null) || 
            (max != null) || 
            (base != null) || 
            (contentReference != null) || 
            !type.isEmpty() || 
            (defaultValue != null) || 
            (meaningWhenMissing != null) || 
            (orderMeaning != null) || 
            (fixed != null) || 
            (pattern != null) || 
            !example.isEmpty() || 
            (minValue != null) || 
            (maxValue != null) || 
            (maxLength != null) || 
            !condition.isEmpty() || 
            !constraint.isEmpty() || 
            (mustSupport != null) || 
            (isModifier != null) || 
            (isModifierReason != null) || 
            (isSummary != null) || 
            (binding != null) || 
            !mapping.isEmpty();
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
                accept(path, "path", visitor);
                accept(representation, "representation", visitor, PropertyRepresentation.class);
                accept(sliceName, "sliceName", visitor);
                accept(sliceIsConstraining, "sliceIsConstraining", visitor);
                accept(label, "label", visitor);
                accept(code, "code", visitor, Coding.class);
                accept(slicing, "slicing", visitor);
                accept(_short, "short", visitor);
                accept(definition, "definition", visitor);
                accept(comment, "comment", visitor);
                accept(requirements, "requirements", visitor);
                accept(alias, "alias", visitor, String.class);
                accept(min, "min", visitor);
                accept(max, "max", visitor);
                accept(base, "base", visitor);
                accept(contentReference, "contentReference", visitor);
                accept(type, "type", visitor, Type.class);
                accept(defaultValue, "defaultValue", visitor);
                accept(meaningWhenMissing, "meaningWhenMissing", visitor);
                accept(orderMeaning, "orderMeaning", visitor);
                accept(fixed, "fixed", visitor);
                accept(pattern, "pattern", visitor);
                accept(example, "example", visitor, Example.class);
                accept(minValue, "minValue", visitor);
                accept(maxValue, "maxValue", visitor);
                accept(maxLength, "maxLength", visitor);
                accept(condition, "condition", visitor, Id.class);
                accept(constraint, "constraint", visitor, Constraint.class);
                accept(mustSupport, "mustSupport", visitor);
                accept(isModifier, "isModifier", visitor);
                accept(isModifierReason, "isModifierReason", visitor);
                accept(isSummary, "isSummary", visitor);
                accept(binding, "binding", visitor);
                accept(mapping, "mapping", visitor, Mapping.class);
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
        ElementDefinition other = (ElementDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(path, other.path) && 
            Objects.equals(representation, other.representation) && 
            Objects.equals(sliceName, other.sliceName) && 
            Objects.equals(sliceIsConstraining, other.sliceIsConstraining) && 
            Objects.equals(label, other.label) && 
            Objects.equals(code, other.code) && 
            Objects.equals(slicing, other.slicing) && 
            Objects.equals(_short, other._short) && 
            Objects.equals(definition, other.definition) && 
            Objects.equals(comment, other.comment) && 
            Objects.equals(requirements, other.requirements) && 
            Objects.equals(alias, other.alias) && 
            Objects.equals(min, other.min) && 
            Objects.equals(max, other.max) && 
            Objects.equals(base, other.base) && 
            Objects.equals(contentReference, other.contentReference) && 
            Objects.equals(type, other.type) && 
            Objects.equals(defaultValue, other.defaultValue) && 
            Objects.equals(meaningWhenMissing, other.meaningWhenMissing) && 
            Objects.equals(orderMeaning, other.orderMeaning) && 
            Objects.equals(fixed, other.fixed) && 
            Objects.equals(pattern, other.pattern) && 
            Objects.equals(example, other.example) && 
            Objects.equals(minValue, other.minValue) && 
            Objects.equals(maxValue, other.maxValue) && 
            Objects.equals(maxLength, other.maxLength) && 
            Objects.equals(condition, other.condition) && 
            Objects.equals(constraint, other.constraint) && 
            Objects.equals(mustSupport, other.mustSupport) && 
            Objects.equals(isModifier, other.isModifier) && 
            Objects.equals(isModifierReason, other.isModifierReason) && 
            Objects.equals(isSummary, other.isSummary) && 
            Objects.equals(binding, other.binding) && 
            Objects.equals(mapping, other.mapping);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                modifierExtension, 
                path, 
                representation, 
                sliceName, 
                sliceIsConstraining, 
                label, 
                code, 
                slicing, 
                _short, 
                definition, 
                comment, 
                requirements, 
                alias, 
                min, 
                max, 
                base, 
                contentReference, 
                type, 
                defaultValue, 
                meaningWhenMissing, 
                orderMeaning, 
                fixed, 
                pattern, 
                example, 
                minValue, 
                maxValue, 
                maxLength, 
                condition, 
                constraint, 
                mustSupport, 
                isModifier, 
                isModifierReason, 
                isSummary, 
                binding, 
                mapping);
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
        private String path;
        private List<PropertyRepresentation> representation = new ArrayList<>();
        private String sliceName;
        private Boolean sliceIsConstraining;
        private String label;
        private List<Coding> code = new ArrayList<>();
        private Slicing slicing;
        private String _short;
        private Markdown definition;
        private Markdown comment;
        private Markdown requirements;
        private List<String> alias = new ArrayList<>();
        private UnsignedInt min;
        private String max;
        private Base base;
        private Uri contentReference;
        private List<Type> type = new ArrayList<>();
        private Element defaultValue;
        private Markdown meaningWhenMissing;
        private String orderMeaning;
        private Element fixed;
        private Element pattern;
        private List<Example> example = new ArrayList<>();
        private Element minValue;
        private Element maxValue;
        private Integer maxLength;
        private List<Id> condition = new ArrayList<>();
        private List<Constraint> constraint = new ArrayList<>();
        private Boolean mustSupport;
        private Boolean isModifier;
        private String isModifierReason;
        private Boolean isSummary;
        private Binding binding;
        private List<Mapping> mapping = new ArrayList<>();

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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored even if unrecognized
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name 
         * of the resource or extension.
         * 
         * <p>This element is required.
         * 
         * @param path
         *     Path of the element in the hierarchy of elements
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder path(String path) {
            this.path = path;
            return this;
        }

        /**
         * Codes that define how this element is represented in instances, when the deviation varies from the normal case.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param representation
         *     xmlAttr | xmlText | typeAttr | cdaText | xhtml
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder representation(PropertyRepresentation... representation) {
            for (PropertyRepresentation value : representation) {
                this.representation.add(value);
            }
            return this;
        }

        /**
         * Codes that define how this element is represented in instances, when the deviation varies from the normal case.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param representation
         *     xmlAttr | xmlText | typeAttr | cdaText | xhtml
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder representation(Collection<PropertyRepresentation> representation) {
            this.representation = new ArrayList<>(representation);
            return this;
        }

        /**
         * The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. 
         * This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to 
         * different slices of the same element.
         * 
         * @param sliceName
         *     Name for this particular element (in a set of slices)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sliceName(String sliceName) {
            this.sliceName = sliceName;
            return this;
        }

        /**
         * If true, indicates that this slice definition is constraining a slice definition with the same name in an inherited 
         * profile. If false, the slice is not overriding any slice in an inherited profile. If missing, the slice might or might 
         * not be overriding a slice in an inherited profile, depending on the sliceName.
         * 
         * @param sliceIsConstraining
         *     If this slice definition constrains an inherited slice definition (or not)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sliceIsConstraining(Boolean sliceIsConstraining) {
            this.sliceIsConstraining = sliceIsConstraining;
            return this;
        }

        /**
         * A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt 
         * for the element in a user display or form.
         * 
         * @param label
         *     Name for element to display with or prompt for element
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder label(String label) {
            this.label = label;
            return this;
        }

        /**
         * A code that has the same meaning as the element in a particular terminology.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param code
         *     Corresponding codes in terminologies
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Coding... code) {
            for (Coding value : code) {
                this.code.add(value);
            }
            return this;
        }

        /**
         * A code that has the same meaning as the element in a particular terminology.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param code
         *     Corresponding codes in terminologies
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Collection<Coding> code) {
            this.code = new ArrayList<>(code);
            return this;
        }

        /**
         * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are 
         * multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has 
         * cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that 
         * come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path 
         * terminates the set).
         * 
         * @param slicing
         *     This element is sliced - slices follow
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder slicing(Slicing slicing) {
            this.slicing = slicing;
            return this;
        }

        /**
         * A concise description of what this element means (e.g. for use in autogenerated summaries).
         * 
         * @param _short
         *     Concise definition for space-constrained presentation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder _short(String _short) {
            this._short = _short;
            return this;
        }

        /**
         * Provides a complete explanation of the meaning of the data element for human readability. For the case of elements 
         * derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but 
         * convey the meaning of the element in the particular context of use of the resource. (Note: The text you are reading is 
         * specified in ElementDefinition.definition).
         * 
         * @param definition
         *     Full formal definition as narrative text
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder definition(Markdown definition) {
            this.definition = definition;
            return this;
        }

        /**
         * Explanatory notes and implementation guidance about the data element, including notes about how to use the data 
         * properly, exceptions to proper use, etc. (Note: The text you are reading is specified in ElementDefinition.comment).
         * 
         * @param comment
         *     Comments about the use of this element
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comment(Markdown comment) {
            this.comment = comment;
            return this;
        }

        /**
         * This element is for traceability of why the element was created and why the constraints exist as they do. This may be 
         * used to point to source materials or specifications that drove the structure of this element.
         * 
         * @param requirements
         *     Why this resource has been created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder requirements(Markdown requirements) {
            this.requirements = requirements;
            return this;
        }

        /**
         * Identifies additional names by which this element might also be known.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param alias
         *     Other names
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder alias(String... alias) {
            for (String value : alias) {
                this.alias.add(value);
            }
            return this;
        }

        /**
         * Identifies additional names by which this element might also be known.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param alias
         *     Other names
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder alias(Collection<String> alias) {
            this.alias = new ArrayList<>(alias);
            return this;
        }

        /**
         * The minimum number of times this element SHALL appear in the instance.
         * 
         * @param min
         *     Minimum Cardinality
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder min(UnsignedInt min) {
            this.min = min;
            return this;
        }

        /**
         * The maximum number of times this element is permitted to appear in the instance.
         * 
         * @param max
         *     Maximum Cardinality (a number or *)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder max(String max) {
            this.max = max;
            return this;
        }

        /**
         * Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation 
         * of the element through the derived and related profiles. When the element definition is not the original definition of 
         * an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot - then the 
         * information in provided in the element definition may be different to the base definition. On the original definition 
         * of the element, it will be same.
         * 
         * @param base
         *     Base definition information for tools
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder base(Base base) {
            this.base = base;
            return this;
        }

        /**
         * Identifies an element defined elsewhere in the definition whose content rules should be applied to the current 
         * element. ContentReferences bring across all the rules that are in the ElementDefinition for the element, including 
         * definitions, cardinality constraints, bindings, invariants etc.
         * 
         * @param contentReference
         *     Reference to definition of content for the element
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contentReference(Uri contentReference) {
            this.contentReference = contentReference;
            return this;
        }

        /**
         * The data type or resource that the value of this element is permitted to be.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param type
         *     Data type and Profile for this element
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(Type... type) {
            for (Type value : type) {
                this.type.add(value);
            }
            return this;
        }

        /**
         * The data type or resource that the value of this element is permitted to be.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param type
         *     Data type and Profile for this element
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(Collection<Type> type) {
            this.type = new ArrayList<>(type);
            return this;
        }

        /**
         * The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the 
         * abstract is false').
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Base64Binary}</li>
         * <li>{@link Boolean}</li>
         * <li>{@link Canonical}</li>
         * <li>{@link Code}</li>
         * <li>{@link Date}</li>
         * <li>{@link DateTime}</li>
         * <li>{@link Decimal}</li>
         * <li>{@link Id}</li>
         * <li>{@link Instant}</li>
         * <li>{@link Integer}</li>
         * <li>{@link Markdown}</li>
         * <li>{@link Oid}</li>
         * <li>{@link PositiveInt}</li>
         * <li>{@link String}</li>
         * <li>{@link Time}</li>
         * <li>{@link UnsignedInt}</li>
         * <li>{@link Uri}</li>
         * <li>{@link Url}</li>
         * <li>{@link Uuid}</li>
         * <li>{@link Address}</li>
         * <li>{@link Age}</li>
         * <li>{@link Annotation}</li>
         * <li>{@link Attachment}</li>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Coding}</li>
         * <li>{@link ContactPoint}</li>
         * <li>{@link Count}</li>
         * <li>{@link Distance}</li>
         * <li>{@link Duration}</li>
         * <li>{@link HumanName}</li>
         * <li>{@link Identifier}</li>
         * <li>{@link Money}</li>
         * <li>{@link Period}</li>
         * <li>{@link Quantity}</li>
         * <li>{@link Range}</li>
         * <li>{@link Ratio}</li>
         * <li>{@link Reference}</li>
         * <li>{@link SampledData}</li>
         * <li>{@link Signature}</li>
         * <li>{@link Timing}</li>
         * <li>{@link ContactDetail}</li>
         * <li>{@link Contributor}</li>
         * <li>{@link DataRequirement}</li>
         * <li>{@link Expression}</li>
         * <li>{@link ParameterDefinition}</li>
         * <li>{@link RelatedArtifact}</li>
         * <li>{@link TriggerDefinition}</li>
         * <li>{@link UsageContext}</li>
         * <li>{@link Dosage}</li>
         * <li>{@link Meta}</li>
         * </ul>
         * 
         * @param defaultValue
         *     Specified value if missing from instance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder defaultValue(Element defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        /**
         * The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the 
         * period is ongoing').
         * 
         * @param meaningWhenMissing
         *     Implicit meaning when this element is missing
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder meaningWhenMissing(Markdown meaningWhenMissing) {
            this.meaningWhenMissing = meaningWhenMissing;
            return this;
        }

        /**
         * If present, indicates that the order of the repeating element has meaning and describes what that meaning is. If 
         * absent, it means that the order of the element has no meaning.
         * 
         * @param orderMeaning
         *     What the order of the elements means
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder orderMeaning(String orderMeaning) {
            this.orderMeaning = orderMeaning;
            return this;
        }

        /**
         * Specifies a value that SHALL be exactly the value for this element in the instance. For purposes of comparison, non-
         * significant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing 
         * elements/attributes must also be missing.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Base64Binary}</li>
         * <li>{@link Boolean}</li>
         * <li>{@link Canonical}</li>
         * <li>{@link Code}</li>
         * <li>{@link Date}</li>
         * <li>{@link DateTime}</li>
         * <li>{@link Decimal}</li>
         * <li>{@link Id}</li>
         * <li>{@link Instant}</li>
         * <li>{@link Integer}</li>
         * <li>{@link Markdown}</li>
         * <li>{@link Oid}</li>
         * <li>{@link PositiveInt}</li>
         * <li>{@link String}</li>
         * <li>{@link Time}</li>
         * <li>{@link UnsignedInt}</li>
         * <li>{@link Uri}</li>
         * <li>{@link Url}</li>
         * <li>{@link Uuid}</li>
         * <li>{@link Address}</li>
         * <li>{@link Age}</li>
         * <li>{@link Annotation}</li>
         * <li>{@link Attachment}</li>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Coding}</li>
         * <li>{@link ContactPoint}</li>
         * <li>{@link Count}</li>
         * <li>{@link Distance}</li>
         * <li>{@link Duration}</li>
         * <li>{@link HumanName}</li>
         * <li>{@link Identifier}</li>
         * <li>{@link Money}</li>
         * <li>{@link Period}</li>
         * <li>{@link Quantity}</li>
         * <li>{@link Range}</li>
         * <li>{@link Ratio}</li>
         * <li>{@link Reference}</li>
         * <li>{@link SampledData}</li>
         * <li>{@link Signature}</li>
         * <li>{@link Timing}</li>
         * <li>{@link ContactDetail}</li>
         * <li>{@link Contributor}</li>
         * <li>{@link DataRequirement}</li>
         * <li>{@link Expression}</li>
         * <li>{@link ParameterDefinition}</li>
         * <li>{@link RelatedArtifact}</li>
         * <li>{@link TriggerDefinition}</li>
         * <li>{@link UsageContext}</li>
         * <li>{@link Dosage}</li>
         * <li>{@link Meta}</li>
         * </ul>
         * 
         * @param fixed
         *     Value must be exactly this
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fixed(Element fixed) {
            this.fixed = fixed;
            return this;
        }

        /**
         * Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the 
         * instance. Other additional values may be found too. This is effectively constraint by example. 
         * 
         * <p>When pattern[x] is used to constrain a primitive, it means that the value provided in the pattern[x] must match the 
         * instance value exactly.
         * 
         * <p>When pattern[x] is used to constrain an array, it means that each element provided in the pattern[x] array must 
         * (recursively) match at least one element from the instance array.
         * 
         * <p>When pattern[x] is used to constrain a complex object, it means that each property in the pattern must be present 
         * in the complex object, and its value must recursively match -- i.e.,
         * 
         * <p>1. If primitive: it must match exactly the pattern value
         * <p>2. If a complex object: it must match (recursively) the pattern value
         * <p>3. If an array: it must match (recursively) the pattern value.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Base64Binary}</li>
         * <li>{@link Boolean}</li>
         * <li>{@link Canonical}</li>
         * <li>{@link Code}</li>
         * <li>{@link Date}</li>
         * <li>{@link DateTime}</li>
         * <li>{@link Decimal}</li>
         * <li>{@link Id}</li>
         * <li>{@link Instant}</li>
         * <li>{@link Integer}</li>
         * <li>{@link Markdown}</li>
         * <li>{@link Oid}</li>
         * <li>{@link PositiveInt}</li>
         * <li>{@link String}</li>
         * <li>{@link Time}</li>
         * <li>{@link UnsignedInt}</li>
         * <li>{@link Uri}</li>
         * <li>{@link Url}</li>
         * <li>{@link Uuid}</li>
         * <li>{@link Address}</li>
         * <li>{@link Age}</li>
         * <li>{@link Annotation}</li>
         * <li>{@link Attachment}</li>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Coding}</li>
         * <li>{@link ContactPoint}</li>
         * <li>{@link Count}</li>
         * <li>{@link Distance}</li>
         * <li>{@link Duration}</li>
         * <li>{@link HumanName}</li>
         * <li>{@link Identifier}</li>
         * <li>{@link Money}</li>
         * <li>{@link Period}</li>
         * <li>{@link Quantity}</li>
         * <li>{@link Range}</li>
         * <li>{@link Ratio}</li>
         * <li>{@link Reference}</li>
         * <li>{@link SampledData}</li>
         * <li>{@link Signature}</li>
         * <li>{@link Timing}</li>
         * <li>{@link ContactDetail}</li>
         * <li>{@link Contributor}</li>
         * <li>{@link DataRequirement}</li>
         * <li>{@link Expression}</li>
         * <li>{@link ParameterDefinition}</li>
         * <li>{@link RelatedArtifact}</li>
         * <li>{@link TriggerDefinition}</li>
         * <li>{@link UsageContext}</li>
         * <li>{@link Dosage}</li>
         * <li>{@link Meta}</li>
         * </ul>
         * 
         * @param pattern
         *     Value must have at least these property values
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder pattern(Element pattern) {
            this.pattern = pattern;
            return this;
        }

        /**
         * A sample value for this element demonstrating the type of information that would typically be found in the element.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param example
         *     Example value (as defined for type)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder example(Example... example) {
            for (Example value : example) {
                this.example.add(value);
            }
            return this;
        }

        /**
         * A sample value for this element demonstrating the type of information that would typically be found in the element.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param example
         *     Example value (as defined for type)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder example(Collection<Example> example) {
            this.example = new ArrayList<>(example);
            return this;
        }

        /**
         * The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, 
         * instant, time, decimal, integer, and Quantity.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Date}</li>
         * <li>{@link DateTime}</li>
         * <li>{@link Instant}</li>
         * <li>{@link Time}</li>
         * <li>{@link Decimal}</li>
         * <li>{@link Integer}</li>
         * <li>{@link PositiveInt}</li>
         * <li>{@link UnsignedInt}</li>
         * <li>{@link Quantity}</li>
         * </ul>
         * 
         * @param minValue
         *     Minimum Allowed Value (for some types)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder minValue(Element minValue) {
            this.minValue = minValue;
            return this;
        }

        /**
         * The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, 
         * instant, time, decimal, integer, and Quantity.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Date}</li>
         * <li>{@link DateTime}</li>
         * <li>{@link Instant}</li>
         * <li>{@link Time}</li>
         * <li>{@link Decimal}</li>
         * <li>{@link Integer}</li>
         * <li>{@link PositiveInt}</li>
         * <li>{@link UnsignedInt}</li>
         * <li>{@link Quantity}</li>
         * </ul>
         * 
         * @param maxValue
         *     Maximum Allowed Value (for some types)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder maxValue(Element maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        /**
         * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is 
         * expected to be supported by conformant consumers that support the element.
         * 
         * @param maxLength
         *     Max length for strings
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder maxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        /**
         * A reference to an invariant that may make additional statements about the cardinality or value in the instance.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param condition
         *     Reference to invariant about presence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder condition(Id... condition) {
            for (Id value : condition) {
                this.condition.add(value);
            }
            return this;
        }

        /**
         * A reference to an invariant that may make additional statements about the cardinality or value in the instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param condition
         *     Reference to invariant about presence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder condition(Collection<Id> condition) {
            this.condition = new ArrayList<>(condition);
            return this;
        }

        /**
         * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the 
         * context of the instance.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param constraint
         *     Condition that must evaluate to true
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder constraint(Constraint... constraint) {
            for (Constraint value : constraint) {
                this.constraint.add(value);
            }
            return this;
        }

        /**
         * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the 
         * context of the instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param constraint
         *     Condition that must evaluate to true
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder constraint(Collection<Constraint> constraint) {
            this.constraint = new ArrayList<>(constraint);
            return this;
        }

        /**
         * If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful 
         * way. If false, the element may be ignored and not supported. If false, whether to populate or use the data element in 
         * any way is at the discretion of the implementation.
         * 
         * @param mustSupport
         *     If the element must be supported
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder mustSupport(Boolean mustSupport) {
            this.mustSupport = mustSupport;
            return this;
        }

        /**
         * If true, the value of this element affects the interpretation of the element or resource that contains it, and the 
         * value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The 
         * effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process 
         * it, and/or a pre-determination has been made that it is not relevant to their particular system.
         * 
         * @param isModifier
         *     If this modifies the meaning of other elements
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder isModifier(Boolean isModifier) {
            this.isModifier = isModifier;
            return this;
        }

        /**
         * Explains how that element affects the interpretation of the resource or element that contains it.
         * 
         * @param isModifierReason
         *     Reason that this element is marked as a modifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder isModifierReason(String isModifierReason) {
            this.isModifierReason = isModifierReason;
            return this;
        }

        /**
         * Whether the element should be included if a client requests a search with the parameter _summary=true.
         * 
         * @param isSummary
         *     Include when _summary = true?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder isSummary(Boolean isSummary) {
            this.isSummary = isSummary;
            return this;
        }

        /**
         * Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, 
         * uri).
         * 
         * @param binding
         *     ValueSet details if this is coded
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder binding(Binding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * Identifies a concept from an external specification that roughly corresponds to this element.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param mapping
         *     Map element to another set of definitions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder mapping(Mapping... mapping) {
            for (Mapping value : mapping) {
                this.mapping.add(value);
            }
            return this;
        }

        /**
         * Identifies a concept from an external specification that roughly corresponds to this element.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param mapping
         *     Map element to another set of definitions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder mapping(Collection<Mapping> mapping) {
            this.mapping = new ArrayList<>(mapping);
            return this;
        }

        /**
         * Build the {@link ElementDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>path</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ElementDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ElementDefinition per the base specification
         */
        @Override
        public ElementDefinition build() {
            return new ElementDefinition(this);
        }

        protected Builder from(ElementDefinition elementDefinition) {
            super.from(elementDefinition);
            path = elementDefinition.path;
            representation.addAll(elementDefinition.representation);
            sliceName = elementDefinition.sliceName;
            sliceIsConstraining = elementDefinition.sliceIsConstraining;
            label = elementDefinition.label;
            code.addAll(elementDefinition.code);
            slicing = elementDefinition.slicing;
            _short = elementDefinition._short;
            definition = elementDefinition.definition;
            comment = elementDefinition.comment;
            requirements = elementDefinition.requirements;
            alias.addAll(elementDefinition.alias);
            min = elementDefinition.min;
            max = elementDefinition.max;
            base = elementDefinition.base;
            contentReference = elementDefinition.contentReference;
            type.addAll(elementDefinition.type);
            defaultValue = elementDefinition.defaultValue;
            meaningWhenMissing = elementDefinition.meaningWhenMissing;
            orderMeaning = elementDefinition.orderMeaning;
            fixed = elementDefinition.fixed;
            pattern = elementDefinition.pattern;
            example.addAll(elementDefinition.example);
            minValue = elementDefinition.minValue;
            maxValue = elementDefinition.maxValue;
            maxLength = elementDefinition.maxLength;
            condition.addAll(elementDefinition.condition);
            constraint.addAll(elementDefinition.constraint);
            mustSupport = elementDefinition.mustSupport;
            isModifier = elementDefinition.isModifier;
            isModifierReason = elementDefinition.isModifierReason;
            isSummary = elementDefinition.isSummary;
            binding = elementDefinition.binding;
            mapping.addAll(elementDefinition.mapping);
            return this;
        }
    }

    /**
     * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are 
     * multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has 
     * cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that 
     * come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path 
     * terminates the set).
     */
    public static class Slicing extends BackboneElement {
        @Summary
        private final List<Discriminator> discriminator;
        @Summary
        private final String description;
        @Summary
        private final Boolean ordered;
        @Summary
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "SlicingRules",
            strength = BindingStrength.ValueSet.REQUIRED,
            description = "How slices are interpreted when evaluating an instance.",
            valueSet = "http://hl7.org/fhir/ValueSet/resource-slicing-rules|4.0.1"
        )
        @Required
        private final SlicingRules rules;

        private volatile int hashCode;

        private Slicing(Builder builder) {
            super(builder);
            discriminator = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.discriminator, "discriminator"));
            description = builder.description;
            ordered = builder.ordered;
            rules = ValidationSupport.requireNonNull(builder.rules, "rules");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Designates which child elements are used to discriminate between the slices when processing an instance. If one or 
         * more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish 
         * which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Discriminator} that may be empty.
         */
        public List<Discriminator> getDiscriminator() {
            return discriminator;
        }

        /**
         * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be 
         * present to provide whatever information is possible about how the slices can be differentiated.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * If the matching elements have to occur in the same order as defined in the profile.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getOrdered() {
            return ordered;
        }

        /**
         * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that 
         * additional slices are only allowed at the end.
         * 
         * @return
         *     An immutable object of type {@link SlicingRules} that is non-null.
         */
        public SlicingRules getRules() {
            return rules;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !discriminator.isEmpty() || 
                (description != null) || 
                (ordered != null) || 
                (rules != null);
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
                    accept(discriminator, "discriminator", visitor, Discriminator.class);
                    accept(description, "description", visitor);
                    accept(ordered, "ordered", visitor);
                    accept(rules, "rules", visitor);
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
            Slicing other = (Slicing) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(discriminator, other.discriminator) && 
                Objects.equals(description, other.description) && 
                Objects.equals(ordered, other.ordered) && 
                Objects.equals(rules, other.rules);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    discriminator, 
                    description, 
                    ordered, 
                    rules);
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
            private List<Discriminator> discriminator = new ArrayList<>();
            private String description;
            private Boolean ordered;
            private SlicingRules rules;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Designates which child elements are used to discriminate between the slices when processing an instance. If one or 
             * more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish 
             * which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param discriminator
             *     Element values that are used to distinguish the slices
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder discriminator(Discriminator... discriminator) {
                for (Discriminator value : discriminator) {
                    this.discriminator.add(value);
                }
                return this;
            }

            /**
             * Designates which child elements are used to discriminate between the slices when processing an instance. If one or 
             * more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish 
             * which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param discriminator
             *     Element values that are used to distinguish the slices
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder discriminator(Collection<Discriminator> discriminator) {
                this.discriminator = new ArrayList<>(discriminator);
                return this;
            }

            /**
             * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be 
             * present to provide whatever information is possible about how the slices can be differentiated.
             * 
             * @param description
             *     Text description of how slicing works (or not)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * If the matching elements have to occur in the same order as defined in the profile.
             * 
             * @param ordered
             *     If elements must be in same order as slices
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder ordered(Boolean ordered) {
                this.ordered = ordered;
                return this;
            }

            /**
             * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that 
             * additional slices are only allowed at the end.
             * 
             * <p>This element is required.
             * 
             * @param rules
             *     closed | open | openAtEnd
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rules(SlicingRules rules) {
                this.rules = rules;
                return this;
            }

            /**
             * Build the {@link Slicing}
             * 
             * <p>Required elements:
             * <ul>
             * <li>rules</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Slicing}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Slicing per the base specification
             */
            @Override
            public Slicing build() {
                return new Slicing(this);
            }

            protected Builder from(Slicing slicing) {
                super.from(slicing);
                discriminator.addAll(slicing.discriminator);
                description = slicing.description;
                ordered = slicing.ordered;
                rules = slicing.rules;
                return this;
            }
        }

        /**
         * Designates which child elements are used to discriminate between the slices when processing an instance. If one or 
         * more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish 
         * which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
         */
        public static class Discriminator extends BackboneElement {
            @Summary
            @com.ibm.fhir.model.annotation.Binding(
                bindingName = "DiscriminatorType",
                strength = BindingStrength.ValueSet.REQUIRED,
                description = "How an element value is interpreted when discrimination is evaluated.",
                valueSet = "http://hl7.org/fhir/ValueSet/discriminator-type|4.0.1"
            )
            @Required
            private final DiscriminatorType type;
            @Summary
            @Required
            private final String path;

            private volatile int hashCode;

            private Discriminator(Builder builder) {
                super(builder);
                type = ValidationSupport.requireNonNull(builder.type, "type");
                path = ValidationSupport.requireNonNull(builder.path, "path");
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * How the element value is interpreted when discrimination is evaluated.
             * 
             * @return
             *     An immutable object of type {@link DiscriminatorType} that is non-null.
             */
            public DiscriminatorType getType() {
                return type;
            }

            /**
             * A FHIRPath expression, using [the simple subset of FHIRPath](fhirpath.html#simple), that is used to identify the 
             * element on which discrimination is based.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getPath() {
                return path;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (path != null);
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
                        accept(type, "type", visitor);
                        accept(path, "path", visitor);
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
                Discriminator other = (Discriminator) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(path, other.path);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        path);
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
                private DiscriminatorType type;
                private String path;

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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param extension
                 *     Additional content defined by implementations
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
                 * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
                 * modifierExtension itself).
                 * 
                 * <p>Adds new element(s) to the existing list
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
                 * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
                 * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
                 * modifierExtension itself).
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * How the element value is interpreted when discrimination is evaluated.
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     value | exists | pattern | type | profile
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(DiscriminatorType type) {
                    this.type = type;
                    return this;
                }

                /**
                 * A FHIRPath expression, using [the simple subset of FHIRPath](fhirpath.html#simple), that is used to identify the 
                 * element on which discrimination is based.
                 * 
                 * <p>This element is required.
                 * 
                 * @param path
                 *     Path to element value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder path(String path) {
                    this.path = path;
                    return this;
                }

                /**
                 * Build the {@link Discriminator}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * <li>path</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Discriminator}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Discriminator per the base specification
                 */
                @Override
                public Discriminator build() {
                    return new Discriminator(this);
                }

                protected Builder from(Discriminator discriminator) {
                    super.from(discriminator);
                    type = discriminator.type;
                    path = discriminator.path;
                    return this;
                }
            }
        }
    }

    /**
     * Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation 
     * of the element through the derived and related profiles. When the element definition is not the original definition of 
     * an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot - then the 
     * information in provided in the element definition may be different to the base definition. On the original definition 
     * of the element, it will be same.
     */
    public static class Base extends BackboneElement {
        @Summary
        @Required
        private final String path;
        @Summary
        @Required
        private final UnsignedInt min;
        @Summary
        @Required
        private final String max;

        private volatile int hashCode;

        private Base(Builder builder) {
            super(builder);
            path = ValidationSupport.requireNonNull(builder.path, "path");
            min = ValidationSupport.requireNonNull(builder.min, "min");
            max = ValidationSupport.requireNonNull(builder.max, "max");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, 
         * there is only one base definition of any element - that is, an element definition on a [StructureDefinition]
         * (structuredefinition.html#) without a StructureDefinition.base.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getPath() {
            return path;
        }

        /**
         * Minimum cardinality of the base element identified by the path.
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt} that is non-null.
         */
        public UnsignedInt getMin() {
            return min;
        }

        /**
         * Maximum cardinality of the base element identified by the path.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getMax() {
            return max;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (path != null) || 
                (min != null) || 
                (max != null);
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
                    accept(path, "path", visitor);
                    accept(min, "min", visitor);
                    accept(max, "max", visitor);
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
            Base other = (Base) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(path, other.path) && 
                Objects.equals(min, other.min) && 
                Objects.equals(max, other.max);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    path, 
                    min, 
                    max);
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
            private String path;
            private UnsignedInt min;
            private String max;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, 
             * there is only one base definition of any element - that is, an element definition on a [StructureDefinition]
             * (structuredefinition.html#) without a StructureDefinition.base.
             * 
             * <p>This element is required.
             * 
             * @param path
             *     Path that identifies the base element
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder path(String path) {
                this.path = path;
                return this;
            }

            /**
             * Minimum cardinality of the base element identified by the path.
             * 
             * <p>This element is required.
             * 
             * @param min
             *     Min cardinality of the base element
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder min(UnsignedInt min) {
                this.min = min;
                return this;
            }

            /**
             * Maximum cardinality of the base element identified by the path.
             * 
             * <p>This element is required.
             * 
             * @param max
             *     Max cardinality of the base element
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder max(String max) {
                this.max = max;
                return this;
            }

            /**
             * Build the {@link Base}
             * 
             * <p>Required elements:
             * <ul>
             * <li>path</li>
             * <li>min</li>
             * <li>max</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Base}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Base per the base specification
             */
            @Override
            public Base build() {
                return new Base(this);
            }

            protected Builder from(Base base) {
                super.from(base);
                path = base.path;
                min = base.min;
                max = base.max;
                return this;
            }
        }
    }

    /**
     * The data type or resource that the value of this element is permitted to be.
     */
    public static class Type extends BackboneElement {
        @Summary
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "FHIRDefinedTypeExt",
            strength = BindingStrength.ValueSet.EXTENSIBLE,
            description = "Either a resource or a data type, including logical model types.",
            valueSet = "http://hl7.org/fhir/ValueSet/defined-types"
        )
        @Required
        private final Uri code;
        @Summary
        private final List<Canonical> profile;
        @Summary
        private final List<Canonical> targetProfile;
        @Summary
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "AggregationMode",
            strength = BindingStrength.ValueSet.REQUIRED,
            description = "How resource references can be aggregated.",
            valueSet = "http://hl7.org/fhir/ValueSet/resource-aggregation-mode|4.0.1"
        )
        private final List<AggregationMode> aggregation;
        @Summary
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "ReferenceVersionRules",
            strength = BindingStrength.ValueSet.REQUIRED,
            description = "Whether a reference needs to be version specific or version independent, or whether either can be used.",
            valueSet = "http://hl7.org/fhir/ValueSet/reference-version-rules|4.0.1"
        )
        private final ReferenceVersionRules versioning;

        private volatile int hashCode;

        private Type(Builder builder) {
            super(builder);
            code = ValidationSupport.requireNonNull(builder.code, "code");
            profile = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.profile, "profile"));
            targetProfile = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.targetProfile, "targetProfile"));
            aggregation = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.aggregation, "aggregation"));
            versioning = builder.versioning;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to 
         * http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http://hl7.
         * org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.
         * 
         * @return
         *     An immutable object of type {@link Uri} that is non-null.
         */
        public Uri getCode() {
            return code;
        }

        /**
         * Identifies a profile structure or implementation Guide that applies to the datatype this element refers to. If any 
         * profiles are specified, then the content must conform to at least one of them. The URL can be a local reference - to a 
         * contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical 
         * URL. When an implementation guide is specified, the type SHALL conform to at least one profile defined in the 
         * implementation guide.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
         */
        public List<Canonical> getProfile() {
            return profile;
        }

        /**
         * Used when the type is "Reference" or "canonical", and identifies a profile structure or implementation Guide that 
         * applies to the target of the reference this element refers to. If any profiles are specified, then the content must 
         * conform to at least one of them. The URL can be a local reference - to a contained StructureDefinition, or a reference 
         * to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, 
         * the target resource SHALL conform to at least one profile defined in the implementation guide.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
         */
        public List<Canonical> getTargetProfile() {
            return targetProfile;
        }

        /**
         * If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, 
         * or a reference, and if the context is a bundle, is it included in the bundle.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link AggregationMode} that may be empty.
         */
        public List<AggregationMode> getAggregation() {
            return aggregation;
        }

        /**
         * Whether this reference needs to be version specific or version independent, or whether either can be used.
         * 
         * @return
         *     An immutable object of type {@link ReferenceVersionRules} that may be null.
         */
        public ReferenceVersionRules getVersioning() {
            return versioning;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                !profile.isEmpty() || 
                !targetProfile.isEmpty() || 
                !aggregation.isEmpty() || 
                (versioning != null);
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
                    accept(code, "code", visitor);
                    accept(profile, "profile", visitor, Canonical.class);
                    accept(targetProfile, "targetProfile", visitor, Canonical.class);
                    accept(aggregation, "aggregation", visitor, AggregationMode.class);
                    accept(versioning, "versioning", visitor);
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
            Type other = (Type) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(profile, other.profile) && 
                Objects.equals(targetProfile, other.targetProfile) && 
                Objects.equals(aggregation, other.aggregation) && 
                Objects.equals(versioning, other.versioning);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    profile, 
                    targetProfile, 
                    aggregation, 
                    versioning);
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
            private Uri code;
            private List<Canonical> profile = new ArrayList<>();
            private List<Canonical> targetProfile = new ArrayList<>();
            private List<AggregationMode> aggregation = new ArrayList<>();
            private ReferenceVersionRules versioning;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to 
             * http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http://hl7.
             * org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Data type or Resource (reference to definition)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(Uri code) {
                this.code = code;
                return this;
            }

            /**
             * Identifies a profile structure or implementation Guide that applies to the datatype this element refers to. If any 
             * profiles are specified, then the content must conform to at least one of them. The URL can be a local reference - to a 
             * contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical 
             * URL. When an implementation guide is specified, the type SHALL conform to at least one profile defined in the 
             * implementation guide.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param profile
             *     Profiles (StructureDefinition or IG) - one must apply
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder profile(Canonical... profile) {
                for (Canonical value : profile) {
                    this.profile.add(value);
                }
                return this;
            }

            /**
             * Identifies a profile structure or implementation Guide that applies to the datatype this element refers to. If any 
             * profiles are specified, then the content must conform to at least one of them. The URL can be a local reference - to a 
             * contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical 
             * URL. When an implementation guide is specified, the type SHALL conform to at least one profile defined in the 
             * implementation guide.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param profile
             *     Profiles (StructureDefinition or IG) - one must apply
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder profile(Collection<Canonical> profile) {
                this.profile = new ArrayList<>(profile);
                return this;
            }

            /**
             * Used when the type is "Reference" or "canonical", and identifies a profile structure or implementation Guide that 
             * applies to the target of the reference this element refers to. If any profiles are specified, then the content must 
             * conform to at least one of them. The URL can be a local reference - to a contained StructureDefinition, or a reference 
             * to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, 
             * the target resource SHALL conform to at least one profile defined in the implementation guide.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param targetProfile
             *     Profile (StructureDefinition or IG) on the Reference/canonical target - one must apply
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder targetProfile(Canonical... targetProfile) {
                for (Canonical value : targetProfile) {
                    this.targetProfile.add(value);
                }
                return this;
            }

            /**
             * Used when the type is "Reference" or "canonical", and identifies a profile structure or implementation Guide that 
             * applies to the target of the reference this element refers to. If any profiles are specified, then the content must 
             * conform to at least one of them. The URL can be a local reference - to a contained StructureDefinition, or a reference 
             * to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, 
             * the target resource SHALL conform to at least one profile defined in the implementation guide.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param targetProfile
             *     Profile (StructureDefinition or IG) on the Reference/canonical target - one must apply
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder targetProfile(Collection<Canonical> targetProfile) {
                this.targetProfile = new ArrayList<>(targetProfile);
                return this;
            }

            /**
             * If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, 
             * or a reference, and if the context is a bundle, is it included in the bundle.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param aggregation
             *     contained | referenced | bundled - how aggregated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder aggregation(AggregationMode... aggregation) {
                for (AggregationMode value : aggregation) {
                    this.aggregation.add(value);
                }
                return this;
            }

            /**
             * If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, 
             * or a reference, and if the context is a bundle, is it included in the bundle.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param aggregation
             *     contained | referenced | bundled - how aggregated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder aggregation(Collection<AggregationMode> aggregation) {
                this.aggregation = new ArrayList<>(aggregation);
                return this;
            }

            /**
             * Whether this reference needs to be version specific or version independent, or whether either can be used.
             * 
             * @param versioning
             *     either | independent | specific
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder versioning(ReferenceVersionRules versioning) {
                this.versioning = versioning;
                return this;
            }

            /**
             * Build the {@link Type}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Type}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Type per the base specification
             */
            @Override
            public Type build() {
                return new Type(this);
            }

            protected Builder from(Type type) {
                super.from(type);
                code = type.code;
                profile.addAll(type.profile);
                targetProfile.addAll(type.targetProfile);
                aggregation.addAll(type.aggregation);
                versioning = type.versioning;
                return this;
            }
        }
    }

    /**
     * A sample value for this element demonstrating the type of information that would typically be found in the element.
     */
    public static class Example extends BackboneElement {
        @Summary
        @Required
        private final String label;
        @Summary
        @Choice({ Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class })
        @Required
        private final Element value;

        private volatile int hashCode;

        private Example(Builder builder) {
            super(builder);
            label = ValidationSupport.requireNonNull(builder.label, "label");
            value = ValidationSupport.requireChoiceElement(builder.value, "value", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Describes the purpose of this example amoung the set of examples.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getLabel() {
            return label;
        }

        /**
         * The actual value for the element, which must be one of the types allowed for this element.
         * 
         * @return
         *     An immutable object of type {@link Element} that is non-null.
         */
        public Element getValue() {
            return value;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (label != null) || 
                (value != null);
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
                    accept(label, "label", visitor);
                    accept(value, "value", visitor);
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
            Example other = (Example) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(label, other.label) && 
                Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    label, 
                    value);
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
            private String label;
            private Element value;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Describes the purpose of this example amoung the set of examples.
             * 
             * <p>This element is required.
             * 
             * @param label
             *     Describes the purpose of this example
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder label(String label) {
                this.label = label;
                return this;
            }

            /**
             * The actual value for the element, which must be one of the types allowed for this element.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Base64Binary}</li>
             * <li>{@link Boolean}</li>
             * <li>{@link Canonical}</li>
             * <li>{@link Code}</li>
             * <li>{@link Date}</li>
             * <li>{@link DateTime}</li>
             * <li>{@link Decimal}</li>
             * <li>{@link Id}</li>
             * <li>{@link Instant}</li>
             * <li>{@link Integer}</li>
             * <li>{@link Markdown}</li>
             * <li>{@link Oid}</li>
             * <li>{@link PositiveInt}</li>
             * <li>{@link String}</li>
             * <li>{@link Time}</li>
             * <li>{@link UnsignedInt}</li>
             * <li>{@link Uri}</li>
             * <li>{@link Url}</li>
             * <li>{@link Uuid}</li>
             * <li>{@link Address}</li>
             * <li>{@link Age}</li>
             * <li>{@link Annotation}</li>
             * <li>{@link Attachment}</li>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Coding}</li>
             * <li>{@link ContactPoint}</li>
             * <li>{@link Count}</li>
             * <li>{@link Distance}</li>
             * <li>{@link Duration}</li>
             * <li>{@link HumanName}</li>
             * <li>{@link Identifier}</li>
             * <li>{@link Money}</li>
             * <li>{@link Period}</li>
             * <li>{@link Quantity}</li>
             * <li>{@link Range}</li>
             * <li>{@link Ratio}</li>
             * <li>{@link Reference}</li>
             * <li>{@link SampledData}</li>
             * <li>{@link Signature}</li>
             * <li>{@link Timing}</li>
             * <li>{@link ContactDetail}</li>
             * <li>{@link Contributor}</li>
             * <li>{@link DataRequirement}</li>
             * <li>{@link Expression}</li>
             * <li>{@link ParameterDefinition}</li>
             * <li>{@link RelatedArtifact}</li>
             * <li>{@link TriggerDefinition}</li>
             * <li>{@link UsageContext}</li>
             * <li>{@link Dosage}</li>
             * <li>{@link Meta}</li>
             * </ul>
             * 
             * @param value
             *     Value of Example (one of allowed types)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link Example}
             * 
             * <p>Required elements:
             * <ul>
             * <li>label</li>
             * <li>value</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Example}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Example per the base specification
             */
            @Override
            public Example build() {
                return new Example(this);
            }

            protected Builder from(Example example) {
                super.from(example);
                label = example.label;
                value = example.value;
                return this;
            }
        }
    }

    /**
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the 
     * context of the instance.
     */
    public static class Constraint extends BackboneElement {
        @Summary
        @Required
        private final Id key;
        @Summary
        private final String requirements;
        @Summary
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "ConstraintSeverity",
            strength = BindingStrength.ValueSet.REQUIRED,
            description = "SHALL applications comply with this constraint?",
            valueSet = "http://hl7.org/fhir/ValueSet/constraint-severity|4.0.1"
        )
        @Required
        private final ConstraintSeverity severity;
        @Summary
        @Required
        private final String human;
        @Summary
        private final String expression;
        @Summary
        private final String xpath;
        @Summary
        private final Canonical source;

        private volatile int hashCode;

        private Constraint(Builder builder) {
            super(builder);
            key = ValidationSupport.requireNonNull(builder.key, "key");
            requirements = builder.requirements;
            severity = ValidationSupport.requireNonNull(builder.severity, "severity");
            human = ValidationSupport.requireNonNull(builder.human, "human");
            expression = builder.expression;
            xpath = builder.xpath;
            source = builder.source;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Allows identification of which elements have their cardinalities impacted by the constraint. Will not be referenced 
         * for constraints that do not affect cardinality.
         * 
         * @return
         *     An immutable object of type {@link Id} that is non-null.
         */
        public Id getKey() {
            return key;
        }

        /**
         * Description of why this constraint is necessary or appropriate.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getRequirements() {
            return requirements;
        }

        /**
         * Identifies the impact constraint violation has on the conformance of the instance.
         * 
         * @return
         *     An immutable object of type {@link ConstraintSeverity} that is non-null.
         */
        public ConstraintSeverity getSeverity() {
            return severity;
        }

        /**
         * Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getHuman() {
            return human;
        }

        /**
         * A [FHIRPath](fhirpath.html) expression of constraint that can be executed to see if this constraint is met.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getExpression() {
            return expression;
        }

        /**
         * An XPath expression of constraint that can be executed to see if this constraint is met.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getXpath() {
            return xpath;
        }

        /**
         * A reference to the original source of the constraint, for traceability purposes.
         * 
         * @return
         *     An immutable object of type {@link Canonical} that may be null.
         */
        public Canonical getSource() {
            return source;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (key != null) || 
                (requirements != null) || 
                (severity != null) || 
                (human != null) || 
                (expression != null) || 
                (xpath != null) || 
                (source != null);
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
                    accept(key, "key", visitor);
                    accept(requirements, "requirements", visitor);
                    accept(severity, "severity", visitor);
                    accept(human, "human", visitor);
                    accept(expression, "expression", visitor);
                    accept(xpath, "xpath", visitor);
                    accept(source, "source", visitor);
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
            Constraint other = (Constraint) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(key, other.key) && 
                Objects.equals(requirements, other.requirements) && 
                Objects.equals(severity, other.severity) && 
                Objects.equals(human, other.human) && 
                Objects.equals(expression, other.expression) && 
                Objects.equals(xpath, other.xpath) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    key, 
                    requirements, 
                    severity, 
                    human, 
                    expression, 
                    xpath, 
                    source);
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
            private Id key;
            private String requirements;
            private ConstraintSeverity severity;
            private String human;
            private String expression;
            private String xpath;
            private Canonical source;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Allows identification of which elements have their cardinalities impacted by the constraint. Will not be referenced 
             * for constraints that do not affect cardinality.
             * 
             * <p>This element is required.
             * 
             * @param key
             *     Target of 'condition' reference above
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder key(Id key) {
                this.key = key;
                return this;
            }

            /**
             * Description of why this constraint is necessary or appropriate.
             * 
             * @param requirements
             *     Why this constraint is necessary or appropriate
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder requirements(String requirements) {
                this.requirements = requirements;
                return this;
            }

            /**
             * Identifies the impact constraint violation has on the conformance of the instance.
             * 
             * <p>This element is required.
             * 
             * @param severity
             *     error | warning
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder severity(ConstraintSeverity severity) {
                this.severity = severity;
                return this;
            }

            /**
             * Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
             * 
             * <p>This element is required.
             * 
             * @param human
             *     Human description of constraint
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder human(String human) {
                this.human = human;
                return this;
            }

            /**
             * A [FHIRPath](fhirpath.html) expression of constraint that can be executed to see if this constraint is met.
             * 
             * @param expression
             *     FHIRPath expression of constraint
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder expression(String expression) {
                this.expression = expression;
                return this;
            }

            /**
             * An XPath expression of constraint that can be executed to see if this constraint is met.
             * 
             * @param xpath
             *     XPath expression of constraint
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder xpath(String xpath) {
                this.xpath = xpath;
                return this;
            }

            /**
             * A reference to the original source of the constraint, for traceability purposes.
             * 
             * @param source
             *     Reference to original source of constraint
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Canonical source) {
                this.source = source;
                return this;
            }

            /**
             * Build the {@link Constraint}
             * 
             * <p>Required elements:
             * <ul>
             * <li>key</li>
             * <li>severity</li>
             * <li>human</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Constraint}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Constraint per the base specification
             */
            @Override
            public Constraint build() {
                return new Constraint(this);
            }

            protected Builder from(Constraint constraint) {
                super.from(constraint);
                key = constraint.key;
                requirements = constraint.requirements;
                severity = constraint.severity;
                human = constraint.human;
                expression = constraint.expression;
                xpath = constraint.xpath;
                source = constraint.source;
                return this;
            }
        }
    }

    /**
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, 
     * uri).
     */
    public static class Binding extends BackboneElement {
        @Summary
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "BindingStrength",
            strength = BindingStrength.ValueSet.REQUIRED,
            description = "Indication of the degree of conformance expectations associated with a binding.",
            valueSet = "http://hl7.org/fhir/ValueSet/binding-strength|4.0.1"
        )
        @Required
        private final BindingStrength strength;
        @Summary
        private final String description;
        @Summary
        private final Canonical valueSet;

        private volatile int hashCode;

        private Binding(Builder builder) {
            super(builder);
            strength = ValidationSupport.requireNonNull(builder.strength, "strength");
            description = builder.description;
            valueSet = builder.valueSet;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the 
         * provided value set must be adhered to in the instances.
         * 
         * @return
         *     An immutable object of type {@link BindingStrength} that is non-null.
         */
        public BindingStrength getStrength() {
            return strength;
        }

        /**
         * Describes the intended use of this particular set of codes.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Refers to the value set that identifies the set of codes the binding refers to.
         * 
         * @return
         *     An immutable object of type {@link Canonical} that may be null.
         */
        public Canonical getValueSet() {
            return valueSet;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (strength != null) || 
                (description != null) || 
                (valueSet != null);
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
                    accept(strength, "strength", visitor);
                    accept(description, "description", visitor);
                    accept(valueSet, "valueSet", visitor);
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
            Binding other = (Binding) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(strength, other.strength) && 
                Objects.equals(description, other.description) && 
                Objects.equals(valueSet, other.valueSet);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    strength, 
                    description, 
                    valueSet);
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
            private BindingStrength strength;
            private String description;
            private Canonical valueSet;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the 
             * provided value set must be adhered to in the instances.
             * 
             * <p>This element is required.
             * 
             * @param strength
             *     required | extensible | preferred | example
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder strength(BindingStrength strength) {
                this.strength = strength;
                return this;
            }

            /**
             * Describes the intended use of this particular set of codes.
             * 
             * @param description
             *     Human explanation of the value set
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Refers to the value set that identifies the set of codes the binding refers to.
             * 
             * @param valueSet
             *     Source of value set
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder valueSet(Canonical valueSet) {
                this.valueSet = valueSet;
                return this;
            }

            /**
             * Build the {@link Binding}
             * 
             * <p>Required elements:
             * <ul>
             * <li>strength</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Binding}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Binding per the base specification
             */
            @Override
            public Binding build() {
                return new Binding(this);
            }

            protected Builder from(Binding binding) {
                super.from(binding);
                strength = binding.strength;
                description = binding.description;
                valueSet = binding.valueSet;
                return this;
            }
        }
    }

    /**
     * Identifies a concept from an external specification that roughly corresponds to this element.
     */
    public static class Mapping extends BackboneElement {
        @Summary
        @Required
        private final Id identity;
        @Summary
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "MimeType",
            strength = BindingStrength.ValueSet.REQUIRED,
            description = "The mime type of an attachment. Any valid mime type is allowed.",
            valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.0.1"
        )
        private final Code language;
        @Summary
        @Required
        private final String map;
        @Summary
        private final String comment;

        private volatile int hashCode;

        private Mapping(Builder builder) {
            super(builder);
            identity = ValidationSupport.requireNonNull(builder.identity, "identity");
            language = builder.language;
            map = ValidationSupport.requireNonNull(builder.map, "map");
            comment = builder.comment;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * An internal reference to the definition of a mapping.
         * 
         * @return
         *     An immutable object of type {@link Id} that is non-null.
         */
        public Id getIdentity() {
            return identity;
        }

        /**
         * Identifies the computable language in which mapping.map is expressed.
         * 
         * @return
         *     An immutable object of type {@link Code} that may be null.
         */
        public Code getLanguage() {
            return language;
        }

        /**
         * Expresses what part of the target specification corresponds to this element.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getMap() {
            return map;
        }

        /**
         * Comments that provide information about the mapping or its use.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getComment() {
            return comment;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (identity != null) || 
                (language != null) || 
                (map != null) || 
                (comment != null);
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
                    accept(identity, "identity", visitor);
                    accept(language, "language", visitor);
                    accept(map, "map", visitor);
                    accept(comment, "comment", visitor);
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
            Mapping other = (Mapping) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identity, other.identity) && 
                Objects.equals(language, other.language) && 
                Objects.equals(map, other.map) && 
                Objects.equals(comment, other.comment);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identity, 
                    language, 
                    map, 
                    comment);
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
            private Id identity;
            private Code language;
            private String map;
            private String comment;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * An internal reference to the definition of a mapping.
             * 
             * <p>This element is required.
             * 
             * @param identity
             *     Reference to mapping declaration
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identity(Id identity) {
                this.identity = identity;
                return this;
            }

            /**
             * Identifies the computable language in which mapping.map is expressed.
             * 
             * @param language
             *     Computable language of mapping
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder language(Code language) {
                this.language = language;
                return this;
            }

            /**
             * Expresses what part of the target specification corresponds to this element.
             * 
             * <p>This element is required.
             * 
             * @param map
             *     Details of the mapping
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder map(String map) {
                this.map = map;
                return this;
            }

            /**
             * Comments that provide information about the mapping or its use.
             * 
             * @param comment
             *     Comments about the mapping or its use
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder comment(String comment) {
                this.comment = comment;
                return this;
            }

            /**
             * Build the {@link Mapping}
             * 
             * <p>Required elements:
             * <ul>
             * <li>identity</li>
             * <li>map</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Mapping}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Mapping per the base specification
             */
            @Override
            public Mapping build() {
                return new Mapping(this);
            }

            protected Builder from(Mapping mapping) {
                super.from(mapping);
                identity = mapping.identity;
                language = mapping.language;
                map = mapping.map;
                comment = mapping.comment;
                return this;
            }
        }
    }
}
