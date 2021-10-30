package com.jotno.voip.dto.response;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendeeResponse {

    private String AttendeeId;
    private String ExternalUserId;
    private String JoinToken;
}
