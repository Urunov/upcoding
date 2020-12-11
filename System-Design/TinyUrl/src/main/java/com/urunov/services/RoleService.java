package com.urunov.services;

import com.urunov.models.Role;
import org.springframework.stereotype.Service;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Service
public interface RoleService {

    Role getByName(String role);
}
