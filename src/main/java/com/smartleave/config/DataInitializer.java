package com.smartleave.config;

import com.smartleave.model.Role;
import com.smartleave.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        seedRole("USER");
        seedRole("ADMIN");
    }

    private void seedRole(String roleName) {
        boolean exists = roleRepository.findByName(roleName).isPresent();
        if (!exists) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.println("Seeded role: " + roleName);
        }
    }
}
