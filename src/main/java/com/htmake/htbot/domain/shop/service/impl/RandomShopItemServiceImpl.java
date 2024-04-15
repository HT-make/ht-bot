package com.htmake.htbot.domain.shop.service.impl;

import com.htmake.htbot.domain.shop.entity.RandomShop;
import com.htmake.htbot.domain.shop.exception.NotFoundRandomShopException;
import com.htmake.htbot.domain.shop.presentation.data.response.RandomShopItemListResponse;
import com.htmake.htbot.domain.shop.presentation.data.response.RandomShopItemResponse;
import com.htmake.htbot.domain.shop.repository.RandomShopRepository;
import com.htmake.htbot.domain.shop.service.RandomShopItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RandomShopItemServiceImpl implements RandomShopItemService {

    private final RandomShopRepository randomShopRepository;

    @Override
    public RandomShopItemListResponse execute() {
        List<RandomShop> randomShopList = randomShopRepository.findAll();

        if (randomShopList.isEmpty()) {
            throw new NotFoundRandomShopException();
        }

        return RandomShopItemListResponse.builder()
                .itemList(
                        randomShopList.stream()
                                .map(RandomShopItemResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
