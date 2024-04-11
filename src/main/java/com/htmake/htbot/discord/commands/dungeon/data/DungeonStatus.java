package com.htmake.htbot.discord.commands.dungeon.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DungeonStatus {

    private String id;

    private int stage;

    private List<GetItem> getItemList;
}
