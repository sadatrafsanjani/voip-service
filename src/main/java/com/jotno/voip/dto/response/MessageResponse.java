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
public class MessageResponse {

    private String messageId;
    private String userArn;
    private String username;
    private String content;
    private String createdTime;
}
