/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

/**
 * Group Privilege 
 */
public class GroupPrivilege {
    private final String groupName;
    private final Privilege privilege;
    
    public GroupPrivilege(String groupName, Privilege p) {
        this.groupName = groupName;
        this.privilege = p;
    }

    /**
     * Add this privilege to the object
     * @param obj
     */
    public void addToObject(BaseObject obj) {
        obj.addPrivilege(this.groupName, this.privilege);
    }
}
