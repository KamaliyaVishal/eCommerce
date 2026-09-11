package com.order_service.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Data
public class OrderDevController {

    @Value("${devVariable:NotDevProfile}")
    private String devVariable;

    @GetMapping("/devVariable")
    public String getDevVariable() {
        return devVariable;
    }
}
