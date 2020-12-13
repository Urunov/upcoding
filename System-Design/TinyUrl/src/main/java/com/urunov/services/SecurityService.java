package com.urunov.services;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
public interface SecurityService {

    String findLoggedInEmail();

    void autoLogin(String email, String password);
}
