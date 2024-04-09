package com.htmake.htbot.domain.shop.service.impl;

import com.htmake.htbot.domain.equipment.entity.Armor;
import com.htmake.htbot.domain.equipment.entity.Weapon;
import com.htmake.htbot.domain.equipment.repository.ArmorRepository;
import com.htmake.htbot.domain.equipment.repository.WeaponRepository;
import com.htmake.htbot.domain.shop.entity.RandomShop;
import com.htmake.htbot.domain.shop.entity.RandomShopArmor;
import com.htmake.htbot.domain.shop.entity.RandomShopWeapon;
import com.htmake.htbot.domain.shop.repository.RandomShopArmorRepository;
import com.htmake.htbot.domain.shop.repository.RandomShopRepository;
import com.htmake.htbot.domain.shop.repository.RandomShopWeaponRepository;
import com.htmake.htbot.domain.shop.service.RandomShopItemShuffleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RandomShopItemShuffleServiceImpl implements RandomShopItemShuffleService {
    private final RandomShopRepository randomShopRepository;
    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;

    private final RandomShopArmorRepository randomShopArmorRepository;

    private final RandomShopWeaponRepository randomShopWeaponRepository;


    @Override
    public void execute() {
        randomShopRepository.deleteAll();

        RandomShop randomShop = RandomShop.builder().build();
        randomShopRepository.save(randomShop);

        List<Weapon> weapons = weaponRepository.findAll();
        List<Armor> armors = armorRepository.findAll();

        RandomGenerator random = new MersenneTwister();

        int weaponMax = weapons.size();
        int armorMax = armors.size();

        List<RandomShopWeapon> randomShopWeapons = new ArrayList<>();
        List<RandomShopArmor> randomShopArmors = new ArrayList<>();

        for (int i=0; i<3; i++){
            int weaponRandom = random.nextInt(weaponMax);
            int armorRandom = random.nextInt(armorMax);
            int weaponQuantityRandom = random.nextInt(10) + 1;
            int armorQuantityRandom = random.nextInt(10) + 1;

            Weapon randomWeapon = weapons.get(weaponRandom);
            Armor randomArmor = armors.get(armorRandom);

            randomShopWeapons.add(RandomShopWeapon.builder()
                    .id(randomWeapon.getId())
                    .name(randomWeapon.getName())
                    .gold(randomWeapon.getGold())
                    .quantity(weaponQuantityRandom)
                    .randomShop(randomShop)
                    .build());

            randomShopArmors.add(RandomShopArmor.builder()
                    .id(randomArmor.getId())
                    .name(randomArmor.getName())
                    .gold(randomArmor.getGold())
                    .quantity(armorQuantityRandom)
                    .randomShop(randomShop)
                    .build());

        }

        randomShopWeaponRepository.saveAll(randomShopWeapons);
        randomShopArmorRepository.saveAll(randomShopArmors);
    }
}
