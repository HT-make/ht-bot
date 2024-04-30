package com.htmake.htbot.domain.dictionary.presentation;

import com.htmake.htbot.domain.dictionary.presentation.data.response.DictionaryResponse;
import com.htmake.htbot.domain.dictionary.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @GetMapping("/info")
    public ResponseEntity<DictionaryResponse> getDictionaryInfo(
            @RequestParam("category") String category,
            @RequestParam("name") String name
    ) {
        DictionaryResponse res = dictionaryService.execute(category, name);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
