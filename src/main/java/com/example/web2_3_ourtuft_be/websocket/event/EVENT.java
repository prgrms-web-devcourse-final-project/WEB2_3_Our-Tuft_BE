package com.example.web2_3_ourtuft_be.websocket.event;

public enum EVENT {READY("PLAYER_CHANGE_READY"),
    SWITCHING_ROOM_TO_GAME("SWITCHING_ROOM_TO_GAME"),
    GAME_STARTED("GAME_STARTED"),
    GAME_END("GAME_END");

    private final String value;

    EVENT(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
