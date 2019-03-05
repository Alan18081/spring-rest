package com.alex.springrest.controllers;

import com.alex.springrest.models.request.UserLoginRequestModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthRestController {

    @PostMapping("/login")
    @ApiOperation("User login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Response body")
    })
    public void theFakeLogin(@RequestBody @Valid UserLoginRequestModel userLoginRequestModel) {
        throw new IllegalStateException();
    }

}
