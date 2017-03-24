/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ResourceNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ReferenceDataCache;

/**
 * This Data Access Object extends the "basic" implementation to provide functionality specific to the "normalized"
 * relational schema.
 * @author markd
 *
 */
public class ResourceDAONormalizedImpl extends ResourceDAOBasicImpl implements ResourceNormalizedDAO {
	private static final Logger log = Logger.getLogger(ResourceDAONormalizedImpl.class.getName());
	private static final String CLASSNAME = ResourceDAONormalizedImpl.class.getName(); 
	
	private static final String SQL_READ = "SELECT DATA, ID, LAST_UPDATED, LOGICAL_ID, RESOURCE_TYPE_ID, VERSION_ID FROM FHIR_RESOURCE R WHERE R.LOGICAL_ID = ? AND R.RESOURCE_TYPE_ID = ? AND R.VERSION_ID = " +
            "(SELECT MAX(R2.VERSION_ID) FROM FHIR_RESOURCE R2 WHERE R2.LOGICAL_ID = ? AND R2.RESOURCE_TYPE_ID = ?)";
	
	private static final String SQL_VERSION_READ = "SELECT DATA, ID, LAST_UPDATED, LOGICAL_ID, RESOURCE_TYPE_ID, VERSION_ID FROM FHIR_RESOURCE R WHERE R.LOGICAL_ID = ? AND R.RESOURCE_TYPE_ID = ? AND R.VERSION_ID = ?";
	
	private static final String SQL_READ_ALL_RESOURCE_TYPES = "SELECT ID, RESOURCE_TYPE FROM FHIR_RESOURCE_TYPE";
	
	private static final String SQL_INSERT = "INSERT INTO FHIR_RESOURCE (RESOURCE_TYPE_ID, LOGICAL_ID, VERSION_ID, DATA, LAST_UPDATED)" +
			" VALUES(?,?,?,?,?)";
	
	//protected final String SQL_SEARCH_BY_ID = "SELECT DATA, ID, LAST_UPDATED, LOGICAL_ID, RESOURCE_TYPE_ID, VERSION_ID FROM RESOURCE R WHERE R.ID IN ";
	
	// The following History related SQL strings are used by the history related methods in the superclass.
	private static final String SQL_HISTORY = "SELECT DATA, ID, LAST_UPDATED, LOGICAL_ID, RESOURCE_TYPE_ID, VERSION_ID FROM FHIR_RESOURCE R WHERE R.LOGICAL_ID = ? "
												+ "ORDER BY R.VERSION_ID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	private static final String SQL_HISTORY_COUNT = "SELECT COUNT(*) FROM FHIR_RESOURCE R WHERE R.LOGICAL_ID = ?";
	private static final String SQL_HISTORY_FROM_DATETIME = "SELECT DATA, ID, LAST_UPDATED, LOGICAL_ID, RESOURCE_TYPE_ID, VERSION_ID FROM FHIR_RESOURCE R WHERE R.LOGICAL_ID = ? AND R.LAST_UPDATED >= ?" + 
			 													" ORDER BY R.VERSION_ID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
	private static final String SQL_HISTORY_FROM_DATETIME_COUNT = "SELECT COUNT(*) FROM FHIR_RESOURCE R WHERE R.LOGICAL_ID = ? AND R.LAST_UPDATED >= ?";

	
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
	 */
	public ResourceDAONormalizedImpl() {
		super();
	}
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections based on the passed database type specific properties.
	 * @param dbProperties
	 */
	public ResourceDAONormalizedImpl(Properties dbProperties) {
		super(dbProperties);
	}

	@Override
	public Resource insert(Resource resource)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "insert";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		long resourceTypeId;
				
		try {
			resourceTypeId = ReferenceDataCache.getResourceTypeId(resource.getResourceType());
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, resourceTypeId);
			stmt.setString(2, resource.getLogicalId());
			stmt.setInt(3, resource.getVersionId());
			stmt.setBytes(4, resource.getData());
			stmt.setTimestamp(5, resource.getLastUpdated());
					
			stmt.executeUpdate();
			resultSet = stmt.getGeneratedKeys();
			if (resultSet.next()) {
				resource.setId(resultSet.getLong(1));
				log.fine("Succesfully inserted Resource. id=" + resource.getId());
			}
			else
			{
				throw new FHIRPersistenceDataAccessException("Generated key not returned after new Resource inserted.");
			}
		}
		catch(FHIRPersistenceDataAccessException | FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure inserting Resource.", e);
		}
		finally {
			this.cleanup(resultSet, stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return resource;
		
	}

	@Override
	public Resource read(String logicalId, String resourceType)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "read";
		log.entering(CLASSNAME, METHODNAME);
		
		Resource resource = null;
		List<Resource> resources;
		Long resourceTypeId;
				
		try {
			resourceTypeId = ReferenceDataCache.getResourceTypeId(resourceType);
			resources = this.runQuery(SQL_READ, logicalId, resourceTypeId, logicalId, resourceTypeId);
			if (!resources.isEmpty()) {
				resource = resources.get(0);
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		return resource;
	}

	@Override
	public Resource versionRead(String logicalId, String resourceType, int versionId)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "versionRead";
		log.entering(CLASSNAME, METHODNAME);
		
		Resource resource = null;
		List<Resource> resources;
		long resourceTypeId;
				
		try {
			resourceTypeId = ReferenceDataCache.getResourceTypeId(resourceType);
			resources = this.runQuery(SQL_VERSION_READ, logicalId, resourceTypeId, versionId);
			if (!resources.isEmpty()) {
				resource = resources.get(0);
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
				
		return resource;
	}


	@Override
	public Map<String, Long> readAllResourceTypes() throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "readAllResourceTypes";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String resourceTypeName;
		Long resourceTypeId;
		Map<String, Long> resourceTypeIdMap = new HashMap<>();
		String errMsg = "Failure retrieving all Resource types.";
				
		try {
			
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_READ_ALL_RESOURCE_TYPES);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				resourceTypeName = resultSet.getString("RESOURCE_TYPE");
				resourceTypeId = resultSet.getLong("ID");
				resourceTypeIdMap.put(resourceTypeName, resourceTypeId);
			}
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
				
		return resourceTypeIdMap;
	}
	
	/**
	 * Creates and returns a Resource DTO based on the contents of the passed ResultSet
	 * @param resultSet A ResultSet containing FHIR persistent object data.
	 * @return Resource - A Resource DTO
	 * @throws FHIRPersistenceDataAccessException
	 */
	protected Resource createDTO(ResultSet resultSet) throws FHIRPersistenceDataAccessException {
		final String METHODNAME = "createDTO";
		log.entering(CLASSNAME, METHODNAME);
		
		Resource resource = new Resource();
		
		try {
			resource.setData(resultSet.getBytes("DATA"));
			resource.setId(resultSet.getLong("ID"));
			resource.setLastUpdated(resultSet.getTimestamp("LAST_UPDATED"));
			resource.setLogicalId(resultSet.getString("LOGICAL_ID"));
			resource.setResourceType(ReferenceDataCache.getResourceTypeName(resultSet.getLong("RESOURCE_TYPE_ID")));
			resource.setVersionId(resultSet.getInt("VERSION_ID"));
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure creating Resource DTO.", e);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return resource;
	}

	protected String getSqlHistoryCount() {
		return SQL_HISTORY_COUNT;
	}
	
	protected String getSqlHistoryFromDateTimeCount() {
		return SQL_HISTORY_FROM_DATETIME_COUNT;
	}
	
	protected String getSqlHistory() {
		return SQL_HISTORY;
	}
	
	protected String getSqlHistoryFromDateTime() {
		return SQL_HISTORY_FROM_DATETIME;
	}

}
