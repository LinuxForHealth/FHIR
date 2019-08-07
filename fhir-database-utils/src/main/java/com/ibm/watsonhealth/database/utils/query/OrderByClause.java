/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rarnold
 *
 */
public class OrderByClause {

    private final List<String> items = new ArrayList<>();

    /**
     * @param expressions
     */
    public void add(String... expressions) {
        items.addAll(Arrays.asList(expressions));
    }

    @Override
    public String toString() {
        return items.stream().collect(Collectors.joining(", "));
    }

}
