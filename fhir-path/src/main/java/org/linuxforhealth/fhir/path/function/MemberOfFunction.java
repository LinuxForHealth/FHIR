/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.model.util.ModelSupport.FHIR_STRING;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.empty;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getDisplay;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getElementNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getString;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getSystem;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getVersion;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isCodedElementNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isQuantityNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isStringElementNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isStringValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isUriElementNode;
import static org.linuxforhealth.fhir.term.util.ValueSetSupport.getValueSet;
import static org.linuxforhealth.fhir.term.util.ValueSetSupport.isExpanded;

import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.ValueSet;
import org.linuxforhealth.fhir.model.resource.ValueSet.Compose;
import org.linuxforhealth.fhir.model.resource.ValueSet.Compose.Include;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.CodeableReference;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.path.FHIRPathElementNode;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathTree;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.term.service.FHIRTermService;
import org.linuxforhealth.fhir.term.service.ValidationOutcome;
import org.linuxforhealth.fhir.term.service.exception.FHIRTermServiceException;
import org.linuxforhealth.fhir.term.util.CodeSystemSupport;

/**
 * Implementation of the 'memberOf' FHIRPath function per: <a href="http://hl7.org/fhir/fhirpath.html#functions">http://hl7.org/fhir/fhirpath.html#functions</a>
 *
 * <p>This implementation supports an optional second argument (binding strength). The binding strength
 * is used to determine whether or not to add a warning to the evaluation context if the membership check fails.
 *
 * <p>In addition, if the optional argument is specified, this implementation will successfully validate a Code, Coding,
 * Quantity, String, or Uri element in which the <a href="http://hl7.org/fhir/StructureDefinition/data-absent-reason">data-absent-reason</a>
 * extension is specified and no value or code+system is specified.
 */
public class MemberOfFunction extends FHIRPathAbstractFunction {
    public static final String ALL_LANG_VALUE_SET_URL = "http://hl7.org/fhir/ValueSet/all-languages";
    public static final String UCUM_UNITS_VALUE_SET_URL = "http://hl7.org/fhir/ValueSet/ucum-units";

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
        return 2;
    }

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (context.isEmpty()) {
            return empty();
        }

        if (!isCodedElementNode(context) && !isQuantityNode(context) && !isStringElementNode(context) && !isUriElementNode(context)) {
            throw new IllegalArgumentException("The 'memberOf' function must be invoked on a coded element node, quantity element node, string element node, or uri element node");
        }

        if (!isStringValue(arguments.get(0))) {
            throw new IllegalArgumentException("The argument to the 'memberOf' function must be a string value");
        }

        if (arguments.size() == 2 && !isStringValue(arguments.get(1))) {
            throw new IllegalArgumentException("The optional second argument to the 'memberOf' function must be a string value");
        }

        FHIRPathElementNode elementNode = getElementNode(context);
        Element element = elementNode.element();
        String url = getString(arguments.get(0));
        String strength = (arguments.size() == 2) ? getString(arguments.get(1)) : null;

        ValueSet valueSet = getValueSet(url);
        if (valueSet != null) {
            // Validate against data-absent-reason extension (only if using extended version of operation)
            if (strength != null && ValidationSupport.hasOnlyDataAbsentReasonExtension(element)) {
                return SINGLETON_TRUE;
            }
            FHIRTermService service = FHIRTermService.getInstance();
            if (isExpanded(valueSet) || service.isExpandable(valueSet)) {
                try {
                    // Validate against expanded value set
                    if (element.is(Code.class)) {
                        FHIRPathTree tree = evaluationContext.getTree();
                        Uri system = getSystem(tree, elementNode);
                        org.linuxforhealth.fhir.model.type.String version = getVersion(tree, elementNode);
                        Code code = element.as(Code.class);
                        org.linuxforhealth.fhir.model.type.String display = getDisplay(tree, elementNode);
                        if ((system != null && validateCode(service, valueSet, system, version, code, display, evaluationContext, elementNode, strength)) ||
                                (system == null && validateCode(service, valueSet, code, evaluationContext, elementNode, strength))) {
                            return SINGLETON_TRUE;
                        }
                    } else if (element.is(Coding.class)) {
                        Coding coding = element.as(Coding.class);
                        if (validateCode(service, valueSet, coding, evaluationContext, elementNode, strength)) {
                            return SINGLETON_TRUE;
                        }
                    } else if (element.is(CodeableConcept.class)) {
                        CodeableConcept codeableConcept = element.as(CodeableConcept.class);
                        if (codeableConcept.getCoding() != null && validateCode(service, valueSet, codeableConcept, evaluationContext, elementNode, strength)) {
                            return SINGLETON_TRUE;
                        }
                    } else if (element.is(CodeableReference.class)) {
                        CodeableConcept concept = element.as(CodeableReference.class).getConcept();
                        if (concept == null) {
                            // A codeableReference with a required binding is allowed to have a reference instead of a concept
                            return SINGLETON_TRUE;
                        }
                        // If we have a concept, then it follow the same rules as a normal CodeableConcept
                        if (concept.getCoding() != null && validateCode(service, valueSet, concept, evaluationContext, elementNode, strength)) {
                            return SINGLETON_TRUE;
                        }
                    } else if (element.is(Quantity.class)) {
                        Quantity quantity = element.as(Quantity.class);
                        if (validateCode(service, valueSet, quantity.getSystem(), null, quantity.getCode(), null, evaluationContext, elementNode, strength)) {
                            return SINGLETON_TRUE;
                        }
                    } else {
                        // element.is(FHIR_STRING) || element.is(Uri.class)
                        Code code = element.is(FHIR_STRING) ? Code.of(element.as(FHIR_STRING).getValue()) : Code.of(element.as(Uri.class).getValue());
                        if (validateCode(service, valueSet, code, evaluationContext, elementNode, strength)) {
                            return SINGLETON_TRUE;
                        }
                    }
                    return membershipCheckFailed(evaluationContext, elementNode, url, strength);
                } catch (FHIRTermServiceException e) {
                    generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.INCOMPLETE, "Membership check was not performed: value set '" + url + "' membership could not be checked due to the following error: '" + e.getMessage() + "'", elementNode.path());
                }
            } else if (isSyntaxBased(valueSet)) {
                // Validate against syntax-based value set
                try {
                    ValidationSupport.checkValueSetBinding(elementNode.element(), elementNode.path(), valueSet.getUrl().getValue(), null);
                    return SINGLETON_TRUE;
                } catch (IllegalStateException e) {
                    generateIssue(e.getMessage(), evaluationContext, elementNode, strength);
                }
                return membershipCheckFailed(evaluationContext, elementNode, url, strength);
            } else {
                generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.INCOMPLETE, "Membership check was not performed: value set '" + url + "' is empty or could not be expanded", elementNode.path());
            }
        } else {
            generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, "Membership check was not performed: value set '" + url + "' is not supported", elementNode.path());
        }

        return SINGLETON_TRUE;
    }

    /**
     * Determines if the value set is syntax-based.
     * @param valueSet the value set
     * @return true or false
     */
    private boolean isSyntaxBased(ValueSet valueSet) {
        String valueSetUrl = valueSet.getUrl() != null ? valueSet.getUrl().getValue() : null;
        return ALL_LANG_VALUE_SET_URL.equals(valueSetUrl) || UCUM_UNITS_VALUE_SET_URL.equals(valueSetUrl);
    }

    private boolean validateCode(FHIRTermService service, ValueSet valueSet, Code code, EvaluationContext evaluationContext, FHIRPathElementNode elementNode, String strength) {
        ValidationOutcome outcome = service.validateCode(valueSet, code);
        if (Boolean.FALSE.equals(outcome.getResult())) {
            generateIssue(outcome, evaluationContext, elementNode, strength);
            return false;
        }
        return true;
    }

    private boolean validateCode(FHIRTermService service, ValueSet valueSet, Uri system, org.linuxforhealth.fhir.model.type.String version, Code code, org.linuxforhealth.fhir.model.type.String display, EvaluationContext evaluationContext, FHIRPathElementNode elementNode, String strength) {
        if (system == null && version == null && code == null && display == null) {
            return false;
        }
        ValidationOutcome outcome = null;
        if (convertsToCodeSystemValidateCode(valueSet, system, version, code)) {
            // optimization
            CodeSystem codeSystem = getCodeSystem(valueSet, system, version);
            outcome = service.validateCode(codeSystem, code, display);
        } else {
            outcome = service.validateCode(valueSet, system, version, code, display);
        }
        if (Boolean.FALSE.equals(outcome.getResult())) {
            generateIssue(outcome, evaluationContext, elementNode, strength);
            return false;
        }
        return true;
    }

    private boolean validateCode(FHIRTermService service, ValueSet valueSet, Coding coding,
            EvaluationContext evaluationContext, FHIRPathElementNode elementNode, String strength) {
        ValidationOutcome outcome = null;
        if (convertsToCodeSystemValidateCode(valueSet, coding)) {
            // optimization
            CodeSystem codeSystem = getCodeSystem(valueSet, coding);
            outcome = service.validateCode(codeSystem, coding);
        } else {
            outcome = service.validateCode(valueSet, coding);
        }
        if (Boolean.FALSE.equals(outcome.getResult())) {
            generateIssue(outcome, evaluationContext, elementNode, strength);
            return false;
        }
        return true;
    }

    private boolean validateCode(FHIRTermService service, ValueSet valueSet, CodeableConcept codeableConcept,
            EvaluationContext evaluationContext, FHIRPathElementNode elementNode, String strength) {
        ValidationOutcome outcome = null;
        if (convertsToCodeSystemValidateCode(valueSet, codeableConcept)) {
            // optimization
            for (Coding coding : codeableConcept.getCoding()) {
                CodeSystem codeSystem = getCodeSystem(valueSet, coding);
                outcome = service.validateCode(codeSystem, coding);
                if (Boolean.TRUE.equals(outcome.getResult())) {
                    break;
                }
            }
            if (Boolean.FALSE.equals(outcome.getResult())) {
                outcome = outcome.toBuilder()
                        .message(null)
                        .display(null)
                        .build();
            }
        } else {
            outcome = service.validateCode(valueSet, codeableConcept);
        }

        if (Boolean.FALSE.equals(outcome.getResult())) {
            generateIssue(outcome, evaluationContext, elementNode, strength);
            return false;
        } else if (outcome.getMessage() != null) {
            // this uses inside knowledge about the reason an outcome might come back true but with a message;
            // is there a cleaner way to get this supplemental detail back from the validateCode?
            generateIssue(evaluationContext, IssueSeverity.INFORMATION, IssueType.NOT_SUPPORTED, outcome.getMessage().getValue(), elementNode.path());
        }
        return true;
    }

    /**
     * Adds an issue to the evaluation context.
     * @param outcome the validation outcome containing the message
     * @param evaluationContext the evaluation context
     * @param elementNode the element name
     * @param strength the binding strength
     */
    private void generateIssue(ValidationOutcome outcome, EvaluationContext evaluationContext, FHIRPathElementNode elementNode, String strength) {
        if (outcome.getMessage() != null) {
            generateIssue(outcome.getMessage().getValue(), evaluationContext, elementNode, strength);
        }
    }

    /**
     * Adds an issue to the evaluation context.
     * @param message the message
     * @param evaluationContext the evaluation context
     * @param elementNode the element name
     * @param strength the binding strength
     */
    private void generateIssue(String message, EvaluationContext evaluationContext, FHIRPathElementNode elementNode, String strength) {
        IssueSeverity severity = ("required".equals(strength)) ? IssueSeverity.ERROR : IssueSeverity.INFORMATION;
        generateIssue(evaluationContext, severity, IssueType.CODE_INVALID, message, elementNode.path());
    }

    private Collection<FHIRPathNode> membershipCheckFailed(EvaluationContext evaluationContext, FHIRPathElementNode elementNode, String url, String strength) {
        if ("extensible".equals(strength) || "preferred".equals(strength)) {
            String prefix = evaluationContext.hasConstraint() ? evaluationContext.getConstraint().id() + ": " : "";
            String description = prefix + "A code in this element " + ("extensible".equals(strength) ? "must" : "should") + " be from the specified value set '" + url + "' if possible";
            generateIssue(evaluationContext, IssueSeverity.INFORMATION, IssueType.CODE_INVALID, description, elementNode.path());
            return SINGLETON_TRUE;
        }
        return SINGLETON_FALSE;
    }

    private boolean convertsToCodeSystemValidateCode(ValueSet valueSet, CodeableConcept codeableConcept) {
        if (codeableConcept.getCoding().isEmpty()) {
            return false;
        }
        for (Coding coding : codeableConcept.getCoding()) {
            if (!convertsToCodeSystemValidateCode(valueSet, coding)) {
                return false;
            }
        }
        return true;
    }

    private boolean convertsToCodeSystemValidateCode(ValueSet valueSet, Coding coding) {
        return convertsToCodeSystemValidateCode(valueSet, coding.getSystem(), coding.getVersion(), coding.getCode());
    }

    private boolean convertsToCodeSystemValidateCode(ValueSet valueSet, Uri system, org.linuxforhealth.fhir.model.type.String version, Code code) {
        if (system == null || system.getValue() == null || code == null || code.getValue() == null) {
            return false;
        }

        if (isExpanded(valueSet)) {
            return false;
        }

        Compose compose = valueSet.getCompose();

        if (!compose.getExclude().isEmpty()) {
            return false;
        }

        for (Include include : compose.getInclude()) {
            if (!include.getConcept().isEmpty() ||
                    !include.getFilter().isEmpty() ||
                    !include.getValueSet().isEmpty() ||
                    include.getSystem() == null) {
                return false;
            }
        }

        return hasCodeSystem(valueSet, system, version);
    }

    private boolean hasCodeSystem(ValueSet valueSet, Uri system, org.linuxforhealth.fhir.model.type.String version) {
        return getCodeSystem(valueSet, system, version) != null;
    }

    private CodeSystem getCodeSystem(ValueSet valueSet, Coding coding) {
        return getCodeSystem(valueSet, coding.getSystem(), coding.getVersion());
    }

    private CodeSystem getCodeSystem(ValueSet valueSet, Uri system, org.linuxforhealth.fhir.model.type.String version) {
        Compose compose = valueSet.getCompose();
        for (Include include : compose.getInclude()) {
            if (include.getSystem().equals(system) &&
                    (include.getVersion() == null || version == null || include.getVersion().equals(version))) {

                String url = system.getValue();
                if (version != null && version.hasValue()) {
                    url += "|" + version.getValue();
                } else if (include.getVersion() != null && include.getVersion().hasValue()) {
                    url += "|" + include.getVersion().getValue();
                }

                return CodeSystemSupport.getCodeSystem(url);
            }
        }
        return null;
    }
}
