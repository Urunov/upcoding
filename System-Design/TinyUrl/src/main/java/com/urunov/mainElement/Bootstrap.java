package com.urunov.mainElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@SpringBootApplication
@SpringBootConfiguration
@ComponentScan(basePackages = {
        "com.urunov.models",
        "com.urunov.controller",
        "com.urunov.services",
        "com.urunov.models",
        "com.urunov.mainElement",
        "com.urunov.configuration"
})
@EnableCassandraRepositories("com.urunov.repository")
public class Bootstrap {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Bootstrap.class, args);
    }
}
