package com.jotno.voip.service;

import com.jotno.voip.dto.response.Payload;

public interface FirebaseService {

    String sendCallNotification(Payload payload);
}
