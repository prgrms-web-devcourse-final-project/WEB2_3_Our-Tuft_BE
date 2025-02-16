package com.mockApi.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResponseRoomSettingsDTO {
    private long id ;
    private String gameType = "SPEED_QUIZ" ;
    private String roomName = "퀴즈방" ;
    private boolean disclosure = false ;
    private String roomPassword = null;
    private int peopleEntering = 8;
    private int round =30 ;
    private int timeLimit = 10;
    private String host = "manager" ;

    @Builder
    public ResponseRoomSettingsDTO(long id, String gameType, String roomName, boolean disclosure, String roomPassword, int peopleEntering, int round, int timeLimit, String host) {
        this.id = id;
        this.gameType = gameType;
        this.roomName = roomName;
        this.disclosure = disclosure;
        this.roomPassword = roomPassword;
        this.peopleEntering = peopleEntering;
        this.round = round;
        this.timeLimit = timeLimit;
        this.host = host;
    }

}
