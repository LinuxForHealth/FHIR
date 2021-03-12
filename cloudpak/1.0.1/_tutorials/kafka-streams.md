---
title: "Running Kafka Streams applications"
description: "Learn how to run Kafka Streams applications in IBM Event Streams."
permalink: /tutorials/kafka-streams-app/
toc: true
section: "Tutorials for IBM Event Streams"
cardType: "large"
---

You can run [Kafka Streams](https://kafka.apache.org/documentation/streams/){:target="_blank"} applications in {{site.data.reuse.long_name}}.

Follow the steps in this tutorial to understand how to set up your existing Kafka Streams application to run in {{site.data.reuse.short_name}}, including how to set the correct connection and permission properties to allow your application to work with Event Streams.

The examples mentioned in this tutorial are based on the [`WordCountDemo.java` sample](https://github.com/apache/kafka/blob/2.1/streams/examples/src/main/java/org/apache/kafka/streams/examples/wordcount/WordCountDemo.java){:target="_blank"} which reads messages from an input topic called `streams-plaintext-input` and writes the words, together with an occurrence count for each word, to an output topic called `streams-wordcount-output`.

## Prerequisites

- Ensure you have an {{site.data.reuse.short_name}} installation available. This tutorial is based on {{site.data.reuse.short_name}} version 2019.1.1.
- Ensure you have a [Kafka Streams](https://kafka.apache.org/documentation/streams/){:target="_blank"} application ready to use. You can also use one of the Kafka Streams [sample applications](https://github.com/apache/kafka/tree/2.1/streams/examples/src/main/java/org/apache/kafka/streams/examples){:target="_blank"} such as the  `WordCountDemo.java` sample used here.

## Creating input and output topics

Create the input and output topics in {{site.data.reuse.short_name}}.

For example, you can create the topics and name them as they are named in the [`WordCountDemo.java` sample](https://github.com/apache/kafka/blob/2.1/streams/examples/src/main/java/org/apache/kafka/streams/examples/wordcount/WordCountDemo.java){:target="_blank"} application. For demonstration purposes, the topics only have 1 replica and 1 partition.

To create the topics:
1. Log in to your {{site.data.reuse.long_name}} UI.
2. Click the **Topics** tab and click **Create topic**.
3. Enter the name `streams-plaintext-input` and click **Next**.
4. Set 1 partition for the topic, leave the default retention period, and select 1 replica.
5. Click **Create topic**.
6. Repeat the same steps to create a topic called `streams-wordcount-output`.


## Sending data to input topic

To send data to the topic, first set up permissions to produce to the input topic, and then run the Kafka Streams producer to add messages to the topic.

To set up permissions:

1. Log in to your {{site.data.reuse.long_name}} UI.
2. Click the **Topics** tab.
3. Select your input topic you created earlier from the list, for example `streams-plaintext-input`.
4. Click **Connect to this topic** on the right.
3. On the **Connect a client** tab, copy the address from the **Bootstrap server** section. This gives the bootstrap address for Kafka clients.
4. From the **Certificates** section, download the server certificate from the **Java truststore** section, and make a note of the password.
5. To generate an API key, go to the **API key** section and follow the instructions. Ensure you select **Produce only**. The name of the input topic is filled in automatically, for example `streams-plaintext-input`.
6. Click the **Sample code** tab, and copy the snippet from the **Sample configuration properties** section into a new file called `streams-demo-input.properties`. This creates a new properties file for your Kafka Streams application.
7. Replace `<certs.jks_file_location>` with the path to your truststore file, `<truststore_password>` with the password for the JKS file, and `<api_key>` with the API key generated for the input topic. For example:\\
   ```
   security.protocol=SASL_SSL
   ssl.protocol=TLSv1.2
   ssl.truststore.location=/Users/john.smith/Downloads/es-cert.jks
   ssl.truststore.password=password
   sasl.mechanism=PLAIN
   sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="token" password="abcAt0vjYZ1hEwXsRIuy8pxxXHNbEppOF";
   ```

To send messages to the input topic, use the bootstrap address, the input topic name, and the new properties file you created. For example, run [`kafka-console-producer.sh`](https://github.com/apache/kafka/blob/2.1/bin/kafka-console-producer.sh){:target="_blank"} with the following options:

- `--broker-list <broker_url>`: where `<broker_url>` is your cluster's broker URL copied earlier from the **Bootstrap server** section.
- `--topic <topic_name>`: where `<topic_name>` is the name of your input topic, in this example, `streams-plaintext-input`.
- `--producer.config <properties_file>`: where `<properties_file>` is the new properties file including full path to it, in this example, `streams-demo-input.properties`.

For example:

```
kafka_2.12-1.1.0 $ ./bin/kafka-console-producer.sh \
              --broker-list 192.0.2.24:31248 \
              --topic streams-plaintext-input \
              --producer.config streams-demo-input.properties
>This is a test message
>This will be used to demo the Streams sample app
>It is a Kafka Streams test message
>The words in these messages will be counted by the Streams app
```

Another method to produce messages to the topic is by using the [{{site.data.reuse.short_name}} producer API](../../2019.1.1/connecting/rest-api/#producing-messages-using-rest).

## Running the application

Set up your Kafka Streams application to connect to your {{site.data.reuse.short_name}} instance, have permission to create topics, join consumer groups, and produce and consume messages. You can then use your application to create intermediate Kafka Streams topics, consume from the input topic, and produce to the output topic.

To set up permissions and secure the connection:

1. Log in to your {{site.data.reuse.long_name}} UI.
2. Click **Connect to this cluster** on the right.
3. From the **Certificates** section, download the server certificate from the **Java truststore** section, and make a note of the password.
4. To generate an API key, go to the **API key** section and follow the instructions. Ensure you select **Produce, consume and create topics**.\\
   The permissions are required to do the following:
   - Create topics: Kafka Streams creates intermediate topics for the operations performed in the stream.
   - Join a consumer group: to be able to read messages from the input topic, it joins the group [`streams-wordcount`](https://github.com/apache/kafka/blob/74c8b831472ed07e10ceda660e0e504a6a6821c4/streams/examples/src/main/java/org/apache/kafka/streams/examples/wordcount/WordCountDemo.java#L49){:target="_blank"}.
   - Produce and consume messages.
5. Click the **Sample code** tab, and copy the snippet from the **Sample connection code** section into your Kafka Streams application to set up a secure connection from your application to your {{site.data.reuse.short_name}} instance.
6. Using the snippet, import the following libraries to your application:\\
   ```
   import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
```
7. Using the snippet, reconstruct the Properties object as follows, replacing `<certs.jks_file_location>` with the path to your truststore file, `<truststore_password>` with the password for the JKS file, and `<api_key>` with the generated API key, for example:\\
   ```
   Properties properties = new Properties();
   properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "192.0.2.24:31248");
   properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
   properties.put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLSv1.2");
   properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "<certs.jks_file_location>");
   properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "<truststore_password>");
   properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
   String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required "
       + "username=\"token\" password=\"<api_key>\";";
   properties.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
   ```
8. Ensure you [download](../../2019.1.1/getting-started/client/#creating-an-apache-kafka-java-client-application) the JAR files for SLF4J and add them to your classpath.


Run your Kafka Streams application. To view the topics, log in to your {{site.data.reuse.short_name}} UI and click the **Topics** tab.

For example, the following topics are created by the `WordCountDemo.java` Kafka Streams application:

```
streams-wordcount-KSTREAM-AGGREGATE-STATE-STORE-0000000003-changelog
streams-wordcount-KSTREAM-AGGREGATE-STATE-STORE-0000000003-repartition
```

## Viewing messages on output topic

To receive messages from the input topic, first set up permissions so that the output topic can consume messages, and then run the Kafka Streams consumer to send messages to the topic.

To set up permissions:

1. Log in to your {{site.data.reuse.long_name}} UI.
2. Click the **Topics** tab.
3. Select your output topic you created earlier from the list, for example `streams-wordcount-output`.
4. Click **Connect to this topic** on the right.
3. On the **Connect a client** tab, copy the address from the **Bootstrap server** section. This gives the bootstrap address for Kafka clients.
4. From the **Certificates** section, download the server certificate from the **Java truststore** section, and make a note of the password.
5. To generate an API key, go to the **API key** section and follow the instructions. Ensure you select **Consume only**. The name of the output topic is filled in automatically, for example `streams-wordcount-output`.
6. Click the **Sample code** tab, and copy the snippet from the **Sample configuration properties** section into a new file called `streams-demo-output.properties`. This creates a new properties file for your Kafka Streams application.
7. Replace `<certs.jks_file_location>` with the path to your truststore file, `<truststore_password>` with the password for the JKS file, and `<api_key>` with the API key generated for the input topic.

To view messages on the output topic, use the bootstrap address, the output topic name, and the new properties file you created. For example, run [`kafka-console-consumer.sh`](https://github.com/apache/kafka/blob/2.1/bin/kafka-console-consumer.sh){:target="_blank"} with the following options:

- `--bootstrap-server <broker_url>`: where `<broker_url>` is your cluster's broker URL copied earlier from the **Bootstrap server** section.
- `--topic <topic_name>`: where `<topic_name>` is the name of your output topic, in this example, `streams-wordcount-output`.
- `--consumer.config <properties_file>`: where `<properties_file>` is the new properties file including full path to it, in this example, `streams-demo-output.properties`.

For example:

```
$ ./bin/kafka-console-consumer.sh \
> --bootstrap-server 192.0.2.24:31248 \
> --topic streams-wordcount-output \
> --consumer.config streams-demo-output.properties \
> --from-beginning \
> --group streams-demo-group-consumer \
> --formatter kafka.tools.DefaultMessageFormatter \
> --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
> --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer \
> --property print.key=true
this    1
is    1
a    1
test    1
message    1
this    2
will    1
be    1
used    1
to    3
demo    1
the    1
streams    5
sample    1
app    1
it    1
is    2
a    2
kafka    7
streams    6
test    2
message    2
the    2
words    1
in    1
these    1
messages    1
will    2
be    2
counted    1
by    1
the    3
streams    7
app    2

Processed a total of 34 messages
```
