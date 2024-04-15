package com.htmake.htbot.domain.shop.presentation;

import com.htmake.htbot.domain.shop.presentation.data.request.RandomShopPurchaseRequest;
import com.htmake.htbot.domain.shop.presentation.data.response.RandomShopItemListResponse;
import com.htmake.htbot.domain.shop.service.RandomShopItemPurchaseService;
import com.htmake.htbot.domain.shop.service.RandomShopItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {
    private final RandomShopItemService randomShopItemService;
    private final RandomShopItemPurchaseService randomShopItemPurchaseService;

    @GetMapping("/random/list")
    public ResponseEntity<RandomShopItemListResponse> randomShopItemListInfo() {
        RandomShopItemListResponse response = randomShopItemService.execute();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/random/purchase/{player_id}")
    public ResponseEntity<Void> randomShopPurchase(
            @PathVariable("player_id") String playerId,
            @RequestBody @Valid RandomShopPurchaseRequest request
    ) {
        randomShopItemPurchaseService.execute(playerId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
