---
order: 1
forID: kc-sink-arangodb
categories: [sink]
classes: wide
---

1. Clone the Git repository and build the connector plugin JAR files:

    ```
    git clone https://github.com/jaredpetersen/kafka-connect-arangodb.git
    cd kafka-connect-arangodb
    mvn package
    ```

2. {{site.data.reuse.kafkaConnectStep2_title}}

    {{site.data.reuse.kafkaConnectStep2_content_1}}
    {{site.data.reuse.kafkaConnectStep2_content1_example}}

3. {{site.data.reuse.kafkaConnectStep3_title}}