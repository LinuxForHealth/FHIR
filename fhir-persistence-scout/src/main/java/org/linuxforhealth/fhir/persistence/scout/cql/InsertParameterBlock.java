/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.scout.cql;

import com.datastax.oss.driver.api.core.CqlSession;
import org.linuxforhealth.fhir.persistence.scout.SearchParameters.ParameterBlock;

/**
 * CQL command to insert the parameter block using the resource logical id as the key
 */
public class InsertParameterBlock {
    
    private final ParameterBlock parameterBlock;
    private final byte[] payload;
    
    public InsertParameterBlock(ParameterBlock pb, byte[] payload) {
        this.parameterBlock = pb;
        this.payload = payload;
    }

    /**
     * Run the command against the given session
     * @param s
     */
    public void run(CqlSession s) {
        
    }
}
