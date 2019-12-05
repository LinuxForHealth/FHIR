/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.fhir.path.util.FHIRPathUtil.getElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.getString;
import static com.ibm.fhir.path.util.FHIRPathUtil.hasElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.hasStringValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Compose;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include;
import com.ibm.fhir.model.resource.ValueSet.Expansion;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.registry.FHIRRegistry;

public class MemberOfFunction extends FHIRPathAbstractFunction {
    private static final Map<String, Set<String>> VALUE_SET_CACHE = createLRUCache(1024);
    
    @Override
    public String getName() {
        return "memberOf";
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
        if (!hasElementNode(context)) {
            throw new IllegalArgumentException("The 'memberOf' function can only be invoked on a Resource or Element node");
        }
        
        FHIRPathElementNode elementNode = getElementNode(context);
        Element element = elementNode.element();
        if (!isCodedElement(element)) {
            throw new IllegalArgumentException("The 'memberOf' function can only be invoked on a coded element");
        }
        
        if (!hasStringValue(arguments.get(0))) {
            throw new IllegalArgumentException("The argument to the 'memberOf' function must be a string");
        }
        
        String url = getString(arguments.get(0));
                
        if (FHIRRegistry.getInstance().hasResource(url)) {
            Set<String> valueSet = getValueSet(url);
            if (!valueSet.isEmpty()) {
                if (element.is(Code.class)) {
                    Code code = element.as(Code.class);
                    return valueSet.contains(code.getValue()) ? SINGLETON_TRUE : SINGLETON_FALSE;
                } else if (element.is(Coding.class)) {
                    Coding coding = element.as(Coding.class);
                    return valueSet.contains(coding.getCode().getValue()) ? SINGLETON_TRUE : SINGLETON_FALSE;
                } else {
                    CodeableConcept codeableConcept = element.as(CodeableConcept.class);
                    for (Coding coding : codeableConcept.getCoding()) {
                        if (valueSet.contains(coding.getCode().getValue())) {
                            return SINGLETON_TRUE;
                        }
                    }
                    return SINGLETON_FALSE;
                }
            }
        }
        
        return SINGLETON_TRUE;
    }
    
    private Set<String> getValueSet(String url) {
        return VALUE_SET_CACHE.computeIfAbsent(url, k -> computeValueSet(FHIRRegistry.getInstance().getResource(url,  ValueSet.class)));
    }
    
    private Set<String> computeValueSet(ValueSet valueSet) {
        Set<String> result = new HashSet<>();
        
        Expansion expansion = valueSet.getExpansion();
        if (expansion != null) {
            result.addAll(Stream.concat(expansion.getContains().stream(), expansion.getContains().stream().flatMap(contains -> contains.getContains().stream()))
                .map(contains -> contains.getCode().getValue())
                .collect(Collectors.toList()));
        } else {
            Compose compose = valueSet.getCompose();
            if (compose != null) {
                for (Include include : compose.getInclude()) {
                    if (!include.getConcept().isEmpty()) {
                        result.addAll(include.getConcept().stream()
                            .map(concept -> concept.getCode().getValue())
                            .collect(Collectors.toList()));
                    } else if (include.getSystem() != null){
                        String system = include.getSystem().getValue();
                        if (FHIRRegistry.getInstance().hasResource(system)) {
                            CodeSystem codeSystem = FHIRRegistry.getInstance().getResource(system, CodeSystem.class);
                            result.addAll(Stream.concat(codeSystem.getConcept().stream(), codeSystem.getConcept().stream().flatMap(concept -> concept.getConcept().stream()))
                                .map(concept -> concept.getCode().getValue())
                                .collect(Collectors.toList()));
                        }
                    }
                }
            }
        }
        
        // TODO: add support for exclude
        
        return result;
    }

    private boolean isCodedElement(Element element) {
        return (element instanceof Code) || 
                (element instanceof Coding) || 
                (element instanceof CodeableConcept);
    }
}
