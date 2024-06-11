package com.htmake.htbot.domain.shop.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.shop.entity.RandomShop;
import com.htmake.htbot.domain.shop.exception.NotEnoughGoldException;
import com.htmake.htbot.domain.shop.exception.NotEnoughQuantityException;
import com.htmake.htbot.domain.shop.exception.NotFoundItemException;
import com.htmake.htbot.domain.shop.presentation.data.request.RandomShopPurchaseRequest;
import com.htmake.htbot.domain.shop.presentation.data.response.SuccessPurchaseResponse;
import com.htmake.htbot.domain.shop.repository.RandomShopRepository;
import com.htmake.htbot.domain.shop.service.RandomShopItemPurchaseService;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

@TransactionalService
@RequiredArgsConstructor
public class RandomShopItemPurchaseServiceImpl implements RandomShopItemPurchaseService {

    private final RandomShopRepository randomShopRepository;
    private final InventoryRepository inventoryRepository;
    private final PlayerRepository playerRepository;

    @Override
    public SuccessPurchaseResponse execute(String playerId, RandomShopPurchaseRequest request) {
        RandomShop item = randomShopRepository.findByName(request.getName())
                .orElseThrow(NotFoundItemException::new);

        if (item.getQuantity() <= 0) {
            throw new NotEnoughQuantityException();
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        int playerGold = player.getGold();

        if (playerGold < item.getGold()) {
            throw new NotEnoughGoldException();
        }

        player.setGold(playerGold - item.getGold());
        playerRepository.save(player);
        item.setQuantity(item.getQuantity() - 1);
        randomShopRepository.save(item);

        Inventory existingItem = inventoryRepository
                .findByPlayerIdAndItemId(player.getId(), item.getId()).orElse(null);

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

        return SuccessPurchaseResponse.builder()
                .gold(player.getGold())
                .build();
    }
}
