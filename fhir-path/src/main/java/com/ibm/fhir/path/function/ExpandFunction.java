/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;
import static com.ibm.fhir.term.util.ValueSetSupport.isExpanded;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.spi.ExpansionParameters;

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
        map.put("filter", com.ibm.fhir.model.type.String::of);
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
    protected Collection<FHIRPathNode> apply(
            EvaluationContext evaluationContext,
            Collection<FHIRPathNode> context,
            List<Collection<FHIRPathNode>> arguments,
            FHIRTermService service,
            Parameters parameters) {
        ValueSet valueSet = getResource(arguments, ValueSet.class);
        if (!isExpanded(valueSet) && !service.isExpandable(valueSet)) {
            String url = (valueSet.getUrl() != null) ? valueSet.getUrl().getValue() : null;
            generateIssue(evaluationContext, IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, "ValueSet with url '" + url + "' is not expandable", "%terminologies");
            return empty();
        }
        ValueSet expanded = service.expand(valueSet, ExpansionParameters.from(parameters));
        return singleton(FHIRPathResourceNode.resourceNode(expanded));
    }
}
