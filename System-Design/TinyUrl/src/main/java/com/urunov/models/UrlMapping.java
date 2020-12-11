package com.urunov.models;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import javax.persistence.Table;

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
@Table(catalog = "UrlMap")
public class UrlMapping {

    @PrimaryKey
    private String newUrl;
    private String oldUrl;
    private String email;

}
