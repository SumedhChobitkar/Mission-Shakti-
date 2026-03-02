package com.Mission.Shakti.Security;

import com.Mission.Shakti.Entity.User;
import com.Mission.Shakti.Enum.ApprovalStatus;
import com.Mission.Shakti.Enum.Role;
import com.Mission.Shakti.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUpDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("superadmin").isEmpty()) {

            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setEmail("superadmin@gmail.com");
            superAdmin.setPassword(passwordEncoder.encode("super123"));
            superAdmin.setRole(Role.SUPER_ADMIN);
            superAdmin.setStatus(ApprovalStatus.APPROVED);

            userRepository.save(superAdmin);

            System.out.println("SUPER ADMIN CREATED");
        }
    }
}
