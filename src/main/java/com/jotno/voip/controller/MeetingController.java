package com.jotno.voip.controller;

import com.jotno.voip.dto.request.CallRequest;
import com.jotno.voip.service.FirebaseService;
import com.jotno.voip.service.MeetingService;
import com.jotno.voip.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    private MeetingService meetingService;
    private FirebaseService firebaseService;
    private UserService userService;

    @Autowired
    public MeetingController(MeetingService meetingService,
                             FirebaseService firebaseService,
                             UserService userService) {
        this.meetingService = meetingService;
        this.firebaseService = firebaseService;
        this.userService = userService;
    }

    @PostMapping("/call")
    public ResponseEntity<?> initiateCall(@RequestBody CallRequest callerRequest) {

        callerRequest.setMeetingId(UUID.randomUUID().toString());

        CallRequest calleeRequest = CallRequest.builder()
                .meetingId(callerRequest.getMeetingId())
                .attendeeName(userService.getUsernameByPhoneNumber(callerRequest.getPhoneNo()))
                .build();

        Map<String, Object> callerResponse = meetingService.generateMeetingSession(callerRequest);
        Map<String, Object> calleeResponse = meetingService.generateMeetingSession(calleeRequest);

        if(callerResponse != null){

            userService.getUserDevicesByPhoneNumber(callerRequest.getPhoneNo()).forEach( device -> {
                firebaseService.sendCallNotification(calleeResponse, device);
            });

            return ResponseEntity.ok(callerResponse);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/attendee")
    public ResponseEntity<?> getAttendee(@RequestParam("title") String title, @RequestParam("attendee") String attendee) {

        Map<String, Object> response = meetingService.getAttendeeInfo(title, attendee);

        if(response != null){

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }
}
