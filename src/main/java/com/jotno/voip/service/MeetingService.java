package com.jotno.voip.service;

import com.jotno.voip.dto.request.CallRequest;
import java.util.Map;

public interface MeetingService {

    Map<String, Object> generateMeetingSession(CallRequest request);
    Map<String, Object> getAttendeeInfo(String title, String attendeeId);
}
