package com.alex.springrest.repositories;

import com.alex.springrest.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    UserEntity findByEmailVerificationToken(String token);

}
