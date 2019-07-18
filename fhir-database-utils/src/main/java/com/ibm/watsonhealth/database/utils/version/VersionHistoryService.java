/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.version;

import java.util.Collection;
import java.util.Map;

import com.ibm.watsonhealth.database.utils.api.DataAccessException;
import com.ibm.watsonhealth.database.utils.api.IConnectionProvider;
import com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTarget;
import com.ibm.watsonhealth.database.utils.api.ITransaction;
import com.ibm.watsonhealth.database.utils.api.ITransactionProvider;
import com.ibm.watsonhealth.database.utils.api.IVersionHistoryService;

/**
 * Encapsulation of the transaction needed to read the version history table
 * @author rarnold
 *
 */
public class VersionHistoryService implements IVersionHistoryService {
    
    private final String schemaName;
    
    // Allows us to start a transaction
    private ITransactionProvider transactionProvider;

    // The target representing the database we want to interact with
    private IDatabaseAdapter target;

    // The map of version history information loaded from the database
    private Map<String,Integer> versionHistoryMap;
    
    public VersionHistoryService(String schemaName) {
        this.schemaName = schemaName;
    }
    
    /**
     * For injection of the {@link IConnectionProvider}
     * @param cp
     */
    public void setTransactionProvider(ITransactionProvider tp) {
        this.transactionProvider = tp;
    }
   
    /**
     * For injection of the {@link IDatabaseTarget}
     * @param tgt
     */
    public void setTarget(IDatabaseAdapter tgt) {
        this.target = tgt;
    }

    /**
     * Fetch the version history map for the given schema. This then
     * becomes the reference for everything we try to apply going
     * forward. This assumes, of course. If someone else comes along
     * and tries to update the schema after we read this map, then
     * it's possible (likely) that we'll try to apply a change that
     * is no longer required, which is probably going to end in tears.
     * But it's OK. A second attempt will see that the change has
     * already been applied, so won't try again.
     * @param schemaName
     * @return
     */
    public void init() {
        // defend
        if (this.transactionProvider == null) {
            throw new IllegalStateException("Programming error - must setTransactionProvider before calling init");
        }
        
        if (this.target == null) {
            throw new IllegalStateException("Programming error - must setTarget before calling init");
        }
        
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                // Note how we don't care about connections here...that is all
                // hidden inside the target adapter implementation
                GetLatestVersionDAO dao = new GetLatestVersionDAO(schemaName);
                this.versionHistoryMap = target.runStatement(dao);
            }
            catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }


    /**
     * Insert all the entries in the versionHistoryMap. This must be called in the
     * context of an existing transaction
     * @param schemaName
     * @param versionHistoryMap
     */
    public void insertVersionHistoriesInTx(Collection<TypeNameVersion> versionHistories) {
        for (TypeNameVersion tuple: versionHistories) {
            insertVersionHistoryInTx(tuple.getType(), tuple.getName(), tuple.getVersion());
        }
    }
    
    /**
     * Insert the version history for the objectType/objectName/version.
     * @param objectType
     * @param objectName
     * @param version
     */
    public void insertVersionHistoryInTx(String objectType, String objectName, int version) {
        AddVersionDAO dao = new AddVersionDAO(schemaName, objectType, objectName, version);
        target.runStatement(dao);
    }
    
    /**
     * Insert all the entries in the versionHistoryMap in a new transaction (useful
     * for testing).
     * @param schemaName
     * @param versionHistoryMap
     */
    public void insertVersionHistory(Collection<TypeNameVersion> versionHistories) {
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                insertVersionHistoriesInTx(versionHistories);
            }
            catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Factory method for creating a {@link TypeNameVersion} tuple
     * @param type
     * @param name
     * @param version
     * @return
     */
    public static TypeNameVersion createTypeNameVersion(String type, String name, int version) {
        return new TypeNameVersion(type, name, version);
    }
    
    public static class TypeNameVersion {
        private final String type;
        private final String name;
        private final int version;
        
        private TypeNameVersion(String type, String name, int version) {
            this.type = type;
            this.name = name;
            this.version = version;
        }
        
        private String getType() {
            return this.type;
        }
        
        private String getName() {
            return this.name;
        }
        
        private int getVersion() {
            return this.version;
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IVersionHistoryService#addVersion(java.lang.String, java.lang.String, int)
     */
    @Override
    public void addVersion(String objectType, String objectName, int version) {
        insertVersionHistoryInTx(objectType, objectName, version);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IVersionHistoryService#applies(java.lang.String, java.lang.String, int)
     */
    @Override
    public boolean applies(String objectType, String objectName, int changeVersion) {
        String key = objectType + ":" + objectName;
        Integer currentVersion = this.versionHistoryMap.get(key);
        return currentVersion == null || currentVersion < changeVersion;
    }
}
