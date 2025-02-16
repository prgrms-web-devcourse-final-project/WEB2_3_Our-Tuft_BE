package com.mockApi.api.controller;

import com.mockApi.api.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @PutMapping("/{id}/settings")
    public ResponseEntity<ResponseRoomSettingsDTO> roomSetting(
            @PathVariable("id") Long id,
            @RequestBody RequestRoomSettingsDTO requestRoomSettingsDTO) {


        ResponseRoomSettingsDTO responseRoomSettingsDTO = ResponseRoomSettingsDTO.builder()
                .id(id)
                .gameType(requestRoomSettingsDTO.getGameType())
                .roomName(requestRoomSettingsDTO.getRoomName())
                .disclosure(requestRoomSettingsDTO.isDisclosure())
                .roomPassword(requestRoomSettingsDTO.getRoomPassword())
                .peopleEntering(requestRoomSettingsDTO.getPeopleEntering())
                .round(requestRoomSettingsDTO.getRound())
                .timeLimit(requestRoomSettingsDTO.getTimeLimit())
                .host(requestRoomSettingsDTO.getHost())
                .build();

        return ResponseEntity.ok(responseRoomSettingsDTO);
    }

    @PutMapping("/{id}/settings/game")
    public ResponseEntity<ResponseRoomSettingsDTO> roomSettingGame(
            @PathVariable("id") Long id,
            @RequestParam ("gameType") String gameType
            ) {

        ResponseRoomSettingsDTO responseRoomSettingsDTO =new ResponseRoomSettingsDTO();
        responseRoomSettingsDTO.setGameType(gameType);

        return ResponseEntity.ok(responseRoomSettingsDTO);
    }

    @PutMapping("/{id}/settings/host")
    public ResponseEntity<ResponseRoomSettingsDTO> roomSettingHost(
            @PathVariable("id") Long id,
            @RequestParam ("host") String host
    ) {
        ResponseRoomSettingsDTO responseRoomSettingsDTO =new ResponseRoomSettingsDTO();
        responseRoomSettingsDTO.setHost(host);

        return ResponseEntity.ok(responseRoomSettingsDTO);

    }

    @PutMapping("/{nickName}/deport")
    public ResponseEntity<List<String>> deprotPlayer(
            @PathVariable("nickName") String nickName,
            @RequestBody List<String> enterPlayers) {

        Iterator<String> iterator = enterPlayers.iterator();
        while (iterator.hasNext()) {
            String depotPlayer = iterator.next();
            if (depotPlayer.equals(nickName)) {
                iterator.remove();
            }
        }

        return ResponseEntity.ok(enterPlayers);
    }

    @GetMapping("/quizzes")
    public ResponseEntity<List> getQuizeList() {
        List<String> quizeList = new ArrayList<>();
        quizeList.add("퀴즈Sample1");
        quizeList.add("퀴즈Sample2");
        quizeList.add("퀴즈Sample3");

        return ResponseEntity.ok(quizeList) ;

    }

    @PutMapping("/{roomId}/players/{playerId}/ready")
    public ResponseEntity<ReadyStatusResponseDTO> updateReadyStatus(
            @PathVariable Long roomId,
            @PathVariable Long playerId,
            @RequestParam boolean isReady) {

        ReadyStatusResponseDTO readyStatusResponseDTO = new ReadyStatusResponseDTO(
                playerId,
                roomId,
                isReady,
                isReady ? "플레이어가 준비 완료되었습니다." : "플레이어가 준비 취소되었습니다."
        );

        return ResponseEntity.ok(readyStatusResponseDTO);
    }

    @PutMapping("/{roomId}/start")
    public ResponseEntity<GameStartResponseDTO> startGame(
            @PathVariable Long roomId,
            @RequestBody GameStartRequestDTO requestDTO) {

        //  최소 인원 체크
        if (requestDTO.getPlayers().size() < requestDTO.getMinPlayers()) {
            return ResponseEntity.badRequest()
                    .body(new GameStartResponseDTO(roomId, "최소 인원 미달로 게임을 시작할 수 없습니다.", 0, requestDTO.getPlayers()));
        }

        // 모든 플레이어 준비 상태 체크
        boolean allReady = requestDTO.getPlayers().stream().allMatch(PlayerDTO::isReady);
        if (!allReady) {
            return ResponseEntity.badRequest()
                    .body(new GameStartResponseDTO(roomId, "모든 플레이어가 준비 완료 상태여야 합니다.", 0, requestDTO.getPlayers()));
        }

        //  게임 시작 응답 생성
        GameStartResponseDTO responseDTO = new GameStartResponseDTO(
                roomId, "Game Start", 1, requestDTO.getPlayers()
        );

        return ResponseEntity.ok(responseDTO);
    }






}
