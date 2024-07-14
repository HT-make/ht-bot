package com.htmake.htbot.domain.quest.presentation;

import com.htmake.htbot.domain.quest.presentation.data.request.PlayerQuestMonsterQuantityRequest;
import com.htmake.htbot.domain.quest.presentation.data.response.PlayerQuestInfoResponse;
import com.htmake.htbot.domain.quest.presentation.data.response.QuestDialogueResponse;
import com.htmake.htbot.domain.quest.presentation.data.response.ReadDialogueCheckResponse;
import com.htmake.htbot.domain.quest.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quest")
public class QuestController {

    private final PlayerQuestInfoService playerQuestInfoService;
    private final ReadDialogueCheckService readDialogueCheckService;
    private final QuestDialogueService questDialogueService;
    private final PlayerQuestProgressService playerQuestProgressService;
    private final PlayerQuestMonsterQuantityService playerQuestMonsterQuantityService;

    @PostMapping("/{player_id}")
    public ResponseEntity<PlayerQuestInfoResponse> quest(@PathVariable("player_id") String playerId) {
        PlayerQuestInfoResponse response = playerQuestInfoService.execute(playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/dialogue/check/{player_id}")
    public ResponseEntity<ReadDialogueCheckResponse> readDialogueCheck(@PathVariable("player_id") String playerId) {
        ReadDialogueCheckResponse response = readDialogueCheckService.execute(playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/dialogue/{player_id}")
    public ResponseEntity<QuestDialogueResponse> questDialogue(
            @PathVariable("player_id") String playerId,
            @RequestParam("trigger") String trigger
    ) {
        QuestDialogueResponse response = questDialogueService.execute(playerId, trigger);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/progress/{player_id}")
    public ResponseEntity<Void> questProgress(@PathVariable("player_id") String playerId) {
        playerQuestProgressService.execute(playerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/monster/{player_id}")
    public ResponseEntity<Void> questMonster(
            @PathVariable("player_id") String playerId,
            @RequestBody @Valid PlayerQuestMonsterQuantityRequest request
    ) {
        playerQuestMonsterQuantityService.execute(playerId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
