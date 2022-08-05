/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/related-artifact-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class RelatedArtifactType extends Code {
    /**
     * Documentation
     * 
     * <p>Additional documentation for the knowledge resource. This would include additional instructions on usage as well as 
     * additional information on clinical context or appropriateness.
     */
    public static final RelatedArtifactType DOCUMENTATION = RelatedArtifactType.builder().value(Value.DOCUMENTATION).build();

    /**
     * Justification
     * 
     * <p>A summary of the justification for the knowledge resource including supporting evidence, relevant guidelines, or 
     * other clinically important information. This information is intended to provide a way to make the justification for 
     * the knowledge resource available to the consumer of interventions or results produced by the knowledge resource.
     */
    public static final RelatedArtifactType JUSTIFICATION = RelatedArtifactType.builder().value(Value.JUSTIFICATION).build();

    /**
     * Citation
     * 
     * <p>Bibliographic citation for papers, references, or other relevant material for the knowledge resource. This is 
     * intended to allow for citation of related material, but that was not necessarily specifically prepared in connection 
     * with this knowledge resource.
     */
    public static final RelatedArtifactType CITATION = RelatedArtifactType.builder().value(Value.CITATION).build();

    /**
     * Predecessor
     * 
     * <p>The previous version of the knowledge resource.
     */
    public static final RelatedArtifactType PREDECESSOR = RelatedArtifactType.builder().value(Value.PREDECESSOR).build();

    /**
     * Successor
     * 
     * <p>The next version of the knowledge resource.
     */
    public static final RelatedArtifactType SUCCESSOR = RelatedArtifactType.builder().value(Value.SUCCESSOR).build();

    /**
     * Derived From
     * 
     * <p>The knowledge resource is derived from the related artifact. This is intended to capture the relationship in which 
     * a particular knowledge resource is based on the content of another artifact, but is modified to capture either a 
     * different set of overall requirements, or a more specific set of requirements such as those involved in a particular 
     * institution or clinical setting.
     */
    public static final RelatedArtifactType DERIVED_FROM = RelatedArtifactType.builder().value(Value.DERIVED_FROM).build();

    /**
     * Depends On
     * 
     * <p>The knowledge resource depends on the given related artifact.
     */
    public static final RelatedArtifactType DEPENDS_ON = RelatedArtifactType.builder().value(Value.DEPENDS_ON).build();

    /**
     * Composed Of
     * 
     * <p>The knowledge resource is composed of the given related artifact.
     */
    public static final RelatedArtifactType COMPOSED_OF = RelatedArtifactType.builder().value(Value.COMPOSED_OF).build();

    private volatile int hashCode;

    private RelatedArtifactType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this RelatedArtifactType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating RelatedArtifactType objects from a passed enum value.
     */
    public static RelatedArtifactType of(Value value) {
        switch (value) {
        case DOCUMENTATION:
            return DOCUMENTATION;
        case JUSTIFICATION:
            return JUSTIFICATION;
        case CITATION:
            return CITATION;
        case PREDECESSOR:
            return PREDECESSOR;
        case SUCCESSOR:
            return SUCCESSOR;
        case DERIVED_FROM:
            return DERIVED_FROM;
        case DEPENDS_ON:
            return DEPENDS_ON;
        case COMPOSED_OF:
            return COMPOSED_OF;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating RelatedArtifactType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static RelatedArtifactType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating RelatedArtifactType objects from a passed string value.
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
     * Inherited factory method for creating RelatedArtifactType objects from a passed string value.
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
        RelatedArtifactType other = (RelatedArtifactType) obj;
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
         *     An enum constant for RelatedArtifactType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public RelatedArtifactType build() {
            RelatedArtifactType relatedArtifactType = new RelatedArtifactType(this);
            if (validating) {
                validate(relatedArtifactType);
            }
            return relatedArtifactType;
        }

        protected void validate(RelatedArtifactType relatedArtifactType) {
            super.validate(relatedArtifactType);
        }

        protected Builder from(RelatedArtifactType relatedArtifactType) {
            super.from(relatedArtifactType);
            return this;
        }
    }

    public enum Value {
        /**
         * Documentation
         * 
         * <p>Additional documentation for the knowledge resource. This would include additional instructions on usage as well as 
         * additional information on clinical context or appropriateness.
         */
        DOCUMENTATION("documentation"),

        /**
         * Justification
         * 
         * <p>A summary of the justification for the knowledge resource including supporting evidence, relevant guidelines, or 
         * other clinically important information. This information is intended to provide a way to make the justification for 
         * the knowledge resource available to the consumer of interventions or results produced by the knowledge resource.
         */
        JUSTIFICATION("justification"),

        /**
         * Citation
         * 
         * <p>Bibliographic citation for papers, references, or other relevant material for the knowledge resource. This is 
         * intended to allow for citation of related material, but that was not necessarily specifically prepared in connection 
         * with this knowledge resource.
         */
        CITATION("citation"),

        /**
         * Predecessor
         * 
         * <p>The previous version of the knowledge resource.
         */
        PREDECESSOR("predecessor"),

        /**
         * Successor
         * 
         * <p>The next version of the knowledge resource.
         */
        SUCCESSOR("successor"),

        /**
         * Derived From
         * 
         * <p>The knowledge resource is derived from the related artifact. This is intended to capture the relationship in which 
         * a particular knowledge resource is based on the content of another artifact, but is modified to capture either a 
         * different set of overall requirements, or a more specific set of requirements such as those involved in a particular 
         * institution or clinical setting.
         */
        DERIVED_FROM("derived-from"),

        /**
         * Depends On
         * 
         * <p>The knowledge resource depends on the given related artifact.
         */
        DEPENDS_ON("depends-on"),

        /**
         * Composed Of
         * 
         * <p>The knowledge resource is composed of the given related artifact.
         */
        COMPOSED_OF("composed-of");

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
         * Factory method for creating RelatedArtifactType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding RelatedArtifactType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "documentation":
                return DOCUMENTATION;
            case "justification":
                return JUSTIFICATION;
            case "citation":
                return CITATION;
            case "predecessor":
                return PREDECESSOR;
            case "successor":
                return SUCCESSOR;
            case "derived-from":
                return DERIVED_FROM;
            case "depends-on":
                return DEPENDS_ON;
            case "composed-of":
                return COMPOSED_OF;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
