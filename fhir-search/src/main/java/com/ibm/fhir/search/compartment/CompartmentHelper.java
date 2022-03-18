/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.compartment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.model.resource.CompartmentDefinition;
import com.ibm.fhir.model.resource.CompartmentDefinition.Resource;
import com.ibm.fhir.model.type.code.CompartmentType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;

/**
 * A helper class with methods for working with HL7 FHIR compartments.<br>
 * The compartments are defined by the CompartmentDefinition resources in FHIRRegistry.<br>
 * The R4 CompartmentDefintions and boundaries are defined at https://hl7.org/fhir/R4/compartmentdefinition.html<br>
 * <br>
 * Default CompartmentDefintion:
 * <ul>
 * <li>Patient - https://hl7.org/fhir/R4/compartmentdefinition-patient.json</li>
 * <li>Encounter - https://hl7.org/fhir/R4/compartmentdefinition-encounter.json</li>
 * <li>RelatedPerson - https://hl7.org/fhir/R4/compartmentdefinition-relatedperson.json</li>
 * <li>Practitioner - https://hl7.org/fhir/R4/compartmentdefinition-practitioner.json</li>
 * <li>Device - https://hl7.org/fhir/R4/compartmentdefinition-device.json</li>
 * </ul>
 */
public class CompartmentHelper {
    private static final Logger LOG = Logger.getLogger(CompartmentHelper.class.getName());

    // The URL of the compartment subtype extension...useful for defining new compartments
    public static final String CUSTOM_COMPARTMENT_TYPE_EXT = FHIRConstants.EXT_BASE + "custom-compartment-type";

    // Exceptions:
    public static final String INVALID_COMPARTMENT = "Invalid compartment: %s";
    public static final String INVALID_COMPARTMENT_AND_RESOURCE = "Invalid resource type: %s for compartment: %s";

    // Map of Compartment name to CompartmentCache
    private final Map<String, CompartmentCache> compartmentMap = new HashMap<>();

    // Map of Inclusion resource type to ResourceCompartmentCache
    private final Map<String, ResourceCompartmentCache> resourceCompartmentMap = new HashMap<>();

    public CompartmentHelper() {
        // make one pass over the CompartmentDefinitions to build both maps
        buildMaps(compartmentMap, resourceCompartmentMap);

        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("built compartmentMap with keys: " + compartmentMap.keySet());
            if (LOG.isLoggable(Level.FINER)) {
                for (Entry<String, CompartmentCache> entry : compartmentMap.entrySet()) {
                    CompartmentCache cc = entry.getValue();
                    Map<String, Set<String>> paramsByResourceType = cc.getResourceTypesInCompartment().stream()
                        .collect(Collectors.toMap(t -> t, t -> cc.getParametersByResourceTypeInCompartment(t)));
                    LOG.finer(entry.getKey() + ": " + paramsByResourceType);
                }
            }
            LOG.fine("built resourceCompartmentMap with keys: " + resourceCompartmentMap.keySet());
            if (LOG.isLoggable(Level.FINER)) {
                for (Entry<String, ResourceCompartmentCache> entry : resourceCompartmentMap.entrySet()) {
                    LOG.finer(entry.getKey() + ": " + entry.getValue().getCompartmentReferenceParams());
                }
            }
        }
    }

    /**
     * Builds an in-memory model of the Compartment map defined in compartments.json, for supporting compartment based
     * FHIR searches.
     * @implNote the maps being built are passed in as arguments to aid unit testing
     * @param compMap map of compartment name to CompartmentCache
     * @param resourceCompMap map of resource type name to ResourceCompartmentCache
     */
    public static final void buildMaps(Map<String, CompartmentCache> compMap, Map<String, ResourceCompartmentCache> resourceCompMap) {
        Objects.requireNonNull(compMap, "compMap");
        Objects.requireNonNull(compMap, "resourceCompMap");

        Collection<CompartmentDefinition> definitions = FHIRRegistry.getInstance().getResources(CompartmentDefinition.class);
        if (LOG.isLoggable(Level.FINER)) {
            LOG.finer("using the following compartment definitions from the registry: " + definitions);
        }

        for (CompartmentDefinition compartmentDefinition : definitions) {
            CompartmentType type = compartmentDefinition.getCode();

            String compartmentName = type.hasValue() ? type.getValue()
                    : FHIRUtil.getExtensionStringValue(type, CUSTOM_COMPARTMENT_TYPE_EXT);

            // The cached object (a smaller/lighter lookup resource) used for point lookups
            CompartmentCache compartmentDefinitionCache = new CompartmentCache();

            // Iterates over the resources embedded in the CompartmentDefinition.
            for (Resource resource : compartmentDefinition.getResource()) {
                String inclusionResourceCode = resource.getCode().getValue();
                List<com.ibm.fhir.model.type.String> params = resource.getParam();
                // Make sure to only add the valid resource types (at least with one inclusion) instead of all types.
                if (!params.isEmpty()) {
                    compartmentDefinitionCache.add(inclusionResourceCode, resource.getParam());

                    // Look up the ResourceCompartmentCache and create a new one if needed
                    ResourceCompartmentCache rcc = resourceCompMap.get(inclusionResourceCode);
                    if (rcc == null) {
                        rcc = new ResourceCompartmentCache();
                        resourceCompMap.put(inclusionResourceCode, rcc);
                    }

                    // Add the mapping for this parameter to the target compartment name
                    rcc.add(params, compartmentName);
                }
            }

            compMap.put(compartmentName, compartmentDefinitionCache);
        }
    }

    /**
     * get the resource types that can be in the compartment
     *
     * @param compartment the compartment code value (resourceType name) to look up
     * @return
     * @throws FHIRSearchException
     */
    public Set<String> getCompartmentResourceTypes(final String compartment) throws FHIRSearchException {
        checkValidCompartment(compartment);
        return compartmentMap.get(compartment).getResourceTypesInCompartment();
    }

    /**
     * gets the compartment and resource type inclusion criteria.
     *
     * @param compartment
     * @param resourceType
     * @return
     * @throws FHIRSearchException if the passed resourceType does not exist within the passed compartment
     */
    public Set<String> getCompartmentResourceTypeInclusionCriteria(final String compartment, final String resourceType) throws FHIRSearchException {
        checkValidCompartmentAndResource(compartment, resourceType);
        return compartmentMap.get(compartment).getParametersByResourceTypeInCompartment(resourceType);
    }

    /**
     * @param compartment
     * @return whether the passed string matches the name of a configured CompartmentDefinition
     */
    public boolean isCompartmentType(final String compartment) {
        return compartmentMap.containsKey(compartment);
    }

    /**
     * checks that the compartment is valid and throws an exception if not
     *
     * @param compartment
     * @throws FHIRSearchException
     */
    public void checkValidCompartment(final String compartment) throws FHIRSearchException {
        if (!isCompartmentType(compartment)) {
            String msg = String.format(INVALID_COMPARTMENT, compartment);
            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
        }
    }

    /**
     * checks that the compartment and resource are valid and throws an exception if not
     *
     * @param compartment
     * @throws FHIRSearchException
     */
    public void checkValidCompartmentAndResource(final String compartment, final String resourceType) throws FHIRSearchException {
        checkValidCompartment(compartment);

        if (compartmentMap.get(compartment).getParametersByResourceTypeInCompartment(resourceType).isEmpty()) {
            String msg = String.format(INVALID_COMPARTMENT_AND_RESOURCE, resourceType, compartment);
            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
        }
    }

    /**
     * Get the map of parameter names used as compartment references for the
     * given resource type. For example for CareTeam:
     * <pre>
     *   participant -> {RelatedPerson, Patient}
     *   patient -> {Patient}
     *   encounter -> {Encounter}
     *   ...
     * </pre>
     * @param resourceType the resource type name
     * @return a map from parameter name to the set of compartment names
     *      for which that parameter is an inclusion criteria
     */
    public Map<String, Set<java.lang.String>> getCompartmentParamsForResourceType(java.lang.String resourceType) {
        ResourceCompartmentCache rcc = resourceCompartmentMap.get(resourceType);
        if (rcc != null) {
            return rcc.getCompartmentReferenceParams();
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * Create the special parameter name used for references to the given
     * compartment (e.g. Patient, RelatedPerson etc).
     * @param compartmentName
     * @return
     */
    public static String makeCompartmentParamName(String compartmentName) {
        final StringBuilder result = new StringBuilder();
        result.append("ibm-internal-")
            .append(compartmentName)
            .append("-Compartment");
        return result.toString();
    }
}