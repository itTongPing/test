package com.example.serviceribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {

    @RestController
    public class HelloControler {

        @Autowired
        HelloService helloService;

        @RequestMapping(value = "/hi")
        public String hi(@RequestParam String name) {
            return helloService.hiService(name);
        }

    }
}