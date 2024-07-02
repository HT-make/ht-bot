package com.htmake.htbot.domain.monster.presentation;

import com.htmake.htbot.domain.monster.presentation.data.response.BossMonsterLootResponse;
import com.htmake.htbot.domain.monster.presentation.data.response.MonsterLootResponse;
import com.htmake.htbot.domain.monster.service.BossMonsterLootService;
import com.htmake.htbot.domain.monster.service.MonsterLootService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monster")
public class MonsterController {

    private final MonsterLootService monsterLootService;
    private final BossMonsterLootService bossMonsterLootService;

    @GetMapping("/loot/{monster_id}")
    public ResponseEntity<MonsterLootResponse> getLoot(@PathVariable("monster_id") String monsterId) {
        MonsterLootResponse response = monsterLootService.execute(monsterId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/boss/loot/{boss_id}")
    public ResponseEntity<BossMonsterLootResponse> getBossLoot(@PathVariable("boss_id") String bossId) {
        BossMonsterLootResponse response = bossMonsterLootService.execute(bossId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
