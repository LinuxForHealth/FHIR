/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.model;

import java.util.Set;

import com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter;

/**
 * Adds a session variable to the database
 * @author rarnold
 *
 */
public class SessionVariableDef extends BaseObject {
    
    public SessionVariableDef(String schemaName, String variableName, int version) {
        super(schemaName, variableName, DatabaseObjectType.VARIABLE, version);
    }
    

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.model.IDatabaseObject#apply(com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter)
     */
    @Override
    public void apply(IDatabaseAdapter target) {
        target.createIntVariable(getSchemaName(), getObjectName());
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.model.IDatabaseObject#drop(com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter)
     */
    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropVariable(getSchemaName(), getObjectName());
    }
    
       @Override
       protected void grantGroupPrivileges(IDatabaseAdapter target, Set<Privilege> group, String toUser) {
            target.grantVariablePrivileges(getSchemaName(), getObjectName(), group, toUser);
        }

}
