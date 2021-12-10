/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.model.builder.Builder;
import com.ibm.fhir.model.resource.ClinicalUseDefinition;
import com.ibm.fhir.model.resource.ClinicalUseIssue;
import com.ibm.fhir.model.resource.ExampleScenario;
import com.ibm.fhir.model.resource.GraphDefinition;
import com.ibm.fhir.model.resource.PlanDefinition;
import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ModelSupport.ElementInfo;
import com.ibm.fhir.model.visitor.AbstractVisitable;

/**
 * This class creates FHIR resources.
 * It depends on the fhir-model classes having been compiled in debug mode so that
 * we can infer the parameter names on the builder method of each model class.
 */
public abstract class DataCreatorBase {
    public static final Extension DATA_ABSENT = Extension.builder()
            .url("http://hl7.org/fhir/StructureDefinition/data-absent-reason")
            .value(Code.of("unknown"))
            .build();
    public static final Coding ABSENT_CODING = Coding.builder().extension(DATA_ABSENT).build();
    public static final CodeableConcept ABSENT_CODEABLE_CONCEPT = CodeableConcept.builder().coding(ABSENT_CODING).build();
    private String resourcePackageName = "com.ibm.fhir.model.resource";

    /**
     * Compute the maximum number of allowed types across all choice elements of a resource;
     * useful for knowing how many examples would be needed in order to cover all the variants.
     * @param resourceName
     * @return
     * @throws ClassNotFoundException
     */
    public int getMaxChoiceCount(String resourceName) throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<Resource> resourceClass = (Class<Resource>) Class.forName(resourcePackageName + "." + resourceName);
        return getMaxChoiceCount(resourceClass, 1, 1);
    }

    protected int getMaxChoiceCount(Class<?> resourceOrElementClass, int maxChoiceCount, int levelsDeep) {
        if (ClinicalUseDefinition.class == resourceOrElementClass ||
                ClinicalUseIssue.class == resourceOrElementClass) {
            // special case to ensure we get all the variants of these ones (because 5 elements are mutually exclusive)
            return 5;
        }
        Collection<ElementInfo> elementsInfo = ModelSupport.getElementInfo(resourceOrElementClass);
        for (ElementInfo elementInfo : elementsInfo) {
            if (elementInfo.isChoice()) {
                maxChoiceCount = Math.max(maxChoiceCount, elementInfo.getChoiceTypes().size());
            }
            if (levelsDeep < 10 && resourceOrElementClass != elementInfo.getType() && BackboneElement.class.isAssignableFrom(elementInfo.getType())) {
                // recursively call getMaxChoiceCount
                maxChoiceCount = getMaxChoiceCount(elementInfo.getType(), maxChoiceCount, levelsDeep+1);
            }
        }
        return maxChoiceCount;
    }

    /**
     * Create a resource by name
     *
     * @param resourceName
     * @param choiceIndicator An integer for controlling which type is used for choice elements
     * @return
     * @throws Exception
     */
    public Resource createResource(String resourceName, int choiceIndicator) throws Exception {
        @SuppressWarnings("unchecked")
        Class<Resource> resourceClass = (Class<Resource>) Class.forName(resourcePackageName + "." + resourceName);
        return createResource(resourceClass, choiceIndicator);
    }

    /**
     * Create a resource by class
     * @param resourceClass
     * @param choiceIndicator An integer for controlling which type is used for choice elements
     * @return
     * @throws Exception
     */
    public Resource createResource(Class<? extends Resource> resourceClass, int choiceIndicator) throws Exception {
        Method builderMethod = getBuilderMethod(resourceClass);
        Resource.Builder builder = (Resource.Builder) builderMethod.invoke(null);    
        return (Resource) addData(builder, choiceIndicator).build();
    }

    /**
     * Create an element of type Reference
     * @param targetProfile the target profile to use for the generated reference
     * @return
     * @throws Exception
     */
    public Element createReference(String targetProfile) throws Exception {
        return (Element) addData(Reference.builder(), targetProfile).build();
    }

    /**
     * Create an element
     * @param elementClass
     * @param choiceIndicator An integer for controlling which type is used for choice elements
     * @return
     * @throws Exception
     */
    public Element createElement(Class<? extends Element> elementClass, int choiceIndicator) throws Exception {
        Method builderMethod = getBuilderMethod(elementClass);
        Builder<?> builder = (Builder<?>) builderMethod.invoke(null);
        return (Element) addData(builder, choiceIndicator).build();
    }

    private Method getBuilderMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals("builder")) {
                return method;
            }
        }
        throw new RuntimeException("Unable to find builder method for class " + clazz.getSimpleName());
    }

    /**
     * Creates an argument to pass to the builder based on the parameter type needed
     * @param owningClass
     * @param builderMethod
     * @param parameterType
     * @param i
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    protected Object createArgument(Class<?> owningClass, Method builderMethod, Class<?> parameterType, int i, int choiceIndicator)
            throws Exception {

        if (Collection.class.isAssignableFrom(parameterType)) {
            // The parameter is a List so infer the generic type of the list
            Type[] parameterTypes = builderMethod.getGenericParameterTypes();
            if (parameterTypes[i] instanceof ParameterizedType) {
                parameterType = (Class<?>) ((ParameterizedType) parameterTypes[i]).getActualTypeArguments()[0];
            } else {
                // TODO how can we find the generic type of the collection?!
                throw new Exception("Type '" + parameterTypes[i].getTypeName() + "' is not generic!?");
            }

            List<AbstractVisitable> elementList = new ArrayList<AbstractVisitable>();
            if (Element.class.equals(parameterType)) {
                // Seeing a parameter of type Element is our clue that we have a choice element
                // There are no repeating choice elements in R4, but handle it just in case
                String elementName = builderMethod.getParameters()[i].getName();

                Set<Class<?>> choiceElementTypes = ModelSupport.getChoiceElementTypes(owningClass, elementName);
                for (Class<?> choiceTypeName : choiceElementTypes) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Element> choiceType = (Class<? extends Element>) choiceTypeName;
                    elementList.add(createElement(choiceType, choiceIndicator));
                }
            } else {
                // Skip Extension so we keep the size more reasonable
                // Also skip recursive elements like Codesystem.concept,
                // ExampleScenario.process.step.process
                // ExampleScenario.process.step.operation.request
                // ExampleScenario.process.step.operation.response
                // ExampleScenario.process.step.alternative.step
                // GraphDefinition.link.target
                // PlanDefinition.action
                // QuestionnaireResponse.item.answer
                if (!Extension.class.equals(parameterType) && !parameterType.equals(owningClass) &&
                        !ExampleScenario.Process.Step.class.equals(parameterType) &&
                        !GraphDefinition.Link.Target.class.equals(parameterType) &&
                        !PlanDefinition.Action.class.equals(parameterType) &&
                        !QuestionnaireResponse.Item.class.equals(parameterType)) {
                    // Otherwise just create a single element
                    if (Reference.class.equals(parameterType)){
                        // Handling references specially
                        String elementName = reverseJavaEncoding(builderMethod.getParameters()[i].getName());
                        Set<String> referenceTargetTypes = ModelSupport.getReferenceTargetTypes(owningClass, elementName);
                        if (!referenceTargetTypes.isEmpty()) {
                            String[] targetTypes = new String[referenceTargetTypes.size()];
                            referenceTargetTypes.toArray(targetTypes);

                            // use the "choiceIndicator" to pick the reference type
                            String targetType = targetTypes[(targetTypes.length - 1) % choiceIndicator];
                            elementList = Collections.singletonList(createReference(targetType));
                        } else {
                            elementList = Collections.singletonList(createElement((Class<? extends Element>)parameterType, choiceIndicator));
                        }
                    } else {
                        elementList = Collections.singletonList(createElement((Class<? extends Element>)parameterType, choiceIndicator));
                    }
                }
            }
            return elementList;
        } else if (Element.class.equals(parameterType)){
            // Seeing a parameter of type Element is our clue that we have a choice element
            String elementName = builderMethod.getParameters()[i].getName();
            Class<? extends Element> choiceType = null;

            Set<Class<?>> choiceElementTypes = ModelSupport.getChoiceElementTypes(owningClass, elementName);
            if (!choiceElementTypes.isEmpty()) {
                @SuppressWarnings("unchecked")
                Class<? extends Element>[] choiceTypesArray = new Class[choiceElementTypes.size()];
                choiceElementTypes.toArray(choiceTypesArray);
                choiceType = choiceTypesArray[(choiceTypesArray.length - 1) % choiceIndicator];
            }

            if (Reference.class.equals(choiceType)){

                // Handling references specially
                Set<String> referenceTargetTypes = ModelSupport.getReferenceTargetTypes(owningClass, elementName);
                if (!referenceTargetTypes.isEmpty()) {
                    String[] targetTypes = new String[referenceTargetTypes.size()];
                    referenceTargetTypes.toArray(targetTypes);

                    // use the "choiceIndicator" to pick the reference type
                    String targetType = targetTypes[(targetTypes.length - 1) % choiceIndicator];
                    return createReference(targetType);
                }
            }
            return createElement(choiceType, choiceIndicator);
        } else if (Reference.class.equals(parameterType)){
            // Handling references specially
            String elementName = reverseJavaEncoding(builderMethod.getParameters()[i].getName());
            Set<String> referenceTargetTypes = ModelSupport.getReferenceTargetTypes(owningClass, elementName);
            if (!referenceTargetTypes.isEmpty()) {
                String[] targetTypes = new String[referenceTargetTypes.size()];
                referenceTargetTypes.toArray(targetTypes);

                // use the "choiceIndicator" to pick the reference type
                String targetType = targetTypes[(targetTypes.length - 1) % choiceIndicator];
                return createReference(targetType);
            } else {
                return createElement(Reference.class, choiceIndicator);
            }
        } else if (Resource.class.isAssignableFrom(parameterType)) {
            @SuppressWarnings("unchecked")
            Resource r = createResource((Class<? extends Resource>) parameterType, choiceIndicator);
            return r;
        } else if (Element.class.isAssignableFrom(parameterType)) {
            @SuppressWarnings("unchecked")
            Element e = createElement((Class<? extends Element>) parameterType, choiceIndicator);
            return e;
        } else if (Narrative.class.isAssignableFrom(owningClass) && String.class.equals(parameterType)){
            // special case for generating narrative
            return "<div xmlns=\"http://www.w3.org/1999/xhtml\"></div>";
        } else if (Extension.class.isAssignableFrom(owningClass) && String.class.equals(parameterType)){
            // special case for extension urls
            return "http://example.com";
        } else {
            throw new RuntimeException("Unhandled element of type " + parameterType + "; DataCreator subclasses must handle this.");
        }
    }

    protected String reverseJavaEncoding(String javaName) {
        if (javaName.equals("clazz")) {
            return "class";
        } else if (javaName.startsWith("_")) {
            return javaName.substring(1);
        }
        return javaName;
    }
    
    protected Element.Builder setDataAbsentReason(Element.Builder builder) {
        return builder.extension(DATA_ABSENT);
    }

    protected String titleCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    protected String camelCase(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    abstract protected Builder<?> addData(Builder<?> builder, int choiceIndicator) throws Exception;

    abstract protected Builder<?> addData(Reference.Builder builder, String targetProfile) throws Exception;
}
