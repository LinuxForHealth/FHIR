/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Abstract model of a FHIR search query we intend to build.
 * The purpose of this class and its sub-classes is to drive
 * calls to a {@link SearchQueryVisitor} implementation to render
 * a runnable SQL statement.
 */
public abstract class SearchQuery {
    private static final String CLASSNAME = SearchQuery.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);
    private final String rootResourceType;
    private final List<SearchParam> searchParams = new ArrayList<>();

    // A list of non-parameter related extensions (e.g. location processing)
    private final List<SearchExtension> extensions = new ArrayList<>();

    /**
     * Public constructor
     * @param rootResourceType
     */
    public SearchQuery(String rootResourceType) {
        this.rootResourceType = rootResourceType;
    }

    /**
     * Getter for the root resource type for this search query
     * @return
     */
    public String getRootResourceType() {
        return this.rootResourceType;
    }

    /**
     * Add the search parameter to the model
     * @param sp
     */
    public void add(SearchParam sp) {
        this.searchParams.add(sp);
    }

    /**
     * Add the search extension to the model
     * @param ext
     */
    public void add(SearchExtension ext) {
        this.extensions.add(ext);
    }

    /**
     * Get the root query and attach the parameter filters to it
     * @param <T>
     * @param visitor
     * @return
     */
    public <T> T visit(SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        logger.entering(CLASSNAME, "visit");
        T query = getRoot(visitor);

        // Pre-process any extensions before we process the parameters
        visitExtensions(query, visitor);

        T parameterBase = visitor.getParameterBaseQuery(query);
        visitSearchParams(parameterBase, visitor);
        logger.exiting(CLASSNAME, "visit");
        return query;
    }

    /**
     * Visit each of the extensions configured for this query
     * @param <T>
     * @param query
     * @param visitor
     * @throws FHIRPersistenceException
     */
    public <T> void visitExtensions(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        for (SearchExtension ext: this.extensions) {
            ext.visit(query, visitor);
        }
    }

    /**
     * Process the logical query definition through the visitor
     * @param <T>
     * @param visitor
     * @return
     */
    public abstract <T> T getRoot(SearchQueryVisitor<T> visitor);

    /**
     * Visit each of the search parameters
     * @param <T>
     * @param query
     * @param visitor
     * @throws FHIRPersistenceException
     */
    protected <T> void visitSearchParams(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        for (SearchParam sp: this.searchParams) {
            sp.visit(query, visitor);
        }
    }
}