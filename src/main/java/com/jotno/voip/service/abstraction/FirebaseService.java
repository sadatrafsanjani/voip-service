package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.call.request.CallRequest;
import com.jotno.voip.dto.call.response.JoinInfoResponse;

public interface FirebaseService {

    void sendCallNotification(JoinInfoResponse data, String deviceToken, CallRequest request);
    void sendCallRejectNotification(String deviceToken);
}
