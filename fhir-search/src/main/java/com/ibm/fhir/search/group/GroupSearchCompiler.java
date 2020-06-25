/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.group;

import java.util.List;
import java.util.Map;

import com.ibm.fhir.model.resource.Group;

/**
 * The Dynamic Group to Search Query Parameters compiler
 * @implNote This feature is experimental.
 */
public interface GroupSearchCompiler {

    /**
     * translates the given group to a search query
     * @param group
     * @return
     * @throws GroupSearchCompilerException
     *  <li>not enabled as a Group
     *  <li>not a descriptive Group
     */
    public Map<String, List<String>> groupToSearch(Group group) throws GroupSearchCompilerException;
}