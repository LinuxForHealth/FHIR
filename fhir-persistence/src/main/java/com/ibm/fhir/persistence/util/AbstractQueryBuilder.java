/*
 * (C) Copyright IBM Corp. 2016,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.location.NearLocationHandler;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.util.LocationUtil;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * This class defines a reusable method structure and common functionality for a
 * FHIR persistence query builder.
 */
@Deprecated
public abstract class AbstractQueryBuilder<T1> implements QueryBuilder<T1> {

    private static final Logger log = java.util.logging.Logger.getLogger(AbstractQueryBuilder.class.getName());
    private static final String CLASSNAME = AbstractQueryBuilder.class.getName();

    /**
     * Examines the passed ParamaterValue, and checks to see if the value is a URL.
     * If it is, the {ResourceType/id} part of the URL path is extracted and returned.
     * For example:
     * If the input value is http://localhost:8080/fhir/Patient/123,
     * then Patient/123 is returned.
     *
     * @param parmValue A valid String parameter value that may or may not contain a
     *                  URL.
     * @return String - The last 2 segments of the URL path is returned if the
     *         passed parmValue is a URL;
     *         otherwise, null is returned.
     */
    public static String extractReferenceFromUrl(String parmValue) {
        final String METHODNAME = "extractReferenceFromUrl";
        log.entering(CLASSNAME, METHODNAME, parmValue);

        String referenceValue = null;
        URL parmValueUrl;
        String urlString;
        String[] urlPath;
        try {
            parmValueUrl   = new URL(parmValue);
            urlString      = parmValueUrl.getPath();
            urlPath        = urlString.split("/");
            referenceValue = urlPath[urlPath.length - 2] + "/" + urlPath[urlPath.length - 1];
        } catch (MalformedURLException e) {
        }

        log.exiting(CLASSNAME, METHODNAME);
        return referenceValue;
    }

    public static boolean isAbsoluteURL(String s) {
        try {
            new URL(s);
            return true;
        } catch (MalformedURLException e) {
        }
        return false;
    }

    /**
     * Builds a query segment for the passed query parameter.
     *
     * @param resourceType - A valid FHIR Resource type
     * @param queryParm - A Parameter object describing the name, value and type of search parm.
     * @return T1 - An object representing the selector query segment for the passed search parm.
     * @throws Exception
     */
    protected T1 buildQueryParm(Class<?> resourceType, QueryParameter queryParm)
            throws Exception {
        final String METHODNAME = "buildQueryParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        T1 databaseQueryParm = null;
        Type type;

        try {
            // NOTE: The special logic needed to process NEAR query parameter for the Location resource type is
            // found in method processLocationPosition(). This method will not handle those.
            if (!LocationUtil.isLocation(resourceType, queryParm)) {
                type = queryParm.getType();
                switch (type) {
                case STRING:
                    databaseQueryParm = this.processStringParm(queryParm);
                    break;
                case REFERENCE:
                    if (queryParm.isChained()) {
                        databaseQueryParm = this.processChainedReferenceParm(queryParm);
                    } else if (queryParm.isInclusionCriteria()) {
                        databaseQueryParm = this.processInclusionCriteria(queryParm);
                    } else {
                        databaseQueryParm = this.processReferenceParm(resourceType, queryParm);
                    }
                    break;
                case DATE:
                    databaseQueryParm = this.processDateParm(resourceType, queryParm);
                    break;
                case TOKEN:
                    databaseQueryParm = this.processTokenParm(resourceType, queryParm);
                    break;
                case NUMBER:
                    databaseQueryParm = this.processNumberParm(resourceType, queryParm);
                    break;
                case QUANTITY:
                    databaseQueryParm = this.processQuantityParm(resourceType, queryParm);
                    break;
                case URI:
                    databaseQueryParm = this.processUriParm(queryParm);
                    break;
                case COMPOSITE:
                    databaseQueryParm = this.processCompositeParm(resourceType, queryParm);
                    break;

                default:
                    throw new FHIRPersistenceNotSupportedException("Parm type not yet supported: " + type.value());
                }
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME, new Object[] { databaseQueryParm });
        }
        return databaseQueryParm;
    }

    /**
     * Creates a query segment for a String type parameter.
     *
     * @param queryParm - The query parameter.
     * @return T1 - An object containing query segment.
     */
    protected abstract T1 processStringParm(QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Creates a query segment for a Reference type parameter.
     *
     * @param resourceType - The resource type.
     * @param queryParm - The query parameter.
     * @return T1 - An object containing query segment.
     * @throws Exception
     */
    protected abstract T1 processReferenceParm(Class<?> resourceType, QueryParameter queryParm) throws Exception;

    /**
     * Contains special logic for handling chained reference search parameters.
     *
     * @see https://www.hl7.org/fhir/search.html#chaining
     * @param queryParm - The query parameter.
     * @return T1 - An object containing a query segment.
     * @throws FHIRPersistenceException
     */
    protected abstract T1 processChainedReferenceParm(QueryParameter queryParm) throws Exception;

    /**
     * Contains special logic for handling reverse chained reference search parameters.
     *
     * @see https://www.hl7.org/fhir/search.html#has
     * @param resourceType - The resource type.
     * @param queryParm - The query parameter.
     * @return T1 - An object containing a query segment.
     * @throws FHIRPersistenceException
     */
    protected abstract T1 processReverseChainedReferenceParm(Class<?> resourceType, QueryParameter queryParm) throws Exception;

    /**
     * Contains special logic for handling Compartment based searches.
     *
     * @see https://www.hl7.org/fhir/compartments.html
     * @param queryParm - The query parameter.
     * @return T1 - An object containing a query segment.
     * @throws FHIRPersistenceException
     */
    protected abstract T1 processInclusionCriteria(QueryParameter queryParm) throws Exception;

    /**
     * Creates a query segment for a Date type parameter.
     *
     * @param resourceType - The resource type.
     * @param queryParm - The query parameter.
     * @return T1 - An object containing query segment.
     * @throws Exception
     */
    protected abstract T1 processDateParm(Class<?> resourceType, QueryParameter queryParm) throws Exception;

    /**
     * Creates a query segment for a Token type parameter.
     *
     * @param resourceType - The resource type.
     * @param queryParm - The query parameter.
     * @return T1 - An object containing query segment.
     */
    protected abstract T1 processTokenParm(Class<?> resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Creates a query segment for a Number type parameter.
     *
     * @param resourceType - The resource type.
     * @param queryParm - The query parameter.
     * @return T1 - An object containing query segment.
     * @throws FHIRPersistenceException
     */
    protected abstract T1 processNumberParm(Class<?> resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Creates a query segment for a Quantity type parameter.
     *
     * @param resourceType - The resource type.
     * @param queryParm - The query parameter.
     * @return T1 - An object containing query segment.
     * @throws Exception
     */
    protected abstract T1 processQuantityParm(Class<?> resourceType, QueryParameter queryParm) throws Exception;

    /**
     * Creates a query segment for a URI type parameter.
     *
     *
     * @param queryParm - The query parameter.
     * @return T1 - An object containing query segment.
     * @throws FHIRPersistenceException
     */
    protected abstract T1 processUriParm(QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * Creates a query segment for a Composite type parameter.
     *
     * @param resourceType - The resource type.
     * @param queryParm - The query parameter
     * @return T1 - An object containing query segment
     * @throws FHIRPersistenceException
     */
    protected abstract T1 processCompositeParm(Class<?> resourceType, QueryParameter queryParm) throws FHIRPersistenceException;

    /**
     * This method executes special logic for a Token type query that maps to a
     * LocationPosition data type.
     *
     * @param queryParameters The entire collection of query input parameters
     * @param paramTableAlias the alias name of the parameter table
     * @return T1 - A query segment related to a LocationPosition
     * @throws FHIRPersistenceException
     */
    protected T1 processLocationPosition(List<QueryParameter> queryParameters, String paramTableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processLocationPosition";
        log.entering(CLASSNAME, METHODNAME);

        NearLocationHandler handler = new NearLocationHandler();
        List<Bounding> boundingAreas;
        try {
            boundingAreas = handler.generateLocationPositionsFromParameters(queryParameters);
        } catch (FHIRSearchException e) {
            throw new FHIRPersistenceException("input parameter is invalid bounding area, bad prefix, or bad units", e);
        }

        T1 parmRoot = null;
        if (!boundingAreas.isEmpty()) {
            parmRoot = this.buildLocationQuerySegment(NearLocationHandler.NEAR, boundingAreas, paramTableAlias);
        }

        log.exiting(CLASSNAME, METHODNAME);
        return parmRoot;
    }

    /**
     * Builds a query segment for the passed parameter name using the geospatial
     * data contained with the passed BoundingBox
     *
     * @param parmName    - The name of the search parameter
     * @param boundingAreas - Container for the geospatial data needed to construct
     *                    the query segment.
     * @param paramTableAlias the alias name of the parameter table
     * @return T1 - The query segment necessary for searching locations that are
     *         inside the bounding box.
     */
    protected abstract T1 buildLocationQuerySegment(String parmName, List<Bounding> boundingAreas, String paramTableAlias)
            throws FHIRPersistenceException;

}
