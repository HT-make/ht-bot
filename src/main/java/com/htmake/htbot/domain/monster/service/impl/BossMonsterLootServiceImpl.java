package com.htmake.htbot.domain.monster.service.impl;

import com.htmake.htbot.domain.monster.entity.Monster;
import com.htmake.htbot.domain.monster.presentation.data.response.BossMonsterLootResponse;
import com.htmake.htbot.domain.monster.presentation.data.response.DropItemResponse;
import com.htmake.htbot.domain.monster.repository.MonsterRepository;
import com.htmake.htbot.domain.monster.service.BossMonsterLootService;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@ReadOnlyService
@RequiredArgsConstructor
public class BossMonsterLootServiceImpl implements BossMonsterLootService {

    private final MonsterRepository monsterRepository;

    @Override
    public BossMonsterLootResponse execute(String bossId) {

        Monster bossMonster = monsterRepository.findById(bossId)
                .orElseThrow();

        return BossMonsterLootResponse.builder()
                .exp(bossMonster.getExp())
                .gold(bossMonster.getGold())
                .gem(bossMonster.getGem())
                .bossCoin(bossMonster.getBossCoin())
                .dropItemList(
                        bossMonster.getDropItems().stream()
                                .map(DropItemResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
