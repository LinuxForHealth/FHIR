/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.compartment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CompartmentDefinition;
import com.ibm.fhir.model.resource.CompartmentDefinition.Resource;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;

/**
 * The compartments are defined using FHIR R4 CompartmentDefinitions. <br>
 * The R4 CompartmentDefintions and boundaries are defined at https://www.hl7.org/fhir/compartmentdefinition.html <br>
 * <br>
 * CompartmentDefintion:
 * <ul>
 * <li>Patient - https://www.hl7.org/fhir/compartmentdefinition-patient.json</li>
 * <li>Encounter - https://www.hl7.org/fhir/compartmentdefinition-encounter.json</li>
 * <li>RelatedPerson - https://www.hl7.org/fhir/compartmentdefinition-relatedperson.json</li>
 * <li>Practitioner - https://www.hl7.org/fhir/compartmentdefinition-practitioner.json</li>
 * <li>Device - https://www.hl7.org/fhir/compartmentdefinition-device.json</li>
 * </ul>
 *
 * This class extracts the Compartment Logic from SearchUtil, converts the Custom Compartment logic and format into the
 * ComponentDefintion, adds support for the the default definition.
 *
 * <br>
 * Load the class in the classloader to initialize static members. Call this before using the class in order to avoid a
 * slight performance hit on first use.
 */
public class CompartmentUtil {

    private static final String CLASSNAME = CompartmentUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    // FHIR:
    public static final String FHIR_PATH_BUNDLE_ENTRY = "entry.children()";
    public static final String RESOURCE = "/compartments.json";

    // List of compartmentDefinitions.
    private static final Set<String> compartmentDefinitions = new HashSet<String>() {

        private static final long serialVersionUID = 7152515293380769882L;

        {
            add("/compartments/compartmentdefinition-device.json");
            add("/compartments/compartmentdefinition-encounter.json");
            add("/compartments/compartmentdefinition-patient.json");
            add("/compartments/compartmentdefinition-practitioner.json");
            add("/compartments/compartmentdefinition-relatedperson.json");
        }
    };

    // Map of Compartment name to CompartmentCache
    private static final Map<String, CompartmentCache> compartmentMap = new HashMap<>();

    // Map of Resource type to ResourceCompartmentCache
    private static final Map<String, ResourceCompartmentCache> resourceCompartmentMap = new HashMap<>();

    static {
        // make one pass over the configuration to build both maps
        buildMaps(compartmentMap, resourceCompartmentMap);
    }


    /**
     * Loads the class in the classloader to initialize static members. Call this before using the class in order to
     * avoid a slight performance hit on first use.
     */
    public static void init() {
        // No Operation
    }

    // Exceptions:
    public static final String PARSE_EXCEPTION = "Unable to parse the entry that is read from compartments.json %s";
    public static final String IO_EXCEPTION = "Unable to read the entry that is read from compartments.json %s";
    public static final String INVALID_COMPARTMENT = "Invalid compartment: %s";
    public static final String INVALID_COMPARTMENT_AND_RESOURCE = "Invalid resource type: %s for compartment: %s";
    public static final String FROM_STREAM = "from_stream";

    private CompartmentUtil() {
        // No Operation
    }

    /**
     * Builds an in-memory model of the Compartment map defined in compartments.json, for supporting compartment based
     * FHIR searches.
     * @implNote the maps being built are passed in as arguments to aid unit testing
     * @param compMap map of compartment name to CompartmentCache
     * @param resourceCompMap map of resource type name to ResourceCompartmentCache
     * @throws IOException
     */
    public static final void buildMaps(Map<String, CompartmentCache> compMap, Map<String, ResourceCompartmentCache> resourceCompMap) {
        buildMaps(RESOURCE, compMap, resourceCompMap);
    }

    /**
     * Builds an in-memory model of the Compartment map defined in compartments.json, for supporting compartment based
     * FHIR searches.
     * @implNote the maps being built are passed in as arguments to aid unit testing
     * @param the source resource to be read using getResourceAsStream
     * @param compMap map of compartment name to CompartmentCache
     * @param resourceCompMap map of resource type name to ResourceCompartmentCache
     * @throws IOException
     */
    public static final void buildMaps(String source, Map<String, CompartmentCache> compMap, Map<String, ResourceCompartmentCache> resourceCompMap) {
        if (compMap == null) {
            throw new IllegalArgumentException("compMap must not be null");
        }

        if (resourceCompMap == null) {
            throw new IllegalArgumentException("resourceCompMap must not be null");
        }

        try (InputStreamReader reader = new InputStreamReader(CompartmentUtil.class.getResourceAsStream(source))) {
            Bundle bundle = FHIRParser.parser(Format.JSON).parse(reader);

            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(bundle);

            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, FHIR_PATH_BUNDLE_ENTRY);

            Iterator<FHIRPathNode> iter = result.iterator();
            while (iter.hasNext()) {
                FHIRPathResourceNode node = iter.next().asResourceNode();

                // Convert to Resource and lookup.
                CompartmentDefinition compartmentDefinition = node.resource().as(CompartmentDefinition.class);
                String compartmentName = compartmentDefinition.getCode().getValue();

                // The cached object (a smaller/lighter lookup resource) used for point lookups
                CompartmentCache compartmentDefinitionCache = new CompartmentCache();

                // Iterates over the resources embedded in the CompartmentDefinition.
                for (Resource resource : compartmentDefinition.getResource()) {
                    String inclusionCode = resource.getCode().getValue();
                    List<com.ibm.fhir.model.type.String> params = resource.getParam();
                    // Make sure to only add the valid resource types (at least with one inclusion) instead of all types.
                    if (!params.isEmpty()) {
                        compartmentDefinitionCache.add(inclusionCode, params);

                        // Look up the ResourceCompartmentCache and create a new one if needed
                        ResourceCompartmentCache rcc = resourceCompMap.get(inclusionCode);
                        if (rcc == null) {
                            rcc = new ResourceCompartmentCache();
                            resourceCompMap.put(inclusionCode, rcc);
                        }

                        // Add the mapping for this parameter to the target compartment name
                        rcc.add(params, compartmentName);
                    }
                }

                compMap.put(compartmentName, compartmentDefinitionCache);
            }

        } catch (FHIRException e) {
            log.warning(String.format(PARSE_EXCEPTION, FROM_STREAM));
        } catch (IOException e1) {
            log.warning(String.format(IO_EXCEPTION, FROM_STREAM));
        }
    }

    /**
     * gets the compartment
     *
     * @param compartment
     * @return
     * @throws FHIRSearchException
     */
    public static List<String> getCompartmentResourceTypes(final String compartment) throws FHIRSearchException {
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
    public static List<String> getCompartmentResourceTypeInclusionCriteria(final String compartment, final String resourceType) throws FHIRSearchException {
        checkValidCompartmentAndResource(compartment, resourceType);
        return compartmentMap.get(compartment).getParametersByResourceTypeInCompartment(resourceType);
    }

    /**
     * checks that the compartment is valid, and throws and exception if, not
     *
     * @param compartment
     * @throws FHIRSearchException
     */
    public static void checkValidCompartment(final String compartment) throws FHIRSearchException {
        if (!compartmentMap.containsKey(compartment)) {
            String msg = String.format(INVALID_COMPARTMENT, compartment);
            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
        }
    }

    /**
     * checks that the compartment and resource are valid, and throws and exception if, not
     *
     * @param compartment
     * @throws FHIRSearchException
     */
    public static void checkValidCompartmentAndResource(final String compartment, final String resourceType) throws FHIRSearchException {
        checkValidCompartment(compartment);

        if (compartmentMap.get(compartment).getParametersByResourceTypeInCompartment(resourceType).isEmpty()) {
            String msg = String.format(INVALID_COMPARTMENT_AND_RESOURCE, resourceType, compartment);
            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
        }
    }

    /**
     * builds the bundle and the resources for the compartments.json and puts out to the output stream.
     *
     * @throws FHIRGeneratorException
     */
    public static void buildCompositeBundle(PrintStream out) throws FHIRGeneratorException {

        Bundle.Builder build = Bundle.builder().type(BundleType.COLLECTION);
        for (String compartmentDefintion : compartmentDefinitions) {

            try (InputStreamReader reader = new InputStreamReader(CompartmentUtil.class.getResourceAsStream(compartmentDefintion))) {
                CompartmentDefinition compartmentDefinitionResource = FHIRParser.parser(Format.JSON).parse(reader);

                build.entry(Bundle.Entry.builder().resource(compartmentDefinitionResource).build());

            } catch (FHIRException e) {
                log.warning(String.format(PARSE_EXCEPTION, compartmentDefintion));
            } catch (IOException e1) {
                log.warning(String.format(IO_EXCEPTION, compartmentDefintion));
            }
        }

        Bundle bundle = build.build();
        FHIRGenerator.generator(Format.JSON, true).generate(bundle, out);
        out.println(bundle.toString());

    }

    /**
     * Get the map of parameter names used as compartment references for the
     * given resource type. For example for CareTeam:
     *   participant -> {RelatedPerson, Patient}
     *   patient -> {Patient}
     *   encounter -> {Encounter}
     *   ...
     * etc.
     * @param resourceType the resource type name
     * @return a map of parameter name to set of compartment names
     */
    public static Map<String, Set<java.lang.String>> getCompartmentParamsForResourceType(java.lang.String resourceType) {
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