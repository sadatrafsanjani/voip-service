package com.jotno.voip.dto.call.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AttendeeInfoResponse {

    @JsonProperty("AttendeeInfo")
    private AttendeeInfo attendeeInfo;
}
