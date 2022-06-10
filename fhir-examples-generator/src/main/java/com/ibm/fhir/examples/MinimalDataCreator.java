/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples;

import static com.ibm.fhir.model.type.Xhtml.xhtml;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;

import com.ibm.fhir.model.builder.Builder;
import com.ibm.fhir.model.resource.AllergyIntolerance;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ModelSupport.ElementInfo;

public class MinimalDataCreator extends DataCreatorBase {

    public MinimalDataCreator() throws IOException {
        super();
    }

    @Override
    protected Builder<?> addData(com.ibm.fhir.model.type.Reference.Builder builder, String targetProfile) throws Exception {
        return addData(builder, -1, targetProfile);
    }

    @Override
    protected Builder<?> addData(Builder<?> builder, int choiceIndicator) throws Exception {
        return addData(builder, choiceIndicator, null);
    }

    private Builder<?> addData(Builder<?> builder, int choiceIndicator, String referenceTargetProfile) throws Exception {
        if (builder instanceof Resource.Builder) {
            tag((Resource.Builder) builder, "ibm/minimal");
        }

        if (builder instanceof CodeableConcept.Builder){
            // we have a CodeableConcept type - add Coding with data-absent-reason extension due to validation rules
            return ((CodeableConcept.Builder) builder).coding(ABSENT_CODING);
        }

        boolean empty = true;

        Method[] methods = builder.getClass().getDeclaredMethods();
        for (Method method : methods) {
            String name = reverseJavaEncoding(method.getName());

            if (isRequiredElement(builder.getClass().getEnclosingClass(), name)) {

                Class<?>[] parameterClasses = method.getParameterTypes();

                if (parameterClasses.length != 1) {
                    throw new RuntimeException("Error adding data via builder " + builder.getClass() + "; expected 1 parameter, but found " + parameterClasses.length);
                }

                Class<?> parameterType = parameterClasses[0];
                // Special case to avoid infinite recursion
                if (builder instanceof Identifier.Builder && Reference.class.isAssignableFrom(parameterType)) {
                    continue;
                }

                // Special case for Narrative
                if (builder instanceof Narrative.Builder && method.getName().equals("div")) {
                    ((Narrative.Builder) builder).div(xhtml("<div xmlns=\"http://www.w3.org/1999/xhtml\"></div>"));
                    continue;
                }

                if (Element.class.isAssignableFrom(parameterType)
                    || Collection.class.isAssignableFrom(parameterType)) {

                    Object argument = createArgument(builder.getClass().getEnclosingClass(), method, parameterType, 0, choiceIndicator);
                    if (argument != null && !(argument instanceof Collection && ((Collection<?>) argument).isEmpty())) {
                        method.invoke(builder, argument);
                        empty = false;
                    }
                }
            }
        }

        if (empty) {
            if (builder instanceof Element.Builder){
                // We have a primitive type (i.e. an edge node)
                setDataAbsentReason((Element.Builder) builder);
            }
        }
        return builder;
    }

    /**
     * @implNote For the minimal data creator, we only count required elements.
     * @implNote `levelsDeep` isn't actually used here but we could add a check for it just to be safe
     */
    @Override
    protected int getMaxChoiceCount(Class<?> resourceOrElementClass, int maxChoiceCount, int levelsDeep) {
        Collection<ElementInfo> elementsInfo = ModelSupport.getElementInfo(resourceOrElementClass);
        for (ElementInfo elementInfo : elementsInfo) {
            if (elementInfo.isRequired()) {
                if (elementInfo.isChoice()) {
                    maxChoiceCount = Math.max(maxChoiceCount, elementInfo.getChoiceTypes().size());
                } else {
                    // recursively call getMaxChoiceCount
                    maxChoiceCount = getMaxChoiceCount(elementInfo.getType(), maxChoiceCount, levelsDeep+1);
                }
            }
        }
        return maxChoiceCount;
    }

    private boolean isRequiredElement(Class<?> clazz, String name) {
        // Special case for AllergyIntolerance
        if (AllergyIntolerance.class.isAssignableFrom(clazz) && "clinicalStatus".equals(name)) {
            // ait-1: AllergyIntolerance.clinicalStatus SHALL be present if verificationStatus is not entered-in-error. (AllergyIntolerance)
            return true;
        }
        return ModelSupport.isRequiredElement(clazz, name);
    }

    /**
     * Tag the resource
     *
     * @param resource
     *      the resource to tag
     * @param tag
     *      the tag to tag it with
     * @return
     */
    protected Resource.Builder tag(Resource.Builder resourceBuilder, String tag) {
        Meta.Builder metaBuilder = Meta.builder()
                .tag(Coding.builder().code(Code.of(tag))
                .build());
        return resourceBuilder.meta(metaBuilder.build());
    }
}
