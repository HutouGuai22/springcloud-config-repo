package ynu.edu.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RefreshScope
public class MyController {
    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/app-name")
    public String getAppName() {
        return appName;
    }
}
