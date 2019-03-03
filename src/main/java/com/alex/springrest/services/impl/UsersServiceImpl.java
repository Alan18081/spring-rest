package com.alex.springrest.services.impl;

import com.alex.springrest.dto.AddressDto;
import com.alex.springrest.dto.UserDto;
import com.alex.springrest.entities.PasswordResetTokenEntity;
import com.alex.springrest.entities.UserEntity;
import com.alex.springrest.exceptions.UserServiceException;
import com.alex.springrest.models.response.ErrorMessages;
import com.alex.springrest.repositories.PasswordResetTokensRepository;
import com.alex.springrest.repositories.UsersRepository;
import com.alex.springrest.services.EmailsService;
import com.alex.springrest.services.UsersService;
import com.alex.springrest.shared.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordResetTokensRepository passwordResetTokensRepository;

    @Autowired
    private EmailsService emailsService;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> userDtoList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, limit);

        Page<UserEntity> userEntityPage = usersRepository.findAll(pageable);
        List<UserEntity> userEntityList = userEntityPage.getContent();

        for(UserEntity userEntity : userEntityList) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            userDtoList.add(userDto);
        }

        return userDtoList;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if(userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new User(
                userEntity.getEmail(), userEntity.getEncryptedPassword(),
                userEntity.isEmailVerificationStatus(),
                true, true,
                true, new ArrayList<>()
        );
    }

    @Override
    public boolean resetPassword(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if(userEntity == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.name());
        }

        String token = utils.generateResetPasswordToken(userEntity.getUserId(), userEntity.getEmail());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity(token, userEntity);
        passwordResetTokensRepository.save(passwordResetTokenEntity);

        emailsService.sendVerificationEmail(email, token);

        return true;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if(usersRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserServiceException("User already exists");
        }

        for(AddressDto addressDto : userDto.getAddresses()) {
            addressDto.setUserDetails(userDto);
            addressDto.setAddressId(utils.generateAddressId(30));
        }


        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        String token = utils.generateEmailVerificationToken(userEntity.getUserId());
        userEntity.setUserId(utils.generateUserId(30));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setEmailVerificationToken(token);
        userEntity.setEmailVerificationStatus(false);

        UserEntity storedUserEntity = usersRepository.save(userEntity);

        emailsService.sendVerificationEmail(userEntity.getEmail(), token);

        return modelMapper.map(storedUserEntity, UserDto.class);
    }

    @Override
    public UserDto findByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if(userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto findByUserId(String userId) {
        UserEntity userEntity = usersRepository.findByUserId(userId);

        if(userEntity == null) {
            throw new RuntimeException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public boolean verifyEmailToken(String token) {
        UserEntity userEntity = usersRepository.findByEmailVerificationToken(token);

        if(userEntity != null) {
            boolean isTokenExpired = Utils.hasTokenExpired(token);
            if(!isTokenExpired) {
                userEntity.setEmailVerificationStatus(true);
                userEntity.setEmailVerificationToken(null);
                usersRepository.save(userEntity);
                return true;
            }
        }

        return false;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = usersRepository.findByUserId(userId);
        if(userEntity == null) {
            throw new RuntimeException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        usersRepository.delete(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = usersRepository.findByUserId(userDto.getUserId());
        if(userEntity == null) {
            throw new RuntimeException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        UserEntity updatedUserEntity = usersRepository.save(userEntity);

        return modelMapper.map(updatedUserEntity, UserDto.class);
    }

    @Override
    public List<AddressDto> getAddresses(String userId) {
        return null;
    }

    @Override
    public boolean setNewPassword(String token, String password) {
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokensRepository.findByToken(token);
        if(passwordResetTokenEntity == null) {
            throw new RuntimeException(ErrorMessages.NO_RECORD_FOUND.name());
        }

        if(Utils.hasTokenExpired(token)) {
            throw new RuntimeException(ErrorMessages.AUTHENTICATION_FAILED.name());
        }

        UserEntity userEntity = passwordResetTokenEntity.getUser();
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(password));
        usersRepository.save(userEntity);
        passwordResetTokensRepository.delete(passwordResetTokenEntity);

        return true;
    }
}
