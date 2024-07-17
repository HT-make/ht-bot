package com.htmake.htbot.domain.shop.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.shop.entity.BossShop;
import com.htmake.htbot.domain.shop.exception.NotEnoughBossCoinException;
import com.htmake.htbot.domain.shop.exception.NotFoundItemException;
import com.htmake.htbot.domain.shop.repository.BossShopRepository;
import com.htmake.htbot.domain.shop.service.BossShopItemPurchaseService;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

@TransactionalService
@RequiredArgsConstructor
public class BossShopItemPurchaseServiceImpl implements BossShopItemPurchaseService {

    private final BossShopRepository bossShopRepository;
    private final InventoryRepository inventoryRepository;
    private final PlayerRepository playerRepository;

    @Override
    public void execute(String playerId, String itemId) {

        BossShop item = bossShopRepository.findById(itemId)
                .orElseThrow(NotFoundItemException::new);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        Inventory bossCoin = inventoryRepository.findByPlayerIdAndName(player.getId(), "보스 코인")
                .orElseThrow(NotEnoughBossCoinException::new);

        if (bossCoin.getQuantity() < item.getCoin()) {
            throw new NotEnoughBossCoinException();
        }

        bossCoin.setQuantity(bossCoin.getQuantity() - item.getCoin());
        if (bossCoin.getQuantity() == 0) {
            inventoryRepository.delete(bossCoin);
        }

        Inventory existingItem = inventoryRepository.findByPlayerIdAndItemId(player.getId(), item.getId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            inventoryRepository.save(existingItem);
        } else {
            Inventory inventory = Inventory.builder()
                    .itemId(item.getId())
                    .player(player)
                    .name(item.getName())
                    .quantity(1)
                    .build();

            inventoryRepository.save(inventory);
        }
    }
}
