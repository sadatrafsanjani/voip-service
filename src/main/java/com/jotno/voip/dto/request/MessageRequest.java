package com.jotno.voip.dto.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MessageRequest {

    private String clientRequestToken;
    private String content;
    private String persistence = "PERSISTENT";
    private String type = "STANDARD";
}
