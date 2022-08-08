/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility for parsing our UTC timestamp format.

 *
 */
public class TimestampUtil {
    
    // format for converting to/from our tstamp string in UTC
    private final ZoneId utc = ZoneId.of("UTC");
    private final DateTimeFormatter formatterPartial = DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00.000Z'").withZone(utc);
    private final DateTimeFormatter formatterFullTimestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(utc);

    /**
     * Create a new utility instance, so each thread can get its own
     * @return
     */
    public static TimestampUtil create() {
        return new TimestampUtil();
    }

    /**
     * Parse the date string
     * @param str
     * @return the parsed date string
     * @throws ReplicatorException for an error
     */
    public Date getTimestamp(String str) {
        try {
            LocalDate ld = LocalDate.parse(str);
            ZonedDateTime dt = ld.atStartOfDay(utc);
            return Date.from(dt.toInstant());
        } catch (Exception e1) {
            try {
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(str);
                return Date.from(offsetDateTime.toInstant());
            } catch (Exception e2) {
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(addUTCTimezone(str));
                return Date.from(offsetDateTime.toInstant());
            }
        }
    }

    private CharSequence addUTCTimezone(String date) {
        return date + "Z";
    }

    /**
     * Format the tstamp into our ISO8601 standard
     * @param tstamp
     * @return
     */
    public String format(Date tstamp) {
        Instant inst = tstamp.toInstant();
        try {
            return formatterFullTimestamp.format(inst);
        } catch (Exception e) {
            return formatterPartial.format(inst);
        }
    }
}
