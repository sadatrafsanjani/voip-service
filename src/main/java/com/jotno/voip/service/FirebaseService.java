package com.jotno.voip.service;

import java.util.Map;

public interface FirebaseService {

    void sendCallNotification(Map<String, Object> payload, String client);
}
