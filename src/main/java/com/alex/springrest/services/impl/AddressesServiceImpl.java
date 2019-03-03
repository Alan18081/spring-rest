package com.alex.springrest.services.impl;

import com.alex.springrest.dto.AddressDto;
import com.alex.springrest.entities.AddressEntity;
import com.alex.springrest.entities.UserEntity;
import com.alex.springrest.repositories.AddressesRepository;
import com.alex.springrest.repositories.UsersRepository;
import com.alex.springrest.services.AddressesService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressesServiceImpl implements AddressesService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AddressDto> getAddresses(String userId) {
        UserEntity userEntity = usersRepository.findByUserId(userId);

        if(userEntity == null) {
            return new ArrayList<>();
        }

        List<AddressEntity> addressEntities = addressesRepository.findAllByUser(userEntity);
        Type listType = new TypeToken<List<AddressDto>>() {}.getType();
        return modelMapper.map(addressEntities, listType);
    }

    @Override
    public AddressDto getAddressById(String userId, String id) {
        UserEntity userEntity = usersRepository.findByUserId(userId);

        if(userEntity == null) {
            return null;
        }

        AddressEntity addressEntity = addressesRepository.findByAddressId(id);

        if(addressEntity == null) {
            return null;
        }

        return modelMapper.map(addressEntity, AddressDto.class);
    }
}
