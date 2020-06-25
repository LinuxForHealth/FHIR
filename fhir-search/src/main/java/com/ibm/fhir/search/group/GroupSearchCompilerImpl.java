/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;

public class GroupSearchCompilerImpl implements GroupSearchCompiler {
    private static final Logger logger = Logger.getLogger(GroupSearchCompilerImpl.class.getName());

    @Override
    public Map<String, List<String>> groupToSearch(Group group) throws GroupSearchCompilerException {
        Objects.requireNonNull(group, "The Group must not be null");
        Objects.requireNonNull(group.getActive(), "The group.active must not be null");

        if (!group.getActive().getValue().booleanValue()) {
            throw new GroupSearchCompilerException("The Group must be active");
        }

        Map<String, List<String>> queryParms = new HashMap<>();
        try {
            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(group);
            String expr = "characteristic.value";

            Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, expr);
            for (FHIRPathNode node : tmpResults) {
                System.out.println(node);
            }
        } catch (FHIRPathException exception) {
            logger.warning("Issue processing the Group into Search Query");
            throw new GroupSearchCompilerException("Issue with Search Query");
        }


        List<String> savedLastUpdated = Collections.emptyList();
        queryParms.put("_lastUpdated", savedLastUpdated);

        return queryParms;
    }
}