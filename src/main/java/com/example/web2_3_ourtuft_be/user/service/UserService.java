package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.auth.dto.CreateUserDto;
import com.example.web2_3_ourtuft_be.auth.dto.OAuth2Response;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.entity.enums.Provider;
import com.example.web2_3_ourtuft_be.user.entity.enums.Role;
import com.example.web2_3_ourtuft_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));
    }

    public User createUser(OAuth2Response userInfo) {
        User newUser =
                User.builder()
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .socialId(userInfo.getProviderId())
                        .provider(userInfo.getProvider().toString())
                        .role(Role.ROLE_USER.toString())
                        .build();
        userRepository.save(newUser);

        return newUser;
    }

    public User findUserBySocialId(String socialId) {
        return userRepository.findBySocialId(socialId).orElse(null);
    }

    // 프론트엔드 개발용 (소셜 구현되면 삭제 예정)
    public User createUserForFE(CreateUserDto userInfo) {
        User newUser =
                User.builder()
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .socialId(userInfo.getProviderId())
                        .provider(Provider.KAKAO.toString())
                        .role(Role.ROLE_USER.toString())
                        .build();
        userRepository.save(newUser);

        return newUser;
    }
}
