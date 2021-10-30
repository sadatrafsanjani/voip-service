package com.jotno.voip.service;

import com.jotno.voip.dto.request.MeetingRequest;
import java.util.Map;

public interface MeetingService {

    Map<String, Object> generateMeetingSession(MeetingRequest request);
    Map<String, Object> getAttendeeInfo(String title, String attendeeId);
}
