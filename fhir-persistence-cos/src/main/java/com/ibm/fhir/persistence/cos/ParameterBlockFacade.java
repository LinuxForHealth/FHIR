/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.scout;

import com.ibm.fhir.persistence.scout.SearchParameters.ParameterBlock;

/**
 * Facade that wraps a parameter block to provide some useful additional
 * functionality and hide some of the more complex interactions with it
 */
public class ParameterBlockFacade {

    // The ParameterBlock being wrapped
    private final ParameterBlock parameterBlock;
    
    public ParameterBlockFacade(ParameterBlock pb) {
        this.parameterBlock = pb;
    }
    
    /**
     * Returns true iff all the parameter maps in the {@link ParameterBlock} are empty
     * @return
     */
    public boolean isEmpty() {
        return     parameterBlock.getDateValuesMap().isEmpty()
                && parameterBlock.getLatlngValuesMap().isEmpty()
                && parameterBlock.getNumberValuesMap().isEmpty()
                && parameterBlock.getQuantityValuesMap().isEmpty()
                && parameterBlock.getStringValuesMap().isEmpty()
                && parameterBlock.getTokenValuesMap().isEmpty();
    }
    
    /**
     * @return the total number of parameters in the wrapped {@link ParameterBlock}
     */
    public int count() {
        return 
              parameterBlock.getDateValuesCount()
            + parameterBlock.getLatlngValuesCount()
            + parameterBlock.getNumberValuesCount()
            + parameterBlock.getQuantityValuesCount()
            + parameterBlock.getStringValuesCount()
            + parameterBlock.getTokenValuesCount();
    }
    
    /**
     * Add the contents of this to the given {@link Builder} target
     * @param target
     */
    public ParameterBlockFacade mergeInto(ParameterBlock.Builder target) {
        // Could it really be this simple?
        target.mergeFrom(parameterBlock);
        return this;
    }
}
