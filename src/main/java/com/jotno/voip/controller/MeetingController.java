package com.jotno.voip.controller;

import com.jotno.voip.dto.response.AttendeeInfoResponse;
import com.jotno.voip.dto.request.MeetingRequest;
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

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinMeeting(@RequestBody MeetingRequest request) {

        return ResponseEntity.ok(meetingService.generateMeetingSession(request));
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
