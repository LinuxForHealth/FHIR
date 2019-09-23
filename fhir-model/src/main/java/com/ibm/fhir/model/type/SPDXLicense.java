/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class SPDXLicense extends Code {
    /**
     * Not open source
     */
    public static final SPDXLicense LICENSE_NOT_OPEN_SOURCE = SPDXLicense.of(ValueSet.LICENSE_NOT_OPEN_SOURCE);

    /**
     * BSD Zero Clause License
     */
    public static final SPDXLicense LICENSE_0BSD = SPDXLicense.of(ValueSet.LICENSE_0BSD);

    /**
     * Attribution Assurance License
     */
    public static final SPDXLicense LICENSE_AAL = SPDXLicense.of(ValueSet.LICENSE_AAL);

    /**
     * Abstyles License
     */
    public static final SPDXLicense LICENSE_ABSTYLES = SPDXLicense.of(ValueSet.LICENSE_ABSTYLES);

    /**
     * Adobe Systems Incorporated Source Code License Agreement
     */
    public static final SPDXLicense LICENSE_ADOBE_2006 = SPDXLicense.of(ValueSet.LICENSE_ADOBE_2006);

    /**
     * Adobe Glyph List License
     */
    public static final SPDXLicense LICENSE_ADOBE_GLYPH = SPDXLicense.of(ValueSet.LICENSE_ADOBE_GLYPH);

    /**
     * Amazon Digital Services License
     */
    public static final SPDXLicense LICENSE_ADSL = SPDXLicense.of(ValueSet.LICENSE_ADSL);

    /**
     * Academic Free License v1.1
     */
    public static final SPDXLicense LICENSE_AFL_1_1 = SPDXLicense.of(ValueSet.LICENSE_AFL_1_1);

    /**
     * Academic Free License v1.2
     */
    public static final SPDXLicense LICENSE_AFL_1_2 = SPDXLicense.of(ValueSet.LICENSE_AFL_1_2);

    /**
     * Academic Free License v2.0
     */
    public static final SPDXLicense LICENSE_AFL_2_0 = SPDXLicense.of(ValueSet.LICENSE_AFL_2_0);

    /**
     * Academic Free License v2.1
     */
    public static final SPDXLicense LICENSE_AFL_2_1 = SPDXLicense.of(ValueSet.LICENSE_AFL_2_1);

    /**
     * Academic Free License v3.0
     */
    public static final SPDXLicense LICENSE_AFL_3_0 = SPDXLicense.of(ValueSet.LICENSE_AFL_3_0);

    /**
     * Afmparse License
     */
    public static final SPDXLicense LICENSE_AFMPARSE = SPDXLicense.of(ValueSet.LICENSE_AFMPARSE);

    /**
     * Affero General Public License v1.0 only
     */
    public static final SPDXLicense LICENSE_AGPL_1_0_ONLY = SPDXLicense.of(ValueSet.LICENSE_AGPL_1_0_ONLY);

    /**
     * Affero General Public License v1.0 or later
     */
    public static final SPDXLicense LICENSE_AGPL_1_0_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_AGPL_1_0_OR_LATER);

    /**
     * GNU Affero General Public License v3.0 only
     */
    public static final SPDXLicense LICENSE_AGPL_3_0_ONLY = SPDXLicense.of(ValueSet.LICENSE_AGPL_3_0_ONLY);

    /**
     * GNU Affero General Public License v3.0 or later
     */
    public static final SPDXLicense LICENSE_AGPL_3_0_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_AGPL_3_0_OR_LATER);

    /**
     * Aladdin Free Public License
     */
    public static final SPDXLicense LICENSE_ALADDIN = SPDXLicense.of(ValueSet.LICENSE_ALADDIN);

    /**
     * AMD's plpa_map.c License
     */
    public static final SPDXLicense LICENSE_AMDPLPA = SPDXLicense.of(ValueSet.LICENSE_AMDPLPA);

    /**
     * Apple MIT License
     */
    public static final SPDXLicense LICENSE_AML = SPDXLicense.of(ValueSet.LICENSE_AML);

    /**
     * Academy of Motion Picture Arts and Sciences BSD
     */
    public static final SPDXLicense LICENSE_AMPAS = SPDXLicense.of(ValueSet.LICENSE_AMPAS);

    /**
     * ANTLR Software Rights Notice
     */
    public static final SPDXLicense LICENSE_ANTLR_PD = SPDXLicense.of(ValueSet.LICENSE_ANTLR_PD);

    /**
     * Apache License 1.0
     */
    public static final SPDXLicense LICENSE_APACHE_1_0 = SPDXLicense.of(ValueSet.LICENSE_APACHE_1_0);

    /**
     * Apache License 1.1
     */
    public static final SPDXLicense LICENSE_APACHE_1_1 = SPDXLicense.of(ValueSet.LICENSE_APACHE_1_1);

    /**
     * Apache License 2.0
     */
    public static final SPDXLicense LICENSE_APACHE_2_0 = SPDXLicense.of(ValueSet.LICENSE_APACHE_2_0);

    /**
     * Adobe Postscript AFM License
     */
    public static final SPDXLicense LICENSE_APAFML = SPDXLicense.of(ValueSet.LICENSE_APAFML);

    /**
     * Adaptive Public License 1.0
     */
    public static final SPDXLicense LICENSE_APL_1_0 = SPDXLicense.of(ValueSet.LICENSE_APL_1_0);

    /**
     * Apple Public Source License 1.0
     */
    public static final SPDXLicense LICENSE_APSL_1_0 = SPDXLicense.of(ValueSet.LICENSE_APSL_1_0);

    /**
     * Apple Public Source License 1.1
     */
    public static final SPDXLicense LICENSE_APSL_1_1 = SPDXLicense.of(ValueSet.LICENSE_APSL_1_1);

    /**
     * Apple Public Source License 1.2
     */
    public static final SPDXLicense LICENSE_APSL_1_2 = SPDXLicense.of(ValueSet.LICENSE_APSL_1_2);

    /**
     * Apple Public Source License 2.0
     */
    public static final SPDXLicense LICENSE_APSL_2_0 = SPDXLicense.of(ValueSet.LICENSE_APSL_2_0);

    /**
     * Artistic License 1.0 w/clause 8
     */
    public static final SPDXLicense LICENSE_ARTISTIC_1_0_CL8 = SPDXLicense.of(ValueSet.LICENSE_ARTISTIC_1_0_CL8);

    /**
     * Artistic License 1.0 (Perl)
     */
    public static final SPDXLicense LICENSE_ARTISTIC_1_0_PERL = SPDXLicense.of(ValueSet.LICENSE_ARTISTIC_1_0_PERL);

    /**
     * Artistic License 1.0
     */
    public static final SPDXLicense LICENSE_ARTISTIC_1_0 = SPDXLicense.of(ValueSet.LICENSE_ARTISTIC_1_0);

    /**
     * Artistic License 2.0
     */
    public static final SPDXLicense LICENSE_ARTISTIC_2_0 = SPDXLicense.of(ValueSet.LICENSE_ARTISTIC_2_0);

    /**
     * Bahyph License
     */
    public static final SPDXLicense LICENSE_BAHYPH = SPDXLicense.of(ValueSet.LICENSE_BAHYPH);

    /**
     * Barr License
     */
    public static final SPDXLicense LICENSE_BARR = SPDXLicense.of(ValueSet.LICENSE_BARR);

    /**
     * Beerware License
     */
    public static final SPDXLicense LICENSE_BEERWARE = SPDXLicense.of(ValueSet.LICENSE_BEERWARE);

    /**
     * BitTorrent Open Source License v1.0
     */
    public static final SPDXLicense LICENSE_BIT_TORRENT_1_0 = SPDXLicense.of(ValueSet.LICENSE_BIT_TORRENT_1_0);

    /**
     * BitTorrent Open Source License v1.1
     */
    public static final SPDXLicense LICENSE_BIT_TORRENT_1_1 = SPDXLicense.of(ValueSet.LICENSE_BIT_TORRENT_1_1);

    /**
     * Borceux license
     */
    public static final SPDXLicense LICENSE_BORCEUX = SPDXLicense.of(ValueSet.LICENSE_BORCEUX);

    /**
     * BSD 1-Clause License
     */
    public static final SPDXLicense LICENSE_BSD_1_CLAUSE = SPDXLicense.of(ValueSet.LICENSE_BSD_1_CLAUSE);

    /**
     * BSD 2-Clause FreeBSD License
     */
    public static final SPDXLicense LICENSE_BSD_2_CLAUSE_FREE_BSD = SPDXLicense.of(ValueSet.LICENSE_BSD_2_CLAUSE_FREE_BSD);

    /**
     * BSD 2-Clause NetBSD License
     */
    public static final SPDXLicense LICENSE_BSD_2_CLAUSE_NET_BSD = SPDXLicense.of(ValueSet.LICENSE_BSD_2_CLAUSE_NET_BSD);

    /**
     * BSD-2-Clause Plus Patent License
     */
    public static final SPDXLicense LICENSE_BSD_2_CLAUSE_PATENT = SPDXLicense.of(ValueSet.LICENSE_BSD_2_CLAUSE_PATENT);

    /**
     * BSD 2-Clause "Simplified" License
     */
    public static final SPDXLicense LICENSE_BSD_2_CLAUSE = SPDXLicense.of(ValueSet.LICENSE_BSD_2_CLAUSE);

    /**
     * BSD with attribution
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_ATTRIBUTION = SPDXLicense.of(ValueSet.LICENSE_BSD_3_CLAUSE_ATTRIBUTION);

    /**
     * BSD 3-Clause Clear License
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_CLEAR = SPDXLicense.of(ValueSet.LICENSE_BSD_3_CLAUSE_CLEAR);

    /**
     * Lawrence Berkeley National Labs BSD variant license
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_LBNL = SPDXLicense.of(ValueSet.LICENSE_BSD_3_CLAUSE_LBNL);

    /**
     * BSD 3-Clause No Nuclear License 2014
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE_2014 = SPDXLicense.of(ValueSet.LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE_2014);

    /**
     * BSD 3-Clause No Nuclear License
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE = SPDXLicense.of(ValueSet.LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE);

    /**
     * BSD 3-Clause No Nuclear Warranty
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_WARRANTY = SPDXLicense.of(ValueSet.LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_WARRANTY);

    /**
     * BSD 3-Clause "New" or "Revised" License
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE = SPDXLicense.of(ValueSet.LICENSE_BSD_3_CLAUSE);

    /**
     * BSD-4-Clause (University of California-Specific)
     */
    public static final SPDXLicense LICENSE_BSD_4_CLAUSE_UC = SPDXLicense.of(ValueSet.LICENSE_BSD_4_CLAUSE_UC);

    /**
     * BSD 4-Clause "Original" or "Old" License
     */
    public static final SPDXLicense LICENSE_BSD_4_CLAUSE = SPDXLicense.of(ValueSet.LICENSE_BSD_4_CLAUSE);

    /**
     * BSD Protection License
     */
    public static final SPDXLicense LICENSE_BSD_PROTECTION = SPDXLicense.of(ValueSet.LICENSE_BSD_PROTECTION);

    /**
     * BSD Source Code Attribution
     */
    public static final SPDXLicense LICENSE_BSD_SOURCE_CODE = SPDXLicense.of(ValueSet.LICENSE_BSD_SOURCE_CODE);

    /**
     * Boost Software License 1.0
     */
    public static final SPDXLicense LICENSE_BSL_1_0 = SPDXLicense.of(ValueSet.LICENSE_BSL_1_0);

    /**
     * bzip2 and libbzip2 License v1.0.5
     */
    public static final SPDXLicense LICENSE_BZIP2_1_0_5 = SPDXLicense.of(ValueSet.LICENSE_BZIP2_1_0_5);

    /**
     * bzip2 and libbzip2 License v1.0.6
     */
    public static final SPDXLicense LICENSE_BZIP2_1_0_6 = SPDXLicense.of(ValueSet.LICENSE_BZIP2_1_0_6);

    /**
     * Caldera License
     */
    public static final SPDXLicense LICENSE_CALDERA = SPDXLicense.of(ValueSet.LICENSE_CALDERA);

    /**
     * Computer Associates Trusted Open Source License 1.1
     */
    public static final SPDXLicense LICENSE_CATOSL_1_1 = SPDXLicense.of(ValueSet.LICENSE_CATOSL_1_1);

    /**
     * Creative Commons Attribution 1.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_1_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_1_0);

    /**
     * Creative Commons Attribution 2.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_2_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_2_0);

    /**
     * Creative Commons Attribution 2.5 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_2_5 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_2_5);

    /**
     * Creative Commons Attribution 3.0 Unported
     */
    public static final SPDXLicense LICENSE_CC_BY_3_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_3_0);

    /**
     * Creative Commons Attribution 4.0 International
     */
    public static final SPDXLicense LICENSE_CC_BY_4_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_4_0);

    /**
     * Creative Commons Attribution Non Commercial 1.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_1_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_1_0);

    /**
     * Creative Commons Attribution Non Commercial 2.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_2_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_2_0);

    /**
     * Creative Commons Attribution Non Commercial 2.5 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_2_5 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_2_5);

    /**
     * Creative Commons Attribution Non Commercial 3.0 Unported
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_3_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_3_0);

    /**
     * Creative Commons Attribution Non Commercial 4.0 International
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_4_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_4_0);

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 1.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_1_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_ND_1_0);

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 2.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_2_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_ND_2_0);

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 2.5 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_2_5 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_ND_2_5);

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 3.0 Unported
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_3_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_ND_3_0);

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 4.0 International
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_4_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_ND_4_0);

    /**
     * Creative Commons Attribution Non Commercial Share Alike 1.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_1_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_SA_1_0);

    /**
     * Creative Commons Attribution Non Commercial Share Alike 2.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_2_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_SA_2_0);

    /**
     * Creative Commons Attribution Non Commercial Share Alike 2.5 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_2_5 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_SA_2_5);

    /**
     * Creative Commons Attribution Non Commercial Share Alike 3.0 Unported
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_3_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_SA_3_0);

    /**
     * Creative Commons Attribution Non Commercial Share Alike 4.0 International
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_4_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_NC_SA_4_0);

    /**
     * Creative Commons Attribution No Derivatives 1.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_1_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_ND_1_0);

    /**
     * Creative Commons Attribution No Derivatives 2.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_2_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_ND_2_0);

    /**
     * Creative Commons Attribution No Derivatives 2.5 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_2_5 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_ND_2_5);

    /**
     * Creative Commons Attribution No Derivatives 3.0 Unported
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_3_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_ND_3_0);

    /**
     * Creative Commons Attribution No Derivatives 4.0 International
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_4_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_ND_4_0);

    /**
     * Creative Commons Attribution Share Alike 1.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_1_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_SA_1_0);

    /**
     * Creative Commons Attribution Share Alike 2.0 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_2_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_SA_2_0);

    /**
     * Creative Commons Attribution Share Alike 2.5 Generic
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_2_5 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_SA_2_5);

    /**
     * Creative Commons Attribution Share Alike 3.0 Unported
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_3_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_SA_3_0);

    /**
     * Creative Commons Attribution Share Alike 4.0 International
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_4_0 = SPDXLicense.of(ValueSet.LICENSE_CC_BY_SA_4_0);

    /**
     * Creative Commons Zero v1.0 Universal
     */
    public static final SPDXLicense LICENSE_CC0_1_0 = SPDXLicense.of(ValueSet.LICENSE_CC0_1_0);

    /**
     * Common Development and Distribution License 1.0
     */
    public static final SPDXLicense LICENSE_CDDL_1_0 = SPDXLicense.of(ValueSet.LICENSE_CDDL_1_0);

    /**
     * Common Development and Distribution License 1.1
     */
    public static final SPDXLicense LICENSE_CDDL_1_1 = SPDXLicense.of(ValueSet.LICENSE_CDDL_1_1);

    /**
     * Community Data License Agreement Permissive 1.0
     */
    public static final SPDXLicense LICENSE_CDLA_PERMISSIVE_1_0 = SPDXLicense.of(ValueSet.LICENSE_CDLA_PERMISSIVE_1_0);

    /**
     * Community Data License Agreement Sharing 1.0
     */
    public static final SPDXLicense LICENSE_CDLA_SHARING_1_0 = SPDXLicense.of(ValueSet.LICENSE_CDLA_SHARING_1_0);

    /**
     * CeCILL Free Software License Agreement v1.0
     */
    public static final SPDXLicense LICENSE_CECILL_1_0 = SPDXLicense.of(ValueSet.LICENSE_CECILL_1_0);

    /**
     * CeCILL Free Software License Agreement v1.1
     */
    public static final SPDXLicense LICENSE_CECILL_1_1 = SPDXLicense.of(ValueSet.LICENSE_CECILL_1_1);

    /**
     * CeCILL Free Software License Agreement v2.0
     */
    public static final SPDXLicense LICENSE_CECILL_2_0 = SPDXLicense.of(ValueSet.LICENSE_CECILL_2_0);

    /**
     * CeCILL Free Software License Agreement v2.1
     */
    public static final SPDXLicense LICENSE_CECILL_2_1 = SPDXLicense.of(ValueSet.LICENSE_CECILL_2_1);

    /**
     * CeCILL-B Free Software License Agreement
     */
    public static final SPDXLicense LICENSE_CECILL_B = SPDXLicense.of(ValueSet.LICENSE_CECILL_B);

    /**
     * CeCILL-C Free Software License Agreement
     */
    public static final SPDXLicense LICENSE_CECILL_C = SPDXLicense.of(ValueSet.LICENSE_CECILL_C);

    /**
     * Clarified Artistic License
     */
    public static final SPDXLicense LICENSE_CL_ARTISTIC = SPDXLicense.of(ValueSet.LICENSE_CL_ARTISTIC);

    /**
     * CNRI Jython License
     */
    public static final SPDXLicense LICENSE_CNRI_JYTHON = SPDXLicense.of(ValueSet.LICENSE_CNRI_JYTHON);

    /**
     * CNRI Python Open Source GPL Compatible License Agreement
     */
    public static final SPDXLicense LICENSE_CNRI_PYTHON_GPL_COMPATIBLE = SPDXLicense.of(ValueSet.LICENSE_CNRI_PYTHON_GPL_COMPATIBLE);

    /**
     * CNRI Python License
     */
    public static final SPDXLicense LICENSE_CNRI_PYTHON = SPDXLicense.of(ValueSet.LICENSE_CNRI_PYTHON);

    /**
     * Condor Public License v1.1
     */
    public static final SPDXLicense LICENSE_CONDOR_1_1 = SPDXLicense.of(ValueSet.LICENSE_CONDOR_1_1);

    /**
     * Common Public Attribution License 1.0
     */
    public static final SPDXLicense LICENSE_CPAL_1_0 = SPDXLicense.of(ValueSet.LICENSE_CPAL_1_0);

    /**
     * Common Public License 1.0
     */
    public static final SPDXLicense LICENSE_CPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_CPL_1_0);

    /**
     * Code Project Open License 1.02
     */
    public static final SPDXLicense LICENSE_CPOL_1_02 = SPDXLicense.of(ValueSet.LICENSE_CPOL_1_02);

    /**
     * Crossword License
     */
    public static final SPDXLicense LICENSE_CROSSWORD = SPDXLicense.of(ValueSet.LICENSE_CROSSWORD);

    /**
     * CrystalStacker License
     */
    public static final SPDXLicense LICENSE_CRYSTAL_STACKER = SPDXLicense.of(ValueSet.LICENSE_CRYSTAL_STACKER);

    /**
     * CUA Office Public License v1.0
     */
    public static final SPDXLicense LICENSE_CUA_OPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_CUA_OPL_1_0);

    /**
     * Cube License
     */
    public static final SPDXLicense LICENSE_CUBE = SPDXLicense.of(ValueSet.LICENSE_CUBE);

    /**
     * curl License
     */
    public static final SPDXLicense LICENSE_CURL = SPDXLicense.of(ValueSet.LICENSE_CURL);

    /**
     * Deutsche Freie Software Lizenz
     */
    public static final SPDXLicense LICENSE_D_FSL_1_0 = SPDXLicense.of(ValueSet.LICENSE_D_FSL_1_0);

    /**
     * diffmark license
     */
    public static final SPDXLicense LICENSE_DIFFMARK = SPDXLicense.of(ValueSet.LICENSE_DIFFMARK);

    /**
     * DOC License
     */
    public static final SPDXLicense LICENSE_DOC = SPDXLicense.of(ValueSet.LICENSE_DOC);

    /**
     * Dotseqn License
     */
    public static final SPDXLicense LICENSE_DOTSEQN = SPDXLicense.of(ValueSet.LICENSE_DOTSEQN);

    /**
     * DSDP License
     */
    public static final SPDXLicense LICENSE_DSDP = SPDXLicense.of(ValueSet.LICENSE_DSDP);

    /**
     * dvipdfm License
     */
    public static final SPDXLicense LICENSE_DVIPDFM = SPDXLicense.of(ValueSet.LICENSE_DVIPDFM);

    /**
     * Educational Community License v1.0
     */
    public static final SPDXLicense LICENSE_ECL_1_0 = SPDXLicense.of(ValueSet.LICENSE_ECL_1_0);

    /**
     * Educational Community License v2.0
     */
    public static final SPDXLicense LICENSE_ECL_2_0 = SPDXLicense.of(ValueSet.LICENSE_ECL_2_0);

    /**
     * Eiffel Forum License v1.0
     */
    public static final SPDXLicense LICENSE_EFL_1_0 = SPDXLicense.of(ValueSet.LICENSE_EFL_1_0);

    /**
     * Eiffel Forum License v2.0
     */
    public static final SPDXLicense LICENSE_EFL_2_0 = SPDXLicense.of(ValueSet.LICENSE_EFL_2_0);

    /**
     * eGenix.com Public License 1.1.0
     */
    public static final SPDXLicense LICENSE_E_GENIX = SPDXLicense.of(ValueSet.LICENSE_E_GENIX);

    /**
     * Entessa Public License v1.0
     */
    public static final SPDXLicense LICENSE_ENTESSA = SPDXLicense.of(ValueSet.LICENSE_ENTESSA);

    /**
     * Eclipse Public License 1.0
     */
    public static final SPDXLicense LICENSE_EPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_EPL_1_0);

    /**
     * Eclipse Public License 2.0
     */
    public static final SPDXLicense LICENSE_EPL_2_0 = SPDXLicense.of(ValueSet.LICENSE_EPL_2_0);

    /**
     * Erlang Public License v1.1
     */
    public static final SPDXLicense LICENSE_ERL_PL_1_1 = SPDXLicense.of(ValueSet.LICENSE_ERL_PL_1_1);

    /**
     * EU DataGrid Software License
     */
    public static final SPDXLicense LICENSE_EUDATAGRID = SPDXLicense.of(ValueSet.LICENSE_EUDATAGRID);

    /**
     * European Union Public License 1.0
     */
    public static final SPDXLicense LICENSE_EUPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_EUPL_1_0);

    /**
     * European Union Public License 1.1
     */
    public static final SPDXLicense LICENSE_EUPL_1_1 = SPDXLicense.of(ValueSet.LICENSE_EUPL_1_1);

    /**
     * European Union Public License 1.2
     */
    public static final SPDXLicense LICENSE_EUPL_1_2 = SPDXLicense.of(ValueSet.LICENSE_EUPL_1_2);

    /**
     * Eurosym License
     */
    public static final SPDXLicense LICENSE_EUROSYM = SPDXLicense.of(ValueSet.LICENSE_EUROSYM);

    /**
     * Fair License
     */
    public static final SPDXLicense LICENSE_FAIR = SPDXLicense.of(ValueSet.LICENSE_FAIR);

    /**
     * Frameworx Open License 1.0
     */
    public static final SPDXLicense LICENSE_FRAMEWORX_1_0 = SPDXLicense.of(ValueSet.LICENSE_FRAMEWORX_1_0);

    /**
     * FreeImage Public License v1.0
     */
    public static final SPDXLicense LICENSE_FREE_IMAGE = SPDXLicense.of(ValueSet.LICENSE_FREE_IMAGE);

    /**
     * FSF All Permissive License
     */
    public static final SPDXLicense LICENSE_FSFAP = SPDXLicense.of(ValueSet.LICENSE_FSFAP);

    /**
     * FSF Unlimited License
     */
    public static final SPDXLicense LICENSE_FSFUL = SPDXLicense.of(ValueSet.LICENSE_FSFUL);

    /**
     * FSF Unlimited License (with License Retention)
     */
    public static final SPDXLicense LICENSE_FSFULLR = SPDXLicense.of(ValueSet.LICENSE_FSFULLR);

    /**
     * Freetype Project License
     */
    public static final SPDXLicense LICENSE_FTL = SPDXLicense.of(ValueSet.LICENSE_FTL);

    /**
     * GNU Free Documentation License v1.1 only
     */
    public static final SPDXLicense LICENSE_GFDL_1_1_ONLY = SPDXLicense.of(ValueSet.LICENSE_GFDL_1_1_ONLY);

    /**
     * GNU Free Documentation License v1.1 or later
     */
    public static final SPDXLicense LICENSE_GFDL_1_1_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_GFDL_1_1_OR_LATER);

    /**
     * GNU Free Documentation License v1.2 only
     */
    public static final SPDXLicense LICENSE_GFDL_1_2_ONLY = SPDXLicense.of(ValueSet.LICENSE_GFDL_1_2_ONLY);

    /**
     * GNU Free Documentation License v1.2 or later
     */
    public static final SPDXLicense LICENSE_GFDL_1_2_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_GFDL_1_2_OR_LATER);

    /**
     * GNU Free Documentation License v1.3 only
     */
    public static final SPDXLicense LICENSE_GFDL_1_3_ONLY = SPDXLicense.of(ValueSet.LICENSE_GFDL_1_3_ONLY);

    /**
     * GNU Free Documentation License v1.3 or later
     */
    public static final SPDXLicense LICENSE_GFDL_1_3_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_GFDL_1_3_OR_LATER);

    /**
     * Giftware License
     */
    public static final SPDXLicense LICENSE_GIFTWARE = SPDXLicense.of(ValueSet.LICENSE_GIFTWARE);

    /**
     * GL2PS License
     */
    public static final SPDXLicense LICENSE_GL2PS = SPDXLicense.of(ValueSet.LICENSE_GL2PS);

    /**
     * 3dfx Glide License
     */
    public static final SPDXLicense LICENSE_GLIDE = SPDXLicense.of(ValueSet.LICENSE_GLIDE);

    /**
     * Glulxe License
     */
    public static final SPDXLicense LICENSE_GLULXE = SPDXLicense.of(ValueSet.LICENSE_GLULXE);

    /**
     * gnuplot License
     */
    public static final SPDXLicense LICENSE_GNUPLOT = SPDXLicense.of(ValueSet.LICENSE_GNUPLOT);

    /**
     * GNU General Public License v1.0 only
     */
    public static final SPDXLicense LICENSE_GPL_1_0_ONLY = SPDXLicense.of(ValueSet.LICENSE_GPL_1_0_ONLY);

    /**
     * GNU General Public License v1.0 or later
     */
    public static final SPDXLicense LICENSE_GPL_1_0_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_GPL_1_0_OR_LATER);

    /**
     * GNU General Public License v2.0 only
     */
    public static final SPDXLicense LICENSE_GPL_2_0_ONLY = SPDXLicense.of(ValueSet.LICENSE_GPL_2_0_ONLY);

    /**
     * GNU General Public License v2.0 or later
     */
    public static final SPDXLicense LICENSE_GPL_2_0_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_GPL_2_0_OR_LATER);

    /**
     * GNU General Public License v3.0 only
     */
    public static final SPDXLicense LICENSE_GPL_3_0_ONLY = SPDXLicense.of(ValueSet.LICENSE_GPL_3_0_ONLY);

    /**
     * GNU General Public License v3.0 or later
     */
    public static final SPDXLicense LICENSE_GPL_3_0_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_GPL_3_0_OR_LATER);

    /**
     * gSOAP Public License v1.3b
     */
    public static final SPDXLicense LICENSE_G_SOAP_1_3B = SPDXLicense.of(ValueSet.LICENSE_G_SOAP_1_3B);

    /**
     * Haskell Language Report License
     */
    public static final SPDXLicense LICENSE_HASKELL_REPORT = SPDXLicense.of(ValueSet.LICENSE_HASKELL_REPORT);

    /**
     * Historical Permission Notice and Disclaimer
     */
    public static final SPDXLicense LICENSE_HPND = SPDXLicense.of(ValueSet.LICENSE_HPND);

    /**
     * IBM PowerPC Initialization and Boot Software
     */
    public static final SPDXLicense LICENSE_IBM_PIBS = SPDXLicense.of(ValueSet.LICENSE_IBM_PIBS);

    /**
     * ICU License
     */
    public static final SPDXLicense LICENSE_ICU = SPDXLicense.of(ValueSet.LICENSE_ICU);

    /**
     * Independent JPEG Group License
     */
    public static final SPDXLicense LICENSE_IJG = SPDXLicense.of(ValueSet.LICENSE_IJG);

    /**
     * ImageMagick License
     */
    public static final SPDXLicense LICENSE_IMAGE_MAGICK = SPDXLicense.of(ValueSet.LICENSE_IMAGE_MAGICK);

    /**
     * iMatix Standard Function Library Agreement
     */
    public static final SPDXLicense LICENSE_I_MATIX = SPDXLicense.of(ValueSet.LICENSE_I_MATIX);

    /**
     * Imlib2 License
     */
    public static final SPDXLicense LICENSE_IMLIB2 = SPDXLicense.of(ValueSet.LICENSE_IMLIB2);

    /**
     * Info-ZIP License
     */
    public static final SPDXLicense LICENSE_INFO_ZIP = SPDXLicense.of(ValueSet.LICENSE_INFO_ZIP);

    /**
     * Intel ACPI Software License Agreement
     */
    public static final SPDXLicense LICENSE_INTEL_ACPI = SPDXLicense.of(ValueSet.LICENSE_INTEL_ACPI);

    /**
     * Intel Open Source License
     */
    public static final SPDXLicense LICENSE_INTEL = SPDXLicense.of(ValueSet.LICENSE_INTEL);

    /**
     * Interbase Public License v1.0
     */
    public static final SPDXLicense LICENSE_INTERBASE_1_0 = SPDXLicense.of(ValueSet.LICENSE_INTERBASE_1_0);

    /**
     * IPA Font License
     */
    public static final SPDXLicense LICENSE_IPA = SPDXLicense.of(ValueSet.LICENSE_IPA);

    /**
     * IBM Public License v1.0
     */
    public static final SPDXLicense LICENSE_IPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_IPL_1_0);

    /**
     * ISC License
     */
    public static final SPDXLicense LICENSE_ISC = SPDXLicense.of(ValueSet.LICENSE_ISC);

    /**
     * JasPer License
     */
    public static final SPDXLicense LICENSE_JAS_PER_2_0 = SPDXLicense.of(ValueSet.LICENSE_JAS_PER_2_0);

    /**
     * JSON License
     */
    public static final SPDXLicense LICENSE_JSON = SPDXLicense.of(ValueSet.LICENSE_JSON);

    /**
     * Licence Art Libre 1.2
     */
    public static final SPDXLicense LICENSE_LAL_1_2 = SPDXLicense.of(ValueSet.LICENSE_LAL_1_2);

    /**
     * Licence Art Libre 1.3
     */
    public static final SPDXLicense LICENSE_LAL_1_3 = SPDXLicense.of(ValueSet.LICENSE_LAL_1_3);

    /**
     * Latex2e License
     */
    public static final SPDXLicense LICENSE_LATEX2E = SPDXLicense.of(ValueSet.LICENSE_LATEX2E);

    /**
     * Leptonica License
     */
    public static final SPDXLicense LICENSE_LEPTONICA = SPDXLicense.of(ValueSet.LICENSE_LEPTONICA);

    /**
     * GNU Library General Public License v2 only
     */
    public static final SPDXLicense LICENSE_LGPL_2_0_ONLY = SPDXLicense.of(ValueSet.LICENSE_LGPL_2_0_ONLY);

    /**
     * GNU Library General Public License v2 or later
     */
    public static final SPDXLicense LICENSE_LGPL_2_0_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_LGPL_2_0_OR_LATER);

    /**
     * GNU Lesser General Public License v2.1 only
     */
    public static final SPDXLicense LICENSE_LGPL_2_1_ONLY = SPDXLicense.of(ValueSet.LICENSE_LGPL_2_1_ONLY);

    /**
     * GNU Lesser General Public License v2.1 or later
     */
    public static final SPDXLicense LICENSE_LGPL_2_1_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_LGPL_2_1_OR_LATER);

    /**
     * GNU Lesser General Public License v3.0 only
     */
    public static final SPDXLicense LICENSE_LGPL_3_0_ONLY = SPDXLicense.of(ValueSet.LICENSE_LGPL_3_0_ONLY);

    /**
     * GNU Lesser General Public License v3.0 or later
     */
    public static final SPDXLicense LICENSE_LGPL_3_0_OR_LATER = SPDXLicense.of(ValueSet.LICENSE_LGPL_3_0_OR_LATER);

    /**
     * Lesser General Public License For Linguistic Resources
     */
    public static final SPDXLicense LICENSE_LGPLLR = SPDXLicense.of(ValueSet.LICENSE_LGPLLR);

    /**
     * libpng License
     */
    public static final SPDXLicense LICENSE_LIBPNG = SPDXLicense.of(ValueSet.LICENSE_LIBPNG);

    /**
     * libtiff License
     */
    public static final SPDXLicense LICENSE_LIBTIFF = SPDXLicense.of(ValueSet.LICENSE_LIBTIFF);

    /**
     * Licence Libre du Québec – Permissive version 1.1
     */
    public static final SPDXLicense LICENSE_LI_LI_Q_P_1_1 = SPDXLicense.of(ValueSet.LICENSE_LI_LI_Q_P_1_1);

    /**
     * Licence Libre du Québec – Réciprocité version 1.1
     */
    public static final SPDXLicense LICENSE_LI_LI_Q_R_1_1 = SPDXLicense.of(ValueSet.LICENSE_LI_LI_Q_R_1_1);

    /**
     * Licence Libre du Québec – Réciprocité forte version 1.1
     */
    public static final SPDXLicense LICENSE_LI_LI_Q_RPLUS_1_1 = SPDXLicense.of(ValueSet.LICENSE_LI_LI_Q_RPLUS_1_1);

    /**
     * Linux Kernel Variant of OpenIB.org license
     */
    public static final SPDXLicense LICENSE_LINUX_OPEN_IB = SPDXLicense.of(ValueSet.LICENSE_LINUX_OPEN_IB);

    /**
     * Lucent Public License Version 1.0
     */
    public static final SPDXLicense LICENSE_LPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_LPL_1_0);

    /**
     * Lucent Public License v1.02
     */
    public static final SPDXLicense LICENSE_LPL_1_02 = SPDXLicense.of(ValueSet.LICENSE_LPL_1_02);

    /**
     * LaTeX Project Public License v1.0
     */
    public static final SPDXLicense LICENSE_LPPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_LPPL_1_0);

    /**
     * LaTeX Project Public License v1.1
     */
    public static final SPDXLicense LICENSE_LPPL_1_1 = SPDXLicense.of(ValueSet.LICENSE_LPPL_1_1);

    /**
     * LaTeX Project Public License v1.2
     */
    public static final SPDXLicense LICENSE_LPPL_1_2 = SPDXLicense.of(ValueSet.LICENSE_LPPL_1_2);

    /**
     * LaTeX Project Public License v1.3a
     */
    public static final SPDXLicense LICENSE_LPPL_1_3A = SPDXLicense.of(ValueSet.LICENSE_LPPL_1_3A);

    /**
     * LaTeX Project Public License v1.3c
     */
    public static final SPDXLicense LICENSE_LPPL_1_3C = SPDXLicense.of(ValueSet.LICENSE_LPPL_1_3C);

    /**
     * MakeIndex License
     */
    public static final SPDXLicense LICENSE_MAKE_INDEX = SPDXLicense.of(ValueSet.LICENSE_MAKE_INDEX);

    /**
     * MirOS License
     */
    public static final SPDXLicense LICENSE_MIR_OS = SPDXLicense.of(ValueSet.LICENSE_MIR_OS);

    /**
     * MIT No Attribution
     */
    public static final SPDXLicense LICENSE_MIT_0 = SPDXLicense.of(ValueSet.LICENSE_MIT_0);

    /**
     * Enlightenment License (e16)
     */
    public static final SPDXLicense LICENSE_MIT_ADVERTISING = SPDXLicense.of(ValueSet.LICENSE_MIT_ADVERTISING);

    /**
     * CMU License
     */
    public static final SPDXLicense LICENSE_MIT_CMU = SPDXLicense.of(ValueSet.LICENSE_MIT_CMU);

    /**
     * enna License
     */
    public static final SPDXLicense LICENSE_MIT_ENNA = SPDXLicense.of(ValueSet.LICENSE_MIT_ENNA);

    /**
     * feh License
     */
    public static final SPDXLicense LICENSE_MIT_FEH = SPDXLicense.of(ValueSet.LICENSE_MIT_FEH);

    /**
     * MIT License
     */
    public static final SPDXLicense LICENSE_MIT = SPDXLicense.of(ValueSet.LICENSE_MIT);

    /**
     * MIT +no-false-attribs license
     */
    public static final SPDXLicense LICENSE_MITNFA = SPDXLicense.of(ValueSet.LICENSE_MITNFA);

    /**
     * Motosoto License
     */
    public static final SPDXLicense LICENSE_MOTOSOTO = SPDXLicense.of(ValueSet.LICENSE_MOTOSOTO);

    /**
     * mpich2 License
     */
    public static final SPDXLicense LICENSE_MPICH2 = SPDXLicense.of(ValueSet.LICENSE_MPICH2);

    /**
     * Mozilla Public License 1.0
     */
    public static final SPDXLicense LICENSE_MPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_MPL_1_0);

    /**
     * Mozilla Public License 1.1
     */
    public static final SPDXLicense LICENSE_MPL_1_1 = SPDXLicense.of(ValueSet.LICENSE_MPL_1_1);

    /**
     * Mozilla Public License 2.0 (no copyleft exception)
     */
    public static final SPDXLicense LICENSE_MPL_2_0_NO_COPYLEFT_EXCEPTION = SPDXLicense.of(ValueSet.LICENSE_MPL_2_0_NO_COPYLEFT_EXCEPTION);

    /**
     * Mozilla Public License 2.0
     */
    public static final SPDXLicense LICENSE_MPL_2_0 = SPDXLicense.of(ValueSet.LICENSE_MPL_2_0);

    /**
     * Microsoft Public License
     */
    public static final SPDXLicense LICENSE_MS_PL = SPDXLicense.of(ValueSet.LICENSE_MS_PL);

    /**
     * Microsoft Reciprocal License
     */
    public static final SPDXLicense LICENSE_MS_RL = SPDXLicense.of(ValueSet.LICENSE_MS_RL);

    /**
     * Matrix Template Library License
     */
    public static final SPDXLicense LICENSE_MTLL = SPDXLicense.of(ValueSet.LICENSE_MTLL);

    /**
     * Multics License
     */
    public static final SPDXLicense LICENSE_MULTICS = SPDXLicense.of(ValueSet.LICENSE_MULTICS);

    /**
     * Mup License
     */
    public static final SPDXLicense LICENSE_MUP = SPDXLicense.of(ValueSet.LICENSE_MUP);

    /**
     * NASA Open Source Agreement 1.3
     */
    public static final SPDXLicense LICENSE_NASA_1_3 = SPDXLicense.of(ValueSet.LICENSE_NASA_1_3);

    /**
     * Naumen Public License
     */
    public static final SPDXLicense LICENSE_NAUMEN = SPDXLicense.of(ValueSet.LICENSE_NAUMEN);

    /**
     * Net Boolean Public License v1
     */
    public static final SPDXLicense LICENSE_NBPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_NBPL_1_0);

    /**
     * University of Illinois/NCSA Open Source License
     */
    public static final SPDXLicense LICENSE_NCSA = SPDXLicense.of(ValueSet.LICENSE_NCSA);

    /**
     * Net-SNMP License
     */
    public static final SPDXLicense LICENSE_NET_SNMP = SPDXLicense.of(ValueSet.LICENSE_NET_SNMP);

    /**
     * NetCDF license
     */
    public static final SPDXLicense LICENSE_NET_CDF = SPDXLicense.of(ValueSet.LICENSE_NET_CDF);

    /**
     * Newsletr License
     */
    public static final SPDXLicense LICENSE_NEWSLETR = SPDXLicense.of(ValueSet.LICENSE_NEWSLETR);

    /**
     * Nethack General Public License
     */
    public static final SPDXLicense LICENSE_NGPL = SPDXLicense.of(ValueSet.LICENSE_NGPL);

    /**
     * Norwegian Licence for Open Government Data
     */
    public static final SPDXLicense LICENSE_NLOD_1_0 = SPDXLicense.of(ValueSet.LICENSE_NLOD_1_0);

    /**
     * No Limit Public License
     */
    public static final SPDXLicense LICENSE_NLPL = SPDXLicense.of(ValueSet.LICENSE_NLPL);

    /**
     * Nokia Open Source License
     */
    public static final SPDXLicense LICENSE_NOKIA = SPDXLicense.of(ValueSet.LICENSE_NOKIA);

    /**
     * Netizen Open Source License
     */
    public static final SPDXLicense LICENSE_NOSL = SPDXLicense.of(ValueSet.LICENSE_NOSL);

    /**
     * Noweb License
     */
    public static final SPDXLicense LICENSE_NOWEB = SPDXLicense.of(ValueSet.LICENSE_NOWEB);

    /**
     * Netscape Public License v1.0
     */
    public static final SPDXLicense LICENSE_NPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_NPL_1_0);

    /**
     * Netscape Public License v1.1
     */
    public static final SPDXLicense LICENSE_NPL_1_1 = SPDXLicense.of(ValueSet.LICENSE_NPL_1_1);

    /**
     * Non-Profit Open Software License 3.0
     */
    public static final SPDXLicense LICENSE_NPOSL_3_0 = SPDXLicense.of(ValueSet.LICENSE_NPOSL_3_0);

    /**
     * NRL License
     */
    public static final SPDXLicense LICENSE_NRL = SPDXLicense.of(ValueSet.LICENSE_NRL);

    /**
     * NTP License
     */
    public static final SPDXLicense LICENSE_NTP = SPDXLicense.of(ValueSet.LICENSE_NTP);

    /**
     * Open CASCADE Technology Public License
     */
    public static final SPDXLicense LICENSE_OCCT_PL = SPDXLicense.of(ValueSet.LICENSE_OCCT_PL);

    /**
     * OCLC Research Public License 2.0
     */
    public static final SPDXLicense LICENSE_OCLC_2_0 = SPDXLicense.of(ValueSet.LICENSE_OCLC_2_0);

    /**
     * ODC Open Database License v1.0
     */
    public static final SPDXLicense LICENSE_ODB_L_1_0 = SPDXLicense.of(ValueSet.LICENSE_ODB_L_1_0);

    /**
     * SIL Open Font License 1.0
     */
    public static final SPDXLicense LICENSE_OFL_1_0 = SPDXLicense.of(ValueSet.LICENSE_OFL_1_0);

    /**
     * SIL Open Font License 1.1
     */
    public static final SPDXLicense LICENSE_OFL_1_1 = SPDXLicense.of(ValueSet.LICENSE_OFL_1_1);

    /**
     * Open Group Test Suite License
     */
    public static final SPDXLicense LICENSE_OGTSL = SPDXLicense.of(ValueSet.LICENSE_OGTSL);

    /**
     * Open LDAP Public License v1.1
     */
    public static final SPDXLicense LICENSE_OLDAP_1_1 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_1_1);

    /**
     * Open LDAP Public License v1.2
     */
    public static final SPDXLicense LICENSE_OLDAP_1_2 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_1_2);

    /**
     * Open LDAP Public License v1.3
     */
    public static final SPDXLicense LICENSE_OLDAP_1_3 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_1_3);

    /**
     * Open LDAP Public License v1.4
     */
    public static final SPDXLicense LICENSE_OLDAP_1_4 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_1_4);

    /**
     * Open LDAP Public License v2.0.1
     */
    public static final SPDXLicense LICENSE_OLDAP_2_0_1 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_0_1);

    /**
     * Open LDAP Public License v2.0 (or possibly 2.0A and 2.0B)
     */
    public static final SPDXLicense LICENSE_OLDAP_2_0 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_0);

    /**
     * Open LDAP Public License v2.1
     */
    public static final SPDXLicense LICENSE_OLDAP_2_1 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_1);

    /**
     * Open LDAP Public License v2.2.1
     */
    public static final SPDXLicense LICENSE_OLDAP_2_2_1 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_2_1);

    /**
     * Open LDAP Public License 2.2.2
     */
    public static final SPDXLicense LICENSE_OLDAP_2_2_2 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_2_2);

    /**
     * Open LDAP Public License v2.2
     */
    public static final SPDXLicense LICENSE_OLDAP_2_2 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_2);

    /**
     * Open LDAP Public License v2.3
     */
    public static final SPDXLicense LICENSE_OLDAP_2_3 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_3);

    /**
     * Open LDAP Public License v2.4
     */
    public static final SPDXLicense LICENSE_OLDAP_2_4 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_4);

    /**
     * Open LDAP Public License v2.5
     */
    public static final SPDXLicense LICENSE_OLDAP_2_5 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_5);

    /**
     * Open LDAP Public License v2.6
     */
    public static final SPDXLicense LICENSE_OLDAP_2_6 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_6);

    /**
     * Open LDAP Public License v2.7
     */
    public static final SPDXLicense LICENSE_OLDAP_2_7 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_7);

    /**
     * Open LDAP Public License v2.8
     */
    public static final SPDXLicense LICENSE_OLDAP_2_8 = SPDXLicense.of(ValueSet.LICENSE_OLDAP_2_8);

    /**
     * Open Market License
     */
    public static final SPDXLicense LICENSE_OML = SPDXLicense.of(ValueSet.LICENSE_OML);

    /**
     * OpenSSL License
     */
    public static final SPDXLicense LICENSE_OPEN_SSL = SPDXLicense.of(ValueSet.LICENSE_OPEN_SSL);

    /**
     * Open Public License v1.0
     */
    public static final SPDXLicense LICENSE_OPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_OPL_1_0);

    /**
     * OSET Public License version 2.1
     */
    public static final SPDXLicense LICENSE_OSET_PL_2_1 = SPDXLicense.of(ValueSet.LICENSE_OSET_PL_2_1);

    /**
     * Open Software License 1.0
     */
    public static final SPDXLicense LICENSE_OSL_1_0 = SPDXLicense.of(ValueSet.LICENSE_OSL_1_0);

    /**
     * Open Software License 1.1
     */
    public static final SPDXLicense LICENSE_OSL_1_1 = SPDXLicense.of(ValueSet.LICENSE_OSL_1_1);

    /**
     * Open Software License 2.0
     */
    public static final SPDXLicense LICENSE_OSL_2_0 = SPDXLicense.of(ValueSet.LICENSE_OSL_2_0);

    /**
     * Open Software License 2.1
     */
    public static final SPDXLicense LICENSE_OSL_2_1 = SPDXLicense.of(ValueSet.LICENSE_OSL_2_1);

    /**
     * Open Software License 3.0
     */
    public static final SPDXLicense LICENSE_OSL_3_0 = SPDXLicense.of(ValueSet.LICENSE_OSL_3_0);

    /**
     * ODC Public Domain Dedication &amp; License 1.0
     */
    public static final SPDXLicense LICENSE_PDDL_1_0 = SPDXLicense.of(ValueSet.LICENSE_PDDL_1_0);

    /**
     * PHP License v3.0
     */
    public static final SPDXLicense LICENSE_PHP_3_0 = SPDXLicense.of(ValueSet.LICENSE_PHP_3_0);

    /**
     * PHP License v3.01
     */
    public static final SPDXLicense LICENSE_PHP_3_01 = SPDXLicense.of(ValueSet.LICENSE_PHP_3_01);

    /**
     * Plexus Classworlds License
     */
    public static final SPDXLicense LICENSE_PLEXUS = SPDXLicense.of(ValueSet.LICENSE_PLEXUS);

    /**
     * PostgreSQL License
     */
    public static final SPDXLicense LICENSE_POSTGRE_SQL = SPDXLicense.of(ValueSet.LICENSE_POSTGRE_SQL);

    /**
     * psfrag License
     */
    public static final SPDXLicense LICENSE_PSFRAG = SPDXLicense.of(ValueSet.LICENSE_PSFRAG);

    /**
     * psutils License
     */
    public static final SPDXLicense LICENSE_PSUTILS = SPDXLicense.of(ValueSet.LICENSE_PSUTILS);

    /**
     * Python License 2.0
     */
    public static final SPDXLicense LICENSE_PYTHON_2_0 = SPDXLicense.of(ValueSet.LICENSE_PYTHON_2_0);

    /**
     * Qhull License
     */
    public static final SPDXLicense LICENSE_QHULL = SPDXLicense.of(ValueSet.LICENSE_QHULL);

    /**
     * Q Public License 1.0
     */
    public static final SPDXLicense LICENSE_QPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_QPL_1_0);

    /**
     * Rdisc License
     */
    public static final SPDXLicense LICENSE_RDISC = SPDXLicense.of(ValueSet.LICENSE_RDISC);

    /**
     * Red Hat eCos Public License v1.1
     */
    public static final SPDXLicense LICENSE_RHE_COS_1_1 = SPDXLicense.of(ValueSet.LICENSE_RHE_COS_1_1);

    /**
     * Reciprocal Public License 1.1
     */
    public static final SPDXLicense LICENSE_RPL_1_1 = SPDXLicense.of(ValueSet.LICENSE_RPL_1_1);

    /**
     * Reciprocal Public License 1.5
     */
    public static final SPDXLicense LICENSE_RPL_1_5 = SPDXLicense.of(ValueSet.LICENSE_RPL_1_5);

    /**
     * RealNetworks Public Source License v1.0
     */
    public static final SPDXLicense LICENSE_RPSL_1_0 = SPDXLicense.of(ValueSet.LICENSE_RPSL_1_0);

    /**
     * RSA Message-Digest License
     */
    public static final SPDXLicense LICENSE_RSA_MD = SPDXLicense.of(ValueSet.LICENSE_RSA_MD);

    /**
     * Ricoh Source Code Public License
     */
    public static final SPDXLicense LICENSE_RSCPL = SPDXLicense.of(ValueSet.LICENSE_RSCPL);

    /**
     * Ruby License
     */
    public static final SPDXLicense LICENSE_RUBY = SPDXLicense.of(ValueSet.LICENSE_RUBY);

    /**
     * Sax Public Domain Notice
     */
    public static final SPDXLicense LICENSE_SAX_PD = SPDXLicense.of(ValueSet.LICENSE_SAX_PD);

    /**
     * Saxpath License
     */
    public static final SPDXLicense LICENSE_SAXPATH = SPDXLicense.of(ValueSet.LICENSE_SAXPATH);

    /**
     * SCEA Shared Source License
     */
    public static final SPDXLicense LICENSE_SCEA = SPDXLicense.of(ValueSet.LICENSE_SCEA);

    /**
     * Sendmail License
     */
    public static final SPDXLicense LICENSE_SENDMAIL = SPDXLicense.of(ValueSet.LICENSE_SENDMAIL);

    /**
     * SGI Free Software License B v1.0
     */
    public static final SPDXLicense LICENSE_SGI_B_1_0 = SPDXLicense.of(ValueSet.LICENSE_SGI_B_1_0);

    /**
     * SGI Free Software License B v1.1
     */
    public static final SPDXLicense LICENSE_SGI_B_1_1 = SPDXLicense.of(ValueSet.LICENSE_SGI_B_1_1);

    /**
     * SGI Free Software License B v2.0
     */
    public static final SPDXLicense LICENSE_SGI_B_2_0 = SPDXLicense.of(ValueSet.LICENSE_SGI_B_2_0);

    /**
     * Simple Public License 2.0
     */
    public static final SPDXLicense LICENSE_SIM_PL_2_0 = SPDXLicense.of(ValueSet.LICENSE_SIM_PL_2_0);

    /**
     * Sun Industry Standards Source License v1.2
     */
    public static final SPDXLicense LICENSE_SISSL_1_2 = SPDXLicense.of(ValueSet.LICENSE_SISSL_1_2);

    /**
     * Sun Industry Standards Source License v1.1
     */
    public static final SPDXLicense LICENSE_SISSL = SPDXLicense.of(ValueSet.LICENSE_SISSL);

    /**
     * Sleepycat License
     */
    public static final SPDXLicense LICENSE_SLEEPYCAT = SPDXLicense.of(ValueSet.LICENSE_SLEEPYCAT);

    /**
     * Standard ML of New Jersey License
     */
    public static final SPDXLicense LICENSE_SMLNJ = SPDXLicense.of(ValueSet.LICENSE_SMLNJ);

    /**
     * Secure Messaging Protocol Public License
     */
    public static final SPDXLicense LICENSE_SMPPL = SPDXLicense.of(ValueSet.LICENSE_SMPPL);

    /**
     * SNIA Public License 1.1
     */
    public static final SPDXLicense LICENSE_SNIA = SPDXLicense.of(ValueSet.LICENSE_SNIA);

    /**
     * Spencer License 86
     */
    public static final SPDXLicense LICENSE_SPENCER_86 = SPDXLicense.of(ValueSet.LICENSE_SPENCER_86);

    /**
     * Spencer License 94
     */
    public static final SPDXLicense LICENSE_SPENCER_94 = SPDXLicense.of(ValueSet.LICENSE_SPENCER_94);

    /**
     * Spencer License 99
     */
    public static final SPDXLicense LICENSE_SPENCER_99 = SPDXLicense.of(ValueSet.LICENSE_SPENCER_99);

    /**
     * Sun Public License v1.0
     */
    public static final SPDXLicense LICENSE_SPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_SPL_1_0);

    /**
     * SugarCRM Public License v1.1.3
     */
    public static final SPDXLicense LICENSE_SUGAR_CRM_1_1_3 = SPDXLicense.of(ValueSet.LICENSE_SUGAR_CRM_1_1_3);

    /**
     * Scheme Widget Library (SWL) Software License Agreement
     */
    public static final SPDXLicense LICENSE_SWL = SPDXLicense.of(ValueSet.LICENSE_SWL);

    /**
     * TCL/TK License
     */
    public static final SPDXLicense LICENSE_TCL = SPDXLicense.of(ValueSet.LICENSE_TCL);

    /**
     * TCP Wrappers License
     */
    public static final SPDXLicense LICENSE_TCP_WRAPPERS = SPDXLicense.of(ValueSet.LICENSE_TCP_WRAPPERS);

    /**
     * TMate Open Source License
     */
    public static final SPDXLicense LICENSE_TMATE = SPDXLicense.of(ValueSet.LICENSE_TMATE);

    /**
     * TORQUE v2.5+ Software License v1.1
     */
    public static final SPDXLicense LICENSE_TORQUE_1_1 = SPDXLicense.of(ValueSet.LICENSE_TORQUE_1_1);

    /**
     * Trusster Open Source License
     */
    public static final SPDXLicense LICENSE_TOSL = SPDXLicense.of(ValueSet.LICENSE_TOSL);

    /**
     * Unicode License Agreement - Data Files and Software (2015)
     */
    public static final SPDXLicense LICENSE_UNICODE_DFS_2015 = SPDXLicense.of(ValueSet.LICENSE_UNICODE_DFS_2015);

    /**
     * Unicode License Agreement - Data Files and Software (2016)
     */
    public static final SPDXLicense LICENSE_UNICODE_DFS_2016 = SPDXLicense.of(ValueSet.LICENSE_UNICODE_DFS_2016);

    /**
     * Unicode Terms of Use
     */
    public static final SPDXLicense LICENSE_UNICODE_TOU = SPDXLicense.of(ValueSet.LICENSE_UNICODE_TOU);

    /**
     * The Unlicense
     */
    public static final SPDXLicense LICENSE_UNLICENSE = SPDXLicense.of(ValueSet.LICENSE_UNLICENSE);

    /**
     * Universal Permissive License v1.0
     */
    public static final SPDXLicense LICENSE_UPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_UPL_1_0);

    /**
     * Vim License
     */
    public static final SPDXLicense LICENSE_VIM = SPDXLicense.of(ValueSet.LICENSE_VIM);

    /**
     * VOSTROM Public License for Open Source
     */
    public static final SPDXLicense LICENSE_VOSTROM = SPDXLicense.of(ValueSet.LICENSE_VOSTROM);

    /**
     * Vovida Software License v1.0
     */
    public static final SPDXLicense LICENSE_VSL_1_0 = SPDXLicense.of(ValueSet.LICENSE_VSL_1_0);

    /**
     * W3C Software Notice and License (1998-07-20)
     */
    public static final SPDXLicense LICENSE_W3C_19980720 = SPDXLicense.of(ValueSet.LICENSE_W3C_19980720);

    /**
     * W3C Software Notice and Document License (2015-05-13)
     */
    public static final SPDXLicense LICENSE_W3C_20150513 = SPDXLicense.of(ValueSet.LICENSE_W3C_20150513);

    /**
     * W3C Software Notice and License (2002-12-31)
     */
    public static final SPDXLicense LICENSE_W3C = SPDXLicense.of(ValueSet.LICENSE_W3C);

    /**
     * Sybase Open Watcom Public License 1.0
     */
    public static final SPDXLicense LICENSE_WATCOM_1_0 = SPDXLicense.of(ValueSet.LICENSE_WATCOM_1_0);

    /**
     * Wsuipa License
     */
    public static final SPDXLicense LICENSE_WSUIPA = SPDXLicense.of(ValueSet.LICENSE_WSUIPA);

    /**
     * Do What The F*ck You Want To Public License
     */
    public static final SPDXLicense LICENSE_WTFPL = SPDXLicense.of(ValueSet.LICENSE_WTFPL);

    /**
     * X11 License
     */
    public static final SPDXLicense LICENSE_X11 = SPDXLicense.of(ValueSet.LICENSE_X11);

    /**
     * Xerox License
     */
    public static final SPDXLicense LICENSE_XEROX = SPDXLicense.of(ValueSet.LICENSE_XEROX);

    /**
     * XFree86 License 1.1
     */
    public static final SPDXLicense LICENSE_XFREE86_1_1 = SPDXLicense.of(ValueSet.LICENSE_XFREE86_1_1);

    /**
     * xinetd License
     */
    public static final SPDXLicense LICENSE_XINETD = SPDXLicense.of(ValueSet.LICENSE_XINETD);

    /**
     * X.Net License
     */
    public static final SPDXLicense LICENSE_XNET = SPDXLicense.of(ValueSet.LICENSE_XNET);

    /**
     * XPP License
     */
    public static final SPDXLicense LICENSE_XPP = SPDXLicense.of(ValueSet.LICENSE_XPP);

    /**
     * XSkat License
     */
    public static final SPDXLicense LICENSE_XSKAT = SPDXLicense.of(ValueSet.LICENSE_XSKAT);

    /**
     * Yahoo! Public License v1.0
     */
    public static final SPDXLicense LICENSE_YPL_1_0 = SPDXLicense.of(ValueSet.LICENSE_YPL_1_0);

    /**
     * Yahoo! Public License v1.1
     */
    public static final SPDXLicense LICENSE_YPL_1_1 = SPDXLicense.of(ValueSet.LICENSE_YPL_1_1);

    /**
     * Zed License
     */
    public static final SPDXLicense LICENSE_ZED = SPDXLicense.of(ValueSet.LICENSE_ZED);

    /**
     * Zend License v2.0
     */
    public static final SPDXLicense LICENSE_ZEND_2_0 = SPDXLicense.of(ValueSet.LICENSE_ZEND_2_0);

    /**
     * Zimbra Public License v1.3
     */
    public static final SPDXLicense LICENSE_ZIMBRA_1_3 = SPDXLicense.of(ValueSet.LICENSE_ZIMBRA_1_3);

    /**
     * Zimbra Public License v1.4
     */
    public static final SPDXLicense LICENSE_ZIMBRA_1_4 = SPDXLicense.of(ValueSet.LICENSE_ZIMBRA_1_4);

    /**
     * zlib/libpng License with Acknowledgement
     */
    public static final SPDXLicense LICENSE_ZLIB_ACKNOWLEDGEMENT = SPDXLicense.of(ValueSet.LICENSE_ZLIB_ACKNOWLEDGEMENT);

    /**
     * zlib License
     */
    public static final SPDXLicense LICENSE_ZLIB = SPDXLicense.of(ValueSet.LICENSE_ZLIB);

    /**
     * Zope Public License 1.1
     */
    public static final SPDXLicense LICENSE_ZPL_1_1 = SPDXLicense.of(ValueSet.LICENSE_ZPL_1_1);

    /**
     * Zope Public License 2.0
     */
    public static final SPDXLicense LICENSE_ZPL_2_0 = SPDXLicense.of(ValueSet.LICENSE_ZPL_2_0);

    /**
     * Zope Public License 2.1
     */
    public static final SPDXLicense LICENSE_ZPL_2_1 = SPDXLicense.of(ValueSet.LICENSE_ZPL_2_1);

    private volatile int hashCode;

    private SPDXLicense(Builder builder) {
        super(builder);
    }

    public static SPDXLicense of(java.lang.String value) {
        return SPDXLicense.builder().value(value).build();
    }

    public static SPDXLicense of(ValueSet value) {
        return SPDXLicense.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return SPDXLicense.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return SPDXLicense.builder().value(value).build();
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
        SPDXLicense other = (SPDXLicense) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public SPDXLicense build() {
            return new SPDXLicense(this);
        }
    }

    public enum ValueSet {
        /**
         * Not open source
         */
        LICENSE_NOT_OPEN_SOURCE("not-open-source"),

        /**
         * BSD Zero Clause License
         */
        LICENSE_0BSD("0BSD"),

        /**
         * Attribution Assurance License
         */
        LICENSE_AAL("AAL"),

        /**
         * Abstyles License
         */
        LICENSE_ABSTYLES("Abstyles"),

        /**
         * Adobe Systems Incorporated Source Code License Agreement
         */
        LICENSE_ADOBE_2006("Adobe-2006"),

        /**
         * Adobe Glyph List License
         */
        LICENSE_ADOBE_GLYPH("Adobe-Glyph"),

        /**
         * Amazon Digital Services License
         */
        LICENSE_ADSL("ADSL"),

        /**
         * Academic Free License v1.1
         */
        LICENSE_AFL_1_1("AFL-1.1"),

        /**
         * Academic Free License v1.2
         */
        LICENSE_AFL_1_2("AFL-1.2"),

        /**
         * Academic Free License v2.0
         */
        LICENSE_AFL_2_0("AFL-2.0"),

        /**
         * Academic Free License v2.1
         */
        LICENSE_AFL_2_1("AFL-2.1"),

        /**
         * Academic Free License v3.0
         */
        LICENSE_AFL_3_0("AFL-3.0"),

        /**
         * Afmparse License
         */
        LICENSE_AFMPARSE("Afmparse"),

        /**
         * Affero General Public License v1.0 only
         */
        LICENSE_AGPL_1_0_ONLY("AGPL-1.0-only"),

        /**
         * Affero General Public License v1.0 or later
         */
        LICENSE_AGPL_1_0_OR_LATER("AGPL-1.0-or-later"),

        /**
         * GNU Affero General Public License v3.0 only
         */
        LICENSE_AGPL_3_0_ONLY("AGPL-3.0-only"),

        /**
         * GNU Affero General Public License v3.0 or later
         */
        LICENSE_AGPL_3_0_OR_LATER("AGPL-3.0-or-later"),

        /**
         * Aladdin Free Public License
         */
        LICENSE_ALADDIN("Aladdin"),

        /**
         * AMD's plpa_map.c License
         */
        LICENSE_AMDPLPA("AMDPLPA"),

        /**
         * Apple MIT License
         */
        LICENSE_AML("AML"),

        /**
         * Academy of Motion Picture Arts and Sciences BSD
         */
        LICENSE_AMPAS("AMPAS"),

        /**
         * ANTLR Software Rights Notice
         */
        LICENSE_ANTLR_PD("ANTLR-PD"),

        /**
         * Apache License 1.0
         */
        LICENSE_APACHE_1_0("Apache-1.0"),

        /**
         * Apache License 1.1
         */
        LICENSE_APACHE_1_1("Apache-1.1"),

        /**
         * Apache License 2.0
         */
        LICENSE_APACHE_2_0("Apache-2.0"),

        /**
         * Adobe Postscript AFM License
         */
        LICENSE_APAFML("APAFML"),

        /**
         * Adaptive Public License 1.0
         */
        LICENSE_APL_1_0("APL-1.0"),

        /**
         * Apple Public Source License 1.0
         */
        LICENSE_APSL_1_0("APSL-1.0"),

        /**
         * Apple Public Source License 1.1
         */
        LICENSE_APSL_1_1("APSL-1.1"),

        /**
         * Apple Public Source License 1.2
         */
        LICENSE_APSL_1_2("APSL-1.2"),

        /**
         * Apple Public Source License 2.0
         */
        LICENSE_APSL_2_0("APSL-2.0"),

        /**
         * Artistic License 1.0 w/clause 8
         */
        LICENSE_ARTISTIC_1_0_CL8("Artistic-1.0-cl8"),

        /**
         * Artistic License 1.0 (Perl)
         */
        LICENSE_ARTISTIC_1_0_PERL("Artistic-1.0-Perl"),

        /**
         * Artistic License 1.0
         */
        LICENSE_ARTISTIC_1_0("Artistic-1.0"),

        /**
         * Artistic License 2.0
         */
        LICENSE_ARTISTIC_2_0("Artistic-2.0"),

        /**
         * Bahyph License
         */
        LICENSE_BAHYPH("Bahyph"),

        /**
         * Barr License
         */
        LICENSE_BARR("Barr"),

        /**
         * Beerware License
         */
        LICENSE_BEERWARE("Beerware"),

        /**
         * BitTorrent Open Source License v1.0
         */
        LICENSE_BIT_TORRENT_1_0("BitTorrent-1.0"),

        /**
         * BitTorrent Open Source License v1.1
         */
        LICENSE_BIT_TORRENT_1_1("BitTorrent-1.1"),

        /**
         * Borceux license
         */
        LICENSE_BORCEUX("Borceux"),

        /**
         * BSD 1-Clause License
         */
        LICENSE_BSD_1_CLAUSE("BSD-1-Clause"),

        /**
         * BSD 2-Clause FreeBSD License
         */
        LICENSE_BSD_2_CLAUSE_FREE_BSD("BSD-2-Clause-FreeBSD"),

        /**
         * BSD 2-Clause NetBSD License
         */
        LICENSE_BSD_2_CLAUSE_NET_BSD("BSD-2-Clause-NetBSD"),

        /**
         * BSD-2-Clause Plus Patent License
         */
        LICENSE_BSD_2_CLAUSE_PATENT("BSD-2-Clause-Patent"),

        /**
         * BSD 2-Clause "Simplified" License
         */
        LICENSE_BSD_2_CLAUSE("BSD-2-Clause"),

        /**
         * BSD with attribution
         */
        LICENSE_BSD_3_CLAUSE_ATTRIBUTION("BSD-3-Clause-Attribution"),

        /**
         * BSD 3-Clause Clear License
         */
        LICENSE_BSD_3_CLAUSE_CLEAR("BSD-3-Clause-Clear"),

        /**
         * Lawrence Berkeley National Labs BSD variant license
         */
        LICENSE_BSD_3_CLAUSE_LBNL("BSD-3-Clause-LBNL"),

        /**
         * BSD 3-Clause No Nuclear License 2014
         */
        LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE_2014("BSD-3-Clause-No-Nuclear-License-2014"),

        /**
         * BSD 3-Clause No Nuclear License
         */
        LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE("BSD-3-Clause-No-Nuclear-License"),

        /**
         * BSD 3-Clause No Nuclear Warranty
         */
        LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_WARRANTY("BSD-3-Clause-No-Nuclear-Warranty"),

        /**
         * BSD 3-Clause "New" or "Revised" License
         */
        LICENSE_BSD_3_CLAUSE("BSD-3-Clause"),

        /**
         * BSD-4-Clause (University of California-Specific)
         */
        LICENSE_BSD_4_CLAUSE_UC("BSD-4-Clause-UC"),

        /**
         * BSD 4-Clause "Original" or "Old" License
         */
        LICENSE_BSD_4_CLAUSE("BSD-4-Clause"),

        /**
         * BSD Protection License
         */
        LICENSE_BSD_PROTECTION("BSD-Protection"),

        /**
         * BSD Source Code Attribution
         */
        LICENSE_BSD_SOURCE_CODE("BSD-Source-Code"),

        /**
         * Boost Software License 1.0
         */
        LICENSE_BSL_1_0("BSL-1.0"),

        /**
         * bzip2 and libbzip2 License v1.0.5
         */
        LICENSE_BZIP2_1_0_5("bzip2-1.0.5"),

        /**
         * bzip2 and libbzip2 License v1.0.6
         */
        LICENSE_BZIP2_1_0_6("bzip2-1.0.6"),

        /**
         * Caldera License
         */
        LICENSE_CALDERA("Caldera"),

        /**
         * Computer Associates Trusted Open Source License 1.1
         */
        LICENSE_CATOSL_1_1("CATOSL-1.1"),

        /**
         * Creative Commons Attribution 1.0 Generic
         */
        LICENSE_CC_BY_1_0("CC-BY-1.0"),

        /**
         * Creative Commons Attribution 2.0 Generic
         */
        LICENSE_CC_BY_2_0("CC-BY-2.0"),

        /**
         * Creative Commons Attribution 2.5 Generic
         */
        LICENSE_CC_BY_2_5("CC-BY-2.5"),

        /**
         * Creative Commons Attribution 3.0 Unported
         */
        LICENSE_CC_BY_3_0("CC-BY-3.0"),

        /**
         * Creative Commons Attribution 4.0 International
         */
        LICENSE_CC_BY_4_0("CC-BY-4.0"),

        /**
         * Creative Commons Attribution Non Commercial 1.0 Generic
         */
        LICENSE_CC_BY_NC_1_0("CC-BY-NC-1.0"),

        /**
         * Creative Commons Attribution Non Commercial 2.0 Generic
         */
        LICENSE_CC_BY_NC_2_0("CC-BY-NC-2.0"),

        /**
         * Creative Commons Attribution Non Commercial 2.5 Generic
         */
        LICENSE_CC_BY_NC_2_5("CC-BY-NC-2.5"),

        /**
         * Creative Commons Attribution Non Commercial 3.0 Unported
         */
        LICENSE_CC_BY_NC_3_0("CC-BY-NC-3.0"),

        /**
         * Creative Commons Attribution Non Commercial 4.0 International
         */
        LICENSE_CC_BY_NC_4_0("CC-BY-NC-4.0"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 1.0 Generic
         */
        LICENSE_CC_BY_NC_ND_1_0("CC-BY-NC-ND-1.0"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 2.0 Generic
         */
        LICENSE_CC_BY_NC_ND_2_0("CC-BY-NC-ND-2.0"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 2.5 Generic
         */
        LICENSE_CC_BY_NC_ND_2_5("CC-BY-NC-ND-2.5"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 3.0 Unported
         */
        LICENSE_CC_BY_NC_ND_3_0("CC-BY-NC-ND-3.0"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 4.0 International
         */
        LICENSE_CC_BY_NC_ND_4_0("CC-BY-NC-ND-4.0"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 1.0 Generic
         */
        LICENSE_CC_BY_NC_SA_1_0("CC-BY-NC-SA-1.0"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 2.0 Generic
         */
        LICENSE_CC_BY_NC_SA_2_0("CC-BY-NC-SA-2.0"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 2.5 Generic
         */
        LICENSE_CC_BY_NC_SA_2_5("CC-BY-NC-SA-2.5"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 3.0 Unported
         */
        LICENSE_CC_BY_NC_SA_3_0("CC-BY-NC-SA-3.0"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 4.0 International
         */
        LICENSE_CC_BY_NC_SA_4_0("CC-BY-NC-SA-4.0"),

        /**
         * Creative Commons Attribution No Derivatives 1.0 Generic
         */
        LICENSE_CC_BY_ND_1_0("CC-BY-ND-1.0"),

        /**
         * Creative Commons Attribution No Derivatives 2.0 Generic
         */
        LICENSE_CC_BY_ND_2_0("CC-BY-ND-2.0"),

        /**
         * Creative Commons Attribution No Derivatives 2.5 Generic
         */
        LICENSE_CC_BY_ND_2_5("CC-BY-ND-2.5"),

        /**
         * Creative Commons Attribution No Derivatives 3.0 Unported
         */
        LICENSE_CC_BY_ND_3_0("CC-BY-ND-3.0"),

        /**
         * Creative Commons Attribution No Derivatives 4.0 International
         */
        LICENSE_CC_BY_ND_4_0("CC-BY-ND-4.0"),

        /**
         * Creative Commons Attribution Share Alike 1.0 Generic
         */
        LICENSE_CC_BY_SA_1_0("CC-BY-SA-1.0"),

        /**
         * Creative Commons Attribution Share Alike 2.0 Generic
         */
        LICENSE_CC_BY_SA_2_0("CC-BY-SA-2.0"),

        /**
         * Creative Commons Attribution Share Alike 2.5 Generic
         */
        LICENSE_CC_BY_SA_2_5("CC-BY-SA-2.5"),

        /**
         * Creative Commons Attribution Share Alike 3.0 Unported
         */
        LICENSE_CC_BY_SA_3_0("CC-BY-SA-3.0"),

        /**
         * Creative Commons Attribution Share Alike 4.0 International
         */
        LICENSE_CC_BY_SA_4_0("CC-BY-SA-4.0"),

        /**
         * Creative Commons Zero v1.0 Universal
         */
        LICENSE_CC0_1_0("CC0-1.0"),

        /**
         * Common Development and Distribution License 1.0
         */
        LICENSE_CDDL_1_0("CDDL-1.0"),

        /**
         * Common Development and Distribution License 1.1
         */
        LICENSE_CDDL_1_1("CDDL-1.1"),

        /**
         * Community Data License Agreement Permissive 1.0
         */
        LICENSE_CDLA_PERMISSIVE_1_0("CDLA-Permissive-1.0"),

        /**
         * Community Data License Agreement Sharing 1.0
         */
        LICENSE_CDLA_SHARING_1_0("CDLA-Sharing-1.0"),

        /**
         * CeCILL Free Software License Agreement v1.0
         */
        LICENSE_CECILL_1_0("CECILL-1.0"),

        /**
         * CeCILL Free Software License Agreement v1.1
         */
        LICENSE_CECILL_1_1("CECILL-1.1"),

        /**
         * CeCILL Free Software License Agreement v2.0
         */
        LICENSE_CECILL_2_0("CECILL-2.0"),

        /**
         * CeCILL Free Software License Agreement v2.1
         */
        LICENSE_CECILL_2_1("CECILL-2.1"),

        /**
         * CeCILL-B Free Software License Agreement
         */
        LICENSE_CECILL_B("CECILL-B"),

        /**
         * CeCILL-C Free Software License Agreement
         */
        LICENSE_CECILL_C("CECILL-C"),

        /**
         * Clarified Artistic License
         */
        LICENSE_CL_ARTISTIC("ClArtistic"),

        /**
         * CNRI Jython License
         */
        LICENSE_CNRI_JYTHON("CNRI-Jython"),

        /**
         * CNRI Python Open Source GPL Compatible License Agreement
         */
        LICENSE_CNRI_PYTHON_GPL_COMPATIBLE("CNRI-Python-GPL-Compatible"),

        /**
         * CNRI Python License
         */
        LICENSE_CNRI_PYTHON("CNRI-Python"),

        /**
         * Condor Public License v1.1
         */
        LICENSE_CONDOR_1_1("Condor-1.1"),

        /**
         * Common Public Attribution License 1.0
         */
        LICENSE_CPAL_1_0("CPAL-1.0"),

        /**
         * Common Public License 1.0
         */
        LICENSE_CPL_1_0("CPL-1.0"),

        /**
         * Code Project Open License 1.02
         */
        LICENSE_CPOL_1_02("CPOL-1.02"),

        /**
         * Crossword License
         */
        LICENSE_CROSSWORD("Crossword"),

        /**
         * CrystalStacker License
         */
        LICENSE_CRYSTAL_STACKER("CrystalStacker"),

        /**
         * CUA Office Public License v1.0
         */
        LICENSE_CUA_OPL_1_0("CUA-OPL-1.0"),

        /**
         * Cube License
         */
        LICENSE_CUBE("Cube"),

        /**
         * curl License
         */
        LICENSE_CURL("curl"),

        /**
         * Deutsche Freie Software Lizenz
         */
        LICENSE_D_FSL_1_0("D-FSL-1.0"),

        /**
         * diffmark license
         */
        LICENSE_DIFFMARK("diffmark"),

        /**
         * DOC License
         */
        LICENSE_DOC("DOC"),

        /**
         * Dotseqn License
         */
        LICENSE_DOTSEQN("Dotseqn"),

        /**
         * DSDP License
         */
        LICENSE_DSDP("DSDP"),

        /**
         * dvipdfm License
         */
        LICENSE_DVIPDFM("dvipdfm"),

        /**
         * Educational Community License v1.0
         */
        LICENSE_ECL_1_0("ECL-1.0"),

        /**
         * Educational Community License v2.0
         */
        LICENSE_ECL_2_0("ECL-2.0"),

        /**
         * Eiffel Forum License v1.0
         */
        LICENSE_EFL_1_0("EFL-1.0"),

        /**
         * Eiffel Forum License v2.0
         */
        LICENSE_EFL_2_0("EFL-2.0"),

        /**
         * eGenix.com Public License 1.1.0
         */
        LICENSE_E_GENIX("eGenix"),

        /**
         * Entessa Public License v1.0
         */
        LICENSE_ENTESSA("Entessa"),

        /**
         * Eclipse Public License 1.0
         */
        LICENSE_EPL_1_0("EPL-1.0"),

        /**
         * Eclipse Public License 2.0
         */
        LICENSE_EPL_2_0("EPL-2.0"),

        /**
         * Erlang Public License v1.1
         */
        LICENSE_ERL_PL_1_1("ErlPL-1.1"),

        /**
         * EU DataGrid Software License
         */
        LICENSE_EUDATAGRID("EUDatagrid"),

        /**
         * European Union Public License 1.0
         */
        LICENSE_EUPL_1_0("EUPL-1.0"),

        /**
         * European Union Public License 1.1
         */
        LICENSE_EUPL_1_1("EUPL-1.1"),

        /**
         * European Union Public License 1.2
         */
        LICENSE_EUPL_1_2("EUPL-1.2"),

        /**
         * Eurosym License
         */
        LICENSE_EUROSYM("Eurosym"),

        /**
         * Fair License
         */
        LICENSE_FAIR("Fair"),

        /**
         * Frameworx Open License 1.0
         */
        LICENSE_FRAMEWORX_1_0("Frameworx-1.0"),

        /**
         * FreeImage Public License v1.0
         */
        LICENSE_FREE_IMAGE("FreeImage"),

        /**
         * FSF All Permissive License
         */
        LICENSE_FSFAP("FSFAP"),

        /**
         * FSF Unlimited License
         */
        LICENSE_FSFUL("FSFUL"),

        /**
         * FSF Unlimited License (with License Retention)
         */
        LICENSE_FSFULLR("FSFULLR"),

        /**
         * Freetype Project License
         */
        LICENSE_FTL("FTL"),

        /**
         * GNU Free Documentation License v1.1 only
         */
        LICENSE_GFDL_1_1_ONLY("GFDL-1.1-only"),

        /**
         * GNU Free Documentation License v1.1 or later
         */
        LICENSE_GFDL_1_1_OR_LATER("GFDL-1.1-or-later"),

        /**
         * GNU Free Documentation License v1.2 only
         */
        LICENSE_GFDL_1_2_ONLY("GFDL-1.2-only"),

        /**
         * GNU Free Documentation License v1.2 or later
         */
        LICENSE_GFDL_1_2_OR_LATER("GFDL-1.2-or-later"),

        /**
         * GNU Free Documentation License v1.3 only
         */
        LICENSE_GFDL_1_3_ONLY("GFDL-1.3-only"),

        /**
         * GNU Free Documentation License v1.3 or later
         */
        LICENSE_GFDL_1_3_OR_LATER("GFDL-1.3-or-later"),

        /**
         * Giftware License
         */
        LICENSE_GIFTWARE("Giftware"),

        /**
         * GL2PS License
         */
        LICENSE_GL2PS("GL2PS"),

        /**
         * 3dfx Glide License
         */
        LICENSE_GLIDE("Glide"),

        /**
         * Glulxe License
         */
        LICENSE_GLULXE("Glulxe"),

        /**
         * gnuplot License
         */
        LICENSE_GNUPLOT("gnuplot"),

        /**
         * GNU General Public License v1.0 only
         */
        LICENSE_GPL_1_0_ONLY("GPL-1.0-only"),

        /**
         * GNU General Public License v1.0 or later
         */
        LICENSE_GPL_1_0_OR_LATER("GPL-1.0-or-later"),

        /**
         * GNU General Public License v2.0 only
         */
        LICENSE_GPL_2_0_ONLY("GPL-2.0-only"),

        /**
         * GNU General Public License v2.0 or later
         */
        LICENSE_GPL_2_0_OR_LATER("GPL-2.0-or-later"),

        /**
         * GNU General Public License v3.0 only
         */
        LICENSE_GPL_3_0_ONLY("GPL-3.0-only"),

        /**
         * GNU General Public License v3.0 or later
         */
        LICENSE_GPL_3_0_OR_LATER("GPL-3.0-or-later"),

        /**
         * gSOAP Public License v1.3b
         */
        LICENSE_G_SOAP_1_3B("gSOAP-1.3b"),

        /**
         * Haskell Language Report License
         */
        LICENSE_HASKELL_REPORT("HaskellReport"),

        /**
         * Historical Permission Notice and Disclaimer
         */
        LICENSE_HPND("HPND"),

        /**
         * IBM PowerPC Initialization and Boot Software
         */
        LICENSE_IBM_PIBS("IBM-pibs"),

        /**
         * ICU License
         */
        LICENSE_ICU("ICU"),

        /**
         * Independent JPEG Group License
         */
        LICENSE_IJG("IJG"),

        /**
         * ImageMagick License
         */
        LICENSE_IMAGE_MAGICK("ImageMagick"),

        /**
         * iMatix Standard Function Library Agreement
         */
        LICENSE_I_MATIX("iMatix"),

        /**
         * Imlib2 License
         */
        LICENSE_IMLIB2("Imlib2"),

        /**
         * Info-ZIP License
         */
        LICENSE_INFO_ZIP("Info-ZIP"),

        /**
         * Intel ACPI Software License Agreement
         */
        LICENSE_INTEL_ACPI("Intel-ACPI"),

        /**
         * Intel Open Source License
         */
        LICENSE_INTEL("Intel"),

        /**
         * Interbase Public License v1.0
         */
        LICENSE_INTERBASE_1_0("Interbase-1.0"),

        /**
         * IPA Font License
         */
        LICENSE_IPA("IPA"),

        /**
         * IBM Public License v1.0
         */
        LICENSE_IPL_1_0("IPL-1.0"),

        /**
         * ISC License
         */
        LICENSE_ISC("ISC"),

        /**
         * JasPer License
         */
        LICENSE_JAS_PER_2_0("JasPer-2.0"),

        /**
         * JSON License
         */
        LICENSE_JSON("JSON"),

        /**
         * Licence Art Libre 1.2
         */
        LICENSE_LAL_1_2("LAL-1.2"),

        /**
         * Licence Art Libre 1.3
         */
        LICENSE_LAL_1_3("LAL-1.3"),

        /**
         * Latex2e License
         */
        LICENSE_LATEX2E("Latex2e"),

        /**
         * Leptonica License
         */
        LICENSE_LEPTONICA("Leptonica"),

        /**
         * GNU Library General Public License v2 only
         */
        LICENSE_LGPL_2_0_ONLY("LGPL-2.0-only"),

        /**
         * GNU Library General Public License v2 or later
         */
        LICENSE_LGPL_2_0_OR_LATER("LGPL-2.0-or-later"),

        /**
         * GNU Lesser General Public License v2.1 only
         */
        LICENSE_LGPL_2_1_ONLY("LGPL-2.1-only"),

        /**
         * GNU Lesser General Public License v2.1 or later
         */
        LICENSE_LGPL_2_1_OR_LATER("LGPL-2.1-or-later"),

        /**
         * GNU Lesser General Public License v3.0 only
         */
        LICENSE_LGPL_3_0_ONLY("LGPL-3.0-only"),

        /**
         * GNU Lesser General Public License v3.0 or later
         */
        LICENSE_LGPL_3_0_OR_LATER("LGPL-3.0-or-later"),

        /**
         * Lesser General Public License For Linguistic Resources
         */
        LICENSE_LGPLLR("LGPLLR"),

        /**
         * libpng License
         */
        LICENSE_LIBPNG("Libpng"),

        /**
         * libtiff License
         */
        LICENSE_LIBTIFF("libtiff"),

        /**
         * Licence Libre du Québec – Permissive version 1.1
         */
        LICENSE_LI_LI_Q_P_1_1("LiLiQ-P-1.1"),

        /**
         * Licence Libre du Québec – Réciprocité version 1.1
         */
        LICENSE_LI_LI_Q_R_1_1("LiLiQ-R-1.1"),

        /**
         * Licence Libre du Québec – Réciprocité forte version 1.1
         */
        LICENSE_LI_LI_Q_RPLUS_1_1("LiLiQ-Rplus-1.1"),

        /**
         * Linux Kernel Variant of OpenIB.org license
         */
        LICENSE_LINUX_OPEN_IB("Linux-OpenIB"),

        /**
         * Lucent Public License Version 1.0
         */
        LICENSE_LPL_1_0("LPL-1.0"),

        /**
         * Lucent Public License v1.02
         */
        LICENSE_LPL_1_02("LPL-1.02"),

        /**
         * LaTeX Project Public License v1.0
         */
        LICENSE_LPPL_1_0("LPPL-1.0"),

        /**
         * LaTeX Project Public License v1.1
         */
        LICENSE_LPPL_1_1("LPPL-1.1"),

        /**
         * LaTeX Project Public License v1.2
         */
        LICENSE_LPPL_1_2("LPPL-1.2"),

        /**
         * LaTeX Project Public License v1.3a
         */
        LICENSE_LPPL_1_3A("LPPL-1.3a"),

        /**
         * LaTeX Project Public License v1.3c
         */
        LICENSE_LPPL_1_3C("LPPL-1.3c"),

        /**
         * MakeIndex License
         */
        LICENSE_MAKE_INDEX("MakeIndex"),

        /**
         * MirOS License
         */
        LICENSE_MIR_OS("MirOS"),

        /**
         * MIT No Attribution
         */
        LICENSE_MIT_0("MIT-0"),

        /**
         * Enlightenment License (e16)
         */
        LICENSE_MIT_ADVERTISING("MIT-advertising"),

        /**
         * CMU License
         */
        LICENSE_MIT_CMU("MIT-CMU"),

        /**
         * enna License
         */
        LICENSE_MIT_ENNA("MIT-enna"),

        /**
         * feh License
         */
        LICENSE_MIT_FEH("MIT-feh"),

        /**
         * MIT License
         */
        LICENSE_MIT("MIT"),

        /**
         * MIT +no-false-attribs license
         */
        LICENSE_MITNFA("MITNFA"),

        /**
         * Motosoto License
         */
        LICENSE_MOTOSOTO("Motosoto"),

        /**
         * mpich2 License
         */
        LICENSE_MPICH2("mpich2"),

        /**
         * Mozilla Public License 1.0
         */
        LICENSE_MPL_1_0("MPL-1.0"),

        /**
         * Mozilla Public License 1.1
         */
        LICENSE_MPL_1_1("MPL-1.1"),

        /**
         * Mozilla Public License 2.0 (no copyleft exception)
         */
        LICENSE_MPL_2_0_NO_COPYLEFT_EXCEPTION("MPL-2.0-no-copyleft-exception"),

        /**
         * Mozilla Public License 2.0
         */
        LICENSE_MPL_2_0("MPL-2.0"),

        /**
         * Microsoft Public License
         */
        LICENSE_MS_PL("MS-PL"),

        /**
         * Microsoft Reciprocal License
         */
        LICENSE_MS_RL("MS-RL"),

        /**
         * Matrix Template Library License
         */
        LICENSE_MTLL("MTLL"),

        /**
         * Multics License
         */
        LICENSE_MULTICS("Multics"),

        /**
         * Mup License
         */
        LICENSE_MUP("Mup"),

        /**
         * NASA Open Source Agreement 1.3
         */
        LICENSE_NASA_1_3("NASA-1.3"),

        /**
         * Naumen Public License
         */
        LICENSE_NAUMEN("Naumen"),

        /**
         * Net Boolean Public License v1
         */
        LICENSE_NBPL_1_0("NBPL-1.0"),

        /**
         * University of Illinois/NCSA Open Source License
         */
        LICENSE_NCSA("NCSA"),

        /**
         * Net-SNMP License
         */
        LICENSE_NET_SNMP("Net-SNMP"),

        /**
         * NetCDF license
         */
        LICENSE_NET_CDF("NetCDF"),

        /**
         * Newsletr License
         */
        LICENSE_NEWSLETR("Newsletr"),

        /**
         * Nethack General Public License
         */
        LICENSE_NGPL("NGPL"),

        /**
         * Norwegian Licence for Open Government Data
         */
        LICENSE_NLOD_1_0("NLOD-1.0"),

        /**
         * No Limit Public License
         */
        LICENSE_NLPL("NLPL"),

        /**
         * Nokia Open Source License
         */
        LICENSE_NOKIA("Nokia"),

        /**
         * Netizen Open Source License
         */
        LICENSE_NOSL("NOSL"),

        /**
         * Noweb License
         */
        LICENSE_NOWEB("Noweb"),

        /**
         * Netscape Public License v1.0
         */
        LICENSE_NPL_1_0("NPL-1.0"),

        /**
         * Netscape Public License v1.1
         */
        LICENSE_NPL_1_1("NPL-1.1"),

        /**
         * Non-Profit Open Software License 3.0
         */
        LICENSE_NPOSL_3_0("NPOSL-3.0"),

        /**
         * NRL License
         */
        LICENSE_NRL("NRL"),

        /**
         * NTP License
         */
        LICENSE_NTP("NTP"),

        /**
         * Open CASCADE Technology Public License
         */
        LICENSE_OCCT_PL("OCCT-PL"),

        /**
         * OCLC Research Public License 2.0
         */
        LICENSE_OCLC_2_0("OCLC-2.0"),

        /**
         * ODC Open Database License v1.0
         */
        LICENSE_ODB_L_1_0("ODbL-1.0"),

        /**
         * SIL Open Font License 1.0
         */
        LICENSE_OFL_1_0("OFL-1.0"),

        /**
         * SIL Open Font License 1.1
         */
        LICENSE_OFL_1_1("OFL-1.1"),

        /**
         * Open Group Test Suite License
         */
        LICENSE_OGTSL("OGTSL"),

        /**
         * Open LDAP Public License v1.1
         */
        LICENSE_OLDAP_1_1("OLDAP-1.1"),

        /**
         * Open LDAP Public License v1.2
         */
        LICENSE_OLDAP_1_2("OLDAP-1.2"),

        /**
         * Open LDAP Public License v1.3
         */
        LICENSE_OLDAP_1_3("OLDAP-1.3"),

        /**
         * Open LDAP Public License v1.4
         */
        LICENSE_OLDAP_1_4("OLDAP-1.4"),

        /**
         * Open LDAP Public License v2.0.1
         */
        LICENSE_OLDAP_2_0_1("OLDAP-2.0.1"),

        /**
         * Open LDAP Public License v2.0 (or possibly 2.0A and 2.0B)
         */
        LICENSE_OLDAP_2_0("OLDAP-2.0"),

        /**
         * Open LDAP Public License v2.1
         */
        LICENSE_OLDAP_2_1("OLDAP-2.1"),

        /**
         * Open LDAP Public License v2.2.1
         */
        LICENSE_OLDAP_2_2_1("OLDAP-2.2.1"),

        /**
         * Open LDAP Public License 2.2.2
         */
        LICENSE_OLDAP_2_2_2("OLDAP-2.2.2"),

        /**
         * Open LDAP Public License v2.2
         */
        LICENSE_OLDAP_2_2("OLDAP-2.2"),

        /**
         * Open LDAP Public License v2.3
         */
        LICENSE_OLDAP_2_3("OLDAP-2.3"),

        /**
         * Open LDAP Public License v2.4
         */
        LICENSE_OLDAP_2_4("OLDAP-2.4"),

        /**
         * Open LDAP Public License v2.5
         */
        LICENSE_OLDAP_2_5("OLDAP-2.5"),

        /**
         * Open LDAP Public License v2.6
         */
        LICENSE_OLDAP_2_6("OLDAP-2.6"),

        /**
         * Open LDAP Public License v2.7
         */
        LICENSE_OLDAP_2_7("OLDAP-2.7"),

        /**
         * Open LDAP Public License v2.8
         */
        LICENSE_OLDAP_2_8("OLDAP-2.8"),

        /**
         * Open Market License
         */
        LICENSE_OML("OML"),

        /**
         * OpenSSL License
         */
        LICENSE_OPEN_SSL("OpenSSL"),

        /**
         * Open Public License v1.0
         */
        LICENSE_OPL_1_0("OPL-1.0"),

        /**
         * OSET Public License version 2.1
         */
        LICENSE_OSET_PL_2_1("OSET-PL-2.1"),

        /**
         * Open Software License 1.0
         */
        LICENSE_OSL_1_0("OSL-1.0"),

        /**
         * Open Software License 1.1
         */
        LICENSE_OSL_1_1("OSL-1.1"),

        /**
         * Open Software License 2.0
         */
        LICENSE_OSL_2_0("OSL-2.0"),

        /**
         * Open Software License 2.1
         */
        LICENSE_OSL_2_1("OSL-2.1"),

        /**
         * Open Software License 3.0
         */
        LICENSE_OSL_3_0("OSL-3.0"),

        /**
         * ODC Public Domain Dedication &amp; License 1.0
         */
        LICENSE_PDDL_1_0("PDDL-1.0"),

        /**
         * PHP License v3.0
         */
        LICENSE_PHP_3_0("PHP-3.0"),

        /**
         * PHP License v3.01
         */
        LICENSE_PHP_3_01("PHP-3.01"),

        /**
         * Plexus Classworlds License
         */
        LICENSE_PLEXUS("Plexus"),

        /**
         * PostgreSQL License
         */
        LICENSE_POSTGRE_SQL("PostgreSQL"),

        /**
         * psfrag License
         */
        LICENSE_PSFRAG("psfrag"),

        /**
         * psutils License
         */
        LICENSE_PSUTILS("psutils"),

        /**
         * Python License 2.0
         */
        LICENSE_PYTHON_2_0("Python-2.0"),

        /**
         * Qhull License
         */
        LICENSE_QHULL("Qhull"),

        /**
         * Q Public License 1.0
         */
        LICENSE_QPL_1_0("QPL-1.0"),

        /**
         * Rdisc License
         */
        LICENSE_RDISC("Rdisc"),

        /**
         * Red Hat eCos Public License v1.1
         */
        LICENSE_RHE_COS_1_1("RHeCos-1.1"),

        /**
         * Reciprocal Public License 1.1
         */
        LICENSE_RPL_1_1("RPL-1.1"),

        /**
         * Reciprocal Public License 1.5
         */
        LICENSE_RPL_1_5("RPL-1.5"),

        /**
         * RealNetworks Public Source License v1.0
         */
        LICENSE_RPSL_1_0("RPSL-1.0"),

        /**
         * RSA Message-Digest License
         */
        LICENSE_RSA_MD("RSA-MD"),

        /**
         * Ricoh Source Code Public License
         */
        LICENSE_RSCPL("RSCPL"),

        /**
         * Ruby License
         */
        LICENSE_RUBY("Ruby"),

        /**
         * Sax Public Domain Notice
         */
        LICENSE_SAX_PD("SAX-PD"),

        /**
         * Saxpath License
         */
        LICENSE_SAXPATH("Saxpath"),

        /**
         * SCEA Shared Source License
         */
        LICENSE_SCEA("SCEA"),

        /**
         * Sendmail License
         */
        LICENSE_SENDMAIL("Sendmail"),

        /**
         * SGI Free Software License B v1.0
         */
        LICENSE_SGI_B_1_0("SGI-B-1.0"),

        /**
         * SGI Free Software License B v1.1
         */
        LICENSE_SGI_B_1_1("SGI-B-1.1"),

        /**
         * SGI Free Software License B v2.0
         */
        LICENSE_SGI_B_2_0("SGI-B-2.0"),

        /**
         * Simple Public License 2.0
         */
        LICENSE_SIM_PL_2_0("SimPL-2.0"),

        /**
         * Sun Industry Standards Source License v1.2
         */
        LICENSE_SISSL_1_2("SISSL-1.2"),

        /**
         * Sun Industry Standards Source License v1.1
         */
        LICENSE_SISSL("SISSL"),

        /**
         * Sleepycat License
         */
        LICENSE_SLEEPYCAT("Sleepycat"),

        /**
         * Standard ML of New Jersey License
         */
        LICENSE_SMLNJ("SMLNJ"),

        /**
         * Secure Messaging Protocol Public License
         */
        LICENSE_SMPPL("SMPPL"),

        /**
         * SNIA Public License 1.1
         */
        LICENSE_SNIA("SNIA"),

        /**
         * Spencer License 86
         */
        LICENSE_SPENCER_86("Spencer-86"),

        /**
         * Spencer License 94
         */
        LICENSE_SPENCER_94("Spencer-94"),

        /**
         * Spencer License 99
         */
        LICENSE_SPENCER_99("Spencer-99"),

        /**
         * Sun Public License v1.0
         */
        LICENSE_SPL_1_0("SPL-1.0"),

        /**
         * SugarCRM Public License v1.1.3
         */
        LICENSE_SUGAR_CRM_1_1_3("SugarCRM-1.1.3"),

        /**
         * Scheme Widget Library (SWL) Software License Agreement
         */
        LICENSE_SWL("SWL"),

        /**
         * TCL/TK License
         */
        LICENSE_TCL("TCL"),

        /**
         * TCP Wrappers License
         */
        LICENSE_TCP_WRAPPERS("TCP-wrappers"),

        /**
         * TMate Open Source License
         */
        LICENSE_TMATE("TMate"),

        /**
         * TORQUE v2.5+ Software License v1.1
         */
        LICENSE_TORQUE_1_1("TORQUE-1.1"),

        /**
         * Trusster Open Source License
         */
        LICENSE_TOSL("TOSL"),

        /**
         * Unicode License Agreement - Data Files and Software (2015)
         */
        LICENSE_UNICODE_DFS_2015("Unicode-DFS-2015"),

        /**
         * Unicode License Agreement - Data Files and Software (2016)
         */
        LICENSE_UNICODE_DFS_2016("Unicode-DFS-2016"),

        /**
         * Unicode Terms of Use
         */
        LICENSE_UNICODE_TOU("Unicode-TOU"),

        /**
         * The Unlicense
         */
        LICENSE_UNLICENSE("Unlicense"),

        /**
         * Universal Permissive License v1.0
         */
        LICENSE_UPL_1_0("UPL-1.0"),

        /**
         * Vim License
         */
        LICENSE_VIM("Vim"),

        /**
         * VOSTROM Public License for Open Source
         */
        LICENSE_VOSTROM("VOSTROM"),

        /**
         * Vovida Software License v1.0
         */
        LICENSE_VSL_1_0("VSL-1.0"),

        /**
         * W3C Software Notice and License (1998-07-20)
         */
        LICENSE_W3C_19980720("W3C-19980720"),

        /**
         * W3C Software Notice and Document License (2015-05-13)
         */
        LICENSE_W3C_20150513("W3C-20150513"),

        /**
         * W3C Software Notice and License (2002-12-31)
         */
        LICENSE_W3C("W3C"),

        /**
         * Sybase Open Watcom Public License 1.0
         */
        LICENSE_WATCOM_1_0("Watcom-1.0"),

        /**
         * Wsuipa License
         */
        LICENSE_WSUIPA("Wsuipa"),

        /**
         * Do What The F*ck You Want To Public License
         */
        LICENSE_WTFPL("WTFPL"),

        /**
         * X11 License
         */
        LICENSE_X11("X11"),

        /**
         * Xerox License
         */
        LICENSE_XEROX("Xerox"),

        /**
         * XFree86 License 1.1
         */
        LICENSE_XFREE86_1_1("XFree86-1.1"),

        /**
         * xinetd License
         */
        LICENSE_XINETD("xinetd"),

        /**
         * X.Net License
         */
        LICENSE_XNET("Xnet"),

        /**
         * XPP License
         */
        LICENSE_XPP("xpp"),

        /**
         * XSkat License
         */
        LICENSE_XSKAT("XSkat"),

        /**
         * Yahoo! Public License v1.0
         */
        LICENSE_YPL_1_0("YPL-1.0"),

        /**
         * Yahoo! Public License v1.1
         */
        LICENSE_YPL_1_1("YPL-1.1"),

        /**
         * Zed License
         */
        LICENSE_ZED("Zed"),

        /**
         * Zend License v2.0
         */
        LICENSE_ZEND_2_0("Zend-2.0"),

        /**
         * Zimbra Public License v1.3
         */
        LICENSE_ZIMBRA_1_3("Zimbra-1.3"),

        /**
         * Zimbra Public License v1.4
         */
        LICENSE_ZIMBRA_1_4("Zimbra-1.4"),

        /**
         * zlib/libpng License with Acknowledgement
         */
        LICENSE_ZLIB_ACKNOWLEDGEMENT("zlib-acknowledgement"),

        /**
         * zlib License
         */
        LICENSE_ZLIB("Zlib"),

        /**
         * Zope Public License 1.1
         */
        LICENSE_ZPL_1_1("ZPL-1.1"),

        /**
         * Zope Public License 2.0
         */
        LICENSE_ZPL_2_0("ZPL-2.0"),

        /**
         * Zope Public License 2.1
         */
        LICENSE_ZPL_2_1("ZPL-2.1");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
