package com.Mission.Shakti.service;


import com.Mission.Shakti.dto.LoginResponse;
import com.Mission.Shakti.dto.UserDto;

public interface AuthService {



    String register(UserDto dto);


    LoginResponse login(String username, String password);
    public String approveUser(Long userId, String approverUsername, boolean approve);
}
