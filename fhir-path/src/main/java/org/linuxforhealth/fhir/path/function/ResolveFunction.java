/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.model.util.FHIRUtil.REFERENCE_PATTERN;
import static org.linuxforhealth.fhir.model.util.ModelSupport.isResourceType;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getRootResourceNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathResourceNode;
import org.linuxforhealth.fhir.path.FHIRPathTree;
import org.linuxforhealth.fhir.path.FHIRPathType;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.path.util.FHIRPathUtil;

public class ResolveFunction extends FHIRPathAbstractFunction {
    private static final int BASE_URL_GROUP = 1;
    private static final int RESOURCE_TYPE_GROUP = 4;
    private static final int LOGICAL_ID_GROUP = 5;
    private static final int VERSION_ID_GROUP = 7;

    @Override
    public String getName() {
        return "resolve";
    }

    @Override
    public int getMinArity() {
        return 0;
    }

    @Override
    public int getMaxArity() {
        return 0;
    }

    /**
     * For each item in the collection, if it is a string that is a uri (or canonical or url), locate the target of the
     * reference, and add it to the resulting collection. If the item does not resolve to a resource, the item is ignored
     * and nothing is added to the output collection. The items in the collection may also represent a Reference, in which
     * case the Reference.reference is resolved.
     *
     * <p>This method creates a resource node that is a placeholder for the actual resource, thus allowing for the FHIRPath
     * evaluator to perform type checking on the result of the resolve function. For example:
     *
     * <pre>Observation.subject.where(resolve() is Patient)</pre>
     *
     * <p>If the resource type cannot be inferred from the reference URL or type, then {@code FHIR_UNKNOWN_RESOURCE_TYPE} is used
     * so that we index these references by default.
     *
     * @param evaluationContext
     *     the evaluation environment
     * @param context
     *     the current evaluation context
     * @param arguments
     *     the arguments for this function
     * @return
     *     the result of the function applied to the context and arguments
     */
    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        Collection<FHIRPathNode> result = new ArrayList<>();
        FHIRPathNode root = FHIRPathUtil.getSingleton(evaluationContext.getExternalConstant("rootResource"));
        boolean isBundleContext = root != null && root.type() == FHIRPathType.FHIR_BUNDLE;
        Map<String, Resource> bundleResources = new HashMap<>();
        if (isBundleContext) {
            Bundle bundle = root.asResourceNode().resource().as(Bundle.class);
            for (Entry e : bundle.getEntry()) {
                if (e.getResource() != null && e.getFullUrl() != null && e.getFullUrl().hasValue()) {
                    bundleResources.put(e.getFullUrl().getValue(), e.getResource());
                }
            }
        }
        for (FHIRPathNode node : context) {
            if (node.isElementNode() && node.asElementNode().element().is(Reference.class)) {
                Reference reference = node.asElementNode().element().as(Reference.class);

                String referenceReference = getReferenceReference(reference);
                String referenceType = getReferenceType(reference);

                String resourceType = null;
                Resource resource = null;
                FHIRPathResourceNode resourceNode = null;

                // if a literal reference
                if (referenceReference != null) {
                    if (referenceReference.startsWith("#")) {
                        // internal fragment reference
                        resourceNode = resolveInternalFragmentReference(node.asElementNode().getTree(), node, referenceReference);
                        if (resourceNode != null) {
                            resource = resourceNode.resource();
                            if (resource != null) {
                                resourceType = resource.getClass().getSimpleName();
                            }
                        }
                    } else {
                        if (isBundleContext) {
                            // We know the root of the tree is a Bundle and the current node is of type Reference,
                            // so walk up the tree until we get to highest Bundle.entry.fullUrl in the tree
                            // and save the value of its fullUrl.
                            // For example:
                            //   A. if node represents Organization.partOf at Bundle.entry[1]
                            //     1. we walk up the tree until the first resource node:  Bundle.entry[1].resource
                            //     2. we look for a fullUrl that is peer to this element
                            //     3. we find it and assign the local fullUrl variable that value
                            //     4. we continue to walk up the tree until we get to the root (Bundle)
                            //   B. if node represents Organization.partOf under Patient.contained[0] at Bundle.entry[2]
                            //     1. we walk up the tree until the first resource node:  Patient.contained[0]
                            //     2. we look for a fullUrl that is peer to this element, but we don't find one
                            //     3. we continue to walk up the tree until we get to the next resource node: Bundle.entry[2].resource
                            //     4. we look for a fullUrl that is peer to this element
                            //     5. we find it and assign the local fullUrl variable that value
                            //     6. we continue to walk up the tree until we get to the root (Bundle)
                            String fullUrl = null;
                            FHIRPathTree tree = evaluationContext.getTree();
                            FHIRPathNode nodeUnderTest = tree.getParent(node);
                            while (nodeUnderTest != root) {
                                if (nodeUnderTest.isResourceNode()) {
                                    FHIRPathNode sibling = tree.getSibling(nodeUnderTest, "fullUrl");
                                    if (sibling != null && sibling.hasValue()) {
                                        fullUrl = sibling.getValue().asStringValue().string();
                                    }
                                }
                                nodeUnderTest = tree.getParent(nodeUnderTest);
                            }

                            String bundleReference = FHIRUtil.buildBundleReference(reference, fullUrl);
                            if (bundleReference != null) {
                                resource = bundleResources.get(bundleReference);
                                if (resource != null) {
                                    resourceType = resource.getClass().getSimpleName();
                                }
                            }
                        }

                        if (resource == null) {
                            Matcher matcher = REFERENCE_PATTERN.matcher(referenceReference);
                            if (matcher.matches()) {
                                if (resourceType == null) {
                                    resourceType = matcher.group(RESOURCE_TYPE_GROUP);
                                }

                                if (referenceType != null && !resourceType.equals(referenceType)) {
                                    throw new IllegalArgumentException("Resource type found in reference URL does not match reference type");
                                }

                                // if we're not in a bundle or the target resource wasn't found in the bundle
                                if (resource == null) {
                                    String baseUrl = matcher.group(BASE_URL_GROUP);
                                    if ((baseUrl == null ||  matchesServiceBaseUrl(baseUrl)) && evaluationContext.resolveRelativeReferences()) {
                                        // relative reference
                                        resource = resolveRelativeReference(evaluationContext, node, resourceType, matcher.group(LOGICAL_ID_GROUP), matcher.group(VERSION_ID_GROUP));
                                    }
                                }
                            }
                        }
                    }
                }

                if (resourceType == null) {
                    resourceType = referenceType;
                }

                FHIRPathType type = isResourceType(resourceType) ? FHIRPathType.from(resourceType) : FHIRPathType.FHIR_UNKNOWN_RESOURCE_TYPE;

                if (referenceReference != null && FHIRPathType.FHIR_UNKNOWN_RESOURCE_TYPE.equals(type)) {
                    generateIssue(evaluationContext, IssueSeverity.INFORMATION, IssueType.INFORMATIONAL, "Resource type could not be inferred from reference: " + referenceReference, node.path());
                }

                if (resourceNode == null) {
                    resourceNode = (resource != null) ? FHIRPathTree.tree(resource).getRoot().asResourceNode() : FHIRPathResourceNode.resourceNode(type);
                }

                result.add(resourceNode);
            }
        }
        return result;
    }

    protected Resource resolveRelativeReference(EvaluationContext evaluationContext, FHIRPathNode node, String type, String logicalId, String versionId) {
        return null;
    }

    protected boolean matchesServiceBaseUrl(String baseUrl) {
        return false;
    }

    private FHIRPathResourceNode resolveInternalFragmentReference(FHIRPathTree tree, FHIRPathNode node, String referenceReference) {
        if (tree != null) {
            FHIRPathResourceNode rootResource = getRootResourceNode(tree, node);
            if (rootResource != null) {
                if ("#".equals(referenceReference)) {
                    return rootResource;
                }
                String id = referenceReference.substring(1);
                if (FHIRPathType.FHIR_DOMAIN_RESOURCE.isAssignableFrom(rootResource.type())) {
                    for (FHIRPathNode child : rootResource.children()) {
                        if ("contained".equals(child.name())) {
                            Resource contained = child.asResourceNode().resource();
                            if (contained.getId() != null && contained.getId().equals(id)) {
                                return child.asResourceNode();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getReferenceReference(Reference reference) {
        if (reference.getReference() != null && reference.getReference().getValue() != null) {
            return reference.getReference().getValue();
        }
        return null;
    }

    private String getReferenceType(Reference reference) {
        if (reference.getType() != null && reference.getType().getValue() != null) {
            return reference.getType().getValue();
        }
        return null;
    }
}
