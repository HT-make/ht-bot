package com.htmake.htbot.domain.shop.service.impl;

import com.htmake.htbot.domain.equipment.entity.Armor;
import com.htmake.htbot.domain.equipment.entity.Weapon;
import com.htmake.htbot.domain.equipment.repository.ArmorRepository;
import com.htmake.htbot.domain.equipment.repository.WeaponRepository;
import com.htmake.htbot.domain.shop.entity.RandomShop;
import com.htmake.htbot.domain.shop.repository.RandomShopRepository;
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

    @Override
    public void execute() {
        randomShopRepository.deleteAll();

        List<Weapon> weaponList = weaponRepository.findAll();
        List<Armor> armorList = armorRepository.findAll();

        List<RandomShop> randomShopList = new ArrayList<>();

        List<Weapon> randomWeapons = getRandomItemsWithoutDuplicates(weaponList);
        List<Armor> randomArmors = getRandomItemsWithoutDuplicates(armorList);

        for (int i = 0; i < 3; i++) {
            Weapon randomWeapon = randomWeapons.get(i);
            Armor randomArmor = randomArmors.get(i);

            int weaponQuantity = getQuantity(randomWeapon.getLevel());
            int armorQuantity = getQuantity(randomArmor.getLevel());

            RandomShop randomShopWeapon = RandomShop.builder()
                    .id(randomWeapon.getId())
                    .name(randomWeapon.getName())
                    .gold(randomWeapon.getGold())
                    .quantity(weaponQuantity)
                    .build();

            RandomShop randomShopItemArmor = RandomShop.builder()
                    .id(randomArmor.getId())
                    .name(randomArmor.getName())
                    .gold(randomArmor.getGold())
                    .quantity(armorQuantity)
                    .build();

            randomShopList.add(randomShopWeapon);
            randomShopList.add(randomShopItemArmor);
        }

        randomShopRepository.saveAll(randomShopList);
    }

    private <T> List<T> getRandomItemsWithoutDuplicates(List<T> itemList) {
        List<T> itemsCopy = new ArrayList<>(itemList);
        List<T> randomItems = new ArrayList<>();

        RandomGenerator random = new MersenneTwister();
        random.setSeed(System.currentTimeMillis() ^ System.nanoTime());

        while (randomItems.size() < 3 && !itemsCopy.isEmpty()) {
            int randomIndex = random.nextInt(itemsCopy.size());
            T selectedItem = itemsCopy.get(randomIndex);

            if (!randomItems.contains(selectedItem)) {
                randomItems.add(selectedItem);
            }
            itemsCopy.remove(randomIndex);
        }

        return randomItems;
    }

    private int getQuantity(int level) {
        int quantity = 0;

        if (level <= 20) {
            quantity = 500;
        } else if (level <= 40) {
            quantity = 100;
        } else if (level <= 60) {
            quantity = 50;
        } else if (level <= 80) {
            quantity = 10;
        } else if (level <= 100) {
            quantity = 3;
        }

        return quantity;
    }
}
