package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.request.MessageRequest;
import com.jotno.voip.dto.response.ChannelResponse;
import com.jotno.voip.dto.response.MessageResponse;
import com.jotno.voip.dto.response.PatientResponse;
import com.jotno.voip.dto.response.SendMessageResponse;
import java.util.List;

public interface ChatService {

    String createChannel();
    String deleteChannel(String channelArn);
    PatientResponse createMember();
    ChannelResponse addMemberToChannel(String memberArn);
    SendMessageResponse sendMessage(MessageRequest request);
    List<MessageResponse> listMessages(String userArn, String channelArn);
}
