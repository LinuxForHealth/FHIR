/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.annotation.Maturity;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.StandardsStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;

/**
 * A resource that includes narrative, extensions, and contained resources.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Constraint(
    id = "dom-2",
    level = "Rule",
    location = "(base)",
    description = "If the resource is contained in another resource, it SHALL NOT contain nested Resources",
    expression = "contained.contained.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/DomainResource"
)
@Constraint(
    id = "dom-3",
    level = "Rule",
    location = "(base)",
    description = "If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource or SHALL refer to the containing resource",
    expression = "contained.where(((id.exists() and ('#'+id in (%resource.descendants().reference | %resource.descendants().as(canonical) | %resource.descendants().as(uri) | %resource.descendants().as(url)))) or descendants().where(reference = '#').exists() or descendants().where(as(canonical) = '#').exists() or descendants().where(as(uri) = '#').exists()).not()).trace('unmatched', id).empty()",
    source = "http://hl7.org/fhir/StructureDefinition/DomainResource"
)
@Constraint(
    id = "dom-4",
    level = "Rule",
    location = "(base)",
    description = "If a resource is contained in another resource, it SHALL NOT have a meta.versionId or a meta.lastUpdated",
    expression = "contained.meta.versionId.empty() and contained.meta.lastUpdated.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/DomainResource"
)
@Constraint(
    id = "dom-5",
    level = "Rule",
    location = "(base)",
    description = "If a resource is contained in another resource, it SHALL NOT have a security label",
    expression = "contained.meta.security.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/DomainResource"
)
@Constraint(
    id = "dom-6",
    level = "Warning",
    location = "(base)",
    description = "A resource should have narrative for robust management",
    expression = "text.`div`.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/DomainResource"
)
@Constraint(
    id = "dom-r4b",
    level = "Warning",
    location = "DomainResource.contained",
    description = "Containing new R4B resources within R4 resources may cause interoperability issues if instances are shared with R4 systems",
    expression = "($this is Citation or $this is Evidence or $this is EvidenceReport or $this is EvidenceVariable or $this is MedicinalProductDefinition or $this is PackagedProductDefinition or $this is AdministrableProductDefinition or $this is Ingredient or $this is ClinicalUseDefinition or $this is RegulatedAuthorization or $this is SubstanceDefinition or $this is SubscriptionStatus or $this is SubscriptionTopic) implies (%resource is Citation or %resource is Evidence or %resource is EvidenceReport or %resource is EvidenceVariable or %resource is MedicinalProductDefinition or %resource is PackagedProductDefinition or %resource is AdministrableProductDefinition or %resource is Ingredient or %resource is ClinicalUseDefinition or %resource is RegulatedAuthorization or %resource is SubstanceDefinition or %resource is SubscriptionStatus or %resource is SubscriptionTopic)",
    source = "http://hl7.org/fhir/StructureDefinition/DomainResource"
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
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
     * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
     * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
     * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
     * content should be represented in the narrative to ensure clinical safety.
     * 
     * @return
     *     An immutable object of type {@link Narrative} that may be null.
     */
    public Narrative getText() {
        return text;
    }

    /**
     * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
     * identified independently, and nor can they have their own independent transaction scope.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Resource} that may be empty.
     */
    public List<Resource> getContained() {
        return contained;
    }

    /**
     * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
     * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
     * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
     * of the definition of the extension.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Extension} that may be empty.
     */
    public List<Extension> getExtension() {
        return extension;
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
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Extension} that may be empty.
     */
    public List<Extension> getModifierExtension() {
        return modifierExtension;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (text != null) || 
            !contained.isEmpty() || 
            !extension.isEmpty() || 
            !modifierExtension.isEmpty();
    }

    @Override
    public abstract Builder toBuilder();

    public static abstract class Builder extends Resource.Builder {
        protected Narrative text;
        protected List<Resource> contained = new ArrayList<>();
        protected List<Extension> extension = new ArrayList<>();
        protected List<Extension> modifierExtension = new ArrayList<>();

        protected Builder() {
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
        public Builder text(Narrative text) {
            this.text = text;
            return this;
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
        public Builder contained(Resource... contained) {
            for (Resource value : contained) {
                this.contained.add(value);
            }
            return this;
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
        public Builder contained(Collection<Resource> contained) {
            this.contained = new ArrayList<>(contained);
            return this;
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
        public Builder extension(Extension... extension) {
            for (Extension value : extension) {
                this.extension.add(value);
            }
            return this;
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
        public Builder extension(Collection<Extension> extension) {
            this.extension = new ArrayList<>(extension);
            return this;
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
        public Builder modifierExtension(Extension... modifierExtension) {
            for (Extension value : modifierExtension) {
                this.modifierExtension.add(value);
            }
            return this;
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
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            this.modifierExtension = new ArrayList<>(modifierExtension);
            return this;
        }

        @Override
        public abstract DomainResource build();

        protected void validate(DomainResource domainResource) {
            super.validate(domainResource);
            ValidationSupport.checkList(domainResource.contained, "contained", Resource.class);
            ValidationSupport.checkList(domainResource.extension, "extension", Extension.class);
            ValidationSupport.checkList(domainResource.modifierExtension, "modifierExtension", Extension.class);
        }

        protected Builder from(DomainResource domainResource) {
            super.from(domainResource);
            text = domainResource.text;
            contained.addAll(domainResource.contained);
            extension.addAll(domainResource.extension);
            modifierExtension.addAll(domainResource.modifierExtension);
            return this;
        }
    }
}
