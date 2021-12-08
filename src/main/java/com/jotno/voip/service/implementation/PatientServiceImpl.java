package com.jotno.voip.service.implementation;

import com.jotno.voip.dto.response.PatientResponse;
import com.jotno.voip.model.Patient;
import com.jotno.voip.repository.PatientRepository;
import com.jotno.voip.service.abstraction.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponse savePatient(String userArn){

        Patient patient = patientRepository.save(Patient.builder()
                        .userArn(userArn)
                .build());

        return PatientResponse.builder()
                .id(patient.getId())
                .userArn(patient.getUserArn())
                .build();
    }

    @Override
    public PatientResponse findByUserArn(String userArn){

        Patient patient = patientRepository.findByUserArn(userArn);

        return PatientResponse.builder()
                .id(patient.getId())
                .userArn(patient.getUserArn())
                .build();
    }
}
