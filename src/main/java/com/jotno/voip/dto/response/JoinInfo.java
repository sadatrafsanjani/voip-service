package com.jotno.voip.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
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
