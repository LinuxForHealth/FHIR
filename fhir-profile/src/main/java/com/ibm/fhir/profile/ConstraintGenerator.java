/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile;

import static com.ibm.fhir.model.util.ModelSupport.delimit;
import static com.ibm.fhir.model.util.ModelSupport.isKeyword;
import static com.ibm.fhir.model.util.ModelSupport.isResourceType;
import static com.ibm.fhir.profile.ProfileSupport.HL7_STRUCTURE_DEFINITION_URL_PREFIX;
import static com.ibm.fhir.profile.ProfileSupport.createConstraint;
import static com.ibm.fhir.profile.ProfileSupport.getBinding;
import static com.ibm.fhir.profile.ProfileSupport.getElementDefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.ElementDefinition.Binding;
import com.ibm.fhir.model.type.ElementDefinition.Slicing;
import com.ibm.fhir.model.type.ElementDefinition.Slicing.Discriminator;
import com.ibm.fhir.model.type.ElementDefinition.Type;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DiscriminatorType;
import com.ibm.fhir.model.util.ModelSupport;

import net.jcip.annotations.NotThreadSafe;

/**
 * A class used to generate FHIRPath expressions from a profile
 */
@NotThreadSafe
public class ConstraintGenerator {
    private static final Logger log = Logger.getLogger(ConstraintGenerator.class.getName());

    private static final String MONEY_QUANTITY_PROFILE = "http://hl7.org/fhir/StructureDefinition/MoneyQuantity";
    private static final String SIMPLE_QUANTITY_PROFILE = "http://hl7.org/fhir/StructureDefinition/SimpleQuantity";
    private static final String MAX_VALUE_SET_EXTENSION_URL = "http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet";

    private final StructureDefinition profile;
    private final Map<String, ElementDefinition> elementDefinitionMap;
    private final Tree tree;

    public ConstraintGenerator(StructureDefinition profile) {
        Objects.requireNonNull(profile, "profile");
        this.profile = profile;
        elementDefinitionMap = buildElementDefinitionMap(this.profile);
        tree = buildTree(this.profile);
    }

    public List<Constraint> generate() {
        List<Constraint> constraints = new ArrayList<>();

        String url = profile.getUrl().getValue();
        String prefix = url.substring(url.lastIndexOf("/") + 1);

        int index = 1;

        Set<String> generated = new HashSet<>();

        log.finest("Element definition -> constraint expression:");
        for (Node child : tree.root.children) {
            String expr = generate(child);
            if (generated.contains(expr)) {
                continue;
            }
            String description = "Constraint violation: " + expr;
            constraints.add(constraint("generated-" + prefix + "-" + index, expr, description));
            index++;
            generated.add(expr);
        }
        log.finest("");

        if (log.isLoggable(Level.FINEST)) {
            log.finest("Generated constraints:");
            for (Constraint constraint : constraints) {
                log.finest(constraint.id() + ": " + constraint.expression());
            }
            log.finest("");
        }

        return constraints;
    }

    private boolean accept(List<String> paths, String relativePath) {
        for (String path : paths) {
            if (path.startsWith(relativePath)) {
                return true;
            }
        }
        return false;
    }

    private String antecedent(Node node) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;

        if (isSlice(elementDefinition)) {
            sb.append(discriminator(node));
        } else {
            sb.append(prefix(node));
        }

        sb.append(".exists()");

        return sb.toString();
    }

    private Map<String, ElementDefinition> buildElementDefinitionMap(StructureDefinition profile) {
        Map<String, ElementDefinition> elementDefinitionMap = new LinkedHashMap<>();
        for (ElementDefinition elementDefinition : profile.getSnapshot().getElement()) {
            elementDefinitionMap.put(elementDefinition.getId(), elementDefinition);
        }
        return elementDefinitionMap;
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

        if (log.isLoggable(Level.FINEST)) {
            log.finest("Element definitions BEFORE pruning:");
            for (String id : nodeMap.keySet()) {
                log.finest(id);
            }
            log.finest("");
        }

        prune(tree);

        if (log.isLoggable(Level.FINEST)) {
            log.finest("Element definitions AFTER pruning:");
            for (String id : nodeMap.keySet()) {
                log.finest(id);
            }
            log.finest("");

            log.finest("Slice definitions:");
            for (String id : sliceDefinitionMap.keySet()) {
                log.finest(id);
            }
            log.finest("");
        }

        return tree;
    }

    private String cardinality(Node node, String expr) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;

        if (isSlice(elementDefinition) || (isRepeating(elementDefinition) && !hasChildren(node))) {
            sb.append(".count()");

            Integer min = elementDefinition.getMin().getValue();
            if (isOptional(elementDefinition)) {
                min = 1;
            }
            String max = elementDefinition.getMax().getValue();

            if ("*".equals(max)) {
                sb.append(" >= ").append(min);
            } else if ("1".equals(max)) {
                if (min == 0) {
                    sb.append(" <= 1");
                } else {
                    sb.append(" = 1");
                }
            } else {
                sb.append(" >= ").append(min).append(" and ").append(expr).append(".count() <= ").append(max);
            }
        } else if (!isRepeating(elementDefinition)) {
            sb.append(".exists()");
        }

        return sb.toString();
    }

    private String consequent(Node node) {
        ElementDefinition elementDefinition = node.elementDefinition;

        if (isProhibited(elementDefinition)) {
            return generateProhibitedConstraint(elementDefinition);
        }

        if (hasValueConstraint(elementDefinition)) {
            return generateValueConstraint(node);
        }

        if (hasExtensionConstraint(elementDefinition)) {
            return generateExtensionConstraint(node);
        }

        if (hasReferenceTypeConstraint(elementDefinition)) {
            return generateReferenceTypeConstraint(node);
        }

        if (hasProfileConstraint(elementDefinition)) {
            return generateProfileConstraint(node);
        }

        StringBuilder sb = new StringBuilder();

        if (hasVocabularyConstraint(elementDefinition)) {
            String expr = generateVocabularyConstraint(node);
            if (!hasChildren(node)) {
                // no constraints exist on the children of this node, the expression is complete
                return expr;
            }
            // there are additional constraints to generate
            sb.append(expr).append(" and ");
        }

        String prefix = prefix(node);
        sb.append(prefix);

        if (hasChildren(node)) {
            if (isRepeating(elementDefinition) && !isSlice(elementDefinition)) {
                if (isRequired(elementDefinition)) {
                    sb.append(".exists() and ").append(prefix);
                }
                sb.append(".all(");
            } else {
                // !isRepeating || isSlice
                sb.append(".where(");
            }
            sb.append(generate(node.children));
            sb.append(")");
        }

        sb.append(cardinality(node, sb.toString()));

        return sb.toString();
    }

    private Constraint constraint(String id, String expr, String description) {
        return createConstraint(id, Constraint.LEVEL_RULE, Constraint.LOCATION_BASE, description, expr, false, true);
    }

    private Node copy(Node node, List<String> paths, String prefix) {
        Node nodeCopy = new Node();
        nodeCopy.label = node.label;
        nodeCopy.elementDefinition = node.elementDefinition;

        for (Node child : node.children) {
            String path = child.elementDefinition.getPath().getValue();
            String relativePath = path.replace(prefix + ".", "");
            if (accept(paths, relativePath)) {
                Node childCopy = copy(child, paths, prefix);
                childCopy.parent = nodeCopy;
                nodeCopy.children.add(childCopy);
            }
        }

        return nodeCopy;
    }

    private String discriminator(Node node) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;
        String identifier = getIdentifier(elementDefinition);

        ElementDefinition sliceDefinition = getSliceDefinition(elementDefinition);
        if (sliceDefinition != null) {
            Slicing slicing = sliceDefinition.getSlicing();

            Map<DiscriminatorType.Value, List<Discriminator>> discriminatorMap = new LinkedHashMap<>();
            for (Discriminator discriminator : slicing.getDiscriminator()) {
                discriminatorMap.computeIfAbsent(discriminator.getType().getValueAsEnum(), k -> new ArrayList<>()).add(discriminator);
            }

            for (DiscriminatorType.Value key : discriminatorMap.keySet()) {
                List<Discriminator> discriminators = discriminatorMap.get(key);

                List<String> paths = discriminators.stream()
                        .map(discriminator -> discriminator.getPath().getValue())
                        .collect(Collectors.toList());

                String prefix = elementDefinition.getPath().getValue();

                if (DiscriminatorType.Value.VALUE.equals(key) ||
                        DiscriminatorType.Value.PATTERN.equals(key) ||
                        DiscriminatorType.Value.PROFILE.equals(key)) {
                    if ("extension".equals(identifier)) {
                        // optimization
                        if (hasProfileConstraint(elementDefinition)) {
                            Type type = getTypes(elementDefinition).get(0);
                            String profile = getProfiles(type).get(0);
                            sb.append("extension('").append(profile).append("')");
                        } else {
                            String url = getExtensionUrl(node);
                            if (url != null) {
                                sb.append("extension('").append(url).append("')");
                            }
                        }
                    } else {
                        sb.append(expression((paths.size() == 1 && "$this".equals(paths.get(0))) ?
                            node :
                            copy(node, paths, prefix)));
                    }
                } else if (DiscriminatorType.Value.TYPE.equals(key)) {
                    Type type = getTypes(elementDefinition).get(0);
                    if (type.getCode() != null) {
                        String code = type.getCode().getValue();
                        sb.append(identifier).append(".as(").append(code).append(")");
                    }
                } else {
                    throw new IllegalArgumentException("Discriminator type: " + key.value() + " is not supported");
                }
            }
        } else {
            throw new IllegalArgumentException("Slice definition not found for slice: " + elementDefinition.getId());
        }

        return sb.toString();
    }

    private String expression(Node node) {
        ElementDefinition elementDefinition = node.elementDefinition;

        if (hasValueConstraint(elementDefinition)) {
            return generateValueConstraint(node, true);
        }

        if (hasProfileConstraint(elementDefinition)) {
            return generateProfileConstraint(node, true);
        }

        if (hasVocabularyConstraint(elementDefinition)) {
            return generateVocabularyConstraint(node);
        }

        StringBuilder sb = new StringBuilder();

        String identifier = getIdentifier(elementDefinition);
        sb.append(identifier);

        if (hasChildren(node)) {
            sb.append(".where(");
            StringJoiner joiner = new StringJoiner(" and ");
            for (Node child : node.children) {
                joiner.add(expression(child));
            }
            sb.append(joiner.toString());
            sb.append(")");
        }

        return sb.toString();
    }

    private String generate(List<Node> nodes) {
        StringJoiner joiner = new StringJoiner(" and ");
        for (Node node : nodes) {
            if (isExtensionUrl(node.elementDefinition)) {
                continue;
            }
            if (isOptional(node.elementDefinition)) {
                joiner.add("(" + generate(node) + ")");
            } else {
                joiner.add(generate(node));
            }
        }
        return joiner.toString();
    }

    private String generate(Node node) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;

        // P (antecedent) -> (implies) Q (consequent)
        if (isOptional(elementDefinition)) {
            sb.append(antecedent(node)).append(" implies (");
        }

        sb.append(consequent(node));

        if (isOptional(elementDefinition)) {
            sb.append(")");
        }

        return trace(node, sb.toString());
    }

    private String generateExtensionConstraint(Node node) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;

        Type type = getTypes(elementDefinition).get(0);
        String profile = getProfiles(type).get(0);

        sb.append("extension('").append(profile).append("')");

        sb.append(cardinality(node, sb.toString()));

        return sb.toString();
    }

    private String generateFixedValueConstraint(Node node, boolean discriminator) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;

        String identifier = getIdentifier(elementDefinition);
        sb.append(identifier);

        Element fixed = elementDefinition.getFixed();
        if (fixed.is(Uri.class)) {
            // fixed uri
            sb.append(" = '").append(fixed.as(Uri.class).getValue()).append("'");
        } else if (fixed.is(Code.class)) {
            // fixed code
            sb.append(" = '").append(fixed.as(Code.class).getValue()).append("'");
        } else if (fixed.is(CodeableConcept.class)) {
            // fixed codeable concept
            CodeableConcept codeableConcept = fixed.as(CodeableConcept.class);
            Coding coding = codeableConcept.getCoding().get(0);
            String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;

            sb.append(".where(coding.where(");

            if (system != null) {
                sb.append("system = '").append(system).append("' and ");
            }

            sb.append("code = '").append(coding.getCode().getValue()).append("').exists())");

            if (!discriminator) {
                sb.append(cardinality(node, sb.toString()));
            }
        }

        return sb.toString();
    }

    private String generatePatternValueConstraint(Node node, boolean discriminator) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;
        String identifier = getIdentifier(elementDefinition);

        Element pattern = elementDefinition.getPattern();
        if (pattern.is(CodeableConcept.class)) {
            CodeableConcept codeableConcept = pattern.as(CodeableConcept.class);
            Coding coding = codeableConcept.getCoding().get(0);
            String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;

            sb.append(identifier).append(".where(coding.where(");

            if (system != null) {
                sb.append("system = '").append(system).append("' and ");
            }

            sb.append("code = '").append(coding.getCode().getValue()).append("').exists())");

            if (!discriminator) {
                sb.append(cardinality(node, sb.toString()));
            }
        } else if (pattern.is(Identifier.class)) {
            Identifier _identifier = pattern.as(Identifier.class);
            String system = _identifier.getSystem().getValue();

            sb.append(identifier).append(".where(system = '").append(system).append("')");

            if (!discriminator) {
                sb.append(cardinality(node, sb.toString()));
            }
        } else if (pattern.is(Uri.class)) {
            sb.append(identifier).append(" = '").append(pattern.as(Uri.class).getValue()).append("'");
        } else if (pattern.is(Code.class)) {
            sb.append(identifier).append(" = '").append(pattern.as(Code.class).getValue()).append("'");
        }

        return sb.toString();
    }

    private String generateProfileConstraint(Node node) {
        return generateProfileConstraint(node, false);
    }

    private String generateProfileConstraint(Node node, boolean discriminator) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;

        String prefix = prefix(node);
        sb.append(prefix);

        if (!isOptional(elementDefinition)) {
            sb.append(".exists() and ").append(prefix);
        }

        if (isRepeating(elementDefinition)) {
            if (isSlice(elementDefinition)) {
                sb.append(".where(");
            } else {
                sb.append(".all(");
            }
        } else {
            sb.append(".");
        }

        Type type = getTypes(elementDefinition).get(0);
        String profile = getProfiles(type).get(0);
        String code = type.getCode().getValue();
        if (isResourceType(code)) {
            sb.append("conformsTo('").append(profile).append("', true)");
        } else {
            sb.append("conformsTo('").append(profile).append("')");
        }

        if (isRepeating(elementDefinition)) {
            sb.append(")");
            if (isSlice(elementDefinition) && !discriminator) {
                sb.append(cardinality(node, sb.toString()));
            }
        }

        return sb.toString();
    }

    private String generateProhibitedConstraint(ElementDefinition elementDefinition) {
        StringBuilder sb = new StringBuilder();
        String identifier = getIdentifier(elementDefinition);
        sb.append(identifier).append(".exists().not()");
        return sb.toString();
    }

    private String generateReferenceTypeConstraint(Node node) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;

        String prefix = prefix(node);

        if (!isOptional(elementDefinition)) {
            sb.append(prefix).append(".exists() and ");
        }

        if (isRepeating(elementDefinition)) {
            sb.append(prefix).append(".all(");
            prefix = "";
        } else {
            prefix = prefix + ".";
        }

        List<String> targetProfiles = getTargetProfiles(getTypes(elementDefinition).get(0));
        StringJoiner joiner = new StringJoiner(" or ");
        for (String targetProfile : targetProfiles) {
            if (isResourceDefinition(targetProfile)) {
                String resourceType = targetProfile.substring(HL7_STRUCTURE_DEFINITION_URL_PREFIX.length());
                joiner.add(prefix + "resolve().is(" + resourceType + ")");
            } else {
                joiner.add(prefix + "resolve().conformsTo('" + targetProfile + "')");
            }
        }
        sb.append(joiner.toString());

        if (isRepeating(elementDefinition)) {
            sb.append(")");
        }

        return sb.toString();
    }

    private String generateValueConstraint(Node node) {
        return generateValueConstraint(node, false);
    }

    private String generateValueConstraint(Node node, boolean discriminator) {
        return hasFixedValueConstraint(node.elementDefinition) ?
                generateFixedValueConstraint(node, discriminator) :
                generatePatternValueConstraint(node, discriminator);
    }

    private String generateVocabularyConstraint(Node node) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;

        String prefix = prefix(node);
        sb.append(prefix);

        if (!isOptional(elementDefinition)) {
            sb.append(".exists() and ").append(prefix);
        }

        if (isRepeating(elementDefinition)) {
            sb.append(".all(");
        } else {
            sb.append(".");
        }

        Binding binding = elementDefinition.getBinding();
        String valueSet = binding.getValueSet().getValue();
        String strength = binding.getStrength().getValue();

        sb.append("memberOf('").append(valueSet).append("', '").append(strength).append("')");

        if (isRepeating(elementDefinition)) {
            sb.append(")");
        }

        // if the binding defines a maxValueSet, then add a "required" binding to the maxValueSet
        String maxValueSet = getMaxValueSet(binding);
        if (maxValueSet != null) {
            sb.append(" and ").append(prefix);

            if (isRepeating(elementDefinition)) {
                sb.append(".all(");
            } else {
                sb.append(".");
            }

            sb.append("memberOf('").append(maxValueSet).append("', '").append(BindingStrength.REQUIRED.getValue()).append("')");

            if (isRepeating(elementDefinition)) {
                sb.append(")");
            }
        }

        return sb.toString();
    }

    private String getExtensionUrl(Node node) {
        for (Node child : node.children) {
            if (isExtensionUrl(child.elementDefinition) && child.elementDefinition.getFixed() instanceof Uri) {
                return child.elementDefinition.getFixed().as(Uri.class).getValue();
            }
        }
        return null;
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

    private String getMaxValueSet(Binding binding) {
        if (binding != null) {
            for (Extension extension : binding.getExtension()) {
                if (MAX_VALUE_SET_EXTENSION_URL.equals(extension.getUrl())) {
                    String valueUri = extension.getValue().is(Uri.class) ? extension.getValue().as(Uri.class).getValue() : null;
                    if (valueUri != null) {
                        return valueUri;
                    }
                    String valueCanonical = extension.getValue().is(Canonical.class) ? extension.getValue().as(Canonical.class).getValue() : null;
                    if (valueCanonical != null) {
                        return valueCanonical;
                    }
                }
            }
        }
        return null;
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

    private ElementDefinition getSliceDefinition(ElementDefinition slice) {
        String id = slice.getId();
        return tree.sliceDefinitionMap.get(id.substring(0, id.lastIndexOf(":")));
    }

    private List<String> getTargetProfiles(List<Type> types) {
        List<String> targetProfiles = new ArrayList<>();
        for (Type type : types) {
            targetProfiles.addAll(getTargetProfiles(type));
        }
        return targetProfiles;
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

    private List<Type> getTypes(ElementDefinition elementDefinition) {
        if (elementDefinition.getContentReference() != null) {
            String contentReference = elementDefinition.getContentReference().getValue();
            int index = contentReference.indexOf("#");
            if (index == -1 || index >= contentReference.length() - 1) {
                throw new IllegalArgumentException("Invalid content reference: " + contentReference);
            }
            contentReference = contentReference.substring(index + 1);
            if (!elementDefinitionMap.containsKey(contentReference)) {
                throw new IllegalArgumentException("Element definition not found for content reference: " + contentReference);
            }
            elementDefinition = elementDefinitionMap.get(contentReference);
        }
        return elementDefinition.getType();
    }

    private boolean hasCardinalityConstraint(ElementDefinition elementDefinition) {
        return isRequired(elementDefinition) || isProhibited(elementDefinition);
    }

    private boolean hasChildren(Node node) {
        return !node.children.isEmpty();
    }

    private boolean hasChoiceTypeConstraint(ElementDefinition elementDefinition) {
        return isChoiceElement(elementDefinition) && getTypes(elementDefinition).size() == 1;
    }

    private boolean hasConstraint(ElementDefinition elementDefinition) {
        return hasCardinalityConstraint(elementDefinition) ||
                hasValueConstraint(elementDefinition) ||
                hasReferenceTypeConstraint(elementDefinition) ||
                hasChoiceTypeConstraint(elementDefinition) ||
                hasVocabularyConstraint(elementDefinition) ||
                hasExtensionConstraint(elementDefinition) ||
                hasProfileConstraint(elementDefinition);
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

    private boolean hasExtensionConstraint(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);

        if (types.size() != 1) {
            return false;
        }

        Type type = types.get(0);
        String code = type.getCode().getValue();
        if (!"Extension".equals(code)) {
            return false;
        }

        List<Canonical> profile = type.getProfile();
        if (profile.size() != 1) {
            return false;
        }

        return true;
    }

    private boolean hasFixedValueConstraint(ElementDefinition elementDefinition) {
        Element fixed = elementDefinition.getFixed();
        return ((fixed instanceof Uri) && (fixed.as(Uri.class).getValue() != null)) ||
               ((fixed instanceof Code) && (fixed.as(Code.class).getValue() != null)) ||
               ((fixed instanceof CodeableConcept) &&
                (fixed.as(CodeableConcept.class).getCoding().stream()
                        .allMatch(coding -> (coding.getCode() != null && coding.getCode().getValue() != null))));
    }

    private boolean hasPatternValueConstraint(ElementDefinition elementDefinition) {
        Element pattern = elementDefinition.getPattern();
        return ((pattern instanceof CodeableConcept) &&
                (pattern.as(CodeableConcept.class).getCoding().stream()
                        .allMatch(coding -> (coding.getCode() != null && coding.getCode().getValue() != null)))) ||
               ((pattern instanceof Identifier) &&
                (pattern.as(Identifier.class).getSystem() != null) &&
                (pattern.as(Identifier.class).getSystem().getValue() != null)) ||
               ((pattern instanceof Uri) && (pattern.as(Uri.class).getValue() != null)) ||
               ((pattern instanceof Code) && (pattern.as(Code.class).getValue() != null));
    }

    private boolean hasProfileConstraint(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);
        if (types.size() == 1) {
            List<String> profiles = getProfiles(types.get(0));
            return (profiles.size() == 1) && !isQuantityProfile(profiles.get(0));
        }
        return false;
    }

    private boolean hasReferenceTypeConstraint(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);
        List<Type> baseTypes = getTypes(getElementDefinition(elementDefinition.getBase().getPath().getValue()));
        List<String> targetProfiles = getTargetProfiles(types);
        List<String> baseTargetProfiles = getTargetProfiles(baseTypes);
        return isReferenceType(types) && !targetProfiles.equals(baseTargetProfiles);
    }

    private boolean hasValueConstraint(ElementDefinition elementDefinition) {
        return hasFixedValueConstraint(elementDefinition) || hasPatternValueConstraint(elementDefinition);
    }

    private boolean hasVocabularyConstraint(ElementDefinition elementDefinition) {
        Binding binding = elementDefinition.getBinding();
        if (binding != null && !BindingStrength.EXAMPLE.equals(binding.getStrength()) && binding.getValueSet() != null &&
                (isCodedElement(elementDefinition) || isQuantityElement(elementDefinition) || isStringElement(elementDefinition) || isUriElement(elementDefinition))) {
            BindingStrength.Value strength = binding.getStrength().getValueAsEnum();
            String valueSet = binding.getValueSet().getValue();

            Binding baseBinding = getBinding(elementDefinition.getBase().getPath().getValue());
            BindingStrength.Value baseStrength = (baseBinding != null) ? baseBinding.getStrength().getValueAsEnum() : null;
            String baseValueSet = (baseBinding != null && baseBinding.getValueSet() != null) ? baseBinding.getValueSet().getValue() : null;

            return (isStronger(strength, baseStrength) || (strength.equals(baseStrength) && !valueSetEqualsIgnoreVersion(valueSet, baseValueSet)));
        }
        return false;
    }

    private boolean isChoiceElement(ElementDefinition elementDefinition) {
        return elementDefinition.getPath().getValue().endsWith("[x]") || elementDefinition.getBase().getPath().getValue().endsWith("[x]");
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

    private boolean isExtensionUrl(ElementDefinition elementDefinition) {
        return "Extension.url".equals(elementDefinition.getBase().getPath().getValue());
    }

    private boolean isOptional(ElementDefinition elementDefinition) {
        return (elementDefinition.getMin().getValue() == 0) && !isProhibited(elementDefinition);
    }

    private boolean isProhibited(ElementDefinition elementDefinition) {
        return "0".equals(elementDefinition.getMax().getValue());
    }

    private boolean isQuantityElement(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);
        if (types.size() != 1) {
            return false;
        }
        Type type = types.get(0);
        if (type.getCode() != null) {
            String code = type.getCode().getValue();
            return "Quantity".equals(code);
        }
        return false;
    }

    private boolean isQuantityProfile(String profile) {
        return SIMPLE_QUANTITY_PROFILE.equals(profile) || MONEY_QUANTITY_PROFILE.equals(profile);
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

    private boolean isRepeating(ElementDefinition elementDefinition) {
        String max = elementDefinition.getBase().getMax().getValue();
        return "*".equals(max) || (Integer.parseInt(max) > 1);
    }

    private boolean isRequired(ElementDefinition elementDefinition) {
        return (elementDefinition.getBase().getMin().getValue() == 0 && elementDefinition.getMin().getValue() > 0);
    }

    private boolean isResourceDefinition(String targetProfile) {
        if (targetProfile.startsWith(HL7_STRUCTURE_DEFINITION_URL_PREFIX)) {
            String s = targetProfile.substring(HL7_STRUCTURE_DEFINITION_URL_PREFIX.length());
            return ModelSupport.isResourceType(s);
        }
        return false;
    }

    private boolean isSlice(ElementDefinition elementDefinition) {
        return elementDefinition.getSliceName() != null;
    }

    private boolean isSliceDefinition(ElementDefinition elementDefinition) {
        return elementDefinition.getSlicing() != null;
    }

    private boolean isStringElement(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);
        if (types.size() != 1) {
            return false;
        }
        Type type = types.get(0);
        if (type.getCode() != null) {
            String code = type.getCode().getValue();
            return "string".equals(code);
        }
        return false;
    }

    private boolean isStronger(BindingStrength.Value strength, BindingStrength.Value baseStrength) {
        return (baseStrength == null) || (strength.ordinal() < baseStrength.ordinal());
    }

    private boolean isUriElement(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);
        if (types.size() != 1) {
            return false;
        }
        Type type = types.get(0);
        if (type.getCode() != null) {
            String code = type.getCode().getValue();
            return "uri".equals(code);
        }
        return false;
    }

    private String prefix(Node node) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;
        String identifier = getIdentifier(elementDefinition);

        sb.append(identifier);

        if ("extension".equals(identifier)) {
            String url = getExtensionUrl(node);
            if (url != null) {
                sb.append("('").append(url).append("')");
            }
        }

        if (hasChoiceTypeConstraint(elementDefinition)) {
            Type type = getTypes(elementDefinition).get(0);
            if (type.getCode() != null) {
                String code = type.getCode().getValue();
                sb.append(".as(").append(code).append(")");
            }
        }

        return sb.toString();
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

    private void prune(Tree tree) {
        List<Node> nodes = prune(tree.root);
        for (Node node : nodes) {
            if (node.parent != null) {
                node.parent.children.remove(node);
            }
            tree.nodeMap.remove(node.elementDefinition.getId(), node);
        }
    }

    private String trace(Node node, String expr) {
        if (log.isLoggable(Level.FINEST)) {
            log.finest(node.elementDefinition.getId() + " -> " + expr);
        }
        return expr;
    }

    private boolean valueSetEqualsIgnoreVersion(String valueSet, String baseValueSet) {
        if (baseValueSet == null) {
            return false;
        }

        int index = valueSet.indexOf("|");
        String url = (index != -1) ? valueSet.substring(0, index) : valueSet;

        index = baseValueSet.indexOf("|");
        String baseUrl = (index != -1) ? baseValueSet.substring(0, index) : baseValueSet;

        return url.equals(baseUrl);
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
}
