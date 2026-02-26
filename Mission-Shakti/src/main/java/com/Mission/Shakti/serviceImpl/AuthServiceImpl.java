package com.Mission.Shakti.serviceImpl;

import com.Mission.Shakti.Entity.User;
import com.Mission.Shakti.Enum.ApprovalStatus;
import com.Mission.Shakti.Enum.Role;
import com.Mission.Shakti.dto.LoginResponse;
import com.Mission.Shakti.dto.UserDto;
import com.Mission.Shakti.exception.ResourceAlreadyExistsException;
import com.Mission.Shakti.repository.UserRepository;
import com.Mission.Shakti.Security.JwtUtil;
import com.Mission.Shakti.service.AuthService;
import com.Mission.Shakti.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Override
    public String register(UserDto dto) {

        log.info("Attempting to register user: {}", dto.getUsername());

        if (userRepository.existsByUsername(dto.getUsername())) {
            log.warn("Username already exists: {}", dto.getUsername());
            throw new ResourceAlreadyExistsException("Username already taken");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Email already exists: {}", dto.getEmail());
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        // Default Status
        user.setStatus(ApprovalStatus.PENDING);
        userRepository.save(user);

        log.info("User registered successfully: {}", dto.getUsername());

        return "User Registered Successfully , waiting for an approval";
    }

    @Override
    public LoginResponse login(String username, String password) {

        log.info("Login attempt for username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("Invalid password attempt for user: {}", username);
            throw new RuntimeException("Invalid password");
        }
        if (user.getStatus() != ApprovalStatus.APPROVED) {
            throw new RuntimeException("Your account is not approved yet.");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        emailService.sendLoginEmail(user.getEmail(), user.getUsername());

        log.info("User logged in successfully: {}", username);

        return new LoginResponse(
                "User logged in successfully",
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }

    @Override
    public String approveUser(Long userId, String approverUsername, boolean approve) {

        User approver = userRepository.findByUsername(approverUsername)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (approver.getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You cannot approve yourself.");
        }

        if (!isAuthorizedToApprove(approver.getRole(), user.getRole())) {
            throw new RuntimeException("You are not authorized to approve this user.");
        }

        user.setStatus(approve ? ApprovalStatus.APPROVED : ApprovalStatus.UNAPPROVED);
        userRepository.save(user);

        if (approve) {
            emailService.sendApprovalEmail(user.getEmail(), user.getUsername());
            return "User approved successfully";
        } else {
            emailService.sendRejectionEmail(user.getEmail(), user.getUsername());
            return "User rejected successfully";
        }
    }

    private boolean isAuthorizedToApprove(Role approverRole, Role targetRole) {

        //  SUPER_ADMIN can approve ANYONE
        if (approverRole == Role.SUPER_ADMIN) {
            return true;
        }

        // District Admin can approve Federation Admin
        if (approverRole == Role.DISTRICT_ADMIN
                && targetRole == Role.FEDERATION_ADMIN) {
            return true;
        }

        // Federation Admin can approve SHG Admin
        if (approverRole == Role.FEDERATION_ADMIN
                && targetRole == Role.SHG_ADMIN) {
            return true;
        }

//        // SHG Admin can approve SHG Member & Customer
//        if (approverRole == Role.SHG_ADMIN
//                && (targetRole == Role.SHG_MEMBER
//                || targetRole == Role.CUSTOMER)) {
//            return true;
//        }

        return false;
    }
}