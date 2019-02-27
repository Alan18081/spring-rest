package com.alex.springrest.controllers;

import com.alex.springrest.dto.UserDto;
import com.alex.springrest.models.request.UserDetailsRequestModel;
import com.alex.springrest.models.response.UserRest;
import com.alex.springrest.services.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersRestController {

    @Autowired
    private UsersService usersService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public UserRest createUser(@RequestBody @Valid UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = usersService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;

    }

    @GetMapping(path = "{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest getUserById(@PathVariable String id) {
        UserRest returnValue = new UserRest();
        UserDto userDto = usersService.findByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);
        return returnValue;
    }

}
