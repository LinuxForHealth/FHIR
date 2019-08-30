/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.database.utils.query;

/**
 * @author rarnold
 *
 */
public class FromSubQueryAdapter extends SelectAdapter {
    
    // The select statement we'll be part of when complete
    private final Select parentSelect;
    
    // The From clause we are part of because we need to unwind
    private final FromAdapter from;
    

    /**
     * @param alias
     */
    protected FromSubQueryAdapter(Select parentSelect, FromAdapter from) {
        // Start with a fresh Select statement representing this sub-query
        super(new Select());
        this.parentSelect = parentSelect;
        this.from = from;
    }

    /**
     * End construction of this sub-query by returning out parent {@link FromAdapter}
     * thus allowing a caller to continue with their fluent building of the
     * select statement
     * @return our parent {@link FromAdapter}
     */
    public FromAdapter subEnd(String alias) {
        this.parentSelect.addFrom(getSelect(), new Alias(alias));
        return this.from;
    }
}
