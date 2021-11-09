package com.jotno.voip.service.implementation;

import com.google.gson.Gson;
import com.jotno.voip.service.FirebaseService;
import com.jotno.voip.utility.Constant;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FirebaseServiceImpl implements FirebaseService {

    @Override
    public void sendCallNotification(Map<String, Object> payload)  {

        log.info("FirebaseService sendCallNotification(): Entry");
        log.info("FirebaseService sendCallNotification(): Payload- " + payload);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + Constant.FIREBASE_AUTHORIZATION_KEY);
        httpHeaders.set("Content-Type", "application/json");

        Map<String,String> notification = new HashMap<>();
        notification.put("title", "This is the title");
        notification.put("body", "This is the body");

        JSONObject json = new JSONObject();
        json.put("notification", notification);
        json.put("data", new JSONObject(new Gson().toJson(payload)));
        json.put("to", Constant.FIREBASE_TOKEN_ID);

        HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), httpHeaders);
        String response = restTemplate.postForObject(Constant.FIREBASE_URL, httpEntity, String.class);

        log.info("FirebaseService sendCallNotification(): Response- " + response);
        log.info("FirebaseService sendCallNotification(): Exit");
    }
}
