package com.urunov.models;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.persistence.Entity;
import java.util.Set;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("user")
public class User {

    @PrimaryKey
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Set<String> roles;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email= email;
        this.password = password;
    }
}
