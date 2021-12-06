package com.jotno.voip.service.implementation;

import com.jotno.voip.dto.request.MessageRequest;
import com.jotno.voip.service.abstraction.ChatService;
import com.jotno.voip.utility.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chime.ChimeClient;
import software.amazon.awssdk.services.chime.model.*;
import java.util.UUID;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private ChimeClient chimeClient;
    private String userArn;
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
                .appInstanceUserId("doctor-" + UUID.randomUUID())
                .appInstanceArn(Constant.AWS_APP_INSTANCE_ARN)
                .name("DOCTOR")
                .build();

        CreateAppInstanceUserResponse userResponse = chimeClient.createAppInstanceUser(createAppInstanceUserRequest);
        userArn = userResponse.appInstanceUserArn();
        log.info(userArn);

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
    public String createMember(){

        CreateAppInstanceUserRequest createAppInstanceUserRequest = CreateAppInstanceUserRequest.builder()
                .appInstanceUserId("patient-101")
                .appInstanceArn(Constant.AWS_APP_INSTANCE_ARN)
                .name("PATIENT-101")
                .build();

        CreateAppInstanceUserResponse userResponse = chimeClient.createAppInstanceUser(createAppInstanceUserRequest);

        return userResponse.appInstanceUserArn();
    }

    /*
     *
     * Add member to a channel
     * Request: Member Arn of the user whom we want to add to a channel
     * Returns: Nothing
     * */
    @Override
    public void addMemberToChannel(String memberArn){

        memberArn = Constant.AWS_PREFIX + "/user/" + memberArn;

        //channelArn = "arn:aws:chime:us-east-1:252894276123:app-instance/2d40b42c-3b41-41d0-b409-ea94f01a6982/channel/60dae12bffd11de40321d101c3774a40393a9463c038aeca8bb5be2567c9231b";

        CreateChannelMembershipRequest request = CreateChannelMembershipRequest.builder()
                .chimeBearer(userArn)
                .memberArn(memberArn)
                .channelArn(channelArn)
                .type("DEFAULT")
                .build();

        CreateChannelMembershipResponse response = chimeClient.createChannelMembership(request);
        log.info(response.toString());
    }


    /*
     *
     * Delete a created channel
     * Request: Channel Arn that we want to delete
     * Returns: Nothing
     * */
    @Override
    public void deleteChannel(String channelArn){

        channelArn = Constant.AWS_PREFIX + "/channel/" + channelArn;

        DeleteChannelRequest deleteChannelRequest = DeleteChannelRequest.builder()
                .chimeBearer(userArn)
                .channelArn(channelArn)
                .build();

        DeleteChannelResponse response = chimeClient.deleteChannel(deleteChannelRequest);

        log.info(response.toString());
    }

    @Override
    public void sendMessage(MessageRequest request){

        SendChannelMessageRequest sendChannelMessageRequest = SendChannelMessageRequest.builder()
                .chimeBearer(userArn)
                .channelArn(channelArn)
                .clientRequestToken(request.getClientRequestToken())
                .content(request.getContent().trim())
                .persistence(request.getPersistence())
                .type(request.getType())
                .build();

        SendChannelMessageResponse response = chimeClient.sendChannelMessage(sendChannelMessageRequest);
        log.info(response.toString());
    }
}