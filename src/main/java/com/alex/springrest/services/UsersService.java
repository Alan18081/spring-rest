package com.alex.springrest.services;

import com.alex.springrest.dto.AddressDto;
import com.alex.springrest.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {

    List<UserDto> getUsers(int page, int limit);

    UserDto createUser(UserDto userDto);

    UserDto findByEmail(String email);

    UserDto findByUserId(String userId);

    UserDto updateUser(UserDto userDto);

    void deleteUser(String userId);

    List<AddressDto> getAddresses(String userId);

    boolean verifyEmailToken(String token);

    boolean resetPassword(String email);

    boolean setNewPassword(String token, String password);

}
