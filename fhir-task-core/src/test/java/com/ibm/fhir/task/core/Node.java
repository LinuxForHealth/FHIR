/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.task.core;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskGroup;


/**
 * Test class representing a dependency tree

 *
 */
public class Node {
	// This node depends on the nodes in this set
	private final Set<Node> dependencies = new HashSet<>();

	// The name of this node
	private final String name;

	/**
	 * Public constructor
	 * @param name
	 */
	public Node(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			throw new IllegalArgumentException("Object other is null");
		}
		
		if (other instanceof Node) {
			Node that = (Node)other;
			return this.name.equals(that.name);
		}
		else {
			throw new IllegalArgumentException("Not a Node: " + other);
		}
	}

	/**
	 * Get the name of this node
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		// Just print the immediate dependencies, do no recurse
		String children = dependencies.stream().map(n -> n.getName()).collect(Collectors.joining(","));
		return this.name + ": " + children;
	}

	/**
	 * add the given node as a dependency for this node
	 * @param n
	 */
	public void addDependency(Node n) {
		this.dependencies.add(n);
	}

	/**
	 * Add each of the dependencies in the given array as a dependency for this {@link Node}
	 * @param deps
	 */
	public void addDependencies(Node... deps) {
		for (Node n: deps) {
			addDependency(n);
		}
	}

	/**
	 * Get the set of dependencies of this node
	 * @return
	 */
	public Set<Node> getDependencies() {
		return Collections.unmodifiableSet(this.dependencies);
	}

	/**
	 * Collect tasks for this model
	 * @param collector
	 * @return
	 */
	public ITaskGroup collect(ITaskCollector collector) {
		// Collect any dependencies we have. These will need to be processed
		// first, before us
		List<ITaskGroup> children = null;
		if (this.dependencies.size() > 0) {
			children = new ArrayList<>(this.dependencies.size());
		
			for (Node n: this.dependencies) {
				ITaskGroup child = n.collect(collector);
				children.add(child);
			}
		}
		
		return collector.makeTaskGroup(this.name, () -> processNode(), children);
	}
	
	/**
	 * The function called by the task manager when we are scheduled to run
	 * (probably in a thread-pool).
	 */
	public void processNode() {
		System.out.println("[" + Thread.currentThread().getId() + "] Processing node: " + this.name);
	}
}
