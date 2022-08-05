/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.empty;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isResourceNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isStringValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.singleton;
import static org.linuxforhealth.fhir.term.util.ValueSetSupport.isExpanded;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.ValueSet;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathResourceNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.term.service.ExpansionParameters;

public class ExpandFunction extends FHIRPathAbstractTermFunction {
    @Override
    public String getName() {
        return "expand";
    }

    @Override
    public int getMinArity() {
        return 1;
    }

    @Override
    public int getMaxArity() {
        return 2;
    }

    @Override
    protected Map<String, Function<String, Element>> buildElementFactoryMap() {
        Map<String, Function<String, Element>> map = new HashMap<>();
        map.put("context", Uri::of);
        map.put("contextDirection", Code::of);
        map.put("filter", org.linuxforhealth.fhir.model.type.String::of);
        map.put("date", DateTime::of);
        map.put("offset", Integer::of);
        map.put("count", Integer::of);
        map.put("includeDesignations", Boolean::of);
        map.put("activeOnly", Boolean::of);
        map.put("excludeNested", Boolean::of);
        map.put("excludeNotForUI", Boolean::of);
        map.put("excludePostCoordinated", Boolean::of);
        map.put("displayLanguage", Code::of);
        map.put("excludeSystem", Canonical::of);
        map.put("systemVersion", Canonical::of);
        map.put("checkSystemVersion", Canonical::of);
        map.put("forceSystemVersion", Canonical::of);
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!isTermServiceNode(context) ||
                (!isResourceNode(arguments.get(0)) && !isStringValue(arguments.get(0))) ||
                (arguments.size() == 2 && !isStringValue(arguments.get(1)))) {
            return empty();
        }
        ValueSet valueSet = getResource(arguments, ValueSet.class);
        if (!isExpanded(valueSet) && !service.isExpandable(valueSet)) {
            String url = (valueSet.getUrl() != null) ? valueSet.getUrl().getValue() : null;
            generateIssue(evaluationContext, IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, "ValueSet with url '" + url + "' is not expandable", "%terminologies");
            return empty();
        }
        Parameters parameters = getParameters(arguments);
        ValueSet expanded = service.expand(valueSet, ExpansionParameters.from(parameters));
        return singleton(FHIRPathResourceNode.resourceNode(expanded));
    }
}
