package com.ominext.trainning.pharmacy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {

    @GetMapping("/error-page")
    public String getAccessDenied() {
        return "403";
    }
}