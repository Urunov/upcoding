package com.urunov.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Table("role")
public class Role {

    @Id
    private String name;


    public Role(String name) {
        super();
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
