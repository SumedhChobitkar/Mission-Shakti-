package com.Mission.Shakti.service;

public interface EmailService {

    void sendRegistrationEmail(String to, String username);

    void sendApprovalEmail(String to, String username);

    void sendRejectionEmail(String to, String username);

    void sendLoginEmail(String to, String username);
}