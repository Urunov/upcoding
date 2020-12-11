package com.urunov.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Configuration
@EnableWebSecurity
@PropertySource({"classpath:/configurations/cassandra.properties"})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {




}
