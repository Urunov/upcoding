package com.urunov.controller;

import com.urunov.models.User;
import com.urunov.services.SecurityService;
import com.urunov.services.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */

@Controller
@RequestMapping(value = "/api/")
public class WebController {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = {"/", "/login", "/register"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }


    public void registration(@RequestBody final Map<String, String>map, Model model, HttpServletResponse response) throws IOException, JSONException
    {
        User user = new User(map.get("firstName"), map.get("lastName"), map.get("email"), map.get("password"));
        if(VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail()).matches() && !user.getPassword().isEmpty() && userService.getByEmail(user.getEmail()) == null)
        {
            userService.save(user);
            //securityService.autoLogin(user.getEmail(), user.getPassword());
            JSONObject data = new JSONObject();
            data.put("statusCode", 200);
            data.put("message", "Success");
            String sJson = data.toString();
            PrintWriter writer = response.getWriter();
            writer.write(sJson);
            writer.flush();
            writer.close();
        }
        response.getWriter().write("{statusCode:401}");
    }



}
