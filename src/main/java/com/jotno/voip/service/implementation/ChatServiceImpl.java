package com.jotno.voip.service.implementation;

import com.jotno.voip.service.abstraction.ChatService;
import com.jotno.voip.utility.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chime.ChimeClient;
import software.amazon.awssdk.services.chime.model.CreateChannelRequest;
import software.amazon.awssdk.services.chime.model.CreateChannelResponse;
import software.amazon.awssdk.services.chime.model.DeleteChannelRequest;
import software.amazon.awssdk.services.chime.model.DeleteChannelResponse;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private ChimeClient chimeClient;

    @Autowired
    public ChatServiceImpl() {

        this.chimeClient = ChimeClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create(
                        Constant.AWS_ACCESS_KEY_ID,
                        Constant.AW_SECRET_ACCESS_KEY
                ))
                .build();
    }

    @Override
    public String createChannel(){

        CreateChannelRequest createChannelRequest = CreateChannelRequest.builder()
                .chimeBearer(Constant.AWS_CHIME_BEARER)
                .appInstanceArn(Constant.AWS_APP_INSTANCE_ARN)
                .clientRequestToken("jotno-web-client")
                .mode("UNRESTRICTED")
                .name("appointment-101")
                .privacy("PRIVATE")
                .build();

        CreateChannelResponse response = chimeClient.createChannel(createChannelRequest);
        log.info(response.toString());

        return response.channelArn();
    }

    @Override
    public void deleteChannel(String channelArn){

        DeleteChannelRequest deleteChannelRequest = DeleteChannelRequest.builder()
                .chimeBearer(Constant.AWS_CHIME_BEARER)
                .channelArn(channelArn)
                .build();

        DeleteChannelResponse response = chimeClient.deleteChannel(deleteChannelRequest);

        log.info(response.toString());
    }
}
