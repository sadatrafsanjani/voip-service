package com.jotno.voip.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "patients")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "USER_ARN")
    private String userArn;
}
