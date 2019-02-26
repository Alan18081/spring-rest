package com.alex.springrest.services;

import com.alex.springrest.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

}
