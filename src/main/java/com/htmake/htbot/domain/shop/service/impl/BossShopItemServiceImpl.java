package com.htmake.htbot.domain.shop.service.impl;

import com.htmake.htbot.domain.shop.entity.BossShop;
import com.htmake.htbot.domain.shop.exception.NotFoundBossShopException;
import com.htmake.htbot.domain.shop.presentation.data.response.BossShopItemListResponse;
import com.htmake.htbot.domain.shop.presentation.data.response.BossShopItemResponse;
import com.htmake.htbot.domain.shop.repository.BossShopRepository;
import com.htmake.htbot.domain.shop.service.BossShopItemService;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@ReadOnlyService
@RequiredArgsConstructor
public class BossShopItemServiceImpl implements BossShopItemService {

    private final BossShopRepository bossShopRepository;

    @Override
    public BossShopItemListResponse execute() {
        List<BossShop> bossShopList = bossShopRepository.findAll();

        if (bossShopList.isEmpty()) {
            throw new NotFoundBossShopException();
        }

        return BossShopItemListResponse.builder()
                .itemList(
                        bossShopList.stream()
                                .map(BossShopItemResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
