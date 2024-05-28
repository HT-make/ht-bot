package com.htmake.htbot.domain.profession.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.misc.entity.Misc;
import com.htmake.htbot.domain.misc.exception.MiscNotFoundException;
import com.htmake.htbot.domain.misc.repository.MiscRepository;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.profession.entity.Profession;
import com.htmake.htbot.domain.profession.exception.NotFoundJobException;
import com.htmake.htbot.domain.profession.presentation.data.JobPromotionInfoResponse;
import com.htmake.htbot.domain.profession.repository.ProfessionRepository;
import com.htmake.htbot.domain.profession.service.JobPromotionInfoService;
import com.htmake.htbot.domain.shop.exception.NotFoundItemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class JobPromotionInfoServiceImpl implements JobPromotionInfoService {

    private final PlayerRepository playerRepository;
    private final ProfessionRepository professionRepository;
    private final MiscRepository miscRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public JobPromotionInfoResponse execute(String playerId, String jobName) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        Job nextJob = null;
        Job currentJob = player.getJob();

        if (jobName.equals("first")){
            if (currentJob == Job.WARRIOR) nextJob = Job.SKILLED_WARRIOR;
            else if (currentJob == Job.ARCHER) nextJob = Job.SKILLED_ARCHER;
            else if (currentJob == Job.WIZARD) nextJob = Job.SKILLED_WIZARD;
        } else {
            nextJob = Job.valueOf(jobName);
        }

        Profession profession = professionRepository.findByNextJob(nextJob)
                .orElseThrow(NotFoundJobException::new);

        Misc misc = miscRepository.findByName(profession.getItemName())
                .orElseThrow(MiscNotFoundException::new);

        Inventory inventory = inventoryRepository.findByPlayerIdAndItemId(playerId, misc.getId())
                .orElseThrow(NotFoundItemException::new);

        return JobPromotionInfoResponse.builder()
                .nextJob(String.valueOf(nextJob))
                .nextJobName(nextJob.getName())
                .level(profession.getLevel())
                .gem(profession.getGem())
                .gold(profession.getGold())
                .itemName(misc.getName())
                .itemQuantity(profession.getItemQuantity())
                .requiredLevel(player.getLevel())
                .requiredGold(player.getGold())
                .requiredGem(player.getGem())
                .requiredItemQuantity(inventory.getQuantity())
                .build();
    }
}
