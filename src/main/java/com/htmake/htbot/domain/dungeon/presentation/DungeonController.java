package com.htmake.htbot.domain.dungeon.presentation;

import com.htmake.htbot.domain.dungeon.presentation.data.response.BossDungeonInfoResponse;
import com.htmake.htbot.domain.dungeon.presentation.data.response.DungeonResponse;
import com.htmake.htbot.domain.dungeon.service.BossDungeonEntryService;
import com.htmake.htbot.domain.dungeon.service.BossDungeonInfoService;
import com.htmake.htbot.domain.dungeon.service.FieldDungeonEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dungeon")
public class DungeonController {

    private final FieldDungeonEntryService fieldDungeonEntryService;
    private final BossDungeonInfoService bossDungeonInfoService;
    private final BossDungeonEntryService bossDungeonEntryService;

    @GetMapping("/{dungeon_id}")
    public ResponseEntity<DungeonResponse> fieldDungeonEntry(@PathVariable("dungeon_id") String dungeonId) {
        DungeonResponse response = fieldDungeonEntryService.execute(dungeonId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{dungeon_id}/{player_id}")
    public ResponseEntity<BossDungeonInfoResponse> bossDungeonInfo(
            @PathVariable("dungeon_id") String dungeonId,
            @PathVariable("player_id") String playerId
    ) {
        BossDungeonInfoResponse response = bossDungeonInfoService.execute(dungeonId, playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{dungeon_id}/{player_id}")
    public ResponseEntity<DungeonResponse> bossDungeonEntry(
            @PathVariable("dungeon_id") String dungeonId,
            @PathVariable("player_id") String playerId
    ) {
        DungeonResponse response = bossDungeonEntryService.execute(dungeonId, playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
