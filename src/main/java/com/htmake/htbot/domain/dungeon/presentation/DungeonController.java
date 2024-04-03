package com.htmake.htbot.domain.dungeon.presentation;

import com.htmake.htbot.domain.dungeon.presentation.data.response.DungeonResponse;
import com.htmake.htbot.domain.dungeon.presentation.data.response.MonsterLootResponse;
import com.htmake.htbot.domain.dungeon.service.DungeonService;
import com.htmake.htbot.domain.dungeon.service.MonsterLootService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dungeon")
public class DungeonController {

    private final DungeonService dungeonService;
    private final MonsterLootService monsterLootService;

    @GetMapping("/{dungeon_id}")
    public ResponseEntity<DungeonResponse> findById(@PathVariable("dungeon_id") String dungeonId) {
        DungeonResponse response = dungeonService.execute(dungeonId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/monster/{monster_name}")
    public ResponseEntity<MonsterLootResponse> getLoot(@PathVariable("monster_name") String monsterName) {
        MonsterLootResponse response = monsterLootService.execute(monsterName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
