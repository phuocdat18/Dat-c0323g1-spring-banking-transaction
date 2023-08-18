package com.cg.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/cp")
    public String showHomePage() {
        return "cp/home";
    }

    @GetMapping("/test")
    public String showTestPage() {
        return "cp/test";
    }
}
