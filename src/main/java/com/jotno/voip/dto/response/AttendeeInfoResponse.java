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
public class AttendeeInfoResponse {

    @JsonProperty("AttendeeInfo")
    private AttendeeInfo attendeeInfo;
}
