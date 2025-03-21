package com.LMS.service;

import com.LMS.dto.RefreshTokenDto;
import com.LMS.dto.UserDto;

public interface RefreshTokenService {


    public RefreshTokenDto createRefreshToken(String username);

    public RefreshTokenDto findByToken(String token);

    public RefreshTokenDto verifyToken(RefreshTokenDto refreshTokenDto);

    UserDto getUser(RefreshTokenDto tokenDto);

}
