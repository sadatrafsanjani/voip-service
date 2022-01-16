package com.jotno.voip.dto.chat.response;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomResponse {

    private Long id;
    private String channelArn;
    private String doctorArn;
    private String patientArn;
}
