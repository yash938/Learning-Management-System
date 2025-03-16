package com.LMS.serviceImpl;

import com.LMS.dto.UserDto;
import com.LMS.entity.Role;
import com.LMS.entity.User;
import com.LMS.repository.RoleRepo;
import com.LMS.repository.UserRepo;
import com.LMS.service.UserService;
import com.LMS.utility.Helper;
import com.LMS.utility.PaegableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Role role = roleRepo.findByName("ROLE_STUDENT").orElseThrow(() -> new RuntimeException("Role is not found"));
        user.setRoles(List.of(role));
        User createdUser = userRepo.save(user);
        UserDto savedUser = modelMapper.map(createdUser, UserDto.class);
        return savedUser;
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User is not found"));
        user.setFullName(userDto.getFullName());
        user.setEnrollment_no(user.getEnrollment_no());
        user.setPassword(user.getPassword());
        user.setMobile_no(user.getMobile_no());
        user.setGender(userDto.getGender());

        User updateUser = userRepo.save(user);
        return modelMapper.map(updateUser,UserDto.class);
    }

    @Override
    public void deleteUser(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User is not found"));
        userRepo.delete(user);
        System.out.println("User is deleted");
    }

    @Override
    public PaegableResponse<UserDto> allUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> allUsers = userRepo.findAll(pageRequest);
        PaegableResponse<UserDto> paegable = Helper.getPaegable(allUsers, UserDto.class);
        return paegable;
    }

    @Override
    public UserDto getSingleUser(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("user is not found"));
        UserDto findUser = modelMapper.map(user, UserDto.class);
        return findUser;
    }
}
