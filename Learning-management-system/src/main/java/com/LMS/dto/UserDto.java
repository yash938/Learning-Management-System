package com.LMS.dto;

import com.LMS.entity.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String mobile_no;
    private String image;
    private Gender gender;
    private String enrollment_no;
}
