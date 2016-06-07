/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.context.impl.FHIRHistoryContextImpl;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

public class FHIRPersistenceUtil {
    private static final Logger log = Logger.getLogger(FHIRPersistenceUtil.class.getName());
    
    private static final ObjectFactory objectFactory = new ObjectFactory();
    
    // Parse history parameters into a FHIRHistoryContext
    public static FHIRHistoryContext parseHistoryParameters(Map<String, List<String>> queryParameters) throws FHIRPersistenceException {
        if (log.isLoggable(Level.FINE)) {
            log.entering(FHIRPersistenceUtil.class.getName(), "parseHistoryParameters");
        }
        FHIRHistoryContext context = new FHIRHistoryContextImpl();
        try {
            for (String name : queryParameters.keySet()) {
               List<String> values = queryParameters.get(name);
               String first = values.get(0);
               if ("_page".equals(name)) {
                   int pageNumber = Integer.parseInt(first);
                   context.setPageNumber(pageNumber);
               } else if ("_count".equals(name)) {
                   int pageSize = Integer.parseInt(first);
                   context.setPageSize(pageSize);
               } else if ("_since".equals(name)) {
                   XMLGregorianCalendar calendar = FHIRUtilities.parseDateTime(first, false);
                   if (FHIRUtilities.isDateTime(calendar)) {
                       calendar = calendar.normalize();
                       Instant since = objectFactory.createInstant().withValue(calendar);
                       context.setSince(since);
                   } else {
                       throw new FHIRPersistenceException("The '_since' parameter must be a fully specified ISO 8601 date/time");
                   }
               } else if ("_format".equals(name)) {
                   // safely ignore
                   continue;
               } else {
                   throw new FHIRPersistenceException("Unrecognized history parameter: '" + name + "'");
               }
            }
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIRPersistenceException("Error parsing history parameters", e);
        } finally {
            if (log.isLoggable(Level.FINE)) {
                log.exiting(FHIRPersistenceUtil.class.getName(), "parseHistoryParameters");
            }
        }
        return context;
    }
}
