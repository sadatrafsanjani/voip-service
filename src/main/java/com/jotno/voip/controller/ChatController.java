package com.jotno.voip.controller;

import com.jotno.voip.service.abstraction.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<?> createChannel(){

        return ResponseEntity.ok(chatService.createChannel());
    }

    @DeleteMapping("/{channelArn}")
    public ResponseEntity<?> deleteChannel(@PathVariable("channelArn") String channelArn){

        chatService.deleteChannel(channelArn);

        return ResponseEntity.noContent().build();
    }
}
