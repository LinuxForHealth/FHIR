/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.task.core;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Test our test class

 */
public class TestNode {
	private static final String N1 = "n1";
	private static final String N2 = "n2";
	
	@Test
	public void testName() {
		Node n1 = new Node(N1);
		assertEquals(n1.getName(), N1);
	}
	
	@Test
	public void testDependency() {
	
		// Make n2 a dependency for n1
		Node n1 = new Node(N1);
		Node n2 = new Node(N2);
		n1.addDependency(n2);
		
		assertEquals(1, n1.getDependencies().size());
	}

}
