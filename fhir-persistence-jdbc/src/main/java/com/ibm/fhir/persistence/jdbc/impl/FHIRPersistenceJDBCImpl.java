/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.impl;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_JDBC_ENABLE_CODE_SYSTEMS_CACHE;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_JDBC_ENABLE_PARAMETER_NAMES_CACHE;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_JDBC_ENABLE_RESOURCE_TYPES_CACHE;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_UPDATE_CREATE_ENABLED;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.core.FHIRUtilities;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRJsonParser;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.path.FHIRPathNode;
import com.ibm.fhir.model.path.FHIRPathPrimitiveValue;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.JsonSupport;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.FHIRDbDAOImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ParameterDAOImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import com.ibm.fhir.persistence.jdbc.util.CodeSystemsCache;
import com.ibm.fhir.persistence.jdbc.util.JDBCParameterBuildingVisitor;
import com.ibm.fhir.persistence.jdbc.util.JDBCQueryBuilder;
import com.ibm.fhir.persistence.jdbc.util.ParameterNamesCache;
import com.ibm.fhir.persistence.jdbc.util.QueryBuilderUtil;
import com.ibm.fhir.persistence.jdbc.util.ResourceTypesCache;
import com.ibm.fhir.persistence.jdbc.util.SqlQueryData;
import com.ibm.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.SummaryValueSet;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * The JDBC implementation of the FHIRPersistence interface, 
 * providing implementations for CRUD APIs and search.
 */
public class FHIRPersistenceJDBCImpl implements FHIRPersistence, FHIRPersistenceTransaction {
    private static final String CLASSNAME = FHIRPersistenceJDBCImpl.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);
    
    protected static final String TXN_JNDI_NAME = "java:comp/UserTransaction";
    public static final String TRX_SYNCH_REG_JNDI_NAME = "java:comp/TransactionSynchronizationRegistry";
    
    private FHIRDbDAO baseDao;
    private ResourceDAO resourceDao;
    private ParameterDAO parameterDao;
    private TransactionSynchronizationRegistry trxSynchRegistry;
    
    protected Connection sharedConnection = null;
    protected UserTransaction userTransaction = null;
    protected Boolean updateCreateEnabled = null;
    
    // only used outside a web container
    private Connection managedConnection;

    /**
     * Constructor for use when running as web application in WLP. 
     * @throws Exception 
     */
    public FHIRPersistenceJDBCImpl() throws Exception {
        final String METHODNAME = "FHIRPersistenceJDBCImpl()";
        log.entering(CLASSNAME, METHODNAME);
        
        PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
        this.updateCreateEnabled = fhirConfig.getBooleanProperty(PROPERTY_UPDATE_CREATE_ENABLED, Boolean.TRUE);
        this.userTransaction = retrieveUserTransaction(TXN_JNDI_NAME);
        
        ParameterNamesCache.setEnabled(fhirConfig.getBooleanProperty(PROPERTY_JDBC_ENABLE_PARAMETER_NAMES_CACHE, 
                                       Boolean.TRUE.booleanValue()));
        CodeSystemsCache.setEnabled(fhirConfig.getBooleanProperty(PROPERTY_JDBC_ENABLE_CODE_SYSTEMS_CACHE, 
                                    Boolean.TRUE.booleanValue()));
        ResourceTypesCache.setEnabled(fhirConfig.getBooleanProperty(PROPERTY_JDBC_ENABLE_RESOURCE_TYPES_CACHE, 
                                      Boolean.TRUE.booleanValue()));
        this.resourceDao = new ResourceDAOImpl(this.getTrxSynchRegistry());
        this.parameterDao = new ParameterDAOImpl(this.getTrxSynchRegistry());
        
        log.exiting(CLASSNAME, METHODNAME);
    }
    
    /**
     * Constructor for use when running standalone, outside of any web container.
     * @throws Exception 
     */
    public FHIRPersistenceJDBCImpl(Properties configProps) throws Exception {
        final String METHODNAME = "FHIRPersistenceJDBCImpl(Properties)";
        log.entering(CLASSNAME, METHODNAME);
        
        this.updateCreateEnabled = Boolean.parseBoolean(configProps.getProperty("updateCreateEnabled"));
        
        FHIRDbDAO dao = new FHIRDbDAOImpl(configProps);
        
        this.setBaseDao(dao);
        this.setManagedConnection(this.getBaseDao().getConnection());
        this.resourceDao = new ResourceDAOImpl(this.getManagedConnection());
        this.parameterDao = new ParameterDAOImpl(this.getManagedConnection());
                
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Constructor for use when running standalone, outside of any web container.
     * @throws Exception 
     */
    public FHIRPersistenceJDBCImpl(Properties configProps, IConnectionProvider cp) throws Exception {
        final String METHODNAME = "FHIRPersistenceJDBCImpl(Properties, IConnectionProvider)";
        log.entering(CLASSNAME, METHODNAME);
        
        this.updateCreateEnabled = Boolean.parseBoolean(configProps.getProperty("updateCreateEnabled"));
        
        FHIRDbDAO dao = new FHIRDbDAOImpl(cp.getConnection());
        
        this.setBaseDao(dao);
        this.setManagedConnection(this.getBaseDao().getConnection());
        this.resourceDao = new ResourceDAOImpl(this.getManagedConnection());
        this.parameterDao = new ParameterDAOImpl(this.getManagedConnection());
                
        log.exiting(CLASSNAME, METHODNAME);
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException  {
        final String METHODNAME = "create";
        log.entering(CLASSNAME, METHODNAME);
        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String logicalId;
        
        // We need to update the meta in the resource, so we need a modifiable version
        Resource.Builder resultBuilder = resource.toBuilder();

        
        try {
            // This create() operation is only called by a REST create. If the given resource
            // contains an id, the for R4 we need to ignore it and replace it with our
            // system-generated value. For the update-or-create scenario, see doUpdate()
            // Default version is 1 for a brand new FHIR Resource.
            int newVersionNumber = 1;
            logicalId = UUID.randomUUID().toString();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Creating new FHIR Resource of type '" + resource.getClass().getSimpleName() + "'");
            }

            // Set the resource id and meta fields.
            Instant lastUpdated = Instant.now(ZoneOffset.UTC);
            resultBuilder.id(Id.of(logicalId));
            Meta meta = resource.getMeta();
            Meta.Builder metaBuilder = meta == null ? Meta.builder() : meta.toBuilder();
            metaBuilder.versionId(Id.of(Integer.toString(newVersionNumber)));
            metaBuilder.lastUpdated(lastUpdated);
            resultBuilder.meta(metaBuilder.build());
            
            // rebuild the resource with updated meta
            @SuppressWarnings("unchecked")
            T updatedResource = (T) resultBuilder.build();
            
            // Create the new Resource DTO instance.
            com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO = new com.ibm.fhir.persistence.jdbc.dto.Resource();
            resourceDTO.setLogicalId(logicalId);
            resourceDTO.setVersionId(newVersionNumber);
            Timestamp timestamp = FHIRUtilities.convertToTimestamp(lastUpdated.getValue());
            resourceDTO.setLastUpdated(timestamp);
            resourceDTO.setResourceType(updatedResource.getClass().getSimpleName());
            
            // Serialize and compress the Resource
            GZIPOutputStream zipStream = new GZIPOutputStream(stream);
            FHIRGenerator.generator( Format.JSON, false).generate(updatedResource, zipStream);
            zipStream.finish();
            resourceDTO.setData(stream.toByteArray());
            zipStream.close();
            
            // Persist the Resource DTO.
            this.getResourceDao().setPersistenceContext(context);
            this.getResourceDao().insert(resourceDTO, this.extractSearchParameters(updatedResource, resourceDTO), this.parameterDao);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' id=" + resourceDTO.getId()
                            + ", version=" + resourceDTO.getVersionId());
            }
            
            SingleResourceResult<T> result = new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource(updatedResource)
                    .build();
            
            return result;
        }
        catch(FHIRPersistenceFKVException e) {
            log.log(Level.SEVERE, "FK violation", e);
//            log.log(Level.SEVERE, this.performCacheDiagnostics());
            throw e;
        }
        catch(FHIRPersistenceException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a create operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
           log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.FHIRPersistence#update(com.ibm.fhir.persistence.context.FHIRPersistenceContext, java.lang.String, com.ibm.fhir.model.Resource)
     */
    @Override
    public <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, String logicalId, T resource) throws FHIRPersistenceException {
        final String METHODNAME = "update";
        log.entering(CLASSNAME, METHODNAME);
        
        Class<? extends Resource> resourceType = resource.getClass();
        com.ibm.fhir.persistence.jdbc.dto.Resource existingResourceDTO;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        // Resources are immutable, so we need a new builder to update it (since R4)
        Resource.Builder resultBuilder = resource.toBuilder();
        
        try {
            // Assume we have no existing resource.
            int existingVersion = 0;
            
            // Compute the new version # from the existing version #.
            
            // If the "previous resource" is set in the persistence event, then get the 
            // existing version # from that.
            if (context.getPersistenceEvent() != null && context.getPersistenceEvent().isPrevFhirResourceSet()) {
                Resource existingResource = context.getPersistenceEvent().getPrevFhirResource();
                if (existingResource != null) {
                    log.fine("Using pre-fetched 'previous' resource.");
                    String version = existingResource.getMeta().getVersionId().getValue();
                    existingVersion = Integer.valueOf(version);
                }
            } 
            
            // Otherwise, go ahead and read the resource from the datastore and get the
            // existing version # from it.
            else {
                log.fine("Fetching 'previous' resource for update.");
                existingResourceDTO = this.getResourceDao().read(logicalId, resourceType.getSimpleName());
                if (existingResourceDTO != null) {
                    existingVersion = existingResourceDTO.getVersionId();
                }
            }
            
            // If this logical resource didn't exist and the "updateCreate" feature is not enabled,
            // then this is an error.
            if (existingVersion == 0 && !updateCreateEnabled) {
                String msg = "Resource '" + resourceType.getSimpleName() + "/" + logicalId + "' not found.";
                log.log(Level.SEVERE, msg);
                throw new FHIRPersistenceResourceNotFoundException(msg);
            }
            
            // Bump up the existing version # to get the new version.
            int newVersionNumber = existingVersion + 1;
            
            if (log.isLoggable(Level.FINE)) {
                if (existingVersion != 0) {
                    log.fine("Updating FHIR Resource '" + resource.getClass().getSimpleName() + "/" + logicalId + "', version=" + existingVersion);
                }
                log.fine("Storing new FHIR Resource '" + resource.getClass().getSimpleName() + "/" + logicalId + "', version=" + newVersionNumber);
            }
            
            Instant lastUpdated = Instant.now(ZoneOffset.UTC);
            
            // Set the resource id and meta fields.
            resultBuilder.id(Id.of(logicalId));
            Meta meta = resource.getMeta();
            Meta.Builder metaBuilder = meta == null ? Meta.builder() : meta.toBuilder();
            metaBuilder.versionId(Id.of(Integer.toString(newVersionNumber)));
            metaBuilder.lastUpdated(lastUpdated);
            resultBuilder.meta(metaBuilder.build());
            
            @SuppressWarnings("unchecked")
            T updatedResource = (T) resultBuilder.build();
            
            // Create the new Resource DTO instance.
            com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO = new com.ibm.fhir.persistence.jdbc.dto.Resource();
            resourceDTO.setLogicalId(logicalId);
            resourceDTO.setVersionId(newVersionNumber);
            Timestamp timestamp = FHIRUtilities.convertToTimestamp(lastUpdated.getValue());
            resourceDTO.setLastUpdated(timestamp);
            resourceDTO.setResourceType(updatedResource.getClass().getSimpleName());
                        
            // Serialize and compress the Resource
            GZIPOutputStream zipStream = new GZIPOutputStream(stream);
            FHIRGenerator.generator(Format.JSON, false).generate(updatedResource, zipStream);
            zipStream.finish();
            resourceDTO.setData(stream.toByteArray());
            zipStream.close();
            
            // Persist the Resource DTO.
            this.getResourceDao().setPersistenceContext(context);
            this.getResourceDao().insert(resourceDTO, this.extractSearchParameters(updatedResource, resourceDTO), this.parameterDao);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' id=" + resourceDTO.getId()
                            + ", version=" + resourceDTO.getVersionId());
            }
            
            SingleResourceResult<T> result = new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource(updatedResource)
                    .build();
            
            return result;
        }
        catch(FHIRPersistenceFKVException e) {
            log.log(Level.SEVERE, this.performCacheDiagnostics());
            throw e;
        }
        catch(FHIRPersistenceException e) {
            throw e;
        }
        catch(Throwable e) {
            // don't chain the exception to avoid leaking secrets
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing an update operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }
    
    @Override
    public MultiResourceResult<Resource> search(FHIRPersistenceContext context, Class<? extends Resource> resourceType)
            throws FHIRPersistenceException {
        final String METHODNAME = "search";
        log.entering(CLASSNAME, METHODNAME);
        
        List<Resource> resources = new ArrayList<Resource>();
        FHIRSearchContext searchContext = context.getSearchContext();
        JDBCQueryBuilder queryBuilder;
        List<Long> sortedIdList;
        List<com.ibm.fhir.persistence.jdbc.dto.Resource> unsortedResultsList;
        int searchResultCount = 0;
        int pageSize;
        int lastPageNumber;
        SqlQueryData countQuery;
        SqlQueryData query;
                
        try {
            queryBuilder = new JDBCQueryBuilder((ParameterDAO)this.getParameterDao(),
                                                          (ResourceDAO)this.getResourceDao());
             
            countQuery = queryBuilder.buildCountQuery(resourceType, searchContext);
            if (countQuery != null) {
                searchResultCount = this.getResourceDao().searchCount(countQuery);
                if (log.isLoggable(Level.FINE)) {
                    log.fine("searchResultCount = " + searchResultCount);
                }
                searchContext.setTotalCount(searchResultCount);
                pageSize = searchContext.getPageSize();
                lastPageNumber = (int) ((searchResultCount + pageSize - 1) / pageSize);
                searchContext.setLastPageNumber(lastPageNumber);
                
                // For _summary=count, we don't need to return any resource 
                if (searchResultCount > 0 && 
                        !(searchContext.getSummaryParameter() != null 
                        && searchContext.getSummaryParameter().equals(SummaryValueSet.COUNT))) {
                    query = queryBuilder.buildQuery(resourceType, searchContext);
                    
                    List<String> elements = searchContext.getElementsParameters();
                    
                    //Only consider _summary if _elements parameter is empty
                    if (elements == null && searchContext.hasSummaryParameter()) {
                        Set<String> summaryElements = null;
                        SummaryValueSet summary = searchContext.getSummaryParameter();
                        
                        switch (summary) {
                        case TRUE:
                            summaryElements = JsonSupport.getSummaryElementNames(resourceType);
                            break;
                        case TEXT:
                            summaryElements = SearchUtil.getSummaryTextElementNames(resourceType);
                            break;
                        case DATA:
                            summaryElements = JsonSupport.getSummaryDataElementNames(resourceType);
                            break;
                        default:
                            break;
                            
                        }

                        if (summaryElements != null) {
                            elements = new ArrayList<String>();
                            elements.addAll(summaryElements);
                        }
                    }
                    
                    if (searchContext.hasSortParameters()) {
                        // Sorting results of a system-level search is limited, and has a different logic path
                        // than other sorted searches.
                        if (resourceType.equals(Resource.class)) {
                           resources = this.convertResourceDTOList(this.resourceDao.search(query), resourceType, elements);
                        }
                        else {
                            sortedIdList = this.resourceDao.searchForIds(query);
                            resources = this.buildSortedFhirResources(context, resourceType, sortedIdList, elements);
                        }
                    }
                    else {
                        unsortedResultsList = this.getResourceDao().search(query);
                        resources = this.convertResourceDTOList(unsortedResultsList, resourceType, elements);
                    }  
                }
            }
            
            MultiResourceResult<Resource> result = new MultiResourceResult.Builder<>()
                    .success(true)
                    .resource(resources)
                    .build();
            
            return result;
        }
        catch(FHIRPersistenceException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a search operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }
    
    private ParameterDAO getParameterDao() {
        return parameterDao;
    }

    private ResourceDAO getResourceDao() {
        return resourceDao;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.FHIRPersistence#delete(com.ibm.fhir.persistence.context.FHIRPersistenceContext, Class<? extends Resource> resourceType, java.lang.String)
     */
    @Override
    public <T extends Resource> SingleResourceResult<T> delete(FHIRPersistenceContext context, Class<T> resourceType, String logicalId) throws FHIRPersistenceException {
        final String METHODNAME = "delete";
        log.entering(CLASSNAME, METHODNAME);
        
        
        com.ibm.fhir.persistence.jdbc.dto.Resource existingResourceDTO = null;
        T existingResource = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        Resource.Builder resourceBuilder;
        
        try {
            existingResourceDTO = this.getResourceDao().read(logicalId, resourceType.getSimpleName());
            
            if (existingResourceDTO != null) {
                if (existingResourceDTO.isDeleted()) {
                    existingResource = this.convertResourceDTO(existingResourceDTO, resourceType, null);
                    resourceBuilder = existingResource.toBuilder();
                    
                    SingleResourceResult<T> result = new SingleResourceResult.Builder<T>()
                            .success(true)
                            .resource(existingResource)
                            .build();
                    
                    return result;
                }
                else {
                    existingResource = this.convertResourceDTO(existingResourceDTO, resourceType, null);
                    
                    // Resources are immutable, so we need a new builder to update it (since R4)
                    resourceBuilder = existingResource.toBuilder();
    
                    int newVersionNumber = existingResourceDTO.getVersionId() + 1;
                    Instant lastUpdated = Instant.now(ZoneOffset.UTC);
                    
                    // Update the soft-delete resource to reflect the new version and lastUpdated values.
                    Meta meta = existingResource.getMeta();
                    Meta.Builder metaBuilder = meta == null ? Meta.builder() : meta.toBuilder();
                    metaBuilder.versionId(Id.of(Integer.toString(newVersionNumber)));
                    metaBuilder.lastUpdated(lastUpdated);
                    resourceBuilder.meta(metaBuilder.build());
                    
                    
                    @SuppressWarnings("unchecked")
                    T updatedResource = (T) resourceBuilder.build();
    
                    // Create a new Resource DTO instance to represent the deleted version.
                    com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO = new com.ibm.fhir.persistence.jdbc.dto.Resource();
                    resourceDTO.setLogicalId(logicalId);
                    resourceDTO.setVersionId(newVersionNumber);
                    
                    // Serialize and compress the Resource
                    GZIPOutputStream zipStream = new GZIPOutputStream(stream);
                    FHIRGenerator.generator(Format.JSON, false).generate(updatedResource, zipStream);
                    zipStream.finish();
                    resourceDTO.setData(stream.toByteArray());
                    zipStream.close();
                    
                    Timestamp timestamp = FHIRUtilities.convertToTimestamp(lastUpdated.getValue());
                    resourceDTO.setLastUpdated(timestamp);
                    resourceDTO.setResourceType(resourceType.getSimpleName());
                    resourceDTO.setDeleted(true);
    
                    // Persist the logically deleted Resource DTO.
                    this.getResourceDao().setPersistenceContext(context);
                    this.getResourceDao().insert(resourceDTO, null, null);
                    
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' id=" + resourceDTO.getId()
                                    + ", version=" + resourceDTO.getVersionId());
                    }
                    
                    SingleResourceResult<T> result = new SingleResourceResult.Builder<T>()
                            .success(true)
                            .resource(updatedResource)
                            .build();
                    
                    return result;
                }
            }
            else {
                // issue fhir-527. Need to return not found
                throw new FHIRPersistenceResourceNotFoundException("resource does not exist: " + resourceType.getSimpleName() + ":" + logicalId);
            }
        }
        catch(FHIRPersistenceFKVException e) {
            log.log(Level.SEVERE, this.performCacheDiagnostics());
            throw e;
        }
        catch(FHIRPersistenceException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a delete operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.FHIRPersistence#read(com.ibm.fhir.persistence.context.FHIRPersistenceContext, java.lang.Class, java.lang.String)
     */
    @Override
    public <T extends Resource> SingleResourceResult<T> read(FHIRPersistenceContext context, Class<T> resourceType, String logicalId)
                            throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
        final String METHODNAME = "read";
        log.entering(CLASSNAME, METHODNAME);
        
        T resource = null;
        com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO = null;

        FHIRSearchContext searchContext = context.getSearchContext();
        List<String> elements = null;
        //Check if _summary is required
        if (searchContext != null && searchContext.hasSummaryParameter()) {
            Set<String> summaryElements = null;
            SummaryValueSet summary = searchContext.getSummaryParameter();
            
            switch (summary) {
            case TRUE:
                summaryElements = JsonSupport.getSummaryElementNames(resourceType);
                break;
            case TEXT:
                summaryElements = SearchUtil.getSummaryTextElementNames(resourceType);
                break;
            case DATA:
                summaryElements = JsonSupport.getSummaryDataElementNames(resourceType);
                break;
            default:
                break;
                
            }

            if (summaryElements != null) {
                elements = new ArrayList<String>();
                elements.addAll(summaryElements);
            }
        }
                
        try {
            resourceDTO = this.getResourceDao().read(logicalId, resourceType.getSimpleName());
            if (resourceDTO != null && resourceDTO.isDeleted() && !context.includeDeleted()) {
                throw new FHIRPersistenceResourceDeletedException("Resource '" + resourceType.getSimpleName() + "/" + logicalId + "' is deleted.");
            }
            resource = this.convertResourceDTO(resourceDTO, resourceType, elements);
            
            SingleResourceResult<T> result = new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource(resource)
                    .build();
            
            return result;
        }
        catch(FHIRPersistenceResourceDeletedException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a read operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;

        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }        
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.FHIRPersistence#history(com.ibm.fhir.persistence.context.FHIRPersistenceContext, java.lang.Class, java.lang.String)
     */
    @Override
    public <T extends Resource> MultiResourceResult<T> history(FHIRPersistenceContext context, Class<T> resourceType,
            String logicalId) throws FHIRPersistenceException {
        final String METHODNAME = "history";
        log.entering(CLASSNAME, METHODNAME);
        
        List<T> resources = new ArrayList<>();
        List<com.ibm.fhir.persistence.jdbc.dto.Resource> resourceDTOList;
        Map<String,List<Integer>> deletedResourceVersions = new HashMap<>();
        FHIRHistoryContext historyContext;
        int resourceCount;
        Instant since;
        Timestamp fromDateTime = null;
        int pageSize;
        int lastPageNumber;
        int offset;
                
        try {
            historyContext = context.getHistoryContext();
            historyContext.setDeletedResources(deletedResourceVersions);
            since = historyContext.getSince();
            if (since != null) {
                fromDateTime = FHIRUtilities.convertToTimestamp(since.getValue());
            }
            
            resourceCount = this.getResourceDao().historyCount(resourceType.getSimpleName(), logicalId, fromDateTime);
            historyContext.setTotalCount(resourceCount);
            pageSize = historyContext.getPageSize();
            lastPageNumber = (int) ((resourceCount + pageSize - 1) / pageSize);
            historyContext.setLastPageNumber(lastPageNumber);            
            
            
            
            if (resourceCount > 0) {
                offset = (historyContext.getPageNumber() - 1) * pageSize;
                resourceDTOList = this.getResourceDao().history(resourceType.getSimpleName(), logicalId, fromDateTime, offset, pageSize);
                for (com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO : resourceDTOList) {
                    if (resourceDTO.isDeleted()) {
                        deletedResourceVersions.putIfAbsent(logicalId, new ArrayList<Integer>());
                        deletedResourceVersions.get(logicalId).add(resourceDTO.getVersionId());
                    }
                }
                log.log(Level.FINE, "deletedResourceVersions=" + deletedResourceVersions);
                resources = this.convertResourceDTOList(resourceDTOList, resourceType);
            } 
            
            MultiResourceResult<T> result = new MultiResourceResult.Builder<T>()
                    .success(true)
                    .resource(resources)
                    .build();
            
            return result;
        }
        catch(FHIRPersistenceException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a history operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.FHIRPersistence#vread(com.ibm.fhir.persistence.context.FHIRPersistenceContext, java.lang.Class, java.lang.String, java.lang.String)
     */
    @Override
    public <T extends Resource> SingleResourceResult<T> vread(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, String versionId) 
                        throws FHIRPersistenceException {
        final String METHODNAME = "vread";
        log.entering(CLASSNAME, METHODNAME);
        
        T resource = null;
        com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO = null;
        int version;

        try {
            version = Integer.parseInt(versionId);
            resourceDTO = this.getResourceDao().versionRead(logicalId, resourceType.getSimpleName(), version);
            if (resourceDTO != null && resourceDTO.isDeleted()) {
                throw new FHIRPersistenceResourceDeletedException("Resource '" + resourceType.getSimpleName() + "/" + logicalId + "' version " + versionId + " is deleted.");
            }
            resource = this.convertResourceDTO(resourceDTO, resourceType, null);
            
            SingleResourceResult<T> result = new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource(resource)
                    .build();
            
            return result;
        }
        catch(FHIRPersistenceResourceDeletedException e) {
            throw e;
        }
        catch (NumberFormatException e) {
            throw new FHIRPersistenceException("Invalid version id specified for vread operation: " + versionId);
        }
        catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a version read operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }
    
    /**
     * This method takes the passed list of sorted Resource ids, acquires the Resource corresponding to each id, and returns those Resources in a List,
     * sorted according to the input sorted ids.
     * @param context - The FHIR persistence context for the current request.
     * @param resourceType - The type of Resource that each id in the passed list represents.
     * @param sortedIdList - A list of Resource ids representing the proper sort order for the list of Resources to be returned.
     * @param elements - An optional list of element names to include in the resources. If null, filtering will be skipped.
     * @return List<Resource> - A list of Resources of the passed resourceType, sorted according the order of ids in the passed sortedIdList.
     * @throws FHIRPersistenceException
     * @throws IOException 
     */
    protected List<Resource> buildSortedFhirResources(FHIRPersistenceContext context, Class<? extends Resource> resourceType, List<Long> sortedIdList, 
                                                      List<String> elements) 
                            throws FHIRException, FHIRPersistenceException, IOException {
        final String METHOD_NAME = "buildFhirResource";
        log.entering(this.getClass().getName(), METHOD_NAME);
        
        long resourceId;
        Resource[] sortedFhirResources = new Resource[sortedIdList.size()];
        Resource fhirResource;
        int sortIndex;
        List<Resource> sortedResourceList = new ArrayList<>();
        List<com.ibm.fhir.persistence.jdbc.dto.Resource> resourceDTOList;
        Map<Long,Integer> idPositionMap = new HashMap<>();
                
        // This loop builds a Map where key=resourceId, and value=its proper position in the returned sorted collection.
        for(int i = 0; i < sortedIdList.size(); i++) {
            resourceId = sortedIdList.get(i);
            idPositionMap.put(new Long(resourceId), new Integer(i));
        }
        
        resourceDTOList = this.getResourceDTOs(resourceType, sortedIdList);
        
        
        // Convert the returned JPA Resources to FHIR Resources, and store each FHIRResource in its proper position
        // in the returned sorted resource list.
        for (com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO : resourceDTOList) {
            fhirResource = this.convertResourceDTO(resourceDTO, resourceType, elements);
            if (fhirResource != null) {
                sortIndex = idPositionMap.get(resourceDTO.getId());
                sortedFhirResources[sortIndex] = fhirResource;
            }
        }
        
        for (int i = 0; i <sortedFhirResources.length; i++) {
            if (sortedFhirResources[i] != null) {
                sortedResourceList.add(sortedFhirResources[i]);
            }
        }
        log.exiting(this.getClass().getName(), METHOD_NAME);
        return sortedResourceList;
    }
    
    /**
     * Returns a List of Resource DTOs corresponding to the passed list of Resource IDs.
     * @param resourceType The type of resource being queried.
     * @param sortedIdList A sorted list of Resource IDs.
     * @return List - A list of ResourceDTOs
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     */
    private List<com.ibm.fhir.persistence.jdbc.dto.Resource> getResourceDTOs(
            Class<? extends Resource> resourceType, List<Long> sortedIdList) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
         
        return this.getResourceDao().searchByIds(resourceType.getSimpleName(), sortedIdList);
    }
    
    /**
     * Converts the passed Resource Data Transfer Object collection to a collection of FHIR Resource objects.
     * @param resourceDTOList
     * @param resourceType
     * @return
     * @throws FHIRException
     * @throws IOException 
     */
    protected List<Resource> convertResourceDTOList(List<com.ibm.fhir.persistence.jdbc.dto.Resource> resourceDTOList, 
            Class<? extends Resource> resourceType, List<String> elements) throws FHIRException, IOException {
        final String METHODNAME = "convertResourceDTO List";
        log.entering(CLASSNAME, METHODNAME);
        
        List<Resource> resources = new ArrayList<>();
        try {
            for (com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO : resourceDTOList) {
                Resource existingResource = this.convertResourceDTO(resourceDTO, resourceType, elements);
                if (resourceDTO.isDeleted()) {
                    Resource deletedResourceMarker = FHIRPersistenceUtil.createDeletedResourceMarker(existingResource);
                    resources.add(deletedResourceMarker);
                } else {
                    resources.add(existingResource);
                }
            }
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resources;
    }
    
   /**
     * Calls some cache analysis methods and aggregates the output into a single String.
     * @return
     */
    private String performCacheDiagnostics() {
        
        StringBuffer diags = new StringBuffer();
        diags.append(ParameterNamesCache.dumpCacheContents()).append(ParameterNamesCache.reportCacheDiscrepancies(this.parameterDao));
        diags.append(CodeSystemsCache.dumpCacheContents()).append(CodeSystemsCache.reportCacheDiscrepancies(this.parameterDao));
        diags.append(ResourceTypesCache.dumpCacheContents()).append(ResourceTypesCache.reportCacheDiscrepancies(this.resourceDao));
        
        return diags.toString();
    }
    
    /**
     * Looks up and returns an instance of TransactionSynchronizationRegistry, which is used in support of writing committed
     * data to JDBC PL in-memory caches.
     * @return TransactionSynchronizationRegistry
     * @throws FHIRPersistenceException
     */
    private TransactionSynchronizationRegistry getTrxSynchRegistry() throws FHIRPersistenceException {
        
        InitialContext ctxt;
        
        if (this.trxSynchRegistry == null) {
            try {
                ctxt = new InitialContext();
                this.trxSynchRegistry = (TransactionSynchronizationRegistry) ctxt.lookup(TRX_SYNCH_REG_JNDI_NAME);
            }
            catch(Throwable e) {
                FHIRPersistenceException fx = new FHIRPersistenceException("Failed to acquire TrxSynchRegistry service");
                log.log(Level.SEVERE, fx.getMessage(), e);
                throw fx;
            }
        }
        
        return this.trxSynchRegistry;
    }
    
    /**
     * Extracts search parameters for the passed FHIR Resource.
     * @param fhirResource - Some FHIR Resource
     * @param resourceDTO - A Resource DTO representation of the passed FHIR Resource.
     * @throws Exception 
     */
    private List<Parameter> extractSearchParameters(Resource fhirResource, com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO) 
                 throws Exception {
        final String METHODNAME = "extractSearchParameters";
        log.entering(CLASSNAME, METHODNAME);
        
        Map<SearchParameter, List<FHIRPathNode>> map;
        String code;
        String type;
        String expression;
        
        List<Parameter> allParameters = new ArrayList<>();
        
        try {
            map = SearchUtil.extractParameterValues(fhirResource);
            
            for (Entry<SearchParameter, List<FHIRPathNode>> entry : map.entrySet()) {
                 
                code = entry.getKey().getCode().getValue();
                type = entry.getKey().getType().getValue();
                expression = entry.getKey().getExpression().getValue();
                
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Processing SearchParameter code: " + code + ", type: " + type + ", expression: " + expression);
                }
                
                JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(code);
                
                List<FHIRPathNode> values = entry.getValue();
                
                for (FHIRPathNode value : values) {
                    if (value.isElementNode()) {
                        // parameterBuilder aggregates the results for later retrieval
                        value.asElementNode().element().accept(parameterBuilder);
                    } else if (value.isPrimitiveValue()){
                        Parameter p = processPrimitiveValue(value.asPrimitiveValue());
                        p.setName(code);
                        p.setType(Type.fromValue(type));
                        p.setResourceId(resourceDTO.getId());
                        p.setResourceType(fhirResource.getClass().getSimpleName());
                        allParameters.add(p);
                        if (log.isLoggable(Level.FINE)) {
                            log.fine("Extracted Parameter '" + p.getName() + "' from Resource.");
                        }
                    } else {
                        // log and continue
                        if (log.isLoggable(Level.FINE)) {
                            log.fine("Unable to extract value from '" + value.path() +
                                    "'; search parameter value extraction can only be performed on Elements and primitive values.");
                        }
                        // TODO: return this as a OperationOutcomeIssue with severity of WARNING
                        continue;
                    }
                }
                // retrieve the list of parameters built from all the FHIRPathElementNode values 
                List<Parameter> parameters = parameterBuilder.getResult();
                for (Parameter p : parameters) {
                    p.setType(Type.fromValue(type));
                    p.setResourceId(resourceDTO.getId());
                    p.setResourceType(fhirResource.getClass().getSimpleName());
                    allParameters.add(p);
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Extracted Parameter '" + p.getName() + "' from Resource.");
                    }
                }
            }
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return allParameters;
    }

    /**
     * Create a Parameter DTO from the primitive value.
     * Note: this method only sets the value; 
     * caller is responsible for setting all other fields on the created Parameter.
     */
    private Parameter processPrimitiveValue(FHIRPathPrimitiveValue primitiveValue) {
        Parameter p = new Parameter();
        if (primitiveValue.isBooleanValue()) {
            if (primitiveValue.asBooleanValue()._boolean()) {
                p.setValueCode("true");
            } else {
                p.setValueCode("false");
            }
        } else if (primitiveValue.isDateTimeValue()) {
            p.setValueDate(Timestamp.from(QueryBuilderUtil.getInstantFromPartial(primitiveValue.asDateTimeValue().dateTime())));
        } else if (primitiveValue.isStringValue()) {
            p.setValueString(primitiveValue.asStringValue().string());
        } else if (primitiveValue.isTimeValue()) {
            p.setValueDate(Timestamp.from(QueryBuilderUtil.getInstantFromPartial(primitiveValue.asTimeValue().time())));
        } else if (primitiveValue.isNumberValue()) {
            p.setValueNumber(primitiveValue.asNumberValue().decimal());
        }
        return p;
    }

    @Override
    public void begin() throws FHIRPersistenceException {
        final String METHODNAME = "begin";
        log.entering(CLASSNAME, METHODNAME);
            
        try {
            if (userTransaction != null) {
                sharedConnection = createConnection();
                resourceDao.setExternalConnection(sharedConnection);
                parameterDao.setExternalConnection(sharedConnection);
                userTransaction.begin();
            }
            else if (this.getManagedConnection() != null) {
                this.getManagedConnection().setAutoCommit(false);
            }
        }
        catch (Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while starting a transaction.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;            
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    @Override
    public void commit() throws FHIRPersistenceException {
        final String METHODNAME = "commit";
        log.entering(CLASSNAME, METHODNAME);
        
        try {
            if (userTransaction != null) {
                userTransaction.commit();
            } else if (this.getManagedConnection() != null) {
                this.getManagedConnection().commit();
            }
        } 
        catch (Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while committing a transaction.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;            
        }
        finally {
            if (sharedConnection != null) {
                try {
                    sharedConnection.close();
                } catch (SQLException e) {
                    throw new FHIRPersistenceException("Failure closing DB Conection", e);
                }
                sharedConnection = null;
            }
            log.exiting(CLASSNAME, METHODNAME);
        }
    
    }

    @Override
    public void rollback() throws FHIRPersistenceException {
        final String METHODNAME = "rollback";
        log.entering(CLASSNAME, METHODNAME);
        
        try {
            if (userTransaction != null) {
                userTransaction.rollback();
            } 
            else if (this.getManagedConnection() != null) {
                this.getManagedConnection().rollback();
            }
        } 
        catch (Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while rolling back a transaction.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;            
        } 
        finally {
            if (sharedConnection != null) {
                try {
                    sharedConnection.close();
                } 
                catch (SQLException e) {
                    throw new FHIRPersistenceException("Failure closing DB Conection", e);
                }
                sharedConnection = null;
            }
        }
    }

    @Override
    public void setRollbackOnly() throws FHIRPersistenceException {
        final String METHODNAME = "setRollbackOnly";
        log.entering(CLASSNAME, METHODNAME);
        String errorMessage = "Unexpected error while rolling a transaction.";
        
        try {
            if (userTransaction != null) {
                errorMessage = "Unexpected error while marking a transaction for rollback.";
                userTransaction.setRollbackOnly();
            } 
            else if (this.getManagedConnection() != null) {
                this.getManagedConnection().rollback();
            }
        } 
        catch (Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException(errorMessage);
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;            
        } 
        finally {
            if (sharedConnection != null) {
                try {
                    sharedConnection.close();
                } 
                catch (SQLException e) {
                    FHIRPersistenceException fx = new FHIRPersistenceException("Failure closing DB Conection");
                    log.log(Level.SEVERE, fx.getMessage(), e);
                    throw fx;            
                }
                sharedConnection = null;
            }
        }
    }

    @Override
    public OperationOutcome getHealth() throws FHIRPersistenceException {
        try (Connection connection = createConnection()){
            if (connection.isValid(2)) {
                return buildOKOperationOutcome();
            } else {
                return buildErrorOperationOutcome();
            }
        } catch (SQLException e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Error while validating the database connection");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
    }

    /**
     * Retrieves (via a JNDI lookup) a reference to the UserTransaction. If the JNDI lookup fails, we'll assume that
     * we're not running inside the container.
     */
    protected UserTransaction retrieveUserTransaction(String jndiName) {
        UserTransaction txn = null;
        try {
            InitialContext ctx = new InitialContext();
            txn = (UserTransaction) ctx.lookup(jndiName);
        } catch (Throwable t) {
            // ignore any exceptions here.
        }

        return txn;
    }
    
    @Override
    public boolean isActive() throws FHIRPersistenceException {
        boolean isActive = false;
        try {
            if (userTransaction != null) {
                isActive = (userTransaction.getStatus() == Status.STATUS_ACTIVE);
            }
        } 
        catch (Throwable e) {
            String msg = "An unexpected error occurred while examining transactional status.";
            FHIRPersistenceException fx = new FHIRPersistenceException(msg);
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        return isActive;
    }
    
    /**
     * Converts the passed Resource Data Transfer Object collection to a collection of FHIR Resource objects.
     * @param resourceDTOList
     * @param resourceType
     * @return
     * @throws FHIRException
     * @throws IOException 
     */
    // This variant uses generics and is used in history.
    // TODO: this method needs to either get merged or better differentiated with the old one used for search
    protected <T extends Resource> List<T> convertResourceDTOList(List<com.ibm.fhir.persistence.jdbc.dto.Resource> resourceDTOList, Class<T> resourceType) 
                                throws FHIRException, IOException {
        final String METHODNAME = "convertResourceDTO List";
        log.entering(CLASSNAME, METHODNAME);
        
        List<T> resources = new ArrayList<>();
        try {
            for (com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO : resourceDTOList) {
                resources.add(this.convertResourceDTO(resourceDTO, resourceType, null));
            }
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resources;
    }
    
    /**
     * Converts the passed Resource Data Transfer Object collection to a collection of FHIR Resource objects.
     * @param resourceDTOList
     * @param resourceType
     * @return
     * @throws FHIRException
     * @throws IOException 
     */
    // This variant doesn't use generics and is used in search.
    // TODO: this method needs to either get merged or better differentiated with the new one that supports history operation via generics.
    // Start by better understanding what happens for `_include` and `_revinclude` search results that contain multiple different types
    protected List<Resource> convertResourceDTOListOld(List<com.ibm.fhir.persistence.jdbc.dto.Resource> resourceDTOList, Class<? extends Resource> resourceType) 
                                throws FHIRException, IOException {
        final String METHODNAME = "convertResourceDTO List";
        log.entering(CLASSNAME, METHODNAME);
        
        List<Resource> resources = new ArrayList<>();
        try {
            for (com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO : resourceDTOList) {
                resources.add(this.convertResourceDTO(resourceDTO, resourceType, null));
            }
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resources;
    }
    
    /**
     * Converts the passed Resource Data Transfer Object to a FHIR Resource object.
     * @param resourceDTO - A valid Resource DTO
     * @param resourceType - The FHIR type of resource to be converted.
     * @param elements - An optional filter for including only specified elements inside a Resource.
     * @return Resource - A FHIR Resource object representation of the data portion of the passed Resource DTO.
     * @throws FHIRException
     * @throws IOException 
     */
    private <T extends Resource> T convertResourceDTO(com.ibm.fhir.persistence.jdbc.dto.Resource resourceDTO, 
            Class<T> resourceType, 
            List<String> elements) throws FHIRException, IOException {
        final String METHODNAME = "convertResourceDTO";
        log.entering(CLASSNAME, METHODNAME);
        T resource = null;
        try {
            if (resourceDTO != null) {
                InputStream in = new GZIPInputStream(new ByteArrayInputStream(resourceDTO.getData()));
                if (elements != null) {
                    // parse/filter the resource using elements
                    resource = FHIRParser.parser(Format.JSON).as(FHIRJsonParser.class).parseAndFilter(in, elements);
                    if (resourceType.equals(resource.getClass()) && !FHIRUtil.hasTag(resource, SearchConstants.SUBSETTED_TAG)) {
                        // add a SUBSETTED tag to this resource to indicate that its elements have been filtered
                        resource = FHIRUtil.addTag(resource, SearchConstants.SUBSETTED_TAG);
                    }
                } else {
                    resource = FHIRParser.parser(Format.JSON).parse(in);  
                }
                in.close();
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resource;
    }

    @Override
    public boolean isTransactional() {
        return true;
    }

    @Override
    public FHIRPersistenceTransaction getTransaction() {
        return this;
    }

    @Override
    public boolean isDeleteSupported() {
        return true;
    }
    
    private FHIRDbDAO getBaseDao() {
        return baseDao;
    }
    
    private void setBaseDao(FHIRDbDAO baseDao) {
        this.baseDao = baseDao;
    }
    
    private Connection getManagedConnection() {
        return managedConnection;
    }

    private void setManagedConnection(Connection managedConnection) {
        this.managedConnection = managedConnection;
    }
    
    private OperationOutcome buildOKOperationOutcome() {
        return FHIRUtil.buildOperationOutcome("All OK", IssueType.INFORMATIONAL, IssueSeverity.INFORMATION);
    }

    private OperationOutcome buildErrorOperationOutcome() {
        return FHIRUtil.buildOperationOutcome("The database connection was not valid", IssueType.NO_STORE, IssueSeverity.ERROR);
    }
    
    private Connection createConnection() throws FHIRPersistenceDBConnectException {
        FHIRDbDAOImpl dao = new FHIRDbDAOImpl();
        return dao.getConnection();
    }
}
