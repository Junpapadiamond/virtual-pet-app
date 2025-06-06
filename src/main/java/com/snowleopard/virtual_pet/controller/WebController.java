package com.snowleopard.virtual_pet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "index"; // This will look for index.html in static folder
    }

    // Catch-all for React Router - only for non-API routes
    @RequestMapping(value = {
            "/dashboard",
            "/create-pet",
            "/pet/**",
            "/profile",
            "/test"
    })
    public String reactRoutes() {
        return "forward:/index.html";
    }
}