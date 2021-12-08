package com.jotno.voip.dto.response;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AppointmentResponse {

    private long id;
    private String channelArn;
    private String userArn;
    private boolean status;
}
