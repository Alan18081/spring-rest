package com.alex.springrest.services.impl;

import com.alex.springrest.dto.UserDto;
import com.alex.springrest.entities.UserEntity;
import com.alex.springrest.models.response.ErrorMessages;
import com.alex.springrest.repositories.UsersRepository;
import com.alex.springrest.services.UsersService;
import com.alex.springrest.shared.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if(userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if(usersRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException("User already exists");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setUserId(utils.generateUserId(30));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        UserEntity storedUserEntity = usersRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto findByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if(userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public UserDto findByUserId(String userId) {
        UserEntity userEntity = usersRepository.findByUserId(userId);

        if(userEntity == null) {
            throw new RuntimeException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;

    }
}
