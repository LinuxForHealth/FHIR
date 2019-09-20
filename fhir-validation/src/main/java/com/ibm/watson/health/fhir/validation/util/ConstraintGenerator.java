/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.validation.util;

import static com.ibm.watson.health.fhir.validation.util.ProfileSupport.createConstraint;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import com.ibm.watson.health.fhir.model.annotation.Constraint;
import com.ibm.watson.health.fhir.model.resource.StructureDefinition;
import com.ibm.watson.health.fhir.model.type.Code;
import com.ibm.watson.health.fhir.model.type.Element;
import com.ibm.watson.health.fhir.model.type.ElementDefinition;
import com.ibm.watson.health.fhir.model.type.Uri;

public class ConstraintGenerator {
    public List<Constraint> generate(StructureDefinition profile) {
        List<Constraint> constraints = new ArrayList<>();
        Tree tree = buildTree(profile);
        for (Node child : tree.root.children) {
            String expr = transform(child);
            constraints.add(createConstraint(UUID.randomUUID().toString(), Constraint.LEVEL_RULE, Constraint.LOCATION_BASE, "<no description>", expr, false));
        }
        return constraints;
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
                node.label = path.substring(index + 1);
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

    private String transform(Node node) {
        StringBuilder sb = new StringBuilder();
        
        Integer min = getMin(node);
        String max = getMax(node);
        
        sb.append(node.label);
        
        Element fixed = getFixed(node);
        Element pattern = getPattern(node);
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
        
        if (!node.children.isEmpty()) {
            sb.append(".where(");
            
            StringJoiner joiner = new StringJoiner(" and ");
            for (Node child : node.children) {
                joiner.add(transform(child));
            }
            sb.append(joiner.toString());
            
            sb.append(")");
        }
        
        if (min != null && max != null && fixed == null) {
            if (min == 1 && "*".equals(max)) {
                sb.append(".exists()");
            } else if (min == 1 && "1".equals(max)) {
                sb.append(".count() = 1");
            }
        }
        
        return sb.toString();
    }

    private Integer getMin(Node node) {
        for (ElementDefinition elementDefinition : node.elementDefinitions) {
            if (elementDefinition.getMin() != null) {
                return elementDefinition.getMin().getValue();
            }
        }
        return null;
    }

    private String getMax(Node node) {
        for (ElementDefinition elementDefinition : node.elementDefinitions) {
            if (elementDefinition.getMax() != null) {
                return elementDefinition.getMax().getValue();
            }
        }
        return null;
    }
    
    private Element getFixed(Node node) {
        for (ElementDefinition elementDefinition : node.elementDefinitions) {
            if (elementDefinition.getFixed() != null) {
                return elementDefinition.getFixed();
            }
        }
        return null;
    }
    
    private Element getPattern(Node node) {
        for (ElementDefinition elementDefinition : node.elementDefinitions) {
            if (elementDefinition.getPattern() != null) {
                return elementDefinition.getPattern();
            }
        }
        return null;
    }

    static class Tree {
        Node root;
        Map<String, Node> nodeMap;
        
        Node getNode(String path) {
            return nodeMap.get(path);
        }
    }

    static class Node {
        String label;
        String path;
        Node parent;
        List<Node> children = new ArrayList<>();
        List<ElementDefinition> elementDefinitions = new ArrayList<>();
    
        int getLevel() {
            int level = 0;
            Node parent = this.parent;
            while (parent != null) {
                level++;
                parent = parent.parent;
            }
            return level;
        }
    }    
}