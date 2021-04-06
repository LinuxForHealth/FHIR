#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Runs the build with all the preconfigured profiles with gpg:sign
# The relevant keys for this signature are published, and can be confirmed at 
# http://keys.gnupg.net/pks/lookup?search=fhir&fingerprint=on&op=index

# fhir-examples
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-examples").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn install source:jar source:test-jar javadoc:jar gpg:sign -f fhir-examples \
        -DadditionalJOption=-Xdoclint:none \
        -f fhir-examples -P "${BUILD_PROFILES}" -DskipTests

# fhir-tools
export BUILD_PROFILES=" $(jq -r '.build | map(.)| join(",")' build/release/config/release.json)"
mvn install source:jar source:test-jar javadoc:jar \
        -DadditionalJOption=-Xdoclint:none \
        gpg:sign -Dgpg.passphrase="${FHIR_GPG_PASSPHRASE}" -Dgpg.defaultKeyring=false -Dgpg.keyname="${FHIR_GPG_KEYNAME}" \
        -f fhir-tools -P "${BUILD_PROFILES}" -DskipTests

# fhir-parent
mvn install source:jar source:test-jar javadoc:jar \
        -DadditionalJOption=-Xdoclint:none \
        gpg:sign -Dgpg.passphrase="${FHIR_GPG_PASSPHRASE}" -Dgpg.defaultKeyring=false -Dgpg.keyname="${FHIR_GPG_KEYNAME}" \
        -f fhir-parent -P "${BUILD_PROFILES}" -DskipTests 

# EOF