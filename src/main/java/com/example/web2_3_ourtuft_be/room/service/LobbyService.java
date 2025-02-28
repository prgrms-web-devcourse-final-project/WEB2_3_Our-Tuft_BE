package com.example.web2_3_ourtuft_be.room.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.AccessDeniedException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.AccessDeniedMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.room.dto.RoomRequestDto;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final RoomRepository roomRepository;
    private final UserService userService;

    public List<RoomResponseDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        if (rooms.isEmpty()) {
            throw new NotFoundException(NotFoundMessages.ROOM);
        }

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
                        .build();

        room = roomRepository.save(room);

        RoomResponseDto responseDto = new RoomResponseDto(room);

        // messagingTemplate.converAndSend("/topin/room" + roomId, responseDto);

        return responseDto;
    }

    @Transactional
    public void changeRoomHost(Long roomId, Long newHostId) {

        Room room = findByRoomId(roomId);
        User newHost = userService.getUser(newHostId);

        room.changeHost(newHost.getId());

        roomRepository.save(room);

        /* 방장 변경 시 방 안에 있는 유저들에게 즉시 반영..
        Map<String, Object> hostChangeInfo = new HashMap<>();
        hostChangeInfo.put("roomID", roomId);
        hostChangeInfo.put("newHostID", newHostId);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, hostChangeInfo);
         */
    }

    private Room findByRoomId(Long roomId) {
        return roomRepository
                .findById(roomId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.ROOM_ID));
    }

    public void deleteRoom(Long roomId) {

        Room room =
                roomRepository
                        .findById(roomId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.ROOM_ID));

        roomRepository.delete(room);
    }
}
