/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.compartment;

import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.eval;

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

import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathResourceNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.CompartmentDefinition;
import com.ibm.watsonhealth.fhir.model.resource.CompartmentDefinition.Resource;
import com.ibm.watsonhealth.fhir.model.type.BundleType;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.exception.SearchExceptionUtil;

/**
 * The compartments are defined using FHIR R4 CompartmentDefinitions. <br/>
 * The R4 CompartmentDefintions and boundaries are defined at https://www.hl7.org/fhir/compartmentdefinition.html <br/>
 * <br/>
 * CompartmentDefintion: <br/>
 * <li>Patient - https://www.hl7.org/fhir/compartmentdefinition-patient.json</li>
 * <li>Encounter - https://www.hl7.org/fhir/compartmentdefinition-encounter.json</li>
 * <li>RelatedPerson - https://www.hl7.org/fhir/compartmentdefinition-relatedperson.json</li>
 * <li>Practioner - https://www.hl7.org/fhir/compartmentdefinition-practitioner.json</li>
 * <li>Device - https://www.hl7.org/fhir/compartmentdefinition-device.json</li> <br/>
 * 
 * History:<br/>
 * 1 - Extracted the Compartment Logic from SearchUtil 2 - Converted the Custom Compartment logic and format into the
 * ComponentDefintion. 3 - Added support for the the default definition.
 * 
 * 
 * @author pbastide
 *
 */
public class CompartmentUtil {

    private static final String CLASSNAME = CompartmentUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    public static final String FHIR_PATH_BUNDLE_ENTRY = "entry.children()";

    private CompartmentUtil() {
        // No Operation
    }

    private static Map<String, CompartmentCache> compartmentMap = buildCompartmentMap();

    /**
     * Builds an in-memory model of the Compartment map defined in compartments.json, for supporting compartment based
     * FHIR searches.
     *
     * @return Map<String, CompartmentCache>
     * @throws IOException
     */
    public static final Map<String, CompartmentCache> buildCompartmentMap() {
        Map<String, CompartmentCache> cachedCompartmentMap = compartmentMap;

        if (cachedCompartmentMap == null) {
            // If cachedCompartmentMap is empty, there is something else going on.

            cachedCompartmentMap = new HashMap<>();

            try (InputStreamReader reader = new InputStreamReader(CompartmentUtil.class.getResourceAsStream("/compartments.json"))) {
                Bundle bundle = FHIRUtil.read(Bundle.class, Format.JSON, reader);

                FHIRPathTree tree = FHIRPathTree.tree(bundle);

                Collection<FHIRPathNode> result = eval(FHIR_PATH_BUNDLE_ENTRY, tree.getRoot());

                Iterator<FHIRPathNode> iter = result.iterator();
                while (iter.hasNext()) {
                    FHIRPathResourceNode node = iter.next().asResourceNode();

                    // Convert to Resource and lookup.
                    CompartmentDefinition compartmentDefinition = node.resource().as(CompartmentDefinition.class);

                    // The cached object (a smaller/lighter lookup resource) used for point lookups
                    CompartmentCache compartmentDefinitionCache = new CompartmentCache();

                    // Iterates over the resources embedded in the CompartmentDefinition.
                    for (Resource resource : compartmentDefinition.getResource()) {
                        String inclusionCode = resource.getCode().getValue();
                        List<com.ibm.watsonhealth.fhir.model.type.String> params = resource.getParam();
                        compartmentDefinitionCache.add(inclusionCode, params);
                    }

                    String codeCacheName = compartmentDefinition.getCode().getValue();
                    cachedCompartmentMap.put(codeCacheName, compartmentDefinitionCache);

                }

                // Make unmodifiable.
                cachedCompartmentMap = Collections.unmodifiableMap(cachedCompartmentMap);

            } catch (FHIRException e) {
                log.warning("Unable to parse the entry that is read from compartments.json");
            } catch (IOException e1) {
                log.warning("Unable to read the entry that is read from compartments.json");
            }
        }

        return cachedCompartmentMap;

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
     * @throws FHIRSearchException
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
            String msg = String.format("Invalid compartment: %s", compartment);
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
            String msg = "Invalid resource type: " + resourceType + " for compartment: " + compartment;
            throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
        }
    }

    /**
     * builds the bundle and the resources for the compartments.json and puts out to the output stream.
     * 
     * @throws FHIRGeneratorException
     */
    public static void buildCompositeBundle(PrintStream out) throws FHIRGeneratorException {
        Set<String> compartmentDefinitions = new HashSet<>();
        compartmentDefinitions.add("/compartments/compartmentdefinition-device.json");
        compartmentDefinitions.add("/compartments/compartmentdefinition-encounter.json");
        compartmentDefinitions.add("/compartments/compartmentdefinition-patient.json");
        compartmentDefinitions.add("/compartments/compartmentdefinition-practitioner.json");
        compartmentDefinitions.add("/compartments/compartmentdefinition-relatedperson.json");

        Bundle.Builder build = Bundle.builder(BundleType.COLLECTION);
        for (String compartmentDefintion : compartmentDefinitions) {

            try (InputStreamReader reader = new InputStreamReader(CompartmentUtil.class.getResourceAsStream(compartmentDefintion))) {
                CompartmentDefinition compartmentDefinitionResource = FHIRUtil.read(CompartmentDefinition.class, Format.JSON, reader);

                build.entry(Bundle.Entry.builder().resource(compartmentDefinitionResource).build());

            } catch (FHIRException e) {
                log.warning("Unable to parse the entry that is read from the compartment definition " + compartmentDefintion);
            } catch (IOException e1) {
                log.warning("Unable to read the entry that is read from the compartment definition " + compartmentDefintion);
            }
        }

        Bundle bundle = build.build();
        FHIRGenerator.generator(Format.JSON, true).generate(bundle, out);
        out.println(bundle.toString());

    }

}
