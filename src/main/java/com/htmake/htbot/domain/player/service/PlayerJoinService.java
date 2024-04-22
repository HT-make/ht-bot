package com.htmake.htbot.domain.player.service;

import com.htmake.htbot.domain.player.presentation.data.request.PlayerJoinRequest;

public interface PlayerJoinService {

    void execute(PlayerJoinRequest request);
}
