package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.player.entity.Equipment;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.entity.Status;
import com.htmake.htbot.domain.player.presentation.data.response.PlayerInfoResponse;
import com.htmake.htbot.domain.player.repository.EquipmentRepository;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.repository.StatusRepository;
import com.htmake.htbot.domain.player.service.PlayerInfoService;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import lombok.RequiredArgsConstructor;

@ReadOnlyService
@RequiredArgsConstructor
public class PlayerInfoServiceImpl implements PlayerInfoService {

    private final PlayerRepository playerRepository;
    private final StatusRepository statusRepository;
    private final EquipmentRepository equipmentRepository;

    @Override
    public PlayerInfoResponse execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow();

        Status status = statusRepository.findById(playerId)
                .orElseThrow();

        Equipment equipment = equipmentRepository.findById(playerId)
                .orElseThrow();

        return PlayerInfoResponse.builder()
                .level(player.getLevel())
                .currentExp(player.getCurrentExp())
                .maxExp(player.getMaxExp())
                .job(player.getJob().name())
                .damage(status.getDamage())
                .health(status.getHealth())
                .defence(status.getDefence())
                .mana(status.getMana())
                .criticalChance(status.getCriticalChance())
                .criticalDamage(status.getCriticalDamage())
                .weaponName(equipment.getWeaponName())
                .armorName(equipment.getArmorName())
                .build();
    }
}
