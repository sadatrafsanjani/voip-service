package com.jotno.voip.service;

import java.util.Set;

public interface UserService {

    Set<String> getUserDevicesByPhoneNumber(String phone);
    String getUsernameByPhoneNumber(String phone);
}
