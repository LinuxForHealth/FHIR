/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.dryrun;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

/**
 * Dry Run Container is used to store the ordered SQL statements.
 */
public class DryRunContainer {
    private static final boolean SELECT = true;

    private static DryRunContainer container = new DryRunContainer();
    private Map<String, List<Object>> statements = new LinkedHashMap<>();

    private Boolean dryRun = Boolean.FALSE;

    public static DryRunContainer getSingleInstance() {
        return container;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    public Boolean isDryRun() {
        return dryRun;
    }

    public void print(String separator, PrintStream out) {
        // The Statements from the Simulation are: 
        out.println("-- [DRY RUN] Output Start");
        for (Entry<String, List<Object>> entry : statements.entrySet()) {
            if (entry.getKey().isEmpty()) {
                out.println();
            } else if (entry.getKey().startsWith("--")) {
                out.println(entry.getKey());
            } else if (!entry.getKey().startsWith("SELECT") || SELECT) {
                if (entry.getValue() != null) {
                    out.print("-- [");
                    StringJoiner args = new StringJoiner(",");
                    for (Object o : entry.getValue()) {
                        args.add(o.toString());
                    }
                    out.print(args.toString());
                    out.println("]");
                }
                out.println(entry.getKey() + separator);
            }
        }
        out.println();
        out.println("-- [DRY RUN] Output Done");
    }

    /**
     * Adds the statement and parameters to the list of statements.
     * 
     * @param stmt
     * @param objs
     */
    public void add(String stmt, List<Object> objs) {
        statements.put(stmt, objs);
    }

    public void addComment(String string) {
        statements.put("", null);
        statements.put("-- " + string, null);
    }
}