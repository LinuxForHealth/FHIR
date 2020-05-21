/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import static com.ibm.fhir.schema.control.FhirSchemaConstants.PK;

import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.database.utils.model.GroupPrivilege;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Privilege;
import com.ibm.fhir.database.utils.model.Table;

/**
 * Encapsulates the generation of the Liberty OAuth 2.0 schema artifacts
 * @see <a href="https://www.ibm.com/support/knowledgecenter/SSD28V_liberty/com.ibm.websphere.wlp.core.doc/ae/twlp_oauth_dbs.html">
 *      https://www.ibm.com/support/knowledgecenter/SSD28V_liberty/com.ibm.websphere.wlp.core.doc/ae/twlp_oauth_dbs.html</a>
 */
public class OAuthSchemaGenerator {
    public static final String OAUTH_SCHEMANAME = "FHIR_OAUTH";

    // The schema holding all the oauth client data
    private final String schemaName;

    private static final String OAUTH20CACHE = "OAUTH20CACHE";
    private static final String OAUTH20CLIENTCONFIG = "OAUTH20CLIENTCONFIG";
    private static final String OAUTH20CONSENTCACHE = "OAUTH20CONSENTCACHE";

    private static final String LOOKUPKEY = "LOOKUPKEY";
    private static final String UNIQUEID = "UNIQUEID";
    private static final String COMPONENTID = "COMPONENTID";
    private static final String TYPE = "TYPE";
    private static final String SUBTYPE = "SUBTYPE";
    private static final String CREATEDAT = "CREATEDAT";
    private static final String LIFETIME = "LIFETIME";
    private static final String EXPIRES = "EXPIRES";
    private static final String TOKENSTRING = "TOKENSTRING";
    private static final String CLIENTID = "CLIENTID";
    private static final String USERNAME = "USERNAME";
    private static final String SCOPE = "SCOPE";
    private static final String REDIRECTURI = "REDIRECTURI";
    private static final String STATEID = "STATEID";
    private static final String EXTENDEDFIELDS = "EXTENDEDFIELDS";

    private static final String CLIENTSECRET = "CLIENTSECRET";
    private static final String DISPLAYNAME = "DISPLAYNAME";
    private static final String ENABLED = "ENABLED";
    private static final String CLIENTMETADATA = "CLIENTMETADATA";

    private static final String USERID = "USERID";
    private static final String PROVIDERID = "PROVIDERID";

    /**
     * sets up the OAuth Schema with the given schema.
     * @param schemaName
     */
    public OAuthSchemaGenerator(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Create the tables needed by the Liberty OAuthProvider databaseStore
     * @param model
     */
    public void buildOAuthSchema(PhysicalDataModel model) {
        addCacheTable(model);
        addClientConfigTable(model);
        addConsentCacheTable(model);
    }

    protected List<GroupPrivilege> generateGroupPrivilege(){
        return Arrays.asList(
            new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.SELECT),
            new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.INSERT),
            new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.DELETE),
            new GroupPrivilege(FhirSchemaConstants.FHIR_USER_GRANT_GROUP, Privilege.UPDATE));
    }

    protected void addCacheTable(PhysicalDataModel model) {
        Table cache = Table.builder(schemaName, OAUTH20CACHE)
                .addVarcharColumn(  LOOKUPKEY, 256,  false)
                .addVarcharColumn(   UNIQUEID, 128,  false)
                .addVarcharColumn(COMPONENTID, 256,  false)
                .addVarcharColumn(       TYPE, 64,   false)
                .addVarcharColumn(    SUBTYPE, 64,   true)
                .addBigIntColumn(   CREATEDAT,       true)
                .addIntColumn(       LIFETIME,       true)
                .addBigIntColumn(     EXPIRES,       true)
                .addVarcharColumn(TOKENSTRING, 2048, false)
                .addVarcharColumn(   CLIENTID, 64,   false)
                .addVarcharColumn(   USERNAME, 64,   false)
                .addVarcharColumn(      SCOPE, 512,  false)
                .addVarcharColumn(REDIRECTURI, 2048, true)
                .addVarcharColumn(    STATEID, 64,   false)
                .addClobColumn(EXTENDEDFIELDS, false, "{}")
                .addPrimaryKey(PK + LOOKUPKEY, LOOKUPKEY)
                .addUniqueIndex(OAUTH20CACHE + "_" + EXPIRES, EXPIRES) // ASC is the default
                .addPrivileges(generateGroupPrivilege())
                .build(model);

        model.addTable(cache);
        model.addObject(cache);
    }

    protected void addClientConfigTable(PhysicalDataModel model) {
        Table clientConfig = Table.builder(schemaName, OAUTH20CLIENTCONFIG)
                .addVarcharColumn( COMPONENTID, 256,  false)
                .addVarcharColumn(    CLIENTID, 256,  false)
                .addVarcharColumn(CLIENTSECRET, 256,  true)
                .addVarcharColumn( DISPLAYNAME, 256,  false)
                .addVarcharColumn( REDIRECTURI, 2048, true)
                .addIntColumn(         ENABLED,       true)
                .addClobColumn( CLIENTMETADATA,       false, "{}")
                .addPrimaryKey(PK + "COMPIDCLIENTID", COMPONENTID, CLIENTID)
                .addPrivileges(generateGroupPrivilege())
                .build(model);
        model.addTable(clientConfig);
        model.addObject(clientConfig);
    }

    protected void addConsentCacheTable(PhysicalDataModel model) {
        Table consentCache = Table.builder(schemaName, OAUTH20CONSENTCACHE)
                .addVarcharColumn(    CLIENTID, 256,  false)
                .addVarcharColumn(      USERID, 256,  true)
                .addVarcharColumn(  PROVIDERID, 256,  false)
                .addVarcharColumn(       SCOPE, 1024, false)
                .addBigIntColumn(      EXPIRES,       true)
                .addClobColumn( EXTENDEDFIELDS,       false, "{}")
                .addPrivileges(generateGroupPrivilege())
                .build(model);
        model.addTable(consentCache);
        model.addObject(consentCache);
    }
}