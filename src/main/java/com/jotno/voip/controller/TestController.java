package com.jotno.voip.controller;

import com.jotno.voip.dto.response.Payload;
import com.jotno.voip.service.FirebaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@Slf4j
public class TestController {

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping
    public String index(){

        Map<String,String> data = new HashMap<>();
        data.put("K1", "V1");

        Payload payload = Payload.builder()
                .title("This is a title")
                .body("This is body")
                .data(data)
                .build();

        String response = firebaseService.sendCallNotification(payload);

        log.info("TestController: index()" + response);

        return "<h1>Live</h1>";
    }
}
