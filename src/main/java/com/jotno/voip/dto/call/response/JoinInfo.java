package com.jotno.voip.dto.call.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JoinInfo {

    @JsonProperty("Title")
    private String Title;
    @JsonProperty("Meeting")
    private Map<String, Object> Meeting;
    @JsonProperty("Attendee")
    private AttendeeResponse Attendee;
}
