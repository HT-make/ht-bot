package com.htmake.htbot.domain.profession.presentation.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPromotionInfoListResponse {
    private List<JobPromotionInfoResponse> jobPromotionInfoList;
}
