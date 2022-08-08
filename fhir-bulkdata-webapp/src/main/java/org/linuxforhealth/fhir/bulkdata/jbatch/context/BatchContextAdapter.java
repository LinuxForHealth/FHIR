/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.context;

import java.util.Properties;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.BulkDataContext;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.OperationFields;

/**
 * Adapts the JobContext or the StepContext
 *
 * @implNote There is annoying error in the batch with JWT's cdi
 * This avoids the error, and consistently treats the job parameters. Previously
 * we had multiple patterns of reading parameters.
 */
public class BatchContextAdapter {
    private Properties props = null;

    public BatchContextAdapter(StepContext stepCtx) {
        this.props = stepCtx.getProperties();
    }

    public BatchContextAdapter(JobContext jobCtx) {
        this.props = jobCtx.getProperties();
    }

    public BatchContextAdapter(Properties props) {
        this.props = props;
    }

    public BulkDataContext getStepContextForExportPartitionMapper() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        source(ctx);
        ctx.setFhirResourceTypes(props.getProperty(OperationFields.FHIR_RESOURCE_TYPES));
        return ctx;
    }


    public BulkDataContext getStepContextForSystemChunkReader() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        source(ctx);
        audit(ctx);
        search(ctx);
        systemWrite(ctx);
        format(ctx);
        return ctx;
    }

    public BulkDataContext getStepContextForPatientChunkReader() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        source(ctx);
        audit(ctx);
        search(ctx);
        systemWrite(ctx);
        ctx.setPartitionResourceType(props.getProperty(OperationFields.PARTITION_RESOURCETYPE));
        format(ctx);
        return ctx;
    }

    public BulkDataContext getStepContextForGroupChunkReader() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        source(ctx);
        audit(ctx);
        search(ctx);
        systemWrite(ctx);
        ctx.setPartitionResourceType(props.getProperty(OperationFields.PARTITION_RESOURCETYPE));
        ctx.setGroupId(props.getProperty(OperationFields.FHIR_SEARCH_PATIENT_GROUP_ID));
        format(ctx);
        return ctx;
    }

    public BulkDataContext getJobContextForExportJobListener() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        source(ctx);
        ctx.setDataSourcesInfo(props.getProperty(OperationFields.FHIR_DATA_SOURCES_INFO));
        return ctx;
    }

    public BulkDataContext getStepContextForSystemChunkWriter() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        source(ctx);
        audit(ctx);
        systemWrite(ctx);
        format(ctx);
        return ctx;
    }

    public BulkDataContext getStepContextForFastResourceWriter() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        source(ctx);
        audit(ctx);
        systemWrite(ctx);
        search(ctx);
        format(ctx);
        return ctx;
    }

    private void search(BulkDataContext ctx) {
        int pageSize = ConfigurationFactory.getInstance().getCorePageSize();
        ctx.setFhirSearchPageSize(pageSize);
        ctx.setFhirTypeFilters(props.getProperty(OperationFields.FHIR_SEARCH_TYPE_FILTERS));
        ctx.setFhirSearchToDate(props.getProperty(OperationFields.FHIR_SEARCH_TO_DATE));
        ctx.setFhirSearchFromDate(props.getProperty(OperationFields.FHIR_SEARCH_FROM_DATE));
    }

    private void format(BulkDataContext ctx) {
        ctx.setFhirExportFormat(props.getProperty(OperationFields.FHIR_EXPORT_FORMAT));
    }

    private void systemWrite(BulkDataContext ctx) {
        ctx.setCosBucketPathPrefix(props.getProperty(OperationFields.COS_BUCKET_PATH_PREFIX));
        ctx.setPartitionResourceType(props.getProperty(OperationFields.PARTITION_RESOURCETYPE));
    }

    public BulkDataContext getStepContextForImportChunkReader() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        source(ctx);
        addImport(ctx);
        return ctx;
    }

    public BulkDataContext getStepContextForImportChunkWriter() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        audit(ctx);
        source(ctx);
        addImport(ctx);
        return ctx;
    }

    public BulkDataContext getStepContextForPartitionMapperForImport() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        audit(ctx);
        source(ctx);
        datasourceInfo(ctx);
        return ctx;
    }

    public BulkDataContext getStepContextForImportPartitionCollector() {
        BulkDataContext ctx = new BulkDataContext();
        context(ctx);
        source(ctx);
        ctx.setCosBucketPathPrefix(props.getProperty(OperationFields.COS_BUCKET_PATH_PREFIX));
        return ctx;
    }

    public BulkDataContext getImportJobListener() {
        BulkDataContext ctx = new BulkDataContext();
        datasourceInfo(ctx);
        return ctx;
    }

    private void datasourceInfo(BulkDataContext ctx) {
        ctx.setDataSourcesInfo(props.getProperty(OperationFields.FHIR_DATA_SOURCES_INFO));
    }

    private void context(BulkDataContext ctx) {
        ctx.setTenantId(props.getProperty(OperationFields.FHIR_TENANT_ID));
        ctx.setDatastoreId(props.getProperty(OperationFields.FHIR_DATASTORE_ID));
        ctx.setIncomingUrl(props.getProperty(OperationFields.FHIR_INCOMING_URL));
    }

    private void audit(BulkDataContext ctx) {
        ctx.setUsers(props.getProperty(OperationFields.FHIR_REQUESTING_USER));
    }

    private void source(BulkDataContext ctx) {
        ctx.setSource(props.getProperty(OperationFields.FHIR_BULKDATA_SOURCE));
        ctx.setOutcome(props.getProperty(OperationFields.FHIR_BULKDATA_OUTCOME));
    }

    private void addImport(BulkDataContext ctx) {
        ctx.setPartitionResourceType(props.getProperty(OperationFields.PARTITION_RESOURCETYPE));
        ctx.setImportPartitionWorkitem(props.getProperty(OperationFields.PARTITION_WORKITEM));
        ctx.setDataSourceStorageType(props.getProperty(OperationFields.FHIR_IMPORT_STORAGE_TYPE));
    }
}