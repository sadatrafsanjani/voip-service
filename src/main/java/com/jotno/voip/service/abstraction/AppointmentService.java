package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.response.AppointmentResponse;

public interface AppointmentService {

    AppointmentResponse createAppointment(long id);
}
