package com.urunov.services;

import com.urunov.models.UrlMapping;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;


import java.util.List;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
public interface UrlMappingService {

    @Cacheable(value = "UrlMapping", key = "#UrlMapping.newUrl")
    UrlMapping getByNewUrl(String sNewUrl);

    @CachePut(value = "UrlMapping", key = "#UrlMapping.newUrl")
    UrlMapping save(UrlMapping urlMapping);

    UrlMapping findByOldUrl(String sOldUrl);
    List<UrlMapping> findByEmail(String email);
}



