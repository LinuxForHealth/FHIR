/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.path.util.FHIRPathUtil.evaluatesToBoolean;
import static com.ibm.fhir.path.util.FHIRPathUtil.isFalse;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Observation.Component;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.util.DiagnosticsEvaluationListener;
import com.ibm.fhir.path.util.EvaluationResultTree;
import com.ibm.fhir.path.util.EvaluationResultTree.Node;
import com.ibm.fhir.path.util.EvaluationResultTree.Visitor;

public class EvaluationResultTreeTest {
    public static final String EXPRESSION = "component.where(code.where(coding.where(system = 'http://loinc.org' and code = '8480-6').exists()).exists()).count() = 1 and component.where(code.where(coding.where(system = 'http://loinc.org' and code = '8480-6').exists()).exists()).all(code.exists() and code.all(coding.where(system = 'http://loinc.org' and code = '8480-6').count() = 1 and memberOf('http://hl7.org/fhir/ValueSet/observation-vitalsignresult', 'extensible')) and (value.exists() implies (value.as(Quantity).exists() and value.as(Quantity).all(value.exists() and unit.exists() and system = 'http://unitsofmeasure.org' and code = 'mm[Hg]' and memberOf('http://hl7.org/fhir/ValueSet/ucum-vitals-common|4.0.1', 'required')))) and (value.exists() or dataAbsentReason.exists()))";

    public static void main(String[] args) throws Exception {
        Reader reader = ExamplesUtil.resourceReader("json/spec/observation-example-bloodpressure.json");

        Observation observation = FHIRParser.parser(Format.JSON).parse(reader);
        System.out.println(observation);
        System.out.println("");

        List<Component> component = new ArrayList<>(observation.getComponent());
        component.set(0, component.get(0).toBuilder()
            .value((Element)null)
            .build());
        observation = observation.toBuilder()
            .component(component)
            .build();
        System.out.println(observation);
        System.out.println("");

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(observation);

        DiagnosticsEvaluationListener diagnosticsEvaluationListener = new DiagnosticsEvaluationListener();
        evaluationContext.addEvaluationListener(diagnosticsEvaluationListener);

        Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, EXPRESSION);
        System.out.println("result: " + result);

        if (evaluatesToBoolean(result) && isFalse(result)) {
            EvaluationResultTree resultTree = diagnosticsEvaluationListener.getEvaluationResultTree();

            resultTree.getRoot().accept(new PrintingVisitor());
            System.out.println("");

            Issue issue = Issue.builder()
                .severity(IssueSeverity.ERROR)
                .code(IssueType.INVARIANT)
                .details(CodeableConcept.builder()
                    .text(string("Constraint violation: " + EXPRESSION))
                    .build())
                .diagnostics(string(diagnosticsEvaluationListener.getDiagnostics()))
                .expression(string("Observation"))
                .build();

            System.out.println(issue);
        }

        evaluationContext.removeEvaluationListener(diagnosticsEvaluationListener);
    }

    public static class CollectingVisitor implements Visitor {
        private final Predicate<Node> predicate;
        private final List<Node> result = new ArrayList<>();

        public CollectingVisitor(Predicate<Node> predicate) {
            this.predicate = predicate;
        }

        public List<Node> getResult() {
            return result;
        }

        @Override
        public void visit(Node node) {
            collect(node);
            for (Node child : node.getChildren()) {
                visit(child);
            }
        }

        private void collect(Node node) {
            if (predicate.test(node)) {
                result.add(node);
            }
        }
    }

    private static class PrintingVisitor implements Visitor {
        private int indentLevel = 0;

        @Override
        public void visit(Node node) {
            print(node);
            indentLevel++;
            for (Node child : node.getChildren()) {
                visit(child);
            }
            indentLevel--;
        }

        private void print(Node node) {
            System.out.println(indent() + node + ", paths: " + getPaths(node.getContext()));
        }

        private String indent() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < indentLevel; i++) {
                sb.append("    ");
            }
            return sb.toString();
        }

        private List<String> getPaths(Collection<FHIRPathNode> context) {
            List<String> paths = new ArrayList<>();
            for (FHIRPathNode node : context) {
                paths.add(node.path());
            }
            return paths;
        }
    }
}
