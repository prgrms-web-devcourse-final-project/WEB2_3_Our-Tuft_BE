// package com.mockApi.api.controller;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.mockApi.api.dto.RequestRoomSettingsDTO;
// import com.mockApi.api.dto.ResponseRoomSettingsDTO;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// @WebMvcTest(RoomController.class)
// class RoomControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @DisplayName("대기실 설정 변경시 변경값을 리턴한다.")
//    @Test
//    void testRoomSettings() throws Exception  {
//
//        long roomId = 123L;
//        RequestRoomSettingsDTO requestDto = RequestRoomSettingsDTO.builder()
//                .id(roomId)
//                .gameType("SPEED_QUIZ")
//                .roomName("퀴즈방")
//                .disclosure(false)
//                .roomPassword(null)
//                .peopleEntering(5)
//                .round(3)
//                .timeLimit(10)
//                .host("manager")
//                .build();
//
//        // When & Then
//        mockMvc.perform(put("/room/{id}/settings",roomId )
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))) // JSON 변환
//                .andExpect(status().isOk()) // 200 응답 코드 검증
//                .andExpect(jsonPath("$.id").value(123))
//                .andExpect(jsonPath("$.gameType").value("SPEED_QUIZ"))
//                .andExpect(jsonPath("$.roomName").value("퀴즈방"))
//                .andExpect(jsonPath("$.disclosure").value(false))
//                .andExpect(jsonPath("$.roomPassword").isEmpty())
//                .andExpect(jsonPath("$.peopleEntering").value(5))
//                .andExpect(jsonPath("$.round").value(3))
//                .andExpect(jsonPath("$.timeLimit").value(10))
//                .andExpect(jsonPath("$.host").value("manager"))
//        ;
//    }
//
//    @DisplayName("gameType을 전달받아 해당하는 룸의 게임종목을 변경한다.")
//    @Test
//    void roomSettingGame() throws Exception {
//        // given
//        long id = 123L; // 테스트용 room ID
//        String gameType = "OX퀴즈"; // 테스트용 gameType
//
//        // when & then
//        mockMvc.perform(put("/room/{id}/settings/game", id)
//                        .param("gameType", gameType) // gameType 파라미터 전달
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()) // 응답 상태 코드 확인
//                .andExpect(jsonPath("$.gameType").value(gameType)); // gameType 값 확인
//    }
//
//    @DisplayName("변경할 host 닉네임을 전달받아 해당하는 룸의 host를 변경한다.")
//    @Test
//    void roomSettingHost() throws Exception {
//        // given
//        long id = 123L; // 테스트용 room ID
//        String host = "manager2"; // 테스트용 gameType
//
//        // when & then
//        mockMvc.perform(put("/room/{id}/settings/host", id)
//                        .param("host", host) // gameType 파라미터 전달
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()) // 응답 상태 코드 확인
//                .andExpect(jsonPath("$.host").value(host)); // gameType 값 확인
//    }
//
//
//    @DisplayName("닉네임으로 전달받은 플레이어를 리스트에서 삭제후 리턴한다. ")
//    @Test
//    void deportPlayers() throws Exception {
//    	// given
//        List<String> players = new ArrayList<>();
//        players.add("한놈");
//        players.add("두식이");
//        players.add("석삼");
//        players.add("너구리");
//
//
//        // when // then
//        mockMvc.perform(put("/room/석삼/deport")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(players))) // JSON 변환
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0]").value("한놈"))
//                .andExpect(jsonPath("$[1]").value("두식이"))
//                .andExpect(jsonPath("$[2]").value("너구리"));
//
//
//    }
//
//    @DisplayName("등록되어 있는 quizeList를 리턴한다.")
//    @Test
//    void getquizeList() throws Exception {
//
//        // when // then
//        mockMvc.perform(get("/room/quizzes"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0]").value("퀴즈Sample1"))
//                .andExpect(jsonPath("$[1]").value("퀴즈Sample2"))
//                .andExpect(jsonPath("$[2]").value("퀴즈Sample3"));
//
//
//    }
// }
