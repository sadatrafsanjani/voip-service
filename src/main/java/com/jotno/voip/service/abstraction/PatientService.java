package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.response.PatientResponse;

public interface PatientService {

    PatientResponse savePatient(String userArn);
    PatientResponse findByUserArn(String userArn);
}
