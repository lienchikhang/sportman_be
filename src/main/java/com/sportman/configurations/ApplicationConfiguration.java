package com.sportman.configurations;

import com.sportman.entities.Role;
import com.sportman.entities.User;
import com.sportman.entities.UserRole;
import com.sportman.enums.EnumRole;
import com.sportman.exceptions.AppException;
import com.sportman.exceptions.ErrorCode;
import com.sportman.repositories.RoleRepository;
import com.sportman.repositories.UserRepository;
import com.sportman.repositories.UserRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationConfiguration {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Bean
    @Transactional
    public ApplicationRunner applicationRunner(UserRoleRepository userRoleRepository) {
        return args -> {

            if (!roleRepository.existsById(EnumRole.ADMIN)) {
                roleRepository.save(Role.builder().roleDesc("This is an admin role").name(EnumRole.ADMIN).build());
            }


            if (!userRepository.existsByUsername("admin")) {

                //get admin role
                Role role = roleRepository.findById(EnumRole.ADMIN).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

                HashSet<UserRole> roles = new HashSet<>();


                //create new admin user
                User admin = User
                        .builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123456@Ll"))
                        .firstName("admin")
                        .lastName("admin")
                        .email("admin@gmail.com")
                        .build();

                admin.setIsDeleted(false);

                roles.add(UserRole
                        .builder()
                        .role(role)
                        .build());

                admin.setUserRole(roles);
                admin.getUserRole().forEach(userRole -> userRole.setUser(admin)); //set ngược lại (do mỗi userRole tham chiếu đến user nên không thể null)

                userRepository.save(admin);

                log.info("Admin account has been created with default password");
            }
        };
    }


}
