package com.urunov.authentication;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */


public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public JsonUsernamePasswordAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher("/api/login", "POST"));

    }

    public JsonUsernamePasswordAuthenticationFilter() {
        super();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {


        if(!HttpMethod.POST.name().equals(httpServletRequest.getMethod())){
            if(logger.isDebugEnabled()){
                logger.debug("Authentication method not supported. Request method:" + httpServletRequest.getMethod());
            }
            throw new AuthenticationException("Authentication method not supported") {
            };
        }

        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        BufferedReader reader = httpServletRequest.getReader();
        while((line=reader.readLine()) !=null)
        {
            stringBuffer.append(line);
        }

        JSONObject jsonObject = null;
        Authentication token = null;
        try {
            jsonObject = new JSONObject(stringBuffer.toString());
            String username = (String) jsonObject.get("username");
            String password = (String) jsonObject.get("password");
          token = new UsernamePasswordAuthenticationToken(username, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        token = this.getAuthenticationManager().authenticate(token);
        if(token.isAuthenticated())
        {
            SecurityContextHolder.getContext().setAuthentication(token);
            HttpSession session = httpServletRequest.getSession(true);

            session.setAttribute("SPRING SECURITY CONTEXT", SecurityContextHolder.getContext());
        }
        return token;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        JSONObject data = new JSONObject();

        try {
            data.put("statusCode", 200);
            data.put("message", "Success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sJson = data.toString();
        PrintWriter writer = response.getWriter();
        writer.write(sJson);
        writer.flush();
        writer.close();
        getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
      //  super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected AuthenticationSuccessHandler getSuccessHandler() {
        return super.getSuccessHandler();
    }

    @Autowired
    @Qualifier
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
