package com.alex.springrest.repositories;

import com.alex.springrest.entities.PasswordResetTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokensRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

    PasswordResetTokenEntity findByToken(String token);

}
