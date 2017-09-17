package com.barath.app;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
public class AppController {


    @GetMapping("/")
    public String home(){
        return "Welcome to Spring Security database Authentication example";
    }


    @GetMapping("/user")
    public String getUser(){

        Principal principal=(Principal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return "Welcome User "+principal.getName();
    }
}
