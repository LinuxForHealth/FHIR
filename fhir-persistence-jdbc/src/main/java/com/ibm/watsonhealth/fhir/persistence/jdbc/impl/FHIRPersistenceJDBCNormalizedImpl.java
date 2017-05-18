/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.impl;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_UPDATE_CREATE_ENABLED;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.id;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.instant;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ResourceNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.FHIRDbDAOBasicImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ParameterDAONormalizedImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ResourceDAONormalizedImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.JDBCNormalizedQueryBuilder;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.SqlQueryData;
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
	
	
	private ResourceNormalizedDAO resourceDao;
	private ParameterNormalizedDAO parameterDao;
		

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
		this.userTransaction = retrieveUserTransaction(TXN_JNDI_NAME);
		this.resourceDao = new ResourceDAONormalizedImpl();
		this.parameterDao = new ParameterDAONormalizedImpl();
							
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
		this.parameterDao = new ParameterDAONormalizedImpl(this.getManagedConnection());
		
		log.exiting(CLASSNAME, METHODNAME);
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistence#create(com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext, com.ibm.watsonhealth.fhir.model.Resource)
	 */
	@Override
	public void create(FHIRPersistenceContext context, Resource resource) throws FHIRPersistenceException  {
		final String METHODNAME = "create";
		log.entering(CLASSNAME, METHODNAME);
		
		boolean txnStarted = false;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		String logicalId;
		Connection myConnection = null;
		
		try {
			if (this.getManagedConnection() == null) {
				myConnection = this.createConnection();
				this.getParameterDao().setExternalConnection(myConnection);
				this.getResourceDao().setExternalConnection(myConnection);
			}
	        FHIRUtil.write(resource, Format.XML, stream, false);
	
	        // Start a new txn.
	        if (!isActive()) {
	            begin();
	            txnStarted = true;
	        }
	
	        // Default version is 1 for a brand new FHIR Resource.
	        int newVersionNumber = 1;
	        logicalId = (resource.getId() != null ? resource.getId().getValue() : UUID.randomUUID().toString());
	        log.fine("Creating new FHIR Resource of type '" + resource.getClass().getSimpleName() + "'");
	        // Create the new Resource DTO instance.
	        com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = new com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource();
	        resourceDTO.setLogicalId(logicalId);
	        resourceDTO.setVersionId(newVersionNumber);
	        resourceDTO.setData(FHIRUtilities.gzipCompress(stream.toByteArray()));
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
	        
	        // Store search parameters BEFORE persisting the resource. Stored procedures that are called in the DAO layer depend upon
	        // the search parameters being persisted first (in a global temporary table).
	        this.storeSearchParameters(resource, resourceDTO);
	        
	        // Persist the Resource DTO.
	        this.getResourceDao().insert(resourceDTO);
	        log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' id=" + resourceDTO.getId()
	        + ", version=" + resourceDTO.getVersionId());
	        
	        // Time to commit the changes.
	        if (txnStarted) {
	            commit();
	            txnStarted = false;
	        }
		}
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch(Throwable e) {
			String msg = "Unexpected error while performing a create operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, e);
		}
		finally {
		    // Time to rollback if we still have an active txn that we started.
		    if (txnStarted) {
		        rollback();
		        txnStarted = false;
		    }
		    if (myConnection != null) {
		    	try {
					myConnection.close();
				} 
		    	catch (SQLException e) {
					throw new FHIRPersistenceException("Failure closing DB Conection", e);
				}
		    }
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
		boolean txnStarted = false;
		int newVersionNumber = 1;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Connection myConnection = null;
		
		try {
			if (this.getManagedConnection() == null) {
				myConnection = this.createConnection();
				this.getParameterDao().setExternalConnection(myConnection);
				this.getResourceDao().setExternalConnection(myConnection);
			}
			// Get the current version of the Resource.
			existingResourceDTO = this.getResourceDao().read(logicalId, resourceType.getSimpleName());
	        
	        // If this FHIR Resource doesn't exist and updateCreateEnabled is turned off, throw an exception
	        if (existingResourceDTO == null && !updateCreateEnabled) {
	            String msg = "Resource '" + resourceType.getSimpleName() + "/" + logicalId + " not found.";
	            log.log(Level.SEVERE, msg);
	            throw new FHIRPersistenceResourceNotFoundException(msg);
	        }
	        
	        stream = new ByteArrayOutputStream();
	        FHIRUtil.write(resource, Format.XML, stream, false);
	
	        // Start a new txn.
	        if (!isActive()) {
	            begin();
	            txnStarted = true;
	        }
	        
	        // If the FHIR Resource already exists, then we'll simply bump up the version #, use its logical id,
	        // and remove its Parameter entries.
	        if (existingResourceDTO != null) {
	            newVersionNumber = existingResourceDTO.getVersionId() + 1;
	            log.fine("Updating FHIR Resource '" + existingResourceDTO.getResourceType() + "/" + existingResourceDTO.getLogicalId() + "', version="
	                        + existingResourceDTO.getVersionId());
	        } 
	        
	        // Create the new Resource DTO instance.
	        com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = new com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource();
	        resourceDTO.setLogicalId(logicalId);
	        resourceDTO.setVersionId(newVersionNumber);
	        resourceDTO.setData(FHIRUtilities.gzipCompress(stream.toByteArray()));
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
	        
	        // Store search parameters BEFORE persisting the resource. Stored procedures that are called in the DAO layer depend upon
	        // the search parameters being persisted first (in a global temporary table).
	        this.storeSearchParameters(resource, resourceDTO);
	        
	        // Persist the Resource DTO.
	        this.getResourceDao().insert(resourceDTO);
	        log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' id=" + resourceDTO.getId()
	        + ", version=" + resourceDTO.getVersionId());
	                
	        // Time to commit the changes.
	        if (txnStarted) {
	            commit();
	            txnStarted = false;
	        }
		}
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch(Throwable e) {
			String msg = "Unexpected error while performing an update operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, e);
		}
		finally {
	        // Time to rollback if we still have an active txn that we started.
	        if (txnStarted) {
	            rollback();
	            txnStarted = false;
	        }
	        if (myConnection != null) {
		    	try {
					myConnection.close();
				} 
		    	catch (SQLException e) {
					throw new FHIRPersistenceException("Failure closing DB Conection", e);
				}
		    }
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
	    //List<Long> sortedIdList;
	    List<com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource> unsortedResultsList;
	    int searchResultCount = 0;
	    int pageSize;
	    int lastPageNumber;
	    SqlQueryData countQuery;
	    SqlQueryData query;
	    Connection myConnection = null;
	    
	    try {
	    	if (this.getManagedConnection() == null) {
				myConnection = this.createConnection();
				this.getParameterDao().setExternalConnection(myConnection);
				this.getResourceDao().setExternalConnection(myConnection);
			}
	        if (searchContext.hasSortParameters()) {
	        	throw new FHIRPersistenceNotSupportedException("Search result sorting is not supported at this time.");
	        	//queryBuilder = new JDBCNormalizedSortQueryBuilder();
	        }
	         
	        queryBuilder = new JDBCNormalizedQueryBuilder((ParameterNormalizedDAO)this.getParameterDao());
	         
	        countQuery = queryBuilder.buildCountQuery(resourceType, searchContext);
	        if (countQuery != null) {
	        	searchResultCount = this.getResourceDao().searchCount(countQuery);
	        	log.fine("searchResultCount = " + searchResultCount);
	        	searchContext.setTotalCount(searchResultCount);
	            pageSize = searchContext.getPageSize();
	            lastPageNumber = (int) ((searchResultCount + pageSize - 1) / pageSize);
	            searchContext.setLastPageNumber(lastPageNumber);
	            
	             
	            if (searchResultCount > 0) {
	            	query = queryBuilder.buildQuery(resourceType, searchContext);
	            	
	                //if (searchContext.hasSortParameters()) {
	                	//sortedIdList = this.resourceDao.searchForIds(queryString);
	                	//resources = this.buildSortedFhirResources(context, resourceType, sortedIdList);
	               // }
	                //else {
	                	unsortedResultsList = this.getResourceDao().search(query);
	                	resources = this.convertResourceDTOList(unsortedResultsList, resourceType);
	                //}  
	            }
	        }
	    }
	    catch(FHIRPersistenceException e) {
			throw e;
		}
		catch(Throwable e) {
			String msg = "Unexpected error while performing a search operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, e);
		}
		finally {
			if (myConnection != null) {
		    	try {
					myConnection.close();
				} 
		    	catch (SQLException e) {
					throw new FHIRPersistenceException("Failure closing DB Conection", e);
				}
		    }
			log.exiting(CLASSNAME, METHODNAME);
		}
	    
		return resources;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceTransaction#begin()
	 */
	@Override
	public void begin() throws FHIRPersistenceException {
		final String METHODNAME = "begin";
		log.entering(CLASSNAME, METHODNAME);
		
			
		try {
			if (userTransaction != null) {
				userTransaction.begin();
			}
			else if (this.getManagedConnection() != null) {
				this.getManagedConnection().setAutoCommit(false);
			}
		}
        catch (Throwable e) {
            String msg = "An unexpected error occurred while starting a transaction.";
            log.log(Level.SEVERE, msg, e);
            throw new FHIRPersistenceException(msg);
        }
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceTransaction#commit()
	 */
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
            String msg = "An unexpected error occurred while committing a transaction.";
            log.log(Level.SEVERE, msg, e);
            throw new FHIRPersistenceException(msg);
        }
        finally {
        	log.exiting(CLASSNAME, METHODNAME);
        }

	}
	
	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceTransaction#rollback()
	 */
	@Override
	public void rollback() throws FHIRPersistenceException {
		final String METHODNAME = "rollback";
		log.entering(CLASSNAME, METHODNAME);
		
        try {
        	if (userTransaction != null) {
                userTransaction.rollback();
            } else if (this.getManagedConnection() != null) {
            	this.getManagedConnection().rollback();
			}
        } 
        catch (Throwable e) {
            String msg = "An unexpected error occurred while rolling back a transaction.";
            log.log(Level.SEVERE, msg, e);
            throw new FHIRPersistenceException(msg);
        }
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
		Resource deletedResource = null;
		boolean txnStarted = false;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Connection myConnection = null;
		
		try {
			if (this.getManagedConnection() == null) {
				myConnection = this.createConnection();
				this.getParameterDao().setExternalConnection(myConnection);
				this.getResourceDao().setExternalConnection(myConnection);
			}

	        // Start a new txn.
	        if (!isActive()) {
	            begin();
	            txnStarted = true;
	        }
			existingResourceDTO = this.getResourceDao().read(logicalId, resourceType.getSimpleName());
			
			// If the resource DTO exists, then convert it to a FHIR resource.
            if (existingResourceDTO != null) {
                deletedResource = this.convertResourceDTO(existingResourceDTO, resourceType);

                // If the resource was not already deleted, then update it to be logically deleted.
                if (!existingResourceDTO.isDeleted()) {
                    int newVersionNumber = existingResourceDTO.getVersionId() + 1;
                    Instant lastUpdated = instant(System.currentTimeMillis());

                    // Update the returned resource to reflect the new version and lastUpdated values.
                    deletedResource.getMeta().setVersionId(id(String.valueOf(newVersionNumber)));
                    deletedResource.getMeta().setLastUpdated(lastUpdated);

                    // Create a new Resource DTO instance to represent the deleted version.
                    com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = new com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource();
                    resourceDTO.setLogicalId(logicalId);
                    resourceDTO.setVersionId(newVersionNumber);
                    FHIRUtil.write(deletedResource, Format.XML, stream, false);
                    resourceDTO.setData(FHIRUtilities.gzipCompress(stream.toByteArray()));
                    Timestamp timestamp = FHIRUtilities.convertToTimestamp(lastUpdated.getValue());
                    resourceDTO.setLastUpdated(timestamp);
                    resourceDTO.setResourceType(resourceType.getSimpleName());
                    resourceDTO.setDeleted(true);

                    // Persist the logically deleted Resource DTO.
                    this.getResourceDao().insert(resourceDTO);
                    log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' id=" + resourceDTO.getId()
                            + ", version=" + resourceDTO.getVersionId());
                }
            }
			
			// Time to commit the changes.
	        if (txnStarted) {
	            commit();
	            txnStarted = false;
	        }
	        
	        return deletedResource;
		}
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch(Throwable e) {
			String msg = "Unexpected error while performing a delete operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, e);
		}
		finally {
	        // Time to rollback if we still have an active txn that we started.
	        if (txnStarted) {
	            rollback();
	            txnStarted = false;
	        }
	        if (myConnection != null) {
		    	try {
					myConnection.close();
				} 
		    	catch (SQLException e) {
					throw new FHIRPersistenceException("Failure closing DB Conection", e);
				}
		    }
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
		Connection myConnection = null;
		
		try {
			if (this.getManagedConnection() == null) {
				myConnection = this.createConnection();
				this.getParameterDao().setExternalConnection(myConnection);
				this.getResourceDao().setExternalConnection(myConnection);
			}

			resourceDTO = this.getResourceDao().read(logicalId, resourceType.getSimpleName());
			if (resourceDTO != null && resourceDTO.isDeleted() && !context.includeDeleted()) {
                throw new FHIRPersistenceResourceDeletedException("Resource '" + resourceType.getSimpleName() + "/" + logicalId + "' is deleted.");
			}
			resource = this.convertResourceDTO(resourceDTO, resourceType);
		}
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch(Throwable e) {
			String msg = "Unexpected error while performing a read operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, e);
		}
		finally {
			if (myConnection != null) {
		    	try {
					myConnection.close();
				} 
		    	catch (SQLException e) {
					throw new FHIRPersistenceException("Failure closing DB Conection", e);
				}
		    }
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
		Connection myConnection = null;
		
		try {
			if (this.getManagedConnection() == null) {
				myConnection = this.createConnection();
				this.getParameterDao().setExternalConnection(myConnection);
				this.getResourceDao().setExternalConnection(myConnection);
			}
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
	        	resources = this.convertResourceDTOList(resourceDTOList, resourceType);
	        } 
		}
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch(Throwable e) {
			String msg = "Unexpected error while performing a history operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, e);
		}
		finally {
			if (myConnection != null) {
		    	try {
					myConnection.close();
				} 
		    	catch (SQLException e) {
					throw new FHIRPersistenceException("Failure closing DB Conection", e);
				}
		    }
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
		Connection myConnection = null;
								
		try {
			if (this.getManagedConnection() == null) {
				myConnection = this.createConnection();
				this.getParameterDao().setExternalConnection(myConnection);
				this.getResourceDao().setExternalConnection(myConnection);
			}
			version = Integer.parseInt(versionId);
			resourceDTO = this.getResourceDao().versionRead(logicalId, resourceType.getSimpleName(), version);
			if (resourceDTO != null && resourceDTO.isDeleted()) {
                throw new FHIRPersistenceResourceDeletedException("Resource '" + resourceType.getSimpleName() + "/" + logicalId + "' version " + versionId + " is deleted.");
			}
			resource = this.convertResourceDTO(resourceDTO, resourceType);
		}
		catch(FHIRPersistenceException e) {
			throw e;
		}
		catch (NumberFormatException e) {
	        throw new FHIRPersistenceException("Invalid version id specified for vread operation: " + versionId);
	    }
		catch(Throwable e) {
			String msg = "Unexpected error while performing a version read operation.";
	        log.log(Level.SEVERE, msg, e);
	        throw new FHIRPersistenceException(msg, e);
		}
		finally {
			if (myConnection != null) {
		    	try {
					myConnection.close();
				} 
		    	catch (SQLException e) {
					throw new FHIRPersistenceException("Failure closing DB Conection", e);
				}
		    }
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return resource;
	}

}
