package com.jotno.voip.service.implementation;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.jotno.voip.dto.response.Payload;
import com.jotno.voip.service.FirebaseService;
import com.jotno.voip.utility.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FirebaseServiceImpl implements FirebaseService {

    private FirebaseMessaging firebaseMessaging;

    @Autowired
    public FirebaseServiceImpl(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public String sendCallNotification(Payload payload)  {

        log.info("FirebaseService sendCallNotification(): Entry");

        Notification notification = Notification.builder()
                .setTitle(payload.getTitle())
                .setBody(payload.getBody())
                .build();

        Message message = Message.builder()
                .setToken(Constant.FIREBASE_TOKEN_ID)
                .setNotification(notification)
                .putAllData(payload.getData())
                .build();

        try {

            log.info("FirebaseService sendCallNotification(): Exit- Success");

            return firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.debug(e.getMessage());
        }

        log.info("FirebaseService sendCallNotification(): Exit- Failure");

        return null;
    }
}
