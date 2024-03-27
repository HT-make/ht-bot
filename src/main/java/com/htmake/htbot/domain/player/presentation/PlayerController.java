package com.htmake.htbot.domain.player.presentation;

import com.htmake.htbot.domain.player.presentation.data.request.PlayerJoinRequest;
import com.htmake.htbot.domain.player.presentation.data.response.PlayerJoinResponse;
import com.htmake.htbot.domain.player.service.PlayerJoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
public class PlayerController {

    private final PlayerJoinService playerJoinService;

    @PostMapping("/join")
    public ResponseEntity<PlayerJoinResponse> join(@RequestBody @Valid PlayerJoinRequest request) {
        PlayerJoinResponse response = playerJoinService.execute(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
