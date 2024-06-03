package com.htmake.htbot.domain.shop.presentation;

import com.htmake.htbot.domain.shop.presentation.data.request.ItemSellRequest;
import com.htmake.htbot.domain.shop.presentation.data.request.RandomShopPurchaseRequest;
import com.htmake.htbot.domain.shop.presentation.data.response.BossShopItemListResponse;
import com.htmake.htbot.domain.shop.presentation.data.response.RandomShopItemListResponse;
import com.htmake.htbot.domain.shop.presentation.data.response.SuccessPurchaseResponse;
import com.htmake.htbot.domain.shop.service.*;
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
    private final ItemSellService itemSellService;
    private final BossShopItemService bossShopItemService;
    private final BossShopItemPurchaseService bossShopItemPurchaseService;

    @GetMapping("/random/list")
    public ResponseEntity<RandomShopItemListResponse> randomShopItemListInfo() {
        RandomShopItemListResponse response = randomShopItemService.execute();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/random/purchase/{player_id}")
    public ResponseEntity<SuccessPurchaseResponse> randomShopPurchase(
            @PathVariable("player_id") String playerId,
            @RequestBody @Valid RandomShopPurchaseRequest request
    ) {
        SuccessPurchaseResponse response = randomShopItemPurchaseService.execute(playerId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/sell/{player_id}")
    public ResponseEntity<Void> sellItem(
            @PathVariable("player_id") String playerId,
            @RequestBody @Valid ItemSellRequest request
    ) {
        itemSellService.execute(playerId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/boss/list")
    public ResponseEntity<BossShopItemListResponse> bossShopItemListInfo() {
        BossShopItemListResponse response = bossShopItemService.execute();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/boss/purchase/{player_id}/{item_id}")
    public ResponseEntity<SuccessPurchaseResponse> bossShopPurchase(
            @PathVariable("player_id") String playerId,
            @PathVariable("item_id") String itemId
    ) {
        bossShopItemPurchaseService.execute(playerId, itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
