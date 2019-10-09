/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.util;

import static com.ibm.fhir.model.path.util.FHIRPathUtil.delimit;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.isKeyword;
import static com.ibm.fhir.validation.util.ProfileSupport.STRUCTURE_DEFINITION_URL_PREFIX;
import static com.ibm.fhir.validation.util.ProfileSupport.createConstraint;
import static com.ibm.fhir.validation.util.ProfileSupport.getBinding;
import static com.ibm.fhir.validation.util.ProfileSupport.getElementDefinition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.ElementDefinition.Binding;
import com.ibm.fhir.model.type.ElementDefinition.Type;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * A class used to generate FHIRPath expressions from a profile
 */
public class ConstraintGenerator {
    public List<Constraint> generate(StructureDefinition profile) {
        List<Constraint> constraints = new ArrayList<>();

        String url = profile.getUrl().getValue();
        String prefix = url.substring(url.lastIndexOf("/") + 1);

        int index = 1;

        Tree tree = buildTree(profile);

        for (Node child : tree.root.children) {
            String expr = generate(child);
            String description = "Constraint violation: " + expr;
            constraints.add(constraint("generated-" + prefix + "-" + index, expr, description));
            index++;
        }

        return constraints;
    }

    private boolean hasConstraint(Node node) {
        if (hasConstraint(node.elementDefinition)) {
            return true;
        }
        for (Node child : node.children) {
            if (hasConstraint(child)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasConstraint(ElementDefinition elementDefinition) {
        return hasCardinalityConstraint(elementDefinition) || 
                hasValueConstraint(elementDefinition) || 
                hasReferenceTypeConstraint(elementDefinition) || 
                hasChoiceTypeConstraint(elementDefinition) || 
                hasVocabularyConstraint(elementDefinition) || 
                hasExtensionConstraint(elementDefinition);
    }

    private boolean hasValueConstraint(ElementDefinition elementDefinition) {
        return hasFixedValueConstraint(elementDefinition) || hasPatternValueConstraint(elementDefinition);
    }

    private boolean hasFixedValueConstraint(ElementDefinition elementDefinition) {
        return elementDefinition.getFixed() != null;
    }

    private boolean hasPatternValueConstraint(ElementDefinition elementDefinition) {
        return elementDefinition.getPattern() != null;
    }

    private boolean hasCardinalityConstraint(ElementDefinition elementDefinition) {
        return isRequired(elementDefinition) || isProhibited(elementDefinition);
    }

    private boolean isRequired(ElementDefinition elementDefinition) {
        return (elementDefinition.getBase().getMin().getValue() == 0 && elementDefinition.getMin().getValue() > 0);
    }

    private boolean isProhibited(ElementDefinition elementDefinition) {
        return "0".equals(elementDefinition.getMax().getValue());
    }

    private boolean hasVocabularyConstraint(ElementDefinition elementDefinition) {
        Binding binding = elementDefinition.getBinding();
        if (binding != null && isCodedElement(elementDefinition)) {
            Binding baseBinding = getBinding(elementDefinition.getBase().getPath().getValue());
            String baseStrength = (baseBinding != null) ? baseBinding.getStrength().getValue() : null;
            String strength = binding.getStrength().getValue();
            return (!"required".equals(baseStrength) && "required".equals(strength))
                    || ("required".equals(baseStrength) && "required".equals(strength) && !binding.getValueSet().equals(baseBinding.getValueSet()));
        }
        return false;
    }

    private boolean hasChoiceTypeConstraint(ElementDefinition elementDefinition) {
        return isChoiceElement(elementDefinition) && getTypes(elementDefinition).size() == 1;
    }

    private boolean isChoiceElement(ElementDefinition elementDefinition) {
        return elementDefinition.getPath().getValue().endsWith("[x]") || elementDefinition.getBase().getPath().getValue().endsWith("[x]");
    }

    private String generate(Node node) {
        ElementDefinition elementDefinition = node.elementDefinition;

        if (hasValueConstraint(elementDefinition)) {
            return generateValueConstraint(elementDefinition);
        }

        if (hasReferenceTypeConstraint(elementDefinition)) {
            return generateReferenceTypeConstraint(elementDefinition);
        }

        if (hasVocabularyConstraint(elementDefinition)) {
            return generateVocabularyConstraint(elementDefinition);
        }
        
        if (hasExtensionConstraint(elementDefinition)) {
            return generateExtensionConstraint(elementDefinition);
        }
        
        StringBuilder sb = new StringBuilder();

        String identifier = getIdentifier(elementDefinition);

        if (isOptional(elementDefinition)) {
            sb.append(identifier).append(".exists()").append(" implies (");
        }

        sb.append(identifier);

        if (hasChoiceTypeConstraint(elementDefinition)) {
            Type type = getTypes(elementDefinition).get(0);
            if (type.getCode() != null) {
                String code = type.getCode().getValue();
                sb.append(".as(").append(code).append(")");
            }
        }

        if (!node.children.isEmpty()) {
            sb.append(".where(");
            StringJoiner joiner = new StringJoiner(" and ");
            for (Node child : node.children) {
                joiner.add(generate(child));
            }
            sb.append(joiner.toString());
            sb.append(")");
        }

        sb.append(".exists()");

        if (isProhibited(elementDefinition)) {
            sb.append(".not()");
        }

        if (isOptional(elementDefinition)) {
            sb.append(")");
        }

        return sb.toString();
    }

    private String generateExtensionConstraint(ElementDefinition elementDefinition) {
        StringBuilder sb = new StringBuilder();
        
        Type type = getTypes(elementDefinition).get(0);
        String profile = getProfiles(type).get(0);

        if (isOptional(elementDefinition)) {
            sb.append("extension('").append(profile).append("')").append(".exists()").append(" implies (");
        }
        
        sb.append("extension('").append(profile).append("').conformsTo('").append(profile).append("').exists()");
        
        if (isOptional(elementDefinition)) {
            sb.append(")");
        }
        
        return sb.toString();
    }

    private List<String> getProfiles(Type type) {
        List<String> profiles = new ArrayList<>();
        for (Canonical profile : type.getProfile()) {
            if (profile.getValue() != null) {
                profiles.add(profile.getValue());
            }
        }
        return profiles;
    }

    private boolean hasExtensionConstraint(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);
        if (types.size() != 1) {
            return false;
        }
        Type type = types.get(0);
        String code = type.getCode().getValue();
        return "Extension".equals(code) && !type.getProfile().isEmpty();
    }

    private boolean isOptional(ElementDefinition elementDefinition) {
        return (elementDefinition.getMin().getValue() == 0) && !isProhibited(elementDefinition);
    }

    private String generateVocabularyConstraint(ElementDefinition elementDefinition) {
        StringBuilder sb = new StringBuilder();
        String identifier = getIdentifier(elementDefinition);
        Binding binding = elementDefinition.getBinding();
        String valueSet = binding.getValueSet().getValue();
        sb.append(identifier);
        if (hasChoiceTypeConstraint(elementDefinition)) {
            Type type = getTypes(elementDefinition).get(0);
            if (type.getCode() != null) {
                String code = type.getCode().getValue();
                sb.append(".as(").append(code).append(")");
            }
        }
        sb.append(".memberOf('").append(valueSet).append("')");
        return sb.toString();
    }

    private String generateValueConstraint(ElementDefinition elementDefinition) {
        return hasFixedValueConstraint(elementDefinition) ? generateFixedValueConstraint(elementDefinition) : generatePatternValueConstraint(elementDefinition);
    }

    private String generateFixedValueConstraint(ElementDefinition elementDefinition) {
        StringBuilder sb = new StringBuilder();
        String identifier = getIdentifier(elementDefinition);
        sb.append(identifier);
        Element fixed = elementDefinition.getFixed();
        if (fixed.is(Uri.class)) {
            // fixed uri
            sb.append(" = '").append(fixed.as(Uri.class).getValue()).append("'");
        } else if (fixed.is(Code.class)) {
            // fixed code
            sb.append(" = '").append(fixed.as(Code.class).getValue()).append("'");
        }
        return sb.toString();
    }

    private String generatePatternValueConstraint(ElementDefinition elementDefinition) {
        StringBuilder sb = new StringBuilder();
        String identifier = getIdentifier(elementDefinition);
        sb.append(identifier);
        Element pattern = elementDefinition.getPattern();
        if (pattern.is(CodeableConcept.class)) {
            CodeableConcept codeableConcept = pattern.as(CodeableConcept.class);
            Coding coding = codeableConcept.getCoding().get(0);
            sb.append(".where(coding.where(system = '").append(coding.getSystem().getValue()).append("' and ").append("code = '").append(coding.getCode().getValue()).append("').exists()).exists()");
        }
        return sb.toString();
    }

    private String generateReferenceTypeConstraint(ElementDefinition elementDefinition) {
        StringBuilder sb = new StringBuilder();

        String identifier = getIdentifier(elementDefinition);
        if (isOptional(elementDefinition)) {
            sb.append(identifier).append(".exists()").append(" implies (");
        }

        String prefix = "";
        if (isRepeating(elementDefinition)) {
            sb.append(identifier);
            sb.append(".all(");
        } else {
            prefix = identifier + ".";
        }

        List<String> targetProfiles = getTargetProfiles(getTypes(elementDefinition).get(0));
        StringJoiner joiner = new StringJoiner(" or ");
        for (String targetProfile : targetProfiles) {
            if (isResourceDefinition(targetProfile)) {
                String resourceType = targetProfile.substring(STRUCTURE_DEFINITION_URL_PREFIX.length());
                joiner.add(prefix + "resolve().is(" + resourceType + ")");
            } else {
                joiner.add(prefix + "resolve().conformsTo('" + targetProfile + "')");
            }
        }
        sb.append(joiner.toString());

        if (isRepeating(elementDefinition)) {
            sb.append(")");
        }

        if (isOptional(elementDefinition)) {
            sb.append(")");
        }

        return sb.toString();
    }

    private boolean isRepeating(ElementDefinition elementDefinition) {
        String max = elementDefinition.getMax().getValue();
        return "*".equals(max) || (Integer.parseInt(max) > 1);
    }

    private String getIdentifier(ElementDefinition elementDefinition) {
        String basePath = elementDefinition.getBase().getPath().getValue();
        int index = basePath.lastIndexOf(".");
        String identifier = basePath.substring(index + 1).replace("[x]", "");
        if (isKeyword(identifier)) {
            identifier = delimit(identifier);
        }
        return identifier;
    }

    private boolean isResourceDefinition(String targetProfile) {
        if (targetProfile.startsWith(STRUCTURE_DEFINITION_URL_PREFIX)) {
            String s = targetProfile.substring(STRUCTURE_DEFINITION_URL_PREFIX.length());
            return ModelSupport.isResourceType(s);
        }
        return false;
    }

    private List<Type> getTypes(ElementDefinition elementDefinition) {
        if (elementDefinition.getContentReference() != null) {
            String contentReference = elementDefinition.getContentReference().getValue();
            return getElementDefinition(contentReference.substring(1)).getType();
        }
        return elementDefinition.getType();
    }

    private boolean hasReferenceTypeConstraint(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);
        List<Type> baseTypes = getTypes(getElementDefinition(elementDefinition.getBase().getPath().getValue()));
        return isReferenceType(types) && !types.equals(baseTypes);
    }

    private List<String> getTargetProfiles(Type type) {
        List<String> targetProfiles = new ArrayList<>();
        for (Canonical canonical : type.getTargetProfile()) {
            if (canonical.getValue() != null) {
                targetProfiles.add(canonical.getValue());
            }
        }
        return targetProfiles;
    }

    private boolean isReferenceType(List<Type> types) {
        if (types.size() != 1) {
            return false;
        }
        Type type = types.get(0);
        if (type.getCode() != null) {
            String code = type.getCode().getValue();
            return "Reference".equals(code);
        }
        return false;
    }

    private boolean isCodedElement(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);
        if (types.size() != 1) {
            return false;
        }
        Type type = types.get(0);
        if (type.getCode() != null) {
            String code = type.getCode().getValue();
            return "code".equals(code) || "Coding".equals(code) || "CodeableConcept".equals(code);
        }
        return false;
    }

    private Constraint constraint(String id, String expr, String description) {
        return createConstraint(id, Constraint.LEVEL_RULE, Constraint.LOCATION_BASE, description, expr, false);
    }

    private boolean isSliceDefinition(ElementDefinition elementDefinition) {
        return elementDefinition.getSlicing() != null;
    }

    private Tree buildTree(StructureDefinition profile) {
        Node root = null;
        Map<String, Node> nodeMap = new LinkedHashMap<>();
        Map<String, ElementDefinition> sliceDefinitionMap = new LinkedHashMap<>();

        for (ElementDefinition elementDefinition : profile.getSnapshot().getElement()) {
            String id = elementDefinition.getId();

            if (isSliceDefinition(elementDefinition)) {
                sliceDefinitionMap.put(id, elementDefinition);
            }

            int index = id.lastIndexOf(".");

            Node node = new Node();
            node.label = id.substring(index + 1);
            node.parent = (index != -1) ? nodeMap.get(id.substring(0, index)) : null;

            if (node.parent == null) {
                root = node;
            } else {
                node.parent.children.add(node);
            }

            node.elementDefinition = elementDefinition;

            nodeMap.put(id, node);
        }

        Tree tree = new Tree();
        tree.root = root;
        tree.nodeMap = nodeMap;
        tree.sliceDefinitionMap = sliceDefinitionMap;

        System.out.println("Paths before pruning:");
        for (String path : nodeMap.keySet()) {
            System.out.println("path: " + path);
        }
        System.out.println("");

        prune(tree);

        System.out.println("Paths after pruning:");
        for (String path : nodeMap.keySet()) {
            System.out.println("path: " + path);
        }
        System.out.println("");

        System.out.println("Slice definitions:");
        for (String id : sliceDefinitionMap.keySet()) {
            System.out.println("id: " + id);
        }
        System.out.println("");

        return tree;
    }

    private void prune(Tree tree) {
        List<Node> nodes = prune(tree.root);
        for (Node node : nodes) {
            node.parent.children.remove(node);
            tree.nodeMap.remove(node.elementDefinition.getId(), node);
        }
    }

    private List<Node> prune(Node node) {
        List<Node> nodes = new ArrayList<>();
        if (!hasConstraint(node) || isSliceDefinition(node.elementDefinition)) {
            nodes.add(node);
        }
        for (Node child : node.children) {
            nodes.addAll(prune(child));
        }
        return nodes;
    }

    static class Node {
        String label;
        Node parent;
        List<Node> children = new ArrayList<>();
        ElementDefinition elementDefinition;
    }

    static class Tree {
        Node root;
        Map<String, Node> nodeMap;
        Map<String, ElementDefinition> sliceDefinitionMap;
    }

    public static void main(String[] args) throws Exception {
        StructureDefinition profile = FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/us/core/StructureDefinition/us-core-race", StructureDefinition.class);
        ConstraintGenerator generator = new ConstraintGenerator();
        generator.generate(profile).stream().map(constraint -> constraint.expression()).forEach(System.out::println);
    }
}