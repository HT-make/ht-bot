package com.htmake.htbot.domain.skill.presentation;

import com.htmake.htbot.domain.skill.presentation.data.request.RegisterSkillRequest;
import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillListResponse;
import com.htmake.htbot.domain.skill.service.AvailableSkillListService;
import com.htmake.htbot.domain.skill.service.RegisterSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skill")
public class SkillController {

    private final AvailableSkillListService availableSkillListService;
    private final RegisterSkillService registerSkillService;

    @GetMapping("/{player_id}")
    public ResponseEntity<AvailableSkillListResponse> availableSkillList(@PathVariable("player_id") String playerId) {
        AvailableSkillListResponse response = availableSkillListService.execute(playerId);
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
