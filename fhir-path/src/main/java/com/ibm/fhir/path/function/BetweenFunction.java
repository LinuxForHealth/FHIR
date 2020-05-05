/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;


import static com.ibm.fhir.path.FHIRPathQuantityValue.quantityValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.getChronoUnit;
import static com.ibm.fhir.path.util.FHIRPathUtil.getString;
import static com.ibm.fhir.path.util.FHIRPathUtil.getTemporalValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.hasTemporalValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.isStringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

/**
 * An implementation defined FHIRPath function that calculates the time between two date/time values. The behavior of this function is specified by the underlying java.time library function:
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/time/temporal/ChronoUnit.html#between-java.time.temporal.Temporal-java.time.temporal.Temporal-">ChronoUnit.between(Temporal, Temporal)</a>
 */
public class BetweenFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "between";
    }

    @Override
    public int getMinArity() {
        return 3;
    }

    @Override
    public int getMaxArity() {
        return 3;
    }

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!hasTemporalValue(arguments.get(0))) {
            throw new IllegalArgumentException("The first argument to the 'between' function must be a temporal value");
        }

        if (!hasTemporalValue(arguments.get(1))) {
            throw new IllegalArgumentException("The second argument to the 'between' function must be a temporal value");
        }

        if (!isStringValue(arguments.get(2))) {
            throw new IllegalArgumentException("The third argument to the 'between' function must be a string value");
        }

        Temporal firstTemporal = getTemporalValue(arguments.get(0)).temporal();
        Temporal secondTemporal = getTemporalValue(arguments.get(1)).temporal();
        String unit = getString(arguments.get(2));
        ChronoUnit chronoUnit = getChronoUnit(unit);

        return singleton(quantityValue(new BigDecimal(chronoUnit.between(firstTemporal, secondTemporal)), unit));
    }
}
