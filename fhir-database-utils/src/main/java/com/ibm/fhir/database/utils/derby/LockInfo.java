/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;


/**
 * DTO representing a row from the Derby lock diagnostic table SYSCS_DIAG.LOCK_TABLE
 */
public class LockInfo {

    private final String xid;
    private final String type;
    private final String mode;
    private final String tablename;
    private final String lockname;
    private final String state;
    private final String tabletype;
    private final String lockcount;
    private final String indexname;

    /**
     * Public constructor
     * @param xid
     * @param type
     * @param mode
     * @param tablename
     * @param lockname
     * @param state
     * @param tabletype
     * @param lockcount
     * @param indexname
     */
    public LockInfo(String xid, String type, String mode, String tablename, String lockname, String state,
        String tabletype, String lockcount, String indexname) {
        this.xid = xid;
        this.type = type;
        this.mode = mode;
        this.tablename = tablename;
        this.lockname = lockname;
        this.state = state;
        this.tabletype = tabletype;
        this.lockcount = lockcount;
        this.indexname = indexname;
    }
    
    @Override
    public String toString() {
        return String.format("%15s %5s %4s %24s %10s %5s %9s %5s %s", xid, type, mode, tablename, lockname, state, tabletype, lockcount, indexname);
    }
    
    /**
     * Print a header to match the toString columns
     * @return
     */
    public static String header() {
        return String.format("%15s %5s %4s %24s %10s %5s %9s %5s %s", "xid", "type", "mode", "tablename", "lockname", "state", "tabletype", "lockcount", "indexname");
    }
}
