package com.jotno.voip.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaPlacementResponse {

    @JsonProperty("AudioFallbackUrl")
    private String AudioFallbackUrl;
    @JsonProperty("AudioHostUrl")
    private String AudioHostUrl;
    @JsonProperty("ScreenDataUrl")
    private String ScreenDataUrl;
    @JsonProperty("ScreenSharingUrl")
    private String ScreenSharingUrl;
    @JsonProperty("ScreenViewingUrl")
    private String ScreenViewingUrl;
    @JsonProperty("SignalingUrl")
    private String SignalingUrl;
    @JsonProperty("TurnControlUrl")
    private String TurnControlUrl;
}
