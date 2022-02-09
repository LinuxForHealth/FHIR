/*
 * (C) Copyright IBM Corp. 2019, 2022
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

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.FHIRAllTypes;
import com.ibm.fhir.model.type.code.SortDirection;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of 
 * the data.
 */
@Constraint(
    id = "drq-1",
    level = "Rule",
    location = "DataRequirement.codeFilter",
    description = "Either a path or a searchParam must be provided, but not both",
    expression = "path.exists() xor searchParam.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/DataRequirement"
)
@Constraint(
    id = "drq-2",
    level = "Rule",
    location = "DataRequirement.dateFilter",
    description = "Either a path or a searchParam must be provided, but not both",
    expression = "path.exists() xor searchParam.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/DataRequirement"
)
@Constraint(
    id = "dataRequirement-3",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/subject-type",
    expression = "subject.as(CodeableConcept).exists() implies (subject.as(CodeableConcept).memberOf('http://hl7.org/fhir/ValueSet/subject-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/DataRequirement",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DataRequirement extends Element {
    @Summary
    @Binding(
        bindingName = "FHIRAllTypes",
        strength = BindingStrength.Value.REQUIRED,
        valueSet = "http://hl7.org/fhir/ValueSet/all-types|4.3.0-CIBUILD"
    )
    @Required
    private final FHIRAllTypes type;
    @Summary
    private final List<Canonical> profile;
    @Summary
    @ReferenceTarget({ "Group" })
    @Choice({ CodeableConcept.class, Reference.class })
    @Binding(
        bindingName = "SubjectType",
        strength = BindingStrength.Value.EXTENSIBLE,
        valueSet = "http://hl7.org/fhir/ValueSet/subject-type"
    )
    private final Element subject;
    @Summary
    private final List<String> mustSupport;
    @Summary
    private final List<CodeFilter> codeFilter;
    @Summary
    private final List<DateFilter> dateFilter;
    @Summary
    private final PositiveInt limit;
    @Summary
    private final List<Sort> sort;

    private DataRequirement(Builder builder) {
        super(builder);
        type = builder.type;
        profile = Collections.unmodifiableList(builder.profile);
        subject = builder.subject;
        mustSupport = Collections.unmodifiableList(builder.mustSupport);
        codeFilter = Collections.unmodifiableList(builder.codeFilter);
        dateFilter = Collections.unmodifiableList(builder.dateFilter);
        limit = builder.limit;
        sort = Collections.unmodifiableList(builder.sort);
    }

    /**
     * The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type 
     * of the base resource of the profile.
     * 
     * @return
     *     An immutable object of type {@link FHIRAllTypes} that is non-null.
     */
    public FHIRAllTypes getType() {
        return type;
    }

    /**
     * The profile of the required data, specified as the uri of the profile definition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getProfile() {
        return profile;
    }

    /**
     * The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} or {@link Reference} that may be null.
     */
    public Element getSubject() {
        return subject;
    }

    /**
     * Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the 
     * consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, 
     * only that the consuming system must understand the element and be able to provide values for it if they are available. 
     * 
     * <p>The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist 
     * only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for 
     * full details).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getMustSupport() {
        return mustSupport;
    }

    /**
     * Code filters specify additional constraints on the data, specifying the value set of interest for a particular element 
     * of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeFilter} that may be empty.
     */
    public List<CodeFilter> getCodeFilter() {
        return codeFilter;
    }

    /**
     * Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. 
     * Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DateFilter} that may be empty.
     */
    public List<DateFilter> getDateFilter() {
        return dateFilter;
    }

    /**
     * Specifies a maximum number of results that are required (uses the _count search parameter).
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} that may be null.
     */
    public PositiveInt getLimit() {
        return limit;
    }

    /**
     * Specifies the order of the results to be returned.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Sort} that may be empty.
     */
    public List<Sort> getSort() {
        return sort;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (type != null) || 
            !profile.isEmpty() || 
            (subject != null) || 
            !mustSupport.isEmpty() || 
            !codeFilter.isEmpty() || 
            !dateFilter.isEmpty() || 
            (limit != null) || 
            !sort.isEmpty();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(type, "type", visitor);
                accept(profile, "profile", visitor, Canonical.class);
                accept(subject, "subject", visitor);
                accept(mustSupport, "mustSupport", visitor, String.class);
                accept(codeFilter, "codeFilter", visitor, CodeFilter.class);
                accept(dateFilter, "dateFilter", visitor, DateFilter.class);
                accept(limit, "limit", visitor);
                accept(sort, "sort", visitor, Sort.class);
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
        DataRequirement other = (DataRequirement) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(type, other.type) && 
            Objects.equals(profile, other.profile) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(mustSupport, other.mustSupport) && 
            Objects.equals(codeFilter, other.codeFilter) && 
            Objects.equals(dateFilter, other.dateFilter) && 
            Objects.equals(limit, other.limit) && 
            Objects.equals(sort, other.sort);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                type, 
                profile, 
                subject, 
                mustSupport, 
                codeFilter, 
                dateFilter, 
                limit, 
                sort);
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

    public static class Builder extends Element.Builder {
        private FHIRAllTypes type;
        private List<Canonical> profile = new ArrayList<>();
        private Element subject;
        private List<String> mustSupport = new ArrayList<>();
        private List<CodeFilter> codeFilter = new ArrayList<>();
        private List<DateFilter> dateFilter = new ArrayList<>();
        private PositiveInt limit;
        private List<Sort> sort = new ArrayList<>();

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
         * The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type 
         * of the base resource of the profile.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     The type of the required data
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(FHIRAllTypes type) {
            this.type = type;
            return this;
        }

        /**
         * The profile of the required data, specified as the uri of the profile definition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param profile
         *     The profile of the required data
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
         * The profile of the required data, specified as the uri of the profile definition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param profile
         *     The profile of the required data
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder profile(Collection<Canonical> profile) {
            this.profile = new ArrayList<>(profile);
            return this;
        }

        /**
         * The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Reference}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     E.g. Patient, Practitioner, RelatedPerson, Organization, Location, Device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Element subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Convenience method for setting {@code mustSupport}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param mustSupport
         *     Indicates specific structure elements that are referenced by the knowledge module
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #mustSupport(com.ibm.fhir.model.type.String)
         */
        public Builder mustSupport(java.lang.String... mustSupport) {
            for (java.lang.String value : mustSupport) {
                this.mustSupport.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the 
         * consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, 
         * only that the consuming system must understand the element and be able to provide values for it if they are available. 
         * 
         * <p>The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist 
         * only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for 
         * full details).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param mustSupport
         *     Indicates specific structure elements that are referenced by the knowledge module
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder mustSupport(String... mustSupport) {
            for (String value : mustSupport) {
                this.mustSupport.add(value);
            }
            return this;
        }

        /**
         * Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the 
         * consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, 
         * only that the consuming system must understand the element and be able to provide values for it if they are available. 
         * 
         * <p>The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist 
         * only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for 
         * full details).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param mustSupport
         *     Indicates specific structure elements that are referenced by the knowledge module
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder mustSupport(Collection<String> mustSupport) {
            this.mustSupport = new ArrayList<>(mustSupport);
            return this;
        }

        /**
         * Code filters specify additional constraints on the data, specifying the value set of interest for a particular element 
         * of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param codeFilter
         *     What codes are expected
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder codeFilter(CodeFilter... codeFilter) {
            for (CodeFilter value : codeFilter) {
                this.codeFilter.add(value);
            }
            return this;
        }

        /**
         * Code filters specify additional constraints on the data, specifying the value set of interest for a particular element 
         * of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param codeFilter
         *     What codes are expected
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder codeFilter(Collection<CodeFilter> codeFilter) {
            this.codeFilter = new ArrayList<>(codeFilter);
            return this;
        }

        /**
         * Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. 
         * Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dateFilter
         *     What dates/date ranges are expected
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dateFilter(DateFilter... dateFilter) {
            for (DateFilter value : dateFilter) {
                this.dateFilter.add(value);
            }
            return this;
        }

        /**
         * Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. 
         * Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dateFilter
         *     What dates/date ranges are expected
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder dateFilter(Collection<DateFilter> dateFilter) {
            this.dateFilter = new ArrayList<>(dateFilter);
            return this;
        }

        /**
         * Specifies a maximum number of results that are required (uses the _count search parameter).
         * 
         * @param limit
         *     Number of results
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder limit(PositiveInt limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Specifies the order of the results to be returned.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param sort
         *     Order of the results
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sort(Sort... sort) {
            for (Sort value : sort) {
                this.sort.add(value);
            }
            return this;
        }

        /**
         * Specifies the order of the results to be returned.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param sort
         *     Order of the results
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder sort(Collection<Sort> sort) {
            this.sort = new ArrayList<>(sort);
            return this;
        }

        /**
         * Build the {@link DataRequirement}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link DataRequirement}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DataRequirement per the base specification
         */
        @Override
        public DataRequirement build() {
            DataRequirement dataRequirement = new DataRequirement(this);
            if (validating) {
                validate(dataRequirement);
            }
            return dataRequirement;
        }

        protected void validate(DataRequirement dataRequirement) {
            super.validate(dataRequirement);
            ValidationSupport.requireNonNull(dataRequirement.type, "type");
            ValidationSupport.checkList(dataRequirement.profile, "profile", Canonical.class);
            ValidationSupport.choiceElement(dataRequirement.subject, "subject", CodeableConcept.class, Reference.class);
            ValidationSupport.checkList(dataRequirement.mustSupport, "mustSupport", String.class);
            ValidationSupport.checkList(dataRequirement.codeFilter, "codeFilter", CodeFilter.class);
            ValidationSupport.checkList(dataRequirement.dateFilter, "dateFilter", DateFilter.class);
            ValidationSupport.checkList(dataRequirement.sort, "sort", Sort.class);
            ValidationSupport.checkReferenceType(dataRequirement.subject, "subject", "Group");
            ValidationSupport.requireValueOrChildren(dataRequirement);
        }

        protected Builder from(DataRequirement dataRequirement) {
            super.from(dataRequirement);
            type = dataRequirement.type;
            profile.addAll(dataRequirement.profile);
            subject = dataRequirement.subject;
            mustSupport.addAll(dataRequirement.mustSupport);
            codeFilter.addAll(dataRequirement.codeFilter);
            dateFilter.addAll(dataRequirement.dateFilter);
            limit = dataRequirement.limit;
            sort.addAll(dataRequirement.sort);
            return this;
        }
    }

    /**
     * Code filters specify additional constraints on the data, specifying the value set of interest for a particular element 
     * of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.
     */
    public static class CodeFilter extends BackboneElement {
        @Summary
        private final String path;
        @Summary
        private final String searchParam;
        @Summary
        private final Canonical valueSet;
        @Summary
        private final List<Coding> code;

        private CodeFilter(Builder builder) {
            super(builder);
            path = builder.path;
            searchParam = builder.searchParam;
            valueSet = builder.valueSet;
            code = Collections.unmodifiableList(builder.code);
        }

        /**
         * The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of 
         * the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to 
         * contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-
         * elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an 
         * integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getPath() {
            return path;
        }

        /**
         * A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which 
         * searches on elements of type code, Coding, or CodeableConcept.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSearchParam() {
            return searchParam;
        }

        /**
         * The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter 
         * will return only those data items for which the value of the code-valued element specified in the path is a member of 
         * the specified valueset.
         * 
         * @return
         *     An immutable object of type {@link Canonical} that may be null.
         */
        public Canonical getValueSet() {
            return valueSet;
        }

        /**
         * The codes for the code filter. If values are given, the filter will return only those data items for which the code-
         * valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in 
         * addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
         */
        public List<Coding> getCode() {
            return code;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (path != null) || 
                (searchParam != null) || 
                (valueSet != null) || 
                !code.isEmpty();
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
                    accept(searchParam, "searchParam", visitor);
                    accept(valueSet, "valueSet", visitor);
                    accept(code, "code", visitor, Coding.class);
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
            CodeFilter other = (CodeFilter) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(path, other.path) && 
                Objects.equals(searchParam, other.searchParam) && 
                Objects.equals(valueSet, other.valueSet) && 
                Objects.equals(code, other.code);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    path, 
                    searchParam, 
                    valueSet, 
                    code);
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
            private String searchParam;
            private Canonical valueSet;
            private List<Coding> code = new ArrayList<>();

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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Convenience method for setting {@code path}.
             * 
             * @param path
             *     A code-valued attribute to filter on
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #path(com.ibm.fhir.model.type.String)
             */
            public Builder path(java.lang.String path) {
                this.path = (path == null) ? null : String.of(path);
                return this;
            }

            /**
             * The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of 
             * the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to 
             * contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-
             * elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an 
             * integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
             * 
             * @param path
             *     A code-valued attribute to filter on
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder path(String path) {
                this.path = path;
                return this;
            }

            /**
             * Convenience method for setting {@code searchParam}.
             * 
             * @param searchParam
             *     A coded (token) parameter to search on
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #searchParam(com.ibm.fhir.model.type.String)
             */
            public Builder searchParam(java.lang.String searchParam) {
                this.searchParam = (searchParam == null) ? null : String.of(searchParam);
                return this;
            }

            /**
             * A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which 
             * searches on elements of type code, Coding, or CodeableConcept.
             * 
             * @param searchParam
             *     A coded (token) parameter to search on
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder searchParam(String searchParam) {
                this.searchParam = searchParam;
                return this;
            }

            /**
             * The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter 
             * will return only those data items for which the value of the code-valued element specified in the path is a member of 
             * the specified valueset.
             * 
             * @param valueSet
             *     Valueset for the filter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder valueSet(Canonical valueSet) {
                this.valueSet = valueSet;
                return this;
            }

            /**
             * The codes for the code filter. If values are given, the filter will return only those data items for which the code-
             * valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in 
             * addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     What code is expected
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
             * The codes for the code filter. If values are given, the filter will return only those data items for which the code-
             * valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in 
             * addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     What code is expected
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder code(Collection<Coding> code) {
                this.code = new ArrayList<>(code);
                return this;
            }

            /**
             * Build the {@link CodeFilter}
             * 
             * @return
             *     An immutable object of type {@link CodeFilter}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid CodeFilter per the base specification
             */
            @Override
            public CodeFilter build() {
                CodeFilter codeFilter = new CodeFilter(this);
                if (validating) {
                    validate(codeFilter);
                }
                return codeFilter;
            }

            protected void validate(CodeFilter codeFilter) {
                super.validate(codeFilter);
                ValidationSupport.checkList(codeFilter.code, "code", Coding.class);
                ValidationSupport.requireValueOrChildren(codeFilter);
            }

            protected Builder from(CodeFilter codeFilter) {
                super.from(codeFilter);
                path = codeFilter.path;
                searchParam = codeFilter.searchParam;
                valueSet = codeFilter.valueSet;
                code.addAll(codeFilter.code);
                return this;
            }
        }
    }

    /**
     * Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. 
     * Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.
     */
    public static class DateFilter extends BackboneElement {
        @Summary
        private final String path;
        @Summary
        private final String searchParam;
        @Summary
        @Choice({ DateTime.class, Period.class, Duration.class })
        private final Element value;

        private DateFilter(Builder builder) {
            super(builder);
            path = builder.path;
            searchParam = builder.searchParam;
            value = builder.value;
        }

        /**
         * The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of 
         * the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to 
         * contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-
         * elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an 
         * integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getPath() {
            return path;
        }

        /**
         * A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which 
         * searches on elements of type date, dateTime, Period, Schedule, or Timing.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSearchParam() {
            return searchParam;
        }

        /**
         * The value of the filter. If period is specified, the filter will return only those data items that fall within the 
         * bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return 
         * only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return 
         * only those data items that fall within Duration before now.
         * 
         * @return
         *     An immutable object of type {@link DateTime}, {@link Period} or {@link Duration} that may be null.
         */
        public Element getValue() {
            return value;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (path != null) || 
                (searchParam != null) || 
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
                    accept(path, "path", visitor);
                    accept(searchParam, "searchParam", visitor);
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
            DateFilter other = (DateFilter) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(path, other.path) && 
                Objects.equals(searchParam, other.searchParam) && 
                Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    path, 
                    searchParam, 
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
            private String path;
            private String searchParam;
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Convenience method for setting {@code path}.
             * 
             * @param path
             *     A date-valued attribute to filter on
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #path(com.ibm.fhir.model.type.String)
             */
            public Builder path(java.lang.String path) {
                this.path = (path == null) ? null : String.of(path);
                return this;
            }

            /**
             * The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of 
             * the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to 
             * contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-
             * elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an 
             * integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.
             * 
             * @param path
             *     A date-valued attribute to filter on
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder path(String path) {
                this.path = path;
                return this;
            }

            /**
             * Convenience method for setting {@code searchParam}.
             * 
             * @param searchParam
             *     A date valued parameter to search on
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #searchParam(com.ibm.fhir.model.type.String)
             */
            public Builder searchParam(java.lang.String searchParam) {
                this.searchParam = (searchParam == null) ? null : String.of(searchParam);
                return this;
            }

            /**
             * A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which 
             * searches on elements of type date, dateTime, Period, Schedule, or Timing.
             * 
             * @param searchParam
             *     A date valued parameter to search on
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder searchParam(String searchParam) {
                this.searchParam = searchParam;
                return this;
            }

            /**
             * The value of the filter. If period is specified, the filter will return only those data items that fall within the 
             * bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return 
             * only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return 
             * only those data items that fall within Duration before now.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link DateTime}</li>
             * <li>{@link Period}</li>
             * <li>{@link Duration}</li>
             * </ul>
             * 
             * @param value
             *     The value of the filter, as a Period, DateTime, or Duration value
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link DateFilter}
             * 
             * @return
             *     An immutable object of type {@link DateFilter}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid DateFilter per the base specification
             */
            @Override
            public DateFilter build() {
                DateFilter dateFilter = new DateFilter(this);
                if (validating) {
                    validate(dateFilter);
                }
                return dateFilter;
            }

            protected void validate(DateFilter dateFilter) {
                super.validate(dateFilter);
                ValidationSupport.choiceElement(dateFilter.value, "value", DateTime.class, Period.class, Duration.class);
                ValidationSupport.requireValueOrChildren(dateFilter);
            }

            protected Builder from(DateFilter dateFilter) {
                super.from(dateFilter);
                path = dateFilter.path;
                searchParam = dateFilter.searchParam;
                value = dateFilter.value;
                return this;
            }
        }
    }

    /**
     * Specifies the order of the results to be returned.
     */
    public static class Sort extends BackboneElement {
        @Summary
        @Required
        private final String path;
        @Summary
        @Binding(
            bindingName = "SortDirection",
            strength = BindingStrength.Value.REQUIRED,
            valueSet = "http://hl7.org/fhir/ValueSet/sort-direction|4.3.0-CIBUILD"
        )
        @Required
        private final SortDirection direction;

        private Sort(Builder builder) {
            super(builder);
            path = builder.path;
            direction = builder.direction;
        }

        /**
         * The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is 
         * allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality 
         * sub-elements. Note that the index must be an integer constant.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getPath() {
            return path;
        }

        /**
         * The direction of the sort, ascending or descending.
         * 
         * @return
         *     An immutable object of type {@link SortDirection} that is non-null.
         */
        public SortDirection getDirection() {
            return direction;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (path != null) || 
                (direction != null);
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
                    accept(direction, "direction", visitor);
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
            Sort other = (Sort) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(path, other.path) && 
                Objects.equals(direction, other.direction);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    path, 
                    direction);
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
            private SortDirection direction;

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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Convenience method for setting {@code path}.
             * 
             * <p>This element is required.
             * 
             * @param path
             *     The name of the attribute to perform the sort
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #path(com.ibm.fhir.model.type.String)
             */
            public Builder path(java.lang.String path) {
                this.path = (path == null) ? null : String.of(path);
                return this;
            }

            /**
             * The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is 
             * allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality 
             * sub-elements. Note that the index must be an integer constant.
             * 
             * <p>This element is required.
             * 
             * @param path
             *     The name of the attribute to perform the sort
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder path(String path) {
                this.path = path;
                return this;
            }

            /**
             * The direction of the sort, ascending or descending.
             * 
             * <p>This element is required.
             * 
             * @param direction
             *     ascending | descending
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder direction(SortDirection direction) {
                this.direction = direction;
                return this;
            }

            /**
             * Build the {@link Sort}
             * 
             * <p>Required elements:
             * <ul>
             * <li>path</li>
             * <li>direction</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Sort}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Sort per the base specification
             */
            @Override
            public Sort build() {
                Sort sort = new Sort(this);
                if (validating) {
                    validate(sort);
                }
                return sort;
            }

            protected void validate(Sort sort) {
                super.validate(sort);
                ValidationSupport.requireNonNull(sort.path, "path");
                ValidationSupport.requireNonNull(sort.direction, "direction");
                ValidationSupport.requireValueOrChildren(sort);
            }

            protected Builder from(Sort sort) {
                super.from(sort);
                path = sort.path;
                direction = sort.direction;
                return this;
            }
        }
    }
}
