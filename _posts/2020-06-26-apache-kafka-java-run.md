---
title: Apache Kafka Run In Java
categories:
 - kafka
tags:
 - kafka, messaging, broker, queue, apache, java
---

In order to use kafka in a Java we need to get the kafka-client library from the maven repository (maven or gradle can be used)

## Producer

In order to create Kafka producer in the Java, we need to create a producer propertie, create a producer itself and send the data, below simple code snippet is given. 
In order to set necessary properties we should refer to [kafka-docs](https://kafka.apache.org/documentation/#producerconfigs) especially to the Producer Configs part.

```java
Properties propertie = new Properties();

// Hard coding way
properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
properties.setProperty("key.serializer", StringSerializer.class.getName());
properties.setProperty("value.serializer", StringSerializer.class.getName());

// Better way
properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

// Create the producer
KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

// Create a producer record
ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "hello world!!");


// Send data
producer.send(record);

// flush data
producer.flush()
// or we can use 
producer.close() // <-- this command flushes and closes 
```

producer.send() is an asynchronous function, that is why if don't want to use flush command but create a callback function that informs us about sending process completion. Here how the send() function should be changed:

```java
producer.send(record, new Callback() {
	public void onCompletion(RecordMetaData recordMetadata, Exception e) {
		// executed every time when the record is sent successfully or exception has occured
		if (e == null) {
			// the record was successfully sent
			logger.info("Received new metadata:" + 
				"topic: " + recordMetadata.topic() + 
				"Partition: " + recordMetadata.partition() + 
				"Offset: " + recordMetadata.offset() + 
				"Timestamp: " + recordMetadata.timestamp() );
		} else {
			// exception case
		}
		
	}
}).get(); // block the .send() to make it synchronous - don't do this in production
```

## Consumer

Consumer is created in a similar fashion as Producer

```java
Properties propertie = new Properties();

String groupId = "my-first-application";
String topic = "first_topic";

// Hard coding way
properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // possible values: "earliest", "none", "latest"

// create consumer
KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

// subscribe consumer to our topic(s)
consumer.subscribe(Collections.singleton(topic));
// or we can subscribe to multiple topics by 
// consumer.subscribe(Arrays.asList(topic));

// poll for the data
while (true) {
	ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

	for (ConsumerRecord<String, String> record : records) {
		logger.info("Key: " + record.key() + " , Value: " + record.value());
		logger.info("Partition: " + record.partition() + " , Offset: " + record.offset());
	}
}
```


## Assign and Seek from


```java
TopicPartition partitionToReadFrom = new TopicPartition(topic, 0);
long offsetToReadFrom = 15L;
consumer.assign(Arrays.asList(partitionToReadFrom));

// seek
consumer.seek(partitionToReadFrom, offsetToReadFrom);

int numberOfMessagesToRead = 5;
boolean keepOnReading = true;
int numberOfMessagesReadSoFar = 0;

// poll for new data
while (keepOnReading) {
	ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

	for (ConsumerRecord<String, String> record : records) {
		numberOfMessagesReadSoFar += 1;
		logger.info("Key : " + record.key() + " Value: " + record.value());
		logger.info("Partition: " + records.partition() + ", Offset: " + record.offset());
		if (numberOfMessagesReadSoFar >= numberOfMessagesToRead) {
			keepOnReading = false;
			break;
		}
	}
}
```

Note: The following code is not exactly related to Kafka, but it has been used in the example of Kafka and Tweet integration. I have found this snippet is useful to be used in other projects

```
Runtime.getRuntime().addShutdownHook(new Thread() -> {
	logger.info("stopping application...");
})
```

It runs when the console project is stopped or interrupt signal is sent to java application.