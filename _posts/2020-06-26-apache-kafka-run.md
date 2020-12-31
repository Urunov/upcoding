---
title: Apache Kafka Run
categories:
 - kafka
tags:
 - kafka, messaging, broker, queue, apache
---

Lets do the quick-start with Kafka broker service, and try some operations on it.

```
tar xvf kafka-x.x.x.tgz
cd kafka-x.x.x
```

Set the bin folder inside the kafka as a path to get access

Here how we start zookeper and kafka:

```
zookeeper-server-start.sh config/zookeeper.properties
kafka-server-start.sh config/server.properties
```

Lets create some topic, list it and see the detail information about that topic

```
kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 1
kafka-topics --zookeeper 127.0.0.1:2181 --list
kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --describe
```

the following result will be shown:

```
Topic: first_topic	PartitionCount: 3	ReplicationFactor: 1	Configs:
	Topic: first_topic	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: first_topic	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: first_topic	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
```

To read data from console input and publish it to Kafka you can use kafka-console-producer, as follows (when done with entering put Ctrl+C):

```
kafka-console-produce --broker-list 127.0.0.1:9092 --topic first_topic
# or 
kafka-console-produce --broker-list 127.0.0.1:9092 --topic first_topic --producer-property acks=all
```

Lets run consumer on topic `first_topic`, it starts consuming messages from the point when the consumer started

```
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic
```

if we want to start consumer that reads messages from very beginning (starting from initial offset)

```
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic --from-beginning
```

Here how we create a kafka consumer group. (The messages are distributed balancedly among consumers of the same group)

```
kafka-console-consumer --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-first-group
```

To perform operations over kafka consumer groups the following command can be used.

```
kafka-consumer-groups --bootstrap-server localhost:9092 --list
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-second-application
```

LAG - show the number of lagging messages that are supposed to be consumed by group `my-second-application`

## Resetting Offsets

```
kafka-consumer-groups --bootstrap-server localhost:9092 --group my-second-application --reset-offsets --to-earliest --execute --topic first_topic
```

`--to-earliest` argument reffers to start set the offset to the beginning
`--topic` defines to which topic this operation supposed to be applied

additional parameters: 
`--shift-by` can be used to shift offset by given number


## Kafka Tool

Open [kafkatool.com](http://kafkatool.com) to download Kafka UI Tool

Kafka Tool is a GUI application for managing and using Apache Kafka ® clusters. It provides an intuitive UI that allows one to quickly view objects within a Kafka cluster as well as the messages stored in the topics of the cluster. It contains features geared towards both developers and administrators. Some of the key features include

- Quickly view all your Kafka clusters, including their brokers, topics and consumers
- View contents of messages in your partitions and add new messages
- View offsets of the consumers, including Apache Storm Kafka spout consumers
- Show JSON and XML messages in a pretty-printed format
- Add and drop topics plus other management features
- Save individual messages from your partitions to local hard drive
- Write your own plugins that allow you to view custom data formats
- Kafka Tool runs on Windows, Linux and Mac OS
