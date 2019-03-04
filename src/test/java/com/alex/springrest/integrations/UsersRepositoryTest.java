package com.alex.springrest.integrations;

import com.alex.springrest.entities.UserEntity;
import com.alex.springrest.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    void setup() {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setEmail("email@gmail.com");
//        userEntity.setFirstName("Alan");
//        userEntity.setLastName("Markus");
//        userEntity.setUserId("gfhgfhgfh");
//        userEntity.setEncryptedPassword("123456");
//        userEntity.setEmailVerificationStatus(true);
//
//        usersRepository.save(userEntity);
    }

    @Test
    void testGetVerifiedUsers() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<UserEntity> pages = usersRepository.findAllUsersWithConfirmedEmail(pageable);
        List<UserEntity> userEntities = pages.getContent();
        assertNotNull(pages);
        assertEquals(userEntities.size(), 1);
    }

    @Test
    void testGetUsersWithName() {
        List<UserEntity> userEntities = usersRepository.findAllUsersWithFirstName("Alan");
        assertNotNull(userEntities);
        assertEquals(userEntities.size(), 1);
    }

}
