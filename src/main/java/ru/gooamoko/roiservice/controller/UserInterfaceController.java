package ru.gooamoko.roiservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class UserInterfaceController {

    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }
}
