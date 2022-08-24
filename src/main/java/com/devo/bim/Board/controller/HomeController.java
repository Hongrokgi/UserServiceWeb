package com.devo.bim.Board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String main() {
        return "home";
    }
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "common/login";
    }

    @GetMapping("/authFail")
    public String error() {
        return "authFail";
    }
    @GetMapping("/denied")
    public String denied() {
        return "denied";
    }
    @GetMapping("/badRequest")
    public String badRequest() {
        return "badRequest";
    }
}
