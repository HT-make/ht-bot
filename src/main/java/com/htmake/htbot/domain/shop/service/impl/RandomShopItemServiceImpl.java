package com.htmake.htbot.domain.shop.service.impl;

import com.htmake.htbot.domain.shop.entity.RandomShop;
import com.htmake.htbot.domain.shop.entity.RandomShopArmor;
import com.htmake.htbot.domain.shop.entity.RandomShopWeapon;
import com.htmake.htbot.domain.shop.presentation.data.response.RandomShopArmorResponse;
import com.htmake.htbot.domain.shop.presentation.data.response.RandomShopItemListResponse;
import com.htmake.htbot.domain.shop.presentation.data.response.RandomShopWeaponResponse;
import com.htmake.htbot.domain.shop.repository.RandomShopRepository;
import com.htmake.htbot.domain.shop.service.RandomShopItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RandomShopItemServiceImpl implements RandomShopItemService {

    private final RandomShopRepository randomShopRepository;

    @Override
    public RandomShopItemListResponse execute() {
        RandomShop randomShop = randomShopRepository.findAll().get(0);

        List<RandomShopArmor> randomShopArmorList = randomShop.getRandomShopArmors();
        List<RandomShopWeapon> randomShopWeaponList = randomShop.getRandomShopWeapons();

        return RandomShopItemListResponse.builder()
                .randomShopArmorList(
                        randomShopArmorList.stream()
                                .map(RandomShopArmorResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .randomShopWeaponList(
                        randomShopWeaponList.stream()
                                .map(RandomShopWeaponResponse::toResponse)
                                .collect(Collectors.toList())
                ).build();
    }
}
