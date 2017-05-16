/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.derby.tools.ij;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.persistence.jdbc.SchemaType;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * This class contains bootstrapping code for the Derby Database.
 * @author hjagann
 *
 */
public class DerbyBootstrapper {
	private static final Logger log = Logger.getLogger(DerbyBootstrapper.class.getName());
	private static final String className = DerbyBootstrapper.class.getName();
	
	/**
	 * 
	 * Install Java Stored Procedure jar file into derby DB
	 */
	protected static boolean runScript(String commands, Connection connection) {
		if (log.isLoggable(Level.FINER)) {
			log.entering(className, "runScript");
		}
		log.fine("Attempting to install java stored procedure JAR file into derby database");
		InputStream inStream = new BufferedInputStream( new ReaderInputStream( new StringReader(commands)));
	    try { 
	        int result  = ij.runScript(connection,inStream,"UTF-8",System.out,"UTF-8"); 
	        return (result==0); 
	    } catch (IOException e) { 
	        return false; 
	    } finally { 
	        if(inStream!=null) { 
	            try { 
	            	inStream.close(); 
	            } 
	            catch (IOException e) { 
	            } 
	        } 
	        if (log.isLoggable(Level.FINER)) {
    			log.exiting(className, "runScript");
    		}
	    } 
	} 

	/**
     * Bootstraps the FHIR database.
     * A DB connection is acquired. Then, a Liquibase changelog is run against the database to define tables.
     * Note: this is only done for Derby databases.
     * @param schemaType - An enumerated value indicating the schema type to be used when bootstrapping the database.
     * @param derbySProcJarLocation - The derby java stored procedures .JAR file location.
	 * @throws SQLException 
     */
    public static void bootstrapDb(DataSource fhirDb, SchemaType schemaType, String derbySProcJarLocation) throws SQLException  {
    	if (log.isLoggable(Level.FINER)) {
			log.entering(className, "bootstrapDb");
		}
    	
    	Connection connection = null;
    	String dbDriverName;
    	Database database;
		Liquibase liquibase;
		String changeLogPath = null;
    	    	
    	try {
    	    String msg = "Performing derby db bootstrapping for tenant-id '" + FHIRRequestContext.get().getTenantId() 
                    + "', datastore-id '" + FHIRRequestContext.get().getDataStoreId() + "'.";
    	    log.info(msg);
    	    log.finer("DataSource: " + fhirDb.toString());
			connection = fhirDb.getConnection();
			log.finer("Connection: " + connection.toString());
			
			dbDriverName = connection.getMetaData().getDriverName();
						
			if (dbDriverName != null && dbDriverName.contains("Derby")) {
				switch (schemaType) {
				case BASIC: 	 changeLogPath = "liquibase/ddl/derby/basic-schema/fhirserver.derby.basic.xml";
								 break;
				case NORMALIZED: changeLogPath = "liquibase/ddl/derby/normalized-schema/fhirserver.derby.normalized.xml";
								 break;
				}
				database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
				StringBuilder sb = new StringBuilder();
				sb.append("CALL sqlj.install_jar('" + derbySProcJarLocation + "', 'APP.FhirDerbySProcs',0);");
				sb.append("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.classpath','APP.FhirDerbySProcs');");
				if(schemaType == SchemaType.NORMALIZED) {
					if(runScript(sb.toString(), connection)) {
						liquibase = new Liquibase(changeLogPath, new ClassLoaderResourceAccessor(), database);
						liquibase.update((Contexts)null);
					}
				} else if(schemaType == SchemaType.BASIC) {
					liquibase = new Liquibase(changeLogPath, new ClassLoaderResourceAccessor(), database);
					liquibase.update((Contexts)null);
				}
			}
    	}
    	catch(Throwable e) {
    		String msg = "Encountered an exception while bootstrapping the FHIR database";
		    log.log(Level.SEVERE, msg, e);
    	}
    	finally {
//    	    if (connection != null) {
//    	        log.finer("Committing and closing connection...");
//    	        connection.commit();
//    	        connection.close();
//    	    }
    		if (log.isLoggable(Level.FINER)) {
    			log.exiting(className, "bootstrapDb");
    		}
    	}
    }
}
