package com.boot.pf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Maximos on 3/4/2015.
 */
@Controller
public class LoginController {
    
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    
}
