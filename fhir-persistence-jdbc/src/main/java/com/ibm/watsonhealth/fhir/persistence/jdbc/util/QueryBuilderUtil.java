/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.TemporalAccessor;

import com.ibm.watsonhealth.fhir.model.type.DateTime;

/**
 * @author rarnold
 *
 */
public class QueryBuilderUtil {

    /**
     * Compute the end time to use as a range filter based on the "partialness"
     * of the given dateTime field.
     * @param dateTime
     * @return
     */
    public static java.time.Instant getEnd(DateTime dateTime) {
        java.time.Instant result;

        TemporalAccessor ta = dateTime.getValue();
        if (ta instanceof Year) {
            result = java.time.Instant.from(((Year) ta).plusYears(1));
        }
        else if (ta instanceof YearMonth) {
            result = java.time.Instant.from(((YearMonth) ta).plusMonths(1));
        }
        else if (ta instanceof LocalDate) {
            result = java.time.Instant.from(((LocalDate) ta).plusDays(1));
        }
        else {
            throw new IllegalStateException("DateTime must be partial");
        }

        return result;
    }

}
