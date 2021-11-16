package com.jotno.voip.model;

import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private long id;
    private String username;
    private Set<Device> devices = new HashSet<>();
}
