package com.fastshipmentsdev.backend_fastshipments.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping("/prova")
    public String test(){
        return "test page";
    }

}
