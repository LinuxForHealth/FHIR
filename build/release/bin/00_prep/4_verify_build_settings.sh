


# If Build is for 

${BUILD_ID}
GIT_COMMIT
GIT_BRANCH
GIT_URL
FHIR_GPG_PASSPHRASE
FHIR_GPG_KEYNAME
GPG_PASSPHRASE
GPG_KEYNAME

# Check the BUILD_PASSWORD is set
if [ ! -z "${BUILD_PASSWORD}" ]
then 
    info "BUILD PASSWORD is set!"
else 
    warn "BUILD PASSWORD is NOT set!"
fi

# Check the BUILD_PASSWORD is set
if [ ! -z "${BUILD_USERNAME}" ]
then 
    info "BUILD USERNAME is set!"
else 
    warn "BUILD USERNAME is NOT set!"
fi