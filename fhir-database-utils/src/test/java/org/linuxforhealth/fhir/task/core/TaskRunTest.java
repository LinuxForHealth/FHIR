/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.task.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.task.api.ITaskCollector;
import org.linuxforhealth.fhir.task.core.service.TaskService;

/**
 * Unit tests for the core task executor
 */
public class TaskRunTest {
	private final static int POOL_SIZE = 10;
	private final static String nodeNames[] = {
			"n0", "n1", "n2", "n3", "n4", "n5", "n6", "n7"
	};

	@Test
	public void test1() {
		ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
		
		// Build our simple dependency tree:
		List<Node> nodes = new ArrayList<Node>();
		for (String name: nodeNames) {
			nodes.add(new Node(name));
		}
		
		// Just to make it a little easier to encode the dependencies. Imagine
		// these nodes are create table statements, with the dependencies
		// being FK relationships to other tables. When processing in
		// parallel, we need to make sure that all the dependent targets are
		// processed first before winding back up the tree.
		Node n0 = nodes.get(0);
		Node n1 = nodes.get(1);
		Node n2 = nodes.get(2);
		Node n3 = nodes.get(3);
		Node n4 = nodes.get(4);
		Node n5 = nodes.get(5);
		Node n6 = nodes.get(6);
		Node n7 = nodes.get(7);
		
		// leaf-nodes are n4, n5, n6, n7
		n0.addDependencies(n1, n2, n7);
		n1.addDependencies(n3, n4);
		n2.addDependencies(n1, n5, n6);
		n3.addDependencies(n4);
		
		for (Node n: nodes) {
			System.out.println(n.toString());
		}
		
		// A node can be submitted to the thread pool if it has node 
		// dependencies, or if all of its dependencies have been processed.
		// It's a simple visitor pattern to traverse the tree and collect
		// all the task work.
		TaskService svc = new TaskService();
		ITaskCollector tc = svc.makeTaskCollector(pool);

		for (Node n: nodes) {
			n.collect(tc);
		}
		
		tc.startAndWait();
	}
	
}
