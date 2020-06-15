/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function.registry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.fhir.path.function.AllFalseFunction;
import com.ibm.fhir.path.function.AllTrueFunction;
import com.ibm.fhir.path.function.AnyFalseFunction;
import com.ibm.fhir.path.function.AnyTrueFunction;
import com.ibm.fhir.path.function.BetweenFunction;
import com.ibm.fhir.path.function.CheckModifiersFunction;
import com.ibm.fhir.path.function.ChildrenFunction;
import com.ibm.fhir.path.function.CombineFunction;
import com.ibm.fhir.path.function.ConformsToFunction;
import com.ibm.fhir.path.function.ContainsFunction;
import com.ibm.fhir.path.function.ConvertsToBooleanFunction;
import com.ibm.fhir.path.function.ConvertsToDateFunction;
import com.ibm.fhir.path.function.ConvertsToDateTimeFunction;
import com.ibm.fhir.path.function.ConvertsToDecimalFunction;
import com.ibm.fhir.path.function.ConvertsToIntegerFunction;
import com.ibm.fhir.path.function.ConvertsToQuantityFunction;
import com.ibm.fhir.path.function.ConvertsToStringFunction;
import com.ibm.fhir.path.function.ConvertsToTimeFunction;
import com.ibm.fhir.path.function.CountFunction;
import com.ibm.fhir.path.function.DescendantsFunction;
import com.ibm.fhir.path.function.DistinctFunction;
import com.ibm.fhir.path.function.EmptyFunction;
import com.ibm.fhir.path.function.EndsWithFunction;
import com.ibm.fhir.path.function.ExcludeFunction;
import com.ibm.fhir.path.function.ExpandFunction;
import com.ibm.fhir.path.function.ExtensionFunction;
import com.ibm.fhir.path.function.FHIRPathFunction;
import com.ibm.fhir.path.function.FirstFunction;
import com.ibm.fhir.path.function.GetValueFunction;
import com.ibm.fhir.path.function.HasValueFunction;
import com.ibm.fhir.path.function.HtmlChecksFunction;
import com.ibm.fhir.path.function.IndexOfFunction;
import com.ibm.fhir.path.function.IntersectFunction;
import com.ibm.fhir.path.function.IsDistinctFunction;
import com.ibm.fhir.path.function.ItemFunction;
import com.ibm.fhir.path.function.LastFunction;
import com.ibm.fhir.path.function.LengthFunction;
import com.ibm.fhir.path.function.LookupFunction;
import com.ibm.fhir.path.function.LowerFunction;
import com.ibm.fhir.path.function.MatchesFunction;
import com.ibm.fhir.path.function.MemberOfFunction;
import com.ibm.fhir.path.function.NotFunction;
import com.ibm.fhir.path.function.NowFunction;
import com.ibm.fhir.path.function.ReplaceFunction;
import com.ibm.fhir.path.function.ReplaceMatchesFunction;
import com.ibm.fhir.path.function.ResolveFunction;
import com.ibm.fhir.path.function.SingleFunction;
import com.ibm.fhir.path.function.SkipFunction;
import com.ibm.fhir.path.function.SliceFunction;
import com.ibm.fhir.path.function.StartsWithFunction;
import com.ibm.fhir.path.function.SubsetOfFunction;
import com.ibm.fhir.path.function.SubstringFunction;
import com.ibm.fhir.path.function.SubsumedByFunction;
import com.ibm.fhir.path.function.SubsumesFunction;
import com.ibm.fhir.path.function.SupersetOfFunction;
import com.ibm.fhir.path.function.TailFunction;
import com.ibm.fhir.path.function.TakeFunction;
import com.ibm.fhir.path.function.TimeOfDayFunction;
import com.ibm.fhir.path.function.ToBooleanFunction;
import com.ibm.fhir.path.function.ToCharsFunction;
import com.ibm.fhir.path.function.ToDateFunction;
import com.ibm.fhir.path.function.ToDateTimeFunction;
import com.ibm.fhir.path.function.ToDecimalFunction;
import com.ibm.fhir.path.function.ToIntegerFunction;
import com.ibm.fhir.path.function.ToQuantityFunction;
import com.ibm.fhir.path.function.ToStringFunction;
import com.ibm.fhir.path.function.ToTimeFunction;
import com.ibm.fhir.path.function.TodayFunction;
import com.ibm.fhir.path.function.TranslateFunction;
import com.ibm.fhir.path.function.TypeFunction;
import com.ibm.fhir.path.function.UnionFunction;
import com.ibm.fhir.path.function.UpperFunction;
import com.ibm.fhir.path.function.ValidateCSFunction;
import com.ibm.fhir.path.function.ValidateVSFunction;

public final class FHIRPathFunctionRegistry {
    private static final FHIRPathFunctionRegistry INSTANCE = new FHIRPathFunctionRegistry();
    private Map<String, FHIRPathFunction> functionMap = new ConcurrentHashMap<>();

    private FHIRPathFunctionRegistry() {
        registerFunctions();
    }

    public static FHIRPathFunctionRegistry getInstance() {
        return INSTANCE;
    }

    public void register(FHIRPathFunction function) {
        functionMap.put(function.getName(), function);
    }

    public FHIRPathFunction getFunction(String functionName) {
        return functionMap.get(functionName);
    }

    public Set<String> getFunctionNames() {
        return Collections.unmodifiableSet(functionMap.keySet());
    }

    private void registerFunctions() {
        register(new AllFalseFunction());
        register(new AllTrueFunction());
        register(new AnyFalseFunction());
        register(new AnyTrueFunction());
        register(new BetweenFunction());
        register(new CheckModifiersFunction());
        register(new ChildrenFunction());
        register(new CombineFunction());
        register(new ConformsToFunction());
        register(new ContainsFunction());
        register(new ConvertsToBooleanFunction());
        register(new ConvertsToDateFunction());
        register(new ConvertsToDateTimeFunction());
        register(new ConvertsToDecimalFunction());
        register(new ConvertsToIntegerFunction());
        register(new ConvertsToQuantityFunction());
        register(new ConvertsToStringFunction());
        register(new ConvertsToTimeFunction());
        register(new CountFunction());
        register(new DescendantsFunction());
        register(new DistinctFunction());
        register(new EmptyFunction());
        register(new EndsWithFunction());
        register(new ExcludeFunction());
        register(new ExtensionFunction());
        register(new FirstFunction());
        register(new GetValueFunction());
        register(new HasValueFunction());
        register(new HtmlChecksFunction());
        register(new IndexOfFunction());
        register(new IntersectFunction());
        register(new IsDistinctFunction());
        register(new ItemFunction());
        register(new LastFunction());
        register(new LengthFunction());
        register(new LowerFunction());
        register(new MatchesFunction());
        register(new MemberOfFunction());
        register(new NotFunction());
        register(new NowFunction());
        register(new ReplaceFunction());
        register(new ReplaceMatchesFunction());
        register(new ResolveFunction());
        register(new SingleFunction());
        register(new SkipFunction());
        register(new SliceFunction());
        register(new StartsWithFunction());
        register(new SubsetOfFunction());
        register(new SubstringFunction());
        register(new SupersetOfFunction());
        register(new TailFunction());
        register(new TakeFunction());
        register(new TimeOfDayFunction());
        register(new ToBooleanFunction());
        register(new ToCharsFunction());
        register(new ToDateFunction());
        register(new ToDateTimeFunction());
        register(new ToDecimalFunction());
        register(new ToIntegerFunction());
        register(new ToQuantityFunction());
        register(new ToStringFunction());
        register(new ToTimeFunction());
        register(new TodayFunction());
        register(new TypeFunction());
        register(new UnionFunction());
        register(new UpperFunction());

        // register terminology functions
        register(new ExpandFunction());
        register(new LookupFunction());
        register(new SubsumedByFunction());
        register(new SubsumesFunction());
        register(new TranslateFunction());
        register(new ValidateCSFunction());
        register(new ValidateVSFunction());
    }
}