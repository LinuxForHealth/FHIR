/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import org.linuxforhealth.fhir.config.FHIRConfigProvider;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.spec.test.IExampleProcessor;
import org.linuxforhealth.fhir.model.visitor.ResourceFingerprintVisitor;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceTransaction;
import org.linuxforhealth.fhir.persistence.context.FHIRHistoryContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import org.linuxforhealth.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import org.linuxforhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import org.linuxforhealth.fhir.search.util.SearchHelper;

/**
 * Reads R4 example resources and performs a sequence of persistance
 * operations against each
 *
 */
public class R4JDBCExamplesProcessor implements IExampleProcessor {

    // the list of operations we apply to reach resource
    private final List<ITestResourceOperation> operations = new ArrayList<>();

    // The persistence API
    private final FHIRPersistence persistence;

    // supplier of FHIRPersistenceContext for normal create/update/delete ops
    private final Supplier<FHIRPersistenceContext> persistenceContextSupplier;

    // supplier of FHIRPersistenceContext for history operations
    private final Supplier<FHIRPersistenceContext> historyContextSupplier;

    // a shared searchHelper to use while processing resources
    private final SearchHelper searchHelper;

    private Properties configProps;
    private IConnectionProvider cp;
    private String tenantName;
    private String tenantKey;
    private ITransactionProvider transactionProvider;

    // Adapter used by the persistence layer to access certain fhir-server-config properties
    private final FHIRConfigProvider configProvider;

    private final FHIRPersistenceJDBCCache cache;

    /**
     * Public constructor. Uses a defaultlist of operations
     * @param persistence
     * @param persistenceContextSupplier
     * @param historyContextSupplier
     * @param configProvider
     */
    public R4JDBCExamplesProcessor(FHIRPersistence persistence,
            Supplier<FHIRPersistenceContext> persistenceContextSupplier,
            Supplier<FHIRPersistenceContext> historyContextSupplier,
            FHIRConfigProvider configProvider, FHIRPersistenceJDBCCache cache) {
        this.persistence = persistence;
        this.persistenceContextSupplier = persistenceContextSupplier;
        this.historyContextSupplier = historyContextSupplier;
        this.configProvider = configProvider;
        this.cache = cache;
        this.searchHelper = new SearchHelper();

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

        // Use a custom configuration provider so that we can support passing the tenant key
        // without having to create a fhir-server-configuration.json file
    }

    /**
     * Create a processor with a specific set of operations
     * @param persistence
     * @param persistenceContextSupplier
     * @param historyContextSupplier
     * @param operations
     * @param configProvider
     */
    public R4JDBCExamplesProcessor(FHIRPersistence persistence, Supplier<FHIRPersistenceContext> persistenceContextSupplier,
            Supplier<FHIRPersistenceContext> historyContextSupplier, Collection<ITestResourceOperation> operations,
            FHIRConfigProvider configProvider, FHIRPersistenceJDBCCache cache) {
        this.persistence = persistence;
        this.persistenceContextSupplier = persistenceContextSupplier;
        this.historyContextSupplier = historyContextSupplier;
        this.configProvider = configProvider;
        this.cache = cache;
        this.searchHelper = new SearchHelper();

        // The sequence of operations we want to apply to each resource
        this.operations.addAll(operations);
    }

    /**
     * Create a processor with a specific set of operations
     * @param operations
     * @param configProps
     * @param cp
     * @param tenantName
     * @param tenantKey
     * @param transactionProvider
     * @param configProvider
     * @param cache
     */
    public R4JDBCExamplesProcessor(Collection<ITestResourceOperation> operations,
            Properties configProps, IConnectionProvider cp, String tenantName, String tenantKey,
            ITransactionProvider transactionProvider, FHIRConfigProvider configProvider, FHIRPersistenceJDBCCache cache) {
        this.persistence = null;
        this.persistenceContextSupplier = null;
        this.historyContextSupplier = null;
        this.configProps = configProps;
        this.cp = cp;
        this.tenantName = tenantName;
        this.tenantKey = tenantKey;
        this.transactionProvider = transactionProvider;
        this.configProvider = configProvider;
        this.cache = cache;
        this.searchHelper = new SearchHelper();

        // The sequence of operations we want to apply to each resource
        this.operations.addAll(operations);
    }

    @Override
    public void process(String jsonFile, Resource resource) throws Exception {

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

            tmpPersistence = new FHIRPersistenceJDBCImpl(this.configProps, this.cp, configProvider, this.cache, searchHelper);

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

            // ITestResourceOperation#process throws Exception, which precludes the
            // use of forEach here...so going old-school keeps it simpler
            for (ITestResourceOperation op: operations) {
                FHIRPersistenceTransaction tx = context.getPersistence().getTransaction();
                tx.begin();
                try {
                    op.process(context);
                } catch (Throwable t) {
                    tx.setRollbackOnly();
                    throw t;
                } finally {
                    // will do a commit, or rollback if tx.setRollbackOnly() has been set
                    tx.end();
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
