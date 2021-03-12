#!/bin/bash
#
# Licensed Materials - Property of IBM
#
# 5737-H33
#
# (C) Copyright IBM Corp. 2020  All Rights Reserved.
#
# US Government Users Restricted Rights - Use, duplication or
# disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
#

PROGRAM_NAME="${0}"
VERSION="2020.2.6"
DATE=`date +%d-%m-%y`
TIME=`date +%H-%M-%S`

####################################################################################################
# Script helper functions
####################################################################################################

usage() {
    echo "${PROGRAM_NAME} v${VERSION}"
    echo ""
    echo "This script collects the log files from available pods and tars them up."
    echo "It uses the component label names instead of the pod names as the pod names could be truncated."
    echo ""
    echo "usage: ${PROGRAM_NAME} [-h] [-n|-ns|-namespace=NAMESPACE] [-r|-rel|-release=RELEASE]"
    echo "  -h                       display help"
    echo "  -n | -ns  | -namespace   specify the required NAMESPACE"
    echo "  -r | -rel | -release     specify the required RELEASE"
    echo "  -s | -since              specify how many hours back the logs should go (default 120)"
    echo ""
    echo "Pre-conditions:"
    echo "  1) kubectl/oc must be installed"
    echo "  2) (preferred) helm & openssl installed"
    echo "  3) User must be logged into host as cluster-admin"
    echo "  4) User must know the namespace that Event Streams is deployed in, and the name of that release"
    echo ""
}

# prints the incoming to the terminal and writes it to the output.log
printAndLog () {
    tee -a "${LOGDIR}/output.log"
}

# only print valid printable characters
cleanOutput () {
    tr -cd '\11\12\15\40-\176'
}

TPUT_PRESENCE=$(command -v tput > /dev/null; echo ${?})

# print the supplied text in red by setting the forground colour to red then unsetting it
printRed () {
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput setaf 1
    echo "${@}"
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput sgr0
}

# print the supplied text in green
printGreen () {
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput setaf 2
    echo "${@}"
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput sgr0
}

# print the supplied text in yellow
printYellow () {
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput setaf 3
    echo "${@}"
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput sgr0
}

# print the supplied text in red and write it to the log, note `printRed blah | printAndLog` will give formatting problems in the log
printRedAndLog () {
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput setaf 1
    echo "${@}" | printAndLog
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput sgr0
}

# print the supplied text in green and write it to the log
printGreenAndLog () {
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput setaf 2
    echo "${@}" | printAndLog
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput sgr0
}

# print the supplied text in yellow and write it to the log
printYellowAndLog () {
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput setaf 3
    echo "${@}" | printAndLog
    [ "${TPUT_PRESENCE}" -eq 0 ] && tput sgr0
}

# print the standard green done message
printDone () {
    printGreen "    [DONE]"
}

# print the standard green done message and write it to the log
printDoneAndLog () {
    printGreenAndLog "    [DONE]"
}

####################################################################################################
# Operator Configuration
####################################################################################################

declare -a OPERATOR_RESOURCES=(
    "client"
    "configmaps"
    "deployments"
    "networkpolicies"
    "persistentvolumeclaims"
    "poddisruptionbudgets"
    "replicasets"
    "rolebindings"
	"roles"
    "routes"
    "secrets"
    "services"
    "statefulsets"
)

declare -a OPERATOR_GLOBAL_RESOURCES=(
    "nodes"
    "persistentvolumes"
    "storageclasses"
)

declare -a OPERATOR_COMPONENT_LABELS=(
    "app.kubernetes.io/name=admin-api"
    "app.kubernetes.io/name=admin-ui"
    "app.kubernetes.io/name=entity-operator"
    "app.kubernetes.io/name=kafka"
    "app.kubernetes.io/name=kafka-mirror-maker-2"
    "app.kubernetes.io/name=metrics"
    "app.kubernetes.io/name=rest-producer"
    "app.kubernetes.io/name=schema-registry"
    "app.kubernetes.io/name=zookeeper"
)

declare -a OPERATOR_PERSISTENT_COMPONENT_LABELS=(
    "app.kubernetes.io/name=kafka"
    "app.kubernetes.io/name=schema-registry"
    "app.kubernetes.io/name=zookeeper"
)

declare -a OPERATOR_SUPPORTING_COMPONENTS_LABELS=(
    "component=auth-idp"
    "component=auth-pap"
    "component=auth-pdp"
)

declare -a OPERATOR_CERT_SECRETS=(
    "ibm-es-admapi-cert"
    "ibm-es-recapi-cert"
    "ibm-es-schema-cert"
    "ibm-es-metrics-cert"
    "cluster-ca-cert"
    "kafka-brokers"
)

declare -a OPERATOR_CRDS=(
    "eventstreams.eventstreams.ibm.com"
    "eventstreamsgeoreplicators.eventstreams.ibm.com"
    "kafkaconnectors.eventstreams.ibm.com"
    "kafkaconnects.eventstreams.ibm.com"
    "kafkaconnects2is.eventstreams.ibm.com"
    "kafkamirrormaker2s.eventstreams.ibm.com"
    "kafkarebalances.eventstreams.ibm.com"
    "kafkas.eventstreams.ibm.com"
    "kafkatopics.eventstreams.ibm.com"
    "kafkausers.eventstreams.ibm.com"
)

####################################################################################################
# Helm Configuration
####################################################################################################

declare -a HELM_RESOURCES=(
    "networkpolicies"
    "services"
    "persistentvolumeclaims"
    "configmaps"
    "statefulsets"
    "deployments"
    "secrets"
    "routes"
)

declare -a HELM_GLOBAL_RESOURCES=(
    "nodes"
    "persistentvolumes"
    "storageclasses"
)

declare -a HELM_COMPONENT_LABELS=(
    "component=collector"
    "component=elastic"
    "component=essential"
    "component=indexmgr"
    "component=kafka"
    "component=proxy"
    "component=replicator"
    "component=rest"
    "component=restproducer"
    "component=restproxy"
    "component=schemaregistry"
    "component=security"
    "component=ui"
    "component=zookeeper"
)

declare -a HELM_PERSISTENT_COMPONENT_LABELS=(
    "serviceSelector=kafka-sts"
    "serviceSelector=schemaregistry-sts"
    "serviceSelector=zookeeper-sts"
)

declare -a HELM_EXTERNAL_ENDPOINTS_SERVICES=(
    "ibm-es-ui-svc"
    "ibm-es-rest-proxy-external-svc"
    "ibm-es-proxy-svc"
)

declare -a HELM_SUPPORTING_COMPONENTS_LABELS=(
    "component=auth-idp"
    "component=auth-pap"
    "component=auth-pdp"
    "app=helm"
    "app=kube-dns"
)

declare -a HELM_CERT_SECRETS=(
    "ibm-es-proxy-secret"
)

####################################################################################################
# Check user input and system capabilities required to run
####################################################################################################

unset NAMESPACE
unset RELEASE
unset SINCE
unset BUNDLED
BUNDLED=1
SINCE=120

# Read in the required parameters. the releases name and namespace
while [ "${#}" -gt 0 ]; do
    arg="${1}"
    case "${1}" in
        -*"="*) shift; set - "${arg%%=*}" "${arg#*=}" "${@}"; continue;;
        -n|-ns|-namespace) shift; NAMESPACE="${1}";;
        -r|-rel|-release) shift; RELEASE="${1}";;
        -s|-since) shift; SINCE="${1}";;
        -bundled) BUNDLED=0;; # This parameter skips steps when bundled inside oc adm must-gather
        -h) usage;;
        *) break;;
    esac
    shift
done

# Assert that the required fields were supplied
if [ -z "${NAMESPACE}" ] || [ -z "${RELEASE}" ]; then
    printRed "Both the namespace and release name must be specified to run this script."
    printRed "Please re-run the script with these required arguements."
    printRed "Example:"
    printRed "  ./get-logs.sh -n=myNamespace -r=myRelease"
    echo ""
    usage
    exit 1
fi

if [ "${BUNDLED}" -eq 0 ]; then
    echo "INFO: gather_eventstreams_instance collecting ..."

    # import global env
    . env

    LOGDIR="${BASE_COLLECTION_PATH}/eventstreams-${RELEASE}/"
    mkdir -p "${LOGDIR}"
    EXE=oc
else
    # Check to see if the log output directory can be created
    LOGDIR="es-diagnostics-${DATE}_${TIME}"
    INVOCATION_DIR=$(pwd)
    if [ -d "${INVOCATION_DIR}/${LOGDIR}" ]; then
        printYellow "The output directory \"${LOGDIR}\" already exists!"
        printYellow "Please rename or delete this directory and re-run."
        exit 1
    fi

    # Check to see if kubectl or oc is available, prefer oc over kubectl
    EXE=oc
    echo -n "Checking for presence of kubectl or oc"
    OC_PRESENCE=$(command -v "${EXE}" > /dev/null; echo ${?})
    if [ "${OC_PRESENCE}" -ne 0 ]; then
        EXE=kubectl
        KUBECTL_PRESENCE=$(command -v "${EXE}" > /dev/null; echo ${?})
        if [ "${KUBECTL_PRESENCE}" -ne 0 ]; then
            printRed 'You must have kubectl or oc installed to run diagnostics'
            exit 1
        fi
    fi
    printDone
    
    # Check that the user running the script is a cluster-admin
    echo -n "Checking authorization"
    ${EXE} auth can-i describe nodes > /dev/null
    AUTHORIZED=$(echo ${?})
    if [ "${AUTHORIZED}" -ne 0 ]; then
        printRed 'You must be logged in as cluster-admin to run diagnostics'
        exit 1
    fi
    printDone

    # make the output directory so we can start storing information using printAndLog
    mkdir "${LOGDIR}"
fi

####################################################################################################
# Check user input and system capabilities that are optional
####################################################################################################

if [ "${BUNDLED}" -eq 0 ]; then
    OPENSSL_PRESENCE=0
    IS_OPERATOR_RELEASE=0
    RELEASE_LABEL="eventstreams.ibm.com/cluster=${RELEASE}"
    SUPPORTING_COMPONENTS_NAMESPACE="ibm-common-services"
    RESOURCES=("${OPERATOR_RESOURCES[@]}")
    GLOBAL_RESOURCES=("${OPERATOR_GLOBAL_RESOURCES[@]}")
    COMPONENT_LABELS=("${OPERATOR_COMPONENT_LABELS[@]}")
    PERSISTENT_COMPONENT_LABELS=("${OPERATOR_PERSISTENT_COMPONENT_LABELS[@]}")
    EXTERNAL_ENDPOINTS_SERVICES=("${OPERATOR_EXTERNAL_ENDPOINTS_SERVICES[@]}")
    CERT_SECRETS=("${OPERATOR_CERT_SECRETS[@]}")
    SUPPORTING_COMPONENTS_LABELS=("${OPERATOR_SUPPORTING_COMPONENTS_LABELS[@]}")
    CRDS=("${OPERATOR_CRDS[@]}")
else
    # Check to see if the user has openssl available
    echo -n "Checking for presence of openssl" | printAndLog
    command -v openssl > /dev/null
    OPENSSL_PRESENCE=$(echo ${?})
    if [ "${OPENSSL_PRESENCE}" -ne 0 ]; then
        printYellowAndLog '  openssl is desirable for diagnostics but absent on this system - continuing...    [SKIP]'
    else
        printDoneAndLog
    fi

    # Check to see if referenced release is operator or helm and setting up accordingly
    echo -n "Checking if release is from operator" | printAndLog
    IS_OPERATOR_RELEASE=$(${EXE} get es -n ${NAMESPACE} ${RELEASE} &> /dev/null; echo ${?})
    if [ "${IS_OPERATOR_RELEASE}" -eq 0 ]; then
        RELEASE_LABEL="eventstreams.ibm.com/cluster=${RELEASE}"
        SUPPORTING_COMPONENTS_NAMESPACE="ibm-common-services"
        RESOURCES=("${OPERATOR_RESOURCES[@]}")
        GLOBAL_RESOURCES=("${OPERATOR_GLOBAL_RESOURCES[@]}")
        COMPONENT_LABELS=("${OPERATOR_COMPONENT_LABELS[@]}")
        PERSISTENT_COMPONENT_LABELS=("${OPERATOR_PERSISTENT_COMPONENT_LABELS[@]}")
        EXTERNAL_ENDPOINTS_SERVICES=("${OPERATOR_EXTERNAL_ENDPOINTS_SERVICES[@]}")
        CERT_SECRETS=("${OPERATOR_CERT_SECRETS[@]}")
        SUPPORTING_COMPONENTS_LABELS=("${OPERATOR_SUPPORTING_COMPONENTS_LABELS[@]}")
        CRDS=("${OPERATOR_CRDS[@]}")
    else
        RELEASE_LABEL="release=${RELEASE}"
        SUPPORTING_COMPONENTS_NAMESPACE="kube-system"
        RESOURCES=("${HELM_RESOURCES[@]}")
        GLOBAL_RESOURCES=("${HELM_GLOBAL_RESOURCES[@]}")
        COMPONENT_LABELS=("${HELM_COMPONENT_LABELS[@]}")
        PERSISTENT_COMPONENT_LABELS=("${HELM_PERSISTENT_COMPONENT_LABELS[@]}")
        EXTERNAL_ENDPOINTS_SERVICES=("${HELM_EXTERNAL_ENDPOINTS_SERVICES[@]}")
        CERT_SECRETS=("${HELM_CERT_SECRETS[@]}")
        SUPPORTING_COMPONENTS_LABELS=("${HELM_SUPPORTING_COMPONENTS_LABELS[@]}")
        CRDS=()
    fi
    printDoneAndLog
fi

####################################################################################################
# Final steps before starting
####################################################################################################

if [ "${BUNDLED}" -ne 0 ]; then
    # Print out a disclaimer about multiple runs potentially being required
    echo ""
    printYellow "******************************  WARNING  ******************************"
    printYellow "Due to the transient nature of the cluster - it may be necessary to run"
    printYellow "this script a second time if the final message 'COMPLETE' is not shown."
    printYellow "***********************************************************************"
    echo ""

    # Perpare and begin diasnostics collection
    echo "Diagnostics collection v${VERSION} started at ${DATE}_${TIME} for release: ${RELEASE} in namespace: ${NAMESPACE}" | printAndLog
fi

####################################################################################################
# Gathering helper functions
####################################################################################################

get_pod_logs () {
    NS="${1}"
    POD="${2}"
    PARAMS="${3}"
    POD_DIR="${LOGDIR}/${POD}"
    echo "Gathering diagnostics for pod: ${POD}" | printAndLog
    mkdir -p "${POD_DIR}"
    ${EXE} describe pod "${POD}" -n "${NS}" > "${POD_DIR}/pod-describe.log"
    CONTAINERS=$(${EXE} get pod ${POD} -n ${NS} -o jsonpath="{.spec.containers[*].name}" | cleanOutput)
    INIT_CONTAINERS=$(${EXE} get pod ${POD} -n ${NS} -o jsonpath="{.spec.initContainers[*].name}" | cleanOutput)
    JOB_NAME=$(${EXE} get pod ${POD} -n ${NS} -o jsonpath="{.metadata.ownerReferences[?(@.kind == 'Job')].name}" | cleanOutput)
    if [ "${JOB_NAME}" ]; then
        echo -n "  Gathering Job logs" | printAndLog
        ${EXE} logs "${POD}" -n "${NS}" --since="${SINCE}h" ${PARAMS} > "${POD_DIR}/${JOB_NAME}.log"
        printDoneAndLog
    else
        for CONTAINER in ${CONTAINERS[@]}; do
            echo "  Gathering diagnostics for container: ${CONTAINER}" | printAndLog
            get_container_diagnostics "${NS}" "${POD}" "${CONTAINER}" "${POD_DIR}"
            get_container_logs "${NS}" "${POD}" "${CONTAINER}" "${POD_DIR}" "${PARAMS}"
        done
        for CONTAINER in ${INIT_CONTAINERS[@]}; do
            echo "  Gathering diagnostics for init container: ${CONTAINER}" | printAndLog
            get_init_container_logs "${NS}" "${POD}" "${CONTAINER}" "${POD_DIR}" "${PARAMS}"
        done
    fi
}

get_container_diagnostics () {
    NS="${1}"
    POD="${2}"
    CONTAINER="${3}"
    DIR="${4}"
    PHASE=$(${EXE} get pod ${POD} -n ${NS} -o jsonpath="{.status.phase}" | cleanOutput )
    if [ "${PHASE}" == "Running" ]; then
        if [ "${IS_OPERATOR_RELEASE}" -eq 0 ]; then
            echo -n "    Retrieving image name" | printAndLog
            IMAGE=$(${EXE} exec ${POD} -n ${NS} -c ${CONTAINER} -i -- sh -c "if [ -s \"/image.txt\" ]; then cat \"/image.txt\"; else echo -n notPresent; fi" | cleanOutput)
            echo "Pod: ${POD}, Container: ${CONTAINER}, Image: ${IMAGE}" >> "${DIR}/images.log"
            printDoneAndLog
        fi
        if [ ! -s "${DIR}/etc_hosts.log" ]; then
            echo -n "    Retrieving hosts file" | printAndLog
            ${EXE} exec "${POD}" -n "${NS}" -c "${CONTAINER}" -i -- sh -c "cat /etc/hosts" > "${DIR}/etc_hosts.log"
            printDoneAndLog
        fi
        if [ ! -s ${DIR}/resolv_conf.log ]; then
            echo -n "    Retrieving resolv.conf file" | printAndLog
            ${EXE} exec "${POD}" -n "${NS}" -c "${CONTAINER}" -i -- sh -c "cat /etc/resolv.conf" > "${DIR}/resolv_conf.log"
            printDoneAndLog
        fi
    fi
}

get_container_logs () {
    NS="${1}"
    POD="${2}"
    CONTAINER="${3}"
    DIR="${4}"
    PARAMS="${5}"
    echo -n "    Gathering container logs" | printAndLog
    ${EXE} logs "${POD}" -n "${NS}" -c "${CONTAINER}" --since="${SINCE}h" ${PARAMS} > "${DIR}/container_log-${CONTAINER}.log"
    printDoneAndLog
    RESTART_COUNT=$(${EXE} get pod ${POD} -n ${NS} -o jsonpath="{.status.containerStatuses[?(@.name == \"${CONTAINER}\")].restartCount}" | cleanOutput)
    if [ "${RESTART_COUNT}" -ne 0 ]; then
        echo -n "    Gathering previous container logs" | printAndLog
        ${EXE} logs "${POD}" -n "${NS}" -c "${CONTAINER}" --previous --limit-bytes=10000000 ${PARAMS} > "${DIR}/previous_container_log-${CONTAINER}.log"
        printDoneAndLog
    fi
}

get_init_container_logs () {
    NS="${1}"
    POD="${2}"
    CONTAINER="${3}"
    DIR="${4}"
    PARAMS="${5}"
    echo -n "    Gathering init container logs" | printAndLog
    ${EXE} logs "${POD}" -n "${NS}" -c "${CONTAINER}" --since="${SINCE}h" ${PARAMS} > "${DIR}/init_container_log-${CONTAINER}.log"
    printDoneAndLog
    RESTART_COUNT=$(${EXE} get pod ${POD} -n ${NS} -o jsonpath="{.status.initContainerStatuses[?(@.name == \"${CONTAINER}\")].restartCount}" | cleanOutput)
    if [ "${RESTART_COUNT}" -ne 0 ]; then
        echo -n "    Gathering previous init container logs" | printAndLog
        ${EXE} logs "${POD}" -n "${NS}" -c "${CONTAINER}" --previous --limit-bytes=10000000 ${PARAMS} > "${DIR}/previous_init_container_log-${CONTAINER}.log"
        printDoneAndLog
    fi
}

analyse_cert () {
    DIR="${1}"
    NAME="${2}"
    if [ -s "${DIR}/${NAME}" ]; then
        # Attempt decode
        echo -n "  Decode certificate: ${NAME}" | printAndLog
        base64 --decode "${DIR}/${NAME}" > "${DIR}/decoded-${NAME}"
        printDoneAndLog
        if [ "${OPENSSL_PRESENCE}" -eq 0 ]; then
            # Attempt openssl
            echo -n "  Inspect certificate: ${NAME}" | printAndLog
            openssl x509 -text -in "${DIR}/decoded-${NAME}" > "${DIR}/openssl-${NAME}"
            printDoneAndLog
        else
            printYellowAndLog 'openssl not available on this system - skipping certificate inspection...     [SKIP]'
        fi
    else
        echo "No Certicate at ${DIR}/${NAME}, removing"
        rm -f "${DIR}/${NAME}"
    fi
}

get_external_endpoint_cert () {
    ADDRESS="${1}"
    OUTPUT_FILE="${2}"
    if [ "${OPENSSL_PRESENCE}" -eq 0 ]; then
        echo -n "  Get presented certificate at endpoint: ${ADDRESS}" | printAndLog
        CONNECT_RC=$(echo -n | openssl s_client -connect ${ADDRESS} -servername ${ADDRESS} &> /dev/null; echo ${?})
        if [ "${CONNECT_RC}" -eq 0 ]; then
            echo -n | openssl s_client -connect "${ADDRESS}" -servername "${ADDRESS}" &> "${OUTPUT_FILE}"
            printDoneAndLog
        else
            printRedAndLog '     [ERR]'
        fi
    else
        printYellowAndLog 'openssl not available on this system - skipping endpoint certificate discovery...     [SKIP]'
    fi
}

check_pvc () {
    NS="${1}"
    PVC="${2}"
    PHASE=$(${EXE} get pvc -n ${NS} ${PVC} --no-headers -o=jsonpath="{.status.phase}" | cleanOutput)
    VOLUME=$(${EXE} get pvc -n ${NS} ${PVC} --no-headers -o=jsonpath="{.spec.volumeName}" | cleanOutput)
    VOLUME_EXISTS=$(${EXE} get pv ${VOLUME} --no-headers &> /dev/null; echo ${?})
    if [ "${PHASE}" != "Bound" ]; then
        printRedAndLog "  Persistence problem for pvc: ${PVC} phase is not Bound, is: ${PHASE}...    [ERR]"
    elif [ "${VOLUME_EXISTS}" -ne 0 ]; then
        printRedAndLog "  Persistence problem for pvc: ${PVC} bound to volume ${VOLUME} which does not exist...    [ERR]"
    else
        printGreenAndLog "  Persistence is okay for pvc: ${PVC}"
    fi
}

####################################################################################################
# Gather logs/descriptions/manifests for the namespace
####################################################################################################

# Gather information about the namespaces as a whole - pods etc.
echo -n "Gathering namespace overview information" | printAndLog
${EXE} get pods -n "${NAMESPACE}" -o wide -L zone > "${LOGDIR}/es-pods.log"
${EXE} get pods -n "${NAMESPACE}" -o json > "${LOGDIR}/es-pods-json.json"
printDoneAndLog

####################################################################################################
# Run simple diagnostics against the release
####################################################################################################

# Check to see if persistence is enabled and if it is (pvcs exist) that they are bound to an existing pv
for LABEL in ${PERSISTENT_COMPONENT_LABELS[@]}; do
    echo "Checking for persistence for ${LABEL}" | printAndLog
    PODS=$(${EXE} get pods -n ${NAMESPACE} -l "${RELEASE_LABEL},${LABEL}" --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    TEMP_ARR=(${PODS[@]})
    NUM_PODS=${#TEMP_ARR[@]}
    PVCS=$(${EXE} get pvc -n ${NAMESPACE} -l "${RELEASE_LABEL},${LABEL}" --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    TEMP_ARR=(${PVCS[@]})
    NUM_PVCS=${#TEMP_ARR[@]}
    if [ "${NUM_PVCS}" -eq 0 ]; then
        printYellowAndLog "Persistence not enabled for ${LABEL}"
    elif [ "${NUM_PODS}" -eq "${NUM_PVCS}" ]; then
        for PVC in ${PVCS[@]}; do
            echo "  Checking pvc ${PVC} for ${LABEL}" | printAndLog
            check_pvc "${NAMESPACE}" "${PVC}"
        done
    elif [ "${NUM_PVCS}" -eq 1 ]; then
        ACCESS_MODE=$(${EXE} get pvc -n ${NAMESPACE} ${PVCS[0]} --no-headers -o=jsonpath="{.spec.accessModes}" | cleanOutput)
        if [ "${ACCESS_MODE}" == "[ReadWriteMany]" ]; then
            echo "  Checking pvc ${PVCS[0]} for ${LABEL}" | printAndLog
            check_pvc "${NAMESPACE}" "${PVCS[0]}"
        else
            printRedAndLog "Persistence problem for ${LABEL}, there are ${NUM_PODS} pods with 1 ${ACCESS_MODE} pvc...    [ERR]"
        fi
    else
        printRedAndLog "Persistence problem for ${LABEL}, there are ${NUM_PODS} pods and ${NUM_PVCS} pvcs...    [ERR]"
    fi
done

# Check to see if zookeepers are okay, which is the leader, and that they can communicate (operator only!)
ZK_PODS=$(${EXE} get pods -n ${NAMESPACE} -l "${RELEASE_LABEL},app.kubernetes.io/name=zookeeper" --no-headers -o custom-columns=":metadata.name" | cleanOutput)
for POD in ${ZK_PODS[@]}; do
    echo "Checking zookeeper pod ${POD} status" | printAndLog
    OKAY=$(${EXE} exec "${POD}" -n "${NAMESPACE}" -c "zookeeper" -i -- sh -c "echo ruok | nc localhost 12181" | cleanOutput)
    MODE=$(${EXE} exec "${POD}" -n "${NAMESPACE}" -c "zookeeper" -i -- sh -c "echo srvr | nc localhost 12181 | grep Mode" | cleanOutput)
    if [ "${OKAY}" == "imok" ]; then
        printYellowAndLog "  zookeeper pod ${POD} is in ${MODE}"
    else
        printRedAndLog "  zookeeper pod ${POD} is not okay, state ${OKAY}"
    fi

    echo "  checking zookeeper to zookeeper connections" | printAndLog
    for TEST_POD in ${ZK_PODS[@]}; do
        echo -n "    connection test from ${POD} to ${TEST_POD}" | printAndLog
        # This is not the ideal but it's the only thing that worked
        CMD="
        echo -n \"Run \"
        OUT=\$(curl -Ss -k --cert /opt/kafka/zookeeper-node-certs/${POD}.crt --key /opt/kafka/zookeeper-node-certs/${POD}.key https://${TEST_POD}.${RELEASE}-zookeeper-nodes.${NAMESPACE}.svc:3888 2>&1)
        if [ \"\${OUT}\" == \"curl: (52) Empty reply from server\" ]; then
            echo -n \"Succeeded\"
        elif [ \"\${OUT}\" == \"curl: (35) SSL peer had some unspecified issue with the certificate it received.\" ]; then
            echo -n \"Intermittent\"
        else
            echo -n \"Failed: \${OUT}\"
        fi
        "
        RESPONSE=$(${EXE} exec "${POD}" -n "${NAMESPACE}" -c "zookeeper" -i -- sh -c "${CMD}" | cleanOutput)
        if [ "${RESPONSE}" == "Run Succeeded" ]; then
            printDoneAndLog
        elif [ "${RESPONSE}" == "Run Intermittent" ]; then
            # This is an intermittent error that was seen during testing. logs of the failure can be seen in the addressed ZK's logs. They
            # indicate the failure was due to a random DNS resoltion blip
            printDoneAndLog
            printYellowAndLog "    saw intermittent error: 'curl: (35) SSL peer had some unspecified issue with the certificate it received.'"
            printYellowAndLog "    this will produce a stack trace in the addressed zookeepers logs indicating an ssl failure due to hostname'"
            printYellowAndLog "    verification'"
        elif [ "${RESPONSE}" == "Run " ]; then
            # This is an intermittent error that was seen during testing. It occurs when the curl command fails to run for some unknown
            # reason and causes the command to exit prematurely. It does not indicate a connection failure
            printDoneAndLog
            printYellowAndLog "    saw an intermittent error indicating curl failed to run abd the connection was not tested'"
        else
            printRedAndLog "    [ERR]"
            printRedAndLog "    connection test from ${POD} to ${TEST_POD} failed with response: ${RESPONSE}"
        fi
    done
done

# Check to see if the kafka pods can reach the zookeepers (operator only!)
KAFKA_PODS=$(${EXE} get pods -n ${NAMESPACE} -l "${RELEASE_LABEL},app.kubernetes.io/name=kafka" --no-headers -o custom-columns=":metadata.name" | cleanOutput)
for KAFKA_POD in ${KAFKA_PODS[@]}; do
    echo "Checking ${KAFKA_POD} to zookeeper connections" | printAndLog
    # Kafka connects to ZK via the tls-sidecar on localhost 2181. This connects it to a random ZK so re-rerun a couple of times
    # to look for interesting behaviour
    for i in 1 2 3; do
        echo -n "  checking random connection run ${i}" | printAndLog
        OKAY=$(${EXE} exec "${KAFKA_POD}" -n "${NAMESPACE}" -c "kafka" -i -- sh -c "echo ruok | nc localhost 2181" | cleanOutput)
        if [ "${OKAY}" == "imok" ]; then
            printDoneAndLog
        else
            printRedAndLog "    [ERR]"
            printRedAndLog "  connection to ${ZK_POD} failed, response: ${OKAY}"
        fi
    done
    echo "  checking Kafka tls sidecar to zookeeper connections" | printAndLog
    for ZK_POD in ${ZK_PODS[@]}; do
        echo -n "    connection test from ${KAFKA_POD} sidecar to ${ZK_POD}" | printAndLog
        OKAY=$(${EXE} exec "${KAFKA_POD}" -n "${NAMESPACE}" -c "tls-sidecar" -i -- sh -c "echo ruok | nc --ssl-cert /etc/tls-sidecar/kafka-brokers/${KAFKA_POD}.crt --ssl-key /etc/tls-sidecar/kafka-brokers/${KAFKA_POD}.key ${ZK_POD}.${RELEASE}-zookeeper-nodes.${NAMESPACE}.svc 2181" | cleanOutput)
        if [ "${OKAY}" == "imok" ]; then
            printDoneAndLog
        else
            printRedAndLog "    [ERR]"
            printRedAndLog "  connection to ${ZK_POD} failed, response: ${OKAY}"
        fi
    done
done

####################################################################################################
# Gather logs/descriptions/manifests for the desired release
####################################################################################################

echo "Gathering operator pod logs" | printAndLog
NAMESPACE_OPERATOR_PODS=$(${EXE} get pods -n ${NAMESPACE} -l app.kubernetes.io/name=eventstreams-operator -l eventstreams.ibm.com/kind=cluster-operator --no-headers -o custom-columns=":metadata.name" | cleanOutput)
for POD in ${NAMESPACE_OPERATOR_PODS[@]}; do
    get_pod_logs "${NAMESPACE}" "${POD}"
done

GLOBAL_OPERATOR_PODS=$(${EXE} get pods -n openshift-operators -l app.kubernetes.io/name=eventstreams-operator -l eventstreams.ibm.com/kind=cluster-operator --no-headers -o custom-columns=":metadata.name" | cleanOutput)
for POD in ${GLOBAL_OPERATOR_PODS[@]}; do
    get_pod_logs "openshift-operators" "${POD}"
done

# Gather container logs/descriptions/manifests for ES components
for COMPONENT_LABEL in ${COMPONENT_LABELS[@]}; do
    PODS=$(${EXE} get pods -n ${NAMESPACE} -l "${RELEASE_LABEL},${COMPONENT_LABEL}" --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    for POD in ${PODS[@]}; do
        get_pod_logs "${NAMESPACE}" "${POD}"
    done
done

# Gather CRDs and CRs for ES components
CRD_DIR="${LOGDIR}/crds"
CR_DIR="${LOGDIR}/crs"
mkdir -p "${CRD_DIR}"
mkdir -p "${CR_DIR}"
echo "Gathering crds and crs" | printAndLog
for CRD in ${CRDS[@]}; do
    echo -n "Gathering crd ${CRD}" | printAndLog
    ${EXE} get crd ${CRD} -o yaml > "${CRD_DIR}/${CRD}.yaml"
    printDoneAndLog
    CRS=$(${EXE} get ${CRD} -n ${NAMESPACE} --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    for CR in ${CRS[@]}; do
        echo -n "Gathering ${CRD} instance - ${CR}" | printAndLog
        ${EXE} get ${CRD} ${CR} -n ${NAMESPACE} -o yaml > "${CR_DIR}/${CRD}-${CR}.yaml"
        printDoneAndLog
    done
done

# Gather descriptions and manifests for non-pod resources
for RESOURCE in ${RESOURCES[@]}; do
    RESOURCE_DIR="${LOGDIR}/${RESOURCE}"
    mkdir -p "${RESOURCE_DIR}"

    ${EXE} get "${RESOURCE}" -n "${NAMESPACE}" -l "${RELEASE_LABEL}" -o wide > "${RESOURCE_DIR}/${RESOURCE}-get.log"
    ITEM_NAMES=$(${EXE} get ${RESOURCE} -n ${NAMESPACE} -l ${RELEASE_LABEL} --no-headers -o custom-columns=":metadata.name" | cleanOutput)

    for ITEM_NAME in ${ITEM_NAMES[@]}; do
        echo -n "Gathering diagnostics for ${RESOURCE}: ${ITEM_NAME}" | printAndLog
        ${EXE} describe "${RESOURCE}" "${ITEM_NAME}" -n "${NAMESPACE}" > "${RESOURCE_DIR}/${ITEM_NAME}-describe.log"
        if [ "${RESOURCE}" != "secrets" ]; then
            ${EXE} get "${RESOURCE}" "${ITEM_NAME}" -n "${NAMESPACE}" -o json > "${RESOURCE_DIR}/${ITEM_NAME}-json.json"
        fi
        printDoneAndLog
    done
done

# Gather descriptions and manifests for global resources
for RESOURCE in ${GLOBAL_RESOURCES[@]}; do
    RESOURCE_DIR="${LOGDIR}/${RESOURCE}"
    mkdir -p "${RESOURCE_DIR}"

    ${EXE} get "${RESOURCE}" -o wide > "${RESOURCE_DIR}/${RESOURCE}-get.log"
    ITEM_NAMES=$(${EXE} get ${RESOURCE} --no-headers -o custom-columns=":metadata.name" | cleanOutput)

    for ITEM_NAME in ${ITEM_NAMES[@]}; do
        echo -n "Gathering diagnostics for ${RESOURCE}: ${ITEM_NAME}" | printAndLog
        ${EXE} describe "${RESOURCE}" "${ITEM_NAME}" -n "${NAMESPACE}" > "${RESOURCE_DIR}/${ITEM_NAME}-describe.log"
        if [ "${RESOURCE}" != "secrets" ]; then
            ${EXE} get "${RESOURCE}" "${ITEM_NAME}" -n "${NAMESPACE}" -o json > "${RESOURCE_DIR}/${ITEM_NAME}-json.json"
        fi
        printDoneAndLog
    done
done

echo "Gathering certificates from secrets" | printAndLog
for SECRET in ${CERT_SECRETS[@]}; do
    SECRET_NAME="${RELEASE}-${SECRET}"
    CERT_DIR="${LOGDIR}/${SECRET_NAME}-certificates"
    mkdir -p "${CERT_DIR}"
    DATA_MAP=($(oc get secret -n ${NAMESPACE} ${SECRET_NAME} -o=jsonpath="{.data}" | cut -d '[' -f2 | tr -d '[]' | cleanOutput))
    for DATUM in ${DATA_MAP[@]}; do
        NAME=$(cut -d ':' -f1 <<< ${DATUM})
        VALUE=$(cut -d ':' -f2 <<< ${DATUM})
        case ${NAME} in
            *.crt|*.cert|*.cacert)
                echo -n "  Got encoded certificate: ${NAME}" | printAndLog
                echo "${VALUE}" > "${CERT_DIR}/${NAME}"
                printDoneAndLog
                analyse_cert "${CERT_DIR}" "${NAME}"
                ;;
            *)
                ;;
        esac
    done
done

HOST="localhost"
# determine the external hostname of cluster. Currently only routes for operator so doesn't matter
if [ "${IS_OPERATOR_RELEASE}" -ne 0 ]; then
    echo -n "Gathering external host address" | printAndLog
    PROXY_CM_MAP=$(${EXE} get cm -n ${NAMESPACE} -l ${RELEASE_LABEL} -l component=proxy --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    HOST=$(${EXE} get cm -n ${NAMESPACE} ${PROXY_CM_MAP} -o jsonpath="{.data.externalHostOrIP}" | cleanOutput)
    printDoneAndLog
fi

echo "Gathering certs from external services" | printAndLog
EXTERNAL_PRESENTED_CERTS_DIR="${LOGDIR}/presented-certificates-external"
mkdir -p "${EXTERNAL_PRESENTED_CERTS_DIR}"
for SERVICE in ${EXTERNAL_ENDPOINTS_SERVICES[@]}; do
    echo "Gathering connection details for endpoint: ${SERVICE}" | printAndLog
    SVC_TYPE=$(${EXE} get svc -n ${NAMESPACE} ${RELEASE}-${SERVICE} -o jsonpath="{.spec.type}" | cleanOutput)
    if [ "${SVC_TYPE}" == "NodePort" ]; then
        NODEPORTS=$(${EXE} get svc -n ${NAMESPACE} ${RELEASE}-${SERVICE} -o jsonpath="{.spec.ports[*].nodePort}" | cleanOutput)
        for NODEPORT in ${NODEPORTS[@]}; do
            get_external_endpoint_cert "${HOST}:${NODEPORT}" "${EXTERNAL_PRESENTED_CERTS_DIR}/${SERVICE}-${HOST}-${NODEPORT}.log"
        done
    fi
done

echo "Gathering certs from routes" | printAndLog
HAS_ROUTES=$(${EXE} api-resources | cleanOutput | grep route.openshift.io &> /dev/null; echo ${?})
if [ "${HAS_ROUTES}" -eq 0 ]; then
    ROUTES=$(${EXE} get routes -n ${NAMESPACE} -l ${RELEASE_LABEL} --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    for ROUTE in ${ROUTES[@]}; do
    echo "Gathering connection details for route ${ROUTE}" | printAndLog
    TLS_ENABLED=$(${EXE} get route -n ${NAMESPACE} ${ROUTE} -o jsonpath="{.spec.tls}" | cleanOutput)
    ADDRESS=$(${EXE} get route -n ${NAMESPACE} ${ROUTE} -o jsonpath="{.spec.host}" | cleanOutput)
    if [ "${TLS_ENABLED}" != "<none>" ]; then
        get_external_endpoint_cert "${ADDRESS}:443" "${EXTERNAL_PRESENTED_CERTS_DIR}/${ROUTE}.log"
    fi
    done
fi

# ####################################################################################################
# # Gather logs/descriptions/manifests for supporting elements. Helm, ICP, CS etc.
# ####################################################################################################

${EXE} get namespaces > "${LOGDIR}/namespaces.log"
${EXE} get pods -n ${SUPPORTING_COMPONENTS_NAMESPACE}  > "${LOGDIR}/${SUPPORTING_COMPONENTS_NAMESPACE}-pods.log"

echo -n "Gathering common services operator pod logs" | printAndLog
CS_OPERATOR_PODS_NAMESPACES=$(${EXE} get pods --all-namespaces -l app.kubernetes.io/name=ibm-common-service-operator --no-headers -o custom-columns=":metadata.namespace" | cleanOutput)
CS_OPERATOR_PODS_NAMES=$(${EXE} get pods --all-namespaces -l app.kubernetes.io/name=ibm-common-service-operator --no-headers -o custom-columns=":metadata.name" | cleanOutput)
TEMP_ARR=(${CS_OPERATOR_PODS_NAMES[@]})
LENGTH=${#TEMP_ARR[@]}
for (( i=0; i<${LENGTH}; i++ )); do
   get_pod_logs "${CS_OPERATOR_PODS_NAMESPACES[${i}]}" "${CS_OPERATOR_PODS_NAMES[${i}]}"
done

echo -n "Gathering kube-public configmap(s)" | printAndLog
CONFIGMAP_NAMES=$(${EXE} get configmaps -n kube-public --no-headers -o custom-columns=":metadata.name" | cleanOutput)
for CONFIGMAP in ${CONFIGMAP_NAMES[@]}; do
    ${EXE} describe configmap "${CONFIGMAP}" -n kube-public > "${LOGDIR}/${CONFIGMAP}-describe.log"
done
printDoneAndLog

echo "Gather etcd diagnostics if applicable" | printAndLog
ETCD_POD_NAMES=$(${EXE} get pods -n openshift-etcd --no-headers -o custom-columns=":metadata.name" | cleanOutput)
for ETCD_POD_NAME in ${ETCD_POD_NAMES[@]}; do
    get_pod_logs openshift-etcd "${ETCD_POD_NAME}" "--timestamps"
done

echo "Gather openshift-dns logs if applicable" | printAndLog
DNS_PODS=$(${EXE} get pods -n openshift-dns --no-headers -o custom-columns=":metadata.name" | cleanOutput)
for POD in ${DNS_PODS[@]}; do
    get_pod_logs openshift-dns "${POD}" "--timestamps"
done

echo "Gather supporting component logs if applicable" | printAndLog
for COMPONENT_LABEL in ${SUPPORTING_COMPONENTS_LABELS[@]}; do
    SUPPORTING_POD_NAMES=$(${EXE} get pods -n ${SUPPORTING_COMPONENTS_NAMESPACE} -l ${COMPONENT_LABEL} --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    for POD in ${SUPPORTING_POD_NAMES[@]}; do
        get_pod_logs ${SUPPORTING_COMPONENTS_NAMESPACE} "${POD}" "--timestamps"
    done
done

echo -n "Gathering events in all namespaces" | printAndLog
oc get events --all-namespaces -o wide > "${LOGDIR}/all-events.log"
printDoneAndLog

echo "Gather node logs if applicable" | printAndLog
if [ "${EXE}" == "oc" ]; then
    RESOURCE_DIR="${LOGDIR}/node-logs"
    mkdir -p "${RESOURCE_DIR}"
    ITEM_NAMES=$(${EXE} get nodes --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    for ITEM_NAME in ${ITEM_NAMES[@]}; do
        echo -n "Gathering logs for node: ${ITEM_NAME}" | printAndLog
        # Only get last 12 hours. oc adm node-logs is strange. If you specify too much time or no time. It will just cut out at somepoint
        # causing it to miss the most recent and arguably relevant logs. 12h is arbitrary but I have seen it complete with a healthy system
        oc adm node-logs "${ITEM_NAME}" --since=-12h > "${RESOURCE_DIR}/${ITEM_NAME}.log"
        printDoneAndLog
    done
fi

echo -n "Gather kubectl top diagnostics" | printAndLog
[ "${EXE}" == "oc" ] && TOP_COMMAND_DELTA="adm"
${EXE} ${TOP_COMMAND_DELTA} top nodes > "${LOGDIR}/nodes-resource-usage.log"
${EXE} ${TOP_COMMAND_DELTA} top pods -n ${SUPPORTING_COMPONENTS_NAMESPACE} -l app=icp-mongodb > "${LOGDIR}/mongodb-resource-usage.log"
printDoneAndLog

####################################################################################################
# Legacy gathering. Move legacy gathering under here
####################################################################################################

if [ "${IS_OPERATOR_RELEASE}" -ne 0 ]; then

    # If Helm is present collect the helm history and values for the release. If it is not continue to the next step
    HELM_PRESENCE=$(command -v helm > /dev/null; echo ${?})
    if [ "${HELM_PRESENCE}" -ne 0 ]; then
        printYellowAndLog '  Helm is desirable for diagnostics but absent on this system - continuing...    [SKIP]'
    else
        echo "Checking Helm client capability" | printAndLog
        HELM_OK=$(helm history ${RELEASE} --tls > /dev/null; echo ${?})
        if [ "${HELM_OK}" -ne 0 ]; then
            printRedAndLog 'Helm client is not able to comminicate with the cluster - continuing...    [ERR]'
        else
            echo -n "  Gathering Helm history" | printAndLog
            HELM_DIR="${LOGDIR}/helm"
            mkdir -p "${HELM_DIR}"
            helm history "${RELEASE}" --tls > "${HELM_DIR}/helm_history.log"
            printDoneAndLog

            echo -n "  Gathering Helm values" | printAndLog
            helm get values "${RELEASE}" --tls > "${HELM_DIR}/helm_values.log"
            CERT_VALUE_LINE=$(eval grep -n -w "cert:" ${HELM_DIR}/helm_values.log | cut -f1 -d:)
            KEY_VALUE_LINE=$(eval grep -n -w "key:" ${HELM_DIR}/helm_values.log | cut -f1 -d:)
            [ ! -z "${CERT_VALUE_LINE}" ] && (sed -i '' -e "${CERT_VALUE_LINE}s/.*/  cert: REDACTED/" "${HELM_DIR}/helm_values.log" || true )
            [ ! -z "${KEY_VALUE_LINE}" ] && (sed -i '' -e "${KEY_VALUE_LINE}s/.*/  key: REDACTED/" "${HELM_DIR}/helm_values.log" || true )
            printDoneAndLog
        fi
    fi

    echo "Gather kube etcd diagnostics if applicable" | printAndLog
    KUBE_ETCD_POD_NAMES=$(${EXE} get pods -n kube-system --no-headers -o custom-columns=":metadata.name" | cleanOutput | grep "k8s-etcd-" || true )
    for POD_NAME in ${KUBE_ETCD_POD_NAMES[@]}; do
        get_pod_logs kube-system "${POD_NAME}" "--timestamps"
    done

    echo "Gather legacy certgen diagnostics if applicable" | printAndLog
    LEGACY_CERT_GEN_POD_NAMES=$(${EXE} get pods -n kube-system -l component=essential -l ${RELEASE_LABEL} --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    for POD_NAME in ${LEGACY_CERT_GEN_POD_NAMES[@]}; do
        get_pod_logs kube-system "${POD_NAME}"
    done

    echo "Gather legacy oauth diagnostics if applicable" | printAndLog
    LEGACY_OAUTH_POD_NAMES=$(${EXE} get pods -n kube-system -l component=ui -l ${RELEASE_LABEL} --no-headers -o custom-columns=":metadata.name" | cleanOutput)
    for POD_NAME in ${LEGACY_OAUTH_POD_NAMES[@]}; do
        get_pod_logs kube-system "${POD_NAME}"
    done
fi

####################################################################################################
# Gathering finished package and finish
####################################################################################################

if [ "${BUNDLED}" -eq 0 ]; then
    echo "INFO: Event Streams instance log collection complete."
else
    echo ""
    echo "--- END OF GATHER ---" | printAndLog
    echo ""

    # Sanitise and archive diagnostics
    find "${LOGDIR}" -type f -empty -delete
    TARBALL="${LOGDIR}.tar.gz"
    tar czf "${TARBALL}" "${LOGDIR}"
    if [ -s "${TARBALL}" ]; then
        printGreenAndLog "COMPLETE - Results are in ${LOGDIR}.tar.gz"
        rm -rf "${LOGDIR}"
    else
        printRedAndLog "There was an issue creating \"${LOGDIR}.tar.gz\""
    fi
fi
