package com.alex.springrest.repositories;

import com.alex.springrest.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    UserEntity findByEmailVerificationToken(String token);

    @Query(value = "select * from users where email_verification_status = true",
            countQuery = "select count(*) from users where email_verification_status = true",
            nativeQuery = true
    )
    Page<UserEntity> findAllUsersWithConfirmedEmail(Pageable pageable);

    @Query(value = "select user from UserEntity user where user.firstName = :firstName")
    List<UserEntity> findAllUsersWithFirstName(@Param("firstName") String firstName);

    @Modifying
    @Transactional
    @Query(value = "update UserEntity user set user.emailVerificationStatus = :status where user.userId = :userId")
    void updateUserVerificationStatus(@Param("status") boolean status, @Param("userId") String userId);

}
