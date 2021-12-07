package com.jotno.voip.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "appointments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "CHANNEL_ID")
    private Channel channel;

    @OneToOne
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;

    @Column(name = "STATUS")
    private boolean status;
}
