package com.alex.springrest.controllers;

import com.alex.springrest.dto.AddressDto;
import com.alex.springrest.dto.UserDto;
import com.alex.springrest.models.request.PasswordResetRequestModel;
import com.alex.springrest.models.request.SetNewPasswordRequestModel;
import com.alex.springrest.models.request.UserDetailsRequestModel;
import com.alex.springrest.models.response.AddressRest;
import com.alex.springrest.models.response.OperationStatusModel;
import com.alex.springrest.models.response.RequestOperationStatus;
import com.alex.springrest.models.response.UserRest;
import com.alex.springrest.services.AddressesService;
import com.alex.springrest.services.UsersService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = { "http://localhost:3000" })
public class UsersRestController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private AddressesService addressesService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Bearer JWT token", paramType = "header")
    })
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

    @GetMapping(path = "{userId}/addresses",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiOperation(value = "Get user addresses by user id")
    public Resources<AddressRest> getUserAddresses(@PathVariable String userId) {
        List<AddressDto> addressDtos = usersService.getAddresses(userId);

        if(addressDtos == null || addressDtos.isEmpty()) {
            return new Resources<>(new ArrayList<>());
        }

        Type listType = new TypeToken<List<AddressRest>>() {}.getType();

        List<AddressRest> addressRestList = modelMapper.map(addressDtos, listType);
        return new Resources<>(addressRestList.stream().map(addressRest -> {
            Link addressLink = linkTo(methodOn(UsersRestController.class).getUserAddressById(userId, addressRest.getAddressId())).withRel("address");
            Link userLink = linkTo(methodOn(UsersRestController.class).getUserById(userId)).withRel("user");

            addressRest.add(addressLink);
            addressRest.add(userLink);
            return addressRest;
        }).collect(Collectors.toList()));
    }

    @GetMapping(path = "{userId}/addresses/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Resource<AddressRest> getUserAddressById(@PathVariable String userId, @PathVariable String id) {
        AddressDto addressDto = addressesService.getAddressById(userId, id);
        Link addressLink = linkTo(methodOn(UsersRestController.class).getUserAddressById(userId, id)).withSelfRel();
        Link userLink = linkTo(UsersRestController.class).slash(userId).withRel("user");
        AddressRest addressRest = modelMapper.map(addressDto, AddressRest.class);
        addressRest.add(addressLink);
        addressRest.add(userLink);
        return new Resource<>(addressRest);
    }

    @GetMapping(path = "email-verification/{token}")
    public OperationStatusModel verifyEmailToken(@PathVariable("token") String token) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.EMAIL_VERIFICATION.name());

        boolean isVerified = usersService.verifyEmailToken(token);
        if(isVerified) {
            operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return operationStatusModel;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public UserRest createUser(@RequestBody @Valid UserDetailsRequestModel userDetails) {
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = usersService.createUser(userDto);
        return modelMapper.map(createdUser, UserRest.class);
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
    public OperationStatusModel deleteUserById(@PathVariable String id) {
        usersService.deleteUser(id);
        return new OperationStatusModel(RequestOperationName.DELETE.name(), RequestOperationStatus.SUCCESS.name());
    }

    @PostMapping("reset-password")
    public OperationStatusModel resetPassword(@RequestBody @Valid PasswordResetRequestModel passwordResetRequestModel) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.RESET_PASSWORD.name());

        boolean operationResult = usersService.resetPassword(passwordResetRequestModel.getEmail());

        if(operationResult) {
            operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return operationStatusModel;
    }

    @PostMapping("set-new-password")
    public OperationStatusModel setNewPassword(@RequestBody @Valid SetNewPasswordRequestModel requestModel) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.SET_NEW_PASSWORD.name());
        boolean operationResult = usersService.setNewPassword(requestModel.getToken(), requestModel.getPassword());
        if(operationResult) {
            operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return operationStatusModel;
    }

}
