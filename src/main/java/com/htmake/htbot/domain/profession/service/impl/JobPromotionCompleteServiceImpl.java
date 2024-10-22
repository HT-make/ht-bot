package com.htmake.htbot.domain.profession.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.entity.Status;
import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.repository.StatusRepository;
import com.htmake.htbot.domain.profession.entity.Profession;
import com.htmake.htbot.domain.profession.exception.NotFoundJobException;
import com.htmake.htbot.domain.profession.repository.ProfessionRepository;
import com.htmake.htbot.domain.profession.service.JobPromotionCompleteService;
import com.htmake.htbot.domain.shop.exception.NotEnoughQuantityException;
import com.htmake.htbot.domain.shop.exception.NotFoundItemException;
import com.htmake.htbot.domain.skill.repository.PlayerSkillRepository;
import com.htmake.htbot.global.annotation.TransactionalService;
import com.htmake.htbot.global.util.SkillUtil;
import lombok.RequiredArgsConstructor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@TransactionalService
@RequiredArgsConstructor
public class JobPromotionCompleteServiceImpl implements JobPromotionCompleteService {

    private final ProfessionRepository professionRepository;
    private final PlayerRepository playerRepository;
    private final InventoryRepository inventoryRepository;
    private final PlayerSkillRepository playerSkillRepository;
    private final StatusRepository statusRepository;

    private final SkillUtil skillUtil;

    @Override
    public void execute(String playerId, String jobName) {
        String decodedJob = URLDecoder.decode(jobName, StandardCharsets.UTF_8);
        Job job = Job.valueOf(decodedJob);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        Profession profession = professionRepository.findByNextJob(Job.valueOf(decodedJob))
                .orElseThrow(NotFoundJobException::new);

        Inventory existingItem = inventoryRepository.findByPlayerIdAndName(playerId, profession.getItemName())
                .orElseThrow(NotFoundItemException::new);

        if (player.getLevel() < profession.getLevel()) {
            throw new NotEnoughQuantityException();
        }

        if (player.getGold() < profession.getGold() || player.getGem() < profession.getGem()) {
            throw new NotEnoughQuantityException();
        }

        if (existingItem.getQuantity() < profession.getItemQuantity()) {
            throw new NotEnoughQuantityException();
        }

        if (existingItem.getQuantity() == 1) {
            inventoryRepository.delete(existingItem);
        } else {
            existingItem.setQuantity(existingItem.getQuantity() - profession.getItemQuantity());
            inventoryRepository.save(existingItem);
        }

        player.setGold(player.getGold() - profession.getGold());
        player.setGem(player.getGem() - profession.getGem());
        player.setJob(job);

        Status status = statusRepository.findById(playerId)
                        .orElseThrow(NotFoundPlayerException::new);
        status.addCriticalChance(20);

        playerRepository.save(player);
        statusRepository.save(status);
        playerSkillRepository.saveAll(skillUtil.buildPlayerSkillList(player, job));
    }
}
