package com.urunov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Service
public class SecurityServiceImpl implements SecurityService{

   @Autowired
   private AuthenticationManager authenticationManager;

   @Autowired
   private UserDetailsService userDetailsService;

    @Override
    public String findLoggedInEmail() {

        SecurityContext sc = SecurityContextHolder.getContext();
        Authentication token = sc.getAuthentication();
        Object userDetails = token.getDetails();
        if(userDetails instanceof UserDetails)
            return ((UserDetails) userDetails).getUsername();
        return null;
    }

    @Override
    public void autoLogin(String email, String password) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        authenticationManager.authenticate(token);

        if(token.isAuthenticated())
        {
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }
}
