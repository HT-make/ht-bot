package com.htmake.htbot.domain.profession.service;

import com.htmake.htbot.domain.profession.presentation.data.JobPromotionInfoResponse;

public interface JobPromotionInfoService {
    JobPromotionInfoResponse execute(String playerId, String jobName);
}
