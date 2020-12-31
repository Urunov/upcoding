---
title: Overview of OrientDB
categories:
 - database
tags:
 - graphdb, orientdb, graphs
---

In this blog I have collected some knowledges about OrientDB which is multi-model graph database. This blog can be referred to to some kind of fact checks about OrientDB.

| WARNING: **The following facts may change, by the time**: For most recent info refer to OrientDB [homepage](https://orientdb.com/docs/last/index.html) |
| --- |


# Fact List 
- An edge is a relationship, the same as a link. Edges provide more flexibility and are available in graph databases while a link is also available in a document database
- Edges are not always stored in the database as a document
- Fully restricted schema-ful data model can be created in OrientDB
- A record is described as a vertex in graph terminology and a Document in a document terminology
- A record is made up of the cluster number and record number
- Classes provide rules and structure to the database schema. They provided through inheritance, properties and constraints
- Clusters in OrientDB serves as:
    - Logical separation to limit queries to only the most relevant data
    - Geographic dispersed data for more localized access to increase performance
    - Sharding large data sets over multiple disks
    - Providing data replication options

## OrientDB Studio
- OrientDB Studio feature that is available not only in Enterprise Edition
- There are some default classes beginning with "O" inside the database. They add special capabilities to the database such as permissions and server side functions
- Studio provides mechanisms for using both the visual and command interfaces in the browser tab
- V is the Vertex base class
- E is the Edge base class
- Outside of batch operations, you can execute any command against OrientDB in the browse tab.
- In the graph view, only 20 records are displayed from the query. Subsequent queries with different parameters will add nodes to the view within these result limits
- The _studio class stores user specific settings like colors, past queries etc.

# Basic Console commands:
- in order to run the console you have to start the shell script ./console.sh

```
orientdb> create database remote:localhost/ConsoleDemo root hello plocal
```

`plocal` is specified as a storage

the database will be created and connected automatically

```
Current database is: remote:localhost/ConsoleDemo
orientdb {db=ConsoleDemo}>
```

In order to disconnect from the db type `disconnect` command. To connect back to server level use `connect` command as follows:

```
orientdb> connect remote:localhost root hello
Connecting to remote Server instance [remote:localhost] with user 'root'...OK
```

at the server level we can see the list of databases with `list databases` command. In order to connect our created database we have to use connect command from above by giving default username and password which is admin/admin correspondingly.

```
orientdb {server=remote:localhost/}> connect remote:localhost/databases/ConsoleDemo admin admin
```

`config` is used to see all the configurations of the database

`list classes` - to see all the classes
`create class Person extends V` - is used to create a vertex
`create property Person.name string` - to create a property in the class
`info calss Person` - is used to get information about Person class

`insert into Person (name) values ("Anvar"),("Bobur"),("Rustam")` - used to add some data
`browse class Person` - used to see records

## Backup
In order to backup we have to disconnect from the database and the shutdown the OrientDB database (can be done just by clicking CTRL+C in the console where the orientDB was running in a verbose mode)
In a state while OrientDB is turned off, we connect to our database and run backup command (IMPORTANT: BACKUPS IN COMMUNITY VERSION CAN BE PERFORMED ONLY LOCALLY)

```
orientdb> connect plocal:../databases/ConsoleDemo admin admin
orientdb {db=ConsoleDemo}> backup database /Users/folderWhereYouWantToStore/ConsoleDemo.zip
```

Database also can be exported and imported

```
orientdb {db=ConsoleDemo}> export database /Users/folderWhereYouWantToStore/ConsoleDemo.export
orientdb {db=ConsoleDemo}> import database /Users/folderWhereYouWantToStore/ConsoleDemo.export
```

# Basic CRUD
- Main difference between using the `INSERT INTO` command in comparison to the `CREATE VERTEX` command is that `CREATE VERTEX` does some extra checks and balances related to a graph database, but either command creates a record

# Users and Roles
- Based on Users and Roles (OUser and ORole classes)
- Three default users are created
- Roles can inherit from other roles
- Permissions enforced per resource
- Record level security built in

Applying rules based on the many resource options in OrientDB can provide a very robust permission system, in many cases (Solves about 90% of requirements)
Assigning permissions to resources based on role, then assign users to the appropriate roles