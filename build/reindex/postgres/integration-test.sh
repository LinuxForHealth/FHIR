#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# run_test_part1 - 
run_test_part1() {


}

# run_test_part2 - 
run_test_part2()  {


}

# reset_server -
reset_server() {

}

###############################################################################
# Store the current directory
pushd $(pwd) > /dev/null

# Change to the reindex/bin directory
cd "${WORKSPACE}"

run_test_part1
reset_server
run_test_part2

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################