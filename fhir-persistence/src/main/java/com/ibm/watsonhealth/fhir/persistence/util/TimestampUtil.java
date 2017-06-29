/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility for parsing our UTC timestamp format.
 * @author rarnold
 *
 */
public class TimestampUtil {
	
	// format for converting to/from our tstamp string in UTC
	private final ZoneId utc = ZoneId.of("UTC");
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(utc);

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
		Instant instant = Instant.parse(str);
		ZonedDateTime dt = instant.atZone(utc);
		return Date.from(dt.toInstant());
	}

	/**
	 * Format the tstamp into our ISO8601 standard
	 * @param tstamp
	 * @return
	 */
	public String format(Date tstamp) {
		Instant inst = tstamp.toInstant();
		return formatter.format(inst);
	}
}
