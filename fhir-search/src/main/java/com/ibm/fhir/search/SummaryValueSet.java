package com.ibm.fhir.search;

public enum SummaryValueSet {    
        TRUE("true"),
        TEXT("text"),
        DATA("data"),
        COUNT("count"),
        FALSE("false");
     
        private final String value;
     
        SummaryValueSet(String value) {
            this.value = value;
        }
     
        public String value() {
            return value;
        }
        
        public static SummaryValueSet from(String value) {
            for (SummaryValueSet c : SummaryValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
}
