package com.htmake.htbot.discord.commands.dungeon.data;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldDungeonStatus {

    private String name;

    private int stage;

    private Map<Integer, DungeonMonster> monsterByStage;

    private DungeonPlayer dungeonPlayer;

    private List<GetItem> getItemList;
}
