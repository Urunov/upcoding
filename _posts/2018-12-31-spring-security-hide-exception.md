---
title: Disable Hiding Exception Messages (Spring Security)
categories:
 - spring-security
tags:
 - books, review, spring security, microservices, introduction, features
---


We will need a UserDetailService implementation, which depends on each development requirements. 

```java
@Autowired
private CustomUserDetailsService userDetailsService;
```

I think the configureGlobal can be considered as a starting point. 

```java
@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authProvider());
}
```

AuthenticationProvider object with disabling hideUserNotFoundException should be created

```java
public AuthenticationProvider authProvider() {
    DaoAuthenticationProvider impl = new DaoAuthenticationProvider();
    impl.setUserDetailsService(userDetailsService);
    impl.setPasswordEncoder(getPasswordEncoder());//new BCryptPasswordEncoder());
    impl.setHideUserNotFoundExceptions(false) ;
    return impl;
}
```

in order to process exceptions we have to register custom AuthenticationFailureHandler at the HttpSecurity.

```java
http.formLogin()
    .loginPage("/login")
    .failureHandler(new CustomAuthenticationFailureHandler())
```

Custom authentication failure handler may simply encode the error message to be fit into the query request argument

```java
public class    CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String exceptionMessage = exception.getMessage();

        String encodedMsg = Base64.getUrlEncoder().encodeToString(exceptionMessage.getBytes());

        response.sendRedirect("/login?error=" + encodedMsg);
    }
}
```

Finally login controller which gets the message and organizes the string into view model

```java
@Controller
public class Login {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(Model model, String error, String logout) {
        ModelAndView modelAndView = new ModelAndView("login");
        if (error != null) {
            byte[] decodedBytes = Base64.getDecoder().decode(error);
            String decodedString = new String(decodedBytes);
            modelAndView.addObject("errorMsg", decodedString );
        }
        return modelAndView;
    }
}
```

## References

- https://stackoverflow.com/questions/46256206/spring-boot-security-custom-messages-while-user-login
- https://stackoverflow.com/questions/17439628/spring-security-custom-exception-message-from-userdetailsservice
- http://forum.spring.io/forum/spring-projects/security/93883-security-exception-when-login-turn-different
