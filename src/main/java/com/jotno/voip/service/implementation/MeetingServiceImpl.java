package com.jotno.voip.service.implementation;

import com.jotno.voip.dto.request.CallRequest;
import com.jotno.voip.dto.response.*;
import com.jotno.voip.service.abstraction.FirebaseService;
import com.jotno.voip.service.abstraction.MeetingService;
import com.jotno.voip.service.abstraction.UserService;
import com.jotno.voip.service.domain.AttendeeInfoData;
import com.jotno.voip.service.domain.JoinInfoData;
import com.jotno.voip.utility.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chime.ChimeClient;
import software.amazon.awssdk.services.chime.model.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static java.util.Map.of;

@Slf4j
@Service
public class MeetingServiceImpl implements MeetingService {

    private UserService userService;
    private FirebaseService firebaseService;
    private Map<String, Map<String, Object>> attendees = new HashMap<>();
    private ChimeClient chimeClient;

    @Autowired
    public MeetingServiceImpl(UserService userService, FirebaseService firebaseService) {
        this.userService = userService;
        this.firebaseService = firebaseService;
        this.chimeClient = ChimeClient.builder()
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(() -> AwsBasicCredentials.create(
                        Constant.AWS_ACCESS_KEY_ID,
                        Constant.AW_SECRET_ACCESS_KEY
                ))
                .build();
    }

    @Override
    public JoinInfoResponse initiateCall(CallRequest callerRequest){

        log.info("MeetingService initiateCall(): Entry");
        callerRequest.setMeetingId(UUID.randomUUID().toString());

        CallRequest calleeRequest = CallRequest.builder()
                .meetingId(callerRequest.getMeetingId())
                .attendeeName(userService.getUsernameByPhoneNumber(callerRequest.getReceiverPhoneNo()))
                .build();

        JoinInfoResponse callerResponse = generateMeetingSession(callerRequest);
        JoinInfoResponse calleeResponse = generateMeetingSession(calleeRequest);

        if(callerResponse != null){
            sendCallNotification(calleeResponse, callerRequest);
            log.info("MeetingService initiateCall(): Call notification sent");
        }

        log.info("MeetingService initiateCall(): Exit");

        return callerResponse;
    }

    private JoinInfoResponse generateMeetingSession(CallRequest request){

        log.info("MeetingService generateMeetingSession(): Entry");

        CreateMeetingRequest meetingRequest = CreateMeetingRequest.builder()
                .clientRequestToken(request.getMeetingId())
                .build();

        Meeting meeting = chimeClient.createMeeting(meetingRequest).meeting();
        MediaPlacement mediaPlacement = meeting.mediaPlacement();

        MediaPlacementResponse mediaPlacementResponse = MediaPlacementResponse.builder()
                .AudioFallbackUrl(mediaPlacement.audioFallbackUrl())
                .AudioHostUrl(mediaPlacement.audioHostUrl())
                .ScreenDataUrl(mediaPlacement.screenDataUrl())
                .ScreenSharingUrl(mediaPlacement.screenSharingUrl())
                .ScreenViewingUrl(mediaPlacement.screenViewingUrl())
                .SignalingUrl(mediaPlacement.signalingUrl())
                .TurnControlUrl(mediaPlacement.turnControlUrl())
                .build();

        Map<String, Object> meetingData = of(
                "MeetingId", meeting.meetingId(),
                "MediaRegion", meeting.mediaRegion(),
                "MediaPlacement", mediaPlacementResponse
        );

        CreateAttendeeRequest attendeeRequest = CreateAttendeeRequest.builder()
                .meetingId(meeting.meetingId())
                .externalUserId(UUID.randomUUID().toString())
                .build();

        Attendee attendee = chimeClient.createAttendee(attendeeRequest).attendee();

        attendees.put(request.getMeetingId(), of("attendeeId",attendee.attendeeId(), "attendeeName", request.getAttendeeName()));

        AttendeeResponse attendeeData = AttendeeResponse.builder()
                .AttendeeId(attendee.attendeeId())
                .ExternalUserId(attendee.externalUserId())
                .JoinToken(attendee.joinToken())
                .build();

        JoinInfo joinInfo = JoinInfo.builder()
                .Title(request.getMeetingId())
                .Meeting(meetingData)
                .Attendee(attendeeData)
                .build();

        JoinInfoResponse response = JoinInfoData.toDto(joinInfo);

        log.info("MeetingService generateMeetingSession(): Success- Exit");

        return response;
    }

    @Override
    public AttendeeInfoResponse getAttendeeInfo(String meetingTitle, String attendeeId){

        log.info("MeetingService getAttendeeInfo(): Entry");

        if(attendees.containsKey(meetingTitle)){

            Map<String, Object> attendee = attendees.get(meetingTitle);

            log.info("MeetingService getAttendeeInfo(): Success- Exit");

            AttendeeInfo attendeeInfo = AttendeeInfo.builder()
                    .AttendeeId(attendee.get("attendeeId").toString())
                    .Name(attendee.get("attendeeName").toString())
                    .build();

            return AttendeeInfoData.toDto(attendeeInfo);
        }

        log.info("MeetingService getAttendeeInfo(): Failure- Exit");

        return null;
    }

    @Override
    public void rejectCall(String receiverNo){

        log.info("MeetingService rejectCall(): Entry");

        userService.getUserDevicesByPhoneNumber(receiverNo).forEach( device ->
                firebaseService.sendCallRejectNotification(device)
        );

        log.info("MeetingService rejectCall(): Exit");
    }

    private void sendCallNotification(JoinInfoResponse response, CallRequest request){

        log.info("MeetingService sendCallNotification(): Entry");

        userService.getUserDevicesByPhoneNumber(request.getReceiverPhoneNo()).forEach( device ->
                firebaseService.sendCallNotification(response, device, request)
        );

        log.info("MeetingService sendCallNotification(): Exit");
    }
}
