/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile;

import static com.ibm.fhir.model.util.ModelSupport.delimit;
import static com.ibm.fhir.model.util.ModelSupport.isKeyword;
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
import com.ibm.fhir.model.type.code.SlicingRules;
import com.ibm.fhir.model.util.ModelSupport;

/**
 * A class used to generate FHIRPath expressions from a profile
 */
public class ConstraintGenerator {
    private static final Logger log = Logger.getLogger(ConstraintGenerator.class.getName());

    private static final String MONEY_QUANTITY_PROFILE = "http://hl7.org/fhir/StructureDefinition/MoneyQuantity";
    private static final String SIMPLE_QUANTITY_PROFILE = "http://hl7.org/fhir/StructureDefinition/SimpleQuantity";
    private static final String MAX_VALUE_SET_EXTENSION_URL = "http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet";

    private final StructureDefinition profile;
    private final Map<String, ElementDefinition> elementDefinitionMap;
    private final Tree tree;

    public ConstraintGenerator(StructureDefinition profile) {
        Objects.requireNonNull(profile);
        this.profile = profile;
        elementDefinitionMap = buildElementDefinitionMap(this.profile);
        tree = buildTree(this.profile);
    }

    private Map<String, ElementDefinition> buildElementDefinitionMap(StructureDefinition profile2) {
        Map<String, ElementDefinition> elementDefinitionMap = new LinkedHashMap<>();
        for (ElementDefinition elementDefinition : profile.getSnapshot().getElement()) {
            elementDefinitionMap.put(elementDefinition.getId(), elementDefinition);
        }
        return elementDefinitionMap;
    }

    public List<Constraint> generate() {
        List<Constraint> constraints = new ArrayList<>();

        String url = profile.getUrl().getValue();
        String prefix = url.substring(url.lastIndexOf("/") + 1);

        int index = 1;

        Set<String> generated = new HashSet<>();

        log.finest("Generated constraint expressions:");
        for (Node child : tree.root.children) {
            String expr = generate(child);
            if (generated.contains(expr)) {
                continue;
            }
            log.finest(expr);
            String description = "Constraint violation: " + expr;
            constraints.add(constraint("generated-" + prefix + "-" + index, expr, description));
            index++;
            generated.add(expr);
        }
        log.finest("");

        return constraints;
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

    private Constraint constraint(String id, String expr, String description) {
        return createConstraint(id, Constraint.LEVEL_RULE, Constraint.LOCATION_BASE, description, expr, false, true);
    }

    private String generate(Node node) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;
        String identifier = getIdentifier(elementDefinition);

        if (isProhibited(elementDefinition)) {
            sb.append(identifier).append(".exists().not()");
            return sb.toString();
        }

        if (hasValueConstraint(elementDefinition)) {
            return generateValueConstraint(node);
        }

        if (hasReferenceTypeConstraint(elementDefinition)) {
            return generateReferenceTypeConstraint(elementDefinition);
        }

        if (hasVocabularyConstraint(elementDefinition)) {
            String expr = generateVocabularyConstraint(elementDefinition);
            if (node.children.isEmpty()) {
                // no constraints exist on the children of this node, the expression is complete
                return expr;
            }
            // there are additional constraints to generate
            sb.append(expr).append(" and ");
        }

        if (hasExtensionConstraint(elementDefinition)) {
            return generateExtensionConstraint(elementDefinition);
        }

        if (isOptional(elementDefinition)) {
            sb.append(identifier);

            if ("extension".equals(identifier)) {
                String url = getExtensionUrl(node);
                if (url != null) {
                    sb.append("('").append(url).append("')");
                }
            } else if (isSlice(elementDefinition)) {
                ElementDefinition sliceDefinition = getSliceDefinition(elementDefinition);
                if (sliceDefinition != null) {
                    Slicing slicing = sliceDefinition.getSlicing();
                    StringJoiner joiner = new StringJoiner(" and ");
                    for (Discriminator discriminator : slicing.getDiscriminator()) {
                        if (DiscriminatorType.VALUE.equals(discriminator.getType()) || DiscriminatorType.PATTERN.equals(discriminator.getType())) {
                            String id = elementDefinition.getId() + "." + discriminator.getPath().getValue();
                            if (tree.nodeMap.containsKey(id)) {
                                Node dNode = tree.nodeMap.get(id);
                                if (hasValueConstraint(dNode.elementDefinition)) {
                                    joiner.add(generateValueConstraint(dNode));
                                } else if (hasVocabularyConstraint(dNode.elementDefinition)) {
                                    joiner.add(generateVocabularyConstraint(dNode.elementDefinition));
                                } else {
                                    log.fine("Discriminator has no value or vocabulary constraint");
                                }
                            } else {
                                // no discriminator in the slice
                                if (SlicingRules.CLOSED.equals(slicing.getRules())) {
                                    // in the case of a closed slice with no discriminator
                                    // we use the absence of the discriminator field to determine we're in this slice
                                    String dPath = discriminator.getPath().getValue();
                                    joiner.add(dPath + ".exists().not()");
                                } else {
                                    // setting false will cause the `implies` to short-circuit,
                                    // effectively skipping the constraints on this slice
                                    joiner.add("false");
                                    log.fine("Slice for '" + id + "' is open and missing discriminator");
                                }
                            }
                        } else if (DiscriminatorType.TYPE.equals(discriminator.getType())) {
                            Type type = getTypes(elementDefinition).get(0);
                            if (type.getCode() != null) {
                                String code = type.getCode().getValue();
                                sb.append(".as(").append(code).append(")");
                            }
                        }
                    }
                    if (joiner.length() > 0) {
                        sb.append(".where(").append(joiner.toString()).append(")");
                    }
                }
            }

            sb.append(".exists() implies (");
        }

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

        if (!node.children.isEmpty()) {
            if (isRepeating(elementDefinition) && !isSlice(elementDefinition)) {
                if (isRequired(elementDefinition)) {
                    sb.append(".exists() and ").append(identifier);
                }
                sb.append(".all(");
            } else {
                // !isRepeating || isSlice
                sb.append(".where(");
            }

            sb.append(generate(node.children));

            sb.append(")");

            if (!isRepeating(elementDefinition) || isSlice(elementDefinition)) {
                sb.append(".exists()");
            }
        } else {
            sb.append(".exists()");
        }

        if (hasProfileConstraint(elementDefinition)) {
            String profile = getProfiles(getTypes(elementDefinition).get(0)).get(0);
            sb.append(" and ");
            if (isRepeating(elementDefinition)) {
                sb.append(identifier).append(".all(conformsTo('").append(profile).append("'))");
            } else {
                sb.append(identifier).append(".conformsTo('").append(profile).append("')");
            }
        }

        if (isOptional(elementDefinition)) {
            sb.append(")");
        }

        return sb.toString();
    }

    private ElementDefinition getSliceDefinition(ElementDefinition slice) {
        String id = slice.getId();
        return tree.sliceDefinitionMap.get(id.substring(0, id.lastIndexOf(":")));
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

    private String generateExtensionConstraint(ElementDefinition elementDefinition) {
        StringBuilder sb = new StringBuilder();

        Type type = getTypes(elementDefinition).get(0);
        String profile = getProfiles(type).get(0);

        sb.append("extension('").append(profile).append("').count()");

        Integer min = elementDefinition.getMin().getValue();
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
            sb.append(" >= ").append(min).append(" and ").append("extension('").append(profile).append("').count() <= ").append(max);
        }

        return sb.toString();
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
        } else if (fixed.is(CodeableConcept.class)) {
            // fixed codeable concept
            CodeableConcept codeableConcept = fixed.as(CodeableConcept.class);
            Coding coding = codeableConcept.getCoding().get(0);
            String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;

            sb.append(".where(coding.where(");

            if (system != null) {
                sb.append("system = '").append(system).append("' and ");
            }

            sb.append("code = '").append(coding.getCode().getValue()).append("').exists()).exists()");
        }

        return sb.toString();
    }

    private String generatePatternValueConstraint(Node node) {
        StringBuilder sb = new StringBuilder();

        ElementDefinition elementDefinition = node.elementDefinition;

        String identifier = getIdentifier(elementDefinition);
        sb.append(identifier);

        Element pattern = elementDefinition.getPattern();
        if (pattern.is(CodeableConcept.class)) {
            CodeableConcept codeableConcept = pattern.as(CodeableConcept.class);
            Coding coding = codeableConcept.getCoding().get(0);
            String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;

            sb.append(".where(coding.where(");

            if (system != null) {
                sb.append("system = '").append(system).append("' and ");
            }

            sb.append("code = '").append(coding.getCode().getValue()).append("').exists()).exists()");
        } else if (pattern.is(Identifier.class)) {
            Identifier _identifier = pattern.as(Identifier.class);
            String system = _identifier.getSystem().getValue();

            sb.append(".where(system = '").append(system).append("').count()");

            Integer min = elementDefinition.getMin().getValue();
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
                sb.append(" >= ").append(min).append(" and ").append(identifier).append(".where(system = '").append(system).append("').count() <= ").append(max);
            }

            if (!node.children.isEmpty()) {
                sb.append(" and (").append(identifier).append(".where(system = '").append(system).append("').exists()").append(" implies (").append(identifier).append(".where(system = '").append(system).append("' and ").append(generate(node.children)).append(")))");
            }
        }

        return sb.toString();
    }

    private String generateReferenceTypeConstraint(ElementDefinition elementDefinition) {
        StringBuilder sb = new StringBuilder();

        String identifier = getIdentifier(elementDefinition);

        sb.append(identifier);
        if (hasChoiceTypeConstraint(elementDefinition)) {
            sb.append(".as(Reference)");
        }

        String prefix = sb.toString();

        if (isOptional(elementDefinition)) {
            sb.append(".exists() implies (");
        } else {
            sb.append(".exists() and ");
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

        if (isOptional(elementDefinition)) {
            sb.append(")");
        }

        return sb.toString();
    }

    private String generateValueConstraint(Node node) {
        return hasFixedValueConstraint(node.elementDefinition) ? generateFixedValueConstraint(node.elementDefinition) : generatePatternValueConstraint(node);
    }

    /**
     * Generates FHIRPath constraint based on the cardinality, choice type, and binding of the element.
     * @param elementDefinition the element definition
     * @return the FHIRPath constraint
     */
    private String generateVocabularyConstraint(ElementDefinition elementDefinition) {
        StringBuilder sb = new StringBuilder();

        String identifier = getIdentifier(elementDefinition);

        sb.append(identifier);
        if (hasChoiceTypeConstraint(elementDefinition)) {
            Type type = getTypes(elementDefinition).get(0);
            if (type.getCode() != null) {
                String code = type.getCode().getValue();
                sb.append(".as(").append(code).append(")");
            }
        }

        if (isOptional(elementDefinition)) {
            sb.append(".exists() implies (");
        } else {
            sb.append(".exists() and ");
        }

        sb.append(identifier);
        if (hasChoiceTypeConstraint(elementDefinition)) {
            Type type = getTypes(elementDefinition).get(0);
            if (type.getCode() != null) {
                String code = type.getCode().getValue();
                sb.append(".as(").append(code).append(")");
            }
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

        // If the binding defines a maxValueSet, then add a "required" binding to the maxValueSet
        String maxValueSet = getMaxValueSet(binding);
        if (maxValueSet != null) {
            sb.append(" and ").append(identifier);
            if (hasChoiceTypeConstraint(elementDefinition)) {
                Type type = getTypes(elementDefinition).get(0);
                if (type.getCode() != null) {
                    String code = type.getCode().getValue();
                    sb.append(".as(").append(code).append(")");
                }
            }

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

        if (isOptional(elementDefinition)) {
            sb.append(")");
        }

        return sb.toString();
    }

    /**
     * Gets the URI or Canonical of the maximum allowable value set. This defines a 'required' binding over the top of the 'extensible' or 'preferred' binding.
     * @param binding the binding
     * @return the URI or Canonical of the maximum allowable value set, or null
     */
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

    private List<String> getProfiles(Type type) {
        List<String> profiles = new ArrayList<>();
        for (Canonical profile : type.getProfile()) {
            if (profile.getValue() != null) {
                profiles.add(profile.getValue());
            }
        }
        return profiles;
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

    private List<String> getTargetProfiles(List<Type> types) {
        List<String> targetProfiles = new ArrayList<>();
        for (Type type : types) {
            targetProfiles.addAll(getTargetProfiles(type));
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

    private boolean hasProfileConstraint(ElementDefinition elementDefinition) {
        List<Type> types = getTypes(elementDefinition);
        if (types.size() == 1) {
            List<String> profiles = getProfiles(types.get(0));
            return (profiles.size() == 1) && !isQuantityProfile(profiles.get(0));
        }
        return false;
    }

    private boolean isQuantityProfile(String profile) {
        return SIMPLE_QUANTITY_PROFILE.equals(profile) || MONEY_QUANTITY_PROFILE.equals(profile);
    }

    private boolean hasFixedValueConstraint(ElementDefinition elementDefinition) {
        return (elementDefinition.getFixed() instanceof Uri || elementDefinition.getFixed() instanceof Code || elementDefinition.getFixed() instanceof CodeableConcept);
    }

    private boolean hasPatternValueConstraint(ElementDefinition elementDefinition) {
        Element pattern = elementDefinition.getPattern();
        return ((pattern instanceof CodeableConcept) &&
                (pattern.as(CodeableConcept.class).getCoding().stream()
                        .allMatch(coding -> (coding.getCode() != null && coding.getCode().getValue() != null)))) ||
               ((pattern instanceof Identifier) &&
                (pattern.as(Identifier.class).getSystem() != null) &&
                (pattern.as(Identifier.class).getSystem().getValue() != null));
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

    private boolean isStronger(BindingStrength.Value strength, BindingStrength.Value baseStrength) {
        return (baseStrength == null) || (strength.ordinal() < baseStrength.ordinal());
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

    private boolean isExtensionUrl(ElementDefinition elementDefinition) {
        return "Extension.url".equals(elementDefinition.getBase().getPath().getValue());
    }

    private boolean isOptional(ElementDefinition elementDefinition) {
        return (elementDefinition.getMin().getValue() == 0) && !"0".equals(elementDefinition.getMax().getValue());
    }

    private boolean isProhibited(ElementDefinition elementDefinition) {
        return "0".equals(elementDefinition.getMax().getValue());
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
        String max = elementDefinition.getMax().getValue();
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
