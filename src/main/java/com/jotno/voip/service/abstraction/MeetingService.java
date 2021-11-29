package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.request.CallRequest;
import com.jotno.voip.dto.response.AttendeeInfoResponse;
import com.jotno.voip.dto.response.JoinInfoResponse;

public interface MeetingService {

    AttendeeInfoResponse getAttendeeInfo(String title, String attendeeId);
    void rejectCall(String receiverNo);
    JoinInfoResponse initiateCall(CallRequest callerRequest);
}
