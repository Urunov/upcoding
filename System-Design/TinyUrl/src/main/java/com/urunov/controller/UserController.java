package com.urunov.controller;

import com.urunov.models.User;
import com.urunov.services.SecurityServiceImpl;
import com.urunov.services.UrlMappingService;
import com.urunov.services.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private UrlMappingService urlMappingService;

    @Autowired
    private SecurityServiceImpl securityServiceImpl;

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public void resetPassword(@RequestBody final Map<String, String> payload, HttpServletResponse response) throws IOException
    {
        // Validate the Password
        String password = payload.get("password");
        String confirm = payload.get("confirm");
        String email = payload.get("email");

        if(password.equals(confirm))
        {
            User user = userService.getByEmail(email);
            user.setPassword(bcrypt.encode(password));
            userService.save(user);
        }

        response.sendRedirect("/login");
    }

    public void getMyMapping(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException
    {
        HttpSession session = request.getSession();
        SecurityContext sc = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication auth = sc.
    }


}
