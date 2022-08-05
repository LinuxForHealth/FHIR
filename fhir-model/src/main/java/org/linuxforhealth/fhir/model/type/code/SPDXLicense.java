/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/spdx-license")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class SPDXLicense extends Code {
    /**
     * Not open source
     * 
     * <p>Not an open source license.
     */
    public static final SPDXLicense LICENSE_NOT_OPEN_SOURCE = SPDXLicense.builder().value(Value.LICENSE_NOT_OPEN_SOURCE).build();

    /**
     * BSD Zero Clause License
     * 
     * <p>BSD Zero Clause License.
     */
    public static final SPDXLicense LICENSE_0BSD = SPDXLicense.builder().value(Value.LICENSE_0BSD).build();

    /**
     * Attribution Assurance License
     * 
     * <p>Attribution Assurance License.
     */
    public static final SPDXLicense LICENSE_AAL = SPDXLicense.builder().value(Value.LICENSE_AAL).build();

    /**
     * Abstyles License
     * 
     * <p>Abstyles License.
     */
    public static final SPDXLicense LICENSE_ABSTYLES = SPDXLicense.builder().value(Value.LICENSE_ABSTYLES).build();

    /**
     * Adobe Systems Incorporated Source Code License Agreement
     * 
     * <p>Adobe Systems Incorporated Source Code License Agreement.
     */
    public static final SPDXLicense LICENSE_ADOBE_2006 = SPDXLicense.builder().value(Value.LICENSE_ADOBE_2006).build();

    /**
     * Adobe Glyph List License
     * 
     * <p>Adobe Glyph List License.
     */
    public static final SPDXLicense LICENSE_ADOBE_GLYPH = SPDXLicense.builder().value(Value.LICENSE_ADOBE_GLYPH).build();

    /**
     * Amazon Digital Services License
     * 
     * <p>Amazon Digital Services License.
     */
    public static final SPDXLicense LICENSE_ADSL = SPDXLicense.builder().value(Value.LICENSE_ADSL).build();

    /**
     * Academic Free License v1.1
     * 
     * <p>Academic Free License v1.1.
     */
    public static final SPDXLicense LICENSE_AFL_1_1 = SPDXLicense.builder().value(Value.LICENSE_AFL_1_1).build();

    /**
     * Academic Free License v1.2
     * 
     * <p>Academic Free License v1.2.
     */
    public static final SPDXLicense LICENSE_AFL_1_2 = SPDXLicense.builder().value(Value.LICENSE_AFL_1_2).build();

    /**
     * Academic Free License v2.0
     * 
     * <p>Academic Free License v2.0.
     */
    public static final SPDXLicense LICENSE_AFL_2_0 = SPDXLicense.builder().value(Value.LICENSE_AFL_2_0).build();

    /**
     * Academic Free License v2.1
     * 
     * <p>Academic Free License v2.1.
     */
    public static final SPDXLicense LICENSE_AFL_2_1 = SPDXLicense.builder().value(Value.LICENSE_AFL_2_1).build();

    /**
     * Academic Free License v3.0
     * 
     * <p>Academic Free License v3.0.
     */
    public static final SPDXLicense LICENSE_AFL_3_0 = SPDXLicense.builder().value(Value.LICENSE_AFL_3_0).build();

    /**
     * Afmparse License
     * 
     * <p>Afmparse License.
     */
    public static final SPDXLicense LICENSE_AFMPARSE = SPDXLicense.builder().value(Value.LICENSE_AFMPARSE).build();

    /**
     * Affero General Public License v1.0 only
     * 
     * <p>Affero General Public License v1.0 only.
     */
    public static final SPDXLicense LICENSE_AGPL_1_0_ONLY = SPDXLicense.builder().value(Value.LICENSE_AGPL_1_0_ONLY).build();

    /**
     * Affero General Public License v1.0 or later
     * 
     * <p>Affero General Public License v1.0 or later.
     */
    public static final SPDXLicense LICENSE_AGPL_1_0_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_AGPL_1_0_OR_LATER).build();

    /**
     * GNU Affero General Public License v3.0 only
     * 
     * <p>GNU Affero General Public License v3.0 only.
     */
    public static final SPDXLicense LICENSE_AGPL_3_0_ONLY = SPDXLicense.builder().value(Value.LICENSE_AGPL_3_0_ONLY).build();

    /**
     * GNU Affero General Public License v3.0 or later
     * 
     * <p>GNU Affero General Public License v3.0 or later.
     */
    public static final SPDXLicense LICENSE_AGPL_3_0_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_AGPL_3_0_OR_LATER).build();

    /**
     * Aladdin Free Public License
     * 
     * <p>Aladdin Free Public License.
     */
    public static final SPDXLicense LICENSE_ALADDIN = SPDXLicense.builder().value(Value.LICENSE_ALADDIN).build();

    /**
     * AMD's plpa_map.c License
     * 
     * <p>AMD's plpa_map.c License.
     */
    public static final SPDXLicense LICENSE_AMDPLPA = SPDXLicense.builder().value(Value.LICENSE_AMDPLPA).build();

    /**
     * Apple MIT License
     * 
     * <p>Apple MIT License.
     */
    public static final SPDXLicense LICENSE_AML = SPDXLicense.builder().value(Value.LICENSE_AML).build();

    /**
     * Academy of Motion Picture Arts and Sciences BSD
     * 
     * <p>Academy of Motion Picture Arts and Sciences BSD.
     */
    public static final SPDXLicense LICENSE_AMPAS = SPDXLicense.builder().value(Value.LICENSE_AMPAS).build();

    /**
     * ANTLR Software Rights Notice
     * 
     * <p>ANTLR Software Rights Notice.
     */
    public static final SPDXLicense LICENSE_ANTLR_PD = SPDXLicense.builder().value(Value.LICENSE_ANTLR_PD).build();

    /**
     * Apache License 1.0
     * 
     * <p>Apache License 1.0.
     */
    public static final SPDXLicense LICENSE_APACHE_1_0 = SPDXLicense.builder().value(Value.LICENSE_APACHE_1_0).build();

    /**
     * Apache License 1.1
     * 
     * <p>Apache License 1.1.
     */
    public static final SPDXLicense LICENSE_APACHE_1_1 = SPDXLicense.builder().value(Value.LICENSE_APACHE_1_1).build();

    /**
     * Apache License 2.0
     * 
     * <p>Apache License 2.0.
     */
    public static final SPDXLicense LICENSE_APACHE_2_0 = SPDXLicense.builder().value(Value.LICENSE_APACHE_2_0).build();

    /**
     * Adobe Postscript AFM License
     * 
     * <p>Adobe Postscript AFM License.
     */
    public static final SPDXLicense LICENSE_APAFML = SPDXLicense.builder().value(Value.LICENSE_APAFML).build();

    /**
     * Adaptive Public License 1.0
     * 
     * <p>Adaptive Public License 1.0.
     */
    public static final SPDXLicense LICENSE_APL_1_0 = SPDXLicense.builder().value(Value.LICENSE_APL_1_0).build();

    /**
     * Apple Public Source License 1.0
     * 
     * <p>Apple Public Source License 1.0.
     */
    public static final SPDXLicense LICENSE_APSL_1_0 = SPDXLicense.builder().value(Value.LICENSE_APSL_1_0).build();

    /**
     * Apple Public Source License 1.1
     * 
     * <p>Apple Public Source License 1.1.
     */
    public static final SPDXLicense LICENSE_APSL_1_1 = SPDXLicense.builder().value(Value.LICENSE_APSL_1_1).build();

    /**
     * Apple Public Source License 1.2
     * 
     * <p>Apple Public Source License 1.2.
     */
    public static final SPDXLicense LICENSE_APSL_1_2 = SPDXLicense.builder().value(Value.LICENSE_APSL_1_2).build();

    /**
     * Apple Public Source License 2.0
     * 
     * <p>Apple Public Source License 2.0.
     */
    public static final SPDXLicense LICENSE_APSL_2_0 = SPDXLicense.builder().value(Value.LICENSE_APSL_2_0).build();

    /**
     * Artistic License 1.0 w/clause 8
     * 
     * <p>Artistic License 1.0 w/clause 8.
     */
    public static final SPDXLicense LICENSE_ARTISTIC_1_0_CL8 = SPDXLicense.builder().value(Value.LICENSE_ARTISTIC_1_0_CL8).build();

    /**
     * Artistic License 1.0 (Perl)
     * 
     * <p>Artistic License 1.0 (Perl).
     */
    public static final SPDXLicense LICENSE_ARTISTIC_1_0_PERL = SPDXLicense.builder().value(Value.LICENSE_ARTISTIC_1_0_PERL).build();

    /**
     * Artistic License 1.0
     * 
     * <p>Artistic License 1.0.
     */
    public static final SPDXLicense LICENSE_ARTISTIC_1_0 = SPDXLicense.builder().value(Value.LICENSE_ARTISTIC_1_0).build();

    /**
     * Artistic License 2.0
     * 
     * <p>Artistic License 2.0.
     */
    public static final SPDXLicense LICENSE_ARTISTIC_2_0 = SPDXLicense.builder().value(Value.LICENSE_ARTISTIC_2_0).build();

    /**
     * Bahyph License
     * 
     * <p>Bahyph License.
     */
    public static final SPDXLicense LICENSE_BAHYPH = SPDXLicense.builder().value(Value.LICENSE_BAHYPH).build();

    /**
     * Barr License
     * 
     * <p>Barr License.
     */
    public static final SPDXLicense LICENSE_BARR = SPDXLicense.builder().value(Value.LICENSE_BARR).build();

    /**
     * Beerware License
     * 
     * <p>Beerware License.
     */
    public static final SPDXLicense LICENSE_BEERWARE = SPDXLicense.builder().value(Value.LICENSE_BEERWARE).build();

    /**
     * BitTorrent Open Source License v1.0
     * 
     * <p>BitTorrent Open Source License v1.0.
     */
    public static final SPDXLicense LICENSE_BIT_TORRENT_1_0 = SPDXLicense.builder().value(Value.LICENSE_BIT_TORRENT_1_0).build();

    /**
     * BitTorrent Open Source License v1.1
     * 
     * <p>BitTorrent Open Source License v1.1.
     */
    public static final SPDXLicense LICENSE_BIT_TORRENT_1_1 = SPDXLicense.builder().value(Value.LICENSE_BIT_TORRENT_1_1).build();

    /**
     * Borceux license
     * 
     * <p>Borceux license.
     */
    public static final SPDXLicense LICENSE_BORCEUX = SPDXLicense.builder().value(Value.LICENSE_BORCEUX).build();

    /**
     * BSD 1-Clause License
     * 
     * <p>BSD 1-Clause License.
     */
    public static final SPDXLicense LICENSE_BSD_1_CLAUSE = SPDXLicense.builder().value(Value.LICENSE_BSD_1_CLAUSE).build();

    /**
     * BSD 2-Clause FreeBSD License
     * 
     * <p>BSD 2-Clause FreeBSD License.
     */
    public static final SPDXLicense LICENSE_BSD_2_CLAUSE_FREE_BSD = SPDXLicense.builder().value(Value.LICENSE_BSD_2_CLAUSE_FREE_BSD).build();

    /**
     * BSD 2-Clause NetBSD License
     * 
     * <p>BSD 2-Clause NetBSD License.
     */
    public static final SPDXLicense LICENSE_BSD_2_CLAUSE_NET_BSD = SPDXLicense.builder().value(Value.LICENSE_BSD_2_CLAUSE_NET_BSD).build();

    /**
     * BSD-2-Clause Plus Patent License
     * 
     * <p>BSD-2-Clause Plus Patent License.
     */
    public static final SPDXLicense LICENSE_BSD_2_CLAUSE_PATENT = SPDXLicense.builder().value(Value.LICENSE_BSD_2_CLAUSE_PATENT).build();

    /**
     * BSD 2-Clause "Simplified" License
     * 
     * <p>BSD 2-Clause "Simplified" License.
     */
    public static final SPDXLicense LICENSE_BSD_2_CLAUSE = SPDXLicense.builder().value(Value.LICENSE_BSD_2_CLAUSE).build();

    /**
     * BSD with attribution
     * 
     * <p>BSD with attribution.
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_ATTRIBUTION = SPDXLicense.builder().value(Value.LICENSE_BSD_3_CLAUSE_ATTRIBUTION).build();

    /**
     * BSD 3-Clause Clear License
     * 
     * <p>BSD 3-Clause Clear License.
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_CLEAR = SPDXLicense.builder().value(Value.LICENSE_BSD_3_CLAUSE_CLEAR).build();

    /**
     * Lawrence Berkeley National Labs BSD variant license
     * 
     * <p>Lawrence Berkeley National Labs BSD variant license.
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_LBNL = SPDXLicense.builder().value(Value.LICENSE_BSD_3_CLAUSE_LBNL).build();

    /**
     * BSD 3-Clause No Nuclear License 2014
     * 
     * <p>BSD 3-Clause No Nuclear License 2014.
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE_2014 = SPDXLicense.builder().value(Value.LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE_2014).build();

    /**
     * BSD 3-Clause No Nuclear License
     * 
     * <p>BSD 3-Clause No Nuclear License.
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE = SPDXLicense.builder().value(Value.LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE).build();

    /**
     * BSD 3-Clause No Nuclear Warranty
     * 
     * <p>BSD 3-Clause No Nuclear Warranty.
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_WARRANTY = SPDXLicense.builder().value(Value.LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_WARRANTY).build();

    /**
     * BSD 3-Clause "New" or "Revised" License
     * 
     * <p>BSD 3-Clause "New" or "Revised" License.
     */
    public static final SPDXLicense LICENSE_BSD_3_CLAUSE = SPDXLicense.builder().value(Value.LICENSE_BSD_3_CLAUSE).build();

    /**
     * BSD-4-Clause (University of California-Specific)
     * 
     * <p>BSD-4-Clause (University of California-Specific).
     */
    public static final SPDXLicense LICENSE_BSD_4_CLAUSE_UC = SPDXLicense.builder().value(Value.LICENSE_BSD_4_CLAUSE_UC).build();

    /**
     * BSD 4-Clause "Original" or "Old" License
     * 
     * <p>BSD 4-Clause "Original" or "Old" License.
     */
    public static final SPDXLicense LICENSE_BSD_4_CLAUSE = SPDXLicense.builder().value(Value.LICENSE_BSD_4_CLAUSE).build();

    /**
     * BSD Protection License
     * 
     * <p>BSD Protection License.
     */
    public static final SPDXLicense LICENSE_BSD_PROTECTION = SPDXLicense.builder().value(Value.LICENSE_BSD_PROTECTION).build();

    /**
     * BSD Source Code Attribution
     * 
     * <p>BSD Source Code Attribution.
     */
    public static final SPDXLicense LICENSE_BSD_SOURCE_CODE = SPDXLicense.builder().value(Value.LICENSE_BSD_SOURCE_CODE).build();

    /**
     * Boost Software License 1.0
     * 
     * <p>Boost Software License 1.0.
     */
    public static final SPDXLicense LICENSE_BSL_1_0 = SPDXLicense.builder().value(Value.LICENSE_BSL_1_0).build();

    /**
     * bzip2 and libbzip2 License v1.0.5
     * 
     * <p>bzip2 and libbzip2 License v1.0.5.
     */
    public static final SPDXLicense LICENSE_BZIP2_1_0_5 = SPDXLicense.builder().value(Value.LICENSE_BZIP2_1_0_5).build();

    /**
     * bzip2 and libbzip2 License v1.0.6
     * 
     * <p>bzip2 and libbzip2 License v1.0.6.
     */
    public static final SPDXLicense LICENSE_BZIP2_1_0_6 = SPDXLicense.builder().value(Value.LICENSE_BZIP2_1_0_6).build();

    /**
     * Caldera License
     * 
     * <p>Caldera License.
     */
    public static final SPDXLicense LICENSE_CALDERA = SPDXLicense.builder().value(Value.LICENSE_CALDERA).build();

    /**
     * Computer Associates Trusted Open Source License 1.1
     * 
     * <p>Computer Associates Trusted Open Source License 1.1.
     */
    public static final SPDXLicense LICENSE_CATOSL_1_1 = SPDXLicense.builder().value(Value.LICENSE_CATOSL_1_1).build();

    /**
     * Creative Commons Attribution 1.0 Generic
     * 
     * <p>Creative Commons Attribution 1.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_1_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_1_0).build();

    /**
     * Creative Commons Attribution 2.0 Generic
     * 
     * <p>Creative Commons Attribution 2.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_2_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_2_0).build();

    /**
     * Creative Commons Attribution 2.5 Generic
     * 
     * <p>Creative Commons Attribution 2.5 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_2_5 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_2_5).build();

    /**
     * Creative Commons Attribution 3.0 Unported
     * 
     * <p>Creative Commons Attribution 3.0 Unported.
     */
    public static final SPDXLicense LICENSE_CC_BY_3_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_3_0).build();

    /**
     * Creative Commons Attribution 4.0 International
     * 
     * <p>Creative Commons Attribution 4.0 International.
     */
    public static final SPDXLicense LICENSE_CC_BY_4_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_4_0).build();

    /**
     * Creative Commons Attribution Non Commercial 1.0 Generic
     * 
     * <p>Creative Commons Attribution Non Commercial 1.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_1_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_1_0).build();

    /**
     * Creative Commons Attribution Non Commercial 2.0 Generic
     * 
     * <p>Creative Commons Attribution Non Commercial 2.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_2_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_2_0).build();

    /**
     * Creative Commons Attribution Non Commercial 2.5 Generic
     * 
     * <p>Creative Commons Attribution Non Commercial 2.5 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_2_5 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_2_5).build();

    /**
     * Creative Commons Attribution Non Commercial 3.0 Unported
     * 
     * <p>Creative Commons Attribution Non Commercial 3.0 Unported.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_3_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_3_0).build();

    /**
     * Creative Commons Attribution Non Commercial 4.0 International
     * 
     * <p>Creative Commons Attribution Non Commercial 4.0 International.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_4_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_4_0).build();

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 1.0 Generic
     * 
     * <p>Creative Commons Attribution Non Commercial No Derivatives 1.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_1_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_ND_1_0).build();

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 2.0 Generic
     * 
     * <p>Creative Commons Attribution Non Commercial No Derivatives 2.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_2_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_ND_2_0).build();

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 2.5 Generic
     * 
     * <p>Creative Commons Attribution Non Commercial No Derivatives 2.5 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_2_5 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_ND_2_5).build();

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 3.0 Unported
     * 
     * <p>Creative Commons Attribution Non Commercial No Derivatives 3.0 Unported.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_3_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_ND_3_0).build();

    /**
     * Creative Commons Attribution Non Commercial No Derivatives 4.0 International
     * 
     * <p>Creative Commons Attribution Non Commercial No Derivatives 4.0 International.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_ND_4_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_ND_4_0).build();

    /**
     * Creative Commons Attribution Non Commercial Share Alike 1.0 Generic
     * 
     * <p>Creative Commons Attribution Non Commercial Share Alike 1.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_1_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_SA_1_0).build();

    /**
     * Creative Commons Attribution Non Commercial Share Alike 2.0 Generic
     * 
     * <p>Creative Commons Attribution Non Commercial Share Alike 2.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_2_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_SA_2_0).build();

    /**
     * Creative Commons Attribution Non Commercial Share Alike 2.5 Generic
     * 
     * <p>Creative Commons Attribution Non Commercial Share Alike 2.5 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_2_5 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_SA_2_5).build();

    /**
     * Creative Commons Attribution Non Commercial Share Alike 3.0 Unported
     * 
     * <p>Creative Commons Attribution Non Commercial Share Alike 3.0 Unported.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_3_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_SA_3_0).build();

    /**
     * Creative Commons Attribution Non Commercial Share Alike 4.0 International
     * 
     * <p>Creative Commons Attribution Non Commercial Share Alike 4.0 International.
     */
    public static final SPDXLicense LICENSE_CC_BY_NC_SA_4_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_NC_SA_4_0).build();

    /**
     * Creative Commons Attribution No Derivatives 1.0 Generic
     * 
     * <p>Creative Commons Attribution No Derivatives 1.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_1_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_ND_1_0).build();

    /**
     * Creative Commons Attribution No Derivatives 2.0 Generic
     * 
     * <p>Creative Commons Attribution No Derivatives 2.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_2_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_ND_2_0).build();

    /**
     * Creative Commons Attribution No Derivatives 2.5 Generic
     * 
     * <p>Creative Commons Attribution No Derivatives 2.5 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_2_5 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_ND_2_5).build();

    /**
     * Creative Commons Attribution No Derivatives 3.0 Unported
     * 
     * <p>Creative Commons Attribution No Derivatives 3.0 Unported.
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_3_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_ND_3_0).build();

    /**
     * Creative Commons Attribution No Derivatives 4.0 International
     * 
     * <p>Creative Commons Attribution No Derivatives 4.0 International.
     */
    public static final SPDXLicense LICENSE_CC_BY_ND_4_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_ND_4_0).build();

    /**
     * Creative Commons Attribution Share Alike 1.0 Generic
     * 
     * <p>Creative Commons Attribution Share Alike 1.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_1_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_SA_1_0).build();

    /**
     * Creative Commons Attribution Share Alike 2.0 Generic
     * 
     * <p>Creative Commons Attribution Share Alike 2.0 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_2_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_SA_2_0).build();

    /**
     * Creative Commons Attribution Share Alike 2.5 Generic
     * 
     * <p>Creative Commons Attribution Share Alike 2.5 Generic.
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_2_5 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_SA_2_5).build();

    /**
     * Creative Commons Attribution Share Alike 3.0 Unported
     * 
     * <p>Creative Commons Attribution Share Alike 3.0 Unported.
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_3_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_SA_3_0).build();

    /**
     * Creative Commons Attribution Share Alike 4.0 International
     * 
     * <p>Creative Commons Attribution Share Alike 4.0 International.
     */
    public static final SPDXLicense LICENSE_CC_BY_SA_4_0 = SPDXLicense.builder().value(Value.LICENSE_CC_BY_SA_4_0).build();

    /**
     * Creative Commons Zero v1.0 Universal
     * 
     * <p>Creative Commons Zero v1.0 Universal.
     */
    public static final SPDXLicense LICENSE_CC0_1_0 = SPDXLicense.builder().value(Value.LICENSE_CC0_1_0).build();

    /**
     * Common Development and Distribution License 1.0
     * 
     * <p>Common Development and Distribution License 1.0.
     */
    public static final SPDXLicense LICENSE_CDDL_1_0 = SPDXLicense.builder().value(Value.LICENSE_CDDL_1_0).build();

    /**
     * Common Development and Distribution License 1.1
     * 
     * <p>Common Development and Distribution License 1.1.
     */
    public static final SPDXLicense LICENSE_CDDL_1_1 = SPDXLicense.builder().value(Value.LICENSE_CDDL_1_1).build();

    /**
     * Community Data License Agreement Permissive 1.0
     * 
     * <p>Community Data License Agreement Permissive 1.0.
     */
    public static final SPDXLicense LICENSE_CDLA_PERMISSIVE_1_0 = SPDXLicense.builder().value(Value.LICENSE_CDLA_PERMISSIVE_1_0).build();

    /**
     * Community Data License Agreement Sharing 1.0
     * 
     * <p>Community Data License Agreement Sharing 1.0.
     */
    public static final SPDXLicense LICENSE_CDLA_SHARING_1_0 = SPDXLicense.builder().value(Value.LICENSE_CDLA_SHARING_1_0).build();

    /**
     * CeCILL Free Software License Agreement v1.0
     * 
     * <p>CeCILL Free Software License Agreement v1.0.
     */
    public static final SPDXLicense LICENSE_CECILL_1_0 = SPDXLicense.builder().value(Value.LICENSE_CECILL_1_0).build();

    /**
     * CeCILL Free Software License Agreement v1.1
     * 
     * <p>CeCILL Free Software License Agreement v1.1.
     */
    public static final SPDXLicense LICENSE_CECILL_1_1 = SPDXLicense.builder().value(Value.LICENSE_CECILL_1_1).build();

    /**
     * CeCILL Free Software License Agreement v2.0
     * 
     * <p>CeCILL Free Software License Agreement v2.0.
     */
    public static final SPDXLicense LICENSE_CECILL_2_0 = SPDXLicense.builder().value(Value.LICENSE_CECILL_2_0).build();

    /**
     * CeCILL Free Software License Agreement v2.1
     * 
     * <p>CeCILL Free Software License Agreement v2.1.
     */
    public static final SPDXLicense LICENSE_CECILL_2_1 = SPDXLicense.builder().value(Value.LICENSE_CECILL_2_1).build();

    /**
     * CeCILL-B Free Software License Agreement
     * 
     * <p>CeCILL-B Free Software License Agreement.
     */
    public static final SPDXLicense LICENSE_CECILL_B = SPDXLicense.builder().value(Value.LICENSE_CECILL_B).build();

    /**
     * CeCILL-C Free Software License Agreement
     * 
     * <p>CeCILL-C Free Software License Agreement.
     */
    public static final SPDXLicense LICENSE_CECILL_C = SPDXLicense.builder().value(Value.LICENSE_CECILL_C).build();

    /**
     * Clarified Artistic License
     * 
     * <p>Clarified Artistic License.
     */
    public static final SPDXLicense LICENSE_CL_ARTISTIC = SPDXLicense.builder().value(Value.LICENSE_CL_ARTISTIC).build();

    /**
     * CNRI Jython License
     * 
     * <p>CNRI Jython License.
     */
    public static final SPDXLicense LICENSE_CNRI_JYTHON = SPDXLicense.builder().value(Value.LICENSE_CNRI_JYTHON).build();

    /**
     * CNRI Python Open Source GPL Compatible License Agreement
     * 
     * <p>CNRI Python Open Source GPL Compatible License Agreement.
     */
    public static final SPDXLicense LICENSE_CNRI_PYTHON_GPL_COMPATIBLE = SPDXLicense.builder().value(Value.LICENSE_CNRI_PYTHON_GPL_COMPATIBLE).build();

    /**
     * CNRI Python License
     * 
     * <p>CNRI Python License.
     */
    public static final SPDXLicense LICENSE_CNRI_PYTHON = SPDXLicense.builder().value(Value.LICENSE_CNRI_PYTHON).build();

    /**
     * Condor Public License v1.1
     * 
     * <p>Condor Public License v1.1.
     */
    public static final SPDXLicense LICENSE_CONDOR_1_1 = SPDXLicense.builder().value(Value.LICENSE_CONDOR_1_1).build();

    /**
     * Common Public Attribution License 1.0
     * 
     * <p>Common Public Attribution License 1.0.
     */
    public static final SPDXLicense LICENSE_CPAL_1_0 = SPDXLicense.builder().value(Value.LICENSE_CPAL_1_0).build();

    /**
     * Common Public License 1.0
     * 
     * <p>Common Public License 1.0.
     */
    public static final SPDXLicense LICENSE_CPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_CPL_1_0).build();

    /**
     * Code Project Open License 1.02
     * 
     * <p>Code Project Open License 1.02.
     */
    public static final SPDXLicense LICENSE_CPOL_1_02 = SPDXLicense.builder().value(Value.LICENSE_CPOL_1_02).build();

    /**
     * Crossword License
     * 
     * <p>Crossword License.
     */
    public static final SPDXLicense LICENSE_CROSSWORD = SPDXLicense.builder().value(Value.LICENSE_CROSSWORD).build();

    /**
     * CrystalStacker License
     * 
     * <p>CrystalStacker License.
     */
    public static final SPDXLicense LICENSE_CRYSTAL_STACKER = SPDXLicense.builder().value(Value.LICENSE_CRYSTAL_STACKER).build();

    /**
     * CUA Office Public License v1.0
     * 
     * <p>CUA Office Public License v1.0.
     */
    public static final SPDXLicense LICENSE_CUA_OPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_CUA_OPL_1_0).build();

    /**
     * Cube License
     * 
     * <p>Cube License.
     */
    public static final SPDXLicense LICENSE_CUBE = SPDXLicense.builder().value(Value.LICENSE_CUBE).build();

    /**
     * curl License
     * 
     * <p>curl License.
     */
    public static final SPDXLicense LICENSE_CURL = SPDXLicense.builder().value(Value.LICENSE_CURL).build();

    /**
     * Deutsche Freie Software Lizenz
     * 
     * <p>Deutsche Freie Software Lizenz.
     */
    public static final SPDXLicense LICENSE_D_FSL_1_0 = SPDXLicense.builder().value(Value.LICENSE_D_FSL_1_0).build();

    /**
     * diffmark license
     * 
     * <p>diffmark license.
     */
    public static final SPDXLicense LICENSE_DIFFMARK = SPDXLicense.builder().value(Value.LICENSE_DIFFMARK).build();

    /**
     * DOC License
     * 
     * <p>DOC License.
     */
    public static final SPDXLicense LICENSE_DOC = SPDXLicense.builder().value(Value.LICENSE_DOC).build();

    /**
     * Dotseqn License
     * 
     * <p>Dotseqn License.
     */
    public static final SPDXLicense LICENSE_DOTSEQN = SPDXLicense.builder().value(Value.LICENSE_DOTSEQN).build();

    /**
     * DSDP License
     * 
     * <p>DSDP License.
     */
    public static final SPDXLicense LICENSE_DSDP = SPDXLicense.builder().value(Value.LICENSE_DSDP).build();

    /**
     * dvipdfm License
     * 
     * <p>dvipdfm License.
     */
    public static final SPDXLicense LICENSE_DVIPDFM = SPDXLicense.builder().value(Value.LICENSE_DVIPDFM).build();

    /**
     * Educational Community License v1.0
     * 
     * <p>Educational Community License v1.0.
     */
    public static final SPDXLicense LICENSE_ECL_1_0 = SPDXLicense.builder().value(Value.LICENSE_ECL_1_0).build();

    /**
     * Educational Community License v2.0
     * 
     * <p>Educational Community License v2.0.
     */
    public static final SPDXLicense LICENSE_ECL_2_0 = SPDXLicense.builder().value(Value.LICENSE_ECL_2_0).build();

    /**
     * Eiffel Forum License v1.0
     * 
     * <p>Eiffel Forum License v1.0.
     */
    public static final SPDXLicense LICENSE_EFL_1_0 = SPDXLicense.builder().value(Value.LICENSE_EFL_1_0).build();

    /**
     * Eiffel Forum License v2.0
     * 
     * <p>Eiffel Forum License v2.0.
     */
    public static final SPDXLicense LICENSE_EFL_2_0 = SPDXLicense.builder().value(Value.LICENSE_EFL_2_0).build();

    /**
     * eGenix.com Public License 1.1.0
     * 
     * <p>eGenix.com Public License 1.1.0.
     */
    public static final SPDXLicense LICENSE_E_GENIX = SPDXLicense.builder().value(Value.LICENSE_E_GENIX).build();

    /**
     * Entessa Public License v1.0
     * 
     * <p>Entessa Public License v1.0.
     */
    public static final SPDXLicense LICENSE_ENTESSA = SPDXLicense.builder().value(Value.LICENSE_ENTESSA).build();

    /**
     * Eclipse Public License 1.0
     * 
     * <p>Eclipse Public License 1.0.
     */
    public static final SPDXLicense LICENSE_EPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_EPL_1_0).build();

    /**
     * Eclipse Public License 2.0
     * 
     * <p>Eclipse Public License 2.0.
     */
    public static final SPDXLicense LICENSE_EPL_2_0 = SPDXLicense.builder().value(Value.LICENSE_EPL_2_0).build();

    /**
     * Erlang Public License v1.1
     * 
     * <p>Erlang Public License v1.1.
     */
    public static final SPDXLicense LICENSE_ERL_PL_1_1 = SPDXLicense.builder().value(Value.LICENSE_ERL_PL_1_1).build();

    /**
     * EU DataGrid Software License
     * 
     * <p>EU DataGrid Software License.
     */
    public static final SPDXLicense LICENSE_EUDATAGRID = SPDXLicense.builder().value(Value.LICENSE_EUDATAGRID).build();

    /**
     * European Union Public License 1.0
     * 
     * <p>European Union Public License 1.0.
     */
    public static final SPDXLicense LICENSE_EUPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_EUPL_1_0).build();

    /**
     * European Union Public License 1.1
     * 
     * <p>European Union Public License 1.1.
     */
    public static final SPDXLicense LICENSE_EUPL_1_1 = SPDXLicense.builder().value(Value.LICENSE_EUPL_1_1).build();

    /**
     * European Union Public License 1.2
     * 
     * <p>European Union Public License 1.2.
     */
    public static final SPDXLicense LICENSE_EUPL_1_2 = SPDXLicense.builder().value(Value.LICENSE_EUPL_1_2).build();

    /**
     * Eurosym License
     * 
     * <p>Eurosym License.
     */
    public static final SPDXLicense LICENSE_EUROSYM = SPDXLicense.builder().value(Value.LICENSE_EUROSYM).build();

    /**
     * Fair License
     * 
     * <p>Fair License.
     */
    public static final SPDXLicense LICENSE_FAIR = SPDXLicense.builder().value(Value.LICENSE_FAIR).build();

    /**
     * Frameworx Open License 1.0
     * 
     * <p>Frameworx Open License 1.0.
     */
    public static final SPDXLicense LICENSE_FRAMEWORX_1_0 = SPDXLicense.builder().value(Value.LICENSE_FRAMEWORX_1_0).build();

    /**
     * FreeImage Public License v1.0
     * 
     * <p>FreeImage Public License v1.0.
     */
    public static final SPDXLicense LICENSE_FREE_IMAGE = SPDXLicense.builder().value(Value.LICENSE_FREE_IMAGE).build();

    /**
     * FSF All Permissive License
     * 
     * <p>FSF All Permissive License.
     */
    public static final SPDXLicense LICENSE_FSFAP = SPDXLicense.builder().value(Value.LICENSE_FSFAP).build();

    /**
     * FSF Unlimited License
     * 
     * <p>FSF Unlimited License.
     */
    public static final SPDXLicense LICENSE_FSFUL = SPDXLicense.builder().value(Value.LICENSE_FSFUL).build();

    /**
     * FSF Unlimited License (with License Retention)
     * 
     * <p>FSF Unlimited License (with License Retention).
     */
    public static final SPDXLicense LICENSE_FSFULLR = SPDXLicense.builder().value(Value.LICENSE_FSFULLR).build();

    /**
     * Freetype Project License
     * 
     * <p>Freetype Project License.
     */
    public static final SPDXLicense LICENSE_FTL = SPDXLicense.builder().value(Value.LICENSE_FTL).build();

    /**
     * GNU Free Documentation License v1.1 only
     * 
     * <p>GNU Free Documentation License v1.1 only.
     */
    public static final SPDXLicense LICENSE_GFDL_1_1_ONLY = SPDXLicense.builder().value(Value.LICENSE_GFDL_1_1_ONLY).build();

    /**
     * GNU Free Documentation License v1.1 or later
     * 
     * <p>GNU Free Documentation License v1.1 or later.
     */
    public static final SPDXLicense LICENSE_GFDL_1_1_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_GFDL_1_1_OR_LATER).build();

    /**
     * GNU Free Documentation License v1.2 only
     * 
     * <p>GNU Free Documentation License v1.2 only.
     */
    public static final SPDXLicense LICENSE_GFDL_1_2_ONLY = SPDXLicense.builder().value(Value.LICENSE_GFDL_1_2_ONLY).build();

    /**
     * GNU Free Documentation License v1.2 or later
     * 
     * <p>GNU Free Documentation License v1.2 or later.
     */
    public static final SPDXLicense LICENSE_GFDL_1_2_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_GFDL_1_2_OR_LATER).build();

    /**
     * GNU Free Documentation License v1.3 only
     * 
     * <p>GNU Free Documentation License v1.3 only.
     */
    public static final SPDXLicense LICENSE_GFDL_1_3_ONLY = SPDXLicense.builder().value(Value.LICENSE_GFDL_1_3_ONLY).build();

    /**
     * GNU Free Documentation License v1.3 or later
     * 
     * <p>GNU Free Documentation License v1.3 or later.
     */
    public static final SPDXLicense LICENSE_GFDL_1_3_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_GFDL_1_3_OR_LATER).build();

    /**
     * Giftware License
     * 
     * <p>Giftware License.
     */
    public static final SPDXLicense LICENSE_GIFTWARE = SPDXLicense.builder().value(Value.LICENSE_GIFTWARE).build();

    /**
     * GL2PS License
     * 
     * <p>GL2PS License.
     */
    public static final SPDXLicense LICENSE_GL2PS = SPDXLicense.builder().value(Value.LICENSE_GL2PS).build();

    /**
     * 3dfx Glide License
     * 
     * <p>3dfx Glide License.
     */
    public static final SPDXLicense LICENSE_GLIDE = SPDXLicense.builder().value(Value.LICENSE_GLIDE).build();

    /**
     * Glulxe License
     * 
     * <p>Glulxe License.
     */
    public static final SPDXLicense LICENSE_GLULXE = SPDXLicense.builder().value(Value.LICENSE_GLULXE).build();

    /**
     * gnuplot License
     * 
     * <p>gnuplot License.
     */
    public static final SPDXLicense LICENSE_GNUPLOT = SPDXLicense.builder().value(Value.LICENSE_GNUPLOT).build();

    /**
     * GNU General Public License v1.0 only
     * 
     * <p>GNU General Public License v1.0 only.
     */
    public static final SPDXLicense LICENSE_GPL_1_0_ONLY = SPDXLicense.builder().value(Value.LICENSE_GPL_1_0_ONLY).build();

    /**
     * GNU General Public License v1.0 or later
     * 
     * <p>GNU General Public License v1.0 or later.
     */
    public static final SPDXLicense LICENSE_GPL_1_0_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_GPL_1_0_OR_LATER).build();

    /**
     * GNU General Public License v2.0 only
     * 
     * <p>GNU General Public License v2.0 only.
     */
    public static final SPDXLicense LICENSE_GPL_2_0_ONLY = SPDXLicense.builder().value(Value.LICENSE_GPL_2_0_ONLY).build();

    /**
     * GNU General Public License v2.0 or later
     * 
     * <p>GNU General Public License v2.0 or later.
     */
    public static final SPDXLicense LICENSE_GPL_2_0_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_GPL_2_0_OR_LATER).build();

    /**
     * GNU General Public License v3.0 only
     * 
     * <p>GNU General Public License v3.0 only.
     */
    public static final SPDXLicense LICENSE_GPL_3_0_ONLY = SPDXLicense.builder().value(Value.LICENSE_GPL_3_0_ONLY).build();

    /**
     * GNU General Public License v3.0 or later
     * 
     * <p>GNU General Public License v3.0 or later.
     */
    public static final SPDXLicense LICENSE_GPL_3_0_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_GPL_3_0_OR_LATER).build();

    /**
     * gSOAP Public License v1.3b
     * 
     * <p>gSOAP Public License v1.3b.
     */
    public static final SPDXLicense LICENSE_G_SOAP_1_3B = SPDXLicense.builder().value(Value.LICENSE_G_SOAP_1_3B).build();

    /**
     * Haskell Language Report License
     * 
     * <p>Haskell Language Report License.
     */
    public static final SPDXLicense LICENSE_HASKELL_REPORT = SPDXLicense.builder().value(Value.LICENSE_HASKELL_REPORT).build();

    /**
     * Historical Permission Notice and Disclaimer
     * 
     * <p>Historical Permission Notice and Disclaimer.
     */
    public static final SPDXLicense LICENSE_HPND = SPDXLicense.builder().value(Value.LICENSE_HPND).build();

    /**
     * IBM PowerPC Initialization and Boot Software
     * 
     * <p>IBM PowerPC Initialization and Boot Software.
     */
    public static final SPDXLicense LICENSE_IBM_PIBS = SPDXLicense.builder().value(Value.LICENSE_IBM_PIBS).build();

    /**
     * ICU License
     * 
     * <p>ICU License.
     */
    public static final SPDXLicense LICENSE_ICU = SPDXLicense.builder().value(Value.LICENSE_ICU).build();

    /**
     * Independent JPEG Group License
     * 
     * <p>Independent JPEG Group License.
     */
    public static final SPDXLicense LICENSE_IJG = SPDXLicense.builder().value(Value.LICENSE_IJG).build();

    /**
     * ImageMagick License
     * 
     * <p>ImageMagick License.
     */
    public static final SPDXLicense LICENSE_IMAGE_MAGICK = SPDXLicense.builder().value(Value.LICENSE_IMAGE_MAGICK).build();

    /**
     * iMatix Standard Function Library Agreement
     * 
     * <p>iMatix Standard Function Library Agreement.
     */
    public static final SPDXLicense LICENSE_I_MATIX = SPDXLicense.builder().value(Value.LICENSE_I_MATIX).build();

    /**
     * Imlib2 License
     * 
     * <p>Imlib2 License.
     */
    public static final SPDXLicense LICENSE_IMLIB2 = SPDXLicense.builder().value(Value.LICENSE_IMLIB2).build();

    /**
     * Info-ZIP License
     * 
     * <p>Info-ZIP License.
     */
    public static final SPDXLicense LICENSE_INFO_ZIP = SPDXLicense.builder().value(Value.LICENSE_INFO_ZIP).build();

    /**
     * Intel ACPI Software License Agreement
     * 
     * <p>Intel ACPI Software License Agreement.
     */
    public static final SPDXLicense LICENSE_INTEL_ACPI = SPDXLicense.builder().value(Value.LICENSE_INTEL_ACPI).build();

    /**
     * Intel Open Source License
     * 
     * <p>Intel Open Source License.
     */
    public static final SPDXLicense LICENSE_INTEL = SPDXLicense.builder().value(Value.LICENSE_INTEL).build();

    /**
     * Interbase Public License v1.0
     * 
     * <p>Interbase Public License v1.0.
     */
    public static final SPDXLicense LICENSE_INTERBASE_1_0 = SPDXLicense.builder().value(Value.LICENSE_INTERBASE_1_0).build();

    /**
     * IPA Font License
     * 
     * <p>IPA Font License.
     */
    public static final SPDXLicense LICENSE_IPA = SPDXLicense.builder().value(Value.LICENSE_IPA).build();

    /**
     * IBM Public License v1.0
     * 
     * <p>IBM Public License v1.0.
     */
    public static final SPDXLicense LICENSE_IPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_IPL_1_0).build();

    /**
     * ISC License
     * 
     * <p>ISC License.
     */
    public static final SPDXLicense LICENSE_ISC = SPDXLicense.builder().value(Value.LICENSE_ISC).build();

    /**
     * JasPer License
     * 
     * <p>JasPer License.
     */
    public static final SPDXLicense LICENSE_JAS_PER_2_0 = SPDXLicense.builder().value(Value.LICENSE_JAS_PER_2_0).build();

    /**
     * JSON License
     * 
     * <p>JSON License.
     */
    public static final SPDXLicense LICENSE_JSON = SPDXLicense.builder().value(Value.LICENSE_JSON).build();

    /**
     * Licence Art Libre 1.2
     * 
     * <p>Licence Art Libre 1.2.
     */
    public static final SPDXLicense LICENSE_LAL_1_2 = SPDXLicense.builder().value(Value.LICENSE_LAL_1_2).build();

    /**
     * Licence Art Libre 1.3
     * 
     * <p>Licence Art Libre 1.3.
     */
    public static final SPDXLicense LICENSE_LAL_1_3 = SPDXLicense.builder().value(Value.LICENSE_LAL_1_3).build();

    /**
     * Latex2e License
     * 
     * <p>Latex2e License.
     */
    public static final SPDXLicense LICENSE_LATEX2E = SPDXLicense.builder().value(Value.LICENSE_LATEX2E).build();

    /**
     * Leptonica License
     * 
     * <p>Leptonica License.
     */
    public static final SPDXLicense LICENSE_LEPTONICA = SPDXLicense.builder().value(Value.LICENSE_LEPTONICA).build();

    /**
     * GNU Library General Public License v2 only
     * 
     * <p>GNU Library General Public License v2 only.
     */
    public static final SPDXLicense LICENSE_LGPL_2_0_ONLY = SPDXLicense.builder().value(Value.LICENSE_LGPL_2_0_ONLY).build();

    /**
     * GNU Library General Public License v2 or later
     * 
     * <p>GNU Library General Public License v2 or later.
     */
    public static final SPDXLicense LICENSE_LGPL_2_0_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_LGPL_2_0_OR_LATER).build();

    /**
     * GNU Lesser General Public License v2.1 only
     * 
     * <p>GNU Lesser General Public License v2.1 only.
     */
    public static final SPDXLicense LICENSE_LGPL_2_1_ONLY = SPDXLicense.builder().value(Value.LICENSE_LGPL_2_1_ONLY).build();

    /**
     * GNU Lesser General Public License v2.1 or later
     * 
     * <p>GNU Lesser General Public License v2.1 or later.
     */
    public static final SPDXLicense LICENSE_LGPL_2_1_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_LGPL_2_1_OR_LATER).build();

    /**
     * GNU Lesser General Public License v3.0 only
     * 
     * <p>GNU Lesser General Public License v3.0 only.
     */
    public static final SPDXLicense LICENSE_LGPL_3_0_ONLY = SPDXLicense.builder().value(Value.LICENSE_LGPL_3_0_ONLY).build();

    /**
     * GNU Lesser General Public License v3.0 or later
     * 
     * <p>GNU Lesser General Public License v3.0 or later.
     */
    public static final SPDXLicense LICENSE_LGPL_3_0_OR_LATER = SPDXLicense.builder().value(Value.LICENSE_LGPL_3_0_OR_LATER).build();

    /**
     * Lesser General Public License For Linguistic Resources
     * 
     * <p>Lesser General Public License For Linguistic Resources.
     */
    public static final SPDXLicense LICENSE_LGPLLR = SPDXLicense.builder().value(Value.LICENSE_LGPLLR).build();

    /**
     * libpng License
     * 
     * <p>libpng License.
     */
    public static final SPDXLicense LICENSE_LIBPNG = SPDXLicense.builder().value(Value.LICENSE_LIBPNG).build();

    /**
     * libtiff License
     * 
     * <p>libtiff License.
     */
    public static final SPDXLicense LICENSE_LIBTIFF = SPDXLicense.builder().value(Value.LICENSE_LIBTIFF).build();

    /**
     * Licence Libre du Québec – Permissive version 1.1
     * 
     * <p>Licence Libre du Québec – Permissive version 1.1.
     */
    public static final SPDXLicense LICENSE_LI_LI_Q_P_1_1 = SPDXLicense.builder().value(Value.LICENSE_LI_LI_Q_P_1_1).build();

    /**
     * Licence Libre du Québec – Réciprocité version 1.1
     * 
     * <p>Licence Libre du Québec – Réciprocité version 1.1.
     */
    public static final SPDXLicense LICENSE_LI_LI_Q_R_1_1 = SPDXLicense.builder().value(Value.LICENSE_LI_LI_Q_R_1_1).build();

    /**
     * Licence Libre du Québec – Réciprocité forte version 1.1
     * 
     * <p>Licence Libre du Québec – Réciprocité forte version 1.1.
     */
    public static final SPDXLicense LICENSE_LI_LI_Q_RPLUS_1_1 = SPDXLicense.builder().value(Value.LICENSE_LI_LI_Q_RPLUS_1_1).build();

    /**
     * Linux Kernel Variant of OpenIB.org license
     * 
     * <p>Linux Kernel Variant of OpenIB.org license.
     */
    public static final SPDXLicense LICENSE_LINUX_OPEN_IB = SPDXLicense.builder().value(Value.LICENSE_LINUX_OPEN_IB).build();

    /**
     * Lucent Public License Version 1.0
     * 
     * <p>Lucent Public License Version 1.0.
     */
    public static final SPDXLicense LICENSE_LPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_LPL_1_0).build();

    /**
     * Lucent Public License v1.02
     * 
     * <p>Lucent Public License v1.02.
     */
    public static final SPDXLicense LICENSE_LPL_1_02 = SPDXLicense.builder().value(Value.LICENSE_LPL_1_02).build();

    /**
     * LaTeX Project Public License v1.0
     * 
     * <p>LaTeX Project Public License v1.0.
     */
    public static final SPDXLicense LICENSE_LPPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_LPPL_1_0).build();

    /**
     * LaTeX Project Public License v1.1
     * 
     * <p>LaTeX Project Public License v1.1.
     */
    public static final SPDXLicense LICENSE_LPPL_1_1 = SPDXLicense.builder().value(Value.LICENSE_LPPL_1_1).build();

    /**
     * LaTeX Project Public License v1.2
     * 
     * <p>LaTeX Project Public License v1.2.
     */
    public static final SPDXLicense LICENSE_LPPL_1_2 = SPDXLicense.builder().value(Value.LICENSE_LPPL_1_2).build();

    /**
     * LaTeX Project Public License v1.3a
     * 
     * <p>LaTeX Project Public License v1.3a.
     */
    public static final SPDXLicense LICENSE_LPPL_1_3A = SPDXLicense.builder().value(Value.LICENSE_LPPL_1_3A).build();

    /**
     * LaTeX Project Public License v1.3c
     * 
     * <p>LaTeX Project Public License v1.3c.
     */
    public static final SPDXLicense LICENSE_LPPL_1_3C = SPDXLicense.builder().value(Value.LICENSE_LPPL_1_3C).build();

    /**
     * MakeIndex License
     * 
     * <p>MakeIndex License.
     */
    public static final SPDXLicense LICENSE_MAKE_INDEX = SPDXLicense.builder().value(Value.LICENSE_MAKE_INDEX).build();

    /**
     * MirOS License
     * 
     * <p>MirOS License.
     */
    public static final SPDXLicense LICENSE_MIR_OS = SPDXLicense.builder().value(Value.LICENSE_MIR_OS).build();

    /**
     * MIT No Attribution
     * 
     * <p>MIT No Attribution.
     */
    public static final SPDXLicense LICENSE_MIT_0 = SPDXLicense.builder().value(Value.LICENSE_MIT_0).build();

    /**
     * Enlightenment License (e16)
     * 
     * <p>Enlightenment License (e16).
     */
    public static final SPDXLicense LICENSE_MIT_ADVERTISING = SPDXLicense.builder().value(Value.LICENSE_MIT_ADVERTISING).build();

    /**
     * CMU License
     * 
     * <p>CMU License.
     */
    public static final SPDXLicense LICENSE_MIT_CMU = SPDXLicense.builder().value(Value.LICENSE_MIT_CMU).build();

    /**
     * enna License
     * 
     * <p>enna License.
     */
    public static final SPDXLicense LICENSE_MIT_ENNA = SPDXLicense.builder().value(Value.LICENSE_MIT_ENNA).build();

    /**
     * feh License
     * 
     * <p>feh License.
     */
    public static final SPDXLicense LICENSE_MIT_FEH = SPDXLicense.builder().value(Value.LICENSE_MIT_FEH).build();

    /**
     * MIT License
     * 
     * <p>MIT License.
     */
    public static final SPDXLicense LICENSE_MIT = SPDXLicense.builder().value(Value.LICENSE_MIT).build();

    /**
     * MIT +no-false-attribs license
     * 
     * <p>MIT +no-false-attribs license.
     */
    public static final SPDXLicense LICENSE_MITNFA = SPDXLicense.builder().value(Value.LICENSE_MITNFA).build();

    /**
     * Motosoto License
     * 
     * <p>Motosoto License.
     */
    public static final SPDXLicense LICENSE_MOTOSOTO = SPDXLicense.builder().value(Value.LICENSE_MOTOSOTO).build();

    /**
     * mpich2 License
     * 
     * <p>mpich2 License.
     */
    public static final SPDXLicense LICENSE_MPICH2 = SPDXLicense.builder().value(Value.LICENSE_MPICH2).build();

    /**
     * Mozilla Public License 1.0
     * 
     * <p>Mozilla Public License 1.0.
     */
    public static final SPDXLicense LICENSE_MPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_MPL_1_0).build();

    /**
     * Mozilla Public License 1.1
     * 
     * <p>Mozilla Public License 1.1.
     */
    public static final SPDXLicense LICENSE_MPL_1_1 = SPDXLicense.builder().value(Value.LICENSE_MPL_1_1).build();

    /**
     * Mozilla Public License 2.0 (no copyleft exception)
     * 
     * <p>Mozilla Public License 2.0 (no copyleft exception).
     */
    public static final SPDXLicense LICENSE_MPL_2_0_NO_COPYLEFT_EXCEPTION = SPDXLicense.builder().value(Value.LICENSE_MPL_2_0_NO_COPYLEFT_EXCEPTION).build();

    /**
     * Mozilla Public License 2.0
     * 
     * <p>Mozilla Public License 2.0.
     */
    public static final SPDXLicense LICENSE_MPL_2_0 = SPDXLicense.builder().value(Value.LICENSE_MPL_2_0).build();

    /**
     * Microsoft Public License
     * 
     * <p>Microsoft Public License.
     */
    public static final SPDXLicense LICENSE_MS_PL = SPDXLicense.builder().value(Value.LICENSE_MS_PL).build();

    /**
     * Microsoft Reciprocal License
     * 
     * <p>Microsoft Reciprocal License.
     */
    public static final SPDXLicense LICENSE_MS_RL = SPDXLicense.builder().value(Value.LICENSE_MS_RL).build();

    /**
     * Matrix Template Library License
     * 
     * <p>Matrix Template Library License.
     */
    public static final SPDXLicense LICENSE_MTLL = SPDXLicense.builder().value(Value.LICENSE_MTLL).build();

    /**
     * Multics License
     * 
     * <p>Multics License.
     */
    public static final SPDXLicense LICENSE_MULTICS = SPDXLicense.builder().value(Value.LICENSE_MULTICS).build();

    /**
     * Mup License
     * 
     * <p>Mup License.
     */
    public static final SPDXLicense LICENSE_MUP = SPDXLicense.builder().value(Value.LICENSE_MUP).build();

    /**
     * NASA Open Source Agreement 1.3
     * 
     * <p>NASA Open Source Agreement 1.3.
     */
    public static final SPDXLicense LICENSE_NASA_1_3 = SPDXLicense.builder().value(Value.LICENSE_NASA_1_3).build();

    /**
     * Naumen Public License
     * 
     * <p>Naumen Public License.
     */
    public static final SPDXLicense LICENSE_NAUMEN = SPDXLicense.builder().value(Value.LICENSE_NAUMEN).build();

    /**
     * Net Boolean Public License v1
     * 
     * <p>Net Boolean Public License v1.
     */
    public static final SPDXLicense LICENSE_NBPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_NBPL_1_0).build();

    /**
     * University of Illinois/NCSA Open Source License
     * 
     * <p>University of Illinois/NCSA Open Source License.
     */
    public static final SPDXLicense LICENSE_NCSA = SPDXLicense.builder().value(Value.LICENSE_NCSA).build();

    /**
     * Net-SNMP License
     * 
     * <p>Net-SNMP License.
     */
    public static final SPDXLicense LICENSE_NET_SNMP = SPDXLicense.builder().value(Value.LICENSE_NET_SNMP).build();

    /**
     * NetCDF license
     * 
     * <p>NetCDF license.
     */
    public static final SPDXLicense LICENSE_NET_CDF = SPDXLicense.builder().value(Value.LICENSE_NET_CDF).build();

    /**
     * Newsletr License
     * 
     * <p>Newsletr License.
     */
    public static final SPDXLicense LICENSE_NEWSLETR = SPDXLicense.builder().value(Value.LICENSE_NEWSLETR).build();

    /**
     * Nethack General Public License
     * 
     * <p>Nethack General Public License.
     */
    public static final SPDXLicense LICENSE_NGPL = SPDXLicense.builder().value(Value.LICENSE_NGPL).build();

    /**
     * Norwegian Licence for Open Government Data
     * 
     * <p>Norwegian Licence for Open Government Data.
     */
    public static final SPDXLicense LICENSE_NLOD_1_0 = SPDXLicense.builder().value(Value.LICENSE_NLOD_1_0).build();

    /**
     * No Limit Public License
     * 
     * <p>No Limit Public License.
     */
    public static final SPDXLicense LICENSE_NLPL = SPDXLicense.builder().value(Value.LICENSE_NLPL).build();

    /**
     * Nokia Open Source License
     * 
     * <p>Nokia Open Source License.
     */
    public static final SPDXLicense LICENSE_NOKIA = SPDXLicense.builder().value(Value.LICENSE_NOKIA).build();

    /**
     * Netizen Open Source License
     * 
     * <p>Netizen Open Source License.
     */
    public static final SPDXLicense LICENSE_NOSL = SPDXLicense.builder().value(Value.LICENSE_NOSL).build();

    /**
     * Noweb License
     * 
     * <p>Noweb License.
     */
    public static final SPDXLicense LICENSE_NOWEB = SPDXLicense.builder().value(Value.LICENSE_NOWEB).build();

    /**
     * Netscape Public License v1.0
     * 
     * <p>Netscape Public License v1.0.
     */
    public static final SPDXLicense LICENSE_NPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_NPL_1_0).build();

    /**
     * Netscape Public License v1.1
     * 
     * <p>Netscape Public License v1.1.
     */
    public static final SPDXLicense LICENSE_NPL_1_1 = SPDXLicense.builder().value(Value.LICENSE_NPL_1_1).build();

    /**
     * Non-Profit Open Software License 3.0
     * 
     * <p>Non-Profit Open Software License 3.0.
     */
    public static final SPDXLicense LICENSE_NPOSL_3_0 = SPDXLicense.builder().value(Value.LICENSE_NPOSL_3_0).build();

    /**
     * NRL License
     * 
     * <p>NRL License.
     */
    public static final SPDXLicense LICENSE_NRL = SPDXLicense.builder().value(Value.LICENSE_NRL).build();

    /**
     * NTP License
     * 
     * <p>NTP License.
     */
    public static final SPDXLicense LICENSE_NTP = SPDXLicense.builder().value(Value.LICENSE_NTP).build();

    /**
     * Open CASCADE Technology Public License
     * 
     * <p>Open CASCADE Technology Public License.
     */
    public static final SPDXLicense LICENSE_OCCT_PL = SPDXLicense.builder().value(Value.LICENSE_OCCT_PL).build();

    /**
     * OCLC Research Public License 2.0
     * 
     * <p>OCLC Research Public License 2.0.
     */
    public static final SPDXLicense LICENSE_OCLC_2_0 = SPDXLicense.builder().value(Value.LICENSE_OCLC_2_0).build();

    /**
     * ODC Open Database License v1.0
     * 
     * <p>ODC Open Database License v1.0.
     */
    public static final SPDXLicense LICENSE_ODB_L_1_0 = SPDXLicense.builder().value(Value.LICENSE_ODB_L_1_0).build();

    /**
     * SIL Open Font License 1.0
     * 
     * <p>SIL Open Font License 1.0.
     */
    public static final SPDXLicense LICENSE_OFL_1_0 = SPDXLicense.builder().value(Value.LICENSE_OFL_1_0).build();

    /**
     * SIL Open Font License 1.1
     * 
     * <p>SIL Open Font License 1.1.
     */
    public static final SPDXLicense LICENSE_OFL_1_1 = SPDXLicense.builder().value(Value.LICENSE_OFL_1_1).build();

    /**
     * Open Group Test Suite License
     * 
     * <p>Open Group Test Suite License.
     */
    public static final SPDXLicense LICENSE_OGTSL = SPDXLicense.builder().value(Value.LICENSE_OGTSL).build();

    /**
     * Open LDAP Public License v1.1
     * 
     * <p>Open LDAP Public License v1.1.
     */
    public static final SPDXLicense LICENSE_OLDAP_1_1 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_1_1).build();

    /**
     * Open LDAP Public License v1.2
     * 
     * <p>Open LDAP Public License v1.2.
     */
    public static final SPDXLicense LICENSE_OLDAP_1_2 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_1_2).build();

    /**
     * Open LDAP Public License v1.3
     * 
     * <p>Open LDAP Public License v1.3.
     */
    public static final SPDXLicense LICENSE_OLDAP_1_3 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_1_3).build();

    /**
     * Open LDAP Public License v1.4
     * 
     * <p>Open LDAP Public License v1.4.
     */
    public static final SPDXLicense LICENSE_OLDAP_1_4 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_1_4).build();

    /**
     * Open LDAP Public License v2.0.1
     * 
     * <p>Open LDAP Public License v2.0.1.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_0_1 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_0_1).build();

    /**
     * Open LDAP Public License v2.0 (or possibly 2.0A and 2.0B)
     * 
     * <p>Open LDAP Public License v2.0 (or possibly 2.0A and 2.0B).
     */
    public static final SPDXLicense LICENSE_OLDAP_2_0 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_0).build();

    /**
     * Open LDAP Public License v2.1
     * 
     * <p>Open LDAP Public License v2.1.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_1 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_1).build();

    /**
     * Open LDAP Public License v2.2.1
     * 
     * <p>Open LDAP Public License v2.2.1.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_2_1 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_2_1).build();

    /**
     * Open LDAP Public License 2.2.2
     * 
     * <p>Open LDAP Public License 2.2.2.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_2_2 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_2_2).build();

    /**
     * Open LDAP Public License v2.2
     * 
     * <p>Open LDAP Public License v2.2.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_2 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_2).build();

    /**
     * Open LDAP Public License v2.3
     * 
     * <p>Open LDAP Public License v2.3.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_3 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_3).build();

    /**
     * Open LDAP Public License v2.4
     * 
     * <p>Open LDAP Public License v2.4.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_4 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_4).build();

    /**
     * Open LDAP Public License v2.5
     * 
     * <p>Open LDAP Public License v2.5.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_5 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_5).build();

    /**
     * Open LDAP Public License v2.6
     * 
     * <p>Open LDAP Public License v2.6.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_6 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_6).build();

    /**
     * Open LDAP Public License v2.7
     * 
     * <p>Open LDAP Public License v2.7.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_7 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_7).build();

    /**
     * Open LDAP Public License v2.8
     * 
     * <p>Open LDAP Public License v2.8.
     */
    public static final SPDXLicense LICENSE_OLDAP_2_8 = SPDXLicense.builder().value(Value.LICENSE_OLDAP_2_8).build();

    /**
     * Open Market License
     * 
     * <p>Open Market License.
     */
    public static final SPDXLicense LICENSE_OML = SPDXLicense.builder().value(Value.LICENSE_OML).build();

    /**
     * OpenSSL License
     * 
     * <p>OpenSSL License.
     */
    public static final SPDXLicense LICENSE_OPEN_SSL = SPDXLicense.builder().value(Value.LICENSE_OPEN_SSL).build();

    /**
     * Open Public License v1.0
     * 
     * <p>Open Public License v1.0.
     */
    public static final SPDXLicense LICENSE_OPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_OPL_1_0).build();

    /**
     * OSET Public License version 2.1
     * 
     * <p>OSET Public License version 2.1.
     */
    public static final SPDXLicense LICENSE_OSET_PL_2_1 = SPDXLicense.builder().value(Value.LICENSE_OSET_PL_2_1).build();

    /**
     * Open Software License 1.0
     * 
     * <p>Open Software License 1.0.
     */
    public static final SPDXLicense LICENSE_OSL_1_0 = SPDXLicense.builder().value(Value.LICENSE_OSL_1_0).build();

    /**
     * Open Software License 1.1
     * 
     * <p>Open Software License 1.1.
     */
    public static final SPDXLicense LICENSE_OSL_1_1 = SPDXLicense.builder().value(Value.LICENSE_OSL_1_1).build();

    /**
     * Open Software License 2.0
     * 
     * <p>Open Software License 2.0.
     */
    public static final SPDXLicense LICENSE_OSL_2_0 = SPDXLicense.builder().value(Value.LICENSE_OSL_2_0).build();

    /**
     * Open Software License 2.1
     * 
     * <p>Open Software License 2.1.
     */
    public static final SPDXLicense LICENSE_OSL_2_1 = SPDXLicense.builder().value(Value.LICENSE_OSL_2_1).build();

    /**
     * Open Software License 3.0
     * 
     * <p>Open Software License 3.0.
     */
    public static final SPDXLicense LICENSE_OSL_3_0 = SPDXLicense.builder().value(Value.LICENSE_OSL_3_0).build();

    /**
     * ODC Public Domain Dedication &amp; License 1.0
     * 
     * <p>ODC Public Domain Dedication &amp; License 1.0.
     */
    public static final SPDXLicense LICENSE_PDDL_1_0 = SPDXLicense.builder().value(Value.LICENSE_PDDL_1_0).build();

    /**
     * PHP License v3.0
     * 
     * <p>PHP License v3.0.
     */
    public static final SPDXLicense LICENSE_PHP_3_0 = SPDXLicense.builder().value(Value.LICENSE_PHP_3_0).build();

    /**
     * PHP License v3.01
     * 
     * <p>PHP License v3.01.
     */
    public static final SPDXLicense LICENSE_PHP_3_01 = SPDXLicense.builder().value(Value.LICENSE_PHP_3_01).build();

    /**
     * Plexus Classworlds License
     * 
     * <p>Plexus Classworlds License.
     */
    public static final SPDXLicense LICENSE_PLEXUS = SPDXLicense.builder().value(Value.LICENSE_PLEXUS).build();

    /**
     * PostgreSQL License
     * 
     * <p>PostgreSQL License.
     */
    public static final SPDXLicense LICENSE_POSTGRE_SQL = SPDXLicense.builder().value(Value.LICENSE_POSTGRE_SQL).build();

    /**
     * psfrag License
     * 
     * <p>psfrag License.
     */
    public static final SPDXLicense LICENSE_PSFRAG = SPDXLicense.builder().value(Value.LICENSE_PSFRAG).build();

    /**
     * psutils License
     * 
     * <p>psutils License.
     */
    public static final SPDXLicense LICENSE_PSUTILS = SPDXLicense.builder().value(Value.LICENSE_PSUTILS).build();

    /**
     * Python License 2.0
     * 
     * <p>Python License 2.0.
     */
    public static final SPDXLicense LICENSE_PYTHON_2_0 = SPDXLicense.builder().value(Value.LICENSE_PYTHON_2_0).build();

    /**
     * Qhull License
     * 
     * <p>Qhull License.
     */
    public static final SPDXLicense LICENSE_QHULL = SPDXLicense.builder().value(Value.LICENSE_QHULL).build();

    /**
     * Q Public License 1.0
     * 
     * <p>Q Public License 1.0.
     */
    public static final SPDXLicense LICENSE_QPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_QPL_1_0).build();

    /**
     * Rdisc License
     * 
     * <p>Rdisc License.
     */
    public static final SPDXLicense LICENSE_RDISC = SPDXLicense.builder().value(Value.LICENSE_RDISC).build();

    /**
     * Red Hat eCos Public License v1.1
     * 
     * <p>Red Hat eCos Public License v1.1.
     */
    public static final SPDXLicense LICENSE_RHE_COS_1_1 = SPDXLicense.builder().value(Value.LICENSE_RHE_COS_1_1).build();

    /**
     * Reciprocal Public License 1.1
     * 
     * <p>Reciprocal Public License 1.1.
     */
    public static final SPDXLicense LICENSE_RPL_1_1 = SPDXLicense.builder().value(Value.LICENSE_RPL_1_1).build();

    /**
     * Reciprocal Public License 1.5
     * 
     * <p>Reciprocal Public License 1.5.
     */
    public static final SPDXLicense LICENSE_RPL_1_5 = SPDXLicense.builder().value(Value.LICENSE_RPL_1_5).build();

    /**
     * RealNetworks Public Source License v1.0
     * 
     * <p>RealNetworks Public Source License v1.0.
     */
    public static final SPDXLicense LICENSE_RPSL_1_0 = SPDXLicense.builder().value(Value.LICENSE_RPSL_1_0).build();

    /**
     * RSA Message-Digest License
     * 
     * <p>RSA Message-Digest License.
     */
    public static final SPDXLicense LICENSE_RSA_MD = SPDXLicense.builder().value(Value.LICENSE_RSA_MD).build();

    /**
     * Ricoh Source Code Public License
     * 
     * <p>Ricoh Source Code Public License.
     */
    public static final SPDXLicense LICENSE_RSCPL = SPDXLicense.builder().value(Value.LICENSE_RSCPL).build();

    /**
     * Ruby License
     * 
     * <p>Ruby License.
     */
    public static final SPDXLicense LICENSE_RUBY = SPDXLicense.builder().value(Value.LICENSE_RUBY).build();

    /**
     * Sax Public Domain Notice
     * 
     * <p>Sax Public Domain Notice.
     */
    public static final SPDXLicense LICENSE_SAX_PD = SPDXLicense.builder().value(Value.LICENSE_SAX_PD).build();

    /**
     * Saxpath License
     * 
     * <p>Saxpath License.
     */
    public static final SPDXLicense LICENSE_SAXPATH = SPDXLicense.builder().value(Value.LICENSE_SAXPATH).build();

    /**
     * SCEA Shared Source License
     * 
     * <p>SCEA Shared Source License.
     */
    public static final SPDXLicense LICENSE_SCEA = SPDXLicense.builder().value(Value.LICENSE_SCEA).build();

    /**
     * Sendmail License
     * 
     * <p>Sendmail License.
     */
    public static final SPDXLicense LICENSE_SENDMAIL = SPDXLicense.builder().value(Value.LICENSE_SENDMAIL).build();

    /**
     * SGI Free Software License B v1.0
     * 
     * <p>SGI Free Software License B v1.0.
     */
    public static final SPDXLicense LICENSE_SGI_B_1_0 = SPDXLicense.builder().value(Value.LICENSE_SGI_B_1_0).build();

    /**
     * SGI Free Software License B v1.1
     * 
     * <p>SGI Free Software License B v1.1.
     */
    public static final SPDXLicense LICENSE_SGI_B_1_1 = SPDXLicense.builder().value(Value.LICENSE_SGI_B_1_1).build();

    /**
     * SGI Free Software License B v2.0
     * 
     * <p>SGI Free Software License B v2.0.
     */
    public static final SPDXLicense LICENSE_SGI_B_2_0 = SPDXLicense.builder().value(Value.LICENSE_SGI_B_2_0).build();

    /**
     * Simple Public License 2.0
     * 
     * <p>Simple Public License 2.0.
     */
    public static final SPDXLicense LICENSE_SIM_PL_2_0 = SPDXLicense.builder().value(Value.LICENSE_SIM_PL_2_0).build();

    /**
     * Sun Industry Standards Source License v1.2
     * 
     * <p>Sun Industry Standards Source License v1.2.
     */
    public static final SPDXLicense LICENSE_SISSL_1_2 = SPDXLicense.builder().value(Value.LICENSE_SISSL_1_2).build();

    /**
     * Sun Industry Standards Source License v1.1
     * 
     * <p>Sun Industry Standards Source License v1.1.
     */
    public static final SPDXLicense LICENSE_SISSL = SPDXLicense.builder().value(Value.LICENSE_SISSL).build();

    /**
     * Sleepycat License
     * 
     * <p>Sleepycat License.
     */
    public static final SPDXLicense LICENSE_SLEEPYCAT = SPDXLicense.builder().value(Value.LICENSE_SLEEPYCAT).build();

    /**
     * Standard ML of New Jersey License
     * 
     * <p>Standard ML of New Jersey License.
     */
    public static final SPDXLicense LICENSE_SMLNJ = SPDXLicense.builder().value(Value.LICENSE_SMLNJ).build();

    /**
     * Secure Messaging Protocol Public License
     * 
     * <p>Secure Messaging Protocol Public License.
     */
    public static final SPDXLicense LICENSE_SMPPL = SPDXLicense.builder().value(Value.LICENSE_SMPPL).build();

    /**
     * SNIA Public License 1.1
     * 
     * <p>SNIA Public License 1.1.
     */
    public static final SPDXLicense LICENSE_SNIA = SPDXLicense.builder().value(Value.LICENSE_SNIA).build();

    /**
     * Spencer License 86
     * 
     * <p>Spencer License 86.
     */
    public static final SPDXLicense LICENSE_SPENCER_86 = SPDXLicense.builder().value(Value.LICENSE_SPENCER_86).build();

    /**
     * Spencer License 94
     * 
     * <p>Spencer License 94.
     */
    public static final SPDXLicense LICENSE_SPENCER_94 = SPDXLicense.builder().value(Value.LICENSE_SPENCER_94).build();

    /**
     * Spencer License 99
     * 
     * <p>Spencer License 99.
     */
    public static final SPDXLicense LICENSE_SPENCER_99 = SPDXLicense.builder().value(Value.LICENSE_SPENCER_99).build();

    /**
     * Sun Public License v1.0
     * 
     * <p>Sun Public License v1.0.
     */
    public static final SPDXLicense LICENSE_SPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_SPL_1_0).build();

    /**
     * SugarCRM Public License v1.1.3
     * 
     * <p>SugarCRM Public License v1.1.3.
     */
    public static final SPDXLicense LICENSE_SUGAR_CRM_1_1_3 = SPDXLicense.builder().value(Value.LICENSE_SUGAR_CRM_1_1_3).build();

    /**
     * Scheme Widget Library (SWL) Software License Agreement
     * 
     * <p>Scheme Widget Library (SWL) Software License Agreement.
     */
    public static final SPDXLicense LICENSE_SWL = SPDXLicense.builder().value(Value.LICENSE_SWL).build();

    /**
     * TCL/TK License
     * 
     * <p>TCL/TK License.
     */
    public static final SPDXLicense LICENSE_TCL = SPDXLicense.builder().value(Value.LICENSE_TCL).build();

    /**
     * TCP Wrappers License
     * 
     * <p>TCP Wrappers License.
     */
    public static final SPDXLicense LICENSE_TCP_WRAPPERS = SPDXLicense.builder().value(Value.LICENSE_TCP_WRAPPERS).build();

    /**
     * TMate Open Source License
     * 
     * <p>TMate Open Source License.
     */
    public static final SPDXLicense LICENSE_TMATE = SPDXLicense.builder().value(Value.LICENSE_TMATE).build();

    /**
     * TORQUE v2.5+ Software License v1.1
     * 
     * <p>TORQUE v2.5+ Software License v1.1.
     */
    public static final SPDXLicense LICENSE_TORQUE_1_1 = SPDXLicense.builder().value(Value.LICENSE_TORQUE_1_1).build();

    /**
     * Trusster Open Source License
     * 
     * <p>Trusster Open Source License.
     */
    public static final SPDXLicense LICENSE_TOSL = SPDXLicense.builder().value(Value.LICENSE_TOSL).build();

    /**
     * Unicode License Agreement - Data Files and Software (2015)
     * 
     * <p>Unicode License Agreement - Data Files and Software (2015).
     */
    public static final SPDXLicense LICENSE_UNICODE_DFS_2015 = SPDXLicense.builder().value(Value.LICENSE_UNICODE_DFS_2015).build();

    /**
     * Unicode License Agreement - Data Files and Software (2016)
     * 
     * <p>Unicode License Agreement - Data Files and Software (2016).
     */
    public static final SPDXLicense LICENSE_UNICODE_DFS_2016 = SPDXLicense.builder().value(Value.LICENSE_UNICODE_DFS_2016).build();

    /**
     * Unicode Terms of Use
     * 
     * <p>Unicode Terms of Use.
     */
    public static final SPDXLicense LICENSE_UNICODE_TOU = SPDXLicense.builder().value(Value.LICENSE_UNICODE_TOU).build();

    /**
     * The Unlicense
     * 
     * <p>The Unlicense.
     */
    public static final SPDXLicense LICENSE_UNLICENSE = SPDXLicense.builder().value(Value.LICENSE_UNLICENSE).build();

    /**
     * Universal Permissive License v1.0
     * 
     * <p>Universal Permissive License v1.0.
     */
    public static final SPDXLicense LICENSE_UPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_UPL_1_0).build();

    /**
     * Vim License
     * 
     * <p>Vim License.
     */
    public static final SPDXLicense LICENSE_VIM = SPDXLicense.builder().value(Value.LICENSE_VIM).build();

    /**
     * VOSTROM Public License for Open Source
     * 
     * <p>VOSTROM Public License for Open Source.
     */
    public static final SPDXLicense LICENSE_VOSTROM = SPDXLicense.builder().value(Value.LICENSE_VOSTROM).build();

    /**
     * Vovida Software License v1.0
     * 
     * <p>Vovida Software License v1.0.
     */
    public static final SPDXLicense LICENSE_VSL_1_0 = SPDXLicense.builder().value(Value.LICENSE_VSL_1_0).build();

    /**
     * W3C Software Notice and License (1998-07-20)
     * 
     * <p>W3C Software Notice and License (1998-07-20).
     */
    public static final SPDXLicense LICENSE_W3C_19980720 = SPDXLicense.builder().value(Value.LICENSE_W3C_19980720).build();

    /**
     * W3C Software Notice and Document License (2015-05-13)
     * 
     * <p>W3C Software Notice and Document License (2015-05-13).
     */
    public static final SPDXLicense LICENSE_W3C_20150513 = SPDXLicense.builder().value(Value.LICENSE_W3C_20150513).build();

    /**
     * W3C Software Notice and License (2002-12-31)
     * 
     * <p>W3C Software Notice and License (2002-12-31).
     */
    public static final SPDXLicense LICENSE_W3C = SPDXLicense.builder().value(Value.LICENSE_W3C).build();

    /**
     * Sybase Open Watcom Public License 1.0
     * 
     * <p>Sybase Open Watcom Public License 1.0.
     */
    public static final SPDXLicense LICENSE_WATCOM_1_0 = SPDXLicense.builder().value(Value.LICENSE_WATCOM_1_0).build();

    /**
     * Wsuipa License
     * 
     * <p>Wsuipa License.
     */
    public static final SPDXLicense LICENSE_WSUIPA = SPDXLicense.builder().value(Value.LICENSE_WSUIPA).build();

    /**
     * Do What The F*ck You Want To Public License
     * 
     * <p>Do What The F*ck You Want To Public License.
     */
    public static final SPDXLicense LICENSE_WTFPL = SPDXLicense.builder().value(Value.LICENSE_WTFPL).build();

    /**
     * X11 License
     * 
     * <p>X11 License.
     */
    public static final SPDXLicense LICENSE_X11 = SPDXLicense.builder().value(Value.LICENSE_X11).build();

    /**
     * Xerox License
     * 
     * <p>Xerox License.
     */
    public static final SPDXLicense LICENSE_XEROX = SPDXLicense.builder().value(Value.LICENSE_XEROX).build();

    /**
     * XFree86 License 1.1
     * 
     * <p>XFree86 License 1.1.
     */
    public static final SPDXLicense LICENSE_XFREE86_1_1 = SPDXLicense.builder().value(Value.LICENSE_XFREE86_1_1).build();

    /**
     * xinetd License
     * 
     * <p>xinetd License.
     */
    public static final SPDXLicense LICENSE_XINETD = SPDXLicense.builder().value(Value.LICENSE_XINETD).build();

    /**
     * X.Net License
     * 
     * <p>X.Net License.
     */
    public static final SPDXLicense LICENSE_XNET = SPDXLicense.builder().value(Value.LICENSE_XNET).build();

    /**
     * XPP License
     * 
     * <p>XPP License.
     */
    public static final SPDXLicense LICENSE_XPP = SPDXLicense.builder().value(Value.LICENSE_XPP).build();

    /**
     * XSkat License
     * 
     * <p>XSkat License.
     */
    public static final SPDXLicense LICENSE_XSKAT = SPDXLicense.builder().value(Value.LICENSE_XSKAT).build();

    /**
     * Yahoo! Public License v1.0
     * 
     * <p>Yahoo! Public License v1.0.
     */
    public static final SPDXLicense LICENSE_YPL_1_0 = SPDXLicense.builder().value(Value.LICENSE_YPL_1_0).build();

    /**
     * Yahoo! Public License v1.1
     * 
     * <p>Yahoo! Public License v1.1.
     */
    public static final SPDXLicense LICENSE_YPL_1_1 = SPDXLicense.builder().value(Value.LICENSE_YPL_1_1).build();

    /**
     * Zed License
     * 
     * <p>Zed License.
     */
    public static final SPDXLicense LICENSE_ZED = SPDXLicense.builder().value(Value.LICENSE_ZED).build();

    /**
     * Zend License v2.0
     * 
     * <p>Zend License v2.0.
     */
    public static final SPDXLicense LICENSE_ZEND_2_0 = SPDXLicense.builder().value(Value.LICENSE_ZEND_2_0).build();

    /**
     * Zimbra Public License v1.3
     * 
     * <p>Zimbra Public License v1.3.
     */
    public static final SPDXLicense LICENSE_ZIMBRA_1_3 = SPDXLicense.builder().value(Value.LICENSE_ZIMBRA_1_3).build();

    /**
     * Zimbra Public License v1.4
     * 
     * <p>Zimbra Public License v1.4.
     */
    public static final SPDXLicense LICENSE_ZIMBRA_1_4 = SPDXLicense.builder().value(Value.LICENSE_ZIMBRA_1_4).build();

    /**
     * zlib/libpng License with Acknowledgement
     * 
     * <p>zlib/libpng License with Acknowledgement.
     */
    public static final SPDXLicense LICENSE_ZLIB_ACKNOWLEDGEMENT = SPDXLicense.builder().value(Value.LICENSE_ZLIB_ACKNOWLEDGEMENT).build();

    /**
     * zlib License
     * 
     * <p>zlib License.
     */
    public static final SPDXLicense LICENSE_ZLIB = SPDXLicense.builder().value(Value.LICENSE_ZLIB).build();

    /**
     * Zope Public License 1.1
     * 
     * <p>Zope Public License 1.1.
     */
    public static final SPDXLicense LICENSE_ZPL_1_1 = SPDXLicense.builder().value(Value.LICENSE_ZPL_1_1).build();

    /**
     * Zope Public License 2.0
     * 
     * <p>Zope Public License 2.0.
     */
    public static final SPDXLicense LICENSE_ZPL_2_0 = SPDXLicense.builder().value(Value.LICENSE_ZPL_2_0).build();

    /**
     * Zope Public License 2.1
     * 
     * <p>Zope Public License 2.1.
     */
    public static final SPDXLicense LICENSE_ZPL_2_1 = SPDXLicense.builder().value(Value.LICENSE_ZPL_2_1).build();

    private volatile int hashCode;

    private SPDXLicense(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SPDXLicense as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SPDXLicense objects from a passed enum value.
     */
    public static SPDXLicense of(Value value) {
        switch (value) {
        case LICENSE_NOT_OPEN_SOURCE:
            return LICENSE_NOT_OPEN_SOURCE;
        case LICENSE_0BSD:
            return LICENSE_0BSD;
        case LICENSE_AAL:
            return LICENSE_AAL;
        case LICENSE_ABSTYLES:
            return LICENSE_ABSTYLES;
        case LICENSE_ADOBE_2006:
            return LICENSE_ADOBE_2006;
        case LICENSE_ADOBE_GLYPH:
            return LICENSE_ADOBE_GLYPH;
        case LICENSE_ADSL:
            return LICENSE_ADSL;
        case LICENSE_AFL_1_1:
            return LICENSE_AFL_1_1;
        case LICENSE_AFL_1_2:
            return LICENSE_AFL_1_2;
        case LICENSE_AFL_2_0:
            return LICENSE_AFL_2_0;
        case LICENSE_AFL_2_1:
            return LICENSE_AFL_2_1;
        case LICENSE_AFL_3_0:
            return LICENSE_AFL_3_0;
        case LICENSE_AFMPARSE:
            return LICENSE_AFMPARSE;
        case LICENSE_AGPL_1_0_ONLY:
            return LICENSE_AGPL_1_0_ONLY;
        case LICENSE_AGPL_1_0_OR_LATER:
            return LICENSE_AGPL_1_0_OR_LATER;
        case LICENSE_AGPL_3_0_ONLY:
            return LICENSE_AGPL_3_0_ONLY;
        case LICENSE_AGPL_3_0_OR_LATER:
            return LICENSE_AGPL_3_0_OR_LATER;
        case LICENSE_ALADDIN:
            return LICENSE_ALADDIN;
        case LICENSE_AMDPLPA:
            return LICENSE_AMDPLPA;
        case LICENSE_AML:
            return LICENSE_AML;
        case LICENSE_AMPAS:
            return LICENSE_AMPAS;
        case LICENSE_ANTLR_PD:
            return LICENSE_ANTLR_PD;
        case LICENSE_APACHE_1_0:
            return LICENSE_APACHE_1_0;
        case LICENSE_APACHE_1_1:
            return LICENSE_APACHE_1_1;
        case LICENSE_APACHE_2_0:
            return LICENSE_APACHE_2_0;
        case LICENSE_APAFML:
            return LICENSE_APAFML;
        case LICENSE_APL_1_0:
            return LICENSE_APL_1_0;
        case LICENSE_APSL_1_0:
            return LICENSE_APSL_1_0;
        case LICENSE_APSL_1_1:
            return LICENSE_APSL_1_1;
        case LICENSE_APSL_1_2:
            return LICENSE_APSL_1_2;
        case LICENSE_APSL_2_0:
            return LICENSE_APSL_2_0;
        case LICENSE_ARTISTIC_1_0_CL8:
            return LICENSE_ARTISTIC_1_0_CL8;
        case LICENSE_ARTISTIC_1_0_PERL:
            return LICENSE_ARTISTIC_1_0_PERL;
        case LICENSE_ARTISTIC_1_0:
            return LICENSE_ARTISTIC_1_0;
        case LICENSE_ARTISTIC_2_0:
            return LICENSE_ARTISTIC_2_0;
        case LICENSE_BAHYPH:
            return LICENSE_BAHYPH;
        case LICENSE_BARR:
            return LICENSE_BARR;
        case LICENSE_BEERWARE:
            return LICENSE_BEERWARE;
        case LICENSE_BIT_TORRENT_1_0:
            return LICENSE_BIT_TORRENT_1_0;
        case LICENSE_BIT_TORRENT_1_1:
            return LICENSE_BIT_TORRENT_1_1;
        case LICENSE_BORCEUX:
            return LICENSE_BORCEUX;
        case LICENSE_BSD_1_CLAUSE:
            return LICENSE_BSD_1_CLAUSE;
        case LICENSE_BSD_2_CLAUSE_FREE_BSD:
            return LICENSE_BSD_2_CLAUSE_FREE_BSD;
        case LICENSE_BSD_2_CLAUSE_NET_BSD:
            return LICENSE_BSD_2_CLAUSE_NET_BSD;
        case LICENSE_BSD_2_CLAUSE_PATENT:
            return LICENSE_BSD_2_CLAUSE_PATENT;
        case LICENSE_BSD_2_CLAUSE:
            return LICENSE_BSD_2_CLAUSE;
        case LICENSE_BSD_3_CLAUSE_ATTRIBUTION:
            return LICENSE_BSD_3_CLAUSE_ATTRIBUTION;
        case LICENSE_BSD_3_CLAUSE_CLEAR:
            return LICENSE_BSD_3_CLAUSE_CLEAR;
        case LICENSE_BSD_3_CLAUSE_LBNL:
            return LICENSE_BSD_3_CLAUSE_LBNL;
        case LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE_2014:
            return LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE_2014;
        case LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE:
            return LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE;
        case LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_WARRANTY:
            return LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_WARRANTY;
        case LICENSE_BSD_3_CLAUSE:
            return LICENSE_BSD_3_CLAUSE;
        case LICENSE_BSD_4_CLAUSE_UC:
            return LICENSE_BSD_4_CLAUSE_UC;
        case LICENSE_BSD_4_CLAUSE:
            return LICENSE_BSD_4_CLAUSE;
        case LICENSE_BSD_PROTECTION:
            return LICENSE_BSD_PROTECTION;
        case LICENSE_BSD_SOURCE_CODE:
            return LICENSE_BSD_SOURCE_CODE;
        case LICENSE_BSL_1_0:
            return LICENSE_BSL_1_0;
        case LICENSE_BZIP2_1_0_5:
            return LICENSE_BZIP2_1_0_5;
        case LICENSE_BZIP2_1_0_6:
            return LICENSE_BZIP2_1_0_6;
        case LICENSE_CALDERA:
            return LICENSE_CALDERA;
        case LICENSE_CATOSL_1_1:
            return LICENSE_CATOSL_1_1;
        case LICENSE_CC_BY_1_0:
            return LICENSE_CC_BY_1_0;
        case LICENSE_CC_BY_2_0:
            return LICENSE_CC_BY_2_0;
        case LICENSE_CC_BY_2_5:
            return LICENSE_CC_BY_2_5;
        case LICENSE_CC_BY_3_0:
            return LICENSE_CC_BY_3_0;
        case LICENSE_CC_BY_4_0:
            return LICENSE_CC_BY_4_0;
        case LICENSE_CC_BY_NC_1_0:
            return LICENSE_CC_BY_NC_1_0;
        case LICENSE_CC_BY_NC_2_0:
            return LICENSE_CC_BY_NC_2_0;
        case LICENSE_CC_BY_NC_2_5:
            return LICENSE_CC_BY_NC_2_5;
        case LICENSE_CC_BY_NC_3_0:
            return LICENSE_CC_BY_NC_3_0;
        case LICENSE_CC_BY_NC_4_0:
            return LICENSE_CC_BY_NC_4_0;
        case LICENSE_CC_BY_NC_ND_1_0:
            return LICENSE_CC_BY_NC_ND_1_0;
        case LICENSE_CC_BY_NC_ND_2_0:
            return LICENSE_CC_BY_NC_ND_2_0;
        case LICENSE_CC_BY_NC_ND_2_5:
            return LICENSE_CC_BY_NC_ND_2_5;
        case LICENSE_CC_BY_NC_ND_3_0:
            return LICENSE_CC_BY_NC_ND_3_0;
        case LICENSE_CC_BY_NC_ND_4_0:
            return LICENSE_CC_BY_NC_ND_4_0;
        case LICENSE_CC_BY_NC_SA_1_0:
            return LICENSE_CC_BY_NC_SA_1_0;
        case LICENSE_CC_BY_NC_SA_2_0:
            return LICENSE_CC_BY_NC_SA_2_0;
        case LICENSE_CC_BY_NC_SA_2_5:
            return LICENSE_CC_BY_NC_SA_2_5;
        case LICENSE_CC_BY_NC_SA_3_0:
            return LICENSE_CC_BY_NC_SA_3_0;
        case LICENSE_CC_BY_NC_SA_4_0:
            return LICENSE_CC_BY_NC_SA_4_0;
        case LICENSE_CC_BY_ND_1_0:
            return LICENSE_CC_BY_ND_1_0;
        case LICENSE_CC_BY_ND_2_0:
            return LICENSE_CC_BY_ND_2_0;
        case LICENSE_CC_BY_ND_2_5:
            return LICENSE_CC_BY_ND_2_5;
        case LICENSE_CC_BY_ND_3_0:
            return LICENSE_CC_BY_ND_3_0;
        case LICENSE_CC_BY_ND_4_0:
            return LICENSE_CC_BY_ND_4_0;
        case LICENSE_CC_BY_SA_1_0:
            return LICENSE_CC_BY_SA_1_0;
        case LICENSE_CC_BY_SA_2_0:
            return LICENSE_CC_BY_SA_2_0;
        case LICENSE_CC_BY_SA_2_5:
            return LICENSE_CC_BY_SA_2_5;
        case LICENSE_CC_BY_SA_3_0:
            return LICENSE_CC_BY_SA_3_0;
        case LICENSE_CC_BY_SA_4_0:
            return LICENSE_CC_BY_SA_4_0;
        case LICENSE_CC0_1_0:
            return LICENSE_CC0_1_0;
        case LICENSE_CDDL_1_0:
            return LICENSE_CDDL_1_0;
        case LICENSE_CDDL_1_1:
            return LICENSE_CDDL_1_1;
        case LICENSE_CDLA_PERMISSIVE_1_0:
            return LICENSE_CDLA_PERMISSIVE_1_0;
        case LICENSE_CDLA_SHARING_1_0:
            return LICENSE_CDLA_SHARING_1_0;
        case LICENSE_CECILL_1_0:
            return LICENSE_CECILL_1_0;
        case LICENSE_CECILL_1_1:
            return LICENSE_CECILL_1_1;
        case LICENSE_CECILL_2_0:
            return LICENSE_CECILL_2_0;
        case LICENSE_CECILL_2_1:
            return LICENSE_CECILL_2_1;
        case LICENSE_CECILL_B:
            return LICENSE_CECILL_B;
        case LICENSE_CECILL_C:
            return LICENSE_CECILL_C;
        case LICENSE_CL_ARTISTIC:
            return LICENSE_CL_ARTISTIC;
        case LICENSE_CNRI_JYTHON:
            return LICENSE_CNRI_JYTHON;
        case LICENSE_CNRI_PYTHON_GPL_COMPATIBLE:
            return LICENSE_CNRI_PYTHON_GPL_COMPATIBLE;
        case LICENSE_CNRI_PYTHON:
            return LICENSE_CNRI_PYTHON;
        case LICENSE_CONDOR_1_1:
            return LICENSE_CONDOR_1_1;
        case LICENSE_CPAL_1_0:
            return LICENSE_CPAL_1_0;
        case LICENSE_CPL_1_0:
            return LICENSE_CPL_1_0;
        case LICENSE_CPOL_1_02:
            return LICENSE_CPOL_1_02;
        case LICENSE_CROSSWORD:
            return LICENSE_CROSSWORD;
        case LICENSE_CRYSTAL_STACKER:
            return LICENSE_CRYSTAL_STACKER;
        case LICENSE_CUA_OPL_1_0:
            return LICENSE_CUA_OPL_1_0;
        case LICENSE_CUBE:
            return LICENSE_CUBE;
        case LICENSE_CURL:
            return LICENSE_CURL;
        case LICENSE_D_FSL_1_0:
            return LICENSE_D_FSL_1_0;
        case LICENSE_DIFFMARK:
            return LICENSE_DIFFMARK;
        case LICENSE_DOC:
            return LICENSE_DOC;
        case LICENSE_DOTSEQN:
            return LICENSE_DOTSEQN;
        case LICENSE_DSDP:
            return LICENSE_DSDP;
        case LICENSE_DVIPDFM:
            return LICENSE_DVIPDFM;
        case LICENSE_ECL_1_0:
            return LICENSE_ECL_1_0;
        case LICENSE_ECL_2_0:
            return LICENSE_ECL_2_0;
        case LICENSE_EFL_1_0:
            return LICENSE_EFL_1_0;
        case LICENSE_EFL_2_0:
            return LICENSE_EFL_2_0;
        case LICENSE_E_GENIX:
            return LICENSE_E_GENIX;
        case LICENSE_ENTESSA:
            return LICENSE_ENTESSA;
        case LICENSE_EPL_1_0:
            return LICENSE_EPL_1_0;
        case LICENSE_EPL_2_0:
            return LICENSE_EPL_2_0;
        case LICENSE_ERL_PL_1_1:
            return LICENSE_ERL_PL_1_1;
        case LICENSE_EUDATAGRID:
            return LICENSE_EUDATAGRID;
        case LICENSE_EUPL_1_0:
            return LICENSE_EUPL_1_0;
        case LICENSE_EUPL_1_1:
            return LICENSE_EUPL_1_1;
        case LICENSE_EUPL_1_2:
            return LICENSE_EUPL_1_2;
        case LICENSE_EUROSYM:
            return LICENSE_EUROSYM;
        case LICENSE_FAIR:
            return LICENSE_FAIR;
        case LICENSE_FRAMEWORX_1_0:
            return LICENSE_FRAMEWORX_1_0;
        case LICENSE_FREE_IMAGE:
            return LICENSE_FREE_IMAGE;
        case LICENSE_FSFAP:
            return LICENSE_FSFAP;
        case LICENSE_FSFUL:
            return LICENSE_FSFUL;
        case LICENSE_FSFULLR:
            return LICENSE_FSFULLR;
        case LICENSE_FTL:
            return LICENSE_FTL;
        case LICENSE_GFDL_1_1_ONLY:
            return LICENSE_GFDL_1_1_ONLY;
        case LICENSE_GFDL_1_1_OR_LATER:
            return LICENSE_GFDL_1_1_OR_LATER;
        case LICENSE_GFDL_1_2_ONLY:
            return LICENSE_GFDL_1_2_ONLY;
        case LICENSE_GFDL_1_2_OR_LATER:
            return LICENSE_GFDL_1_2_OR_LATER;
        case LICENSE_GFDL_1_3_ONLY:
            return LICENSE_GFDL_1_3_ONLY;
        case LICENSE_GFDL_1_3_OR_LATER:
            return LICENSE_GFDL_1_3_OR_LATER;
        case LICENSE_GIFTWARE:
            return LICENSE_GIFTWARE;
        case LICENSE_GL2PS:
            return LICENSE_GL2PS;
        case LICENSE_GLIDE:
            return LICENSE_GLIDE;
        case LICENSE_GLULXE:
            return LICENSE_GLULXE;
        case LICENSE_GNUPLOT:
            return LICENSE_GNUPLOT;
        case LICENSE_GPL_1_0_ONLY:
            return LICENSE_GPL_1_0_ONLY;
        case LICENSE_GPL_1_0_OR_LATER:
            return LICENSE_GPL_1_0_OR_LATER;
        case LICENSE_GPL_2_0_ONLY:
            return LICENSE_GPL_2_0_ONLY;
        case LICENSE_GPL_2_0_OR_LATER:
            return LICENSE_GPL_2_0_OR_LATER;
        case LICENSE_GPL_3_0_ONLY:
            return LICENSE_GPL_3_0_ONLY;
        case LICENSE_GPL_3_0_OR_LATER:
            return LICENSE_GPL_3_0_OR_LATER;
        case LICENSE_G_SOAP_1_3B:
            return LICENSE_G_SOAP_1_3B;
        case LICENSE_HASKELL_REPORT:
            return LICENSE_HASKELL_REPORT;
        case LICENSE_HPND:
            return LICENSE_HPND;
        case LICENSE_IBM_PIBS:
            return LICENSE_IBM_PIBS;
        case LICENSE_ICU:
            return LICENSE_ICU;
        case LICENSE_IJG:
            return LICENSE_IJG;
        case LICENSE_IMAGE_MAGICK:
            return LICENSE_IMAGE_MAGICK;
        case LICENSE_I_MATIX:
            return LICENSE_I_MATIX;
        case LICENSE_IMLIB2:
            return LICENSE_IMLIB2;
        case LICENSE_INFO_ZIP:
            return LICENSE_INFO_ZIP;
        case LICENSE_INTEL_ACPI:
            return LICENSE_INTEL_ACPI;
        case LICENSE_INTEL:
            return LICENSE_INTEL;
        case LICENSE_INTERBASE_1_0:
            return LICENSE_INTERBASE_1_0;
        case LICENSE_IPA:
            return LICENSE_IPA;
        case LICENSE_IPL_1_0:
            return LICENSE_IPL_1_0;
        case LICENSE_ISC:
            return LICENSE_ISC;
        case LICENSE_JAS_PER_2_0:
            return LICENSE_JAS_PER_2_0;
        case LICENSE_JSON:
            return LICENSE_JSON;
        case LICENSE_LAL_1_2:
            return LICENSE_LAL_1_2;
        case LICENSE_LAL_1_3:
            return LICENSE_LAL_1_3;
        case LICENSE_LATEX2E:
            return LICENSE_LATEX2E;
        case LICENSE_LEPTONICA:
            return LICENSE_LEPTONICA;
        case LICENSE_LGPL_2_0_ONLY:
            return LICENSE_LGPL_2_0_ONLY;
        case LICENSE_LGPL_2_0_OR_LATER:
            return LICENSE_LGPL_2_0_OR_LATER;
        case LICENSE_LGPL_2_1_ONLY:
            return LICENSE_LGPL_2_1_ONLY;
        case LICENSE_LGPL_2_1_OR_LATER:
            return LICENSE_LGPL_2_1_OR_LATER;
        case LICENSE_LGPL_3_0_ONLY:
            return LICENSE_LGPL_3_0_ONLY;
        case LICENSE_LGPL_3_0_OR_LATER:
            return LICENSE_LGPL_3_0_OR_LATER;
        case LICENSE_LGPLLR:
            return LICENSE_LGPLLR;
        case LICENSE_LIBPNG:
            return LICENSE_LIBPNG;
        case LICENSE_LIBTIFF:
            return LICENSE_LIBTIFF;
        case LICENSE_LI_LI_Q_P_1_1:
            return LICENSE_LI_LI_Q_P_1_1;
        case LICENSE_LI_LI_Q_R_1_1:
            return LICENSE_LI_LI_Q_R_1_1;
        case LICENSE_LI_LI_Q_RPLUS_1_1:
            return LICENSE_LI_LI_Q_RPLUS_1_1;
        case LICENSE_LINUX_OPEN_IB:
            return LICENSE_LINUX_OPEN_IB;
        case LICENSE_LPL_1_0:
            return LICENSE_LPL_1_0;
        case LICENSE_LPL_1_02:
            return LICENSE_LPL_1_02;
        case LICENSE_LPPL_1_0:
            return LICENSE_LPPL_1_0;
        case LICENSE_LPPL_1_1:
            return LICENSE_LPPL_1_1;
        case LICENSE_LPPL_1_2:
            return LICENSE_LPPL_1_2;
        case LICENSE_LPPL_1_3A:
            return LICENSE_LPPL_1_3A;
        case LICENSE_LPPL_1_3C:
            return LICENSE_LPPL_1_3C;
        case LICENSE_MAKE_INDEX:
            return LICENSE_MAKE_INDEX;
        case LICENSE_MIR_OS:
            return LICENSE_MIR_OS;
        case LICENSE_MIT_0:
            return LICENSE_MIT_0;
        case LICENSE_MIT_ADVERTISING:
            return LICENSE_MIT_ADVERTISING;
        case LICENSE_MIT_CMU:
            return LICENSE_MIT_CMU;
        case LICENSE_MIT_ENNA:
            return LICENSE_MIT_ENNA;
        case LICENSE_MIT_FEH:
            return LICENSE_MIT_FEH;
        case LICENSE_MIT:
            return LICENSE_MIT;
        case LICENSE_MITNFA:
            return LICENSE_MITNFA;
        case LICENSE_MOTOSOTO:
            return LICENSE_MOTOSOTO;
        case LICENSE_MPICH2:
            return LICENSE_MPICH2;
        case LICENSE_MPL_1_0:
            return LICENSE_MPL_1_0;
        case LICENSE_MPL_1_1:
            return LICENSE_MPL_1_1;
        case LICENSE_MPL_2_0_NO_COPYLEFT_EXCEPTION:
            return LICENSE_MPL_2_0_NO_COPYLEFT_EXCEPTION;
        case LICENSE_MPL_2_0:
            return LICENSE_MPL_2_0;
        case LICENSE_MS_PL:
            return LICENSE_MS_PL;
        case LICENSE_MS_RL:
            return LICENSE_MS_RL;
        case LICENSE_MTLL:
            return LICENSE_MTLL;
        case LICENSE_MULTICS:
            return LICENSE_MULTICS;
        case LICENSE_MUP:
            return LICENSE_MUP;
        case LICENSE_NASA_1_3:
            return LICENSE_NASA_1_3;
        case LICENSE_NAUMEN:
            return LICENSE_NAUMEN;
        case LICENSE_NBPL_1_0:
            return LICENSE_NBPL_1_0;
        case LICENSE_NCSA:
            return LICENSE_NCSA;
        case LICENSE_NET_SNMP:
            return LICENSE_NET_SNMP;
        case LICENSE_NET_CDF:
            return LICENSE_NET_CDF;
        case LICENSE_NEWSLETR:
            return LICENSE_NEWSLETR;
        case LICENSE_NGPL:
            return LICENSE_NGPL;
        case LICENSE_NLOD_1_0:
            return LICENSE_NLOD_1_0;
        case LICENSE_NLPL:
            return LICENSE_NLPL;
        case LICENSE_NOKIA:
            return LICENSE_NOKIA;
        case LICENSE_NOSL:
            return LICENSE_NOSL;
        case LICENSE_NOWEB:
            return LICENSE_NOWEB;
        case LICENSE_NPL_1_0:
            return LICENSE_NPL_1_0;
        case LICENSE_NPL_1_1:
            return LICENSE_NPL_1_1;
        case LICENSE_NPOSL_3_0:
            return LICENSE_NPOSL_3_0;
        case LICENSE_NRL:
            return LICENSE_NRL;
        case LICENSE_NTP:
            return LICENSE_NTP;
        case LICENSE_OCCT_PL:
            return LICENSE_OCCT_PL;
        case LICENSE_OCLC_2_0:
            return LICENSE_OCLC_2_0;
        case LICENSE_ODB_L_1_0:
            return LICENSE_ODB_L_1_0;
        case LICENSE_OFL_1_0:
            return LICENSE_OFL_1_0;
        case LICENSE_OFL_1_1:
            return LICENSE_OFL_1_1;
        case LICENSE_OGTSL:
            return LICENSE_OGTSL;
        case LICENSE_OLDAP_1_1:
            return LICENSE_OLDAP_1_1;
        case LICENSE_OLDAP_1_2:
            return LICENSE_OLDAP_1_2;
        case LICENSE_OLDAP_1_3:
            return LICENSE_OLDAP_1_3;
        case LICENSE_OLDAP_1_4:
            return LICENSE_OLDAP_1_4;
        case LICENSE_OLDAP_2_0_1:
            return LICENSE_OLDAP_2_0_1;
        case LICENSE_OLDAP_2_0:
            return LICENSE_OLDAP_2_0;
        case LICENSE_OLDAP_2_1:
            return LICENSE_OLDAP_2_1;
        case LICENSE_OLDAP_2_2_1:
            return LICENSE_OLDAP_2_2_1;
        case LICENSE_OLDAP_2_2_2:
            return LICENSE_OLDAP_2_2_2;
        case LICENSE_OLDAP_2_2:
            return LICENSE_OLDAP_2_2;
        case LICENSE_OLDAP_2_3:
            return LICENSE_OLDAP_2_3;
        case LICENSE_OLDAP_2_4:
            return LICENSE_OLDAP_2_4;
        case LICENSE_OLDAP_2_5:
            return LICENSE_OLDAP_2_5;
        case LICENSE_OLDAP_2_6:
            return LICENSE_OLDAP_2_6;
        case LICENSE_OLDAP_2_7:
            return LICENSE_OLDAP_2_7;
        case LICENSE_OLDAP_2_8:
            return LICENSE_OLDAP_2_8;
        case LICENSE_OML:
            return LICENSE_OML;
        case LICENSE_OPEN_SSL:
            return LICENSE_OPEN_SSL;
        case LICENSE_OPL_1_0:
            return LICENSE_OPL_1_0;
        case LICENSE_OSET_PL_2_1:
            return LICENSE_OSET_PL_2_1;
        case LICENSE_OSL_1_0:
            return LICENSE_OSL_1_0;
        case LICENSE_OSL_1_1:
            return LICENSE_OSL_1_1;
        case LICENSE_OSL_2_0:
            return LICENSE_OSL_2_0;
        case LICENSE_OSL_2_1:
            return LICENSE_OSL_2_1;
        case LICENSE_OSL_3_0:
            return LICENSE_OSL_3_0;
        case LICENSE_PDDL_1_0:
            return LICENSE_PDDL_1_0;
        case LICENSE_PHP_3_0:
            return LICENSE_PHP_3_0;
        case LICENSE_PHP_3_01:
            return LICENSE_PHP_3_01;
        case LICENSE_PLEXUS:
            return LICENSE_PLEXUS;
        case LICENSE_POSTGRE_SQL:
            return LICENSE_POSTGRE_SQL;
        case LICENSE_PSFRAG:
            return LICENSE_PSFRAG;
        case LICENSE_PSUTILS:
            return LICENSE_PSUTILS;
        case LICENSE_PYTHON_2_0:
            return LICENSE_PYTHON_2_0;
        case LICENSE_QHULL:
            return LICENSE_QHULL;
        case LICENSE_QPL_1_0:
            return LICENSE_QPL_1_0;
        case LICENSE_RDISC:
            return LICENSE_RDISC;
        case LICENSE_RHE_COS_1_1:
            return LICENSE_RHE_COS_1_1;
        case LICENSE_RPL_1_1:
            return LICENSE_RPL_1_1;
        case LICENSE_RPL_1_5:
            return LICENSE_RPL_1_5;
        case LICENSE_RPSL_1_0:
            return LICENSE_RPSL_1_0;
        case LICENSE_RSA_MD:
            return LICENSE_RSA_MD;
        case LICENSE_RSCPL:
            return LICENSE_RSCPL;
        case LICENSE_RUBY:
            return LICENSE_RUBY;
        case LICENSE_SAX_PD:
            return LICENSE_SAX_PD;
        case LICENSE_SAXPATH:
            return LICENSE_SAXPATH;
        case LICENSE_SCEA:
            return LICENSE_SCEA;
        case LICENSE_SENDMAIL:
            return LICENSE_SENDMAIL;
        case LICENSE_SGI_B_1_0:
            return LICENSE_SGI_B_1_0;
        case LICENSE_SGI_B_1_1:
            return LICENSE_SGI_B_1_1;
        case LICENSE_SGI_B_2_0:
            return LICENSE_SGI_B_2_0;
        case LICENSE_SIM_PL_2_0:
            return LICENSE_SIM_PL_2_0;
        case LICENSE_SISSL_1_2:
            return LICENSE_SISSL_1_2;
        case LICENSE_SISSL:
            return LICENSE_SISSL;
        case LICENSE_SLEEPYCAT:
            return LICENSE_SLEEPYCAT;
        case LICENSE_SMLNJ:
            return LICENSE_SMLNJ;
        case LICENSE_SMPPL:
            return LICENSE_SMPPL;
        case LICENSE_SNIA:
            return LICENSE_SNIA;
        case LICENSE_SPENCER_86:
            return LICENSE_SPENCER_86;
        case LICENSE_SPENCER_94:
            return LICENSE_SPENCER_94;
        case LICENSE_SPENCER_99:
            return LICENSE_SPENCER_99;
        case LICENSE_SPL_1_0:
            return LICENSE_SPL_1_0;
        case LICENSE_SUGAR_CRM_1_1_3:
            return LICENSE_SUGAR_CRM_1_1_3;
        case LICENSE_SWL:
            return LICENSE_SWL;
        case LICENSE_TCL:
            return LICENSE_TCL;
        case LICENSE_TCP_WRAPPERS:
            return LICENSE_TCP_WRAPPERS;
        case LICENSE_TMATE:
            return LICENSE_TMATE;
        case LICENSE_TORQUE_1_1:
            return LICENSE_TORQUE_1_1;
        case LICENSE_TOSL:
            return LICENSE_TOSL;
        case LICENSE_UNICODE_DFS_2015:
            return LICENSE_UNICODE_DFS_2015;
        case LICENSE_UNICODE_DFS_2016:
            return LICENSE_UNICODE_DFS_2016;
        case LICENSE_UNICODE_TOU:
            return LICENSE_UNICODE_TOU;
        case LICENSE_UNLICENSE:
            return LICENSE_UNLICENSE;
        case LICENSE_UPL_1_0:
            return LICENSE_UPL_1_0;
        case LICENSE_VIM:
            return LICENSE_VIM;
        case LICENSE_VOSTROM:
            return LICENSE_VOSTROM;
        case LICENSE_VSL_1_0:
            return LICENSE_VSL_1_0;
        case LICENSE_W3C_19980720:
            return LICENSE_W3C_19980720;
        case LICENSE_W3C_20150513:
            return LICENSE_W3C_20150513;
        case LICENSE_W3C:
            return LICENSE_W3C;
        case LICENSE_WATCOM_1_0:
            return LICENSE_WATCOM_1_0;
        case LICENSE_WSUIPA:
            return LICENSE_WSUIPA;
        case LICENSE_WTFPL:
            return LICENSE_WTFPL;
        case LICENSE_X11:
            return LICENSE_X11;
        case LICENSE_XEROX:
            return LICENSE_XEROX;
        case LICENSE_XFREE86_1_1:
            return LICENSE_XFREE86_1_1;
        case LICENSE_XINETD:
            return LICENSE_XINETD;
        case LICENSE_XNET:
            return LICENSE_XNET;
        case LICENSE_XPP:
            return LICENSE_XPP;
        case LICENSE_XSKAT:
            return LICENSE_XSKAT;
        case LICENSE_YPL_1_0:
            return LICENSE_YPL_1_0;
        case LICENSE_YPL_1_1:
            return LICENSE_YPL_1_1;
        case LICENSE_ZED:
            return LICENSE_ZED;
        case LICENSE_ZEND_2_0:
            return LICENSE_ZEND_2_0;
        case LICENSE_ZIMBRA_1_3:
            return LICENSE_ZIMBRA_1_3;
        case LICENSE_ZIMBRA_1_4:
            return LICENSE_ZIMBRA_1_4;
        case LICENSE_ZLIB_ACKNOWLEDGEMENT:
            return LICENSE_ZLIB_ACKNOWLEDGEMENT;
        case LICENSE_ZLIB:
            return LICENSE_ZLIB;
        case LICENSE_ZPL_1_1:
            return LICENSE_ZPL_1_1;
        case LICENSE_ZPL_2_0:
            return LICENSE_ZPL_2_0;
        case LICENSE_ZPL_2_1:
            return LICENSE_ZPL_2_1;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SPDXLicense objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SPDXLicense of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SPDXLicense objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SPDXLicense objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(Value.from(value));
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
        return new Builder().from(this);
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
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for SPDXLicense
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SPDXLicense build() {
            SPDXLicense sPDXLicense = new SPDXLicense(this);
            if (validating) {
                validate(sPDXLicense);
            }
            return sPDXLicense;
        }

        protected void validate(SPDXLicense sPDXLicense) {
            super.validate(sPDXLicense);
        }

        protected Builder from(SPDXLicense sPDXLicense) {
            super.from(sPDXLicense);
            return this;
        }
    }

    public enum Value {
        /**
         * Not open source
         * 
         * <p>Not an open source license.
         */
        LICENSE_NOT_OPEN_SOURCE("not-open-source"),

        /**
         * BSD Zero Clause License
         * 
         * <p>BSD Zero Clause License.
         */
        LICENSE_0BSD("0BSD"),

        /**
         * Attribution Assurance License
         * 
         * <p>Attribution Assurance License.
         */
        LICENSE_AAL("AAL"),

        /**
         * Abstyles License
         * 
         * <p>Abstyles License.
         */
        LICENSE_ABSTYLES("Abstyles"),

        /**
         * Adobe Systems Incorporated Source Code License Agreement
         * 
         * <p>Adobe Systems Incorporated Source Code License Agreement.
         */
        LICENSE_ADOBE_2006("Adobe-2006"),

        /**
         * Adobe Glyph List License
         * 
         * <p>Adobe Glyph List License.
         */
        LICENSE_ADOBE_GLYPH("Adobe-Glyph"),

        /**
         * Amazon Digital Services License
         * 
         * <p>Amazon Digital Services License.
         */
        LICENSE_ADSL("ADSL"),

        /**
         * Academic Free License v1.1
         * 
         * <p>Academic Free License v1.1.
         */
        LICENSE_AFL_1_1("AFL-1.1"),

        /**
         * Academic Free License v1.2
         * 
         * <p>Academic Free License v1.2.
         */
        LICENSE_AFL_1_2("AFL-1.2"),

        /**
         * Academic Free License v2.0
         * 
         * <p>Academic Free License v2.0.
         */
        LICENSE_AFL_2_0("AFL-2.0"),

        /**
         * Academic Free License v2.1
         * 
         * <p>Academic Free License v2.1.
         */
        LICENSE_AFL_2_1("AFL-2.1"),

        /**
         * Academic Free License v3.0
         * 
         * <p>Academic Free License v3.0.
         */
        LICENSE_AFL_3_0("AFL-3.0"),

        /**
         * Afmparse License
         * 
         * <p>Afmparse License.
         */
        LICENSE_AFMPARSE("Afmparse"),

        /**
         * Affero General Public License v1.0 only
         * 
         * <p>Affero General Public License v1.0 only.
         */
        LICENSE_AGPL_1_0_ONLY("AGPL-1.0-only"),

        /**
         * Affero General Public License v1.0 or later
         * 
         * <p>Affero General Public License v1.0 or later.
         */
        LICENSE_AGPL_1_0_OR_LATER("AGPL-1.0-or-later"),

        /**
         * GNU Affero General Public License v3.0 only
         * 
         * <p>GNU Affero General Public License v3.0 only.
         */
        LICENSE_AGPL_3_0_ONLY("AGPL-3.0-only"),

        /**
         * GNU Affero General Public License v3.0 or later
         * 
         * <p>GNU Affero General Public License v3.0 or later.
         */
        LICENSE_AGPL_3_0_OR_LATER("AGPL-3.0-or-later"),

        /**
         * Aladdin Free Public License
         * 
         * <p>Aladdin Free Public License.
         */
        LICENSE_ALADDIN("Aladdin"),

        /**
         * AMD's plpa_map.c License
         * 
         * <p>AMD's plpa_map.c License.
         */
        LICENSE_AMDPLPA("AMDPLPA"),

        /**
         * Apple MIT License
         * 
         * <p>Apple MIT License.
         */
        LICENSE_AML("AML"),

        /**
         * Academy of Motion Picture Arts and Sciences BSD
         * 
         * <p>Academy of Motion Picture Arts and Sciences BSD.
         */
        LICENSE_AMPAS("AMPAS"),

        /**
         * ANTLR Software Rights Notice
         * 
         * <p>ANTLR Software Rights Notice.
         */
        LICENSE_ANTLR_PD("ANTLR-PD"),

        /**
         * Apache License 1.0
         * 
         * <p>Apache License 1.0.
         */
        LICENSE_APACHE_1_0("Apache-1.0"),

        /**
         * Apache License 1.1
         * 
         * <p>Apache License 1.1.
         */
        LICENSE_APACHE_1_1("Apache-1.1"),

        /**
         * Apache License 2.0
         * 
         * <p>Apache License 2.0.
         */
        LICENSE_APACHE_2_0("Apache-2.0"),

        /**
         * Adobe Postscript AFM License
         * 
         * <p>Adobe Postscript AFM License.
         */
        LICENSE_APAFML("APAFML"),

        /**
         * Adaptive Public License 1.0
         * 
         * <p>Adaptive Public License 1.0.
         */
        LICENSE_APL_1_0("APL-1.0"),

        /**
         * Apple Public Source License 1.0
         * 
         * <p>Apple Public Source License 1.0.
         */
        LICENSE_APSL_1_0("APSL-1.0"),

        /**
         * Apple Public Source License 1.1
         * 
         * <p>Apple Public Source License 1.1.
         */
        LICENSE_APSL_1_1("APSL-1.1"),

        /**
         * Apple Public Source License 1.2
         * 
         * <p>Apple Public Source License 1.2.
         */
        LICENSE_APSL_1_2("APSL-1.2"),

        /**
         * Apple Public Source License 2.0
         * 
         * <p>Apple Public Source License 2.0.
         */
        LICENSE_APSL_2_0("APSL-2.0"),

        /**
         * Artistic License 1.0 w/clause 8
         * 
         * <p>Artistic License 1.0 w/clause 8.
         */
        LICENSE_ARTISTIC_1_0_CL8("Artistic-1.0-cl8"),

        /**
         * Artistic License 1.0 (Perl)
         * 
         * <p>Artistic License 1.0 (Perl).
         */
        LICENSE_ARTISTIC_1_0_PERL("Artistic-1.0-Perl"),

        /**
         * Artistic License 1.0
         * 
         * <p>Artistic License 1.0.
         */
        LICENSE_ARTISTIC_1_0("Artistic-1.0"),

        /**
         * Artistic License 2.0
         * 
         * <p>Artistic License 2.0.
         */
        LICENSE_ARTISTIC_2_0("Artistic-2.0"),

        /**
         * Bahyph License
         * 
         * <p>Bahyph License.
         */
        LICENSE_BAHYPH("Bahyph"),

        /**
         * Barr License
         * 
         * <p>Barr License.
         */
        LICENSE_BARR("Barr"),

        /**
         * Beerware License
         * 
         * <p>Beerware License.
         */
        LICENSE_BEERWARE("Beerware"),

        /**
         * BitTorrent Open Source License v1.0
         * 
         * <p>BitTorrent Open Source License v1.0.
         */
        LICENSE_BIT_TORRENT_1_0("BitTorrent-1.0"),

        /**
         * BitTorrent Open Source License v1.1
         * 
         * <p>BitTorrent Open Source License v1.1.
         */
        LICENSE_BIT_TORRENT_1_1("BitTorrent-1.1"),

        /**
         * Borceux license
         * 
         * <p>Borceux license.
         */
        LICENSE_BORCEUX("Borceux"),

        /**
         * BSD 1-Clause License
         * 
         * <p>BSD 1-Clause License.
         */
        LICENSE_BSD_1_CLAUSE("BSD-1-Clause"),

        /**
         * BSD 2-Clause FreeBSD License
         * 
         * <p>BSD 2-Clause FreeBSD License.
         */
        LICENSE_BSD_2_CLAUSE_FREE_BSD("BSD-2-Clause-FreeBSD"),

        /**
         * BSD 2-Clause NetBSD License
         * 
         * <p>BSD 2-Clause NetBSD License.
         */
        LICENSE_BSD_2_CLAUSE_NET_BSD("BSD-2-Clause-NetBSD"),

        /**
         * BSD-2-Clause Plus Patent License
         * 
         * <p>BSD-2-Clause Plus Patent License.
         */
        LICENSE_BSD_2_CLAUSE_PATENT("BSD-2-Clause-Patent"),

        /**
         * BSD 2-Clause "Simplified" License
         * 
         * <p>BSD 2-Clause "Simplified" License.
         */
        LICENSE_BSD_2_CLAUSE("BSD-2-Clause"),

        /**
         * BSD with attribution
         * 
         * <p>BSD with attribution.
         */
        LICENSE_BSD_3_CLAUSE_ATTRIBUTION("BSD-3-Clause-Attribution"),

        /**
         * BSD 3-Clause Clear License
         * 
         * <p>BSD 3-Clause Clear License.
         */
        LICENSE_BSD_3_CLAUSE_CLEAR("BSD-3-Clause-Clear"),

        /**
         * Lawrence Berkeley National Labs BSD variant license
         * 
         * <p>Lawrence Berkeley National Labs BSD variant license.
         */
        LICENSE_BSD_3_CLAUSE_LBNL("BSD-3-Clause-LBNL"),

        /**
         * BSD 3-Clause No Nuclear License 2014
         * 
         * <p>BSD 3-Clause No Nuclear License 2014.
         */
        LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE_2014("BSD-3-Clause-No-Nuclear-License-2014"),

        /**
         * BSD 3-Clause No Nuclear License
         * 
         * <p>BSD 3-Clause No Nuclear License.
         */
        LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE("BSD-3-Clause-No-Nuclear-License"),

        /**
         * BSD 3-Clause No Nuclear Warranty
         * 
         * <p>BSD 3-Clause No Nuclear Warranty.
         */
        LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_WARRANTY("BSD-3-Clause-No-Nuclear-Warranty"),

        /**
         * BSD 3-Clause "New" or "Revised" License
         * 
         * <p>BSD 3-Clause "New" or "Revised" License.
         */
        LICENSE_BSD_3_CLAUSE("BSD-3-Clause"),

        /**
         * BSD-4-Clause (University of California-Specific)
         * 
         * <p>BSD-4-Clause (University of California-Specific).
         */
        LICENSE_BSD_4_CLAUSE_UC("BSD-4-Clause-UC"),

        /**
         * BSD 4-Clause "Original" or "Old" License
         * 
         * <p>BSD 4-Clause "Original" or "Old" License.
         */
        LICENSE_BSD_4_CLAUSE("BSD-4-Clause"),

        /**
         * BSD Protection License
         * 
         * <p>BSD Protection License.
         */
        LICENSE_BSD_PROTECTION("BSD-Protection"),

        /**
         * BSD Source Code Attribution
         * 
         * <p>BSD Source Code Attribution.
         */
        LICENSE_BSD_SOURCE_CODE("BSD-Source-Code"),

        /**
         * Boost Software License 1.0
         * 
         * <p>Boost Software License 1.0.
         */
        LICENSE_BSL_1_0("BSL-1.0"),

        /**
         * bzip2 and libbzip2 License v1.0.5
         * 
         * <p>bzip2 and libbzip2 License v1.0.5.
         */
        LICENSE_BZIP2_1_0_5("bzip2-1.0.5"),

        /**
         * bzip2 and libbzip2 License v1.0.6
         * 
         * <p>bzip2 and libbzip2 License v1.0.6.
         */
        LICENSE_BZIP2_1_0_6("bzip2-1.0.6"),

        /**
         * Caldera License
         * 
         * <p>Caldera License.
         */
        LICENSE_CALDERA("Caldera"),

        /**
         * Computer Associates Trusted Open Source License 1.1
         * 
         * <p>Computer Associates Trusted Open Source License 1.1.
         */
        LICENSE_CATOSL_1_1("CATOSL-1.1"),

        /**
         * Creative Commons Attribution 1.0 Generic
         * 
         * <p>Creative Commons Attribution 1.0 Generic.
         */
        LICENSE_CC_BY_1_0("CC-BY-1.0"),

        /**
         * Creative Commons Attribution 2.0 Generic
         * 
         * <p>Creative Commons Attribution 2.0 Generic.
         */
        LICENSE_CC_BY_2_0("CC-BY-2.0"),

        /**
         * Creative Commons Attribution 2.5 Generic
         * 
         * <p>Creative Commons Attribution 2.5 Generic.
         */
        LICENSE_CC_BY_2_5("CC-BY-2.5"),

        /**
         * Creative Commons Attribution 3.0 Unported
         * 
         * <p>Creative Commons Attribution 3.0 Unported.
         */
        LICENSE_CC_BY_3_0("CC-BY-3.0"),

        /**
         * Creative Commons Attribution 4.0 International
         * 
         * <p>Creative Commons Attribution 4.0 International.
         */
        LICENSE_CC_BY_4_0("CC-BY-4.0"),

        /**
         * Creative Commons Attribution Non Commercial 1.0 Generic
         * 
         * <p>Creative Commons Attribution Non Commercial 1.0 Generic.
         */
        LICENSE_CC_BY_NC_1_0("CC-BY-NC-1.0"),

        /**
         * Creative Commons Attribution Non Commercial 2.0 Generic
         * 
         * <p>Creative Commons Attribution Non Commercial 2.0 Generic.
         */
        LICENSE_CC_BY_NC_2_0("CC-BY-NC-2.0"),

        /**
         * Creative Commons Attribution Non Commercial 2.5 Generic
         * 
         * <p>Creative Commons Attribution Non Commercial 2.5 Generic.
         */
        LICENSE_CC_BY_NC_2_5("CC-BY-NC-2.5"),

        /**
         * Creative Commons Attribution Non Commercial 3.0 Unported
         * 
         * <p>Creative Commons Attribution Non Commercial 3.0 Unported.
         */
        LICENSE_CC_BY_NC_3_0("CC-BY-NC-3.0"),

        /**
         * Creative Commons Attribution Non Commercial 4.0 International
         * 
         * <p>Creative Commons Attribution Non Commercial 4.0 International.
         */
        LICENSE_CC_BY_NC_4_0("CC-BY-NC-4.0"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 1.0 Generic
         * 
         * <p>Creative Commons Attribution Non Commercial No Derivatives 1.0 Generic.
         */
        LICENSE_CC_BY_NC_ND_1_0("CC-BY-NC-ND-1.0"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 2.0 Generic
         * 
         * <p>Creative Commons Attribution Non Commercial No Derivatives 2.0 Generic.
         */
        LICENSE_CC_BY_NC_ND_2_0("CC-BY-NC-ND-2.0"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 2.5 Generic
         * 
         * <p>Creative Commons Attribution Non Commercial No Derivatives 2.5 Generic.
         */
        LICENSE_CC_BY_NC_ND_2_5("CC-BY-NC-ND-2.5"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 3.0 Unported
         * 
         * <p>Creative Commons Attribution Non Commercial No Derivatives 3.0 Unported.
         */
        LICENSE_CC_BY_NC_ND_3_0("CC-BY-NC-ND-3.0"),

        /**
         * Creative Commons Attribution Non Commercial No Derivatives 4.0 International
         * 
         * <p>Creative Commons Attribution Non Commercial No Derivatives 4.0 International.
         */
        LICENSE_CC_BY_NC_ND_4_0("CC-BY-NC-ND-4.0"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 1.0 Generic
         * 
         * <p>Creative Commons Attribution Non Commercial Share Alike 1.0 Generic.
         */
        LICENSE_CC_BY_NC_SA_1_0("CC-BY-NC-SA-1.0"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 2.0 Generic
         * 
         * <p>Creative Commons Attribution Non Commercial Share Alike 2.0 Generic.
         */
        LICENSE_CC_BY_NC_SA_2_0("CC-BY-NC-SA-2.0"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 2.5 Generic
         * 
         * <p>Creative Commons Attribution Non Commercial Share Alike 2.5 Generic.
         */
        LICENSE_CC_BY_NC_SA_2_5("CC-BY-NC-SA-2.5"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 3.0 Unported
         * 
         * <p>Creative Commons Attribution Non Commercial Share Alike 3.0 Unported.
         */
        LICENSE_CC_BY_NC_SA_3_0("CC-BY-NC-SA-3.0"),

        /**
         * Creative Commons Attribution Non Commercial Share Alike 4.0 International
         * 
         * <p>Creative Commons Attribution Non Commercial Share Alike 4.0 International.
         */
        LICENSE_CC_BY_NC_SA_4_0("CC-BY-NC-SA-4.0"),

        /**
         * Creative Commons Attribution No Derivatives 1.0 Generic
         * 
         * <p>Creative Commons Attribution No Derivatives 1.0 Generic.
         */
        LICENSE_CC_BY_ND_1_0("CC-BY-ND-1.0"),

        /**
         * Creative Commons Attribution No Derivatives 2.0 Generic
         * 
         * <p>Creative Commons Attribution No Derivatives 2.0 Generic.
         */
        LICENSE_CC_BY_ND_2_0("CC-BY-ND-2.0"),

        /**
         * Creative Commons Attribution No Derivatives 2.5 Generic
         * 
         * <p>Creative Commons Attribution No Derivatives 2.5 Generic.
         */
        LICENSE_CC_BY_ND_2_5("CC-BY-ND-2.5"),

        /**
         * Creative Commons Attribution No Derivatives 3.0 Unported
         * 
         * <p>Creative Commons Attribution No Derivatives 3.0 Unported.
         */
        LICENSE_CC_BY_ND_3_0("CC-BY-ND-3.0"),

        /**
         * Creative Commons Attribution No Derivatives 4.0 International
         * 
         * <p>Creative Commons Attribution No Derivatives 4.0 International.
         */
        LICENSE_CC_BY_ND_4_0("CC-BY-ND-4.0"),

        /**
         * Creative Commons Attribution Share Alike 1.0 Generic
         * 
         * <p>Creative Commons Attribution Share Alike 1.0 Generic.
         */
        LICENSE_CC_BY_SA_1_0("CC-BY-SA-1.0"),

        /**
         * Creative Commons Attribution Share Alike 2.0 Generic
         * 
         * <p>Creative Commons Attribution Share Alike 2.0 Generic.
         */
        LICENSE_CC_BY_SA_2_0("CC-BY-SA-2.0"),

        /**
         * Creative Commons Attribution Share Alike 2.5 Generic
         * 
         * <p>Creative Commons Attribution Share Alike 2.5 Generic.
         */
        LICENSE_CC_BY_SA_2_5("CC-BY-SA-2.5"),

        /**
         * Creative Commons Attribution Share Alike 3.0 Unported
         * 
         * <p>Creative Commons Attribution Share Alike 3.0 Unported.
         */
        LICENSE_CC_BY_SA_3_0("CC-BY-SA-3.0"),

        /**
         * Creative Commons Attribution Share Alike 4.0 International
         * 
         * <p>Creative Commons Attribution Share Alike 4.0 International.
         */
        LICENSE_CC_BY_SA_4_0("CC-BY-SA-4.0"),

        /**
         * Creative Commons Zero v1.0 Universal
         * 
         * <p>Creative Commons Zero v1.0 Universal.
         */
        LICENSE_CC0_1_0("CC0-1.0"),

        /**
         * Common Development and Distribution License 1.0
         * 
         * <p>Common Development and Distribution License 1.0.
         */
        LICENSE_CDDL_1_0("CDDL-1.0"),

        /**
         * Common Development and Distribution License 1.1
         * 
         * <p>Common Development and Distribution License 1.1.
         */
        LICENSE_CDDL_1_1("CDDL-1.1"),

        /**
         * Community Data License Agreement Permissive 1.0
         * 
         * <p>Community Data License Agreement Permissive 1.0.
         */
        LICENSE_CDLA_PERMISSIVE_1_0("CDLA-Permissive-1.0"),

        /**
         * Community Data License Agreement Sharing 1.0
         * 
         * <p>Community Data License Agreement Sharing 1.0.
         */
        LICENSE_CDLA_SHARING_1_0("CDLA-Sharing-1.0"),

        /**
         * CeCILL Free Software License Agreement v1.0
         * 
         * <p>CeCILL Free Software License Agreement v1.0.
         */
        LICENSE_CECILL_1_0("CECILL-1.0"),

        /**
         * CeCILL Free Software License Agreement v1.1
         * 
         * <p>CeCILL Free Software License Agreement v1.1.
         */
        LICENSE_CECILL_1_1("CECILL-1.1"),

        /**
         * CeCILL Free Software License Agreement v2.0
         * 
         * <p>CeCILL Free Software License Agreement v2.0.
         */
        LICENSE_CECILL_2_0("CECILL-2.0"),

        /**
         * CeCILL Free Software License Agreement v2.1
         * 
         * <p>CeCILL Free Software License Agreement v2.1.
         */
        LICENSE_CECILL_2_1("CECILL-2.1"),

        /**
         * CeCILL-B Free Software License Agreement
         * 
         * <p>CeCILL-B Free Software License Agreement.
         */
        LICENSE_CECILL_B("CECILL-B"),

        /**
         * CeCILL-C Free Software License Agreement
         * 
         * <p>CeCILL-C Free Software License Agreement.
         */
        LICENSE_CECILL_C("CECILL-C"),

        /**
         * Clarified Artistic License
         * 
         * <p>Clarified Artistic License.
         */
        LICENSE_CL_ARTISTIC("ClArtistic"),

        /**
         * CNRI Jython License
         * 
         * <p>CNRI Jython License.
         */
        LICENSE_CNRI_JYTHON("CNRI-Jython"),

        /**
         * CNRI Python Open Source GPL Compatible License Agreement
         * 
         * <p>CNRI Python Open Source GPL Compatible License Agreement.
         */
        LICENSE_CNRI_PYTHON_GPL_COMPATIBLE("CNRI-Python-GPL-Compatible"),

        /**
         * CNRI Python License
         * 
         * <p>CNRI Python License.
         */
        LICENSE_CNRI_PYTHON("CNRI-Python"),

        /**
         * Condor Public License v1.1
         * 
         * <p>Condor Public License v1.1.
         */
        LICENSE_CONDOR_1_1("Condor-1.1"),

        /**
         * Common Public Attribution License 1.0
         * 
         * <p>Common Public Attribution License 1.0.
         */
        LICENSE_CPAL_1_0("CPAL-1.0"),

        /**
         * Common Public License 1.0
         * 
         * <p>Common Public License 1.0.
         */
        LICENSE_CPL_1_0("CPL-1.0"),

        /**
         * Code Project Open License 1.02
         * 
         * <p>Code Project Open License 1.02.
         */
        LICENSE_CPOL_1_02("CPOL-1.02"),

        /**
         * Crossword License
         * 
         * <p>Crossword License.
         */
        LICENSE_CROSSWORD("Crossword"),

        /**
         * CrystalStacker License
         * 
         * <p>CrystalStacker License.
         */
        LICENSE_CRYSTAL_STACKER("CrystalStacker"),

        /**
         * CUA Office Public License v1.0
         * 
         * <p>CUA Office Public License v1.0.
         */
        LICENSE_CUA_OPL_1_0("CUA-OPL-1.0"),

        /**
         * Cube License
         * 
         * <p>Cube License.
         */
        LICENSE_CUBE("Cube"),

        /**
         * curl License
         * 
         * <p>curl License.
         */
        LICENSE_CURL("curl"),

        /**
         * Deutsche Freie Software Lizenz
         * 
         * <p>Deutsche Freie Software Lizenz.
         */
        LICENSE_D_FSL_1_0("D-FSL-1.0"),

        /**
         * diffmark license
         * 
         * <p>diffmark license.
         */
        LICENSE_DIFFMARK("diffmark"),

        /**
         * DOC License
         * 
         * <p>DOC License.
         */
        LICENSE_DOC("DOC"),

        /**
         * Dotseqn License
         * 
         * <p>Dotseqn License.
         */
        LICENSE_DOTSEQN("Dotseqn"),

        /**
         * DSDP License
         * 
         * <p>DSDP License.
         */
        LICENSE_DSDP("DSDP"),

        /**
         * dvipdfm License
         * 
         * <p>dvipdfm License.
         */
        LICENSE_DVIPDFM("dvipdfm"),

        /**
         * Educational Community License v1.0
         * 
         * <p>Educational Community License v1.0.
         */
        LICENSE_ECL_1_0("ECL-1.0"),

        /**
         * Educational Community License v2.0
         * 
         * <p>Educational Community License v2.0.
         */
        LICENSE_ECL_2_0("ECL-2.0"),

        /**
         * Eiffel Forum License v1.0
         * 
         * <p>Eiffel Forum License v1.0.
         */
        LICENSE_EFL_1_0("EFL-1.0"),

        /**
         * Eiffel Forum License v2.0
         * 
         * <p>Eiffel Forum License v2.0.
         */
        LICENSE_EFL_2_0("EFL-2.0"),

        /**
         * eGenix.com Public License 1.1.0
         * 
         * <p>eGenix.com Public License 1.1.0.
         */
        LICENSE_E_GENIX("eGenix"),

        /**
         * Entessa Public License v1.0
         * 
         * <p>Entessa Public License v1.0.
         */
        LICENSE_ENTESSA("Entessa"),

        /**
         * Eclipse Public License 1.0
         * 
         * <p>Eclipse Public License 1.0.
         */
        LICENSE_EPL_1_0("EPL-1.0"),

        /**
         * Eclipse Public License 2.0
         * 
         * <p>Eclipse Public License 2.0.
         */
        LICENSE_EPL_2_0("EPL-2.0"),

        /**
         * Erlang Public License v1.1
         * 
         * <p>Erlang Public License v1.1.
         */
        LICENSE_ERL_PL_1_1("ErlPL-1.1"),

        /**
         * EU DataGrid Software License
         * 
         * <p>EU DataGrid Software License.
         */
        LICENSE_EUDATAGRID("EUDatagrid"),

        /**
         * European Union Public License 1.0
         * 
         * <p>European Union Public License 1.0.
         */
        LICENSE_EUPL_1_0("EUPL-1.0"),

        /**
         * European Union Public License 1.1
         * 
         * <p>European Union Public License 1.1.
         */
        LICENSE_EUPL_1_1("EUPL-1.1"),

        /**
         * European Union Public License 1.2
         * 
         * <p>European Union Public License 1.2.
         */
        LICENSE_EUPL_1_2("EUPL-1.2"),

        /**
         * Eurosym License
         * 
         * <p>Eurosym License.
         */
        LICENSE_EUROSYM("Eurosym"),

        /**
         * Fair License
         * 
         * <p>Fair License.
         */
        LICENSE_FAIR("Fair"),

        /**
         * Frameworx Open License 1.0
         * 
         * <p>Frameworx Open License 1.0.
         */
        LICENSE_FRAMEWORX_1_0("Frameworx-1.0"),

        /**
         * FreeImage Public License v1.0
         * 
         * <p>FreeImage Public License v1.0.
         */
        LICENSE_FREE_IMAGE("FreeImage"),

        /**
         * FSF All Permissive License
         * 
         * <p>FSF All Permissive License.
         */
        LICENSE_FSFAP("FSFAP"),

        /**
         * FSF Unlimited License
         * 
         * <p>FSF Unlimited License.
         */
        LICENSE_FSFUL("FSFUL"),

        /**
         * FSF Unlimited License (with License Retention)
         * 
         * <p>FSF Unlimited License (with License Retention).
         */
        LICENSE_FSFULLR("FSFULLR"),

        /**
         * Freetype Project License
         * 
         * <p>Freetype Project License.
         */
        LICENSE_FTL("FTL"),

        /**
         * GNU Free Documentation License v1.1 only
         * 
         * <p>GNU Free Documentation License v1.1 only.
         */
        LICENSE_GFDL_1_1_ONLY("GFDL-1.1-only"),

        /**
         * GNU Free Documentation License v1.1 or later
         * 
         * <p>GNU Free Documentation License v1.1 or later.
         */
        LICENSE_GFDL_1_1_OR_LATER("GFDL-1.1-or-later"),

        /**
         * GNU Free Documentation License v1.2 only
         * 
         * <p>GNU Free Documentation License v1.2 only.
         */
        LICENSE_GFDL_1_2_ONLY("GFDL-1.2-only"),

        /**
         * GNU Free Documentation License v1.2 or later
         * 
         * <p>GNU Free Documentation License v1.2 or later.
         */
        LICENSE_GFDL_1_2_OR_LATER("GFDL-1.2-or-later"),

        /**
         * GNU Free Documentation License v1.3 only
         * 
         * <p>GNU Free Documentation License v1.3 only.
         */
        LICENSE_GFDL_1_3_ONLY("GFDL-1.3-only"),

        /**
         * GNU Free Documentation License v1.3 or later
         * 
         * <p>GNU Free Documentation License v1.3 or later.
         */
        LICENSE_GFDL_1_3_OR_LATER("GFDL-1.3-or-later"),

        /**
         * Giftware License
         * 
         * <p>Giftware License.
         */
        LICENSE_GIFTWARE("Giftware"),

        /**
         * GL2PS License
         * 
         * <p>GL2PS License.
         */
        LICENSE_GL2PS("GL2PS"),

        /**
         * 3dfx Glide License
         * 
         * <p>3dfx Glide License.
         */
        LICENSE_GLIDE("Glide"),

        /**
         * Glulxe License
         * 
         * <p>Glulxe License.
         */
        LICENSE_GLULXE("Glulxe"),

        /**
         * gnuplot License
         * 
         * <p>gnuplot License.
         */
        LICENSE_GNUPLOT("gnuplot"),

        /**
         * GNU General Public License v1.0 only
         * 
         * <p>GNU General Public License v1.0 only.
         */
        LICENSE_GPL_1_0_ONLY("GPL-1.0-only"),

        /**
         * GNU General Public License v1.0 or later
         * 
         * <p>GNU General Public License v1.0 or later.
         */
        LICENSE_GPL_1_0_OR_LATER("GPL-1.0-or-later"),

        /**
         * GNU General Public License v2.0 only
         * 
         * <p>GNU General Public License v2.0 only.
         */
        LICENSE_GPL_2_0_ONLY("GPL-2.0-only"),

        /**
         * GNU General Public License v2.0 or later
         * 
         * <p>GNU General Public License v2.0 or later.
         */
        LICENSE_GPL_2_0_OR_LATER("GPL-2.0-or-later"),

        /**
         * GNU General Public License v3.0 only
         * 
         * <p>GNU General Public License v3.0 only.
         */
        LICENSE_GPL_3_0_ONLY("GPL-3.0-only"),

        /**
         * GNU General Public License v3.0 or later
         * 
         * <p>GNU General Public License v3.0 or later.
         */
        LICENSE_GPL_3_0_OR_LATER("GPL-3.0-or-later"),

        /**
         * gSOAP Public License v1.3b
         * 
         * <p>gSOAP Public License v1.3b.
         */
        LICENSE_G_SOAP_1_3B("gSOAP-1.3b"),

        /**
         * Haskell Language Report License
         * 
         * <p>Haskell Language Report License.
         */
        LICENSE_HASKELL_REPORT("HaskellReport"),

        /**
         * Historical Permission Notice and Disclaimer
         * 
         * <p>Historical Permission Notice and Disclaimer.
         */
        LICENSE_HPND("HPND"),

        /**
         * IBM PowerPC Initialization and Boot Software
         * 
         * <p>IBM PowerPC Initialization and Boot Software.
         */
        LICENSE_IBM_PIBS("IBM-pibs"),

        /**
         * ICU License
         * 
         * <p>ICU License.
         */
        LICENSE_ICU("ICU"),

        /**
         * Independent JPEG Group License
         * 
         * <p>Independent JPEG Group License.
         */
        LICENSE_IJG("IJG"),

        /**
         * ImageMagick License
         * 
         * <p>ImageMagick License.
         */
        LICENSE_IMAGE_MAGICK("ImageMagick"),

        /**
         * iMatix Standard Function Library Agreement
         * 
         * <p>iMatix Standard Function Library Agreement.
         */
        LICENSE_I_MATIX("iMatix"),

        /**
         * Imlib2 License
         * 
         * <p>Imlib2 License.
         */
        LICENSE_IMLIB2("Imlib2"),

        /**
         * Info-ZIP License
         * 
         * <p>Info-ZIP License.
         */
        LICENSE_INFO_ZIP("Info-ZIP"),

        /**
         * Intel ACPI Software License Agreement
         * 
         * <p>Intel ACPI Software License Agreement.
         */
        LICENSE_INTEL_ACPI("Intel-ACPI"),

        /**
         * Intel Open Source License
         * 
         * <p>Intel Open Source License.
         */
        LICENSE_INTEL("Intel"),

        /**
         * Interbase Public License v1.0
         * 
         * <p>Interbase Public License v1.0.
         */
        LICENSE_INTERBASE_1_0("Interbase-1.0"),

        /**
         * IPA Font License
         * 
         * <p>IPA Font License.
         */
        LICENSE_IPA("IPA"),

        /**
         * IBM Public License v1.0
         * 
         * <p>IBM Public License v1.0.
         */
        LICENSE_IPL_1_0("IPL-1.0"),

        /**
         * ISC License
         * 
         * <p>ISC License.
         */
        LICENSE_ISC("ISC"),

        /**
         * JasPer License
         * 
         * <p>JasPer License.
         */
        LICENSE_JAS_PER_2_0("JasPer-2.0"),

        /**
         * JSON License
         * 
         * <p>JSON License.
         */
        LICENSE_JSON("JSON"),

        /**
         * Licence Art Libre 1.2
         * 
         * <p>Licence Art Libre 1.2.
         */
        LICENSE_LAL_1_2("LAL-1.2"),

        /**
         * Licence Art Libre 1.3
         * 
         * <p>Licence Art Libre 1.3.
         */
        LICENSE_LAL_1_3("LAL-1.3"),

        /**
         * Latex2e License
         * 
         * <p>Latex2e License.
         */
        LICENSE_LATEX2E("Latex2e"),

        /**
         * Leptonica License
         * 
         * <p>Leptonica License.
         */
        LICENSE_LEPTONICA("Leptonica"),

        /**
         * GNU Library General Public License v2 only
         * 
         * <p>GNU Library General Public License v2 only.
         */
        LICENSE_LGPL_2_0_ONLY("LGPL-2.0-only"),

        /**
         * GNU Library General Public License v2 or later
         * 
         * <p>GNU Library General Public License v2 or later.
         */
        LICENSE_LGPL_2_0_OR_LATER("LGPL-2.0-or-later"),

        /**
         * GNU Lesser General Public License v2.1 only
         * 
         * <p>GNU Lesser General Public License v2.1 only.
         */
        LICENSE_LGPL_2_1_ONLY("LGPL-2.1-only"),

        /**
         * GNU Lesser General Public License v2.1 or later
         * 
         * <p>GNU Lesser General Public License v2.1 or later.
         */
        LICENSE_LGPL_2_1_OR_LATER("LGPL-2.1-or-later"),

        /**
         * GNU Lesser General Public License v3.0 only
         * 
         * <p>GNU Lesser General Public License v3.0 only.
         */
        LICENSE_LGPL_3_0_ONLY("LGPL-3.0-only"),

        /**
         * GNU Lesser General Public License v3.0 or later
         * 
         * <p>GNU Lesser General Public License v3.0 or later.
         */
        LICENSE_LGPL_3_0_OR_LATER("LGPL-3.0-or-later"),

        /**
         * Lesser General Public License For Linguistic Resources
         * 
         * <p>Lesser General Public License For Linguistic Resources.
         */
        LICENSE_LGPLLR("LGPLLR"),

        /**
         * libpng License
         * 
         * <p>libpng License.
         */
        LICENSE_LIBPNG("Libpng"),

        /**
         * libtiff License
         * 
         * <p>libtiff License.
         */
        LICENSE_LIBTIFF("libtiff"),

        /**
         * Licence Libre du Québec – Permissive version 1.1
         * 
         * <p>Licence Libre du Québec – Permissive version 1.1.
         */
        LICENSE_LI_LI_Q_P_1_1("LiLiQ-P-1.1"),

        /**
         * Licence Libre du Québec – Réciprocité version 1.1
         * 
         * <p>Licence Libre du Québec – Réciprocité version 1.1.
         */
        LICENSE_LI_LI_Q_R_1_1("LiLiQ-R-1.1"),

        /**
         * Licence Libre du Québec – Réciprocité forte version 1.1
         * 
         * <p>Licence Libre du Québec – Réciprocité forte version 1.1.
         */
        LICENSE_LI_LI_Q_RPLUS_1_1("LiLiQ-Rplus-1.1"),

        /**
         * Linux Kernel Variant of OpenIB.org license
         * 
         * <p>Linux Kernel Variant of OpenIB.org license.
         */
        LICENSE_LINUX_OPEN_IB("Linux-OpenIB"),

        /**
         * Lucent Public License Version 1.0
         * 
         * <p>Lucent Public License Version 1.0.
         */
        LICENSE_LPL_1_0("LPL-1.0"),

        /**
         * Lucent Public License v1.02
         * 
         * <p>Lucent Public License v1.02.
         */
        LICENSE_LPL_1_02("LPL-1.02"),

        /**
         * LaTeX Project Public License v1.0
         * 
         * <p>LaTeX Project Public License v1.0.
         */
        LICENSE_LPPL_1_0("LPPL-1.0"),

        /**
         * LaTeX Project Public License v1.1
         * 
         * <p>LaTeX Project Public License v1.1.
         */
        LICENSE_LPPL_1_1("LPPL-1.1"),

        /**
         * LaTeX Project Public License v1.2
         * 
         * <p>LaTeX Project Public License v1.2.
         */
        LICENSE_LPPL_1_2("LPPL-1.2"),

        /**
         * LaTeX Project Public License v1.3a
         * 
         * <p>LaTeX Project Public License v1.3a.
         */
        LICENSE_LPPL_1_3A("LPPL-1.3a"),

        /**
         * LaTeX Project Public License v1.3c
         * 
         * <p>LaTeX Project Public License v1.3c.
         */
        LICENSE_LPPL_1_3C("LPPL-1.3c"),

        /**
         * MakeIndex License
         * 
         * <p>MakeIndex License.
         */
        LICENSE_MAKE_INDEX("MakeIndex"),

        /**
         * MirOS License
         * 
         * <p>MirOS License.
         */
        LICENSE_MIR_OS("MirOS"),

        /**
         * MIT No Attribution
         * 
         * <p>MIT No Attribution.
         */
        LICENSE_MIT_0("MIT-0"),

        /**
         * Enlightenment License (e16)
         * 
         * <p>Enlightenment License (e16).
         */
        LICENSE_MIT_ADVERTISING("MIT-advertising"),

        /**
         * CMU License
         * 
         * <p>CMU License.
         */
        LICENSE_MIT_CMU("MIT-CMU"),

        /**
         * enna License
         * 
         * <p>enna License.
         */
        LICENSE_MIT_ENNA("MIT-enna"),

        /**
         * feh License
         * 
         * <p>feh License.
         */
        LICENSE_MIT_FEH("MIT-feh"),

        /**
         * MIT License
         * 
         * <p>MIT License.
         */
        LICENSE_MIT("MIT"),

        /**
         * MIT +no-false-attribs license
         * 
         * <p>MIT +no-false-attribs license.
         */
        LICENSE_MITNFA("MITNFA"),

        /**
         * Motosoto License
         * 
         * <p>Motosoto License.
         */
        LICENSE_MOTOSOTO("Motosoto"),

        /**
         * mpich2 License
         * 
         * <p>mpich2 License.
         */
        LICENSE_MPICH2("mpich2"),

        /**
         * Mozilla Public License 1.0
         * 
         * <p>Mozilla Public License 1.0.
         */
        LICENSE_MPL_1_0("MPL-1.0"),

        /**
         * Mozilla Public License 1.1
         * 
         * <p>Mozilla Public License 1.1.
         */
        LICENSE_MPL_1_1("MPL-1.1"),

        /**
         * Mozilla Public License 2.0 (no copyleft exception)
         * 
         * <p>Mozilla Public License 2.0 (no copyleft exception).
         */
        LICENSE_MPL_2_0_NO_COPYLEFT_EXCEPTION("MPL-2.0-no-copyleft-exception"),

        /**
         * Mozilla Public License 2.0
         * 
         * <p>Mozilla Public License 2.0.
         */
        LICENSE_MPL_2_0("MPL-2.0"),

        /**
         * Microsoft Public License
         * 
         * <p>Microsoft Public License.
         */
        LICENSE_MS_PL("MS-PL"),

        /**
         * Microsoft Reciprocal License
         * 
         * <p>Microsoft Reciprocal License.
         */
        LICENSE_MS_RL("MS-RL"),

        /**
         * Matrix Template Library License
         * 
         * <p>Matrix Template Library License.
         */
        LICENSE_MTLL("MTLL"),

        /**
         * Multics License
         * 
         * <p>Multics License.
         */
        LICENSE_MULTICS("Multics"),

        /**
         * Mup License
         * 
         * <p>Mup License.
         */
        LICENSE_MUP("Mup"),

        /**
         * NASA Open Source Agreement 1.3
         * 
         * <p>NASA Open Source Agreement 1.3.
         */
        LICENSE_NASA_1_3("NASA-1.3"),

        /**
         * Naumen Public License
         * 
         * <p>Naumen Public License.
         */
        LICENSE_NAUMEN("Naumen"),

        /**
         * Net Boolean Public License v1
         * 
         * <p>Net Boolean Public License v1.
         */
        LICENSE_NBPL_1_0("NBPL-1.0"),

        /**
         * University of Illinois/NCSA Open Source License
         * 
         * <p>University of Illinois/NCSA Open Source License.
         */
        LICENSE_NCSA("NCSA"),

        /**
         * Net-SNMP License
         * 
         * <p>Net-SNMP License.
         */
        LICENSE_NET_SNMP("Net-SNMP"),

        /**
         * NetCDF license
         * 
         * <p>NetCDF license.
         */
        LICENSE_NET_CDF("NetCDF"),

        /**
         * Newsletr License
         * 
         * <p>Newsletr License.
         */
        LICENSE_NEWSLETR("Newsletr"),

        /**
         * Nethack General Public License
         * 
         * <p>Nethack General Public License.
         */
        LICENSE_NGPL("NGPL"),

        /**
         * Norwegian Licence for Open Government Data
         * 
         * <p>Norwegian Licence for Open Government Data.
         */
        LICENSE_NLOD_1_0("NLOD-1.0"),

        /**
         * No Limit Public License
         * 
         * <p>No Limit Public License.
         */
        LICENSE_NLPL("NLPL"),

        /**
         * Nokia Open Source License
         * 
         * <p>Nokia Open Source License.
         */
        LICENSE_NOKIA("Nokia"),

        /**
         * Netizen Open Source License
         * 
         * <p>Netizen Open Source License.
         */
        LICENSE_NOSL("NOSL"),

        /**
         * Noweb License
         * 
         * <p>Noweb License.
         */
        LICENSE_NOWEB("Noweb"),

        /**
         * Netscape Public License v1.0
         * 
         * <p>Netscape Public License v1.0.
         */
        LICENSE_NPL_1_0("NPL-1.0"),

        /**
         * Netscape Public License v1.1
         * 
         * <p>Netscape Public License v1.1.
         */
        LICENSE_NPL_1_1("NPL-1.1"),

        /**
         * Non-Profit Open Software License 3.0
         * 
         * <p>Non-Profit Open Software License 3.0.
         */
        LICENSE_NPOSL_3_0("NPOSL-3.0"),

        /**
         * NRL License
         * 
         * <p>NRL License.
         */
        LICENSE_NRL("NRL"),

        /**
         * NTP License
         * 
         * <p>NTP License.
         */
        LICENSE_NTP("NTP"),

        /**
         * Open CASCADE Technology Public License
         * 
         * <p>Open CASCADE Technology Public License.
         */
        LICENSE_OCCT_PL("OCCT-PL"),

        /**
         * OCLC Research Public License 2.0
         * 
         * <p>OCLC Research Public License 2.0.
         */
        LICENSE_OCLC_2_0("OCLC-2.0"),

        /**
         * ODC Open Database License v1.0
         * 
         * <p>ODC Open Database License v1.0.
         */
        LICENSE_ODB_L_1_0("ODbL-1.0"),

        /**
         * SIL Open Font License 1.0
         * 
         * <p>SIL Open Font License 1.0.
         */
        LICENSE_OFL_1_0("OFL-1.0"),

        /**
         * SIL Open Font License 1.1
         * 
         * <p>SIL Open Font License 1.1.
         */
        LICENSE_OFL_1_1("OFL-1.1"),

        /**
         * Open Group Test Suite License
         * 
         * <p>Open Group Test Suite License.
         */
        LICENSE_OGTSL("OGTSL"),

        /**
         * Open LDAP Public License v1.1
         * 
         * <p>Open LDAP Public License v1.1.
         */
        LICENSE_OLDAP_1_1("OLDAP-1.1"),

        /**
         * Open LDAP Public License v1.2
         * 
         * <p>Open LDAP Public License v1.2.
         */
        LICENSE_OLDAP_1_2("OLDAP-1.2"),

        /**
         * Open LDAP Public License v1.3
         * 
         * <p>Open LDAP Public License v1.3.
         */
        LICENSE_OLDAP_1_3("OLDAP-1.3"),

        /**
         * Open LDAP Public License v1.4
         * 
         * <p>Open LDAP Public License v1.4.
         */
        LICENSE_OLDAP_1_4("OLDAP-1.4"),

        /**
         * Open LDAP Public License v2.0.1
         * 
         * <p>Open LDAP Public License v2.0.1.
         */
        LICENSE_OLDAP_2_0_1("OLDAP-2.0.1"),

        /**
         * Open LDAP Public License v2.0 (or possibly 2.0A and 2.0B)
         * 
         * <p>Open LDAP Public License v2.0 (or possibly 2.0A and 2.0B).
         */
        LICENSE_OLDAP_2_0("OLDAP-2.0"),

        /**
         * Open LDAP Public License v2.1
         * 
         * <p>Open LDAP Public License v2.1.
         */
        LICENSE_OLDAP_2_1("OLDAP-2.1"),

        /**
         * Open LDAP Public License v2.2.1
         * 
         * <p>Open LDAP Public License v2.2.1.
         */
        LICENSE_OLDAP_2_2_1("OLDAP-2.2.1"),

        /**
         * Open LDAP Public License 2.2.2
         * 
         * <p>Open LDAP Public License 2.2.2.
         */
        LICENSE_OLDAP_2_2_2("OLDAP-2.2.2"),

        /**
         * Open LDAP Public License v2.2
         * 
         * <p>Open LDAP Public License v2.2.
         */
        LICENSE_OLDAP_2_2("OLDAP-2.2"),

        /**
         * Open LDAP Public License v2.3
         * 
         * <p>Open LDAP Public License v2.3.
         */
        LICENSE_OLDAP_2_3("OLDAP-2.3"),

        /**
         * Open LDAP Public License v2.4
         * 
         * <p>Open LDAP Public License v2.4.
         */
        LICENSE_OLDAP_2_4("OLDAP-2.4"),

        /**
         * Open LDAP Public License v2.5
         * 
         * <p>Open LDAP Public License v2.5.
         */
        LICENSE_OLDAP_2_5("OLDAP-2.5"),

        /**
         * Open LDAP Public License v2.6
         * 
         * <p>Open LDAP Public License v2.6.
         */
        LICENSE_OLDAP_2_6("OLDAP-2.6"),

        /**
         * Open LDAP Public License v2.7
         * 
         * <p>Open LDAP Public License v2.7.
         */
        LICENSE_OLDAP_2_7("OLDAP-2.7"),

        /**
         * Open LDAP Public License v2.8
         * 
         * <p>Open LDAP Public License v2.8.
         */
        LICENSE_OLDAP_2_8("OLDAP-2.8"),

        /**
         * Open Market License
         * 
         * <p>Open Market License.
         */
        LICENSE_OML("OML"),

        /**
         * OpenSSL License
         * 
         * <p>OpenSSL License.
         */
        LICENSE_OPEN_SSL("OpenSSL"),

        /**
         * Open Public License v1.0
         * 
         * <p>Open Public License v1.0.
         */
        LICENSE_OPL_1_0("OPL-1.0"),

        /**
         * OSET Public License version 2.1
         * 
         * <p>OSET Public License version 2.1.
         */
        LICENSE_OSET_PL_2_1("OSET-PL-2.1"),

        /**
         * Open Software License 1.0
         * 
         * <p>Open Software License 1.0.
         */
        LICENSE_OSL_1_0("OSL-1.0"),

        /**
         * Open Software License 1.1
         * 
         * <p>Open Software License 1.1.
         */
        LICENSE_OSL_1_1("OSL-1.1"),

        /**
         * Open Software License 2.0
         * 
         * <p>Open Software License 2.0.
         */
        LICENSE_OSL_2_0("OSL-2.0"),

        /**
         * Open Software License 2.1
         * 
         * <p>Open Software License 2.1.
         */
        LICENSE_OSL_2_1("OSL-2.1"),

        /**
         * Open Software License 3.0
         * 
         * <p>Open Software License 3.0.
         */
        LICENSE_OSL_3_0("OSL-3.0"),

        /**
         * ODC Public Domain Dedication &amp; License 1.0
         * 
         * <p>ODC Public Domain Dedication &amp; License 1.0.
         */
        LICENSE_PDDL_1_0("PDDL-1.0"),

        /**
         * PHP License v3.0
         * 
         * <p>PHP License v3.0.
         */
        LICENSE_PHP_3_0("PHP-3.0"),

        /**
         * PHP License v3.01
         * 
         * <p>PHP License v3.01.
         */
        LICENSE_PHP_3_01("PHP-3.01"),

        /**
         * Plexus Classworlds License
         * 
         * <p>Plexus Classworlds License.
         */
        LICENSE_PLEXUS("Plexus"),

        /**
         * PostgreSQL License
         * 
         * <p>PostgreSQL License.
         */
        LICENSE_POSTGRE_SQL("PostgreSQL"),

        /**
         * psfrag License
         * 
         * <p>psfrag License.
         */
        LICENSE_PSFRAG("psfrag"),

        /**
         * psutils License
         * 
         * <p>psutils License.
         */
        LICENSE_PSUTILS("psutils"),

        /**
         * Python License 2.0
         * 
         * <p>Python License 2.0.
         */
        LICENSE_PYTHON_2_0("Python-2.0"),

        /**
         * Qhull License
         * 
         * <p>Qhull License.
         */
        LICENSE_QHULL("Qhull"),

        /**
         * Q Public License 1.0
         * 
         * <p>Q Public License 1.0.
         */
        LICENSE_QPL_1_0("QPL-1.0"),

        /**
         * Rdisc License
         * 
         * <p>Rdisc License.
         */
        LICENSE_RDISC("Rdisc"),

        /**
         * Red Hat eCos Public License v1.1
         * 
         * <p>Red Hat eCos Public License v1.1.
         */
        LICENSE_RHE_COS_1_1("RHeCos-1.1"),

        /**
         * Reciprocal Public License 1.1
         * 
         * <p>Reciprocal Public License 1.1.
         */
        LICENSE_RPL_1_1("RPL-1.1"),

        /**
         * Reciprocal Public License 1.5
         * 
         * <p>Reciprocal Public License 1.5.
         */
        LICENSE_RPL_1_5("RPL-1.5"),

        /**
         * RealNetworks Public Source License v1.0
         * 
         * <p>RealNetworks Public Source License v1.0.
         */
        LICENSE_RPSL_1_0("RPSL-1.0"),

        /**
         * RSA Message-Digest License
         * 
         * <p>RSA Message-Digest License.
         */
        LICENSE_RSA_MD("RSA-MD"),

        /**
         * Ricoh Source Code Public License
         * 
         * <p>Ricoh Source Code Public License.
         */
        LICENSE_RSCPL("RSCPL"),

        /**
         * Ruby License
         * 
         * <p>Ruby License.
         */
        LICENSE_RUBY("Ruby"),

        /**
         * Sax Public Domain Notice
         * 
         * <p>Sax Public Domain Notice.
         */
        LICENSE_SAX_PD("SAX-PD"),

        /**
         * Saxpath License
         * 
         * <p>Saxpath License.
         */
        LICENSE_SAXPATH("Saxpath"),

        /**
         * SCEA Shared Source License
         * 
         * <p>SCEA Shared Source License.
         */
        LICENSE_SCEA("SCEA"),

        /**
         * Sendmail License
         * 
         * <p>Sendmail License.
         */
        LICENSE_SENDMAIL("Sendmail"),

        /**
         * SGI Free Software License B v1.0
         * 
         * <p>SGI Free Software License B v1.0.
         */
        LICENSE_SGI_B_1_0("SGI-B-1.0"),

        /**
         * SGI Free Software License B v1.1
         * 
         * <p>SGI Free Software License B v1.1.
         */
        LICENSE_SGI_B_1_1("SGI-B-1.1"),

        /**
         * SGI Free Software License B v2.0
         * 
         * <p>SGI Free Software License B v2.0.
         */
        LICENSE_SGI_B_2_0("SGI-B-2.0"),

        /**
         * Simple Public License 2.0
         * 
         * <p>Simple Public License 2.0.
         */
        LICENSE_SIM_PL_2_0("SimPL-2.0"),

        /**
         * Sun Industry Standards Source License v1.2
         * 
         * <p>Sun Industry Standards Source License v1.2.
         */
        LICENSE_SISSL_1_2("SISSL-1.2"),

        /**
         * Sun Industry Standards Source License v1.1
         * 
         * <p>Sun Industry Standards Source License v1.1.
         */
        LICENSE_SISSL("SISSL"),

        /**
         * Sleepycat License
         * 
         * <p>Sleepycat License.
         */
        LICENSE_SLEEPYCAT("Sleepycat"),

        /**
         * Standard ML of New Jersey License
         * 
         * <p>Standard ML of New Jersey License.
         */
        LICENSE_SMLNJ("SMLNJ"),

        /**
         * Secure Messaging Protocol Public License
         * 
         * <p>Secure Messaging Protocol Public License.
         */
        LICENSE_SMPPL("SMPPL"),

        /**
         * SNIA Public License 1.1
         * 
         * <p>SNIA Public License 1.1.
         */
        LICENSE_SNIA("SNIA"),

        /**
         * Spencer License 86
         * 
         * <p>Spencer License 86.
         */
        LICENSE_SPENCER_86("Spencer-86"),

        /**
         * Spencer License 94
         * 
         * <p>Spencer License 94.
         */
        LICENSE_SPENCER_94("Spencer-94"),

        /**
         * Spencer License 99
         * 
         * <p>Spencer License 99.
         */
        LICENSE_SPENCER_99("Spencer-99"),

        /**
         * Sun Public License v1.0
         * 
         * <p>Sun Public License v1.0.
         */
        LICENSE_SPL_1_0("SPL-1.0"),

        /**
         * SugarCRM Public License v1.1.3
         * 
         * <p>SugarCRM Public License v1.1.3.
         */
        LICENSE_SUGAR_CRM_1_1_3("SugarCRM-1.1.3"),

        /**
         * Scheme Widget Library (SWL) Software License Agreement
         * 
         * <p>Scheme Widget Library (SWL) Software License Agreement.
         */
        LICENSE_SWL("SWL"),

        /**
         * TCL/TK License
         * 
         * <p>TCL/TK License.
         */
        LICENSE_TCL("TCL"),

        /**
         * TCP Wrappers License
         * 
         * <p>TCP Wrappers License.
         */
        LICENSE_TCP_WRAPPERS("TCP-wrappers"),

        /**
         * TMate Open Source License
         * 
         * <p>TMate Open Source License.
         */
        LICENSE_TMATE("TMate"),

        /**
         * TORQUE v2.5+ Software License v1.1
         * 
         * <p>TORQUE v2.5+ Software License v1.1.
         */
        LICENSE_TORQUE_1_1("TORQUE-1.1"),

        /**
         * Trusster Open Source License
         * 
         * <p>Trusster Open Source License.
         */
        LICENSE_TOSL("TOSL"),

        /**
         * Unicode License Agreement - Data Files and Software (2015)
         * 
         * <p>Unicode License Agreement - Data Files and Software (2015).
         */
        LICENSE_UNICODE_DFS_2015("Unicode-DFS-2015"),

        /**
         * Unicode License Agreement - Data Files and Software (2016)
         * 
         * <p>Unicode License Agreement - Data Files and Software (2016).
         */
        LICENSE_UNICODE_DFS_2016("Unicode-DFS-2016"),

        /**
         * Unicode Terms of Use
         * 
         * <p>Unicode Terms of Use.
         */
        LICENSE_UNICODE_TOU("Unicode-TOU"),

        /**
         * The Unlicense
         * 
         * <p>The Unlicense.
         */
        LICENSE_UNLICENSE("Unlicense"),

        /**
         * Universal Permissive License v1.0
         * 
         * <p>Universal Permissive License v1.0.
         */
        LICENSE_UPL_1_0("UPL-1.0"),

        /**
         * Vim License
         * 
         * <p>Vim License.
         */
        LICENSE_VIM("Vim"),

        /**
         * VOSTROM Public License for Open Source
         * 
         * <p>VOSTROM Public License for Open Source.
         */
        LICENSE_VOSTROM("VOSTROM"),

        /**
         * Vovida Software License v1.0
         * 
         * <p>Vovida Software License v1.0.
         */
        LICENSE_VSL_1_0("VSL-1.0"),

        /**
         * W3C Software Notice and License (1998-07-20)
         * 
         * <p>W3C Software Notice and License (1998-07-20).
         */
        LICENSE_W3C_19980720("W3C-19980720"),

        /**
         * W3C Software Notice and Document License (2015-05-13)
         * 
         * <p>W3C Software Notice and Document License (2015-05-13).
         */
        LICENSE_W3C_20150513("W3C-20150513"),

        /**
         * W3C Software Notice and License (2002-12-31)
         * 
         * <p>W3C Software Notice and License (2002-12-31).
         */
        LICENSE_W3C("W3C"),

        /**
         * Sybase Open Watcom Public License 1.0
         * 
         * <p>Sybase Open Watcom Public License 1.0.
         */
        LICENSE_WATCOM_1_0("Watcom-1.0"),

        /**
         * Wsuipa License
         * 
         * <p>Wsuipa License.
         */
        LICENSE_WSUIPA("Wsuipa"),

        /**
         * Do What The F*ck You Want To Public License
         * 
         * <p>Do What The F*ck You Want To Public License.
         */
        LICENSE_WTFPL("WTFPL"),

        /**
         * X11 License
         * 
         * <p>X11 License.
         */
        LICENSE_X11("X11"),

        /**
         * Xerox License
         * 
         * <p>Xerox License.
         */
        LICENSE_XEROX("Xerox"),

        /**
         * XFree86 License 1.1
         * 
         * <p>XFree86 License 1.1.
         */
        LICENSE_XFREE86_1_1("XFree86-1.1"),

        /**
         * xinetd License
         * 
         * <p>xinetd License.
         */
        LICENSE_XINETD("xinetd"),

        /**
         * X.Net License
         * 
         * <p>X.Net License.
         */
        LICENSE_XNET("Xnet"),

        /**
         * XPP License
         * 
         * <p>XPP License.
         */
        LICENSE_XPP("xpp"),

        /**
         * XSkat License
         * 
         * <p>XSkat License.
         */
        LICENSE_XSKAT("XSkat"),

        /**
         * Yahoo! Public License v1.0
         * 
         * <p>Yahoo! Public License v1.0.
         */
        LICENSE_YPL_1_0("YPL-1.0"),

        /**
         * Yahoo! Public License v1.1
         * 
         * <p>Yahoo! Public License v1.1.
         */
        LICENSE_YPL_1_1("YPL-1.1"),

        /**
         * Zed License
         * 
         * <p>Zed License.
         */
        LICENSE_ZED("Zed"),

        /**
         * Zend License v2.0
         * 
         * <p>Zend License v2.0.
         */
        LICENSE_ZEND_2_0("Zend-2.0"),

        /**
         * Zimbra Public License v1.3
         * 
         * <p>Zimbra Public License v1.3.
         */
        LICENSE_ZIMBRA_1_3("Zimbra-1.3"),

        /**
         * Zimbra Public License v1.4
         * 
         * <p>Zimbra Public License v1.4.
         */
        LICENSE_ZIMBRA_1_4("Zimbra-1.4"),

        /**
         * zlib/libpng License with Acknowledgement
         * 
         * <p>zlib/libpng License with Acknowledgement.
         */
        LICENSE_ZLIB_ACKNOWLEDGEMENT("zlib-acknowledgement"),

        /**
         * zlib License
         * 
         * <p>zlib License.
         */
        LICENSE_ZLIB("Zlib"),

        /**
         * Zope Public License 1.1
         * 
         * <p>Zope Public License 1.1.
         */
        LICENSE_ZPL_1_1("ZPL-1.1"),

        /**
         * Zope Public License 2.0
         * 
         * <p>Zope Public License 2.0.
         */
        LICENSE_ZPL_2_0("ZPL-2.0"),

        /**
         * Zope Public License 2.1
         * 
         * <p>Zope Public License 2.1.
         */
        LICENSE_ZPL_2_1("ZPL-2.1");

        private final java.lang.String value;

        Value(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating SPDXLicense.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SPDXLicense.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "not-open-source":
                return LICENSE_NOT_OPEN_SOURCE;
            case "0BSD":
                return LICENSE_0BSD;
            case "AAL":
                return LICENSE_AAL;
            case "Abstyles":
                return LICENSE_ABSTYLES;
            case "Adobe-2006":
                return LICENSE_ADOBE_2006;
            case "Adobe-Glyph":
                return LICENSE_ADOBE_GLYPH;
            case "ADSL":
                return LICENSE_ADSL;
            case "AFL-1.1":
                return LICENSE_AFL_1_1;
            case "AFL-1.2":
                return LICENSE_AFL_1_2;
            case "AFL-2.0":
                return LICENSE_AFL_2_0;
            case "AFL-2.1":
                return LICENSE_AFL_2_1;
            case "AFL-3.0":
                return LICENSE_AFL_3_0;
            case "Afmparse":
                return LICENSE_AFMPARSE;
            case "AGPL-1.0-only":
                return LICENSE_AGPL_1_0_ONLY;
            case "AGPL-1.0-or-later":
                return LICENSE_AGPL_1_0_OR_LATER;
            case "AGPL-3.0-only":
                return LICENSE_AGPL_3_0_ONLY;
            case "AGPL-3.0-or-later":
                return LICENSE_AGPL_3_0_OR_LATER;
            case "Aladdin":
                return LICENSE_ALADDIN;
            case "AMDPLPA":
                return LICENSE_AMDPLPA;
            case "AML":
                return LICENSE_AML;
            case "AMPAS":
                return LICENSE_AMPAS;
            case "ANTLR-PD":
                return LICENSE_ANTLR_PD;
            case "Apache-1.0":
                return LICENSE_APACHE_1_0;
            case "Apache-1.1":
                return LICENSE_APACHE_1_1;
            case "Apache-2.0":
                return LICENSE_APACHE_2_0;
            case "APAFML":
                return LICENSE_APAFML;
            case "APL-1.0":
                return LICENSE_APL_1_0;
            case "APSL-1.0":
                return LICENSE_APSL_1_0;
            case "APSL-1.1":
                return LICENSE_APSL_1_1;
            case "APSL-1.2":
                return LICENSE_APSL_1_2;
            case "APSL-2.0":
                return LICENSE_APSL_2_0;
            case "Artistic-1.0-cl8":
                return LICENSE_ARTISTIC_1_0_CL8;
            case "Artistic-1.0-Perl":
                return LICENSE_ARTISTIC_1_0_PERL;
            case "Artistic-1.0":
                return LICENSE_ARTISTIC_1_0;
            case "Artistic-2.0":
                return LICENSE_ARTISTIC_2_0;
            case "Bahyph":
                return LICENSE_BAHYPH;
            case "Barr":
                return LICENSE_BARR;
            case "Beerware":
                return LICENSE_BEERWARE;
            case "BitTorrent-1.0":
                return LICENSE_BIT_TORRENT_1_0;
            case "BitTorrent-1.1":
                return LICENSE_BIT_TORRENT_1_1;
            case "Borceux":
                return LICENSE_BORCEUX;
            case "BSD-1-Clause":
                return LICENSE_BSD_1_CLAUSE;
            case "BSD-2-Clause-FreeBSD":
                return LICENSE_BSD_2_CLAUSE_FREE_BSD;
            case "BSD-2-Clause-NetBSD":
                return LICENSE_BSD_2_CLAUSE_NET_BSD;
            case "BSD-2-Clause-Patent":
                return LICENSE_BSD_2_CLAUSE_PATENT;
            case "BSD-2-Clause":
                return LICENSE_BSD_2_CLAUSE;
            case "BSD-3-Clause-Attribution":
                return LICENSE_BSD_3_CLAUSE_ATTRIBUTION;
            case "BSD-3-Clause-Clear":
                return LICENSE_BSD_3_CLAUSE_CLEAR;
            case "BSD-3-Clause-LBNL":
                return LICENSE_BSD_3_CLAUSE_LBNL;
            case "BSD-3-Clause-No-Nuclear-License-2014":
                return LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE_2014;
            case "BSD-3-Clause-No-Nuclear-License":
                return LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_LICENSE;
            case "BSD-3-Clause-No-Nuclear-Warranty":
                return LICENSE_BSD_3_CLAUSE_NO_NUCLEAR_WARRANTY;
            case "BSD-3-Clause":
                return LICENSE_BSD_3_CLAUSE;
            case "BSD-4-Clause-UC":
                return LICENSE_BSD_4_CLAUSE_UC;
            case "BSD-4-Clause":
                return LICENSE_BSD_4_CLAUSE;
            case "BSD-Protection":
                return LICENSE_BSD_PROTECTION;
            case "BSD-Source-Code":
                return LICENSE_BSD_SOURCE_CODE;
            case "BSL-1.0":
                return LICENSE_BSL_1_0;
            case "bzip2-1.0.5":
                return LICENSE_BZIP2_1_0_5;
            case "bzip2-1.0.6":
                return LICENSE_BZIP2_1_0_6;
            case "Caldera":
                return LICENSE_CALDERA;
            case "CATOSL-1.1":
                return LICENSE_CATOSL_1_1;
            case "CC-BY-1.0":
                return LICENSE_CC_BY_1_0;
            case "CC-BY-2.0":
                return LICENSE_CC_BY_2_0;
            case "CC-BY-2.5":
                return LICENSE_CC_BY_2_5;
            case "CC-BY-3.0":
                return LICENSE_CC_BY_3_0;
            case "CC-BY-4.0":
                return LICENSE_CC_BY_4_0;
            case "CC-BY-NC-1.0":
                return LICENSE_CC_BY_NC_1_0;
            case "CC-BY-NC-2.0":
                return LICENSE_CC_BY_NC_2_0;
            case "CC-BY-NC-2.5":
                return LICENSE_CC_BY_NC_2_5;
            case "CC-BY-NC-3.0":
                return LICENSE_CC_BY_NC_3_0;
            case "CC-BY-NC-4.0":
                return LICENSE_CC_BY_NC_4_0;
            case "CC-BY-NC-ND-1.0":
                return LICENSE_CC_BY_NC_ND_1_0;
            case "CC-BY-NC-ND-2.0":
                return LICENSE_CC_BY_NC_ND_2_0;
            case "CC-BY-NC-ND-2.5":
                return LICENSE_CC_BY_NC_ND_2_5;
            case "CC-BY-NC-ND-3.0":
                return LICENSE_CC_BY_NC_ND_3_0;
            case "CC-BY-NC-ND-4.0":
                return LICENSE_CC_BY_NC_ND_4_0;
            case "CC-BY-NC-SA-1.0":
                return LICENSE_CC_BY_NC_SA_1_0;
            case "CC-BY-NC-SA-2.0":
                return LICENSE_CC_BY_NC_SA_2_0;
            case "CC-BY-NC-SA-2.5":
                return LICENSE_CC_BY_NC_SA_2_5;
            case "CC-BY-NC-SA-3.0":
                return LICENSE_CC_BY_NC_SA_3_0;
            case "CC-BY-NC-SA-4.0":
                return LICENSE_CC_BY_NC_SA_4_0;
            case "CC-BY-ND-1.0":
                return LICENSE_CC_BY_ND_1_0;
            case "CC-BY-ND-2.0":
                return LICENSE_CC_BY_ND_2_0;
            case "CC-BY-ND-2.5":
                return LICENSE_CC_BY_ND_2_5;
            case "CC-BY-ND-3.0":
                return LICENSE_CC_BY_ND_3_0;
            case "CC-BY-ND-4.0":
                return LICENSE_CC_BY_ND_4_0;
            case "CC-BY-SA-1.0":
                return LICENSE_CC_BY_SA_1_0;
            case "CC-BY-SA-2.0":
                return LICENSE_CC_BY_SA_2_0;
            case "CC-BY-SA-2.5":
                return LICENSE_CC_BY_SA_2_5;
            case "CC-BY-SA-3.0":
                return LICENSE_CC_BY_SA_3_0;
            case "CC-BY-SA-4.0":
                return LICENSE_CC_BY_SA_4_0;
            case "CC0-1.0":
                return LICENSE_CC0_1_0;
            case "CDDL-1.0":
                return LICENSE_CDDL_1_0;
            case "CDDL-1.1":
                return LICENSE_CDDL_1_1;
            case "CDLA-Permissive-1.0":
                return LICENSE_CDLA_PERMISSIVE_1_0;
            case "CDLA-Sharing-1.0":
                return LICENSE_CDLA_SHARING_1_0;
            case "CECILL-1.0":
                return LICENSE_CECILL_1_0;
            case "CECILL-1.1":
                return LICENSE_CECILL_1_1;
            case "CECILL-2.0":
                return LICENSE_CECILL_2_0;
            case "CECILL-2.1":
                return LICENSE_CECILL_2_1;
            case "CECILL-B":
                return LICENSE_CECILL_B;
            case "CECILL-C":
                return LICENSE_CECILL_C;
            case "ClArtistic":
                return LICENSE_CL_ARTISTIC;
            case "CNRI-Jython":
                return LICENSE_CNRI_JYTHON;
            case "CNRI-Python-GPL-Compatible":
                return LICENSE_CNRI_PYTHON_GPL_COMPATIBLE;
            case "CNRI-Python":
                return LICENSE_CNRI_PYTHON;
            case "Condor-1.1":
                return LICENSE_CONDOR_1_1;
            case "CPAL-1.0":
                return LICENSE_CPAL_1_0;
            case "CPL-1.0":
                return LICENSE_CPL_1_0;
            case "CPOL-1.02":
                return LICENSE_CPOL_1_02;
            case "Crossword":
                return LICENSE_CROSSWORD;
            case "CrystalStacker":
                return LICENSE_CRYSTAL_STACKER;
            case "CUA-OPL-1.0":
                return LICENSE_CUA_OPL_1_0;
            case "Cube":
                return LICENSE_CUBE;
            case "curl":
                return LICENSE_CURL;
            case "D-FSL-1.0":
                return LICENSE_D_FSL_1_0;
            case "diffmark":
                return LICENSE_DIFFMARK;
            case "DOC":
                return LICENSE_DOC;
            case "Dotseqn":
                return LICENSE_DOTSEQN;
            case "DSDP":
                return LICENSE_DSDP;
            case "dvipdfm":
                return LICENSE_DVIPDFM;
            case "ECL-1.0":
                return LICENSE_ECL_1_0;
            case "ECL-2.0":
                return LICENSE_ECL_2_0;
            case "EFL-1.0":
                return LICENSE_EFL_1_0;
            case "EFL-2.0":
                return LICENSE_EFL_2_0;
            case "eGenix":
                return LICENSE_E_GENIX;
            case "Entessa":
                return LICENSE_ENTESSA;
            case "EPL-1.0":
                return LICENSE_EPL_1_0;
            case "EPL-2.0":
                return LICENSE_EPL_2_0;
            case "ErlPL-1.1":
                return LICENSE_ERL_PL_1_1;
            case "EUDatagrid":
                return LICENSE_EUDATAGRID;
            case "EUPL-1.0":
                return LICENSE_EUPL_1_0;
            case "EUPL-1.1":
                return LICENSE_EUPL_1_1;
            case "EUPL-1.2":
                return LICENSE_EUPL_1_2;
            case "Eurosym":
                return LICENSE_EUROSYM;
            case "Fair":
                return LICENSE_FAIR;
            case "Frameworx-1.0":
                return LICENSE_FRAMEWORX_1_0;
            case "FreeImage":
                return LICENSE_FREE_IMAGE;
            case "FSFAP":
                return LICENSE_FSFAP;
            case "FSFUL":
                return LICENSE_FSFUL;
            case "FSFULLR":
                return LICENSE_FSFULLR;
            case "FTL":
                return LICENSE_FTL;
            case "GFDL-1.1-only":
                return LICENSE_GFDL_1_1_ONLY;
            case "GFDL-1.1-or-later":
                return LICENSE_GFDL_1_1_OR_LATER;
            case "GFDL-1.2-only":
                return LICENSE_GFDL_1_2_ONLY;
            case "GFDL-1.2-or-later":
                return LICENSE_GFDL_1_2_OR_LATER;
            case "GFDL-1.3-only":
                return LICENSE_GFDL_1_3_ONLY;
            case "GFDL-1.3-or-later":
                return LICENSE_GFDL_1_3_OR_LATER;
            case "Giftware":
                return LICENSE_GIFTWARE;
            case "GL2PS":
                return LICENSE_GL2PS;
            case "Glide":
                return LICENSE_GLIDE;
            case "Glulxe":
                return LICENSE_GLULXE;
            case "gnuplot":
                return LICENSE_GNUPLOT;
            case "GPL-1.0-only":
                return LICENSE_GPL_1_0_ONLY;
            case "GPL-1.0-or-later":
                return LICENSE_GPL_1_0_OR_LATER;
            case "GPL-2.0-only":
                return LICENSE_GPL_2_0_ONLY;
            case "GPL-2.0-or-later":
                return LICENSE_GPL_2_0_OR_LATER;
            case "GPL-3.0-only":
                return LICENSE_GPL_3_0_ONLY;
            case "GPL-3.0-or-later":
                return LICENSE_GPL_3_0_OR_LATER;
            case "gSOAP-1.3b":
                return LICENSE_G_SOAP_1_3B;
            case "HaskellReport":
                return LICENSE_HASKELL_REPORT;
            case "HPND":
                return LICENSE_HPND;
            case "IBM-pibs":
                return LICENSE_IBM_PIBS;
            case "ICU":
                return LICENSE_ICU;
            case "IJG":
                return LICENSE_IJG;
            case "ImageMagick":
                return LICENSE_IMAGE_MAGICK;
            case "iMatix":
                return LICENSE_I_MATIX;
            case "Imlib2":
                return LICENSE_IMLIB2;
            case "Info-ZIP":
                return LICENSE_INFO_ZIP;
            case "Intel-ACPI":
                return LICENSE_INTEL_ACPI;
            case "Intel":
                return LICENSE_INTEL;
            case "Interbase-1.0":
                return LICENSE_INTERBASE_1_0;
            case "IPA":
                return LICENSE_IPA;
            case "IPL-1.0":
                return LICENSE_IPL_1_0;
            case "ISC":
                return LICENSE_ISC;
            case "JasPer-2.0":
                return LICENSE_JAS_PER_2_0;
            case "JSON":
                return LICENSE_JSON;
            case "LAL-1.2":
                return LICENSE_LAL_1_2;
            case "LAL-1.3":
                return LICENSE_LAL_1_3;
            case "Latex2e":
                return LICENSE_LATEX2E;
            case "Leptonica":
                return LICENSE_LEPTONICA;
            case "LGPL-2.0-only":
                return LICENSE_LGPL_2_0_ONLY;
            case "LGPL-2.0-or-later":
                return LICENSE_LGPL_2_0_OR_LATER;
            case "LGPL-2.1-only":
                return LICENSE_LGPL_2_1_ONLY;
            case "LGPL-2.1-or-later":
                return LICENSE_LGPL_2_1_OR_LATER;
            case "LGPL-3.0-only":
                return LICENSE_LGPL_3_0_ONLY;
            case "LGPL-3.0-or-later":
                return LICENSE_LGPL_3_0_OR_LATER;
            case "LGPLLR":
                return LICENSE_LGPLLR;
            case "Libpng":
                return LICENSE_LIBPNG;
            case "libtiff":
                return LICENSE_LIBTIFF;
            case "LiLiQ-P-1.1":
                return LICENSE_LI_LI_Q_P_1_1;
            case "LiLiQ-R-1.1":
                return LICENSE_LI_LI_Q_R_1_1;
            case "LiLiQ-Rplus-1.1":
                return LICENSE_LI_LI_Q_RPLUS_1_1;
            case "Linux-OpenIB":
                return LICENSE_LINUX_OPEN_IB;
            case "LPL-1.0":
                return LICENSE_LPL_1_0;
            case "LPL-1.02":
                return LICENSE_LPL_1_02;
            case "LPPL-1.0":
                return LICENSE_LPPL_1_0;
            case "LPPL-1.1":
                return LICENSE_LPPL_1_1;
            case "LPPL-1.2":
                return LICENSE_LPPL_1_2;
            case "LPPL-1.3a":
                return LICENSE_LPPL_1_3A;
            case "LPPL-1.3c":
                return LICENSE_LPPL_1_3C;
            case "MakeIndex":
                return LICENSE_MAKE_INDEX;
            case "MirOS":
                return LICENSE_MIR_OS;
            case "MIT-0":
                return LICENSE_MIT_0;
            case "MIT-advertising":
                return LICENSE_MIT_ADVERTISING;
            case "MIT-CMU":
                return LICENSE_MIT_CMU;
            case "MIT-enna":
                return LICENSE_MIT_ENNA;
            case "MIT-feh":
                return LICENSE_MIT_FEH;
            case "MIT":
                return LICENSE_MIT;
            case "MITNFA":
                return LICENSE_MITNFA;
            case "Motosoto":
                return LICENSE_MOTOSOTO;
            case "mpich2":
                return LICENSE_MPICH2;
            case "MPL-1.0":
                return LICENSE_MPL_1_0;
            case "MPL-1.1":
                return LICENSE_MPL_1_1;
            case "MPL-2.0-no-copyleft-exception":
                return LICENSE_MPL_2_0_NO_COPYLEFT_EXCEPTION;
            case "MPL-2.0":
                return LICENSE_MPL_2_0;
            case "MS-PL":
                return LICENSE_MS_PL;
            case "MS-RL":
                return LICENSE_MS_RL;
            case "MTLL":
                return LICENSE_MTLL;
            case "Multics":
                return LICENSE_MULTICS;
            case "Mup":
                return LICENSE_MUP;
            case "NASA-1.3":
                return LICENSE_NASA_1_3;
            case "Naumen":
                return LICENSE_NAUMEN;
            case "NBPL-1.0":
                return LICENSE_NBPL_1_0;
            case "NCSA":
                return LICENSE_NCSA;
            case "Net-SNMP":
                return LICENSE_NET_SNMP;
            case "NetCDF":
                return LICENSE_NET_CDF;
            case "Newsletr":
                return LICENSE_NEWSLETR;
            case "NGPL":
                return LICENSE_NGPL;
            case "NLOD-1.0":
                return LICENSE_NLOD_1_0;
            case "NLPL":
                return LICENSE_NLPL;
            case "Nokia":
                return LICENSE_NOKIA;
            case "NOSL":
                return LICENSE_NOSL;
            case "Noweb":
                return LICENSE_NOWEB;
            case "NPL-1.0":
                return LICENSE_NPL_1_0;
            case "NPL-1.1":
                return LICENSE_NPL_1_1;
            case "NPOSL-3.0":
                return LICENSE_NPOSL_3_0;
            case "NRL":
                return LICENSE_NRL;
            case "NTP":
                return LICENSE_NTP;
            case "OCCT-PL":
                return LICENSE_OCCT_PL;
            case "OCLC-2.0":
                return LICENSE_OCLC_2_0;
            case "ODbL-1.0":
                return LICENSE_ODB_L_1_0;
            case "OFL-1.0":
                return LICENSE_OFL_1_0;
            case "OFL-1.1":
                return LICENSE_OFL_1_1;
            case "OGTSL":
                return LICENSE_OGTSL;
            case "OLDAP-1.1":
                return LICENSE_OLDAP_1_1;
            case "OLDAP-1.2":
                return LICENSE_OLDAP_1_2;
            case "OLDAP-1.3":
                return LICENSE_OLDAP_1_3;
            case "OLDAP-1.4":
                return LICENSE_OLDAP_1_4;
            case "OLDAP-2.0.1":
                return LICENSE_OLDAP_2_0_1;
            case "OLDAP-2.0":
                return LICENSE_OLDAP_2_0;
            case "OLDAP-2.1":
                return LICENSE_OLDAP_2_1;
            case "OLDAP-2.2.1":
                return LICENSE_OLDAP_2_2_1;
            case "OLDAP-2.2.2":
                return LICENSE_OLDAP_2_2_2;
            case "OLDAP-2.2":
                return LICENSE_OLDAP_2_2;
            case "OLDAP-2.3":
                return LICENSE_OLDAP_2_3;
            case "OLDAP-2.4":
                return LICENSE_OLDAP_2_4;
            case "OLDAP-2.5":
                return LICENSE_OLDAP_2_5;
            case "OLDAP-2.6":
                return LICENSE_OLDAP_2_6;
            case "OLDAP-2.7":
                return LICENSE_OLDAP_2_7;
            case "OLDAP-2.8":
                return LICENSE_OLDAP_2_8;
            case "OML":
                return LICENSE_OML;
            case "OpenSSL":
                return LICENSE_OPEN_SSL;
            case "OPL-1.0":
                return LICENSE_OPL_1_0;
            case "OSET-PL-2.1":
                return LICENSE_OSET_PL_2_1;
            case "OSL-1.0":
                return LICENSE_OSL_1_0;
            case "OSL-1.1":
                return LICENSE_OSL_1_1;
            case "OSL-2.0":
                return LICENSE_OSL_2_0;
            case "OSL-2.1":
                return LICENSE_OSL_2_1;
            case "OSL-3.0":
                return LICENSE_OSL_3_0;
            case "PDDL-1.0":
                return LICENSE_PDDL_1_0;
            case "PHP-3.0":
                return LICENSE_PHP_3_0;
            case "PHP-3.01":
                return LICENSE_PHP_3_01;
            case "Plexus":
                return LICENSE_PLEXUS;
            case "PostgreSQL":
                return LICENSE_POSTGRE_SQL;
            case "psfrag":
                return LICENSE_PSFRAG;
            case "psutils":
                return LICENSE_PSUTILS;
            case "Python-2.0":
                return LICENSE_PYTHON_2_0;
            case "Qhull":
                return LICENSE_QHULL;
            case "QPL-1.0":
                return LICENSE_QPL_1_0;
            case "Rdisc":
                return LICENSE_RDISC;
            case "RHeCos-1.1":
                return LICENSE_RHE_COS_1_1;
            case "RPL-1.1":
                return LICENSE_RPL_1_1;
            case "RPL-1.5":
                return LICENSE_RPL_1_5;
            case "RPSL-1.0":
                return LICENSE_RPSL_1_0;
            case "RSA-MD":
                return LICENSE_RSA_MD;
            case "RSCPL":
                return LICENSE_RSCPL;
            case "Ruby":
                return LICENSE_RUBY;
            case "SAX-PD":
                return LICENSE_SAX_PD;
            case "Saxpath":
                return LICENSE_SAXPATH;
            case "SCEA":
                return LICENSE_SCEA;
            case "Sendmail":
                return LICENSE_SENDMAIL;
            case "SGI-B-1.0":
                return LICENSE_SGI_B_1_0;
            case "SGI-B-1.1":
                return LICENSE_SGI_B_1_1;
            case "SGI-B-2.0":
                return LICENSE_SGI_B_2_0;
            case "SimPL-2.0":
                return LICENSE_SIM_PL_2_0;
            case "SISSL-1.2":
                return LICENSE_SISSL_1_2;
            case "SISSL":
                return LICENSE_SISSL;
            case "Sleepycat":
                return LICENSE_SLEEPYCAT;
            case "SMLNJ":
                return LICENSE_SMLNJ;
            case "SMPPL":
                return LICENSE_SMPPL;
            case "SNIA":
                return LICENSE_SNIA;
            case "Spencer-86":
                return LICENSE_SPENCER_86;
            case "Spencer-94":
                return LICENSE_SPENCER_94;
            case "Spencer-99":
                return LICENSE_SPENCER_99;
            case "SPL-1.0":
                return LICENSE_SPL_1_0;
            case "SugarCRM-1.1.3":
                return LICENSE_SUGAR_CRM_1_1_3;
            case "SWL":
                return LICENSE_SWL;
            case "TCL":
                return LICENSE_TCL;
            case "TCP-wrappers":
                return LICENSE_TCP_WRAPPERS;
            case "TMate":
                return LICENSE_TMATE;
            case "TORQUE-1.1":
                return LICENSE_TORQUE_1_1;
            case "TOSL":
                return LICENSE_TOSL;
            case "Unicode-DFS-2015":
                return LICENSE_UNICODE_DFS_2015;
            case "Unicode-DFS-2016":
                return LICENSE_UNICODE_DFS_2016;
            case "Unicode-TOU":
                return LICENSE_UNICODE_TOU;
            case "Unlicense":
                return LICENSE_UNLICENSE;
            case "UPL-1.0":
                return LICENSE_UPL_1_0;
            case "Vim":
                return LICENSE_VIM;
            case "VOSTROM":
                return LICENSE_VOSTROM;
            case "VSL-1.0":
                return LICENSE_VSL_1_0;
            case "W3C-19980720":
                return LICENSE_W3C_19980720;
            case "W3C-20150513":
                return LICENSE_W3C_20150513;
            case "W3C":
                return LICENSE_W3C;
            case "Watcom-1.0":
                return LICENSE_WATCOM_1_0;
            case "Wsuipa":
                return LICENSE_WSUIPA;
            case "WTFPL":
                return LICENSE_WTFPL;
            case "X11":
                return LICENSE_X11;
            case "Xerox":
                return LICENSE_XEROX;
            case "XFree86-1.1":
                return LICENSE_XFREE86_1_1;
            case "xinetd":
                return LICENSE_XINETD;
            case "Xnet":
                return LICENSE_XNET;
            case "xpp":
                return LICENSE_XPP;
            case "XSkat":
                return LICENSE_XSKAT;
            case "YPL-1.0":
                return LICENSE_YPL_1_0;
            case "YPL-1.1":
                return LICENSE_YPL_1_1;
            case "Zed":
                return LICENSE_ZED;
            case "Zend-2.0":
                return LICENSE_ZEND_2_0;
            case "Zimbra-1.3":
                return LICENSE_ZIMBRA_1_3;
            case "Zimbra-1.4":
                return LICENSE_ZIMBRA_1_4;
            case "zlib-acknowledgement":
                return LICENSE_ZLIB_ACKNOWLEDGEMENT;
            case "Zlib":
                return LICENSE_ZLIB;
            case "ZPL-1.1":
                return LICENSE_ZPL_1_1;
            case "ZPL-2.0":
                return LICENSE_ZPL_2_0;
            case "ZPL-2.1":
                return LICENSE_ZPL_2_1;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
