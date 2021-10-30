package com.jotno.voip.dto.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingRequest {

    private String meetingId;
    private String userId;
    private String region = "us-east-1";
}
