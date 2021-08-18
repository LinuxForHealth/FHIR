/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;

/**
 *  This class tests the persistence layer support for the FHIR canonical search parameters.
 */
public abstract class AbstractCanonicalTest extends AbstractPersistenceTest {
    private static CarePlan savedCarePlan1;
    private static CarePlan savedCarePlan2;
    private static CarePlan savedCarePlan3;
    private static Measure savedMeasure1;
    private static Measure savedMeasure2;
    private static Measure savedMeasure3;
    private static Measure savedMeasure4;
    private static Library savedLibrary1;
    private static Library savedLibrary2;
    private static Library savedLibrary3;
    private static String uuid = UUID.randomUUID().toString();

    /**
     * Loads up and saves a bunch of resources with various canonical references to one another
     */
    @BeforeClass
    public void createResources() throws Exception {
        CarePlan carePlan = TestUtil.getMinimalResource(CarePlan.class);
        Measure measure = TestUtil.getMinimalResource(Measure.class);
        Library library = TestUtil.getMinimalResource(Library.class);

        // a Library that is referenced by a Measure
        savedLibrary1 = library.toBuilder()
                                .url(Uri.of("http://example.com/Library/" + uuid))
                                .version(string("1.0"))
                                .name(string(uuid + "library1"))
                                .build();
        savedLibrary1 = persistence.create(getDefaultPersistenceContext(), savedLibrary1).getResource();

        // a Library with a different version that is referenced by a Measure
        savedLibrary2 = library.toBuilder()
                                .url(Uri.of("http://example.com/Library/" + uuid))
                                .version(string("2.0"))
                                .name(string(uuid + "library2"))
                                .build();
        savedLibrary2 = persistence.create(getDefaultPersistenceContext(), savedLibrary2).getResource();

        // a Library with no version that is referenced by a Measure
        savedLibrary3 = library.toBuilder()
                                .url(Uri.of("http://example.com/Library/" + uuid))
                                .name(string(uuid + "library3"))
                                .build();
        savedLibrary3 = persistence.create(getDefaultPersistenceContext(), savedLibrary3).getResource();

        // a Measure that references a Library and is referenced by a CarePlan
        savedMeasure1 = measure.toBuilder()
                .url(Uri.of("http://example.com/Measure/" + uuid + "measure1"))
                .version(string("1.0"))
                .library(Canonical.of("http://example.com/Library/" + uuid, "1.0"))
                .name(string(uuid + "measure1"))
                .build();
        savedMeasure1 = persistence.create(getDefaultPersistenceContext(), savedMeasure1).getResource();

        // another Measure that references a Library
        savedMeasure2 = measure.toBuilder()
                .url(Uri.of("http://example.com/Measure/" + uuid + "measure2"))
                .version(string("1.0"))
                .library(Canonical.of("http://example.com/Library/" + uuid))
                .name(string(uuid + "measure2"))
                .build();
        savedMeasure2 = persistence.create(getDefaultPersistenceContext(), savedMeasure2).getResource();

        // another Measure that references a Library and contains a reference fragment
        savedMeasure3 = measure.toBuilder()
                .url(Uri.of("http://example.com/Measure/" + uuid + "measure1"))
                .library(Canonical.of("http://example.com/Library/" + uuid + "|2.0#ignore"))
                .name(string(uuid + "measure3"))
                .build();
        savedMeasure3 = persistence.create(getDefaultPersistenceContext(), savedMeasure3).getResource();

        // a Measure with depends-on missing
        savedMeasure4 = measure.toBuilder()
                .url(Uri.of("http://example.com/Measure/" + uuid + "measure4"))
                .name(string(uuid + "measure4"))
                .build();
        savedMeasure4 = persistence.create(getDefaultPersistenceContext(), savedMeasure4).getResource();

        // a CarePlan that references a Measure
        savedCarePlan1 = carePlan.toBuilder()
                .instantiatesCanonical(Canonical.of("http://example.com/Measure/" + uuid + "measure1", "1.0"))
                .instantiatesUri(Uri.of(uuid + "carePlan1"))
                .build();
        savedCarePlan1 = persistence.create(getDefaultPersistenceContext(), savedCarePlan1).getResource();

        // another CarePlan that references a Measure, but does not specify a version
        savedCarePlan2 = carePlan.toBuilder()
                .instantiatesCanonical(Canonical.of("http://example.com/Measure/" + uuid + "measure1"))
                .instantiatesUri(Uri.of(uuid + "carePlan2"))
                .build();
        savedCarePlan2 = persistence.create(getDefaultPersistenceContext(), savedCarePlan2).getResource();

        // a CarePlan that references a different Measure
        savedCarePlan3 = carePlan.toBuilder()
                .instantiatesCanonical(Canonical.of("http://example.com/Measure/" + uuid + "measure2", "1.0"))
                .instantiatesUri(Uri.of(uuid + "carePlan3"))
                .basedOn(Reference.builder().reference(string("CarePlan/" + savedCarePlan1.getId())).build())
                .build();
        savedCarePlan3 = persistence.create(getDefaultPersistenceContext(), savedCarePlan3).getResource();
    }

    @AfterClass
    public void deleteResources() throws Exception {
        Resource[] resources = {savedCarePlan1, savedCarePlan2, savedCarePlan3,
                savedMeasure1, savedMeasure2, savedMeasure3, savedMeasure4,
                savedLibrary1, savedLibrary2, savedLibrary3};

        if (persistence.isDeleteSupported()) {
            if (persistence.isTransactional()) {
                persistence.getTransaction().begin();
            }

            try {
                for (Resource resource : resources) {
                    persistence.delete(getDefaultPersistenceContext(), resource.getClass(), resource.getId());
                }
            } catch (Throwable t) {
                if (persistence.isTransactional()) {
                    persistence.getTransaction().setRollbackOnly();
                }
                throw t;
            } finally {
                if (persistence.isTransactional()) {
                    persistence.getTransaction().end();
                }
            }
        }
    }

    /**
     * This test queries for Libraries by url, with no version specified.
     * @throws Exception
     */
    @Test
    public void testUrl() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("url", Collections.singletonList("http://example.com/Library/" + uuid));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedLibrary1.getId()));
        assertTrue(ids.contains(savedLibrary2.getId()));
        assertTrue(ids.contains(savedLibrary3.getId()));
    }

    /**
     * This test queries for Libraries by url, with a version specified.
     * @throws Exception
     */
    @Test
    public void testUrlWithVersion() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("url", Collections.singletonList("http://example.com/Library/" + uuid + "|2.0"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedLibrary2.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Libraries by url, with a version and fragment specified
     * (fragment is currently ignored).
     * @throws Exception
     */
    @Test
    public void testUrlWithVersionAndFragment() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("url", Collections.singletonList("http://example.com/Library/" + uuid + "|1.0#fragment"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedLibrary1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Measures by url, with multiple values specified.
     * @throws Exception
     */
    @Test
    public void testUrlWithMultipleValues() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("url",
            Collections.singletonList("http://example.com/Measure/" + uuid + "measure1,http://example.com/Measure/" + uuid + "measure2"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedMeasure1.getId()));
        assertTrue(ids.contains(savedMeasure2.getId()));
        assertTrue(ids.contains(savedMeasure3.getId()));
    }

    /**
     * This test queries for Measures by canonical search parm, with no version specified.
     * @throws Exception
     */
    @Test
    public void testCanonical() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("depends-on", Collections.singletonList("http://example.com/Library/" + uuid));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedMeasure1.getId()));
        assertTrue(ids.contains(savedMeasure2.getId()));
        assertTrue(ids.contains(savedMeasure3.getId()));
    }

    /**
     * This test queries for Measures by canonical search parm, with a version specified.
     * @throws Exception
     */
    @Test
    public void testCanonicalWithVersion() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("depends-on", Collections.singletonList("http://example.com/Library/" + uuid + "|1.0"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedMeasure1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Measures by canonical search parm, with a version and fragment specified.
     * (fragment is currently ignored).
     * @throws Exception
     */
    @Test
    public void testCanonicalWithVersionAndFragment() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("depends-on", Collections.singletonList("http://example.com/Library/" + uuid + "|2.0#fragment"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedMeasure3.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Measures by canonical search parm, with multiple values specified.
     * @throws Exception
     */
    @Test
    public void testCanonicalWithMultipleValues() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("depends-on",
            Collections.singletonList("http://example.com/Library/" + uuid + ",http://example.com/Library/" + uuid + "|2.0"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedMeasure1.getId()));
        assertTrue(ids.contains(savedMeasure2.getId()));
        assertTrue(ids.contains(savedMeasure3.getId()));
    }

    /**
     * This test queries for Measures which reference a Library which only
     * has a url value and no version value. The matching Measures should be
     * any Measure whose library value has a matching url part.
     * @throws Exception
     */
    @Test
    public void testCanonicalChainSingle1() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("depends-on:Library.name", Collections.singletonList(uuid + "library3"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedMeasure1.getId()));
        assertTrue(ids.contains(savedMeasure2.getId()));
        assertTrue(ids.contains(savedMeasure3.getId()));
    }

    /**
     * This test queries for Measures which reference a Library which
     * has a url value and a version value. The matching Measures should be
     * any Measure whose library value has a matching url part and a matching
     * version part.
     * @throws Exception
     */
    @Test
    public void testCanonicalChainSingle2() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("depends-on:Library.name", Collections.singletonList(uuid + "library1"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedMeasure1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Measures which reference a Library which
     * has a url value and a version value. The matching Measures should be any Measure
     * whose library value has a matching url part and a matching version part, even
     * if the Measure's library value has a fragment part. The fragment is ignored.
     * @throws Exception
     */
    @Test
    public void testCanonicalChainSingle3() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("depends-on:Library.name", Collections.singletonList(uuid + "library2"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedMeasure3.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for CarePlans which reference Measures which reference
     * a Library which has a url value and version value. The matching Measures
     * should be any Measure whose library value has a matching url and version part,
     * and the matching CarePlans should be any CarePlan whose instantiatesCanonical value
     * has matching url and version parts to the Measure url and version values.
     * @throws Exception
     */
    @Test
    public void testCanonicalChainMultiple1() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("instantiates-canonical:Measure.depends-on:Library.name",
            Collections.singletonList(uuid + "library1"));
        List<Resource> resources = runQueryTest(CarePlan.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedCarePlan1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for CarePlans which reference Measures which reference
     * a Library which has a url value and version value. The matching Measures
     * should be any Measure whose library value has a matching url and version part,
     * and the matching CarePlans should be any CarePlan whose instantiatesCanonical value
     * has a matching url part to the Measure url (matching Measure has no version value).
     * @throws Exception
     */
    @Test
    public void testCanonicalChainMultiple2() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("instantiates-canonical:Measure.depends-on:Library.name",
            Collections.singletonList(uuid + "library2"));
        List<Resource> resources = runQueryTest(CarePlan.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedCarePlan1.getId()));
        assertTrue(ids.contains(savedCarePlan2.getId()));
    }

    /**
     * This test queries for CarePlans which reference Measures which reference
     * Libraries, some of which have a url value and version value, and some of which
     * only have a url value.
     * @throws Exception
     */
    @Test
    public void testCanonicalChainMultiple3() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("instantiates-canonical:Measure.depends-on:Library.name",
            Collections.singletonList(uuid + "library3"));
        List<Resource> resources = runQueryTest(CarePlan.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedCarePlan1.getId()));
        assertTrue(ids.contains(savedCarePlan2.getId()));
        assertTrue(ids.contains(savedCarePlan3.getId()));
    }

    /**
     * This test queries for Libraries which are referenced by Measures whose library
     * value has a url value and no version value. The matching Libraries should be
     * any Library whose url value matches the Measure library value's url part.
     * @throws Exception
     */
    @Test
    public void testCanonicalReverseChainSingle1() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Measure:depends-on:name", Collections.singletonList(uuid + "measure2"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedLibrary1.getId()));
        assertTrue(ids.contains(savedLibrary2.getId()));
        assertTrue(ids.contains(savedLibrary3.getId()));
    }

    /**
     * This test queries for Libraries which are referenced by Measures whose library
     * value has a url value and a version value. The matching Libraries should be
     * any Library whose url value and version value matches the Measure library value's
     * url and version parts.
     * @throws Exception
     */
    @Test
    public void testCanonicalReverseChainSingle2() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Measure:depends-on:name", Collections.singletonList(uuid + "measure1"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedLibrary1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Libraries which are referenced by Measures whose library
     * value has a url value and a version value and a fragment value. The fragment value
     * will be ignored. The matching Libraries should be any Library whose url value and
     * version value matches the Measure library value's url and version parts.
     * @throws Exception
     */
    @Test
    public void testCanonicalReverseChainSingle3() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Measure:depends-on:name", Collections.singletonList(uuid + "measure3"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedLibrary2.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Libraries which are referenced by Measures which are
     * referenced by CarePlans.
     * @throws Exception
     */
    @Test
    public void testCanonicalReverseChainMultiple1() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Measure:depends-on:_has:CarePlan:instantiates-canonical:instantiates-uri",
            Collections.singletonList(uuid + "carePlan2"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedLibrary1.getId()));
        assertTrue(ids.contains(savedLibrary2.getId()));
    }

    /**
     * This test queries for Libraries which are referenced by Measures which are
     * referenced by CarePlans.
     * @throws Exception
     */
    @Test
    public void testCanonicalReverseChainMultiple2() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Measure:depends-on:_has:CarePlan:instantiates-canonical:instantiates-uri",
            Collections.singletonList(uuid + "carePlan3"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedLibrary1.getId()));
        assertTrue(ids.contains(savedLibrary2.getId()));
        assertTrue(ids.contains(savedLibrary3.getId()));
    }

    /**
     * This test queries for Libraries which are referenced by Measures which are
     * referenced by CarePlans.
     * @throws Exception
     */
    @Test
    public void testCanonicalReverseChainMultiple3() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:Measure:depends-on:_has:CarePlan:instantiates-canonical:instantiates-uri",
            Collections.singletonList(uuid + "carePlan1"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedLibrary1.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Measures which are referenced by CarePlans which
     * reference other CarePlans with a given instantiates-uri value.
     * @throws Exception
     */
    @Test
    public void testCanonicalReverseChainWithChain() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_has:CarePlan:instantiates-canonical:based-on.instantiates-uri",
            Collections.singletonList(uuid + "carePlan1"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedMeasure2.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Measures and Libraries referenced by them.
     * @throws Exception
     */
    @Test
    public void testCanonicalIncludeSingle1() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_include", Collections.singletonList("Measure:depends-on:Library"));
        queryParms.put("name", Collections.singletonList(uuid + "measure1"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedMeasure1.getId()));
        assertTrue(ids.contains(savedLibrary1.getId()));
    }

    /**
     * This test queries for Measures and Libraries referenced by them.
     * @throws Exception
     */
    @Test
    public void testCanonicalIncludeSingle2() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_include", Collections.singletonList("Measure:depends-on:Library"));
        queryParms.put("name", Collections.singletonList(uuid + "measure2"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(4, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedMeasure2.getId()));
        assertTrue(ids.contains(savedLibrary1.getId()));
        assertTrue(ids.contains(savedLibrary2.getId()));
        assertTrue(ids.contains(savedLibrary3.getId()));
    }

    /**
     * This test queries for Measures and Libraries referenced by them.
     * @throws Exception
     */
    @Test
    public void testCanonicalIncludeSingle3() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_include", Collections.singletonList("Measure:depends-on:Library"));
        queryParms.put("name", Collections.singletonList(uuid + "measure3"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedMeasure3.getId()));
        assertTrue(ids.contains(savedLibrary2.getId()));
    }

    /**
     * This test queries for CarePlans, and Measures and other CarePlans referenced by them.
     * @throws Exception
     */
    @Test
    public void testCanonicalIncludeMixed() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_include",
            Arrays.asList(new String[] {"CarePlan:instantiates-canonical:Measure", "CarePlan:based-on"}));
        queryParms.put("instantiates-uri", Collections.singletonList(uuid + "carePlan3"));
        List<Resource> resources = runQueryTest(CarePlan.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedCarePlan3.getId()));
        assertTrue(ids.contains(savedMeasure2.getId()));
        assertTrue(ids.contains(savedCarePlan1.getId()));
    }

    /**
     * This test queries for Libraries and Measures which reference them.
     * @throws Exception
     */
    @Test
    public void testCanonicalRevincludeSingle1() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_revinclude", Collections.singletonList("Measure:depends-on"));
        queryParms.put("name", Collections.singletonList(uuid + "library1"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedLibrary1.getId()));
        assertTrue(ids.contains(savedMeasure1.getId()));
        assertTrue(ids.contains(savedMeasure2.getId()));
    }

    /**
     * This test queries for Libraries and Measures which reference them.
     * @throws Exception
     */
    @Test
    public void testCanonicalRevincludeSingle2() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_revinclude", Collections.singletonList("Measure:depends-on"));
        queryParms.put("name", Collections.singletonList(uuid + "library2"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedLibrary2.getId()));
        assertTrue(ids.contains(savedMeasure2.getId()));
        assertTrue(ids.contains(savedMeasure3.getId()));
    }

    /**
     * This test queries for Libraries and Measures which reference them.
     * @throws Exception
     */
    @Test
    public void testCanonicalRevincludeSingle3() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_revinclude", Collections.singletonList("Measure:depends-on"));
        queryParms.put("name", Collections.singletonList(uuid + "library3"));
        List<Resource> resources = runQueryTest(Library.class, queryParms);
        assertNotNull(resources);
        assertEquals(2, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedLibrary3.getId()));
        assertTrue(ids.contains(savedMeasure2.getId()));
    }

    /**
     * This test queries for Measures, and for Libraries which they reference,
     * and for CarePlans which reference them.
     * @throws Exception
     */
    @Test
    public void testCanonicalIncludeRevinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_include", Collections.singletonList("Measure:depends-on:Library"));
        queryParms.put("_revinclude", Collections.singletonList("CarePlan:instantiates-canonical"));
        queryParms.put("name", Collections.singletonList(uuid + "measure2"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(5, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedMeasure2.getId()));
        assertTrue(ids.contains(savedLibrary1.getId()));
        assertTrue(ids.contains(savedLibrary2.getId()));
        assertTrue(ids.contains(savedLibrary3.getId()));
        assertTrue(ids.contains(savedCarePlan3.getId()));
    }

    /**
     * This test queries for Measures which are missing depends-on.
     * @throws Exception
     */
    @Test
    public void testCanonicalMissing() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("depends-on:missing", Collections.singletonList("true"));
        queryParms.put("name", Collections.singletonList(uuid + "measure"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(savedMeasure4.getId(), resources.get(0).getId());
    }

    /**
     * This test queries for Measures which are missing depends-on.
     * @throws Exception
     */
    @Test
    public void testCanonicalNotMissing() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("depends-on:missing", Collections.singletonList("false"));
        queryParms.put("name", Collections.singletonList(uuid + "measure"));
        List<Resource> resources = runQueryTest(Measure.class, queryParms);
        assertNotNull(resources);
        assertEquals(3, resources.size());
        List<String> ids = new ArrayList<>();
        for (Resource resource : resources) {
            ids.add(resource.getId());
        }
        assertTrue(ids.contains(savedMeasure1.getId()));
        assertTrue(ids.contains(savedMeasure2.getId()));
        assertTrue(ids.contains(savedMeasure3.getId()));
    }

}