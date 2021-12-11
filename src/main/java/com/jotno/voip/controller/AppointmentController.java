package com.jotno.voip.controller;

import com.jotno.voip.dto.request.AppointmentRequest;
import com.jotno.voip.service.abstraction.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<?> getAppointments(){

        return new ResponseEntity<>(appointmentService.getAppointments(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> applyForAppointment(@RequestBody AppointmentRequest request){

        return new ResponseEntity<>(appointmentService.createAppointment(request.getUserId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> approveAppointment(@PathVariable("id") long id){

        appointmentService.approveAppointment(id);

        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
}
