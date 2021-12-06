package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.request.MessageRequest;

public interface ChatService {

    String createChannel();
    void deleteChannel(String channelArn);
    String createMember();
    void addMemberToChannel(String memberArn);
    void sendMessage(MessageRequest request);
}
