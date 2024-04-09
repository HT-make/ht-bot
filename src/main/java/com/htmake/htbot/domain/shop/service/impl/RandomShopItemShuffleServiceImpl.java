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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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


        Random random = new Random();

        int min = 0;
        int weaponMax = weapons.size();
        int armorMax = armors.size();

        List<RandomShopWeapon> randomShopWeapons = new ArrayList<>();
        List<RandomShopArmor> randomShopArmors = new ArrayList<>();

        for (int i=0; i<3; i++){
            int weaponRandom = random.nextInt(min, weaponMax);
            int armorRandom = random.nextInt(min, armorMax);
            int weaponQuantityRandom = random.nextInt(1, 10);
            int armorQuantityRandom = random.nextInt(1, 10);

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
/*        System.out.println(randomShopWeapons.get(0).getName());
        System.out.println(randomShopWeapons.get(1).getName());
        System.out.println(randomShopWeapons.get(2).getName());
        System.out.println(randomShopArmors.get(0).getName());
        System.out.println(randomShopArmors.get(1).getName());
        System.out.println(randomShopArmors.get(2).getName());*/

        randomShopWeaponRepository.saveAll(randomShopWeapons);
        randomShopArmorRepository.saveAll(randomShopArmors);
    }
}
