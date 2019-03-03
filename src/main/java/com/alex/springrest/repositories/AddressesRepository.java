package com.alex.springrest.repositories;

import com.alex.springrest.entities.AddressEntity;
import com.alex.springrest.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressesRepository extends CrudRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByUser(UserEntity userEntity);

    AddressEntity findByAddressId(String id);

}
