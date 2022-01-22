package com.jotno.voip.dto.chat.response;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserResponse {

    private String channelArn;
    private String userArn;
}
