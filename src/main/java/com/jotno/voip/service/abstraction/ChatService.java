package com.jotno.voip.service.abstraction;

public interface ChatService {

    String createChannel();
    void deleteChannel(String channelArn);
}
