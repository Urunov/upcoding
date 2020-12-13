package com.urunov.authentication;

import com.urunov.models.User;
import com.urunov.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

   @Autowired
   private UserService userService;

   @Bean
   public PasswordEncoder bCryptPasswordEncoder()
   {
       return new BCryptPasswordEncoder();
   }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       String email = authentication.getName();
       String password = authentication.getCredentials().toString();
       User user  = userService.getByEmail(email);
       if(user == null){
           throw new BadCredentialsException("1000");
       }

       if(!bCryptPasswordEncoder().matches(password, user.getPassword()))
       {
           throw new BadCredentialsException("1000");
       }
        return new UsernamePasswordAuthenticationToken(email, password, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class
        );
    }


}
