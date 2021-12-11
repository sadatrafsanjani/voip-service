package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.response.AppointmentResponse;
import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(long id);
    List<AppointmentResponse> getAppointments();
    void approveAppointment(long id);
    AppointmentResponse getById(long id);
}
