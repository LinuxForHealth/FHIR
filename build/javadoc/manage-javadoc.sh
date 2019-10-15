#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# The purpose of this 
# Manage Existing JavaDocs 

# Check for JAVA_HOME and it must be set. 
if [ -z "${JAVA_HOME}" ] 
then 
    # The next command is really designed for Linux images to more quickly find the JAVA_HOME
    export JAVA_HOME=$(readlink $(which java)/../..)
fi 
echo "JAVA_HOME: ${JAVA_HOME}"

# RC


# RELEASE 


# EOF 