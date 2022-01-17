package com.jotno.voip.dto.chat.request;

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
    private String content;
}
