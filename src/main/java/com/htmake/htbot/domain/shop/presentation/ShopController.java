package com.htmake.htbot.domain.shop.presentation;

import com.htmake.htbot.domain.shop.presentation.data.response.RandomShopItemListResponse;
import com.htmake.htbot.domain.shop.service.RandomShopItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {
    private final RandomShopItemService randomShopItemService;

    @GetMapping("/random/list")
    public ResponseEntity<RandomShopItemListResponse> randomShopItemListInfo() {
        RandomShopItemListResponse response = randomShopItemService.execute();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
