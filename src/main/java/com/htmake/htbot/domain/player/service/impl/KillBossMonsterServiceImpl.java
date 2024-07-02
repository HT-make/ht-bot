package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.presentation.data.request.KillBossMonsterRequest;
import com.htmake.htbot.domain.player.presentation.data.response.LevelUpResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.KillBossMonsterService;
import com.htmake.htbot.global.annotation.TransactionalService;
import com.htmake.htbot.global.util.PlayerUtil;
import lombok.RequiredArgsConstructor;

@TransactionalService
@RequiredArgsConstructor
public class KillBossMonsterServiceImpl implements KillBossMonsterService {

    private final PlayerRepository playerRepository;
    private final InventoryRepository inventoryRepository;
    private final PlayerUtil playerUtil;

    @Override
    public LevelUpResponse execute(String playerId, KillBossMonsterRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow();

        int exp = request.getExp();
        int gold = request.getGold();
        int gem = request.getGem();

        Inventory bossCoin = inventoryRepository
                .findByPlayerIdAndName(playerId, "보스 코인").orElse(null);

        if (bossCoin != null) {
            bossCoin.setQuantity(bossCoin.getQuantity() + request.getBossCoin());
            inventoryRepository.save(bossCoin);
        } else {
            Inventory inventory = Inventory.builder()
                    .itemId("43000")
                    .player(player)
                    .name("보스 코인")
                    .quantity(request.getBossCoin())
                    .build();

            inventoryRepository.save(inventory);
        }

        player.setGem(player.getGem() + gem);

        return playerUtil.executeLevelUp(player, exp, gold);
    }
}
