package com.htmake.htbot.domain.monster.service.impl;

import com.htmake.htbot.domain.monster.entity.Monster;
import com.htmake.htbot.domain.monster.presentation.data.response.DropItemResponse;
import com.htmake.htbot.domain.monster.presentation.data.response.MonsterLootResponse;
import com.htmake.htbot.domain.monster.repository.MonsterRepository;
import com.htmake.htbot.domain.monster.service.MonsterLootService;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@ReadOnlyService
@RequiredArgsConstructor
public class MonsterLootServiceImpl implements MonsterLootService {

    private final MonsterRepository monsterRepository;

    @Override
    public MonsterLootResponse execute(String monsterId) {

        Monster monster = monsterRepository.findById(monsterId)
                .orElseThrow();

        return MonsterLootResponse.builder()
                .exp(monster.getExp())
                .gold(monster.getGold())
                .dropItemList(
                        monster.getDropItems().stream()
                                .map(DropItemResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
