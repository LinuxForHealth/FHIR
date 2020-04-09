/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.util.group.examples;

import java.util.List;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.type.Meta;

/**
 * 
 */
public abstract class GroupExample {

    public Group.Builder base(Boolean active) {
        Meta meta = Meta.builder().build();
        return Group.builder().meta(meta);
    }

    /**
     * used to label any output files.
     * 
     * @return
     */
    public abstract String typeName();

    public abstract List<Group> groups();

    public abstract Bundle sampleData();

}
