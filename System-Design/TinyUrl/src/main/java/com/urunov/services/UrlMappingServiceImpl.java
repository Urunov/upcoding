package com.urunov.services;

import com.urunov.models.UrlMapping;
import com.urunov.repository.UrlRepository;
import com.urunov.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Service
public class UrlMappingServiceImpl implements UrlMappingService {

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public UrlMapping getByNewUrl(String sNewUrl) {
        UrlMapping url = urlRepository.findOne(sNewUrl);
        return url;
    }

    @Override
    public UrlMapping save(UrlMapping urlMapping) {
        UrlMapping url = urlRepository.save(urlMapping);
        return url;
    }

    @Override
    public UrlMapping findByOldUrl(String sOldUrl) {
       try {
           return urlRepository.findByOldUrl(sOldUrl);
       }
       catch (Exception e){
           return null;
       }
    }

    @Override
    public List<UrlMapping> findByEmail(String email) {
        return urlRepository.findByEmail(email);
    }
}
