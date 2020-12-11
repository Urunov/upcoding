package com.urunov.repository;

import com.urunov.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, String> {

    Role getByName(String role);
}
