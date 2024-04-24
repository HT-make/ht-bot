package com.htmake.htbot.discord.commands.dungeon.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DungeonStatus {

    private String name;

    private int stage;

    private List<DungeonMonster> dungeonMonsterList;

    private DungeonPlayer dungeonPlayer;

    private List<GetItem> getItemList;
}
