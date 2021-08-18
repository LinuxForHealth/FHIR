/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model.type;

/**
 * Bulk Data Context wraps the Partitions, JobContext details
 */
public class BulkDataContext {

    // BatchProperty(name = OperationFields.FHIR_TENANT_ID)
    String tenantId;

    // BatchProperty(name = OperationFields.FHIR_DATASTORE_ID)
    String datastoreId;

    // BatchProperty(name = OperationFields.FHIR_INCOMING_URL)
    String incomingUrl;

    // BatchProperty(name = OperationFields.FHIR_BULKDATA_SOURCE)
    String source;

    // BatchProperty(name = OperationFields.FHIR_BULKDATA_OUTCOME)
    String outcome;

    // BatchProperty(name = OperationFields.FHIR_IMPORT_STORAGE_TYPE)
    String dataSourceStorageType;

    // BatchProperty(name = OperationFields.PARTITTION_WORKITEM)
    String importPartitionWorkitem;

    // BatchProperty(name = OperationFields.PARTITION_RESOURCETYPE)
    String partitionResourceType;

    // BatchProperty(name = OperationFields.FHIR_REQUESTING_USER)
    String users;

    // BatchProperty(name = OperationFields.FHIR_DATA_SOURCES_INFO)
    String dataSourcesInfo;

    // BatchProperty(name = OperationFields.EXPORT_FHIR_SEARCH_PAGESIZE)
    int fhirSearchPageSize;

    // BatchProperty(name = OperationFields.EXPORT_FHIR_SEARCH_TYPEFILTERS)
    String fhirTypeFilters;

    // BatchProperty(name = OperationFields.EXPORT_FHIR_SEARCH_TODATE)
    String fhirSearchToDate;

    // BatchProperty(name = OperationFields.EXPORT_FHIR_SEARCH_FROMDATE)
    String fhirSearchFromDate;

    // BatchProperty(name = OperationFields.EXPORT_FHIR_FORMAT)
    String fhirExportFormat;

    // BatchProperty(name = OperationFields.EXPORT_COS_OBJECT_PATHPREFIX)
    String cosBucketPathPrefix;

    // BatchProperty(name = OperationFields.FHIR_RESOURCETYPES)
    String fhirResourceTypes;

    // BatchProperty(name = OperationFields.EXPORT_FHIR_SEARCH_PATIENTGROUPID)
    String groupId;

    public BulkDataContext() {
        // No Operation
    }

    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @param tenantId
     *            the tenantId to set
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return the datastoreId
     */
    public String getDatastoreId() {
        return datastoreId;
    }

    /**
     * @param datastoreId
     *            the datastoreId to set
     */
    public void setDatastoreId(String datastoreId) {
        this.datastoreId = datastoreId;
    }

    /**
     * @return the incomingUrl
     */
    public String getIncomingUrl() {
        return incomingUrl;
    }

    /**
     * @param incomingUrl
     *            the incomingUrl to set
     */
    public void setIncomingUrl(String incomingUrl) {
        this.incomingUrl = incomingUrl;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the outcome
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * @param outcome
     *            the outcome to set
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /**
     * @return the dataSourceStorageType
     */
    public String getDataSourceStorageType() {
        return dataSourceStorageType;
    }

    /**
     * @param dataSourceStorageType
     *            the dataSourceStorageType to set
     */
    public void setDataSourceStorageType(String dataSourceStorageType) {
        this.dataSourceStorageType = dataSourceStorageType;
    }

    /**
     * @return the importPartitionWorkitem
     */
    public String getImportPartitionWorkitem() {
        return importPartitionWorkitem;
    }

    /**
     * @param importPartitionWorkitem
     *            the importPartitionWorkitem to set
     */
    public void setImportPartitionWorkitem(String importPartitionWorkitem) {
        this.importPartitionWorkitem = importPartitionWorkitem;
    }

    /**
     * @return the partitionResourceType
     */
    public String getPartitionResourceType() {
        return partitionResourceType;
    }

    /**
     * @param partitionResourceType
     *            the partitionResourceType to set
     */
    public void setPartitionResourceType(String partitionResourceType) {
        this.partitionResourceType = partitionResourceType;
    }

    /**
     * @return the users
     */
    public String getUsers() {
        return users;
    }

    /**
     * @param users
     *            the users to set
     */
    public void setUsers(String users) {
        this.users = users;
    }

    /**
     * @return the dataSourcesInfo
     */
    public String getDataSourcesInfo() {
        return dataSourcesInfo;
    }

    /**
     * @param dataSourcesInfo
     *            the dataSourcesInfo to set
     */
    public void setDataSourcesInfo(String dataSourcesInfo) {
        this.dataSourcesInfo = dataSourcesInfo;
    }

    /**
     * @return the fhirSearchPageSize
     */
    public int getFhirSearchPageSize() {
        return fhirSearchPageSize;
    }

    /**
     * @param fhirSearchPageSize
     *            the fhirSearchPageSize to set
     */
    public void setFhirSearchPageSize(int fhirSearchPageSize) {
        this.fhirSearchPageSize = fhirSearchPageSize;
    }

    /**
     * @return the fhirTypeFilters
     */
    public String getFhirTypeFilters() {
        return fhirTypeFilters;
    }

    /**
     * @param fhirTypeFilters
     *            the fhirTypeFilters to set
     */
    public void setFhirTypeFilters(String fhirTypeFilters) {
        this.fhirTypeFilters = fhirTypeFilters;
    }

    /**
     * @return the fhirSearchToDate
     */
    public String getFhirSearchToDate() {
        return fhirSearchToDate;
    }

    /**
     * @param fhirSearchToDate
     *            the fhirSearchToDate to set
     */
    public void setFhirSearchToDate(String fhirSearchToDate) {
        this.fhirSearchToDate = fhirSearchToDate;
    }

    /**
     * @return the fhirSearchFromDate
     */
    public String getFhirSearchFromDate() {
        return fhirSearchFromDate;
    }

    /**
     * @param fhirSearchFromDate
     *            the fhirSearchFromDate to set
     */
    public void setFhirSearchFromDate(String fhirSearchFromDate) {
        this.fhirSearchFromDate = fhirSearchFromDate;
    }

    /**
     * @return the fhirExportFormat
     */
    public String getFhirExportFormat() {
        return fhirExportFormat;
    }

    /**
     * @param fhirExportFormat
     *            the fhirExportFormat to set
     */
    public void setFhirExportFormat(String fhirExportFormat) {
        this.fhirExportFormat = fhirExportFormat;
    }

    /**
     * @return the cosBucketPathPrefix
     */
    public String getCosBucketPathPrefix() {
        return cosBucketPathPrefix;
    }

    /**
     * @param cosBucketPathPrefix
     *            the cosBucketPathPrefix to set
     */
    public void setCosBucketPathPrefix(String cosBucketPathPrefix) {
        this.cosBucketPathPrefix = cosBucketPathPrefix;
    }

    /**
     * @return the comma-delimited list of FHIR resource types that were requested for export
     */
    public String getFhirResourceTypes() {
        return fhirResourceTypes;
    }

    /**
     * @param fhirResourceType
     *            the comma-delimited list of FHIR resource types that were requested for export
     */
    public void setFhirResourceTypes(String fhirResourceType) {
        this.fhirResourceTypes = fhirResourceType;
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     *            the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "BulkDataContext [tenantId=" + tenantId + ", datastoreId=" + datastoreId + ", incomingUrl=" + incomingUrl + ", source=" + source + ", outcome="
                + outcome + ", dataSourceStorageType=" + dataSourceStorageType + ", importPartitionWorkitem=" + importPartitionWorkitem
                + ", partitionResourceType=" + partitionResourceType + ", users=" + users + ", dataSourcesInfo=" + dataSourcesInfo + ", fhirSearchPageSize="
                + fhirSearchPageSize + ", fhirTypeFilters=" + fhirTypeFilters + ", fhirSearchToDate=" + fhirSearchToDate + ", fhirSearchFromDate="
                + fhirSearchFromDate + ", fhirExportFormat=" + fhirExportFormat + ", cosBucketPathPrefix=" + cosBucketPathPrefix + ", fhirResourceType="
                + fhirResourceTypes + ", groupId=" + groupId + "]";
    }

}