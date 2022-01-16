package com.jotno.voip.controller;

import com.jotno.voip.dto.chat.request.MessageRequest;
import com.jotno.voip.dto.chat.response.RoomResponse;
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

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest request){

        return new ResponseEntity<>(chatService.sendMessage(request), HttpStatus.OK);
    }

    @GetMapping("/listMessages")
    public ResponseEntity<?> listMessages(@RequestParam("userArn") String userArn, @RequestParam("channelArn") String channelArn){

        return new ResponseEntity<>(chatService.listMessages(userArn, channelArn), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteChannel(@RequestParam("channelArn") String channelArn){

        return new ResponseEntity<>(chatService.deleteChannel(channelArn), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/create-room")
    public ResponseEntity<RoomResponse> createRoom(){

        return new ResponseEntity<>(chatService.createRoom(), HttpStatus.CREATED);
    }
}
