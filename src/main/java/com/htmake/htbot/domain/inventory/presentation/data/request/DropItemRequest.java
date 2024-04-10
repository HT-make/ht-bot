package com.htmake.htbot.domain.inventory.presentation.data.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DropItemRequest {

    private String id;

    private String name;
}
