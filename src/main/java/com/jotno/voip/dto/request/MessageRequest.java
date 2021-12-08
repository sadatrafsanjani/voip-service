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

    private String userArn;
    private String channelArn;
    private String clientRequestToken;
    private String content;
    @Builder.Default
    private String persistence = "PERSISTENT";
    @Builder.Default
    private String type = "STANDARD";
}
