package com.community.world.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {

    @GetMapping("/home")
    public String getMemberHome() {
        return "home";
    }
}
