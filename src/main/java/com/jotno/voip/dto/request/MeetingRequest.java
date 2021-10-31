package com.jotno.voip.dto.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MeetingRequest {

    private String meetingId;
    private String attendeeName;
    @Builder.Default
    private String region = "us-east-1";
}
