package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.chat.request.MessageRequest;
import com.jotno.voip.dto.chat.response.ChannelResponse;
import com.jotno.voip.dto.chat.response.MessageResponse;
import com.jotno.voip.dto.chat.response.PatientResponse;
import com.jotno.voip.dto.chat.response.SendMessageResponse;
import java.util.List;

public interface ChatService {

    String createChannel();
    String deleteChannel(String channelArn);
    PatientResponse createMember();
    ChannelResponse addMemberToChannel(String memberArn);
    SendMessageResponse sendMessage(MessageRequest request);
    List<MessageResponse> listMessages(String userArn, String channelArn);
}
