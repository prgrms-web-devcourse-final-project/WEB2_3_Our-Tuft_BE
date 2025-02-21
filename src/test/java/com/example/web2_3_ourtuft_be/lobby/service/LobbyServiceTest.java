package com.example.web2_3_ourtuft_be.lobby.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.web2_3_ourtuft_be.lobby.util.LobbyTestUtils;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LobbyServiceTest {

    @Autowired private RoomRepository roomRepository;

    @Autowired private LobbyService lobbyService;

    @BeforeEach
    void setUp() {
        roomRepository.save(LobbyTestUtils.createRoom("스피드퀴즈 1", 4, 3, "WAITING", "host1", true));
        roomRepository.save(
                LobbyTestUtils.createRoom("스피드퀴즈 2", 5, 5, "IN_PROGRESS", "host2", false));
        roomRepository.save(LobbyTestUtils.createRoom("OX 퀴즈", 6, 2, "WAITING", "host3", true));
        roomRepository.save(LobbyTestUtils.createRoom("캐치마인드", 8, 3, "WAITING", "host4", false));
    }

    @Test
    @DisplayName("전체 방 조회")
    void testGetAllRoom() {

        List<RoomResponseDto> result = lobbyService.getAllRooms();

        assertEquals(4, result.size());
    }

    @Test
    @DisplayName("방 검색 테스트")
    void testSearchRoom() {

    }
}
