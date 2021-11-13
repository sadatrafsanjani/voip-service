package com.jotno.voip.controller;

import com.jotno.voip.dto.request.MeetingRequest;
import com.jotno.voip.service.FirebaseService;
import com.jotno.voip.service.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    private MeetingService meetingService;
    private FirebaseService firebaseService;

    @Autowired
    public MeetingController(MeetingService meetingService,
                             FirebaseService firebaseService) {
        this.meetingService = meetingService;
        this.firebaseService = firebaseService;
    }

    @PostMapping("/call")
    public ResponseEntity<?> initiateCall(@RequestBody MeetingRequest callerRequest) {

        log.info("MeetingController initiateCall(): Entry");
        log.info("MeetingController initiateCall(): MeetingRequest- " + callerRequest);

        MeetingRequest calleeRequest = MeetingRequest.builder()
                .meetingId(callerRequest.getMeetingId())
                .attendeeName("Remote User")
                .build();

        Map<String, Object> callerResponse = meetingService.generateMeetingSession(callerRequest);
        Map<String, Object> calleeResponse = meetingService.generateMeetingSession(calleeRequest);

        if(callerResponse != null){

            log.info("MeetingController initiateCall(): Success- Exit");
            firebaseService.sendCallNotification(calleeResponse, callerRequest.getClient());

            return ResponseEntity.ok(callerResponse);
        }

        log.info("MeetingController initiateCall(): Failure- Exit");

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/attendee")
    public ResponseEntity<?> getAttendee(@RequestParam("title") String title, @RequestParam("attendee") String attendee) {

        log.info("MeetingController getAttendee(): Entry");
        log.info("MeetingController getAttendee(): Params- title: " + title + " attendee: " + attendee);

        Map<String, Object> response = meetingService.getAttendeeInfo(title, attendee);

        if(response != null){

            log.info("MeetingController getAttendee(): Success- Exit");

            return ResponseEntity.ok(response);
        }

        log.info("MeetingController getAttendee(): Failure- Exit");

        return ResponseEntity.notFound().build();
    }
}
