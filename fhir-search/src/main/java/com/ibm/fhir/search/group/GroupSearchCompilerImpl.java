/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.search.group.characteristic.AdministrativeGenderCharacteristicProcessor;
import com.ibm.fhir.search.group.characteristic.CharacteristicProcessor;
import com.ibm.fhir.search.group.characteristic.NoOpCharacteristicProcessor;
import com.ibm.fhir.search.group.characteristic.PatientAgeCharacteristicProcessor;
import com.ibm.fhir.search.group.characteristic.PregnancyStatusCharacteristicProcessor;

/**
 * Group Search Compiler routes the Group based on characteristics to each of the corresponding processors for the
 * Supported Elements, and generates the corresponding Search QueryParameters based on the relevant target.
 */
public class GroupSearchCompilerImpl implements GroupSearchCompiler {
    private static final Logger logger = Logger.getLogger(GroupSearchCompilerImpl.class.getName());

    // For any new processors, add them to the appropriate Group.Characteristic choice element.
    private static final List<CharacteristicProcessor> CODEABLE_CONCEPT_PROCESSORS = Arrays.asList(new PregnancyStatusCharacteristicProcessor(), new AdministrativeGenderCharacteristicProcessor());
    private static final List<CharacteristicProcessor> BOOLEAN_PROCESSORS = Arrays.asList(new NoOpCharacteristicProcessor());
    private static final List<CharacteristicProcessor> RANGE_PROCESSORS = Arrays.asList(new NoOpCharacteristicProcessor());
    private static final List<CharacteristicProcessor> REFERENCE_PROCESSORS = Arrays.asList(new NoOpCharacteristicProcessor());
    private static final List<CharacteristicProcessor> QUANTITY_PROCESSORS = Arrays.asList(new PatientAgeCharacteristicProcessor());

    @Override
    public MultivaluedMap<String, String> groupToSearch(Group group, String target) throws GroupSearchCompilerException {
        Objects.requireNonNull(group, "The Group must not be null");
        Objects.requireNonNull(group.getActive(), "The group.active must not be null");

        if (!group.getActive().getValue().booleanValue()) {
            throw new GroupSearchCompilerException("The Group must be active");
        }

        MultivaluedMap<String, String> queryParams = new javax.ws.rs.core.MultivaluedHashMap<>();
        try {
            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(group);
            String expr = "characteristic";

            Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, expr);
            for (FHIRPathNode node : tmpResults) {
                Group.Characteristic characteristic = node.asElementNode().element().as(Group.Characteristic.class);
                // Quantity also takes care of assignable values.
                if (characteristic.getValue().is(Quantity.class)) {
                    QUANTITY_PROCESSORS.stream().forEach(m -> m.process(characteristic, target, queryParams));
                } else if (characteristic.getValue().is(CodeableConcept.class)) {
                    CODEABLE_CONCEPT_PROCESSORS.stream().forEach(m -> m.process(characteristic, target, queryParams));
                } else if (characteristic.getValue().is(com.ibm.fhir.model.type.Boolean.class)) {
                    BOOLEAN_PROCESSORS.stream().forEach(m -> m.process(characteristic, target, queryParams));
                } else if (characteristic.getValue().is(Range.class)) {
                    RANGE_PROCESSORS.stream().forEach(m -> m.process(characteristic, target, queryParams));
                } else if (characteristic.getValue().is(Reference.class)) {
                    REFERENCE_PROCESSORS.stream().forEach(m -> m.process(characteristic, target, queryParams));
                }
            }
        } catch (FHIRPathException exception) {
            logger.warning("Issue processing the Group into Search Query");
            throw new GroupSearchCompilerException("Issue with Search Query");
        }
        return queryParams;
    }
}