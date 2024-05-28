package com.htmake.htbot.domain.skill.presentation;

import com.htmake.htbot.domain.skill.presentation.data.request.RegisterSkillRequest;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillListResponse;
import com.htmake.htbot.domain.skill.presentation.data.response.RegisteredSkillListResponse;
import com.htmake.htbot.domain.skill.service.SkillListService;
import com.htmake.htbot.domain.skill.service.NotRegisteredSkillListService;
import com.htmake.htbot.domain.skill.service.RegisterSkillService;
import com.htmake.htbot.domain.skill.service.RegisteredSkillListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skill")
public class SkillController {

    private final SkillListService skillListService;
    private final RegisteredSkillListService registeredSkillListService;
    private final RegisterSkillService registerSkillService;
    private final NotRegisteredSkillListService notRegisteredSkillListService;

    @GetMapping("/{player_id}")
    public ResponseEntity<SkillListResponse> availableSkillList(@PathVariable("player_id") String playerId) {
        SkillListResponse response = skillListService.execute(playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/registered/{player_id}")
    public ResponseEntity<RegisteredSkillListResponse> registeredSkillList(@PathVariable("player_id") String playerId) {
        RegisteredSkillListResponse response = registeredSkillListService.execute(playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/not/registered/{player_id}")
    public ResponseEntity<SkillListResponse> notRegisteredSkillList(@PathVariable("player_id") String playerId) {
        SkillListResponse response = notRegisteredSkillListService.execute(playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{player_id}")
    public ResponseEntity<Void> registerSkill(
            @PathVariable("player_id") String playerId,
            @RequestBody @Valid RegisterSkillRequest request
    ) {
        registerSkillService.execute(playerId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
