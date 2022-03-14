/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;


/**
 * Defines a SQL VIEW object
 */
public class View extends BaseObject {

    // The select clause defining the view
    private final String selectClause;

    /**
     * Protected constructor
     * @param schemaName
     * @param objectName
     * @param objectType
     * @param version
     */
    protected View(String schemaName, String objectName, int version, String selectClause, 
        Collection<IDatabaseObject> dependencies, Map<String,String> tags,
        Collection<GroupPrivilege> privileges, List<Migration> migrations) {
        super(schemaName, objectName, DatabaseObjectType.VIEW, version);
        this.selectClause = selectClause;
        
        
        addDependencies(dependencies.stream().filter(x -> x != null).collect(Collectors.toList()));

        addTags(tags);
        privileges.forEach(p -> p.addToObject(this));
    }

    @Override
    public void apply(IDatabaseAdapter target, SchemaApplyContext context) {
        target.createOrReplaceView(getSchemaName(), getObjectName(), this.selectClause);
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target, SchemaApplyContext context) {
        apply(target, context);
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropView(getSchemaName(), getObjectName());
    }

    @Override
    public void visit(DataModelVisitor v) {
        // NOP
    }

    @Override
    public void visitReverse(DataModelVisitor v) {
        // NOP
    }

    /**
     * Create a new Builder instance which can be used to create immutable
     * instances of {@link View}
     * @return
     */
    public static Builder builder(String schemaName, String viewName) {
        return new Builder(schemaName, viewName);
    }

    /**
     * Fluent builder for {@link View}
     */
    public static class Builder extends VersionedSchemaObject {

        // other dependencies of this view (the tables it depends on)
        private Set<IDatabaseObject> dependencies = new HashSet<>();

        // The selectClause used to define the view
        private String selectClause;

        // A map of tags
        private Map<String,String> tags = new HashMap<>();

        // Privileges to be granted on this view
        private List<GroupPrivilege> privileges = new ArrayList<>();
        
        /**
         * Private constructor to force creation through factory method
         * @param schemaName
         * @param viewName
         */
        private Builder(String schemaName, String viewName) {
            super(schemaName, viewName);
        }

        /**
         * Setter for the fromClause used to build the view
         * @param fromClause
         */
        public Builder setSelectClause(String selectClause) {
            this.selectClause = selectClause;
            return this;
        }

        /**
         * Set the version
         * @param v
         * @return
         */
        public Builder setVersion(int v) {
            setVersionValue(v);
            return this;
        }

        /**
         * @param tagName
         * @param tagValue
         * @return
         */
        public Builder addTag(String tagName, String tagValue) {
            this.tags.put(tagName, tagValue);
            return this;
        }

        public Builder addPrivilege(String groupName, Privilege p) {
            this.privileges.add(new GroupPrivilege(groupName, p));
            return this;
        }

        /**
         * Add the collection of group privileges to this view
         * @param gps
         * @return
         */
        public Builder addPrivileges(Collection<GroupPrivilege> gps) {
            this.privileges.addAll(gps);
            return this;
        }

        /**
         * Add the {@link IDatabaseObject} as a dependency for this view
         * @param obj
         * @return
         */
        public Builder addDependency(IDatabaseObject obj) {
            this.dependencies.add(obj);
            return this;
        }

        @Override
        public Builder addMigration(Migration... migration) {
            super.addMigration(migration);
            return this;
        }

        /**
         * Construct an immutable instance of a {@link View} from the current state
         * of this builder
         * @return
         */
        public View build() {
            return new View(getSchemaName(), getObjectName(), version, selectClause,
                dependencies, tags, privileges, migrations);
        }
    }
}
