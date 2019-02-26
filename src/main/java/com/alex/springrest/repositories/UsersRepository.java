package com.alex.springrest.repositories;

import com.alex.springrest.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
