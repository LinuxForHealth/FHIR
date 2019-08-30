/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.util;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.type.DateTime;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.model.type.Meta;
import com.ibm.watson.health.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watson.health.fhir.persistence.exception.FHIRPersistenceException;

public class FHIRPersistenceUtil {
    private static final Logger log = Logger.getLogger(FHIRPersistenceUtil.class.getName());
    private final static double EARTH_RADIUS_KILOMETERS = 6371.0; // earth radius in kilometers

    // Parse history parameters into a FHIRHistoryContext
    public static FHIRHistoryContext parseHistoryParameters(Map<String, List<String>> queryParameters) throws FHIRPersistenceException {
        log.entering(FHIRPersistenceUtil.class.getName(), "parseHistoryParameters");
        FHIRHistoryContext context = FHIRPersistenceContextFactory.createHistoryContext();
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
                    DateTime dt = DateTime.of(first);
                    if (!dt.isPartial()) {
                        Instant since = Instant.of(ZonedDateTime.from(dt.getValue()));
                        context.setSince(since);
                    }
                    else {
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
            log.exiting(FHIRPersistenceUtil.class.getName(), "parseHistoryParameters");
        }
        return context;
    }

    /**
     * Method to build a bounding box given a latitude, longitude and distance
     * 
     * @param latitude
     * @param longitude
     * @param distance
     * @param unit
     * @return
     */
    public static BoundingBox createBoundingBox(double latitude, double longitude, double distance, String unit) {
        log.entering(FHIRPersistenceUtil.class.getName(), "createBoundingBox");
        if (log.isLoggable(Level.FINE)) {
            log.fine("distance   :" + distance + ":unit:" + unit + ":latitude :" + latitude + ":longitude:" + longitude);
        }
        try {
            if (!"km".equalsIgnoreCase(unit) && !"kilometers".equalsIgnoreCase(unit) && !"mi".equalsIgnoreCase(unit) && !"miles".equalsIgnoreCase(unit)) {
                throw new IllegalArgumentException("Invalid unit: '" + unit + "'. Must be one of: ['km', 'kilometers', 'mi', 'miles']");
            }
            
            if ("mi".equalsIgnoreCase(unit) || "miles".equalsIgnoreCase(unit)) {
                distance = convertMilesToKilometers(distance);
                unit = "kilometers";
            }
            
            if (log.isLoggable(Level.FINE)) {
                log.fine("distance: " + distance + ", unit: " + unit);
            }

            // build bounding box points
            double minLatitude = latitude - (distance / EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI);
            double maxLatitude = latitude + (distance / EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI);
            double minLongitude = longitude - ((distance / EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI)) / Math.cos(latitude * (180.0 / Math.PI));
            double maxLongitude = longitude + ((distance / EARTH_RADIUS_KILOMETERS) * (180.0 / Math.PI)) / Math.cos(latitude * (180.0 / Math.PI));
            
            BoundingBox boundingBox = new BoundingBox(minLatitude, maxLatitude, minLongitude, maxLongitude);
            
            if (log.isLoggable(Level.FINE)) {
                log.fine("bounding box: " + boundingBox);
            }
            
            return boundingBox;
        } finally {
            log.exiting(FHIRPersistenceUtil.class.getName(), "createBoundingBox");
        }
    }

    /**
     * Method to convert miles into kilometers
     * 
     * @param miles distance in miles
     * @return distance in kilometers
     */
    public static double convertMilesToKilometers(double miles) {
        log.entering(FHIRPersistenceUtil.class.getName(), "convertMilesToKilometers");
        try {
            return miles * 1.609344;
        } finally {
            log.exiting(FHIRPersistenceUtil.class.getName(), "convertMilesToKilometers");
        }
    }
    
    /**
     * Create a minimal deleted resource marker from the given resource
     * 
     * @param deletedResource
     * @return deletedResourceMarker
     */
    public static Resource createDeletedResourceMarker(Resource deletedResource) {
        try {
            // Build a fresh meta with only versionid/lastupdated defined
            Meta meta = Meta.builder()
                    .versionId(deletedResource.getMeta().getVersionId())
                    .lastUpdated(deletedResource.getMeta().getLastUpdated())
                    .build();

            // TODO this will clone the entire resource, but we only want the minimal parameters
            Resource deletedResourceMarker = deletedResource.toBuilder()
                    .id(deletedResource.getId())
                    .meta(meta)
                    .build();

            return deletedResourceMarker;
        } catch (Exception e) {
            throw new IllegalStateException("Error while creating deletion marker for resource of type "
                    + deletedResource.getClass().getSimpleName());
        }
    }
}
