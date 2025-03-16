package com.LMS.controller;

import com.LMS.dto.UserDto;
import com.LMS.Exception.HandlerException;
import com.LMS.service.UserService;
import com.LMS.utility.PaegableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/new")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<PaegableResponse<UserDto>> allUser(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "fullName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir
    ){
        PaegableResponse<UserDto> userDtoPaegableResponse = userService.allUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoPaegableResponse,HttpStatus.FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable int id,@RequestBody UserDto userDto){
        UserDto updateUser = userService.updateUser(id, userDto);
        return new ResponseEntity<>(updateUser,HttpStatus.OK);
    }

    @GetMapping("/single/{id}")
    public ResponseEntity<UserDto> singleUser(@PathVariable int id){
        UserDto singleUser = userService.getSingleUser(id);
        return new ResponseEntity<>(singleUser,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HandlerException> deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        HandlerException deletedUser = HandlerException.builder().message("User deleted successfully").success(true).httpStatus(HttpStatus.OK).localDate(LocalDate.now()).build();
        return new ResponseEntity<>(deletedUser,HttpStatus.OK);
    }
}

