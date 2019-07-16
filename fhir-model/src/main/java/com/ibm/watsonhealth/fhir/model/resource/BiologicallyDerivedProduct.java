/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.BiologicallyDerivedProductCategory;
import com.ibm.watsonhealth.fhir.model.type.BiologicallyDerivedProductStatus;
import com.ibm.watsonhealth.fhir.model.type.BiologicallyDerivedProductStorageScale;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A material substance originating from a biological entity intended to be transplanted or infused
 * into another (possibly the same) biological entity.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class BiologicallyDerivedProduct extends DomainResource {
    private final List<Identifier> identifier;
    private final BiologicallyDerivedProductCategory productCategory;
    private final CodeableConcept productCode;
    private final BiologicallyDerivedProductStatus status;
    private final List<Reference> request;
    private final Integer quantity;
    private final List<Reference> parent;
    private final Collection collection;
    private final List<Processing> processing;
    private final Manipulation manipulation;
    private final List<Storage> storage;

    private volatile int hashCode;

    private BiologicallyDerivedProduct(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        productCategory = builder.productCategory;
        productCode = builder.productCode;
        status = builder.status;
        request = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.request, "request"));
        quantity = builder.quantity;
        parent = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.parent, "parent"));
        collection = builder.collection;
        processing = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.processing, "processing"));
        manipulation = builder.manipulation;
        storage = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.storage, "storage"));
    }

    /**
     * <p>
     * This records identifiers associated with this biologically derived product instance that are defined by business 
     * processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in 
     * CDA documents, or in written / printed documentation).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * Broad category of this product.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link BiologicallyDerivedProductCategory}.
     */
    public BiologicallyDerivedProductCategory getProductCategory() {
        return productCategory;
    }

    /**
     * <p>
     * A code that identifies the kind of this biologically derived product (SNOMED Ctcode).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getProductCode() {
        return productCode;
    }

    /**
     * <p>
     * Whether the product is currently available.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link BiologicallyDerivedProductStatus}.
     */
    public BiologicallyDerivedProductStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Procedure request to obtain this biologically derived product.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getRequest() {
        return request;
    }

    /**
     * <p>
     * Number of discrete units within this product.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Integer}.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * <p>
     * Parent product (if any).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getParent() {
        return parent;
    }

    /**
     * <p>
     * How this product was collected.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Collection}.
     */
    public Collection getCollection() {
        return collection;
    }

    /**
     * <p>
     * Any processing of the product during collection that does not change the fundamental nature of the product. For 
     * example adding anti-coagulants during the collection of Peripheral Blood Stem Cells.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Processing}.
     */
    public List<Processing> getProcessing() {
        return processing;
    }

    /**
     * <p>
     * Any manipulation of product post-collection that is intended to alter the product. For example a buffy-coat enrichment 
     * or CD8 reduction of Peripheral Blood Stem Cells to make it more suitable for infusion.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Manipulation}.
     */
    public Manipulation getManipulation() {
        return manipulation;
    }

    /**
     * <p>
     * Product storage.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Storage}.
     */
    public List<Storage> getStorage() {
        return storage;
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
                accept(productCategory, "productCategory", visitor);
                accept(productCode, "productCode", visitor);
                accept(status, "status", visitor);
                accept(request, "request", visitor, Reference.class);
                accept(quantity, "quantity", visitor);
                accept(parent, "parent", visitor, Reference.class);
                accept(collection, "collection", visitor);
                accept(processing, "processing", visitor, Processing.class);
                accept(manipulation, "manipulation", visitor);
                accept(storage, "storage", visitor, Storage.class);
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
        BiologicallyDerivedProduct other = (BiologicallyDerivedProduct) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(productCategory, other.productCategory) && 
            Objects.equals(productCode, other.productCode) && 
            Objects.equals(status, other.status) && 
            Objects.equals(request, other.request) && 
            Objects.equals(quantity, other.quantity) && 
            Objects.equals(parent, other.parent) && 
            Objects.equals(collection, other.collection) && 
            Objects.equals(processing, other.processing) && 
            Objects.equals(manipulation, other.manipulation) && 
            Objects.equals(storage, other.storage);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                text, 
                contained, 
                extension, 
                modifierExtension, 
                identifier, 
                productCategory, 
                productCode, 
                status, 
                request, 
                quantity, 
                parent, 
                collection, 
                processing, 
                manipulation, 
                storage);
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

    public static class Builder extends DomainResource.Builder {
        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private BiologicallyDerivedProductCategory productCategory;
        private CodeableConcept productCode;
        private BiologicallyDerivedProductStatus status;
        private List<Reference> request = new ArrayList<>();
        private Integer quantity;
        private List<Reference> parent = new ArrayList<>();
        private Collection collection;
        private List<Processing> processing = new ArrayList<>();
        private Manipulation manipulation;
        private List<Storage> storage = new ArrayList<>();

        private Builder() {
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(java.util.Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
        public Builder extension(java.util.Collection<Extension> extension) {
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * This records identifiers associated with this biologically derived product instance that are defined by business 
         * processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in 
         * CDA documents, or in written / printed documentation).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     External ids for this item
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * This records identifiers associated with this biologically derived product instance that are defined by business 
         * processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in 
         * CDA documents, or in written / printed documentation).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     External ids for this item
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(java.util.Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * <p>
         * Broad category of this product.
         * </p>
         * 
         * @param productCategory
         *     organ | tissue | fluid | cells | biologicalAgent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder productCategory(BiologicallyDerivedProductCategory productCategory) {
            this.productCategory = productCategory;
            return this;
        }

        /**
         * <p>
         * A code that identifies the kind of this biologically derived product (SNOMED Ctcode).
         * </p>
         * 
         * @param productCode
         *     What this biologically derived product is
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder productCode(CodeableConcept productCode) {
            this.productCode = productCode;
            return this;
        }

        /**
         * <p>
         * Whether the product is currently available.
         * </p>
         * 
         * @param status
         *     available | unavailable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(BiologicallyDerivedProductStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * Procedure request to obtain this biologically derived product.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param request
         *     Procedure request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(Reference... request) {
            for (Reference value : request) {
                this.request.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Procedure request to obtain this biologically derived product.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param request
         *     Procedure request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(java.util.Collection<Reference> request) {
            this.request = new ArrayList<>(request);
            return this;
        }

        /**
         * <p>
         * Number of discrete units within this product.
         * </p>
         * 
         * @param quantity
         *     The amount of this biologically derived product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * <p>
         * Parent product (if any).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param parent
         *     BiologicallyDerivedProduct parent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parent(Reference... parent) {
            for (Reference value : parent) {
                this.parent.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Parent product (if any).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param parent
         *     BiologicallyDerivedProduct parent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parent(java.util.Collection<Reference> parent) {
            this.parent = new ArrayList<>(parent);
            return this;
        }

        /**
         * <p>
         * How this product was collected.
         * </p>
         * 
         * @param collection
         *     How this product was collected
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder collection(Collection collection) {
            this.collection = collection;
            return this;
        }

        /**
         * <p>
         * Any processing of the product during collection that does not change the fundamental nature of the product. For 
         * example adding anti-coagulants during the collection of Peripheral Blood Stem Cells.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param processing
         *     Any processing of the product during collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder processing(Processing... processing) {
            for (Processing value : processing) {
                this.processing.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Any processing of the product during collection that does not change the fundamental nature of the product. For 
         * example adding anti-coagulants during the collection of Peripheral Blood Stem Cells.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param processing
         *     Any processing of the product during collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder processing(java.util.Collection<Processing> processing) {
            this.processing = new ArrayList<>(processing);
            return this;
        }

        /**
         * <p>
         * Any manipulation of product post-collection that is intended to alter the product. For example a buffy-coat enrichment 
         * or CD8 reduction of Peripheral Blood Stem Cells to make it more suitable for infusion.
         * </p>
         * 
         * @param manipulation
         *     Any manipulation of product post-collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manipulation(Manipulation manipulation) {
            this.manipulation = manipulation;
            return this;
        }

        /**
         * <p>
         * Product storage.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param storage
         *     Product storage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder storage(Storage... storage) {
            for (Storage value : storage) {
                this.storage.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Product storage.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param storage
         *     Product storage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder storage(java.util.Collection<Storage> storage) {
            this.storage = new ArrayList<>(storage);
            return this;
        }

        @Override
        public BiologicallyDerivedProduct build() {
            return new BiologicallyDerivedProduct(this);
        }

        private Builder from(BiologicallyDerivedProduct biologicallyDerivedProduct) {
            id = biologicallyDerivedProduct.id;
            meta = biologicallyDerivedProduct.meta;
            implicitRules = biologicallyDerivedProduct.implicitRules;
            language = biologicallyDerivedProduct.language;
            text = biologicallyDerivedProduct.text;
            contained.addAll(biologicallyDerivedProduct.contained);
            extension.addAll(biologicallyDerivedProduct.extension);
            modifierExtension.addAll(biologicallyDerivedProduct.modifierExtension);
            identifier.addAll(biologicallyDerivedProduct.identifier);
            productCategory = biologicallyDerivedProduct.productCategory;
            productCode = biologicallyDerivedProduct.productCode;
            status = biologicallyDerivedProduct.status;
            request.addAll(biologicallyDerivedProduct.request);
            quantity = biologicallyDerivedProduct.quantity;
            parent.addAll(biologicallyDerivedProduct.parent);
            collection = biologicallyDerivedProduct.collection;
            processing.addAll(biologicallyDerivedProduct.processing);
            manipulation = biologicallyDerivedProduct.manipulation;
            storage.addAll(biologicallyDerivedProduct.storage);
            return this;
        }
    }

    /**
     * <p>
     * How this product was collected.
     * </p>
     */
    public static class Collection extends BackboneElement {
        private final Reference collector;
        private final Reference source;
        private final Element collected;

        private volatile int hashCode;

        private Collection(Builder builder) {
            super(builder);
            collector = builder.collector;
            source = builder.source;
            collected = ValidationSupport.choiceElement(builder.collected, "collected", DateTime.class, Period.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Healthcare professional who is performing the collection.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getCollector() {
            return collector;
        }

        /**
         * <p>
         * The patient or entity, such as a hospital or vendor in the case of a processed/manipulated/manufactured product, 
         * providing the product.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getSource() {
            return source;
        }

        /**
         * <p>
         * Time of product collection.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getCollected() {
            return collected;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (collector != null) || 
                (source != null) || 
                (collected != null);
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
                    accept(collector, "collector", visitor);
                    accept(source, "source", visitor);
                    accept(collected, "collected", visitor);
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
            Collection other = (Collection) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(collector, other.collector) && 
                Objects.equals(source, other.source) && 
                Objects.equals(collected, other.collected);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    collector, 
                    source, 
                    collected);
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
            private Reference collector;
            private Reference source;
            private Element collected;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Healthcare professional who is performing the collection.
             * </p>
             * 
             * @param collector
             *     Individual performing collection
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder collector(Reference collector) {
                this.collector = collector;
                return this;
            }

            /**
             * <p>
             * The patient or entity, such as a hospital or vendor in the case of a processed/manipulated/manufactured product, 
             * providing the product.
             * </p>
             * 
             * @param source
             *     Who is product from
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference source) {
                this.source = source;
                return this;
            }

            /**
             * <p>
             * Time of product collection.
             * </p>
             * 
             * @param collected
             *     Time of product collection
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder collected(Element collected) {
                this.collected = collected;
                return this;
            }

            @Override
            public Collection build() {
                return new Collection(this);
            }

            private Builder from(Collection collection) {
                id = collection.id;
                extension.addAll(collection.extension);
                modifierExtension.addAll(collection.modifierExtension);
                collector = collection.collector;
                source = collection.source;
                collected = collection.collected;
                return this;
            }
        }
    }

    /**
     * <p>
     * Any processing of the product during collection that does not change the fundamental nature of the product. For 
     * example adding anti-coagulants during the collection of Peripheral Blood Stem Cells.
     * </p>
     */
    public static class Processing extends BackboneElement {
        private final String description;
        private final CodeableConcept procedure;
        private final Reference additive;
        private final Element time;

        private volatile int hashCode;

        private Processing(Builder builder) {
            super(builder);
            description = builder.description;
            procedure = builder.procedure;
            additive = builder.additive;
            time = ValidationSupport.choiceElement(builder.time, "time", DateTime.class, Period.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Description of of processing.
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
         * Procesing code.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getProcedure() {
            return procedure;
        }

        /**
         * <p>
         * Substance added during processing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getAdditive() {
            return additive;
        }

        /**
         * <p>
         * Time of processing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getTime() {
            return time;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (procedure != null) || 
                (additive != null) || 
                (time != null);
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
                    accept(description, "description", visitor);
                    accept(procedure, "procedure", visitor);
                    accept(additive, "additive", visitor);
                    accept(time, "time", visitor);
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
            Processing other = (Processing) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(procedure, other.procedure) && 
                Objects.equals(additive, other.additive) && 
                Objects.equals(time, other.time);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    procedure, 
                    additive, 
                    time);
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
            private String description;
            private CodeableConcept procedure;
            private Reference additive;
            private Element time;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Description of of processing.
             * </p>
             * 
             * @param description
             *     Description of of processing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * <p>
             * Procesing code.
             * </p>
             * 
             * @param procedure
             *     Procesing code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder procedure(CodeableConcept procedure) {
                this.procedure = procedure;
                return this;
            }

            /**
             * <p>
             * Substance added during processing.
             * </p>
             * 
             * @param additive
             *     Substance added during processing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder additive(Reference additive) {
                this.additive = additive;
                return this;
            }

            /**
             * <p>
             * Time of processing.
             * </p>
             * 
             * @param time
             *     Time of processing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder time(Element time) {
                this.time = time;
                return this;
            }

            @Override
            public Processing build() {
                return new Processing(this);
            }

            private Builder from(Processing processing) {
                id = processing.id;
                extension.addAll(processing.extension);
                modifierExtension.addAll(processing.modifierExtension);
                description = processing.description;
                procedure = processing.procedure;
                additive = processing.additive;
                time = processing.time;
                return this;
            }
        }
    }

    /**
     * <p>
     * Any manipulation of product post-collection that is intended to alter the product. For example a buffy-coat enrichment 
     * or CD8 reduction of Peripheral Blood Stem Cells to make it more suitable for infusion.
     * </p>
     */
    public static class Manipulation extends BackboneElement {
        private final String description;
        private final Element time;

        private volatile int hashCode;

        private Manipulation(Builder builder) {
            super(builder);
            description = builder.description;
            time = ValidationSupport.choiceElement(builder.time, "time", DateTime.class, Period.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Description of manipulation.
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
         * Time of manipulation.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getTime() {
            return time;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (time != null);
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
                    accept(description, "description", visitor);
                    accept(time, "time", visitor);
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
            Manipulation other = (Manipulation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(time, other.time);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    time);
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
            private String description;
            private Element time;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Description of manipulation.
             * </p>
             * 
             * @param description
             *     Description of manipulation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * <p>
             * Time of manipulation.
             * </p>
             * 
             * @param time
             *     Time of manipulation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder time(Element time) {
                this.time = time;
                return this;
            }

            @Override
            public Manipulation build() {
                return new Manipulation(this);
            }

            private Builder from(Manipulation manipulation) {
                id = manipulation.id;
                extension.addAll(manipulation.extension);
                modifierExtension.addAll(manipulation.modifierExtension);
                description = manipulation.description;
                time = manipulation.time;
                return this;
            }
        }
    }

    /**
     * <p>
     * Product storage.
     * </p>
     */
    public static class Storage extends BackboneElement {
        private final String description;
        private final Decimal temperature;
        private final BiologicallyDerivedProductStorageScale scale;
        private final Period duration;

        private volatile int hashCode;

        private Storage(Builder builder) {
            super(builder);
            description = builder.description;
            temperature = builder.temperature;
            scale = builder.scale;
            duration = builder.duration;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Description of storage.
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
         * Storage temperature.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getTemperature() {
            return temperature;
        }

        /**
         * <p>
         * Temperature scale used.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link BiologicallyDerivedProductStorageScale}.
         */
        public BiologicallyDerivedProductStorageScale getScale() {
            return scale;
        }

        /**
         * <p>
         * Storage timeperiod.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getDuration() {
            return duration;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (temperature != null) || 
                (scale != null) || 
                (duration != null);
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
                    accept(description, "description", visitor);
                    accept(temperature, "temperature", visitor);
                    accept(scale, "scale", visitor);
                    accept(duration, "duration", visitor);
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
            Storage other = (Storage) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(temperature, other.temperature) && 
                Objects.equals(scale, other.scale) && 
                Objects.equals(duration, other.duration);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    temperature, 
                    scale, 
                    duration);
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
            private String description;
            private Decimal temperature;
            private BiologicallyDerivedProductStorageScale scale;
            private Period duration;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Description of storage.
             * </p>
             * 
             * @param description
             *     Description of storage
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * <p>
             * Storage temperature.
             * </p>
             * 
             * @param temperature
             *     Storage temperature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder temperature(Decimal temperature) {
                this.temperature = temperature;
                return this;
            }

            /**
             * <p>
             * Temperature scale used.
             * </p>
             * 
             * @param scale
             *     farenheit | celsius | kelvin
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder scale(BiologicallyDerivedProductStorageScale scale) {
                this.scale = scale;
                return this;
            }

            /**
             * <p>
             * Storage timeperiod.
             * </p>
             * 
             * @param duration
             *     Storage timeperiod
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder duration(Period duration) {
                this.duration = duration;
                return this;
            }

            @Override
            public Storage build() {
                return new Storage(this);
            }

            private Builder from(Storage storage) {
                id = storage.id;
                extension.addAll(storage.extension);
                modifierExtension.addAll(storage.modifierExtension);
                description = storage.description;
                temperature = storage.temperature;
                scale = storage.scale;
                duration = storage.duration;
                return this;
            }
        }
    }
}
