package com.htmake.htbot.domain.dictionary.service.impl;

import com.htmake.htbot.domain.dictionary.exception.NotFoundCategoryException;
import com.htmake.htbot.domain.dictionary.exception.NotFoundNameException;
import com.htmake.htbot.domain.dictionary.presentation.data.response.DictionaryResponse;
import com.htmake.htbot.domain.dictionary.service.DictionaryService;
import com.htmake.htbot.domain.equipment.entity.Armor;
import com.htmake.htbot.domain.equipment.entity.Weapon;
import com.htmake.htbot.domain.equipment.repository.ArmorRepository;
import com.htmake.htbot.domain.equipment.repository.WeaponRepository;
import com.htmake.htbot.domain.misc.entity.Misc;
import com.htmake.htbot.domain.misc.repository.MiscRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {

    private final ArmorRepository armorRepository;
    private final WeaponRepository weaponRepository;
    private final MiscRepository miscRepository;

    @Override
    public DictionaryResponse execute(String category, String name) {
        String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);

        return switch (category) {
            case "weapon" -> createWeaponResponse(decodedName);
            case "armor" -> createArmorResponse(decodedName);
            case "misc" -> createMiscResponse(decodedName);
            default -> throw new NotFoundCategoryException();
        };
    }

    private DictionaryResponse createWeaponResponse(String name) {
        Weapon weapon = weaponRepository.findByName(name).orElseThrow(NotFoundNameException::new);
        return DictionaryResponse.builder()
                .name(weapon.getName())
                .level(weapon.getLevel())
                .damage(weapon.getDamage())
                .health(weapon.getHealth())
                .defence(weapon.getDefence())
                .criticalChance(weapon.getCriticalChance())
                .criticalDamage(weapon.getCriticalDamage())
                .mana(weapon.getMana())
                .gold(weapon.getGold())
                .build();
    }

    private DictionaryResponse createArmorResponse(String name) {
        Armor armor = armorRepository.findByName(name).orElseThrow(NotFoundNameException::new);
        return DictionaryResponse.builder()
                .name(armor.getName())
                .level(armor.getLevel())
                .health(armor.getHealth())
                .defence(armor.getDefence())
                .gold(armor.getGold())
                .build();
    }

    private DictionaryResponse createMiscResponse(String name) {
        Misc misc = miscRepository.findByName(name).orElseThrow(NotFoundNameException::new);
        return DictionaryResponse.builder()
                .name(misc.getName())
                .gold(misc.getGold())
                .build();
    }
}
