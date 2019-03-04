package com.alex.springrest.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    private Utils utils;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGenerateUserId() {
        String userId = utils.generateUserId(30);

        assertNotNull(userId);
        assertEquals(userId.length(), 30);
    }

    @Test
    void testHasTokenExpired() {
        String token = utils.generateEmailVerificationToken("fdsgdfbg");
        assertNotNull(token);
        boolean hasExpired = Utils.hasTokenExpired(token);
        assertFalse(hasExpired);
    }
}