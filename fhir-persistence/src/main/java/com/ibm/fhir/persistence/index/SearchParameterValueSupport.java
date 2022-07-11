/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Collection of support methods related to SearchParameterValue objects
 */
public class SearchParameterValueSupport {
    /**
     * Compare the two Integer values, taking into account nulls
     * @param left
     * @param right
     * @return
     */
    public static int compareValue(Integer left, Integer right) {
        if (left == null && right == null) {
            return 0;
        }

        if (left == null) {
            return -1;
        }

        if (right == null) {
            return 1;
        }

        return left.compareTo(right);
    }

    /**
     * Compare the two Boolean values, taking into account nulls
     * @param left
     * @param right
     * @return
     */
    public static int compareValue(Boolean left, Boolean right) {
        if (left == null && right == null) {
            return 0;
        }

        if (left == null) {
            return -1;
        }

        if (right == null) {
            return 1;
        }

        return left.compareTo(right);
    }

    /**
     * Compare the two Instant values, taking into account nulls
     * @param left
     * @param right
     * @return
     */
    public static int compareValue(Instant left, Instant right) {
        if (left == null && right == null) {
            return 0;
        }

        if (left == null) {
            return -1;
        }

        if (right == null) {
            return 1;
        }

        return left.compareTo(right);
    }

    /**
     * Compare the two BigDecimal values, taking into account nulls
     * @param left
     * @param right
     * @return
     */
    public static int compareValue(BigDecimal left, BigDecimal right) {
        if (left == null && right == null) {
            return 0;
        }

        if (left == null) {
            return -1;
        }

        if (right == null) {
            return 1;
        }

        return left.compareTo(right);
    }

    /**
     * Compare the two Double values, taking into account nulls
     * @param left
     * @param right
     * @return
     */
    public static int compareValue(Double left, Double right) {
        if (left == null && right == null) {
            return 0;
        }

        if (left == null) {
            return -1;
        }

        if (right == null) {
            return 1;
        }

        return left.compareTo(right);
    }

    /**
     * Compare the two String values taking into account nulls
     * @param left
     * @param right
     * @return
     */
    public static int compareValue(String left, String right) {
        if (left == null && right == null) {
            return 0;
        }

        if (left == null) {
            return -1;
        }

        if (right == null) {
            return 1;
        }

        return left.compareTo(right);
    }

    /**
     * Compare the SearchParameterValue elements, taking into account any null fields
     * each may have
     * @param left not null
     * @param right not null
     * @return
     */
    public static int compareSearchParameterValue(SearchParameterValue left, SearchParameterValue right) {
        int result = left.getName().compareTo(right.getName());
        if (0 == result) {
            result = compareValue(left.getCompositeId(), right.getCompositeId());
            if (0 == result) {
                result = compareValue(left.getWholeSystem(), right.getWholeSystem());
            }
        }
        return result;
    }

    /**
     * Comparator function for comparing two {@link DateParameter} values
     * @param left
     * @param right
     * @return
     */
    public static int compare(DateParameter left, DateParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getValueDateStart(), right.getValueDateStart());
            if (0 == result) {
                result = compareValue(left.getValueDateEnd(), right.getValueDateEnd());
            }
        }
        return result;
    }

    /**
     * Comparator function for comparing two {@link LocationParameter} values
     *
     * @param left
     * @param right
     * @return
     */
    public static int compare(LocationParameter left, LocationParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getValueLatitude(), right.getValueLatitude());
            if (0 == result) {
                result = compareValue(left.getValueLongitude(), right.getValueLongitude());
            }
        }
        return result;
    }

    /**
     * Comparator function for comparing two {@link NumberParameter} values
     * @param left
     * @param right
     * @return
     */
    public static int compare(NumberParameter left, NumberParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getValue(), right.getValue());
            if (0 == result) {
                result = compareValue(left.getLowValue(), right.getLowValue());
                if (0 == result) {
                    result = compareValue(left.getHighValue(), right.getHighValue());
                }
            }
        }
        return result;
    }

    /**
     * Comparator function for comparing two {@link ProfileParameter} values
     * @param left
     * @param right
     * @return
     */
    public static int compare(ProfileParameter left, ProfileParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getUrl(), right.getUrl());
            if (0 == result) {
                result = compareValue(left.getVersion(), right.getVersion());
                if (0 == result) {
                    result = compareValue(left.getFragment(), right.getFragment());
                }
            }
        }
        return result;
    }

    /**
     * Comparator function for comparing two {@link QuantityParameter} values
     * @param left
     * @param right
     * @return
     */
    public static int compare(QuantityParameter left, QuantityParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getValueCode(), right.getValueCode());
            if (0 == result) {
                result = compareValue(left.getValueSystem(), right.getValueSystem());
                if (0 == result) {
                    result = compareValue(left.getValueNumber(), right.getValueNumber());
                    if (0 == result) {
                        result = compareValue(left.getValueNumberLow(), right.getValueNumberLow());
                        if (0 == result) {
                            result = compareValue(left.getValueNumberHigh(), right.getValueNumberHigh());
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Comparator function for comparing two {@link ReferenceParameter} values
     * @param left
     * @param right
     * @return
     */
    public static int compare(ReferenceParameter left, ReferenceParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getResourceType(), right.getResourceType());
            if (0 == result) {
                result = compareValue(left.getLogicalId(), right.getLogicalId());
                if (0 == result) {
                    result = compareValue(left.getRefVersionId(), right.getRefVersionId());
                }
            }
        }
        return result;
    }

    /**
     * Comparator function for comparing two {@link SecurityParameter} values
     * @param left
     * @param right
     * @return
     */
    public static int compare(SecurityParameter left, SecurityParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getValueCode(), right.getValueSystem());
            if (0 == result) {
                result = compareValue(left.getValueSystem(), right.getValueSystem());
            }
        }
        return result;
    }

    /**
     * Comparator function for comparing two {@link StringParameter} values
     * @param left
     * @param right
     * @return
     */
    public static int compare(StringParameter left, StringParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getValue(), right.getValue());
        }
        return result;
        
    }

    /**
     * Comparator function for comparing two {@link TagParameter} values
     * @param left
     * @param right
     * @return
     */
    public static int compare(TagParameter left, TagParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getValueCode(), right.getValueSystem());
            if (0 == result) {
                result = compareValue(left.getValueSystem(), right.getValueSystem());
            }
        }
        return result;
    }

    /**
     * Comparator function for comparing two {@link TokenParameter} values
     * @param left
     * @param right
     * @return
     */
    public static int compare(TokenParameter left, TokenParameter right) {
        int result = compareSearchParameterValue(left, right);
        if (0 == result) {
            result = compareValue(left.getValueCode(), right.getValueSystem());
            if (0 == result) {
                result = compareValue(left.getValueSystem(), right.getValueSystem());
                if (0 == result) {
                    result = compareValue(left.getRefVersionId(), right.getRefVersionId());
                }
            }
        }
        return result;
    }
}