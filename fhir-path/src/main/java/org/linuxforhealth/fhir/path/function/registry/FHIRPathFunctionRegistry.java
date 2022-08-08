/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function.registry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.linuxforhealth.fhir.path.function.AllFalseFunction;
import org.linuxforhealth.fhir.path.function.AllTrueFunction;
import org.linuxforhealth.fhir.path.function.AnyFalseFunction;
import org.linuxforhealth.fhir.path.function.AnyTrueFunction;
import org.linuxforhealth.fhir.path.function.BetweenFunction;
import org.linuxforhealth.fhir.path.function.CheckModifiersFunction;
import org.linuxforhealth.fhir.path.function.ChildrenFunction;
import org.linuxforhealth.fhir.path.function.CombineFunction;
import org.linuxforhealth.fhir.path.function.ConformsToFunction;
import org.linuxforhealth.fhir.path.function.ContainsFunction;
import org.linuxforhealth.fhir.path.function.ConvertsToBooleanFunction;
import org.linuxforhealth.fhir.path.function.ConvertsToDateFunction;
import org.linuxforhealth.fhir.path.function.ConvertsToDateTimeFunction;
import org.linuxforhealth.fhir.path.function.ConvertsToDecimalFunction;
import org.linuxforhealth.fhir.path.function.ConvertsToIntegerFunction;
import org.linuxforhealth.fhir.path.function.ConvertsToQuantityFunction;
import org.linuxforhealth.fhir.path.function.ConvertsToStringFunction;
import org.linuxforhealth.fhir.path.function.ConvertsToTimeFunction;
import org.linuxforhealth.fhir.path.function.CountFunction;
import org.linuxforhealth.fhir.path.function.DescendantsFunction;
import org.linuxforhealth.fhir.path.function.DistinctFunction;
import org.linuxforhealth.fhir.path.function.EmptyFunction;
import org.linuxforhealth.fhir.path.function.EndsWithFunction;
import org.linuxforhealth.fhir.path.function.ExcludeFunction;
import org.linuxforhealth.fhir.path.function.ExpandFunction;
import org.linuxforhealth.fhir.path.function.ExtensionFunction;
import org.linuxforhealth.fhir.path.function.FHIRPathFunction;
import org.linuxforhealth.fhir.path.function.FirstFunction;
import org.linuxforhealth.fhir.path.function.GetValueFunction;
import org.linuxforhealth.fhir.path.function.HasValueFunction;
import org.linuxforhealth.fhir.path.function.HtmlChecksFunction;
import org.linuxforhealth.fhir.path.function.IndexOfFunction;
import org.linuxforhealth.fhir.path.function.IntersectFunction;
import org.linuxforhealth.fhir.path.function.IsDistinctFunction;
import org.linuxforhealth.fhir.path.function.ItemFunction;
import org.linuxforhealth.fhir.path.function.LastFunction;
import org.linuxforhealth.fhir.path.function.LengthFunction;
import org.linuxforhealth.fhir.path.function.LookupFunction;
import org.linuxforhealth.fhir.path.function.LowerFunction;
import org.linuxforhealth.fhir.path.function.MatchesFunction;
import org.linuxforhealth.fhir.path.function.MemberOfFunction;
import org.linuxforhealth.fhir.path.function.NotFunction;
import org.linuxforhealth.fhir.path.function.NowFunction;
import org.linuxforhealth.fhir.path.function.ReplaceFunction;
import org.linuxforhealth.fhir.path.function.ReplaceMatchesFunction;
import org.linuxforhealth.fhir.path.function.ResolveFunction;
import org.linuxforhealth.fhir.path.function.SingleFunction;
import org.linuxforhealth.fhir.path.function.SkipFunction;
import org.linuxforhealth.fhir.path.function.SliceFunction;
import org.linuxforhealth.fhir.path.function.StartsWithFunction;
import org.linuxforhealth.fhir.path.function.SubsetOfFunction;
import org.linuxforhealth.fhir.path.function.SubstringFunction;
import org.linuxforhealth.fhir.path.function.SubsumedByFunction;
import org.linuxforhealth.fhir.path.function.SubsumesFunction;
import org.linuxforhealth.fhir.path.function.SupersetOfFunction;
import org.linuxforhealth.fhir.path.function.TailFunction;
import org.linuxforhealth.fhir.path.function.TakeFunction;
import org.linuxforhealth.fhir.path.function.TimeOfDayFunction;
import org.linuxforhealth.fhir.path.function.ToBooleanFunction;
import org.linuxforhealth.fhir.path.function.ToCharsFunction;
import org.linuxforhealth.fhir.path.function.ToDateFunction;
import org.linuxforhealth.fhir.path.function.ToDateTimeFunction;
import org.linuxforhealth.fhir.path.function.ToDecimalFunction;
import org.linuxforhealth.fhir.path.function.ToIntegerFunction;
import org.linuxforhealth.fhir.path.function.ToQuantityFunction;
import org.linuxforhealth.fhir.path.function.ToStringFunction;
import org.linuxforhealth.fhir.path.function.ToTimeFunction;
import org.linuxforhealth.fhir.path.function.TodayFunction;
import org.linuxforhealth.fhir.path.function.TranslateFunction;
import org.linuxforhealth.fhir.path.function.TypeFunction;
import org.linuxforhealth.fhir.path.function.UnionFunction;
import org.linuxforhealth.fhir.path.function.UpperFunction;
import org.linuxforhealth.fhir.path.function.ValidateCSFunction;
import org.linuxforhealth.fhir.path.function.ValidateVSFunction;

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