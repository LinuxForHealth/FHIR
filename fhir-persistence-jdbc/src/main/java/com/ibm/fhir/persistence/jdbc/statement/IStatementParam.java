/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A parameter (bind value) to be set on the statement
 */
public interface IStatementParam {

    /**
     * Set this parameter as a value on the given {@link PreparedStatement}
     * @param ps
     * @param posn
     * @throws SQLException
     */
    public void setValue(PreparedStatement ps, int posn) throws SQLException;
}
