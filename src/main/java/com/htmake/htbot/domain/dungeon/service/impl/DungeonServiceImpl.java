package com.htmake.htbot.domain.dungeon.service.impl;

import com.htmake.htbot.domain.dungeon.entity.Dungeon;
import com.htmake.htbot.domain.dungeon.presentation.data.response.DungeonResponse;
import com.htmake.htbot.domain.dungeon.presentation.data.response.MonsterResponse;
import com.htmake.htbot.domain.dungeon.repository.DungeonRepository;
import com.htmake.htbot.domain.dungeon.service.DungeonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DungeonServiceImpl implements DungeonService {

    private final DungeonRepository dungeonRepository;

    @Override
    public DungeonResponse execute(String dungeonId) {
        Dungeon dungeon = dungeonRepository.findById(dungeonId)
                .orElseThrow();

        return DungeonResponse.builder()
                .name(dungeon.getName())
                .monsterList(
                        dungeon.getMonsters().stream().map(MonsterResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
