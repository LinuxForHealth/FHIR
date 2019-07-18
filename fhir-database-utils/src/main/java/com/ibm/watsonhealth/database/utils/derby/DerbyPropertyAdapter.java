/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.derby;

import java.util.Properties;

import com.ibm.watsonhealth.database.utils.common.JdbcPropertyAdapter;

/**
 * @author rarnold
 *
 */
public class DerbyPropertyAdapter extends JdbcPropertyAdapter {

    /**
     * @param properties
     */
    public DerbyPropertyAdapter(Properties properties) {
        super(properties);
    }

}
