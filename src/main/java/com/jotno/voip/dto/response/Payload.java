package com.jotno.voip.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class Payload {

    private String title;
    private String body;
    private Map<String, String> data;
}
