package com.example.web2_3_ourtuft_be.room.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.AccessDeniedException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidValueException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.AccessDeniedMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.BadRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomSettingService;
import com.example.web2_3_ourtuft_be.room.dto.RoomDetailResponseDto;
import com.example.web2_3_ourtuft_be.room.dto.RoomRequestDto;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.repository.UserRepository;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import com.example.web2_3_ourtuft_be.websocket.service.WebSocketService;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final RoomRepository roomRepository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ParticipantService participantService;
    private final UserRepository userRepository;
    private final RoomSettingService roomSettingService;
    private final WebSocketService webSocketService;

    @Transactional
    public void changeRoomPlayingStatus(String roomId) {
        Room room = findByRoomId(Long.valueOf(roomId));

        room.changePlayingStatus();
    }

    public List<RoomResponseDto> searchRoom(String roomName, Long roomId) {

        List<Room> rooms = new ArrayList<>();

        if (roomId != null) {

            Room room = roomRepository.findById(roomId).orElse(null);

            if (room != null) {
                rooms.add(room);
            }

        } else if (roomName != null) {

            rooms = roomRepository.findByRoomNameContaining(roomName);

        } else {
            throw new InvalidRequestException(InvalidRequestMessages.EMPTY_SEARCH_CONDITION);
        }

        return rooms.stream().map(RoomResponseDto::new).collect(Collectors.toList());
    }

    public RoomResponseDto createRoom(RoomRequestDto roomRequestDto, Long userId) {

        Room room =
                Room.builder()
                        .roomName(roomRequestDto.getRoomName())
                        .disclosure(roomRequestDto.isDisclosure())
                        .roomPassword(roomRequestDto.getPassword())
                        .round(roomRequestDto.getRound())
                        .hostId(userId)
                        .gameType(roomRequestDto.getGameType())
                        .maxUsers(roomRequestDto.getMaxUsers())
                        .time(roomRequestDto.getTime())
                        .build();

        room = roomRepository.save(room);

        // 해당 룸 key로 참여인원 저장
        User host =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));

        participantService.addHost(room.getId(), userId, host.getName());

        roomSettingService.saveRoomSettingsToRedis(room.getId(), roomRequestDto);

        webSocketService.sendEvent("lobby", "ROOM_CREATED");

        return new RoomResponseDto(room);
    }

    public RoomResponseDto updateRoomSettings(
            Long roomId, Long userId, RoomRequestDto roomRequestDto) {

        Room room = findByRoomId(roomId);

        if (!room.getHostId().equals(userId)) {
            throw new AccessDeniedException(AccessDeniedMessages.ROOM_SETTING);
        }

        room =
                Room.builder()
                        .id(room.getId())
                        .roomName(roomRequestDto.getRoomName())
                        .disclosure(roomRequestDto.isDisclosure())
                        .roomPassword(roomRequestDto.getPassword())
                        .round(roomRequestDto.getRound())
                        .gameType(roomRequestDto.getGameType())
                        .time(roomRequestDto.getTime())
                        .maxUsers(roomRequestDto.getMaxUsers())
                        .build();

        room = roomRepository.save(room);

        RoomResponseDto responseDto = new RoomResponseDto(room);

        messagingTemplate.convertAndSend("/topic/lobby/", responseDto);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, responseDto);

        return responseDto;
    }

    @Transactional
    public void changeRoomHost(Long roomId, Long newHostId) {

        Room room = findByRoomId(roomId);
        User newHost = userService.getUser(newHostId);

        room.changeHost(newHost.getId());

        participantService.changeReadyForNewHost(room.getId(), newHost.getId());

        roomRepository.save(room);
    }

    public Room findByRoomId(Long roomId) {
        return roomRepository
                .findById(roomId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.ROOM_ID));
    }

    public Long getHostIdByRoomId(String roomId) {
        if ("lobby".equals(roomId))
            throw new InvalidRequestException(InvalidRequestMessages.INVALID_ROOM_ID);

        Room room = findByRoomId(Long.valueOf(roomId));

        return room.getHostId();
    }

    public void deleteRoom(Long roomId) {

        Room room = findByRoomId(roomId);
        roomRepository.delete(room);
    }

    public boolean isHost(Long roomId, Long userId) {
        Room room = findByRoomId(roomId);
        Long hostId = room.getHostId();
        return hostId != null && hostId.equals(userId);
    }

    public RoomDetailResponseDto getRoomDetail(Long roomId, String password) {
        Room room = findByRoomId(roomId);

        if (room.isGameRunning()) {
            throw new InvalidValueException(BadRequestMessages.PLAYING_GAME_ROOM);
        }

        if (room.isDisclosure()) { // 공개방
            if (password != null && !password.isEmpty()) {
                throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD_DISCLOSURE);
            }
        } else { // 비공개방
            if (password == null || password.isEmpty()) {
                throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD);
            }
            if (password.length() != 4) {
                throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD_LENGTH);
            }
            if (!password.matches("^[0-9]+$")) {
                throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD_FORMAT);
            }
            if (!room.getRoomPassword().equals(password)) {
                throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD_WRONG);
            }
        }

        return new RoomDetailResponseDto(room);
    }
}
