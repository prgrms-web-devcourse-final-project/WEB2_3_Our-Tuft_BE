package com.example.web2_3_ourtuft_be.lobby.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.lobby.util.LobbyTestUtils;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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

    private Room testRoom;

    @BeforeEach
    void setUp() {
        testRoom =
                roomRepository.save(
                        LobbyTestUtils.createRoom("스피드퀴즈 할 사람", 4, 3, "WAITING", "host1", true));
        roomRepository.save(LobbyTestUtils.createRoom("스피드퀴즈 1", 4, 3, "WAITING", "host2", true));
        roomRepository.save(
                LobbyTestUtils.createRoom("스피드퀴즈 2", 5, 5, "IN_PROGRESS", "host3", false));
        roomRepository.save(LobbyTestUtils.createRoom("OX 퀴즈 한판만", 6, 2, "WAITING", "host4", true));
        roomRepository.save(
                LobbyTestUtils.createRoom("OX 하고 캐치마인드 함", 7, 5, "WAITING", "host5", true));
        roomRepository.save(LobbyTestUtils.createRoom("캐치마인드", 8, 3, "WAITING", "host6", false));
    }

    @AfterEach
    void clear() {
        roomRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 방 조회")
    void testGetAllRoom() {

        List<RoomResponseDto> result = lobbyService.getAllRooms();

        assertEquals(4, result.size());
    }

    @Test
    @DisplayName("방 검색 테스트 - roomId로 검색")
    void testSearchRoomById() {

        List<RoomResponseDto> result = lobbyService.searchRoom(null, testRoom.getId());

        assertEquals(1, result.size());
        assertEquals(testRoom.getId(), result.get(0).getRoomId());
        assertEquals("스피드퀴즈 할 사람", result.get(0).getRoomName());
    }

    @Test
    @DisplayName("방 검색 테스트 - roomName으로 검색")
    void testSearchRoomByName() {

        List<RoomResponseDto> result = lobbyService.searchRoom("OX", null);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.getRoomName().equals("OX 퀴즈 한판만")));
        assertTrue(result.stream().anyMatch(r -> r.getRoomName().equals("OX 하고 캐치마인드 함")));
    }

    @Test
    @DisplayName("방 검색 테스트 - 존재하지 않는 roomId로 검색")
    void testSearchRoomByInvalidId() {

        List<RoomResponseDto> result = lobbyService.searchRoom(null, 999L);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("방 검색 테스트 - 존재하지 않는 roomName 검색")
    void testSearchRoomByInvalidName() {

        List<RoomResponseDto> result = lobbyService.searchRoom("캐티마인트", null);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("방 검색 테스트 - 검색 조건 없을 때")
    void testSearchRoomWithEmptyCondition() {

        InvalidRequestException e =
                assertThrows(
                        InvalidRequestException.class, () -> lobbyService.searchRoom(null, null));

        assertTrue(e.getMessage().contains("검색 조건을 입력하세요."));
    }
}
