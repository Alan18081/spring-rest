package com.alex.springrest.services.impl;

import com.alex.springrest.dto.AddressDto;
import com.alex.springrest.dto.UserDto;
import com.alex.springrest.entities.AddressEntity;
import com.alex.springrest.entities.UserEntity;
import com.alex.springrest.repositories.UsersRepository;
import com.alex.springrest.services.EmailsService;
import com.alex.springrest.shared.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceImplTest {

    @InjectMocks
    private UsersServiceImpl usersService;

    @Mock
    private UsersRepository usersRepository;

    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private Utils utils;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private EmailsService emailsService;

    private String userId = "fdgdfy5trhb";
    private String password = "tretyrtythb";
    private UserEntity userEntity;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("email@gmail.com");
        userEntity.setFirstName("Alan");
        userEntity.setLastName("Markus");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(password);

        userDto = new UserDto();
        userDto.setAddresses(getAddressDtoList());
        userDto.setEmail(userEntity.getEmail());
        userDto.setUserId(userEntity.getUserId());
    }

    @Test
    void testFindUserByEmail() {
        when(usersRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = usersService.findByEmail("test@test.com");
        assertNotNull(userDto);
        assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        assertEquals(userEntity.getLastName(), userDto.getLastName());
        assertEquals(userEntity.getUserId(), userDto.getUserId());
    }

    @Test
    void testFindUserByEmail_UsernameNotFoundException() {
        when(usersRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> usersService.findByEmail("test@gmail.com"));
    }

    @Test
    void testCreateUser() {
        when(usersRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("gfdtgregvdf");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(utils.generateEmailVerificationToken(anyString())).thenReturn("rdsgdfgfhgfhnjh");
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(password);
        when(usersRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        Mockito.doNothing().when(emailsService).sendVerificationEmail(anyString(), anyString());
        userEntity.setAddresses(getAddressEntityList());

        UserDto storedUserDto = usersService.createUser(userDto);

        assertNotNull(storedUserDto);
        assertEquals(userEntity.getFirstName(), storedUserDto.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDto.getLastName());
        assertEquals(userEntity.getUserId(), storedUserDto.getUserId());
        assertEquals(userEntity.getAddresses().size(), storedUserDto.getAddresses().size());

        verify(utils, times(2)).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode(userDto.getPassword());
        verify(usersRepository, times(1)).save(any(UserEntity.class));
        verify(emailsService, times(1)).sendVerificationEmail(anyString(), anyString());
    }

    @Test
    void testCreateUser_RuntimeException() {
        when(usersRepository.findByEmail(anyString())).thenReturn(userEntity);
        assertThrows(RuntimeException.class, () -> {
           usersService.createUser(userDto);
        });
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

    private List<AddressEntity> getAddressEntityList() {
        Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
        return modelMapper.map(getAddressDtoList(), listType);
    }
}