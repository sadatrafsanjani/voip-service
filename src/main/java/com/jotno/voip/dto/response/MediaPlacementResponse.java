package com.jotno.voip.dto.response;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaPlacementResponse {

    private String AudioFallbackUrl;
    private String AudioHostUrl;
    private String ScreenDataUrl;
    private String ScreenSharingUrl;
    private String ScreenViewingUrl;
    private String SignalingUrl;
    private String TurnControlUrl;
}
