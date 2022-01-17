package com.jotno.voip.controller;

import com.jotno.voip.dto.chat.request.MessageRequest;
import com.jotno.voip.dto.chat.response.MessageResponse;
import com.jotno.voip.dto.chat.response.RoomResponse;
import com.jotno.voip.dto.chat.response.SendMessageResponse;
import com.jotno.voip.service.abstraction.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<SendMessageResponse> sendMessage(@RequestBody MessageRequest request){

        return new ResponseEntity<>(chatService.sendMessage(request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> listMessages(@RequestParam("userArn") String userArn, @RequestParam("channelArn") String channelArn){

        return new ResponseEntity<>(chatService.listMessages(userArn, channelArn), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteChannel(@RequestParam("channelArn") String channelArn, @RequestParam("creatorArn") String creatorArn){

        return new ResponseEntity<>(chatService.deleteChannel(channelArn, creatorArn), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/create-room")
    public ResponseEntity<RoomResponse> createRoom(){

        return new ResponseEntity<>(chatService.createRoom(), HttpStatus.CREATED);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable("id") long id){

        return new ResponseEntity<>(chatService.getRoomById(id), HttpStatus.CREATED);
    }
}
