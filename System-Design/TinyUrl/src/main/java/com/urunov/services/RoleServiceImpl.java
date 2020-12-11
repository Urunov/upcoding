package com.urunov.services;

import com.urunov.models.Role;
import com.urunov.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getByName(String role) {

        roleRepository.getByName(role);
        return null;
    }
}
