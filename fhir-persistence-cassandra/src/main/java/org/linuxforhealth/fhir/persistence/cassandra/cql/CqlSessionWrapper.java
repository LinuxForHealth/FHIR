/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.cassandra.cql;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.context.DriverContext;
import com.datastax.oss.driver.api.core.metadata.Metadata;
import com.datastax.oss.driver.api.core.metrics.Metrics;
import com.datastax.oss.driver.api.core.session.Request;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

/**
 * A wrapper so that we can intercept {@link #close()} calls
 */
public class CqlSessionWrapper implements CqlSession {
    private static final Logger logger = Logger.getLogger(CqlSessionWrapper.class.getName());
    
    // the actual CqlSession we delegate calls to
    private final CqlSession delegate;

    /**
     * Public constructor
     * @param delegate
     */
    public CqlSessionWrapper(CqlSession delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Metadata getMetadata() {
        return delegate.getMetadata();
    }

    @Override
    public boolean isSchemaMetadataEnabled() {
        return delegate.isSchemaMetadataEnabled();
    }

    @Override
    public CompletionStage<Metadata> setSchemaMetadataEnabled(Boolean newValue) {
        return delegate.setSchemaMetadataEnabled(newValue);
    }

    @Override
    public CompletionStage<Metadata> refreshSchemaAsync() {
        return delegate.refreshSchemaAsync();
    }

    @Override
    public CompletionStage<Boolean> checkSchemaAgreementAsync() {
        return delegate.checkSchemaAgreementAsync();
    }

    @Override
    public DriverContext getContext() {
        return delegate.getContext();
    }

    @Override
    public Optional<CqlIdentifier> getKeyspace() {
        return delegate.getKeyspace();
    }

    @Override
    public Optional<Metrics> getMetrics() {
        return delegate.getMetrics();
    }

    @Override
    public <RequestT extends Request, ResultT> ResultT execute(RequestT request, GenericType<ResultT> resultType) {
        return delegate.execute(request, resultType);
    }

    @Override
    public CompletionStage<Void> closeFuture() {
        logger.warning("closeFuture called on session wrapper");
        return delegate.closeFuture();
    }

    @Override
    public CompletionStage<Void> closeAsync() {
        logger.warning("closeAsync called on session wrapper");
        return delegate.closeAsync();
    }

    @Override
    public CompletionStage<Void> forceCloseAsync() {
        logger.warning("forceCloseAsync called on session wrapper");
        return delegate.forceCloseAsync();
    }

    @Override
    public void close() {
        // Intercept the close call so that try-with-resource patterns don't actually
        // close the session. Only the DatasourceSessions object should close the
        // the real CqlSession.
    }
}