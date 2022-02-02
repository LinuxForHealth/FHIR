/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.core.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.ResourceTypeName;

/**
 * Helper methods for working with FHIR Resource Type Strings
 */
public class ResourceTypeHelper {
    private static final Set<ResourceTypeName> REMOVED_RESOURCE_TYPES = collectRemovedResourceTypes();
    private static final Set<ResourceTypeName> R4B_ONLY_RESOURCE_TYPES = collectR4bOnlyResourceTypes();

    private static final Set<ResourceTypeName> ABSTRACT_TYPES = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList(
                ResourceTypeName.RESOURCE,
                ResourceTypeName.DOMAIN_RESOURCE
            )));

    private static final Set<String> R4_RESOURCES = Collections.unmodifiableSet(new LinkedHashSet<>(
            Arrays.stream(ResourceTypeName.values())
                .filter(rtn -> !REMOVED_RESOURCE_TYPES.contains(rtn))
                .filter(rtn -> !R4B_ONLY_RESOURCE_TYPES.contains(rtn))
                .filter(rtn -> !ABSTRACT_TYPES.contains(rtn))
                .map(ResourceTypeName::value)
                .collect(Collectors.toList())));

    private static final Set<String> R4B_RESOURCES = Collections.unmodifiableSet(new LinkedHashSet<>(
            Arrays.stream(ResourceTypeName.values())
                .filter(rtn -> !REMOVED_RESOURCE_TYPES.contains(rtn))
                .filter(rtn -> !ABSTRACT_TYPES.contains(rtn))
                .map(ResourceTypeName::value)
                .collect(Collectors.toList())));

    private static final Set<String> R4B_ONLY_RESOURCES = Collections.unmodifiableSet(new LinkedHashSet<>(
            R4B_ONLY_RESOURCE_TYPES.stream()
                .map(ResourceTypeName::value)
                .collect(Collectors.toList())));

    /**
     * @param fhirVersion The value of the MIME-type parameter 'fhirVersion' for the current interaction
     *          (e.g. "4.3" not "4.3.0")
     * @return a set of resource type names that corresponds to the requested fhirVersion
     */
    public static Set<String> getResourceTypesFor(FHIRVersionParam fhirVersion) {
        switch (fhirVersion) {
        case VERSION_43:
            return R4B_RESOURCES;
        case VERSION_40:
        default:
            return R4_RESOURCES;
        }
    }

    /**
     * @return the set of resource type names that were either introduced in 4.3.0 (e.g. Ingredient) or changed
     *          in backwards-incompatible ways in the 4.3.0 release (e.g. Evidence and EvidenceVariable)
     */
    public static Set<String> getNewOrBreakingResourceTypeNames() {
        return R4B_ONLY_RESOURCES;
    }

    private static Set<ResourceTypeName> collectRemovedResourceTypes() {
        Set<ResourceTypeName> set = new HashSet<>();
        set.add(ResourceTypeName.EFFECT_EVIDENCE_SYNTHESIS);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_AUTHORIZATION);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_CONTRAINDICATION);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_INDICATION);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_INGREDIENT);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_INTERACTION);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_MANUFACTURED);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_PACKAGED);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_PHARMACEUTICAL);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT);
        set.add(ResourceTypeName.RISK_EVIDENCE_SYNTHESIS);
        set.add(ResourceTypeName.SUBSTANCE_NUCLEIC_ACID);
        set.add(ResourceTypeName.SUBSTANCE_POLYMER);
        set.add(ResourceTypeName.SUBSTANCE_PROTEIN);
        set.add(ResourceTypeName.SUBSTANCE_REFERENCE_INFORMATION);
        set.add(ResourceTypeName.SUBSTANCE_SOURCE_MATERIAL);
        set.add(ResourceTypeName.SUBSTANCE_SPECIFICATION);
        return set;
    }

    private static Set<ResourceTypeName> collectR4bOnlyResourceTypes() {
        Set<ResourceTypeName> set = new HashSet<>();
        set.add(ResourceTypeName.ADMINISTRABLE_PRODUCT_DEFINITION);
        set.add(ResourceTypeName.CITATION);
        set.add(ResourceTypeName.CLINICAL_USE_DEFINITION);
        set.add(ResourceTypeName.EVIDENCE_REPORT);
        set.add(ResourceTypeName.INGREDIENT);
        set.add(ResourceTypeName.MANUFACTURED_ITEM_DEFINITION);
        set.add(ResourceTypeName.MEDICINAL_PRODUCT_DEFINITION);
        set.add(ResourceTypeName.NUTRITION_PRODUCT);
        set.add(ResourceTypeName.PACKAGED_PRODUCT_DEFINITION);
        set.add(ResourceTypeName.REGULATED_AUTHORIZATION);
        set.add(ResourceTypeName.SUBSCRIPTION_STATUS);
        set.add(ResourceTypeName.SUBSCRIPTION_TOPIC);
        set.add(ResourceTypeName.SUBSTANCE_DEFINITION);
        // The following resource types existed in R4, but have breaking changes in R4B.
        // Because we only support the R4B version, we don't want to advertise these in our 4.0.1 statement.
        set.add(ResourceTypeName.DEVICE_DEFINITION);
        set.add(ResourceTypeName.EVIDENCE);
        set.add(ResourceTypeName.EVIDENCE_VARIABLE);
        // TODO: make final decision on whether to lump these in with the breaking resources
        // R4B_ONLY_RESOURCES.add(ResourceTypeName.PLAN_DEFINITION);
        // R4B_ONLY_RESOURCES.add(ResourceTypeName.ACTIVITY_DEFINITION);
        return set;
    }
}
