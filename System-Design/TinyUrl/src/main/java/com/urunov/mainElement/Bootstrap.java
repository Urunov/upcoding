package com.urunov.mainElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@SpringBootApplication
@SpringBootConfiguration
@ComponentScan(basePackages = {
        "com.urunov.models"
})
public class Bootstrap {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Bootstrap.class, args);
    }
}
