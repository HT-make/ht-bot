package com.htmake.htbot.domain.player.service;

import com.htmake.htbot.domain.player.presentation.data.request.EquipmentEquipRequest;

public interface EquipmentEquipService {

    void execute(String playerId, EquipmentEquipRequest request);
}
