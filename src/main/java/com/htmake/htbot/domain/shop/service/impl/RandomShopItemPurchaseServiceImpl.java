package com.htmake.htbot.domain.shop.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.shop.entity.RandomShop;
import com.htmake.htbot.domain.shop.entity.RandomShopArmor;
import com.htmake.htbot.domain.shop.entity.RandomShopWeapon;
import com.htmake.htbot.domain.shop.exception.NotEnoughGoldException;
import com.htmake.htbot.domain.shop.exception.NotEnoughQuantityException;
import com.htmake.htbot.domain.shop.exception.NotFoundItemException;
import com.htmake.htbot.domain.shop.presentation.data.request.RandomShopPurchaseRequest;
import com.htmake.htbot.domain.shop.presentation.data.temp.RandomShopItemTemp;
import com.htmake.htbot.domain.shop.repository.RandomShopArmorRepository;
import com.htmake.htbot.domain.shop.repository.RandomShopRepository;
import com.htmake.htbot.domain.shop.repository.RandomShopWeaponRepository;
import com.htmake.htbot.domain.shop.service.RandomShopItemPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RandomShopItemPurchaseServiceImpl implements RandomShopItemPurchaseService {

    private final RandomShopRepository randomShopRepository;
    private final InventoryRepository inventoryRepository;
    private final PlayerRepository playerRepository;
    private final RandomShopWeaponRepository randomShopWeaponRepository;
    private final RandomShopArmorRepository randomShopArmorRepository;

    @Override
    public void execute(String playerId, RandomShopPurchaseRequest request) {
        RandomShop randomShop = randomShopRepository.findAll().get(0);
        RandomShopItemTemp item = toTemp(randomShop, request.getName());

        Player player = playerRepository.findById(playerId)
                .orElseThrow();

        int playerGold = player.getGold();

        if (playerGold < item.getGold()) {
            throw new NotEnoughGoldException();
        }

        player.setGold(playerGold - item.getGold());
        playerRepository.save(player);

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
    }

    private RandomShopItemTemp toTemp(RandomShop randomShop, String name) {
        List<RandomShopWeapon> weaponList = randomShop.getRandomShopWeapons();
        RandomShopWeapon weapon = weaponList.stream()
                .filter(w -> w.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (weapon != null) {
            if (weapon.getQuantity() <= 0) {
                throw new NotEnoughQuantityException();
            }
            weapon.setQuantity(weapon.getQuantity() - 1);
            randomShopWeaponRepository.save(weapon);

            return RandomShopItemTemp.builder()
                    .id(weapon.getId())
                    .name(weapon.getName())
                    .gold(weapon.getGold())
                    .quantity(weapon.getQuantity())
                    .build();
        }

        List<RandomShopArmor> armorList = randomShop.getRandomShopArmors();
        RandomShopArmor armor = armorList.stream()
                .filter(a -> a.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (armor != null) {
            if (armor.getQuantity() <= 0) {
                throw new NotEnoughQuantityException();
            }
            armor.setQuantity(armor.getQuantity() - 1);
            randomShopArmorRepository.save(armor);

            return RandomShopItemTemp.builder()
                    .id(armor.getId())
                    .name(armor.getName())
                    .gold(armor.getGold())
                    .quantity(armor.getQuantity())
                    .build();
        }

        throw new NotFoundItemException();
    }
}
