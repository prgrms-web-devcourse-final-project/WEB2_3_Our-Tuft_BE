package com.mockApi.api.controller;

import com.mockApi.api.dto.*;
import com.mockApi.api.global.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@Tag(name = "📟 게임 방", description = "게임 방 관련 API")
public class GameRoomController {

  @Operation(summary = "방 설정 변경", description = "방 설정을 변경하는 API 입니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PutMapping("/{id}/settings")
  public ResponseEntity<GlobalResponse<ResponseRoomSettingsDTO>> roomSetting(
      @PathVariable("id") Long id, @RequestBody RequestRoomSettingsDTO requestRoomSettingsDTO) {

    ResponseRoomSettingsDTO responseRoomSettingsDTO =
        ResponseRoomSettingsDTO.builder()
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

    return ResponseEntity.ok().body(GlobalResponse.success(responseRoomSettingsDTO));
  }

  @Operation(summary = "방 게임 종류 변경", description = "방의 게임 종류를 변경합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PutMapping("/{id}/settings/game")
  public ResponseEntity<GlobalResponse<ResponseRoomSettingsDTO>> roomSettingGame(
      @PathVariable("id") Long id, @RequestParam("gameType") String gameType) {

    ResponseRoomSettingsDTO responseRoomSettingsDTO = new ResponseRoomSettingsDTO();
    responseRoomSettingsDTO.setGameType(gameType);

    return ResponseEntity.ok().body(GlobalResponse.success(responseRoomSettingsDTO));
  }

  @Operation(summary = "방장 변경", description = "방의 방장을 변경합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PutMapping("/{id}/settings/host")
  public ResponseEntity<GlobalResponse<ResponseRoomSettingsDTO>> roomSettingHost(
      @PathVariable("id") Long id, @RequestParam("host") String host) {
    ResponseRoomSettingsDTO responseRoomSettingsDTO = new ResponseRoomSettingsDTO();
    responseRoomSettingsDTO.setHost(host);

    return ResponseEntity.ok().body(GlobalResponse.success(responseRoomSettingsDTO));
  }

  @Operation(summary = "강제 퇴장", description = "특정 인원을 강제 퇴장 시킵니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PutMapping("/{nickName}/deport")
  public ResponseEntity<GlobalResponse<List<String>>> deprotPlayer(
      @PathVariable("nickName") String nickName, @RequestBody List<String> enterPlayers) {

    Iterator<String> iterator = enterPlayers.iterator();
    while (iterator.hasNext()) {
      String depotPlayer = iterator.next();
      if (depotPlayer.equals(nickName)) {
        iterator.remove();
      }
    }

    return ResponseEntity.ok().body(GlobalResponse.success(enterPlayers));
  }

  @Operation(summary = "퀴즈 목록 조회", description = "퀴즈 목록을 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/quizzes")
  public ResponseEntity<GlobalResponse<List>> getQuizeList() {
    List<String> quizeList = new ArrayList<>();
    quizeList.add("퀴즈Sample1");
    quizeList.add("퀴즈Sample2");
    quizeList.add("퀴즈Sample3");

    return ResponseEntity.ok().body(GlobalResponse.success(quizeList));
  }

  @Operation(summary = "준비 완료/취소", description = "참가자의 상태를 준비 완료/취소로 변경합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PutMapping("/{roomId}/players/{playerId}/ready")
  public ResponseEntity<GlobalResponse<ReadyStatusResponseDTO>> updateReadyStatus(
      @PathVariable Long roomId, @PathVariable Long playerId, @RequestParam boolean isReady) {

    ReadyStatusResponseDTO readyStatusResponseDTO =
        new ReadyStatusResponseDTO(
            playerId, roomId, isReady, isReady ? "플레이어가 준비 완료되었습니다." : "플레이어가 준비 취소되었습니다.");

    return ResponseEntity.ok().body(GlobalResponse.success(readyStatusResponseDTO));
  }

  @Operation(summary = "퀴즈 시작", description = "퀴즈를 시작합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PutMapping("/{roomId}/start")
  public ResponseEntity<GlobalResponse<GameStartResponseDTO>> startGame(
      @PathVariable Long roomId, @RequestBody GameStartRequestDTO requestDTO) {

    //  최소 인원 체크
    if (requestDTO.getPlayers().size() < requestDTO.getMinPlayers()) {
      return ResponseEntity.badRequest()
          .body(GlobalResponse.failed(HttpStatus.BAD_REQUEST, "최소 인원 미달로 게임을 시작할 수 없습니다."));
    }

    // 모든 플레이어 준비 상태 체크
    boolean allReady = requestDTO.getPlayers().stream().allMatch(PlayerDTO::isReady);
    if (!allReady) {
      return ResponseEntity.badRequest()
          .body(GlobalResponse.failed(HttpStatus.BAD_REQUEST, "모든 플레이어가 준비 완료 상태여야 합니다."));
    }

    //  게임 시작 응답 생성
    GameStartResponseDTO responseDTO =
        new GameStartResponseDTO(roomId, "Game Start", 1, requestDTO.getPlayers());

    return ResponseEntity.ok().body(GlobalResponse.success(responseDTO));
  }
}
