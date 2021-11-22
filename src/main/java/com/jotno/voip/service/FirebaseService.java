package com.jotno.voip.service;

import com.jotno.voip.dto.request.CallRequest;
import java.util.Map;

public interface FirebaseService {

    void sendCallNotification(Map<String, Object> data, String deviceToken, CallRequest request);
    void sendCallRejectNotification(String deviceToken);
}
