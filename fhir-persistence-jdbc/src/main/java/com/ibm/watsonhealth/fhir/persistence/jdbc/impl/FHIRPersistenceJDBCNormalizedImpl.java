/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.impl;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_JDBC_ENABLE_CODE_SYSTEMS_CACHE;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_JDBC_ENABLE_PARAMETER_NAMES_CACHE;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_JDBC_ENABLE_RESOURCE_TYPES_CACHE;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_REPL_INTERCEPTOR_ENABLED;
import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_UPDATE_CREATE_ENABLED;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.id;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.instant;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import javax.naming.InitialContext;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.model.Meta;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ResourceNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.FHIRDbDAOBasicImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ParameterDAONormalizedImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ResourceDAONormalizedImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.CodeSystemsCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.JDBCNormalizedQueryBuilder;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ParameterNamesCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ResourceTypesCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.SqlQueryData;
import com.ibm.watsonhealth.fhir.replication.api.util.ReplicationUtil;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;

/**
 * This class is the JDBC implementation of the FHIRPersistence interface to support the "normalized" DB schema, 
 * providing implementations for CRUD type APIs and search.
 * @author markd
 *
 */
public class FHIRPersistenceJDBCNormalizedImpl extends FHIRPersistenceJDBCImpl implements FHIRPersistence, FHIRPersistenceTransaction {
	
	private static final String CLASSNAME = FHIRPersistenceJDBCNormalizedImpl.class.getName();
	private static final Logger log = Logger.getLogger(CLASSNAME);
	
	public static final String TRX_SYNCH_REG_JNDI_NAME = "java:comp/TransactionSynchronizationRegistry";
	
	
	private ResourceNormalizedDAO resourceDao;
	private ParameterNormalizedDAO parameterDao;
	private TransactionSynchronizationRegistry trxSynchRegistry;

	/**
	 * Constructor for use when running as web application in WLP. 
	 * @throws Exception 
	 */
	public FHIRPersistenceJDBCNormalizedImpl() throws Exception {
		super();
		final String METHODNAME = "FHIRPersistenceJDBCNormalizedImpl()";
		log.entering(CLASSNAME, METHODNAME);
		
		PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
        this.updateCreateEnabled = fhirConfig.getBooleanProperty(PROPERTY_UPDATE_CREATE_ENABLED, Boolean.TRUE);
        ParameterNamesCache.setEnabled(fhirConfig.getBooleanProperty(PROPERTY_JDBC_ENABLE_PARAMETER_NAMES_CACHE, 
                                       Boolean.TRUE.booleanValue()));
        CodeSystemsCache.setEnabled(fhirConfig.getBooleanProperty(PROPERTY_JDBC_ENABLE_CODE_SYSTEMS_CACHE, 
                                    Boolean.TRUE.booleanValue()));
        ResourceTypesCache.setEnabled(fhirConfig.getBooleanProperty(PROPERTY_JDBC_ENABLE_RESOURCE_TYPES_CACHE, 
                                      Boolean.TRUE.booleanValue()));
		this.resourceDao = new ResourceDAONormalizedImpl(this.getTrxSynchRegistry());
		this.resourceDao.setRepInfoRequired(fhirConfig.getBooleanProperty(PROPERTY_REPL_INTERCEPTOR_ENABLED, Boolean.FALSE));
		this.parameterDao = new ParameterDAONormalizedImpl(this.getTrxSynchRegistry());
		
		log.exiting(CLASSNAME, METHODNAME);
	}
	
	/**
	 * Constructor for use when running standalone, outside of any web container.
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public FHIRPersistenceJDBCNormalizedImpl(Properties configProps) throws Exception {
		super(configProps);
		final String METHODNAME = "FHIRPersistenceJDBCNormalizedImpl(Properties)";
		log.entering(CLASSNAME, METHODNAME);
		
		this.updateCreateEnabled = Boolean.parseBoolean(configProps.getProperty("updateCreateEnabled"));
		
		this.setBaseDao(new FHIRDbDAOBasicImpl(configProps));
		this.setManagedConnection(this.getBaseDao().getConnection());
		this.resourceDao = new ResourceDAONormalizedImpl(this.getManagedConnection());
		this.resourceDao.setRepInfoRequired(false);
		this.parameterDao = new ParameterDAONormalizedImpl(this.getManagedConnection());
				
		log.exiting(CLASSNAME, METHODNAME);
	}

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistence#isDeleteSupported()
     */
    @Override
    public boolean isDeleteSupported() {
        return true;
    }
    
	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistence#create(com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext, com.ibm.watsonhealth.fhir.model.Resource)
	 */
	@Override
	public void create(FHIRPersistenceContext context, Resource resource) throws FHIRPersistenceException  {
		final String METHODNAME = "create";
		log.entering(CLASSNAME, METHODNAME);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		String logicalId;
		
		try {
	        	
	        // Default version is 1 for a brand new FHIR Resource.
	        int newVersionNumber = 1;
	        logicalId = (resource.getId() != null ? resource.getId().getValue() : UUID.randomUUID().toString());
	        if (log.isLoggable(Level.FINE)) {
	        	log.fine("Creating new FHIR Resource of type '" + resource.getClass().getSimpleName() + "'");
	        }
	        // Create the new Resource DTO instance.
	        com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = new com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource();
	        resourceDTO.setLogicalId(logicalId);
	        resourceDTO.setVersionId(newVersionNumber);
	        Instant lastUpdated = instant(System.currentTimeMillis());
	        Timestamp timestamp = FHIRUtilities.convertToTimestamp(lastUpdated.getValue());
	        resourceDTO.setLastUpdated(timestamp);
	        resourceDTO.setResourceType(resource.getClass().getSimpleName());
	        
	        // Set the resource id and meta fields.
	        resource.setId(id(resourceDTO.getLogicalId()));
	        Meta meta = resource.getMeta();
	        if (meta == null) {
	            meta = objectFactory.createMeta();
	        }
	        meta.setVersionId(id(Integer.toString(newVersionNumber)));
	        meta.setLastUpdated(lastUpdated);
	        resource.setMeta(meta);
	        
	        // Serialize and compress the Resource
	        GZIPOutputStream zipStream = new GZIPOutputStream(stream);
	        FHIRUtil.write(resource, Format.JSON, zipStream, false);
	        zipStream.finish();
	        resourceDTO.setData(stream.toByteArray());
	        zipStream.close();
	        
	        // Store search parameters BEFORE persisting the resource. Stored procedures that are called in the DAO layer depend upon
	        // the search parameters being persisted first (in a global temporary table).
	        this.storeSearchParameters(resource, resourceDTO);
	        
	        // Persist the Resource DTO.
	        this.getResourceDao().setPersistenceContext(context);
	        this.getResourceDao().insert(resourceDTO);
	        if (log.isLoggable(Level.FINE)) {
	        	log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' id=" + resourceDTO.getId()
	        				+ ", version=" + resourceDTO.getVersionId());
	        }
	    }
		catch(FHIRPersistenceFKVException e) {
			String msg = "Unexpected FK Violation while performing a create operation.";
	        log.log(Level.SEVERE, msg, e);
	        log.log(Level.SEVERE, this.performCacheDiagnostics());
	        throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		catch(Throwable e) {
            String msg = "Unexpected error while performing a create operation.";
            log.log(Level.SEVERE, msg, e);
            throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
        }
		finally {
		   log.exiting(CLASSNAME, METHODNAME);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistence#update(com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext, java.lang.String, com.ibm.watsonhealth.fhir.model.Resource)
	 */
	@Override
	public void update(FHIRPersistenceContext context, String logicalId, Resource resource) throws FHIRPersistenceException {
		final String METHODNAME = "update";
		log.entering(CLASSNAME, METHODNAME);
		
		Class<? extends Resource> resourceType = resource.getClass();
		com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource existingResourceDTO;
		int newVersionNumber = 1;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		try {
			// Get the current version of the Resource.
			existingResourceDTO = this.getResourceDao().read(logicalId, resourceType.getSimpleName());
	        
	        // If this FHIR Resource doesn't exist and updateCreateEnabled is turned off, throw an exception
	        if (existingResourceDTO == null && !updateCreateEnabled) {
	            String msg = "Resource '" + resourceType.getSimpleName() + "/" + logicalId + " not found.";
	            log.log(Level.SEVERE, msg);
	            throw new FHIRPersistenceResourceNotFoundException(msg);
	        }
	        
	        // If the FHIR Resource already exists, then we'll simply bump up the version #, use its logical id,
	        // and remove its Parameter entries.
	        if (existingResourceDTO != null) {
	            newVersionNumber = existingResourceDTO.getVersionId() + 1;
	            if (log.isLoggable(Level.FINE)) {
	            	log.fine("Updating FHIR Resource '" + resource.getClass().getSimpleName() + "/" + existingResourceDTO.getLogicalId() + "', version="
	                        	+ existingResourceDTO.getVersionId());
	            }
	        } 
	        
	        // Create the new Resource DTO instance.
	        com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = new com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource();
	        resourceDTO.setLogicalId(logicalId);
	        resourceDTO.setVersionId(newVersionNumber);
	        Instant lastUpdated = instant(System.currentTimeMillis());
	        Timestamp timestamp = FHIRUtilities.convertToTimestamp(lastUpdated.getValue());
	        resourceDTO.setLastUpdated(timestamp);
	        resourceDTO.setResourceType(resource.getClass().getSimpleName());
	        
	        // Set the resource id and meta fields.
	        resource.setId(id(logicalId));
	        Meta meta = resource.getMeta();
	        if (meta == null) {
	            meta = objectFactory.createMeta();
	        }
	        meta.setVersionId(id(Integer.toString(newVersionNumber)));
	        meta.setLastUpdated(lastUpdated);
	        resource.setMeta(meta);
	        
	        // Serialize and compress the Resource
	        GZIPOutputStream zipStream = new GZIPOutputStream(stream);
	        FHIRUtil.write(resource, Format.JSON, zipStream, false);
	        zipStream.finish();
	        resourceDTO.setData(stream.toByteArray());
	        zipStream.close();
	        
	        // Store search parameters BEFORE persisting the resource. Stored procedures that are called in the DAO layer depend upon
	        // the search parameters being persisted first (in a global temporary table).
	        this.storeSearchParameters(resource, resourceDTO);
	        
	        // Persist the Resource DTO.
	        this.getResourceDao().setPersistenceContext(context);
	        this.getResourceDao().insert(resourceDTO);
	        if (log.isLoggable(Level.FINE)) {
	        	log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' id=" + resourceDTO.getId()
	        				+ ", version=" + resourceDTO.getVersionId());
	        }
		}
		catch(FHIRPersistenceResourceNotFoundException e) {
			throw e;
		}
        catch(FHIRPersistenceFKVException e) {
            String msg = "Unexpected FK Violation while performing an update operation.";
            log.log(Level.SEVERE, msg, e);
            log.log(Level.SEVERE, this.performCacheDiagnostics());
            throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
        }
		catch(Throwable e) {
			String msg = "Unexpected error while performing an update operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		finally {
	        log.exiting(CLASSNAME, METHODNAME);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistence#search(com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext, java.lang.Class)
	 */
	@Override
	public List<Resource> search(FHIRPersistenceContext context, Class<? extends Resource> resourceType)
			throws FHIRPersistenceException {
		final String METHODNAME = "search";
		log.entering(CLASSNAME, METHODNAME);
		
		List<Resource> resources = new ArrayList<Resource>();
	    FHIRSearchContext searchContext = context.getSearchContext();
	    JDBCNormalizedQueryBuilder queryBuilder;
	    List<Long> sortedIdList;
	    List<com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource> unsortedResultsList;
	    int searchResultCount = 0;
	    int pageSize;
	    int lastPageNumber;
	    SqlQueryData countQuery;
	    SqlQueryData query;
	    	    
	    try {
	    	queryBuilder = new JDBCNormalizedQueryBuilder((ParameterNormalizedDAO)this.getParameterDao(),
	    	                                              (ResourceNormalizedDAO)this.getResourceDao());
	         
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
	            
	             
	            if (searchResultCount > 0) {
	            	query = queryBuilder.buildQuery(resourceType, searchContext);
	            	
	                if (searchContext.hasSortParameters()) {
	                	sortedIdList = this.resourceDao.searchForIds(query);
	                	resources = this.buildSortedFhirResources(context, resourceType, sortedIdList);
	                }
	                else {
	                	unsortedResultsList = this.getResourceDao().search(query);
	                	resources = this.convertResourceDTOList(unsortedResultsList, resourceType);
	                }  
	            }
	        }
	    }
	    catch(Throwable e) {
			String msg = "Unexpected error while performing a search operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	    
		return resources;
	}
	
	protected ParameterDAO getParameterDao() {
		return this.parameterDao;
	}

	protected ResourceNormalizedDAO getResourceDao() {
		return resourceDao;
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistence#delete(com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext, Class<? extends Resource> resourceType, java.lang.String)
	 */
	@Override
	public Resource delete(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException {
		final String METHODNAME = "delete";
		log.entering(CLASSNAME, METHODNAME);
		
		com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource existingResourceDTO = null;
		Resource existingResource = null;
		Resource deletedResource = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		try {
	        existingResourceDTO = this.getResourceDao().read(logicalId, resourceType.getSimpleName());
			
			if (existingResourceDTO != null) {
				if (existingResourceDTO.isDeleted()) {
					deletedResource = this.convertResourceDTO(existingResourceDTO, resourceType);
				}
				else {
				    existingResource = this.convertResourceDTO(existingResourceDTO, resourceType);
				    
					// Create a soft-delete Resource instance with minimal data
	            	deletedResource = FHIRUtil.createResource(resourceType);
	            	deletedResource.setMeta(objectFactory.createMeta());
	            	deletedResource.setId(objectFactory.createId().withValue(logicalId));
	            	
	            	// If replication info is required, add the value of the patientId, siteId, and subjectId extensions 
                    // to the RepInfo and to the deletedResource.
                    if (this.resourceDao.isRepInfoRequired()) {
                        ReplicationUtil.addExtensionDataToRepInfo(context, existingResource);
                        ReplicationUtil.addExtensionDataToResource(existingResource, deletedResource);
                    }
	
	                int newVersionNumber = existingResourceDTO.getVersionId() + 1;
	                Instant lastUpdated = instant(System.currentTimeMillis());
	
	                // Update the soft-delete resource to reflect the new version and lastUpdated values.
	                deletedResource.getMeta().setVersionId(id(String.valueOf(newVersionNumber)));
	                deletedResource.getMeta().setLastUpdated(lastUpdated);
	
	                // Create a new Resource DTO instance to represent the deleted version.
	                com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = new com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource();
	                resourceDTO.setLogicalId(logicalId);
	                resourceDTO.setVersionId(newVersionNumber);
	                
	                // Serialize and compress the Resource
	    	        GZIPOutputStream zipStream = new GZIPOutputStream(stream);
	    	        FHIRUtil.write(deletedResource, Format.JSON, zipStream, false);
	    	        zipStream.finish();
	    	        resourceDTO.setData(stream.toByteArray());
	    	        zipStream.close();
	    	        
	                Timestamp timestamp = FHIRUtilities.convertToTimestamp(lastUpdated.getValue());
	                resourceDTO.setLastUpdated(timestamp);
	                resourceDTO.setResourceType(resourceType.getSimpleName());
	                resourceDTO.setDeleted(true);
	
	                // Persist the logically deleted Resource DTO.
	                this.getResourceDao().setPersistenceContext(context);
	                this.getResourceDao().insert(resourceDTO);
	                if (log.isLoggable(Level.FINE)) {
	                	log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' id=" + resourceDTO.getId()
	                        		+ ", version=" + resourceDTO.getVersionId());
	                }
				}
            }
			        
	        return deletedResource;
		}
		catch(FHIRPersistenceFKVException e) {
            String msg = "Unexpected FK Violation while performing a delete operation.";
            log.log(Level.SEVERE, msg, e);
            log.log(Level.SEVERE, this.performCacheDiagnostics());
            throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
        }
		catch(Throwable e) {
			String msg = "Unexpected error while performing a delete operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		finally {
	        log.exiting(CLASSNAME, METHODNAME);
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistence#read(com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext, java.lang.Class, java.lang.String)
	 */
	@Override
	public Resource read(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId)
							throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
		final String METHODNAME = "read";
		log.entering(CLASSNAME, METHODNAME);
		
		Resource resource = null;
		com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = null;
				
		try {
			resourceDTO = this.getResourceDao().read(logicalId, resourceType.getSimpleName());
			if (resourceDTO != null && resourceDTO.isDeleted() && !context.includeDeleted()) {
                throw new FHIRPersistenceResourceDeletedException("Resource '" + resourceType.getSimpleName() + "/" + logicalId + "' is deleted.");
			}
			resource = this.convertResourceDTO(resourceDTO, resourceType);
		}
		catch(FHIRPersistenceResourceDeletedException e) {
			throw e;
		}
		catch(Throwable e) {
			String msg = "Unexpected error while performing a read operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return resource;
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistence#history(com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext, java.lang.Class, java.lang.String)
	 */
	@Override
	public List<Resource> history(FHIRPersistenceContext context, Class<? extends Resource> resourceType,
			String logicalId) throws FHIRPersistenceException {
		final String METHODNAME = "history";
		log.entering(CLASSNAME, METHODNAME);
		
		List<Resource> resources = new ArrayList<>();
		List<com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource> resourceDTOList;
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
	        	for (com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource resourceDTO : resourceDTOList) {
	        		if (resourceDTO.isDeleted()) {
	        			deletedResourceVersions.putIfAbsent(logicalId, new ArrayList<Integer>());
	        			deletedResourceVersions.get(logicalId).add(resourceDTO.getVersionId());
	        		}
	        	}
	        	log.log(Level.FINE, "deletedResourceVersions=" + deletedResourceVersions);
	        	resources = this.convertResourceDTOList(resourceDTOList, resourceType);
	        } 
		}
		catch(Throwable e) {
			String msg = "Unexpected error while performing a history operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return resources;
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistence#vread(com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext, java.lang.Class, java.lang.String, java.lang.String)
	 */
	@Override
	public Resource vread(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId, String versionId) 
						throws FHIRPersistenceException {
		final String METHODNAME = "vread";
		log.entering(CLASSNAME, METHODNAME);
		
		Resource resource = null;
		com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = null;
		int version;
										
		try {
			version = Integer.parseInt(versionId);
			resourceDTO = this.getResourceDao().versionRead(logicalId, resourceType.getSimpleName(), version);
			if (resourceDTO != null && resourceDTO.isDeleted()) {
                throw new FHIRPersistenceResourceDeletedException("Resource '" + resourceType.getSimpleName() + "/" + logicalId + "' version " + versionId + " is deleted.");
			}
			resource = this.convertResourceDTO(resourceDTO, resourceType);
		}
		catch(FHIRPersistenceResourceDeletedException e) {
			throw e;
		}
		catch (NumberFormatException e) {
	        throw new FHIRPersistenceException("Invalid version id specified for vread operation: " + versionId);
	    }
		catch(Throwable e) {
			String msg = "Unexpected error while performing a version read operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, Response.Status.INTERNAL_SERVER_ERROR, e);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return resource;
	}
	
	/**
	 * Returns a List of Resource DTOs corresponding to the passed list of Resource IDs.
	 * @param resourceType The type of resource being queried.
	 * @param sortedIdList A sorted list of Resource IDs.
	 * @return List - A list of ResourceDTOs
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	@Override
	protected List<com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource> getResourceDTOs(
			Class<? extends Resource> resourceType, List<Long> sortedIdList) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		 
		return this.getResourceDao().searchByIds(resourceType.getSimpleName(), sortedIdList);
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
                throw new FHIRPersistenceException("Failed to acquire TrxSynchRegistry service", Status.INTERNAL_SERVER_ERROR, e);
            }
        }
        
        return this.trxSynchRegistry;
    }

}
