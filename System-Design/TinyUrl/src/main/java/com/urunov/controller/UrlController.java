package com.urunov.controller;

import com.urunov.models.UrlMapping;
import com.urunov.services.UrlGeneratorService;
import com.urunov.services.UrlMappingService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.Map;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@RestController
public class UrlController {

    @Autowired
    private UrlGeneratorService urlGeneratorService;

    @Autowired
    private UrlMappingService urlMappingService;

    public void generateURL(@RequestBody final Map<String, String> map, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException
    {
        URL url;
        HttpURLConnection huc;
        String sOldUrl = map.get("src");
        try {
            url = new URL(sOldUrl);
            huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            int responseCode = huc.getResponseCode();
            if(responseCode == 404) response.sendError(404);

        }catch (MalformedInputException e){
            e.printStackTrace();
            response.sendError(404);
        }
        catch (IOException e){
            e.printStackTrace();
            response.sendError(404);
        }

        UrlMapping urlMapping = urlMappingService.findByOldUrl(sOldUrl);
        HttpSession session = request.getSession();
        SecurityContext sc = (SecurityContext) session.getAttribute("SPEING_SECURITY_CONTECT");
        Authentication authentication = sc.getAuthentication();

        JSONObject data = new JSONObject();

      data.put("statusCode", 200);

      if (urlMapping!=null) data.put("output", urlMapping.getNewUrl());

      urlMapping = new UrlMapping(urlGeneratorService.getNewUrl(sOldUrl), sOldUrl, (String) authentication.getPrincipal());
      data.put("output", "localhost:8080/urls/" + urlMappingService.save(urlMapping).getNewUrl());
      String sJson = data.toString();

        PrintWriter writer = response.getWriter();
        writer.write(sJson);
        writer.flush();
        writer.close();
    }

    public void getOriginalUrl(@PathVariable(value ="tinyUrl") String sTinyUrl, HttpServletResponse response) throws IOException
    {
        try {
            UrlMapping urlMapping = urlMappingService.getByNewUrl(sTinyUrl);
            if(urlMapping == null || urlMapping.getOldUrl() == null || urlMapping.getOldUrl().isEmpty())
            {
                response.sendError(404);
            }
            else
            {
                response.sendRedirect(urlMapping.getOldUrl());
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}
