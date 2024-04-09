package com.htmake.htbot.discord.commands.battle.data;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Situation {

    private List<String> messageList;
}
