package com.alex.springrest.services;

public interface EmailsService {

    void sendVerificationEmail(String email, String emailVerificationToken);

    void sendPasswordResetEmail(String email, String resetPasswordToken);

}
