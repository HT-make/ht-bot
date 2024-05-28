package com.htmake.htbot.domain.profession.presentation;

import com.htmake.htbot.domain.profession.presentation.data.JobPromotionInfoListResponse;
import com.htmake.htbot.domain.profession.presentation.data.JobPromotionInfoResponse;
import com.htmake.htbot.domain.profession.service.JobPromotionInfoService;
import com.htmake.htbot.domain.profession.service.JobPromotionCompleteService;
import com.htmake.htbot.domain.profession.service.PlayerJobInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profession")
public class ProfessionController {

    private final JobPromotionInfoService jobPromotionInfoService;
    private final JobPromotionCompleteService jobPromotionCompleteService;
    private final PlayerJobInfoService playerJobInfoService;

    @GetMapping("/promotion/{player_id}/{job_name}")
    public ResponseEntity<JobPromotionInfoResponse> getJobInfo(@PathVariable("player_id") String playerId, @PathVariable("job_name") String jobName) {
        JobPromotionInfoResponse response = jobPromotionInfoService.execute(playerId, jobName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/job/{player_id}")
    public ResponseEntity<JobPromotionInfoListResponse> checkPlayerJob(@PathVariable("player_id") String playerId) {
        JobPromotionInfoListResponse response = playerJobInfoService.execute(playerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/promotion/{player_id}/{job_name}")
    public ResponseEntity<Void> promotionPlayerJob(@PathVariable("player_id") String playerId,
                                              @PathVariable("job_name") String jobName) {
        jobPromotionCompleteService.execute(playerId, jobName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
