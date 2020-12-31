---
title: SQL Alchemy Migrations (Alembic)
categories:
 - database
tags:
 - python, database, migrations, orm, sqlalchemy
---

Alembic is a lightweight database migration tool for usage with the SQLAlchemy Database Toolkit for Python.

Installation

```bash
$ pip install alembic
```

go to the project folder and initialize alembic

```bash
$ alembic init alembic
```

> **Notion:** The second alembic is the name of the migration project folder. It is a kind of convention to 
> give a name "alembic". The folder and all internal containings should go to git source control.

Next we have to edit alembic.ini file (it is not inside the alembic folder) in order to tell alembic about our project. There are many settings over there, and lets start with setting the database connection. To do that we have to set configuration `sqlalchemy.url` . Basicaly it is just a copy of sqlalchemy database connection from our source code.

```
sqlalchemy.url = driver://user:password@localhost/dbname
```

After setting database connection string we can run command to get version information with current code and database status

```bash
$ alembic current
```

At first run you will get following response which basically tells us that it has no track of any changes

```
    INFO  [alembic.runtime.migration] Context impl MySQLImpl.
    INFO  [alembic.runtime.migration] Will assume non-transactional DDL.
```

By the way in our database we can see a new table named `alembic_version` is created, and it is done to track the version of changes.

Next, we have to let the alembic know where the database models and SqlAlchemyBase object (which is created by dec.declarative_base()) are located. In order to do that we have to edit alembic/env.py file as follows:

```python
from myproject.data.modelbase import SqlAlchemyBase
from myproject.data import *

target_metadata = SqlAlchemyBase.metadata
```

After that we can just run 

```bash
$ alembic revision --autogenerate -m "last updated on packages"
```

and it will automatically generates alembic differences

**CAUTION:** When I just run above command right away alembic couldn't find my myproject module and showed me following error:

```
...
File "<frozen importlib._bootstrap_external>", line 728, in exec_module
File "<frozen importlib._bootstrap>", line 219, in _call_with_frames_removed
File "alembic/env.py", line 8, in <module>
  from myproject.data.modelbase import SqlAlchemyBase
ModuleNotFoundError: No module named 'myproject'
```

To solve above problem I have added system path corrections just before importing modelbase

```python
import sys                                   # <-- this one
sys.path = ['', '..'] + sys.path[1:]         # <-- and this one

from myproject.data.modelbase import SqlAlchemyBase
from myproject.data import *
```

For details of above solution refer to [here]([https://stackoverflow.com/questions/57468141/alembic-modulenotfounderror-in-env-py](https://stackoverflow.com/questions/57468141/alembic-modulenotfounderror-in-env-py))

Autogeneration of revision in alembic has created the python script file that can help us to upgrade our database. The upgrade script file located under alembic/versions/*.

To apply the script we can just simply run the command:

```bash
$ alembic upgrade head
```

Now our database tables are upgraded according to our models

At then end we can run the command `alembic current` to see the current version of our table.