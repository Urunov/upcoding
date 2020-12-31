---
title: DDD, Repsitory
categories:
 - DDD
tags:
 - Domain-Driven-Design, Repository
---
We are continuing our Domain-Driven Design related chapters, and here I've tried to describe my understadings regarding to Repositories in the DDD. Repositories are easily confused with *Factory patterns*, while the main difference is that *Factory Pattern* does not provide persistency. Lets just dive into repository topic which starts with retrieving objects.

## Retrieving and Saving Objects

Repository provides two basic interface:
 - Lookup
 - Save

Saving is impelemented based on root aggregate. In DDD, a repository is an objcect that participates in the domain but really abstracts away storage and infrastructure details.

![No Image](/assets/2018-05-10-ddd-repositories/retrievingObjects.png)

Most systems have a persistent storage like a database for its fully functioning. Applying repositories happens by integrating and synchronizing with existing aggregate objects in the system. Aggregate root controls the data access to the repository, by limiting queries on random objects.

## Object Life Cycles
 - No persistence
 - With persistence 

![No Image](/assets/2018-05-10-ddd-repositories/ObjectLifecycle.png)

> A **repository** represents all objects of a certain type as a conceptual set ... like a collection with more elaborate querying capability
>
> <cite>Eric Evans (Domain-Driven Design)</cite>


## Repository Tips
- Think of it as an in-memory collection. Perfoming add, remove and retrieve operations on repositories
- Implement a known, common access interface. Global interface for developers to know how to interact with repository
Example:
```
public interface IRepository<T> {
	T getById(int id);
	void add(T entity);
	void Remove(T entity);
	void Update(T entity);
	IEnumerable<T> List();
}
```
Interface may contain not only lowest level methods, but can contain little higher level methods too. If they are used by all classes that are uses that interface. For example: getByIdThroughCache() could be another useful method that employs the usage of in-memory cache
	
- Methods for add & remove. These are the actual impelementation of the common interface and of course it may differ depend on the repository type.
- Methods that predefine criteria for object selection. Predefine the most repeated queries in a form of methods.
Example:

```C#
public Schedule GetScheduledAppointmentsForDate(int clinicId, DateTime date) {
	var scheduleGraph = QueryScheduleForThisOffice(clinicId)
			.Select(s => new
			{
				Schedule = s,
				Appointements = s.Appointments
					.Where(a =>
					DbFunctions.DiffDays(date, a.TimeRange.Start) == 0
					)
			})
			.SingleOrDefault();
	var schedule = scheduleGraph.Schedule;
	schedule.DateRange = new DateTimeRange(date, date.AddDays(1));
	return schedule;
}
```

- Repos for aggregate roots only
- Client focuses on the model, repo focuses on persistence. Client should not care about persistence related detail works.

# Repository Benefits
- Provides common abstraction for persistence
- Promotes Separation of Concerns. Domain logic and user interface stays independent from data and backend data sources.
- Communicates Design Decisions. Only certain objects can access to the repository, that way repository itself can control accesses to the real storage.
- Enables Testability. Repositories can be mocked for the testing purpose.
- Improved Maintainability. Tuning performance, adding caching operations can be easily implemented on repository level

# Common Repository Blunders
- *N+1* Query Errors. When we use one query for fetching the list, and then *N* individual queries for fetching each item.
- Inappropriate use of eager or lazy loading.
- Fetching more data than required. (Try not to use `select * from table` if possible )


The Repositories and Factories are quite similar, because we use those patterns to get objects we want to work with. However factories are involved in creating a new objects, while repositories are used to find and update existing objects. Sometimes repositories use a factory pattern to create its objects.


# Generic Repositories in DDD
- Create non-generic Implementation class of generic interface is more preferable by DDD. 

class implementation:

```C#
public class Repository<TEntity> : IRepository<TEntity> where TEntity : class, IEntity
{
	private readonly CrudContext _context;
	private readonly DbSet<TEntity> _dbSet;
	public Repository(CrudContext context) {...}
	public IEnumerable<TEntity> List() {...}
	public TEntity GetById(int id) {...}
	public void Insert(TEntity entity) {...}
	public void Update(TEntity entity) {...}
	public void Delete(int id) {...}
}
```

usage: 

```C#
var repo = new Repository<Patient>(new CrudContext())
rep.Insert(new Patient())
```

If the generic class of the generic interface is created as shown above, then any client applications can access the repository by giving their own T class as an generic argument. This kind of usage of repository opposes the rule of accessing repository through root-aggregate.

![No Image](/assets/2018-05-10-ddd-repositories/genericRepositories.png)


- refactoring may be required to control the repository access from the different clients
- Do the test cases and they should pass after refactoring


JPA: Java Persistence API
Spring Data JPA is not a JPA Provider. It is a library/framework that adds an extra layer of abstraction on top of JPA provider. It contains three layers:
- Spring Data JPA: provides support for creating JPA repositories by extending the Spring Data repository interface
- Spring Data Commons: provides the infrastructure that is shared by the datastore specific Spring Data projects
- JPA Provider implements the Java Persistence API

JPA example: 

```java
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
 
 
interface TodoRepository extends Repository<Todo, Long> { 
     
    @Query("SELECT t FROM Todo t where t.title = :title AND t.description = :description")
    public Optional<Todo> findByTitleAndDescription(@Param("title") String title, 
                                                    @Param("description") String description);
     
    @Query(
        value = "SELECT * FROM todos t where t.title = :title AND t.description = :description", 
        nativeQuery=true
    )
    public Optional<Todo> findByTitleAndDescription(@Param("title") String title, 
                                                    @Param("description") String description);
}
```

*Mediating between the domain and data mapping layers using a collection-like interface for accessing domain objects.*

![No Image](https://martinfowler.com/eaaCatalog/repositorySketch.gif)

A Repository mediates between the domain and data mapping layers, acting like an in-memory domain object collection. This becomes more important when there are a large number of domain classes or heavy querying. In these cases particularly, adding this layer helps minimize duplicate query logic.


**What is difference between CrudRepository and JpaRepository interfaces in Spring Data JPA?**
JpaRepository extends PagingAndSortingRepository which in turn extends CrudRepository. Main functionalities are:
- CrudRepository mainly provides CRUD functions.
- PagingAndSortingRepository provide methods to do pagination and sorting records.
- JpaRepository provides some JPA related method such as flushing the persistence context and delete record in a batch.



# DDD with repository by JPA: simple Example
![No Image](http://blog.ncortes.com/wp-content/uploads/2014/06/DDDAcademy2.png)
for details refer to [Nico Corte's blog](http://blog.ncortes.com/2014/06/30/ddd-repository-by-jpa-simple-example/), [sourceCode](https://bitbucket.org/coganic/hexagonalacademy/src)



