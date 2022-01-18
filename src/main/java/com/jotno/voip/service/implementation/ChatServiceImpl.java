package com.jotno.voip.service.implementation;

import com.jotno.voip.dto.chat.request.MessageRequest;
import com.jotno.voip.dto.chat.response.*;
import com.jotno.voip.model.Room;
import com.jotno.voip.repository.RoomRepository;
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
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import static java.util.UUID.randomUUID;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private ChimeClient chimeClient;
    private RoomRepository roomRepository;

    @Autowired
    public ChatServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @PostConstruct
    private void init(){

        this.chimeClient = ChimeClient.builder()
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(() -> AwsBasicCredentials.create(
                        Constant.AWS_ACCESS_KEY_ID,
                        Constant.AW_SECRET_ACCESS_KEY
                ))
                .build();
    }

    private String createDoctor(String doctorName){

        CreateAppInstanceUserRequest createAppInstanceUserRequest = CreateAppInstanceUserRequest.builder()
                .appInstanceUserId(doctorName)
                .appInstanceArn(Constant.AWS_APP_INSTANCE_ARN)
                .name(doctorName)
                .build();

        CreateAppInstanceUserResponse doctorResponse = chimeClient.createAppInstanceUser(createAppInstanceUserRequest);

        return doctorResponse.appInstanceUserArn();
    }


    private String createPatient(String patientName){

        CreateAppInstanceUserRequest createAppInstanceUserRequest = CreateAppInstanceUserRequest.builder()
                .appInstanceUserId("Patient-" + randomUUID())
                .appInstanceArn(Constant.AWS_APP_INSTANCE_ARN)
                .name(patientName)
                .build();

        CreateAppInstanceUserResponse patientResponse = chimeClient.createAppInstanceUser(createAppInstanceUserRequest);

        return patientResponse.appInstanceUserArn();
    }

    private String createChannel(String channelCreatorArn){

        String token = UUID.randomUUID().toString();

        CreateChannelRequest createChannelRequest = CreateChannelRequest.builder()
                .chimeBearer(channelCreatorArn)
                .appInstanceArn(Constant.AWS_APP_INSTANCE_ARN)
                .clientRequestToken(token)
                .mode("UNRESTRICTED")
                .name("CHANNEL-" + token)
                .privacy("PRIVATE")
                .build();

        CreateChannelResponse channelResponse = chimeClient.createChannel(createChannelRequest);

        return channelResponse.channelArn();
    }

    private void addMemberToChannel(String doctorArn, String patientArn, String channelArn){

        CreateChannelMembershipRequest request = CreateChannelMembershipRequest.builder()
                .chimeBearer(doctorArn)
                .memberArn(patientArn)
                .channelArn(channelArn)
                .type("DEFAULT")
                .build();

        chimeClient.createChannelMembership(request);
    }

    @Override
    public String deleteChannel(String channelArn, String creatorArn){

        DeleteChannelRequest deleteChannelRequest = DeleteChannelRequest.builder()
                .chimeBearer(creatorArn)
                .channelArn(channelArn)
                .build();

        DeleteChannelResponse response = chimeClient.deleteChannel(deleteChannelRequest);

        return response.toString();
    }

    @Override
    public SendMessageResponse sendMessage(MessageRequest request){

        SendChannelMessageRequest sendChannelMessageRequest = SendChannelMessageRequest.builder()
                .chimeBearer(request.getUserArn())
                .channelArn(request.getChannelArn())
                .clientRequestToken(UUID.randomUUID().toString())
                .content(request.getContent().trim())
                .persistence("PERSISTENT")
                .type("STANDARD")
                .build();

        SendChannelMessageResponse response = chimeClient.sendChannelMessage(sendChannelMessageRequest);

        return SendMessageResponse.builder()
                .messageId(response.messageId())
                .channelArn(response.channelArn())
                .build();
    }

    @Override
    public List<MessageResponse> listMessages(String userArn, String channelArn){

        List<MessageResponse> responses = new ArrayList<>();

        ListChannelMessagesRequest listChannelMessagesRequest = ListChannelMessagesRequest.builder()
                .chimeBearer(userArn)
                .channelArn(channelArn)
                .build();

        chimeClient.listChannelMessages(listChannelMessagesRequest).channelMessages().forEach(summary ->
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

    @Override
    public RoomResponse createRoom(){

        String doctor = createDoctor("Doctor-" + UUID.randomUUID());
        String patient = createPatient("Patient-" + UUID.randomUUID());
        String channel = createChannel(doctor);

        addMemberToChannel(doctor, patient, channel);

        Room room = Room.builder()
                .channelArn(channel)
                .doctorArn(doctor)
                .patientArn(patient)
                .build();

        return modelToDto(roomRepository.save(room));
    }

    private RoomResponse modelToDto(Room room){

        return RoomResponse.builder()
                .id(room.getId())
                .channelArn(room.getChannelArn())
                .doctorArn(room.getDoctorArn())
                .patientArn(room.getPatientArn())
                .build();
    }

    @Override
    public List<RoomResponse> getRooms(){

        return roomRepository.findAll().stream().map(room -> modelToDto(room))
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse getRoomById(long id){

        return modelToDto(roomRepository.getById(id));
    }
}