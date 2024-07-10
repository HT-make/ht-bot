package com.htmake.htbot.domain.monster.presentation.data.response;

import com.htmake.htbot.domain.monster.entity.DropItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DropItemResponse {

    private String id;

    private String name;

    private int chance;

    public static DropItemResponse toResponse(DropItem dropItem) {
        return DropItemResponse.builder()
                .id(dropItem.getItemId())
                .name(dropItem.getName())
                .chance(dropItem.getChance())
                .build();
    }
}
