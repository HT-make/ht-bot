package com.htmake.htbot.domain.shop.presentation;

import com.htmake.htbot.domain.shop.presentation.data.request.ItemSellRequest;
import com.htmake.htbot.domain.shop.presentation.data.request.RandomShopPurchaseRequest;
import com.htmake.htbot.domain.shop.presentation.data.response.RandomShopItemListResponse;
import com.htmake.htbot.domain.shop.presentation.data.response.SuccessPurchaseResponse;
import com.htmake.htbot.domain.shop.service.ItemSellService;
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
    private final ItemSellService itemSellService;

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
}
