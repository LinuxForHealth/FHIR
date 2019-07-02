/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.FHIRAllTypes;
import com.ibm.watsonhealth.fhir.model.type.SortDirection;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of 
 * the data.
 * </p>
 */
@Constraint(
    id = "drq-1",
    level = "Rule",
    location = "DataRequirement.codeFilter",
    description = "Either a path or a searchParam must be provided, but not both",
    expression = "path.exists() xor searchParam.exists()"
)
@Constraint(
    id = "drq-2",
    level = "Rule",
    location = "DataRequirement.dateFilter",
    description = "Either a path or a searchParam must be provided, but not both",
    expression = "path.exists() xor searchParam.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class DataRequirement extends Element {
    private final FHIRAllTypes type;
    private final List<Canonical> profile;
    private final Element subject;
    private final List<String> mustSupport;
    private final List<CodeFilter> codeFilter;
    private final List<DateFilter> dateFilter;
    private final PositiveInt limit;
    private final List<Sort> sort;

    private volatile int hashCode;

    private DataRequirement(Builder builder) {
        super(builder);
        type = ValidationSupport.requireNonNull(builder.type, "type");
        profile = Collections.unmodifiableList(builder.profile);
        subject = ValidationSupport.choiceElement(builder.subject, "subject", CodeableConcept.class, Reference.class);
        mustSupport = Collections.unmodifiableList(builder.mustSupport);
        codeFilter = Collections.unmodifiableList(builder.codeFilter);
        dateFilter = Collections.unmodifiableList(builder.dateFilter);
        limit = builder.limit;
        sort = Collections.unmodifiableList(builder.sort);
    }

    /**
     * <p>
     * The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type 
     * of the base resource of the profile.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link FHIRAllTypes}.
     */
    public FHIRAllTypes getType() {
        return type;
    }

    /**
     * <p>
     * The profile of the required data, specified as the uri of the profile definition.
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
     * The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getSubject() {
        return subject;
    }

    /**
     * <p>
     * Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the 
     * consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, 
     * only that the consuming system must understand the element and be able to provide values for it if they are available. 
     * </p>
     * <p>
     * The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist 
     * only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for 
     * full details).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link String}.
     */
    public List<String> getMustSupport() {
        return mustSupport;
    }

    /**
     * <p>
     * Code filters specify additional constraints on the data, specifying the value set of interest for a particular element 
     * of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeFilter}.
     */
    public List<CodeFilter> getCodeFilter() {
        return codeFilter;
    }

    /**
     * <p>
     * Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. 
     * Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link DateFilter}.
     */
    public List<DateFilter> getDateFilter() {
        return dateFilter;
    }

    /**
     * <p>
     * Specifies a maximum number of results that are required (uses the _count search parameter).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PositiveInt}.
     */
    public PositiveInt getLimit() {
        return limit;
    }

    /**
     * <p>
     * Specifies the order of the results to be returned.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Sort}.
     */
    public List<Sort> getSort() {
        return sort;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
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
            visitor.visitEnd(elementName, this);
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
        return new Builder(type).from(this);
    }

    public Builder toBuilder(FHIRAllTypes type) {
        return new Builder(type).from(this);
    }

    public static Builder builder(FHIRAllTypes type) {
        return new Builder(type);
    }

    public static class Builder extends Element.Builder {
        // required
        private final FHIRAllTypes type;

        // optional
        private List<Canonical> profile = new ArrayList<>();
        private Element subject;
        private List<String> mustSupport = new ArrayList<>();
        private List<CodeFilter> codeFilter = new ArrayList<>();
        private List<DateFilter> dateFilter = new ArrayList<>();
        private PositiveInt limit;
        private List<Sort> sort = new ArrayList<>();

        private Builder(FHIRAllTypes type) {
            super();
            this.type = type;
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
         * The profile of the required data, specified as the uri of the profile definition.
         * </p>
         * 
         * @param profile
         *     The profile of the required data
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
         * The profile of the required data, specified as the uri of the profile definition.
         * </p>
         * 
         * @param profile
         *     The profile of the required data
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
         * The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.
         * </p>
         * 
         * @param subject
         *     E.g. Patient, Practitioner, RelatedPerson, Organization, Location, Device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Element subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the 
         * consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, 
         * only that the consuming system must understand the element and be able to provide values for it if they are available. 
         * </p>
         * <p>
         * The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist 
         * only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for 
         * full details).
         * </p>
         * 
         * @param mustSupport
         *     Indicates specific structure elements that are referenced by the knowledge module
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder mustSupport(String... mustSupport) {
            for (String value : mustSupport) {
                this.mustSupport.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the 
         * consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, 
         * only that the consuming system must understand the element and be able to provide values for it if they are available. 
         * </p>
         * <p>
         * The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist 
         * only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for 
         * full details).
         * </p>
         * 
         * @param mustSupport
         *     Indicates specific structure elements that are referenced by the knowledge module
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder mustSupport(Collection<String> mustSupport) {
            this.mustSupport.addAll(mustSupport);
            return this;
        }

        /**
         * <p>
         * Code filters specify additional constraints on the data, specifying the value set of interest for a particular element 
         * of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.
         * </p>
         * 
         * @param codeFilter
         *     What codes are expected
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder codeFilter(CodeFilter... codeFilter) {
            for (CodeFilter value : codeFilter) {
                this.codeFilter.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Code filters specify additional constraints on the data, specifying the value set of interest for a particular element 
         * of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.
         * </p>
         * 
         * @param codeFilter
         *     What codes are expected
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder codeFilter(Collection<CodeFilter> codeFilter) {
            this.codeFilter.addAll(codeFilter);
            return this;
        }

        /**
         * <p>
         * Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. 
         * Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.
         * </p>
         * 
         * @param dateFilter
         *     What dates/date ranges are expected
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dateFilter(DateFilter... dateFilter) {
            for (DateFilter value : dateFilter) {
                this.dateFilter.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. 
         * Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.
         * </p>
         * 
         * @param dateFilter
         *     What dates/date ranges are expected
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dateFilter(Collection<DateFilter> dateFilter) {
            this.dateFilter.addAll(dateFilter);
            return this;
        }

        /**
         * <p>
         * Specifies a maximum number of results that are required (uses the _count search parameter).
         * </p>
         * 
         * @param limit
         *     Number of results
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder limit(PositiveInt limit) {
            this.limit = limit;
            return this;
        }

        /**
         * <p>
         * Specifies the order of the results to be returned.
         * </p>
         * 
         * @param sort
         *     Order of the results
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sort(Sort... sort) {
            for (Sort value : sort) {
                this.sort.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Specifies the order of the results to be returned.
         * </p>
         * 
         * @param sort
         *     Order of the results
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sort(Collection<Sort> sort) {
            this.sort.addAll(sort);
            return this;
        }

        @Override
        public DataRequirement build() {
            return new DataRequirement(this);
        }

        private Builder from(DataRequirement dataRequirement) {
            id = dataRequirement.id;
            extension.addAll(dataRequirement.extension);
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
     * <p>
     * Code filters specify additional constraints on the data, specifying the value set of interest for a particular element 
     * of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.
     * </p>
     */
    public static class CodeFilter extends BackboneElement {
        private final String path;
        private final String searchParam;
        private final Canonical valueSet;
        private final List<Coding> code;

        private volatile int hashCode;

        private CodeFilter(Builder builder) {
            super(builder);
            path = builder.path;
            searchParam = builder.searchParam;
            valueSet = builder.valueSet;
            code = Collections.unmodifiableList(builder.code);
        }

        /**
         * <p>
         * The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of 
         * the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to 
         * contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-
         * elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an 
         * integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
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
         * A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which 
         * searches on elements of type code, Coding, or CodeableConcept.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getSearchParam() {
            return searchParam;
        }

        /**
         * <p>
         * The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter 
         * will return only those data items for which the value of the code-valued element specified in the path is a member of 
         * the specified valueset.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getValueSet() {
            return valueSet;
        }

        /**
         * <p>
         * The codes for the code filter. If values are given, the filter will return only those data items for which the code-
         * valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in 
         * addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Coding}.
         */
        public List<Coding> getCode() {
            return code;
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
                    accept(searchParam, "searchParam", visitor);
                    accept(valueSet, "valueSet", visitor);
                    accept(code, "code", visitor, Coding.class);
                }
                visitor.visitEnd(elementName, this);
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
            // optional
            private String path;
            private String searchParam;
            private Canonical valueSet;
            private List<Coding> code = new ArrayList<>();

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
             * The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of 
             * the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to 
             * contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-
             * elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an 
             * integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
             * </p>
             * 
             * @param path
             *     A code-valued attribute to filter on
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder path(String path) {
                this.path = path;
                return this;
            }

            /**
             * <p>
             * A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which 
             * searches on elements of type code, Coding, or CodeableConcept.
             * </p>
             * 
             * @param searchParam
             *     A coded (token) parameter to search on
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder searchParam(String searchParam) {
                this.searchParam = searchParam;
                return this;
            }

            /**
             * <p>
             * The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter 
             * will return only those data items for which the value of the code-valued element specified in the path is a member of 
             * the specified valueset.
             * </p>
             * 
             * @param valueSet
             *     Valueset for the filter
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder valueSet(Canonical valueSet) {
                this.valueSet = valueSet;
                return this;
            }

            /**
             * <p>
             * The codes for the code filter. If values are given, the filter will return only those data items for which the code-
             * valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in 
             * addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.
             * </p>
             * 
             * @param code
             *     What code is expected
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
             * The codes for the code filter. If values are given, the filter will return only those data items for which the code-
             * valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in 
             * addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.
             * </p>
             * 
             * @param code
             *     What code is expected
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder code(Collection<Coding> code) {
                this.code.addAll(code);
                return this;
            }

            @Override
            public CodeFilter build() {
                return new CodeFilter(this);
            }

            private Builder from(CodeFilter codeFilter) {
                id = codeFilter.id;
                extension.addAll(codeFilter.extension);
                modifierExtension.addAll(codeFilter.modifierExtension);
                path = codeFilter.path;
                searchParam = codeFilter.searchParam;
                valueSet = codeFilter.valueSet;
                code.addAll(codeFilter.code);
                return this;
            }
        }
    }

    /**
     * <p>
     * Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. 
     * Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.
     * </p>
     */
    public static class DateFilter extends BackboneElement {
        private final String path;
        private final String searchParam;
        private final Element value;

        private volatile int hashCode;

        private DateFilter(Builder builder) {
            super(builder);
            path = builder.path;
            searchParam = builder.searchParam;
            value = ValidationSupport.choiceElement(builder.value, "value", DateTime.class, Period.class, Duration.class);
        }

        /**
         * <p>
         * The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of 
         * the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to 
         * contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-
         * elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an 
         * integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.
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
         * A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which 
         * searches on elements of type date, dateTime, Period, Schedule, or Timing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getSearchParam() {
            return searchParam;
        }

        /**
         * <p>
         * The value of the filter. If period is specified, the filter will return only those data items that fall within the 
         * bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return 
         * only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return 
         * only those data items that fall within Duration before now.
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
                    accept(path, "path", visitor);
                    accept(searchParam, "searchParam", visitor);
                    accept(value, "value", visitor);
                }
                visitor.visitEnd(elementName, this);
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
            // optional
            private String path;
            private String searchParam;
            private Element value;

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
             * The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of 
             * the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to 
             * contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-
             * elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an 
             * integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.
             * </p>
             * 
             * @param path
             *     A date-valued attribute to filter on
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder path(String path) {
                this.path = path;
                return this;
            }

            /**
             * <p>
             * A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which 
             * searches on elements of type date, dateTime, Period, Schedule, or Timing.
             * </p>
             * 
             * @param searchParam
             *     A date valued parameter to search on
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder searchParam(String searchParam) {
                this.searchParam = searchParam;
                return this;
            }

            /**
             * <p>
             * The value of the filter. If period is specified, the filter will return only those data items that fall within the 
             * bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return 
             * only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return 
             * only those data items that fall within Duration before now.
             * </p>
             * 
             * @param value
             *     The value of the filter, as a Period, DateTime, or Duration value
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            @Override
            public DateFilter build() {
                return new DateFilter(this);
            }

            private Builder from(DateFilter dateFilter) {
                id = dateFilter.id;
                extension.addAll(dateFilter.extension);
                modifierExtension.addAll(dateFilter.modifierExtension);
                path = dateFilter.path;
                searchParam = dateFilter.searchParam;
                value = dateFilter.value;
                return this;
            }
        }
    }

    /**
     * <p>
     * Specifies the order of the results to be returned.
     * </p>
     */
    public static class Sort extends BackboneElement {
        private final String path;
        private final SortDirection direction;

        private volatile int hashCode;

        private Sort(Builder builder) {
            super(builder);
            path = ValidationSupport.requireNonNull(builder.path, "path");
            direction = ValidationSupport.requireNonNull(builder.direction, "direction");
        }

        /**
         * <p>
         * The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is 
         * allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality 
         * sub-elements. Note that the index must be an integer constant.
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
         * The direction of the sort, ascending or descending.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link SortDirection}.
         */
        public SortDirection getDirection() {
            return direction;
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
                    accept(direction, "direction", visitor);
                }
                visitor.visitEnd(elementName, this);
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
            return new Builder(path, direction).from(this);
        }

        public Builder toBuilder(String path, SortDirection direction) {
            return new Builder(path, direction).from(this);
        }

        public static Builder builder(String path, SortDirection direction) {
            return new Builder(path, direction);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String path;
            private final SortDirection direction;

            private Builder(String path, SortDirection direction) {
                super();
                this.path = path;
                this.direction = direction;
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
            public Sort build() {
                return new Sort(this);
            }

            private Builder from(Sort sort) {
                id = sort.id;
                extension.addAll(sort.extension);
                modifierExtension.addAll(sort.modifierExtension);
                return this;
            }
        }
    }
}
