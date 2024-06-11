package com.htmake.htbot.domain.inventory.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.presentation.data.response.InventoryInfoListResponse;
import com.htmake.htbot.domain.inventory.presentation.data.response.InventoryInfoResponse;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.inventory.service.InventoryInfoService;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@ReadOnlyService
@RequiredArgsConstructor
public class InventoryInfoServiceImpl implements InventoryInfoService {

    private final InventoryRepository inventoryRepository;
    private final PlayerRepository playerRepository;

    @Override
    public InventoryInfoListResponse execute(String playerId) {
        List<Inventory> inventoryList = inventoryRepository.findByPlayerId(playerId);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        return InventoryInfoListResponse.builder()
                .gold(player.getGold())
                .gem(player.getGem())
                .inventoryList(
                        inventoryList.stream()
                                .map(InventoryInfoResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
