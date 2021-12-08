package com.jotno.voip.dto.response;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PatientResponse {

    private long id;
    private String userArn;
}
