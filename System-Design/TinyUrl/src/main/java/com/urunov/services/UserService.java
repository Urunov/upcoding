package com.urunov.services;

import com.urunov.models.User;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
public interface UserService {

    User getByEmail(String email);
    User save(User user);
    Boolean isUser(String email);
}
