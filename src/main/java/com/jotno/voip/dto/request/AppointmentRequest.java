package com.jotno.voip.dto.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AppointmentRequest {

    private long userId;
}
