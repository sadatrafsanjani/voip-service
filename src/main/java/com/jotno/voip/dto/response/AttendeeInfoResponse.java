package com.jotno.voip.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendeeInfoResponse {

    @JsonProperty("AttendeeId")
    private String AttendeeId;
    @JsonProperty("Name")
    private String Name;
}
