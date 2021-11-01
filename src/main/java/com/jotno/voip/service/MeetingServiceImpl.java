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
import java.util.UUID;
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
        log.info("MeetingService Constructor: ChimeClient initiated");
    }

    @Override
    public Map<String, Object> generateMeetingSession(MeetingRequest request){

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

        JoinResponse joinResponse = JoinResponse.builder()
                .Title(request.getMeetingId())
                .Meeting(meetingData)
                .Attendee(attendeeData)
                .build();

        log.info("MeetingService generateMeetingSession(): Success- Exit");

        return of("JoinInfo", joinResponse);
    }

    @Override
    public Map<String, Object> getAttendeeInfo(String meetingTitle, String attendeeId){

        log.info("MeetingService getAttendeeInfo(): Entry");

        if(attendees.containsKey(meetingTitle)){

            Map<String, Object> attendee = attendees.get(meetingTitle);

            log.info("MeetingService getAttendeeInfo(): Success- Exit");

            return of("AttendeeInfo", AttendeeInfoResponse.builder()
                    .AttendeeId(attendee.get("attendeeId").toString())
                    .Name(attendee.get("attendeeName").toString())
                    .build());
        }

        log.info("MeetingService getAttendeeInfo(): Failure- Exit");

        return null;
    }
}
