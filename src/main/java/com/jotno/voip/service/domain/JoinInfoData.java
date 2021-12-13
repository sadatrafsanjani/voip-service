package com.jotno.voip.service.domain;

import com.jotno.voip.dto.call.response.JoinInfo;
import com.jotno.voip.dto.call.response.JoinInfoResponse;

public class JoinInfoData {

    public static JoinInfoResponse toDto(JoinInfo joinInfo){

        return JoinInfoResponse.builder()
                .JoinInfo(joinInfo)
                .build();
    }
}
