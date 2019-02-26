package com.alex.springrest.controllers;

import com.alex.springrest.dto.UserDto;
import com.alex.springrest.models.request.UserDetailsRequestModel;
import com.alex.springrest.models.response.UserRest;
import com.alex.springrest.services.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersRestController {

    @Autowired
    private UsersService usersService;

    @PostMapping
    public UserRest createUser(@RequestBody @Valid UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = usersService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;

    }

}
