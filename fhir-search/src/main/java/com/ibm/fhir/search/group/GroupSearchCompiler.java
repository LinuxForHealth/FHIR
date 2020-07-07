/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Group;

/**
 * The Dynamic Group to Search Query Parameters compiler
 * @implNote This feature is experimental.
 */
public interface GroupSearchCompiler {

    /**
     * translates the given group to a search query
     * @param group the dynamic group
     * @param target the resource type for the search query
     * @return
     * @throws GroupSearchCompilerException
     *  <li>not enabled as a Group
     *  <li>not a descriptive Group
     */
    public MultivaluedMap<String, String> groupToSearch(Group group, String target) throws GroupSearchCompilerException;
}