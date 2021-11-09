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

    @PostMapping("/join")
    public ResponseEntity<?> joinMeeting(@RequestBody MeetingRequest request) {

        log.info("MeetingController joinMeeting(): Entry");
        log.info("MeetingController joinMeeting(): MeetingRequest- " + request);

        MeetingRequest remoteRequest = MeetingRequest.builder()
                .meetingId(request.getMeetingId())
                .attendeeName("Remote user Name")
                .build();

        Map<String, Object> selfResponse = meetingService.generateMeetingSession(request);
        Map<String, Object> remoteResponse = meetingService.generateMeetingSession(remoteRequest);

        firebaseService.sendCallNotification(remoteResponse);

        if(selfResponse != null){

            log.info("MeetingController joinMeeting(): Success- Exit");

            return ResponseEntity.ok(selfResponse);
        }

        log.info("MeetingController joinMeeting(): Failure- Exit");

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
