/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.tool.helpers.dynamic;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.type.Meta;

/**
 * This class is a common base for each Dynamic Group used as an example
 * in Bulk Data export.
 */
public abstract class GroupExample {
    public Group.Builder base(Boolean active) {
        Meta meta = Meta.builder().build();
        return Group.builder().meta(meta);
    }

    /**
     * gets the filename that is going to be generated
     * @return
     */
    public abstract String filename();

    /**
     * gets the group
     * @return
     */
    public abstract Group group();

    /**
     * gets the sample data
     * @return
     */
    public abstract Bundle sampleData();
}