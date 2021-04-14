/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class CacheKey {
    private final Object[] values;
    private final int hashCode;

    private CacheKey(Object[] values) {
        this.values = Objects.requireNonNull(values, "values");
        hashCode = Arrays.deepHashCode(values);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CacheKey other = (CacheKey) obj;
        return Arrays.deepEquals(values, other.values);
    }

    public static CacheKey key(Object... values) {
        return new CacheKey(values);
    }

    public interface Generator {
        public static final Generator DEFAULT = new Generator() {
            @Override
            public CacheKey generate(Object target, Method method, Object[] args) {
                return key(method, args);
            }
        };
        CacheKey generate(Object target, Method method, Object[] args);
    }
}
