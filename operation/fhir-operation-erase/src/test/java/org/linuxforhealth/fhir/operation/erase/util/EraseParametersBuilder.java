/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.erase.util;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;

import net.jcip.annotations.ThreadSafe;

/**
 * Supports various use cases where a parameters object must be generated
 */
@ThreadSafe
public final class EraseParametersBuilder {

    public static final Extension DATA_ABSENT =
            Extension.builder().url("http://hl7.org/fhir/StructureDefinition/data-absent-reason").value(Code.of("unknown")).build();

    private static final String REASON = "reason";
    private static final String PATIENT = "patient";
    private static final String VERSION = "version";
    private static final String ID = "id";

    private Parameters.Builder builder = null;
    private List<Parameter> parameters = new ArrayList<>();

    private EraseParametersBuilder() {
        // @implNote we want to use the builder only.
        builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
    }

    /**
     * Generates a reason given a string.
     *
     * @param reason
     *            the reason
     * @return
     */
    public EraseParametersBuilder reason(String reason) {
        parameters.add(Parameter.builder().name(string(REASON)).value(string(reason)).build());
        return this;
    }

    /**
     *
     * @return a builder with a bad reason
     */
    public EraseParametersBuilder badReason() {
        parameters.add(Parameter.builder().name(string(REASON)).value(org.linuxforhealth.fhir.model.type.Integer.of("1")).build());
        return this;
    }

    /**
     *
     * @return a builder with a null reason
     */
    public EraseParametersBuilder nullReason() {
        parameters.add(Parameter.builder().name(string(REASON)).build());
        return this;
    }

    /**
     * generates a parameter with data absent reason
     *
     * @return
     */
    public EraseParametersBuilder absentReason() {
        org.linuxforhealth.fhir.model.type.String str = org.linuxforhealth.fhir.model.type.String.builder().extension(DATA_ABSENT).build();
        parameters.add(Parameter.builder().name(string(REASON)).value(str).build());
        return this;
    }

    /**
     *
     * @param patient
     *            to add to built parameters object
     * @return a builder with the patient parameter
     */
    public EraseParametersBuilder patient(String patient) {
        parameters.add(Parameter.builder().name(string(PATIENT)).value(string(patient)).build());
        return this;
    }

    /**
     *
     * @return a bad patient in the builder
     */
    public EraseParametersBuilder badPatient() {
        parameters.add(Parameter.builder().name(string(PATIENT)).value(org.linuxforhealth.fhir.model.type.Integer.of("1")).build());
        return this;
    }

    /**
     *
     * @return a null patient in the builder
     */
    public EraseParametersBuilder nullPatient() {
        parameters.add(Parameter.builder().name(string(PATIENT)).build());
        return this;
    }

    /**
     *
     * @return a patient in the builder with absent
     */
    public EraseParametersBuilder absentPatient() {
        org.linuxforhealth.fhir.model.type.String str = org.linuxforhealth.fhir.model.type.String.builder().extension(DATA_ABSENT).build();
        parameters.add(Parameter.builder().name(string(PATIENT)).value(str).build());
        return this;
    }

    /**
     *
     * @param version
     *            to add to built parameters object
     * @return a builder with the patient parameter
     */
    public EraseParametersBuilder version(Integer version) {
        parameters.add(Parameter.builder().name(string(VERSION)).value(org.linuxforhealth.fhir.model.type.Integer.of(version)).build());
        return this;
    }

    /**
     *
     * @return a bad version in the builder
     */
    public EraseParametersBuilder badVersion() {
        parameters.add(Parameter.builder().name(string(VERSION)).value(string("1")).build());
        return this;
    }

    /**
     *
     * @return a null version in the builder
     */
    public EraseParametersBuilder nullVersion() {
        parameters.add(Parameter.builder().name(string(VERSION)).build());
        return this;
    }

    /**
     *
     * @return a parameter that is data-absent for version
     */
    public EraseParametersBuilder absentVersion() {
        org.linuxforhealth.fhir.model.type.Integer itr = org.linuxforhealth.fhir.model.type.Integer.builder().extension(DATA_ABSENT).build();
        parameters.add(Parameter.builder().name(string(VERSION)).value(itr).build());
        return this;
    }

    /**
     *
     * @param id
     *            to add to built parameters object
     * @return a builder with the id parameter
     */
    public EraseParametersBuilder id(String id) {
        parameters.add(Parameter.builder().name(string(ID)).value(string(id)).build());
        return this;
    }

    /**
     *
     * @return a bad id in the builder
     */
    public EraseParametersBuilder badId() {
        parameters.add(Parameter.builder().name(string(ID)).value(org.linuxforhealth.fhir.model.type.Integer.of("1")).build());
        return this;
    }

    /**
     *
     * @return a null id in the builder
     */
    public EraseParametersBuilder nullId() {
        parameters.add(Parameter.builder().name(string(ID)).build());
        return this;
    }

    /**
     *
     * @return a id in the builder with absent
     */
    public EraseParametersBuilder absentId() {
        org.linuxforhealth.fhir.model.type.String str = org.linuxforhealth.fhir.model.type.String.builder().extension(DATA_ABSENT).build();
        parameters.add(Parameter.builder().name(string(ID)).value(str).build());
        return this;
    }

    /**
     *
     * @return a single instance of the builder
     */
    public static EraseParametersBuilder builder() {
        return new EraseParametersBuilder();
    }

    /**
     * Builds the parameter object.
     *
     * @implNote the builder is left in tact, and can be reused (adding, not subtracting parameters).
     *
     * @param includeEmpty
     *
     * @return
     */
    public Parameters build(boolean includeEmpty) {
        if (!parameters.isEmpty()) {
            builder.parameter(parameters);
        } else if (includeEmpty) {
            builder.parameter(Collections.emptyList());
        }
        return builder.build();
    }
}