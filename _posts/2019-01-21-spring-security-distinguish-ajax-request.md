---
title: Filter to Distinguish WebAPI and RestAPI Requests
categories:
 - spring-security
tags:
 - spring security, microservices, features, json, jackson, rest, filter
---

When we use Spring Security in a standard way, it performs quite a standard actions. For example, Spring Security blocks the un-authenticated requests to the resource (URL resource) and redirects (with 302) to the /login page. But sometimes we want different behavior. Customized behavior such as if the requester is web-browser we want to redirect them to the /login page. But if the requester is not a browser and purely generates RestAPI type of request, then redirection would have no sense. Of course, we could have written some logic on the requester client side to consider 302 as non-authorized behavior.

I have started searching for the solution to this issue. Couldn't find a solution with the standard Spring Security approach (couldn't find some kind of option for Spring Security which we could just turn on or off to get this behavior).

I have approached it with a filter solution, which registered at the configuration phase:

```java
@Override
protected void configure(final HttpSecurity http) throws Exception {
        http.addFilterBefore(new ContentTypeApplicationJsonFilter(domainName), FilterSecurityInterceptor.class);
}
```


Here is the filter itself:

```java
public class ContentTypeApplicationJsonFilter extends GenericFilterBean {

    private String domainName;
    private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();
    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    public ContentTypeApplicationJsonFilter(String domainName) {
        this.domainName = domainName;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {

            if (e instanceof AccessDeniedException) {
                HttpServletRequest rq = (HttpServletRequest) request;
                HttpServletResponse rs = (HttpServletResponse) response;

                if (isAjax(rq)) {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication == null || authenticationTrustResolver.isAnonymous(authentication)) {
                        rs.sendError(HttpStatus.UNAUTHORIZED.value(), domainName);
                    } else {
                        accessDeniedHandler.handle(rq, rs,
                                (AccessDeniedException) e);
                    }
                }
                else {
                    throw e;
                }
            }
        }
    }

    private Boolean isAjax(HttpServletRequest request) {
        return request.getContentType() != null &&
                request.getContentType().contains("application/json");
    }
}
```

The code of filter above is quite a simple one. ```filterChain.doFilter(request, response);``` simply joins to the filter chain and does nothing, until exception case happens and processing is started only when *AccessDeniedException* is thrown.

```java
if (isAjax(rq)) {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication == null || authenticationTrustResolver.isAnonymous(authentication)) {
                        rs.sendError(HttpStatus.UNAUTHORIZED.value(), domainName);
                    } else {
                        accessDeniedHandler.handle(rq, rs,
                                (AccessDeniedException) e);
                    }
                }
                else {
                    throw e;
                }

```

isAjax is the function that checks the request being json content-type or not. If it is confirmed to be json type then user checked being authenticated or not. This was required to distinguish the errors 401 and 403. *authenticationTrustResolver.isAnonymous* checks if the user is anonymous or not, if anonymous then definitely throwing UNAUTHORIZED 401 is suitable. In all other cases, the processing is given to AccessDeniedHandler who is going to throw 403 FORBIDDEN. 

In case if the traffic is not ajax (content type is not "application/json") then the exception is thrown and forwarded to the upper layer to process, where it will be decided by upper layers. And upper layer exception processor is Spring Security exception processor, who immediately redirects to /login page.

Little note here is while we are sending UNAUTHORIZED message back to the requester client we are inserting the message with domain name. It is given just in case if the requester client is smart enough to decide to for login page. But it is absolutely up to client to decide does it needs to be logged in or just keep its way out of it.

## Test with Java Script

In order to test above described behavior simple java script code can be used:

```
function callJson() {
        var xhttp = new XMLHttpRequest();
        xhttp.withCredentials = true;

        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                alert(this.responseText);
            }
        };

        xhttp.open("GET", "http://localhost:8080/necessaryAPIController", true);
        xhttp.setRequestHeader("Content-Type", "application/json");
        xhttp.send();
    }

...

<button type="submit" onclick="callJson()">callJson</button>

```

