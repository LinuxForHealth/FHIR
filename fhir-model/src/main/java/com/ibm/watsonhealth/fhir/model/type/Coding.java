/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A reference to a code defined by a terminology system.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Coding extends Element {
    private final Uri system;
    private final String version;
    private final Code code;
    private final String display;
    private final Boolean userSelected;

    private volatile int hashCode;

    private Coding(Builder builder) {
        super(builder);
        system = builder.system;
        version = builder.version;
        code = builder.code;
        display = builder.display;
        userSelected = builder.userSelected;
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * <p>
     * The identification of the code system that defines the meaning of the symbol in the code.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getSystem() {
        return system;
    }

    /**
     * <p>
     * The version of the code system which was used when choosing this code. Note that a well-maintained code system does 
     * not need the version reported, because the meaning of codes is consistent across versions. However this cannot 
     * consistently be assured, and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by 
     * the coding system (e.g. post-coordination).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Code}.
     */
    public Code getCode() {
        return code;
    }

    /**
     * <p>
     * A representation of the meaning of the code in the system, following the rules of the system.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * <p>
     * Indicates that this coding was chosen by a user directly - e.g. off a pick list of available items (codes or displays).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getUserSelected() {
        return userSelected;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (system != null) || 
            (version != null) || 
            (code != null) || 
            (display != null) || 
            (userSelected != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(system, "system", visitor);
                accept(version, "version", visitor);
                accept(code, "code", visitor);
                accept(display, "display", visitor);
                accept(userSelected, "userSelected", visitor);
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
        Coding other = (Coding) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(system, other.system) && 
            Objects.equals(version, other.version) && 
            Objects.equals(code, other.code) && 
            Objects.equals(display, other.display) && 
            Objects.equals(userSelected, other.userSelected);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                system, 
                version, 
                code, 
                display, 
                userSelected);
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
        private Uri system;
        private String version;
        private Code code;
        private String display;
        private Boolean userSelected;

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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * The identification of the code system that defines the meaning of the symbol in the code.
         * </p>
         * 
         * @param system
         *     Identity of the terminology system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder system(Uri system) {
            this.system = system;
            return this;
        }

        /**
         * <p>
         * The version of the code system which was used when choosing this code. Note that a well-maintained code system does 
         * not need the version reported, because the meaning of codes is consistent across versions. However this cannot 
         * consistently be assured, and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.
         * </p>
         * 
         * @param version
         *     Version of the system - if relevant
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * <p>
         * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by 
         * the coding system (e.g. post-coordination).
         * </p>
         * 
         * @param code
         *     Symbol in syntax defined by the system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Code code) {
            this.code = code;
            return this;
        }

        /**
         * <p>
         * A representation of the meaning of the code in the system, following the rules of the system.
         * </p>
         * 
         * @param display
         *     Representation defined by the system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder display(String display) {
            this.display = display;
            return this;
        }

        /**
         * <p>
         * Indicates that this coding was chosen by a user directly - e.g. off a pick list of available items (codes or displays).
         * </p>
         * 
         * @param userSelected
         *     If this coding was chosen directly by the user
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder userSelected(Boolean userSelected) {
            this.userSelected = userSelected;
            return this;
        }

        @Override
        public Coding build() {
            return new Coding(this);
        }

        protected Builder from(Coding coding) {
            super.from(coding);
            system = coding.system;
            version = coding.version;
            code = coding.code;
            display = coding.display;
            userSelected = coding.userSelected;
            return this;
        }
    }
}
