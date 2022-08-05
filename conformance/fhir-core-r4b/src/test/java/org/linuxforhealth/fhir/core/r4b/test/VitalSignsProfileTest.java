/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.core.r4b.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.config.FHIRModelConfig;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.ElementDefinition;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.TypeDerivationRule;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

public class VitalSignsProfileTest {
    private static final String VITAL_SIGNS_PROFILE_URL = "http://hl7.org/fhir/StructureDefinition/vitalsigns";
    private static final Comparator<Constraint> CONSTRAINT_COMPARATOR = new Comparator<Constraint>() {
        @Override
        public int compare(Constraint first, Constraint second) {
            return first.id().compareTo(second.id());
        }
    };

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

    public static Tree buildTree(StructureDefinition profile) {
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

    private static String transform(Node node) {
        StringBuilder sb = new StringBuilder();

        Integer min = getMin(node);
        String max = getMax(node);

        sb.append(node.label);

        Element fixed = getFixed(node);
        if (fixed != null) {
            sb.append(" = ");
            if (fixed.is(Code.class)) {
                sb.append("'").append(fixed.as(Code.class).getValue()).append("'");
            } else if (fixed.is(Uri.class)) {
                sb.append("'").append(fixed.as(Uri.class).getValue()).append("'");
            }
        }

        // TODO: pattern

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

    private static Integer getMin(Node node) {
        for (ElementDefinition elementDefinition : node.elementDefinitions) {
            if (elementDefinition.getMin() != null) {
                return elementDefinition.getMin().getValue();
            }
        }
        return null;
    }

    private static String getMax(Node node) {
        for (ElementDefinition elementDefinition : node.elementDefinitions) {
            if (elementDefinition.getMax() != null) {
                return elementDefinition.getMax().getValue();
            }
        }
        return null;
    }

    public static Element getFixed(Node node) {
        for (ElementDefinition elementDefinition : node.elementDefinitions) {
            if (elementDefinition.getFixed() != null) {
                return elementDefinition.getFixed();
            }
        }
        return null;
    }

    public static Element getPattern(Node node) {
        for (ElementDefinition elementDefinition : node.elementDefinitions) {
            if (elementDefinition.getPattern() != null) {
                return elementDefinition.getPattern();
            }
        }
        return null;
    }

    public static List<ElementDefinition.Constraint> getConstraints(Node node) {
        List<ElementDefinition.Constraint> constraints = new ArrayList<>();
        for (ElementDefinition elementDefinition : node.elementDefinitions) {
            constraints.addAll(elementDefinition.getConstraint());
        }
        return constraints;
    }

    public static List<StructureDefinition> getProfiles(StructureDefinition profile) {
        List<StructureDefinition> profiles = new ArrayList<>();
        while (TypeDerivationRule.CONSTRAINT.equals(profile.getDerivation())) {
            profiles.add(profile);
            profile = FHIRRegistry.getInstance().getResource(profile.getBaseDefinition().getValue(), StructureDefinition.class);
        }
        Collections.reverse(profiles);
        return profiles;
    }

    public static void main(String[] args) {
        FHIRModelConfig.setToStringPrettyPrinting(false);

        StructureDefinition vitalSignsProfile = FHIRRegistry.getInstance().getResource(VITAL_SIGNS_PROFILE_URL, StructureDefinition.class);

        Tree tree = buildTree(vitalSignsProfile);
        Node root = tree.root;

        String result = transform(root);
        System.out.println("result: " + result);

        for (Node child : root.children) {
            String expr = transform(child);
            System.out.println("expr: " + expr);
        }

        System.out.println("Constraints: ");
        for (Constraint constraint : getConstraints(vitalSignsProfile)) {
            System.out.println("    " + constraint);
        }
    }

    public static List<Constraint> getConstraints(List<StructureDefinition> profiles) {
        List<Constraint> constraints = new ArrayList<>();
        for (StructureDefinition profile : profiles) {
            constraints.addAll(getConstraints(profile));
        }
        return constraints;
    }

    public static List<Constraint> getConstraints(StructureDefinition profile) {
        List<Constraint> constraints = new ArrayList<>();
        for (ElementDefinition elementDefinition : profile.getDifferential().getElement()) {
            constraints.addAll(getConstraints(elementDefinition));
        }
        Collections.sort(constraints, CONSTRAINT_COMPARATOR);
        return constraints;
    }

    private static List<Constraint> getConstraints(ElementDefinition elementDefinition) {
        List<Constraint> constraints = new ArrayList<>();
        String path = elementDefinition.getPath().getValue();
        for (ElementDefinition.Constraint constraint : elementDefinition.getConstraint()) {
            constraints.add(createConstraint(path, constraint));
        }
        return constraints;
    }

    private static Constraint createConstraint(String path, ElementDefinition.Constraint constraint) {
        String id = constraint.getKey().getValue();
        String level = "error".equals(constraint.getSeverity().getValue()) ? "Rule" : "Warning";
        String location = path.contains(".") ? path.replace("[x]", "") : "(base)";
        String description = constraint.getHuman().getValue();
        String expression = constraint.getExpression().getValue();
        String source = (constraint.getSource() != null) ? constraint.getSource().getValue() : "";
        return Constraint.Factory.createConstraint(id, level, location, description, expression, source, false, false);
    }
}
