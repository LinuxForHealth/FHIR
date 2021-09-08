/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.query.expression.BindMarkerNodeVisitor;

/**
 * Visitor implementation to bind (set) values for a PreparedStatement.
 * <br>
 * The bindXX methods translate any SQLException to a DataAccessException
 * which is unchecked - this avoids polluting callers with SQLException
 * for looser coupling.
 */
public class BindVisitor implements BindMarkerNodeVisitor {
    private static final Logger logger = Logger.getLogger(BindVisitor.class.getName());

    // the statement to bind values to
    private final PreparedStatement statement;

    // for translating SQLException to DataAccessException
    private final IDatabaseTranslator translator;

    // Keep track of the parameter index
    private int parameterIndex = 1;

    // Do not make static - Calendar is not thread-safe
    private final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    
    /**
     * Public constructor
     * @param ps
     * @param translator
     */
    public BindVisitor(PreparedStatement ps, IDatabaseTranslator translator) {
        this.statement = ps;
        this.translator = translator;
    }

    @Override
    public void bindString(String value) {
        int idx = parameterIndex++;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("bindString(" + idx + ", '" + value + "')");
        }

        try {
            if (value != null) {
                statement.setString(idx, value);
            } else {
                statement.setNull(idx, Types.VARCHAR);
            }
        } catch (SQLException x) {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "value='" + value + "'", x);
            }
            throw translator.translate(x);
        }
    }

    @Override
    public void bindLong(Long value) {
        int idx = parameterIndex++;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("bindLong(" + idx + ", " + value + ")");
        }

        try {
            if (value != null) {
                statement.setLong(idx, value);
            } else {
                statement.setNull(idx, Types.BIGINT);
            }
        } catch (SQLException x) {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "value=" + value, x);
            }
            throw translator.translate(x);
        }
    }

    @Override
    public void bindInt(Integer value) {
        int idx = parameterIndex++;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("bindInt(" + idx + ", " + value + ")");
        }

        try {
            if (value != null) {
                statement.setInt(idx, value);
            } else {
                statement.setNull(idx, Types.INTEGER);
            }
        } catch (SQLException x) {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "value=" + value, x);
            }
            throw translator.translate(x);
        }
    }

    @Override
    public void bindInstant(Instant value) {
        int idx = parameterIndex++;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("bindTimestamp(" + idx + ", " + value + ", UTC)");
        }

        try {
            if (value != null) {
                statement.setTimestamp(idx, Timestamp.from(value), UTC);
            } else {
                statement.setNull(idx, Types.TIMESTAMP);
            }
        } catch (SQLException x) {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "value=" + value, x);
            }
            throw translator.translate(x);
        }
    }

    @Override
    public void bindDouble(Double value) {
        int idx = parameterIndex++;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("bindDouble(" + idx + ", " + value + ")");
        }

        try {
            if (value != null) {
                statement.setDouble(idx, value);
            } else {
                statement.setNull(idx, Types.DOUBLE);
            }
        } catch (SQLException x) {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "value=" + value, x);
            }
            throw translator.translate(x);
        }
    }

    @Override
    public void bindBigDecimal(BigDecimal value) {
        int idx = parameterIndex++;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("bindBigDecimal(" + idx + ", " + value + ")");
        }

        try {
            if (value != null) {
                statement.setBigDecimal(idx, value);
            } else {
                statement.setNull(idx, Types.DECIMAL);
            }
        } catch (SQLException x) {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "value=" + value, x);
            }
            throw translator.translate(x);
        }
    }

}