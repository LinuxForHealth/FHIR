/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.AggregationMode;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.BindingStrength;
import com.ibm.watsonhealth.fhir.model.type.ConstraintSeverity;
import com.ibm.watsonhealth.fhir.model.type.DiscriminatorType;
import com.ibm.watsonhealth.fhir.model.type.PropertyRepresentation;
import com.ibm.watsonhealth.fhir.model.type.ReferenceVersionRules;
import com.ibm.watsonhealth.fhir.model.type.SlicingRules;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Captures constraints on each element within the resource, profile, or extension.
 * </p>
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
    expression = "aggregation.empty() or (code = 'Reference')"
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
    description = "Pattern and value are mutually exclusive",
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
    expression = "isModifier implies isModifierReason.exists()"
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
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ElementDefinition extends BackboneElement {
    private final String path;
    private final List<PropertyRepresentation> representation;
    private final String sliceName;
    private final Boolean sliceIsConstraining;
    private final String label;
    private final List<Coding> code;
    private final Slicing slicing;
    private final String _short;
    private final Markdown definition;
    private final Markdown comment;
    private final Markdown requirements;
    private final List<String> alias;
    private final UnsignedInt min;
    private final String max;
    private final Base base;
    private final Uri contentReference;
    private final List<Type> type;
    private final Element defaultValue;
    private final Markdown meaningWhenMissing;
    private final String orderMeaning;
    private final Element fixed;
    private final Element pattern;
    private final List<Example> example;
    private final Element minValue;
    private final Element maxValue;
    private final Integer maxLength;
    private final List<Id> condition;
    private final List<Constraint> constraint;
    private final Boolean mustSupport;
    private final Boolean isModifier;
    private final String isModifierReason;
    private final Boolean isSummary;
    private final Binding binding;
    private final List<Mapping> mapping;

    private ElementDefinition(Builder builder) {
        super(builder);
        this.path = ValidationSupport.requireNonNull(builder.path, "path");
        this.representation = builder.representation;
        this.sliceName = builder.sliceName;
        this.sliceIsConstraining = builder.sliceIsConstraining;
        this.label = builder.label;
        this.code = builder.code;
        this.slicing = builder.slicing;
        this._short = builder._short;
        this.definition = builder.definition;
        this.comment = builder.comment;
        this.requirements = builder.requirements;
        this.alias = builder.alias;
        this.min = builder.min;
        this.max = builder.max;
        this.base = builder.base;
        this.contentReference = builder.contentReference;
        this.type = builder.type;
        this.defaultValue = ValidationSupport.choiceElement(builder.defaultValue, "defaultValue", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class);
        this.meaningWhenMissing = builder.meaningWhenMissing;
        this.orderMeaning = builder.orderMeaning;
        this.fixed = ValidationSupport.choiceElement(builder.fixed, "fixed", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class);
        this.pattern = ValidationSupport.choiceElement(builder.pattern, "pattern", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class);
        this.example = builder.example;
        this.minValue = ValidationSupport.choiceElement(builder.minValue, "minValue", Date.class, DateTime.class, Instant.class, Time.class, Decimal.class, Integer.class, PositiveInt.class, UnsignedInt.class, Quantity.class);
        this.maxValue = ValidationSupport.choiceElement(builder.maxValue, "maxValue", Date.class, DateTime.class, Instant.class, Time.class, Decimal.class, Integer.class, PositiveInt.class, UnsignedInt.class, Quantity.class);
        this.maxLength = builder.maxLength;
        this.condition = builder.condition;
        this.constraint = builder.constraint;
        this.mustSupport = builder.mustSupport;
        this.isModifier = builder.isModifier;
        this.isModifierReason = builder.isModifierReason;
        this.isSummary = builder.isSummary;
        this.binding = builder.binding;
        this.mapping = builder.mapping;
    }

    /**
     * <p>
     * The path identifies the element and is expressed as a "."-separated list of ancestor elements, beginning with the name 
     * of the resource or extension.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPath() {
        return path;
    }

    /**
     * <p>
     * Codes that define how this element is represented in instances, when the deviation varies from the normal case.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link PropertyRepresentation}.
     */
    public List<PropertyRepresentation> getRepresentation() {
        return representation;
    }

    /**
     * <p>
     * The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. 
     * This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to 
     * different slices of the same element.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getSliceName() {
        return sliceName;
    }

    /**
     * <p>
     * If true, indicates that this slice definition is constraining a slice definition with the same name in an inherited 
     * profile. If false, the slice is not overriding any slice in an inherited profile. If missing, the slice might or might 
     * not be overriding a slice in an inherited profile, depending on the sliceName.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getSliceIsConstraining() {
        return sliceIsConstraining;
    }

    /**
     * <p>
     * A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt 
     * for the element in a user display or form.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getLabel() {
        return label;
    }

    /**
     * <p>
     * A code that has the same meaning as the element in a particular terminology.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Coding}.
     */
    public List<Coding> getCode() {
        return code;
    }

    /**
     * <p>
     * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are 
     * multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has 
     * cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that 
     * come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path 
     * terminates the set).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Slicing}.
     */
    public Slicing getSlicing() {
        return slicing;
    }

    /**
     * <p>
     * A concise description of what this element means (e.g. for use in autogenerated summaries).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getshort() {
        return _short;
    }

    /**
     * <p>
     * Provides a complete explanation of the meaning of the data element for human readability. For the case of elements 
     * derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but 
     * convey the meaning of the element in the particular context of use of the resource. (Note: The text you are reading is 
     * specified in ElementDefinition.definition).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getDefinition() {
        return definition;
    }

    /**
     * <p>
     * Explanatory notes and implementation guidance about the data element, including notes about how to use the data 
     * properly, exceptions to proper use, etc. (Note: The text you are reading is specified in ElementDefinition.comment).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getComment() {
        return comment;
    }

    /**
     * <p>
     * This element is for traceability of why the element was created and why the constraints exist as they do. This may be 
     * used to point to source materials or specifications that drove the structure of this element.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getRequirements() {
        return requirements;
    }

    /**
     * <p>
     * Identifies additional names by which this element might also be known.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link String}.
     */
    public List<String> getAlias() {
        return alias;
    }

    /**
     * <p>
     * The minimum number of times this element SHALL appear in the instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt}.
     */
    public UnsignedInt getMin() {
        return min;
    }

    /**
     * <p>
     * The maximum number of times this element is permitted to appear in the instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getMax() {
        return max;
    }

    /**
     * <p>
     * Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation 
     * of the element through the derived and related profiles. When the element definition is not the original definition of 
     * an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot - then the 
     * information in provided in the element definition may be different to the base definition. On the original definition 
     * of the element, it will be same.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Base}.
     */
    public Base getBase() {
        return base;
    }

    /**
     * <p>
     * Identifies an element defined elsewhere in the definition whose content rules should be applied to the current 
     * element. ContentReferences bring across all the rules that are in the ElementDefinition for the element, including 
     * definitions, cardinality constraints, bindings, invariants etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getContentReference() {
        return contentReference;
    }

    /**
     * <p>
     * The data type or resource that the value of this element is permitted to be.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Type}.
     */
    public List<Type> getType() {
        return type;
    }

    /**
     * <p>
     * The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the 
     * abstract is false').
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getDefaultValue() {
        return defaultValue;
    }

    /**
     * <p>
     * The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the 
     * period is ongoing').
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getMeaningWhenMissing() {
        return meaningWhenMissing;
    }

    /**
     * <p>
     * If present, indicates that the order of the repeating element has meaning and describes what that meaning is. If 
     * absent, it means that the order of the element has no meaning.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getOrderMeaning() {
        return orderMeaning;
    }

    /**
     * <p>
     * Specifies a value that SHALL be exactly the value for this element in the instance. For purposes of comparison, non-
     * significant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing 
     * elements/attributes must also be missing.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getFixed() {
        return fixed;
    }

    /**
     * <p>
     * Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the 
     * instance. Other additional values may be found too. This is effectively constraint by example. 
     * </p>
     * <p>
     * When pattern[x] is used to constrain a primitive, it means that the value provided in the pattern[x] must match the 
     * instance value exactly.
     * </p>
     * <p>
     * When pattern[x] is used to constrain an array, it means that each element provided in the pattern[x] array must 
     * (recursively) match at least one element from the instance array.
     * </p>
     * <p>
     * When pattern[x] is used to constrain a complex object, it means that each property in the pattern must be present in 
     * the complex object, and its value must recursively match -- i.e.,
     * </p>
     * <p>
     * 1. If primitive: it must match exactly the pattern value
     * 2. If a complex object: it must match (recursively) the pattern value
     * 3. If an array: it must match (recursively) the pattern value.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getPattern() {
        return pattern;
    }

    /**
     * <p>
     * A sample value for this element demonstrating the type of information that would typically be found in the element.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Example}.
     */
    public List<Example> getExample() {
        return example;
    }

    /**
     * <p>
     * The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, 
     * instant, time, decimal, integer, and Quantity.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getMinValue() {
        return minValue;
    }

    /**
     * <p>
     * The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, 
     * instant, time, decimal, integer, and Quantity.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getMaxValue() {
        return maxValue;
    }

    /**
     * <p>
     * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is 
     * expected to be supported by conformant consumers that support the element.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Integer}.
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * <p>
     * A reference to an invariant that may make additional statements about the cardinality or value in the instance.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Id}.
     */
    public List<Id> getCondition() {
        return condition;
    }

    /**
     * <p>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the 
     * context of the instance.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Constraint}.
     */
    public List<Constraint> getConstraint() {
        return constraint;
    }

    /**
     * <p>
     * If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful 
     * way. If false, the element may be ignored and not supported. If false, whether to populate or use the data element in 
     * any way is at the discretion of the implementation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getMustSupport() {
        return mustSupport;
    }

    /**
     * <p>
     * If true, the value of this element affects the interpretation of the element or resource that contains it, and the 
     * value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The 
     * effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process 
     * it, and/or a pre-determination has been made that it is not relevant to their particular system.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getIsModifier() {
        return isModifier;
    }

    /**
     * <p>
     * Explains how that element affects the interpretation of the resource or element that contains it.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getIsModifierReason() {
        return isModifierReason;
    }

    /**
     * <p>
     * Whether the element should be included if a client requests a search with the parameter _summary=true.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getIsSummary() {
        return isSummary;
    }

    /**
     * <p>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, 
     * uri).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Binding}.
     */
    public Binding getBinding() {
        return binding;
    }

    /**
     * <p>
     * Identifies a concept from an external specification that roughly corresponds to this element.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Mapping}.
     */
    public List<Mapping> getMapping() {
        return mapping;
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
                accept(defaultValue, "defaultValue", visitor, true);
                accept(meaningWhenMissing, "meaningWhenMissing", visitor);
                accept(orderMeaning, "orderMeaning", visitor);
                accept(fixed, "fixed", visitor, true);
                accept(pattern, "pattern", visitor, true);
                accept(example, "example", visitor, Example.class);
                accept(minValue, "minValue", visitor, true);
                accept(maxValue, "maxValue", visitor, true);
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(path);
        builder.id = id;
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.representation.addAll(representation);
        builder.sliceName = sliceName;
        builder.sliceIsConstraining = sliceIsConstraining;
        builder.label = label;
        builder.code.addAll(code);
        builder.slicing = slicing;
        builder._short = _short;
        builder.definition = definition;
        builder.comment = comment;
        builder.requirements = requirements;
        builder.alias.addAll(alias);
        builder.min = min;
        builder.max = max;
        builder.base = base;
        builder.contentReference = contentReference;
        builder.type.addAll(type);
        builder.defaultValue = defaultValue;
        builder.meaningWhenMissing = meaningWhenMissing;
        builder.orderMeaning = orderMeaning;
        builder.fixed = fixed;
        builder.pattern = pattern;
        builder.example.addAll(example);
        builder.minValue = minValue;
        builder.maxValue = maxValue;
        builder.maxLength = maxLength;
        builder.condition.addAll(condition);
        builder.constraint.addAll(constraint);
        builder.mustSupport = mustSupport;
        builder.isModifier = isModifier;
        builder.isModifierReason = isModifierReason;
        builder.isSummary = isSummary;
        builder.binding = binding;
        builder.mapping.addAll(mapping);
        return builder;
    }

    public static Builder builder(String path) {
        return new Builder(path);
    }

    public static class Builder extends BackboneElement.Builder {
        // required
        private final String path;

        // optional
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

        private Builder(String path) {
            super();
            this.path = path;
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
         * Codes that define how this element is represented in instances, when the deviation varies from the normal case.
         * </p>
         * 
         * @param representation
         *     xmlAttr | xmlText | typeAttr | cdaText | xhtml
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder representation(PropertyRepresentation... representation) {
            for (PropertyRepresentation value : representation) {
                this.representation.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Codes that define how this element is represented in instances, when the deviation varies from the normal case.
         * </p>
         * 
         * @param representation
         *     xmlAttr | xmlText | typeAttr | cdaText | xhtml
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder representation(Collection<PropertyRepresentation> representation) {
            this.representation.addAll(representation);
            return this;
        }

        /**
         * <p>
         * The name of this element definition slice, when slicing is working. The name must be a token with no dots or spaces. 
         * This is a unique name referring to a specific set of constraints applied to this element, used to provide a name to 
         * different slices of the same element.
         * </p>
         * 
         * @param sliceName
         *     Name for this particular element (in a set of slices)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sliceName(String sliceName) {
            this.sliceName = sliceName;
            return this;
        }

        /**
         * <p>
         * If true, indicates that this slice definition is constraining a slice definition with the same name in an inherited 
         * profile. If false, the slice is not overriding any slice in an inherited profile. If missing, the slice might or might 
         * not be overriding a slice in an inherited profile, depending on the sliceName.
         * </p>
         * 
         * @param sliceIsConstraining
         *     If this slice definition constrains an inherited slice definition (or not)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sliceIsConstraining(Boolean sliceIsConstraining) {
            this.sliceIsConstraining = sliceIsConstraining;
            return this;
        }

        /**
         * <p>
         * A single preferred label which is the text to display beside the element indicating its meaning or to use to prompt 
         * for the element in a user display or form.
         * </p>
         * 
         * @param label
         *     Name for element to display with or prompt for element
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder label(String label) {
            this.label = label;
            return this;
        }

        /**
         * <p>
         * A code that has the same meaning as the element in a particular terminology.
         * </p>
         * 
         * @param code
         *     Corresponding codes in terminologies
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(Coding... code) {
            for (Coding value : code) {
                this.code.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A code that has the same meaning as the element in a particular terminology.
         * </p>
         * 
         * @param code
         *     Corresponding codes in terminologies
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(Collection<Coding> code) {
            this.code.addAll(code);
            return this;
        }

        /**
         * <p>
         * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are 
         * multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has 
         * cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that 
         * come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path 
         * terminates the set).
         * </p>
         * 
         * @param slicing
         *     This element is sliced - slices follow
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder slicing(Slicing slicing) {
            this.slicing = slicing;
            return this;
        }

        /**
         * <p>
         * A concise description of what this element means (e.g. for use in autogenerated summaries).
         * </p>
         * 
         * @param _short
         *     Concise definition for space-constrained presentation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder _short(String _short) {
            this._short = _short;
            return this;
        }

        /**
         * <p>
         * Provides a complete explanation of the meaning of the data element for human readability. For the case of elements 
         * derived from existing elements (e.g. constraints), the definition SHALL be consistent with the base definition, but 
         * convey the meaning of the element in the particular context of use of the resource. (Note: The text you are reading is 
         * specified in ElementDefinition.definition).
         * </p>
         * 
         * @param definition
         *     Full formal definition as narrative text
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder definition(Markdown definition) {
            this.definition = definition;
            return this;
        }

        /**
         * <p>
         * Explanatory notes and implementation guidance about the data element, including notes about how to use the data 
         * properly, exceptions to proper use, etc. (Note: The text you are reading is specified in ElementDefinition.comment).
         * </p>
         * 
         * @param comment
         *     Comments about the use of this element
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder comment(Markdown comment) {
            this.comment = comment;
            return this;
        }

        /**
         * <p>
         * This element is for traceability of why the element was created and why the constraints exist as they do. This may be 
         * used to point to source materials or specifications that drove the structure of this element.
         * </p>
         * 
         * @param requirements
         *     Why this resource has been created
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder requirements(Markdown requirements) {
            this.requirements = requirements;
            return this;
        }

        /**
         * <p>
         * Identifies additional names by which this element might also be known.
         * </p>
         * 
         * @param alias
         *     Other names
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder alias(String... alias) {
            for (String value : alias) {
                this.alias.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies additional names by which this element might also be known.
         * </p>
         * 
         * @param alias
         *     Other names
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder alias(Collection<String> alias) {
            this.alias.addAll(alias);
            return this;
        }

        /**
         * <p>
         * The minimum number of times this element SHALL appear in the instance.
         * </p>
         * 
         * @param min
         *     Minimum Cardinality
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder min(UnsignedInt min) {
            this.min = min;
            return this;
        }

        /**
         * <p>
         * The maximum number of times this element is permitted to appear in the instance.
         * </p>
         * 
         * @param max
         *     Maximum Cardinality (a number or *)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder max(String max) {
            this.max = max;
            return this;
        }

        /**
         * <p>
         * Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation 
         * of the element through the derived and related profiles. When the element definition is not the original definition of 
         * an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot - then the 
         * information in provided in the element definition may be different to the base definition. On the original definition 
         * of the element, it will be same.
         * </p>
         * 
         * @param base
         *     Base definition information for tools
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder base(Base base) {
            this.base = base;
            return this;
        }

        /**
         * <p>
         * Identifies an element defined elsewhere in the definition whose content rules should be applied to the current 
         * element. ContentReferences bring across all the rules that are in the ElementDefinition for the element, including 
         * definitions, cardinality constraints, bindings, invariants etc.
         * </p>
         * 
         * @param contentReference
         *     Reference to definition of content for the element
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contentReference(Uri contentReference) {
            this.contentReference = contentReference;
            return this;
        }

        /**
         * <p>
         * The data type or resource that the value of this element is permitted to be.
         * </p>
         * 
         * @param type
         *     Data type and Profile for this element
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder type(Type... type) {
            for (Type value : type) {
                this.type.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The data type or resource that the value of this element is permitted to be.
         * </p>
         * 
         * @param type
         *     Data type and Profile for this element
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder type(Collection<Type> type) {
            this.type.addAll(type);
            return this;
        }

        /**
         * <p>
         * The value that should be used if there is no value stated in the instance (e.g. 'if not otherwise specified, the 
         * abstract is false').
         * </p>
         * 
         * @param defaultValue
         *     Specified value if missing from instance
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder defaultValue(Element defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        /**
         * <p>
         * The Implicit meaning that is to be understood when this element is missing (e.g. 'when this element is missing, the 
         * period is ongoing').
         * </p>
         * 
         * @param meaningWhenMissing
         *     Implicit meaning when this element is missing
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder meaningWhenMissing(Markdown meaningWhenMissing) {
            this.meaningWhenMissing = meaningWhenMissing;
            return this;
        }

        /**
         * <p>
         * If present, indicates that the order of the repeating element has meaning and describes what that meaning is. If 
         * absent, it means that the order of the element has no meaning.
         * </p>
         * 
         * @param orderMeaning
         *     What the order of the elements means
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder orderMeaning(String orderMeaning) {
            this.orderMeaning = orderMeaning;
            return this;
        }

        /**
         * <p>
         * Specifies a value that SHALL be exactly the value for this element in the instance. For purposes of comparison, non-
         * significant whitespace is ignored, and all values must be an exact match (case and accent sensitive). Missing 
         * elements/attributes must also be missing.
         * </p>
         * 
         * @param fixed
         *     Value must be exactly this
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder fixed(Element fixed) {
            this.fixed = fixed;
            return this;
        }

        /**
         * <p>
         * Specifies a value that the value in the instance SHALL follow - that is, any value in the pattern must be found in the 
         * instance. Other additional values may be found too. This is effectively constraint by example. 
         * </p>
         * <p>
         * When pattern[x] is used to constrain a primitive, it means that the value provided in the pattern[x] must match the 
         * instance value exactly.
         * </p>
         * <p>
         * When pattern[x] is used to constrain an array, it means that each element provided in the pattern[x] array must 
         * (recursively) match at least one element from the instance array.
         * </p>
         * <p>
         * When pattern[x] is used to constrain a complex object, it means that each property in the pattern must be present in 
         * the complex object, and its value must recursively match -- i.e.,
         * </p>
         * <p>
         * 1. If primitive: it must match exactly the pattern value
         * 2. If a complex object: it must match (recursively) the pattern value
         * 3. If an array: it must match (recursively) the pattern value.
         * </p>
         * 
         * @param pattern
         *     Value must have at least these property values
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder pattern(Element pattern) {
            this.pattern = pattern;
            return this;
        }

        /**
         * <p>
         * A sample value for this element demonstrating the type of information that would typically be found in the element.
         * </p>
         * 
         * @param example
         *     Example value (as defined for type)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder example(Example... example) {
            for (Example value : example) {
                this.example.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A sample value for this element demonstrating the type of information that would typically be found in the element.
         * </p>
         * 
         * @param example
         *     Example value (as defined for type)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder example(Collection<Example> example) {
            this.example.addAll(example);
            return this;
        }

        /**
         * <p>
         * The minimum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, 
         * instant, time, decimal, integer, and Quantity.
         * </p>
         * 
         * @param minValue
         *     Minimum Allowed Value (for some types)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder minValue(Element minValue) {
            this.minValue = minValue;
            return this;
        }

        /**
         * <p>
         * The maximum allowed value for the element. The value is inclusive. This is allowed for the types date, dateTime, 
         * instant, time, decimal, integer, and Quantity.
         * </p>
         * 
         * @param maxValue
         *     Maximum Allowed Value (for some types)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder maxValue(Element maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        /**
         * <p>
         * Indicates the maximum length in characters that is permitted to be present in conformant instances and which is 
         * expected to be supported by conformant consumers that support the element.
         * </p>
         * 
         * @param maxLength
         *     Max length for strings
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder maxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        /**
         * <p>
         * A reference to an invariant that may make additional statements about the cardinality or value in the instance.
         * </p>
         * 
         * @param condition
         *     Reference to invariant about presence
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder condition(Id... condition) {
            for (Id value : condition) {
                this.condition.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A reference to an invariant that may make additional statements about the cardinality or value in the instance.
         * </p>
         * 
         * @param condition
         *     Reference to invariant about presence
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder condition(Collection<Id> condition) {
            this.condition.addAll(condition);
            return this;
        }

        /**
         * <p>
         * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the 
         * context of the instance.
         * </p>
         * 
         * @param constraint
         *     Condition that must evaluate to true
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder constraint(Constraint... constraint) {
            for (Constraint value : constraint) {
                this.constraint.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the 
         * context of the instance.
         * </p>
         * 
         * @param constraint
         *     Condition that must evaluate to true
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder constraint(Collection<Constraint> constraint) {
            this.constraint.addAll(constraint);
            return this;
        }

        /**
         * <p>
         * If true, implementations that produce or consume resources SHALL provide "support" for the element in some meaningful 
         * way. If false, the element may be ignored and not supported. If false, whether to populate or use the data element in 
         * any way is at the discretion of the implementation.
         * </p>
         * 
         * @param mustSupport
         *     If the element must be supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder mustSupport(Boolean mustSupport) {
            this.mustSupport = mustSupport;
            return this;
        }

        /**
         * <p>
         * If true, the value of this element affects the interpretation of the element or resource that contains it, and the 
         * value of the element cannot be ignored. Typically, this is used for status, negation and qualification codes. The 
         * effect of this is that the element cannot be ignored by systems: they SHALL either recognize the element and process 
         * it, and/or a pre-determination has been made that it is not relevant to their particular system.
         * </p>
         * 
         * @param isModifier
         *     If this modifies the meaning of other elements
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder isModifier(Boolean isModifier) {
            this.isModifier = isModifier;
            return this;
        }

        /**
         * <p>
         * Explains how that element affects the interpretation of the resource or element that contains it.
         * </p>
         * 
         * @param isModifierReason
         *     Reason that this element is marked as a modifier
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder isModifierReason(String isModifierReason) {
            this.isModifierReason = isModifierReason;
            return this;
        }

        /**
         * <p>
         * Whether the element should be included if a client requests a search with the parameter _summary=true.
         * </p>
         * 
         * @param isSummary
         *     Include when _summary = true?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder isSummary(Boolean isSummary) {
            this.isSummary = isSummary;
            return this;
        }

        /**
         * <p>
         * Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, 
         * uri).
         * </p>
         * 
         * @param binding
         *     ValueSet details if this is coded
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder binding(Binding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * <p>
         * Identifies a concept from an external specification that roughly corresponds to this element.
         * </p>
         * 
         * @param mapping
         *     Map element to another set of definitions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder mapping(Mapping... mapping) {
            for (Mapping value : mapping) {
                this.mapping.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies a concept from an external specification that roughly corresponds to this element.
         * </p>
         * 
         * @param mapping
         *     Map element to another set of definitions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder mapping(Collection<Mapping> mapping) {
            this.mapping.addAll(mapping);
            return this;
        }

        @Override
        public ElementDefinition build() {
            return new ElementDefinition(this);
        }
    }

    /**
     * <p>
     * Indicates that the element is sliced into a set of alternative definitions (i.e. in a structure definition, there are 
     * multiple different constraints on a single element in the base resource). Slicing can be used in any resource that has 
     * cardinality ..* on the base resource, or any resource with a choice of types. The set of slices is any elements that 
     * come after this in the element sequence that have the same path, until a shorter path occurs (the shorter path 
     * terminates the set).
     * </p>
     */
    public static class Slicing extends BackboneElement {
        private final List<Discriminator> discriminator;
        private final String description;
        private final Boolean ordered;
        private final SlicingRules rules;

        private Slicing(Builder builder) {
            super(builder);
            this.discriminator = builder.discriminator;
            this.description = builder.description;
            this.ordered = builder.ordered;
            this.rules = ValidationSupport.requireNonNull(builder.rules, "rules");
        }

        /**
         * <p>
         * Designates which child elements are used to discriminate between the slices when processing an instance. If one or 
         * more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish 
         * which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Discriminator}.
         */
        public List<Discriminator> getDiscriminator() {
            return discriminator;
        }

        /**
         * <p>
         * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be 
         * present to provide whatever information is possible about how the slices can be differentiated.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDescription() {
            return description;
        }

        /**
         * <p>
         * If the matching elements have to occur in the same order as defined in the profile.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getOrdered() {
            return ordered;
        }

        /**
         * <p>
         * Whether additional slices are allowed or not. When the slices are ordered, profile authors can also say that 
         * additional slices are only allowed at the end.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link SlicingRules}.
         */
        public SlicingRules getRules() {
            return rules;
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
                    accept(discriminator, "discriminator", visitor, Discriminator.class);
                    accept(description, "description", visitor);
                    accept(ordered, "ordered", visitor);
                    accept(rules, "rules", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(SlicingRules rules) {
            return new Builder(rules);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final SlicingRules rules;

            // optional
            private List<Discriminator> discriminator = new ArrayList<>();
            private String description;
            private Boolean ordered;

            private Builder(SlicingRules rules) {
                super();
                this.rules = rules;
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Designates which child elements are used to discriminate between the slices when processing an instance. If one or 
             * more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish 
             * which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
             * </p>
             * 
             * @param discriminator
             *     Element values that are used to distinguish the slices
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder discriminator(Discriminator... discriminator) {
                for (Discriminator value : discriminator) {
                    this.discriminator.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Designates which child elements are used to discriminate between the slices when processing an instance. If one or 
             * more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish 
             * which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
             * </p>
             * 
             * @param discriminator
             *     Element values that are used to distinguish the slices
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder discriminator(Collection<Discriminator> discriminator) {
                this.discriminator.addAll(discriminator);
                return this;
            }

            /**
             * <p>
             * A human-readable text description of how the slicing works. If there is no discriminator, this is required to be 
             * present to provide whatever information is possible about how the slices can be differentiated.
             * </p>
             * 
             * @param description
             *     Text description of how slicing works (or not)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * <p>
             * If the matching elements have to occur in the same order as defined in the profile.
             * </p>
             * 
             * @param ordered
             *     If elements must be in same order as slices
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder ordered(Boolean ordered) {
                this.ordered = ordered;
                return this;
            }

            @Override
            public Slicing build() {
                return new Slicing(this);
            }

            private static Builder from(Slicing slicing) {
                Builder builder = new Builder(slicing.rules);
                builder.id = slicing.id;
                builder.extension.addAll(slicing.extension);
                builder.modifierExtension.addAll(slicing.modifierExtension);
                builder.discriminator.addAll(slicing.discriminator);
                builder.description = slicing.description;
                builder.ordered = slicing.ordered;
                return builder;
            }
        }

        /**
         * <p>
         * Designates which child elements are used to discriminate between the slices when processing an instance. If one or 
         * more discriminators are provided, the value of the child elements in the instance data SHALL completely distinguish 
         * which slice the element in the resource matches based on the allowed values for those elements in each of the slices.
         * </p>
         */
        public static class Discriminator extends BackboneElement {
            private final DiscriminatorType type;
            private final String path;

            private Discriminator(Builder builder) {
                super(builder);
                this.type = ValidationSupport.requireNonNull(builder.type, "type");
                this.path = ValidationSupport.requireNonNull(builder.path, "path");
            }

            /**
             * <p>
             * How the element value is interpreted when discrimination is evaluated.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link DiscriminatorType}.
             */
            public DiscriminatorType getType() {
                return type;
            }

            /**
             * <p>
             * A FHIRPath expression, using [the simple subset of FHIRPath](fhirpath.html#simple), that is used to identify the 
             * element on which discrimination is based.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getPath() {
                return path;
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
                        accept(type, "type", visitor);
                        accept(path, "path", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(DiscriminatorType type, String path) {
                return new Builder(type, path);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final DiscriminatorType type;
                private final String path;

                private Builder(DiscriminatorType type, String path) {
                    super();
                    this.type = type;
                    this.path = path;
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
                 * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
                 * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
                 * modifierExtension itself).
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
                 * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
                 * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
                 * modifierExtension itself).
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
                public Discriminator build() {
                    return new Discriminator(this);
                }

                private static Builder from(Discriminator discriminator) {
                    Builder builder = new Builder(discriminator.type, discriminator.path);
                    builder.id = discriminator.id;
                    builder.extension.addAll(discriminator.extension);
                    builder.modifierExtension.addAll(discriminator.modifierExtension);
                    return builder;
                }
            }
        }
    }

    /**
     * <p>
     * Information about the base definition of the element, provided to make it unnecessary for tools to trace the deviation 
     * of the element through the derived and related profiles. When the element definition is not the original definition of 
     * an element - i.g. either in a constraint on another type, or for elements from a super type in a snap shot - then the 
     * information in provided in the element definition may be different to the base definition. On the original definition 
     * of the element, it will be same.
     * </p>
     */
    public static class Base extends BackboneElement {
        private final String path;
        private final UnsignedInt min;
        private final String max;

        private Base(Builder builder) {
            super(builder);
            this.path = ValidationSupport.requireNonNull(builder.path, "path");
            this.min = ValidationSupport.requireNonNull(builder.min, "min");
            this.max = ValidationSupport.requireNonNull(builder.max, "max");
        }

        /**
         * <p>
         * The Path that identifies the base element - this matches the ElementDefinition.path for that element. Across FHIR, 
         * there is only one base definition of any element - that is, an element definition on a [StructureDefinition]
         * (structuredefinition.html#) without a StructureDefinition.base.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getPath() {
            return path;
        }

        /**
         * <p>
         * Minimum cardinality of the base element identified by the path.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt}.
         */
        public UnsignedInt getMin() {
            return min;
        }

        /**
         * <p>
         * Maximum cardinality of the base element identified by the path.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getMax() {
            return max;
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
                    accept(path, "path", visitor);
                    accept(min, "min", visitor);
                    accept(max, "max", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(String path, UnsignedInt min, String max) {
            return new Builder(path, min, max);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String path;
            private final UnsignedInt min;
            private final String max;

            private Builder(String path, UnsignedInt min, String max) {
                super();
                this.path = path;
                this.min = min;
                this.max = max;
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
            public Base build() {
                return new Base(this);
            }

            private static Builder from(Base base) {
                Builder builder = new Builder(base.path, base.min, base.max);
                builder.id = base.id;
                builder.extension.addAll(base.extension);
                builder.modifierExtension.addAll(base.modifierExtension);
                return builder;
            }
        }
    }

    /**
     * <p>
     * The data type or resource that the value of this element is permitted to be.
     * </p>
     */
    public static class Type extends BackboneElement {
        private final Uri code;
        private final List<Canonical> profile;
        private final List<Canonical> targetProfile;
        private final List<AggregationMode> aggregation;
        private final ReferenceVersionRules versioning;

        private Type(Builder builder) {
            super(builder);
            this.code = ValidationSupport.requireNonNull(builder.code, "code");
            this.profile = builder.profile;
            this.targetProfile = builder.targetProfile;
            this.aggregation = builder.aggregation;
            this.versioning = builder.versioning;
        }

        /**
         * <p>
         * URL of Data type or Resource that is a(or the) type used for this element. References are URLs that are relative to 
         * http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http://hl7.
         * org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getCode() {
            return code;
        }

        /**
         * <p>
         * Identifies a profile structure or implementation Guide that applies to the datatype this element refers to. If any 
         * profiles are specified, then the content must conform to at least one of them. The URL can be a local reference - to a 
         * contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical 
         * URL. When an implementation guide is specified, the type SHALL conform to at least one profile defined in the 
         * implementation guide.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Canonical}.
         */
        public List<Canonical> getProfile() {
            return profile;
        }

        /**
         * <p>
         * Used when the type is "Reference" or "canonical", and identifies a profile structure or implementation Guide that 
         * applies to the target of the reference this element refers to. If any profiles are specified, then the content must 
         * conform to at least one of them. The URL can be a local reference - to a contained StructureDefinition, or a reference 
         * to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, 
         * the target resource SHALL conform to at least one profile defined in the implementation guide.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Canonical}.
         */
        public List<Canonical> getTargetProfile() {
            return targetProfile;
        }

        /**
         * <p>
         * If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, 
         * or a reference, and if the context is a bundle, is it included in the bundle.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link AggregationMode}.
         */
        public List<AggregationMode> getAggregation() {
            return aggregation;
        }

        /**
         * <p>
         * Whether this reference needs to be version specific or version independent, or whether either can be used.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ReferenceVersionRules}.
         */
        public ReferenceVersionRules getVersioning() {
            return versioning;
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
                    accept(profile, "profile", visitor, Canonical.class);
                    accept(targetProfile, "targetProfile", visitor, Canonical.class);
                    accept(aggregation, "aggregation", visitor, AggregationMode.class);
                    accept(versioning, "versioning", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Uri code) {
            return new Builder(code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Uri code;

            // optional
            private List<Canonical> profile = new ArrayList<>();
            private List<Canonical> targetProfile = new ArrayList<>();
            private List<AggregationMode> aggregation = new ArrayList<>();
            private ReferenceVersionRules versioning;

            private Builder(Uri code) {
                super();
                this.code = code;
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Identifies a profile structure or implementation Guide that applies to the datatype this element refers to. If any 
             * profiles are specified, then the content must conform to at least one of them. The URL can be a local reference - to a 
             * contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical 
             * URL. When an implementation guide is specified, the type SHALL conform to at least one profile defined in the 
             * implementation guide.
             * </p>
             * 
             * @param profile
             *     Profiles (StructureDefinition or IG) - one must apply
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder profile(Canonical... profile) {
                for (Canonical value : profile) {
                    this.profile.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Identifies a profile structure or implementation Guide that applies to the datatype this element refers to. If any 
             * profiles are specified, then the content must conform to at least one of them. The URL can be a local reference - to a 
             * contained StructureDefinition, or a reference to another StructureDefinition or Implementation Guide by a canonical 
             * URL. When an implementation guide is specified, the type SHALL conform to at least one profile defined in the 
             * implementation guide.
             * </p>
             * 
             * @param profile
             *     Profiles (StructureDefinition or IG) - one must apply
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder profile(Collection<Canonical> profile) {
                this.profile.addAll(profile);
                return this;
            }

            /**
             * <p>
             * Used when the type is "Reference" or "canonical", and identifies a profile structure or implementation Guide that 
             * applies to the target of the reference this element refers to. If any profiles are specified, then the content must 
             * conform to at least one of them. The URL can be a local reference - to a contained StructureDefinition, or a reference 
             * to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, 
             * the target resource SHALL conform to at least one profile defined in the implementation guide.
             * </p>
             * 
             * @param targetProfile
             *     Profile (StructureDefinition or IG) on the Reference/canonical target - one must apply
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder targetProfile(Canonical... targetProfile) {
                for (Canonical value : targetProfile) {
                    this.targetProfile.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Used when the type is "Reference" or "canonical", and identifies a profile structure or implementation Guide that 
             * applies to the target of the reference this element refers to. If any profiles are specified, then the content must 
             * conform to at least one of them. The URL can be a local reference - to a contained StructureDefinition, or a reference 
             * to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, 
             * the target resource SHALL conform to at least one profile defined in the implementation guide.
             * </p>
             * 
             * @param targetProfile
             *     Profile (StructureDefinition or IG) on the Reference/canonical target - one must apply
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder targetProfile(Collection<Canonical> targetProfile) {
                this.targetProfile.addAll(targetProfile);
                return this;
            }

            /**
             * <p>
             * If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, 
             * or a reference, and if the context is a bundle, is it included in the bundle.
             * </p>
             * 
             * @param aggregation
             *     contained | referenced | bundled - how aggregated
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder aggregation(AggregationMode... aggregation) {
                for (AggregationMode value : aggregation) {
                    this.aggregation.add(value);
                }
                return this;
            }

            /**
             * <p>
             * If the type is a reference to another resource, how the resource is or can be aggregated - is it a contained resource, 
             * or a reference, and if the context is a bundle, is it included in the bundle.
             * </p>
             * 
             * @param aggregation
             *     contained | referenced | bundled - how aggregated
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder aggregation(Collection<AggregationMode> aggregation) {
                this.aggregation.addAll(aggregation);
                return this;
            }

            /**
             * <p>
             * Whether this reference needs to be version specific or version independent, or whether either can be used.
             * </p>
             * 
             * @param versioning
             *     either | independent | specific
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder versioning(ReferenceVersionRules versioning) {
                this.versioning = versioning;
                return this;
            }

            @Override
            public Type build() {
                return new Type(this);
            }

            private static Builder from(Type type) {
                Builder builder = new Builder(type.code);
                builder.id = type.id;
                builder.extension.addAll(type.extension);
                builder.modifierExtension.addAll(type.modifierExtension);
                builder.profile.addAll(type.profile);
                builder.targetProfile.addAll(type.targetProfile);
                builder.aggregation.addAll(type.aggregation);
                builder.versioning = type.versioning;
                return builder;
            }
        }
    }

    /**
     * <p>
     * A sample value for this element demonstrating the type of information that would typically be found in the element.
     * </p>
     */
    public static class Example extends BackboneElement {
        private final String label;
        private final Element value;

        private Example(Builder builder) {
            super(builder);
            this.label = ValidationSupport.requireNonNull(builder.label, "label");
            this.value = ValidationSupport.requireChoiceElement(builder.value, "value", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class);
        }

        /**
         * <p>
         * Describes the purpose of this example amoung the set of examples.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getLabel() {
            return label;
        }

        /**
         * <p>
         * The actual value for the element, which must be one of the types allowed for this element.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getValue() {
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
                    accept(label, "label", visitor);
                    accept(value, "value", visitor, true);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(String label, Element value) {
            return new Builder(label, value);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String label;
            private final Element value;

            private Builder(String label, Element value) {
                super();
                this.label = label;
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
            public Example build() {
                return new Example(this);
            }

            private static Builder from(Example example) {
                Builder builder = new Builder(example.label, example.value);
                builder.id = example.id;
                builder.extension.addAll(example.extension);
                builder.modifierExtension.addAll(example.modifierExtension);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Formal constraints such as co-occurrence and other constraints that can be computationally evaluated within the 
     * context of the instance.
     * </p>
     */
    public static class Constraint extends BackboneElement {
        private final Id key;
        private final String requirements;
        private final ConstraintSeverity severity;
        private final String human;
        private final String expression;
        private final String xpath;
        private final Canonical source;

        private Constraint(Builder builder) {
            super(builder);
            this.key = ValidationSupport.requireNonNull(builder.key, "key");
            this.requirements = builder.requirements;
            this.severity = ValidationSupport.requireNonNull(builder.severity, "severity");
            this.human = ValidationSupport.requireNonNull(builder.human, "human");
            this.expression = builder.expression;
            this.xpath = builder.xpath;
            this.source = builder.source;
        }

        /**
         * <p>
         * Allows identification of which elements have their cardinalities impacted by the constraint. Will not be referenced 
         * for constraints that do not affect cardinality.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Id}.
         */
        public Id getKey() {
            return key;
        }

        /**
         * <p>
         * Description of why this constraint is necessary or appropriate.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getRequirements() {
            return requirements;
        }

        /**
         * <p>
         * Identifies the impact constraint violation has on the conformance of the instance.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ConstraintSeverity}.
         */
        public ConstraintSeverity getSeverity() {
            return severity;
        }

        /**
         * <p>
         * Text that can be used to describe the constraint in messages identifying that the constraint has been violated.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getHuman() {
            return human;
        }

        /**
         * <p>
         * A [FHIRPath](fhirpath.html) expression of constraint that can be executed to see if this constraint is met.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getExpression() {
            return expression;
        }

        /**
         * <p>
         * An XPath expression of constraint that can be executed to see if this constraint is met.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getXpath() {
            return xpath;
        }

        /**
         * <p>
         * A reference to the original source of the constraint, for traceability purposes.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getSource() {
            return source;
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
                    accept(key, "key", visitor);
                    accept(requirements, "requirements", visitor);
                    accept(severity, "severity", visitor);
                    accept(human, "human", visitor);
                    accept(expression, "expression", visitor);
                    accept(xpath, "xpath", visitor);
                    accept(source, "source", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Id key, ConstraintSeverity severity, String human) {
            return new Builder(key, severity, human);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Id key;
            private final ConstraintSeverity severity;
            private final String human;

            // optional
            private String requirements;
            private String expression;
            private String xpath;
            private Canonical source;

            private Builder(Id key, ConstraintSeverity severity, String human) {
                super();
                this.key = key;
                this.severity = severity;
                this.human = human;
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Description of why this constraint is necessary or appropriate.
             * </p>
             * 
             * @param requirements
             *     Why this constraint is necessary or appropriate
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder requirements(String requirements) {
                this.requirements = requirements;
                return this;
            }

            /**
             * <p>
             * A [FHIRPath](fhirpath.html) expression of constraint that can be executed to see if this constraint is met.
             * </p>
             * 
             * @param expression
             *     FHIRPath expression of constraint
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder expression(String expression) {
                this.expression = expression;
                return this;
            }

            /**
             * <p>
             * An XPath expression of constraint that can be executed to see if this constraint is met.
             * </p>
             * 
             * @param xpath
             *     XPath expression of constraint
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder xpath(String xpath) {
                this.xpath = xpath;
                return this;
            }

            /**
             * <p>
             * A reference to the original source of the constraint, for traceability purposes.
             * </p>
             * 
             * @param source
             *     Reference to original source of constraint
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder source(Canonical source) {
                this.source = source;
                return this;
            }

            @Override
            public Constraint build() {
                return new Constraint(this);
            }

            private static Builder from(Constraint constraint) {
                Builder builder = new Builder(constraint.key, constraint.severity, constraint.human);
                builder.id = constraint.id;
                builder.extension.addAll(constraint.extension);
                builder.modifierExtension.addAll(constraint.modifierExtension);
                builder.requirements = constraint.requirements;
                builder.expression = constraint.expression;
                builder.xpath = constraint.xpath;
                builder.source = constraint.source;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Binds to a value set if this element is coded (code, Coding, CodeableConcept, Quantity), or the data types (string, 
     * uri).
     * </p>
     */
    public static class Binding extends BackboneElement {
        private final BindingStrength strength;
        private final String description;
        private final Canonical valueSet;

        private Binding(Builder builder) {
            super(builder);
            this.strength = ValidationSupport.requireNonNull(builder.strength, "strength");
            this.description = builder.description;
            this.valueSet = builder.valueSet;
        }

        /**
         * <p>
         * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the 
         * provided value set must be adhered to in the instances.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link BindingStrength}.
         */
        public BindingStrength getStrength() {
            return strength;
        }

        /**
         * <p>
         * Describes the intended use of this particular set of codes.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDescription() {
            return description;
        }

        /**
         * <p>
         * Refers to the value set that identifies the set of codes the binding refers to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getValueSet() {
            return valueSet;
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
                    accept(strength, "strength", visitor);
                    accept(description, "description", visitor);
                    accept(valueSet, "valueSet", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(BindingStrength strength) {
            return new Builder(strength);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final BindingStrength strength;

            // optional
            private String description;
            private Canonical valueSet;

            private Builder(BindingStrength strength) {
                super();
                this.strength = strength;
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Describes the intended use of this particular set of codes.
             * </p>
             * 
             * @param description
             *     Human explanation of the value set
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * <p>
             * Refers to the value set that identifies the set of codes the binding refers to.
             * </p>
             * 
             * @param valueSet
             *     Source of value set
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder valueSet(Canonical valueSet) {
                this.valueSet = valueSet;
                return this;
            }

            @Override
            public Binding build() {
                return new Binding(this);
            }

            private static Builder from(Binding binding) {
                Builder builder = new Builder(binding.strength);
                builder.id = binding.id;
                builder.extension.addAll(binding.extension);
                builder.modifierExtension.addAll(binding.modifierExtension);
                builder.description = binding.description;
                builder.valueSet = binding.valueSet;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Identifies a concept from an external specification that roughly corresponds to this element.
     * </p>
     */
    public static class Mapping extends BackboneElement {
        private final Id identity;
        private final Code language;
        private final String map;
        private final String comment;

        private Mapping(Builder builder) {
            super(builder);
            this.identity = ValidationSupport.requireNonNull(builder.identity, "identity");
            this.language = builder.language;
            this.map = ValidationSupport.requireNonNull(builder.map, "map");
            this.comment = builder.comment;
        }

        /**
         * <p>
         * An internal reference to the definition of a mapping.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Id}.
         */
        public Id getIdentity() {
            return identity;
        }

        /**
         * <p>
         * Identifies the computable language in which mapping.map is expressed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Code}.
         */
        public Code getLanguage() {
            return language;
        }

        /**
         * <p>
         * Expresses what part of the target specification corresponds to this element.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getMap() {
            return map;
        }

        /**
         * <p>
         * Comments that provide information about the mapping or its use.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getComment() {
            return comment;
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
                    accept(identity, "identity", visitor);
                    accept(language, "language", visitor);
                    accept(map, "map", visitor);
                    accept(comment, "comment", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Id identity, String map) {
            return new Builder(identity, map);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Id identity;
            private final String map;

            // optional
            private Code language;
            private String comment;

            private Builder(Id identity, String map) {
                super();
                this.identity = identity;
                this.map = map;
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Identifies the computable language in which mapping.map is expressed.
             * </p>
             * 
             * @param language
             *     Computable language of mapping
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder language(Code language) {
                this.language = language;
                return this;
            }

            /**
             * <p>
             * Comments that provide information about the mapping or its use.
             * </p>
             * 
             * @param comment
             *     Comments about the mapping or its use
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder comment(String comment) {
                this.comment = comment;
                return this;
            }

            @Override
            public Mapping build() {
                return new Mapping(this);
            }

            private static Builder from(Mapping mapping) {
                Builder builder = new Builder(mapping.identity, mapping.map);
                builder.id = mapping.id;
                builder.extension.addAll(mapping.extension);
                builder.modifierExtension.addAll(mapping.modifierExtension);
                builder.language = mapping.language;
                builder.comment = mapping.comment;
                return builder;
            }
        }
    }
}
