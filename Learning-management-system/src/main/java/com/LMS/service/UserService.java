package com.LMS.service;

import com.LMS.dto.UserDto;
import com.LMS.utility.PaegableResponse;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(int id,UserDto userDto);
    void deleteUser(int id);
    PaegableResponse<UserDto> allUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
    UserDto getSingleUser(int id);
}
