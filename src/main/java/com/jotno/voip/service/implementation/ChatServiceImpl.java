package com.jotno.voip.service.implementation;

import com.jotno.voip.dto.chat.request.MessageRequest;
import com.jotno.voip.dto.chat.response.ChannelResponse;
import com.jotno.voip.dto.chat.response.MessageResponse;
import com.jotno.voip.dto.chat.response.PatientResponse;
import com.jotno.voip.dto.chat.response.SendMessageResponse;
import com.jotno.voip.service.abstraction.ChatService;
import com.jotno.voip.utility.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chime.ChimeClient;
import software.amazon.awssdk.services.chime.model.*;
import com.jotno.voip.utility.Formatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private ChimeClient chimeClient;
    private String doctorUserArn;
    private String channelArn;

    @Autowired
    public ChatServiceImpl() {

        this.chimeClient = ChimeClient.builder()
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(() -> AwsBasicCredentials.create(
                        Constant.AWS_ACCESS_KEY_ID,
                        Constant.AW_SECRET_ACCESS_KEY
                ))
                .build();
    }

    /*
    *
    * Create Channel
    * Request: Nothing
    * Returns: Channel ARN
    * */
    @Override
    public String createChannel(){

        CreateAppInstanceUserRequest createAppInstanceUserRequest = CreateAppInstanceUserRequest.builder()
                .appInstanceUserId("doctor-101")
                .appInstanceArn(Constant.AWS_APP_INSTANCE_ARN)
                .name("DOCTOR")
                .build();

        CreateAppInstanceUserResponse userResponse = chimeClient.createAppInstanceUser(createAppInstanceUserRequest);
        doctorUserArn = userResponse.appInstanceUserArn();
        log.info(doctorUserArn);

        CreateChannelRequest createChannelRequest = CreateChannelRequest.builder()
                .chimeBearer(userResponse.appInstanceUserArn())
                .appInstanceArn(Constant.AWS_APP_INSTANCE_ARN)
                .clientRequestToken("jotno-web-client")
                .mode("UNRESTRICTED")
                .name("appointment-101")
                .privacy("PRIVATE")
                .build();

        CreateChannelResponse response = chimeClient.createChannel(createChannelRequest);
        channelArn = response.channelArn();

        return response.channelArn();
    }

    /*
     *
     * Create a user for channel communication
     * Request: Nothing
     * Returns: Member ARN
     * */
    @Override
    public PatientResponse createMember(){

        CreateAppInstanceUserRequest createAppInstanceUserRequest = CreateAppInstanceUserRequest.builder()
                .appInstanceUserId("patient-" + UUID.randomUUID())
                .appInstanceArn(Constant.AWS_APP_INSTANCE_ARN)
                .name("PATIENT")
                .build();

        CreateAppInstanceUserResponse userResponse = chimeClient.createAppInstanceUser(createAppInstanceUserRequest);
        log.info("Patient Created: " + userResponse.appInstanceUserArn());

        return PatientResponse.builder().userArn(userResponse.appInstanceUserArn()).build();
    }

    /*
     *
     * Add member to a channel
     * Request: Member Arn of the user whom we want to add to a channel
     * Returns: Nothing
     * */
    @Override
    public ChannelResponse addMemberToChannel(String memberArn){

        //memberArn = Constant.AWS_PREFIX + "/user/" + memberArn;

        CreateChannelMembershipRequest request = CreateChannelMembershipRequest.builder()
                .chimeBearer(doctorUserArn)
                .memberArn(memberArn)
                .channelArn(channelArn)
                .type("DEFAULT")
                .build();

        CreateChannelMembershipResponse response = chimeClient.createChannelMembership(request);
        log.info(response.toString());

        return ChannelResponse.builder().channelArn(response.channelArn()).build();
    }


    /*
     *
     * Delete a created channel
     * Request: Channel Arn that we want to delete
     * Returns: Nothing
     * */
    @Override
    public String deleteChannel(String channelArn){

        channelArn = Constant.AWS_PREFIX + "/channel/" + channelArn;

        DeleteChannelRequest deleteChannelRequest = DeleteChannelRequest.builder()
                .chimeBearer(doctorUserArn)
                .channelArn(channelArn)
                .build();

        DeleteChannelResponse response = chimeClient.deleteChannel(deleteChannelRequest);

        return response.toString();
    }

    /*
     *
     * Send message to a specific channel
     * Request: Message Request DTO
     * Returns: Nothing
     * */
    @Override
    public SendMessageResponse sendMessage(MessageRequest request){

        SendChannelMessageRequest sendChannelMessageRequest = SendChannelMessageRequest.builder()
                .chimeBearer(request.getUserArn())
                .channelArn(request.getChannelArn())
                .clientRequestToken(request.getClientRequestToken())
                .content(request.getContent().trim())
                .persistence(request.getPersistence())
                .type(request.getType())
                .build();

        SendChannelMessageResponse response = chimeClient.sendChannelMessage(sendChannelMessageRequest);

        return SendMessageResponse.builder()
                .messageId(response.messageId())
                .channelArn(response.channelArn())
                .build();
    }

    /*
     *
     * Lists out all messages from a channel
     * Request: Channel Arn that we want to delete
     * Returns: Response Object
     * */
    @Override
    public List<MessageResponse> listMessages(String userArn, String channelArn){

        List<MessageResponse> responses = new ArrayList<>();

        String channel = Constant.AWS_PREFIX + "/channel/" + channelArn;
        String requestUserArn = Constant.AWS_PREFIX + "/user/" + userArn;

        ListChannelMessagesRequest listChannelMessagesRequest = ListChannelMessagesRequest.builder()
                .chimeBearer(requestUserArn)
                .channelArn(channel)
                .build();

        ListChannelMessagesResponse response = chimeClient.listChannelMessages(listChannelMessagesRequest);

        response.channelMessages().forEach(summary ->
            responses.add(MessageResponse.builder()
                    .messageId(summary.messageId())
                    .userArn(summary.sender().arn())
                    .username(summary.sender().name())
                    .content(summary.content())
                    .createdTime(Formatter.formatInstant(summary.createdTimestamp()))
                    .build())
        );

        return responses;
    }
}