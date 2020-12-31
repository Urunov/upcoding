---
title: Couchbase Scale-UP Mistake
categories:
 - couchbase
tags:
 - couchbase
---

I was doing regular scale-up operation by adding two instances with higher CPU than existing two nodes. After adding I was planning to step-by-step remove the old nodes with lower performance. Everything was going smooth, I have added two nodes, did setting for each bucket to have a one replica and pushed rebalance. It took a quite long, I'm not even sure, since it was DEV environment, I just went home. Next day early morning I saw it was finished and balanced. So now I have started removing the old nodes. First I have ordered to old_node_1 to REMOVE and then pushed REBALANCE. It took about 2 hours and it went down. So now I have asked to second old node to be REMOVE and pushed REBALANCE. So another long 2 hour rebalancing process has started I thought. While waiting for that I have decided to remove the old_node_1 which is already out of the cluster. Here the BIG MISTAKE happened. I have logged in to the new_node_2 and stoped the service `sudo service couchbase-server stop`

And then realized that was wrong machine. Right away I have restarted the service `sudo service couchbase-server start`, and when I went to Web-console it was disaster. old_node_1 was in process of rebalancing to get removed and new_node_2 has restarted. Obviously the rebalance process has been stoped. And new_node_2 was in yellow state, here I got a panic and pushed the REBALANCE again. Rebalancing took quite a long time but finally it has successfully finished.

Now I have realized that since I have already ruined and recovered why don't I try more experiments. Especially I was interested in difference between Failover and Remove buttons. Because, If we already turned-on replication at least one for each bucket then do I we really have to do remove operation. Isn't it better just tell to node failover and then rebalance among remaining nodes. May be I didn't even had to wait for long taking REMOVE and then REBALANCE operations.

While experimenting these things I have run into another problem. I have realized that indexing is not replicated and indexes are gone when the node which holds the indexes are gone. I have tried to turn on the index replication:

```
curl -X POST -u <cluster-admin-username>:<password> http://<indexer-node-ip-addr>:9102/settings --data '{"indexer.settings.num_replica": 1}
```

And futher continued my experiments, but the indexes didn't replicate and also I couldn't create a new indexes. So I was got another panic, what could go wrong this time. Every time when i tried to create an index i got the follower error:

```
GSI CreatePrimaryIndex() - cause: Index Replica not supported in non-Enterprise Edition
```

My mistake was I had to turn-off the index replication as I have added it before. [Here](https://forums.couchbase.com/t/cannot-create-a-new-index/22488) my discussion regarding to this topic with couchbase community. 

At the end I have decided to finish up and went by XDCR replication way. Just created a new Cluster and throw all my buckets to there. And then manually created new indexes and everything went smooth and fast.

## Conclusion

If we have to scale-up (not scale-out) Couchbase cluster, it could be much faster use XDCR method, by creating a new cluster. Of course we have to take care a domain-name related switchings, and it may be difficult to do it on real-time working service. But that is the way to go if we can have a short stopping of our service. 


