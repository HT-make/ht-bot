package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.equipment.entity.Armor;
import com.htmake.htbot.domain.equipment.entity.Weapon;
import com.htmake.htbot.domain.equipment.exception.EquipmentNotFoundException;
import com.htmake.htbot.domain.equipment.exception.EquipmentTypeMismatchException;
import com.htmake.htbot.domain.equipment.repository.ArmorRepository;
import com.htmake.htbot.domain.equipment.repository.WeaponRepository;
import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.exception.InventoryIsMaxException;
import com.htmake.htbot.domain.inventory.exception.InventoryItemNotFoundException;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.player.entity.Equipment;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.entity.Status;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.presentation.data.request.EquipmentEquipRequest;
import com.htmake.htbot.domain.player.repository.EquipmentRepository;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.repository.StatusRepository;
import com.htmake.htbot.domain.player.service.EquipmentEquipService;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

@TransactionalService
@RequiredArgsConstructor
public class EquipmentEquipServiceImpl implements EquipmentEquipService {

    private final PlayerRepository playerRepository;
    private final EquipmentRepository equipmentRepository;
    private final StatusRepository statusRepository;
    private final InventoryRepository inventoryRepository;
    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;

    @Override
    public void execute(String playerId, EquipmentEquipRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        String name = request.getName();

        Inventory item = inventoryRepository.findByPlayerIdAndName(playerId, name)
                .orElse(null);

        validateInventory(playerId, item);

        Equipment equipment = equipmentRepository.findById(playerId)
                .orElseThrow(EquipmentNotFoundException::new);

        equipItem(player, equipment, name, item);
    }

    private void validateInventory(String playerId, Inventory item) {
        if (item == null) {
            throw new InventoryItemNotFoundException();
        }

        Long itemCount = inventoryRepository.countByPlayerId(playerId);

        if (itemCount == 45 && item.getQuantity() >= 2) {
            throw new InventoryIsMaxException();
        }
    }

    private void equipItem(Player player, Equipment equipment, String name, Inventory item) {
        if (weaponRepository.existsByName(name)) {
            equipWeapon(player, equipment, name);
        } else if (armorRepository.existsByName(name)) {
            equipArmor(player, equipment, name);
        } else {
            throw new EquipmentNotFoundException();
        }

        if (item.getQuantity() == 1) {
            inventoryRepository.delete(item);
        } else {
            item.setQuantity(item.getQuantity() - 1);
            inventoryRepository.save(item);
        }
    }

    private void equipWeapon(Player player, Equipment equipment, String name) {
        Weapon weapon = weaponRepository.findByName(name)
                .orElseThrow(EquipmentNotFoundException::new);

        if (!weapon.getWeaponType().getJob().equals(player.getJob())) {
            throw new EquipmentTypeMismatchException();
        }

        Weapon currentWeapon = weaponRepository.findById(equipment.getWeaponId())
                .orElseThrow(EquipmentNotFoundException::new);

        Status status = statusRepository.findById(player.getId())
                .orElseThrow(NotFoundPlayerException::new);

        status.updateStatus(
                status.getDamage() + weapon.getDamage() - currentWeapon.getDamage(),
                status.getHealth() + weapon.getHealth() - currentWeapon.getHealth(),
                status.getDefence() + weapon.getDefence() - currentWeapon.getDefence(),
                status.getMana() + weapon.getMana() - currentWeapon.getMana(),
                status.getCriticalChance() + weapon.getCriticalChance() - currentWeapon.getCriticalChance(),
                status.getCriticalDamage() + weapon.getCriticalDamage() - currentWeapon.getCriticalDamage()
        );

        statusRepository.save(status);

        equipment.updateWeapon(weapon.getId(), weapon.getName());
        equipmentRepository.save(equipment);

        updateInventory(player, currentWeapon.getId(), currentWeapon.getName());
    }

    private void equipArmor(Player player, Equipment equipment, String name) {
        Armor armor = armorRepository.findByName(name)
                .orElseThrow(EquipmentNotFoundException::new);

        Armor currentArmor = armorRepository.findById(equipment.getArmorId())
                .orElseThrow(EquipmentNotFoundException::new);

        Status status = statusRepository.findById(player.getId())
                .orElseThrow(NotFoundPlayerException::new);

        status.updateStatus(
                status.getDamage(),
                status.getHealth() + armor.getHealth() - currentArmor.getHealth(),
                status.getDefence() + armor.getDefence() - currentArmor.getDefence(),
                status.getMana(),
                status.getCriticalChance(),
                status.getCriticalDamage()
        );

        statusRepository.save(status);

        equipment.updateArmor(armor.getId(), armor.getName());
        equipmentRepository.save(equipment);

        updateInventory(player, currentArmor.getId(), currentArmor.getName());
    }

    private void updateInventory(Player player, String id, String name) {
        Inventory existsItem = inventoryRepository.findByPlayerIdAndName(player.getId(), name)
                .orElse(null);

        if (existsItem != null) {
            existsItem.setQuantity(existsItem.getQuantity() + 1);
            inventoryRepository.save(existsItem);
        } else {
            Inventory item = Inventory.builder()
                    .player(player)
                    .itemId(id)
                    .name(name)
                    .quantity(1)
                    .build();

            inventoryRepository.save(item);
        }
    }
}
