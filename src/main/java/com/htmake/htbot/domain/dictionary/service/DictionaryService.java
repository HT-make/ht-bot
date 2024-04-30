package com.htmake.htbot.domain.dictionary.service;

import com.htmake.htbot.domain.dictionary.presentation.data.response.DictionaryResponse;

public interface DictionaryService {
    DictionaryResponse execute(String category, String name);
}
