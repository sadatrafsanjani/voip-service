package com.jotno.voip.repository;

import com.jotno.voip.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByUsAndUserArn(String userArn);
}