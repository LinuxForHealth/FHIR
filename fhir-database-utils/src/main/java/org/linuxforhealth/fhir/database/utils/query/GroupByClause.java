/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Refers to the GroupByClause behavior
 */
public class GroupByClause {
    private final List<String> items = new ArrayList<>();

    /**
     * @param expressions
     */
    public void add(String... expressions) {
        items.addAll(Arrays.asList(expressions));
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        } else {
            return "GROUP BY " + items.stream().collect(Collectors.joining(", "));
        }
    }

    /**
     * check if the list of items is empty
     * @return
     */
    public boolean isEmpty() {
        return this.items.isEmpty();
    }
}