package com.urunov.repository;

import com.urunov.models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
public interface UserRepository extends CrudRepository<User, String> {
    Boolean exists(String email);

    User findOne(String email);
}
