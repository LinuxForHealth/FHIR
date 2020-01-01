/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.task.api.ITaskCollector;

public class ActionBean {

    // Properties accumulated as we parse args and read configuration files
    private Properties properties = new Properties();

    // The schema we will use for all the FHIR data tables
    private String schemaName;

    // The schema used for administration of tenants
    private String adminSchemaName = "FHIR_ADMIN";

    // Arguments requesting we drop the objects from the schema
    private boolean dropSchema = false;
    private boolean dropAdmin = false;
    private boolean confirmDrop = false;
    private boolean updateSchema = false;
    private boolean updateProc = false;
    private boolean checkCompatibility = false;
    private boolean createFhirSchemas = false;

    // By default, the dryRun option is OFF, and FALSE
    // When overridden, it simulates the actions. 
    private Boolean dryRun = Boolean.FALSE;

    // The database user we will grant tenant data access privileges to
    private String grantTo;

    // Tenant management
    private boolean allocateTenant;
    private boolean dropTenant;
    private String tenantName;
    private boolean testTenant;
    private String tenantKey;

    private int tenantId;

    private TenantStatus status;

    // The tenant name for when we want to add a new tenant key
    private String addKeyForTenant;

    // The connection pool and transaction provider to support concurrent operations
    private int maxConnectionPoolSize = FhirSchemaConstants.DEFAULT_POOL_SIZE / 2;

    // This utility is designed to work with a DB2 database
    private IDatabaseTranslator translator = new Db2Translator();

    boolean compatible = false;

    private VersionHistoryService vhs;

    private PhysicalDataModel pdm;

    private int exitStatus = 0;

    public PhysicalDataModel getPhysicalDataModel() {
        return pdm;
    }

    public void setPhysicalDataModel(PhysicalDataModel pdm) {
        this.pdm = pdm;
    }

    public int getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(int exitStatus) {
        this.exitStatus = exitStatus;
    }

    private ITaskCollector collector;

    public ITaskCollector getCollector() {
        return collector;
    }

    public void setCollector(ITaskCollector collector) {
        this.collector = collector;
    }

    public VersionHistoryService getVersionHistoryService() {
        return vhs;
    }

    public void setVersionHistoryService(VersionHistoryService vhs) {
        this.vhs = vhs;
    }

    public IDatabaseTranslator getTranslator() {
        return translator;
    }

    public void setTranslator(IDatabaseTranslator translator) {
        this.translator = translator;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getAdminSchemaName() {
        return adminSchemaName;
    }

    public void setAdminSchemaName(String adminSchemaName) {
        this.adminSchemaName = adminSchemaName;
    }

    public boolean isDropSchema() {
        return dropSchema;
    }

    public void setDropSchema(boolean dropSchema) {
        this.dropSchema = dropSchema;
    }

    public boolean isDropAdmin() {
        return dropAdmin;
    }

    public void setDropAdmin(boolean dropAdmin) {
        this.dropAdmin = dropAdmin;
    }

    public boolean isConfirmDrop() {
        return confirmDrop;
    }

    public void setConfirmDrop(boolean confirmDrop) {
        this.confirmDrop = confirmDrop;
    }

    public boolean isUpdateSchema() {
        return updateSchema;
    }

    public void setUpdateSchema(boolean updateSchema) {
        this.updateSchema = updateSchema;
    }

    public boolean isUpdateProc() {
        return updateProc;
    }

    public void setUpdateProc(boolean updateProc) {
        this.updateProc = updateProc;
    }

    public boolean isCheckCompatibility() {
        return checkCompatibility;
    }

    public void setCheckCompatibility(boolean checkCompatibility) {
        this.checkCompatibility = checkCompatibility;
    }

    public boolean isCreateFhirSchemas() {
        return createFhirSchemas;
    }

    public void setCreateFhirSchemas(boolean createFhirSchemas) {
        this.createFhirSchemas = createFhirSchemas;
    }

    public Boolean getDryRun() {
        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getGrantTo() {
        return grantTo;
    }

    public void setGrantTo(String grantTo) {
        this.grantTo = grantTo;
    }

    public boolean isAllocateTenant() {
        return allocateTenant;
    }

    public void setAllocateTenant(boolean allocateTenant) {
        this.allocateTenant = allocateTenant;
    }

    public boolean isDropTenant() {
        return dropTenant;
    }

    public void setDropTenant(boolean dropTenant) {
        this.dropTenant = dropTenant;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public boolean isTestTenant() {
        return testTenant;
    }

    public void setTestTenant(boolean testTenant) {
        this.testTenant = testTenant;
    }

    public String getTenantKey() {
        return tenantKey;
    }

    public void setTenantKey(String tenantKey) {
        this.tenantKey = tenantKey;
    }

    public String getAddKeyForTenant() {
        return addKeyForTenant;
    }

    public void setAddKeyForTenant(String addKeyForTenant) {
        this.addKeyForTenant = addKeyForTenant;
    }

    public int getMaxConnectionPoolSize() {
        return maxConnectionPoolSize;
    }

    public void setMaxConnectionPoolSize(int maxConnectionPoolSize) {
        this.maxConnectionPoolSize = maxConnectionPoolSize;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public TenantStatus getStatus() {
        return status;
    }

    public void setStatus(TenantStatus status) {
        this.status = status;
    }

    public boolean isCompatible() {
        return compatible;
    }

    public void setCompatible(boolean compatible) {
        this.compatible = compatible;
    }

    /**
     * Read the properties from the given file
     * 
     * @param filename
     */
    public void loadPropertyFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            throw new IllegalArgumentException("The properties file does not exist [" + filename + "]");
        }

        try (InputStream is = new FileInputStream(filename)) {
            properties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Parse the given key=value string and add to the properties being collected
     * 
     * @param pair
     */
    public void addProperty(String pair) {
        String[] kv = pair.split("=");
        if (kv.length == 2) {
            properties.put(kv[0], kv[1]);
        } else {
            throw new IllegalArgumentException("Property must be defined as key=value, not: " + pair);
        }
    }

}