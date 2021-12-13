package com.jotno.voip.service.domain;

import com.jotno.voip.dto.call.response.AttendeeInfo;
import com.jotno.voip.dto.call.response.AttendeeInfoResponse;

public class AttendeeInfoData {

    public static AttendeeInfoResponse toDto(AttendeeInfo attendeeInfo){

        return AttendeeInfoResponse.builder()
                .attendeeInfo(attendeeInfo)
                .build();
    }
}
