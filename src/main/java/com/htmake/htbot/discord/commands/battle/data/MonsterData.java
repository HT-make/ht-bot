package com.htmake.htbot.discord.commands.battle.data;

import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterOriginalStatus;
import com.htmake.htbot.discord.commands.battle.data.status.extend.MonsterStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonsterData {

    private MonsterStatus monsterStatus;

    private MonsterOriginalStatus monsterOriginalStatus;
}
