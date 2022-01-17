package com.jotno.voip.service.abstraction;

import com.jotno.voip.dto.chat.request.MessageRequest;
import com.jotno.voip.dto.chat.response.*;
import java.util.List;

public interface ChatService {

    SendMessageResponse sendMessage(MessageRequest request);
    List<MessageResponse> listMessages(String userArn, String channelArn);
    RoomResponse createRoom();
    String deleteChannel(String channelArn, String creatorArn);
    RoomResponse getRoomById(long id);
}
