/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.api;


/**
 * The metric names for tracking search parameter insert performance
 */
public enum ParamMetrics {

    M_PARAM_RESOLVE_LOGICAL_RESOURCES,
    M_PARAM_RESOLVE_COMMON_TOKEN_VALUES,
    M_PARAM_RESOLVE_CODE_SYSTEMS,
    M_PARAM_RESOLVE_PARAMETER_NAMES,
    M_PARAM_RESOLVE_CANONICAL_VALUES,
    M_PARAM_PUBLISH,
    M_PARAM_PUSH_BATCH,
    M_STRINGS,
    M_NUMBERS,
    M_TOKENS,
    M_QUANTITIES,
    M_LOCATIONS,
    M_REFERENCES,
    M_DATES,
    M_TAGS,
    M_PROFILES,
    M_SECURITY
}
