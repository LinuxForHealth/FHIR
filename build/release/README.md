# 
The build process supports Travis and GitHub Actions. 

The build executes in with the following types:
- SNAPSHOT (skips the version change phase)
- EXISTING (ignores the build phase, and is generally local to the user's development machine)
- RELEASE CANDIDATE (automated)
- RELEASE (automated)

The build is triggered by a push of a tag.  For each tag, a new build is executed.  If a build fails, remove the failed build's tag. 

The build executes the following steps: 
- Setup Graphviz environment (for javadoc)
- Setup OpenJDK
- Build and Release for Git Tag
    - at the end of the build gathers logs
- Gather error logs (only run on a failure)
- Upload logs
    - The logs are posted on the action as 'Artifact'

To view the logs: 
- Navigate to https://github.com/IBM/FHIR/actions
- Click on the Build of interest
- Look to the upper right of the Action view, and post build completion the `Download artifacts` is shown. More specifically release-tag-openjdk11.  
- Download the file
- Extract and view the contents. 

The archive contains the following: 
- release-commit-details.txt
- test details from the surefire reports
- and an aggregated view of code coverage - fhir-install/target/site/jacoco-aggregate/index.html

The jacoco-aggregate shows the cross-project unit test code coverage, and NOT the integration tests. 

### CodeCov.io
The IBM project is at https://codecov.io/gh/IBM/FHIR  As of right now it does not have authorization for the details from IBM/FHIR. 

In order to submit the files to codecov.io, a Personal Access token must be added on https://github.com/IBM/FHIR/settings/secrets as CODECOV_TOKEN .  The token in place right now is invalid. 

Also add CODECOV_RUNME secret, and set anything as the value. If this secret is passed as a non-empty string, the codecov.io 
runs.  To stop it running, remove the secret.

To generate the jacoco.xml file locally, run the following: 
```
mvn -f fhir-examples clean install 
PROFILES_ARR=(model-all-tests)
PROFILES_ARR+=(validation-all-tests)
PROFILES_ARR+=(search-all-tests)
PROFILES_ARR+=(jdbc-all-tests)
PROFILES_ARR+=(aggregate-report)
PROFILES=$(IFS=, ; echo "${PROFILES_ARR[*]}")
mvn -P${PROFILES} -f fhir-parent test jacoco:report
mvn -Paggregate-report,${PROFILES} -f fhir-parent jacoco:report-aggregate
```