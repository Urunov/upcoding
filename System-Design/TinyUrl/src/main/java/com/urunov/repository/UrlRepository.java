package com.urunov.repository;

import com.urunov.models.UrlMapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
public interface UrlRepository extends CrudRepository<UrlMapping, String> {

    @Query("select* from UrlMap where oldUrl=?0")
    public UrlMapping findByOldUrl(String sOldUrl);

    @Query("select* from UrlMap where email=?0 ALLOW FILTERING")
    public List<UrlMapping> findByEmail(String email);

    UrlMapping findOne(String sNewUrl);
}
