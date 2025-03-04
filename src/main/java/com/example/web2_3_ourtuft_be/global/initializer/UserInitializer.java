package com.example.web2_3_ourtuft_be.global.initializer;

import com.example.web2_3_ourtuft_be.auth.dto.CreateUserDto;
import com.example.web2_3_ourtuft_be.user.dto.RewardDto;
import com.example.web2_3_ourtuft_be.user.service.UserFacadeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements CommandLineRunner {
    private static final String TESTER = "tester";
    private final UserFacadeService userFacadeService;

    public UserInitializer(UserFacadeService userFacadeService) {
        this.userFacadeService = userFacadeService;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i < 4; i++) {
            createUser(i);
        }
    }

    private void createUser(int id) {
        CreateUserDto createUserDto = new CreateUserDto(TESTER + id, TESTER + id, TESTER + id);
        userFacadeService.registerUserForFE(createUserDto);
        userFacadeService.reward((long) id, new RewardDto(100, 5000));
    }
}
