/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;
import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.getElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.getResourceNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.getSingleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.getString;
import static com.ibm.fhir.path.util.FHIRPathUtil.isElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isResourceNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isSingleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.isStringValue;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
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
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathTree;
import com.ibm.fhir.path.FHIRPathType;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.util.FHIRPathUtil;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.term.service.FHIRTermService;

public abstract class FHIRPathAbstractTermFunction extends FHIRPathAbstractFunction {
    private static final Parameters EMPTY_INPUT_PARAMETERS = Parameters.builder()
            .id("InputParameters")
            .build();

    private final Map<String, Function<String, Element>> elementFactoryMap;

    public FHIRPathAbstractTermFunction() {
        elementFactoryMap = buildElementFactoryMap();
    }

    @Override
    public abstract String getName();

    @Override
    public abstract int getMinArity();

    @Override
    public abstract int getMaxArity();

    @Override
    public final Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!isTermServiceNode(context) && !isCodedElementNode(context)) {
            return empty();
        }

        FHIRTermService service = isTermServiceNode(context) ? getSingleton(context).asTermServiceNode().service() : FHIRTermService.getInstance();

        if (isCodedElementNode(context) && getMaxArity() > 1) {
            // merge context into arguments
            arguments = new ArrayList<>(arguments);
            arguments.add(0, context);
            arguments = Collections.unmodifiableList(arguments);
        }

        Parameters parameters = EMPTY_INPUT_PARAMETERS;
        if (arguments.size() == getMaxArity() && getMaxArity() > 1) {
            if (!isStringValue(arguments.get(arguments.size() - 1))) {
                return empty();
            }
            String params = getString(arguments.get(arguments.size() - 1));
            Map<String, List<String>> queryParameters = parse(params);
            parameters = buildInputParameters(queryParameters);
        }

        return apply(evaluationContext, context, arguments, service, parameters);
    }

    protected Map<String, Function<String, Element>> buildElementFactoryMap() {
        return Collections.emptyMap();
    }

    protected abstract Collection<FHIRPathNode> apply(
            EvaluationContext evaluationContext,
            Collection<FHIRPathNode> context,
            List<Collection<FHIRPathNode>> arguments,
            FHIRTermService service,
            Parameters parameters);

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

    protected Uri getSystem(FHIRPathTree tree, FHIRPathElementNode codedElementNode) {
        if (tree != null) {
            FHIRPathNode systemNode = tree.getSibling(codedElementNode, "system");
            if (systemNode != null && FHIRPathType.FHIR_URI.equals(systemNode.type())) {
                return systemNode.asElementNode().element().as(Uri.class);
            }
        }
        return null;
    }

    protected com.ibm.fhir.model.type.String getVersion(FHIRPathTree tree, FHIRPathElementNode codedElementNode) {
        if (tree != null) {
            FHIRPathNode versionNode = tree.getSibling(codedElementNode, "version");
            if (versionNode != null && FHIRPathType.FHIR_STRING.equals(versionNode.type())) {
                return versionNode.asElementNode().element().as(FHIR_STRING);
            }
        }
        return null;
    }

    protected com.ibm.fhir.model.type.String getDisplay(FHIRPathTree tree, FHIRPathElementNode codedElementNode) {
        if (tree != null) {
            FHIRPathNode displayNode = tree.getSibling(codedElementNode, "display");
            if (displayNode != null && FHIRPathType.FHIR_STRING.equals(displayNode.type())) {
                return displayNode.asElementNode().element().as(FHIR_STRING);
            }
        }
        return null;
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

    private Parameters buildInputParameters(Map<String, List<String>> queryParameters) {
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

    private String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<String>> parse(String params) {
        return Arrays.stream(params.split("&"))
                .map(pair -> Arrays.asList(pair.split("=", 2)))
                .collect(Collectors.collectingAndThen(
                    Collectors.toMap(
                        // key mapping function
                        pair -> decode(pair.get(0)),
                        // value mapping function
                        pair -> Collections.unmodifiableList(Arrays.stream(pair.get(1).split(","))
                            .map(s -> decode(s))
                            .collect(Collectors.toList())),
                        // merge function
                        (u, v) -> {
                            List<String> merged = new ArrayList<>(u);
                            merged.addAll(v);
                            return Collections.unmodifiableList(merged);
                        },
                        // map supplier
                        LinkedHashMap::new),
                    Collections::unmodifiableMap));
    }
}
