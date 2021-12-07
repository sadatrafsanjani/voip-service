package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.request.MessageRequest;
import com.jotno.voip.dto.response.MessageResponse;
import com.jotno.voip.dto.response.SendMessageResponse;
import java.util.List;

public interface ChatService {

    String createChannel();
    String deleteChannel(String channelArn);
    String createMember();
    String addMemberToChannel(String memberArn);
    SendMessageResponse sendMessage(MessageRequest request);
    List<MessageResponse> listMessages(String userArn, String channelArn);
}
