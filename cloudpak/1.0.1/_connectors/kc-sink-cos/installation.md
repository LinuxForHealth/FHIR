---
order: 1
forID: kc-sink-cos
categories: [sink]
---

1. Clone the Git repository and build the connector plugin JAR files:

    ```
    git clone https://github.com/ibm-messaging/kafka-connect-ibmcos-sink
    cd kafka-connect-ibmcos-sink
    gradle shadowJar
    ```

2. {{site.data.reuse.kafkaConnectStep2_title}}

    {{site.data.reuse.kafkaConnectStep2_content_1}}
    {{site.data.reuse.kafkaConnectStep2_content1_example}}

3. {{site.data.reuse.kafkaConnectStep3_title}}