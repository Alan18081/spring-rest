package com.alex.springrest.services;

import com.alex.springrest.dto.AddressDto;

import java.util.List;

public interface AddressesService {

    List<AddressDto> getAddresses(String userId);

    AddressDto getAddressById(String userId, String id);

}
