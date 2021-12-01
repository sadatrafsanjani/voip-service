package com.jotno.voip.service.implementation;

import com.google.gson.Gson;
import com.jotno.voip.dto.request.CallRequest;
import com.jotno.voip.dto.response.JoinInfoResponse;
import com.jotno.voip.service.abstraction.FirebaseService;
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
    public void sendCallNotification(JoinInfoResponse data, String deviceToken, CallRequest request)  {

        log.info("FirebaseService sendCallNotification(): Entry");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + Constant.FIREBASE_AUTHORIZATION_KEY);
        httpHeaders.set("Content-Type", "application/json");

        Map<String,Object> body = new HashMap<>();
        body.put("action", "INCOMING_CALL");
        body.put("title", request.getAttendeeName());
        body.put("body", "Incoming");
        body.put("callerNo", request.getSenderPhoneNo());
        body.put("data", data);

        JSONObject json = new JSONObject();
        json.put("priority", "high");
        json.put("data", new JSONObject(new Gson().toJson(body)));
        json.put("to", deviceToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), httpHeaders);
        String response = restTemplate.postForObject(Constant.FIREBASE_URL, httpEntity, String.class);

        if(response != null){

            log.info("FirebaseService sendCallNotification(): Response- " + response);
            log.info("FirebaseService sendCallNotification(): Exit- Success");
        }
        else{
            log.info("FirebaseService sendCallNotification(): Exit- Failure");
        }
    }

    @Override
    public void sendCallRejectNotification(String deviceToken){

        log.info("FirebaseService sendCallRejectNotification(): Entry");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + Constant.FIREBASE_AUTHORIZATION_KEY);
        httpHeaders.set("Content-Type", "application/json");

        Map<String,Object> body = new HashMap<>();
        body.put("action", "REJECT_CALL");
        body.put("title", "Reject");
        body.put("body", "Call Rejection");
        body.put("data", null);

        JSONObject json = new JSONObject();
        json.put("priority", "high");
        json.put("data", new JSONObject(new Gson().toJson(body)));
        json.put("to", deviceToken);

        log.info("FirebaseService sendCallRejectNotification(): JSON- " + json);
        HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), httpHeaders);
        String response = restTemplate.postForObject(Constant.FIREBASE_URL, httpEntity, String.class);
        log.info("FirebaseService sendCallRejectNotification(): Response- " + response);
    }
}