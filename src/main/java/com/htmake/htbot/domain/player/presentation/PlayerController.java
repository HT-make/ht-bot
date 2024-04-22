package com.htmake.htbot.domain.player.presentation;

import com.htmake.htbot.domain.player.presentation.data.request.KillMonsterRequest;
import com.htmake.htbot.domain.player.presentation.data.request.PlayerJoinRequest;
import com.htmake.htbot.domain.player.presentation.data.response.*;
import com.htmake.htbot.domain.player.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
public class PlayerController {

    private final PlayerJoinService playerJoinService;
    private final PlayerJoinCheckService playerJoinCheckService;
    private final PlayerInfoService playerInfoService;
    private final PlayerBattleService playerBattleService;
    private final KillMonsterService killMonsterService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody @Valid PlayerJoinRequest request) {
        playerJoinService.execute(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/check/{player_id}")
    public ResponseEntity<PlayerJoinCheckResponse> check(@PathVariable("player_id") String playerId) {
        PlayerJoinCheckResponse response = playerJoinCheckService.execute(playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/info/{player_id}")
    public ResponseEntity<PlayerInfoResponse> info(@PathVariable("player_id") String playerId) {
        PlayerInfoResponse response = playerInfoService.execute(playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/battle/{player_id}")
    public ResponseEntity<PlayerBattleResponse> battle(@PathVariable("player_id") String playerId) {
        PlayerBattleResponse response = playerBattleService.execute(playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/battle/win/{player_id}")
    public ResponseEntity<LevelUpResponse> battleWin(
            @PathVariable("player_id") String playerId,
            @RequestBody KillMonsterRequest request
    ) {
        LevelUpResponse response = killMonsterService.execute(playerId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
