package com.jotno.voip.controller;

import com.jotno.voip.dto.request.MemberRequest;
import com.jotno.voip.dto.request.MessageRequest;
import com.jotno.voip.dto.response.ChannelResponse;
import com.jotno.voip.dto.response.PatientResponse;
import com.jotno.voip.service.abstraction.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/createChannel")
    public ResponseEntity<?> createChannel(){

        return new ResponseEntity<>(chatService.createChannel(), HttpStatus.OK);
    }

    @PostMapping("/createMember")
    public ResponseEntity<?> createMember(){

        return new ResponseEntity<>(chatService.createMember(), HttpStatus.CREATED);
    }

    @PostMapping("/addMember")
    public ResponseEntity<?> addMemberToChannel(@RequestBody MemberRequest request){

        return new ResponseEntity<>(chatService.addMemberToChannel(request.getMemberArn()), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest request){

        return new ResponseEntity<>(chatService.sendMessage(request), HttpStatus.OK);
    }

    @GetMapping("/listMessages")
    public ResponseEntity<?> listMessages(@RequestParam("userArn") String userArn, @RequestParam("channelArn") String channelArn){

        return new ResponseEntity<>(chatService.listMessages(userArn, channelArn), HttpStatus.OK);
    }

    @DeleteMapping("/{channelArn}")
    public ResponseEntity<?> deleteChannel(@PathVariable("channelArn") String channelArn){

        return new ResponseEntity<>(chatService.deleteChannel(channelArn), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testChat(){

        String channel = chatService.createChannel();
        PatientResponse patientResponse = chatService.createMember();
        log.info(patientResponse.toString());
        chatService.addMemberToChannel(patientResponse.getUserArn());

        return new ResponseEntity<>(channel, HttpStatus.CREATED);
    }
}
