#!/bin/bash
#
# Licensed Materials - Property of IBM
#
# 5737-H33
#
# (C) Copyright IBM Corp. 2018, 2019  All Rights Reserved.
#
# US Government Users Restricted Rights - Use, duplication or
# disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
#


#############################
# Syntax for running script ./<scriptname> <Releasename> <namespace>
# Must be logged into kubernetes cluster before executing this script, see following command
# cloudctl login -a https://<master-node-external-ip>:<port>
#############################

# Print node resource information
printNodeResourceInformation() {
echo "!!--Printing available node resources"
nodes=$(kubectl get nodes --no-headers | awk '{print $1}')
for node in $nodes; do
    echo "Node: $node"
    kubectl get node $node -o=jsonpath="  Allocatable resources:{'\n'}    cpu:  {.status.allocatable.cpu}{'\n'}    memory:  {.status.allocatable.memory}{'\n'}"
    echo -e "  Allocated resources:"
    kubectl describe node $node | grep Allocated -A 5 | grep -ve Event -ve Allocated -ve percent -ve -- | while read line ; do
    echo "    $line"
    done
echo " "
done
}
# Print event data in an easily readable format
filterevents() {
while read line ; do
    NAME=$(echo $line | awk '{printf $4}' | cut -d. -f1)
    TYPE=$(echo $line | awk '{printf $5}')
    FIRSTSEEN=$(echo $line | awk '{printf $2}')
    LASTSEEN=$(echo $line | awk '{printf $1}')
    ISSUE=$(echo $line | awk '{for(i=9;i<=NF;++i)printf $i""FS ; print ""}' | sed -e 's/ /~/g')
    ISSUE=$(echo $ISSUE | sed -e 's/~/ /g')
    if [[ $TYPE == "Pod" ]]; then
        RESOURCES=$(kubectl get pod $NAME -n$NAMESPACE -o=jsonpath="{range .spec.containers[*]}{'\t'}Container: {.name}{'\n'}{'\t'}{'\t'}Requested:  cpu: {.resources.requests.cpu}{'\t'}memory: {.resources.requests.memory}{'\n'}{'\t'}{'\t'}Limits:     cpu: {.resources.limits.cpu}{'\t'}memory: {.resources.limits.memory}{'\n'}{end}")
        echo -e "Name: $NAME\nType: $TYPE\nPodResources:\n$RESOURCES \nFirstSeen: $FIRSTSEEN\nLastSeen: $LASTSEEN\nIssue: $ISSUE\n"
    else
        echo -e "Name: $NAME\nType: $TYPE\nFirstSeen: $FIRSTSEEN\nLastSeen: $LASTSEEN\nIssue: $ISSUE\n"
    fi
done
}

# Event data by default saved for an hour, If a pod is older than an hour delete it so that it will get redeployed and the events will occur again
restartPodsIfOld() {
    waitPod=false
    if $RESTARTOLDPODS; then
        for pod in $@
        do
            age=$(kubectl get pod -n$NAMESPACE $pod | awk '{printf $5 "\n"}' | grep -e '[h|d]' | cat)
            if [[ $age ]]; then
            echo "Pod older than 1 hour, restarting pod $pod"
            kubectl delete pod $pod -n$NAMESPACE
            waitPods=true
            fi
        done
        if [[ $waitPods ]]; then
        echo 'Sleeping for 60 seconds while pods restart.'
        sleep 60
        fi
    fi
}

checkReleasenameAndNamespaceValid() {
pods=$(kubectl get pods -n$NAMESPACE -l release=$RELEASENAME 2>&1)
if [[ $pods == "No resources found." ]]; then
    echo -e "No pods could be found in Namespace: $NAMESPACE with Releasename: $RELEASENAME\nPlease check correct values have been supplied to this script"
    exit 1
fi
}

# If api-key in ibm-es-iam-secret does not exist print message and exit as this will be the cause of a lot of problems.
checkIamSecretApiKeyExists() {
    echo "-----------------------------------------"
    echo "..Checking the ibm-es-iam-secret API Key"
    testName="IAM Secret Api Key"
    iamSecretName=$(kubectl get secrets -n$NAMESPACE --no-headers -l app=ibm-es,component=security,release=$RELEASENAME -o custom-columns=NAME:.metadata.name 2>&1)
    if [[ $iamSecretName =~ "not found" ]]
    then
        echo "No IAM secret found in namespace $NAMESPACE, please contact support."
        testFailed $testName
    elif [[ $oauthSecret =~ "\"$RELEASENAME-ibm-es-oauth-secret\" is forbidden" ]]
    then
        echo "User does not have the authority to view secrets. Unable to check IAM Secret configuration."
    else
        apiKeyName=$(kubectl get secrets -n$NAMESPACE $iamSecretName -o jsonpath="{.metadata.annotations['ibm\.com/iam-service\.api-key']}")
        apiKeyValue=$(kubectl get secrets -n$NAMESPACE $iamSecretName -o jsonpath="{.data['$apiKeyName']}")
        if [[ -z $apiKeyValue ]]
        then
            echo "No api-key found in [$iamSecretName] secret, please contact support."
            testFailed $testName
        else
            echo "....API Key found"
            testPassed $testName
        fi
    fi
}

# If the service-ID in ibm-es-iam-secret does not exist or if it fails this test will fail
checkIamSecretServiceID() {
    echo "-----------------------------------------"
    echo "..Checking the ibm-es-iam-secret Service ID"
    testName="IAM Secret Service ID"
    if [[ $iamSecretName =~ "not found" ]]
    then
        echo "No IAM secret found in namespace $NAMESPACE, please contact support."
        testFailed $testName
    elif [[ $oauthSecret =~ "\"$RELEASENAME-ibm-es-oauth-secret\" is forbidden" ]]
    then
        echo "User does not have the authority to view secrets. Unable to check IAM Secret configuration."
    else
        serviceIdName=$(kubectl get secrets -n$NAMESPACE $iamSecretName -o jsonpath="{.metadata.annotations['ibm\.com/iam-service\.id']}")
        serviceIdValue=$(kubectl get secrets -n$NAMESPACE $iamSecretName -o jsonpath="{.data['$serviceIdName']}" | base64 --decode)
        trimmedServiceIdValue=${serviceIdValue:4}
        if [[ -z $serviceIdValue ]]
        then
            echo "No service-id found in [$iamSecretName] secret, please contact support."
            testFailed $testName
        else
            echo "....Service ID found"
            if  curl -k -X GET -H "Accept: application/json" -H "Authorization: Bearer $USER_ACCESS_TOKEN" https://$MASTER_IP:8443/iam-token/serviceids/$trimmedServiceIdValue | grep "error"
            then
                echo "The service has failed"
                testFailed $testName
            else
                testPassed $testName
            fi
        fi
    fi
}

# This function checks that the OAuth secret has been defined and it also gets the OAuth registered config to check the endpoints that have been defined.
checkRegisteredOAuthEndpoints() {
    echo "-----------------------------------"
    echo "..Checking registered OAuth Endpoints"
    testName="Oauth Endpoints"
    oauthSecret=$(kubectl get secret -n$NAMESPACE $RELEASENAME-ibm-es-oauth-secret --no-headers 2>&1)

    if [[ $oauthSecret =~ "\"$RELEASENAME-ibm-es-oauth-secret\" not found" ]]
    then
        echo "!!--No OAuth secret found in namespace $NAMESPACE, please contact support."
        testFailed $testName
    elif [[ $oauthSecret =~ "\"$RELEASENAME-ibm-es-oauth-secret\" is forbidden" ]]
    then
       echo "User does not have the authority to view secrets. Unable to check OAuth configuration."
    else
        eventstreamsOIDCEncodedClientID=`kubectl get secret $RELEASENAME-ibm-es-oauth-secret -o=jsonpath='{.data.username}' -n$NAMESPACE`
        if [[ $eventstreamsOIDCEncodedClientID == '' ]]
        then
            echo "!!--No OIDC Client ID found in [$RELEASENAME-ibm-es-oauth-secret] secret, please contact support."
            testFailed $testName
        else
            echo "....OAuth Secret correctly generated"

            eventstreamsOIDCClientID=`echo -n $eventstreamsOIDCEncodedClientID| base64 --decode`
            eventstreamsOIDCClientSecret=`kubectl get secret $RELEASENAME-ibm-es-oauth-secret -o=jsonpath='{.data.password}' -n$NAMESPACE | base64 --decode`
            # Now we have the OIDC userid and secret, we can use this to do further security checks.
            OIDC_BASIC_AUTH_TOKEN=`printf "%s:%s" $eventstreamsOIDCClientID $eventstreamsOIDCClientSecret | base64`

            # At this point we have proved that the OAuth secret has been created successfully. We'll now attempt to list the registered endpoints.
            # We need to get the OIDC pwd from the kube system secret to be able to do this. Check for permissions failures.
            encryptedOAuthAdminToken=$(kubectl get secret platform-oidc-credentials -n kube-system -o=jsonpath='{.data.OAUTH2_CLIENT_REGISTRATION_SECRET}' 2>&1)

            if [[ $encryptedOAuthAdminToken =~ "\"platform-oidc-credentials\" is forbidden" ]]
            then
                echo "User does not have the authority to view secrets in kube system namespace. Unable to check OAuth configuration."
            else
                echo "..Checking the Registered OAuth Endpoints"

                oauthAdminToken=`echo $encryptedOAuthAdminToken | base64 --decode`
                oauthOutput=`curl -s -k -u oauthadmin:$oauthAdminToken -H "Content-Type: application/json" https://$MASTER_IP:$CLUSTER_PORT/oidc/endpoint/OP/registration/$eventstreamsOIDCClientID`

                # If we have an error description in the response, then extract the error and warn the user.
                if [[ $oauthOutput =~ error_description ]]
                then
                    error=${oauthOutput##*\"error_description\":\"}
                    error=${error%%\"*}
                    echo "!!--Unable to find OAuth registration for client id $eventstreamsOIDCClientID: Error : $error"
                    testFailed $testName
                elif [[ $oauthOutput =~ "500 Internal Server Error" ]]
                then
                    echo "Unable to get OIDC Endpoint definitions: $oauthOutput"
                    testFailed $testName
                else
                    # Find the redirect_uris key. Assign everything after this key to the redirect_uris variable.
                    redirect_uris=${oauthOutput##*redirect_uris\":[}
                    # Find the closing array ] bracket, and remove everything after this.
                    redirect_uris=${redirect_uris%%]*}
                    # Finally replace the ','s with spaces.
                    redirect_uris=${redirect_uris//,/ }

                    # Find the redirect_uris key. Assign everything after this key to the trusted_uri_prefixes variable.
                    trusted_uri_prefixes=${oauthOutput##*trusted_uri_prefixes\":[}
                    # Find the closing array ] bracket, and remove everything after this.
                    trusted_uri_prefixes=${trusted_uri_prefixes%%]*}
                    # Finally replace the ','s with spaces.
                    trusted_uri_prefixes=${trusted_uri_prefixes//,/ }

                    # Find the post_logout_redirect_uris key. Assign everything after this key to the post_logout_redirect_uris variable.
                    post_logout_redirect_uris=${oauthOutput##*post_logout_redirect_uris\":[}
                    # Find the closing array ] bracket, and remove everything after this.
                    post_logout_redirect_uris=${post_logout_redirect_uris%%]*}
                    # Finally replace the ','s with spaces.
                    post_logout_redirect_uris=${post_logout_redirect_uris//,/ }

                    echo "Trusted URI prefixes: ${trusted_uri_prefixes//\"/}"
                    echo "Redirect URIs: ${redirect_uris//\"/}"
                    echo "Post Logout Redirect URIs: ${post_logout_redirect_uris//\"/}"
                    testPassed $testName
                    echo "....OAuthEndpoint check complete"
                fi
            fi
        fi
    fi
}

# This function checks that the security role mappings have been defined.
checkSecurityRoleMappings() {
    echo "-------------------------------------------------"
    echo "..Checking the registered security role mappings"
    testName="Role Mappings"

    esRoleMapping=`curl -s -k -H 'Content-Type: application/json' -H 'Accept: application/json' -H "Authorization: Bearer $USER_ID_TOKEN" https://$MASTER_IP:$CLUSTER_PORT/iam-pap/acms/v1/services/eventstreams`
    # If we have an errors object in the response, then extract the error and warn the user.
    if [[ $esRoleMapping =~ "{\"errors\":[" ]]
    then
        echo "!!--WARNING: Event Streams security service not found. Therefore no role mappings defined."
        testFailed $testName
    else
        echo "Role mapping actions defined:"
        actions=${esRoleMapping##*actions\":[}

        actions=${actions%%]\}]*}
        actions=${actions//,/ }
        actions=${actions//{/ }

        for actionID in ${actions[@]}; do
            # echo $actionID
            if [[ ${actionID} =~ \"id\":\"(.*)\" ]]
            then
                echo "  action: ${BASH_REMATCH[1]}"
            fi
        done
        echo "....role mappings check complete."
        testPassed $testName
    fi
}

checkAccessControllerLogs() {
    echo "--------------------------------------------"
    echo "..Checking AccessController logs for errors"
    accessControllerPods=`kubectl get pods -o name -n$NAMESPACE | grep access-controller`

    testName="Access Controller logs"
    testPassed=true
    for acpod in $accessControllerPods; do
        podoutput=`kubectl logs $acpod -c access-controller -n$NAMESPACE | grep '"error":' | grep -v 'hyperion' | wc -l`

        if [ $podoutput != 0 ]
        then
            echo "!!-- $podoutput Errors found in $acpod log, see tmpLogs/$acpod/access-controller.log"
            testPassed=false
        else
            echo "....No errors found in $acpod log."
        fi
  done

  if $testPassed
  then
      testPassed $testName
  else
      testFailed $testName
  fi
}

# This function attempts to get the current role for the user running this script. We need to have got the OIDC client id and secret from the OAuth check
# before we can do this, and we also need to be able to get the users access token from cloudctl. If either of these are not available we can't do this check.
getCurrentUserRole() {
    testName="User's Role"
    echo "---------------------------"
    echo "..Checking User's current Role"
    if [[ ! -z $OIDC_BASIC_AUTH_TOKEN && ! -z $USER_ACCESS_TOKEN ]]
    then
        introspectOutput=`curl -X POST -s -k -H 'Content-Type: application/x-www-form-urlencoded' -H 'Accept: application/json' -H "Authorization: Basic $OIDC_BASIC_AUTH_TOKEN" -d "token=$USER_ACCESS_TOKEN" https://$MASTER_IP:$CLUSTER_PORT/idprovider/v1/auth/introspect`

        user=${introspectOutput##*\"sub\":\"}
        # Find the quotes and remove everything after this
        user=${user%%\"*}

        USER_ROLE=`curl -k -s -X GET -H "Content-Type: application/json" -H "Accept: application/json" -H "Authorization: Bearer $USER_ACCESS_TOKEN" https://$MASTER_IP:$CLUSTER_PORT/idmgmt/identity/api/v1/teams/highestRole?crn=crn%3Av1%3Aicp%3Aprivate%3Aeventstreams%3A$CLUSTER_NAME%3An%2F$NAMESPACE%3Ar%2F$RELEASENAME%3A%3A`
        echo "....User $user running with $USER_ROLE Role"
    else
        echo "....Unable to check User's current Role due to permissions"
    fi
}

# If no kafka or zookeeper pods are found, get Failed events for the release
# Kafka and zookeeper pods do not appear if the resource request for these was greater than the limits set for them
# the stateful sets produce failedcreate events in this case
checkKafkaZookeeperPodsPresent() {
    echo "---------------------------------"
    testName="Kafka/Zookeeper Pods"
    testPassed=true
    kafkaPresent=$(kubectl get pods -n$NAMESPACE -l component=kafka,release=$RELEASENAME 2>&1)
    zookPresent=$(kubectl get pods -n$NAMESPACE -l component=zookeeper,release=$RELEASENAME 2>&1)

    # If no kafka pods found

    echo "..Checking kafka-sts pods"
    if [[ $kafkaPresent == "No resources found." ]]; then
        testPassed=false
        echo '!!--Could not find kafka-sts pods.'
        kubectl get events -n$NAMESPACE --field-selector reason=FailedCreate | grep $RELEASENAME | filterevents
        echo '------------------'
    else
        echo "....kafka-sts pods found"
    fi

    # If no Zookeeper pods found
    echo "..Checking zookeeper-sts pods"
    if [[ $zookPresent == "No resources found." ]]; then
        testPassed=false
        echo '!!--Could not find zookeeper-sts pods.'
        kubectl get events -n$NAMESPACE --field-selector reason=FailedCreate | grep $RELEASENAME | filterevents
        echo '------------------'
    else
        echo "....zookeeper-sts pods found"
    fi

    if $testPassed
    then
        testPassed $testName
    else
        testFailed $testName
    fi
}

# If found pods in "Failed" state, get reason and msg from describe
checkPodsInFailedState() {
    echo "---------------------------"
    echo "..Checking for Failed pods"
    testName="Failed Pods"
    podsFailed=($(kubectl get pods -n$NAMESPACE -l $RELEASENAME -o jsonpath='{.items[?(@.status.phase=="Failed")].metadata.name}'))
    if [[ $podsFailed ]]
    then
        testFailed $testName
        echo '!!--Failed pods found, checking pod events'
        restartPodsIfOld ${podsPending[@]}
        for pod in ${podsFailed[@]}
        do
            failedMessage=$(kubectl get pod -n$NAMESPACE $pod -o jsonpath='{.status.message}')
            failedReason=$(kubectl get pod -n$NAMESPACE $pod -o jsonpath='{.status.reason}')
            failedAge=$(kubectl get pod -n$NAMESPACE $pod --no-headers | awk '{printf $5}')
            echo -e "Pod: $pod\nAge: $failedAge\nReason: $failedReason\nMessage: $failedMessage "
        done
    else
        testPassed $testName
        echo "....No Failed pods found"
    fi
}

# If found pods in "Pending" state, get Failed events for those pods
checkPodsInPendingState() {
    echo "----------------------------"
    echo "..Checking for Pending pods"
    testName="Pending Pods"
    podsPending=($(kubectl get pods -n$NAMESPACE -l release=$RELEASENAME -o jsonpath='{.items[?(@.status.phase=="Pending")].metadata.name}'))
    if [[ $podsPending ]]; then
        testFailed $testName
        echo '!!--Pending pods found, checking pod for failed events'
        restartPodsIfOld ${podsPending[@]}
        for pod in ${podsPending[@]}
        do
            podsPendingAbnormalEvents=$(kubectl get events -n$NAMESPACE --field-selector involvedObject.name=$pod | grep Failed)
            if [[ $podsPendingAbnormalEvents ]]; then
                echo $podsPendingAbnormalEvents | filterevents
            else
                echo -e "No failed events found for pod $pod\n"
            fi
        done
        printNodeResourceInformation
    else
        testPassed $testName
        echo "....No Pending pods found"
    fi
}

# If found a BackOff event and those pods are still in backoff state, print last lines of logs
checkPodsInCrashLoopBackOff() {
    echo "-------------------------------------"
    echo "..Checking for CrashLoopBackOff pods"
    testName="CrashLoopBackOff Pods"
    podsCrashLoopBackoff=($(kubectl get pods -n$NAMESPACE -l release=$RELEASENAME | grep CrashLoopBackOff | awk '{printf $1 "\n"}'))
    if [[ $podsCrashLoopBackoff ]]; then
        testFailed $testName
        echo '!!--CrashloopBackOff event found, investigating'
        for i in "${!podsCrashLoopBackoff[@]}"
        do
            badContainers=($(kubectl get pod -n$NAMESPACE ${podsCrashLoopBackoff[$i]} -o jsonpath='{.status.containerStatuses[?(@.ready==false)].name}'))
            echo "!!--Printing logs for pod ${podsCrashLoopBackoff[$i]} found to be in CrashLoopBackOff state."
            for container in ${badContainers[@]}
            do
                echo "Container: $container"
                kubectl logs -n$NAMESPACE ${podsCrashLoopBackoff[$i]} $container --previous | tail -n 15
                echo ""
            done
        done
    else
        echo "....No CrashLoopBackOff pods found"
        testPassed $testName
    fi
}

# In case of none of the above issues, look for containers that aren't ready and print their logs
checkContainersNotReady() {
    echo "..Checking for containers that are not ready"
    testName="Not Ready Containers Check"
    pods=($(kubectl get pods -n$NAMESPACE -l release=$RELEASENAME -oname ))
    foundBadContainers=false
    for pod in ${pods[@]}
    do
        name=$pod
        readyFraction=($(kubectl get $pod -n$NAMESPACE --no-headers | awk '{printf $2}' | sed -e 's/\// /g'))
        if [[ ${readyFraction[0]} != ${readyFraction[1]} ]]; then
            foundBadContainers=true
            badContainers=($(kubectl get $pod -n$NAMESPACE -o jsonpath='{.status.containerStatuses[?(@.ready==false)].name}'))
            for container in ${badContainers[@]}
            do
                echo "!!--Found container $container in pod $pod to be in an non ready state"
                echo "!!--Printing the end of the logs for these containers"
                kubectl logs -n$NAMESPACE $pod $container | tail -n 15
                echo ""
            done
        fi
    done
    if $foundBadContainers
    then
        testFailed $testName
    else
        echo "....All the containers are ready"
        testPassed $testName
    fi
}

testFailed() {
    if [[ -z "$1" ]]
    then
        echo "Unable to add failing test to summary as test name has not been supplied"
    else
        tests=("${tests[@]}" "$*:failed  **")
    fi
    foundIssue=true
}

testPassed() {
    if [[ -z "$1" ]]
    then
        echo "Unable to add successful test to summary as test name has not been supplied"
    else
        tests=("${tests[@]}" "$*:passed")
    fi
  }

printUsage() {
    echo "This script attempts to provide concise issues which would cause your install to fail."
    echo " "
    echo "installation-diagnostic-script [-n namespace] [-r releasename] [options]"
    echo " "
    echo "options"
    echo "--help, -h            Show brief help"
    echo "--namespace, -n       Provide the namespace for the install"
    echo "--releasename, -r     Provide the release name for the install"
    echo "--restartoldpods      Restart pods that are in pending state and more than an hour old to regenerate events"
}

###########################################################
# beginning of script
###########################################################
NAMESPACE=''
RELEASENAME=''
foundIssue=false
RESTARTOLDPODS=false

while [ ! $# -eq 0 ]
do
	case "$1" in
		--namespace | -n)
            shift
			NAMESPACE=$1
			shift
			;;
		--releasename | -r)
			shift
            RELEASENAME=$1
			shift
			;;
        --restartoldpods)
            shift
            RESTARTOLDPODS=true
            ;;
        --help | -h)
            shift
            printUsage
            A 0
            ;;
        *)
            shift
            echo "Incorrect usage!"
            echo " "
            printUsage
            exit 0
            ;;
	esac
done

if [ -z ${NAMESPACE} ]; then
    read -p "Namespace : " NAMESPACE
fi

if [ -z ${RELEASENAME} ]; then
    read -p "Chart releasename : " RELEASENAME
fi

MASTER_IP=`kubectl get cm ibmcloud-cluster-info -o=jsonpath="{.data.cluster_address}" -n kube-public 2>/dev/null`
if [ ! -z "$MASTER_IP" ]; then
    CLUSTER_PORT=`kubectl get cm ibmcloud-cluster-info -o=jsonpath="{.data.cluster_router_https_port}" -n kube-public`
else
    MASTER_IP=`kubectl get configmap oauth-client-map  -n services -o jsonpath="{.data.MASTER_IP}"`
    IDENTITY_URL=`kubectl get configmap platform-api -nkube-system -o jsonpath="{.data.CLUSTER_EXTERNAL_URL}"`
    CLUSTER_PORT=${IDENTITY_URL##*:};
fi

# Attempt to get the user token from cloudctl. If we don't have it then warn that we can't do all the security checks.
# We can get the ID token from kubectl if we have to, but this will limit the number of checks.
USER_ACCESS_TOKEN=$(cloudctl tokens --access 2>/dev/null)
if [[ -z $USER_ACCESS_TOKEN ]]
    then
        echo "cloudctl executable is not accessible on path. Some security checks will not be performed. Please add the directory containing the cloudctl executable to your PATH, and
        rerun script if you want the extra security diagnostics"
    else
        USER_ID_TOKEN=`cloudctl tokens --access 2>/dev/null`
fi

if [[ -z $USER_ID_TOKEN ]]; then
    USER_ID_TOKEN=`kubectl config view --minify=true -o=jsonpath='{.users[0].user.token}'`
fi

CLUSTER_NAME=`kubectl config view --minify=true -o=jsonpath='{.contexts[0].context.cluster}'`
echo ""
echo "-------------------------------"
echo "Printing release candidate tag"
kubectl get deployment -n "${NAMESPACE}" "${RELEASENAME}-ibm-es-access-controller-deploy" -o=jsonpath='{.spec.template.metadata.annotations.releaseCandidate}'
echo ""
echo "-------------------------------"
echo "Printing current state "
kubectl get pods -n$NAMESPACE
echo "-------------------------------"
echo "Starting release diagnostics"
checkReleasenameAndNamespaceValid

checkKafkaZookeeperPodsPresent

checkRegisteredOAuthEndpoints

getCurrentUserRole

checkIamSecretApiKeyExists

checkIamSecretServiceID

checkSecurityRoleMappings

checkAccessControllerLogs

checkPodsInPendingState

checkPodsInFailedState

checkPodsInCrashLoopBackOff

if ! $foundIssue; then
    checkContainersNotReady
fi

echo ""
echo ""
echo ""
echo "======================================================="
echo "                    Test Summary"
echo "-------------------------------------------------------"

for test in "${tests[@]}"
do
    printf -v pad %40s
    testName=${test%:*}$pad
    testName=${testName:0:40}
    testResult=${test##*:}
    echo "  $testName$testResult"
done
echo "======================================================="


if $foundIssue; then
    echo -e "Problems found during release diagnostics check. Please review output to identify potential problems.\nIf unable to identify or fix problems, please contact support."
else
    echo -e "Release diagnostics complete. No issues found.\nIf problems continue, please contact support"
fi
