/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.model;

import com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter;
import com.ibm.watsonhealth.database.utils.api.ITransactionProvider;
import com.ibm.watsonhealth.database.utils.api.IVersionHistoryService;

/**
 * A NOP (no operation) object which can be used to simplify dependencies
 * by making this object depend on other, then everything else simply
 * depend on this, should that be the sort of behavior you want.
 * @author rarnold
 *
 */
public class NopObject extends BaseObject {

    /**
     * Public constructor
     * @param schemaName
     * @param objectName
     */
    public NopObject(String schemaName, String objectName) {
        // this object doesn't change any schema, but we always want to
        // use it, hence the special version of 0.
        super(schemaName, objectName, DatabaseObjectType.NOP, 0);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.model.IDatabaseObject#apply(com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter)
     */
    @Override
    public void apply(IDatabaseAdapter target) {
        // We're NOP so we do nothing on purpose
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.model.IDatabaseObject#drop(com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter)
     */
    @Override
    public void drop(IDatabaseAdapter target) {
        // We're NOP so we do nothing on purpose
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.model.IDatabaseObject#applyTx(com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter)
     */
    @Override
    public void applyTx(IDatabaseAdapter target, ITransactionProvider tp, IVersionHistoryService vhs) {
        // We're NOP so we do nothing on purpose
    }

}
