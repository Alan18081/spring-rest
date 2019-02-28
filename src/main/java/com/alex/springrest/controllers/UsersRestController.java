package com.alex.springrest.controllers;

import com.alex.springrest.dto.UserDto;
import com.alex.springrest.models.request.UserDetailsRequestModel;
import com.alex.springrest.models.response.OperationStatusModel;
import com.alex.springrest.models.response.RequestOperationStatus;
import com.alex.springrest.models.response.UserRest;
import com.alex.springrest.services.UsersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersRestController {

    @Autowired
    private UsersService usersService;

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public List<UserRest> getUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "25") int limit
    ) {
        List<UserRest> returnValue = new ArrayList<>();
        List<UserDto> userDtoList = usersService.getUsers(page, limit);

        for(UserDto userDto : userDtoList) {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDto, userRest);
            returnValue.add(userRest);
        }

        return returnValue;
    }

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

    @PutMapping(path = "{id}", consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest updateUserById(@PathVariable String id, @RequestBody @Valid UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);
        userDto.setUserId(id);

        UserDto updateUser = usersService.updateUser(userDto);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    @DeleteMapping("{id}")
    public OperationStatusModel deleteUserById(@PathVariable String userId) {
        usersService.deleteUser(userId);
        return new OperationStatusModel(RequestOperationName.DELETE.name(), RequestOperationStatus.SUCCESS.name());
    }

}
