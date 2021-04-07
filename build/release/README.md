# Build and Tag Release

The build process supports GitHub Actions and build on multiple branches. 

The build is triggered by a push of a tag. For each tag, a new build is executed. If a build fails, remove the failed build's tag. 

## Dependencies
https://github.com/actions/virtual-environments
Ubuntu 20.04 	ubuntu-latest or ubuntu-20.04 	ubuntu-20.04 	
https://github.com/actions/virtual-environments/blob/main/images/linux/Ubuntu2004-README.md
# jq (already installed in ubuntu 20.04)
# gh (already installed in ubuntu 20.04)
# docker tools (already installed in ubuntu 20.04)

## Process
The build executes the following steps:

- Setup OpenJDK
- Build and Release for Git Tag
    - at the end of the build gathers logs
- Gather error logs (only run on a failure)
- Upload logs
    - The logs are posted on the action as 'Artifact'

The jacoco-aggregate shows the cross-project unit test code coverage, and NOT the integration tests. 

Sonatype max size is 1024M

## Build Archive

The archive contains the following: 
- release-commit-details.txt
- test details from the surefire reports
- and an aggregated view of code coverage - fhir-install/target/site/jacoco-aggregate/index.html

## Tracing and Logging the Build 

To view the logs:

- Navigate to https://github.com/IBM/FHIR/actions
- Click on the Build of interest
- Look to the upper right of the Action view, and post build completion the `Download artifacts` is shown. More specifically release-tag-openjdk11.
- Download the file
- Extract and view the contents. 