/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.scout;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.ibm.fhir.persistence.scout.SearchParameters.BigDec;
import com.ibm.fhir.persistence.scout.SearchParameters.BigInt;
import com.ibm.fhir.persistence.scout.SearchParameters.DateValue;
import com.ibm.fhir.persistence.scout.SearchParameters.DateValueList;
import com.ibm.fhir.persistence.scout.SearchParameters.LatLngValue;
import com.ibm.fhir.persistence.scout.SearchParameters.LatLngValueList;
import com.ibm.fhir.persistence.scout.SearchParameters.NumberValue;
import com.ibm.fhir.persistence.scout.SearchParameters.NumberValueList;
import com.ibm.fhir.persistence.scout.SearchParameters.ParameterBlock;
import com.ibm.fhir.persistence.scout.SearchParameters.QuantityValue;
import com.ibm.fhir.persistence.scout.SearchParameters.QuantityValueList;
import com.ibm.fhir.persistence.scout.SearchParameters.StrValue;
import com.ibm.fhir.persistence.scout.SearchParameters.StrValueList;
import com.ibm.fhir.persistence.scout.SearchParameters.TokenValue;
import com.ibm.fhir.persistence.scout.SearchParameters.TokenValueList;

/**
 * A wrapper to help hide some of the boilerplate stuff we need when adding
 * parameter values to our 
 */
public class ParameterBlockBuilderHelper {
    
    
    // Hold on to individual list builders because built protobuf objects are immutable
    final NumberValueList.Builder numberListBuilder = NumberValueList.newBuilder();
    final LatLngValueList.Builder latLngListBuilder = LatLngValueList.newBuilder();
    final QuantityValueList.Builder quantityListBuilder = QuantityValueList.newBuilder();
    final TokenValueList.Builder tokenListBuilder = TokenValueList.newBuilder();
    final StrValueList.Builder strListBuilder = StrValueList.newBuilder();

    final Map<String, DateValueList.Builder> dateMap;
    final Map<String, NumberValueList.Builder> numberMap;
    final Map<String, LatLngValueList.Builder> latLngMap;
    final Map<String, QuantityValueList.Builder> quantityMap;
    final Map<String, TokenValueList.Builder> tokenMap;
    final Map<String, StrValueList.Builder> strMap;
    
    /**
     * Public constructor
     * @param builder
     */
    public ParameterBlockBuilderHelper() {
        
        this.dateMap = new HashMap<>();
        this.numberMap = new HashMap<>();
        this.latLngMap = new HashMap<>();
        this.quantityMap = new HashMap<>();
        this.tokenMap = new HashMap<>();
        this.strMap = new HashMap<>();
    }
    
    /**
     * Build the parameter block
     * @return
     */
    public ParameterBlock build() {
        ParameterBlock.Builder builder = ParameterBlock.newBuilder();
        
        dateMap.forEach((k,v) -> builder.putDateValues(k, v.build()));
        latLngMap.forEach((k,v) -> builder.putLatlngValues(k, v.build()));
        numberMap.forEach((k,v) -> builder.putNumberValues(k, v.build()));
        quantityMap.forEach((k,v) -> builder.putQuantityValues(k, v.build()));
        strMap.forEach((k,v) -> builder.putStringValues(k, v.build()));
        tokenMap.forEach((k,v) -> builder.putTokenValues(k, v.build()));
        return builder.build();
    }

    /**
     * Add a DateValue parameter
     * @param name
     * @param start
     * @param end
     */
    public void addDateParam(String name, long start, long end) {
        
        DateValueList.Builder dateListBuilder = dateMap.get(name);
        if (dateListBuilder == null) {
            dateListBuilder = DateValueList.newBuilder();
            dateMap.put(name, dateListBuilder);
        }
                
        DateValue.Builder value = DateValue.newBuilder();
        value.setDateStart(start);
        value.setDateEnd(end);

        dateListBuilder.addDateValues(value.build());
    }
    
    public void addLatLngParam(String name, double lat, double lng) {
        LatLngValueList.Builder builder = latLngMap.get(name);
        if (builder == null) {
            builder = LatLngValueList.newBuilder();
            latLngMap.put(name, builder);
        }
        
        // Create a new value and add it to the parameter list
        LatLngValue.Builder value = LatLngValue.newBuilder();
        value.setLatitude(lat);
        value.setLongitude(lng);
        builder.addLatlngValues(value.build());
    }
    
    public void addNumberParam(String name, BigDecimal d) {
        NumberValueList.Builder builder = numberMap.get(name);
        if (builder == null) {
            builder = NumberValueList.newBuilder();
            numberMap.put(name,  builder);
        }
        
        BigDec bdValue = encode(d);
        BigDec lowValue = encode(BigDecimalUtil.generateLowerBound(d));
        BigDec highValue = encode(BigDecimalUtil.generateUpperBound(d));
        NumberValue.Builder numberValue = NumberValue.newBuilder();
        numberValue.setNumberValue(bdValue);
        numberValue.setNumberValueLow(lowValue);
        numberValue.setNumberValueHigh(highValue);

        builder.addNumberValues(numberValue.build());
    }
    
    public void addQuantityParam(String name, BigDecimal quant, BigDecimal lowValue, BigDecimal highValue, String code, String system) {
        QuantityValueList.Builder builder = quantityMap.get(name);
        if (builder == null) {
            builder = QuantityValueList.newBuilder();
            quantityMap.put(name, builder);
        }
        
        QuantityValue.Builder value = QuantityValue.newBuilder();
        value.setQuantityValue(encode(quant));
        value.setQuantityValueLow(encode(lowValue));
        value.setQuantityValueHigh(encode(highValue));
        value.setCode(code);
        if (system != null) {
            value.setCodeSystem(system);
        }
        builder.addQuantityValues(value.build());
    }
    
    public void addStrParam(String name, String str) {
        StrValueList.Builder builder = strMap.get(name);
        if (builder == null) {
            builder = StrValueList.newBuilder();
            strMap.put(name, builder);
        }
        
        StrValue.Builder value = StrValue.newBuilder();
        value.setStrValue(str);
        builder.addStringValues(value.build());
    }
    
    public void addTokenParam(String name, String tokenValue, String system) {
        TokenValueList.Builder builder = tokenMap.get(name);
        if (builder == null) {
            builder = TokenValueList.newBuilder();
            tokenMap.put(name, builder);
        }
        
        TokenValue.Builder value = TokenValue.newBuilder();
        value.setTokenValue(tokenValue);
        if (system != null) {
            value.setCodeSystem(system);
        }
        builder.addTokenValues(value.build());
    }
    
    /**
     * Convert the Java {@link BigDecimal} to the corresponding protobuf custom implementation
     * @param value
     * @return
     */
    private BigDec encode(BigDecimal value) {
        BigInteger biv = value.unscaledValue();
        BigInt.Builder intBuilder = BigInt.newBuilder();
        
        ByteString bs = ByteString.copyFrom(biv.toByteArray());
        intBuilder.setValue(bs);

        BigDec.Builder decBuilder = BigDec.newBuilder();
        decBuilder.setScale(value.scale());
        decBuilder.setValue(intBuilder.build());
        return decBuilder.build();
    }
    
}
