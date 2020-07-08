/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

import com.ibm.fhir.config.DefaultFHIRConfigProvider;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.spec.test.IExampleProcessor;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.persistence.util.ResourceFingerprintVisitor;

/**
 * Reads R4 example resources and performs a sequence of persistance
 * operations against each
 *
 */
public class R4JDBCExamplesProcessor implements IExampleProcessor {
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    // the list of operations we apply to reach resource
    private final List<ITestResourceOperation> operations = new ArrayList<>();

    // The persistence API
    private final FHIRPersistence persistence;

    // supplier of FHIRPersistenceContext for normal create/update/delete ops
    private final Supplier<FHIRPersistenceContext> persistenceContextSupplier;

    // supplier of FHIRPersistenceContext for history operations
    private final Supplier<FHIRPersistenceContext> historyContextSupplier;

    private Properties configProps;
    private IConnectionProvider cp;
    private String tenantName;
    private String tenantKey;
    private ITransactionProvider transactionProvider;

    /**
     * Public constructor. Uses a defaultlist of operations
     * @param persistence
     */
    public R4JDBCExamplesProcessor(FHIRPersistence persistence,
            Supplier<FHIRPersistenceContext> persistenceContextSupplier,
            Supplier<FHIRPersistenceContext> historyContextSupplier) {
        this.persistence = persistence;
        this.persistenceContextSupplier = persistenceContextSupplier;
        this.historyContextSupplier = historyContextSupplier;

        // The sequence of operations we want to apply to each resource
        operations.add(new CreateOperation());
        operations.add(new ReadOperation());
        operations.add(new UpdateOperation());
        operations.add(new UpdateOperation());
        operations.add(new ReadOperation());
        operations.add(new VReadOperation());
        operations.add(new HistoryOperation(3)); // create+update+update = 3 versions
        operations.add(new DeleteOperation());
        operations.add(new DeleteOperation());
        operations.add(new HistoryOperation(4)); // create+update+update+delete = 4 versions
    }

    /**
     * Create a processor with a specific set of operations
     * @param persistence
     * @param persistenceContextSupplier
     * @param historyContextSupplier
     * @param operations
     */
    public R4JDBCExamplesProcessor(FHIRPersistence persistence, Supplier<FHIRPersistenceContext> persistenceContextSupplier,
            Supplier<FHIRPersistenceContext> historyContextSupplier, Collection<ITestResourceOperation> operations) {
        this.persistence = persistence;
        this.persistenceContextSupplier = persistenceContextSupplier;
        this.historyContextSupplier = historyContextSupplier;

        // The sequence of operations we want to apply to each resource
        this.operations.addAll(operations);
    }

    /**
     * Create a processor with a specific set of operations
     * @param persistence
     * @param persistenceContextSupplier
     * @param historyContextSupplier
     * @param operations
     */
    public R4JDBCExamplesProcessor(Collection<ITestResourceOperation> operations,
            Properties configProps, IConnectionProvider cp, String tenantName, String tenantKey, ITransactionProvider transactionProvider) {
        this.persistence = null;
        this.persistenceContextSupplier = null;
        this.historyContextSupplier = null;
        this.configProps = configProps;
        this.cp = cp;
        this.tenantName = tenantName;
        this.tenantKey = tenantKey;
        this.transactionProvider = transactionProvider;

        // The sequence of operations we want to apply to each resource
        this.operations.addAll(operations);
    }

    /**
     * Configure the property group to inject the tenantKey, which is the only attribute
     * required for this scenario
     * @param configProvider
     * @throws Exception
     */
    protected void configure(TestFHIRConfigProvider configProvider) throws Exception {

        final String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/default";

        // The bare necessities we need to provide to the persistence layer in this case
        final String jsonString = " {" + 
                "    \"tenantKey\": \"" + this.tenantKey + "\"," + 
                "    \"type\": \"db2\"," + 
                "    \"multitenant\": true" + 
                "}";
        
        try (JsonReader reader = JSON_READER_FACTORY.createReader(new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8)))) {
            JsonObject jsonObj = reader.readObject();
            PropertyGroup pg = new PropertyGroup(jsonObj);
            configProvider.addPropertyGroup(dsPropertyName, pg);
        }
    }

    @Override
    public void process(String jsonFile, Resource resource) throws Exception {

        // Use a custom configuration provider so that we can support passing the tenant key
        // without having to create a fhir-server-configuration.json file
        TestFHIRConfigProvider configProvider = new TestFHIRConfigProvider(new DefaultFHIRConfigProvider());
        configure(configProvider);

        // Initialize the test context. As we run through the sequence of operations, each
        // one will update the context which will then be used by the next operation
        TestContext context;
        FHIRPersistence tmpPersistence = null;
        if (this.persistence == null) {
            // Set up the FHIRRequestContext on this thread so that the persistence layer
            // can configure itself for this tenant
            if (this.tenantName != null && this.tenantKey != null) {
                FHIRRequestContext rc = FHIRRequestContext.get();
                
                // tenantKey is accessed by the persistence layer using the
                // TenantKeyStrategy implementation
                rc.setTenantId(this.tenantName);
            }

            tmpPersistence = new FHIRPersistenceJDBCImpl(this.configProps, this.cp, configProvider);

            context = new TestContext(tmpPersistence,
                    () -> createPersistenceContext(),
                    () -> createHistoryPersistenceContext());
        } else {
            context = new TestContext(this.persistence, this.persistenceContextSupplier, this.historyContextSupplier);
        }

        // Clear the id so that we can set it ourselves. The ids from the examples are reused
        // even though the resources are supposed to be different
        resource = resource.toBuilder().id(null).build();
        context.setResource(resource);

        // Compute a reference fingerprint of the resource before we perform
        // any operations. We can use this fingerprint to check that operations
        // don't distort the resource in any way
        ResourceFingerprintVisitor v = new ResourceFingerprintVisitor();
        resource.accept(resource.getClass().getSimpleName(), v);
        context.setOriginalFingerprint(v.getSaltAndHash());

        if (this.persistence == null) {
            try (ITransaction tx = this.transactionProvider.getTransaction()) {
                // ITestResourceOperation#process throws Exception, which precludes the
                // use of forEach here...so going old-school keeps it simpler
                for (ITestResourceOperation op: operations) {
                    op.process(context);
                }
            }
        } else {
            for (ITestResourceOperation op: operations) {
                op.process(context);
            }
        }
    }
    
    /**
     * Create a new {@link FHIRPersistenceContext} for the test
     * @return
     */
    protected FHIRPersistenceContext createPersistenceContext() {
        try {
            return FHIRPersistenceContextFactory.createPersistenceContext(null);
        }
        catch (Exception x) {
            // because we're used as a lambda supplier, need to avoid a checked exception
            throw new IllegalStateException(x);
        }
    }

    /**
     * Create a new FHIRPersistenceContext configure with a FHIRHistoryContext
     * @return
     */
    protected FHIRPersistenceContext createHistoryPersistenceContext() {
        try {
            FHIRHistoryContext fhc = FHIRPersistenceContextFactory.createHistoryContext();
            return FHIRPersistenceContextFactory.createPersistenceContext(null, fhc);
        }
        catch (Exception x) {
            // because we're used as a lambda supplier, need to avoid a checked exception
            throw new IllegalStateException(x);
        }
    }
}
