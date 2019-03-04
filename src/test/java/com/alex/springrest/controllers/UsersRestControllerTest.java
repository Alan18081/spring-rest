package com.alex.springrest.controllers;

import com.alex.springrest.dto.AddressDto;
import com.alex.springrest.dto.UserDto;
import com.alex.springrest.models.response.UserRest;
import com.alex.springrest.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.ArgumentMatchers.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersRestControllerTest {

    @InjectMocks
    private UsersRestController usersRestController;

    @Mock
    private UsersService usersService;

    private UserDto userDto;
    private String USER_ID = "gdfghfbhgfju";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userDto = new UserDto();
        userDto.setEmail("alex@gmail.com");
        userDto.setUserId(USER_ID);
        userDto.setAddresses(getAddressDtoList());
    }

    @Test
    void testGetUser() {
        when(usersService.findByUserId(anyString())).thenReturn(userDto);
        UserRest userRest = usersRestController.getUserById(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertEquals(userDto.getAddresses().size(), userRest.getAddresses().size());
    }

    private List<AddressDto> getAddressDtoList() {
        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");
        addressDto.setCity("London");
        addressDto.setCountry("Great Britain");
        addressDto.setPostalCode("ABC123");
        addressDto.setStreetName("1 street");

        AddressDto billingAddressDto = new AddressDto();
        billingAddressDto.setType("billing");
        billingAddressDto.setCity("London");
        billingAddressDto.setCountry("Great Britain");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("1 street");

        List<AddressDto> list = new ArrayList<>();
        list.add(addressDto);
        list.add(billingAddressDto);

        return list;
    }
}