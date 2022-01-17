package com.jotno.voip.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "rooms")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name="CHANNEL_ARN")
    private String channelArn;

    @Column(name="DOCTOR_ARN")
    private String doctorArn;

    @Column(name="PATIENT_ARN")
    private String patientArn;
}
