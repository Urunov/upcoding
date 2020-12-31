---
title: ELK with Metricbeat for Collecting Couchbase Logs
categories:
 - elk, couchbase, metricbeat, logs, elastic stack
tags:
 - elk, elasticsearch, kibana, logstash, metricbeat, logs, couchbase, monitoring
---

![ELK plus Couchbase](/assets/images/elkpluscouchbase.png)

ELK has become important part of the monitoring web-services. When the ELK is used in combination with Beat tools, it is called [Elastic Stack](https://www.elastic.co/products/). Since here we will be using metric beat to collect couchbase logs we can proudly call it Elastic Stack. [Couchbase](https://www.couchbase.com/) is another popular NoSql DB that is used by many [enterprises](https://www.couchbase.com/customers).

# Getting Started

I have tried my ELK installation by following tutorial from [logz](https://logz.io/blog/install-elk-stack-amazon-aws/), but because of these projects are upgrading so often the tutorial didn't exactly match. At the end I had to improvize while installing. But the explanation on elasticsearch website were quite enough to install, config and run.

## ELK Installation

We are going to install whole Elastic Stack in a single standalone machine. So most of the time the address will be settled as localhost.

### Install Elasticsearch


```
curl -L -O https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.2.0-linux-x86_64.tar.gz
tar -xvf elasticsearch-7.2.0-linux-x86_64.tar.gz
vi elasticsearch-7.2.0/config/elasticsearch.yml
```

elasticsearch.yml contains all the configuratios of the Elasticsearch. Since we are installing elasticsearch in a standalone mode, the configuration is very simple:

```
network.host: localhost
http.port: 9200
```

#### Running ES

```
./bin/elasticsearch -d -p pid
```

simple localhost testing the running ES:

```
curl localhost:9200
```

#### Test liveness of Elasticsearch 

```
sudo curl -XGET localhost:9200
```

should response something like:

```
{
  "name" : "ip-XXX-XXX-XXX-XXX.ap-northeast-2.compute.internal",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "XXXXXXXXXX",
  "version" : {
    "number" : "7.2.0",
    "build_flavor" : "default",
    "build_type" : "tar",
    "build_hash" : "508c38a",
    "build_date" : "2019-06-20T15:54:18.811730Z",
    "build_snapshot" : false,
    "lucene_version" : "8.0.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

### Kibana

I have installed kibana through yum installer, and configured

```
sudo yum install kibana
sudo vi /etc/kibana/kibana.yml
```

In the configuration file I have changed only one parameter regarding to elasticsearch (we have to tell kibana which elasticsearch it supposed to connect with):

```
server.port: 5601
server.host: "0.0.0.0"
elasticsearch.hosts: ["http://localhost:9200"]
```


```
sudo service kibana start
```

To test does it work or not I simply called, `curl localhost:5601/api/status` which returns a lot of things about its status.


### Logstash

Logstash installation has follws the same way as kibana, but first we have to edit yum repos and create logstash.repo file

```
sudo rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch
sudo vi /etc/yum.repos.d/logstash.repo

###### logstash.repo ########
[logstash-7.x]
name=Elastic repository for 7.x packages
baseurl=https://artifacts.elastic.co/packages/7.x/yum
gpgcheck=1
gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
enabled=1
autorefresh=1
type=rpm-md
#############################
```

after that installation goes smooth:

```
sudo yum install logstash
```


### Simple Test of ELK 

As I have mentioned before i have followed the logz tutorials and they are giving us a good example [log-file](https://logz.io/learn/complete-guide-elk-stack/)


We can download logs for test by following commands:

```
wget https://s3.amazonaws.com/logzio-elk/apache-daily-access.log
```

Here is the logstash configuration file for this file:

```
sudo vi /etc/logstash/conf.d/apache-01.conf

##### apache-01.conf ######
input {
  file {
    path => "/home/centos/Downloads/apache-daily-access.log"
  start_position => "beginning"
  sincedb_path => "/dev/null"
  }
}

filter {
  grok {
    match => { "message" => "%{COMBINEDAPACHELOG}" }
  }
  date {
    match => [ "timestamp" , "dd/MMM/yyyy:HH:mm:ss Z" ]
  }
  geoip {
    source => "clientip"
  }
}

output {
  elasticsearch {
  hosts => ["localhost:9200"]
  }
}
```

To run the logstash with this configuration:

```
/usr/share/logstash/bin/logstash -f /etc/logstash/conf.d/apache-01.conf
```

Quickest way to check is the data delivered to elasticsearch or not is:

```
sudo curl -XGET 'localhost:9200/_cat/indices?v&pretty'
```

The response should return indeces organized into a table (in a console mode), where logstash-2019.07.27-0000001 (or something similar) will be placed

Now we can go Kibana and find/configure new index from there

## Metricbeat Installation 

Basic installation of metricbeat can be followed [here](https://www.elastic.co/guide/en/beats/metricbeat/current/metricbeat-installation.html)

```
curl -L -O https://artifacts.elastic.co/downloads/beats/metricbeat/metricbeat-7.3.0-x86_64.rpm
sudo rpm -vi metricbeat-7.3.0-x86_64.rpm
```

metricbeat can be configured to send logs kibana/elasticsearch/logstash individually. But more advanced way is to send it towards logstash. (Because that way we can do some advanced manipulations using logstash)

```
sudo vi /etc/metricbeat/metricbeat.yml

###### metricbeat.yml #####
output.logstash:
  # The Logstash hosts
  hosts: ["localhost:5044"]
```

We have to enable the couchbase module in order to make metricbeat work with couchbase metrics:

```
./metricbeat modules enable couchbase
```

After that we can edit couchbase.yml configuration file for the metric beat

```
sudo vi /etc/metricbeat/modules.d/couchbase.yml

#### couchbaes.yml #####
# Module: couchbase
# Docs: https://www.elastic.co/guide/en/beats/metricbeat/7.2/metricbeat-module-couchbase.html

- module: couchbase
  metricsets:
    - bucket
    - cluster
    - node
  period: 10s
  hosts: ["XXX.XXX.XXX.XXX:8091"]
  username: "user"
  password: "password"
```

to start metricbeat:

```
sudo service metricbeat start
```

Now when metricbeat is ready and sending the collected logs toward logstash, we have to reconfigure and restart our logstash to receive input from metricbeat

```
sudo vi /etc/logstash/conf.d/cbmetrics1.conf

##### cbmetrics1.conf ######
input {
  beats {
    port => 5044
  }
}
filter {
  #if "bucketNameExample1" != [couchbase][bucket][name] {
  #  drop {}
  #}

  if [couchbase][bucket][name] == "bucketNameExample1" and [couchbase][bucket][item_count] > 15 {
    throttle {
      key => "%{host}%{message}"
      before_count => -1
      after_count => 1
      period => 120
      add_tag => "bucketNameExample1_throttled"
    }
  }

}
output {
  #stdout { codec => rubydebug }

  if [couchbase][bucket][name] == "bucketNameExample1" and [couchbase][bucket][item_count] > 15 {
    if "bucketNameExample1_throttled" not in [tags] {
      http {
        url => "https://hooks.slack.com/services/XXXXXXXXTOKENXXWEBHOOKXXXXXX"
        content_type => "application/json"
        http_method => "post"
        format => "json"
        mapping => [ "channel", "#channelNameExample1","text",  "bucketNameExample1 bucket items over 15'}" ]
      }
    }
  }

  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "%{[@metadata][beat]}-%{[@metadata][version]}"
  }
}
```

Above example is the one that receives input from metricbeat through port 5044. It handles simple logic such as if the bucket name is equalt to "bucketNameExample1" and item_count is more than 15 then send Slack message to the Slack-webhook. Slack-webhook sent through simple http post request. Another elasticsearch part inside the output block sends the output towards our elasticsearch. [Throttle](https://www.elastic.co/guide/en/logstash/current/plugins-filters-throttle.html) is also used to not keep sending the case, but send it once in a 2 minutes. Throttle is used to avoid overflow of the message flow.
