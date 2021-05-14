/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.core.util.URLSupport.parseQuery;
import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.path.util.FHIRPathUtil.getDisplay;
import static com.ibm.fhir.path.util.FHIRPathUtil.getElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.getResourceNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.getSingleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.getString;
import static com.ibm.fhir.path.util.FHIRPathUtil.getSystem;
import static com.ibm.fhir.path.util.FHIRPathUtil.getVersion;
import static com.ibm.fhir.path.util.FHIRPathUtil.isElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isResourceNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isSingleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.isStringValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathTree;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.util.FHIRPathUtil;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.term.service.FHIRTermService;

public abstract class FHIRPathAbstractTermFunction extends FHIRPathAbstractFunction {
    private static final Parameters EMPTY_PARAMETERS = Parameters.builder()
            .id("InputParameters")
            .build();

    protected final FHIRTermService service;
    private final Map<String, Function<String, Element>> elementFactoryMap;

    public FHIRPathAbstractTermFunction() {
        service = FHIRTermService.getInstance();
        elementFactoryMap = buildElementFactoryMap();
    }

    @Override
    public abstract String getName();

    @Override
    public abstract int getMinArity();

    @Override
    public abstract int getMaxArity();

    @Override
    public abstract Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments);

    protected Map<String, Function<String, Element>> buildElementFactoryMap() {
        return Collections.emptyMap();
    }

    protected boolean isCodedElementNode(Collection<FHIRPathNode> nodes) {
        return isCodedElementNode(nodes, CodeableConcept.class, Coding.class, Code.class);
    }

    protected boolean isCodedElementNode(Collection<FHIRPathNode> nodes, Class<?>... codedElementTypes) {
        if (isElementNode(nodes)) {
            FHIRPathElementNode elementNode = getElementNode(nodes);
            return Arrays.stream(codedElementTypes).anyMatch(type -> type.equals(elementNode.element().getClass()));
        }
        return false;
    }

    protected boolean isTermServiceNode(Collection<FHIRPathNode> nodes) {
        return isSingleton(nodes) && getSingleton(nodes).isTermServiceNode();
    }

    protected Element getCodedElement(FHIRPathTree tree, FHIRPathElementNode codedElementNode) {
        if (codedElementNode.element().is(CodeableConcept.class)) {
            return codedElementNode.element().as(CodeableConcept.class);
        }
        return getCoding(tree, codedElementNode);
    }

    protected Coding getCoding(FHIRPathTree tree, FHIRPathElementNode codedElementNode) {
        if (codedElementNode.element().is(Coding.class)) {
            return codedElementNode.element().as(Coding.class);
        }
        return Coding.builder()
                .system(getSystem(tree, codedElementNode))
                .version(getVersion(tree, codedElementNode))
                .code(codedElementNode.element().as(Code.class))
                .display(getDisplay(tree, codedElementNode))
                .build();
    }

    protected Parameters getParameters(List<Collection<FHIRPathNode>> arguments) {
        if (arguments.size() == getMaxArity()) {
            String params = getString(arguments.get(arguments.size() - 1));
            Map<String, List<String>> queryParameters = parseQuery(params);
            return buildParameters(queryParameters);
        }
        return EMPTY_PARAMETERS;
    }

    protected <T extends Resource> T getResource(List<Collection<FHIRPathNode>> arguments, Class<T> resourceType) {
        if (isStringValue(arguments.get(0))) {
            String url = FHIRPathUtil.getString(arguments.get(0));
            return FHIRRegistry.getInstance().getResource(url, resourceType);
        }
        if (isResourceNode(arguments.get(0))) {
            return resourceType.cast(getResourceNode(arguments.get(0)).resource());
        }
        return null;
    }

    private Parameters buildParameters(Map<String, List<String>> queryParameters) {
        return Parameters.builder()
                .id("InputParameters")
                .parameter(queryParameters.keySet().stream()
                    .filter(key -> !queryParameters.get(key).isEmpty())
                    .filter(key -> elementFactoryMap.containsKey(key))
                    .map(key -> parameter(queryParameters, key, elementFactoryMap.get(key)))
                    .flatMap(List::stream)
                    .collect(Collectors.toList()))
                .build();
    }

    private List<Parameter> parameter(
            Map<String, List<String>> queryParameters,
            String name,
            Function<String, Element> elementFactory) {
        return queryParameters.getOrDefault(name, Collections.emptyList()).stream()
                .map(value -> Parameter.builder()
                    .name(string(name))
                    .value(elementFactory.apply(value))
                    .build())
                .collect(Collectors.toList());
    }
}
