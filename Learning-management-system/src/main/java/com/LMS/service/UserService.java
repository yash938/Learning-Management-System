package com.LMS.service;

import com.LMS.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(int id,UserDto userDto);
    void deleteUser(int id);
    UserDto getSingleUser(int id);
}
