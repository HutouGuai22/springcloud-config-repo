package ynu.edu.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class MyController {
    @GetMapping("/message")
    public String getMessage() {
        return "Hello, World!";
    }
}
