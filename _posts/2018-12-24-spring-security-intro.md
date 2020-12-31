---
title: Introduction to the Spring Security
categories:
 - spring-security
tags:
 - books, review, spring security, microservices, introduction
---

Today I'm starting the series of Spring-Security articles, and most of them will be references from web-sites and book named "Spring-Security" from authors Mick Knutson,Robert Winch and Peter Mularien. Before going into an example of Spring Security lets clarify why do we need spring security.

# Why do We Need Spring Security ?

The internet based hacking has been born with the internet itself and no need to go for detail history about it. They guys who has long experience of web development already seen many kinky ways of internet exploitations. They already have developed skills to stand against those attacks and when they develop something they can write the code that protects agains those attacks. But human factor still remains and developers may forget put the measurements against some existing attacks, then it repeats again. That is ok if the professional forgot to put some prevention code for specific attack, he can remember later and recode it. But what about newcomer, who has no idea about it, he should learn from seniors and it may take a years and years. That is why people started developing the frameworks, that are already contains those preventions by default. There are many such frameworks out there, and many more coming. Here we will talk about Spring Security, which is basically security framework that works and developed particularly to work with spring framework.

Here some are the features provided by default:

- CSRF attack prevention
- Session Fixation protection
- Security Header integration
 - HTTP Strict Transport Security for secure requests
 - X-Content-Type-Options integration
 - Cache Control
 - X-XSS-Protection integration
 - X-Frame-Options integration to help prevent Clickjacking


# Lets Start with Hello Spring Security

First of all we have to integrate Spring Security into our code by declaring it in the gradle configuration file build.gradle:

```
dependencies {
    compile ('org.springframework.security:spring-security-config')
    compile ('org.springframework.security:spring-security-core')
    compile ('org.springframework.security:spring-security-web')
}
```

sometime it may become necessary to declare the version of the package. For Spring Boot users who love to just easy start with starters the following starter adding line would be enough:

```
dependencies {    
    compile('org.springframework.boot:spring-boot-starter-security')
}
```

Most basic start of the spring security is done by declaring the configuration bean:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory
            .getLogger(SecurityConfig.class);

    /**
     * Configure AuthenticationManager with inMemory credentials.
     *
     * @param auth       AuthenticationManagerBuilder
     * @throws Exception Authentication exception
     */
    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("rustam").password("123").roles("USER");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").access("hasRole('USER')")
                .and().formLogin()
                .and().httpBasic()
                .and().logout()

                // CSRF is enabled by default, with Java Config
                .and().csrf().disable();
    }
}
```

In above code we have two configure methods overriden, and they will block from accessing to basically any url "/**" all users who doesn't hold "USER" role (defined in a second configure method). Next obvious question how do we define which role who has, that is defined in a first configure() method. Since this is demonstrative example the authentication is performed in memory. 
- In the first configure we are declaring a user with the name "rustam" with the super-secret password "123" who has "USER" role after successful login. 
- Second configure() methods is more about Authorization which says to access "/**" user must have "USER" role. formLogin() calls default login page provided by Spring Security. 
- httpBasic() says the authentication is performed by HttpBasic method
- logout() says to make it available logout url at /logout. Here what official document says about it *The default is that accessing the URL "/logout" will log the user out by invalidating the HTTP Session, cleaning up any rememberMe() authentication that was configured, clearing the SecurityContextHolder, and then redirect to "/login?success*
- csrf().disable() says to disable CSRF prevention which is turned on by default
- and() is the chainer that is used to help the chain all these functionalities together

As it can be seen from above example Spring Security by default is the safe and easy runnable framework. At the same it time it is very flexible and reconfigurable framework. Almost any part of the framework is reconfigurable. 

# The Main Processes of the Spring Security

Roughly we can consider Spring Security as a two separate processes (anyhting out of these two can be considered as an additional feature):
1. Authentication Process
2. Authoriztion Process

![NoImage](/assets/spring-security/AuthenticationAndAuthorization.jpg)

## Authentication Process

Spring Security applies chain of filters to the incoming requests. When the user authentication request arrives to the server it goes through the chain of filters until it finds the relevant Authentication Filter based on the authentication mechanism. Here some examples of the authentication filters:

- BasicAuthenticationFilter
- DigestAuthenticationFilter
- UsernamePasswordAuthenticationFilter
- X509AuthenticationFilter

In our description we are considering the basic username and password based authentication that is why the UsernamePasswordAuthenticationFilter gets activated and sends the request to the UsernamePasswordAuthenticationToken module, which is going to create a token out of username and password arguments. Using the token object *authenticate* method of the AuthenticationManager will be invoked. AuthenticationManager is the interface with real implementation class depicted as *ProviderManager*. *ProviderManager* has a list of configured *AuthenticationProvider*(s):

- CasAuthenticationProvider
- JaasAuthenticationProvider
- DaoAuthenticationProvider
- OpenIDAuthenticationProvider
- RememberMeAuthenticationProvider
- LdapAuthenticationProvider

Depend on the provider the usage of the above created token would be different. For example DaoAuthenticationProvder is one of the provider that uses UserDetailsService for retrieving the user details based on username. Most of the cases the details are extracted from DB. 

```java
public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {	    
    // ... codes are skipped        
    
    private UserDetailsService userDetailsService;

    // ... codes are skipped    
    protected final UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		prepareTimingAttackProtection();
		try {
			UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
			if (loadedUser == null) {
				throw new InternalAuthenticationServiceException(
						"UserDetailsService returned null, which is an interface contract violation");
			}
			return loadedUser;
		}
		catch (UsernameNotFoundException ex) {
			mitigateAgainstTimingAttack(authentication);
			throw ex;
		}
		catch (InternalAuthenticationServiceException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
    }
    // ... more codes
}
```

To not get confused and ask question *where is the AuthenticationProvider ?*, the AbstractUserDetailsAuthenticationProvider implements it internally and calls *retrieveUser(...)* method while *authenticated(...)* method is called

```java
public abstract class AbstractUserDetailsAuthenticationProvider implements
		AuthenticationProvider, InitializingBean, MessageSourceAware {

    // ... codes are skipped 
    public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		// ... codes are skipped 
        try {
            user = retrieveUser(username,
                    (UsernamePasswordAuthenticationToken) authentication);
        }
        catch (UsernameNotFoundException notFound) {
            logger.debug("User '" + username + "' not found");

            if (hideUserNotFoundExceptions) {
                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
            }
            else {
                throw notFound;
            }
        }
        // ... more codes
	}
    // ... more codes
}
```

I think it would be little bit off topic, but i think i have to mention the variable *hideUserNotFoundExceptions* is something that hides the internal messages. So if in UserDetailService layer you would throw some exception which should be seen by the end user, then you should false this variable. Otherwise the end user will be getting only BadCredentialException. It can be set at the moment when you register your authentication provider object by *setHideUserNotFoundExceptions(false)*. [For Details](https://rusyasoft.github.io/spring-security/2018/12/31/spring-security-hide-exception/)

```java
public interface UserDetailsService
{
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```

As a result of the UserDetailsService must be implemented and inside the *loadUserByUsername()* developer has a full freedome to do whatever desires :). I mean usually the access to the DB is implemented, and if it is decided that authentication is successfully passed then the UserDtails object must be returned otherwise *UsernameNotFoundException* must be thrown. Based on return recurse return happens and AuthenticationObject is returned to the security context, otherwise AuthenticationException is thrown. If AuthenticationException is thrown, that will be handled by the configured AuthenticationEntryPoint that supports for the authentication mechanism. 

## Authorization Process

In order to describe about Authorization process the UserDetails should be studied first. UserDetails is the interface that must provide the GrantedAuthority objects, which are inserted into the Authentication object by the AuthenticationManager. Implementation of UserDetails must contain ```Collection<? extends GrantedAuthority> getAuthorities();``` method. This method allows *AccessDecisionManager*s to obtain a precise *String* representation of the *GrantedAuthority*.

*AccessDecisionManager* interface serves as a core strategy in Authorization process. There are three implementations provided by the framework and all three delegate to a chain of AccessDecisionVoter. AccessDecisionVoter considers a ConfigAttributes that are generated after Authentication process. As name says, AccessDecisionVoter decides allow the action or not based on Authentication and ConfigAttributes information.

```Java
boolean supports(ConfigAttribute attribute);

boolean supports(Class<?> clazz);

int vote(Authentication authentication, S object,
        Collection<ConfigAttribute> attributes);
```

In most cases the default AccessDecisionManager is AffirmativeBased (if no voters decline then access is granted). Any customization tends to happen in the voters, either adding new ones, or modifying the existing one. ConfigAttributes can be represented in Spring Expression Language (SpEL) expresssions, for example ```isFullyAuthenticated() && hasRole('FOO')```. AccessDecisionVoter can handle the expressions and create a context for them.



