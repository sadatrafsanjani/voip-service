package com.jotno.voip.service;

import java.util.Map;

public interface FirebaseService {

    void sendCallNotification(Map<String, Object> data, String deviceToken, String callerName);
}
