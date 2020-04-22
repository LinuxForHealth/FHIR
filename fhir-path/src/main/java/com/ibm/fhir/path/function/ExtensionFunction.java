/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.util.FHIRPathUtil.getStringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.hasStringValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class ExtensionFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "extension";
    }

    @Override
    public int getMinArity() {
        return 1;
    }

    @Override
    public int getMaxArity() {
        return 1;
    }

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        List<FHIRPathNode> result = new ArrayList<>();
        if (hasStringValue(arguments.get(0))) {
            String url = getStringValue(arguments.get(0)).string();
            for (FHIRPathNode node : context) {
                for (FHIRPathNode child : node.children()) {
                    if (child.isElementNode() && child.asElementNode().element().is(Extension.class)) {
                        Extension extension = child.asElementNode().element().as(Extension.class);
                        if (extension.getUrl().equals(url)) {
                            result.add(child);
                        }
                    }
                }
            }
        }
        return result;
    }
}
