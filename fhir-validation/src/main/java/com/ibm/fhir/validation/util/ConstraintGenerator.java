/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.util;

import static com.ibm.fhir.validation.util.ProfileSupport.createConstraint;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.path.util.FHIRPathUtil;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.ElementDefinition.Type;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ModelSupport.ElementInfo;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * A class used to generate FHIRPath expressions from a profile
 */
public class ConstraintGenerator {
    public List<Constraint> generate(StructureDefinition profile, Class<?> resourceType) {
        List<Constraint> constraints = new ArrayList<>();
        
        String url = profile.getUrl().getValue();
        String prefix = url.substring(url.lastIndexOf("/") + 1);
        
        int index = 1;
        
        Tree tree = buildTree(profile);
        
        for (Node child : tree.root.children) {            
            ElementDefinition elementDefinition = child.elementDefinitions.get(0);
            if (!hasConstraint(elementDefinition, resourceType)) {
                continue;
            }
            if (isSliceDefinition(elementDefinition)) {
                // for each slice
                for (int i = 1; i < child.elementDefinitions.size(); i++) {
                    String expr = generate(child, child.elementDefinitions.get(i), resourceType);
                    System.out.println("expr: " + expr);
                    String id = prefix + "-gen-" + index;
                    constraints.add(constraint(id, expr));
                    index++;
                }
            } else {
                String expr = generate(child, elementDefinition, resourceType);
                System.out.println("expr: " + expr);
                String id = prefix + "-gen-" + index;
                constraints.add(constraint(id, expr));
                index++;
            }
        }
        
        return constraints;
    }
    
    private Constraint constraint(String id, String expr) {
        return createConstraint(id, Constraint.LEVEL_RULE, Constraint.LOCATION_BASE, "<no description>", expr, false);
    }

    private boolean isSliceDefinition(ElementDefinition elementDefinition) {
        return elementDefinition.getSlicing() != null;
    }

    private Tree buildTree(StructureDefinition profile) {
        Node root = null;

        Map<String, Node> nodeMap = new LinkedHashMap<>();
        for (ElementDefinition elementDefinition : profile.getDifferential().getElement()) {
            String path = elementDefinition.getPath().getValue();
            Node node = nodeMap.get(path);
            if (node == null) {
                node = new Node();
                
                node.path = path;
                
                int index = path.lastIndexOf(".");
                String label = path.substring(index + 1).replace("[x]", "");
                if (FHIRPathUtil.isKeyword(label)) {
                    label = FHIRPathUtil.delimit(label);
                }
                node.label = label;                
                node.parent = (index != -1) ? nodeMap.get(path.substring(0, index)) : null;
                
                if (node.parent == null) {
                    root = node;
                } else {
                    node.parent.children.add(node);
                }
                
                nodeMap.put(path, node);
            }
            node.elementDefinitions.add(elementDefinition);
        }

        Tree tree = new Tree();
        tree.root = root;
        tree.nodeMap = nodeMap;

        return tree;
    }
    
    private String generate(Node node, ElementDefinition elementDefinition, Class<?> resourceType) {        
        StringBuilder sb = new StringBuilder();
        
        int whereCount = 0;
                
        Integer min = (elementDefinition.getMin() != null) ? elementDefinition.getMin().getValue() : null;
        String max = (elementDefinition.getMax() != null) ? elementDefinition.getMax().getValue() : null;

        if (min == null || min == 0) {
            sb.append(node.label + ".exists() implies (");
        }
        
        if (hasChoiceTypeConstraint(elementDefinition, resourceType)) {
            String path = elementDefinition.getPath().getValue();
            String typeSpecificElementName = path.substring(path.lastIndexOf(".") + 1);
            ElementInfo choiceElementInfo = ModelSupport.getChoiceElementInfo(resourceType, typeSpecificElementName);
            String elementName = choiceElementInfo.getName();
            String typeName = typeSpecificElementName.substring(elementName.length());
            sb.append(elementName).append(".as(").append(typeName).append(")");
        } else {
            sb.append(node.label);
        }
        
        Element fixed = elementDefinition.getFixed();
        Element pattern = elementDefinition.getPattern();
        
        if (fixed != null) {
            sb.append(" = ");
            if (fixed.is(Code.class)) {
                sb.append("'").append(fixed.as(Code.class).getValue()).append("'");
            } else if (fixed.is(Uri.class)) {
                sb.append("'").append(fixed.as(Uri.class).getValue()).append("'");
            }
        } else if (pattern != null) {
            // TODO: pattern

        }

        // TODO: value set binding

        // TODO: reference types
        
        List<Node> children = getChildren(node, elementDefinition);
        if (!children.isEmpty()) {
            whereCount++;
            
            sb.append(".where(");
            StringJoiner joiner = new StringJoiner(" and ");
            for (Node child : children) {
                joiner.add(generate(child, child.elementDefinitions.get(0), resourceType));
            }
            sb.append(joiner.toString());
            sb.append(")");
            
            whereCount--;
        }

        if (min != null && max != null && fixed == null && whereCount == 0) {
            if (min == 1 && "*".equals(max)) {
                sb.append(".exists()");
            } else if (min == 1 && "1".equals(max)) {
                sb.append(".count() = 1");
            }
        }
        
        if (min == null || min == 0) {
            sb.append(")");
        }

        return sb.toString();
    }
    
    private boolean hasConstraint(ElementDefinition elementDefinition, Class<?> resourceType) {
        return hasReferenceTypeConstraint(elementDefinition) || 
                hasCardinalityConstraint(elementDefinition) || 
                hasFixed(elementDefinition) || 
                hasPattern(elementDefinition) || 
                hasBinding(elementDefinition) || 
                hasChoiceTypeConstraint(elementDefinition, resourceType);
    }
    
    private boolean hasChoiceTypeConstraint(ElementDefinition elementDefinition, Class<?> resourceType) {
        String path = elementDefinition.getPath().getValue();
        String elementName = path.substring(path.lastIndexOf(".") + 1);
        return ModelSupport.getChoiceElementInfo(resourceType, elementName) != null;
    }
    
    private boolean hasReferenceTypeConstraint(ElementDefinition elementDefinition) {
        if (elementDefinition.getType().isEmpty()) {
            return false;
        }
        Type type = elementDefinition.getType().get(0);
        if (type.getCode() != null && type.getCode().getValue() != null) {
            String code = type.getCode().getValue();
            return "Reference".equals(code) && !type.getTargetProfile().isEmpty();
        }
        return false;
    }
    
    private boolean hasCardinalityConstraint(ElementDefinition elementDefinition) {
        return elementDefinition.getMin() != null || elementDefinition.getMax() != null;
    }
    
    private boolean hasFixed(ElementDefinition elementDefinition) {
        return elementDefinition.getFixed() != null;
    }
    
    private boolean hasPattern(ElementDefinition elementDefinition) {
        return elementDefinition.getPattern() != null;
    }
    
    private boolean hasBinding(ElementDefinition elementDefinition) {
        return elementDefinition.getBinding() != null;
    }
    
    private List<Node> getChildren(Node node, ElementDefinition elementDefinition) {
        if (isSlice(elementDefinition)) {
            String sliceName = getSliceName(elementDefinition);
            return node.children.stream()
                    .filter(child -> child.elementDefinitions.get(0).getId().contains(sliceName))
                    .collect(Collectors.toList());
        }
        return node.children;
    }

    private boolean isSlice(ElementDefinition elementDefinition) {
        return elementDefinition.getSliceName() != null;
    }
    
    private String getSliceName(ElementDefinition elementDefinition) {
        return elementDefinition.getSliceName().getValue();
    }

    static class Tree {
        Node root;
        Map<String, Node> nodeMap;
    }

    static class Node {
        String label;
        String path;
        Node parent;
        List<Node> children = new ArrayList<>();
        List<ElementDefinition> elementDefinitions = new ArrayList<>();
    }
    
    public static void main(String[] args) throws Exception {
        StructureDefinition profile = FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/StructureDefinition/vitalsigns", StructureDefinition.class);
        ConstraintGenerator generator = new ConstraintGenerator();
        generator.generate(profile, Observation.class);
        
        profile = FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/StructureDefinition/bodyweight", StructureDefinition.class);
        generator = new ConstraintGenerator();
        generator.generate(profile, Observation.class);
    }
}
