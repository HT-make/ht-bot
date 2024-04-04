package com.htmake.htbot.domain.inventory.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.presentation.data.response.InventoryInfoListResponse;
import com.htmake.htbot.domain.inventory.presentation.data.response.InventoryInfoResponse;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.inventory.service.InventoryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InventoryInfoServiceImpl implements InventoryInfoService {

    private final InventoryRepository inventoryRepository;

    @Override
    public InventoryInfoListResponse execute(String playerId) {
        List<Inventory> inventory = inventoryRepository.findByPlayerId(playerId);

        return InventoryInfoListResponse.builder()
                .inventoryList(
                        inventory.stream()
                                .map(InventoryInfoResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
