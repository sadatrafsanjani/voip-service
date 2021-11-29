package com.jotno.voip.service.implementation;

import com.jotno.voip.model.Device;
import com.jotno.voip.repository.UserRepository;
import com.jotno.voip.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<String> getUserDevicesByPhoneNumber(String phone){

        Set<String> deviceTokenList = new HashSet<>();

        for(Device device : userRepository.findByPhone(phone).getDevices()){

            deviceTokenList.add(device.getDeviceToken());
        }

        return deviceTokenList;
    }

    @Override
    public String getUsernameByPhoneNumber(String phone){

        return userRepository.findByPhone(phone).getUsername();
    }
}
