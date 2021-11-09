package com.jotno.voip.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AttendeeResponse {

    @JsonProperty("AttendeeId")
    private String AttendeeId;
    @JsonProperty("ExternalUserId")
    private String ExternalUserId;
    @JsonProperty("JoinToken")
    private String JoinToken;
}
