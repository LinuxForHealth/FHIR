/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public abstract class FHIRPathAbstractFunction implements FHIRPathFunction {
    @Override
    public abstract String getName();

    @Override
    public abstract int getMinArity();

    @Override
    public abstract int getMaxArity();

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        throw new UnsupportedOperationException("Function: '" + getName() + "' is not supported");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FHIRPathFunction)) {
            return false;
        }
        FHIRPathFunction other = (FHIRPathFunction) obj;
        return Objects.equals(getName(), other.getName()) && 
                Objects.equals(getMinArity(), other.getMinArity()) && 
                Objects.equals(getMaxArity(), other.getMaxArity());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getMinArity(), getMaxArity());
    }
}
