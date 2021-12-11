package com.jotno.voip.service.implementation;

import com.jotno.voip.dto.response.AppointmentResponse;
import com.jotno.voip.model.Appointment;
import com.jotno.voip.repository.AppointmentRepository;
import com.jotno.voip.repository.PatientRepository;
import com.jotno.voip.service.abstraction.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private PatientRepository patientRepository;
    private AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(PatientRepository patientRepository,
                                  AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentResponse createAppointment(long id){

        Appointment appointment = appointmentRepository.save(Appointment.builder()
                .channel(null)
                .patient(patientRepository.getById(id))
                .status(false)
                .build());

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .channelArn(appointment.getChannel().getChannelArn())
                .userArn(appointment.getPatient().getUserArn())
                .status(appointment.isStatus())
                .build();
    }

    @Override
    public AppointmentResponse getById(long id){

        Appointment appointment = appointmentRepository.getById(id);

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .channelArn(appointment.getChannel().getChannelArn())
                .userArn(appointment.getPatient().getUserArn())
                .status(appointment.isStatus())
                .build();
    }

    @Override
    public List<AppointmentResponse> getAppointments(){

        List<AppointmentResponse> responses = new ArrayList<>();

        appointmentRepository.findAll().forEach(appointment -> {

            AppointmentResponse response = AppointmentResponse.builder()
                    .id(appointment.getId())
                    .channelArn(appointment.getChannel().getChannelArn())
                    .userArn(appointment.getPatient().getUserArn())
                    .status(appointment.isStatus())
                    .build();

            responses.add(response);
        });

        return responses;
    }

    @Override
    public void approveAppointment(long id){

        appointmentRepository.updateAppointmentStatus(id);
    }
}
