package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.equipment.entity.Armor;
import com.htmake.htbot.domain.equipment.entity.Weapon;
import com.htmake.htbot.domain.equipment.repository.ArmorRepository;
import com.htmake.htbot.domain.equipment.repository.WeaponRepository;
import com.htmake.htbot.domain.player.entity.Equipment;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.enums.BasicStatus;
import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.player.presentation.data.request.PlayerJoinRequest;
import com.htmake.htbot.domain.player.presentation.data.response.PlayerJoinResponse;
import com.htmake.htbot.domain.player.repository.EquipmentRepository;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.PlayerJoinService;
import com.htmake.htbot.domain.player.entity.Status;
import com.htmake.htbot.domain.player.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PlayerJoinServiceImpl implements PlayerJoinService {

    private final PlayerRepository playerRepository;
    private final StatusRepository statusRepository;
    private final EquipmentRepository equipmentRepository;
    private final WeaponRepository weaponRepository;
    private final ArmorRepository armorRepository;

    @Override
    public PlayerJoinResponse execute(PlayerJoinRequest request) {

        String message;

        if (playerRepository.existsById(request.getUserId())) {
            message = "이미 가입한 유저입니다.";
        } else {
            newPlayer(request);
            message = "가입에 성공했습니다!";
        }

        return PlayerJoinResponse.builder()
                .message(message)
                .build();
    }

    private void newPlayer(PlayerJoinRequest request) {
        Job job = Job.toEnum(request.getJob());

        Player player = Player.builder()
                .id(request.getUserId())
                .name(request.getName())
                .level(1)
                .currentExp(0)
                .maxExp(100)
                .gold(0)
                .gem(0)
                .job(job)
                .build();

        playerRepository.save(player);

        String weaponId = getWeaponId(job);
        String armorId = "11001";

        Weapon weapon = weaponRepository.findById(weaponId).orElseThrow();
        Armor armor = armorRepository.findById(armorId).orElseThrow();

        Equipment equipment = Equipment.builder()
                .id(player.getId())
                .weaponId(weapon.getId())
                .weaponName(weapon.getName())
                .armorId(armor.getId())
                .armorName(armor.getName())
                .build();

        equipmentRepository.save(equipment);

        Status status = Status.builder()
                .id(player.getId())
                .damage(weapon.getDamage())
                .health(weapon.getHealth() + armor.getHealth())
                .defence(weapon.getDefence() + armor.getDefence())
                .mana(BasicStatus.MANA.getValue() + weapon.getMana())
                .criticalChance(BasicStatus.CRITICAL_CHANCE.getValue() + weapon.getCriticalChance())
                .criticalDamage(BasicStatus.CRITICAL_DAMAGE.getValue() + weapon.getCriticalDamage())
                .build();

        statusRepository.save(status);
    }

    private String getWeaponId(Job job) {
        return switch (job) {
            case WARRIOR -> "21001";
            case ARCHER -> "22001";
            case WIZARD -> "23001";
        };
    }
}
