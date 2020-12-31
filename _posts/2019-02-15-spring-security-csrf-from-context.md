---
title: How to get CSRF for Current Session
categories:
 - java
tags:
 - java, effective java, book, inheritance, composition
---

![How The CSRF Performed](/assets/images/springSecurity/HowTheCSRFPerformed.jpg)

As we know the CSRF is important and spring spec strongly [advises](https://docs.spring.io/autorepo/docs/spring-security/3.2.0.CI-SNAPSHOT/reference/html/csrf.html) to use it for any method except GET. Which is quite reasonable advise from the point of view web-security. The usage of CSRF token is very simple, recent version of spring security framework turns it on by default at the configuration level. In order to disable we have to do following in a configuration code:

```java
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends
   WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable();
  }
}
```

In order to use the CSRF we have to simply add the csrf related codes to into our html template.

```java

<!-- JSP case -->
<form action="${logoutUrl}" method="post">
  <input type="submit" value="Log out" />
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<!-- Thymeleaf case -->
<form action="${logoutUrl}" method="post">
  <input type="submit" value="Log out" />
  <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
</form>

```

We have to make sure when the submit happens we are sending a hidden object with name as ${_csrf.parameterName} and value ${_csrf.token}. The server would be waiting for those CSRF values, since it has generated it specifically for current session.

## What if JavaScript (SPA) need to be used

It is possible extract the CsrfToken from by requesting http session object. 

```java
@RequestMapping("/test")
public ModelAndView testController(HttpServletRequest request)  {
    CsrfToken csrfToken = new HttpSessionCsrfTokenRepository().loadToken(request);
    return modelAndView;
}
```

In the above example the CsrfToken instance would contain token related information(csrf key and value). Usually one csrf is used per one session. It means user receives csrf and keeps sending it until current session is terminated. If it is necessary it is possible to generate new csrf per request. It is important to remember the csrf is stored with session information. In case if the external storages or servers are used for storing session it should be taken into consideration.


## Bonus

Above example needs to have HttpServletRequest object in order to get csrf related information. Sometimes it may be necessary to request csrf information from the current user context. I found the way to implement it without having HttpServletRequest object. We only need to get access to the RequestContextHolder. RequestContextHolder exposes the web request in the form of a thread-bound RequestAttributeObjects. 

```java
public static CsrfToken getCurrentCsrfToken() {
    // quick-test
    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpSession session= attr.getRequest().getSession(false);
    if (session == null) {
        return null;
    }
    return (CsrfToken) session.getAttribute("org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN");
}
```

