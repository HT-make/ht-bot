package com.htmake.htbot.domain.player.service;

import com.htmake.htbot.domain.player.presentation.data.request.PlayerJoinRequest;
import com.htmake.htbot.domain.player.presentation.data.response.PlayerJoinResponse;

public interface PlayerJoinService {

    PlayerJoinResponse execute(PlayerJoinRequest request);
}
