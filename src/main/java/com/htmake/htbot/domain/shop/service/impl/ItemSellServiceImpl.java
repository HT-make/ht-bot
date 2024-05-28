package com.htmake.htbot.domain.shop.service.impl;

import com.htmake.htbot.domain.equipment.repository.ArmorRepository;
import com.htmake.htbot.domain.equipment.repository.WeaponRepository;
import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.exception.InventoryItemNotFoundException;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.misc.repository.MiscRepository;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.quest.exception.NotEnoughItemQuantityException;
import com.htmake.htbot.domain.shop.presentation.data.request.ItemSellRequest;
import com.htmake.htbot.domain.shop.service.ItemSellService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemSellServiceImpl implements ItemSellService {

    private final PlayerRepository playerRepository;
    private final InventoryRepository inventoryRepository;
    private final ArmorRepository armorRepository;
    private final WeaponRepository weaponRepository;
    private final MiscRepository miscRepository;

    @Override
    public void execute(String playerId, ItemSellRequest request) {
        String itemName = request.getName();
        int quantity = request.getQuantity();

        System.out.println("############"+request.getCategory());
        System.out.println("############"+request.getName());
        System.out.println("############"+request.getQuantity());

        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        Inventory existingItem = inventoryRepository.findByPlayerIdAndName(playerId, itemName)
                .orElseThrow(InventoryItemNotFoundException::new);

        if (existingItem.getQuantity() < quantity) {
            throw new NotEnoughItemQuantityException();
        }

        int gold = switch (request.getCategory()) {
            case "armor" -> armorRepository.findByName(itemName).get().getGold() * quantity;
            case "weapon" -> weaponRepository.findByName(itemName).get().getGold() * quantity;
            case "misc" -> miscRepository.findByName(itemName).get().getGold() * quantity;
            default -> 0;
        };

        if (existingItem.getQuantity() == 1) {
            inventoryRepository.delete(existingItem);
        } else {
            existingItem.setQuantity(existingItem.getQuantity() - quantity);
            inventoryRepository.save(existingItem);
        }

        player.setGold(player.getGold() + gold);
        playerRepository.save(player);
    }
}
