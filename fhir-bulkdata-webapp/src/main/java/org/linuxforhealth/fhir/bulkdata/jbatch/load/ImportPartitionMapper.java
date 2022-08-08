/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.load;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.linuxforhealth.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.exception.FHIRLoadException;
import org.linuxforhealth.fhir.bulkdata.load.partition.transformer.PartitionSourceTransformerFactory;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.BulkDataContext;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.BulkDataSource;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.OperationFields;

@Dependent
public class ImportPartitionMapper implements PartitionMapper {
    private static final String CLASSNAME = ImportPartitionMapper.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    @Inject
    JobContext jobCtx;

    public ImportPartitionMapper() {
        // No Operation
    }

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        long executionId = -1;
        try {
            executionId = jobCtx.getExecutionId();
            JobExecution jobExecution = BatchRuntime.getJobOperator().getJobExecution(executionId);

            BatchContextAdapter ctxAdapter = new BatchContextAdapter(jobExecution.getJobParameters());

            BulkDataContext ctx = ctxAdapter.getStepContextForPartitionMapperForImport();

            // Register the context to get the right configuration.
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

            List<BulkDataSource> bdSources = PartitionSourceTransformerFactory.transformToSources(ctx.getSource(), ctx.getDataSourcesInfo());

            PartitionPlanImpl pp = new PartitionPlanImpl();
            pp.setPartitions(bdSources.size());
            pp.setThreads(Math.min(adapter.getCoreMaxPartitions(), bdSources.size()));
            Properties[] partitionProps = new Properties[bdSources.size()];

            int propCount = 0;
            for (BulkDataSource fhirDataSource : bdSources) {
                Properties p = new Properties();
                p.setProperty(OperationFields.PARTITION_WORKITEM, fhirDataSource.getUrl());
                p.setProperty(OperationFields.PARTITION_RESOURCETYPE, fhirDataSource.getType());
                p.setProperty(OperationFields.PARTITION_MATRIX, fhirDataSource.getOriginalLocation());
                partitionProps[propCount++] = p;
            }
            pp.setPartitionProperties(partitionProps);
            return pp;
        } catch (FHIRLoadException e) {
            jobCtx.setExitStatus("FAILED_BAD_SOURCE");
            logger.log(Level.WARNING, "Import PartitionMapper source[" + executionId + "] - " + e.getMessage(), e);
            throw e;
        } catch (FHIRException e) {
            logger.log(Level.SEVERE, "Import PartitionMapper.mapPartitions during job[" + executionId + "] - " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Import PartitionMapper.mapPartitions during job[" + executionId + "]", e);
            throw e;
        }
    }
}