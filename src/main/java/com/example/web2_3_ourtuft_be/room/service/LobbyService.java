package com.example.web2_3_ourtuft_be.room.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.AccessDeniedException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.AccessDeniedMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.room.dto.RoomRequestDto;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import com.example.web2_3_ourtuft_be.websocket.dto.WebSocketResponse;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final RoomRepository roomRepository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ParticipantService participantService;

    public List<RoomResponseDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream().map(RoomResponseDto::new).collect(Collectors.toList());
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

        roomRepository.save(room);

        Map<String, Object> hostChangeInfo = new HashMap<>();
        hostChangeInfo.put("roomID", roomId);
        hostChangeInfo.put("newHostID", newHostId);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, hostChangeInfo);
    }

    public Room findByRoomId(Long roomId) {
        return roomRepository
                .findById(roomId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.ROOM_ID));
    }

    public void deleteRoom(Long roomId) {

        Room room = findByRoomId(roomId);

        roomRepository.delete(room);

        messagingTemplate.convertAndSend("/topic/lobby/", "deleted:" + roomId);
    }

    public boolean isHost(Long roomId, Long userId) {
        Room room = findByRoomId(roomId);
        Long hostId = room.getHostId();
        return hostId != null && hostId.equals(userId);
    }

    @Transactional
    public ResponseEntity<GlobalResponse<String>> leaveRoom(Long roomId, Long userId) {
        boolean isHost = isHost(roomId, userId);
        participantService.removeParticipant(roomId, userId);

        // 남은 참가자수 확인
        Map<String, String> participants = participantService.getParticipants(roomId);

        if (participants.isEmpty()) { // 마지막 사람이 나갔으면

            deleteRoom(roomId);

            messagingTemplate.convertAndSend(
                    "/topic/room/" + roomId.toString(),
                    WebSocketResponse.Send.of("SYSTEM", "방이 삭제되었습니다."));

            return ResponseEntity.ok(GlobalResponse.success("방이 삭제되었습니다."));

        } else { // 잔여인원이 있는 경우

            // 방장이 나갔을 경우 방장 변경
            if (isHost) {
                String newHostId = participantService.getNextHost(String.valueOf(roomId));

                if (newHostId != null) {
                    changeRoomHost(roomId, Long.parseLong(newHostId));

                    messagingTemplate.convertAndSend(
                            "/topic/room/" + roomId.toString(),
                            WebSocketResponse.Send.of("SYSTEM", "방장이 변경되었습니다."));

                    return ResponseEntity.ok(GlobalResponse.success("방장이 변경되었습니다."));
                }
            }

            messagingTemplate.convertAndSend(
                    "/topic/room/" + roomId.toString(),
                    WebSocketResponse.Send.of("SYSTEM", "방을 나갔습니다."));

            return ResponseEntity.ok(GlobalResponse.success("방을 성공적으로 나갔습니다."));
        }
    }
}
