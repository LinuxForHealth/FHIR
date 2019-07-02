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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Uri;

/**
 * <p>
 * A resource that includes narrative, extensions, and contained resources.
 * </p>
 */
@Constraint(
    id = "dom-2",
    level = "Rule",
    location = "(base)",
    description = "If the resource is contained in another resource, it SHALL NOT contain nested Resources",
    expression = "contained.contained.empty()"
)
@Constraint(
    id = "dom-3",
    level = "Rule",
    location = "(base)",
    description = "If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource or SHALL refer to the containing resource",
    expression = "contained.where((('#'+id in (%resource.descendants().reference | %resource.descendants().as(canonical) | %resource.descendants().as(uri) | %resource.descendants().as(url))) or descendants().where(reference = '#').exists() or descendants().where(as(canonical) = '#').exists() or descendants().where(as(canonical) = '#').exists()).not()).trace('unmatched', id).empty()"
)
@Constraint(
    id = "dom-4",
    level = "Rule",
    location = "(base)",
    description = "If a resource is contained in another resource, it SHALL NOT have a meta.versionId or a meta.lastUpdated",
    expression = "contained.meta.versionId.empty() and contained.meta.lastUpdated.empty()"
)
@Constraint(
    id = "dom-5",
    level = "Rule",
    location = "(base)",
    description = "If a resource is contained in another resource, it SHALL NOT have a security label",
    expression = "contained.meta.security.empty()"
)
@Constraint(
    id = "dom-6",
    level = "Warning",
    location = "(base)",
    description = "A resource should have narrative for robust management",
    expression = "text.div.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public abstract class DomainResource extends Resource {
    protected final Narrative text;
    protected final List<Resource> contained;
    protected final List<Extension> extension;
    protected final List<Extension> modifierExtension;

    protected DomainResource(Builder builder) {
        super(builder);
        text = builder.text;
        contained = Collections.unmodifiableList(builder.contained);
        extension = Collections.unmodifiableList(builder.extension);
        modifierExtension = Collections.unmodifiableList(builder.modifierExtension);
    }

    /**
     * <p>
     * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
     * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
     * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
     * content should be represented in the narrative to ensure clinical safety.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Narrative}.
     */
    public Narrative getText() {
        return text;
    }

    /**
     * <p>
     * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
     * identified independently, and nor can they have their own independent transaction scope.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Resource}.
     */
    public List<Resource> getContained() {
        return contained;
    }

    /**
     * <p>
     * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
     * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
     * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
     * of the definition of the extension.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Extension}.
     */
    public List<Extension> getExtension() {
        return extension;
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
     * @return
     *     A list containing immutable objects of type {@link Extension}.
     */
    public List<Extension> getModifierExtension() {
        return modifierExtension;
    }

    @Override
    public abstract Builder toBuilder();

    public static abstract class Builder extends Resource.Builder {
        // optional
        protected Narrative text;
        protected List<Resource> contained = new ArrayList<>();
        protected List<Extension> extension = new ArrayList<>();
        protected List<Extension> modifierExtension = new ArrayList<>();

        protected Builder() {
            super();
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
        public Builder text(Narrative text) {
            this.text = text;
            return this;
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
        public Builder contained(Resource... contained) {
            for (Resource value : contained) {
                this.contained.add(value);
            }
            return this;
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
        public Builder contained(Collection<Resource> contained) {
            this.contained.addAll(contained);
            return this;
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
        public Builder extension(Extension... extension) {
            for (Extension value : extension) {
                this.extension.add(value);
            }
            return this;
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
        public Builder extension(Collection<Extension> extension) {
            this.extension.addAll(extension);
            return this;
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
        public Builder modifierExtension(Extension... modifierExtension) {
            for (Extension value : modifierExtension) {
                this.modifierExtension.add(value);
            }
            return this;
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
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            this.modifierExtension.addAll(modifierExtension);
            return this;
        }

        @Override
        public abstract DomainResource build();
    }
}
