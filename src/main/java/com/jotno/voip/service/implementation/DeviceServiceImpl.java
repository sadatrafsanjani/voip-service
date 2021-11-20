package com.jotno.voip.service.implementation;

import com.jotno.voip.model.Device;
import com.jotno.voip.repository.DeviceRepository;
import com.jotno.voip.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {

    private DeviceRepository deviceRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device getByDeviceToken(String deviceToken){

        return deviceRepository.findByDeviceToken(deviceToken);
    }
}
