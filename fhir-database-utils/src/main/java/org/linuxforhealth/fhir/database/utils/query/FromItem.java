/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

import org.linuxforhealth.fhir.database.utils.query.expression.StatementRenderer;

/**
 * An element in the FROM clause
 *   - TableRowSource:
 *       FROM xx_LOGICAL_RESOURCES AS LR0
 *
 *   - SelectRowSource:
 *       FROM (SELECT LR.LOGICAL_ID FROM ...) AS SUB0
 */
public class FromItem {

    // The source we are joining to
    private final RowSource rowSource;

    // Optional alias...but recommended to avoid nasty surprises
    private final Alias alias;

    /**
     * @param rowSource
     * @param alias
     */
    protected FromItem(RowSource rowSource, Alias alias) {
        this.rowSource = rowSource;
        this.alias = alias;
    }

    protected FromItem(RowSource rowSource) {
        this.rowSource = rowSource;
        this.alias = null;
    }

    /**
     * Get the alias associated with this item in the from clause.
     * @return the alias for this item. Can be null
     */
    public Alias getAlias() {
        // can be null
        return this.alias;
    }

    /**
     * Is this an ANSI-style join?
     * @return
     */
    public boolean isAnsiJoin() {
        return false;
    }

    @Override
    public String toString() {
        return toPrettyString(false);
    }

    /**
     * Render a string version of the item (for use when debugging)
     * @param pretty
     * @return
     */
    public String toPrettyString(boolean pretty) {
        StringBuilder result = new StringBuilder();
        result.append(rowSource.toPrettyString(pretty));

        // the alias can be null - it's not always needed, but recommended
        Alias aliasName = getAlias();
        if (aliasName != null) {
            result.append(" AS ").append(aliasName.toString());
        }

        return result.toString();
    }

    /**
     * Render this item using the given renderer
     * @param <T>
     * @param renderer
     * @return
     */
    public <T> T render(StatementRenderer<T> renderer) {
        T subValue = this.rowSource.render(renderer);
        T aliasValue = null;

        // the alias can be null - it's not always needed, but recommended
        Alias aliasName = getAlias();
        if (aliasName != null) {
            aliasValue = aliasName.render(renderer);
        }

        // aliasValue may be null
        return renderer.fromItem(subValue, aliasValue);
    }
}