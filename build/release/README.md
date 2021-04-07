# Build and Tag Release

The build process supports GitHub Actions and build on multiple branches. 

The build is triggered by a push of a tag. For each tag, a new build is executed. If a build fails, remove the failed build's tag. 

## Pre-requisites
The build uses a [Virtual Environment](https://github.com/actions/virtual-environments) which configures [ubuntu-latest](https://github.com/actions/virtual-environments/blob/main/images/linux/Ubuntu2004-README.md). 

This pre-requisite already has jq, gh, docker tools, podman and skopeo installed.

## Process
The build executes the following steps:

- Checkout the Main Branch
- Setup OpenJDK
- Build and Release
    - Set Global Settings and Confirm Settings
    - Prep the Release (install anything missing)
    - Clean, Set Version, Build, Sign
    - Test
    - Release to Sonatype, Docker Registry
    - Drop (which is a nop, but important to put in place)
- Gather artifacts and materials (always)
- Upload artifacts (always)

**Notes**
- The jacoco-aggregate shows the cross-project unit test code coverage, and NOT the integration tests. 
- Sonatype max size is 1024M

## Build Archive

The archive contains the following: 
- release-commit-details.txt
- test details from the surefire reports
- and an aggregated view of code coverage - fhir-install/target/site/jacoco-aggregate/index.html

## Tracing and Logging the Build 

To view the logs:

- Navigate to https://github.com/IBM/FHIR/actions
- Click on the Build of interest
- Look to the upper right of the Action view, and post build completion the `Download artifacts` is shown. More specifically release-openjdk11.
- Download the file
- Extract and view the contents. 