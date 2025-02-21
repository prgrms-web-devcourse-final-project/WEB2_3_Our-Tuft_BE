package com.example.web2_3_ourtuft_be.item.game.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id", nullable = false)
    private Long id;

    @Column(name = "game_name", nullable = false)
    private String gameName;

    @Column(name = "min_players", nullable = false)
    private String minPlayers;

    @Column(name = "max_players", nullable = false)
    private String maxPlayers;

    @Column(name = "min_round", nullable = false)
    private String minRound;

    @Column(name = "max_round", nullable = false)
    private String maxRound;

    @Column(name = "time_limit", nullable = false)
    private String timeLimit;
}
