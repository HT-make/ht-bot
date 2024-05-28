package com.htmake.htbot.domain.profession.service;

import com.htmake.htbot.domain.profession.presentation.data.JobPromotionInfoListResponse;

public interface PlayerJobInfoService {
    JobPromotionInfoListResponse execute(String playerId);
}
