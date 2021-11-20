package com.jotno.voip.service;

import com.jotno.voip.model.Device;

public interface DeviceService {

    Device getByDeviceToken(String deviceToken);
}
