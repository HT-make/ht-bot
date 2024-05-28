package com.htmake.htbot.domain.profession.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.profession.entity.Profession;
import com.htmake.htbot.domain.profession.presentation.data.JobPromotionInfoListResponse;
import com.htmake.htbot.domain.profession.presentation.data.JobPromotionInfoResponse;
import com.htmake.htbot.domain.profession.repository.ProfessionRepository;
import com.htmake.htbot.domain.profession.service.PlayerJobInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlayerJobInfoServiceImpl implements PlayerJobInfoService {

    private final ProfessionRepository professionRepository;
    private final PlayerRepository playerRepository;

    @Override
    public JobPromotionInfoListResponse execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        List<Profession> nextJobs = professionRepository.findByPreviousJob(player.getJob());

        if (nextJobs.isEmpty()) {
            throw new NotFoundPlayerException();
        }

        return JobPromotionInfoListResponse.builder()
                .jobPromotionInfoList(
                        nextJobs.stream()
                                .map(profession -> JobPromotionInfoResponse.builder()
                                        .nextJob(String.valueOf(profession.getNextJob()))
                                        .nextJobName(profession.getNextJob().getName())
                                        .description(profession.getDescription())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }
}
