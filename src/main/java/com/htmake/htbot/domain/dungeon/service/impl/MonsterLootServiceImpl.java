package com.htmake.htbot.domain.dungeon.service.impl;

import com.htmake.htbot.domain.dungeon.entity.Monster;
import com.htmake.htbot.domain.dungeon.presentation.data.response.DropItemResponse;
import com.htmake.htbot.domain.dungeon.presentation.data.response.MonsterLootResponse;
import com.htmake.htbot.domain.dungeon.repository.MonsterRepository;
import com.htmake.htbot.domain.dungeon.service.MonsterLootService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
