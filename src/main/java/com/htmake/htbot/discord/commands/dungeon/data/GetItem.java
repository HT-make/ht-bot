package com.htmake.htbot.discord.commands.dungeon.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetItem {

    private String id;

    private String name;
}
