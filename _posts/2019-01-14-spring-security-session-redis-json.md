---
title: Storing Json Formatted Spring Session in Redis
categories:
 - spring-security
tags:
 - books, review, spring, security, spring-session, microservices, features, redis, json, jackson, mixin
---

I have found combination of these three is a quite interesting topic. Actually  four, because usually Spring Session is used with Spring Security. There are many resources for Spring Session storing in Redis, but when it comes to storing session in Json format not many infos are there. I have found some but I felt they were not good enough to finish up practically. 


## Advantage?

- It becomes easy to visualy see the current sessions with their details
- Organize reports about real-time sessions
- Real-time management and change the authorizations of the connected users

## Details

First of all RedisHttpSession configuration should be enabled in order to use Redis to store Sessions.

```java
@EnableRedisHttpSession
public class RedisHttpSessionConfig {
    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
```

with following **application.yml** settings:

```yml
spring:
  data:
    redis:
      repositories:
        enabled: true

  redis:
    host: dev-goods-rcache.withstatic.com
    port: 6379
```

Anything that is related to session attribute should be JSON serializable, otherwise, it will be keep throwing and errors. 

Lets assume we have the following controller with cookie based pageView counter.

```java
@RestController
public class HelloResource {
   @Autowired
    private SessionComponent sessionComponent;

   @GetMapping("/test")
   public String getTest(HttpServletRequest request, Model model) {
      System.out.println("Scoped SessionComponent page views: " + sessionComponent.getPageViews());
      Integer pageViews = 1;
      HttpSession httpSession = request.getSession();
      if (request.getSession().getAttribute("pageViews") != null) {
         pageViews += (Integer) request.getSession().getAttribute("pageViews");
      }
      sessionComponent.setPageViews(pageViews);
      request.getSession().setAttribute("pageViews", pageViews);
      model.addAttribute("pageViews", pageViews);
   }
}
```

It uses SesionComponent to store page visit information, which is stored in a scope of the session. It also should contain @JsonSerialize annotation, that way it can be stored in Redis.

```java
@Component
@JsonSerialize
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionComponent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer pageViews;

    public void setPageViews(Integer pageViews) {
        this.pageViews = pageViews;
    }

    public Integer getPageViews() {
        return pageViews;
    }
}
```

BeanClassLoaderAware interface can be considered as a core part of this whole project. It contains the bean RedisSerializer which is actually tells to the spring how to perform the serialization. GenericJackson2JsonredisSerializer(ObjectMapper) is used to set custom-configured ObjectMapper. Spring session must be able to store the spring security related settings and later recover them without any problem. In order to store Spring Security related variables and parameters, SecurityJackson2Modules is used. 

```java
@Configuration
public class SessionConfig implements BeanClassLoaderAware {
    private ClassLoader loader;

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
        return mapper;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }
}
```

As it has been mentioned above spring-session is used to store spring-security credentials. UserDetails interface is the of the main credential that holds user related credential information. It is returned from the service with UserDetailsService interface. It is up to the developer to make the custom user detail class being JSON serializable. 

```java
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUserDetails extends Admin implements UserDetails {
    public CustomUserDetails() {}

    public CustomUserDetails(final Admin admin) {
        super(admin);
    }

    @Autowired
    public AuthorityRepository authorityRepository;

    public void setAuthorities(Collection<? extends GrantedAuthority>  args) {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> listGrantedAuth = new ArrayList<>();
        this.getAuthorityList().forEach(auth -> {
            listGrantedAuth.add(new SimpleGrantedAuthority(auth.toString()));
        });
        return listGrantedAuth;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getNickName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

Default constructor and proper setter/getter methods also must be provided otherwise, You will be keep getting following kind of errors (I got a lot of them):

```java
org.springframework.data.redis.serializer.SerializationException: Could not read JSON: Cannot construct instance of `io.github.rusyasoft.security.model.CustomUserDetails` (no Creators, like default construct, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
 at [Source: UNKNOWN; line: -1, column: -1] (through reference chain: org.springframework.security.core.context.SecurityContextImpl["authentication"]); nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `io.github.rusyasoft.security.model.CustomUserDetails` (no Creators, like default construct, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
```

Admin Entity can have different implementation, In below code just simplified version of it illustrated. If real integration with spring security is needed then `authorityList` should be filled out with the user authority data from DB. As it is shown below the Admin class is annotated with JsonSerializable and reason is it is not used directly in our project. It is used as a parent class for CustomUserDetails. CustomUserDetails has been annotated with @JsonSerializable so it should be enough. Another important thing is that classes that are used for storing into session must contain default constructor (as it has been done below).

```java
@Entity
@Data
public class Admin implements Serializable {
    private long id;
    private String nickName;
    private String name;
    private String password;
    private String phoneNo;
    private List<Long> authorityList;

    // default constructor
    public Admin() {}
}
```

Here the example of `UserDetailsService` which fills and returns UserDetails according to the given username: 

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminsRepository adminsRepository;

    @Autowired
    private AuthorityRepository authorityRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Admin> optionalAdmin = adminsRepository.findByNickName(username);

        List<BigInteger> bigIntegerIds = authorityRepository.findByNickName(username);
        List<Long> authorIds = new ArrayList<>();

        for (int i = 0; i < bigIntegerIds.size();i++) {
            authorIds.add(bigIntegerIds.get(i).longValue());
        }

        optionalAdmin.orElseThrow(() -> new UsernameNotFoundException("User nickname not found"));

        return optionalAdmin.map(users -> {
            users.setAuthorityList(authorIds);
            return new CustomUserDetails(users);
        }).get();
    }
}
```

Two select queries are performed via JPA:

1. select admin information from admin table by username
2. join select where authorities of specific user returned.


## MixIn solution

If we are developing the whole project by ourselves and all classes are created and modifiable by us then above solution (Declaring @JsonSerializable annotation) should be enough. But what if we have to use third party classes in our project ?
MixIn solution comes to our rescue. Here what *Effective Java* book says about mixin: *a mixin is a type that a class can implement in addition to its "primary type", to declare that it provides some optional behavior. They are called mixin because it allows the optional functionality to be "mixed in" to the type's primary functionality.*

### Quick example:

Lets assume there is 3rd party library called Address and it has a field city. 

```java
class Address {
    private String city;
    public String getCity() {
      return city;
    }
    public void setCity(String city) {
      this.city = city;
    }
}
```

Since it is a third party class we have to create a MixIn class for this address class 

```java
public abstract class AddressMixin {
    @JsonProperty("city")
    String city;
}
```

Finally we have to bind AddressMixin class with Json ObjectMapper.

```java
ObjectMapper mapper = new ObjectMapper();
mapper.addMixInAnnotations(Address.class, CityMixin.class);
```

Now ObjectMapper knows whenever it meets Address class it should apply CityMixin class in order to serialize/deserialize the object. The above example is simple example, it may get more complicated when we have sub-classes.


### Detail analaysis of Spring Security Jackson relations

Inside SessionConfig we have initialized the ObjectMapper by registering modules from SecurityJackson2Modules. If we dive into analysis of the SecurityJackson2Modules, then we can see it loads modules from security package:

```java
private static final List<String> securityJackson2ModuleClasses = Arrays.asList(
    "org.springframework.security.jackson2.CoreJackson2Module",
    "org.springframework.security.cas.jackson2.CasJackson2Module",
    "org.springframework.security.web.jackson2.WebJackson2Module"
);
```

Those modules contains mixin classes. for example CoreJackson2Module contains MixIn class registration for basic security functionalities:

```java
public class CoreJackson2Module extends SimpleModule {

	public CoreJackson2Module() {
		super(CoreJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
	}

	@Override
	public void setupModule(SetupContext context) {
		SecurityJackson2Modules.enableDefaultTyping((ObjectMapper) context.getOwner());
		context.setMixInAnnotations(AnonymousAuthenticationToken.class, AnonymousAuthenticationTokenMixin.class);
		context.setMixInAnnotations(RememberMeAuthenticationToken.class, RememberMeAuthenticationTokenMixin.class);
		context.setMixInAnnotations(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
		context.setMixInAnnotations(Collections.<Object>unmodifiableSet(Collections.emptySet()).getClass(), UnmodifiableSetMixin.class);
		context.setMixInAnnotations(Collections.<Object>unmodifiableList(Collections.emptyList()).getClass(), UnmodifiableListMixin.class);
		context.setMixInAnnotations(User.class, UserMixin.class);
		context.setMixInAnnotations(UsernamePasswordAuthenticationToken.class, UsernamePasswordAuthenticationTokenMixin.class);
	}
}
```

## Conclusion

If there is necessity to store some additional informations inside the session in redis in a form of Json then it must be annotated with JsonSerializable or declared as a MixIn classes


## References

1. https://medium.com/@shankar.ganesh.1234/jackson-mixin-a-simple-guide-to-a-powerful-feature-d984341dc9e2
2. https://www.baeldung.com/jackson-annotations
3. https://dzone.com/articles/jackson-mixin-to-the-rescue
