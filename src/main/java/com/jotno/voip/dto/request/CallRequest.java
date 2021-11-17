package com.jotno.voip.dto.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CallRequest {

    private String meetingId;
    private String attendeeName;
    private String phoneNo;
}
