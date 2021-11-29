package com.jotno.voip.controller;

import com.jotno.voip.dto.request.CallRequest;
import com.jotno.voip.dto.response.AttendeeInfoResponse;
import com.jotno.voip.dto.response.JoinInfoResponse;
import com.jotno.voip.service.abstraction.MeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    private MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/call")
    public ResponseEntity<?> initiateCall(@RequestBody CallRequest callerRequest) {

        JoinInfoResponse response = meetingService.initiateCall(callerRequest);

        if(response != null){

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/attendee")
    public ResponseEntity<?> getAttendee(@RequestParam("title") String title, @RequestParam("attendee") String attendee) {

        AttendeeInfoResponse response = meetingService.getAttendeeInfo(title, attendee);

        if(response != null){

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/reject/{receiverNo}")
    public ResponseEntity<?> rejectCall(@PathVariable("receiverNo") String receiverNo){

        meetingService.rejectCall(receiverNo);

        return ResponseEntity.ok().build();
    }
}
