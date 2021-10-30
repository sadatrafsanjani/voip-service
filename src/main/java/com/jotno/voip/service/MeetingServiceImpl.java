package com.jotno.voip.service;

import com.jotno.voip.dto.request.MeetingRequest;
import com.jotno.voip.dto.response.AttendeeInfoResponse;
import com.jotno.voip.dto.response.AttendeeResponse;
import com.jotno.voip.dto.response.JoinResponse;
import com.jotno.voip.dto.response.MediaPlacementResponse;
import com.jotno.voip.utility.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.chime.ChimeClient;
import software.amazon.awssdk.services.chime.model.*;
import java.util.HashMap;
import java.util.Map;
import static java.util.Map.of;

@Slf4j
@Service
public class MeetingServiceImpl implements MeetingService {

    private Map<String, Map<String, Object>> attendees = new HashMap<>();
    private ChimeClient chimeClient;

    public MeetingServiceImpl() {

        chimeClient = ChimeClient.builder()
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(() -> AwsBasicCredentials.create(
                        Constant.ACCESS_KEY_ID,
                        Constant.SECRET_ACCESS_KEY
                )).build();
    }

    @Override
    public Map<String, Object> generateMeetingSession(MeetingRequest request){

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
                .externalUserId(request.getUserId())
                .build();

        Attendee attendee = chimeClient.createAttendee(attendeeRequest).attendee();

        // Saving Attendee Info for later use
        attendees.put(request.getMeetingId(), of("attendeeId",attendee.attendeeId(), "attendeeName", request.getUserId()));

        AttendeeResponse attendeeData = AttendeeResponse.builder()
                .AttendeeId(attendee.attendeeId())
                .ExternalUserId(attendee.externalUserId())
                .JoinToken(attendee.joinToken())
                .build();

        JoinResponse joinResponse = JoinResponse.builder()
                .Title(request.getMeetingId())
                .Meeting(meetingData)
                .Attendee(attendeeData)
                .build();

        return of("JoinInfo", joinResponse);
    }

    @Override
    public Map<String, Object> getAttendeeInfo(String meetingTitle, String attendeeId){

        if(attendees.containsKey(meetingTitle)){

            Map<String, Object> attendee = attendees.get(meetingTitle);


            return of("AttendeeInfo",AttendeeInfoResponse.builder()
                    .AttendeeId(attendee.get("attendeeId").toString())
                    .Name(attendee.get("attendeeName").toString())
                    .build());
        }

        return null;
    }
}
