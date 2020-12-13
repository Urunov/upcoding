package com.urunov.services;

import com.urunov.models.User;
import com.urunov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public User getByEmail(String email) {
        return userRepository.findOne(email);
    }

    @Override
    public User save(User user) {
       user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
       Set<String> set = new HashSet<String>();
       set.add("USER");
       user.setRoles(set);
       userRepository.save(user);
        return user;
    }

    @Override
    public Boolean isUser(String email) {
        return userRepository.exists(email);
    }
}
