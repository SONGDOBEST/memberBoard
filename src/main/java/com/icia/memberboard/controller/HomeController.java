package com.icia.memberboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/main")
    public String loginMain(){
        return "memberPages/loginMain";
    }
}
