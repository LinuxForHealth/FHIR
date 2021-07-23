/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Predicate;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationListener;

/**
 * A tree of nodes used to store evaluation results for each parser rule context visited by the evaluator
 */
public class EvaluationResultTree {
    private final Node root;

    private EvaluationResultTree(Node root) {
        this.root = Objects.requireNonNull(root, "root");
    }

    /**
     * Get the root node of this evaluation result tree.
     *
     * @return
     *     the root node of this evaluation result tree
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Get the path from the given node to the root node of this evaluation result tree.
     *
     * @param node
     *     the node
     * @return
     *     the path from the given node to the root node of this evaluation result tree
     */
    public List<Node> getPathToRoot(Node node){
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.getParent();
        }
        return path;
    }

    /**
     * Get the leaves of this evaluation result tree.
     *
     * @return
     *     the leaves of this evaluation result tree
     */
    public List<Node> getLeaves() {
        return getLeaves(root);
    }

    private List<Node> getLeaves(Node node) {
        List<Node> leaves = new ArrayList<>();
        if (node.isLeaf()) {
            leaves.add(node);
        }
        for (Node child : node.getChildren()) {
            leaves.addAll(getLeaves(child));
        }
        return leaves;
    }

    /**
     * An interface that represents a node in the evaluation result tree
     */
    public interface Node {
        /**
         * Get the parser rule context associated with this evaluation result tree node.
         *
         * @return
         *     the parser rule context associated with this evaluation result tree node
         */
        ParserRuleContext getParserRuleContext();

        /**
         * Get the FHIRPath evaluator context associated with this evaluation result tree node.
         *
         * @return
         *     the FHIRPath evaluator context
         */
        Collection<FHIRPathNode> getContext();

        /**
         * Get the FHIRPath evaluation result associated with this evaluation result tree node.
         *
         * @return
         *     the FHIRPath evaluation result associated with this evaluation result tree node.
         */
        Collection<FHIRPathNode> getResult();

        /**
         * Get the parent of this evaluation result tree node.
         *
         * @return
         *     the parent of this evaluation result tree node, or null if not exists
         */
        Node getParent();

        /**
         * Get the children of this evaluation result tree node.
         *
         * @return
         *     the children of this evaluation result tree node
         */
        List<Node> getChildren();

        /**
         * Indicates whether this evaluation result tree node is the root node.
         *
         * @return
         *     true if this evaluation result tree node is the root node, false otherwise
         */
        boolean isRoot();

        /**
         * Indicates whether this evaluation result tree node is an internal node.
         *
         * @return
         *     true if this evaluation result tree node is an internal node, false otherwise
         */
        boolean isInternal();

        /**
         * Indicates whether this evaluation result tree node is a leaf node.
         *
         * @return
         *     true if this evaluation result tree node is a leaf node, false otherwise
         */
        boolean isLeaf();

        /**
         * Get the depth of this evaluation result tree node.
         *
         * @return
         *     the depth of this evaluation result tree node
         */
        int getDepth();

        /**
         * Get the text (expression, term, or literal) for this evaluation result tree node.
         *
         * @return
         *     the text (expression, term, or literal) for this evaluation result tree node.
         */
        String getText();

        /**
         * Accept a visitor to this evaluation result tree node.
         *
         * @param visitor
         *     the visitor
         */
        void accept(Visitor visitor);
    }

    /**
     * A visitor interface used to visit nodes in an evaluation result tree
     */
    public interface Visitor {
        /**
         * Visit the given evaluation result tree node.
         *
         * @param node
         *     the node
         */
        void visit(Node node);
    }

    /**
     * An implementation of EvaluationListener that builds an EvaluationResultTree
     */
    public static class BuildingEvaluationListener implements EvaluationListener {
        protected final Predicate<ParserRuleContext> predicate;
        protected final Stack<MutableNode> resultTreeNodeStack = new Stack<>();
        protected EvaluationResultTree resultTree;
        protected int depth = 0;

        public BuildingEvaluationListener() {
            this(t -> true);
        }

        /**
         * Create a building evaluation listener with the given parser rule context predicate.
         *
         * <p>The predicate is used to determine which parser rule context(s) should be considered when
         * building the evaluation result tree.
         *
         * @param predicate
         *     the predicate
         */
        public BuildingEvaluationListener(Predicate<ParserRuleContext> predicate) {
            this.predicate = predicate;
        }

        /**
         * Reset this building evaluation listener
         */
        public void reset() {
            resultTreeNodeStack.clear();
            resultTree = null;
            depth = 0;
        }

        /**
         * Get the evaluation result tree.
         *
         * @return
         *     the evaluation result tree, or null if not exists
         */
        public EvaluationResultTree getEvaluationResultTree() {
            return resultTree;
        }

        /**
         * Get the parser rule context predicate.
         *
         * @return
         *     the parser rule context predicate
         */
        public Predicate<ParserRuleContext> getParserRuleContextPredicate() {
            return predicate;
        }

        @Override
        public void beforeEvaluation(ParserRuleContext parserRuleContext, Collection<FHIRPathNode> context) {
            if (!predicate.test(parserRuleContext)) {
                return;
            }
            MutableNode node = new MutableNode(parserRuleContext, context, depth);
            if (resultTreeNodeStack.isEmpty()) {
                resultTree = new EvaluationResultTree(node);
            } else {
                MutableNode parent = resultTreeNodeStack.peek();
                node.setParent(parent);
                parent.addChild(node);
            }
            resultTreeNodeStack.push(node);
            depth++;
        }

        @Override
        public void afterEvaluation(ParserRuleContext parserRuleContext, Collection<FHIRPathNode> result) {
            if (!predicate.test(parserRuleContext)) {
                return;
            }
            depth--;
            resultTreeNodeStack.pop().setResult(result);
        }

        /**
         * An internal implementation of the Node interface
         */
        private static class MutableNode implements Node {
            private final ParserRuleContext parserRuleContext;
            private final Collection<FHIRPathNode> context;
            private Collection<FHIRPathNode> result;
            private Node parent;
            private List<Node> children = new ArrayList<>();
            private int depth;
            private String text;

            public MutableNode(ParserRuleContext parserRuleContext, Collection<FHIRPathNode> context, int depth) {
                this.parserRuleContext = parserRuleContext;
                this.context = context;
                this.depth = depth;
                text = parserRuleContext.getStart().getInputStream().getText(Interval.of(parserRuleContext.getStart().getStartIndex(), parserRuleContext.getStop().getStopIndex()));
            }

            @Override
            public ParserRuleContext getParserRuleContext() {
                return parserRuleContext;
            }

            @Override
            public Collection<FHIRPathNode> getContext() {
                return context;
            }

            public void setResult(Collection<FHIRPathNode> result) {
                this.result = result;
            }

            @Override
            public Collection<FHIRPathNode> getResult() {
                return result;
            }

            public void setParent(Node parent) {
                this.parent = parent;
            }

            @Override
            public Node getParent() {
                return parent;
            }

            public void addChild(Node child) {
                children.add(child);
            }

            @Override
            public List<Node> getChildren() {
                return children;
            }

            @Override
            public boolean isRoot() {
                return parent == null;
            }

            @Override
            public boolean isInternal() {
                return !isRoot() && !isLeaf();
            }

            @Override
            public boolean isLeaf() {
                return children.isEmpty();
            }

            @Override
            public int getDepth() {
                return depth;
            }

            @Override
            public String getText() {
                return text;
            }

            @Override
            public void accept(Visitor visitor) {
                visitor.visit(this);
            }

            @Override
            public String toString() {
                return parserRuleContext.getClass().getSimpleName() + ": " + text + ", result: " + result;
            }
        }
    }
}
